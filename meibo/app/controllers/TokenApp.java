package controllers;

import com.ws.service.TokenService;

import bsz.exch.service.Result;
import play.mvc.Controller;

public class TokenApp extends Controller{
	
	
	//PT 单纯跳转 不涉及登录
	public static void forward(String token,String login_name,String mode,String url,String ptcode){
		
		String domain = "http://"+request.host;
		String redirecturl = domain;
		if(url==null || "".equals(url)){
			 redirecturl = domain;
		}
		//验证登录
		if(token!=null){
			Boolean s=TokenService.validate(token, login_name);
			if(s){
				if(!session.contains("username")){
					 session.put("username", login_name);
				}
			}
		}
		String uri=redirecturl+url;
		if(ptcode!=null && !"".equals(ptcode)){
			 uri=uri+"&ptcode="+ptcode;
		}
		TokenApp.redirect(uri);
	}
	
	

}
