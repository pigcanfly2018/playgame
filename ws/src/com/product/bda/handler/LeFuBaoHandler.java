package com.product.bda.handler;

import java.math.BigDecimal;
import java.security.MessageDigest;
import java.util.Arrays;

import org.springframework.jdbc.core.JdbcTemplate;

import com.product.bda.service.CreditService;
import com.product.bda.service.Customer;
import com.product.bda.service.CustomerService;
import com.product.bda.service.DepositService;
import com.product.bda.service.OrderNoService;
import com.product.bda.service.OrderNumberService;
import com.product.bda.service.PayService;
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

@Handler(name="LeFuBaoPay")
public class LeFuBaoHandler {

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
		
		String pre=ThPayResource.instance().getConfig("lfbpay."+product+".pre");
		String merchno =ThPayResource.instance().getConfig("lfbpay."+product+".merNo"); 
		String mer_key =ThPayResource.instance().getConfig("lfbpay."+product+".mer_key");
		String notifyUrl = ThPayResource.instance().getConfig("lfbpay."+product+".notifyUrl"); //服务器通知返回接口
		String paytype = task.getParam("paytype");  //支付方式
		String remark = task.getParam("login_name");
		String return_url = task.getParam("return_url");  //客户访问用的网址
		String order_no = pre+"" + String.valueOf(System.currentTimeMillis());
		String payip = task.getParam("ip");	//支付ip
		
		JdbcTemplate template=JdbcResource.instance().getJdbcTemplate(inter.getDataSource());
		String ds=inter.getDataSource();
		String total_fee = task.getParam("amount");
		
		PayService payservice = new PayService(template,ds);
		payservice.createOrder("lefubao",order_no, task.getParam("login_name"), total_fee, payip, paytype, remark,return_url);
		
		try{
			
			String PostUrl = "http://www.oeepay.net/gateway";//请查看接口文档或联系客服
			 String p1_MerId=merchno;								// 商户编号 是  Max(11)  商户平台中的商户ID
			 String key=mer_key; 		//商户密钥   商户平台中的商户KEY
	         String p0_Cmd = "Buy";									// 业务类型 是  Max(20)  固定值“Buy” . 1 

	         String p2_Order = order_no;	//  商户订单号 否  Max(50) 若不为””，提交的订单号必须在自身账户交易中唯一;为 ””
	         String p3_Amt = total_fee + "";	//支付金额 否  Max(20)  单位:元，精确到分.此参数为空则无法直连(如直连会报错：抱歉，交易金额太小。),必须到易宝网关让消费者输入金额 4 
	         String p4_Cur = "CNY";									//  交易币种 是  Max(10)  固定值 ”CNY”. 5 
	         String p5_Pid = "mobile";									// 商品名称 否  Max(20) 用于支付时显示在易宝支付网关左侧的订单产品信息.
	         String p6_Pcat = "electronic";									// 商品种类 否  Max(20)  商品种类.
	         String p7_Pdesc = "apple"; 									//商品描述 否  Max(20)  商品描述.
	         String p8_Url = notifyUrl;	    // 商户接收支付成功数据的地址 否  Max(200) 支付成功后易宝支付会向该地址发送两次成功通知
	         String pa_MP = remark;	    // 商户扩展信息 否  Max(200)  返回时原样返回，此参数如用到中文，请注意转码. 11 
	         String pd_FrpId = paytype;  //支付通道编码 否  Max(50) 默认为 ”” ，到易宝支付网关，易宝支付网关默认显示已开通的全部支付通道.
	         String pr_NeedResponse = "1"; 							// 应答机制 否  Max(1) 固定值为“1”: 需要应答机制; 收到易宝支付服务器点对点支付成功通知，必须回写以”success”（无关大小写）开头的字符串，即使您收到成功通知时发现该订单已经处理过，也要正确回写”success”，否则易宝支付将认为您的系统没有收到通知，启动重发机制，直到收到”success”为止。 
	         String hmac = "";
	         
	         String sbOld = "";
	         sbOld += p0_Cmd;
	         sbOld += p1_MerId;
	         sbOld += p2_Order;
	         sbOld += p3_Amt;
	         sbOld += p4_Cur;
	         sbOld += p5_Pid;
	         sbOld += p6_Pcat;
	         sbOld += p7_Pdesc;
	         sbOld += p8_Url;
	         sbOld += pa_MP;
	         sbOld += pd_FrpId;
	         sbOld += pr_NeedResponse;
	         
	         hmac = hmacSign(sbOld, key); //数据签名
	 		
	         String result = "";
	         result += PostUrl;
	         result += "?p0_Cmd=" + p0_Cmd;
	         result += "&p1_MerId=" + p1_MerId;
	         result += "&p2_Order=" + p2_Order;
	         result += "&p3_Amt=" + p3_Amt;
	         result += "&p4_Cur=" + p4_Cur;
	         result += "&p5_Pid=" + p5_Pid;
	         result += "&p6_Pcat=" + p6_Pcat;
	         result += "&p7_Pdesc=" + p7_Pdesc;
	         result += "&p8_Url=" + p8_Url;
	         result += "&pa_MP=" +pa_MP;
	         result += "&pd_FrpId=" + pd_FrpId;
	         result += "&pr_NeedResponse=" + pr_NeedResponse;
	         result += "&hmac=" + hmac;
	         
	         Result r =Result.create(task.getId(), task.getFunId());
				r.addFields(new String[]{"ok","pay_id","message","amount","directurl"});
				r.setFlag("-1");
				r.setIsList(true);
				r.setLength(1);
				r.addRecord(new String[]{"1",order_no,"",total_fee,result});
				return r;
			
		}catch(Exception e){
			e.printStackTrace();
		 }
		Result r =Result.create(task.getId(), task.getFunId());
		r.addFields(new String[]{"ok","pay_id","message","amount","directurl"});
		r.setFlag("-1");
		r.setIsList(true);
		r.setLength(1);
		r.addRecord(new String[]{"0",order_no,"无法创建订单",total_fee,""});
		return r;
		
	}
	
	@Service(name="notify")
	@Params(validateField={"r1_Code"})
	public Result notify(Task task,InterFace inter){
		
		String product = task.getParam("product");
		String r0_Cmd = "Buy";
		String r1_Code = task.getParam("r1_Code");
	    String r2_TrxId = task.getParam("r2_TrxId");
	    BigDecimal r3_Amt = new BigDecimal(task.getParam("r3_Amt"));
	    String r4_Cur = "CNY";	
	    String r5_Order = task.getParam("r5_Order");
	    String r6_Type =task.getParam("r6_Type");
	    String hmac = task.getParam("hmac");
	    
	    String p1_MerId =ThPayResource.instance().getConfig("lfbpay."+product+".merNo"); 
		String Key =ThPayResource.instance().getConfig("lfbpay."+product+".mer_key");
		
		String sbOld = "";
        sbOld += p1_MerId;
        sbOld += r0_Cmd;
        sbOld += r1_Code;
        sbOld += r2_TrxId;
        sbOld += r3_Amt;
        sbOld += r4_Cur;
        sbOld += r5_Order;
        sbOld += r6_Type; 
		
        String nhmac = hmacSign(sbOld, Key); //数据签名
        if (nhmac.equals( hmac)) {
        	if ("1".equals(r1_Code)) {  
        		String ds=ThPayResource.instance().getConfig("ytbpay."+product+".datasource");
        		JdbcTemplate template=JdbcResource.instance().getJdbcTemplate(ds);
    	    	OrderNumberService orderNumberService =new OrderNumberService(template,ds);
    	    	CreditService creditService =new CreditService(template,ds);
    			CustomerService custService =new CustomerService(template,ds);
    			DepositService depositService =new DepositService(template,ds);
    			PayService payservice = new PayService(template,ds);
    			String login_name =payservice.queryLoginName("lefubao", r5_Order);
    			if(payservice.isNotDone("lefubao", r5_Order, login_name)){
    				
    				payservice.updaeteStatus("lefubao", r5_Order, r2_TrxId, "2", r3_Amt, "alipaywap");
					
					orderNumberService.createOrderNumber(r5_Order);
					String paytype = payservice.queryPayType("lefubao",r5_Order);
					if(creditService.add(login_name, r3_Amt,"自动充值", "lfb在线支付:"+paytype, login_name, r5_Order)){
						try{
							Customer customer= custService.getCustomer(login_name);
							
							//添加存款记录和日志
							String dep_no = OrderNoService.createLocalNo("DE");
							depositService.addDeposit2(dep_no, customer.cust_id, login_name, customer.real_name, r3_Amt, "lfb在线支付"+paytype, "", "lfb在线支付"+paytype, "", r5_Order);
							//查询是否第一次存款,如果是,升级用户等级
							
							if(depositService.NTgetCount(customer.cust_id) == 1){
								if(r3_Amt.intValue() >=100){
									custService.NTmodCustlevelFirst(customer.cust_id, 1);
								}else{
									custService.NTmodFirstDepositDateOnly(customer.cust_id, 1);
								}
								
							}else{ 
								if(customer.cust_level == 0){
									if(r3_Amt.intValue() >=100){
										custService.NTmodCustlevelOnly(customer.cust_id, 1);
									}
								}
							}
							
							
							if(customer.promo_flag != null && customer.promo_flag){
								ScoreService scoreService =new ScoreService(template,ds);
								if(scoreService.depositCountToday(login_name)==1 && r3_Amt.intValue() >=100){
									scoreService.modScore(r5_Order, "签到积分",  new BigDecimal(1), login_name, customer.cust_id, "");
								}
								if(r3_Amt.intValue() >=100){
									scoreService.modScore(r5_Order, "存款积分",new BigDecimal( r3_Amt.divide(new BigDecimal(100)).intValue()), login_name, customer.cust_id, "");
								}
							   
							}
							
							Result r =Result.create(task.getId(), task.getFunId());
							r.addFields(new String[]{"ok"});
							r.setFlag("-1");
							r.setIsList(true);
							r.setLength(1);
							r.addRecord(new String[]{"1"});
							
							
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
private static String encodingCharset = "UTF-8";
	
	/**
	 * @param aValue
	 * @param aKey
	 * @return
	 */
	public static String hmacSign(String aValue, String aKey) {
		byte k_ipad[] = new byte[64];
		byte k_opad[] = new byte[64];
		byte keyb[];
		byte value[];
		try {
			keyb = aKey.getBytes(encodingCharset);
			value = aValue.getBytes(encodingCharset);
		} catch (Exception e) {
			keyb = aKey.getBytes();
			value = aValue.getBytes();
		}

		Arrays.fill(k_ipad, keyb.length, 64, (byte) 54);
		Arrays.fill(k_opad, keyb.length, 64, (byte) 92);
		for (int i = 0; i < keyb.length; i++) {
			k_ipad[i] = (byte) (keyb[i] ^ 0x36);
			k_opad[i] = (byte) (keyb[i] ^ 0x5c);
		}

		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (Exception e) {

			return null;
		}
		md.update(k_ipad);
		md.update(value);
		byte dg[] = md.digest();
		md.reset();
		md.update(k_opad);
		md.update(dg, 0, 16);
		dg = md.digest();
		return toHex(dg);
	}

	public static String toHex(byte input[]) {
		if (input == null)
			return null;
		StringBuffer output = new StringBuffer(input.length * 2);
		for (int i = 0; i < input.length; i++) {
			int current = input[i] & 0xff;
			if (current < 16)
				output.append("0");
			output.append(Integer.toString(current, 16));
		}

		return output.toString();
	}

	/**
	 * 
	 * @param args
	 * @param key
	 * @return
	 */
	public static String getHmac(String[] args, String key) {
		if (args == null || args.length == 0) {
			return (null);
		}
		StringBuffer str = new StringBuffer();
		for (int i = 0; i < args.length; i++) {
			str.append(args[i]);
		}
		return (hmacSign(str.toString(), key));
	}

	/**
	 * @param aValue
	 * @return
	 */
	public static String digest(String aValue) {
		aValue = aValue.trim();
		byte value[];
		try {
			value = aValue.getBytes(encodingCharset);
		} catch (Exception e) {
			value = aValue.getBytes();
		}
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return toHex(md.digest(value));

	}
}
