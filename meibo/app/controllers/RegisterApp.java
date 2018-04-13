package controllers;

import java.io.UnsupportedEncodingException;

import org.apache.log4j.Logger;

import com.ws.service.CustomerRegisterService;

import bsz.exch.service.Result;
import play.mvc.Before;
import play.mvc.Controller;

public class RegisterApp extends Controller{

	private static Logger logger=Logger.getLogger(RegisterApp.class);
	
	@Before
	 public static void before(){
		if(IPApp.isxss()){
			renderText("您的输入存在非法字符!");
		}
	}
	
	//1成功  2电话不正确  3电话已经注册
	public static void index(String phone,String real_name,String qq,String callback,String rg,
			String referrer,String wd,String q,String query,String domain) {
		//String callback = context.Request.QueryString["callback"];
		String ip=IPApp.getIpAddr();
		String json = null;
		String res = null;
		if(rg==null)rg="000000";
		
		String reg_domain =domain;
		String referdomain =null;
		String keyword = "无法获取";
		
		/*if(wd!=null){
			
			referdomain = "www.baidu.com";
			keyword =wd;//百度
		}else if(q!=null){
			referdomain = "www.so.com";
			keyword = q;//360
		}else if(query!=null){
			referdomain = "www.sogou.com";
			keyword = query;//搜狗
		}else{*/
			referdomain = referrer ;
		//}
		try {
			 keyword = java.net.URLDecoder.decode(keyword,   "utf-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		Result result = CustomerRegisterService.register(phone,real_name,qq,ip, rg,reg_domain,referdomain,keyword);
		if(result!=null){
			String ok =result.get("ok");
			if("1".equals(ok)){
				json =  "{\"result\":\""+1+"\" , \"message\":\""+result.get("login_name")+";"+result.get("token")+";"+result.get("login_pwd")+"\"}";

			}
			if("2".equals(ok)){
				json =  "{\"result\":\""+2+"\" , \"message\":\"phone error\"}";

			}
			if("3".equals(ok)){
				json =  "{\"result\":\""+3+"\" , \"message\":\"phone error\"}";

			}
			if("4".equals(ok)){
				json =  "{\"result\":\""+4+"\" , \"message\":\"too much try times\"}";
			}
			renderJSON( callback + "(" + json + ")");
		}else{
			json =  "{\"result\":\"0\" , \"message\":\"\"}";
			res = callback + "(" + json + ")";
			renderJSON(res);
		}
    }
	
	public static void login(String token,String login_name,String url){
		  
		  String domain = (request.secure?"https":"http")+"://"+request.host;
		  if(domain.contains("238da") || domain.contains("668da") || domain.contains("258da") || domain.contains("268da")|| domain.contains("278da")|| domain.contains("298da")||
	    			domain.contains("8da188") || domain.contains("8da88") || domain.contains("8da2016") || domain.contains("838da")
	    			 || domain.contains("8da2017")){
	    		if(domain.contains("https")){
	    			
	    		}else{
	    			domain = domain.replaceAll("http", "https");
	    		}
	    		
	    	}
		  String redirecturl = domain;
		  String user_agent=request.headers.get("user-agent")==null?"":request.headers.get("user-agent").toString();
		  String ip=IPApp.getIpAddr();
		  if(url==null || "".equals(url)){
		    redirecturl = domain;
		  }
		  
		  System.out.println("login快速登录方法session获取  = "+session.get("username")+"sessionid = "+session.getId());
		  
		  //验证登录
		  Result rest = CustomerRegisterService.login(token,login_name,ip,user_agent);
		  if(rest!=null){
		   if(rest.getIntOfRes()==1){
		    //session.clear();
			   
		    logger.info("login快速登录方法session获取  = "+session.get("username")+"sessionid = "+session.getId());
		    if(session.contains("username")){
		     session.remove("username");
		    }
		     session.put("username", login_name);
		    if(url==null||"".equals(url)){
		     redirecturl=domain+"/user.php";
		    }else{
		        redirecturl =domain+url;
		    }
		     render(redirecturl);
		   }
		  }
		  redirect(redirecturl);
		 }
	
	
}
