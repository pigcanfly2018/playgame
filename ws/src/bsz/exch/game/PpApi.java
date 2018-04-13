package bsz.exch.game;

import java.math.BigDecimal;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import nu.xom.Builder;
import nu.xom.Document;

import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import bsz.exch.bean.LogInfo;
import bsz.exch.utils.AnyTrustStrategy;
import bsz.exch.utils.MD5Util;
import bsz.exch.utils.RandomUtil;

public class PpApi {
	
	String secureLogin  = null;
	String secretKey  = null;
	String posturl=null;

	private static Logger logger=Logger.getLogger(PpApi.class);
	
    public static final ThreadLocal<LogInfo>  LOCAL_LOG  = new ThreadLocal<LogInfo>();
	
    private PpApi(){
    	
    }
    private static Map<String,PpApi> map =new HashMap<String,PpApi>();
    
    private  PoolingHttpClientConnectionManager connManager;
	private  RequestConfig requestConfig;
	private  HttpClientBuilder build;
	
    public static PpApi get(String product){
    	PpApi api=map.get(product);
    	if(api==null){
    		api=new PpApi();
    		
    		api.secureLogin = GameResource.instance().getConfig("game."+product+".pp.secureLogin");
    		api.secretKey = GameResource.instance().getConfig("game."+product+".pp.secretKey");
    		api.posturl = GameResource.instance().getConfig("game."+product+".pp.posturl");
    		
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
    
    private static String log(String dspurl,List<NameValuePair> nvps,String result){
		StringBuffer sb=new StringBuffer();
		sb.append("BBIN request -"+dspurl+",");
		sb.append("post_data-{");
		for(NameValuePair nv:nvps){
			sb.append(nv.getName()+":"+nv.getValue()+",");
		}
		sb.append("}");
		sb.append("reponse - ");
		sb.append(result);
		LOCAL_LOG.set(new LogInfo());
		LOCAL_LOG.get().addLog(sb.toString());
		return sb.toString();
	}
    
    /**
	 * 创建账户
	 * @param loginname
	 * @param password
	 * @return
	 */
	public  boolean CheckOrCreateGameAccount(String loginname) {
		
		CloseableHttpClient  hc= HttpClients.createDefault();; 
		 CloseableHttpResponse response = null;
		String url=posturl+ "/player/account/create/";
		HttpPost httppost = new HttpPost(url);
		httppost.setConfig(requestConfig);
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("secureLogin", secureLogin));
		nvps.add(new BasicNameValuePair("externalPlayerId", loginname));
		nvps.add(new BasicNameValuePair("currency", "CNY"));
		//nvps.add(new BasicNameValuePair("password", password));
		try {
            String kh = new StringBuilder("currency=CNY&externalPlayerId="+loginname+"&secureLogin="+secureLogin+secretKey).toString();
           // System.out.println(kh);
		     nvps.add(new BasicNameValuePair("hash", MD5Util.MD5(kh).toLowerCase()));
			httppost.setEntity(new UrlEncodedFormEntity(nvps));
			response = hc.execute(httppost);
			System.out.println(response.getStatusLine().getStatusCode()+url);
			if (response.getStatusLine().getStatusCode() == 200) {
				String result=EntityUtils.toString(response.getEntity());
				//System.out.println(result);
				logger.info(log(url,nvps,result));
				if(result==null) return false;
				JSONObject jsresult = JSONObject.fromObject( result ); 
				if(jsresult.getString("error").equals("0"))
					return true;
				
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {  
            try {  
                // 关闭连接,释放资源  
                if (response != null) {  
                    response.close();  
                }  
                if (hc != null) {  
                	hc.close();  
                }  
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
        }
		return false;
	}
	
	
	public  boolean Transfer(String loginname, String tranferno, BigDecimal remit) {
		
		CloseableHttpClient  hc= HttpClients.createDefault();; 
		 CloseableHttpResponse response = null;
		String url=posturl+ "/balance/transfer/";
		HttpPost httppost = new HttpPost(url);
		httppost.setConfig(requestConfig);
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("secureLogin", secureLogin));
		nvps.add(new BasicNameValuePair("externalPlayerId", loginname));
		nvps.add(new BasicNameValuePair("externalTransactionId", tranferno));
		nvps.add(new BasicNameValuePair("amount", remit.toString()));
		//nvps.add(new BasicNameValuePair("password", password));
		try {
            String kh = new StringBuilder("amount="+remit.toString()+"&externalPlayerId="+loginname+"&externalTransactionId="+tranferno+"&secureLogin="+secureLogin+secretKey).toString();
           // System.out.println(kh);
		     nvps.add(new BasicNameValuePair("hash", MD5Util.MD5(kh).toLowerCase()));
			httppost.setEntity(new UrlEncodedFormEntity(nvps));
			response = hc.execute(httppost);
			if (response.getStatusLine().getStatusCode() == 200) {
				String result=EntityUtils.toString(response.getEntity());
				//System.out.println(result);
				logger.info(log(url,nvps,result));
				if(result==null) return false;
				JSONObject jsresult = JSONObject.fromObject( result ); 
				if(jsresult.getString("error").equals("0"))
					return true;
				
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {  
            try {  
                // 关闭连接,释放资源  
                if (response != null) {  
                    response.close();  
                }  
                if (hc != null) {  
                	hc.close();  
                }  
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
        }
		return false;
	}
	
	
	public  BigDecimal GetBalance(String loginname) {
		
		CloseableHttpClient  hc= HttpClients.createDefault();; 
		 CloseableHttpResponse response = null;
		String url=posturl+ "/balance/current/";
		HttpPost httppost = new HttpPost(url);
		httppost.setConfig(requestConfig);
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("secureLogin", secureLogin));
		nvps.add(new BasicNameValuePair("externalPlayerId", loginname));
		//nvps.add(new BasicNameValuePair("password", password));
		try {
            String kh = new StringBuilder("externalPlayerId="+loginname+"&secureLogin="+secureLogin+secretKey).toString();
           
		     nvps.add(new BasicNameValuePair("hash", MD5Util.MD5(kh).toLowerCase()));
			httppost.setEntity(new UrlEncodedFormEntity(nvps));
			response = hc.execute(httppost);
			 //System.out.println("woody      "+response.getStatusLine().getStatusCode());
			if (response.getStatusLine().getStatusCode() == 200) {
				String result=EntityUtils.toString(response.getEntity());
				System.out.println("woody      "+result);
				logger.info(log(url,nvps,result));
				if(result==null) return new BigDecimal(0);
				JSONObject jsresult = JSONObject.fromObject( result ); 
				if(jsresult.getString("error").equals("0"))
					return new BigDecimal(jsresult.getString("balance"));
				
			} else {
				return new BigDecimal(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {  
            try {  
                // 关闭连接,释放资源  
                if (response != null) {  
                    response.close();  
                }  
                if (hc != null) {  
                	hc.close();  
                }  
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
        }
		return new BigDecimal(0);
	}
	
	
	public  String ForwardGame(String loginname,String gamecode) {
		
		CloseableHttpClient  hc= HttpClients.createDefault();; 
		 CloseableHttpResponse response = null;
		String url=posturl+ "/game/start/";
		HttpPost httppost = new HttpPost(url);
		httppost.setConfig(requestConfig);
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("secureLogin", secureLogin));
		nvps.add(new BasicNameValuePair("externalPlayerId", loginname));
		nvps.add(new BasicNameValuePair("gameId", gamecode));
		nvps.add(new BasicNameValuePair("language", "zh"));
		nvps.add(new BasicNameValuePair("lobbyURL", "https://www.8da2017.com"));
		//nvps.add(new BasicNameValuePair("cashierURL", cashierURL));
		try {
            //String kh = new StringBuilder("cashierURL="+cashierURL+"&externalPlayerId="+loginname+"&gameId="+gamecode+"&language=zh&secureLogin="+secureLogin+secretKey).toString();
			 String kh = new StringBuilder("externalPlayerId="+loginname+"&gameId="+gamecode+"&language=zh&lobbyURL=https://www.8da2017.com&secureLogin="+secureLogin+secretKey).toString();
			// System.out.println(kh);
		     nvps.add(new BasicNameValuePair("hash", MD5Util.MD5(kh).toLowerCase()));
			httppost.setEntity(new UrlEncodedFormEntity(nvps));
			response = hc.execute(httppost);
			System.out.println("responsecode  "+response.getStatusLine().getStatusCode());
			if (response.getStatusLine().getStatusCode() == 200) {
				String result=EntityUtils.toString(response.getEntity());
				//System.out.println(result);
				logger.info(log(url,nvps,result));
				if(result==null) return "系统出现故障，请联系管理员";
				JSONObject jsresult = JSONObject.fromObject( result ); 
				if(jsresult.getString("error").equals("0"))
					return jsresult.getString("gameURL");
				
			} else {
				return "系统出现故障，请联系管理员";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {  
            try {  
                // 关闭连接,释放资源  
                if (response != null) {  
                    response.close();  
                }  
                if (hc != null) {  
                	hc.close();  
                }  
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
        }
		return "系统出现故障，请联系管理员";
	}
	
	

	
	public  void getGameList() {
		
		CloseableHttpClient  hc= HttpClients.createDefault();; 
		 CloseableHttpResponse response = null;
		String url=posturl+ "/Integrations/";
		HttpPost httppost = new HttpPost(url);
		httppost.setConfig(requestConfig);
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("secureLogin", secureLogin));
		//nvps.add(new BasicNameValuePair("password", password));
		try {
            String kh = new StringBuilder("secureLogin="+secureLogin+secretKey).toString();
           // System.out.println(kh);
		     nvps.add(new BasicNameValuePair("hash", MD5Util.MD5(kh).toLowerCase()));
			httppost.setEntity(new UrlEncodedFormEntity(nvps));
			response = hc.execute(httppost);
			if (response.getStatusLine().getStatusCode() == 200) {
				String result=EntityUtils.toString(response.getEntity());
				//System.out.println(result);
				JSONObject jsresult = JSONObject.fromObject( result ); 
				JSONArray jarray = JSONArray.fromObject(jsresult.getJSONArray("gameList"));
				System.out.println(jarray.size());
				for(int i = 0 ;i<jarray.size();i++){
					JSONObject game = jarray.optJSONObject(i);
					System.out.println(game.getString("gameID"));
				}
			//	logger.info(log(url,nvps,result));
				
				
			} else {
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {  
            try {  
                // 关闭连接,释放资源  
                if (response != null) {  
                    response.close();  
                }  
                if (hc != null) {  
                	hc.close();  
                }  
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
        }
		
	}

	public static String[] visitors=new String[]{"dawvisitor00",
		"dawvisitor01","dawvisitor02","dawvisitor03","dawvisitor04","dawvisitor05",
		"dawvisitor06","dawvisitor07","dawvisitor08","dawvisitor09","dawvisitor10",
		"dawvisitor11","dawvisitor12","dawvisitor13","dawvisitor14","dawvisitor15",
		"dawvisitor16","dawvisitor17","dawvisitor18","dawvisitor19","dawvisitor20",
		"dawvisitor21","dawvisitor22","dawvisitor23","dawvisitor24","dawvisitor25",
		"dawvisitor26","dawvisitor27","dawvisitor28","dawvisitor29","dawvisitor30"};
	
	public static void main(String[] args) throws Exception{
		
		for(String visitor :visitors){
		
			//System.out.println(PpApi.get("8da").ForwardGame("dawwoody","vs20eightdragons"));
			//System.out.println(visitor+"     " +(PpApi.get("8da").CheckOrCreateGameAccount(visitor)));
		}
		
		//System.out.println(PpApi.get("8da").CheckOrCreateGameAccount("dawwoody"));
		//System.out.println(PpApi.get("8da").GetBalance("dawwoody"));
		System.out.println(PpApi.get("8da").ForwardGame("dawwoody","vs20eightdragons"));
		//PpApi.get("8da").getGameList();
		
		//System.out.println(PpApi.get("8da").Transfer("dawamaya88", "1"+new Date().getTime(), new BigDecimal(-500)));
		//System.out.println(PpApi.get("8da").Transfer("dawwoody", "1"+new Date().getTime(), new BigDecimal(-10)));
	}
    
    
}
