package controllers;

import org.apache.commons.lang3.StringUtils;

import models.Customer;
import models.Deposit;
import play.mvc.Before;
import play.mvc.Controller;
import util.JSONResult;
import util.MD5;

public class AdultApp extends Controller{

	@Before
	 public static void before(){
		if(IPApp.isxss()){
			renderText("您的输入存在非法字符!");
		}
	}
	
	public static void binding() {
		
		String login_name = params.get("login_name");
		String password = params.get("password");
		String adultaccount = params.get("adultaccount");
		if(StringUtils.trimToEmpty(login_name).equals("") || StringUtils.trimToEmpty(password).equals("") || StringUtils.trimToEmpty(adultaccount).equals("")){
			String errorMsg="用户名不存在或密码错误!";
			renderText(JSONResult.adultfailure(errorMsg));
		}
		
		Customer customer=Customer.getCustomer(login_name);
		
		if(customer == null){
			String errorMsg="用户名不存在或密码错误!";
			renderText(JSONResult.adultfailure(errorMsg));
		}
		
		if(!customer.login_pwd.equalsIgnoreCase(MD5.md5(password))){
			String errorMsg="用户名不存在或密码错误!";
			renderText(JSONResult.adultfailure(errorMsg));
		}
		
		if(customer.accountkey == null || customer.accountkey.equals("")  ){
			
		}else{
			String errorMsg="该用户名已被关联,请您再仔细核对!";
			renderText(JSONResult.adultfailure(errorMsg));
		}
		
		String accountkey = MD5.md5(login_name);
		boolean flag = customer.NTmodfyAccountkey(login_name, accountkey);
		if(flag){
			renderText(JSONResult.adultsuccess(Deposit.NTgetSumAll(customer.cust_id).intValue()+"",accountkey));
		}else{
			String errorMsg="系统错误,请您稍后再试!";
			renderText(JSONResult.adultfailure(errorMsg));
		}
			
	}
	
	public static void adultQuery() {
		String accountkey = params.get("accountkey");
		
		if(StringUtils.trimToEmpty(accountkey).equals("")){
			String errorMsg="用户名不存在或密码错误!";
			renderText(JSONResult.adultfailure(errorMsg));
		}
		
		Customer customer=Customer.getCustomerByAccountkey(accountkey);
		if(customer == null){
			String errorMsg="用户key不存在!";
			renderText(JSONResult.adultfailure(errorMsg));
		}
		
		renderText(JSONResult.adultsuccess(Deposit.NTgetSumAll(customer.cust_id).intValue()+"",accountkey));
		
	}
}
