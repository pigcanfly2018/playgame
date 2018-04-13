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
import com.product.bda.service.GaoTongPayService;
import com.product.bda.service.OrderNoService;
import com.product.bda.service.OrderNumberService;
import com.product.bda.service.PayOnline;
import com.product.bda.service.PayOnlineService;
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

@Handler(name="GaoTongPay")
public class GaoTongPayHandler {

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
		String pre=ThPayResource.instance().getConfig("gaotongpay."+product+".pre");
		String url = task.getParam("return_url");  //客户访问用的网址
		
		
        
		
		String order_id = pre+"_" + String.valueOf(System.currentTimeMillis());
		String payamount = task.getParam("amount");
		
		
		String partner =ThPayResource.instance().getConfig("gaotongpay."+product+".partner"); 
		String mer_key =ThPayResource.instance().getConfig("gaotongpay."+product+".mer_key"); 
		
		String hrefbackurl = ThPayResource.instance().getConfig("gaotongpay."+product+".hrefbackurl"); //服务器跳转返回接口
		String callbackurl = ThPayResource.instance().getConfig("gaotongpay."+product+".callbackurl"); //服务器通知返回接口
		
		String paytype = task.getParam("bank");	//支付方式
		String payip = task.getParam("ip");	//支付ip
		
		String submit_value = "";

		String remark = task.getParam("remark");
		JdbcTemplate template=JdbcResource.instance().getJdbcTemplate(inter.getDataSource());
		String ds=inter.getDataSource();
		
		
		GaoTongPayService gtpService = new GaoTongPayService(template,ds);
		PayOnlineService poService= new PayOnlineService(template,ds);
		PayOnline online = poService.getPayOnline("gaotong", product);
		if(online != null){
			
			submit_value = online.submit_value;
			hrefbackurl = online.return_value;
			callbackurl = online.notify_value;
			
			
			
		}
		gtpService.createYbp(order_id, task.getParam("login_name"), payamount, payip, paytype, remark,url);
		String attach = task.getParam("login_name");
		try{
			
			String sign = "";
			//拼接字符串
			String tempStr="partner="+partner+"&banktype="+paytype+"&paymoney="+payamount+
					"&ordernumber="+order_id+"&callbackurl="+callbackurl+mer_key;
			sign = MD5Util.MD5(tempStr);
						 
				//System.out.println(tempStr);
				
			    Result r =Result.create(task.getId(), task.getFunId());
				r.addFields(new String[]{"ok","order_id","message","order_amount","signMsg","paytype","attach","partnerID","submit_value","return_value","notify_value"});
				r.setFlag("-1");
				r.setIsList(true);
				r.setLength(1);
				r.addRecord(new String[]{"1",order_id,"",payamount,sign,paytype,attach,partner,submit_value,hrefbackurl,callbackurl});
			return r;
		 }catch(Exception e){
			e.printStackTrace();
		 }
		
		 Result r =Result.create(task.getId(), task.getFunId());
		 r.addFields(new String[]{"ok","order_id","message","order_amount","signMsg","paytype","attach","partnerID","submit_value","return_value","notify_value"});
		 r.setFlag("-1");
		 r.setIsList(true);
		 r.setLength(1);
		 r.addRecord(new String[]{"0",order_id,"无法创建订单!",payamount,"",paytype,attach,partner,submit_value,hrefbackurl,callbackurl});
		 return r;
   }
	
	
	
	
	@Service(name="notify")
	@Params(validateField={"ordernumber","paymoney","orderstatus","sysnumber","partner","attach","sign"})
	public Result notify(Task task,InterFace inter){
		String product = task.getParam("product");
		
		String ordernumber = task.getParam("ordernumber");
	    String orderstatus = task.getParam("orderstatus");
	    String sysnumber = task.getParam("sysnumber");
	    String partner = task.getParam("partner");
	    String attach = task.getParam("attach");
	    String sign = task.getParam("sign");
	    
	    BigDecimal paymoney = new BigDecimal(task.getParam("paymoney"));
	    if(orderstatus.equals("1")){
	    	String ds=ThPayResource.instance().getConfig("gaotongpay."+product+".datasource");
			String mer_key =ThPayResource.instance().getConfig("gaotongpay."+product+".mer_key");
			
			
			//校验MD5码是不是正确的
			String tempStr="partner="+partner+"&ordernumber="+ordernumber+
					"&orderstatus="+orderstatus+"&paymoney="+paymoney+mer_key;
			String newMD5 = MD5Util.md5Encode(tempStr);
		
			
			
			JdbcTemplate template=JdbcResource.instance().getJdbcTemplate(ds);
			GaoTongPayService gtpService = new GaoTongPayService(template,ds);
			CreditService creditService =new CreditService(template,ds);
			CustomerService custService =new CustomerService(template,ds);
			DepositService depositService =new DepositService(template,ds);
			OrderNumberService orderNumberService =new OrderNumberService(template,ds);
			if(newMD5.equals(sign) ){
				//没有处理
				if(gtpService.isNotDoYdp(ordernumber,attach)){
					//ybpService.updaeteYdp(ordernumber, sysnumber, "2", paymoney);
					String payType = gtpService.queryPayType(ordernumber);
					if(payType == null)
						payType = "";
					
					
					if(payType.equals("WEIXIN")){
						gtpService.updaeteWeixinYdp(ordernumber, sysnumber, "2", paymoney);
					}else{
						gtpService.updaeteZhifubaoYdp(ordernumber, sysnumber, "2", paymoney);
					}
					
					orderNumberService.createOrderNumber(sysnumber);
					
					String login_name=attach;
			
					//支付成功
					
						if(creditService.add(login_name, paymoney,"自动充值", "gt在线支付:"+payType, login_name, ordernumber)){
							  try{
									Customer customer= custService.getCustomer(login_name);
									//添加存款记录和日志
									String dep_no = OrderNoService.createLocalNo("DE");
									depositService.addDeposit2(dep_no, customer.cust_id, login_name, customer.real_name, paymoney, "gt在线支付"+payType, "", "gt在线支付"+payType, "", ordernumber);
									//查询是否第一次存款,如果是,升级用户等级
									/*if(depositService.NTgetCount(customer.cust_id) == 1){
										custService.NTmodCustlevelFirst(customer.cust_id, 1);
									}*/
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
										scoreService.modScore(ordernumber, "存款积分",new BigDecimal( paymoney.divide(new BigDecimal(100)).intValue()), login_name, customer.cust_id, "");
										
										Date now =new Date(System.currentTimeMillis());
										if(now.getTime()>DateUtil.stringToDate("2017-12-01", "yyyy-MM-dd").getTime() &&
									    		  DateUtil.stringToDate("2018-01-01", "yyyy-MM-dd").getTime()>now.getTime() && paymoney.intValue()>=500){
											
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
