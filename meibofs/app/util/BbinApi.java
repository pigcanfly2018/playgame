package util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import models.BbinBetdata;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;

public class BbinApi {
	static String website = "LWIN999";
	static String uppername = "d8dabet";
	static String dspurl = "http://888.8da.ph";
	static String createMemberKeyB = "qYI0s9qmp";
	static String loginKeyB = "jVT56kw";
	static String logoutKeyB = "2c4URy4";
	static String checkUsrBalanceKeyB = "F7rhvnElc";
	static String transferKeyB = "53IkD3JMon";
	static String getbetKeyB = "wIPOb81es7";
	private static Logger logger=Logger.getLogger(BbinApi.class);
	
	private static String log(String dspurl,List<NameValuePair> nvps,String result){
		StringBuffer sb=new StringBuffer();
		sb.append("BBIN request -"+dspurl+",\n");
		sb.append("post_data-{");
		for(NameValuePair nv:nvps){
			sb.append(nv.getName()+":"+nv.getValue()+",");
		}
		sb.append("}\n");
		sb.append("reponse - ");
		sb.append(result);
		return sb.toString();
	}
	/**
	 * 创建账户
	 * @param loginname
	 * @param password
	 * @return
	 */
	public static boolean CheckOrCreateGameAccount(String loginname,String password) {
		HttpClient hc = new DefaultHttpClient();
		hc.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,35000);
		hc.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,35000);
		HttpConnectionParams.setConnectionTimeout(hc.getParams(), 35000);
		HttpConnectionParams.setSoTimeout(hc.getParams(), 35000);
		
		String url=dspurl+ "/app/WebService/XML/display.php/CreateMember";
		HttpPost httppost = new HttpPost(url);
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("website", website));
		nvps.add(new BasicNameValuePair("username", loginname));
		nvps.add(new BasicNameValuePair("uppername", "d8dabet"));
		nvps.add(new BasicNameValuePair("password", password));
		String key = RandomUtil.getRandomChar(5);
		try {
            String kh = new StringBuilder(String.valueOf(website)).append(loginname).append(createMemberKeyB).append(getUsEastTime()).toString();
		     nvps.add(new BasicNameValuePair("key", key + MD5Util.MD5(kh).toLowerCase()
				+ RandomUtil.getRandomChar(2)));
			httppost.setEntity(new UrlEncodedFormEntity(nvps));
			HttpResponse response = hc.execute(httppost);
			if (response.getStatusLine().getStatusCode() == 200) {
				String result=EntityUtils.toString(response.getEntity());
				logger.info(log(url,nvps,result));
				if(result==null) return false;
				Document xmlDoc =null;
				try { 
					xmlDoc = Dom4jUtil.String2Doc(result); 
					String info=xmlDoc.getRootElement().element("Record").element("Code").getStringValue();
					if("21100".equals(info))return true;
				} catch (DocumentException e) {
					e.printStackTrace();
				}
				
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			  hc.getConnectionManager().shutdown();
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
	 public static BigDecimal GetBalance(String loginname) {
		HttpClient hc = new DefaultHttpClient();
		hc.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,35000);
		hc.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,35000);
		HttpConnectionParams.setConnectionTimeout(hc.getParams(), 35000);
		HttpConnectionParams.setSoTimeout(hc.getParams(), 35000);
		String url=dspurl+ "/app/WebService/XML/display.php/CheckUsrBalance";
		HttpPost httppost = new HttpPost(url);
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
			HttpResponse response = hc.execute(httppost);
			if (response.getStatusLine().getStatusCode() == 200) {
				String result= EntityUtils.toString(response.getEntity());
				logger.info(log(url,nvps,result));
				if(result==null) return new BigDecimal(0);
				Document xmlDoc =null;
				try {
					xmlDoc = Dom4jUtil.String2Doc(result);
					String info=xmlDoc.getRootElement().element("Record").element("Balance").getStringValue();
					return getCredit(info);
				} catch (DocumentException e) {
					e.printStackTrace();
				}
			} else {
				return new BigDecimal(0);
			}
	    } catch (Exception e) {
	      e.printStackTrace();
	    }finally{
	  	  hc.getConnectionManager().shutdown();
	    }
	    return new BigDecimal(0);
   }

	 
  public static boolean Transfer(String loginname, String tranferno, String type, BigDecimal remit) {
   HttpClient hc = new DefaultHttpClient();
	hc.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,35000);
	hc.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,35000);
	HttpConnectionParams.setConnectionTimeout(hc.getParams(), 35000);
	HttpConnectionParams.setSoTimeout(hc.getParams(), 35000);
   String url=dspurl+ "/app/WebService/XML/display.php/Transfer";
	HttpPost httppost = new HttpPost(url);
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
	  
	  HttpResponse response = hc.execute(httppost);
		if (response.getStatusLine().getStatusCode() == 200) {
			String result=EntityUtils.toString(response.getEntity());
			logger.info(log(url,nvps,result));
			//System.out.println();
			if(result==null) return false;
			Document xmlDoc =null;
			try {
				xmlDoc = Dom4jUtil.String2Doc(result);
				String info=xmlDoc.getRootElement().element("Record").element("Code").getStringValue();
				if("11100".equals(info))return true;
			} catch (DocumentException e) {
				e.printStackTrace();
			}
			
		} else {
			return false;
		}
  } catch (Exception e) {
    e.printStackTrace();
  }finally{
	  hc.getConnectionManager().shutdown();
  }
  return false;
 }
  
  
  public static List<BbinBetdata> getBetRecords(String username,String rounddate,String starttime,String endtime,int page,String gamekind,String gametype){
	  	HttpClient hc = new DefaultHttpClient();
		hc.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,35000);
		hc.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,35000);
		HttpConnectionParams.setConnectionTimeout(hc.getParams(), 35000);
		HttpConnectionParams.setSoTimeout(hc.getParams(), 35000);
		String url=dspurl+ "/app/WebService/XML/display.php/BetRecord";
		HttpPost httppost = new HttpPost(url);
		try{
			String kh=new StringBuilder(String.valueOf(website)).append(username)
	    	        .append(getbetKeyB).append(getUsEastTime()).toString();
	      String key = RandomUtil.getRandomChar(1) +  MD5Util.MD5(kh).toLowerCase()+RandomUtil.getRandomChar(7);
	      List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		  nvps.add(new BasicNameValuePair("website", website));
		  nvps.add(new BasicNameValuePair("username", username));
		  nvps.add(new BasicNameValuePair("uppername", "d8dabet"));
		  nvps.add(new BasicNameValuePair("rounddate", rounddate));
		  nvps.add(new BasicNameValuePair("starttime", starttime));
		  nvps.add(new BasicNameValuePair("endtime", endtime));
		  nvps.add(new BasicNameValuePair("gamekind", gamekind));
		  if(gamekind.equals("12")){
			  nvps.add(new BasicNameValuePair("gametype", gametype));
		  }
		  nvps.add(new BasicNameValuePair("page", String.valueOf(page)));
		  nvps.add(new BasicNameValuePair("pagelimit", "500"));
		  nvps.add(new BasicNameValuePair("key", key));
		  httppost.setEntity(new UrlEncodedFormEntity(nvps));
		  HttpResponse response = hc.execute(httppost);
		  List<BbinBetdata> list = new ArrayList<BbinBetdata>();
		  BbinBetdata bean;
		  if (response.getStatusLine().getStatusCode() == 200){
			  String result=EntityUtils.toString(response.getEntity());
			  //System.out.println(result);
			  if(result==null) return null;
				Document xmlDoc =null;
				try {
					xmlDoc = Dom4jUtil.String2Doc(result);
					//System.out.println(Page+"    "+PageLimit+"        "+TotalNumber+"        "+TotalPage);
					
					Iterator it = xmlDoc.getRootElement().elementIterator("Record");
					while(it.hasNext()){
						Element e= (Element)it.next();
						if(e.element("UserName") != null){
							bean = new BbinBetdata();
							if(gamekind.equals("1")){ //体育
								bean.login_name = e.element("UserName").getStringValue();
								bean.wagersid = e.element("WagersID").getStringValue();
								bean.wagersdate = DateUtil.useastDatestringToTimestamp(e.element("WagersDate").getStringValue(),"yyyy-MM-dd HH:mm:ss");
								bean.gametype = e.element("GameType").getStringValue();
								bean.result = e.element("Result").getStringValue();
								bean.betamount = BigDecimal.valueOf(Double.parseDouble(e.element("BetAmount").getStringValue()));
								if(e.element("Payoff").getStringValue() != null && !e.element("Payoff").getStringValue().equals("")){
									bean.payoff = BigDecimal.valueOf(Double.parseDouble(e.element("Payoff").getStringValue()));
								}else{
									bean.payoff = BigDecimal.valueOf(0.0);
								}
								bean.currency = e.element("Currency").getStringValue();
								bean.commissionable = BigDecimal.valueOf(Double.parseDouble(e.element("Commissionable").getStringValue()));
								
							}else if(gamekind.equals("3")){//真人
								bean.login_name = e.element("UserName").getStringValue();
								bean.wagersid = e.element("WagersID").getStringValue();
								bean.wagersdate = DateUtil.useastDatestringToTimestamp(e.element("WagersDate").getStringValue(),"yyyy-MM-dd HH:mm:ss");
								bean.serialid = e.element("SerialID").getStringValue();
								bean.roundno = e.element("RoundNo").getStringValue();
								bean.gametype = e.element("GameType").getStringValue();
								bean.gamecode = e.element("GameCode").getStringValue();
								bean.result = e.element("Result").getStringValue();
								bean.resulttype = e.element("ResultType").getStringValue();
								bean.card = e.element("Card").getStringValue();
								bean.betamount = BigDecimal.valueOf(Double.parseDouble(e.element("BetAmount").getStringValue()));
								if(e.element("Payoff").getStringValue() != null && !e.element("Payoff").getStringValue().equals("")){
									bean.payoff = BigDecimal.valueOf(Double.parseDouble(e.element("Payoff").getStringValue()));
								}else{
									bean.payoff = BigDecimal.valueOf(0.0);
								}
								bean.currency = e.element("Currency").getStringValue();
								bean.commissionable = BigDecimal.valueOf(Double.parseDouble(e.element("Commissionable").getStringValue()));
							}else if(gamekind.equals("5")){//电子
								bean.login_name = e.element("UserName").getStringValue();
								bean.wagersid = e.element("WagersID").getStringValue();
								bean.wagersdate = DateUtil.useastDatestringToTimestamp(e.element("WagersDate").getStringValue(),"yyyy-MM-dd HH:mm:ss");
								bean.gametype = e.element("GameType").getStringValue();
								bean.result = e.element("Result").getStringValue();
								bean.betamount = BigDecimal.valueOf(Double.parseDouble(e.element("BetAmount").getStringValue()));
								if(e.element("Payoff").getStringValue() != null && !e.element("Payoff").getStringValue().equals("")){
									bean.payoff = BigDecimal.valueOf(Double.parseDouble(e.element("Payoff").getStringValue()));
								}else{
									bean.payoff = BigDecimal.valueOf(0.0);
								}
								bean.currency = e.element("Currency").getStringValue();
								bean.commissionable = BigDecimal.valueOf(Double.parseDouble(e.element("Commissionable").getStringValue()));
							}else if(gamekind.equals("12")){//彩票
								bean.login_name = e.element("UserName").getStringValue();
								bean.wagersid = e.element("WagersID").getStringValue();
								bean.wagersdate = DateUtil.useastDatestringToTimestamp(e.element("WagersDate").getStringValue(),"yyyy-MM-dd HH:mm:ss");
								bean.gametype = e.element("GameType").getStringValue();
								bean.result = e.element("Result").getStringValue();
								bean.betamount = BigDecimal.valueOf(Double.parseDouble(e.element("BetAmount").getStringValue()));
								if(e.element("Payoff").getStringValue() != null && !e.element("Payoff").getStringValue().equals("")){
									bean.payoff = BigDecimal.valueOf(Double.parseDouble(e.element("Payoff").getStringValue()));
								}else{
									bean.payoff = BigDecimal.valueOf(0.0);
								}
								
								bean.currency = e.element("Currency").getStringValue();
								bean.ispaid = e.element("IsPaid").getStringValue();
								if(e.element("Commission").getStringValue() != null && !e.element("Commission").getStringValue().equals("")){
									bean.commission = BigDecimal.valueOf(Double.parseDouble(e.element("Commission").getStringValue()));
								}else{
									bean.commission = BigDecimal.valueOf(0.0);
								}
							}else if(gamekind.equals("15")){//3D
								bean.login_name = e.element("UserName").getStringValue();
								bean.wagersid = e.element("WagersID").getStringValue();
								bean.wagersdate = DateUtil.useastDatestringToTimestamp(e.element("WagersDate").getStringValue(),"yyyy-MM-dd HH:mm:ss");
								bean.serialid = e.element("SerialID").getStringValue();
								bean.gametype = e.element("GameType").getStringValue();
								bean.result = e.element("Result").getStringValue();
								bean.betamount = BigDecimal.valueOf(Double.parseDouble(e.element("BetAmount").getStringValue()));
								if(e.element("Payoff").getStringValue() != null && !e.element("Payoff").getStringValue().equals("")){
									bean.payoff = BigDecimal.valueOf(Double.parseDouble(e.element("Payoff").getStringValue()));
								}else{
									bean.payoff = BigDecimal.valueOf(0.0);
								}
								bean.currency = e.element("Currency").getStringValue();
								bean.commissionable = BigDecimal.valueOf(Double.parseDouble(e.element("Commissionable").getStringValue()));
							}
							
							list.add(bean);
						}
						
						
						
						
						
						//System.out.println(bean);
					}
					
				return list;	
				}catch(Exception e){
					e.printStackTrace();
				}
		  }
		 
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			  hc.getConnectionManager().shutdown();
		}
		return null;
		
		
  }
  
  
  
  public static Integer getTotalPage(String username,String rounddate,String starttime,String endtime,String gamekind,String gametype){
	  
	  	HttpClient hc = new DefaultHttpClient();
		hc.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,35000);
		hc.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,35000);
		HttpConnectionParams.setConnectionTimeout(hc.getParams(), 35000);
		HttpConnectionParams.setSoTimeout(hc.getParams(), 35000);
		String url=dspurl+ "/app/WebService/XML/display.php/BetRecord";
		HttpPost httppost = new HttpPost(url);
		try{
			String kh=new StringBuilder(String.valueOf(website)).append(username)
	    	        .append(getbetKeyB).append(getUsEastTime()).toString();
	      String key = RandomUtil.getRandomChar(1) +  MD5Util.MD5(kh).toLowerCase()+RandomUtil.getRandomChar(7);
	      List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		  nvps.add(new BasicNameValuePair("website", website));
		  nvps.add(new BasicNameValuePair("username", username));
		  nvps.add(new BasicNameValuePair("uppername", "d8dabet"));
		  nvps.add(new BasicNameValuePair("rounddate", rounddate));
		  nvps.add(new BasicNameValuePair("starttime", starttime));
		  nvps.add(new BasicNameValuePair("endtime", endtime));
		  nvps.add(new BasicNameValuePair("gamekind", gamekind));
		  if(gamekind.equals("12")){
			  nvps.add(new BasicNameValuePair("gametype", gametype));
		  }
		  nvps.add(new BasicNameValuePair("pagelimit", "500"));
		  nvps.add(new BasicNameValuePair("key", key));
		  httppost.setEntity(new UrlEncodedFormEntity(nvps));
		  HttpResponse response = hc.execute(httppost);
		  List<BbinBetdata> list = new ArrayList<BbinBetdata>();
		  BbinBetdata bean;
		  String TotalPage;
		  if (response.getStatusLine().getStatusCode() == 200){
			  String result=EntityUtils.toString(response.getEntity());
			  //System.out.println(result);
			  if(result==null) return null;
				Document xmlDoc =null;
				try {
					xmlDoc = Dom4jUtil.String2Doc(result);
					TotalPage=xmlDoc.getRootElement().attributeValue("TotalPage");	
					if(TotalPage == null){
						return 0;
					}
				return Integer.parseInt(TotalPage);	
				}catch(Exception e){
					e.printStackTrace();
				}
		  }
		 
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			  hc.getConnectionManager().shutdown();
		}
		return null;
		
		
}
  
	 
private static String getUsEastTime() {
	long time = new Date().getTime() - 43200000L;
	Calendar calendar = Calendar.getInstance();
	calendar.setTimeInMillis(time);
	return DateUtil.fmtyyyyMMdd(calendar.getTime());
}
	
	public static void main(String[] args){
		 //System.out.println(BbinApi.CheckOrCreateGameAccount("daw282767761", "b123b123"));
		//BbinApi.getTotalPage("","2015-05-11","4:0:00","4:15:00","5");
		BbinApi.getBetRecords("","2015-05-25","02:25:00","23:59:59",1,"12","1");
		//System.out.println(BbinApi.Transfer("dawlance001", "1"+new Date().getTime(), "IN", new BigDecimal(300)));
		//System.out.println(ForwardGame("dawlance008","a123a123","3DHall"));
	}

}
