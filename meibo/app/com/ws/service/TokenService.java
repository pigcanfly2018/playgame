package com.ws.service;

import bsz.exch.service.Result;
import bsz.exch.service.Task;

public class TokenService {

	//判断是否登录
	public static  Boolean validate (String token,String login_name){
		Task task =Task.create("token_validate_8d");	
		task.addParam("token", token);
		task.addParam("login_name", login_name);
		Result result=task.perform();
		if(result.success()){
			if(result.getIntOfRes()==1)return true;
		}
		return false;
	}
	
	//获取一个TOken
	public static String get (String login_name){
		Task task =Task.create("token_get_8d");	
		task.addParam("login_name", login_name);
		Result result=task.perform();
		if(result.success()){
			return result.getRes();
		}
		return null;
	}
	
	
	
	
	
}
