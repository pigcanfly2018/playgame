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

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

import com.product.bda.service.CashGift;
import com.product.bda.service.CashGiftService;
import com.product.bda.service.CreditService;
import com.product.bda.service.Customer;
import com.product.bda.service.CustomerService;
import com.product.bda.service.DepositService;
import com.product.bda.service.DingYiBaoPayService;
import com.product.bda.service.OrderNoService;
import com.product.bda.service.OrderNumberService;
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

@Handler(name="DYP")
public class DingYiPayHandler {

	protected static String BASE_URL = "http://pay.dingyipay.com/ChargeBank.aspx";
	
	private static Logger logger=Logger.getLogger(DingYiPayHandler.class);
	
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
		
		String pre=ThPayResource.instance().getConfig("dingyipay."+product+".pre");
		String partner =ThPayResource.instance().getConfig("dingyipay."+product+".partner"); 
		String mer_key =ThPayResource.instance().getConfig("dingyipay."+product+".mer_key");
		String notify_url = ThPayResource.instance().getConfig("dingyipay."+product+".callbackurl"); //服务器通知返回接口
		String hrefbackurl = ThPayResource.instance().getConfig("dingyipay."+product+".hrefbackurl"); //服务器通知返回接口
		
		String customer_ip = task.getParam("customer_ip");
		String return_url = task.getParam("return_url");  //客户访问用的网址
		String payType = task.getParam("payType");  //支付方式
		//String show_amount = task.getParam("amount");
		String amount = task.getParam("amount");
		String remark = task.getParam("login_name");
		
		String order_no = pre + String.valueOf(System.currentTimeMillis());
		
		Map<String, String> param = buildRequestParam(false);
		
		param.put("parter", partner);
		param.put("type", payType);
		param.put("value", amount);
		param.put("orderid", order_no);
		param.put("callbackurl", notify_url);
		param.put("hrefbackurl",hrefbackurl);
		param.put("payerIp",customer_ip);
		
		param.put("attach", remark);

		String prestr="parter="+partner+"&type="+payType+"&value="+amount+"&orderid="+order_no+"&callbackurl="+notify_url+mer_key;//"&hrefbackurl="+hrefbackurl+"&payerIp="+customer_ip+"&attach="+remark
		String signMsg = MD5Util.md5Encode(prestr);
		param.put("sign", signMsg);
		
		Map<String, String> reqParam = new HashMap<String, String>();
		Set<String> keySet = param.keySet();
		Iterator<String> iter = keySet.iterator();
		while (iter.hasNext()) {                 
			String key = iter.next();
			reqParam.put(key, param.get(key));
		}
		
		try {
			
			String url = buildUrl("passivePay", reqParam);
			
			
			if(url != null && !"".equals(url)){
				String ds=inter.getDataSource();
				JdbcTemplate template=JdbcResource.instance().getJdbcTemplate(ds);
				DingYiBaoPayService service = new DingYiBaoPayService(template,ds);
				
				if(payType.equals("1004")){
					service.createDybp(order_no, remark, amount, customer_ip, "微信", "", return_url);
				}else if("101".equals(payType)){
					service.createDybp(order_no, remark, amount, customer_ip, "支付宝", "", return_url);
				}else if("1006".equals(payType)){
					service.createDybp(order_no, remark, amount, customer_ip, "QQ扫码", "", return_url);
				}else if("1010".equals(payType)){
					service.createDybp(order_no, remark, amount, customer_ip, "京东扫码", "", return_url);
				}
				
				Result r =Result.create(task.getId(), task.getFunId());
				r.addFields(new String[]{"ok","pay_id","message","amount","directurl"});
				r.setFlag("-1");
				r.setIsList(true);
				r.setLength(1);
				r.addRecord(new String[]{"1",order_no,"",amount,url});
				return r;
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		Result r =Result.create(task.getId(), task.getFunId());
		r.addFields(new String[]{"ok","pay_id","message","amount","directurl"});
		r.setFlag("-1");
		r.setIsList(true);
		r.setLength(1);
		r.addRecord(new String[]{"0",order_no,"无法创建订单",amount,""});
		return r;
	}
	
	@Service(name="notify")
	@Params(validateField={"ovalue","opstate"})
	public Result notify(Task task,InterFace inter){
		String product = task.getParam("product");
		
		String orderid = task.getParam("orderid");
	    String opstate = task.getParam("opstate");
	    BigDecimal ovalue = new BigDecimal(task.getParam("ovalue"));
	    String sysorderid = task.getParam("sysorderid");
	    String systime = task.getParam("systime");
	    String attach = task.getParam("attach");
	    String msg = task.getParam("msg");
	    String sign = task.getParam("sign");
	    
		String mer_key =ThPayResource.instance().getConfig("dingyipay."+product+".mer_key");
	    String preparam="orderid="+orderid+"&opstate="+opstate+"&ovalue="+ovalue+mer_key;
		String md5sign = MD5Util.md5Encode(preparam);
	    if(opstate.equals("0") && md5sign.equals(sign)){
	    	String ds=ThPayResource.instance().getConfig("dingyipay."+product+".datasource");
	    	
	    	JdbcTemplate template=JdbcResource.instance().getJdbcTemplate(ds);
	    	OrderNumberService orderNumberService =new OrderNumberService(template,ds);
	    	CreditService creditService =new CreditService(template,ds);
			CustomerService custService =new CustomerService(template,ds);
			DepositService depositService =new DepositService(template,ds);
			DingYiBaoPayService dybService = new DingYiBaoPayService(template,ds);
			//没有处理
			if(dybService.isNotDoDybp(orderid)){
				String login_name = dybService.queryLoginname(orderid);
				String paytype = dybService.queryPayType(orderid);
				if(paytype.equals("微信")){//微信扫码
					dybService.updaete(orderid, sysorderid,sysorderid, "2", ovalue, new BigDecimal(0.975));
				}else if("支付宝".equals(paytype)){
					dybService.updaete(orderid, sysorderid,sysorderid, "2", ovalue, new BigDecimal(0.975));
				}else if("QQ扫码".equals(paytype)){
					dybService.updaete(orderid, sysorderid,sysorderid, "2", ovalue, new BigDecimal(0.975));
				}else if("京东扫码".equals(paytype)){
					dybService.updaete(orderid, sysorderid,sysorderid, "2", ovalue, new BigDecimal(0.975));
				}
				
				orderNumberService.createOrderNumber(orderid);
				//paytype = paytype.equals("1004")?"微信":"支付宝";
				if(creditService.add(login_name, ovalue,"自动充值", "dingyibao在线支付:"+paytype, login_name, orderid)){
					  try{
							Customer customer= custService.getCustomer(login_name);
							//添加存款记录和日志
							String dep_no = OrderNoService.createLocalNo("DE");
							depositService.addDeposit2(dep_no, customer.cust_id, login_name, customer.real_name, ovalue, "dyb在线支付"+paytype, "", "dyb在线支付"+paytype, "", orderid);
							//查询是否第一次存款,如果是,升级用户等级
//							if(depositService.NTgetCount(customer.cust_id) == 1){
//								custService.NTmodCustlevelFirst(customer.cust_id, 1);
//							}
							
							if(depositService.NTgetCount(customer.cust_id) == 1){
								if(ovalue.intValue() >=100){
									custService.NTmodCustlevelFirst(customer.cust_id, 1);
								}else{
									custService.NTmodFirstDepositDateOnly(customer.cust_id, 1);
								}
								
							}else{ 
								if(customer.cust_level == 0){
									if(ovalue.intValue() >=100){
										custService.NTmodCustlevelOnly(customer.cust_id, 1);
									}
								}
							}
							
							
							if(customer.promo_flag != null && customer.promo_flag){
								ScoreService scoreService =new ScoreService(template,ds);
								if(scoreService.depositCountToday(login_name)==1 && ovalue.intValue() >=100 ){
									scoreService.modScore(orderid, "签到积分",  new BigDecimal(1), login_name, customer.cust_id, "");
								}
								scoreService.modScore(orderid, "存款积分",new BigDecimal( ovalue.divide(new BigDecimal(100)).intValue()), login_name, customer.cust_id, "");
							
								Date now =new Date(System.currentTimeMillis());
								if(now.getTime()>DateUtil.stringToDate("2017-12-01", "yyyy-MM-dd").getTime() &&
							    		  DateUtil.stringToDate("2018-01-01", "yyyy-MM-dd").getTime()>now.getTime() && ovalue.intValue()>=500){
									
									String giftCode=RandomUtil.getRandom(8);
									 CashGift gift =new CashGift();
						        	 gift.gift_code=giftCode;
						        	 gift.login_name=customer.login_name;
						        	 gift.deposit_credit=ovalue;
						        	 gift.valid_credit=new BigDecimal(0);
						        	 gift.net_credit=new BigDecimal(0);
						        	 
						        	 float rate = 0;
						        	 float f = 0.0f;
						        	 if(ovalue.intValue() >= 500 && ovalue.intValue()<5000){
						        		 gift.rate=Float.valueOf(1);
							        	 rate = 1;
							        	 f=ovalue.floatValue()*1/100;
									 }else if(ovalue.intValue() >= 5000 && ovalue.intValue()<10000){
										 gift.rate=Float.valueOf(1.8f);
							        	 rate = 1.8f;
							        	 f=ovalue.floatValue()*1.8f/100;
									 }else if(ovalue.intValue() >= 10000 && ovalue.intValue()<30000){
										 gift.rate=Float.valueOf(2.5f);
							        	 rate = 2.5f;
							        	 f=ovalue.floatValue()*2.5f/100;
									 }else if(ovalue.intValue() >= 30000 && ovalue.intValue()<50000){
										 gift.rate=Float.valueOf(3.8f);
							        	 rate = 3.8f;
							        	 f=ovalue.floatValue()*3.8f/100;
									 }else if(ovalue.intValue() >= 50000 ){
										 gift.rate=Float.valueOf(5);
							        	 rate = 5;
							        	 f=ovalue.floatValue()*5/100;
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
		url.append(BASE_URL+ "?");
		//url.append(transType + "?");
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
		
		/*Map<String, String> param = buildRequestParam(false);
		param.put("merchno", "900410479940001");
		param.put("amount", "50.03");
		param.put("traceno", "678146464414324");
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
	*/	
	String preparam="orderid=dy1504241898992&opstate=0&ovalue=2074f3e848a9248da97bf55b9595b1e2c";
	String md5sign = MD5Util.md5Encode(preparam);
	System.out.println(md5sign);
	}
	
}
