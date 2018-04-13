package com.ws.service;

import java.math.BigDecimal;

import bsz.exch.service.Result;
import bsz.exch.service.Task;

public class ThpService {

	public static Result pay(String login_name,BigDecimal credit,String remark,String return_url,String bank,String ip){
		BigDecimal cr=credit.setScale(2, BigDecimal.ROUND_HALF_UP);
		Task task =Task.create("thp_pay_8d");
		task.addParam("product", task.getProduct());
		task.addParam("login_name",login_name);
		task.addParam("amount",cr.toString());
		task.addParam("return_url",return_url);
		task.addParam("bank",bank);
		task.addParam("customer_ip",ip);
		task.addParam("remark",remark);
		Result result=task.perform();	
		if(result.success()){
			return result;
		}
		return null;
	}
	
	public static Result weixinpay(String login_name,BigDecimal credit,String return_url,String payType,String customer_ip){
		Task task =Task.create("thweixinp_pay_8d");
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
	
	public static Result tonghuipayNotify(String trade_no,String return_params,BigDecimal order_amount,String order_no,String notify_type,String sign,String order_time,String trade_time,String trade_status){
		Task task =Task.create("thp_notify_8d");
		task.addParam("product", task.getProduct());
		task.addParam("trade_no",trade_no);
		
		task.addParam("return_params",return_params);
		task.addParam("order_amount",order_amount);
		task.addParam("order_no",order_no);
		
		task.addParam("notify_type",notify_type);
		task.addParam("sign",sign);
		task.addParam("order_time",order_time);
		task.addParam("trade_time",trade_time);
		task.addParam("trade_status",trade_status);
		
		
		Result result=task.perform();	
		if(result.success()){
			return result;
		}
		return null;
	}
	
	public static Result tonghuiweixinpayNotify(String trade_no,String return_params,BigDecimal order_amount,String order_no,String notify_type,String sign,String order_time,String trade_time,String trade_status){
		Task task =Task.create("thweixinp_notify_8d");
		task.addParam("product", task.getProduct());
		task.addParam("trade_no",trade_no);
		
		task.addParam("return_params",return_params);
		task.addParam("order_amount",order_amount);
		task.addParam("order_no",order_no);
		
		task.addParam("notify_type",notify_type);
		task.addParam("sign",sign);
		task.addParam("order_time",order_time);
		task.addParam("trade_time",trade_time);
		task.addParam("trade_status",trade_status);
		
		
		Result result=task.perform();	
		if(result.success()){
			return result;
		}
		return null;
	}
	
	public static Result queryThOrder(String order_id){
		Task task =Task.create("thp_query_order_by_order_id_8d");
		task.addParam("product", task.getProduct());
		task.addParam("pay_id",order_id);
		Result result=task.perform();	
		if(result.success()){
			return result;
		}
		return null;
	}
	
}
