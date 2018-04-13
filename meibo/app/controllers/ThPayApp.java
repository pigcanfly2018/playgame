package controllers;

import java.math.BigDecimal;

import bsz.exch.service.Result;

import com.ws.service.ThpService;
import com.ws.service.YbpService;

import play.mvc.Controller;

public class ThPayApp extends Controller{
	
	
	
	/**
	 * 查询订单状态
	 * @param order_no
	 */
	public static void queryThOrder(String order_no){
		Result result =ThpService.queryThOrder(order_no);
		if(result!=null&&result.success()&&result.getLength()>0){
			String return_url=result.getFristResult("return_url");
			if(!request.domain.equals(return_url)){
				JbPayApp.redirect("http://"+return_url+"/queryThOrder/"+order_no);
			}
			render(result,order_no);
		}
	}
	
	
	public static void tonghuipayNotify() {
		
		boolean success = false;
		String trade_status = params.get("trade_status");
		if ("success".equalsIgnoreCase(trade_status)){
			success = true;
		}
		if (success){
			String trade_no = params.get("trade_no");
			BigDecimal order_amount = new BigDecimal(params.get("order_amount"));
			String order_no = params.get("order_no");
			String return_params = params.get("return_params");
			String notify_type = params.get("notify_type");
			String sign = params.get("sign");
			String order_time = params.get("order_time");
			String trade_time = params.get("trade_time");
			try {
				
				Thread.sleep((int)Math.random()*20000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Result  result=ThpService.tonghuipayNotify(trade_no, return_params, order_amount, order_no,notify_type,sign,order_time,trade_time,trade_status);
			if(result != null && result.get("ok").equals("1")){
		    	renderText("success");
		    }else{
		    	renderText("fail");
		    }
		}
	    
	    
	    
	    renderText("fail");
	    
	    
	}
	
	public static void tonghuiweixinpayNotify() {
		boolean success = false;
		String trade_status = params.get("trade_status");
		if ("success".equalsIgnoreCase(trade_status)){
			success = true;
		}
		
		if (success){
			String trade_no = params.get("trade_no");
			BigDecimal order_amount = new BigDecimal(params.get("order_amount"));
			String order_no = params.get("order_no");
			String return_params = params.get("return_params");
			String notify_type = params.get("notify_type");
			String sign = params.get("sign");
			String order_time = params.get("order_time");
			String trade_time = params.get("trade_time");
			try {
				
				Thread.sleep((int)Math.random()*20000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Result  result=ThpService.tonghuiweixinpayNotify(trade_no, return_params, order_amount, order_no,notify_type,sign,order_time,trade_time,trade_status);
			if(result != null && result.get("ok").equals("1")){
		    	renderText("success");
		    }else{
		    	renderText("fail");
		    }
		}
		
		renderText("fail");
		
	}

}
