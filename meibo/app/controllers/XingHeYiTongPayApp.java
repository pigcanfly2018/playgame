package controllers;

import java.math.BigDecimal;

import play.mvc.Controller;
import bsz.exch.service.Result;
import bsz.exch.service.Task;

import com.ws.service.XingHeYiService;
import com.ws.service.YinbaoPayService;

public class XingHeYiTongPayApp extends Controller{

	public static void xingheyitongpaynotify() {
		if(params.get("reCode")!=null && "1".equals(params.get("reCode"))){
			redirect("https://m.288da.com");
		}

		boolean success = false;
		String status = params.get("status");
		if ("1".equalsIgnoreCase(status)){
			success = true;
		}
		if (success){
			String service = params.get("service");
		    String merId = params.get("merId");
		    String tradeNo = params.get("tradeNo");
		    String tradeDate = params.get("tradeDate");
		    String opeNo = params.get("opeNo");
		    String opeDate = params.get("opeDate");
		    BigDecimal amount = new BigDecimal(params.get("amount"));
		    String extra = params.get("extra");
		    String payTime = params.get("payTime");
		    String sign = params.get("sign");
		    
		    
		    Result  result=XingHeYiService.xingheyitongpayNotify(service, merId, tradeNo, tradeDate,
		    		opeNo, opeDate, amount,status,extra,payTime,sign);
		    
		    if(result != null && result.get("ok").equals("1")){
		    	renderText("SUCCESS");
		    }else{
		    	renderText("SUCCESS");
		    }
		   
		}
		renderText("SUCCESS");
	    
	}
	
	public static void redirecturl(){
		redirect("https://m.288da.com");
	}
	
}
