package com.ws.service;

import java.math.BigDecimal;

import bsz.exch.service.Result;
import bsz.exch.service.Task;

public class HuiTongPayService {

	public static Result pay(String login_name,BigDecimal credit,String return_url,String bank,String ip){
		
		Task task =Task.create("huitong_pay_8d");
		task.addParam("product", task.getProduct());
		task.addParam("login_name",login_name);
		task.addParam("amount",credit.intValue());
		task.addParam("return_url",return_url);
		task.addParam("bank",bank);
		task.addParam("ip",ip);
		Result result=task.perform();	
		if(result.success()){
			return result;
		}
		return null;
	}
	
	public static String notifypay(String sign, String merchant_code, String order_no, String order_amount,
			String order_time, String trade_no, String trade_time, String trade_status) {
		// TODO Auto-generated method stub
		Task task = Task.create("huitong_notify_8d");
		task.addParam("merchant_code", merchant_code);
		task.addParam("sign", sign);
		task.addParam("order_no", order_no);
		task.addParam("order_time", order_time);
		
		task.addParam("order_amount", order_amount);
		task.addParam("trade_no", trade_no);
		task.addParam("trade_time", trade_time);
		task.addParam("trade_status", trade_status);
		task.addParam("product", task.getProduct());
		Result result = task.perform();
	
		String res = "";
		if(result.success()){
			 res = result.get("ok");
		}
		return res;
	}
	
}
