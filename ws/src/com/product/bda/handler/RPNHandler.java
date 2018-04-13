package com.product.bda.handler;


import java.math.BigDecimal;
import java.util.Date;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

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
import com.product.bda.service.RpnService;
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

@Handler(name="RPN")
public class RPNHandler {

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
		String pre=ThPayResource.instance().getConfig("rpnpay."+product+".pre");
		String url = task.getParam("return_url");  //客户访问用的网址
		
		String order_id = pre+"_" + String.valueOf(System.currentTimeMillis());
		String show_amount = task.getParam("amount");
		String order_amount = (Integer.parseInt(show_amount)*100)+"";
		String mid =ThPayResource.instance().getConfig("rpnpay."+product+".mid"); 
		String key =ThPayResource.instance().getConfig("rpnpay."+product+".key"); 
		String version =ThPayResource.instance().getConfig("rpnpay."+product+".version"); 
		String return_url = ThPayResource.instance().getConfig("rpnpay."+product+".return_url"); //服务器跳转返回接口
		String notify_url = ThPayResource.instance().getConfig("rpnpay."+product+".notify_url"); //服务器通知返回接口
		
		String bank_id = task.getParam("bank_id");	//支付银行
		String payip = task.getParam("ip");	//支付ip
		String remark = task.getParam("remark");
		String sign_type = "MD5";
		String order_time = DateUtil.getOrderTimeyyyyMMddhhmmss();
		JdbcTemplate template=JdbcResource.instance().getJdbcTemplate(inter.getDataSource());
		String ds=inter.getDataSource();
		
		RpnService rpnService = new RpnService(template,ds);
		String login_name = task.getParam("login_name");
		String submit_value = "";
		
		rpnService.createRpn(order_id, login_name, show_amount, payip, "", remark, url);
		
		PayOnlineService poService= new PayOnlineService(template,ds);
		PayOnline online = poService.getPayOnline("rpnpay", product);
		if(online != null){
			submit_value = online.submit_value;
			return_url = online.return_value;
			notify_url = online.notify_value;
			
		}
		
		bank_id = "1";
		remark = task.getParam("login_name");
		try{
			String prestr ="version="+version+"|sign_type="+sign_type+"|mid="+mid+"|return_url="+return_url
					+"|notify_url="+notify_url+"|order_id="+order_id+"|order_amount="+order_amount+"|order_time="+order_time+"|bank_id="+bank_id+"|key="+key;
					 
			System.out.println(prestr);
			String signature = MD5Util.md5Encode(prestr);
			
			 Result r =Result.create(task.getId(), task.getFunId());
				r.addFields(new String[]{"ok","order_id","message","order_amount","show_amount","signature","bank_id","remark","mid","return_url","notify_url","order_time","submit_value"});
				r.setFlag("-1");
				r.setIsList(true);
				r.setLength(1);
				r.addRecord(new String[]{"1",order_id,"",order_amount,show_amount,signature,bank_id,remark,mid,return_url,notify_url,order_time,submit_value});
			return r;
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		Result r =Result.create(task.getId(), task.getFunId());
		 r.addFields(new String[]{"ok","order_id","message","order_amount","sign_value","payip","paytype","remark"});
		 r.setFlag("-1");
		 r.setIsList(true);
		 r.setLength(1);
		 r.addRecord(new String[]{"0",order_id,"无法创建订单!",order_amount,"",bank_id,remark});
		 return r;
	}
	
	
	@Service(name="notify")
	@Params(validateField={"order_id","signature"})
	@Transactional
	public Result notify(Task task,InterFace inter){
		String product = task.getParam("product");
		String order_id = task.getParam("order_id");
	    String order_time = task.getParam("order_time");
	    String order_amount = task.getParam("order_amount");
	    String deal_id = task.getParam("deal_id");
	    String deal_time = task.getParam("deal_time");
	    String pay_amount = task.getParam("pay_amount");
	    String pay_result = task.getParam("pay_result");
	    String signature = task.getParam("signature");
	    
	    if ("3".equalsIgnoreCase(pay_result)){
	    	String ds=ThPayResource.instance().getConfig("rpnpay."+product+".datasource"); 
	    	String key =ThPayResource.instance().getConfig("rpnpay."+product+".key"); 
	    	
	    	String prestr ="order_id="+order_id+"|order_time="+order_time+"|order_amount="+order_amount+"|deal_id="+deal_id
					+"|deal_time="+deal_time+"|pay_amount="+pay_amount+"|pay_result="+pay_result+"|key="+key;
	    
			String aftersign_value =  MD5Util.md5Encode(prestr);
			if(aftersign_value.equals(signature)){
				JdbcTemplate template=JdbcResource.instance().getJdbcTemplate(ds);
				RpnService rpnService = new RpnService(template,ds);
				CreditService creditService =new CreditService(template,ds);
				CustomerService custService =new CustomerService(template,ds);
				DepositService depositService =new DepositService(template,ds);
				OrderNumberService orderNumberService =new OrderNumberService(template,ds);
				if(rpnService.isNotDoRpn(order_id)){
					rpnService.updaeteRpn(order_id, deal_id, "2",  new BigDecimal(pay_amount).divide(new BigDecimal(100)));
					String login_name=rpnService.queryLoginName(order_id);
					
					orderNumberService.createOrderNumber(order_id);
					Customer customer= custService.getCustomer(login_name);
					//支付成功
					if(creditService.add(login_name, new BigDecimal(pay_amount).divide(new BigDecimal(100)),"自动充值", "rpn支付:"+deal_time, login_name, order_id)){
						  try{
								
								//添加存款记录和日志
								String dep_no = OrderNoService.createLocalNo("DE");
								depositService.addDeposit2(dep_no, customer.cust_id, login_name, customer.real_name, new BigDecimal(pay_amount).divide(new BigDecimal(100)), "rpn支付", "", "rpn支付", "", order_id);
								//查询是否第一次存款,如果是,升级用户等级
								if(depositService.NTgetCount(customer.cust_id) == 1){
									if(new BigDecimal(pay_amount).divide(new BigDecimal(100)).intValue() >=100){
										custService.NTmodCustlevelFirst(customer.cust_id, 1);
									}else{
										custService.NTmodFirstDepositDateOnly(customer.cust_id, 1);
									}
									
								}else{ 
									if(customer.cust_level == 0){
										if(new BigDecimal(pay_amount).divide(new BigDecimal(100)).intValue() >=100){
											custService.NTmodCustlevelOnly(customer.cust_id, 1);
										}
									}
								}
								
								if(customer.promo_flag != null && customer.promo_flag){
									ScoreService scoreService =new ScoreService(template,ds);
									if(scoreService.depositCountToday(login_name)==1){
										scoreService.modScore(order_id, "签到积分",  new BigDecimal(1), login_name, customer.cust_id, "");
									}
									scoreService.modScore(order_id, "存款积分",new BigDecimal( new BigDecimal(pay_amount).divide(new BigDecimal(100)).divide(new BigDecimal(100)).intValue()), login_name, customer.cust_id, "");
									
									
									Date now =new Date(System.currentTimeMillis());
									if(now.getTime()>DateUtil.stringToDate("2017-12-01", "yyyy-MM-dd").getTime() &&
								    		  DateUtil.stringToDate("2018-01-01", "yyyy-MM-dd").getTime()>now.getTime() && new BigDecimal(pay_amount).divide(new BigDecimal(100)).intValue()>=500 ){
								    	  //int dcount=depositService.NTgetCountToday(customer.cust_id);
								    	 // if(dcount<=5){
								    		  String giftCode=RandomUtil.getRandom(8);
												 CashGift gift =new CashGift();
									        	 gift.gift_code=giftCode;
									        	 gift.login_name=customer.login_name;
									        	 gift.deposit_credit=new BigDecimal(pay_amount).divide(new BigDecimal(100));
									        	 gift.valid_credit=new BigDecimal(0);
									        	 gift.net_credit=new BigDecimal(0);
									        	
									        	 
									        	 float rate = 0;
									        	 float f = 0.0f;
									        	 if(new BigDecimal(pay_amount).divide(new BigDecimal(100)).intValue() <5000 && new BigDecimal(pay_amount).divide(new BigDecimal(100)).intValue() >= 500){
													 gift.rate=Float.valueOf(1);
										        	 rate = 1;
										        	 f=new BigDecimal(pay_amount).divide(new BigDecimal(100)).floatValue()*1/100;
												 }else if(new BigDecimal(pay_amount).divide(new BigDecimal(100)).intValue() >= 5000 && new BigDecimal(pay_amount).divide(new BigDecimal(100)).intValue()< 10000){
													 gift.rate=Float.valueOf(1.8f);
										        	 rate = 1.8f;
										        	 f=new BigDecimal(pay_amount).divide(new BigDecimal(100)).floatValue()*1.8f/100;
												 }
									        	 
									        	 gift.payout=new BigDecimal(f);
									        	 gift.gift_type="年终现金大回馈";
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
