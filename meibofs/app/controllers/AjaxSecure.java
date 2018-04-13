package controllers;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import models.User;

import play.Play;
import play.cache.Cache;
import play.data.validation.Required;
import play.libs.Codec;
import play.libs.Crypto;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.Http;
import play.utils.Java;
import util.Check;
import util.Constant;
import util.JSONResult;
import util.MD5;

public class AjaxSecure extends Controller {
	static String allow_ip=Play.configuration.getProperty("allow_ip");
	
	protected static boolean isAllow(){
		String ip=IpApp.getIpAddr();
		if(!allow_ip.contains(ip)){
			//return false;
		}
		return true;
	}
	@Before(unless = {"login", "authenticate", "logout" })
	static void checkAccess() throws Throwable {
		if(!isAllow()){
			renderText("无资源可访问！");
		}
		if (!session.contains(Constant.userName)) {
			Http.Header h =request.current().headers.get("x-requested-with");
			String ajax =(h==null)?null:h.value();
			if (ajax != null&& ajax.equalsIgnoreCase("XMLHttpRequest")) {
				Http.Header header =new Http.Header();
				header.name="sessionstatus";
				header.values.add("timeout");
				response.current().headers.put("sessionstatus", header);
				renderJSON(JSONResult.success("没有凭证，无法进入!"));
			}else{
				redirect("/login");
			}
		}
		Check check = getActionAnnotation(Check.class);
		if (check != null) {
			check(check);
		}
		check = getControllerInheritedAnnotation(Check.class);
		if (check != null) {
			check(check);
		}
	}
	
	
	
	private static void check(Check check) throws Throwable {
		// 开始验证
		if (check.code() != null) {

		}
	}
	/**
	 * 登录 判断cookie,如果cookie有则可以进入系统,如果没有则转到登录页面
	 * 
	 * @throws Throwable
	 */
	public static void login() throws Throwable {
		if(!isAllow()){
			renderText("无资源可访问！");
		}
		render();
	}

	/**
	 * 验证用户名密码
	 * 
	 * @param username
	 * @param password
	 * @param remember
	 * @throws Throwable
	 */
	public static void authenticate(@Required String userName, String password,
			@Required String veriCode) throws Throwable {
		if(!isAllow()){
			renderText("无资源可访问！");
		}
		// Check tokens
		User user =new User();
		user.loginname=userName;
		user.password=password;
		user.lastloginip=request.remoteAddress;
		boolean flag =user.NTlogin();
		if(!flag){
			renderJSON(JSONResult.failure("用户名或密码错误,或者帐号已经被禁用!"));
		}
		if (Cache.get("veriCode" + session.getId()) == null) {
			renderJSON(JSONResult.failure("验证码失效!"));
		}
		if (!validation.equals(veriCode.toLowerCase(),
				Cache.get("veriCode" + session.getId()).toString().toLowerCase()).ok) {
			renderJSON(JSONResult.failure("验证码出错!"));
		}
		session.put(Constant.userName, userName);
		
		renderJSON(JSONResult.success("登录成功!"));
	}

	public static void logout() throws Throwable {
		session.clear();
		flash.success("secure.logout");
		login();
	}
	
	public static class Security extends Controller {

		static boolean check(String profile) {

			return true;
		}
		static void onAuthenticated() {
			
		}
		
		static void onCheckFailed(String profile) {
			//根据请求头返回相应的页面
			if(request.isAjax()){
				renderJSON(JSONResult.forbidden("无权访问!"));
			}else{
				renderText(JSONResult.forbidden("无权访问!"));
			}
			forbidden();
		}

		private static Object invoke(String m, Object... args) throws Throwable {
			Class security = null;
			List<Class> classes = Play.classloader.getAssignableClasses(Security.class);
			if (classes.size() == 0) {
				security = Security.class;
			} else {
				security = classes.get(0);
			}
			try {
				return Java.invokeStaticOrParent(security, m, args);
			} catch (InvocationTargetException e) {
				throw e.getTargetException();
			}
		}

	}

}
