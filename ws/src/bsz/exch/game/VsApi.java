package bsz.exch.game;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpCoreContext;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import bsz.exch.bean.LogInfo;

public class VsApi {

private static Logger logger=Logger.getLogger(VsApi.class);
public static final ThreadLocal<LogInfo>  LOCAL_LOG  = new ThreadLocal<LogInfo>();
	
	public  String apiid=null;;
	public  String apihash=null;;
	public  String apikey=null;;
	public  String apiserver=null;;
	
	public static Pattern resultPattern = Pattern.compile("<result>(.*?)</result>");
	public static Pattern sessionPattern = Pattern.compile("<session_token>(.*?)</session_token>");
	
	
	private VsApi(){
		
	}
	
	private static HashMap<String,VsApi> map =new HashMap<String,VsApi>();
	
	public static VsApi get(String product){
		VsApi api =map.get(product);
		if(api==null){
			api= new VsApi();
			api.apiid=GameResource.instance().getConfig("game."+product+".vs.apiid");
			api.apihash=GameResource.instance().getConfig("game."+product+".vs.apihash");
			api.apikey=GameResource.instance().getConfig("game."+product+".vs.apikey");
			api.apiserver=GameResource.instance().getConfig("game."+product+".vs.apiserver");
			
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
	
	private  PoolingHttpClientConnectionManager connManager;
	private  RequestConfig requestConfig;
	private  HttpClientBuilder build;
	
	public  String isAuthenticate(){
		String session = "";
		String url=apiserver;
		StringBuffer sb =new StringBuffer();
		sb.append("?method=auth&");
		sb.append("uid="+apiid+"&");
		sb.append("hash="+apihash+"&");
		sb.append("key="+apikey+"");
		String dataresult= doRequest(url,sb.toString());
		if(dataresult==null) 
			return "";
		Matcher resultMatcher = resultPattern.matcher(dataresult);
		if (resultMatcher.find()) {
			
			String result = resultMatcher.group(1);
			
			if(result != null && result.equals("success")){
				Matcher sessionMatcher = sessionPattern.matcher(dataresult);
				if (sessionMatcher.find()) {
					session = sessionMatcher.group(1);
				}
			}
		}
		return session;
	}
	
	public  String adduser(String username,String token,long extid,String passwd){
		
		String url=apiserver;
		StringBuffer sb =new StringBuffer();
		sb.append("?method=add_shadow_user&");
		sb.append("token="+token+"&");
		sb.append("extid="+extid+"&");
		sb.append("passwd="+passwd+"&");
		sb.append("currency="+"CNY"+"&");
		sb.append("username="+username);
		String dataresult= doRequest(url,sb.toString());
		System.out.println(dataresult);
//		if(dataresult==null) 
//			return "";
//		Matcher resultMatcher = resultPattern.matcher(dataresult);
//		if (resultMatcher.find()) {
//			
//			String result = resultMatcher.group(1);
//			
//			if(result != null && result.equals("success")){
//				Matcher sessionMatcher = sessionPattern.matcher(dataresult);
//				if (sessionMatcher.find()) {
//					//session = sessionMatcher.group(1);
//				}
//			}
//		}
		return "";
	}
	
	public  String get_currency_list(String token){
		
		String url=apiserver;
		StringBuffer sb =new StringBuffer();
		sb.append("?method=get_currency_list&");
		sb.append("token="+token+"");
		String dataresult= doRequest(url,sb.toString());
		System.out.println(dataresult);
		return "";
	}
	
	public  String doRequest(String url,String ps){
		CloseableHttpClient  httpclient= build.build(); 
		LOCAL_LOG.set(new LogInfo());
		LOCAL_LOG.get().addLog("|Game_VS -");
		try{
			url=url+ps;
			HttpGet httpget = new HttpGet(url);  
			//System.out.println(url);
			httpget.setConfig(requestConfig);
			LOCAL_LOG.get().addLog("  Params:"+ps);
			HttpResponse response = httpclient.execute(httpget,HttpCoreContext.create()); 
			LOCAL_LOG.get().addLog("  Reponse:"+response.getStatusLine());
			if(response.getStatusLine().getStatusCode()==200){
				HttpEntity entity = response.getEntity(); 
		        if (entity != null) {
		        	 String result= EntityUtils.toString(entity,"utf-8"); 
		        	 return result;
		        }
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static void main(String[] args){
		//System.out.println(VsApi.get("bojin").isAuthenticate());
		VsApi.get("bojin").adduser("woody",(VsApi.get("bojin").isAuthenticate()),1l,"41a80915fcc94ce6");
	}
	
}
