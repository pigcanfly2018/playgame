package com.product.bda.handler;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.product.bda.service.GaoTongPayService;
import com.product.bda.service.HuiTongService;
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
import bsz.exch.utils.Base64;
import bsz.exch.utils.DateUtil;
import bsz.exch.utils.MD5Util;
import bsz.exch.utils.RandomUtil;
import bsz.exch.utils.RefundResponseEntity;
import bsz.exch.utils.SSLClient;
import bsz.exch.utils.SignUtil;
import net.sf.json.JSONObject;

@Handler(name="XingHeYiTongPay")
public class XingHeYiTongHandler {

	@Before
	public FobbinInfo before(Task task,InterFace inter){
		String product=task.getParam("product");
		if(!"8da".equals(product)){
			return  new FobbinInfo(true,"No product support!");
		}
		return new FobbinInfo(false,"");
	}
	
	
	@Service(name="pay")
	@Params(validateField={"login_name","amount"})
    public Result pay(Task task,InterFace inter){
		
		
		String product = task.getParam("product");
		String pre=ThPayResource.instance().getConfig("xingheyitongpay."+product+".pre");
		
		String service = ThPayResource.instance().getConfig("xingheyitongpay."+product+".service"); //服务器跳转返回接口
		String version =ThPayResource.instance().getConfig("xingheyitongpay."+product+".version"); 
		String merId =ThPayResource.instance().getConfig("xingheyitongpay."+product+".merId"); 
		String mer_key =ThPayResource.instance().getConfig("xingheyitongpay."+product+".mer_key"); 
		String typeId = task.getParam("bank");	//支付方式
		String tradeNo = pre+"_" + String.valueOf(System.currentTimeMillis());
		String tradeDate = DateUtil.dateToString( new java.util.Date(), "yyyyMMdd");
		String amount = task.getParam("amount");
		String notifyUrl = ThPayResource.instance().getConfig("xingheyitongpay."+product+".notifyUrl"); //服务器通知返回接口
		String extra = task.getParam("login_name");
		String summary =ThPayResource.instance().getConfig("xingheyitongpay."+product+".summary"); 
		String clientIp = task.getParam("ip");	//支付ip
		
		String submit_value = "";
		String hrefbackurl="";

		JdbcTemplate template=JdbcResource.instance().getJdbcTemplate(inter.getDataSource());
		String ds=inter.getDataSource();
		
		
		GaoTongPayService gtpService = new GaoTongPayService(template,ds);
		PayOnlineService poService= new PayOnlineService(template,ds);
		PayOnline online = poService.getPayOnline("xingheyitongpay", product);
		if(online != null){
			
			submit_value = online.submit_value;
			hrefbackurl = online.return_value;
			notifyUrl = online.notify_value;
			
			
			
		}
		String paymethod="";
		if(typeId.equals("2")){
			paymethod="xingheyitong微信";
		}else if(typeId.equals("3")){
			paymethod="xingheyitongQQ";
		}else if(typeId.equals("1")){
			paymethod="xingheyitong支付宝";
		}
		gtpService.createYbp(tradeNo, task.getParam("login_name"), amount, clientIp, paymethod, "",submit_value);
		
		//String directurl="";
		String sign = "";
		try{
			//拼接字符串
			String tempStr="service="+service+"&version="+version+"&merId="+merId+
					"&typeId="+typeId+"&tradeNo="+tradeNo+"&tradeDate="+tradeDate+"&amount="+amount+"&notifyUrl="+notifyUrl+
					"&extra="+extra+"&summary="+summary+"&clientIp="+clientIp;
			sign = MD5Util.MD5(tempStr+mer_key);
			
				// directurl = submit_value+"?"+tempStr+"&sign="+sign;
				// System.out.println(directurl);
			    Result r =Result.create(task.getId(), task.getFunId());
				r.addFields(new String[]{"ok","message", "service","version","merId","typeId",
						"tradeNo","tradeDate", "amount","notifyUrl","summary","clientIp","sign","submit_value","extra"});
				r.setFlag("-1");
				r.setIsList(true);
				r.setLength(1);
				r.addRecord(new String[]{"1","订单创建成功",service,version,merId,typeId,
						tradeNo,tradeDate,amount,notifyUrl,summary,clientIp,sign,submit_value,extra});
			return r;
		 }catch(Exception e){
			e.printStackTrace();
		 }
		
		 Result r =Result.create(task.getId(), task.getFunId());
		 r.addFields(new String[]{"ok","message", "service","version","merId","typeId",
					"tradeNo","tradeDate", "amount","notifyUrl","summary","clientIp","sign","submit_value","extra"});
		 r.setFlag("-1");
		 r.setIsList(true);
		 r.setLength(1);
		 r.addRecord(new String[]{"0","无法创建订单!",service,version,merId,typeId,
					tradeNo,tradeNo,tradeDate,amount,notifyUrl,summary,clientIp,sign,submit_value,extra});
		 return r;
   }
	
	
	
	
	@Service(name="notify")
	@Params(validateField={"tradeNo","amount","status","sign"})
	public Result notify(Task task,InterFace inter){
		String product = task.getParam("product");
		
		String service = task.getParam("service");
	    String merId = task.getParam("merId");
	    String tradeNo = task.getParam("tradeNo");
	    String tradeDate = task.getParam("tradeDate");
	    String opeNo = task.getParam("opeNo");
	    String opeDate = task.getParam("opeDate");
	    BigDecimal amount = new BigDecimal(task.getParam("amount"));
	    String status = task.getParam("status");
	    String attach = task.getParam("extra");
	    String payTime = task.getParam("payTime");
	    String sign = task.getParam("sign");
	    
	    if(status.equals("1")){
	    	String ds=ThPayResource.instance().getConfig("jianyue."+product+".datasource");
			String mer_key =ThPayResource.instance().getConfig("jianyue."+product+".mer_key");
			//service=%s&merId=%s&tradeNo=%s&tradeDate=%s&opeNo=%s&opeDate=%s&amount=%s&status=%s&extra=%s&payTime=%s
			//校验MD5码是不是正确的
			String tempStr="service="+service+"&merId="+merId+
					"&tradeNo="+tradeNo+"&tradeDate="+tradeDate+"&opeNo="+opeNo+"&opeDate="+opeDate
					+"&amount="+amount+"&status="+status+"&extra="+attach+"&payTime="+payTime;
			System.out.println("加密前参数："+tempStr);
			String newMD5 = "";
			try {
				newMD5 = SignUtil.signByMD5(tempStr, mer_key);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			JdbcTemplate template=JdbcResource.instance().getJdbcTemplate(ds);
			GaoTongPayService gtpService = new GaoTongPayService(template,ds);
			CreditService creditService =new CreditService(template,ds);
			CustomerService custService =new CustomerService(template,ds);
			DepositService depositService =new DepositService(template,ds);
			OrderNumberService orderNumberService =new OrderNumberService(template,ds);
			if(newMD5.equals(sign) ){
				//没有处理
				if(gtpService.isNotDoYdp(tradeNo,attach)){
					//ybpService.updaeteYdp(ordernumber, sysnumber, "2", paymoney);
					String payType = gtpService.queryPayType(tradeNo);
					
					gtpService.updaeteWeixinYdp(tradeNo, opeNo, "2", amount);
					
					orderNumberService.createOrderNumber(tradeNo);
					
					String login_name=attach;
			
					//支付成功
					
						if(creditService.add(login_name, amount,"自动充值", "剑跃在线支付:"+payType, login_name, tradeNo)){
							  try{
									Customer customer= custService.getCustomer(login_name);
									//添加存款记录和日志
									String dep_no = OrderNoService.createLocalNo("DE");
									depositService.addDeposit2(dep_no, customer.cust_id, login_name, customer.real_name, amount, "剑跃在线支付"+payType, "", "剑跃在线支付"+payType, "", tradeNo);
									//查询是否第一次存款,如果是,升级用户等级
									/*if(depositService.NTgetCount(customer.cust_id) == 1){
										custService.NTmodCustlevelFirst(customer.cust_id, 1);
									}*/
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
										if(scoreService.depositCountToday(login_name)==1 && amount.intValue() >=100 ){
											scoreService.modScore(tradeNo, "签到积分",  new BigDecimal(1), login_name, customer.cust_id, "");
										}
										scoreService.modScore(tradeNo, "存款积分",new BigDecimal( amount.divide(new BigDecimal(100)).intValue()), login_name, customer.cust_id, "");
										
										Date now =new Date(System.currentTimeMillis());
										if(now.getTime()>DateUtil.stringToDate("2017-12-01", "yyyy-MM-dd").getTime() &&
									    		  DateUtil.stringToDate("2018-01-01", "yyyy-MM-dd").getTime()>now.getTime() && amount.intValue()>=500){
											
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
	
	@Service(name="scanpay")
	@Params(validateField={"login_name","amount"})
    public Result scanpay(Task task,InterFace inter){
		
		
		String product = task.getParam("product");
		String BASE_URL = "http://gate.666666pay.cn/cooperate/gateway.cgi";
		String pre=ThPayResource.instance().getConfig("jianyue."+product+".pre");
		
		String scanservice = ThPayResource.instance().getConfig("jianyue."+product+".service"); 
		String version =ThPayResource.instance().getConfig("jianyue."+product+".version"); 
		String merId =ThPayResource.instance().getConfig("jianyue."+product+".merId"); 
		String mer_key =ThPayResource.instance().getConfig("jianyue."+product+".mer_key"); 
		String typeId = task.getParam("bank");	//支付方式
		String tradeNo = pre+"_" + String.valueOf(System.currentTimeMillis());
		String tradeDate = DateUtil.dateToString( new java.util.Date(), "yyyyMMdd");
		String amount = task.getParam("amount");
		String notifyUrl = ThPayResource.instance().getConfig("jianyue."+product+".notifyUrl"); //服务器通知返回接口
		String extra = task.getParam("login_name");
		String summary =ThPayResource.instance().getConfig("jianyue."+product+".summary"); 
		String expireTime = "600";
		String clientIp = task.getParam("ip");	//支付ip
		
		String submit_value = "";
		String hrefbackurl="";

		JdbcTemplate template=JdbcResource.instance().getJdbcTemplate(inter.getDataSource());
		String ds=inter.getDataSource();
		
		
		GaoTongPayService gtpService = new GaoTongPayService(template,ds);
		PayOnlineService poService= new PayOnlineService(template,ds);
		/*PayOnline online = poService.getPayOnline("xingheyitongpay", product);
		if(online != null){
			
			submit_value = online.submit_value;
			hrefbackurl = online.return_value;
			notifyUrl = online.notify_value;
			
			
			
		}*/
		String paymethod="";
		String barCode = "";
		
		String sign = "";
		try{
			//拼接字符串
			String tempStr="service="+scanservice+"&version="+version+"&merId="+merId+
					"&typeId="+typeId+"&tradeNo="+tradeNo+"&tradeDate="+tradeDate+"&amount="+amount+"&notifyUrl="+notifyUrl+
					"&extra="+extra+"&summary="+summary+"&expireTime="+expireTime+"&clientIp="+clientIp;
			//sign = MD5Util.MD5(tempStr+mer_key);
			
			sign = SignUtil.signByMD5(tempStr, mer_key);
			//发送请求
			DefaultHttpClient httpClient = new SSLClient();
			HttpPost postMethod = new HttpPost(BASE_URL);
			List<BasicNameValuePair> nvps = new ArrayList<BasicNameValuePair>();
			
			nvps.add(new BasicNameValuePair("service", scanservice));
			nvps.add(new BasicNameValuePair("version", version));
			nvps.add(new BasicNameValuePair("merId", merId));
			nvps.add(new BasicNameValuePair("typeId", typeId));
			nvps.add(new BasicNameValuePair("tradeNo", tradeNo));
			nvps.add(new BasicNameValuePair("tradeDate", tradeDate));
			nvps.add(new BasicNameValuePair("amount", amount));
			
			nvps.add(new BasicNameValuePair("notifyUrl",notifyUrl));
			nvps.add(new BasicNameValuePair("extra", extra));
			nvps.add(new BasicNameValuePair("summary", summary));
			nvps.add(new BasicNameValuePair("expireTime", expireTime));
			nvps.add(new BasicNameValuePair("clientIp", clientIp));
			
			nvps.add(new BasicNameValuePair("sign", sign));
			
			postMethod.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
            HttpResponse resp = httpClient.execute(postMethod);
            String str = EntityUtils.toString(resp.getEntity(), "UTF-8");
            int statusCode = resp.getStatusLine().getStatusCode();
           
            if (200 == statusCode) {
            	RefundResponseEntity entity = new RefundResponseEntity();
                entity.parse(str,mer_key);

                if (entity.getRespCode().equals("00")) {
                    barCode = entity.getQrCode();
                    barCode = Base64.decodeToString(barCode);
                    
            		if(typeId.equals("3")){ 
            			paymethod="剑跃PCQQ";
            		}else if(typeId.equals("4")){
            			paymethod="剑跃PC银联";
            		}
            		gtpService.createYbp(tradeNo, task.getParam("login_name"), amount, clientIp, paymethod, "",submit_value);
            		
            		
            		Result r =Result.create(task.getId(), task.getFunId());
    				r.addFields(new String[]{"ok","pay_id","message","amount","barCode"});
    				r.setFlag("-1");
    				r.setIsList(true);
    				r.setLength(1);
    				r.addRecord(new String[]{"1",tradeNo,"订单创建成功",amount,barCode});
    				return r;
                }
            	
			
            }
			    
		 }catch(Exception e){
			e.printStackTrace();
		 }
		System.out.println("订单创建失败");
		Result r =Result.create(task.getId(), task.getFunId());
		r.addFields(new String[]{"ok","pay_id","message","amount","barCode"});
		r.setFlag("-1");
		r.setIsList(true);
		r.setLength(1);
		r.addRecord(new String[]{"1",tradeNo,"无法创建订单",amount,barCode});
		return r;
   }
	
	
	@Service(name="jianyuewangyinpay")
	@Params(validateField={"login_name","amount","bank"})
	public Result jianyuewangyinpay(Task task,InterFace inter){
		
		String product = task.getParam("product");
		String BASE_URL = "http://gate.666666pay.cn/cooperate/gateway.cgi";
		String pre=ThPayResource.instance().getConfig("jianyue."+product+".pre");
		
		String service = ThPayResource.instance().getConfig("jianyue."+product+".wangyinservice"); 
		String version =ThPayResource.instance().getConfig("jianyue."+product+".version"); 
		String merId =ThPayResource.instance().getConfig("jianyue."+product+".merId"); 
		String mer_key =ThPayResource.instance().getConfig("jianyue."+product+".mer_key"); 
		String typeId = task.getParam("bank");	//支付方式
		String tradeNo = pre+"_" + String.valueOf(System.currentTimeMillis());
		String tradeDate = DateUtil.dateToString( new java.util.Date(), "yyyyMMdd");
		String amount = task.getParam("amount");
		String notifyUrl = ThPayResource.instance().getConfig("jianyue."+product+".notifyUrl"); //服务器通知返回接口
		String extra = task.getParam("login_name");
		String summary =ThPayResource.instance().getConfig("jianyue."+product+".summary"); 
		String clientIp = "1.1.1.1";//task.getParam("ip");
		String expireTime = "1800";
		String sign = "";
		String submit_value = "";
		String hrefbackurl="";
		String paymethod = "";
		try{
			  
			String tempStr="service="+service+"&version="+version+"&merId="+merId+
				"&tradeNo="+tradeNo+"&tradeDate="+tradeDate+"&amount="+amount+"&notifyUrl="+notifyUrl+
					"&extra="+extra+"&summary="+summary+"&expireTime="+expireTime+"&clientIp="+clientIp+"&bankId="+typeId;
			System.out.println("加密前参数="+tempStr+mer_key);
			//sign = MD5Util.MD5(tempStr+mer_key);
			sign = SignUtil.signByMD5(tempStr, mer_key);
			System.out.println("加密参数sign="+sign);

				JdbcTemplate template=JdbcResource.instance().getJdbcTemplate(inter.getDataSource());
				String ds=inter.getDataSource();
				GaoTongPayService gtpService = new GaoTongPayService(template,ds);
				PayOnlineService poService= new PayOnlineService(template,ds);
				/*PayOnline online = poService.getPayOnline("xingheyitongpay", product);
				if(online != null){
					
					submit_value = online.submit_value;
					hrefbackurl = online.return_value;
					notifyUrl = online.notify_value;
					
				}*/
				
				 paymethod =getBankName(typeId);
				
				gtpService.createYbp(tradeNo, task.getParam("login_name"), amount, clientIp, "网银"+paymethod, "",submit_value);
				
				Result r =Result.create(task.getId(), task.getFunId());
				 r.addFields(new String[]{"ok","submit_url","service","version","merId","tradeNo","tradeDate",
						 "amount","notifyUrl","extra","summary","expireTime","clientIp","bankId","sign"});
				 r.setFlag("-1");
				 r.setIsList(true);
				 r.setLength(1);
				 r.addRecord(new String[]{"1",BASE_URL,service,version,merId,tradeNo,tradeDate,amount,
						 notifyUrl,extra,summary,expireTime,clientIp,typeId,sign});
				 return r;
				 
				
			  
		}catch(Exception e){
			
		}
		Result r =Result.create(task.getId(), task.getFunId());
		 r.addFields(new String[]{"ok","tradeNo","message","order_amount"});
		 r.setFlag("-1");
		 r.setIsList(true);
		 r.setLength(1);
		 r.addRecord(new String[]{"1",tradeNo,"无法创建订单",amount});
		 return r;
		 
	}
	
	@Service(name="H5pay")
	@Params(validateField={"login_name","amount"})
    public Result H5pay(Task task,InterFace inter){
		
		
		String product = task.getParam("product");
		String pre=ThPayResource.instance().getConfig("jianyue."+product+".pre");
		
		String service = ThPayResource.instance().getConfig("jianyue."+product+".h5service"); //服务器跳转返回接口
		String version =ThPayResource.instance().getConfig("jianyue."+product+".version"); 
		String merId =ThPayResource.instance().getConfig("jianyue."+product+".merId"); 
		String mer_key =ThPayResource.instance().getConfig("jianyue."+product+".mer_key"); 
		String typeId = task.getParam("bank");	//支付方式
		String tradeNo = pre+"_" + String.valueOf(System.currentTimeMillis());
		String tradeDate = DateUtil.dateToString( new java.util.Date(), "yyyyMMdd");
		String amount = task.getParam("amount");
		String notifyUrl = ThPayResource.instance().getConfig("jianyue."+product+".notifyUrl"); //服务器通知返回接口
		String extra = task.getParam("login_name");
		String summary =ThPayResource.instance().getConfig("jianyue."+product+".summary"); 
		String expireTime = "1800"; 
		String clientIp = "1.1.1.1";//task.getParam("ip");	//支付ip
		
		String submit_value = "http://gate.666666pay.cn/cooperate/gateway.cgi";
		String hrefbackurl="";

		JdbcTemplate template=JdbcResource.instance().getJdbcTemplate(inter.getDataSource());
		String ds=inter.getDataSource();
		
		
		GaoTongPayService gtpService = new GaoTongPayService(template,ds);
		String paymethod="";
		if(typeId.equals("2")){
			paymethod="jianyue微信";
		}else if(typeId.equals("3")){
			paymethod="h5QQ";
		}else if(typeId.equals("1")){
			paymethod="jianyue支付宝";
		}
		gtpService.createYbp(tradeNo, task.getParam("login_name"), amount, clientIp, paymethod, "",submit_value);
		
		//String directurl="";
		String sign = "";
		try{
			//拼接字符串
			/*String tempStr="service="+service+"&version="+version+"&merId="+merId+
					"&typeId="+typeId+"&tradeNo="+tradeNo+"&tradeDate="+tradeDate+"&amount="+amount+"&notifyUrl="+notifyUrl+
					"&extra="+extra+"&summary="+summary+"&clientIp="+clientIp;
			sign = MD5Util.MD5(tempStr+mer_key);*/
			
			//拼接字符串
			String tempStr="service="+service+"&version="+version+"&merId="+merId+
					"&typeId="+typeId+"&tradeNo="+tradeNo+"&tradeDate="+tradeDate+"&amount="+amount+"&notifyUrl="+notifyUrl+
					"&extra="+extra+"&summary="+summary+"&expireTime="+expireTime+"&clientIp="+clientIp;
				System.out.println("加密前参数="+tempStr+mer_key);
				//sign = MD5Util.MD5(tempStr+mer_key);
				sign = SignUtil.signByMD5(tempStr, mer_key);
				System.out.println("加密参数sign="+sign);
			
			
				// directurl = submit_value+"?"+tempStr+"&sign="+sign;
				// System.out.println(directurl);
			    Result r =Result.create(task.getId(), task.getFunId());
				r.addFields(new String[]{"ok","message", "service","version","merId","typeId",
						"tradeNo","tradeDate", "amount","notifyUrl","summary","expireTime","clientIp","sign","submit_value","extra"});
				r.setFlag("-1");
				r.setIsList(true);
				r.setLength(1);
				r.addRecord(new String[]{"1","订单创建成功",service,version,merId,typeId,
						tradeNo,tradeDate,amount,notifyUrl,summary,expireTime,clientIp,sign,submit_value,extra});
			return r;
		 }catch(Exception e){
			e.printStackTrace();
		 }
		
		 Result r =Result.create(task.getId(), task.getFunId());
		 r.addFields(new String[]{"ok","message", "service","version","merId","typeId",
					"tradeNo","tradeDate", "amount","notifyUrl","summary","clientIp","sign","submit_value","extra"});
		 r.setFlag("-1");
		 r.setIsList(true);
		 r.setLength(1);
		 r.addRecord(new String[]{"0","无法创建订单!",service,version,merId,typeId,
					tradeNo,tradeNo,tradeDate,amount,notifyUrl,summary,clientIp,sign,submit_value,extra});
		 return r;
   }
	
	
	
	
	
	private static String getBankName(String bank_code){
		if(bank_code==null)return "";
		if("ABC".equals(bank_code)){
			return "中国农业银行";
		}
		if("BOC".equals(bank_code)){
			return "中国银行";
		}
		if("CBHB".equals(bank_code)){
			return "渤海银行";
		}
		if("CCB".equals(bank_code)){
			return "建设银行";
		}
		if("CEB".equals(bank_code)){
			return "光大银行";
		}
		if("CIB".equals(bank_code)){
			return "兴业银行";
		}
		if("CMB".equals(bank_code)){
			return "招商银行";
		}
		if("CMBC".equals(bank_code)){
			return "民生银行";
		}
		if("CNCB".equals(bank_code)){
			return "中信银行";
		}
		if("COMM".equals(bank_code)){
			return "交通银行";
		}
		if("GDB".equals(bank_code)){
			return "广发银行";
		}
		if("HXB".equals(bank_code)){
			return "华夏银行";
		}
		if("ICBC".equals(bank_code)){
			return "工商银行";
		}
		if("PAB".equals(bank_code)){
			return "平安银行";
		}
		if("PSBC".equals(bank_code)){
			return "邮储银行";
		}
		if("SPDB".equals(bank_code)){
			return "浦发银行";
		}
		return "";
		
	}
	
	public static void main(String [] rags){
		System.out.println(DateUtil.dateToString( new java.util.Date(), "yyyyMMdd"));
	}
}
