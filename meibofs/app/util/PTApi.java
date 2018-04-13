package util;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;

import models.PtBetdata;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

public class PTApi {

	
	public static final String ptUrl="https://kioskpublicapi.redhorse88.com/player/";
	
	public static final String kioskname="8DABETTLK";
	
	public static final String adminname="8DABETTLA";
	
	public static final String key="115eacc10027340bcbe3f763c3e4495fe8fed9f5db86c95d1907aaef41f8c892b8e3b972dc26e724ed41f07403a2117aa1bdb843c9219470240153bca5608887";
	
	private static Logger logger=Logger.getLogger(PTApi.class);
	
	public static SSLSocketFactory getSSLSocket() throws Exception{
		KeyStore ks = KeyStore.getInstance("PKCS12");
		File file = new File(PTApi.class.getResource("/").getPath()+"kioskpublicapi.redhorse88.1114721.p12");
		FileInputStream fis = new FileInputStream(file);
		ks.load(fis, "TGX8BU20".toCharArray());
		KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
		kmf.init(ks, "TGX8BU20".toCharArray());
		SSLContext ctx = SSLContext.getInstance("TLS");
		ctx.init(kmf.getKeyManagers(), null, null);
		SSLSocketFactory sf = new SSLSocketFactory(ctx, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		return sf;
	}
	
	public static boolean createAccount(String loginName,String password){
		String url=ptUrl+"create/playername/"+loginName+"/kioskname/"+kioskname+"/adminname/"+adminname+"/password/"+password+"/custom01/VBET"+"/custom02/8DABET"+"/custom03/88";
		HttpClient hc = new DefaultHttpClient();
		HttpEntity entity =null;
		try{
		hc.getConnectionManager().getSchemeRegistry().register(new Scheme("https", 443, getSSLSocket())); 
		hc.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,35000);
		hc.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,35000);
		HttpConnectionParams.setConnectionTimeout(hc.getParams(), 35000);
		HttpConnectionParams.setSoTimeout(hc.getParams(), 35000);
		HttpGet httpget = new HttpGet(url.toString());
		httpget.addHeader("X_ENTITY_KEY",key);
		HttpResponse response = hc.execute(httpget);
		entity= response.getEntity();
		if (entity != null && response.getStatusLine().getStatusCode()==200) {
			String result = EntityUtils.toString(entity, "utf-8");
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
			hc.getConnectionManager().shutdown();
		}
		return false;
	}
	
	
	public static BigDecimal queryBalance(String loginName){
		String url=ptUrl+"info/playername/"+loginName;;
		HttpClient hc = new DefaultHttpClient();
		HttpEntity entity =null;
		try{
		hc.getConnectionManager().getSchemeRegistry().register(new Scheme("https", 443, getSSLSocket())); 
		hc.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,35000);
		hc.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,35000);
		HttpConnectionParams.setConnectionTimeout(hc.getParams(), 35000);
		HttpConnectionParams.setSoTimeout(hc.getParams(), 35000);
		HttpGet httpget = new HttpGet(url.toString());
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
			hc.getConnectionManager().shutdown();
		}
		return new BigDecimal(0);
	} 
	
	public static List getBetRecord(String startdate,String enddate,int page){
		String url="https://kioskpublicapi.mightypanda88.com/customreport/getdata/reportname/PlayerGames/startdate/"+startdate+"/enddate/"+enddate+"/frozen/all/sortby/playername/timeperiod/specify/page/"+page+"/perPage/500";
		//String url="https://kioskpublicapi.mightypanda88.com/customreport/getdata/reportname/GameStats/startdate/"+startdate+"/enddate/"+enddate+"/gametype/regular/reportby/playername/sortby/games/timeperiod/specify/kioskname/"+kioskname+"/page/"+page+"/perPage/500";
		
		HttpClient hc = new DefaultHttpClient();
		HttpEntity entity =null;
		List<PtBetdata> list = new ArrayList<PtBetdata>();
		PtBetdata bean = null;
		try{
			hc.getConnectionManager().getSchemeRegistry().register(new Scheme("https", 443, getSSLSocket())); 
			hc.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,35000);
			hc.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,35000);
			HttpConnectionParams.setConnectionTimeout(hc.getParams(), 35000);
			HttpConnectionParams.setSoTimeout(hc.getParams(), 35000);
			//url = URLEncoder.encode(url, "UTF-8");
			//System.out.println(url);
			HttpGet httpget = new HttpGet(url.toString());
			httpget.addHeader("X_ENTITY_KEY",key);
			HttpResponse response = hc.execute(httpget);
			entity = response.getEntity();
			if (entity != null && response.getStatusLine().getStatusCode()==200) {
				String result = EntityUtils.toString(entity, "utf-8");
				//System.out.println(result);
				JSONObject jsresult = JSONObject.fromObject( result ); 
				JSONArray jsarray = jsresult.getJSONArray("result");
				for(int i = 0;i<jsarray.size();i++){
					JSONObject object = jsarray.getJSONObject(i);
					bean = new PtBetdata();
					bean.login_name = object.getString("PLAYERNAME");
					bean.windowcode = object.getString("WINDOWCODE");
					bean.gameid = Integer.parseInt(object.getString("GAMEID"));
					bean.gamecode = Long.parseLong(object.getString("GAMECODE"));
					bean.gametype = object.getString("GAMETYPE");
					bean.gamename = object.getString("GAMENAME");
					bean.sessionid = Integer.parseInt(object.getString("SESSIONID"));
					bean.bet = BigDecimal.valueOf(Double.parseDouble(object.getString("BET")));
					bean.win = BigDecimal.valueOf(Double.parseDouble(object.getString("WIN")));
					bean.progressivebet = BigDecimal.valueOf(Double.parseDouble(object.getString("PROGRESSIVEBET")));
					bean.progressivewin = BigDecimal.valueOf(Double.parseDouble(object.getString("PROGRESSIVEWIN")));
					bean.balance = BigDecimal.valueOf(Double.parseDouble(object.getString("BALANCE")));
					bean.currentbet = BigDecimal.valueOf(Double.parseDouble(object.getString("CURRENTBET")));
					bean.gamedate = DateUtil.stringToTimestamp(object.getString("GAMEDATE"),"yyyy-MM-dd HH:mm:ss");
					list.add(bean);
				}
				return list;
			}
			return null;
		}catch(Exception e){
			e.printStackTrace();return null;
		}finally{
					hc.getConnectionManager().shutdown();
				}
	}
	
	
	public static Integer getTotalPage(String startdate,String enddate){
		//System.out.println(startdate +  "   "+enddate);
		//String url="https://kioskpublicapi.mightypanda88.com/customreport/getdata/reportname/GameStats/startdate/"+startdate+"/enddate/"+enddate+"/gametype/regular/reportby/playername/sortby/games/timeperiod/specify/kioskname/"+kioskname+"/perPage/500";
		String url="https://kioskpublicapi.mightypanda88.com/customreport/getdata/reportname/PlayerGames/startdate/"+startdate+"/enddate/"+enddate+"/frozen/all/sortby/playername/timeperiod/specify/page/1/perPage/500";
		HttpClient hc = new DefaultHttpClient();
		HttpEntity entity =null;
		
		try{
			hc.getConnectionManager().getSchemeRegistry().register(new Scheme("https", 443, getSSLSocket())); 
			hc.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,35000);
			hc.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,35000);
			HttpConnectionParams.setConnectionTimeout(hc.getParams(), 35000);
			HttpConnectionParams.setSoTimeout(hc.getParams(), 35000);
			//url = URLEncoder.encode(url, "UTF-8");
			HttpGet httpget = new HttpGet(url.toString());
			httpget.addHeader("X_ENTITY_KEY",key);
			HttpResponse response = hc.execute(httpget);
			entity = response.getEntity();
			if (entity != null && response.getStatusLine().getStatusCode()==200) {
				String result = EntityUtils.toString(entity, "utf-8");
				//System.out.println(result);
				JSONObject jsresult = JSONObject.fromObject( result );
				String TotalPage = ((JSONObject)jsresult.getJSONObject("pagination")).getString("totalPages");
				if(TotalPage == null){
					return 0;
				}else{
					return Integer.parseInt(TotalPage);	
				}
			}
			return null;
		}catch(Exception e){
			e.printStackTrace();return 0;
		}finally{
			hc.getConnectionManager().shutdown();
		}
	}
	
	
	
	
	public static boolean Transfer(String transfer_type,String login_name, String tranferno,  BigDecimal remit){
		String url="";
		if("IN".equals(transfer_type)){
			url=ptUrl+"deposit/playername/"+login_name+"/amount/"+remit.floatValue()+"/adminname/"+adminname+"/externaltranid/"+tranferno;
		}
		if("OUT".equals(transfer_type)){
			url=ptUrl+"withdraw/playername/"+login_name+"/amount/"+remit.floatValue()+"/adminname/"+adminname+"/externaltranid/"+tranferno+"/isForce/1";
		}
		HttpClient hc = new DefaultHttpClient();
		HttpEntity entity =null;
		try{
		hc.getConnectionManager().getSchemeRegistry().register(new Scheme("https", 443, getSSLSocket())); 
		hc.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,35000);
		hc.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,35000);
		HttpConnectionParams.setConnectionTimeout(hc.getParams(), 35000);
		HttpConnectionParams.setSoTimeout(hc.getParams(), 35000);
		HttpGet httpget = new HttpGet(url.toString());
		httpget.addHeader("X_ENTITY_KEY",key);
		HttpResponse response = hc.execute(httpget);
		entity = response.getEntity();
		if (entity != null && response.getStatusLine().getStatusCode()==200) {
			String result = EntityUtils.toString(entity, "utf-8");
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
			hc.getConnectionManager().shutdown();
		}
		return false;
		
	}
	
	public static boolean UpdatePwd(String loginName,String pwd){
		String url=ptUrl+"update/playername/"+loginName+"/password/"+pwd;;
		HttpClient hc = new DefaultHttpClient();
		HttpEntity entity =null;
		try{
		hc.getConnectionManager().getSchemeRegistry().register(new Scheme("https", 443, getSSLSocket())); 
		hc.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,35000);
		hc.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,35000);
		HttpConnectionParams.setConnectionTimeout(hc.getParams(), 35000);
		HttpConnectionParams.setSoTimeout(hc.getParams(), 35000);
		HttpGet httpget = new HttpGet(url.toString());
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
			hc.getConnectionManager().shutdown();
		}
		return false;
	} 
	
	public static boolean UpdateCustom02(String loginName){
		String url=ptUrl+"update/playername/"+loginName+"/custom02/8DABET";;
		HttpClient hc = new DefaultHttpClient();
		HttpEntity entity =null;
		try{
		hc.getConnectionManager().getSchemeRegistry().register(new Scheme("https", 443, getSSLSocket())); 
		hc.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,35000);
		hc.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,35000);
		HttpConnectionParams.setConnectionTimeout(hc.getParams(), 35000);
		HttpConnectionParams.setSoTimeout(hc.getParams(), 35000);
		HttpGet httpget = new HttpGet(url.toString());
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
			hc.getConnectionManager().shutdown();
		}
		return false;
	} 
	
	
	public static boolean CheckOnline(String loginName){
		String url=ptUrl+"online/playername/"+loginName;
		HttpClient hc = new DefaultHttpClient();
		HttpEntity entity =null;
		try{
		hc.getConnectionManager().getSchemeRegistry().register(new Scheme("https", 443, getSSLSocket())); 
		hc.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,35000);
		hc.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,35000);
		HttpConnectionParams.setConnectionTimeout(hc.getParams(), 35000);
		HttpConnectionParams.setSoTimeout(hc.getParams(), 35000);
		HttpGet httpget = new HttpGet(url.toString());
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
			hc.getConnectionManager().shutdown();
		}
		return false;
	}
	
	public static boolean Logout(String loginName){
		String url=ptUrl+"logout/playername/"+loginName;
		HttpClient hc = new DefaultHttpClient();
		HttpEntity entity =null;
		try{
		hc.getConnectionManager().getSchemeRegistry().register(new Scheme("https", 443, getSSLSocket())); 
		hc.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,35000);
		hc.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,35000);
		HttpConnectionParams.setConnectionTimeout(hc.getParams(), 35000);
		HttpConnectionParams.setSoTimeout(hc.getParams(), 35000);
		HttpGet httpget = new HttpGet(url.toString());
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
			hc.getConnectionManager().shutdown();
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
		for(String v:visitors){
			//System.out.println(createAccount(v.toUpperCase(),"a123a123"));
		}
		//System.out.println(createAccount("dawvisitor32".toUpperCase(),"a123a123"));
		//System.out.println(UpdatePwd("dawlance008","aa123456"));
		
		//System.out.println(queryBalance("DAWLANCE008"));
		//Transfer("OUT","DAWLANCE008","WIT20141210012102202",new BigDecimal(100.5));
		System.out.println(queryBalance("DAWBABY668"));
		//System.out.println(CheckOnline("DAW840901"));
		//System.out.println(Logout("DAW840901"));
		//System.out.println(getBetRecord("2015-05-19%2007:35:20","2015-05-19%2018:50:23",3));
		
	}

}
