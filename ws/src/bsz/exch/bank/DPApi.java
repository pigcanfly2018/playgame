package bsz.exch.bank;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import bsz.exch.bean.LogInfo;
import bsz.exch.utils.MD5Util;


//存款，提款API
public class DPApi {
	
	private static Logger logger=Logger.getLogger(DPApi.class);
    public static final ThreadLocal<LogInfo>  LOCAL_LOG  = new ThreadLocal<LogInfo>();
    
	public static Map<String,DPApi> map= new HashMap<String,DPApi>();
	 
	private String company_id="";
	private String apiUrl="";
	private String keyconfig="";
	private String deposit_notify_url="";
	
	public String getCompanyId(){
		return this.company_id;
	}
	
	public String getDepositNotifyUrl(){
		return this.deposit_notify_url;
	}
	
	public static DPApi get(String product){
		DPApi a=map.get(product);
		if(a==null){
			DPApi api=new DPApi();
			api.company_id=PayResource.instance().getConfig("dpay."+product+".company_id");
			api.apiUrl=PayResource.instance().getConfig("dpay."+product+".apiUrl");
			api.keyconfig=PayResource.instance().getConfig("dpay."+product+".keyconfig");
			api.deposit_notify_url=PayResource.instance().getConfig("dpay."+product+".deposit_notify_url");
	    	return api;
		}
		return a;
	}
	
	private  PoolingHttpClientConnectionManager connManager;
	private  RequestConfig requestConfig;
	private  HttpClientBuilder build;
	
	private DPApi(){
		RegistryBuilder<ConnectionSocketFactory> registryBuilder = RegistryBuilder.<ConnectionSocketFactory>create();  
    	ConnectionSocketFactory plainSF = new PlainConnectionSocketFactory();  
    	registryBuilder.register("http", plainSF);  
    	Registry<ConnectionSocketFactory> registry = registryBuilder.build();
    	connManager = new PoolingHttpClientConnectionManager(registry);  
    	requestConfig = RequestConfig.custom().setSocketTimeout(35000).setConnectTimeout(35000).build();
    	build=HttpClientBuilder.create().setConnectionManager(connManager);
	}
	
	//存款
	public JSONObject deposit(String bank_id,String amount,String company_order_num,String company_user,String estimated_payment_bank,String deposit_mode,
			String group_id,String memo,String note,String note_model,String terminal){
		String url=apiUrl+ "/Deposit?format=json";
		LOCAL_LOG.set(new LogInfo());
		LOCAL_LOG.get().addLog("|URL="+url+" ");
		LOCAL_LOG.get().addLog("|Deposit - Params:{");
		CloseableHttpClient  hc= build.build(); 
		
		HttpPost httppost = new HttpPost(url);
		httppost.setConfig(requestConfig);
		StringBuffer sb =new StringBuffer();
		sb.append(company_id);
		sb.append(bank_id);
		sb.append(amount);
		sb.append(company_order_num);
		sb.append(company_user);
		sb.append(estimated_payment_bank);
		sb.append(deposit_mode);
		sb.append(group_id);
		sb.append(deposit_notify_url);
		sb.append(memo);
		sb.append(note);
		sb.append(note_model);
		String key=MD5Util.MD5(MD5Util.MD5(keyconfig).toLowerCase()+sb.toString()).toLowerCase();
		System.out.println(sb.toString());
		System.out.println("key="+key);
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("company_id", company_id));
		LOCAL_LOG.get().addLog("company_id="+company_id+",");
		
		nvps.add(new BasicNameValuePair("bank_id", bank_id));
		LOCAL_LOG.get().addLog("bank_id="+bank_id+",");
		
		nvps.add(new BasicNameValuePair("amount", amount));
		LOCAL_LOG.get().addLog("amount="+amount+",");
		
		nvps.add(new BasicNameValuePair("company_order_num", company_order_num));
		LOCAL_LOG.get().addLog("company_order_num="+company_order_num+",");
		
		nvps.add(new BasicNameValuePair("company_user", company_user));
		LOCAL_LOG.get().addLog("company_user="+company_user+",");
			
		nvps.add(new BasicNameValuePair("estimated_payment_bank", estimated_payment_bank));
		LOCAL_LOG.get().addLog("estimated_payment_bank="+estimated_payment_bank+",");
		
		nvps.add(new BasicNameValuePair("deposit_mode", deposit_mode));
		LOCAL_LOG.get().addLog("deposit_mode="+deposit_mode+",");
		
		nvps.add(new BasicNameValuePair("group_id", group_id));
		LOCAL_LOG.get().addLog("group_id="+group_id+",");
		
		nvps.add(new BasicNameValuePair("web_url", deposit_notify_url));
		LOCAL_LOG.get().addLog("web_url="+deposit_notify_url+",");
		
		nvps.add(new BasicNameValuePair("memo", memo));
		LOCAL_LOG.get().addLog("memo="+memo+",");
		
		nvps.add(new BasicNameValuePair("note", note));
		LOCAL_LOG.get().addLog("note="+note+",");
		
		nvps.add(new BasicNameValuePair("note_model", note_model));
		LOCAL_LOG.get().addLog("note_model="+note_model+",");
		
		nvps.add(new BasicNameValuePair("key", key));
		LOCAL_LOG.get().addLog("key="+key+",");
		
		nvps.add(new BasicNameValuePair("terminal", terminal));
		LOCAL_LOG.get().addLog("terminal="+terminal+"}");
		
		try {
			httppost.setEntity(new UrlEncodedFormEntity(nvps,"utf-8"));
			httppost.addHeader("Accept-Charset","utf-8;q=0.7,*;q=0.3");
			HttpResponse response = hc.execute(httppost);
			LOCAL_LOG.get().addLog(" Response Code ="+response.getStatusLine().getStatusCode());
			if (response.getStatusLine().getStatusCode() == 200) {
				String result=EntityUtils.toString(response.getEntity());
				LOCAL_LOG.get().addLog(" Content: "+result);
				JSONObject jsresult = JSONObject.fromObject(result);  
				return jsresult;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			 System.out.println(LOCAL_LOG.get().getLog());
			 logger.info(LOCAL_LOG.get().getLog());
		}
		return null;
	}

	//提现	 
	 public JSONObject withdraw(String bank_id,String company_order_num,String amount,String card_num,String card_name,String company_user,String issue_bank_name,
			 String issue_bank_address){
		    String url=apiUrl+ "/Withdrawal?format=json";
			LOCAL_LOG.set(new LogInfo());
			LOCAL_LOG.get().addLog("|URL="+url+" ");
			LOCAL_LOG.get().addLog("|Withdraw - Params:{");
			CloseableHttpClient  hc= build.build(); 
			
			HttpPost httppost = new HttpPost(url);
			httppost.setConfig(requestConfig);
			String memo="";
			StringBuffer sb =new StringBuffer();
			sb.append(company_id);
			LOCAL_LOG.get().addLog("company_id="+company_id+",");
			
			sb.append(bank_id);
			LOCAL_LOG.get().addLog("bank_id="+bank_id+",");
			
			sb.append(company_order_num);
			LOCAL_LOG.get().addLog("company_order_num="+company_order_num+",");
			
			sb.append(amount);
			LOCAL_LOG.get().addLog("amount="+amount+",");
			
			sb.append(card_num);
			LOCAL_LOG.get().addLog("card_num="+card_num+",");
			
			sb.append(card_name);
			LOCAL_LOG.get().addLog("card_name="+card_name+",");
			
			sb.append(company_user);
			LOCAL_LOG.get().addLog("company_user="+company_user+",");
			
			sb.append(issue_bank_name);
			LOCAL_LOG.get().addLog("issue_bank_name="+issue_bank_name+",");
			
			sb.append(issue_bank_address);
			LOCAL_LOG.get().addLog("issue_bank_address="+issue_bank_address+",");
			
			sb.append(memo);
			LOCAL_LOG.get().addLog("memo="+memo+",");
			
			String key=MD5Util.MD5(MD5Util.MD5(keyconfig).toLowerCase()+sb.toString()).toLowerCase();
			LOCAL_LOG.get().addLog("key="+key+"}");
			
			
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("company_id", company_id));
			nvps.add(new BasicNameValuePair("bank_id", bank_id));
			nvps.add(new BasicNameValuePair("company_order_num", company_order_num));
			nvps.add(new BasicNameValuePair("amount", amount));
			nvps.add(new BasicNameValuePair("card_num", card_num));
			nvps.add(new BasicNameValuePair("card_name", card_name));
			nvps.add(new BasicNameValuePair("company_user", company_user));
			nvps.add(new BasicNameValuePair("issue_bank_name", issue_bank_name));
			nvps.add(new BasicNameValuePair("issue_bank_address", issue_bank_address));
			nvps.add(new BasicNameValuePair("memo", memo));
			nvps.add(new BasicNameValuePair("key", key));
			
			try {
				httppost.setEntity(new UrlEncodedFormEntity(nvps,"utf-8"));
				HttpResponse response = hc.execute(httppost);
				LOCAL_LOG.get().addLog(" Response Code ="+response.getStatusLine().getStatusCode());
				if (response.getStatusLine().getStatusCode() == 200) {
					String result=EntityUtils.toString(response.getEntity());
					System.out.println(result);
					LOCAL_LOG.get().addLog(" Content: "+result);
					JSONObject jsresult = JSONObject.fromObject(result);  
					return jsresult;
				} 
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				 System.out.println(LOCAL_LOG.get().getLog());
				 logger.info(LOCAL_LOG.get().getLog());
			  }
			return null;
	 }
	 
	 
	 /**
	  * 充值取消
	    company_order_num
		mownecum_order_num
		status
		error_msg
	  * @return
	  */
	 public JSONObject DepositCancel(String mownecum_order_num,String company_order_num ){
			String url=apiUrl+ "/DepositCancel?format=json";
			LOCAL_LOG.set(new LogInfo());
			LOCAL_LOG.get().addLog("|URL="+url+" ");
			LOCAL_LOG.get().addLog("|DepositCancel - Params:{");
			
			CloseableHttpClient  hc= build.build(); 
			HttpPost httppost = new HttpPost(url);
			httppost.setConfig(requestConfig);
			StringBuffer sb =new StringBuffer();
			sb.append(company_id);
			LOCAL_LOG.get().addLog("company_id="+company_id+",");
			
			sb.append(mownecum_order_num);
			LOCAL_LOG.get().addLog("mownecum_order_num="+mownecum_order_num+",");
			
			sb.append(company_order_num);
			LOCAL_LOG.get().addLog("company_order_num="+company_order_num+",");
		
			String key=MD5Util.MD5(MD5Util.MD5(keyconfig).toLowerCase()+sb.toString()).toLowerCase();
			LOCAL_LOG.get().addLog("key="+key+"}");
			
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("company_id", company_id));
			nvps.add(new BasicNameValuePair("mownecum_order_num", mownecum_order_num));
			nvps.add(new BasicNameValuePair("company_order_num", company_order_num));
			nvps.add(new BasicNameValuePair("key", key));
			try {
				httppost.setEntity(new UrlEncodedFormEntity(nvps,"utf-8"));
				HttpResponse response = hc.execute(httppost);
				LOCAL_LOG.get().addLog(" Response Code ="+response.getStatusLine().getStatusCode());
				if (response.getStatusLine().getStatusCode() == 200) {
					String result=EntityUtils.toString(response.getEntity());
					System.out.println(result);
					LOCAL_LOG.get().addLog(" Content: "+result);
					JSONObject jsresult = JSONObject.fromObject(result);  
					return jsresult;
					
				} 
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				 System.out.println(LOCAL_LOG.get().getLog());
				 logger.info(LOCAL_LOG.get().getLog());
			  }
			return  null;
	 }
	 
	 
	 
	 
	 
	 
	 /**
	  * 查询提款订单
	  * @param mownecum_order_num
	  * @param company_order_num
	  * @return
	  */
 public JSONObject QueryWithdrawal(String mownecum_order_num,String company_order_num){
		 
			String url=apiUrl+ "/QueryWithdrawal?format=json";
			LOCAL_LOG.set(new LogInfo());
			LOCAL_LOG.get().addLog("|URL="+url+" ");
			LOCAL_LOG.get().addLog("|DepositCancel - Params:{");
			
			CloseableHttpClient  hc= build.build(); 
			HttpPost httppost = new HttpPost(url);
			httppost.setConfig(requestConfig);
			StringBuffer sb =new StringBuffer();
			sb.append(company_id);
			LOCAL_LOG.get().addLog("company_id="+company_id+",");
			
			sb.append(mownecum_order_num);
			LOCAL_LOG.get().addLog("mownecum_order_num="+mownecum_order_num+",");
			
			sb.append(company_order_num);
			LOCAL_LOG.get().addLog("company_order_num="+company_order_num+",");
		
			String key=MD5Util.MD5(MD5Util.MD5(keyconfig).toLowerCase()+sb.toString()).toLowerCase();
			LOCAL_LOG.get().addLog("key="+key+"}");
			
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("company_id", company_id));
			nvps.add(new BasicNameValuePair("mownecum_order_num", mownecum_order_num));
			nvps.add(new BasicNameValuePair("company_order_num", company_order_num));
			nvps.add(new BasicNameValuePair("key", key));

			try {
				httppost.setEntity(new UrlEncodedFormEntity(nvps));
				HttpResponse response = hc.execute(httppost);
				LOCAL_LOG.get().addLog(" Response Code ="+response.getStatusLine().getStatusCode());
				if (response.getStatusLine().getStatusCode() == 200) {
					String result=EntityUtils.toString(response.getEntity());
					System.out.println(result);
					LOCAL_LOG.get().addLog(" Content: "+result);
					JSONObject jsresult = JSONObject.fromObject(result);  
					return jsresult;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				 System.out.println(LOCAL_LOG.get().getLog());
				 logger.info(LOCAL_LOG.get().getLog());
			  }
			return null;
	 }
 
 
	 
	 
	 
	 public static void main(String []args){
		 DPApi api= DPApi.get("8da");
		 //api.withdraw("1", ""+System.currentTimeMillis(), "100.00","4545154512124", "张三", "lance008","", "广东深圳");
		 JSONObject jsresult=api.QueryWithdrawal("SM2015090901014703333", "BDW588082697890992");
		 System.out.println(jsresult);
		// System.out.println(api.depositBank("100.00", ""+System.currentTimeMillis(), "lance008","1","yunskk"));
		// String result=api.depositQRCode("100.00", ""+System.currentTimeMillis(), "lance008", "明635124","alipay");
		// JSONObject jsresult = JSONObject.fromObject(result);  
		 
		 //System.out.println(jsresult.get("status"));
		// api.depositAlipay("100.00", ""+System.currentTimeMillis(), "lance008", "1635124");
		 

	 }

}
