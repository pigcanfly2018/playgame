package bsz.exch.game;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;

import bsz.exch.bean.LogInfo;
import bsz.exch.utils.Dom4jUtil;

public class KgApi {
	
	private static Logger logger=Logger.getLogger(KgApi.class);
	
	public  String url=null;
	
	public  String vs=null;
	
	public  String VendorId=null;
	
	public  String FundLink=null;
	
    public static final ThreadLocal<LogInfo>  LOCAL_LOG  = new ThreadLocal<LogInfo>();
    
    
    private KgApi(){
    	
    }
    private static HashMap<String,KgApi> map =new HashMap<String,KgApi>();
    
    public static KgApi get(String product){
    	KgApi kpi =map.get(product);
    	if(kpi==null){
    		kpi=new KgApi();
    		kpi.url=GameResource.instance().getConfig("game."+product+".kg.url");
    		kpi.vs=GameResource.instance().getConfig("game."+product+".kg.vs");
    		kpi.VendorId=GameResource.instance().getConfig("game."+product+".kg.VendorId");
    		kpi.FundLink=GameResource.instance().getConfig("game."+product+".kg.FundLink");
    		RegistryBuilder<ConnectionSocketFactory> registryBuilder = RegistryBuilder.<ConnectionSocketFactory>create();  
        	ConnectionSocketFactory plainSF = new PlainConnectionSocketFactory();  
        	registryBuilder.register("http", plainSF);  
        	Registry<ConnectionSocketFactory> registry = registryBuilder.build();
        	kpi.connManager = new PoolingHttpClientConnectionManager(registry);  
        	kpi.requestConfig = RequestConfig.custom().setSocketTimeout(35000).setConnectTimeout(35000).build();
        	kpi.build=HttpClientBuilder.create().setConnectionManager(kpi.connManager);
        	map.put(product, kpi);
    	}
    	return kpi;
    }
    
	private  PoolingHttpClientConnectionManager connManager;
	private  RequestConfig requestConfig;
	private  HttpClientBuilder build;

	
	
	private  String getRequst(String method,List<KgParam> params){
		StringBuffer sb =new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"iso-8859-1\"?>\n");
		sb.append("<methodCall>\n");
		sb.append("<methodName>"+method+"</methodName>\n");
		sb.append("<params><param><value><struct>\n");
		for(KgParam param:params){
			sb.append("<member><name>"+param.name+"</name><value><string>"+param.value+"</string></value></member>");
		}
		sb.append("</struct></value></param></params></methodCall>\n");
		return sb.toString();
	}
	/**
	 * 创建并获取游戏连接
	 * @throws Exception
	 */
	public  String CreateAccountAndLogin(String login_name,String ip){
		CloseableHttpClient  hc= build.build(); 
		HttpPost httppost = new HttpPost(url+"/player_enter_keno.php");  
		httppost.setConfig(requestConfig);
		List<KgParam> params=new ArrayList<KgParam>();
		params.add(new KgParam("Vendorsite",vs));
		params.add(new KgParam("FundLink",FundLink));
		params.add(new KgParam("VendorId",VendorId));
		params.add(new KgParam("PlayerId",login_name));
		params.add(new KgParam("PlayerRealName",login_name));
		params.add(new KgParam("PlayerCurrency","C01"));
		params.add(new KgParam("PlayerCredit","0"));
		params.add(new KgParam("PlayerAllowStake","1,2,3"));
		if("8datest".equals(login_name)||"8datest01".equals(login_name)
				||"8datest02".equals(login_name)){
			params.add(new KgParam("Trial","1"));
		}else{
			params.add(new KgParam("Trial","0"));
		}
		params.add(new KgParam("Language","SC"));
		params.add(new KgParam("RebateLevel","1"));
		params.add(new KgParam("PlayerIP",ip));
		params.add(new KgParam("VendorRef",""));
		params.add(new KgParam("Remarks","8dabet"));
		try {
			StringEntity myEntity = new StringEntity(getRequst("PlayerLanding",params)); 
	        httppost.addHeader("Content-Type", "text/xml"); 
	        httppost.setEntity(myEntity); 
		 	HttpResponse response = hc.execute(httppost);
			if (response.getStatusLine().getStatusCode() == 200) {
				String result=EntityUtils.toString(response.getEntity());
				Document xmlDoc = Dom4jUtil.String2Doc(result); 
				List<Element> elements= xmlDoc.selectNodes("/methodResponse/params/param/value/struct/member/name");
				logger.info(result);
				if(elements!=null&&elements.size()>0){
					if("Link".equals(elements.get(0).getStringValue())){
						Element e=(Element)xmlDoc.selectNodes("/methodResponse/params/param/value/struct/member/value/string").get(0);
						return e.getStringValue();
					}else{
						logger.error(result);
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			// hc.getConnectionManager().shutdown();
		}
		return null;
	}
	
	private  Long PreTransfer(String loginname,String type, BigDecimal credit) {
		CloseableHttpClient  hc= build.build(); 
		HttpPost httppost = new HttpPost(url+"/player_fund_in_out_first.php");  
		httppost.setConfig(requestConfig);
		List<KgParam> params=new ArrayList<KgParam>();
		params.add(new KgParam("VendorId",VendorId));
		params.add(new KgParam("PlayerId",loginname));
		params.add(new KgParam("Amount",credit.toString()));
		String xmlparams=getRequst("FundInOutFirst",params);
		try {
			StringEntity myEntity = new StringEntity(xmlparams); 
	        httppost.addHeader("Content-Type", "text/xml"); 
	        httppost.setEntity(myEntity); 
		 	HttpResponse response = hc.execute(httppost);
			if (response.getStatusLine().getStatusCode() == 200) {
				String result=EntityUtils.toString(response.getEntity());
				Document xmlDoc = Dom4jUtil.String2Doc(result); 
				logger.debug(result);
				List<Element> elements= xmlDoc.selectNodes("/methodResponse/params/param/value/struct/member/name");
				if(elements!=null&&elements.size()>0){
					if("FundIntegrationId".equals(elements.get(0).getStringValue())){
						Element e=(Element)xmlDoc.selectNodes("/methodResponse/params/param/value/struct/member/value/int").get(0);
					    return Long.valueOf(e.getStringValue());
					}else{
						logger.error(result);
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			 //hc.getConnectionManager().shutdown();
		}
		return null;
		
	}
	
	/**
	 * 
	 * @param loginname
	 * @param type
	 * @param credit
	 * @param ip
	 * @param FundIntegrationId
	 * @return
	 */
	private  boolean TransferConfirm(String loginname,String type, BigDecimal credit,String ip,Long FundIntegrationId) {
		CloseableHttpClient  hc= build.build(); 
		HttpPost httppost = new HttpPost(url+"/player_fund_in_out_confirm.php");  
		httppost.setConfig(requestConfig);
		List<KgParam> params=new ArrayList<KgParam>();
		params.add(new KgParam("VendorId",VendorId));
		params.add(new KgParam("PlayerId",loginname));
		params.add(new KgParam("Amount",credit.toString()));
		params.add(new KgParam("PlayerIP",ip));
		params.add(new KgParam("FundIntegrationId",String.valueOf(FundIntegrationId)));
		params.add(new KgParam("VendorRef",""));
		try {
			StringEntity myEntity = new StringEntity(getRequst("FundInOutConfirm",params)); 
	        httppost.addHeader("Content-Type", "text/xml"); 
	        httppost.setEntity(myEntity); 
		 	HttpResponse response = hc.execute(httppost);
			if (response.getStatusLine().getStatusCode() == 200) {
				String result=EntityUtils.toString(response.getEntity());
				Document xmlDoc = Dom4jUtil.String2Doc(result); 
				logger.debug(result);
				List<Element> elements= xmlDoc.selectNodes("/methodResponse/params/param/value/struct/member/name");
				if(elements!=null&&elements.size()>0){
					if("Remain".equals(elements.get(0).getStringValue())){
					   return true;
					}else{
						logger.error(result);
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			 //hc.getConnectionManager().shutdown();
		}
		return false;
		
	}
	
	public  boolean Transfer(String loginname,String billno,String type, BigDecimal credit,String ip){
	 try{
			 if("OUT".equals(type)){
				if(credit.floatValue()>0){
				    credit=new BigDecimal(0).subtract(credit);
				}
			}
			if("IN".equals(type)){ 
				if(credit.floatValue()<0){
				    credit=new BigDecimal(0).subtract(credit);
				}
			}
			Long id=PreTransfer(loginname,type, credit);
			if(id!=null){
				return TransferConfirm(loginname,type,credit,ip,id);
			}
			return false;
		
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
    }
	
	
	 public  BigDecimal GetBalance(String loginname) {
		    CloseableHttpClient  hc= build.build(); 
			HttpPost httppost = new HttpPost(url+"/player_get_credit.php");  
			httppost.setConfig(requestConfig);
			List<KgParam> params=new ArrayList<KgParam>();
			params.add(new KgParam("VendorId",VendorId));
			params.add(new KgParam("PlayerId",loginname));
			try {
				StringEntity myEntity = new StringEntity(getRequst("GetCredit",params)); 
		        httppost.addHeader("Content-Type", "text/xml"); 
		        httppost.setEntity(myEntity); 
			 	HttpResponse response = hc.execute(httppost);
				if (response.getStatusLine().getStatusCode() == 200) {
					String result=EntityUtils.toString(response.getEntity());
					Document xmlDoc = Dom4jUtil.String2Doc(result); 
					logger.debug(result);
						List<Element> elements= xmlDoc.selectNodes("/methodResponse/params/param/value/struct/member/name");
						if(elements!=null&&elements.size()>0){
							if("Credit".equals(elements.get(0).getStringValue())){
								Element e=(Element)xmlDoc.selectNodes("/methodResponse/params/param/value/struct/member/value/string").get(0);
							   return new BigDecimal(e.getStringValue());
							}else{
								logger.error(result);
							}
						}
				}
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				// hc.getConnectionManager().shutdown();
			}
			return new BigDecimal(0);
	 }
	 
	 public  String ForwardGame(String game_name,String ip){
		String url= CreateAccountAndLogin(game_name,ip);
		return vs+url;
	 }
	
	public static void main(String []stf)throws Exception{
		//System.out.println(KgApi.get("8da").ForwardGame("dawlance008","8.8.8.8"));
		System.out.println(KgApi.get("8da").GetBalance("dawlance008"));
	}

}
