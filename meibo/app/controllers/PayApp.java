package controllers;

import com.ws.service.HuiTongPayService;
import com.ws.service.PayService;

import play.mvc.Controller;

public class PayApp extends Controller{

	public static void huitongNotify() {
		
		String trade_status = params.get("trade_status");
		  if ("success".equalsIgnoreCase(trade_status)){
			  String merchant_code = params.get("merchant_code");
			   String sign = params.get("sign");
			   String order_no = params.get("order_no");
			   String order_amount = params.get("order_amount");
			   String order_time = params.get("order_time");
			   String trade_no = params.get("trade_no");
			   String trade_time = params.get("trade_time");
			   
			   String  result=HuiTongPayService.notifypay(sign, merchant_code, order_no, order_amount, order_time, trade_no, trade_time,trade_status);
			   if(result != null && result.equals("1")){
			       renderText("Success");
			      }else{
			       renderText("Success");
			      }
		  }
		   renderText("faile");
		   
	}
	
	
	public static void antongNotify() {
		
		String status = params.get("status");
		  if ("1".equalsIgnoreCase(status)){
			  String customerid = params.get("customerid");
			   String sdpayno = params.get("sdpayno");
			   String sdorderno = params.get("sdorderno");
			   String total_fee = params.get("total_fee");
			   String paytype = params.get("paytype");
			   String remark = params.get("remark");
			   String sign = params.get("sign");
			   
			   String  result =PayService.antongnotify(status, customerid, sdpayno, sdorderno, total_fee, paytype, remark, sign);
					   
				if(result != null && result.equals("1")){
					       renderText("success");
					      }else{
					       renderText("success");
					      }

		  }
		   renderText("success");
		   
	}
	
	public static void huitpayNotify() {
		
		String status = params.get("opstate");
		  if ("0".equalsIgnoreCase(status)){
			  String orderid = params.get("orderid");
			   String opstate = params.get("opstate");
			   String ovalue = params.get("ovalue");
			   
			   String sysorderid = params.get("sysorderid");
			   String systime = params.get("systime");
			   String attach = params.get("attach");
			   String sign = params.get("sign");
			   String msg = params.get("msg");
			   
			   String  result =PayService.huitpayNotify(orderid, opstate, ovalue, sysorderid, systime, attach, sign, msg);
				if(result != null && result.equals("1")){
			       renderText("opstate=0");
			      }else{
			       renderText("opstate=0");
			      }
		  }
	}
	public static void lefubaopayNotify() {
		
		String r1_Code = params.get("r1_Code");
		  if ("1".equalsIgnoreCase(r1_Code)){
			  String r2_TrxId = params.get("r2_TrxId");
			   String r3_Amt = params.get("r3_Amt");
			   String r5_Order = params.get("r5_Order");
			   String r6_Type = params.get("r6_Type");
			   String hmac = params.get("hmac");
			   
			   String  result =PayService.lefubaonotify(r1_Code, r2_TrxId, r3_Amt, r5_Order,r6_Type, hmac);
					   
				if(result != null && result.equals("1")){
					       renderText("success");
					      }else{
					       renderText("success");
					      }
		  }
		   renderText("success");
		   
	}
}
