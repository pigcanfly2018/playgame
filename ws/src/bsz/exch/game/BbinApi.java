package bsz.exch.game;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import net.sf.json.JSONObject;
import nu.xom.Builder;
import nu.xom.Document;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import bsz.exch.bean.LogInfo;
import bsz.exch.utils.DateUtil;
import bsz.exch.utils.MD5Util;
import bsz.exch.utils.RandomUtil;

public class BbinApi {
	 String website = null;
	 String uppername = null;
	 String dspurl = null;
	 String linkapiurl=null;
	 String createMemberKeyB = null;
	 String loginKeyB = null;
	 String logoutKeyB = null;
	 String checkUsrBalanceKeyB = null;
	 String transferKeyB = null;
	 String getbetKeyB =null;
	 String siteid =null;
	 String mobilekeyB =null;
	 String mobileurl = null;
	 String playgameKeyB = null;
	 
	private static Logger logger=Logger.getLogger(BbinApi.class);
	
    public static final ThreadLocal<LogInfo>  LOCAL_LOG  = new ThreadLocal<LogInfo>();
	
    private BbinApi(){
    	
    }
    private static Map<String,BbinApi> map =new HashMap<String,BbinApi>();
    
    public static BbinApi get(String product){
    	BbinApi api=map.get(product);
    	if(api==null){
    		api=new BbinApi();
    		api.website = GameResource.instance().getConfig("game."+product+".bbin.website");
    		api.uppername = GameResource.instance().getConfig("game."+product+".bbin.uppername");
    		api.dspurl = GameResource.instance().getConfig("game."+product+".bbin.dspurl");
    		api.createMemberKeyB = GameResource.instance().getConfig("game."+product+".bbin.createMemberKeyB");
    		api.loginKeyB = GameResource.instance().getConfig("game."+product+".bbin.loginKeyB");
    		api.logoutKeyB = GameResource.instance().getConfig("game."+product+".bbin.logoutKeyB");
    		api.checkUsrBalanceKeyB = GameResource.instance().getConfig("game."+product+".bbin.checkUsrBalanceKeyB");
    		api.transferKeyB = GameResource.instance().getConfig("game."+product+".bbin.transferKeyB");
    		api.getbetKeyB = GameResource.instance().getConfig("game."+product+".bbin.getbetKeyB");
    		api.siteid = GameResource.instance().getConfig("game."+product+".bbin.siteid");
    		api.mobilekeyB = GameResource.instance().getConfig("game."+product+".bbin.mobilekeyB");
    		api.mobileurl = GameResource.instance().getConfig("game."+product+".bbin.mobileurl");
    		api.linkapiurl=GameResource.instance().getConfig("game."+product+".bbin.linkapiurl");
    		api.playgameKeyB=GameResource.instance().getConfig("game."+product+".bbin.playGameKeyB");
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
	public  boolean CheckOrCreateGameAccount(String loginname,String password) {
		
		CloseableHttpClient  hc= HttpClients.createDefault();; 
		 CloseableHttpResponse response = null;
		String url=linkapiurl+ "/app/WebService/XML/display.php/CreateMember";
		HttpPost httppost = new HttpPost(url);
		httppost.setConfig(requestConfig);
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("website", website));
		nvps.add(new BasicNameValuePair("username", loginname));
		nvps.add(new BasicNameValuePair("uppername", "d8dabet"));
		//nvps.add(new BasicNameValuePair("password", password));
		String key = RandomUtil.getRandomChar(5);
		try {
            String kh = new StringBuilder(String.valueOf(website)).append(loginname).append(createMemberKeyB).append(getUsEastTime()).toString();
		     nvps.add(new BasicNameValuePair("key", key + MD5Util.MD5(kh).toLowerCase()
				+ RandomUtil.getRandomChar(2)));
			httppost.setEntity(new UrlEncodedFormEntity(nvps));
			response = hc.execute(httppost);
			if (response.getStatusLine().getStatusCode() == 200) {
				String result=EntityUtils.toString(response.getEntity());
				logger.info(log(url,nvps,result));
				if(result==null) return false;
				Document xmlDoc =null;
				try { 
					Builder builder = new Builder();
				    xmlDoc = builder.build(result,null);
					String info=xmlDoc.getRootElement().getFirstChildElement("Record").getFirstChildElement("Code").getValue();
					if("21100".equals(info) || "21001".equals(info) )return true;
				} catch (Exception e) {
					e.printStackTrace();
				}
				
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
	
	
	/**
	 * 创建手机账户
	 * @param loginname
	 * @param password
	 * @return
	 */
	public  boolean CheckOrCreateMobileAccount(String loginname,String password) {
		
		CloseableHttpClient  hc= HttpClients.createDefault();; 
		 CloseableHttpResponse response = null; 
		String url=mobileurl+ "/CreateUser.ashx";
		HttpPost httppost = new HttpPost(url);
		httppost.setConfig(requestConfig);
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("siteid", siteid));
		nvps.add(new BasicNameValuePair("username", loginname));
		nvps.add(new BasicNameValuePair("password", password));
		try {
            String kh = new StringBuilder("").append(loginname).append(mobilekeyB).append(getUsEastTime()).toString();
		     nvps.add(new BasicNameValuePair("key", MD5Util.MD5(kh).toLowerCase()));
			httppost.setEntity(new UrlEncodedFormEntity(nvps));
			response = hc.execute(httppost);
			if (response.getStatusLine().getStatusCode() == 200) {
				String result=EntityUtils.toString(response.getEntity());
				
				logger.info(log(url,nvps,result));
				if(result==null) return false;
				try {
					JSONObject jsresult = JSONObject.fromObject( result );
					
					
					String info=jsresult.getJSONObject("data").getString("Code");
					if("50001".equals(info) || "21001".equals(info) )return true;
				} catch (Exception e) {
					e.printStackTrace();
				}
				
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
	
	
	/**
	 * 更新手机账户密码
	 * @param loginname
	 * @param password
	 * @return
	 */
	public  boolean UpdateMobileAccountPassword(String loginname,String password) {
		
		 CloseableHttpClient  hc= HttpClients.createDefault();; 
		 CloseableHttpResponse response = null; 
		String url=mobileurl+ "/ChangeUserPwd.ashx";
		HttpPost httppost = new HttpPost(url);
		httppost.setConfig(requestConfig);
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("siteid", siteid));
		nvps.add(new BasicNameValuePair("username", loginname));
		nvps.add(new BasicNameValuePair("password", password));
		try {
            String kh = new StringBuilder("").append(loginname).append(mobilekeyB).append(getUsEastTime()).toString();
		     nvps.add(new BasicNameValuePair("key", MD5Util.MD5(kh).toLowerCase()));
			httppost.setEntity(new UrlEncodedFormEntity(nvps));
			response = hc.execute(httppost);
			if (response.getStatusLine().getStatusCode() == 200) {
				String result=EntityUtils.toString(response.getEntity());
				
				logger.info(log(url,nvps,result));
				if(result==null) return false;
				try {
					JSONObject jsresult = JSONObject.fromObject( result );
					
					
					String info=jsresult.getJSONObject("data").getString("Code");
					if("50001".equals(info))return true;
				} catch (Exception e) {
					e.printStackTrace();
				}
				
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
	
	
	  private static BigDecimal getCredit(String info){
		  try{
				BigDecimal big=new BigDecimal(info);
				return big;
			}catch(Exception e){
				return new BigDecimal("0.00");
			}
	  }
	/**
	 * 查询余额
	 * @param loginname
	 * @return
	 */
	 public  BigDecimal GetBalance(String loginname) {
	    CloseableHttpClient  hc= HttpClients.createDefault();; 
	    CloseableHttpResponse response = null;
		String url=linkapiurl+ "/app/WebService/XML/display.php/CheckUsrBalance";
		HttpPost httppost = new HttpPost(url);
		httppost.setConfig(requestConfig);
	    try {
	    	String kh=new StringBuilder(String.valueOf(website)).append(loginname)
	    	        .append(checkUsrBalanceKeyB).append(getUsEastTime()).toString();
	      String key = RandomUtil.getRandomChar(9) +  MD5Util.MD5(kh).toLowerCase()+RandomUtil.getRandomChar(6);
	  	  List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		  nvps.add(new BasicNameValuePair("website", website));
		  nvps.add(new BasicNameValuePair("username", loginname));
		  nvps.add(new BasicNameValuePair("uppername", "d8dabet"));
		  nvps.add(new BasicNameValuePair("key", key));
		  httppost.setEntity(new UrlEncodedFormEntity(nvps));
			 response = hc.execute(httppost);
			if (response.getStatusLine().getStatusCode() == 200) {
				String result= EntityUtils.toString(response.getEntity());
				//System.out.println(result);
				logger.info(log(url,nvps,result));
				if(result==null) return new BigDecimal(0);
				Document xmlDoc =null;
				try {
					Builder builder = new Builder();
				    xmlDoc = builder.build(result,null);
				    if(xmlDoc.getRootElement().getFirstChildElement("Record").getFirstChildElement("Balance") != null){
				    	String info=xmlDoc.getRootElement().getFirstChildElement("Record").getFirstChildElement("Balance").getValue();
						return getCredit(info);
				    }
					
				} catch (Exception e) {
					e.printStackTrace();
				}
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

	 
  public  boolean Transfer(String loginname, String tranferno, String type, BigDecimal remit) {
	  
	  CloseableHttpClient  hc= HttpClients.createDefault();; 
	    CloseableHttpResponse response = null;
	  try{
		    //CloseableHttpClient  hc= build.build(); 
	        String url=linkapiurl+ "/app/WebService/XML/display.php/Transfer";
			HttpPost httppost = new HttpPost(url);
			httppost.setConfig(requestConfig);
		   try {
			  String kh=new StringBuilder(String.valueOf(website)).append(loginname).append(tranferno)
			   	       .append(transferKeyB).append(getUsEastTime()).toString();
		      String key = RandomUtil.getRandomChar(2) +  MD5Util.MD5(kh).toLowerCase()+RandomUtil.getRandomChar(7);
		
		      BigDecimal newcredit = null;
		      BigDecimal localCredit=new BigDecimal(0);
		     //.setScale(2,BigDecimal.ROUND_DOWN)
		     if ("IN".equals(type)) {
		       localCredit=remit;
		       newcredit = localCredit.subtract(remit);
		     } else if ("OUT".equals(type)) {
		       newcredit = localCredit.add(remit);
		     }
		 	 List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			  nvps.add(new BasicNameValuePair("website", website));
			  nvps.add(new BasicNameValuePair("username", loginname));
			  nvps.add(new BasicNameValuePair("uppername", "d8dabet"));
			  nvps.add(new BasicNameValuePair("remitno", tranferno));
			  nvps.add(new BasicNameValuePair("action", type));
			  nvps.add(new BasicNameValuePair("remit", ""+remit.intValue()));
			  nvps.add(new BasicNameValuePair("newcredit", ""+newcredit));
			  nvps.add(new BasicNameValuePair("credit", ""+localCredit));
			  nvps.add(new BasicNameValuePair("key", key));
			  httppost.setEntity(new UrlEncodedFormEntity(nvps));
			  response = hc.execute(httppost);
				if (response.getStatusLine().getStatusCode() == 200) {
					String result=EntityUtils.toString(response.getEntity());
					logger.info(log(url,nvps,result));
					if(result==null) return false;
					Document xmlDoc =null;
					try {
						Builder builder = new Builder();
					    xmlDoc = builder.build(result,null);
					    String info=xmlDoc.getRootElement().getFirstChildElement("Record").getFirstChildElement("Code").getValue();
						if("11100".equals(info))return true;
					} catch (Exception e) {
						e.printStackTrace();
					}
					
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
	  }catch(Exception e){
			e.printStackTrace();
			return false;
		}
 }
  
  /**
   * 
   * @param loginname
   * @param password
   * @param psite live|ball|ltlottery|3DHall|live|game
   * @return
   */
	  public  String ForwardGame(String loginname,String password,String psite){
	     if(psite==null||"".equals("psite")){
			 psite="live";
		 }
		  String kh=new StringBuilder(String.valueOf(website)).append(loginname)
					    .append(loginKeyB).append(getUsEastTime()).toString();
		  String key = RandomUtil.getRandomChar(8) +MD5Util.MD5(kh).toLowerCase() + RandomUtil.getRandomChar(1);
		  String params = "?website=" + website + "&username=" + loginname + "&uppername=" + 
				    uppername + "&lang=zh-cn&page_site="+psite+"&key=" + key;
		  return dspurl + "/app/WebService/XML/display.php/Login" + params;
	  }
	  
	  public String login2Forward(String loginname,String password,String gametype){
		  if(gametype==null||"".equals(gametype)){
				 return "请联系管理员。";
			 }
		  
		  CloseableHttpClient  hc= HttpClients.createDefault();; 
			 CloseableHttpResponse response = null;
			String url= dspurl+"/app/WebService/JSON/display.php/Login2";
			HttpPost httppost = new HttpPost(url);
			httppost.setConfig(requestConfig);
			String info = null;
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("website", website));
			nvps.add(new BasicNameValuePair("username", loginname));
			nvps.add(new BasicNameValuePair("uppername", uppername));
			nvps.add(new BasicNameValuePair("lang", "zh-cn"));
			try {
				String kh=new StringBuilder(String.valueOf(website)).append(loginname)
					    .append(loginKeyB).append(getUsEastTime()).toString();
				
			     nvps.add(new BasicNameValuePair("key", RandomUtil.getRandomChar(8) + MD5Util.MD5(kh).toLowerCase()+ RandomUtil.getRandomChar(1)));
			     httppost.setEntity(new UrlEncodedFormEntity(nvps));
					response = hc.execute(httppost);
				if (response.getStatusLine().getStatusCode() == 200) {
					String result=EntityUtils.toString(response.getEntity());
					logger.info(log(url,nvps,result));
					if(result==null) return "";
					
					try { 
						JSONObject jsresult = JSONObject.fromObject( result );
						
						
						 info=jsresult.getJSONObject("data").getString("Code");
						if("99999".equals(info)){
							if(gametype.equals("30599")){
								return ForwardGameH5By30( loginname, password, gametype);
							}else if(gametype.equals("38001")){
								return ForwardGameH5By38( loginname, password, gametype);
							}
							return ForwardGameBygameType( loginname, password, gametype);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					
				} else {
					return info;
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
	 
	  public  String ForwardGameBygameType(String loginname,String password,String gametype){
		     
			  String kh=new StringBuilder(String.valueOf(website)).append(loginname)
						    .append(playgameKeyB).append(getUsEastTime()).toString();
			  String key = RandomUtil.getRandomChar(7) +MD5Util.MD5(kh).toLowerCase() + RandomUtil.getRandomChar(1);
			  String params = "?website=" + website + "&username=" + loginname + "&gamekind=5&gametype="+gametype+ "&lang=zh-cn&key=" + key;
			  return dspurl + "/app/WebService/XML/display.php/PlayGame" + params;
		  }
	  
	  public  String ForwardGameH5By30(String loginname,String password,String gametype){//捕鱼达人
		     
		  String kh=new StringBuilder(String.valueOf(website)).append(loginname)
					    .append(playgameKeyB).append(getUsEastTime()).toString();
		  String key = RandomUtil.getRandomChar(7) +MD5Util.MD5(kh).toLowerCase() + RandomUtil.getRandomChar(1);
		  String params = "?website=" + website + "&username=" + loginname + "&uppername="+uppername+ "&gametype="+gametype+"&key=" + key;
		  return dspurl + "/app/WebService/XML/display.php/ForwardGameH5By30" + params;
	  }
	  
	  public  String ForwardGameH5By38(String loginname,String password,String gametype){//捕鱼大师
		     
		  String kh=new StringBuilder(String.valueOf(website)).append(loginname)
					    .append(playgameKeyB).append(getUsEastTime()).toString();
		  String key = RandomUtil.getRandomChar(7) +MD5Util.MD5(kh).toLowerCase() + RandomUtil.getRandomChar(1);
		  String params = "?website=" + website + "&username=" + loginname + "&uppername="+uppername+ "&gametype="+gametype+"&key=" + key;
		  return dspurl + "/app/WebService/XML/display.php/ForwardGameH5By38" + params;
	  }
	  
//	  public String ForwardGameH5By38(String loginname,String password,String gametype){
//		  if(gametype==null||"".equals(gametype)){
//				 return "请联系管理员。";
//			 }
//		  
//		  CloseableHttpClient  hc= HttpClients.createDefault();
//			 CloseableHttpResponse response = null;
//			String url= dspurl+"/app/WebService/JSON/display.php/Login2";
//			HttpPost httppost = new HttpPost(url);
//			httppost.setConfig(requestConfig);
//			String info = null;
//			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
//			nvps.add(new BasicNameValuePair("website", website));
//			nvps.add(new BasicNameValuePair("username", loginname));
//			nvps.add(new BasicNameValuePair("uppername", uppername));
//			nvps.add(new BasicNameValuePair("lang", "zh-cn"));
//			try {
//				String kh=new StringBuilder(String.valueOf(website)).append(loginname)
//					    .append(loginKeyB).append(getUsEastTime()).toString();
//				
//			     nvps.add(new BasicNameValuePair("key", RandomUtil.getRandomChar(8) + MD5Util.MD5(kh).toLowerCase()+ RandomUtil.getRandomChar(1)));
//			     httppost.setEntity(new UrlEncodedFormEntity(nvps));
//					response = hc.execute(httppost);
//				if (response.getStatusLine().getStatusCode() == 200) {
//					String result=EntityUtils.toString(response.getEntity());
//					logger.info(log(url,nvps,result));
//					if(result==null) return "";
//					
//					try { 
//						JSONObject jsresult = JSONObject.fromObject( result );
//						
//						
//						 info=jsresult.getJSONObject("data").getString("Code");
//						if("99999".equals(info)){
//							return ForwardGameBygameType2( loginname, password, gametype);
//						}
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//					
//				} else {
//					return info;
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}finally {  
//	            try {  
//	                // 关闭连接,释放资源  
//	                if (response != null) {  
//	                    response.close();  
//	                }  
//	                if (hc != null) {  
//	                	hc.close();  
//	                }  
//	            } catch (Exception e) {  
//	                e.printStackTrace();  
//	            }  
//	        }
//			return "系统出现故障，请联系管理员";
//	  }
	 
	private static String getUsEastTime() {
		long time = new Date().getTime() - 43200000L;
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		return DateUtil.fmtyyyyMMdd(calendar.getTime());
	}
	
	public static void main(String[] args) throws Exception{
		 //System.out.println(BbinApi.get("8da").CheckOrCreateMobileAccount("daway121314", "ay123456"));
		 //System.out.println(BbinApi.get("8da").CheckOrCreateGameAccount("daw83587223", "b123b123"));
		//System.out.println(BbinApi.get("8da").get("8da").UpdateMobileAccountPassword("daway121314", "ay123456"));
		System.out.println(BbinApi.get("8da").GetBalance("daw5173888"));
		//Thread.sleep(2000);//去掉报错
		//System.out.println(BbinApi.get("8da").Transfer("dawwoody", "1"+new Date().getTime(), "50", new BigDecimal(500)));
		
		
		System.out.println(BbinApi.get("8da").Transfer("daw5173888", "1"+new Date().getTime(), "OUT", new BigDecimal(1)));
		//System.out.println(BbinApi.get("8da").GetBalance("dawsxh789789"));
		//System.out.println(BbinApi.get("8da").Transfer("dawwoody", "1"+new Date().getTime(), "OUT", new BigDecimal(100)));
		//System.out.println(BbinApi.get("8da").GetBalance("dawwoody"));
//		System.out.println(BbinApi.get("8da").GetBalance("dawwoody"));
		//System.out.println(ForwardGame("dawlance008","a123a123","3DHall"));
		
		
//		Workbook rwb = Workbook.getWorkbook(new File("d:/bbin.xls"));
//		Sheet rs = rwb.getSheet(0);
//		int rsRows = rs.getRows();
//		for(int i=1;i<rsRows;i++){
//			Cell[] cell = rs.getRow(i);
//			String bbinname = cell[7].getContents();
//			BigDecimal balance = BbinApi.get("8da").GetBalance(bbinname);
//			if(balance != null && balance.intValue() >= 10){
//				System.out.println(bbinname.replaceFirst("daw", "") +"     "+i +"      "+balance.intValue());
//			}
//			
//		}
		//System.out.println(BbinApi.get("8da").ForwardGame("daw83587223","a123a123","fishareal"));
		//System.out.println(BbinApi.get("8da").ForwardGameH5By38("daw83587223","b123b123","38001"));
	}

}
