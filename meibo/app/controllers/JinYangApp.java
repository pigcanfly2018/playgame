package controllers;

import java.math.BigDecimal;

import bsz.exch.service.Result;

import com.ws.service.ChangFuPayService;
import com.ws.service.JinYangPayService;

import play.mvc.Before;
import play.mvc.Controller;

public class JinYangApp extends Controller{

	@Before
	 public static void before(){
		if(IPApp.isxss()){
			renderText("您的输入存在非法字符!");
		}
	}
	
	public static void jinyangpayNotify() {
		
		boolean success = false;
		String orderstatus = params.get("orderstatus");
		if ("1".equalsIgnoreCase(orderstatus)){
			success = true;
		}
		if (success){
			String partner = params.get("partner");
		    
		    String ordernumber = params.get("ordernumber");
		    String sysnumber = params.get("sysnumber");
		    String attach = params.get("attach");
		   
		    
		    String sign = params.get("sign");
		   
		    BigDecimal paymoney = new BigDecimal(params.get("paymoney"));
		    
		    Result  result=JinYangPayService.notify(partner, paymoney, ordernumber, orderstatus, sysnumber, attach, sign);
		    
		    if(result != null && result.get("ok").equals("1")){
		    	renderText("ok");
		    }else{
		    	renderText("ok");
		    }
		   
		}
		
		renderText("ok");
	}

}
