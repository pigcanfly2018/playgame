package controllers;

import com.ws.service.JbpService;

import bsz.exch.service.Result;
import play.mvc.Controller;

public class JbPayApp extends Controller{
	/**
	 * 查询订单状态
	 * @param order_no
	 */
	public static void queryJbOrder(String order_no){
		Result result =JbpService.queryJbOrder(order_no);
		if(result!=null&&result.success()&&result.getLength()>0){
			String return_url=result.getFristResult("return_url");
			if(!request.domain.equals(return_url)){
				JbPayApp.redirect("http://"+return_url+"/queryJbOrder/"+order_no);
			}
			render(result);
		}
	}
}
