package com.ws.service;

import java.math.BigDecimal;

import bsz.exch.service.Result;
import bsz.exch.service.Task;

public class XingHeYiService {
							
	public static Result pay(String login_name,BigDecimal credit,String bank,String ip){
		Task task =Task.create("xingheyi_pay_8d");
		
		task.addParam("login_name",login_name);
		task.addParam("amount",credit.floatValue());
		task.addParam("ip",ip);
		task.addParam("bank",bank);
		task.addParam("product", task.getProduct());
		Result result=task.perform();	
		if(result.success()){
			return result;
		}
		return null;
	}

	public static Result xingheyitongpayNotify(String service, String merId, String tradeNo, String tradeDate,
			String opeNo, String opeDate, BigDecimal amount, String status, String extra, String payTime, String sign) {
		// TODO Auto-generated method stub
		Task task =Task.create("xingheyi_notify_8d");
		
		task.addParam("service",service);
		task.addParam("merId",merId);
		task.addParam("tradeNo",tradeNo);
		task.addParam("tradeDate",tradeDate);
		task.addParam("opeNo",opeNo);
		task.addParam("opeDate",opeDate);
		task.addParam("amount",amount);
		task.addParam("status",status);
		task.addParam("extra",extra);
		task.addParam("payTime",payTime);
		task.addParam("sign",sign);
		
		task.addParam("product", task.getProduct());
		Result result=task.perform();	
		if(result.success()){
			return result;
		}
		return null;
	}

	public static Result wangyinpay(String login_name, BigDecimal amount, String domain, String bank,
			String remoteAddress) {
		// TODO Auto-generated method stub
		Task task =Task.create("jianyue_wangyin_pay_8d");
		
		task.addParam("login_name",login_name);
		task.addParam("amount",amount);
		task.addParam("ip",remoteAddress);
		task.addParam("bank",bank);
		task.addParam("product", task.getProduct());
		Result result=task.perform();	
		if(result.success()){
			return result;
		}
		return null;
	}

	public static Result scanpay(String login_name, BigDecimal amount, String domain, String bank, String ipAddr) {
		// TODO Auto-generated method stub
		Task task =Task.create("xingheyi_scan_pay_8d");
		
		task.addParam("login_name",login_name);
		task.addParam("amount",amount);
		task.addParam("domain", domain);
		task.addParam("bank",bank);
		task.addParam("ip",ipAddr);
		task.addParam("product", task.getProduct());
		Result result=task.perform();	
		if(result.success()){
			return result;
		}
		return null;
	}
	
	/*public static Result gaotongpayNotify(String ordernumber,BigDecimal paymoney,String orderstatus,String sysnumber,String partner,String attach,String sign){
		Task task =Task.create("gaotong_notify_8d");
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


	public static Result queryGtOrder(String ordernumber) {
		// TODO Auto-generated method stub
		Task task =Task.create("gaotong_query_order_by_order_id_8d");
		task.addParam("product", task.getProduct());
		task.addParam("pay_id",ordernumber);
		Result result=task.perform();	
		if(result.success()){
			return result;
		}
		return null;
	}*/
	
}
