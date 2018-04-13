package controllers;

import java.math.BigDecimal;

import bsz.exch.service.Result;

import com.ws.service.LypService;
import com.ws.service.YbpService;

import play.mvc.Controller;

public class LyPayApp extends Controller{

	/**
	 * 查询订单状态
	 * @param order_no
	 */
	public static void queryLyOrder(String orderID){
		Result result =LypService.queryLyOrder(orderID);
		if(result!=null&&result.success()&&result.getLength()>0){
			String return_url=result.getFristResult("return_url");
			if(!request.domain.equals(return_url)){
				LyPayApp.redirect("http://"+return_url+"/queryLyOrder/"+orderID);
			}
			render(result,orderID);
		}
	}
	
	public static void leyingpayNotify() {
		
		boolean success = false;
		String resultCode = params.get("resultCode");
		String stateCode = params.get("stateCode");
		if ("2".equalsIgnoreCase(stateCode)){
			success = true;
		}
		if (success){
			String orderID = params.get("orderID");
			
			String orderAmount = params.get("orderAmount");//订单金额
			String payAmount = params.get("payAmount");//实际支付金额
			
			String acquiringTime = params.get("acquiringTime");
			String completeTime = params.get("completeTime");
			String orderNo = params.get("orderNo");
			
		    String partnerID = params.get("partnerID");
		    String remark = params.get("remark");
		    String charset = params.get("charset");
		    String signType = params.get("signType");
		    String signMsg = params.get("signMsg");
		  
		    
		    Result  result=LypService.leyingpayNotify(orderID, orderAmount,payAmount,acquiringTime, completeTime, orderNo,
		    		partnerID, remark, charset, signType,signMsg,resultCode,stateCode);
		    
		    if(result != null && result.get("ok").equals("1")){
		    	renderText("200");
		    }else{
		    	renderText("200");
		    }
		   
		}
		
	    
	    renderText("200");
	    
	}
	
	
	
}
