package controllers;

import com.ws.service.HbpService;

import play.mvc.Controller;

public class HuiBoPayApp extends Controller{

	public static void huibopayNotify(){
		
		boolean success = false;
		String respCode = params.get("respCode");
		if ("000000".equalsIgnoreCase(respCode)){
			success = true;
		}
		if (success){
			String orderId = params.get("orderId");
			String respInfo = params.get("respInfo");
			String amount = params.get("amount");
			String signature = params.get("signature");
			
			HbpService.huibopayNotify(respCode, orderId, amount, respInfo, signature);
			
			renderText("200");
		}
		
		renderText("200");
		
	}
}
