package com.product.bda.handler;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONObject;

import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

import com.product.bda.service.CashGift;
import com.product.bda.service.CashGiftService;
import com.product.bda.service.CreditService;
import com.product.bda.service.Customer;
import com.product.bda.service.CustomerService;
import com.product.bda.service.DepositService;
import com.product.bda.service.OrderNoService;
import com.product.bda.service.OrderNumberService;
import com.product.bda.service.SaoMaPayService;
import com.product.bda.service.ScoreService;
import com.product.bda.service.YingTongBaoPayService;

import bsz.exch.bank.ThPayResource;
import bsz.exch.bean.FobbinInfo;
import bsz.exch.bean.Result;
import bsz.exch.bean.Task;
import bsz.exch.core.Before;
import bsz.exch.core.Handler;
import bsz.exch.core.InterFace;
import bsz.exch.core.JdbcResource;
import bsz.exch.core.Params;
import bsz.exch.core.Service;
import bsz.exch.utils.DateUtil;
import bsz.exch.utils.MD5Util;
import bsz.exch.utils.RandomUtil;
import bsz.exch.utils.SSLClient;

@Handler(name="SMP")
public class SaoMaPayHandler {

	protected static String BASE_URL = "http://payjust.cn/orgReq/qrPay";
	
	private static Logger logger=Logger.getLogger(SaoMaPayHandler.class);
	
	@Before
	public FobbinInfo before(Task task,InterFace inter){
		String product=task.getParam("product");
		if(!"8da".equals(product)){
			return  new FobbinInfo(true,"No product support!");
		}
		return new FobbinInfo(false,"");
	}
	
	
	@Service(name="pay")
	@Params(validateField={"login_name","amount","payType"})
	public Result pay(Task task,InterFace inter){
		
		String product = task.getParam("product");
		
		String pre=ThPayResource.instance().getConfig("saomapay."+product+".pre");
		String merchno =ThPayResource.instance().getConfig("saomapay."+product+".merNo"); 
		String mer_key =ThPayResource.instance().getConfig("saomapay."+product+".mer_key");
		String notify_url = ThPayResource.instance().getConfig("saomapay."+product+".notifyUrl"); //服务器通知返回接口
		
		
		String customer_ip = task.getParam("customer_ip");
		String return_url = task.getParam("return_url");  //客户访问用的网址
		String payType = task.getParam("payType");  //支付方式
		
		
		String amount = task.getParam("amount");
		//String order_amount = (Integer.parseInt(amount)*100)+"";
		String order_amount = new BigDecimal(amount).multiply(new BigDecimal(100)).intValue()+"";
		String remark = task.getParam("login_name");
		
		String order_no = pre+"_" + String.valueOf(System.currentTimeMillis());
		try {
			DefaultHttpClient httpClient = new SSLClient();
		
			HttpPost postMethod = new HttpPost(BASE_URL);
			List<BasicNameValuePair> nvps = new ArrayList<BasicNameValuePair>();
			String requestNo = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
			nvps.add(new BasicNameValuePair("requestNo", requestNo));
			nvps.add(new BasicNameValuePair("version", "V1.0"));
			String productId = payType;
			nvps.add(new BasicNameValuePair("productId", productId));
			String transId = "01";
			nvps.add(new BasicNameValuePair("transId", transId));
			nvps.add(new BasicNameValuePair("merNo", merchno));
			nvps.add(new BasicNameValuePair("orderDate", new SimpleDateFormat("yyyyMMdd").format(new Date())));
			String orderNo = order_no;
			nvps.add(new BasicNameValuePair("orderNo", orderNo));
       
			nvps.add(new BasicNameValuePair("transAmt", order_amount));
        

			String merchantName = "手机配件";
			nvps.add(new BasicNameValuePair("commodityName",merchantName));
			nvps.add(new BasicNameValuePair("notifyUrl", notify_url));
        
			String signature = MD5Util.md5Encode(requestNo+productId+transId+merchno+orderNo+order_amount+mer_key);
			//System.out.println(requestNo+productId+transId+merchno+orderNo+order_amount);
			nvps.add(new BasicNameValuePair("signature", signature));
		
		
			 postMethod.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
	            HttpResponse resp = httpClient.execute(postMethod);
	            String str = EntityUtils.toString(resp.getEntity(), "UTF-8");
	            int statusCode = resp.getStatusLine().getStatusCode();
	           // System.out.println(statusCode);
	            if (200 == statusCode) {
	            	//System.out.println(str);
	            	JSONObject jsresult = JSONObject.fromObject(str);
	            	if(jsresult != null && !jsresult.getString("codeUrl").equals("")){
	            		
	            		String ds=inter.getDataSource();
	    				JdbcTemplate template=JdbcResource.instance().getJdbcTemplate(ds);
	    				SaoMaPayService service = new SaoMaPayService(template,ds);
	    				
	    				String barCode = jsresult.getString("codeUrl");
	    				
	    				if(payType.equals("1")){
	    					service.createYtbp(order_no, remark, amount, customer_ip, "支付宝", "", return_url);
	    				}else if(payType.equals("0101")){
	    					service.createYtbp(order_no, remark, amount, customer_ip, "微信", "", return_url);
	    				}else if(payType.equals("0103")){
	    					service.createYtbp(order_no, remark, amount, customer_ip, "QQ扫码", "", return_url);
	    				}
	    				
	    				
	    				
	    				Result r =Result.create(task.getId(), task.getFunId());
	    				r.addFields(new String[]{"ok","pay_id","message","amount","barCode"});
	    				r.setFlag("-1");
	    				r.setIsList(true);
	    				r.setLength(1);
	    				r.addRecord(new String[]{"1",order_no,"",amount,barCode});
	    				return r;
	    				
	            	}
	            }
		}catch(Exception e){
			e.printStackTrace();
		}
		
		Result r =Result.create(task.getId(), task.getFunId());
		r.addFields(new String[]{"ok","pay_id","message","amount","barCode"});
		r.setFlag("-1");
		r.setIsList(true);
		r.setLength(1);
		r.addRecord(new String[]{"0",order_no,"无法创建订单",amount,""});
		return r;
	}
	
	
	@Service(name="notify")
	@Params(validateField={"transAmt"})
	public Result notify(Task task,InterFace inter){
		String product = task.getParam("product");
		
		
		String merNo = task.getParam("merNo");
	    String orderNo = task.getParam("orderNo");
	    String orderDate = task.getParam("orderDate");
	    String respCode = task.getParam("respCode");
	    String respDesc = task.getParam("respDesc");
	    String payId = task.getParam("payId");
	    
	    String payTime = task.getParam("payTime");
	    String signature = task.getParam("signature");
	    
	    
	    BigDecimal amount = new BigDecimal(task.getParam("transAmt")).divide(new BigDecimal(100));
	    if ("0000".equalsIgnoreCase(respCode) ){
	    	
	    	String mer_key =ThPayResource.instance().getConfig("saomapay."+product+".mer_key");
	    	String orsignature = MD5Util.md5Encode(merNo+orderNo+task.getParam("transAmt")+respCode+payId+payTime+mer_key);
	    	if(signature.equalsIgnoreCase(orsignature)){
	    		String ds=ThPayResource.instance().getConfig("ytbpay."+product+".datasource");
		    	JdbcTemplate template=JdbcResource.instance().getJdbcTemplate(ds);
		    	OrderNumberService orderNumberService =new OrderNumberService(template,ds);
		    	CreditService creditService =new CreditService(template,ds);
				CustomerService custService =new CustomerService(template,ds);
				DepositService depositService =new DepositService(template,ds);
				SaoMaPayService service = new SaoMaPayService(template,ds);
				//没有处理
				if(service.isNotDoYdp(orderNo)){
					String login_name = service.queryLoginname(orderNo);
					
					service.updaete(orderNo, payId,payId, "2", amount, new BigDecimal(0.988));
					
					
					orderNumberService.createOrderNumber(orderNo);
					
					if(creditService.add(login_name, amount,"自动充值", "saoma在线支付:", login_name, orderNo)){
						  try{
								Customer customer= custService.getCustomer(login_name);
								//添加存款记录和日志
								String dep_no = OrderNoService.createLocalNo("DE");
								depositService.addDeposit2(dep_no, customer.cust_id, login_name, customer.real_name, amount, "saoma在线支付", "", "saoma在线支付", "", orderNo);
								//查询是否第一次存款,如果是,升级用户等级
								if(depositService.NTgetCount(customer.cust_id) == 1){
									custService.NTmodCustlevelFirst(customer.cust_id, 1);
								}
								
								if(customer.promo_flag != null && customer.promo_flag){
									ScoreService scoreService =new ScoreService(template,ds);
									if(scoreService.depositCountToday(login_name)==1){
										scoreService.modScore(orderNo, "签到积分",  new BigDecimal(1), login_name, customer.cust_id, "");
									}
									scoreService.modScore(orderNo, "存款积分",new BigDecimal( (amount.add(new BigDecimal(2))).divide(new BigDecimal(100)).intValue()), login_name, customer.cust_id, "");
								
									Date now =new Date(System.currentTimeMillis());
									if(now.getTime()>DateUtil.stringToDate("2017-06-12", "yyyy-MM-dd").getTime() &&
								    		  DateUtil.stringToDate("2017-06-26", "yyyy-MM-dd").getTime()>now.getTime()){
								    	 
								    }
								    
								}
								
								
								
								
								
							    
								Result r =Result.create(task.getId(), task.getFunId());
								r.addFields(new String[]{"ok"});
								r.setFlag("-1");
								r.setIsList(true);
								r.setLength(1);
								r.addRecord(new String[]{"1"});
							return r;
								
								
							}catch(Exception e){
								
								e.printStackTrace();
								
							}
					}
					
					
				}
	    	}
	    	
	    }
		
		 Result r =Result.create(task.getId(), task.getFunId());
		 r.addFields(new String[]{"ok"});
		 r.setFlag("-1");
		 r.setIsList(true);
		 r.setLength(1);
		 r.addRecord(new String[]{"0"});
		 return r;
	}
	
}
