package com.ws.service;

import java.math.BigDecimal;

import bsz.exch.service.Result;
import bsz.exch.service.Task;

public class XunhuibaoPayService {

	public static Result pay(String login_name,BigDecimal credit,String return_url,String payType,String customer_ip){
		Task task =Task.create("xhbp_pay_8d");
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
	
	
	public static Result getDetail(String login_name, String pay_id) {
		// TODO Auto-generated method stub
		Task task = Task.create("xhbp_query_8d");
		task.addParam("login_name", login_name);
		task.addParam("pay_id", pay_id);
		task.addParam("product", task.getProduct());
		task.setActivePage(false);
		Result result = task.perform();
		if(result.success()){
			return result;
		}
		return null;
	}
	
	public static Result xunhuibaopayNotify(String transDate,BigDecimal amount,String transTime,String merchno,String merchName,String signature,String traceno,String payType,String orderno,String channelOrderno,String channelTraceno,String status){
		Task task =Task.create("xhbp_notify_8d");
		task.addParam("product", task.getProduct());
		task.addParam("transDate",transDate);
		task.addParam("amount",amount);
		task.addParam("transTime",transTime);
		task.addParam("merchno",merchno);
		task.addParam("merchName",merchName);
		task.addParam("signature",signature);
		task.addParam("traceno",traceno);
		task.addParam("payType",payType);
		task.addParam("orderno",orderno);
		task.addParam("channelOrderno",channelOrderno);
		task.addParam("channelTraceno",channelTraceno);
		task.addParam("status",status);
		Result result=task.perform();	
		if(result.success()){
			return result;
		}
		return null;
	}
	
	
}
