package com.ws.service;

import java.math.BigDecimal;
import java.util.Random;

import bsz.exch.service.Result;
import bsz.exch.service.Task;

public class SaoMaPayService {

	public static Result pay(String login_name,BigDecimal credit,String return_url,String payType,String customer_ip){
		Task task =Task.create("smp_pay_8d");
		task.addParam("product", task.getProduct());
		task.addParam("login_name",login_name);
		Random r = new Random();
		task.addParam("amount",credit.subtract(new BigDecimal(r.nextDouble() * 0.8 + 0.1)).setScale(2, BigDecimal.ROUND_HALF_UP));
		task.addParam("return_url",return_url);
		task.addParam("customer_ip",customer_ip);
		task.addParam("payType",payType);
		Result result=task.perform();	
		if(result.success()){
			return result;
		}
		return null;
	}
	
	
	public static Result notify(String merNo,BigDecimal transAmt,String orderNo,String orderDate,String respCode,String respDesc,String payId,String payTime,String signature){
		Task task =Task.create("smp_notify_8d");
		task.addParam("product", task.getProduct());
		task.addParam("merNo",merNo);
		task.addParam("transAmt",transAmt);
		task.addParam("orderNo",orderNo);
		task.addParam("orderDate",orderDate);
		task.addParam("respCode",respCode);
		task.addParam("respDesc",respDesc);
		task.addParam("payId",payId);
		task.addParam("payTime",payTime);
		task.addParam("signature",signature);
		Result result=task.perform();	
		if(result.success()){
			return result;
		}
		return null;
	}
	
}
