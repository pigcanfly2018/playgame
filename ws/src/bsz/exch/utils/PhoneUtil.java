package bsz.exch.utils;

import java.security.KeyStore;
import java.util.HashMap;
import java.util.regex.Matcher;

import javax.net.ssl.SSLContext;

import net.sf.json.JSONObject;

import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import bsz.exch.game.GameResource;


public class PhoneUtil {
	
private static Logger logger=Logger.getLogger(PhoneUtil.class);
	
	public static String accessE164=null;;
	public static String accessE164Password=null;
	public static String localnumber=null;
	public static String url=null;
	
	private PhoneUtil(){
		
	}
	
	private static HashMap<String,PhoneUtil> map =new HashMap<String,PhoneUtil>();
	
	
	public static PhoneUtil get(String product){
		PhoneUtil api =map.get(product);
		if(api==null){
			api= new PhoneUtil();
			api.accessE164=GameResource.instance().getConfig("game."+product+".phone.accessE164");
			api.localnumber=GameResource.instance().getConfig("game."+product+".phone.localnumber");
			api.accessE164Password=GameResource.instance().getConfig("game."+product+".phone.accessE164Password");
			api.url=GameResource.instance().getConfig("game."+product+".phone.url");
			
			RegistryBuilder<ConnectionSocketFactory> registryBuilder = RegistryBuilder.<ConnectionSocketFactory>create();  
        	ConnectionSocketFactory plainSF = new PlainConnectionSocketFactory();  
        	registryBuilder.register("http", plainSF);  
        	Registry<ConnectionSocketFactory> registry = registryBuilder.build();
        	api.connManager = new PoolingHttpClientConnectionManager(registry);  
        	api.requestConfig = RequestConfig.custom().setSocketTimeout(35000).setConnectTimeout(35000).build();
        
        	api.build=HttpClientBuilder.create().setConnectionManager(api.connManager);
        	map.put(product, api);
		}
		return api;
		
		
	}
	
	private static PoolingHttpClientConnectionManager connManager;
	private static RequestConfig requestConfig;
	private static HttpClientBuilder build;
	
	
	public static boolean callphone(String calleeE164s){
		CloseableHttpClient  hc= build.build(); 
		HttpPost httppost = new HttpPost(url);  
		httppost.setConfig(requestConfig);
		httppost.setHeader("Content-Type", "application/json;charset=UTF-8");
		String jsonString = "{\"callerDisplayNumber\": \""+calleeE164s+"\",\"callerE164\": \""+localnumber+"\", \"calleeE164s\":\""+calleeE164s+"\", \"accessE164\": \""+accessE164+"\",\"accessE164Password\":\""+accessE164Password+"\"}";
		System.out.println(jsonString);
		HttpResponse response = null;
		try{
			
			StringEntity entity = new StringEntity(jsonString, "UTF-8"); 
			httppost.setEntity(entity); 
			response = hc.execute(httppost); 
			String result = EntityUtils.toString(response.getEntity(), "utf-8");
			System.out.println(result);
			logger.info("callphone:"+url   +result);
			JSONObject jsresult = JSONObject.fromObject( result ); 
			
			if(jsresult.getString("retCode").equals("0"))
				return true;
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			// hc.getConnectionManager().shutdown();
		}
		return false;
	}
	
	public static  void main(String []args){
		PhoneUtil.get("8da").callphone( "13144842043");
		String str = "{\"retCode\":0}";
		JSONObject jsresult = JSONObject.fromObject( str ); 
		System.out.println(jsresult.get("retCode"));
	}
	
	

}
