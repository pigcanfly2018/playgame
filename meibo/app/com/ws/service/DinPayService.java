package com.ws.service;

import java.math.BigDecimal;

import bsz.exch.service.Result;
import bsz.exch.service.Task;

public class DinPayService {

	public static Result pay(String interface_version,String merchant_code,String notify_url,String order_amount,String order_no,String order_time,String product_name){
		
		Task task =Task.create("dinp_pay_8d");
		task.addParam("product", task.getProduct());
		task.addParam("interface_version",interface_version);
		task.addParam("merchant_code",merchant_code);
		task.addParam("notify_url",notify_url);
		task.addParam("order_amount",order_amount);
		task.addParam("order_time",order_time);
		task.addParam("order_no",order_no);
		task.addParam("product_name",product_name);
		Result result=task.perform();	
		if(result.success()){
			return result;
		}
		return null;
	}
	
	public static Result notice(String bank_seq_no,String extra_return_param,String merchant_code,String notify_id,String notify_type,String order_amount,String order_no,String order_time,String trade_no,String trade_status,String trade_time,String sign){
		
		Task task =Task.create("dinp_notice_8d");
		task.addParam("product", task.getProduct());
		task.addParam("bank_seq_no",bank_seq_no);
		task.addParam("extra_return_param",extra_return_param);
		task.addParam("merchant_code",merchant_code);
		task.addParam("notify_id",notify_id);
		task.addParam("notify_type",notify_type);
		task.addParam("order_amount",order_amount);
		task.addParam("order_no",order_no);
		task.addParam("order_time",order_time);
		task.addParam("trade_no",trade_no);
		task.addParam("trade_status",trade_status);
		task.addParam("trade_time",trade_time);
		task.addParam("sign",sign);
		Result result=task.perform();	
		if(result.success()){
			return result;
		}
		return null;
	}
	
}
