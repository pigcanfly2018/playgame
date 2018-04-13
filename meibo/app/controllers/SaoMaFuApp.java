package controllers;

import java.math.BigDecimal;

import bsz.exch.service.Result;

import com.ws.service.SaoMaPayService;
import com.ws.service.YingtongbaoPayService;

import play.mvc.Before;
import play.mvc.Controller;

public class SaoMaFuApp  extends Controller{

	@Before
	 public static void before(){
		if(IPApp.isxss()){
			renderText("您的输入存在非法字符!");
		}
	}
	
	
	public static void saomafupayNotify() {
		
		String respCode = params.get("respCode");
		if ("0000".equalsIgnoreCase(respCode) ){
			
			String merNo = params.get("merNo");
		    String orderNo = params.get("orderNo");
		    
		    String orderDate = params.get("orderDate");
		    
		    String payId = params.get("payId");
		    
		    String payTime = params.get("payTime");
		    String signature = params.get("signature");
		    String respDesc = params.get("respDesc");
		    
		    BigDecimal transAmt = new BigDecimal(params.get("transAmt"));
		    
		    Result  result= SaoMaPayService.notify(merNo, transAmt, orderNo, orderDate, respCode, respDesc, payId, payTime, signature);
		    if(result != null && result.get("ok").equals("1")){
		    	renderText("SUCCESS");
		    }else{
		    	renderText("SUCCESS");
		    }
		}
		
		renderText("SUCCESS");
		
		
		
	}
	
}
