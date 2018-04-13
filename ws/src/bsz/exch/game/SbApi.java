package bsz.exch.game;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.security.KeyStore;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import javax.net.ssl.SSLContext;

import jxl.Cell;
import jxl.CellType;
import jxl.LabelCell;
import jxl.Sheet;
import jxl.Workbook;

import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
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

import bsz.exch.bean.LogInfo;
import bsz.exch.utils.AnyTrustStrategy;
import bsz.exch.utils.Base64Utils;
import bsz.exch.utils.HMACSHA1;
import net.sf.json.JSONArray;
import net.sf.json.JSONNull;
import net.sf.json.JSONObject;

public class SbApi {
	
	public String client_id;
	
	public String epUrl;
	
	public String secret;
	
	public String lobbyurl;
	
	public String loginurl;
	
	public String cashierurl;
	
	public String helpurl;
	
	public String termsurl;
	
	private static Logger logger=Logger.getLogger(SbApi.class);
	
	public static final ThreadLocal<LogInfo>  LOCAL_LOG  = new ThreadLocal<LogInfo>();
	
	private SbApi(){
		
	}
	private static Map<String,SbApi> map =new HashMap<String,SbApi>();
	
	public static SbApi get(String product){
		SbApi api=map.get(product);
		if(api==null){
			api =new SbApi();
			api.client_id=GameResource.instance().getConfig("game."+product+".sb.client_id");
			api.epUrl=GameResource.instance().getConfig("game."+product+".sb.epUrl");
			api.secret=GameResource.instance().getConfig("game."+product+".sb.secret");
			api.lobbyurl=GameResource.instance().getConfig("game."+product+".sb.lobbyurl");
			api.loginurl=GameResource.instance().getConfig("game."+product+".sb.loginurl");
			api.cashierurl=GameResource.instance().getConfig("game."+product+".sb.cashierurl");
			api.helpurl=GameResource.instance().getConfig("game."+product+".sb.helpurl");
			api.termsurl=GameResource.instance().getConfig("game."+product+".sb.termsurl");

			RegistryBuilder<ConnectionSocketFactory> registryBuilder = RegistryBuilder.<ConnectionSocketFactory>create();  
		  	ConnectionSocketFactory plainSF = new PlainConnectionSocketFactory(); 
			registryBuilder.register("http", plainSF);  
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

	public String doRequest(String url){
		CloseableHttpClient  hc= build.build(); 
		HttpGet httpget= new HttpGet(epUrl+url);  
		httpget.setConfig(requestConfig);
		String utc=utc();
		String auth="SGS "+this.client_id+":"+singure(utc)+"";
		httpget.setHeader("Accept", "application/json");
		httpget.setHeader("Content-Type", "application/json");
		httpget.addHeader("Authorization",""+auth);
		httpget.addHeader("X-Sgs-Date", utc);
		HttpResponse response = null;
		try{ 
			logger.info("SGS Request : URL="+epUrl+url);
			response = hc.execute(httpget); 
			String result = EntityUtils.toString(response.getEntity(), "utf-8");
			logger.info("SGS Response :"+result );
			return result;
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			// hc.getConnectionManager().shutdown();
		}
		return null;
	}
	
	public String doRequest(String url,JSONObject object){
		CloseableHttpClient  hc= build.build(); 
		HttpPost httppost = new HttpPost(epUrl+url);  
		httppost.setConfig(requestConfig);
		String utc=utc();
		String auth="SGS "+this.client_id+":"+singure(utc)+"";
		httppost.setHeader("Accept", "application/json");
		httppost.setHeader("Content-Type", "application/json");
		httppost.addHeader("Authorization",""+auth);
		httppost.addHeader("X-Sgs-Date", utc);
		HttpResponse response = null;
		try{ 
			 StringEntity s = new StringEntity(object.toString());
	         s.setContentEncoding("UTF-8");  
	         s.setContentType("application/json");  
			logger.info("SGS Request : URL="+epUrl+url +" params:"+object.toString());
			httppost.setEntity(s);
			response = hc.execute(httppost); 
			String result = EntityUtils.toString(response.getEntity(), "utf-8");
			logger.info("SGS Response :"+result );
			return result;
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			// hc.getConnectionManager().shutdown();
		}
		return null;
	}
	
	
	private String utc(){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss'Z'");
		df.setTimeZone(TimeZone.getTimeZone("UTC"));
		return df.format(new Date(System.currentTimeMillis()));
	}
	private String utc2(){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss'+00:00'");
		df.setTimeZone(TimeZone.getTimeZone("UTC"));
		return df.format(new Date(System.currentTimeMillis()));
	}
	
	public String singure(String utc){
		try {
			return Base64Utils.byteToString(HMACSHA1.HmacSHA1Encrypt(new String((this.secret+utc).getBytes(),"utf-8"),this.secret));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	//创建用户，登录用户用该方法
	public String authorize(String ip,String username){
		JSONObject json=new JSONObject();
		json.put("ipaddress", ip);
		json.put("username", username);
		json.put("userid", username);
		json.put("lang", "zh-CN");
		json.put("cur", "RMB");
		json.put("betlimitid", "1");
		json.put("istestplayer", "false");
		json.put("platformtype", "0");
		json.put("loginurl", loginurl);
		json.put("cashierurl", cashierurl);
		json.put("helpurl", helpurl);
		json.put("termsurl", termsurl);
		String result =doRequest("/api/player/authorize",json);
		if(result!=null){
			JSONObject o=JSONObject.fromObject(result) ;
		    if(JSONNull.getInstance().equals(o.get("err"))){
		    	String token =o.getString("authtoken");
		    	if(token!=null)return token;
		    }
		}
		return null;
	}
	
	
	//创建测试用户，登录用户用该方法
		public String testauthorize(String ip,String username,int betlimitid){
			JSONObject json=new JSONObject();
			json.put("ipaddress", ip);
			json.put("username", username);
			json.put("userid", username);
			json.put("lang", "zh-CN");
			json.put("cur", "RMB");
			json.put("betlimitid", betlimitid);
			json.put("istestplayer", "true");
			json.put("platformtype", "0");
			json.put("loginurl", loginurl);
			json.put("cashierurl", cashierurl);
			json.put("helpurl", helpurl);
			json.put("termsurl", termsurl);
			String result =doRequest("/api/player/authorize",json);
			if(result!=null){
				JSONObject o=JSONObject.fromObject(result) ;
			    if(JSONNull.getInstance().equals(o.get("err"))){
			    	String token =o.getString("authtoken");
			    	if(token!=null)return token;
			    }
			}
			return null;
		}
		
	//登录退出
	public boolean deauthorize(String username){
		JSONObject json=new JSONObject();
		json.put("userid", username);
		String result =doRequest("/api/player/deauthorize",json);
		if(result!=null){
			JSONObject o=JSONObject.fromObject(result) ;
			Boolean success=o.getBoolean("success");
		    return success;
		}
		return false;
	}
	
	
	
	public String forward(String ip,String username,String cashierUrl){
		String token = "";
		if(cashierurl != null && !cashierurl.equals("")){
			this.cashierurl = cashierUrl;
		}
		if(!username.startsWith("sbtest")){
			token =authorize(ip,username);
		}else{
			if(username.equals("sbtest01") || username.equals("sbtest02") ){
				token =testauthorize(ip,username,1);
			}else if(username.equals("sbtest03") || username.equals("sbtest04") || username.equals("sbtest11") ||username.equals("sbtest12") ||username.equals("sbtest13") ||username.equals("sbtest14") ||username.equals("sbtest15") ){
				token =testauthorize(ip,username,2);
			}else if(username.equals("sbtest05") || username.equals("sbtest06") ){
				token =testauthorize(ip,username,3);
			}else if(username.equals("sbtest07") || username.equals("sbtest08") ){
				token =testauthorize(ip,username,4);
			}else if(username.equals("sbtest09") || username.equals("sbtest10") ){
				System.out.println(11111);
				token =testauthorize(ip,username,5);
			}
			
		}
		
		if(token!=null){
			return this.lobbyurl+"?token="+token;
		}
		return null;
	}
	
	
	public String forwardwithgamecode(String ip,String username,String cashierUrl,String gamecode,String gpcode,String platform){
		String token = "";
		if(cashierurl != null && !cashierurl.equals("")){
			this.cashierurl = cashierUrl;
		}
		if(!username.startsWith("sbtest")){
			token =authorize(ip,username);
		}else{
			if(username.equals("sbtest01") || username.equals("sbtest02") ){
				token =testauthorize(ip,username,1);
			}else if(username.equals("sbtest03") || username.equals("sbtest04") || username.equals("sbtest11") ||username.equals("sbtest12") ||username.equals("sbtest13") ||username.equals("sbtest14") ||username.equals("sbtest15") ){
				token =testauthorize(ip,username,2);
			}else if(username.equals("sbtest05") || username.equals("sbtest06") ){
				token =testauthorize(ip,username,3);
			}else if(username.equals("sbtest07") || username.equals("sbtest08") ){
				token =testauthorize(ip,username,4);
			}else if(username.equals("sbtest09") || username.equals("sbtest10") ){
				System.out.println(11111);
				token =testauthorize(ip,username,5);
			}
			
		}
		
		if(token!=null){
			return "https://tgpasia.com"+"/gamelauncher?gpcode="+gpcode+"&gcode="+gamecode+"&platform="+platform+"&token="+token;
		}
		return null;
	}
	
	//查询余额
	public BigDecimal balance(String username){
		String result =doRequest("/api/player/balance?userid="+username+"&cur=RMB");
		if(result!=null){
			JSONObject o=JSONObject.fromObject(result) ;
		    if(JSONNull.getInstance().equals(o.get("err"))){
		    	Double balance =o.getDouble("bal");
		    	if(balance!=null)return new BigDecimal(balance);
		    }
		}		
		return new BigDecimal(0);
	}
	
	//获取游戏列表
	public String getGamelist(){
		
		String result =doRequest("/api/game/list?lang=zh-CN&platformtype=0&iconres=343x200");
		if(result!=null){
			System.out.println(result);
			JSONObject o=JSONObject.fromObject(result) ;
		    if(JSONNull.getInstance().equals(o.get("err"))){
		    	JSONArray array = o.getJSONArray("games");
		    	System.out.println(array.size());
		    	for(int i=0;i<array.size();i++){
		    		JSONObject game = array.getJSONObject(i);
		    		System.out.println(game.get("code") +"    "+game.get("name")+"    "+game.get("description")+"    "+game.get("providercode")+"    "+game.get("isactive")+"    "+game.get("type")+"    "+game.get("iconurl"));
		    	}
		    }else{
		    	System.out.println(o.get("err"));
		    }
		}
		return null;
	}
	
	
	//增加额度
	public boolean addCredit(String username,BigDecimal amount,String txid){
		JSONObject json=new JSONObject();
		json.put("userid", username);
		json.put("amt", amount);
		json.put("cur", "RMB");
		json.put("txid",txid );
		json.put("timestamp", utc2());
		json.put("desc", "addCredit");
		String result =doRequest("/api/wallet/credit",json);
		if(result!=null){
			JSONObject o=JSONObject.fromObject(result) ;
		    if(JSONNull.getInstance().equals(o.get("err"))){
		    	return true;
		    }
		}		
		return false;
	}
	//减少额度
	public boolean reduceCredit(String username,BigDecimal amount,String txid){
		JSONObject json=new JSONObject();
		json.put("userid", username);
		json.put("amt", amount);
		json.put("cur", "RMB");
		json.put("txid",txid );
		json.put("timestamp", utc2());
		json.put("desc", "reduceCredit");
		String result =doRequest("/api/wallet/debit",json);
		if(result!=null){
			JSONObject o=JSONObject.fromObject(result) ;
		    if(JSONNull.getInstance().equals(o.get("err"))){
		    	return true;
		    }
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
	
	public static void main(String[]args){
		SbApi api = SbApi.get("8da");
		//String username="dawwoody";
//		for(int i=0;i<10;i++){
//			String name="sbtest"+i;
//			System.out.println(name);
//		}
//		for(String visitor :visitors){
//			
//			System.out.println(visitor+"     " +(api.authorize("8.8.8.8",visitor)));
//		}
//		System.out.println(api.balance(username));
		//System.out.println(api.authorize("8.8.8.8","dawwoody")); //100,000.00
		//System.out.println(api.testauthorize("8.8.8.8","sbtest15",2));
		
		System.out.println(api.balance("dawwoody"));
		//System.out.println(api.getGamelist());
		//boolean b =api.addCredit("sbtest15",new BigDecimal(100000),"daw"+System.currentTimeMillis());
		//System.out.println(b);
//		b  =api.reduceCredit("dawwoody",new BigDecimal(1),"daw"+System.currentTimeMillis());
//		System.out.println(b);
		//System.out.println(api.balance(username));
		
//		File file = new File("D:/ext_user.xls");
//		try {
//			InputStream stream  = new FileInputStream(file.toString());
//			Workbook wb = Workbook.getWorkbook(stream);
//			Sheet sheet = wb.getSheet(0);
//			int rows =sheet.getRows();
//		    int cols = sheet.getColumns();
//		   // String authtoken = api.authorize("8.8.8.8","dawwoody");
//		    
//		    for (int i = 1; i < rows; i++) {
//		    	String loginname = getStringValue(sheet,i, 1).trim();
//		    	String authtoken = api.authorize("8.8.8.8",loginname);
//		    	if(authtoken == null){
//		    		System.out.println(i+"       "+loginname);
//		    	}else{
//		    		System.out.println(i);
//		    	}
//		    	Thread.sleep(500);
//		    	
//		    }
//		}catch(Exception e){
//			e.printStackTrace();
//		}
		
		
		
	}
	
	public static String getStringValue(Sheet sheet,int rows, int cols) {
		Cell c =sheet.getCell(cols, rows);

		String s = c.getContents();
		if (c.getType() == CellType.LABEL) {
			LabelCell labelc00 = (LabelCell)c;
			s = labelc00.getString();
		}

		return s;
  }
	

}
