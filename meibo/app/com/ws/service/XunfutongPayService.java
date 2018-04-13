package com.ws.service;

import java.math.BigDecimal;

import bsz.exch.service.Result;
import bsz.exch.service.Task;

public class XunfutongPayService {

	public static Result pay(String login_name,BigDecimal credit,String return_url,String payType,String customer_ip){
		Task task =Task.create("xftp_pay_8d");
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
	
	public static Result xunfutongpayNotify(String data){
		Task task =Task.create("xftp_notify_8d");
		task.addParam("product", task.getProduct());
		task.addParam("data",data);
		Result result=task.perform();	
		if(result.success()){
			return result;
		}
		return null;
	}
	
	
}
