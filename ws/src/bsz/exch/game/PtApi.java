package bsz.exch.game;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.security.KeyStore;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;

import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import bsz.exch.bean.LogInfo;

public class PtApi {
	
	public   String ptUrl=null;
	
	public   String kioskname=null;
	
	public   String adminname=null;
	
	public   String key=null;
	
	public String filename=null;
	
	public String filekey=null;
	
	private static Logger logger=Logger.getLogger(PtApi.class);
	
	public static final ThreadLocal<LogInfo>  LOCAL_LOG  = new ThreadLocal<LogInfo>();
	
	private PtApi(){
		
	}
	private static Map<String,PtApi> map =new HashMap<String,PtApi>();
	
	public static PtApi get(String product){
		PtApi api=map.get(product);
		if(api==null){
			api =new PtApi();
			api.ptUrl=GameResource.instance().getConfig("game."+product+".pt.ptUrl");
			api.kioskname=GameResource.instance().getConfig("game."+product+".pt.kioskname");
			api.adminname=GameResource.instance().getConfig("game."+product+".pt.adminname");
			api.key=GameResource.instance().getConfig("game."+product+".pt.key");
			api.filename=GameResource.instance().getConfig("game."+product+".pt.filename");
			api.filekey=GameResource.instance().getConfig("game."+product+".pt.filekey");
			 try {  
			    	KeyStore ks = KeyStore.getInstance("PKCS12");
					File file = new File(PtApi.class.getResource("/").getPath()+api.filename);
					FileInputStream fis = new FileInputStream(file);
					ks.load(fis, api.filekey.toCharArray());
					KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
					kmf.init(ks, api.filekey.toCharArray());
					SSLContext ctx = SSLContext.getInstance("TLS");
					ctx.init(kmf.getKeyManagers(), null, null);
					
					RegistryBuilder<ConnectionSocketFactory> registryBuilder = RegistryBuilder.<ConnectionSocketFactory>create();  
			    	ConnectionSocketFactory plainSF = new SSLConnectionSocketFactory(ctx);  
			    	registryBuilder.register("https", plainSF);  
			    	Registry<ConnectionSocketFactory> registry = registryBuilder.build();
			    	api.connManager = new PoolingHttpClientConnectionManager(registry); 
		    	} catch (Exception e) {  
		    	    throw new RuntimeException(e);  
		    	}  
			 api.requestConfig = RequestConfig.custom().setSocketTimeout(35000).setConnectTimeout(35000).build();
			 api.build=HttpClientBuilder.create().setConnectionManager(api.connManager);
			 map.put(product, api);
		}
		 return api;
		
	}
		
	private  PoolingHttpClientConnectionManager connManager;
	private  RequestConfig requestConfig;
	private  HttpClientBuilder build;
		
	public  boolean CheckOrCreateGameAccount(String loginName,String password){
		String url=ptUrl+"create/playername/"+loginName+"/kioskname/"+kioskname+"/adminname/"+adminname+"/password/"+password+"/custom01/VBET"+"/custom02/8DABET"+"/custom03/88";
		CloseableHttpClient  hc= build.build(); 
		HttpEntity entity =null;
		try{
		HttpGet httpget = new HttpGet(url.toString());
		httpget.setConfig(requestConfig);
		httpget.addHeader("X_ENTITY_KEY",key);
		HttpResponse response = hc.execute(httpget);
		entity= response.getEntity();
		if (entity != null && response.getStatusLine().getStatusCode()==200) {
			String result = EntityUtils.toString(entity, "utf-8");
			System.out.println(result);
			JSONObject jsresult = JSONObject.fromObject( result );  
			if(jsresult.get("result")!=null){
				JSONObject jo=(JSONObject)jsresult.get("result");
				if(jo.getString("result")!=null){
					return true;
				}
			}
		  }
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}finally{
			try{
			logger.info(url+"\n response:"+(entity==null?"":EntityUtils.toString(entity, "utf-8")));
			}catch(Exception e){
				
			}
			//hc.getConnectionManager().shutdown();
		}
		return false;
	}
	
	
	public  BigDecimal GetBalance(String loginName){
		String url=ptUrl+"info/playername/"+loginName;;
		CloseableHttpClient  hc= build.build(); 
		HttpEntity entity =null;
		try{
		HttpGet httpget = new HttpGet(url.toString());
		httpget.setConfig(requestConfig);
		httpget.addHeader("X_ENTITY_KEY",key);
		HttpResponse response = hc.execute(httpget);
		entity = response.getEntity();
		if (entity != null && response.getStatusLine().getStatusCode()==200) {
			String result = EntityUtils.toString(entity, "utf-8");
			JSONObject jsresult = JSONObject.fromObject( result );  
			return new BigDecimal(((JSONObject)jsresult.getJSONObject("result")).getString("BALANCE"));

		}
		}catch(Exception e){
			e.printStackTrace();
			return new BigDecimal(0);
		}finally{
		   try{
			logger.info(url+"\n response:"+(entity==null?"":EntityUtils.toString(entity, "utf-8")));
			}catch(Exception e){
				
			}
			//hc.getConnectionManager().shutdown();
		}
		return new BigDecimal(0);
	} 
	

	public  boolean Transfer(String login_name, String tranferno,String transfer_type,  BigDecimal remit){
		try{
			String url="";
			if("IN".equals(transfer_type)){
				url=ptUrl+"deposit/playername/"+login_name+"/amount/"+remit.floatValue()+"/adminname/"+adminname+"/externaltranid/"+tranferno;
			}
			if("OUT".equals(transfer_type)){
				url=ptUrl+"withdraw/playername/"+login_name+"/amount/"+remit.floatValue()+"/adminname/"+adminname+"/externaltranid/"+tranferno+"/isForce/1";
			}
			CloseableHttpClient  hc= build.build(); 
			HttpEntity entity =null;
			try{
			HttpGet httpget = new HttpGet(url.toString());
			httpget.setConfig(requestConfig);
			httpget.addHeader("X_ENTITY_KEY",key);
			HttpResponse response = hc.execute(httpget);
			entity = response.getEntity();
			if (entity != null && response.getStatusLine().getStatusCode()==200) {
				String result = EntityUtils.toString(entity, "utf-8");
				System.out.println(result);
				JSONObject jsresult = JSONObject.fromObject( result );  
				if(jsresult.get("result")!=null){
					JSONObject jo=(JSONObject)jsresult.get("result");
					if(jo.getString("result")!=null&&jo.getString("result").indexOf("OK")>0){
						return true;
					}
				}
			  }
			}catch(Exception e){
				e.printStackTrace();return false;
			}finally{
				try{
					logger.info(url+"\n response:"+(entity==null?"":EntityUtils.toString(entity, "utf-8")));
					}catch(Exception e){
						
					}
				//hc.getConnectionManager().shutdown();
			}
			return false;
		
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		
	}
	
	public  boolean UpdatePwd(String loginName,String pwd){
		String url=ptUrl+"update/playername/"+loginName+"/password/"+pwd;;
		CloseableHttpClient  hc= build.build(); 
		HttpEntity entity =null;
		try{
		HttpGet httpget = new HttpGet(url.toString());
		httpget.setConfig(requestConfig);
		httpget.addHeader("X_ENTITY_KEY",key);
		HttpResponse response = hc.execute(httpget);
	    entity = response.getEntity();
		if (entity != null && response.getStatusLine().getStatusCode()==200) {
			String result = EntityUtils.toString(entity, "utf-8");
			System.out.println( result);
			JSONObject jsresult = JSONObject.fromObject(result);  
			if(jsresult.get("result")!=null){
				JSONObject jo=(JSONObject)jsresult.get("result");
				if(jo!=null)return true;
			}
		}
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}finally{
			try{
				logger.info(url+"\n response:"+(entity==null?"":EntityUtils.toString(entity, "utf-8")));
				}catch(Exception e){
					
				}
			//hc.getConnectionManager().shutdown();
		}
		return false;
	} 
	
	public  boolean ptlogin(String username,String code){
		String url=ptUrl+"checktoken/playername/"+username+"/token/"+code;;
		CloseableHttpClient  hc= build.build(); 
		HttpEntity entity =null;
		try{
		HttpGet httpget = new HttpGet(url.toString());
		httpget.setConfig(requestConfig);
		httpget.addHeader("X_ENTITY_KEY",key);
		HttpResponse response = hc.execute(httpget);
	    entity = response.getEntity();
		if (entity != null && response.getStatusLine().getStatusCode()==200) {
			String result = EntityUtils.toString(entity, "utf-8");
			JSONObject jsresult = JSONObject.fromObject(result);  
			if(jsresult.get("result")!=null){
				JSONObject jo=(JSONObject)jsresult.get("result");
				if(jo.get("result").equals("1"))return true;
			}
		}
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}finally{
			try{
				logger.info(url+"\n response:"+(entity==null?"":EntityUtils.toString(entity, "utf-8")));
				}catch(Exception e){
					
				}
			//hc.getConnectionManager().shutdown();
		}
		return false;
	}
	
	public  boolean UpdateCustom02(String loginName){
		String url=ptUrl+"update/playername/"+loginName+"/custom02/8DABET";;
		CloseableHttpClient  hc= build.build(); 
		HttpEntity entity =null;
		try{
		HttpGet httpget = new HttpGet(url.toString());
		httpget.setConfig(requestConfig);
		httpget.addHeader("X_ENTITY_KEY",key);
		HttpResponse response = hc.execute(httpget);
		entity = response.getEntity();
		if (entity != null && response.getStatusLine().getStatusCode()==200) {
			String result = EntityUtils.toString(entity, "utf-8");
			JSONObject jsresult = JSONObject.fromObject(result);  
			if(jsresult.get("result")!=null){
				JSONObject jo=(JSONObject)jsresult.get("result");
				if(jo!=null)return true;
			}
		}
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}finally{
			try{
				logger.info(url+"\n response:"+(entity==null?"":EntityUtils.toString(entity, "utf-8")));
				}catch(Exception e){
					
				}
			//hc.getConnectionManager().shutdown();
		}
		return false;
	} 
	
	
	public  boolean CheckOnline(String loginName){
		String url=ptUrl+"online/playername/"+loginName;
		CloseableHttpClient  hc= build.build(); 
		HttpEntity entity =null;
		try{
		HttpGet httpget = new HttpGet(url.toString());
		httpget.setConfig(requestConfig);
		httpget.addHeader("X_ENTITY_KEY",key);
		HttpResponse response = hc.execute(httpget);
	    entity = response.getEntity();
		if (entity != null && response.getStatusLine().getStatusCode()==200) {
			String result = EntityUtils.toString(entity, "utf-8");
			JSONObject jsresult = JSONObject.fromObject(result);  
			if(jsresult.get("result")!=null){
				JSONObject jo=(JSONObject)jsresult.get("result");
				if(jo!=null&&jo.getInt("result")==1)return true;
			}
		}
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}finally{
			try{
				logger.info(url+"\n response:"+(entity==null?"":EntityUtils.toString(entity, "utf-8")));
				}catch(Exception e){
					
				}
			//hc.getConnectionManager().shutdown();
		}
		return false;
	}
	
	public  boolean Logout(String loginName){
		String url=ptUrl+"logout/playername/"+loginName;
		CloseableHttpClient  hc= build.build(); 
		HttpEntity entity =null;
		try{
		HttpGet httpget = new HttpGet(url.toString());
		httpget.setConfig(requestConfig);
		httpget.addHeader("X_ENTITY_KEY",key);
		HttpResponse response = hc.execute(httpget);
	    entity = response.getEntity();
		if (entity != null && response.getStatusLine().getStatusCode()==200) {
			String result = EntityUtils.toString(entity, "utf-8");
			JSONObject jsresult = JSONObject.fromObject(result);  
			if(jsresult.get("result")!=null){
				JSONObject jo=(JSONObject)jsresult.get("result");
				if(jo!=null)return true;
			}
		}
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}finally{
			try{
				logger.info(url+"\n response:"+(entity==null?"":EntityUtils.toString(entity, "utf-8")));
				}catch(Exception e){
					
				}
			//hc.getConnectionManager().shutdown();
		}
		return false;
	}
	
	public static String[] visitors=new String[]{"dawvisitor00",
		"dawvisitor01","dawvisitor02","dawvisitor03","dawvisitor04","dawvisitor05",
		"dawvisitor06","dawvisitor07","dawvisitor08","dawvisitor09","dawvisitor10",
		"dawvisitor11","dawvisitor12","dawvisitor13","dawvisitor14","dawvisitor15",
		"dawvisitor16","dawvisitor17","dawvisitor18","dawvisitor19","dawvisitor20",
		"dawvisitor21","dawvisitor22","dawvisitor23","dawvisitor24","dawvisitor25",
		"dawvisitor26","dawvisitor27","dawvisitor28","dawvisitor29","dawvisitor30"};
	
	public static void main(String[] srt) throws Exception {
//		for(String v:visitors){
//			//System.out.println(createAccount(v.toUpperCase(),"a123a123"));
//		}
		//System.out.println(PtApi.get("8da").CheckOrCreateGameAccount("DAWZZ125522".toUpperCase(),"c123c123"));
		System.out.println(PtApi.get("8da").UpdatePwd("DAWA88538A","688538"));
		
		//System.out.println(PtApi.get("8da").GetBalance("DAWZZ125522"));
		//Transfer("OUT","DAWLANCE008","WIT20141210012102202",new BigDecimal(100.5));
		//System.out.println(queryBalance("DAWLANCE008"));
		//System.out.println(PtApi.get("8da").Transfer("DAWWOODY","1447736576143","IN",new BigDecimal(1)));
		//System.out.println(PtApi.get("8da").Transfer("DAWZZ125522","1447736576131","OUT",new BigDecimal(1)));
		System.out.println(PtApi.get("8da").GetBalance("DAWWOODY"));
		//System.out.println(PtApi.get("8da").Logout("DAWWOODY"));
//		String result = "{\"result\":{\"result\":1}}" ;
//		JSONObject jsresult = JSONObject.fromObject(result);  
//		JSONObject jo=(JSONObject)jsresult.get("result");
//		System.out.println(jo.get("result"));
		
	}

}
