package controllers;

import org.apache.commons.lang.StringUtils;

import play.mvc.Controller;
import play.mvc.Http.Header;

public class IpApp extends Controller{
	
	 protected static boolean isValidIp(String ip) {
		    String UNKNOWN_IP = "unknown";
		    return (ip != null) && ((StringUtils.isNotBlank(ip.trim())) || ("unknown".equalsIgnoreCase(ip)));
		}
	    
	    protected static String getIpAddr(){
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
	    

}
