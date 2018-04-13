package bsz.exch.game;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import nu.xom.Document;

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
import bsz.exch.utils.DESEncrypt;
import bsz.exch.utils.DomUtils;
import bsz.exch.utils.MD5Util;

public class AginApi {
	
	public static Map<String,AginApi> map= new HashMap<String,AginApi>();
	
	private AginApi (){
	   
		
	}
	public static AginApi get(String prouct){
		AginApi a=map.get(prouct);
		if(a==null){
			 AginApi api=new AginApi();
			 api.agurl=GameResource.instance().getConfig("game."+prouct+".ag.agurl");
			 api.agforwardurl=GameResource.instance().getConfig("game."+prouct+".ag.agforwardurl");
			 api.cagent=GameResource.instance().getConfig("game."+prouct+".ag.cagent");
			 api.md5_key=GameResource.instance().getConfig("game."+prouct+".ag.md5_key");
			 api.des_key=GameResource.instance().getConfig("game."+prouct+".ag.des_key");
			 RegistryBuilder<ConnectionSocketFactory> registryBuilder = RegistryBuilder.<ConnectionSocketFactory>create();  
	    	 ConnectionSocketFactory plainSF = new PlainConnectionSocketFactory();  
	    	 registryBuilder.register("http", plainSF);  
	    	 Registry<ConnectionSocketFactory> registry = registryBuilder.build();
	    	 api.connManager = new PoolingHttpClientConnectionManager(registry);  
	    	 api.requestConfig = RequestConfig.custom().setSocketTimeout(35000).setConnectTimeout(35000).build();
	    	 api.build=HttpClientBuilder.create().setConnectionManager(api.connManager);
	    	 return api;
		}
		return a;
	}
	
	public  String agurl=null;
	public String agforwardurl=null;
	public  String cagent=null;
	public  String md5_key=null;;
	public  String des_key=null;
	
	private static Logger logger=Logger.getLogger(AginApi.class);
    public static final ThreadLocal<LogInfo>  LOCAL_LOG  = new ThreadLocal<LogInfo>();
	
	private  PoolingHttpClientConnectionManager connManager;
	private  RequestConfig requestConfig;
	private  HttpClientBuilder build;
	
	public  String doRequest(String url,String ps){
		LOCAL_LOG.set(new LogInfo());
		LOCAL_LOG.get().addLog("|Game_AGIN -");
		CloseableHttpClient  httpclient= build.build(); 
		try{
		DESEncrypt e =new DESEncrypt(des_key);
    	String params=e.encrypt(ps);
		String md5=MD5Util.MD5(params+md5_key).toLowerCase();
		url=url+"params="+params+"&key="+md5;
		
		HttpGet httpget = new HttpGet(url);  
		httpget.setConfig(requestConfig);
		httpget.addHeader("User-Agent", "WEB_LIB_GI_"+cagent);
		LOCAL_LOG.get().addLog("  Params:"+ps);
		
		HttpResponse response = httpclient.execute(httpget,HttpCoreContext.create());   
	   	LOCAL_LOG.get().addLog("  Reponse:"+response.getStatusLine());
		if(response.getStatusLine().getStatusCode()==200){
			HttpEntity entity = response.getEntity(); 
	        if (entity != null) {   
	    	   String result= EntityUtils.toString(entity,"utf-8"); 
	    	   LOCAL_LOG.get().addLog(" Result:"+result);
	    	   return result;
	        }  
		}
		}catch(Exception e){
			LOCAL_LOG.get().addLog(" Result:"+e.getMessage());
			logger.error(e.getMessage());
			e.printStackTrace();
		}finally{
			logger.info(LOCAL_LOG.get().getLog());
		}
		return null;
	}
	public  boolean CheckOrCreateGameAccount(String loginname, String password){
		String url=agurl+"doBusiness.do?";
		StringBuffer sb =new StringBuffer();
		sb.append("cagent="+cagent+"/\\\\/");
		sb.append("loginname="+loginname+"/\\\\/");
		sb.append("method=lg/\\\\/");
		sb.append("actype=1/\\\\/");
		sb.append("password="+password+"/\\\\/");
		sb.append("oddtype=C");
		String result= doRequest(url,sb.toString());
		if(result==null) return false;
		Document xmlDoc =null;
		try {
			xmlDoc = DomUtils.String2Doc(result);
			String info=xmlDoc.getRootElement().getAttributeValue("info");
			if("0".equals(info))return true;
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return false;
	}
	
	//试玩账户
	public  boolean CheckOrCreateTestGameAccount(String loginname, String password){
		String url=agurl+"doBusiness.do?";
		StringBuffer sb =new StringBuffer();
		sb.append("cagent="+cagent+"/\\\\/");
		sb.append("loginname="+loginname+"/\\\\/");
		sb.append("method=lg/\\\\/");
		sb.append("actype=0/\\\\/");
		sb.append("password="+password+"/\\\\/");
		sb.append("oddtype=C");
		String result= doRequest(url,sb.toString());
		if(result==null) return false;
		Document xmlDoc =null;
		try {
			xmlDoc = DomUtils.String2Doc(result);
			String info=xmlDoc.getRootElement().getAttributeValue("info");
			if("0".equals(info))return true;
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return false;
	}
	
	
  private static BigDecimal getCredit(String info){
	  try{
			BigDecimal big=new BigDecimal(info);
			return big;
		}catch(Exception e){
			return new BigDecimal("0.00");
		}
  }
  public  BigDecimal GetBalance(String loginname, String password) {
     String url=agurl+"doBusiness.do?";
     StringBuffer sb =new StringBuffer();
		sb.append("cagent="+cagent+"/\\\\/");
		sb.append("loginname="+loginname+"/\\\\/");
		sb.append("method=gb/\\\\/");
		sb.append("actype=1/\\\\/");
		sb.append("password="+password+"/\\\\/");
		sb.append("cur=CNY");
		String result= doRequest(url,sb.toString());
		if(result==null) return new BigDecimal(0);
		Document xmlDoc =null;
		try {
			xmlDoc = DomUtils.String2Doc(result);
			String info=xmlDoc.getRootElement().getAttributeValue("info");
			return getCredit(info);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new BigDecimal(0);
  }
  
private  boolean PreTransfer(String loginname, String billno, String type, BigDecimal credit, String password) {
	credit.setScale(2,BigDecimal.ROUND_DOWN);
	String url=agurl+"doBusiness.do?";
    StringBuffer sb =new StringBuffer();
    sb.append("cagent="+cagent+"/\\\\/");
	sb.append("method=tc/\\\\/");
	sb.append("loginname="+loginname+"/\\\\/");
	sb.append("billno="+cagent+billno+"/\\\\/");
	sb.append("type="+type+"/\\\\/");
	sb.append("credit="+credit+"/\\\\/");
	sb.append("actype=1/\\\\/");
	sb.append("password="+password+"/\\\\/");
	sb.append("cur=CNY");
	String result= doRequest(url,sb.toString());
	if(result==null) return false;
	Document xmlDoc =null;
	try {
		xmlDoc = DomUtils.String2Doc(result);
		String info=xmlDoc.getRootElement().getAttributeValue("info");
		if("0".equals(info))return true;
	} catch (Exception e) {
		e.printStackTrace();
	}
	return false;
}

private  boolean TransferConfirm(String loginname, String billno, String type, BigDecimal credit, String password, Integer flag) {
	credit.setScale(2,BigDecimal.ROUND_DOWN);
	String url=agurl+"doBusiness.do?";
    StringBuffer sb =new StringBuffer();
    sb.append("cagent="+cagent+"/\\\\/");
	sb.append("loginname="+loginname+"/\\\\/");
	sb.append("method=tcc/\\\\/");
	sb.append("billno="+cagent+billno+"/\\\\/");
	sb.append("type="+type+"/\\\\/");
	sb.append("credit="+credit+"/\\\\/");
	sb.append("actype=1/\\\\/");
	sb.append("flag="+flag+"/\\\\/");
	sb.append("password="+password+"/\\\\/");
	sb.append("cur=CNY");
	String result= doRequest(url,sb.toString());
	if(result==null) return false;
	Document xmlDoc =null;
	try {
		xmlDoc = DomUtils.String2Doc(result);
		String info=xmlDoc.getRootElement().getAttributeValue("info");
		if("0".equals(info))return true;
	} catch (Exception e) {
		e.printStackTrace();
	}
	return false;
}

private  boolean QueryOrderStatus( String billno) {
	
	String url=agurl+"doBusiness.do?";
    StringBuffer sb =new StringBuffer();
    sb.append("cagent="+cagent+"/\\\\/");
	
	sb.append("billno="+cagent+billno+"/\\\\/");
	sb.append("method=qos/\\\\/");
	
	sb.append("actype=1/\\\\/");
	
	sb.append("cur=CNY");
	String result= doRequest(url,sb.toString());
	if(result==null) return false;
	Document xmlDoc =null;
	try {
		xmlDoc = DomUtils.String2Doc(result);
		String info=xmlDoc.getRootElement().getAttributeValue("info");
		if("0".equals(info))return true;
	} catch (Exception e) {
		e.printStackTrace();
	}
	return false;
}

public  boolean Transfer(String loginname, String billno, String type, BigDecimal credit, String password){
	try{
		if(PreTransfer(loginname, billno, type, credit, password)){
			if(TransferConfirm(loginname, billno, type, credit, password, 1)){
				return true;
			}else{
				return QueryOrderStatus( billno);
			}
			
		}
	    return false;
	}catch(Exception e){
		e.printStackTrace();
		return false;
	}
}
  

public  String ForwardGame(String loginname,String password,String gamecode){
	String url=agforwardurl+"forwardGame.do?";
    StringBuffer sb =new StringBuffer();
    sb.append("cagent="+cagent+"/\\\\/");
	sb.append("loginname="+loginname+"/\\\\/");
	sb.append("actype=1/\\\\/");
	sb.append("password="+password+"/\\\\/");
	sb.append("dm=www.8da2017.com/\\\\/");
	sb.append("sid="+cagent+new Date().getTime()+"/\\\\/");
	sb.append("lang=1/\\\\/");
	if(gamecode==null || gamecode.equals("")){
		sb.append("gameType=1/\\\\/");
	}else{
		sb.append("gameType="+gamecode+"/\\\\/");
	}
	
	if(loginname.equalsIgnoreCase("dawjcc668") || loginname.equalsIgnoreCase("daw362880") ){
		sb.append("oddtype=E/\\\\/");
	}else if( loginname.equalsIgnoreCase("dawks1433")  ){
		sb.append("oddtype=D/\\\\/");
	}else if( loginname.equalsIgnoreCase("daw376418434")  ){
		sb.append("oddtype=F/\\\\/");
	}else if( loginname.equalsIgnoreCase("dawylj198721")  ){
		sb.append("oddtype=G/\\\\/");
	}else{
		sb.append("oddtype=C/\\\\/");
	}
	
	sb.append("cur=CNY");
	try{
		DESEncrypt e =new DESEncrypt(des_key);
    	String params=e.encrypt(sb.toString());
		String md5=MD5Util.MD5(params+md5_key).toLowerCase();
		url=url+"params="+params+"&key="+md5;
	}catch(Exception e){
		
	} 
	return url;
}
  
//试玩账户
public  String ForwardTestGame(String loginname,String password){
	String url=agforwardurl+"forwardGame.do?";
    StringBuffer sb =new StringBuffer();
    sb.append("cagent="+cagent+"/\\\\/");
	sb.append("loginname="+loginname+"/\\\\/");
	sb.append("actype=0/\\\\/");
	sb.append("password="+password+"/\\\\/");
	sb.append("dm=www.8da2017.com/\\\\/");
	sb.append("sid="+cagent+new Date().getTime()+"/\\\\/");
	sb.append("lang=1/\\\\/");
	sb.append("gameType=1/\\\\/");
	sb.append("oddtype=C/\\\\/");
	sb.append("cur=CNY");
	try{
		DESEncrypt e =new DESEncrypt(des_key);
    	String params=e.encrypt(sb.toString());
		String md5=MD5Util.MD5(params+md5_key).toLowerCase();
		url=url+"params="+params+"&key="+md5;
	}catch(Exception e){
		
	} 
	return url;
}

	
	public static void main(String[] args){
		System.out.println(AginApi.get("8da").CheckOrCreateTestGameAccount("dawzsp115", "b123b123"));
	//System.out.println((AginApi.get("8da").ForwardTestGame("dawtest110", "b123b123")));
		//System.out.println(AginApi.get("8da").Transfer("dawlance008", ""+new Date().getTime(), "IN", new BigDecimal(1), "b123b123"));
		//System.out.println(AginApi.get("8da").QueryOrderStatus("1472284430631"));
		//System.out.println(AginApi.CheckOrCreateGameAccount("dawlance002", "a123a123"));
//		System.out.println(AginApi.get("8da").Transfer("dawlance008", ""+new Date().getTime(), "OUT", new BigDecimal(105), "a123a123"));
		//System.out.println(AginApi.get("8da").GetBalance("dawlance008", "b123b123"));
//		System.out.println(AginApi.get("8da").GetBalance("dawlance102", "b123b123"));
//		System.out.println(AginApi.get("8da").GetBalance("dawlance102", "b123b123"));
 //System.out.println(AginApi.ForwardGame("dawlance001", "a123a123"));
		
	}
	
	
	
	

}
