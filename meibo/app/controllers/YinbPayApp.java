package controllers;

import java.math.BigDecimal;

import bsz.exch.service.Result;

import com.ws.service.YbpService;
import com.ws.service.YinbaoPayService;

import play.mvc.Controller;

public class YinbPayApp extends Controller{

	/**
	 * 查询订单状态
	 * @param order_no
	 */
	public static void queryYinbOrder(String ordernumber){
		Result result =YinbaoPayService.queryYbOrder(ordernumber);
		if(result!=null&&result.success()&&result.getLength()>0){
			String return_url=result.getFristResult("return_url");
			if(!request.domain.equals(return_url)){
				JbPayApp.redirect("http://"+return_url+"/queryYinbOrder/"+ordernumber);
			}
			render(result,ordernumber);
		}
	}
	
	public static void yinbaopayNotify() {
		
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
		    
		    Result  result=YinbaoPayService.yinbaopayNotify(ordernumber, paymoney, orderstatus, sysnumber, partner, attach, sign);
		    
		    if(result != null && result.get("ok").equals("1")){
		    	renderText("ok");
		    }else{
		    	renderText("ok");
		    }
		   
		}
		
	    
	    renderText("ok");
	    
	}
	
	
	
	
}
