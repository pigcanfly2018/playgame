package com.ws.service;

import java.math.BigDecimal;

import bsz.exch.service.Result;
import bsz.exch.service.Task;

public class LypService {

	public static Result pay(String login_name,BigDecimal credit,String remark,String return_url,String bank,String ip,Integer type){
		Task task =Task.create("lyp_pay_8d");
		task.addParam("product", task.getProduct());
		task.addParam("login_name",login_name);
		task.addParam("amount",credit.intValue());
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
	
	public static Result queryLyOrder(String order_id){
		Task task =Task.create("lyp_query_order_by_order_id_8d");
		task.addParam("product", task.getProduct());
		task.addParam("pay_id",order_id);
		Result result=task.perform();	
		if(result.success()){
			return result;
		}
		return null;
	}
	
	public static Result leyingpayNotify(String orderID,String orderAmount,String payAmount,String acquiringTime,String completeTime,String orderNo,
			String partnerID,String remark,String charset,String signType,String signMsg,String resultCode,String stateCode){
		Task task =Task.create("lyp_notify_8d");
		task.addParam("product", task.getProduct());
		task.addParam("orderID",orderID);
		task.addParam("orderAmount",orderAmount);
		task.addParam("payAmount",payAmount);
		task.addParam("acquiringTime",acquiringTime);
		task.addParam("completeTime",completeTime);
		task.addParam("orderNo",orderNo);
		task.addParam("partnerID",partnerID);
		task.addParam("remark",remark);
		task.addParam("charset",charset);
		task.addParam("signType",signType);
		task.addParam("signMsg",signMsg);
		task.addParam("resultCode",resultCode);
		task.addParam("stateCode",stateCode);
		Result result=task.perform();	
		if(result.success()){
			return result;
		}
		return null;
	}
	
}
