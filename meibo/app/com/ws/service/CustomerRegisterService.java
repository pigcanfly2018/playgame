package com.ws.service;

import bsz.exch.service.Result;
import bsz.exch.service.Task;

public class CustomerRegisterService {

	public static Result register(String phone, String real_name, String qq, String ip,String rg, String reg_domain, String referdomain, String keyword) {
		// TODO Auto-generated method stub
		Task task =Task.create("cust_reg2_8d");		
		task.addParam("phone", phone);
		task.addParam("real_name", real_name);
		task.addParam("reg_ip", ip);
		task.addParam("qq", qq);
		task.addParam("reg_code", rg);
		task.addParam("reg_domain",reg_domain);
		task.addParam("referdomain",referdomain);
		task.addParam("keyword",keyword);
		Result result=task.perform();
		if(result.success()){
			return result;
		}
		return null;
	}
	
	public static Result login(String token, String login_name, String ip, String user_agent) {
		// TODO Auto-generated method stub
		Task task =Task.create("cust_login3_8d");	
		task.addParam("token", token);
		task.addParam("login_name", login_name);
		task.addParam("login_ip", ip);
		task.addParam("user_agent", user_agent);
		
		Result result=task.perform();
		if(result.success()){
			return result;
		}
		return null;
	}
	
}
