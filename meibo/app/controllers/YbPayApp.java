package controllers;

import java.math.BigDecimal;

import bsz.exch.service.Result;

import com.ws.service.JbpService;
import com.ws.service.YbpService;

import play.mvc.Controller;

public class YbPayApp extends Controller{

	/**
	 * 查询订单状态
	 * @param order_no
	 */
	public static void queryYbOrder(String orderid){
		Result result =YbpService.queryYbOrder(orderid);
		if(result!=null&&result.success()&&result.getLength()>0){
			String return_url=result.getFristResult("return_url");
			if(!request.domain.equals(return_url)){
				JbPayApp.redirect("http://"+return_url+"/queryYbOrder/"+orderid);
			}
			render(result,orderid);
		}
	}
	
	public static void yingbaopayNotify() {
	
		boolean success = false;
		String opstate = params.get("opstate");
		if ("2".equalsIgnoreCase(opstate)){
			success = true;
		}
		if (success){
			String orderid = params.get("orderid");
		    
		    String orderno = params.get("orderno");
		    String eypaltime = params.get("eypaltime");
		    String message = params.get("message");
		    String paytype = params.get("paytype");
		    String remark = params.get("remark");
		    String sign = params.get("sign");
		    
		    BigDecimal payamount = new BigDecimal(params.get("payamount"));
		    
		    Result  result=YbpService.yingbaopayNotify(orderid, payamount, opstate, orderno, eypaltime, message, paytype, remark, sign);
		    
		    if(result != null && result.get("ok").equals("1")){
		    	renderText("success");
		    }else{
		    	renderText("success");
		    }
		   
		}
		
	    
	    renderText("success");
	    
	}
	
	
	
}
