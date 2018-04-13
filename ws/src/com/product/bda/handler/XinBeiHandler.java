package com.product.bda.handler;

import java.math.BigDecimal;
import java.util.Date;

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
import com.product.bda.service.XinBeiService;
import com.product.bda.service.YinbaopayService;

import bsz.exch.bank.ThPayResource;
import bsz.exch.bank.YinbaoPayResource;
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

@Handler(name="XinBeiPay")
public class XinBeiHandler {

	@Before
	public FobbinInfo before(Task task,InterFace inter){
		String product=task.getParam("product");
		if(!"8da".equals(product)){
			return  new FobbinInfo(true,"No product support!");
		}
		return new FobbinInfo(false,"");
	}
	
	
	@Service(name="pay")
	@Params(validateField={"login_name","amount","return_url"})
    public Result pay(Task task,InterFace inter){
		
		
		String product = task.getParam("product");
		String pre=ThPayResource.instance().getConfig("xinbeipay."+product+".pre");
		String version=ThPayResource.instance().getConfig("xinbeipay."+product+".version");
		String url = task.getParam("return_url");  //客户访问用的网址
		
		String order_id = pre+"_" + String.valueOf(System.currentTimeMillis());
		String payamount = task.getParam("amount");
		
		
		String MerchantsID =ThPayResource.instance().getConfig("xinbeipay."+product+".MerchantsID"); 
		String mer_key =ThPayResource.instance().getConfig("xinbeipay."+product+".MD5Key"); 
		
		String synNotifyUrl = ThPayResource.instance().getConfig("xinbeipay."+product+".synNotifyUrl"); //同步跳转返回接口
		String asyNotifyUrl = ThPayResource.instance().getConfig("xinbeipay."+product+".asyNotifyUrl"); //异步通知返回接口
		
		String bank = task.getParam("bank");	//支付方式
		String payip = task.getParam("ip");	//支付ip
		
		String submit_value = "";

		String remark1 = task.getParam("login_name");
		JdbcTemplate template=JdbcResource.instance().getJdbcTemplate(inter.getDataSource());
		String ds=inter.getDataSource();
		
		
		XinBeiService xbpService = new XinBeiService(template,ds);
		PayOnlineService poService= new PayOnlineService(template,ds);
		PayOnline online = poService.getPayOnline("xinbei", product);
		if(online != null){
			
			submit_value = online.submit_value;
			synNotifyUrl = online.return_value;
			asyNotifyUrl = online.notify_value;
			
			
			
		}
		
		String paytype="";
		if(bank.equals("100068")){
			paytype="QQ钱包";
		}else if(bank.equals("100040")){
			paytype="微信支付";
			MerchantsID =ThPayResource.instance().getConfig("xinbeipay."+product+".MerchantsIDWECHAT");
			mer_key =ThPayResource.instance().getConfig("xinbeipay."+product+".MD5KeyWECHAT"); 
		}else if(bank.equals("100067")){
			paytype="支付宝支付";
			MerchantsID =ThPayResource.instance().getConfig("xinbeipay."+product+".MerchantsIDAli");
			mer_key =ThPayResource.instance().getConfig("xinbeipay."+product+".MD5KeyAli"); 
		}else if(bank.equals("100069")){
			paytype="京东钱包";
			MerchantsID =ThPayResource.instance().getConfig("xinbeipay."+product+".MerchantsIDJD");
			mer_key =ThPayResource.instance().getConfig("xinbeipay."+product+".MD5KeyJD"); 
		}else if(bank.equals("100012")){
			paytype="银联钱包";
			MerchantsID =ThPayResource.instance().getConfig("xinbeipay."+product+".MerchantsIDYILIAN");
			mer_key =ThPayResource.instance().getConfig("xinbeipay."+product+".MD5KeyYINLIAN"); 
		}
		xbpService.createXbp(order_id, task.getParam("login_name"), payamount, payip, paytype, remark1,url);
		String attach = task.getParam("login_name");
		String OrderDate = DateUtil.getOrderTimeyyyyMMddhhmmss();
		try{
			
			String sign = "";
			//拼接字符串
			
			String SignValue = "Version=[" + version +"]MerchantCode=[" + MerchantsID +"]OrderId=[" + order_id +"]Amount=[" +payamount +"]AsyNotifyUrl=["
					+ asyNotifyUrl +"]SynNotifyUrl=[" + synNotifyUrl +"]OrderDate=[" + OrderDate
					+"]TradeIp=[" + payip +"]PayCode=[" + bank +"]TokenKey=[" + mer_key +"]";
			//System.out.println(SignValue);
			sign = MD5Util.MD5(SignValue);
						 
				//System.out.println(tempStr);
				
			    Result r =Result.create(task.getId(), task.getFunId());
				r.addFields(new String[]{"ok","order_id","version","message","order_amount","signMsg","attach","MerchantsID","submit_value","synNotifyUrl","asyNotifyUrl","OrderDate","payip","bank"});
				r.setFlag("-1");
				r.setIsList(true);
				r.setLength(1);
				r.addRecord(new String[]{"1",order_id,version,"",payamount,sign,attach,MerchantsID,submit_value,synNotifyUrl,asyNotifyUrl,OrderDate,payip,bank});
			return r;
		 }catch(Exception e){
			e.printStackTrace();
		 }
		
		 Result r =Result.create(task.getId(), task.getFunId());
		 r.addFields(new String[]{"ok","order_id","version","message","order_amount","signMsg","attach","MerchantsID","submit_value","synNotifyUrl","asyNotifyUrl","OrderDate","payip","bank"});
		 r.setFlag("-1");
		 r.setIsList(true);
		 r.setLength(1);
		 r.addRecord(new String[]{"0",order_id,"","无法创建订单!",payamount,"",attach,"",submit_value,"","","","",""});
		 return r;
   }
	
	
	@Service(name="notify")
	@Params(validateField={"MerchantCode"})
	public Result notify(Task task,InterFace inter){
		String product = task.getParam("product");
		
		String MerchantCode = task.getParam("MerchantCode");
	    String OrderId = task.getParam("OrderId");
	    String OrderDate = task.getParam("OrderDate");
	    String TradeIp = task.getParam("TradeIp");
	    String SerialNo = task.getParam("SerialNo");
	    String PayCode = task.getParam("PayCode");
	    String State = task.getParam("State");
	    String FinishTime = task.getParam("FinishTime");
	    String SignValue = task.getParam("SignValue");
	    String version=ThPayResource.instance().getConfig("xinbeipay."+product+".version");
	    BigDecimal Amount = new BigDecimal(task.getParam("Amount"));
	    if ("8888".equalsIgnoreCase(State)){
	    	String ds=ThPayResource.instance().getConfig("xinbeipay."+product+".datasource");
	    	String mer_key =ThPayResource.instance().getConfig("xinbeipay."+product+".MD5Key"); 
			
	    	if(PayCode.equals("100040")){//微信
				mer_key =ThPayResource.instance().getConfig("xinbeipay."+product+".MD5KeyWECHAT"); 
			}else if(PayCode.equals("100067")){//支付宝
				mer_key =ThPayResource.instance().getConfig("xinbeipay."+product+".MD5KeyAli"); 
			}else if(PayCode.equals("100069")){//京东
				mer_key =ThPayResource.instance().getConfig("xinbeipay."+product+".MD5KeyJD"); 
			}else if(PayCode.equals("100012")){//
				mer_key =ThPayResource.instance().getConfig("xinbeipay."+product+".MD5KeyYINLIAN"); 
			}
			
			//校验MD5码是不是正确的
	    	String sign = "";
			//拼接字符串
			
			String source = "Version=[" + version +"]MerchantCode=[" + MerchantCode +"]OrderId=[" + OrderId +"]OrderDate=[" +OrderDate +"]TradeIp=["
					+ TradeIp +"]SerialNo=[" + SerialNo +"]Amount=[" + Amount
					+"]PayCode=[" + PayCode +"]State=[" + State +"]FinishTime=[" + FinishTime +"]TokenKey=[" + mer_key +"]";
			//System.out.println(SignValue);
			sign = MD5Util.MD5(source);
		
			
			
			JdbcTemplate template=JdbcResource.instance().getJdbcTemplate(ds);
			XinBeiService xbpService = new XinBeiService(template,ds);
			CreditService creditService =new CreditService(template,ds);
			CustomerService custService =new CustomerService(template,ds);
			DepositService depositService =new DepositService(template,ds);
			OrderNumberService orderNumberService =new OrderNumberService(template,ds);
			if(SignValue.equals(sign) ){ 
				//没有处理
				if(xbpService.isNotDoYdp(OrderId)){
					//ybpService.updaeteYdp(ordernumber, sysnumber, "2", paymoney);
					
					String payType="";
					if(PayCode.equals("100068")){
						payType ="QQ扫码";
						xbpService.updaeteXbp(OrderId, SerialNo, "2", Amount, new BigDecimal(0.985));
						
					}else if(PayCode.equals("100067")){//支付宝
						xbpService.updaeteXbp(OrderId, SerialNo, "2", Amount, new BigDecimal(0.982));
						payType ="支付宝";
					}else if(PayCode.equals("100040")){//微信
						payType ="微信";
						xbpService.updaeteXbp(OrderId, SerialNo, "2", Amount, new BigDecimal(0.988));
					}else if(PayCode.equals("100069")){//京东
						payType ="京东钱包";
						xbpService.updaeteXbp(OrderId, SerialNo, "2", Amount, new BigDecimal(0.985));
					}else if(PayCode.equals("100012")){//
						payType ="银联钱包";
						xbpService.updaeteXbp(OrderId, SerialNo, "2", Amount, new BigDecimal(0.985));
					}
					
					orderNumberService.createOrderNumber(OrderId);
					
					String login_name = xbpService.queryLoginname(OrderId);
			
					//支付成功
					
						if(creditService.add(login_name, Amount,"自动充值", "xb在线支付:"+payType, login_name, OrderId)){
							  try{
									Customer customer= custService.getCustomer(login_name);
									//添加存款记录和日志
									String dep_no = OrderNoService.createLocalNo("DE");
									depositService.addDeposit2(dep_no, customer.cust_id, login_name, customer.real_name, Amount, "xb在线支付"+payType, "", "xb在线支付"+payType, "", OrderId);
									//查询是否第一次存款,如果是,升级用户等级
									if(depositService.NTgetCount(customer.cust_id) == 1){
										custService.NTmodCustlevelFirst(customer.cust_id, 1);
									}
									
									if(customer.promo_flag != null && customer.promo_flag){
										ScoreService scoreService =new ScoreService(template,ds);
										if(scoreService.depositCountToday(login_name)==1){
											scoreService.modScore(OrderId, "签到积分",  new BigDecimal(1), login_name, customer.cust_id, "");
										}
										scoreService.modScore(OrderId, "存款积分",new BigDecimal( Amount.divide(new BigDecimal(100)).intValue()), login_name, customer.cust_id, "");
										
										Date now =new Date(System.currentTimeMillis());
										if(now.getTime()>DateUtil.stringToDate("2017-12-01", "yyyy-MM-dd").getTime() &&
									    		  DateUtil.stringToDate("2018-01-01", "yyyy-MM-dd").getTime()>now.getTime() && Amount.intValue()>=500){
									    	 
											
											 String giftCode=RandomUtil.getRandom(8);
											 CashGift gift =new CashGift();
								        	 gift.gift_code=giftCode;
								        	 gift.login_name=customer.login_name;
								        	 gift.deposit_credit=Amount;
								        	 gift.valid_credit=new BigDecimal(0);
								        	 gift.net_credit=new BigDecimal(0);
								        	 
								        	 float rate = 0;
								        	 float f = 0.0f;
								        	 if(Amount.intValue() >= 500 && Amount.intValue()<5000){
								        		 gift.rate=Float.valueOf(1);
									        	 rate = 1;
									        	 f=Amount.floatValue()*1/100;
											 }else if(Amount.intValue() >= 5000 && Amount.intValue()<10000){
												 gift.rate=Float.valueOf(1.8f);
									        	 rate = 1.8f;
									        	 f=Amount.floatValue()*1.8f/100;
											 }else if(Amount.intValue() >= 10000 && Amount.intValue()<30000){
												 gift.rate=Float.valueOf(2.5f);
									        	 rate = 2.5f;
									        	 f=Amount.floatValue()*2.5f/100;
											 }else if(Amount.intValue() >= 30000 && Amount.intValue()<50000){
												 gift.rate=Float.valueOf(3.8f);
									        	 rate = 3.8f;
									        	 f=Amount.floatValue()*3.8f/100;
											 }else if(Amount.intValue() >= 50000 ){
												 gift.rate=Float.valueOf(5);
									        	 rate = 5;
									        	 f=Amount.floatValue()*5/100;
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
