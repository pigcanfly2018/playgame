package controllers;

import java.math.BigDecimal;

import bsz.exch.service.Result;

import com.ws.service.SanVPayService;
import com.ws.service.XinBeiPayService;

import play.mvc.Before;
import play.mvc.Controller;

public class SanVApp extends Controller{

	@Before
	 public static void before(){
		if(IPApp.isxss()){
			renderText("您的输入存在非法字符!");
		}
	}
	
	
	public static void sanvpayNotify() {
		
		boolean success = false;
		String return_code = params.get("return_code");
		if ("0".equalsIgnoreCase(return_code)){
			success = true;
		}
		if (success){
			
		    
		    String channelOrderId = params.get("channelOrderId");
		    String timeStamp = params.get("timeStamp");
		    String orderId = params.get("orderId");
		     
		    String sign = params.get("sign");
		    String attach = params.get("attach");
		    BigDecimal totalFee = new BigDecimal(params.get("totalFee"));
		    String transactionId = params.get("transactionId");
		    Result  result=SanVPayService.sanvpayNotify(return_code, totalFee, channelOrderId, orderId, timeStamp, sign, attach, transactionId);
		    
		    if(result != null && result.get("ok").equals("1")){
		    	renderText("SUCCESS");
		    }else{
		    	renderText("SUCCESS");
		    }
		   
		}
		
		renderText("SUCCESS");
	}
}
