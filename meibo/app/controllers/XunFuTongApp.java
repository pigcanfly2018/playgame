package controllers;

import java.math.BigDecimal;

import bsz.exch.service.Result;

import com.ws.service.XunfutongPayService;
import com.ws.service.XunhuibaoPayService;

import play.mvc.Before;
import play.mvc.Controller;

public class XunFuTongApp extends Controller{

	
	@Before
	 public static void before(){
//		if(IPApp.isxss()){
//			renderText("您的输入存在非法字符!");
//		}
	}
	
	public static void xunfutongpayNotify() {
		
		String data = params.get("data");
		
		//System.out.println(data);
		
		if (data != null ){
			
		    Result  result= XunfutongPayService.xunfutongpayNotify(data);
		    if(result != null && result.get("ok").equals("1")){
		    	renderText("0");
		    }else{
		    	renderText("0");
		    }
		}
		
		 renderText("0");
	}
	
}
