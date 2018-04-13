package controllers;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import models.CashGift;
import models.Customer;
import models.Deposit;
import models.DepositLog;
import models.Dinpay;
import models.Letter;
import models.OrderNo;

import org.apache.commons.codec.digest.DigestUtils;

import com.ws.service.DinPayService;

import bsz.exch.service.Result;
import play.Play;
import play.mvc.Before;
import play.mvc.Controller;
import service.CreditLogService;
import service.CustomerService;
import util.DateUtil;
import util.DinpayBean;
import util.DinpayRandom;
import util.MyRandom;

public class DinpayApp extends Controller{
	
	@Before
	 public static void before(){
		if(IPApp.isxss()){
			renderText("您的输入存在非法字符!");
		}
	}
	
	public static void show(String order_no){
		if(order_no==null||(!order_no.startsWith("YF"))){
	    	String result2="非法操作！";
			render(result2,order_no);
	    }
		if(Dinpay.queryFinished(order_no)){
			String result1="您的订单号为："+order_no+"。<br/>您的订单已经支付完成！";
			render(result1,order_no);
		}else{
			String result2="您的订单号为："+order_no+"。<br/>暂时查询不到您订单支付结果信息，请稍等20秒再查询！<br/>如果您已经成功支付，请您联系客服人员，为您确认。";
			render(result2,order_no);
		}
	}
	
	public static void showweixin(String order_no){
		if(order_no==null||(!order_no.startsWith("YF"))){
	    	String result2="非法操作！";
			render(result2,order_no);
	    }
		if(Dinpay.queryFinished(order_no)){
			String result1="您的订单号为："+order_no+"。<br/>您的订单已经支付完成！";
			render(result1,order_no);
		}else{
			String result2="您的订单号为："+order_no+"。<br/>暂时查询不到您订单支付结果信息，请稍等20秒再查询！<br/>如果您已经成功支付，请您联系客服人员，为您确认。";
			render(result2,order_no);
		}
	}
	
	public static void cardshow(String order_no){
		if(order_no==null||(!order_no.startsWith("YF"))){
	    	String result2="非法操作！";
			render(result2,order_no);
	    }
		if(Dinpay.queryFinished(order_no)){
			String result1="您的订单号为："+order_no+"。<br/>您的订单已经支付完成！";
			render(result1,order_no);
		}else{
			String result2="您的订单号为："+order_no+"。<br/>暂时查询不到您订单支付结果信息，请稍等20秒再查询！<br/>如果您已经成功支付，请您联系客服人员，为您确认。";
			render(result2,order_no);
		}
	}
	
	public static void yfpay(){
		String merchant_code	= params.get("merchant_code");
		String notify_type = params.get("notify_type");
		String notify_id = params.get("notify_id");
		String interface_version = params.get("interface_version");
		String sign_type = params.get("sign_type");
		String dinpaySign = params.get("sign");
		String order_no = params.get("order_no");
		String order_time = params.get("order_time");
		String order_amount = params.get("order_amount");
		String extra_return_param = params.get("extra_return_param");
		String trade_no = params.get("trade_no");
		String trade_time = params.get("trade_time");
		String trade_status = params.get("trade_status");
		String bank_seq_no =  params.get("bank_seq_no");
		
		DinpayBean dinpayBean =DinpayRandom.get().find(merchant_code);
		if(dinpayBean==null){
			System.out.println("merchant_code ="+merchant_code);
		}
		/**
		 *签名顺序按照参数名a到z的顺序排序，若遇到相同首字母，则看第二个字母，以此类推，
		*同时将商家支付密钥key放在最后参与签名，组成规则如下：
		*参数名1=参数值1&参数名2=参数值2&……&参数名n=参数值n&key=key值
		**/
		//组织订单信息
		StringBuilder signStr = new StringBuilder();
		if(null != bank_seq_no && !bank_seq_no.equals("")) {
			signStr.append("bank_seq_no=").append(bank_seq_no).append("&");
		}
		if(null != extra_return_param && !extra_return_param.equals("")) {
			signStr.append("extra_return_param=").append(extra_return_param).append("&");
		}
		signStr.append("interface_version=V3.0").append("&");
		signStr.append("merchant_code=").append(merchant_code).append("&");
		if(null != notify_id && !notify_id.equals("")) {
			signStr.append("notify_id=").append(notify_id).append("&notify_type=").append(notify_type).append("&");
		}

		signStr.append("order_amount=").append(order_amount).append("&");
		signStr.append("order_no=").append(order_no).append("&");
		signStr.append("order_time=").append(order_time).append("&");
		signStr.append("trade_no=").append(trade_no).append("&");
		signStr.append("trade_status=").append(trade_status).append("&");
		if(null != trade_time && !trade_time.equals("")) {
			signStr.append("trade_time=").append(trade_time).append("&");
		}
		String key=dinpayBean.md5key;
		signStr.append("key=").append(key);
		String signInfo = signStr.toString();
		String sign = DigestUtils.md5Hex(signInfo); 
		if(dinpaySign.equals(sign)) {
			if(Dinpay.queryFinished(order_no)){
				renderText("FAILURE");
			}
			try{
				Dinpay.updatefinished(bank_seq_no, trade_status, trade_no, trade_time, notify_id,
						sign_type, dinpaySign, new BigDecimal(order_amount), order_no);
				if("SUCCESS".equals(trade_status)){
					 Dinpay dinpay=Dinpay.NTget(order_no);
					 Customer cust=Customer.getCustomer(dinpay.login_name);
					 Deposit deposit =new Deposit();
					 deposit.deposit_date=new Date(DateUtil.stringToDate(dinpay.trade_time, "yyyy-MM-dd HH:mm:ss").getTime()); 
					 deposit.cust_id=cust.cust_id;
					 deposit.pdage_status=1;
					 deposit.status=1;
					 deposit.create_user=cust.login_name;
					 deposit.create_date=new Date(System.currentTimeMillis());
					 deposit.ip=IPApp.getIpAddr();
					 deposit.amount=dinpay.pay_amount;
					 deposit.poundage=new BigDecimal(0);
					 deposit.bank_name="智付支付";
					 deposit.account_name=dinpayBean.account_name;
					 deposit.deposit_type="在线支付";
					 deposit.order_no=dinpay.order_no;
					 deposit.location="";
					 deposit.remarks="";
					 deposit.login_name=cust.login_name;
					 deposit.real_name=cust.real_name;
					 deposit.dep_no=OrderNo.createLocalNo("DE");
					 Long dep_id=deposit.NTcreat();
					 if(dep_id!=null){
						 DepositLog log =new DepositLog();
						 log.after_status=1;
						 log.create_user="system";
						 log.deposit_id=dep_id;
						 log.pre_status=0;
						 log.dep_no=deposit.dep_no;
						 log.create_date=new Date(System.currentTimeMillis());
						 log.remarks="系统创建";
						 log.NTcreat();
					 }
					 if(CustomerService.modCredit(deposit.cust_id, CreditLogService.Depoist, deposit.login_name, 
							 deposit.amount, "system","存款", deposit.dep_no)){
							     Deposit.NTchangeStatus(dep_id, 3, 3);
								 DepositLog log2 =new DepositLog();
								 log2.after_status=3;
								 log2.dep_no=deposit.dep_no;
								 log2.create_user="system";
								 log2.deposit_id=dep_id;
								 log2.pre_status=1;
								 log2.create_date=new Date(System.currentTimeMillis());
								 log2.remarks="系统审核";
								 log2.NTcreat();
						      if(Deposit.NTgetCount(deposit.cust_id)==1){
								Deposit.NTrecAuditDate(dep_id);
								Customer.NTmodCustlevelFirst(deposit.cust_id, 1);
								StringBuffer sb =new StringBuffer();
								sb.append("<p>尊敬的客户，您好，恭喜您已经升级为星级一，为了让您有更好的游戏体验，我们为您提供了更优更快的网站线路，请您牢记我们的会员网址：<span>www.268da.com</span> ，祝您生活愉快，盈利多多!</p>");
								sb.append("<p style=\"text-align:right\">八达国际客服部上</p>");
							    Letter.NTcreat(cust.cust_id, cust.login_name, "八达国际娱乐城会员网址：www.268da.com", sb.toString(), cust.login_name, true);
								
							}
						     Dinpay.updateDepNo(deposit.dep_no, order_no); 
						     Calendar calendar = Calendar.getInstance();
						     
						     if(cust.promo_flag != null && cust.promo_flag){
						    	 if(calendar.getTimeInMillis()<=1441036799979l){ //积分翻倍活动持续到2015年8月31号23点59分59秒
							    	 CustomerService.modScore(deposit.dep_no, "存款积分(翻倍)", new BigDecimal(deposit.amount.intValue()*2/100.0), deposit.login_name, deposit.cust_id, deposit.login_name);
							     }else{
							    	 CustomerService.modScore(deposit.dep_no, "存款积分", new BigDecimal(deposit.amount.intValue()/100.0), deposit.login_name, deposit.cust_id, deposit.login_name);
							     }
							     
							     int dcount=Deposit.NTgetCountToday(deposit.cust_id);
							     if(dcount==1){
							    	 CustomerService.modScore(deposit.dep_no, "签到积分", new BigDecimal(1), deposit.login_name, deposit.cust_id, deposit.login_name);
							     }
							     
							     /**光棍节5连礼---start--*/
							      Date depositdate = deposit.create_date;
									 if((depositdate.getTime()>DateUtil.stringToDate("2016-12-21", "yyyy-MM-dd").getTime()&&
												DateUtil.stringToDate("2017-01-02", "yyyy-MM-dd").getTime()>depositdate.getTime())){
										 //dcount=Deposit.NTgetCountToday(deposit.cust_id);
										
											    // if(dcount<=5){
												     String giftCode=MyRandom.getRandom(8);
													 CashGift gift =new CashGift();
										        	 gift.gift_code=giftCode;
										        	 gift.login_name=cust.login_name;
										        	 gift.deposit_credit=deposit.amount;
										        	 gift.valid_credit=new BigDecimal(0);
										        	 gift.net_credit=new BigDecimal(0);
										        	 gift.rate=Float.valueOf(2);
										        	 Integer rate = 2;
										        	 float f=deposit.amount.floatValue()*2/100;
//										        	 if(dcount==5){
//										        		 gift.rate=Float.valueOf(3);
//										        		 f=deposit.amount.floatValue()*3/100;
//										        		 rate = 3;
//										        	 }
//										        	 
//										        	 if(f>1111){
//										        		 f=1111;
//										        	 }
										        	 gift.payout=new BigDecimal(f);
										        	 gift.gift_type="圣诞元旦双节礼";
										        	 gift.status=1;
										        	 gift.gift_no=OrderNo.createLocalNo("GI");
										     		 gift.cust_id=cust.cust_id;
										        	 gift.cs_date=new Date(System.currentTimeMillis());
										        	 gift.real_name=cust.real_name;
										        	 gift.cust_level=cust.cust_level;
										           	 gift.kh_date=cust.create_date;
										        	 gift.create_user="系统";
										        	 gift.create_date=new Date(System.currentTimeMillis());
										        	 Long giftId=gift.NTcreat();
										        	 gift.NTAuditGift(giftId, 3, "系统","系统审核通过-"+deposit.dep_no+" |"+rate);
										        	 CustomerService.modCredit(cust.cust_id,CreditLogService.Gift, 
										 					gift.login_name,gift.payout,"系统", "添加礼金"+gift.gift_type+" |"+rate+"%", gift.gift_no);
											    // }
											     
									 }
									 
						     }
						     
						     
						     
						     /**5连礼---start---*/
//						      Date now =new Date(System.currentTimeMillis());
//						      if((now.getTime()>DateUtil.stringToDate("2015-04-22", "yyyy-MM-dd").getTime()&&
//										DateUtil.stringToDate("2015-04-29", "yyyy-MM-dd").getTime()>now.getTime())){
//						               dcount=Deposit.NTgetCountMoreThan500Today(deposit.cust_id);
//								     if(dcount<=5&&deposit.amount.intValue()>=500){
//									     String giftCode=MyRandom.getRandom(8);
//										 CashGift gift =new CashGift();
//							        	 gift.gift_code=giftCode;
//							        	 gift.login_name=cust.login_name;
//							        	 gift.deposit_credit=deposit.amount;
//							        	 gift.valid_credit=new BigDecimal(0);
//							        	 gift.net_credit=new BigDecimal(0);
//							        	 gift.rate=Float.valueOf(dcount);
//							        	 float f=deposit.amount.floatValue()*dcount/100;
//							        	 if(f>1800){
//							        		 f=1800;
//							        	 }
//							        	 gift.payout=new BigDecimal(f);
//							        	 gift.gift_type="充值5连礼";
//							        	 gift.status=1;
//							        	 gift.gift_no=OrderNo.createLocalNo("GI");
//							     		 gift.cust_id=cust.cust_id;
//							        	 gift.cs_date=new Date(System.currentTimeMillis());
//							        	 gift.real_name=cust.real_name;
//							        	 gift.cust_level=cust.cust_level;
//							           	 gift.kh_date=cust.create_date;
//							        	 gift.create_user="系统";
//							        	 gift.create_date=new Date(System.currentTimeMillis());
//							        	 Long giftId=gift.NTcreat();
//							        	 gift.NTAuditGift(giftId, 3, "系统","系统审核通过-"+deposit.dep_no+" |"+dcount);
//							        	 CustomerService.modCredit(cust.cust_id,CreditLogService.Gift, 
//							 					gift.login_name,gift.payout,"系统", "添加礼金"+gift.gift_type+" |"+dcount+"%", gift.gift_no);
//						           }
//									
//								}		
						    
						     /**5连礼---end---**/
						      
						      
						      
								 
								 
					 }
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			renderText("SUCCESS");
		}else
	        {
			renderText("FAILURE");
		}
	}
	
	
	public static void weixinpay(){
		String merchant_code	= params.get("merchant_code");
		String notify_type = params.get("notify_type");
		String notify_id = params.get("notify_id");
		String interface_version = params.get("interface_version");
		String sign_type = params.get("sign_type");
		String dinpaySign = params.get("sign");
		String order_no = params.get("order_no");
		String order_time = params.get("order_time");
		String order_amount = params.get("order_amount");
		String extra_return_param = params.get("extra_return_param");
		String trade_no = params.get("trade_no");
		String trade_time = params.get("trade_time");
		String trade_status = params.get("trade_status");
		String bank_seq_no =  params.get("bank_seq_no");
		
		
		DinpayBean dinpayBean =DinpayRandom.get().find(merchant_code);
		if(dinpayBean==null){
			System.out.println("merchant_code ="+merchant_code);
		}
		
		Result  result=DinPayService.notice(bank_seq_no, extra_return_param, merchant_code, notify_id, notify_type, order_amount, order_no, order_time, trade_no, trade_status, trade_time, dinpaySign);
		
		
		if(result != null && result.get("ok").equals("1")){
			if(Dinpay.queryFinished(order_no)){
				renderText("SUCCESS");
			}
			try{
				Dinpay.updatefinished(bank_seq_no, trade_status, trade_no, trade_time, notify_id,
						sign_type, dinpaySign, new BigDecimal(order_amount), order_no);
				if("SUCCESS".equals(trade_status)){
					 Dinpay dinpay=Dinpay.NTget(order_no);
					 Customer cust=Customer.getCustomer(dinpay.login_name);
					 Deposit deposit =new Deposit();
					 deposit.deposit_date=new Date(DateUtil.stringToDate(dinpay.trade_time, "yyyy-MM-dd HH:mm:ss").getTime()); 
					 deposit.cust_id=cust.cust_id;
					 deposit.pdage_status=1;
					 deposit.status=1;
					 deposit.create_user=cust.login_name;
					 deposit.create_date=new Date(System.currentTimeMillis());
					 deposit.ip=IPApp.getIpAddr();
					 deposit.amount=dinpay.pay_amount;
					 deposit.poundage=new BigDecimal(0);
					 deposit.bank_name="智付微信支付";
					 deposit.account_name=dinpayBean.account_name;
					 deposit.deposit_type="在线支付";
					 deposit.order_no=dinpay.order_no;
					 deposit.location="";
					 deposit.remarks="";
					 deposit.login_name=cust.login_name;
					 deposit.real_name=cust.real_name;
					 deposit.dep_no=OrderNo.createLocalNo("DE");
					 Long dep_id=deposit.NTcreat();
					 if(dep_id!=null){
						 DepositLog log =new DepositLog();
						 log.after_status=1;
						 log.create_user="system";
						 log.deposit_id=dep_id;
						 log.pre_status=0;
						 log.dep_no=deposit.dep_no;
						 log.create_date=new Date(System.currentTimeMillis());
						 log.remarks="系统创建";
						 log.NTcreat();
					 }
					 if(CustomerService.modCredit(deposit.cust_id, CreditLogService.Depoist, deposit.login_name, 
							 deposit.amount, "system","存款", deposit.dep_no)){
							     Deposit.NTchangeStatus(dep_id, 3, 3);
								 DepositLog log2 =new DepositLog();
								 log2.after_status=3;
								 log2.dep_no=deposit.dep_no;
								 log2.create_user="system";
								 log2.deposit_id=dep_id;
								 log2.pre_status=1;
								 log2.create_date=new Date(System.currentTimeMillis());
								 log2.remarks="系统审核";
								 log2.NTcreat();
						      if(Deposit.NTgetCount(deposit.cust_id)==1){
								Deposit.NTrecAuditDate(dep_id);
								Customer.NTmodCustlevelFirst(deposit.cust_id, 1);
								StringBuffer sb =new StringBuffer();
								sb.append("<p>尊敬的客户，您好，恭喜您已经升级为星级一，为了让您有更好的游戏体验，我们为您提供了更优更快的网站线路，请您牢记我们的会员网址：<span>www.268da.com</span> ，祝您生活愉快，盈利多多!</p>");
								sb.append("<p style=\"text-align:right\">八达国际客服部上</p>");
							    Letter.NTcreat(cust.cust_id, cust.login_name, "八达国际娱乐城会员网址：www.268da.com", sb.toString(), cust.login_name, true);
								
							}
						     Dinpay.updateDepNo(deposit.dep_no, order_no); 
						     Calendar calendar = Calendar.getInstance();
						     
						     if(cust.promo_flag != null && cust.promo_flag){
						    	 if(calendar.getTimeInMillis()<=1441036799979l){ //积分翻倍活动持续到2015年8月31号23点59分59秒
							    	 CustomerService.modScore(deposit.dep_no, "存款积分(翻倍)", new BigDecimal(deposit.amount.intValue()*2/100.0), deposit.login_name, deposit.cust_id, deposit.login_name);
							     }else{
							    	 CustomerService.modScore(deposit.dep_no, "存款积分", new BigDecimal(deposit.amount.intValue()/100.0), deposit.login_name, deposit.cust_id, deposit.login_name);
							     }
							     
							     int dcount=Deposit.NTgetCountToday(deposit.cust_id);
							     if(dcount==1){
							    	 CustomerService.modScore(deposit.dep_no, "签到积分", new BigDecimal(1), deposit.login_name, deposit.cust_id, deposit.login_name);
							     }
							     
							     
							     /**光棍节5连礼---start--*/
							      Date depositdate = deposit.create_date;
									 if((depositdate.getTime()>DateUtil.stringToDate("2016-12-21", "yyyy-MM-dd").getTime()&&
												DateUtil.stringToDate("2017-01-02", "yyyy-MM-dd").getTime()>depositdate.getTime())){
										 //dcount=Deposit.NTgetCountToday(deposit.cust_id);
										
											    // if(dcount<=5){
												     String giftCode=MyRandom.getRandom(8);
													 CashGift gift =new CashGift();
										        	 gift.gift_code=giftCode;
										        	 gift.login_name=cust.login_name;
										        	 gift.deposit_credit=deposit.amount;
										        	 gift.valid_credit=new BigDecimal(0);
										        	 gift.net_credit=new BigDecimal(0);
										        	 gift.rate=Float.valueOf(2);
										        	 Integer rate = 2;
										        	 float f=deposit.amount.floatValue()*2/100;
//										        	 if(dcount==5){
//										        		 gift.rate=Float.valueOf(3);
//										        		 f=deposit.amount.floatValue()*3/100;
//										        		 rate = 3;
//										        	 }
//										        	 
//										        	 if(f>1111){
//										        		 f=1111;
//										        	 }
										        	 gift.payout=new BigDecimal(f);
										        	 gift.gift_type="圣诞元旦双节礼";
										        	 gift.status=1;
										        	 gift.gift_no=OrderNo.createLocalNo("GI");
										     		 gift.cust_id=cust.cust_id;
										        	 gift.cs_date=new Date(System.currentTimeMillis());
										        	 gift.real_name=cust.real_name;
										        	 gift.cust_level=cust.cust_level;
										           	 gift.kh_date=cust.create_date;
										        	 gift.create_user="系统";
										        	 gift.create_date=new Date(System.currentTimeMillis());
										        	 Long giftId=gift.NTcreat();
										        	 gift.NTAuditGift(giftId, 3, "系统","系统审核通过-"+deposit.dep_no+" |"+rate);
										        	 CustomerService.modCredit(cust.cust_id,CreditLogService.Gift, 
										 					gift.login_name,gift.payout,"系统", "添加礼金"+gift.gift_type+" |"+rate+"%", gift.gift_no);
											    // }
											     
									 }
									 
									 
						     }
						     
						     
						     
						     
								 
						     
						     
					 }
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			renderText("SUCCESS");
		}else
	        {
			renderText("SUCCESS");
		}
	}
	
}
