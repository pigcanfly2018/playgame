package bsz.exch.game;

import java.math.BigDecimal;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.SSLContext;

import net.sf.json.JSONObject;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import bsz.exch.utils.AnyTrustStrategy;

public class MgApi {

	private static Logger logger=Logger.getLogger(MgApi.class);
	
	public  String loginName=null;;
	public  String pinCode=null;;
	public  String url=null;;
	public String tournamentsurl = null;
	
	public static Pattern sessionPattern = Pattern.compile("<SessionGUID>(.*?)</SessionGUID>");
	public static Pattern faultstringPattern = Pattern.compile("<faultstring>(.*?)</faultstring>");
	public static Pattern errorMessagePattern = Pattern.compile("<ErrorMessage>(.*?)</ErrorMessage>");
	public static Pattern balancePattern = Pattern.compile("<Balance>(.*?)</Balance>");
	public static Pattern successPattern = Pattern.compile("<IsSucceed>(.*?)</IsSucceed>");
	
	private  String SessionGUID ="";
	
	private MgApi(){
		
	}
	
	private static HashMap<String,MgApi> map =new HashMap<String,MgApi>();
	
	public static MgApi get(String product){
		MgApi api =map.get(product);
		if(api==null){
			api= new MgApi();
			api.loginName=GameResource.instance().getConfig("game."+product+".mg.loginName");
			api.pinCode=GameResource.instance().getConfig("game."+product+".mg.pinCode");
			api.url=GameResource.instance().getConfig("game."+product+".mg.url");
			api.tournamentsurl=GameResource.instance().getConfig("game."+product+".mg.tournamentsurl");
			RegistryBuilder<ConnectionSocketFactory> registryBuilder = RegistryBuilder.<ConnectionSocketFactory>create();  	
	    	try{
	    	KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());  
	 	    SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(trustStore, new AnyTrustStrategy()).build();  
	 	    ConnectionSocketFactory sslSF = new SSLConnectionSocketFactory(sslContext);  
	 	    registryBuilder.register("https", sslSF); 
	    	}catch(Exception d){
	    		d.printStackTrace();
	    	}
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
		CloseableHttpClient  hc= build.build(); 
		String session = "";

		HttpPost httppost = new HttpPost(url);  
		httppost.setConfig(requestConfig);
		httppost.setHeader("Content-Type", "text/xml;charset=UTF-8");
		String isAuthenticateXml = MgXmlUtil.getIsAuthenticateXml(loginName, pinCode);
		HttpResponse response = null;
		try{
			
			StringEntity entity = new StringEntity(isAuthenticateXml, "UTF-8"); 
			httppost.setEntity(entity); 
			response = hc.execute(httppost); 
			String result = EntityUtils.toString(response.getEntity(), "utf-8");
			logger.info("isAuthenticate:"+result);
			Matcher sessionMatcher = sessionPattern.matcher(result);
			if (sessionMatcher.find()) {
				session = sessionMatcher.group(1);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			// hc.getConnectionManager().shutdown();
		}
		return session;
	}
	
	
	public  boolean addAccount(String accountNumber,String password,String ip){
		if(SessionGUID == null || SessionGUID.equals("")){
			SessionGUID = isAuthenticate();
		 }
		CloseableHttpClient  hc= build.build(); 
		HttpPost httppost = new HttpPost(url);  
		httppost.setConfig(requestConfig);
		httppost.setHeader("Content-Type", "text/xml;charset=UTF-8");
		String addAccountXml = MgXmlUtil.getAddAccountXml(accountNumber,password,ip,SessionGUID);
		HttpResponse response = null;
		try{
			StringEntity entity = new StringEntity(addAccountXml, "UTF-8"); 
			httppost.setEntity(entity); 
			response = hc.execute(httppost); 
			String result = EntityUtils.toString(response.getEntity(), "utf-8");
			logger.info("addAccount:"+result);
			//System.out.println(result);
			boolean succeed = isSucceed(result);
			if(succeed ){
				return true;
			}else{//用新的session重新请求创建帐号
				String newAddAccountXml = MgXmlUtil.getAddAccountXml(accountNumber,password,ip,SessionGUID);
				entity = new StringEntity(newAddAccountXml, "UTF-8"); 
				httppost.setEntity(entity);
				response = hc.execute(httppost); 
				result = EntityUtils.toString(response.getEntity(), "utf-8");
				logger.info("addAccountagain:"+result);
				//System.out.println(result);
				if(isSucceed(result)){
					return true;
				}else{
					return false;
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			 //hc.getConnectionManager().shutdown();
		}
		return false;
	}
	
	public boolean registerAlias(String mg_game,String Alias){
		CloseableHttpClient  hc= build.build(); 
		String posturl = tournamentsurl +"/v1/alias/add/1";
		HttpPost httppost = new HttpPost(posturl);  
		System.out.println(posturl);
		String hashPincode = loginName+":"+pinCode;
		HttpResponse response = null;
		try{
			httppost.setHeader("Authorization", "Basic "+ new String(new Base64().encode(hashPincode.getBytes("UTF-8"))));
			httppost.setConfig(requestConfig);
			httppost.setHeader("Accept", "application/json");
			
			ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
			param.add(new BasicNameValuePair("AccountNumber", mg_game));
			param.add(new BasicNameValuePair("Alias", Alias));
			httppost.setEntity(new UrlEncodedFormEntity(param));
			response = hc.execute(httppost);
			String result=EntityUtils.toString(response.getEntity());
			JSONObject jsresult = JSONObject.fromObject( result ); 
			System.out.println(jsresult);
			return jsresult.containsKey("Alias");
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
		
	}
	
	public  boolean Transfer(String loginname,String billno,String type,BigDecimal amount){
		try{
			if("IN".equals(type)){
				return deposit(loginname,billno,amount);
			}
			if("OUT".equals(type)){
				return withdrawal(loginname,billno,amount);
			}
			return false;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		
	}
	
	public  boolean deposit(String accountNumber,String billno,BigDecimal amount){
		
		if(SessionGUID == null || SessionGUID.equals("")){
			SessionGUID =isAuthenticate();
		 }
		
		
		CloseableHttpClient  hc= build.build(); 
		HttpPost httppost = new HttpPost(url);  
		httppost.setConfig(requestConfig);
		httppost.setHeader("Content-Type", "text/xml;charset=UTF-8");
		String depositXml = MgXmlUtil.getDepositXml(accountNumber,billno,amount,SessionGUID);
		HttpResponse response = null;
		try{
			
			StringEntity entity = new StringEntity(depositXml, "UTF-8"); 
			httppost.setEntity(entity); 
			response = hc.execute(httppost); 
			String result = EntityUtils.toString(response.getEntity(), "utf-8");
			logger.info("deposit:"+result);
			System.out.println(depositXml+"|"+result);
			boolean succeed = isSucceed(result);
			if(succeed){// 有可能其他错误
				return true;
			}else{//用新的session重新请求创建帐号
				String newDeposittXml = MgXmlUtil.getDepositXml(accountNumber,billno,amount,SessionGUID);
				entity = new StringEntity(newDeposittXml, "UTF-8"); 
				httppost.setEntity(entity);
				response = hc.execute(httppost); 
				result = EntityUtils.toString(response.getEntity(), "utf-8");
				logger.info("depositagain:"+result);
				if(isSucceed(result)){
					return true;
				}else{
					return false;
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			// hc.getConnectionManager().shutdown();
		}
		return false;
	}
	
	
	public  boolean withdrawal(String accountNumber,String billno,BigDecimal amount){
		if(SessionGUID == null || SessionGUID.equals("")){
			SessionGUID =isAuthenticate();
		 }
		
		CloseableHttpClient  hc= build.build(); 
		HttpPost httppost = new HttpPost(url);  
		httppost.setConfig(requestConfig);
		httppost.setHeader("Content-Type", "text/xml;charset=UTF-8");
		String withdrawalXml = MgXmlUtil.getWithdrawalXml(accountNumber,billno,amount,SessionGUID);
		HttpResponse response = null;
		try{
			
			StringEntity entity = new StringEntity(withdrawalXml, "UTF-8"); 
			httppost.setEntity(entity); 
			response = hc.execute(httppost); 
			String result = EntityUtils.toString(response.getEntity(), "utf-8");
			logger.info("withdrawal:"+result);
			//System.out.println(result);
			boolean succeed = isSucceed(result);
			if(succeed){// 有可能其他错误
				return true;
			}else{//用新的session重新请求创建帐号
				String newWithdrawalXml = MgXmlUtil.getWithdrawalXml(accountNumber,billno,amount,SessionGUID);
				entity = new StringEntity(newWithdrawalXml, "UTF-8"); 
				httppost.setEntity(entity);
				response = hc.execute(httppost); 
				result = EntityUtils.toString(response.getEntity(), "utf-8");
				logger.info("withdrawalagain:"+result);
				if(isSucceed(result)){
					return true;
				}else{
					return false;
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			// hc.getConnectionManager().shutdown();
		}
		return false;
	}
	
	public  boolean updatePassword(String accountNumber,String password){
		
		if(SessionGUID == null || SessionGUID.equals("")){
			SessionGUID = isAuthenticate();
		 }
		
		CloseableHttpClient  hc= build.build(); 
		HttpPost httppost = new HttpPost(url);  
		httppost.setConfig(requestConfig);
		httppost.setHeader("Content-Type", "text/xml;charset=UTF-8");
		String editAccountXml = MgXmlUtil.getEditAccountXml(accountNumber,password,SessionGUID);
		HttpResponse response = null;
		try{
			
			StringEntity entity = new StringEntity(editAccountXml, "UTF-8"); 
			httppost.setEntity(entity); 
			response = hc.execute(httppost); 
			String result = EntityUtils.toString(response.getEntity(), "utf-8");
			logger.info("updatePassword:"+result);
			//System.out.println(result);
			boolean succeed = isSucceed(result);
			if(succeed){// 有可能其他错误
				return true;
			}else{//用新的session重新请求创建帐号
				String newEditAccountXml = MgXmlUtil.getEditAccountXml(accountNumber,password,SessionGUID);
				entity = new StringEntity(newEditAccountXml, "UTF-8"); 
				httppost.setEntity(entity);
				response = hc.execute(httppost); 
				result = EntityUtils.toString(response.getEntity(), "utf-8");
				logger.info("updatePasswordagain:"+result);
				if(isSucceed(result)){
					return true;
				}else{
					return false;
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			 //hc.getConnectionManager().shutdown();
		}
		return false;
	}
	
	public  BigDecimal GetBalance(String accountNumber){
		if(SessionGUID == null || SessionGUID.equals("")){
			SessionGUID =isAuthenticate();
			System.out.println(SessionGUID);
		 }
		CloseableHttpClient  hc= build.build(); 
		HttpPost httppost = new HttpPost(url);  
		httppost.setConfig(requestConfig);
		httppost.setHeader("Content-Type", "text/xml;charset=UTF-8");
		String getAccountDetailsXml = MgXmlUtil.getGetAccountBalanceXml(accountNumber,SessionGUID);
		System.out.println(getAccountDetailsXml);
		HttpResponse response = null;
		try{
			StringEntity entity = new StringEntity(getAccountDetailsXml, "UTF-8"); 
			httppost.setEntity(entity); 
			response = hc.execute(httppost); 
			String result = EntityUtils.toString(response.getEntity(), "utf-8");
			logger.info("GetBalance:"+result);
			System.out.println(getAccountDetailsXml+"|"+result);
			boolean succeed = isSucceed(result);
			if(succeed){// 有可能其他错误
				Matcher balanceMatcher = balancePattern.matcher(result);
				if (balanceMatcher.find()) {
					//request.setAttribute("SessionGUID", SessionGUID);
					return new BigDecimal(balanceMatcher.group(1));
				}else{
					return new BigDecimal(0);
				}
			}else{
				String newGetAccountDetailsXml = MgXmlUtil.getGetAccountBalanceXml(accountNumber,SessionGUID);
				entity = new StringEntity(newGetAccountDetailsXml, "UTF-8"); 
				httppost.setEntity(entity);
				response = hc.execute(httppost); 
				result = EntityUtils.toString(response.getEntity(), "utf-8");
				logger.info("GetAccountDetailsagain:"+result);
				System.out.println(result);
				Matcher balanceMatcher = balancePattern.matcher(result);
				if (balanceMatcher.find()) {
					//request.setAttribute("SessionGUID", SessionGUID);
					return new BigDecimal(balanceMatcher.group(1));
				}else{
					return new BigDecimal(0);
				}
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			// hc.getConnectionManager().shutdown();
		}
		return new BigDecimal(0);
	}
	
	
	public  BigDecimal GetAccountBalance(String accountNumber){
		if(SessionGUID == null || SessionGUID.equals("")){
			SessionGUID =isAuthenticate();
			System.out.println(SessionGUID);
		 }
		CloseableHttpClient  hc= build.build(); 
		HttpPost httppost = new HttpPost(url);  
		httppost.setConfig(requestConfig);
		httppost.setHeader("Content-Type", "text/xml;charset=UTF-8");
		String getAccountDetailsXml = MgXmlUtil.getGetAccountBalanceXml(accountNumber,SessionGUID);
		System.out.println(getAccountDetailsXml);
		HttpResponse response = null;
		try{
			StringEntity entity = new StringEntity(getAccountDetailsXml, "UTF-8"); 
			httppost.setEntity(entity); 
			response = hc.execute(httppost); 
			String result = EntityUtils.toString(response.getEntity(), "utf-8");
			logger.info("GetBalance:"+result);
			System.out.println(getAccountDetailsXml+"|"+result);
			boolean succeed = isSucceed(result);
			if(succeed){// 有可能其他错误
				Matcher balanceMatcher = balancePattern.matcher(result);
				if (balanceMatcher.find()) {
					//request.setAttribute("SessionGUID", SessionGUID);
					return new BigDecimal(balanceMatcher.group(1));
				}else{
					return new BigDecimal(0);
				}
			}else{
				String newGetAccountDetailsXml = MgXmlUtil.getGetAccountBalanceXml(accountNumber,SessionGUID);
				entity = new StringEntity(newGetAccountDetailsXml, "UTF-8"); 
				httppost.setEntity(entity);
				response = hc.execute(httppost); 
				result = EntityUtils.toString(response.getEntity(), "utf-8");
				logger.info("getGetAccountBalanceagain:"+result);
				System.out.println(result);
				Matcher balanceMatcher = balancePattern.matcher(result);
				if (balanceMatcher.find()) {
					//request.setAttribute("SessionGUID", SessionGUID);
					return new BigDecimal(balanceMatcher.group(1));
				}else{
					return new BigDecimal(0);
				}
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			// hc.getConnectionManager().shutdown();
		}
		return new BigDecimal(0);
	}
	
	
	
	
	public  boolean isSucceed(String xml){ //判断session是否过期,如果过期则返回新的session
		Matcher successMatcher = successPattern.matcher(xml);
		if (successMatcher.find()) {
			String IsSucceed = successMatcher.group(1);
			if(IsSucceed.equals("true")){
				return true;
			}
		}
		
		Matcher errorMessageMatcher = errorMessagePattern.matcher(xml);
		if (errorMessageMatcher.find()) {
			String faultstring = errorMessageMatcher.group(1);
			if(faultstring.startsWith("The session has expired") || faultstring.startsWith("Session has already expired")){
				SessionGUID  = isAuthenticate();
			}
			
		}
		return false;
		
	}
	
	public static  void main(String []args){
		
		//isAuthenticate(loginName,pinCode);
		//System.out.println(updatePassword("f8af2f75-e1db-4570-8789-2ed887aaa185","dawvisitor08","a123a123",null));
		//deposit("f8af2f75-e1db-4570-8789-2ed887aaa185","dawvisitor09",new BigDecimal(100));
		//System.out.println(GetBalance("f8af2f75-e1db-4570-8789-2ed887aaa185","dawvisitor08"));
		//withdrawal("f8af2f75-e1db-4570-8789-2ed887aaa185","dawvisitor08",new BigDecimal(50));
		System.out.println(MgApi.get("8da").GetAccountBalance("dawlance008"));
		//System.out.println(new BigDecimal(100).multiply(new BigDecimal(0.87)).setScale(2, RoundingMode.HALF_UP));
		//System.out.println(MgApi.get("8da").Transfer("dawlance008", System.currentTimeMillis()+"", "IN", new BigDecimal(1)));
		//System.out.println(MgApi.get("8da").GetAccountBalance("dawaass2255"));
		//System.out.println(MgApi.get("8da").updatePassword("daw546546","abcd1234"));
		//System.out.println(MgApi.get("8da").registerAlias("dawlance007","lance000"));
//		for(int i=0;i<1;i++){
		//System.out.println(MgApi.get("8da").Transfer("dawlance008", System.currentTimeMillis()+"", "IN", new BigDecimal(1)));
//		System.out.println(MgApi.get("8da").Transfer("dawlance008", System.currentTimeMillis()+"", "OUT", new BigDecimal(1)));
//	}
		//System.out.println(MgApi.get("8da").Transfer("dawrxm1984", System.currentTimeMillis()+"", "IN", new BigDecimal(1)));
		
//		for(String visitor :visitors){
			//System.out.println("     " +MgApi.get("8da").addAccount("daw546546","abcd1234", "127.0.0.1"));
//		}
	}
	public static String[] visitors=new String[]{"dawvisitor00",
		"dawvisitor01","dawvisitor02","dawvisitor03","dawvisitor04","dawvisitor05",
		"dawvisitor06","dawvisitor07","dawvisitor08","dawvisitor09","dawvisitor10",
		"dawvisitor11","dawvisitor12","dawvisitor13","dawvisitor14","dawvisitor15",
		"dawvisitor16","dawvisitor17","dawvisitor18","dawvisitor19","dawvisitor20",
		"dawvisitor21","dawvisitor22","dawvisitor23","dawvisitor24","dawvisitor25",
		"dawvisitor26","dawvisitor27","dawvisitor28","dawvisitor29","dawvisitor30"};
}
