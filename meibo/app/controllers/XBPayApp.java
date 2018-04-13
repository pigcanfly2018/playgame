package controllers;

import java.math.BigDecimal;

import bsz.exch.service.Result;

import com.ws.service.XinBeiPayService;
import com.ws.service.YbpService;
import com.ws.service.YinbaoPayService;

import play.mvc.Before;
import play.mvc.Controller;

public class XBPayApp extends Controller{

	@Before
	 public static void before(){
		if(IPApp.isxss()){
			renderText("您的输入存在非法字符!");
		}
	}
	
	/**
	 * 查询订单状态
	 * @param order_no
	 */
	public static void queryXbOrder(String orderid){
		
		Result result =XinBeiPayService.queryXinBeiOrder(orderid);
		if(result!=null&&result.success()&&result.getLength()>0){
			String return_url=result.getFristResult("return_url");
			if(!request.domain.equals(return_url)){
				JbPayApp.redirect("http://"+return_url+"/queryXbOrder/"+orderid);
			}
			render(result,orderid);
		}
	}
	
	public static void xinbeipayNotify() {
		
		boolean success = false;
		String State = params.get("State");
		if ("8888".equalsIgnoreCase(State)){
			success = true;
		}
		if (success){
			String MerchantCode = params.get("MerchantCode");
		    
		    String OrderId = params.get("OrderId");
		    String OrderDate = params.get("OrderDate");
		    String TradeIp = params.get("TradeIp");
		    String SerialNo = params.get("SerialNo");
		    
		    String PayCode = params.get("PayCode");
		    String FinishTime = params.get("FinishTime"); 
		    String SignValue = params.get("SignValue");
		    BigDecimal Amount = new BigDecimal(params.get("Amount"));
		    
		    Result  result=XinBeiPayService.xinbeipayNotify(MerchantCode, OrderId, OrderDate, TradeIp, SerialNo, Amount, PayCode, State, FinishTime, SignValue);
		    
		    if(result != null && result.get("ok").equals("1")){
		    	renderText("ok");
		    }else{
		    	renderText("ok");
		    }
		   
		}
		
		renderText("ok");
	}
	
}
