package com.product.bda.handler;

import java.math.BigDecimal;

import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpCoreContext;
import org.apache.http.util.EntityUtils;
import org.springframework.jdbc.core.JdbcTemplate;

import com.product.bda.service.CreditService;
import com.product.bda.service.Customer;
import com.product.bda.service.CustomerService;
import com.product.bda.service.DepositService;
import com.product.bda.service.OrderNoService;
import com.product.bda.service.OrderNumberService;
import com.product.bda.service.PayOnline;
import com.product.bda.service.PayOnlineService;
import com.product.bda.service.SanVService;
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
import bsz.exch.utils.MD5Util;
import bsz.exch.utils.SSLClient;

@Handler(name="SanVPay")
public class SanVPayHandler {

	protected static String BASE_URL = "http://tscand01.3vpay.net/thirdPay/pay/gateway";
	
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
		String url = task.getParam("return_url");  //客户访问用的网址
		String pre=ThPayResource.instance().getConfig("3vpay."+product+".pre");
		
		String order_id = pre+"_" + String.valueOf(System.currentTimeMillis());
		String amount = task.getParam("amount");
		String order_amount = (Integer.parseInt(amount)*100)+"";
		
		String login_name = task.getParam("login_name");
		
		JdbcTemplate template=JdbcResource.instance().getJdbcTemplate(inter.getDataSource());
		String ds=inter.getDataSource();
		
		String partnerId =ThPayResource.instance().getConfig("3vpay."+product+".partnerId"); 
		
		
		
		String bank = task.getParam("bank");	//支付方式
		String payip = task.getParam("ip");	//支付ip
		String submit_value = "";
		String notifyUrl = "";
		String MD5Key ="";
		
		PayOnlineService poService= new PayOnlineService(template,ds);
		PayOnline online = poService.getPayOnline("3vpay", product);
		SanVService svservice  = new SanVService(template,ds);
		if(online != null){
			notifyUrl = online.notify_value;
			BASE_URL =online.return_value;
		}
		String timeStamp = System.currentTimeMillis()+"";
		String paytype="";
		String pay_method="";
		if(bank.equals("1001")){
			paytype="1400";
			pay_method="微信支付";
			MD5Key = ThPayResource.instance().getConfig("3vpay."+product+".KeyWECHAT");
		}else if(bank.equals("1003")){
			paytype="1800";
			pay_method="支付宝支付";
			MD5Key = ThPayResource.instance().getConfig("3vpay."+product+".KeyAli");
		}
		
		try{
			String sign = MD5Util.md5Encode("appId="+bank+"&timeStamp="+timeStamp+"&totalFee="+order_amount+"&key="+MD5Key+"");
			//System.out.println("appId="+bank+"&timeStamp="+timeStamp+"&totalFee="+order_amount+"&key="+MD5Key+"");
			StringBuffer sb =new StringBuffer();
			sb.append("appId="+bank+"&");
			sb.append("attach="+login_name+"&");
			sb.append("body=手机配件&");
			sb.append("channelOrderId="+order_id+"&");
			sb.append("notifyUrl="+notifyUrl+"&");
			sb.append("partnerId="+partnerId+"&");
			sb.append("payType="+paytype+"&");
			if(bank.equals("1003")){
				sb.append("returnUrl=www.baidu.com&");
			}
				
			sb.append("sign="+sign+"&");
			sb.append("timeStamp="+timeStamp+"&");
			sb.append("totalFee="+order_amount);
			RegistryBuilder<ConnectionSocketFactory> registryBuilder = RegistryBuilder.<ConnectionSocketFactory>create();  
	    	ConnectionSocketFactory plainSF = new PlainConnectionSocketFactory();  
	    	registryBuilder.register("http", plainSF);  
	    	Registry<ConnectionSocketFactory> registry = registryBuilder.build();
	    	PoolingHttpClientConnectionManager connManager  =new PoolingHttpClientConnectionManager(registry);  
	    	RequestConfig requestConfig =RequestConfig.custom().setSocketTimeout(35000).setConnectTimeout(35000).build();
	    	HttpClientBuilder build =HttpClientBuilder.create().setConnectionManager(connManager); ;
	    	
	    	CloseableHttpClient  httpclient= build.build(); 
	    	HttpGet httpget = new HttpGet(BASE_URL+"?"+sb.toString());  
	    	//System.out.println(BASE_URL+"?"+sb.toString());
			httpget.setConfig(requestConfig);
			HttpResponse response = httpclient.execute(httpget,HttpCoreContext.create());  
			if(response.getStatusLine().getStatusCode()==200){
				HttpEntity entity = response.getEntity(); 
				String result= EntityUtils.toString(entity,"utf-8"); 
				//System.out.println(result);
				JSONObject jsresult = JSONObject.fromObject(result);
				
				if(jsresult != null && jsresult.getString("return_code").equals("0")){//return_code
					
					svservice.create3vp(order_id, task.getParam("login_name"), amount, payip, pay_method, login_name,url);
					submit_value = jsresult.getJSONObject("payParam").getString("code_img_url");
					
					String barCode   = "";
					if(submit_value.contains("uuid=")){
						barCode = submit_value.split("uuid=")[1];
					}else{
						barCode = submit_value;
					}
					 
					
					Result r =Result.create(task.getId(), task.getFunId());
    				r.addFields(new String[]{"ok","order_id","message","order_amount","barCode"});
    				r.setFlag("-1");
    				r.setIsList(true);
    				r.setLength(1);
    				r.addRecord(new String[]{"1",order_id,"",amount,barCode});
    				return r;
    				
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		 Result r =Result.create(task.getId(), task.getFunId());
		 r.addFields(new String[]{"ok","order_id","message","order_amount","barCode"});
		 r.setFlag("-1");
		 r.setIsList(true);
		 r.setLength(1);
		 r.addRecord(new String[]{"0",order_id,"无法创建订单!",amount,""});
		 return r;
		//
		
	}
	
	@Service(name="notify")
	public Result notify(Task task,InterFace inter){
		String product = task.getParam("product");
		String return_code = task.getParam("return_code");
	    String channelOrderId = task.getParam("channelOrderId");
	    String orderId = task.getParam("orderId");
	    String timeStamp = task.getParam("timeStamp");
	    String sign = task.getParam("sign");
	    String attach = task.getParam("attach");
	    String transactionId = task.getParam("transactionId");
	    BigDecimal totalFee = new BigDecimal(task.getParam("totalFee"));
		
	    if ("0".equalsIgnoreCase(return_code)){
	    	
	    	String ds=ThPayResource.instance().getConfig("3vpay."+product+".datasource");
	    	String mer_key =ThPayResource.instance().getConfig("3vpay."+product+".MD5Key");
	    	
	    	//校验MD5码是不是正确的
	    	String signafter = MD5Util.md5Encode("channelOrderId="+channelOrderId+"&key="+mer_key+"&orderId="+orderId+"&timeStamp="+timeStamp+"&totalFee="+totalFee.intValue());
	    	if(signafter.equals(sign) ){
	    		JdbcTemplate template=JdbcResource.instance().getJdbcTemplate(ds);
	    		CreditService creditService =new CreditService(template,ds);
				CustomerService custService =new CustomerService(template,ds);
				DepositService depositService =new DepositService(template,ds);
				OrderNumberService orderNumberService =new OrderNumberService(template,ds);
				SanVService svservice  = new SanVService(template,ds);
				if(svservice.isNotDoYdp(channelOrderId)){
					
					svservice.updaeteXbp(channelOrderId, orderId, "2",  totalFee.divide(new BigDecimal(100)), new BigDecimal(0.988));
					
					orderNumberService.createOrderNumber(channelOrderId);
					
					String login_name = attach;
					if(creditService.add(login_name, totalFee.divide(new BigDecimal(100)),"自动充值", "3v在线支付:", login_name, channelOrderId)){
						try{
							Customer customer= custService.getCustomer(login_name);
							//添加存款记录和日志
							String dep_no = OrderNoService.createLocalNo("DE");
							depositService.addDeposit2(dep_no, customer.cust_id, login_name, customer.real_name, totalFee.divide(new BigDecimal(100)), "3v支付", "", "3v支付", "", channelOrderId);
							//查询是否第一次存款,如果是,升级用户等级
							
							if(depositService.NTgetCount(customer.cust_id) == 1){
								if(totalFee.divide(new BigDecimal(100)).intValue() >=100){
									custService.NTmodCustlevelFirst(customer.cust_id, 1);
								}else{
									custService.NTmodFirstDepositDateOnly(customer.cust_id, 1);
								}
								
							}else{ 
								if(customer.cust_level == 0){
									if(totalFee.divide(new BigDecimal(100)).intValue() >=100){
										custService.NTmodCustlevelOnly(customer.cust_id, 1);
									}
								}
							}
							
							if(customer.promo_flag != null && customer.promo_flag){
								ScoreService scoreService =new ScoreService(template,ds);
								if(scoreService.depositCountToday(login_name)==1 && (totalFee.divide(new BigDecimal(100))).intValue()>=100){
									scoreService.modScore(orderId, "签到积分",  new BigDecimal(1), login_name, customer.cust_id, "");
								}
								if((totalFee.divide(new BigDecimal(100))).intValue()>=100){
									scoreService.modScore(orderId, "存款积分",new BigDecimal(totalFee.divide(new BigDecimal(100)).divide(new BigDecimal(100)).intValue()), login_name, customer.cust_id, "");
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
