package com.product.bda.handler;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.jdbc.core.JdbcTemplate;

import com.product.bda.service.CashGift;
import com.product.bda.service.CashGiftService;
import com.product.bda.service.CreditService;
import com.product.bda.service.Customer;
import com.product.bda.service.CustomerService;
import com.product.bda.service.DepositService;
import com.product.bda.service.JinYangPayService;
import com.product.bda.service.OrderNoService;
import com.product.bda.service.OrderNumberService;
import com.product.bda.service.ScoreService;

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

@Handler(name="JinYangPay")
public class JinYangPayHandler {

	protected static String BASE_URL = "http://pay.095pay.com/zfapi/order/pay";
	
	@Before
	public FobbinInfo before(Task task,InterFace inter){
		String product=task.getParam("product");
		if(!"8da".equals(product)){
			return  new FobbinInfo(true,"No product support!");
		}
		return new FobbinInfo(false,"");
	}
	
	@Service(name="pay")
	@Params(validateField={"login_name"})
    public Result pay(Task task,InterFace inter){
		
		String product = task.getParam("product");
		
		String pre=ThPayResource.instance().getConfig("jinyangpay."+product+".pre");
		String merchno =ThPayResource.instance().getConfig("jinyangpay."+product+".merNo"); 
		String mer_key =ThPayResource.instance().getConfig("jinyangpay."+product+".mer_key");
		String callbackurl = ThPayResource.instance().getConfig("jinyangpay."+product+".notifyUrl"); //服务器通知返回接口
		
		String payType = task.getParam("payType");  //支付方式
		String remark = task.getParam("login_name");
		String customer_ip = task.getParam("customer_ip");
		String return_url = task.getParam("return_url");  //客户访问用的网址
		String order_no = pre+"" + String.valueOf(System.currentTimeMillis());
		
		String amount = task.getParam("amount");
		
		try {
			DefaultHttpClient httpClient = new SSLClient();
			
			HttpPost postMethod = new HttpPost(BASE_URL);
			List<BasicNameValuePair> nvps = new ArrayList<BasicNameValuePair>();
			
			nvps.add(new BasicNameValuePair("p1_mchtid", merchno));
			nvps.add(new BasicNameValuePair("p2_paytype", payType));
			nvps.add(new BasicNameValuePair("p3_paymoney", amount));
			nvps.add(new BasicNameValuePair("p4_orderno", order_no));
			nvps.add(new BasicNameValuePair("p5_callbackurl", callbackurl));
			nvps.add(new BasicNameValuePair("p6_notifyurl", ""));
			nvps.add(new BasicNameValuePair("p7_version", "v2.8"));
			
			nvps.add(new BasicNameValuePair("p8_signtype", "1"));
			nvps.add(new BasicNameValuePair("p9_attach", remark));
			nvps.add(new BasicNameValuePair("p10_appname", ""));
			nvps.add(new BasicNameValuePair("p11_isshow", "0"));
			nvps.add(new BasicNameValuePair("p12_orderip", customer_ip));
			
			String signvalue = "p1_mchtid="+merchno+"&p2_paytype="+payType+"&p3_paymoney="+amount+"&p4_orderno="+order_no+"&p5_callbackurl="+callbackurl+"&p6_notifyurl="
			+"&p7_version=v2.8"+"&p8_signtype=1"+"&p9_attach="+remark+"&p10_appname="+"&p11_isshow=0"+"&p12_orderip="+customer_ip+mer_key;
			String sign = MD5Util.MD5(signvalue).toLowerCase();
			
			nvps.add(new BasicNameValuePair("sign", sign));
			//System.out.println("signvalue   "+signvalue);
			
			//System.out.println("nvps   "+nvps.toString());
			postMethod.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
            HttpResponse resp = httpClient.execute(postMethod);
            String str = EntityUtils.toString(resp.getEntity(), "UTF-8");
            int statusCode = resp.getStatusLine().getStatusCode();
            if (200 == statusCode) {
            	JSONObject jsresult = JSONObject.fromObject(str);
            	System.out.println(jsresult);
            	if(jsresult != null && jsresult.getString("rspCode").equals("1")){
            		JSONObject jsondata = jsresult.getJSONObject("data");
            		String barCode = jsondata.getString("r6_qrcode");
            		String ds=inter.getDataSource();
    				JdbcTemplate template=JdbcResource.instance().getJdbcTemplate(ds);
    				
    				JinYangPayService service = new JinYangPayService(template,ds);
    				
    				if(payType.equals("ALIPAY")){
    					service.createXftp(order_no, remark, amount, customer_ip, "支付宝", "", return_url);
    				}else if(payType.equals("WEIXIN")){
    					service.createXftp(order_no, remark, amount, customer_ip, "微信", "", return_url);
    				}else if(payType.equals("JDPAY")){
    					service.createXftp(order_no, remark, amount, customer_ip, "京东钱包", "", return_url);
    				}else if(payType.equals("QQPAY")){
    					service.createXftp(order_no, remark, amount, customer_ip, "QQ钱包", "", return_url);
    				}else if(payType.equals("UNIONPAY")){
    					service.createXftp(order_no, remark, amount, customer_ip, "银联钱包", "", return_url);
    				}else{
    					service.createXftp(order_no, remark, amount, customer_ip, payType, "", return_url);
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
	
	
	@Service(name="payForWap")
	@Params(validateField={"login_name"})
    public Result payForWap(Task task,InterFace inter){
		
		String product = task.getParam("product");
		
		String pre=ThPayResource.instance().getConfig("jinyangpay."+product+".pre");
		String merchno =ThPayResource.instance().getConfig("jinyangpay."+product+".merNo"); 
		String mer_key =ThPayResource.instance().getConfig("jinyangpay."+product+".mer_key");
		String callbackurl = ThPayResource.instance().getConfig("jinyangpay."+product+".notifyUrl"); //服务器通知返回接口
		
		String payType = task.getParam("payType");  //支付方式
		String remark = task.getParam("login_name");
		String customer_ip = task.getParam("customer_ip");
		String return_url = task.getParam("return_url");  //客户访问用的网址
		String order_no = pre+"" + String.valueOf(System.currentTimeMillis());
		
		String amount = task.getParam("amount");
		
		try {
			String p1_mchtid = merchno;
			String p2_paytype = payType;
			String p3_paymoney = amount;
			String p4_orderno = order_no;
			String p5_callbackurl = callbackurl;
			String p6_notifyurl ="";
			String p7_version = "v2.8";
			String p8_signtype = "1";
			String p9_attach = remark;
			String p10_appname = "";
			String p11_isshow = "0";
			String p12_orderip = customer_ip;
			
			String signvalue = "p1_mchtid="+p1_mchtid+"&p2_paytype="+p2_paytype+"&p3_paymoney="+p3_paymoney+"&p4_orderno="+p4_orderno+"&p5_callbackurl="+callbackurl+"&p6_notifyurl="+p6_notifyurl
					+"&p7_version="+p7_version+"&p8_signtype="+p8_signtype+"&p9_attach="+p9_attach+"&p10_appname="+p10_appname+"&p11_isshow="+p11_isshow+"&p12_orderip="+customer_ip+mer_key;
			String sign = MD5Util.MD5(signvalue).toLowerCase();

			String ds=inter.getDataSource();
			JdbcTemplate template=JdbcResource.instance().getJdbcTemplate(ds);
			
			JinYangPayService service = new JinYangPayService(template,ds);
			
			if(payType.equals("QQPAYWAP")){
				service.createXftp(order_no, remark, amount, customer_ip, "QQWAP", "", return_url);
			}else if(payType.equals("WEIXINWAP")){
				service.createXftp(order_no, remark, amount, customer_ip, "WEIXINWAP", "", return_url);
			}
			
			Result r =Result.create(task.getId(), task.getFunId());
			r.addFields(new String[]{"ok","suburl","p1_mchtid","p2_paytype","p3_paymoney","p4_orderno","p5_callbackurl","p6_notifyurl","p7_version"
					,"p8_signtype","p9_attach","p10_appname","p11_isshow","p12_orderip","sign"});
			r.setFlag("-1");
			r.setIsList(true);
			r.setLength(1);
			r.addRecord(new String[]{"1",BASE_URL,p1_mchtid,p2_paytype,p3_paymoney,p4_orderno,p5_callbackurl,p6_notifyurl,p7_version,
					p8_signtype,p9_attach,p10_appname,p11_isshow,p12_orderip,sign});
			return r;
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		Result r =Result.create(task.getId(), task.getFunId());
		r.addFields(new String[]{"ok"});
		r.setFlag("-1");
		r.setIsList(true);
		r.setLength(1);
		r.addRecord(new String[]{"0"});
		return r;
		
	}
	
	
	@Service(name="notify")
	@Params(validateField={"paymoney"})
	public Result notify(Task task,InterFace inter){
		
		String product = task.getParam("product");
		
		
		String partner = task.getParam("partner");
	    String ordernumber = task.getParam("ordernumber");
	    String orderstatus = task.getParam("orderstatus");
	    String sysnumber = task.getParam("sysnumber");
	    String attach = task.getParam("attach");
	    
	    String sign = task.getParam("sign");
	    
	    BigDecimal paymoney = new BigDecimal(task.getParam("paymoney"));
	    
	    if ("1".equalsIgnoreCase(orderstatus) ){
	    	String mer_key =ThPayResource.instance().getConfig("jinyangpay."+product+".mer_key");
	    	String orsignature = MD5Util.md5Encode("partner="+partner+"&ordernumber="+ordernumber+"&orderstatus="+orderstatus+"&paymoney="+paymoney+mer_key);
	    	if(sign.equalsIgnoreCase(orsignature)){
	    		String ds=ThPayResource.instance().getConfig("changfupay."+product+".datasource");
	    		JdbcTemplate template=JdbcResource.instance().getJdbcTemplate(ds);
	    		OrderNumberService orderNumberService =new OrderNumberService(template,ds);
		    	CreditService creditService =new CreditService(template,ds);
				CustomerService custService =new CustomerService(template,ds);
				DepositService depositService =new DepositService(template,ds);
				JinYangPayService service = new JinYangPayService(template,ds);
				
				if(service.isNotDoYdp(ordernumber)){
					service.update(ordernumber, sysnumber,"8da", "2", paymoney, new BigDecimal(0.99));
					orderNumberService.createOrderNumber(ordernumber);
					
					String login_name = attach;
					String payType = service.queryPayType(ordernumber);
					if(creditService.add(login_name, paymoney,"自动充值", "jy在线支付:"+payType, login_name, ordernumber)){
						try{
							Customer customer= custService.getCustomer(login_name);
							//添加存款记录和日志
							String dep_no = OrderNoService.createLocalNo("DE");
							depositService.addDeposit2(dep_no, customer.cust_id, login_name, customer.real_name, paymoney, "jy在线支付"+payType, "", "jy在线支付"+payType, "", ordernumber);
							
							//查询是否第一次存款,如果是,升级用户等级
//							if(depositService.NTgetCount(customer.cust_id) == 1){
//								custService.NTmodCustlevelFirst(customer.cust_id, 1);
//							}
							
							if(depositService.NTgetCount(customer.cust_id) == 1){
								if(paymoney.intValue() >=100){
									custService.NTmodCustlevelFirst(customer.cust_id, 1);
								}else{
									custService.NTmodFirstDepositDateOnly(customer.cust_id, 1);
								}
								
							}else{ 
								if(customer.cust_level == 0){
									if(paymoney.intValue() >=100){
										custService.NTmodCustlevelOnly(customer.cust_id, 1);
									}
								}
							}
							
							if(customer.promo_flag != null && customer.promo_flag){
								ScoreService scoreService =new ScoreService(template,ds);
								if(scoreService.depositCountToday(login_name)==1 && paymoney.intValue() >=100 ){
									scoreService.modScore(ordernumber, "签到积分",  new BigDecimal(1), login_name, customer.cust_id, "");
								}
								
								if(paymoney.intValue() >=100){
									scoreService.modScore(ordernumber, "存款积分",new BigDecimal( paymoney.divide(new BigDecimal(100)).intValue()), login_name, customer.cust_id, "");
								}
								
								Date now =new Date(System.currentTimeMillis());
								if(now.getTime()>DateUtil.stringToDate("2017-12-01", "yyyy-MM-dd").getTime() &&
							    		  DateUtil.stringToDate("2018-01-01", "yyyy-MM-dd").getTime()>now.getTime() && paymoney.intValue()>=500 ){
									
									String giftCode=RandomUtil.getRandom(8);
									 CashGift gift =new CashGift();
						        	 gift.gift_code=giftCode;
						        	 gift.login_name=customer.login_name;
						        	 gift.deposit_credit=paymoney;
						        	 gift.valid_credit=new BigDecimal(0);
						        	 gift.net_credit=new BigDecimal(0);
						        	 
						        	 float rate = 0;
						        	 float f = 0.0f;
						        	 if(paymoney.intValue() >= 500 && paymoney.intValue()<5000){
						        		 gift.rate=Float.valueOf(1);
							        	 rate = 1;
							        	 f=paymoney.floatValue()*1/100;
									 }else if(paymoney.intValue() >= 5000 && paymoney.intValue()<10000){
										 gift.rate=Float.valueOf(1.8f);
							        	 rate = 1.8f;
							        	 f=paymoney.floatValue()*1.8f/100;
									 }else if(paymoney.intValue() >= 10000 && paymoney.intValue()<30000){
										 gift.rate=Float.valueOf(2.5f);
							        	 rate = 2.5f;
							        	 f=paymoney.floatValue()*2.5f/100;
									 }else if(paymoney.intValue() >= 30000 && paymoney.intValue()<50000){
										 gift.rate=Float.valueOf(3.8f);
							        	 rate = 3.8f;
							        	 f=paymoney.floatValue()*3.8f/100;
									 }else if(paymoney.intValue() >= 50000 ){
										 gift.rate=Float.valueOf(5);
							        	 rate = 5;
							        	 f=paymoney.floatValue()*5/100;
							        	 if(f>2888){
							        		 f=2888;
							        	 }
									 }

						        	 
						        	 gift.payout=new BigDecimal(f);
						        	 gift.gift_type="年终现金大回馈 ";
						        	 gift.status=1;
						        	 gift.gift_no=OrderNoService.createLocalNo("GI");
						     		 gift.cust_id=customer.cust_id;
						        	 gift.cs_date=new Date(System.currentTimeMillis());
						        	 gift.real_name=customer.real_name;
						        	 gift.cust_level=customer.cust_level;
						           	 gift.kh_date=customer.create_date;
						        	 gift.create_user="系统";
						        	 gift.create_date=new Date(System.currentTimeMillis());
						        	 
						        	 CashGiftService giftService =new CashGiftService(template,ds);
						        	 Long giftId = giftService.NTcreat(gift);
						        	 if(giftId != null){
						        		 giftService.NTAuditGift(giftId, 3, "系统", "系统审核通过-"+dep_no+" |"+rate);
						        		 
						        		 creditService.add(login_name, gift.payout,gift.gift_type, "添加礼金"+gift.gift_type+" |"+rate+"%", "系统", gift.gift_no);
						        	 }
						        	 
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
