package controllers;

import bsz.exch.service.Result;

import com.ws.service.RpnService;

import play.mvc.Controller;

public class RpnPayApp extends Controller{
	
	public static void queryRpnOrder(){
		
		String order_id = params.get("order_id");
		System.out.println(order_id);
		Result result =RpnService.queryRpnOrder(order_id);
		if(result!=null&&result.success()&&result.getLength()>0){
			String return_url=result.getFristResult("return_url");
			if(!request.domain.equals(return_url)){
				RpnPayApp.redirect("http://"+return_url+"/queryRpnOrder/?order_id="+order_id);
			}
			render(result,order_id);
		}
	}
	
	public static void rpnpayNotify() {
		boolean success = false;
		String pay_result = params.get("pay_result");
		if ("3".equalsIgnoreCase(pay_result)){
			success = true;
		}
		
		if (success){
			String order_id = params.get("order_id");
			String order_time = params.get("order_time");
			String order_amount = params.get("order_amount");
			String deal_id = params.get("deal_id");
			String deal_time = params.get("deal_time");
			String pay_amount = params.get("pay_amount");
			String signature = params.get("signature");
			
			Result  result=RpnService.rpnpayNotify(order_id, order_time, order_amount, deal_id, deal_time, pay_amount, pay_result, signature);
			if(result != null && result.get("ok").equals("1")){
		    	renderText("Success");
		    }else{
		    	renderText("Success");
		    }
		}
		
		 renderText("Success");
	}
	

}
