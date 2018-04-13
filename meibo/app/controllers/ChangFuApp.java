package controllers;

import java.math.BigDecimal;

import com.ws.service.ChangFuPayService;

import bsz.exch.service.Result;
import play.mvc.Before;
import play.mvc.Controller;

public class ChangFuApp extends Controller{
	
	@Before
	 public static void before(){
		if(IPApp.isxss()){
			renderText("您的输入存在非法字符!");
		}
	}
	
	public static void changfupayNotify() {
		
		boolean success = false;
		String returncode = params.get("returncode");
		if ("1".equalsIgnoreCase(returncode)){
			success = true;
		}
		if (success){
			String userid = params.get("userid");
		    
		    String orderid = params.get("orderid");
		    String sign2 = params.get("sign2");
		    String sign = params.get("sign");
		   
		    
		    String ext = params.get("ext");
		   
		    BigDecimal money = new BigDecimal(params.get("money"));
		    
		    Result  result=ChangFuPayService.changfupayNotify(userid, orderid, returncode, sign2, sign, money, ext);
		    
		    if(result != null && result.get("ok").equals("1")){
		    	renderText("ok");
		    }else{
		    	renderText("ok");
		    }
		   
		}
		
		renderText("ok");
	}

}
