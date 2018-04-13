package com.ws.service;

import java.math.BigDecimal;

import bsz.exch.service.Result;
import bsz.exch.service.Task;

public class DingYiPayService {

	public static Result pay(String login_name,BigDecimal credit,String return_url,String payType,String customer_ip){
		Task task =Task.create("dyp_pay_8d");
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

	public static Result dingyipayNotify(String orderid, String opstate, String sysorderid, String systime,
			String attach, String msg, BigDecimal ovalue, String sign) {
		// TODO Auto-generated method stub
		Task task =Task.create("dyp_notify_8d");
		task.addParam("product", task.getProduct());
		task.addParam("orderid",orderid);
		task.addParam("opstate",opstate);
		task.addParam("sysorderid",sysorderid);
		task.addParam("systime",systime);
		task.addParam("attach",attach);
		task.addParam("msg",msg);
		task.addParam("ovalue",ovalue);
		task.addParam("sign",sign);
		Result result=task.perform();	
		if(result.success()){
			return result;
		}
		return null;
	}
	
}
