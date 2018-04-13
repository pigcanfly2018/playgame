package com.product.bda.handler;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONObject;
import sun.util.locale.provider.DecimalFormatSymbolsProviderImpl;

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
import com.product.bda.service.HuiTianPayService;
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

@Handler(name="HuiTianPay")
public class HuiTianPayHandler {

	protected static String BASE_URL = "https://gateway.999pays.com/Pay/KDBank.aspx";
	
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
		
		String pre=ThPayResource.instance().getConfig("huitianpay."+product+".pre");
		String merchno =ThPayResource.instance().getConfig("huitianpay."+product+".merNo"); 
		String mer_key =ThPayResource.instance().getConfig("huitianpay."+product+".key");
		String callbackurl = ThPayResource.instance().getConfig("huitianpay."+product+".notifyUrl"); //服务器通知返回接口
		
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
		
		String pre=ThPayResource.instance().getConfig("huitianpay."+product+".pre");
		String merchno =ThPayResource.instance().getConfig("huitianpay."+product+".merNo"); 
		String mer_key =ThPayResource.instance().getConfig("huitianpay."+product+".mer_key");
		
		String payType = task.getParam("payType");  //支付方式
		String remark = task.getParam("login_name");
		String customer_ip = task.getParam("customer_ip");
		String return_url = task.getParam("return_url");  //客户访问用的网址
		String order_no = pre+ String.valueOf(System.currentTimeMillis());
		
		String amount = task.getParam("amount");
		
		try {
			String P_UserID = merchno;
			String P_OrderID = order_no;
			String P_CardID = "";
			String P_CardPass = "";
			String P_FaceValue = amount;
			String P_ChannelID =payType;
			String P_Subject = "";
			String P_Price = "1";
			String P_Quantity = "";
			String P_Description = "";
			String P_Notic = remark;
			String P_ISsmart = "0";
			String P_Result_URL = return_url;
			String P_Notify_URL = "";
			
			StringBuffer P_PostKey = new StringBuffer();
			P_PostKey.append(P_UserID).append("|").append(P_OrderID).append("|").append(P_CardID).append("|").append(P_CardPass).append("|")
			.append(P_FaceValue).append("|").append(P_ChannelID).append("|").append(mer_key);
			
			String sign = MD5Util.MD5(P_PostKey.toString()).toLowerCase();

			String ds=inter.getDataSource();
			JdbcTemplate template=JdbcResource.instance().getJdbcTemplate(ds);
			
			HuiTianPayService service = new HuiTianPayService(template,ds);
			
			if(payType.equals("92")){ //qqwap
				service.createHtp(order_no, remark, amount, customer_ip, "QQWAP", "", return_url);
			}else if(payType.equals("33")){//weixinwap
				service.createHtp(order_no, remark, amount, customer_ip, "WEIXINWAP", "", return_url);
			}
			
			Result r =Result.create(task.getId(), task.getFunId());
			r.addFields(new String[]{"ok","BASE_URL","P_UserID","P_OrderID","P_CardID","P_CardPass","P_FaceValue","P_ChannelID",
					"P_Subject","P_Price","P_Quantity","P_Description","P_Notic","P_ISsmart","P_Result_URL",
					"P_Notify_URL","P_PostKey"});
			r.setFlag("-1");
			r.setIsList(true);
			r.setLength(1);
			r.addRecord(new String[]{"1",BASE_URL,P_UserID,P_OrderID,P_CardID,P_CardPass,P_FaceValue,P_ChannelID,P_Subject,
					P_Price,P_Quantity,P_Description,P_Notic,P_ISsmart,P_Result_URL,P_Notify_URL,sign});
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
		String login_name = task.getParam("p_Notic");
		
		String P_ErrCode = task.getParam("p_ErrCode");
		String P_UserID = task.getParam("p_UserID");
		String P_OrderID = task.getParam("p_OrderID");
		String P_CardID = task.getParam("p_CardID");
		String P_CardPass = task.getParam("p_CardPass");
		BigDecimal P_FaceValue = new BigDecimal(task.getParam("p_FaceValue"));
		String P_ChannelID = task.getParam("p_ChannelID");
		String P_PayMoney = task.getParam("p_PayMoney");
		String P_Subject = task.getParam("p_Subject");
		String P_Price = task.getParam("p_Price");
		String P_Quantity = task.getParam("p_Quantity");
		String P_Description = task.getParam("p_Description");
		String P_Notic = task.getParam("p_Notic");
		String P_ErrMsg = task.getParam("p_ErrMsg");
		String P_PostKey = task.getParam("p_PostKey");
		
		
	    if ("0".equals(P_ErrCode) ){
	    	String mer_key =ThPayResource.instance().getConfig("huitianpay."+product+".mer_key");
	    	StringBuffer sign = new StringBuffer();
	    	sign.append(P_UserID).append("|").append(P_OrderID).append("|").append(P_CardID).append("|").append(P_CardPass).append("|")
			.append(P_FaceValue).append("|").append(P_ChannelID).append("|").append(mer_key);
			
			String paramsign = MD5Util.MD5(sign.toString()).toLowerCase();
			System.out.println("============准备验签");
	    	if(paramsign.equals(P_PostKey)){
	    		System.out.println("=============通过验签");
	    		String ds=ThPayResource.instance().getConfig("huitianpay."+product+".datasource");
	    		JdbcTemplate template=JdbcResource.instance().getJdbcTemplate(ds);
	    		OrderNumberService orderNumberService =new OrderNumberService(template,ds);
		    	CreditService creditService =new CreditService(template,ds);
				CustomerService custService =new CustomerService(template,ds);
				DepositService depositService =new DepositService(template,ds);
				HuiTianPayService service = new HuiTianPayService(template,ds);
				
				if(service.isNotDoYdp(P_OrderID)){
					service.update(P_OrderID, "","8da", "2", P_FaceValue, new BigDecimal(0.98));
					orderNumberService.createOrderNumber(P_OrderID);
					
					
					String payType = service.queryPayType(P_OrderID);
					if(creditService.add(P_Notic, P_FaceValue,"自动充值", "ht在线支付:"+payType, login_name, P_OrderID)){
						try{
							Customer customer= custService.getCustomer(login_name);
							//添加存款记录和日志
							String dep_no = OrderNoService.createLocalNo("DE");
							depositService.addDeposit2(dep_no, customer.cust_id, login_name, customer.real_name, P_FaceValue, "ht在线支付"+payType, "", "ht在线支付"+payType, "", P_OrderID);
							
							//查询是否第一次存款,如果是,升级用户等级
//							if(depositService.NTgetCount(customer.cust_id) == 1){
//								custService.NTmodCustlevelFirst(customer.cust_id, 1);
//							}
							
							if(depositService.NTgetCount(customer.cust_id) == 1){
								if(P_FaceValue.intValue() >=100){
									custService.NTmodCustlevelFirst(customer.cust_id, 1);
								}else{
									custService.NTmodFirstDepositDateOnly(customer.cust_id, 1);
								}
								
							}else{ 
								if(customer.cust_level == 0){
									if(P_FaceValue.intValue() >=100){
										custService.NTmodCustlevelOnly(customer.cust_id, 1);
									}
								}
							}
							
							if(customer.promo_flag != null && customer.promo_flag){
								ScoreService scoreService =new ScoreService(template,ds);
								if(scoreService.depositCountToday(login_name)==1 && P_FaceValue.intValue() >=100 ){
									scoreService.modScore(P_OrderID, "签到积分",  new BigDecimal(1), login_name, customer.cust_id, "");
								}
								
								if(P_FaceValue.intValue() >=100){
									scoreService.modScore(P_OrderID, "存款积分",new BigDecimal( P_FaceValue.divide(new BigDecimal(100)).intValue()), login_name, customer.cust_id, "");
								}
								
								Date now =new Date(System.currentTimeMillis());
								if(now.getTime()>DateUtil.stringToDate("2017-12-01", "yyyy-MM-dd").getTime() &&
							    		  DateUtil.stringToDate("2018-01-01", "yyyy-MM-dd").getTime()>now.getTime() && P_FaceValue.intValue()>=500 ){
									
									String giftCode=RandomUtil.getRandom(8);
									 CashGift gift =new CashGift();
						        	 gift.gift_code=giftCode;
						        	 gift.login_name=customer.login_name;
						        	 gift.deposit_credit=P_FaceValue;
						        	 gift.valid_credit=new BigDecimal(0);
						        	 gift.net_credit=new BigDecimal(0);
						        	 
						        	 float rate = 0;
						        	 float f = 0.0f;
						        	 if(P_FaceValue.intValue() >= 500 && P_FaceValue.intValue()<5000){
						        		 gift.rate=Float.valueOf(1);
							        	 rate = 1;
							        	 f=P_FaceValue.floatValue()*1/100;
									 }else if(P_FaceValue.intValue() >= 5000 && P_FaceValue.intValue()<10000){
										 gift.rate=Float.valueOf(1.8f);
							        	 rate = 1.8f;
							        	 f=P_FaceValue.floatValue()*1.8f/100;
									 }else if(P_FaceValue.intValue() >= 10000 && P_FaceValue.intValue()<30000){
										 gift.rate=Float.valueOf(2.5f);
							        	 rate = 2.5f;
							        	 f=P_FaceValue.floatValue()*2.5f/100;
									 }else if(P_FaceValue.intValue() >= 30000 && P_FaceValue.intValue()<50000){
										 gift.rate=Float.valueOf(3.8f);
							        	 rate = 3.8f;
							        	 f=P_FaceValue.floatValue()*3.8f/100;
									 }else if(P_FaceValue.intValue() >= 50000 ){
										 gift.rate=Float.valueOf(5);
							        	 rate = 5;
							        	 f=P_FaceValue.floatValue()*5/100;
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
