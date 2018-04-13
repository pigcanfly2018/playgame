package com.ws.service;

import java.math.BigDecimal;

import bsz.exch.service.Result;
import bsz.exch.service.Task;

public class HbpService {

	public static Result pay(String login_name,BigDecimal credit,String remark,String return_url,String bank,String ip){
		BigDecimal cr=credit.setScale(2, BigDecimal.ROUND_HALF_UP);
		Task task =Task.create("hbp_pay_8d");
		task.addParam("product", task.getProduct());
		task.addParam("login_name",login_name);
		task.addParam("amount",credit.intValue());
		task.addParam("return_url",return_url);
		task.addParam("bank",bank);
		task.addParam("customer_ip",ip);
		task.addParam("remark",remark);
		Result result=task.perform();	
		if(result.success()){
			return result;
		}
		return null;
	}
	
	public static Result qrcodePay(String login_name,BigDecimal credit,String remark,String return_url,String bank,String ip){
		BigDecimal cr=credit.setScale(2, BigDecimal.ROUND_HALF_UP);
		Task task =Task.create("hbqrcode_pay_8d");
		task.addParam("product", task.getProduct());
		task.addParam("login_name",login_name);
		task.addParam("amount",credit.intValue());
		task.addParam("return_url",return_url);
		task.addParam("bank",bank);
		task.addParam("customer_ip",ip);
		task.addParam("remark",remark);
		Result result=task.perform();	
		if(result.success()){
			return result;
		}
		return null;
	}
	
	public static Result huibopayNotify(String respCode,String orderId,String amount,String respInfo,String signature){
		Task task =Task.create("hbp_notify_8d");
		task.addParam("product", task.getProduct());
		task.addParam("respCode",respCode);
		
		task.addParam("orderId",orderId);
		task.addParam("amount",amount);
		task.addParam("respInfo",respInfo);
		
		task.addParam("signature",signature);
		
		
		Result result=task.perform();	
		if(result.success()){
			return result;
		}
		return null;
	}
	
}
