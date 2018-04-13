package com.ws.service;

import java.math.BigDecimal;
import java.util.Random;

import bsz.exch.service.Result;
import bsz.exch.service.Task;

public class ChangFuPayService {

	public static Result pay(String login_name,BigDecimal credit,String return_url,String payType,String customer_ip){
		Task task =Task.create("cfp_pay_8d");
		task.addParam("product", task.getProduct());
		task.addParam("login_name",login_name);
		task.addParam("amount",credit.intValue());
		task.addParam("return_url",return_url);
		task.addParam("customer_ip",customer_ip);
		task.addParam("payType",payType);
		Result result=task.perform();	
		if(result.success()){
			return result;
		}
		return null;
	}
	
	public static Result changfupayNotify(String userid ,String orderid ,String returncode,String sign2,String sign,BigDecimal money,String ext){
		Task task =Task.create("cfp_notify_8d");
		task.addParam("product", task.getProduct());
		task.addParam("userid",userid);
		task.addParam("orderid",orderid);
		task.addParam("returncode",returncode);
		task.addParam("sign2",sign2);
		task.addParam("sign",sign);
		task.addParam("money",money);
		task.addParam("ext",ext);
		Result result=task.perform();	
		if(result.success()){
			return result;
		}
		return null;
	}
	
	
}
