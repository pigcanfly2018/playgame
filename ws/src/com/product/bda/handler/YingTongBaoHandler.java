package com.product.bda.handler;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONObject;

import org.apache.commons.codec.digest.DigestUtils;
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
import com.product.bda.service.ScoreService;
import com.product.bda.service.XunHuiBaoPayService;
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

@Handler(name="YTBP")
public class YingTongBaoHandler {

	protected static String BASE_URL = "http://api.ytbao.net/";
	
	private static Logger logger=Logger.getLogger(YingTongBaoHandler.class);
	
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
		
		String pre=ThPayResource.instance().getConfig("ytbpay."+product+".pre");
		String merchno =ThPayResource.instance().getConfig("ytbpay."+product+".merchno"); 
		String mer_key =ThPayResource.instance().getConfig("ytbpay."+product+".key");
		String notify_url = ThPayResource.instance().getConfig("ytbpay."+product+".notify_url"); //服务器通知返回接口
		
		
		String customer_ip = task.getParam("customer_ip");
		String return_url = task.getParam("return_url");  //客户访问用的网址
		String payType = task.getParam("payType");  //支付方式
		String show_amount = task.getParam("amount");
		String amount = task.getParam("amount");
		String remark = task.getParam("login_name");
		
		String order_no = pre+"_" + String.valueOf(System.currentTimeMillis());
		
		Map<String, String> param = buildRequestParam(false);
		
		param.put("merchno", merchno);
		param.put("amount", amount);
		param.put("traceno", order_no);
		param.put("payType", payType);
		param.put("goodsName", "电器");
		
		param.put("notifyUrl",notify_url);
		param.put("remark", remark);
		// T0参数,T1不需要上传
		param.put("settleType", "1");
		
		Map<String, String> reqParam = new HashMap<String, String>();
		Set<String> keySet = param.keySet();
		Iterator<String> iter = keySet.iterator();
		while (iter.hasNext()) {
			String key = iter.next();
			reqParam.put(key, param.get(key));
		}
		
		try {
			reqParam.put("signature",signature(reqParam, mer_key));
			String url = buildUrl("passivePay", reqParam);
			String result = receiveBySend(url, "GBK");
			JSONObject jsresult = JSONObject.fromObject(result);
			logger.info("jsresult             "+jsresult);
			if(jsresult != null && !jsresult.getString("barCode").equals("")){
				String ds=inter.getDataSource();
				JdbcTemplate template=JdbcResource.instance().getJdbcTemplate(ds);
				YingTongBaoPayService service = new YingTongBaoPayService(template,ds);
				
				String barCode = jsresult.getString("barCode");
				
				if(payType.equals("1")){
					service.createYtbp(order_no, remark, amount, customer_ip, "支付宝", "", return_url);
				}else if(payType.equals("2")){
					service.createYtbp(order_no, remark, amount, customer_ip, "微信", "", return_url);
				}else if(payType.equals("8")){
					service.createYtbp(order_no, remark, amount, customer_ip, "QQ扫码", "", return_url);
				}else if(payType.equals("32")){
					service.createYtbp(order_no, remark, amount, customer_ip, "银联钱包", "", return_url);
				}
				
				
				
				Result r =Result.create(task.getId(), task.getFunId());
				r.addFields(new String[]{"ok","pay_id","message","amount","barCode"});
				r.setFlag("-1");
				r.setIsList(true);
				r.setLength(1);
				r.addRecord(new String[]{"1",order_no,"",amount,barCode});
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
		r.addRecord(new String[]{"0",order_no,"无法创建订单",show_amount,""});
		return r;
	}
	
	
	@Service(name="wangyinpay")
	@Params(validateField={"login_name","amount"})
	public Result wangyinpay(Task task,InterFace inter){
		
		String product = task.getParam("product");
		
		String pre=ThPayResource.instance().getConfig("ytbpay."+product+".pre");
		String merchno =ThPayResource.instance().getConfig("ytbpay."+product+".merchno"); 
		String mer_key =ThPayResource.instance().getConfig("ytbpay."+product+".key");
		String notify_url = ThPayResource.instance().getConfig("ytbpay."+product+".notify_url"); //服务器通知返回接口
		
		
		String customer_ip = task.getParam("customer_ip");
		String return_url = task.getParam("return_url");  //客户访问用的网址
		String bank = task.getParam("bank");  //支付方式
		
		String amount = task.getParam("amount");
		String remark = task.getParam("login_name");
		
		String order_no = pre+"_" + String.valueOf(System.currentTimeMillis());
		
		Map<String, String> param = buildRequestParam(false);
		
		param.put("merchno", merchno);
		param.put("amount", amount);
		param.put("traceno", order_no);
		//param.put("payType", "2");
		param.put("bankCode", bank);
		param.put("settleType", "2");
		param.put("channel", "2");
		
		param.put("notifyUrl",notify_url);
		param.put("returnUrl", "http://www.baidu.com");
		// T0参数,T1不需要上传
		
		
		Map<String, String> reqParam = new HashMap<String, String>();
		Set<String> keySet = param.keySet();
		Iterator<String> iter = keySet.iterator();
		while (iter.hasNext()) {
			String key = iter.next();
			reqParam.put(key, param.get(key));
		}
		
		try {
			//System.out.println(reqParam);
			reqParam.put("signature",signature(reqParam, mer_key));
			
			String url = buildWangyinUrl("gateway.do?m=order&", reqParam);
			
			String result = receiveBySend(url, "GBK");
			
			System.out.println("result       "+result);
			
//			String prestr ="amount="+amount+"&bankCode="+bank+"&channel="+2+"&merchno="+merchno
//					+"&notifyUrl="+notify_url+"&returnUrl="+"http://www.baidu.com"+"&settleType="+2+"&traceno="+order_no+"&"+mer_key;
//			
//			System.out.println(prestr);
			
					 
			//System.out.println(prestr);
			//String sign_value = MD5Util.md5Encode(prestr);
			//String signature = signature(reqParam, mer_key);
			
				String ds=inter.getDataSource();
				JdbcTemplate template=JdbcResource.instance().getJdbcTemplate(ds);
				YingTongBaoPayService service = new YingTongBaoPayService(template,ds);
				
				//if(payType.equals("1")){
					service.createYtbp(order_no, remark, amount, customer_ip, "网银"+bank, "", return_url);
				
					//service.createYtbp(order_no, remark, amount, customer_ip, "微信", "", return_url);
				
				
				//String barCode = jsresult.getString("barCode");
				
					Result r =Result.create(task.getId(), task.getFunId());
					 r.addFields(new String[]{"ok","result"});
					 r.setFlag("-1");
					 r.setIsList(true);
					 r.setLength(1);
					 r.addRecord(new String[]{"1",result});
					 return r;
				
		
		}catch(Exception e){
			e.printStackTrace();
		}
		
		Result r =Result.create(task.getId(), task.getFunId());
		 r.addFields(new String[]{"ok","result"});
		 r.setFlag("-1");
		 r.setIsList(true);
		 r.setLength(1);
		 r.addRecord(new String[]{"0",""});
		 return r;
	}
	
	@Service(name="notify")
	@Params(validateField={"amount","status"})
	public Result notify(Task task,InterFace inter){
		String product = task.getParam("product");
		
		
		String transDate = task.getParam("transDate");
	    String transTime = task.getParam("transTime");
	    String merchno = task.getParam("merchno");
	    String merchName = task.getParam("merchName");
	    String signature = task.getParam("signature");
	    String traceno = task.getParam("traceno");
	    
	    String payType = task.getParam("payType");
	    String orderno = task.getParam("orderno");
	    String channelOrderno = task.getParam("channelOrderno");
	    String channelTraceno = task.getParam("channelTraceno");
	    String status = task.getParam("status");
	    
	    BigDecimal amount = new BigDecimal(task.getParam("amount"));
	    if(status.equals("1")){
	    	String ds=ThPayResource.instance().getConfig("ytbpay."+product+".datasource");
	    	JdbcTemplate template=JdbcResource.instance().getJdbcTemplate(ds);
	    	OrderNumberService orderNumberService =new OrderNumberService(template,ds);
	    	CreditService creditService =new CreditService(template,ds);
			CustomerService custService =new CustomerService(template,ds);
			DepositService depositService =new DepositService(template,ds);
			YingTongBaoPayService ytbService = new YingTongBaoPayService(template,ds);
			//没有处理
			if(ytbService.isNotDoYdp(traceno)){
				String login_name = ytbService.queryLoginname(traceno);
				if(payType.equals("1")){//支付宝
					ytbService.updaete(traceno, orderno,orderno, "2", amount, new BigDecimal(0.988));
					payType ="支付宝";
				}else if(payType.equals("2")){//2是微信
					payType ="微信";
					ytbService.updaete(traceno, orderno,orderno, "2", amount, new BigDecimal(0.99));
				}else if(payType.equals("8")){//8qq
					payType ="QQ";
					ytbService.updaete(traceno, orderno,orderno, "2", amount, new BigDecimal(0.982));
				}else if(payType.equals("32")){//8qq
					payType ="银联钱包";
					ytbService.updaete(traceno, orderno,orderno, "2", amount, new BigDecimal(0.982));
				}
				
				orderNumberService.createOrderNumber(traceno);
				//payType = payType.equals("1")?"支付宝":"微信";
				if(creditService.add(login_name, amount,"自动充值", "yingtongbao在线支付:"+payType, login_name, traceno)){
					  try{
							Customer customer= custService.getCustomer(login_name);
							//添加存款记录和日志
							String dep_no = OrderNoService.createLocalNo("DE");
							depositService.addDeposit2(dep_no, customer.cust_id, login_name, customer.real_name, amount, "ytb在线支付"+payType, "", "ytb在线支付"+payType, "", traceno);
							//查询是否第一次存款,如果是,升级用户等级
							
							if(depositService.NTgetCount(customer.cust_id) == 1){
								if(amount.intValue() >=100){
									custService.NTmodCustlevelFirst(customer.cust_id, 1);
								}else{
									custService.NTmodFirstDepositDateOnly(customer.cust_id, 1);
								}
								
							}else{ 
								if(customer.cust_level == 0){
									if(amount.intValue() >=100){
										custService.NTmodCustlevelOnly(customer.cust_id, 1);
									}
								}
							}
							
							if(customer.promo_flag != null && customer.promo_flag){
								ScoreService scoreService =new ScoreService(template,ds);
								if(scoreService.depositCountToday(login_name)==1 && amount.intValue() >=100){
									scoreService.modScore(traceno, "签到积分",  new BigDecimal(1), login_name, customer.cust_id, "");
								}
								if(amount.intValue() >=100){
									scoreService.modScore(traceno, "存款积分",new BigDecimal( amount.divide(new BigDecimal(100)).intValue()), login_name, customer.cust_id, "");
								}
								
							
								Date now =new Date(System.currentTimeMillis());
								if(now.getTime()>DateUtil.stringToDate("2017-12-01", "yyyy-MM-dd").getTime() &&
							    		  DateUtil.stringToDate("2018-01-01", "yyyy-MM-dd").getTime()>now.getTime() && amount.intValue()>=500 ){
							    	 // int dcount=depositService.NTgetCountToday(customer.cust_id);
							    	  //if(dcount<=5){
							    		  String giftCode=RandomUtil.getRandom(8);
											 CashGift gift =new CashGift();
								        	 gift.gift_code=giftCode;
								        	 gift.login_name=customer.login_name;
								        	 gift.deposit_credit=amount;
								        	 gift.valid_credit=new BigDecimal(0);
								        	 gift.net_credit=new BigDecimal(0);
								        	 
								        	 float rate = 0;
								        	 float f = 0.0f;
								        	 if(amount.intValue() >= 500 && amount.intValue()<5000){
								        		 gift.rate=Float.valueOf(1);
									        	 rate = 1;
									        	 f=amount.floatValue()*1/100;
											 }else if(amount.intValue() >= 5000 && amount.intValue()<10000){
												 gift.rate=Float.valueOf(1.8f);
									        	 rate = 1.8f;
									        	 f=amount.floatValue()*1.8f/100;
											 }else if(amount.intValue() >= 10000 && amount.intValue()<30000){
												 gift.rate=Float.valueOf(2.5f);
									        	 rate = 2.5f;
									        	 f=amount.floatValue()*2.5f/100;
											 }else if(amount.intValue() >= 30000 && amount.intValue()<50000){
												 gift.rate=Float.valueOf(3.8f);
									        	 rate = 3.8f;
									        	 f=amount.floatValue()*3.8f/100;
											 }else if(amount.intValue() >= 50000 ){
												 gift.rate=Float.valueOf(5);
									        	 rate = 5;
									        	 f=amount.floatValue()*5/100;
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
		
		 Result r =Result.create(task.getId(), task.getFunId());
		 r.addFields(new String[]{"ok"});
		 r.setFlag("-1");
		 r.setIsList(true);
		 r.setLength(1);
		 r.addRecord(new String[]{"0"});
		 return r;
	}
	
	
	@Service(name="wangyinnotify")
	@Params(validateField={"amount","status"})
	public Result wangyinnotify(Task task,InterFace inter){
		String product = task.getParam("product");
		
		
		
	    String merchno = task.getParam("merchno");
	    String signature = task.getParam("signature");
	    String traceno = task.getParam("traceno");
	    
	    String orderno = task.getParam("orderno");
	    String channelOrderno = task.getParam("channelOrderno");
	    String status = task.getParam("status");
	    
	    BigDecimal amount = new BigDecimal(task.getParam("amount"));
	    if(status.equals("2")){
	    	String ds=ThPayResource.instance().getConfig("ytbpay."+product+".datasource");
	    	JdbcTemplate template=JdbcResource.instance().getJdbcTemplate(ds);
	    	OrderNumberService orderNumberService =new OrderNumberService(template,ds);
	    	CreditService creditService =new CreditService(template,ds);
			CustomerService custService =new CustomerService(template,ds);
			DepositService depositService =new DepositService(template,ds);
			YingTongBaoPayService ytbService = new YingTongBaoPayService(template,ds);
			//没有处理
			if(ytbService.isNotDoYdp(traceno)){
				String login_name = ytbService.queryLoginname(traceno);
				
					//ytbService.updaete(traceno, orderno,orderno, "2", amount, new BigDecimal(0.99));
				
					ytbService.updaete(traceno, orderno,orderno, "2", amount, new BigDecimal(0.994));
				
				
				orderNumberService.createOrderNumber(traceno);
				//payType = payType.equals("1")?"支付宝":"微信";
				if(creditService.add(login_name, amount,"自动充值", "yingtongbao网银支付:", login_name, traceno)){
					  try{
							Customer customer= custService.getCustomer(login_name);
							//添加存款记录和日志
							String dep_no = OrderNoService.createLocalNo("DE");
							depositService.addDeposit2(dep_no, customer.cust_id, login_name, customer.real_name, amount, "ytb在线支付", "", "ytb在线支付", "", traceno);
							//查询是否第一次存款,如果是,升级用户等级
							if(depositService.NTgetCount(customer.cust_id) == 1){
								if(amount.intValue() >=100){
									custService.NTmodCustlevelFirst(customer.cust_id, 1);
								}else{
									custService.NTmodFirstDepositDateOnly(customer.cust_id, 1);
								}
								
							}else{ 
								if(customer.cust_level == 0){
									if(amount.intValue() >=100){
										custService.NTmodCustlevelOnly(customer.cust_id, 1);
									}
								}
							}
							
							
							if(customer.promo_flag != null && customer.promo_flag){
								ScoreService scoreService =new ScoreService(template,ds);
								if(scoreService.depositCountToday(login_name)==1){
									scoreService.modScore(traceno, "签到积分",  new BigDecimal(1), login_name, customer.cust_id, "");
								}
								scoreService.modScore(traceno, "存款积分",new BigDecimal( amount.divide(new BigDecimal(100)).intValue()), login_name, customer.cust_id, "");
							
								Date now =new Date(System.currentTimeMillis());
								if(now.getTime()>DateUtil.stringToDate("2017-05-27", "yyyy-MM-dd").getTime() &&
							    		  DateUtil.stringToDate("2017-06-01", "yyyy-MM-dd").getTime()>now.getTime()){
							    	 // int dcount=depositService.NTgetCountToday(customer.cust_id);
							    	  //if(dcount<=5){
							    		  String giftCode=RandomUtil.getRandom(8);
											 CashGift gift =new CashGift();
								        	 gift.gift_code=giftCode;
								        	 gift.login_name=customer.login_name;
								        	 gift.deposit_credit=amount;
								        	 gift.valid_credit=new BigDecimal(0);
								        	 gift.net_credit=new BigDecimal(0);
								        	 
								        	 gift.rate=Float.valueOf(2);
								        	 Integer rate = 2;
								        	 float f=amount.floatValue()*2/100;

								        	 
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
							
							
						}catch(Exception e){
							
							e.printStackTrace();
							
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
	
	private static String signature(Map<String, String> param, String keyValue)
			throws Exception {
		Set<String> set = param.keySet();
		List<String> keys = new ArrayList<String>(set);
		Collections.sort(keys);
		boolean start = true;
		StringBuffer sb = new StringBuffer();
		for (String key : keys) {
			String value = param.get(key);
			if (value != null && !value.trim().equals("")
					&& !"signature".equalsIgnoreCase(key)) {
				if (!start) {
					sb.append("&");
				}
				sb.append(key + "=" + value);
				start = false;
			}
		}
		sb.append("&" + keyValue);
		String src = sb.toString();
		System.out.println(src);
		String result = DigestUtils.md5Hex(src.getBytes("GBK")).toUpperCase();
		
		return result;
	}
	
	/**
	 * 构造一个空的请求数据
	 * 
	 * @return
	 */
	private static Map<String, String> buildRequestParam(boolean account) {
		Map<String, String> param = new HashMap<String, String>();
		return param;
	}
	
	/**
	 * 构造请求的地址信息
	 * 
	 * @param transType
	 * @param param
	 * @return
	 * @throws Exception
	 */
	private static String buildUrl(String transType, Map<String, String> param)
			throws Exception {
		StringBuffer url = new StringBuffer();
		url.append(BASE_URL);
		url.append(transType + "?");
		Set<String> set = param.keySet();
		Iterator<String> it = set.iterator();
		boolean isFirst = true;
		while (it.hasNext()) {
			String key = it.next();
			String value = URLEncoder.encode(param.get(key), "GBK");
			if (isFirst) {
				isFirst = false;
			} else {
				url.append("&");
			}
			url.append(key + "=" + value);
		}
		return url.toString();
	}
	
	private static String buildWangyinUrl(String transType, Map<String, String> param)
			throws Exception {
		StringBuffer url = new StringBuffer();
		url.append(BASE_URL);
		url.append(transType);
		Set<String> set = param.keySet();
		Iterator<String> it = set.iterator();
		boolean isFirst = true;
		while (it.hasNext()) {
			String key = it.next();
			String value = URLEncoder.encode(param.get(key), "GBK");
			if (isFirst) {
				isFirst = false;
			} else {
				url.append("&");
			}
			url.append(key + "=" + value);
		}
		return url.toString();
	}
	
	/**
	 * 发送请求到服务器，并接收返回信息
	 * 
	 * @param urlStr
	 * @return
	 * @throws Exception
	 */
	protected static String receiveBySend(String urlStr, String encoding)
			throws Exception {
		
		InputStream is = null;
		BufferedReader br = null;
		InputStreamReader isr = null;
		HttpURLConnection conn = null;
		try {
			StringBuffer sb = new StringBuffer();
			URL url = new URL(urlStr);
			conn = (HttpURLConnection) url.openConnection();
			is = conn.getInputStream();
			isr = new InputStreamReader(is, encoding);
			br = new BufferedReader(isr);
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			br.close();
			String xml = sb.toString();
			return xml;
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				br.close();
				isr.close();
				is.close();
				conn.disconnect();
			} catch (Exception e) {
			}
		}
	}
	
	public static String sendPost(String url,String param) {
        PrintWriter out = null;
        BufferedReader in = null;
     /*   List<Result> list=null;*/
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null){
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
        }
        return result;
    }  
	
public static void main(String[] args){
		
		Map<String, String> param = buildRequestParam(false);
		param.put("merchno", "900410479940001");
		param.put("amount", "11111.03");
		param.put("traceno", "678110154110003");
		param.put("payType", "2");
		param.put("goodsName", "测试商品");
//		param.put("notifyUrl",
//				"http://www.baidu.com/Test/servlet/Result");
		param.put("remark", "remark");
		// T0参数,T1不需要上传
		param.put("settleType", "1");
		
		Map<String, String> reqParam = new HashMap<String, String>();
		Set<String> keySet = param.keySet();
		Iterator<String> iter = keySet.iterator();
		while (iter.hasNext()) {
			String key = iter.next();
			reqParam.put(key, param.get(key));
		}
		try {
			reqParam.put("signature",
					signature(reqParam, "C5D4481CDED823DAE5E9A4954CC88D76"));
			String url = buildUrl("passivePay", reqParam);
			String result = receiveBySend(url, "GBK");
			System.out.println(result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
