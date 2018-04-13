package com.ws.service;

import java.math.BigDecimal;

import bsz.exch.service.Result;
import bsz.exch.service.Task;

public class SanVPayService {

	public static Result pay(String login_name,BigDecimal credit,String return_url,String bank,String ip){
		Task task =Task.create("sanv_pay_8d");
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
	
	
	public static Result sanvpayNotify(String return_code ,BigDecimal totalFee,String channelOrderId ,String orderId,String timeStamp,String sign,String attach,String transactionId ){
		Task task =Task.create("sanv_notify_8d");
		task.addParam("product", task.getProduct());
		task.addParam("return_code",return_code);
		task.addParam("totalFee",totalFee);
		task.addParam("channelOrderId",channelOrderId);
		task.addParam("orderId",orderId);
		task.addParam("timeStamp",timeStamp);
		task.addParam("sign",sign);
		
		task.addParam("attach",attach);
		task.addParam("transactionId",transactionId);
		Result result=task.perform();	
		if(result.success()){
			return result;
		}
		return null;
	}
	
}
