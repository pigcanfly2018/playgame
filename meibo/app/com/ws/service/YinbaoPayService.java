package com.ws.service;

import java.math.BigDecimal;

import bsz.exch.service.Result;
import bsz.exch.service.Task;

public class YinbaoPayService {

	
	public static Result pay(String login_name,BigDecimal credit,String remark,String return_url,String bank,String ip){
		Task task =Task.create("yinbaop_pay_8d");
		task.addParam("product", task.getProduct());
		task.addParam("login_name",login_name);
		task.addParam("amount",credit.intValue());
		task.addParam("return_url",return_url);
		task.addParam("remark",remark);
		task.addParam("ip",ip);
		task.addParam("bank",bank);
		Result result=task.perform();	
		if(result.success()){
			return result;
		}
		return null;
	}
	
	
	public static Result queryYbOrder(String order_id){
		Task task =Task.create("yinbaop_query_order_by_order_id_8d");
		task.addParam("product", task.getProduct());
		task.addParam("pay_id",order_id);
		Result result=task.perform();	
		if(result.success()){
			return result;
		}
		return null;
	}
	
	
	public static Result yinbaopayNotify(String ordernumber,BigDecimal paymoney,String orderstatus,String sysnumber,String partner,String attach,String sign){
		Task task =Task.create("yinbaop_notify_8d");
		task.addParam("product", task.getProduct());
		task.addParam("ordernumber",ordernumber);
		task.addParam("paymoney",paymoney);
		task.addParam("orderstatus",orderstatus);
		task.addParam("sysnumber",sysnumber);
		task.addParam("partner",partner);
		task.addParam("attach",attach);
		task.addParam("sign",sign);
		Result result=task.perform();	
		if(result.success()){
			return result;
		}
		return null;
	}
	
	
	
	
}
