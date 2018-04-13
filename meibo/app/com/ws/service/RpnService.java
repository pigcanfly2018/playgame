package com.ws.service;

import java.math.BigDecimal;

import bsz.exch.service.Result;
import bsz.exch.service.Task;

public class RpnService {

	public static Result pay(String login_name,Integer credit,String remark,String return_url,String bank_id,String ip){
		
		Task task =Task.create("rpn_pay_8d");
		task.addParam("product", task.getProduct());
		task.addParam("login_name",login_name);
		task.addParam("amount",credit);
		task.addParam("return_url",return_url);
		task.addParam("remark",remark);
		task.addParam("ip",ip);
		task.addParam("bank_id",bank_id);
		Result result=task.perform();	
		if(result.success()){
			return result;
		}
		return null;
	}
	
	public static Result queryRpnOrder(String order_id){
		Task task =Task.create("rpn_query_order_by_order_id_8d");
		task.addParam("product", task.getProduct());
		task.addParam("order_id",order_id);
		Result result=task.perform();	
		if(result.success()){
			return result;
		}
		return null;
	}
	
	public static Result rpnpayNotify(String order_id,String order_time,String order_amount,String deal_id,String deal_time,String pay_amount,String pay_result,String signature){
		Task task =Task.create("rpn_notify_8d");
		task.addParam("product", task.getProduct());
		task.addParam("order_id",order_id);
		
		task.addParam("order_time",order_time);
		task.addParam("order_amount",order_amount);
		task.addParam("deal_id",deal_id);
		
		task.addParam("deal_time",deal_time);
		task.addParam("pay_amount",pay_amount);
		task.addParam("pay_result",pay_result);
		task.addParam("signature",signature);
		
		Result result=task.perform();	
		if(result.success()){
			return result;
		}
		return null;
	}
	
}
