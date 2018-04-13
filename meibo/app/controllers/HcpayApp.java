package controllers;

import java.math.BigDecimal;
import java.util.Date;

import models.Customer;
import models.Deposit;
import models.DepositLog;
import models.Hcpay;

import org.apache.commons.codec.digest.DigestUtils;

import play.Play;
import play.mvc.Controller;
import service.CreditLogService;
import service.CustomerService;
import service.EventService;
import util.DateUtil;

public class HcpayApp extends Controller{
	
	public static void hcpay(){
		try{
    	    String BillNo =  params.get("BillNo");
    	    String Amount =  params.get("Amount");
    	    String Succeed =  params.get("Succeed");
    	    String Result =  params.get("Result");
    	    String SignMD5info =  params.get("SignMD5info");
    	    String MD5key = Play.configuration.getProperty("hcpay.md5key");
    	    String md5src = BillNo+"&"+Amount+"&"+Succeed+"&"+MD5key;
    	    String SignMyInfo = DigestUtils.md5Hex(md5src.getBytes("UTF-8")).toUpperCase();
    	    if(SignMyInfo.equals(SignMD5info)){
    	    	if("88".equals(Succeed)){
    	    		if(!Hcpay.queryFinished(BillNo)){
    	    			Hcpay.updatefinished(Result, BillNo);
    	    			Hcpay hcpay=Hcpay.NTget(BillNo);
	   					Customer cust=Customer.getCustomer(hcpay.login_name);
	   					Date deposit_date= new Date(DateUtil.stringToDate(hcpay.order_time, "yyyy-MM-dd HH:mm:ss").getTime());
	   					Deposit dep=EventService.createDeposit(cust, deposit_date,IPApp.getIpAddr(), hcpay.amount, new BigDecimal(0), "汇潮支付", "一麻袋", "在线支付", BillNo);
	   					if(CustomerService.modCredit(dep.deposit_id, CreditLogService.Depoist, hcpay.login_name, 
	   							dep.amount, "system","存款", dep.dep_no)){
								     Deposit.NTchangeStatus(dep.deposit_id, 3, 3);
									 DepositLog log2 =new DepositLog();
									 log2.after_status=3;
									 log2.dep_no=dep.dep_no;
									 log2.create_user="system";
									 log2.deposit_id=dep.deposit_id;
									 log2.pre_status=1;
									 log2.create_date=new Date(System.currentTimeMillis());
									 log2.remarks="系统审核";
									 log2.NTcreat();
								     EventService.depositSuccess(cust, dep, hcpay.amount, BillNo);
								     
						 }
    	    		}
    	    	}
    	    	  renderText("ok");
    	    }else{
    	    	 renderText("fail");
    	    }
    	}catch(Exception e){
    		e.printStackTrace();
    		 renderText("fail");
    	}
		 
	}

}
