package com.product.bda.handler;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;
















import org.springframework.jdbc.core.JdbcTemplate;

import com.product.bda.service.CashGift;
import com.product.bda.service.CashGiftService;
import com.product.bda.service.CreditService;
import com.product.bda.service.Customer;
import com.product.bda.service.CustomerService;
import com.product.bda.service.DepositService;
import com.product.bda.service.HuiTongService;
import com.product.bda.service.OrderNoService;
import com.product.bda.service.OrderNumberService;
import com.product.bda.service.ScoreService;
import com.product.bda.service.XinBeiService;

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

@Handler(name="HuiTongPay")
public class HuiTongHandler {

	@Before
	public FobbinInfo before(Task task,InterFace inter){
		String product=task.getParam("product");
		if(!"8da".equals(product)){
			return  new FobbinInfo(true,"No product support!");
		}
		return new FobbinInfo(false,"");
	}
	
	@Service(name="pay")
	@Params(validateField={"login_name","amount","bank"})
	public Result pay(Task task,InterFace inter){
		
		String product = task.getParam("product");
		
		String pre=ThPayResource.instance().getConfig("huitong."+product+".pre");
		String order_no = pre + String.valueOf(System.currentTimeMillis());
		
		String merchant_code =ThPayResource.instance().getConfig("huitong."+product+".merId"); 
		String key =ThPayResource.instance().getConfig("huitong."+product+".md5key");
		String notify_url =ThPayResource.instance().getConfig("huitong."+product+".notify_url");
		String submit_url =ThPayResource.instance().getConfig("huitong."+product+".submit_url");
		
		String return_url = task.getParam("return_url");  //客户访问用的网址
		
		String bank_code = task.getParam("bank");	//支付方式
		String order_amount = task.getParam("amount");
		String login_name = task.getParam("login_name");
		String customer_ip = task.getParam("ip");	//支付ip
		try{
			
	
			  String order_time=DateUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss");
			  String pay_type="1";
			  String req_referer="https://api.huitongvip.com/";
			  
			  String bank_name =getBankName(bank_code);
			  
			  Map<String, String> map = new HashMap<String,String>();
				map.put("notify_url", notify_url); // 异步通知地址
				map.put("pay_type", pay_type);
				map.put("bank_code", bank_code);
				map.put("merchant_code", merchant_code);
				map.put("order_no", order_no);
				map.put("order_amount", order_amount);
				map.put("order_time", order_time);
				map.put("customer_ip", "127.0.0.1");
				map.put("req_referer", req_referer);// 来路域名
				String sign = getSign(map, key);
				submit_url += "?";
				for (String key1 : map.keySet()) {
					submit_url += key1 + "=" + map.get(key1) + "&";
				}
				submit_url = submit_url+"sign="+sign;
				JdbcTemplate template=JdbcResource.instance().getJdbcTemplate(inter.getDataSource());
				String ds=inter.getDataSource();
				HuiTongService service = new HuiTongService(template,ds);
				service.createCfp(order_no, login_name, order_amount, customer_ip, bank_code, product, return_url);
				
				Result r =Result.create(task.getId(), task.getFunId());
				 r.addFields(new String[]{"ok","order_no","message","order_amount","submit_url","bank_name"});
				 r.setFlag("-1");
				 r.setIsList(true);
				 r.setLength(1);
				 r.addRecord(new String[]{"1",order_no,"",order_amount,submit_url,bank_name});
				 return r;
				 
				
			  
		}catch(Exception e){
			
		}
		Result r =Result.create(task.getId(), task.getFunId());
		 r.addFields(new String[]{"ok","order_no","message","order_amount"});
		 r.setFlag("-1");
		 r.setIsList(true);
		 r.setLength(1);
		 r.addRecord(new String[]{"0",order_no,"无法创建订单!",order_amount});
		 return r;
		 
	}
	
	@Service(name="notify")
	@Params(validateField={"merchant_code"})
    public Result notify(Task task,InterFace inter){
		
		String merchant_code = task.getParam("merchant_code");
	    String backsign = task.getParam("sign");
	    String order_no = task.getParam("order_no");
	    String order_amount = task.getParam("order_amount");
	    String order_time = task.getParam("order_time");
	    String trade_no = task.getParam("trade_no");
	    String trade_time = task.getParam("trade_time");
		String trade_status = task.getParam("trade_status");
		String product =task.getParam("product");
		
		//组织订单信息
		  String key = ThPayResource.instance().getConfig("huitong."+product+".md5key");
		  Map<String, String> map = new HashMap<String,String>();
			map.put("order_no",order_no);
			map.put("merchant_code", merchant_code);
			map.put("order_amount", order_amount);
			map.put("order_time", order_time);
			map.put("trade_no", trade_no);
			map.put("trade_time", trade_time);
			map.put("trade_status", trade_status);// 来路域名
			map.put("notify_type", "back_notify");
			String sign = getSign(map, key);
			BigDecimal money = new BigDecimal(task.getParam("order_amount"));
			try{
				boolean b=backsign.equals(sign);
				if(b){
					
					String ds=ThPayResource.instance().getConfig("xinbeipay."+product+".datasource");
					JdbcTemplate template=JdbcResource.instance().getJdbcTemplate(ds);
					HuiTongService service = new HuiTongService(template,ds);
					CreditService creditService =new CreditService(template,ds);
					CustomerService custService =new CustomerService(template,ds);
					DepositService depositService =new DepositService(template,ds);
					OrderNumberService orderNumberService =new OrderNumberService(template,ds);
					
					if(service.isNotDoYdp(order_no)){
						
						String payType = service.queryPayType(order_no);
						service.updaete(order_no, trade_no,"", "2", money, new BigDecimal(0.99));
						
						orderNumberService.createOrderNumber(order_no);
						
						String login_name = service.queryLoginname(order_no);
						
						
						if(creditService.add(login_name, money,"自动充值", "ht在线支付:"+payType, login_name, order_no)){
							  try{
									Customer customer= custService.getCustomer(login_name);
									//添加存款记录和日志
									String dep_no = OrderNoService.createLocalNo("DE");
									depositService.addDeposit2(dep_no, customer.cust_id, login_name, customer.real_name, money, "ht在线支付"+payType, "", "ht在线支付"+payType, "", order_no);
									//查询是否第一次存款,如果是,升级用户等级
									if(depositService.NTgetCount(customer.cust_id) == 1){
										custService.NTmodCustlevelFirst(customer.cust_id, 1);
									}
									
									if(customer.promo_flag != null && customer.promo_flag){
										ScoreService scoreService =new ScoreService(template,ds);
										if(scoreService.depositCountToday(login_name)==1){
											scoreService.modScore(order_no, "签到积分",  new BigDecimal(1), login_name, customer.cust_id, "");
										}
										scoreService.modScore(order_no, "存款积分",new BigDecimal( money.divide(new BigDecimal(100)).intValue()), login_name, customer.cust_id, "");
										
										Date now =new Date(System.currentTimeMillis());
										if(now.getTime()>DateUtil.stringToDate("2017-12-01", "yyyy-MM-dd").getTime() &&
									    		  DateUtil.stringToDate("2018-01-01", "yyyy-MM-dd").getTime()>now.getTime() && money.intValue()>=500){
									    	 
											
											 String giftCode=RandomUtil.getRandom(8);
											 CashGift gift =new CashGift();
								        	 gift.gift_code=giftCode;
								        	 gift.login_name=customer.login_name;
								        	 gift.deposit_credit=money;
								        	 gift.valid_credit=new BigDecimal(0);
								        	 gift.net_credit=new BigDecimal(0);
								        	 
								        	 float rate = 0;
								        	 float f = 0.0f;
								        	 if(money.intValue() >= 500 && money.intValue()<5000){
								        		 gift.rate=Float.valueOf(1);
									        	 rate = 1;
									        	 f=money.floatValue()*1/100;
											 }else if(money.intValue() >= 5000 && money.intValue()<10000){
												 gift.rate=Float.valueOf(1.8f);
									        	 rate = 1.8f;
									        	 f=money.floatValue()*1.8f/100;
											 }else if(money.intValue() >= 10000 && money.intValue()<30000){
												 gift.rate=Float.valueOf(2.5f);
									        	 rate = 2.5f;
									        	 f=money.floatValue()*2.5f/100;
											 }else if(money.intValue() >= 30000 && money.intValue()<50000){
												 gift.rate=Float.valueOf(3.8f);
									        	 rate = 3.8f;
									        	 f=money.floatValue()*3.8f/100;
											 }else if(money.intValue() >= 50000 ){
												 gift.rate=Float.valueOf(5);
									        	 rate = 5;
									        	 f=money.floatValue()*5/100;
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
			}catch(Exception e){
				
			}
		Result result =Result.create(task.getId(), task.getFunId());
		result.setFlag("-1");
		result.setIsList(true);
		result.setLength(1);
		result.addFields(new String[]{"ok","message"});
		result.addRecord(new String[]{"0",""});
		return result;
		
	}
	
	
	public static String getSign(Map<String, String> params, String key) {
		params.remove("sing");
		Map<String, String> treeMap = new TreeMap<>();
		for (String k : params.keySet()) {
			String v = params.get(k);
			if (v != null) {
				treeMap.put(k, v);
			}
		}
		StringBuffer sb = new StringBuffer();
		for (Entry<String, String> entry : treeMap.entrySet()) {
			if (entry.getValue() != null) {
				sb.append(entry.getKey() + "=" + entry.getValue() + "&");
			}
		}
		String signStr = sb.append("key=" + key).toString();
		System.out.println("signStr    "+signStr);
		return MD5Util.md5Encode(signStr).toLowerCase();
	}
	
	public static String getBankName(String bank_code){
		if(bank_code==null)return "";
		if("ABC".equals(bank_code)){
			return "农业银行";
		}
		if("BOC".equals(bank_code)){
			return "中国银行";
		}
		if("BOCOM".equals(bank_code)){
			return "交通银行";
		}
		if("CCB".equals(bank_code)){
			return "建设银行";
		}
		if("ICBC".equals(bank_code)){
			return "工商银行";
		}
		if("PSBC".equals(bank_code)){
			return "邮储银行";
		}
		if("CMBC".equals(bank_code)){
			return "招商银行";
		}
		if("SPDB".equals(bank_code)){
			return "浦发银行";
		}
		
		if("CEBBANK".equals(bank_code)){
			return "光大银行";
		}
		if("ECITIC".equals(bank_code)){
			return "中信银行";
		}
		if("PINGAN".equals(bank_code)){
			return "平安银行";
		}
		if("CMBCS".equals(bank_code)){
			return "民生银行";
		}
		if("HXB".equals(bank_code)){
			return "华夏银行";
		}
		if("CGB".equals(bank_code)){
			return "广发银行";
		}
		if("BCCB".equals(bank_code)){
			return "北京银行";
		}
		if("BOS".equals(bank_code)){
			return "上海银行";
		}
		
		if("CIB".equals(bank_code)){
			return "兴业银行";
		}
		
		if("BEA".equals(bank_code)){
			return "东亚银行";
		}
		if("NBB".equals(bank_code)){
			return "宁波银行";
		}
		
		if("HZB".equals(bank_code)){
			return "杭州银行";
		}
		
		return "";
		
	}
}
