package com.ws.service;

import java.math.BigDecimal;

import bsz.exch.service.Result;
import bsz.exch.service.Task;

public class YbpService {

	
	public static Result pay(String login_name,BigDecimal credit,String remark,String return_url,String bank,String ip,Integer type){
		BigDecimal cr=credit.setScale(2, BigDecimal.ROUND_HALF_UP);
		Task task =Task.create("ybp_pay_8d");
		task.addParam("product", task.getProduct());
		task.addParam("login_name",login_name);
		task.addParam("amount",cr.toString());
		task.addParam("return_url",return_url);
		task.addParam("remark",remark);
		task.addParam("ip",ip);
		task.addParam("bank",bank);
		task.addParam("type",type);
		Result result=task.perform();	
		if(result.success()){
			return result;
		}
		return null;
	}
	
	public static Result queryYbOrder(String order_id){
		Task task =Task.create("ybp_query_order_by_order_id_8d");
		task.addParam("product", task.getProduct());
		task.addParam("pay_id",order_id);
		Result result=task.perform();	
		if(result.success()){
			return result;
		}
		return null;
	}
	
	public static Result yingbaopayNotify(String orderid,BigDecimal payamount,String opstate,String orderno,String eypaltime,String message,String paytype,String remark,String sign){
		Task task =Task.create("ybp_notify_8d");
		task.addParam("product", task.getProduct());
		task.addParam("orderid",orderid);
		task.addParam("payamount",payamount);
		task.addParam("opstate",opstate);
		task.addParam("orderno",orderno);
		task.addParam("eypaltime",eypaltime);
		task.addParam("message",message);
		task.addParam("paytype",paytype);
		task.addParam("remark",remark);
		task.addParam("sign",sign);
		Result result=task.perform();	
		if(result.success()){
			return result;
		}
		return null;
	}
	
	public static void main(String[] args){
		//pay("lance008",new BigDecimal(100.00),"pd","");
		
	}
	
}
