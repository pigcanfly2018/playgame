package com.ws.service;

import java.math.BigDecimal;

import bsz.exch.service.Result;
import bsz.exch.service.Task;

public class JinYangPayService {
	
	public static Result pay(String login_name,BigDecimal credit,String return_url,String payType,String customer_ip){
		Task task =Task.create("jyp_pay_8d");
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
	
	public static Result notify(String partner,BigDecimal paymoney,String ordernumber,String orderstatus,String sysnumber,String attach,String sign){
		Task task =Task.create("jyp_notify_8d");
		task.addParam("product", task.getProduct());
		task.addParam("partner",partner);
		task.addParam("paymoney",paymoney);
		task.addParam("ordernumber",ordernumber);
		task.addParam("orderstatus",orderstatus);
		task.addParam("sysnumber",sysnumber);
		task.addParam("attach",attach);
		task.addParam("sign",sign);
		Result result=task.perform();	
		if(result.success()){
			return result;
		}
		return null;
	}

}
