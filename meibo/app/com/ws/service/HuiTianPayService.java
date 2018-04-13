package com.ws.service;

import java.math.BigDecimal;

import bsz.exch.service.Result;
import bsz.exch.service.Task;

public class HuiTianPayService {
	
	public static Result pay(String login_name,BigDecimal credit,String return_url,String payType,String customer_ip){
		Task task =Task.create("jyp_pay_8d");
		task.addParam("product", task.getProduct());
		task.addParam("login_name",login_name);
		task.addParam("amount",credit.intValue());
		task.addParam("return_url",return_url);
		task.addParam("customer_ip",customer_ip);
		task.addParam("payType",payType);
		Result result=task.perform();	
		if(result.success()){
			return result;
		}
		return null;
	}

	public static Result notify(String p_ErrCode, String p_UserID, String p_OrderID, String p_CardID, String p_CardPass,
			BigDecimal p_FaceValue, String p_ChannelID, String p_PayMoney, String p_Subject, String p_Price,
			String p_Quantity, String p_Description, String p_Notic, String p_ErrMsg, String p_PostKey) {
		// TODO Auto-generated method stub
		
		Task task =Task.create("htp_notify_8d");
		
		task.addParam("p_ErrCode",p_ErrCode);
		task.addParam("p_UserID",p_UserID);
		task.addParam("p_OrderID",p_OrderID);
		task.addParam("p_CardID",p_CardID);
		task.addParam("p_CardPass",p_CardPass);
		task.addParam("p_FaceValue",p_FaceValue);
		task.addParam("p_ChannelID",p_ChannelID);
		task.addParam("p_PayMoney",p_PayMoney);
		task.addParam("p_Subject",p_Subject);
		task.addParam("p_Price",p_Price);
		task.addParam("p_Quantity",p_Quantity);
		task.addParam("p_Description",p_Description);
		task.addParam("p_Notic",p_Notic);
		task.addParam("p_ErrMsg",p_ErrMsg);
		task.addParam("p_PostKey",p_PostKey);
		System.out.println("p_UserID"+p_UserID+"p_OrderID="+p_OrderID);
		task.addParam("product", task.getProduct());
		Result result=task.perform();	
		if(result.success()){
			return result;
		}
		return null;
		
	}

}
