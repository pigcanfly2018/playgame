package controllers;

import java.math.BigDecimal;

import com.ws.service.DingYiPayService;
import com.ws.service.YingtongbaoPayService;

import bsz.exch.service.Result;


import play.mvc.Before;
import play.mvc.Controller;

public class DingYiPayApp extends Controller{

	@Before
	 public static void before(){
		if(IPApp.isxss()){
			renderText("您的输入存在非法字符!");
		}
	}
	
	public static void queryOrder(String order_no){
		if(order_no==null||(!order_no.startsWith("dy"))){
	    	String result2="非法操作！";
			render(result2,order_no);
	    }
		
		String login_name=session.get("username");
		Result result = YingtongbaoPayService.getDetail(login_name,order_no);
		if("2".equals(result.get("state"))){
			String result1="您的订单号为："+order_no+"。<br/>您的订单已经支付完成！";
			render(result1,order_no);
		}else{
			String result2="您的订单号为："+order_no+"。<br/>&nbsp暂时查询不到您订单支付结果信息，请稍等40秒再查询！<br/>&nbsp如果您已经成功支付，请您联系客服人员，为您确认。";
			render(result2,order_no);
		}
	}
	
	public static void dingyibaopayNotify() {
		
		String opstate = params.get("opstate");
		
		if ("0".equalsIgnoreCase(opstate)){
			String orderid = params.get("orderid");
		    String sysorderid = params.get("sysorderid");
		    String systime = params.get("systime");
		    String attach = params.get("attach");
		    String msg = params.get("msg");
		    BigDecimal ovalue = new BigDecimal(params.get("ovalue"));
		    String sign = params.get("sign");
		   
		    
		    Result  result= DingYiPayService.dingyipayNotify(orderid, opstate, sysorderid, systime, attach, msg, ovalue, sign);
		    if(result != null && result.get("ok").equals("1")){
		    	renderText("success");
		    }else{
		    	renderText("success");
		    }
		}
		
		 renderText("success");
	}
	
}
