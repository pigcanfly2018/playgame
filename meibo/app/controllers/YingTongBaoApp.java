package controllers;

import java.math.BigDecimal;

import com.ws.service.YingtongbaoPayService;

import bsz.exch.service.Result;


import play.mvc.Before;
import play.mvc.Controller;

public class YingTongBaoApp extends Controller{

	@Before
	 public static void before(){
		if(IPApp.isxss()){
			renderText("您的输入存在非法字符!");
		}
	}
	
	public static void show(String order_no){
		if(order_no==null||(!order_no.startsWith("yt"))){
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
	
	public static void yingtongbaopayNotify() {
		boolean success = false;
		String status = params.get("status");
		String payType = params.get("payType");
		if ("1".equalsIgnoreCase(status) && payType != null && !payType.equals("")){
			String transDate = params.get("transDate");
		    String transTime = params.get("transTime");
		    String merchno = params.get("merchno");
		    String merchName = params.get("merchName");
		    String signature = params.get("signature");
		    String traceno = params.get("traceno");
		    
		    String orderno = params.get("orderno");
		    String channelOrderno = params.get("channelOrderno");
		    String channelTraceno = params.get("channelTraceno");
		    
		    BigDecimal amount = new BigDecimal(params.get("amount"));
		    
		    Result  result= YingtongbaoPayService.xunhuibaopayNotify(transDate, amount, transTime, merchno, merchName, signature, traceno, payType, orderno, channelOrderno, channelTraceno,status);
		    if(result != null && result.get("ok").equals("1")){
		    	renderText("success");
		    }else{
		    	renderText("success");
		    }
		}
		
		
		if("2".equalsIgnoreCase(status)){
			
			
		    String merchno = params.get("merchno");
		    String signature = params.get("signature");
		    String traceno = params.get("traceno");
		    
		    String orderno = params.get("orderno");
		    String channelOrderno = params.get("channelOrderno");
		    
		    BigDecimal amount = new BigDecimal(params.get("amount"));
		    
		    Result  result= YingtongbaoPayService.xunhuibaopayNotify(amount, merchno, signature, traceno, orderno, channelOrderno,status);
		    if(result != null && result.get("ok").equals("1")){
		    	renderText("success");
		    }else{
		    	renderText("success");
		    }
		}
		
		 renderText("success");
	}
	
}
