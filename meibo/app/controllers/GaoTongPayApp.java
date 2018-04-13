package controllers;

import java.math.BigDecimal;

import play.mvc.Controller;
import bsz.exch.service.Result;

import com.ws.service.GaoTongPayService;
import com.ws.service.YinbaoPayService;

public class GaoTongPayApp extends Controller{

	public static void gaotongpayNotify() {
		
		boolean success = false;
		String orderstatus = params.get("orderstatus");
		if ("1".equalsIgnoreCase(orderstatus)){
			success = true;
		}
		if (success){
			String ordernumber = params.get("ordernumber");
		    
		    String sysnumber = params.get("sysnumber");
		    String partner = params.get("partner");
		    String attach = params.get("attach");
		    String sign = params.get("sign");
		    
		    BigDecimal paymoney = new BigDecimal(params.get("paymoney"));
		    
		    Result  result=GaoTongPayService.gaotongpayNotify(ordernumber, paymoney, orderstatus, sysnumber, partner, attach, sign);
		    
		    if(result != null && result.get("ok").equals("1")){
		    	renderText("ok");
		    }else{
		    	renderText("ok");
		    }
		   
		}
		
	    
	    renderText("ok");
	    
	}
}
