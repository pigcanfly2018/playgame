package com.product.bda.handler;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.product.bda.service.CashGift;
import com.product.bda.service.CashGiftService;
import com.product.bda.service.CreditService;
import com.product.bda.service.Customer;
import com.product.bda.service.CustomerService;
import com.product.bda.service.DepositService;
import com.product.bda.service.HbpService;
import com.product.bda.service.OrderNoService;
import com.product.bda.service.OrderNumberService;
import com.product.bda.service.ScoreService;
import com.product.bda.service.ThpService;

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
import bsz.exch.utils.Base64Utils;
import bsz.exch.utils.Common;
import bsz.exch.utils.DateUtil;
import bsz.exch.utils.HUIBOBase64Utils;
import bsz.exch.utils.LocalUtil;
import bsz.exch.utils.MyURLConnection;
import bsz.exch.utils.RSAUtils;
import bsz.exch.utils.RandomUtil;

@Handler(name="HBP")
public class HbpayHandler {

	private String PRIVATEKEY = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQC6l6rwCCQqWDMYnA1kM6VcS2o6pPwY8h5+suQf32MbsjfsU6u0vOIjLfdJIqWhUjnN1H+3CQ1yOFcDsTcBIDufyZL3dYO+RZRgkUOkJCf8lfK905qlOaTrB8y+15OTHxJJ4ivVT5xFCQhPBpqprqp24YejwOIAKoogODIqfjcVAFAgeN4V84GPXwmoQsZgV3EgcdveDKUuNUr851MQD78+DFzzOAbJGAfqmdXXjQ6oqpBMXitR6Aed6TvjK8kwL1UZR/SNQfdAo2/p1j415Vz79FCC6OLb3qYskbL83kMfndUYpqkFKHzEKh44ELSBfTRSvvDsdayxR8P9ssjjSQTbAgMBAAECggEBAKMTPM0NgHYVoYqHkFibnpDcRFo+qSkwKiC3PhLxDFA/YQrFUZLjZYwzdyTPCAzGrUhLyNbyLXM+9CWA8z6jqhiLdkuZw+s+KmB/dKt3Ag+KCZ2H22c9yvmyVmoQRKTXCgfSiFakfkIDk+RlNDTKN6cwkng2cg6NHyzrkER9DKznmZKORYNRmznV3UHHEnppYxMUzovD+I2RiyfnEpBpdsk21ezltMr+titg5xZWk+awEHNFKZC1vQVx5BHPtZ3BQpVZ1OPlLQwuFyYeLJIXx7ky079OHJHNIAEqqToFd25/Olm+D3U+HkyFDrY8Ewz8GksWTCewhrekmfl2YLtUAmkCgYEA9Sfm/s1tSBtrXBUuie7wXJxT9DOwcuYQOlvyC5JZ96oXIYiG6QEeWfaVLIWm0Cb2B93c6B8UUUYEf3jsBHmkDVAD2n4EBbpjy1FuRKthNHtVPT9w8c83TQSNn7TYq3RwgJNe3P3E5DYJ8wigdhlQBqgnW4WSUiZ9r9l+UOJL/icCgYEAwtiaxnBrKeETEkmcvh+jWWp5zLiA+qJP7J7SF9yf2e+H6Wp4VAGtyn687Z4NZ6kjy9bbNAC2qWPPQfllH2W1h3cLCLbwBtsUSYmA3MUIsFpsKc/GpCkvFoznNHt1VAAfIHJPXhBv7ek2b0t6BG7EsGF5CbP5oeI4b9+R0o/W6C0CgYB0ROpvKiqMKkTutTUPQf1JTvMaPHGQ7MABjZ3sEwsMROUXiFLDqz4j2KjDJtgInJHPBVBB3ouaSCTV+BmnvbEqhypss5tgxBeUVBiCBO3jICM7Gx2L8YT+yAwLFJA2MNEyibJSCSEaQu7RDuRbXoHvxkx0QZM11v+BKRuZbNoxvQKBgGANDUdzlbBgbtleBnHHAOyMS5y6+4IST5oStPjkXry9kt9jCAxJg5XGInZ0qqEGRUowEYEyNNmOBVa9jbcLmZA9A+h43hxTFU4nBapPSzg8awO1lHPKhZb1QOijT7RoFvAUycFSEwtSIu58Y5sZy8YzVsqMuSuvbCjiKm2FUOjFAoGABQHvO9j6dDuc70rMSAXTawR2M72Ik8vVOjsGgI3v8X5au9nMOgelcUfM6DlQquGkVnECOFJ60ayr28Jy1Qe7wSgVHLmqRhiX/+a6mKIPYzsWVUFKVRGTmuGPAVJGrJiONQkvbkriUgI7PoV1kyCkTob6+c8HFD8gk0TBV0hxkE4=";
	
	private String PRIVATESAOMAKEY = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCrt1zBllMy4a5UfsS1YvKEUkvv9i6ObhsMtSohBIj/LHGzWP5jns0uQfsm/ArkUEJUhu0axPAXeNF3YApAD3OWUYXq8J0DO+OWCsXiI2ZucXzF9upnT5mmPlvoI+lydgh9ocvp8kDXd2NZw8S+xnwGdL0k6PKLPJxPRkxOzkKU2YhWC2tVXp14NjrOOOZy0zbOHcJoQe2qkP4oWlMEMkVLkKlvThLon/blOla3bijQtjNYoiaj1pVdOhogTRqShDfxAzFHYJTc8RWZQZdOnpCKXie5aFmFMtHcOXwMuPbZnBBgyXCyIi7UI8D3+rE7yjkZOiNsJtj5RFaFqOe6WAzFAgMBAAECggEAEMIob0w4enLEDMMCd6FcgcV2W8ju6j+crDTSE77Zj+3KevJogVhyNd2CoV9fwMCEeLNiWIZ32BKWfswEtBPN+BGkvDUXXcVuhKowfc1upcC1qKxcXBDMsgjL92/pzJ3wXjJ4MDDQWzbvn0cX4HaGjCrAeuWxJh2YM7RsphRDMzuOtghzTOxiNdWb/pmBf+oRsO171ZInoZXeFQlZHxu2MoRG2odPSln8ouyxKEZmuk6vvtODE8CzVhc/XHn20Z0viJpfXXyjJmvohNdYYjZ77KqmAjtslJlhW2DhvCI/o2vwa82hgxPQzAEsBPp/X7AqHMWMjKvtQYHilMJpkNqaAQKBgQDb8EbQr1GavAeDI1LMPXSXGGsRMVrr03+Am+sZFj4PugKIabwNOp63ioHYOd+EAo2GOt5+sLW5ZmcGlFO06A/89V31n5eFkWtYG2OwAKAnzwwzxgICIYbWf0EC9v7SLvTDxv6a7CQ0WbKqLpkw5JLr8oEqeZokH33DOJ61eEb5YQKBgQDH3v9OtMrsjXDD/KZg6nXGLIZ5YJvfRNZERtBSFhsPsh3YEF7goU5LzkJ/QJOlk9NrvCIJ9dvkf3JRW0Awh41R9z8XKIF4Mr6ePf4I3QIqfJ42rWDyC4nNYlMLiSjxDCveTVero4R3rwku4Cynekvh2MRyX+EAhUb4288vudCZ5QKBgQCUtI281X0QaOePZBe8XiTxcCIcGsnFv1drBoDbkm1dxr+lqg/qxeLAaFbIcsDMiclUo4MCF8qW00T/btR18Z21w9TGmSQivCLfDI3HV3Tx1Y0DiHKdLDv11U6IiaXbSMJXiLlm2BjWiHtDk4QiSbmI7Ismw9dTazNmBmdQfUVHoQKBgCtMFniBV7g4x6XdNFmoUM0WViczE3EhY101DenKWiBjUG2+1h7JMsHfM66HWbSzukgLYEBSH0l3hlczcktRAf8wlV057BoSvBJhkYdwAQkdJ9TmaCrm7vosA5trIhBdt58XmBdZe9fFdjLOYz4AkzGgmKXCOWa4eHBuhYpsMVE1AoGBAJRkY8tgUKc1O91Pv0tSAtlw1JiF2tvJgOf5y1meGDiuutiSARJvKiwBthLGI/2yzWALIUyL+bnqPdvMO71i4Hk6aRXsEYfq98AA1xXq4wbpmXFfkIBglkV1SG1kYJrfefTX0mJ1ER8j4NVZqhdms1sYpngDhdEoLDL35EtnqLsl";
	
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
		
		String order_no = "hbonline"+"_" + String.valueOf(System.currentTimeMillis());
		String amount = task.getParam("amount");
		String order_amount = (Integer.parseInt(amount)*100)+"";
		String tradeCode = "hb_onLinePay";
		String bank_code = task.getParam("bank");  //客户访问用的
		String customer_ip = task.getParam("customer_ip");  //客户访问用的IP
		String remark = task.getParam("remark");
		String url = task.getParam("return_url");  //客户访问用的网址
		
        JSONObject jsonObject = new JSONObject();
		 // 交易命令
        jsonObject.put("orderCode",tradeCode);
        // 账号
        jsonObject.put("account","13128688114");
     // 支付通道 UNIPAY
        jsonObject.put("channelCode","UNIPAY");
        
        JSONObject msgBody = new JSONObject();
        msgBody.put("orderId",order_no);
        msgBody.put("amount", order_amount);
        msgBody.put("rentunURL","http://www.baidu.com");
        msgBody.put("bankAbbr",bank_code);
        msgBody.put("cardType","1"); 
        
        String msgJson = msgBody.toJSONString();
        msgJson = Base64.encodeToString(msgJson);
        
      //签名
        byte[] sign = LocalUtil.sign(Base64.decode(PRIVATEKEY.getBytes()), msgJson);
        jsonObject.put("msg",msgJson);
        String data = Base64.encodeToString(jsonObject.toJSONString());
        
        try{
        	// 加密
         	PublicKey publicKey = RSAUtils.loadPublicKey(Common.PUBLICKKEY);
         	byte[] decryptByte1 = RSAUtils.encryptData(data.getBytes(),publicKey);
         	//System.out.println("加密:"+ HUIBOBase64Utils.encode(decryptByte1));
         	
         	data = HUIBOBase64Utils.encode(decryptByte1);
         	String signature = new String(sign);
         	
         	JdbcTemplate template=JdbcResource.instance().getJdbcTemplate(inter.getDataSource());
    		String ds=inter.getDataSource();
    		
    		HbpService hbpService = new HbpService(template,ds);
         	
    		hbpService.createHbp(order_no, task.getParam("login_name"), amount, customer_ip, bank_code, remark,url);
    		
         	Result r =Result.create(task.getId(), task.getFunId());
         	r.addFields(new String[]{"ok","order_no","message","amount","signature","bank","data"});
         	r.setFlag("-1");
         	r.setIsList(true);
         	r.setLength(1);
         	r.addRecord(new String[]{"1",order_no,"",amount,signature,bank_code,data});
         	return r;
         	
        }catch(Exception e){
        	e.printStackTrace();
        }
     
     	
		 Result r =Result.create(task.getId(), task.getFunId());
		 r.addFields(new String[]{"ok","order_no","message","amount","signature","bank","data"});
		 r.setFlag("-1");
		 r.setIsList(true);
		 r.setLength(1);
		 r.addRecord(new String[]{"0",order_no,"无法创建订单!",amount,"",bank_code,""});
		 return r;
		 
	}
	
	@Service(name="qrcodepay")
	@Params(validateField={"login_name"})
	public Result qrcodepay(Task task,InterFace inter){
		
		String product = task.getParam("product");
		
		String order_no = "hbonline"+"_" + String.valueOf(System.currentTimeMillis());
		String amount = task.getParam("amount");
		String order_amount = (Integer.parseInt(amount)*100)+"";
		String tradeCode = "hb_Pay";
		String bank_code = task.getParam("bank");  //客户访问用的
		String customer_ip = task.getParam("customer_ip");  //客户访问用的IP
		String remark = task.getParam("remark");
		String url = task.getParam("return_url");  //客户访问用的网址
		
        JSONObject jsonObject = new JSONObject();
        // 交易命令
        jsonObject.put("orderCode",tradeCode);
     // 账号
        jsonObject.put("account","13168889255");
        // 支付通道 UNIPAY
        jsonObject.put("channelCode",bank_code);
		
        
     
        
        JSONObject msgBody = new JSONObject();
        msgBody.put("orderId",order_no);
        msgBody.put("amount", order_amount);
        
        
        
        String msgJson = msgBody.toJSONString();
       // System.out.println("===="+msgJson);
        msgJson = Base64.encodeToString(msgJson);
        
      //签名
        byte[] sign = LocalUtil.sign(Base64.decode(PRIVATESAOMAKEY.getBytes()), msgJson);
        jsonObject.put("msg",msgJson);
        //System.out.println("msg================"+jsonObject);
        String data = Base64.encodeToString(jsonObject.toJSONString());   //Base64编码
        
        try{
        //  
         	PublicKey publicKey = RSAUtils.loadPublicKey(Common.PUBLICSAOMAKKEY);
         	byte[] decryptByte1 = RSAUtils.encryptData(data.getBytes(),publicKey);  //RSA私钥加密
         	//System.out.println("加密:"+ HUIBOBase64Utils.encode(decryptByte1));
         	
         	
         	
         	JSONObject GLOBAL = new JSONObject();
            GLOBAL.put("signature",new String(sign));
            GLOBAL.put("data", HUIBOBase64Utils.encode(decryptByte1));
            String request = GLOBAL.toJSONString();
            System.out.println("Common.SAO_MA_URL     "+Common.SAO_MA_URL);
            byte[] res = MyURLConnection.postBinResource(Common.SAO_MA_URL,request,Common.CHARSET,30);
            String response = new String(res,"UTF-8");
            System.out.println("response"+response);
            
            JSONObject resp = JSONObject.parseObject(response);
            String msg = resp.getString("data");
           // System.out.println(msg);
            //System.out.println();
            String signature = resp.getString("signature");
            
            PrivateKey privateKey = RSAUtils.loadPrivateKey(PRIVATESAOMAKEY);
			byte[] decryptByte = RSAUtils.decryptData(HUIBOBase64Utils.decode(msg), privateKey);
			
			
			String decryptStr = new String(decryptByte);
			String datas = Base64.decodeToString(decryptStr);
			//System.out.println("解密:" + datas);
			JSONObject respData = JSONObject.parseObject(datas);
			 System.out.println("respData"+respData);
			String msgData = respData.getString("msg");
			
			JSONObject msgg = JSONObject.parseObject(msgData);
			String QRCODEURL = msgg.getString("QRcodeURL");
			System.out.println("QRcodeURL:"+QRCODEURL);
            
			if(msgg != null && !QRCODEURL.equals("")){
				JdbcTemplate template=JdbcResource.instance().getJdbcTemplate(inter.getDataSource());
	    		String ds=inter.getDataSource();
	    		
	    		HbpService hbpService = new HbpService(template,ds);
	         	
	    		hbpService.createHbp(order_no, task.getParam("login_name"), amount, customer_ip, bank_code, remark,url);
	    		
	         	Result r =Result.create(task.getId(), task.getFunId());
	         	r.addFields(new String[]{"ok","pay_id","message","amount","barCode"});
	         	r.setFlag("-1");
	         	r.setIsList(true);
	         	r.setLength(1);
	         	r.addRecord(new String[]{"1",order_no,"",amount,QRCODEURL});
	         	return r;
			}
				
         	
         	
        }catch(Exception e){
        	e.printStackTrace();
        }
     
     	
		 Result r =Result.create(task.getId(), task.getFunId());
		 r.addFields(new String[]{"ok","pay_id","message","amount","barCode"});
		 r.setFlag("-1");
		 r.setIsList(true);
		 r.setLength(1);
		 r.addRecord(new String[]{"0",order_no,"",amount,""});
		 return r;
		 
	}
	
	
	@Service(name="notify")
	@Params(validateField={"signature"})
	@Transactional
	public Result notify(Task task,InterFace inter){
		
		String product = task.getParam("product");
		String respCode = task.getParam("respCode");
	    String orderId = task.getParam("orderId");
	    String amount = task.getParam("amount");
	    String respInfo = task.getParam("respInfo");
	    String signature = task.getParam("signature");
	    
	    if ("000000".equalsIgnoreCase(respCode)){
	    	String ds=ThPayResource.instance().getConfig("rpnpay."+product+".datasource"); 
	    	String data = orderId+respCode+respInfo+amount;
	    	
	    	try {
				boolean vfy = LocalUtil.verifySignature(Base64.decode(Common.PUBLICKKEY.getBytes()), Base64.decodeToString(signature), data.getBytes("UTF-8"));
				String type="";
				boolean vfyqq = LocalUtil.verifySignature(Base64.decode(Common.PUBLICSAOMAKKEY.getBytes()), Base64.decodeToString(signature), data.getBytes("UTF-8"));
//				if(vfyqq){
//					type ="QQ扫码";
//				}
				
				
				if(vfy || vfyqq){
					JdbcTemplate template=JdbcResource.instance().getJdbcTemplate(ds);
					OrderNumberService orderNumberService =new OrderNumberService(template,ds);
					HbpService hbpService = new HbpService(template,ds);
					CreditService creditService =new CreditService(template,ds);
					CustomerService custService =new CustomerService(template,ds);
					DepositService depositService =new DepositService(template,ds);
					if(hbpService.isNotDoHB(orderId)){
						hbpService.updaeteHB(orderId, "2",  new BigDecimal(amount).divide(new BigDecimal(100)));
						
						orderNumberService.createOrderNumber(orderId);
						
						type = hbpService.queryPayType(orderId);
						
						String login_name=hbpService.queryLoginName(orderId);
						
						if(creditService.add(login_name,new BigDecimal(amount).divide(new BigDecimal(100)),"自动充值", "hb支付:"+type, login_name, orderId)){
							 // try{
									Customer customer= custService.getCustomer(login_name);
									//添加存款记录和日志
									String dep_no = OrderNoService.createLocalNo("DE");
									depositService.addDeposit2(dep_no, customer.cust_id, login_name, customer.real_name, new BigDecimal(amount).divide(new BigDecimal(100)), "HB支付"+type, "", "HB支付"+type, "", orderId);
									//查询是否第一次存款,如果是,升级用户等级
									
									if(depositService.NTgetCount(customer.cust_id) == 1){
										if(new BigDecimal(amount).divide(new BigDecimal(100)).intValue() >=100){
											custService.NTmodCustlevelFirst(customer.cust_id, 1);
										}else{
											custService.NTmodFirstDepositDateOnly(customer.cust_id, 1);
										}
										
									}else{ 
										if(customer.cust_level == 0){
											if(new BigDecimal(amount).divide(new BigDecimal(100)).intValue() >=100){
												custService.NTmodCustlevelOnly(customer.cust_id, 1);
											}
										}
									}
									
									if(customer.promo_flag != null && customer.promo_flag){
										ScoreService scoreService =new ScoreService(template,ds);
										if(scoreService.depositCountToday(login_name)==1 && (new BigDecimal( amount).divide(new BigDecimal(100))).intValue()>=100){
											scoreService.modScore(orderId, "签到积分",  new BigDecimal(1), login_name, customer.cust_id, "");
										}
										if((new BigDecimal( amount).divide(new BigDecimal(100))).intValue()>=100){
											scoreService.modScore(orderId, "存款积分",new BigDecimal(new BigDecimal( amount).divide(new BigDecimal(100)).divide(new BigDecimal(100)).intValue()), login_name, customer.cust_id, "");
										}
										
										
										Date now =new Date(System.currentTimeMillis());
										if(now.getTime()>DateUtil.stringToDate("2017-12-01", "yyyy-MM-dd").getTime() &&
									    		  DateUtil.stringToDate("2018-01-01", "yyyy-MM-dd").getTime()>now.getTime() && new BigDecimal(amount).divide(new BigDecimal(100)).intValue()>=500 ){
									    	  //int dcount=depositService.NTgetCountToday(customer.cust_id);
									    	 // if(dcount<=5){
									    		  String giftCode=RandomUtil.getRandom(8);
													 CashGift gift =new CashGift();
										        	 gift.gift_code=giftCode;
										        	 gift.login_name=customer.login_name;
										        	 gift.deposit_credit=new BigDecimal(amount).divide(new BigDecimal(100));
										        	 gift.valid_credit=new BigDecimal(0);
										        	 gift.net_credit=new BigDecimal(0);
										        	 
										        	 float rate = 0;
										        	 float f = 0.0f;
										        	 if(new BigDecimal(amount).divide(new BigDecimal(100)).intValue() <5000 && new BigDecimal(amount).divide(new BigDecimal(100)).intValue() >= 500){
														 gift.rate=Float.valueOf(1);
											        	 rate = 1;
											        	 f=new BigDecimal(amount).divide(new BigDecimal(100)).floatValue()*1/100;
													 }else if(new BigDecimal(amount).divide(new BigDecimal(100)).intValue() >= 5000 && new BigDecimal(amount).divide(new BigDecimal(100)).intValue()<10000){
														 gift.rate=Float.valueOf(1.8f);
											        	 rate = 1.8f;
											        	 f=new BigDecimal(amount).divide(new BigDecimal(100)).floatValue()*1.8f/100;
													 }else if(new BigDecimal(amount).divide(new BigDecimal(100)).intValue() >= 10000 && new BigDecimal(amount).divide(new BigDecimal(100)).intValue()< 30000){
														 gift.rate=Float.valueOf(2.5f);
											        	 rate = 2.5f;
											        	 f=new BigDecimal(amount).divide(new BigDecimal(100)).floatValue()*2.5f/100;
													 }else if(new BigDecimal(amount).divide(new BigDecimal(100)).intValue() >= 30000 && new BigDecimal(amount).divide(new BigDecimal(100)).intValue()< 50000){
														 gift.rate=Float.valueOf(3.8f);
											        	 rate = 3.8f;
											        	 f=new BigDecimal(amount).divide(new BigDecimal(100)).floatValue()*3.8f/100;
													 }else if(new BigDecimal(amount).divide(new BigDecimal(100)).intValue() >= 50000 ){
														 gift.rate=Float.valueOf(5);
											        	 rate = 5;
											        	 f=new BigDecimal(amount).divide(new BigDecimal(100)).floatValue()*5/100;
											        	 if(f>2888){
											        		 f=2888;
											        	 }
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
									
									
								//}catch(Exception e){
									
									//e.printStackTrace();
									
								//}
						}
						
						
					}
					
				}
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
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
