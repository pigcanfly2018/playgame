package controllers;

import java.math.BigDecimal;

import bsz.exch.service.Result;

import com.ws.service.ChangFuPayService;
import com.ws.service.HuiTianPayService;

import play.mvc.Before;
import play.mvc.Controller;

public class HuiTianPayApp extends Controller{

	@Before
	 public static void before(){
		if(IPApp.isxss()){
			renderText("您的输入存在非法字符!");
		}
	}
	
	public static void huitianpaynotify() {
		
		
		String P_ErrCode = params.get("P_ErrCode");
		
		if ("0".equals(P_ErrCode)){
			String P_UserID = params.get("P_UserId");
		    String P_OrderID = params.get("P_OrderId");
		    String P_CardID = params.get("P_CardId");
		    String P_CardPass = params.get("P_CardPass");
		    BigDecimal P_FaceValue = new BigDecimal(params.get("P_FaceValue"));
		    String P_ChannelID = params.get("P_ChannelId");
		    String P_PayMoney = params.get("P_PayMoney");
		    String P_Subject = params.get("P_Subject");
		    String P_Price = params.get("P_Price");
		    String P_Quantity = params.get("P_Quantity");
		    String P_Description = params.get("P_Description");
		    String P_Notic = params.get("P_Notic");
		    String P_ErrMsg = params.get("P_ErrMsg");
		    
		    String P_PostKey = params.get("P_PostKey");
		    
		   
		    
		    Result  result=HuiTianPayService.notify(P_ErrCode, P_UserID, P_OrderID, P_CardID, P_CardPass, P_FaceValue, P_ChannelID,
		    		P_PayMoney,P_Subject,P_Price,P_Quantity,P_Description,P_Notic,P_ErrMsg,P_PostKey);
		    
		    if(result != null && result.get("ok").equals("1")){
		    	renderText("ErrCode=0");
		    }else{
		    	renderText("ErrCode=0");
		    }
		   
		}
		
		renderText("ErrCode=0");
	}

}
