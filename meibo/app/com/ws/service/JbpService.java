package com.ws.service;

import java.math.BigDecimal;

import bsz.exch.service.Result;
import bsz.exch.service.Task;

public class JbpService {
	
	public static Result pay(String login_name,BigDecimal credit,String remark,String return_url){
		BigDecimal cr=credit.setScale(2, BigDecimal.ROUND_HALF_UP);
		Task task =Task.create("jbp_pay_8d");
		task.addParam("product", task.getProduct());
		task.addParam("login_name",login_name);
		task.addParam("amount",cr.toString());
		task.addParam("return_url",return_url);
		task.addParam("remark",remark);
		Result result=task.perform();	
		if(result.success()){
			return result;
		}
		return null;
	}
	
	public static Result queryJbOrder(String pay_id){
		Task task =Task.create("jbp_query_order_by_pay_id_8d");
		task.addParam("product", task.getProduct());
		task.addParam("pay_id",pay_id);
		Result result=task.perform();	
		if(result.success()){
			return result;
		}
		return null;
	}
	
	
	public static void main(String[] args){
		pay("lance008",new BigDecimal(100.00),"pd","");
		
	}

}
