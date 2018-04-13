package controllers;


import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import play.cache.Cache;
import play.mvc.Controller;
import play.mvc.Http.Header;
import util.IPSeeker;

public class IPApp extends Controller{
	 private static String allowAddress="本机|局域网|中国|北京|天津|重庆|上海|河北|山西|辽宁|吉林|黑龙江|江苏|浙江|安徽|福建|江西|山东|河南|湖北|湖南|广东|海南|四川|贵州|云南|陕西|甘肃|青海|内蒙古|广西|宁夏|新疆|西藏|美国";
	 private static String allow_ip=play.Play.configuration.getProperty("allow_ip");
	 private static String noallow_ip=play.Play.configuration.getProperty("notallow_ip");
	 
	 
	 protected static boolean isxss(){
		 Map<String,String> map= params.allSimple();
		 for(String o : map.keySet()){  
			   String s= map.get(o);  
			   if(s!=null){
		            if(s.contains("<")||s.contains(">")||s.contains("'")||s.contains("\"")||s.contains(";")){
		                return true;
		            }
	            }
		 }
		 return false;
	 } 
	 
	 public static String getIpAddr(){
		 Header forwardFor= request.headers.get("x-forwarded-for");
	     String ip = forwardFor==null?null:forwardFor.value();
	    if ((ip != null) && (StringUtils.isNotBlank(ip.trim()))) {
	      String[] remoteIps = ip.split(",");
	      for (String tempIp : remoteIps) {
	        if (isValidIp(tempIp)) {
	          return tempIp;
	        }
	      }
	    }
	    if (!isValidIp(ip)) {
	   	  Header proxyIp= request.headers.get("proxy-client-ip");
	      ip = proxyIp==null?null:proxyIp.value();
	    }
	    if (!isValidIp(ip)) {
	      ip = request.remoteAddress;
	    }
	    return ip;
	  }
	  protected static String getIpAddrByClient() {
	    Header clientIp= request.headers.get("client_ip");
	    String ip=clientIp==null?null:clientIp.value();
	    if ((ip != null) && (StringUtils.isNotBlank(ip.trim()))) {
	      ip = getIpAddr();
	    }
	    return ip;
	  }
	  protected static boolean isValidIp(String ip) {
	    String UNKNOWN_IP = "unknown";
	    return (ip != null) && ((StringUtils.isNotBlank(ip.trim())) || ("unknown".equalsIgnoreCase(ip)));
	  }
	  
	  public static void ip(){
	       IPSeeker seeker = IPSeeker.getInstance();
		   String ip=getIpAddr();
		   String address=seeker.getAddress(ip==null?"":ip);
		  renderText(ip+","+address);
	  }
	  public static void addIp(String ip){
		  
		  if(ip==null){
			  renderText(allow_ip);
		  }
		  if(!allow_ip.contains(ip)){
		      allow_ip=allow_ip+","+ip;
		     // play.Play.configuration.setProperty("allow_ip", allow_ip);
		  }
		  renderText(ip);
	  }
      public static void addNoIp(String ip){
		  if(ip==null){
			  renderText(noallow_ip);
		  }
		  if(!noallow_ip.contains(ip)){
			  noallow_ip=noallow_ip+","+ip;
		  }
		  renderText(ip);
	  }
	  
	  /**
		 直辖市：北京市，天津市，重庆市，上海市。
		普通行政省：河北省山西省辽宁省吉林省黑龙江省江苏省浙江省安徽省福建省江西省山东省河南省湖北省湖南省广东省海南省四川省贵州省云南省陕西省甘肃省青海省台湾省
		中国地图
		中国地图
		民族自治区：内蒙古自治区，广西壮族自治区，宁夏回族自治区，新疆维吾尔自治区，西藏自治区。
		特别行政区：香港特别行政区，澳门特别行政区。
	   * @return
	   */
	  protected static boolean fitter(){
			IPSeeker seeker = IPSeeker.getInstance();
			String ip=IPApp.getIpAddr();
			if(noallow_ip.contains(ip)){
				return true;
			}
			
			if(allow_ip.contains(ip)){
				return false;
			}
			
			String address=seeker.getAddress(ip==null?"":ip);
			if(address.length()>=2){
				//菲律宾所有IP可以访问
				if(allowAddress.contains(address)){
			    	 return false;
			     }
				
				
			     String s=address.substring(0, 2);
			     if(allowAddress.contains(s)){
			    	 return false;
			     }
			}
			
			return true;
	  }
	  
	  public static void clearCache(){
		  Cache.clear();
	  }
	  
	  

}
