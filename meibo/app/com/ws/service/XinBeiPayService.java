package com.ws.service;

import java.math.BigDecimal;

import bsz.exch.service.Result;
import bsz.exch.service.Task;

public class XinBeiPayService {

	public static Result pay(String login_name,BigDecimal credit,String return_url,String bank,String ip){
		Task task =Task.create("xinbei_pay_8d");
		task.addParam("product", task.getProduct());
		task.addParam("login_name",login_name);
		task.addParam("amount",credit.intValue());
		task.addParam("return_url",return_url);
		task.addParam("ip",ip);
		task.addParam("bank",bank);
		Result result=task.perform();	
		if(result.success()){
			return result;
		}
		return null;
	}
	
	public static Result xinbeipayNotify(String MerchantCode ,String OrderId ,String OrderDate,String TradeIp,String SerialNo,BigDecimal Amount,String PayCode,String State,String FinishTime,String SignValue ){
		Task task =Task.create("xinbei_notify_8d");
		task.addParam("product", task.getProduct());
		task.addParam("MerchantCode",MerchantCode);
		task.addParam("OrderId",OrderId);
		task.addParam("OrderDate",OrderDate);
		task.addParam("TradeIp",TradeIp);
		task.addParam("SerialNo",SerialNo);
		task.addParam("Amount",Amount);
		task.addParam("PayCode",PayCode);
		task.addParam("State",State);
		task.addParam("FinishTime",FinishTime);
		task.addParam("SignValue",SignValue);
		Result result=task.perform();	
		if(result.success()){
			return result;
		}
		return null;
	}
	
	public static Result queryXinBeiOrder(String order_id){
		Task task =Task.create("xinbei_query_order_by_order_id_8d");
		task.addParam("product", task.getProduct());
		task.addParam("pay_id",order_id);
		Result result=task.perform();	
		if(result.success()){
			return result;
		}
		return null;
	}
	
}
