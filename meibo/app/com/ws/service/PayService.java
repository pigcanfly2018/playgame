package com.ws.service;

import java.math.BigDecimal;

import bsz.exch.service.Result;
import bsz.exch.service.Task;

public class PayService {

	public static Result antongPay(String login_name,BigDecimal credit,String return_url,String paytype,String ip){
		Task task =Task.create("atp_pay_8d");
		task.addParam("product", task.getProduct());
		task.addParam("login_name",login_name);
		task.addParam("amount",credit.setScale(2, BigDecimal.ROUND_HALF_UP));
		task.addParam("return_url",return_url);
		task.addParam("ip",ip);
		task.addParam("paytype",paytype);
		Result result=task.perform();	
		if(result.success()){
			return result;
		}
		return null;
	}
	
	public static Result lefubaoPay(String login_name,BigDecimal credit,String return_url,String paytype,String ip){
		Task task =Task.create("lfbp_pay_8d");
		task.addParam("product", task.getProduct());
		task.addParam("login_name",login_name);
		task.addParam("amount",credit.setScale(2, BigDecimal.ROUND_HALF_UP));
		task.addParam("return_url",return_url);
		task.addParam("ip",ip);
		task.addParam("paytype",paytype);
		Result result=task.perform();	
		if(result.success()){
			return result;
		}
		return null;
	}
	
	
	public static String lefubaonotify(String r1_Code, String r2_TrxId, String r3_Amt, String r5_Order,String r6_Type,
			String hmac) {
		// TODO Auto-generated method stub
		Task task = Task.create("lfbp_notify_8d");
		task.addParam("r1_Code", r1_Code);
		task.addParam("r2_TrxId", r2_TrxId);
		task.addParam("r3_Amt", r3_Amt);
		task.addParam("r5_Order", r5_Order);
		task.addParam("r6_Type", r6_Type);
		task.addParam("hmac", hmac);
		task.addParam("product", task.getProduct());
		Result result = task.perform();
	
		String res = "";
		if(result.success()){
			 res = result.get("ok");
		}
		return res;
	}
	
	public static String antongnotify(String status, String customerid, String sdpayno, String sdorderno,
			String total_fee, String paytype, String remark, String sign) {
		// TODO Auto-generated method stub
		Task task = Task.create("atp_notify_8d");
		task.addParam("status", status);
		task.addParam("customerid", customerid);
		task.addParam("sdpayno", sdpayno);
		task.addParam("sdorderno", sdorderno);
		
		task.addParam("total_fee", total_fee);
		task.addParam("paytype", paytype);
		task.addParam("remark", remark);
		task.addParam("sign", sign);
		task.addParam("product", task.getProduct());
		Result result = task.perform();
	
		String res = "";
		if(result.success()){
			 res = result.get("ok");
		}
		return res;
	}

	public static Result huitpay(String login_name, BigDecimal amount, String domain, String payType, String ipAddr) {
		// TODO Auto-generated method stub
		Task task =Task.create("huit_pay_8d");
		task.addParam("product", task.getProduct());
		task.addParam("login_name",login_name);
		task.addParam("amount",amount.setScale(2, BigDecimal.ROUND_HALF_UP));
		task.addParam("return_url",domain);
		task.addParam("ip",ipAddr);
		task.addParam("paytype",payType);
		Result result=task.perform();	
		if(result.success()){
			return result;
		}
		return null;
	}

	public static String huitpayNotify(String orderid, String opstate, String ovalue, String sysorderid, String systime,
			String attach, String sign, String msg) {
		// TODO Auto-generated method stub
		Task task = Task.create("huit_notify_8d");
		task.addParam("orderid", orderid);
		task.addParam("opstate", opstate);
		task.addParam("ovalue", ovalue);
		task.addParam("sysorderid", sysorderid);
		
		task.addParam("systime", systime);
		task.addParam("attach", attach);
		task.addParam("sign", sign);
		task.addParam("msg", msg);
		task.addParam("product", task.getProduct());
		Result result = task.perform();
	
		String res = "";
		if(result.success()){
			 res = result.get("ok");
		}
		return res;
	}
	
}
