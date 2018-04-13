package com.ws.service;

import java.math.BigDecimal;

import bsz.exch.service.Plat;
import bsz.exch.service.Result;
import bsz.exch.service.Task;

public class DPayService {
	
	/**
	 * 读取银行卡
	 * @param login_name
	 * @param credit
	 * @param from_bank
	 * @param deposit_type
	 * @return
	 */
	public static Result bank(String login_name,BigDecimal credit,String from_bank){
		BigDecimal cr=credit.setScale(2, BigDecimal.ROUND_HALF_UP);
		Task task =Task.create("pay_deposit_8d");
		task.addParam("product", task.getProduct());
		task.addParam("login_name",login_name);
		task.addParam("amount",cr.toString());
		task.addParam("from_bank",from_bank);
		task.addParam("deposit_type","1");
		Result result=task.perform();	
		if(result.success()){
			return result;
		}
		return null;
	}
	
	/**
	 * 读取财付通账号
	 * @param login_name
	 * @param credit
	 * @return
	 */
	public static Result tenPay(String login_name,BigDecimal credit){
		BigDecimal cr=credit.setScale(2, BigDecimal.ROUND_HALF_UP);
		Task task =Task.create("pay_deposit_8d");
		task.addParam("product", task.getProduct());
		task.addParam("login_name",login_name);
		task.addParam("amount",cr.toString());
		task.addParam("from_bank","31");
		task.addParam("deposit_type","2");
		Result result=task.perform();	
		if(result.success()){
			return result;
		}
		return null;
	}
	
	/**
	 * 支付宝二维码
	 * @param login_name
	 * @param credit
	 * @return
	 */
	public static Result alipay(String login_name,BigDecimal credit){
		BigDecimal cr=credit.setScale(2, BigDecimal.ROUND_HALF_UP);
		Task task =Task.create("pay_deposit_8d");
		task.addParam("product", task.getProduct());
		task.addParam("login_name",login_name);
		task.addParam("amount",cr.toString());
		task.addParam("from_bank","30");
		task.addParam("deposit_type","3");
		Result result=task.perform();	
		if(result.success()){
			return result;
		}
		return null;
	}
	
}
