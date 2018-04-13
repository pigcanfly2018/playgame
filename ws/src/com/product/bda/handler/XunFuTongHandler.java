package com.product.bda.handler;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import org.springframework.jdbc.core.JdbcTemplate;

import com.product.bda.service.CreditService;
import com.product.bda.service.Customer;
import com.product.bda.service.CustomerService;
import com.product.bda.service.DepositService;
import com.product.bda.service.OrderNoService;
import com.product.bda.service.OrderNumberService;
import com.product.bda.service.ScoreService;
import com.product.bda.service.XunFuTongService;
import com.product.bda.service.XunHuiBaoPayService;

import net.sf.json.JSONObject;
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
import bsz.exch.utils.MD5Util;

@Handler(name="XFTP")
public class XunFuTongHandler {

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
		
		String pre=ThPayResource.instance().getConfig("xftpay."+product+".pre");
		String merchant_code =ThPayResource.instance().getConfig("xftpay."+product+".merchant_code"); 
		String mer_key =ThPayResource.instance().getConfig("xftpay."+product+".md5_key");
		String notify_url = ThPayResource.instance().getConfig("xftpay."+product+".notify_url"); //服务器通知返回接口
		String customer_ip = task.getParam("customer_ip");
		String return_url = task.getParam("return_url");  //客户访问用的网址
		String payType = task.getParam("payType");  //支付方式
		String show_amount = task.getParam("amount");
		String order_amount = (Integer.parseInt(show_amount)*100)+"";
		String remark = task.getParam("login_name");
		
		Map<String, String> metaSignMap = new TreeMap<String, String>();
		metaSignMap.put("merNo", merchant_code);
		if(payType.equals("1")){
			metaSignMap.put("netway", "ZFB");// WX:微信支付,ZFB:支付宝支付
		}else{
			metaSignMap.put("netway", "WX");// WX:微信支付,ZFB:支付宝支付
		}
		
		metaSignMap.put("random", randomStr(4));// 4位随机数
		String order_no = pre+"_" + String.valueOf(System.currentTimeMillis());
		metaSignMap.put("orderNum", order_no);
		metaSignMap.put("amount", order_amount);// 单位:分
		metaSignMap.put("goodsName", "电脑配件");// 商品名称：20位
		metaSignMap.put("callBackUrl", notify_url);// 回调地址
		
		String metaSignJsonStr = mapToJson(metaSignMap);
		String sign = MD5(metaSignJsonStr + mer_key, "UTF-8");// 32位
		//System.out.println("sign=" + sign); // 英文字母大写
		metaSignMap.put("sign", sign);
		String reqParam = "data=" + mapToJson(metaSignMap);
		String reqUrl = "http://zfb.h8pay.com/api/pay.action";
		if(payType.equals("2")){
			reqUrl = "http://wx.h8pay.com/api/pay.action";
		}
		String resultJsonStr = request(reqUrl, reqParam);

		// 检查状态
		JSONObject resultJsonObj = JSONObject.fromObject(resultJsonStr);
		String stateCode = resultJsonObj.getString("stateCode");
		if (!stateCode.equals("00")) {
			//return;
		}
		String resultSign = resultJsonObj.getString("sign");
		resultJsonObj.remove("sign");
		String targetString = MD5(resultJsonObj.toString() + mer_key, "UTF-8");
		if (targetString.equals(resultSign)) {
			String ds=inter.getDataSource();
			JdbcTemplate template=JdbcResource.instance().getJdbcTemplate(ds);
			XunFuTongService xftService = new XunFuTongService(template,ds);
			if(payType.equals("1")){
				xftService.createXftp(order_no, remark, show_amount, customer_ip, "支付宝", "", return_url);
			}else{
				xftService.createXftp(order_no, remark, show_amount, customer_ip, "微信", "", return_url);
			}
			
			String barCode = resultJsonObj.getString("qrcodeUrl");
			
			Result r =Result.create(task.getId(), task.getFunId());
			r.addFields(new String[]{"ok","pay_id","message","amount","barCode"});
			r.setFlag("-1");
			r.setIsList(true);
			r.setLength(1);
			r.addRecord(new String[]{"1",order_no,"",show_amount,barCode});
			return r;
			
		}
		
		
		Result r =Result.create(task.getId(), task.getFunId());
		r.addFields(new String[]{"ok","pay_id","message","show_amount","barCode"});
			r.setFlag("-1");
			r.setIsList(true);
			r.setLength(1);
			r.addRecord(new String[]{"0",order_no,"无法创建订单",show_amount,""});
		 return r;
		 
	}
	
	@Service(name="notify")
	public Result notify(Task task,InterFace inter){
		String product = task.getParam("product");
		String data = task.getParam("data");
		
		String md5_key =ThPayResource.instance().getConfig("xftpay."+product+".md5_key");
		
		JSONObject jsonObj = JSONObject.fromObject(data);
		Map<String, String> metaSignMap = new TreeMap<String, String>();
		
		String orderNum = jsonObj.getString("orderNum") ;
		String netway = jsonObj.getString("netway");
		BigDecimal amount = new BigDecimal(jsonObj.getString("amount")).divide(new BigDecimal(100));
		
		metaSignMap.put("merNo", jsonObj.getString("merNo"));
		metaSignMap.put("netway", netway);
		metaSignMap.put("orderNum", orderNum);
		metaSignMap.put("amount", jsonObj.getString("amount"));
		metaSignMap.put("goodsName", jsonObj.getString("goodsName"));
		metaSignMap.put("payResult", jsonObj.getString("payResult"));// 支付状态
		metaSignMap.put("payDate", jsonObj.getString("payDate"));// yyyy-MM-dd
																	// HH:mm:ss
		String jsonStr = mapToJson(metaSignMap);
		String sign = MD5(jsonStr.toString() + md5_key, "UTF-8");
		if(sign.equals(jsonObj.getString("sign"))){
			
			//return;
			String ds=ThPayResource.instance().getConfig("xftpay."+product+".datasource");
	    	JdbcTemplate template=JdbcResource.instance().getJdbcTemplate(ds);
	    	OrderNumberService orderNumberService =new OrderNumberService(template,ds);
	    	CreditService creditService =new CreditService(template,ds);
			CustomerService custService =new CustomerService(template,ds);
			DepositService depositService =new DepositService(template,ds);
			XunFuTongService xftService = new XunFuTongService(template,ds);
			
			if(xftService.isNotDoYdp(orderNum)){
				String login_name = xftService.queryLoginname(orderNum);
				if(netway.equals("ZFB")){//支付宝
					xftService.updaete(orderNum, orderNum,orderNum, "2", amount, new BigDecimal(0.999));
				}else{//WX是微信
					xftService.updaete(orderNum, orderNum,orderNum, "2", amount, new BigDecimal(0.992));
				}
				
				orderNumberService.createOrderNumber(orderNum);
				if(creditService.add(login_name, amount,"自动充值", "xunfutong在线支付:"+netway, login_name, orderNum)){
					try{
						Customer customer= custService.getCustomer(login_name);
						//添加存款记录和日志
						String dep_no = OrderNoService.createLocalNo("DE");
						depositService.addDeposit2(dep_no, customer.cust_id, login_name, customer.real_name, amount, "xft在线支付"+netway, "", "xft在线支付"+netway, "", orderNum);
						
						//查询是否第一次存款,如果是,升级用户等级
						if(depositService.NTgetCount(customer.cust_id) == 1){
							custService.NTmodCustlevelFirst(customer.cust_id, 1);
						}
						
						if(customer.promo_flag != null && customer.promo_flag){
							ScoreService scoreService =new ScoreService(template,ds);
							if(scoreService.depositCountToday(login_name)==1){
								scoreService.modScore(orderNum, "签到积分",  new BigDecimal(1), login_name, customer.cust_id, "");
							}
							scoreService.modScore(orderNum, "存款积分",new BigDecimal( amount.divide(new BigDecimal(100)).intValue()), login_name, customer.cust_id, "");
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
	
	private static String getResponseBodyAsString(InputStream in) {
		try {
			BufferedInputStream buf = new BufferedInputStream(in);
			byte[] buffer = new byte[1024];
			StringBuffer data = new StringBuffer();
			int readDataLen;
			while ((readDataLen = buf.read(buffer)) != -1) {
				data.append(new String(buffer, 0, readDataLen, "UTF-8"));
			}
			System.out.println("响应报文=" + data);
			return data.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String request(String url, String params) {
		try {
			System.out.println("请求报文:" + params);
			URL urlObj = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) urlObj
					.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setConnectTimeout(1000 * 5);
			conn.setRequestProperty("Charset", "UTF-8");
			conn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			conn.setRequestProperty("Content-Length",
					String.valueOf(params.length()));
			OutputStream outStream = conn.getOutputStream();
			outStream.write(params.toString().getBytes("UTF-8"));
			outStream.flush();
			outStream.close();
			return getResponseBodyAsString(conn.getInputStream());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public static String randomStr(int num) {
		char[] randomMetaData = new char[] { 'a', 'b', 'c', 'd', 'e', 'f', 'g',
				'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's',
				't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E',
				'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q',
				'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2',
				'3', '4', '5', '6', '7', '8', '9' };
		Random random = new Random();
		String tNonceStr = "";
		for (int i = 0; i < num; i++) {
			tNonceStr += (randomMetaData[random
					.nextInt(randomMetaData.length - 1)]);
		}
		return tNonceStr;
	}
	
	public static String mapToJson(Map<String, String> map) {
		Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
		StringBuffer json = new StringBuffer();
		json.append("{");
		while (it.hasNext()) {
			Map.Entry<String, String> entry = it.next();
			String key = entry.getKey();
			String value = entry.getValue();
			json.append("\"").append(key).append("\"");
			json.append(":");
			json.append("\"").append(value).append("\"");
			if (it.hasNext()) {
				json.append(",");
			}
		}
		json.append("}");
		//System.out.println("mapToJson=" + json.toString());
		return json.toString();
	}
	
	
	public final static String MD5(String s, String encoding) {
		try {
			byte[] btInput = s.getBytes(encoding);
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			mdInst.update(btInput);
			byte[] md = mdInst.digest();
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = HEX_DIGITS[byte0 >>> 4 & 0xf];
				str[k++] = HEX_DIGITS[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	static final char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5', '6', '7',
		'8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
	
	public static void paytest() {
		Map<String, String> metaSignMap = new TreeMap<String, String>();
		metaSignMap.put("merNo", "Mer201612200000");
		metaSignMap.put("netway", "ZFB");// WX:微信支付,ZFB:支付宝支付
		metaSignMap.put("random", randomStr(4));// 4位随机数
		String orderNum = new SimpleDateFormat("yyyyMMddHHmmssSSS")
				.format(new Date()); // 20位
		orderNum += randomStr(3);
		metaSignMap.put("orderNum", orderNum);
		metaSignMap.put("amount", "500");// 单位:分
		metaSignMap.put("goodsName", "笔");// 商品名称：20位
		metaSignMap.put("callBackUrl", "http://127.0.0.1/");// 回调地址
		metaSignMap.put("callBackViewUrl", "http://localhost/view");// 回显地址
		String metaSignJsonStr = mapToJson(metaSignMap);
		String sign = MD5(metaSignJsonStr + "EF6182CC14AF29E06D7FBBE028B0E46E", "UTF-8");// 32位
		//System.out.println("sign=" + sign); // 英文字母大写
		metaSignMap.put("sign", sign);
		String reqParam = "data=" + mapToJson(metaSignMap);
		String resultJsonStr = request("http://zfb.h8pay.com/api/pay.action", reqParam);

		// 检查状态
		JSONObject resultJsonObj = JSONObject.fromObject(resultJsonStr);
		
		//System.out.println(resultJsonObj);
		
		String stateCode = resultJsonObj.getString("stateCode");
		if (!stateCode.equals("00")) {
			//return;
		}
		String resultSign = resultJsonObj.getString("sign");
		resultJsonObj.remove("sign");
		String targetString = MD5(resultJsonObj.toString() + "EF6182CC14AF29E06D7FBBE028B0E46E", "UTF-8");
		if (targetString.equals(resultSign)) {
			//System.out.println("签名校验成功");
		}
		
	}
	
	public static void main(String[] args) throws Throwable {
		paytest();
	}
	
	
}
