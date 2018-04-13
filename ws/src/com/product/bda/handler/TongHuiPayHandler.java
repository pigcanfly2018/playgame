package com.product.bda.handler;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.jdbc.core.JdbcTemplate;

import com.product.bda.service.CashGift;
import com.product.bda.service.CashGiftService;
import com.product.bda.service.CreditService;
import com.product.bda.service.Customer;
import com.product.bda.service.CustomerService;
import com.product.bda.service.DepositService;
import com.product.bda.service.OrderNoService;
import com.product.bda.service.OrderNumberService;
import com.product.bda.service.PayOnline;
import com.product.bda.service.PayOnlineService;
import com.product.bda.service.ScoreService;
import com.product.bda.service.ThpService;
import com.product.bda.service.YbpService;

import bsz.exch.bank.ThPayResource;
import bsz.exch.bank.YbPayResource;
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

@Handler(name="THP")
public class TongHuiPayHandler {

	@Before
	public FobbinInfo before(Task task,InterFace inter){
		String product=task.getParam("product");
		if(!"8da".equals(product)){
			return  new FobbinInfo(true,"No product support!");
		}
		return new FobbinInfo(false,"");
	}
	
	
	@Service(name="pay")
	@Params(validateField={"login_name","amount","bank","customer_ip","return_url"})
    public Result pay(Task task,InterFace inter){
		String product = task.getParam("product");
		String pre=ThPayResource.instance().getConfig("thpay."+product+".pre");
		String url = task.getParam("return_url");  //客户访问用的网址
		String customer_ip = task.getParam("customer_ip");  //客户访问用的IP
		String bank_code = task.getParam("bank");  //客户访问用的
        
		
		String order_no = pre+"_" + String.valueOf(System.currentTimeMillis());
		String order_amount = task.getParam("amount");
		String input_charset = "utf-8";
		String merchant_code =ThPayResource.instance().getConfig("thpay."+product+".merchant_code"); 
		String mer_key =ThPayResource.instance().getConfig("thpay."+product+".mer_key"); 
		String product_name = "mobile";	//商品名称
		String req_referer = ThPayResource.instance().getConfig("thpay."+product+".req_referer");;
		String return_url = ThPayResource.instance().getConfig("thpay."+product+".return_url"); //服务器跳转返回接口
		String notify_url = ThPayResource.instance().getConfig("thpay."+product+".notify_url"); //服务器通知返回接口
		
		String pay_type = "1";	//支付方式，如果为银行直连，值为1
		String order_time = DateUtil.getOrderTime();
		
		String submit_value = "";

		String return_params = task.getParam("login_name");
		
		String remark = task.getParam("remark");
		JdbcTemplate template=JdbcResource.instance().getJdbcTemplate(inter.getDataSource());
		String ds=inter.getDataSource();
		ThpService thpService = new ThpService(template,ds);
		PayOnlineService poService= new PayOnlineService(template,ds);
		PayOnline online = poService.getPayOnline("tonghui", product);
		if(online != null){
			submit_value = online.submit_value;
			return_url = online.return_value;
			notify_url = online.notify_value;
			req_referer = online.req_referer;
			
		}
		thpService.createThp(order_no, task.getParam("login_name"), order_amount, customer_ip, bank_code, remark,url);
		try{
			
				String prestr ="bank_code="+bank_code+"&customer_ip="+customer_ip+"&input_charset="+input_charset+"&merchant_code="+merchant_code
						+"&notify_url="+notify_url+"&order_amount="+order_amount+"&order_no="+order_no+"&order_time="+order_time
						+"&pay_type="+pay_type+"&product_name="+product_name+"&req_referer="+req_referer+"&return_params="+return_params+"&return_url="+return_url+"&key="+mer_key;
						 
				//System.out.println(prestr);
				String sign_value = MD5Util.md5Encode(prestr);
			    Result r =Result.create(task.getId(), task.getFunId());
				r.addFields(new String[]{"ok","order_no","message","order_amount","sign_value","customer_ip","bank","order_time","return_params","submit_value","return_value","notify_value","req_referer"});
				r.setFlag("-1");
				r.setIsList(true);
				r.setLength(1);
				r.addRecord(new String[]{"1",order_no,"",order_amount,sign_value,customer_ip,bank_code,order_time,return_params,submit_value,return_url,notify_url,req_referer});
			return r;
		 }catch(Exception e){
			e.printStackTrace();
		 }
		
		 Result r =Result.create(task.getId(), task.getFunId());
		 r.addFields(new String[]{"ok","order_no","message","order_amount","sign_value","customer_ip","sign_value","bank","order_time","return_params","submit_value","return_value","notify_value","req_referer"});
		 r.setFlag("-1");
		 r.setIsList(true);
		 r.setLength(1);
		 r.addRecord(new String[]{"0",order_no,"无法创建订单!",order_amount,"",customer_ip,bank_code,order_time,return_params,submit_value,return_url,notify_url,req_referer});
		 return r;
   }
	
	
	@Service(name="weixinpay")
	@Params(validateField={"login_name","amount","customer_ip","return_url"})
    public Result weixinpay(Task task,InterFace inter){
		String product = task.getParam("product");
		String pre=ThPayResource.instance().getConfig("thpay."+product+".pre");
		String url = task.getParam("return_url");  //客户访问用的网址
		String customer_ip = task.getParam("customer_ip");  //客户访问用的IP
		String bank_code = "WEIXIN";  //客户访问用的
        
		
		String order_no = pre+"_" + String.valueOf(System.currentTimeMillis());
		String order_amount = task.getParam("amount");
		String input_charset = "utf-8";
		String merchant_code =ThPayResource.instance().getConfig("thwxpay."+product+".merchant_code"); 
		String mer_key =ThPayResource.instance().getConfig("thwxpay."+product+".mer_key"); 
		String product_name = "mobile";	//商品名称
		
		String notify_url = ThPayResource.instance().getConfig("thwxpay."+product+".notify_url"); //服务器通知返回接口
		String req_referer = "";;
		String return_url = ""; //服务器跳转返回接口
		String pay_type = "1";	//支付方式，如果为银行直连，值为1
		String order_time = DateUtil.getOrderTime();
		
		String submit_value = "";

		String return_params = task.getParam("login_name");
		
		String remark = task.getParam("remark");
		JdbcTemplate template=JdbcResource.instance().getJdbcTemplate(inter.getDataSource());
		String ds=inter.getDataSource();
		ThpService thpService = new ThpService(template,ds);
		
		thpService.createThp(order_no, task.getParam("login_name"), order_amount, customer_ip, bank_code, remark,url);
		try{
			
			String prestr ="bank_code="+bank_code+"&customer_ip="+"122.3.235.166"+"&input_charset="+input_charset+"&merchant_code="+merchant_code
					+"&notify_url="+notify_url+"&order_amount="+order_amount+"&order_no="+order_no+"&order_time="+order_time
					+"&pay_type="+pay_type+"&product_name="+product_name+"&req_referer="+"www.baidu.com"+"&return_params="+return_params+"&return_url="+"http://www.baidu.com"+"&key="+mer_key;
				String sign_value = MD5Util.md5Encode(prestr);
				CloseableHttpClient  hc= HttpClients.createDefault();
				CloseableHttpResponse response = null;
				String posturl="http://api.hebaobill.com/gateway";
				HttpPost httppost = new HttpPost(posturl);
				httppost.setConfig(RequestConfig.custom().setSocketTimeout(35000).setConnectTimeout(35000).build());
				List<NameValuePair> nvps = new ArrayList<NameValuePair>();
				nvps.add(new BasicNameValuePair("input_charset", "utf-8"));
				nvps.add(new BasicNameValuePair("notify_url", notify_url));
				nvps.add(new BasicNameValuePair("pay_type", "1"));
				nvps.add(new BasicNameValuePair("bank_code", bank_code));
				nvps.add(new BasicNameValuePair("merchant_code", merchant_code));
				nvps.add(new BasicNameValuePair("order_no", order_no));
				nvps.add(new BasicNameValuePair("order_amount", order_amount));
				nvps.add(new BasicNameValuePair("product_name", product_name));
				nvps.add(new BasicNameValuePair("order_time", order_time));
				nvps.add(new BasicNameValuePair("req_referer", "www.baidu.com"));
				nvps.add(new BasicNameValuePair("return_url", "http://www.baidu.com"));
				nvps.add(new BasicNameValuePair("customer_ip", "122.3.235.166"));
				nvps.add(new BasicNameValuePair("return_params", return_params));
				nvps.add(new BasicNameValuePair("sign", sign_value));
				//System.out.println(prestr);
				httppost.setEntity(new UrlEncodedFormEntity(nvps));
				response = hc.execute(httppost);
				if (response.getStatusLine().getStatusCode() == 200) {
					String result=EntityUtils.toString(response.getEntity());
					
					//System.out.println("result      "+result);
					
					String barCode = result.replaceAll("<url>", "").replaceAll("</url>", "");
					
					Result r =Result.create(task.getId(), task.getFunId());
					r.addFields(new String[]{"ok","pay_id","message","amount","barCode"});
					r.setFlag("-1");
					r.setIsList(true);
					r.setLength(1);
					r.addRecord(new String[]{"1",order_no,"",order_amount,barCode});
					return r;
				}
				
			
		 }catch(Exception e){
			e.printStackTrace();
		 }
		
		 Result r =Result.create(task.getId(), task.getFunId());
		 r.addFields(new String[]{"ok","pay_id","message","show_amount","barCode"});
			r.setFlag("-1");
			r.setIsList(true);
			r.setLength(1);
			r.addRecord(new String[]{"0",order_no,"无法创建订单",order_amount,""});
		 return r;
   }
	
	@Service(name="notify")
	@Params(validateField={"trade_no","order_amount","order_no","return_params","notify_type","sign","order_time","trade_time","trade_status"})
	public Result notify(Task task,InterFace inter){
		String product = task.getParam("product");
		String trade_no = task.getParam("trade_no");
	    String order_no = task.getParam("order_no");
	    String return_params = task.getParam("return_params");
	    String notify_type = task.getParam("notify_type");
	    String sign = task.getParam("sign");
	    String order_time = task.getParam("order_time");
	    String trade_status = task.getParam("trade_status");
	    String trade_time = task.getParam("trade_time");
	 
	    
	    BigDecimal order_amount = new BigDecimal(task.getParam("order_amount"));
	    if(trade_status.equalsIgnoreCase("success")){
	    	try{
	    		
	    	String ds=ThPayResource.instance().getConfig("thpay."+product+".datasource");
	    	String merchant_code =ThPayResource.instance().getConfig("thpay."+product+".merchant_code"); 
			String key =ThPayResource.instance().getConfig("thpay."+product+".mer_key"); 
			String prestr ="merchant_code="+merchant_code
					+"&notify_type="+notify_type+"&order_amount="+order_amount+"&order_no="+order_no+"&order_time="+order_time
					+"&return_params="+return_params+"&trade_no="+trade_no+"&trade_status="+trade_status+"&trade_time="+trade_time+"&key="+key;;
	    
			String aftersign_value =  MD5Util.md5Encode(prestr);
			
			JdbcTemplate template=JdbcResource.instance().getJdbcTemplate(ds);
			ThpService thpService =new ThpService(template,ds);
			CreditService creditService =new CreditService(template,ds);
			CustomerService custService =new CustomerService(template,ds);
			DepositService depositService =new DepositService(template,ds);
			OrderNumberService orderNumberService =new OrderNumberService(template,ds);
			if(aftersign_value.equals(sign)){
				
				//没有处理
				if(thpService.isNotDoThp(order_no,return_params)){
					thpService.updaeteThp(order_no, trade_no, "2", order_amount,return_params);
					
					//
					orderNumberService.createOrderNumber(trade_no);
					
					
					String login_name=thpService.queryLoginName(order_no);
			
					//支付成功
					
						if(creditService.add(login_name, order_amount,"自动充值", "th支付:"+trade_time, login_name, order_no)){
							 // try{
									Customer customer= custService.getCustomer(login_name);
									//添加存款记录和日志
									String dep_no = OrderNoService.createLocalNo("DE");
									depositService.addDeposit2(dep_no, customer.cust_id, login_name, customer.real_name, order_amount, "通汇支付", "", "通汇支付", "", order_no);
									//查询是否第一次存款,如果是,升级用户等级
									if(depositService.NTgetCount(customer.cust_id) == 1){
										custService.NTmodCustlevelFirst(customer.cust_id, 1);
									}
									
									if(customer.promo_flag != null && customer.promo_flag){
										ScoreService scoreService =new ScoreService(template,ds);
										if(scoreService.depositCountToday(login_name)==1){
											scoreService.modScore(order_no, "签到积分",  new BigDecimal(1), login_name, customer.cust_id, "");
										}
										scoreService.modScore(order_no, "存款积分",new BigDecimal( order_amount.divide(new BigDecimal(100)).intValue()), login_name, customer.cust_id, "");
										
										Date now =new Date(System.currentTimeMillis());
										if((now.getTime()>DateUtil.stringToDate("2016-12-21", "yyyy-MM-dd").getTime()&&
									    		  DateUtil.stringToDate("2017-01-02", "yyyy-MM-dd").getTime()>now.getTime())){
									    	  //int dcount=depositService.NTgetCountToday(customer.cust_id);
									    	 // if(dcount<=5){
									    		  String giftCode=RandomUtil.getRandom(8);
													 CashGift gift =new CashGift();
										        	 gift.gift_code=giftCode;
										        	 gift.login_name=customer.login_name;
										        	 gift.deposit_credit=order_amount;
										        	 gift.valid_credit=new BigDecimal(0);
										        	 gift.net_credit=new BigDecimal(0);
										        	 gift.rate=Float.valueOf(2);
										        	 Integer rate = 2;
										        	 float f=order_amount.floatValue()*2/100;
//										        	 if(dcount==5){
//										        		 gift.rate=Float.valueOf(3);
//										        		 f=order_amount.floatValue()*3/100;
//										        		 rate = 3;
//										        	 }
//										        	 
//										        	 if(f>1111){
//										        		 f=1111;
//										        	 }
										        	 gift.payout=new BigDecimal(f);
										        	 gift.gift_type="圣诞元旦双节礼";
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
										        	 
									    	 // }
									    }
									}
									
									
								    
									Result r =Result.create(task.getId(), task.getFunId());
									r.addFields(new String[]{"ok"});
									r.setFlag("-1");
									r.setIsList(true);
									r.setLength(1);
									r.addRecord(new String[]{"1"});
								return r;
									
									
								//}catch(Exception e){
									
									//e.printStackTrace();
									
								//}
						}
					
					}
				}
	    	}catch(Exception e){
	    		e.printStackTrace();
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
	
	
	@Service(name="weixinnotify")
	@Params(validateField={"trade_no","order_amount","order_no","sign","order_time","trade_time","trade_status"})
	public Result weixinnotify(Task task,InterFace inter){
		String product = task.getParam("product");
		String trade_no = task.getParam("trade_no");
	    String order_no = task.getParam("order_no");
	    String return_params = task.getParam("return_params");
	    String notify_type = task.getParam("notify_type");
	    String sign = task.getParam("sign");
	    String order_time = task.getParam("order_time");
	    String trade_status = task.getParam("trade_status");
	    String trade_time = task.getParam("trade_time");
	 
	    
	    BigDecimal order_amount = new BigDecimal(task.getParam("order_amount"));
	    if(trade_status.equalsIgnoreCase("success")){
	    	try{
	    		
	    	String ds=ThPayResource.instance().getConfig("thpay."+product+".datasource");
	    	String merchant_code =ThPayResource.instance().getConfig("thwxpay."+product+".merchant_code"); 
			String key =ThPayResource.instance().getConfig("thwxpay."+product+".mer_key"); 
			String prestr ="merchant_code="+merchant_code
					+"&notify_type="+notify_type+"&order_amount="+order_amount+"&order_no="+order_no+"&order_time="+order_time
					+"&return_params="+return_params+"&trade_no="+trade_no+"&trade_status="+trade_status+"&trade_time="+trade_time+"&key="+key;;
	    
			String aftersign_value =  MD5Util.md5Encode(prestr);
			
			JdbcTemplate template=JdbcResource.instance().getJdbcTemplate(ds);
			ThpService thpService =new ThpService(template,ds);
			CreditService creditService =new CreditService(template,ds);
			CustomerService custService =new CustomerService(template,ds);
			DepositService depositService =new DepositService(template,ds);
			OrderNumberService orderNumberService =new OrderNumberService(template,ds);
			if(aftersign_value.equals(sign)){
				
				//没有处理
				if(thpService.isNotDoThp(order_no,return_params)){
					thpService.updaeteThp(order_no, trade_no, "2", order_amount,return_params);
					
					//
					orderNumberService.createOrderNumber(trade_no);
					
					
					String login_name=thpService.queryLoginName(order_no);
			
					//支付成功
					
						if(creditService.add(login_name, order_amount,"自动充值", "th微信支付:"+trade_time, login_name, order_no)){
							 // try{
									Customer customer= custService.getCustomer(login_name);
									//添加存款记录和日志
									String dep_no = OrderNoService.createLocalNo("DE");
									depositService.addDeposit2(dep_no, customer.cust_id, login_name, customer.real_name, order_amount, "th微信支付", "", "th微信支付", "", order_no);
									//查询是否第一次存款,如果是,升级用户等级
									if(depositService.NTgetCount(customer.cust_id) == 1){
										custService.NTmodCustlevelFirst(customer.cust_id, 1);
									}
									
									if(customer.promo_flag != null && customer.promo_flag){
										ScoreService scoreService =new ScoreService(template,ds);
										if(scoreService.depositCountToday(login_name)==1){
											scoreService.modScore(order_no, "签到积分",  new BigDecimal(1), login_name, customer.cust_id, "");
										}
										scoreService.modScore(order_no, "存款积分",new BigDecimal( order_amount.divide(new BigDecimal(100)).intValue()), login_name, customer.cust_id, "");
										
										Date now =new Date(System.currentTimeMillis());
										if(now.getTime()>DateUtil.stringToDate("2017-05-27", "yyyy-MM-dd").getTime() &&
									    		  DateUtil.stringToDate("2017-06-01", "yyyy-MM-dd").getTime()>now.getTime()){
									    	  //int dcount=depositService.NTgetCountToday(customer.cust_id);
									    	 // if(dcount<=5){
									    		  String giftCode=RandomUtil.getRandom(8);
													 CashGift gift =new CashGift();
										        	 gift.gift_code=giftCode;
										        	 gift.login_name=customer.login_name;
										        	 gift.deposit_credit=order_amount;
										        	 gift.valid_credit=new BigDecimal(0);
										        	 gift.net_credit=new BigDecimal(0);
										        	 

										        	 
										        	 float rate = 0;
										        	 float f = 0.0f;
										        	 if(order_amount.intValue() <=10000){
										        		 gift.rate=Float.valueOf(3);
											        	 rate = 3;
											        	 f=order_amount.floatValue()*3/100;
													 }else if(order_amount.intValue() > 10000 && order_amount.intValue()<=30000){
														 gift.rate=Float.valueOf(3.5f);
											        	 rate = 3.5f;
											        	 f=order_amount.floatValue()*3.5f/100;
													 }else if(order_amount.intValue() > 30000 && order_amount.intValue()<=50000){
														 gift.rate=Float.valueOf(4);
											        	 rate = 4;
											        	 f=order_amount.floatValue()*4/100;
													 }else if(order_amount.intValue() > 50000 ){
														 gift.rate=Float.valueOf(5);
											        	 rate = 5;
											        	 f=order_amount.floatValue()*5/100;
											        	 if(f>2888){
											        		 f=2888;
											        	 }
													 }
										        	 
										        	 gift.payout=new BigDecimal(f);
										        	 gift.gift_type="端午节存送";
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
										        	 
									    	 // }
									    }
									}
									
									
								    
									Result r =Result.create(task.getId(), task.getFunId());
									r.addFields(new String[]{"ok"});
									r.setFlag("-1");
									r.setIsList(true);
									r.setLength(1);
									r.addRecord(new String[]{"1"});
								return r;
									
									
								//}catch(Exception e){
									
									//e.printStackTrace();
									
								//}
						}
					
					}
				}
	    	}catch(Exception e){
	    		e.printStackTrace();
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
