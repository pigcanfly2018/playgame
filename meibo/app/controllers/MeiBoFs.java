package controllers;


import java.io.File;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

import models.Agent;
import models.Config;
import models.Customer;
import models.Deposit;
import models.DepositLog;
import models.Dinpay;
import models.Hcpay;
import models.HuoliGift;
import models.Letter;
import models.Msg;
import models.OrderNo;
import models.Withdraw;
import models.WithdrawLog;
import models.YearGift;

import org.apache.commons.codec.digest.DigestUtils;

import play.Play;
import play.cache.Cache;
import play.mvc.Controller;
import service.CreditLogService;
import service.CustomerService;
import service.MeiBoOpService;
import service.MeiBoService;
import util.DateUtil;
import util.DinpayBean;
import util.DinpayRandom;
import util.MD5;
import util.MyRandom;
import util.PayBank;
import util.QrcodeDis;
import bsz.exch.service.Plat;
import bsz.exch.service.Result;

import com.ws.service.ChangFuPayService;
import com.ws.service.DPayService;
import com.ws.service.DinPayService;
import com.ws.service.DingYiPayService;
import com.ws.service.GaoTongPayService;
import com.ws.service.HbpService;
import com.ws.service.HuiTongPayService;
import com.ws.service.JbpService;
import com.ws.service.JinYangPayService;
import com.ws.service.LypService;
import com.ws.service.PayService;
import com.ws.service.PlatService;
import com.ws.service.RpnService;
import com.ws.service.SanVPayService;
import com.ws.service.SaoMaPayService;
import com.ws.service.ThpService;
import com.ws.service.XinBeiPayService;
import com.ws.service.XingHeYiService;
import com.ws.service.XunfutongPayService;
import com.ws.service.XunhuibaoPayService;
import com.ws.service.YbpService;
import com.ws.service.YinbaoPayService;
import com.ws.service.YingtongbaoPayService;

public class MeiBoFs extends Controller{
	
	 protected static void registerValidate(boolean agent,String template){
	    	String code = params.current().get("veriCode");
			String login_name=params.get("login_name");
			String  login_pwd=params.get("login_pwd");
			String  real_name=params.get("real_name");
			String  phone=params.get("phone");
			String  qq=params.get("qq");
			String errorMsg="很遗憾，您的注册尚未成功，原因：";
			String veriCode=(String)Cache.get("veriCode" + session.getId());
			String ip=IPApp.getIpAddr();
			if(code==null){
				errorMsg=errorMsg+"验证码为空。";
				renderTemplate(template, errorMsg,login_name,real_name,phone,qq);
			}
			if (veriCode==null||(!validation.equals(code.toLowerCase(),veriCode.toLowerCase()).ok)) {
				errorMsg=errorMsg+"验证码错误。";
				renderTemplate(template, errorMsg,login_name,real_name,phone,qq);
			}
			if(!validation.maxSize(login_name, 9).ok||!validation.minSize(login_name, 5).ok){
				errorMsg=errorMsg+"用户名长度不符合要求!";
				renderTemplate(template, errorMsg,login_name,real_name,phone,qq);
			}
			if(!login_name.matches("[a-z0-9]+")){
				errorMsg=errorMsg+"用户名只能小写与数字组合!";
				renderTemplate(template, errorMsg,login_name,real_name,phone,qq);
			}
			if(MeiBoService.NTexist(login_name)){
				errorMsg=errorMsg+login_name+"用户名已经存在!";
				renderTemplate(template, errorMsg,login_name,real_name,phone,qq);
			}
			if(login_pwd==null||!login_pwd.matches("[.A-Za-z0-9]{6,16}")){
				errorMsg=errorMsg+"您输入的密码不符合规则!";
				renderTemplate(template, errorMsg,login_name,real_name,phone,qq);
			}
			if(real_name==null||real_name.length()>15||real_name.length()<2){
				errorMsg=errorMsg+"您的真实姓名不符合规则!";
				renderTemplate(template, errorMsg,login_name,real_name,phone,qq);
			}
			if(phone==null||phone.length()!=11||!validation.phone(phone).ok){
				errorMsg=errorMsg+"您的手机号码不符合规则!";
				renderTemplate(template, errorMsg,login_name,real_name,phone,qq);
			}
			if(MeiBoService.NTexistPhone(phone)){
				errorMsg=errorMsg+"您的手机号码已经被占用!";
				renderTemplate(template, errorMsg,login_name,real_name,phone,qq);
			}
			if(Customer.getRegCntToday(ip)>=2  && !"122.49.213.98".equals(ip)){
				errorMsg=errorMsg+"系统检测到您频繁注册，如您切实有意向开户，请联系在线客服为您开立账户！";
				renderTemplate(template, errorMsg,login_name,real_name,phone,qq);
			}
	    }
	
	/**
	 * 普通用户注册
	 */
	public static void reg(String login_name,String login_pwd,String real_name,
			String qq,String phone){
		registerValidate(false,"/MeiBo/reg.html");
		
		String reg_domain =request.host;
		String referkey = params.get("refer");
		String referdomain =null;
		String keyword = "无法获取";
		if(referkey!=null && !"".equals(referkey)){
			 referdomain = referkey.substring(0, referkey.lastIndexOf("/"));
			if(referkey.contains("&wd=")){//百度
				keyword = referkey.split("&wd=")[1].split("&")[0];
			}else if( referkey.contains("&q=") ){//360
				keyword = referkey.split("&q=")[1].split("&")[0];
			}else if(referkey.contains("query=")){//搜狗
				keyword = referkey.split("query=")[1].split("&")[0];
			}
		 try {
			 keyword = java.net.URLDecoder.decode(keyword,   "utf-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		
		Customer customer =new Customer();
		customer.login_name=login_name;
		customer.login_pwd=MD5.md5(login_pwd);
		customer.real_name=real_name;
		customer.phone=phone;
		customer.qq=qq;
		String ip=IPApp.getIpAddr();
		customer.reg_ip=ip;
		customer.flag=true;
		customer.create_user=customer.login_name;
		customer.cust_level=0;
		customer.is_agent=false;
		customer.reg_source=session.get("rs");
		customer.login_times=0;
		
		customer.sb_game="daw"+login_name;
		customer.sb_pwd="b123b123";
		customer.sb_flag=false;
		customer.sb_actived=true;
		
		customer.ag_game="daw"+login_name;
		customer.ag_pwd="b123b123";
		customer.ag_flag=false;
		customer.ag_actived=true;
		customer.credit=new BigDecimal(0);
		customer.bbin_game="daw"+login_name;
		customer.bbin_pwd="b123b123";
		customer.bbin_flag=false;
		customer.bbin_actived=true;
		
		customer.bbinmobile_game = "daw"+login_name+"@dj8";
		customer.bbinmobile_pwd = MyRandom.getRandom(8);
		customer.bbinmobile_flag = false;
		
		customer.pt_game=("daw"+customer.login_name).toUpperCase();
		customer.pt_pwd="c123c123";
		customer.pt_flag=false;
		customer.pt_actived=true;
		
		customer.kg_game=("daw"+customer.login_name);
		customer.kg_pwd="b123b123";
		customer.kg_flag=false;
		customer.kg_actived=true;
		
		customer.pp_game=("daw"+customer.login_name);
		customer.pp_pwd="b123b123";
		customer.pp_flag=false;
		customer.pp_actived=true;
		
		customer.mg_game=("daw"+customer.login_name);
		customer.mg_pwd="a123a123";
		customer.mg_flag=false;
		customer.mg_actived=true;
		
		customer.reg_domain=reg_domain;
		customer.refer_url=referdomain;
		customer.search_key=keyword;
		
		//从session里面获取推荐码
		String reg_code = session.get("reg_code");
		if(reg_code != null){
			Agent agent = Agent.getAgentByReg_code(reg_code);
			if(agent != null && agent.flag ==3){
				customer.parent_id = agent.agent_id;
			}
				
		}
		
		Customer cust=CustomerService.createCustomer(customer);
		String user_agent=request.headers.get("user-agent")==null?"":request.headers.get("user-agent").toString();
		if(cust!=null){
			Cache.set("user"+ session.getId(), customer);
			session.put("username", customer.login_name);
			MeiBoOpService.logCust(cust.cust_id, cust.login_name, 1, ip, user_agent, "客户注册并登录。");
	        StringBuffer sb=new StringBuffer("<p>尊敬的"+cust.real_name+"，您好！欢迎您加入八达国际娱乐城，八达国际致力为您提供优质的线上博彩服务，同时为了您的资金安全，我们温馨提示您：</p>");                                                                        
	        sb.append("<p>1、请您留意您的注册信息如姓名，手机号码，QQ号码是否真实，我们在您的第一笔提款的时候，会致电对您的注册信息进行确认，也请您保持电话通畅，如果您已绑定错误信息，可以随时联系我们客服申请修正，您也可以采用真实信息重新注册一个帐号。</p>");
	        sb.append("<p>2、请您再次确认您绑定的提款银行卡信息是否与您注册的真实姓名是否一致，以便我们能顺利为您支付您的款项。</p>");
	        sb.append("<p>3、我们提供24小时免费回拨热线，<a href=\"javascript:open800ChatBox();\"><span>在线客服（点击联系）</span></a>，QQ：800186517</p>");
	        sb.append("<p style=\"text-align:right\">八达国际客服部上</p>");
			Letter.NTcreat(cust.cust_id, cust.login_name, "欢迎加入八达国际娱乐城", sb.toString(), cust.login_name, true);
			render(cust,login_pwd);
		}
		String errorMsg="很遗憾，您的注册尚未成功，原因：注册过程出现错误，请联系网站在线客服为您注册。";
		renderTemplate("/MeiBo/reg.html", errorMsg,login_name,real_name,phone,qq);
	}
	
	
	public static void login(){
		
		
	}
	
	/*
	 * 
	 */

	public static void mgalias(){
		String login_name=session.get("username");
		String alias=params.get("alias");
		
		String errorMsg="很遗憾，您注册的别名尚未成功。";
		if(alias==null){
			errorMsg=errorMsg+"请输入您想注册的别名!";
			flash.put("errorMsg", errorMsg);
			render("/MeiBoPhp/mgalias.html",errorMsg);
		}
		if(alias.length()<4 || alias.length()>19){
			errorMsg=errorMsg+"您输入的别名长度不符合规则!";
			flash.put("errorMsg", errorMsg);
			render("/MeiBoPhp/mgalias.html",errorMsg);
		}
		
		
		boolean success = PlatService.registerAlias(Plat.MG, login_name, alias);
		if(success){
			errorMsg = "注册MG别名成功";
			render("/MeiBoPhp/mgalias.html",errorMsg);
		}else{
			errorMsg = errorMsg+"请您尝试其他别名";
			render("/MeiBoPhp/mgalias.html",errorMsg);
		}
		
	}
	
	
	public static void password(){
		String login_name=session.get("username");
		String oldpwd=params.get("login_pwd");
		String newpwd=params.get("login_newpwd");
		String repwd=params.get("login_repwd");
		String errorMsg="很遗憾，您修改的密码尚未成功。原因如下：";
		if(oldpwd==null){
			errorMsg=errorMsg+"请输入您的当前密码!";
			flash.put("errorMsg", errorMsg);
			render("/MeiBoPhp/password.html",errorMsg);
		}
		if(newpwd==null||!newpwd.matches("[.A-Za-z0-9]{6,16}")){
			errorMsg=errorMsg+"您输入的密码不符合规则!";
			flash.put("errorMsg", errorMsg);
			render("/MeiBoPhp/password.html",errorMsg);
		}
		if(repwd==null){
			errorMsg=errorMsg+"请重复输入您的新密码!";
			flash.put("errorMsg", errorMsg);
			render("/MeiBoPhp/password.html",errorMsg);
		}
		if(!repwd.equals(newpwd)){
			errorMsg=errorMsg+"两次输入的密码不一致!";
			flash.put("errorMsg", errorMsg);
			render("/MeiBoPhp/password.html",errorMsg);
		}
		if(!CustomerService.modfiPwd(login_name, oldpwd, newpwd)){
			errorMsg=errorMsg+"当前密码不匹配!";
			flash.put("errorMsg", errorMsg);
			render("/MeiBoPhp/password.html",errorMsg);
		}
		Customer customer=Customer.getCustomer(login_name);
		String ip=IPApp.getIpAddr();
		String user_agent=request.headers.get("user-agent")==null?"":request.headers.get("user-agent").toString();
		MeiBoOpService.logCust(customer.cust_id, customer.login_name, 1, ip, user_agent, "客户修改密码。");
		render();
	}
	
	public static void password_pt(){
		String login_name=session.get("username");
		String pt_pwd=params.get("pt_pwd");
		String errorMsg="很遗憾，您修改的密码尚未成功。原因如下：";
		if(pt_pwd==null){
			errorMsg=errorMsg+"请输入密码!";
			flash.put("errorMsg", errorMsg);
			render("/MeiBoPhp/password.html",errorMsg);
		}
		if(pt_pwd.length()!=8){
			errorMsg=errorMsg+"PT密码由8位数字或者字母组合组成。";
			flash.put("errorMsg", errorMsg);
			render("/MeiBoPhp/password.html",errorMsg);
		}
		Customer customer=Customer.getCustomer(login_name);
		String ip=IPApp.getIpAddr();
		boolean flag=PlatService.pwd(Plat.PT, login_name, ip, pt_pwd);
		String user_agent=request.headers.get("user-agent")==null?"":request.headers.get("user-agent").toString();
		MeiBoOpService.logCust(customer.cust_id, customer.login_name, 1, ip, user_agent, "客户修改PT客户端密码。"+(flag?"成功":"失败"));
		if(flag){
			Customer.updatePTGamePwd(customer.cust_id, pt_pwd);
			render();
		}else{
			errorMsg=errorMsg+"修改失败，请联系客服处理。";
			flash.put("errorMsg", errorMsg);
			render("/MeiBoPhp/password.html",errorMsg);
		}
	}
	
	public static void password_mg(){
		String login_name=session.get("username");
		String mg_pwd=params.get("mg_pwd");
		String errorMsg="很遗憾，您修改的密码尚未成功。原因如下：";
		if(mg_pwd==null){
			errorMsg=errorMsg+"请输入密码!";
			flash.put("errorMsg", errorMsg);
			render("/MeiBoPhp/password.html",errorMsg);
		}
		if(mg_pwd.length()!=8){
			errorMsg=errorMsg+"MG密码由8位数字或者字母组合组成。";
			flash.put("errorMsg", errorMsg);
			render("/MeiBoPhp/password.html",errorMsg);
		}
		Customer customer=Customer.getCustomer(login_name);
		String ip=IPApp.getIpAddr();
		boolean flag=PlatService.pwd(Plat.MG, customer.login_name, ip, mg_pwd);
		String user_agent=request.headers.get("user-agent")==null?"":request.headers.get("user-agent").toString();
		MeiBoOpService.logCust(customer.cust_id, customer.login_name, 1, ip, user_agent, "客户修改MG客户端密码。"+(flag?"成功":"失败"));
		if(flag){
			Customer.updateMGGamePwd(customer.cust_id, mg_pwd);
			render();
		}else{
			errorMsg=errorMsg+"修改失败，请联系客服处理。";
			flash.put("errorMsg", errorMsg);
			render("/MeiBoPhp/password.html",errorMsg);
		}
	}
	
	public static void password_bbin(){
		String login_name=session.get("username");
		String bbin_pwd=params.get("bbin_pwd");
		String errorMsg="很遗憾，您修改的密码尚未成功。原因如下：";
		if(bbin_pwd==null){
			errorMsg=errorMsg+"请输入密码!";
			flash.put("errorMsg", errorMsg);
			render("/MeiBoPhp/password.html",errorMsg);
		}
		if(bbin_pwd.length()!=8){
			errorMsg=errorMsg+"BBIN密码由8位数字或者字母组合组成。";
			flash.put("errorMsg", errorMsg);
			render("/MeiBoPhp/password.html",errorMsg);
		}
		Customer customer=Customer.getCustomer(login_name);
		String ip=IPApp.getIpAddr();
		boolean flag=PlatService.pwd(Plat.BBIN, customer.login_name, ip, bbin_pwd);
		String user_agent=request.headers.get("user-agent")==null?"":request.headers.get("user-agent").toString();
		MeiBoOpService.logCust(customer.cust_id, customer.login_name, 1, ip, user_agent, "客户修改BBIN手机端密码。"+(flag?"成功":"失败"));
		if(flag){
			Customer.updateBBINMobileGamePwd(customer.cust_id, bbin_pwd);
			render();
		}else{
			errorMsg=errorMsg+"修改失败，请联系客服处理。";
			flash.put("errorMsg", errorMsg);
			render("/MeiBoPhp/password.html",errorMsg);
		}
	}
	
	
	/**
	 * 
	 */
	public static void bank(){
		String login_name=session.get("username");
		String bank_name=params.get("bank_name");
		String account=params.get("account");
		String account_type=params.get("account_type");
		String bank_dot=params.get("bank_dot");
		String errorMsg="很遗憾，您提交的银行资料尚未修改成功。原因如下：";
		if(bank_name==null||bank_name.trim().equals("")||bank_name.length()>20){
			errorMsg=errorMsg+"您没有选择银行或者暂不支持该银行名称。";
			render("/MeiBoPhp/bank.html",errorMsg,bank_name,account,account_type,bank_dot);
		}
		if(account==null||account.trim().equals("")||account.length()>30||account.length()<8){
			errorMsg=errorMsg+"您没有填写银行帐号或者您的银行帐号超过30字符。";
			render("/MeiBoPhp/bank.html",errorMsg,bank_name,account,account_type,bank_dot);
		}
		if(account_type==null||account_type.trim().equals("")||account_type.length()>30||account_type.length()<1){
			errorMsg=errorMsg+"您没有选择账户类型或者您的账户类型超过30字符。";
			render("/MeiBoPhp/bank.html",errorMsg,bank_name,account,account_type,bank_dot);
		}
		if(account_type==null||account_type.trim().equals("")||account_type.length()>30||account_type.length()<1){
			errorMsg=errorMsg+"您没有开户网点或者您的账户类型超过50字符。";
			render("/MeiBoPhp/bank.html",errorMsg,bank_name,account,account_type,bank_dot);
		}
		if(CustomerService.modfiBank(login_name, bank_name, account, bank_dot, account_type)){
			Customer customer=Customer.getCustomer(login_name);
			String ip=IPApp.getIpAddr();
			String user_agent=request.headers.get("user-agent")==null?"":request.headers.get("user-agent").toString();
			MeiBoOpService.logCust(customer.cust_id, customer.login_name, 1, ip, user_agent, "客户完善银行资料。");
			MeiBoPhp.bank();
		}
		errorMsg=errorMsg+"修改失败。";
		render("/MeiBoPhp/bank.html",errorMsg,bank_name,account,account_type,bank_dot);
	}
	
	private static void depositK(BigDecimal amount,String bank_name,String account_name,String deposit_type,
			String deposit_date,String location,String remarks,String template){
		if(amount==null){
			amount=new BigDecimal(0);
		}
		String login_name=session.get("username");
		
		Date now =new Date(System.currentTimeMillis());
		BigDecimal poundage = new BigDecimal(0);
		
		if(now.getTime()>DateUtil.stringToDate("2017-12-01", "yyyy-MM-dd").getTime() &&
	    		  DateUtil.stringToDate("2018-01-01", "yyyy-MM-dd").getTime()>now.getTime()){
			 if(amount.intValue()<5000){
				 poundage=new BigDecimal(amount.intValue()/100.0*1);
			 }else if(amount.intValue() >= 5000 && amount.intValue()<10000){
				 poundage=new BigDecimal(amount.intValue()/100.0*1.8);
			 }else if(amount.intValue() >= 10000 && amount.intValue()<30000){
				 poundage=new BigDecimal(amount.intValue()/100.0*2.5);
			 }else if(amount.intValue() >= 30000 && amount.intValue()<50000){
				 poundage=new BigDecimal(amount.intValue()/100.0*3.8);
			 }else if(amount.intValue() >= 50000 ){
				 poundage=new BigDecimal(amount.intValue()/100.0*5);
			 }
		}else{
			 poundage=new BigDecimal(amount.intValue()/100.0);
		}
		
		
		if(poundage.intValue()>2888)poundage=new BigDecimal(2888);
		String errorMsg="很遗憾，您提交的存款尚未提交成功。原因如下：";
		if(amount==null||amount.intValue()>1000000){
			errorMsg=errorMsg+"您没有填写您存款的金额或者存款金额大于100万。";
			render(""+template,errorMsg,amount,bank_name,account_name,deposit_type,deposit_date,location,remarks);
		}
		 if(amount.intValue()==0){
			 errorMsg=errorMsg+"存款金额必须大于0。";
				render(""+template,errorMsg,amount,bank_name,account_name,deposit_type,deposit_date,location,remarks);
		 }
		 
		 if(amount.intValue()<100){
				errorMsg=errorMsg+"您的存款金额必须大于100元。";
				render(""+template,errorMsg,amount,bank_name,account_name,deposit_type,deposit_date,location,remarks);
		 }
		 
		 if(bank_name==null){
			 errorMsg=errorMsg+"请您选择存款银行。";
		     render(""+template,errorMsg,amount,bank_name,account_name,deposit_type,deposit_date,location,remarks);
			 
		 }
		 if(bank_name.length()>30){
			 errorMsg=errorMsg+"存款银行数据太长，非法数据，请重新选择。";
		     render(""+template,errorMsg,amount,bank_name,account_name,deposit_type,deposit_date,location,remarks);
		 }
		 if(account_name==null||account_name.length()>30){
			 errorMsg=errorMsg+"请填写正确的收款人。";
		     render(""+template,errorMsg,amount,bank_name,account_name,deposit_type,deposit_date,location,remarks);
		 }
		 
		 if(deposit_type==null||deposit_type.length()>30){
			 errorMsg=errorMsg+"请选择正确的存款方式。";
		     render(""+template,errorMsg,amount,bank_name,account_name,deposit_type,deposit_date,location,remarks);
		 }

		 if(location==null||location.length()>40){
			 errorMsg=errorMsg+"请选择正确的存款地点。";
		     render(""+template,errorMsg,amount,bank_name,account_name,deposit_type,deposit_date,location,remarks);
		 }
		 
		 if(remarks==null||remarks.length()>100){
			 errorMsg=errorMsg+"您的备注过长。";
		     render(""+template,errorMsg,amount,bank_name,account_name,deposit_type,deposit_date,location,remarks);
		 }
		
		 Customer cust=Customer.getCustomer(login_name);
		 if(Deposit.NTgetNoDoCnt(cust.cust_id)>0){
			 errorMsg=errorMsg+"您存在尚未处理的存款，请处理完毕后再提交。";
		     render(""+template,errorMsg,amount,bank_name,account_name,deposit_type,deposit_date,location,remarks);
		 }
		 
		 Deposit deposit =new Deposit();
		 deposit.deposit_date=new Date(DateUtil.stringToDate(deposit_date, "yyyy-MM-dd HH:mm:ss").getTime()); 
		 deposit.cust_id=cust.cust_id;
		 deposit.pdage_status=1;
		 deposit.status=1;
		 deposit.create_user=cust.login_name;
		 deposit.create_date=new Date(System.currentTimeMillis());
		 deposit.ip=IPApp.getIpAddr();
		 deposit.amount=amount;
		 deposit.poundage=poundage;
		 deposit.bank_name=bank_name;
		 deposit.account_name=account_name;
		 deposit.deposit_type=deposit_type;
		 deposit.location=location;
		 deposit.remarks=remarks;
		 deposit.login_name=cust.login_name;
		 deposit.real_name=cust.real_name;
		 deposit.dep_no=OrderNo.createLocalNo("DE");
		 Long dep_id=deposit.NTcreat();
		 if(dep_id!=null){
			 
			 DepositLog log =new DepositLog();
			 log.after_status=1;
			 log.create_user=cust.login_name;
			 log.deposit_id=dep_id;
			 log.pre_status=0;
			 log.remarks=deposit.remarks;
			 log.NTcreat();
			  Msg.NTcreateMsg(4, "客户"+cust.real_name+"存款"+deposit.amount+"元，请及时处理!");
		 }
		
		
	}
	public static void deposit(BigDecimal amount,String bank_name,String account_name,String deposit_type,
			String deposit_date,String location,String remarks){
	
		 depositK(amount,bank_name,account_name,deposit_type,deposit_date,location,remarks,"/MeiBoPhp/deposit_form.html");
	     render();
	}
	public static void deposit_alipay(BigDecimal amount,String bank_name,String account_name,String deposit_type,
			String deposit_date,String location,String remarks){
		 depositK(amount,bank_name,account_name,deposit_type,deposit_date,location,remarks,"/MeiBoPhp/deposit_alipay_form.html");
	     render();
	}
	public static void withdraw(BigDecimal amount,String login_pwd,String remarks,String bank_name,
			String account,String account_type,String bank_dot){
		String login_name=session.get("username");
		Customer cust=Customer.getCustomer(login_name);
		String ip=IPApp.getIpAddr();
		boolean showBank=false;
		if(cust.account==null||cust.account.length()<7){
			showBank=true;
		}
		String errorMsg="很遗憾，您提交的提款尚未提交成功。原因如下：";
		if(amount==null||amount.intValue()>1000000){
			errorMsg=errorMsg+"您没有填写您提款的金额或者提款金额大于100万。";
			render("/MeiBoPhp/withdraw.html",errorMsg,amount,remarks,bank_name,account,account_type,bank_dot,showBank);
		}
		if(amount.intValue()<100){
			errorMsg=errorMsg+"最低提款为100元。";
			render("/MeiBoPhp/withdraw.html",errorMsg,amount,remarks,bank_name,account,account_type,bank_dot,showBank);
		}
		if(cust.account==null||cust.account.length()<6){
			if(bank_name==null||bank_name.equals("")||bank_name.length()>30){
				errorMsg=errorMsg+"您选择银行不正常，请重新选择。";
				render("/MeiBoPhp/withdraw.html",errorMsg,amount,remarks,bank_name,account,account_type,bank_dot,showBank);
			}
			if(account==null||account.equals("")||account.length()>30){
				errorMsg=errorMsg+"您填写的银行帐号不是真实帐号，请重新填写。";
				render("/MeiBoPhp/withdraw.html",errorMsg,amount,remarks,bank_name,account,account_type,bank_dot,showBank);
			}
			if(account_type==null||account_type.equals("")||account_type.length()>30){
				errorMsg=errorMsg+"您选择的账户类型不正常，请重新选择。";
				render("/MeiBoPhp/withdraw.html",errorMsg,amount,remarks,bank_name,account,account_type,bank_dot,showBank);
			}
			if(bank_dot==null||bank_dot.equals("")||bank_dot.length()>50){
				errorMsg=errorMsg+"您填写的开户网点不正常，请重新填写。";
				render("/MeiBoPhp/withdraw.html",errorMsg,amount,remarks,bank_name,account,account_type,bank_dot,showBank);
			}
		}
		login_pwd=MD5.md5(login_pwd);
		if(!cust.login_pwd.equals(login_pwd)){
			errorMsg=errorMsg+"您输入的密码错误！。";
			render("/MeiBoPhp/withdraw.html",errorMsg,amount,remarks,bank_name,account,account_type,bank_dot,showBank);
		} 
		if(Withdraw.NTgetNoDoCount(cust.cust_id)>0){
			errorMsg=errorMsg+"您存在尚未处理的提款请求，请等待该提款处理完毕再提交。";
			render("/MeiBoPhp/withdraw.html",errorMsg,amount,remarks,bank_name,account,account_type,bank_dot,showBank);
		}
		int wit_cnt=Withdraw.NTgetCount(cust.cust_id);
		if(cust.credit==null)cust.credit=new BigDecimal(0);
		String order_no=""+System.currentTimeMillis();
		PlatService.transferOutAll(login_name, ip, "customer withdraw", remarks, login_name, order_no, null);
		cust=Customer.getCustomer(login_name);
		if(cust.credit.intValue()<amount.intValue()){
			errorMsg=errorMsg+"您的额度不足，无法完成您的提款请求。";
			render("/MeiBoPhp/withdraw.html",errorMsg,amount,remarks,bank_name,account,account_type,bank_dot,showBank);
		}
		Withdraw withdraw =new Withdraw();
		withdraw.wit_no=OrderNo.createLocalNo("WI");
		withdraw.real_name=cust.real_name;
		withdraw.account_name=cust.real_name;
		if(cust.account==null||cust.account.length()<6){
			withdraw.account=account;
			withdraw.account_type=account_type;
			withdraw.bank_name=bank_name;
			withdraw.location=bank_dot;
		}else{
			withdraw.account=cust.account;
			withdraw.account_type=cust.account_type;
			withdraw.bank_name=cust.bank_name;
			withdraw.location=cust.bank_dot;
		}
		withdraw.amount=amount;
		withdraw.remarks=remarks;
		if(cust.credit.intValue()<amount.intValue()){
			errorMsg=errorMsg+"您的额度不足，无法完成您的提款请求。";
			render("/MeiBoPhp/withdraw.html",errorMsg,amount,remarks,bank_name,account,account_type,bank_dot,showBank);
		}
		withdraw.status=1;
		withdraw.cust_id=cust.cust_id;
		withdraw.login_name=cust.login_name;
		withdraw.create_user=cust.login_name;
		withdraw.wit_cnt=wit_cnt;
		if(CustomerService.modCredit(cust.cust_id,CreditLogService.Withdraw, login_name,
				new BigDecimal(0).subtract(amount),withdraw.login_name,"客户提款", withdraw.wit_no)){
			try{
				Long withdraw_id=withdraw.NTcreat();
				WithdrawLog.NTcreat(0, 1, withdraw_id, withdraw.remarks, cust.login_name);
				Msg.NTcreateMsg(5, "客户"+cust.real_name+"提款"+withdraw.amount+"元，请及时处理!");
			}catch(Exception e){
				System.out.println("创建提款提案失败!");
			}
			render();
		}
		errorMsg=errorMsg+"您的额度不足，无法完成您的提款请求。";
		render("/MeiBoPhp/withdraw.html",errorMsg,amount,remarks,bank_name,account,account_type,bank_dot,showBank);
		
	}
	
	public static void getGift(Integer level){
		String login_name=session.get("username");
		Customer cust=Customer.getCustomer(login_name);
		if(level == null){
			level = 0;
		}
		int count = HuoliGift.getLevelCount(login_name, level);
		String uuid =OrderNo.createLocalNo("HL");
		String content = "";
		BigDecimal c = new BigDecimal(0);
		BigDecimal score = new BigDecimal(0);
		if(level == 0){
			
		}else if(level == 1){
			content="客户领取活力值1000红包";
			c= new BigDecimal(8);
			score = new BigDecimal(5);
			BigDecimal deposit = Deposit.NTgetSumNewYear(cust.cust_id);
			if(deposit == null || deposit.intValue()<1000){
				render();
			}
		}else if(level == 2){
			content="客户领取活力值5000红包";
			c= new BigDecimal(28);
			score = new BigDecimal(10);
			BigDecimal deposit = Deposit.NTgetSumNewYear(cust.cust_id);
			if(deposit == null || deposit.intValue()<5000){
				render();
			}
		}else if(level == 3){
			content="客户领取活力值10000红包";
			c= new BigDecimal(58);
			score = new BigDecimal(20);
			BigDecimal deposit = Deposit.NTgetSumNewYear(cust.cust_id);
			if(deposit == null || deposit.intValue()<10000){
				render();
			}
		}else if(level == 4){
			content="客户领取活力值50000红包";
			c= new BigDecimal(88);
			score = new BigDecimal(30);
			BigDecimal deposit = Deposit.NTgetSumNewYear(cust.cust_id);
			if(deposit == null || deposit.intValue()<50000){
				render();
			}
		}else if(level == 5){
			content="客户领取活力值100000红包";
			c= new BigDecimal(188);
			score = new BigDecimal(50);
			BigDecimal deposit = Deposit.NTgetSumNewYear(cust.cust_id);
			if(deposit == null || deposit.intValue()<100000){
				render();
			}
		}else if(level == 6){
			content="客户领取活力值300000红包";
			c= new BigDecimal(288);
			score = new BigDecimal(80);
			BigDecimal deposit = Deposit.NTgetSumNewYear(cust.cust_id);
			if(deposit == null || deposit.intValue()<300000){
				render();
			}
		}else if(level == 7){
			content="客户领取活力值600000红包";
			c= new BigDecimal(518);
			score = new BigDecimal(100);;
			BigDecimal deposit = Deposit.NTgetSumNewYear(cust.cust_id);
			if(deposit == null || deposit.intValue()<600000){
				render();
			}
		}else if(level == 8 ){
			content="客户领取活力值1000000红包";
			c= new BigDecimal(888);
			score = new BigDecimal(180);;
			BigDecimal deposit = Deposit.NTgetSumNewYear(cust.cust_id);
			if(deposit == null || deposit.intValue()<1000000){
				render();
			}
		}
		if(count==0 && c!=new BigDecimal(0) && level!=0 && !"".equals(content)){
			Long gift_id= HuoliGift.create(cust.cust_id, login_name, content, uuid, 0, level);
			boolean b=CustomerService.modCredit(cust.cust_id,CreditLogService.Gift,cust.login_name, c,login_name,"活力礼包:"+uuid, uuid);
			
			int flag=2;
			if(b)flag=1;
			HuoliGift.finishedGift(gift_id, flag);
			if(b){
				CustomerService.modScore(uuid, "活力礼包积分", score, login_name, cust.cust_id, login_name);
			}
		}
		render();
	}
	
	/**
	 * 智付支付
	 * @param momey
	 * @param bank
	 */
	
	
	public static void deposit_onlinepay(String tongdao){
		String result_url="/MeiBoPhp/choose_onlinepay.html";
		String login_name=session.get("username");
		Customer cust=Customer.getCustomer(login_name);
		String errorMsg="很抱歉，您的在线充值未提交成功，原因是：";
		
		
		
		if(tongdao==null||"".equals(tongdao)){
			errorMsg=errorMsg+"您没有选择通道。";
			render(""+result_url,tongdao,errorMsg);
		}
		
		if(!cust.online_pay){
			errorMsg=errorMsg+"订单出现异常，请联系客服处理！";
			render(""+result_url,tongdao,errorMsg);
		}
		
		if(tongdao.equals("1")){
			MeiBoPhp.choose_hbpay_bank();
		}else if(tongdao.equals("2")){
			MeiBoPhp.choose_huitongpay_bank();
		}else if(tongdao.equals("3")){
			MeiBoPhp.choose_jinyangwangyin();
		}else if(tongdao.equals("4")){
			MeiBoPhp.choose_ytbpay_bank();
		}else if(tongdao.equals("5")){
			MeiBoPhp.choose_jianyuewangyin();
		}
		
	}
	public static void online01(Integer momey,String bank){
		String result_url="/MeiBoPhp/deposit_zx01.html";
		String login_name=session.get("username");
		Customer cust=Customer.getCustomer(login_name);
		String errorMsg="很抱歉，您的在线充值未提交成功，原因是：";
		boolean payOn01=MeiBoService.getOnlinePay01();
		if(!payOn01){
			errorMsg=errorMsg+"该支付通道正在维护中....，请选择其他支付通道。";
			render(""+result_url,momey,errorMsg);
		}
		if(momey==null||momey.intValue()==0){
			errorMsg=errorMsg+"您的存款金额为0元";
			render(""+result_url,momey,errorMsg);
		}
		if(momey.intValue()<100){
			errorMsg=errorMsg+"您的存款金额必须大于100元。";
			render(""+result_url,momey,errorMsg);
		}
		if(momey.intValue()>50000){
			errorMsg=errorMsg+"您的存款金额大于5万元。单笔存款必须不大于5万元。";
			render(""+result_url,momey,errorMsg);
		}
		if(bank==null||"".equals(bank)){
			errorMsg=errorMsg+"您没有选择银行。";
			render(""+result_url,momey,errorMsg);
		}
		if(!cust.online_pay){
			errorMsg=errorMsg+"订单出现异常，请联系客服处理！";
			render(""+result_url,momey,errorMsg);
		}
		String bank_name=PayBank.getBankName(bank);
		if(bank_name.equals("")){
			errorMsg=errorMsg+"您选择的银行，目前暂不支持在线支付。";
			render(""+result_url,momey,errorMsg);
		}
		try{
			  DinpayBean dinpay=DinpayRandom.get().random();
		      DigestUtils.md5Hex("".getBytes("UTF-8"));
		      String merchant_code=dinpay.merId;
		      String bank_code=bank;
		      String order_no=OrderNo.createLocalNo("YF");
		      String order_amount=momey.intValue()+"";
		      String service_type="direct_pay";
		      String input_charset="UTF-8";
		      String notify_url=dinpay.notify_url;
		      String interface_version="V3.0";
		      String sign_type="MD5";
		      String order_time=DateUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss");
		      String product_name="Computer";
		      String client_ip=IPApp.getIpAddr();
		      String extend_param="";
		      String extra_return_param="";
		      String product_code="";
		      String product_desc="";
		      String product_num="";
		      String return_url="";
		      String show_url="";
		      StringBuffer signSrc= new StringBuffer();
		  	//组织订单信息
		  	if(!"".equals(bank_code)) {
		  		signSrc.append("bank_code=").append(bank_code).append("&");
		  	}
		  	if(!"".equals(client_ip)) {
		  		signSrc.append("client_ip=").append(client_ip).append("&");
		  	}
		  	if(!"".equals(extend_param)) {
		  		signSrc.append("extend_param=").append(extend_param).append("&");
		  	}
		  	if(!"".equals(extra_return_param)) {
		  		signSrc.append("extra_return_param=").append(extra_return_param).append("&");
		  	}
		  	if (!"".equals(input_charset)) {
		  		signSrc.append("input_charset=").append(input_charset).append("&");
		  	}
		  	if (!"".equals(interface_version)) {
		  		signSrc.append("interface_version=").append(interface_version).append("&");
		  	}
		  	if (!"".equals(merchant_code)) {
		  		signSrc.append("merchant_code=").append(merchant_code).append("&");
		  	}
		  	if(!"".equals(notify_url)) {
		  		signSrc.append("notify_url=").append(notify_url).append("&");
		  	}
		  	if(!"".equals(order_amount)) {
		  		signSrc.append("order_amount=").append(order_amount).append("&");
		  	}
		  	if(!"".equals(order_no)) {
		  		signSrc.append("order_no=").append(order_no).append("&");
		  	}
		  	if(!"".equals(order_time)) {
		  		signSrc.append("order_time=").append(order_time).append("&");
		  	}
		  	if(!"".equals(product_code)) {
		  		signSrc.append("product_code=").append(product_code).append("&");
		  	}
		  	if(!"".equals(product_desc)) {
		  		signSrc.append("product_desc=").append(product_desc).append("&");
		  	}
		  	if(!"".equals(product_name)) {
		  		signSrc.append("product_name=").append(product_name).append("&");
		  	}
		  	if(!"".equals(product_num)) {
		  		signSrc.append("product_num=").append(product_num).append("&");
		  	}
		  	if(!"".equals(return_url)) {
		  		signSrc.append("return_url=").append(return_url).append("&");
		  	}
		  	if(!"".equals(service_type)) {
		  		signSrc.append("service_type=").append(service_type).append("&");
		  	}
		  	if(!"".equals(show_url)) {
		  		signSrc.append("show_url=").append(show_url).append("&");
		  	}
		  	String key = dinpay.md5key; // <支付密钥> 注:此处密钥必须与商家后台里的密钥一致
		  	signSrc.append("key=").append(key);
		  	String singInfo =signSrc.toString();
		  	String sign = DigestUtils.md5Hex(singInfo.getBytes("UTF-8"));
		  	
		  	if(Dinpay.NTcreat(cust.cust_id, cust.login_name,
		  			merchant_code, new BigDecimal(order_amount), order_time, client_ip, product_name, bank_code,
		  			bank_name,  interface_version, sign_type, sign, 0,order_no)){
		    render(sign,merchant_code,bank_code,order_no,order_amount,
		    		service_type,input_charset,notify_url,interface_version,
		    		sign_type,order_time,product_name,
		    		client_ip,extend_param,extra_return_param,
		    		product_code,product_desc,product_num,
		    		return_url,show_url,bank_name,cust,dinpay);
		  	}
		}catch(Exception e){
			e.printStackTrace();
			errorMsg=errorMsg+"订单出现异常。";
			render(""+result_url,momey,errorMsg);
		}
	}
	
	
	/**
	 * 智付点卡支付
	 * @param momey
	 * @param bank
	 */
	public static void onlinePoint(Integer momey,String bank){
		String result_url="/MeiBoPhp/deposit_point.html";
		String login_name=session.get("username");
		Customer cust=Customer.getCustomer(login_name);
		String errorMsg="很抱歉，您的在线充值未提交成功，原因是：";
		boolean payPoint=MeiBoService.getPointPay();
		if(!payPoint){
			errorMsg=errorMsg+"该支付通道正在维护中....，请选择其他支付通道。";
			render(""+result_url,momey,errorMsg);
		}
		if(momey==null||momey.intValue()==0){
			errorMsg=errorMsg+"您的存款金额为0元";
			render(""+result_url,momey,errorMsg);
		}
		if(momey.intValue()<100){
			errorMsg=errorMsg+"您的存款金额必须大于100元。";
			render(""+result_url,momey,errorMsg);
		}
		if(momey.intValue()>500){
			errorMsg=errorMsg+"您的存款金额大于500元。单笔存款必须不大于500元。";
			render(""+result_url,momey,errorMsg);
		}
		if(bank==null||"".equals(bank)){
			errorMsg=errorMsg+"您没有选择点卡类型。";
			render(""+result_url,momey,errorMsg);
		}
		if(!cust.online_pay){
			errorMsg=errorMsg+"订单出现异常，请联系客服处理！";
			render(""+result_url,momey,errorMsg);
		}
		String pointcard_name=PayBank.getPointCardName(bank);
		if(pointcard_name.equals("")){
			errorMsg=errorMsg+"您选择的点卡，目前暂不支持在线支付。";
			render(""+result_url,momey,errorMsg);
		}
		try{
		      DigestUtils.md5Hex("".getBytes("UTF-8"));
		      String merchant_code=Play.configuration.getProperty("dinpay.merId");
		      String bank_code=bank;
		      String order_no=OrderNo.createLocalNo("YF");
		      String order_amount=momey.intValue()+"";
		      String service_type="direct_pay";
		      String input_charset="UTF-8";
		      String notify_url=Play.configuration.getProperty("dinpay.notifyUrl");
		      String interface_version="V3.0";
		      String sign_type="MD5";
		      String order_time=DateUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss");
		      String product_name="Computer";
		      String client_ip=IPApp.getIpAddr();
		      String extend_param="";
		      String extra_return_param="";
		      String product_code="";
		      String product_desc="";
		      String product_num="";
		      String return_url="";
		      String show_url="";
		      StringBuffer signSrc= new StringBuffer();
		  	//组织订单信息
		  	if(!"".equals(bank_code)) {
		  		signSrc.append("bank_code=").append(bank_code).append("&");
		  	}
		  	if(!"".equals(client_ip)) {
		  		signSrc.append("client_ip=").append(client_ip).append("&");
		  	}
		  	if(!"".equals(extend_param)) {
		  		signSrc.append("extend_param=").append(extend_param).append("&");
		  	}
		  	if(!"".equals(extra_return_param)) {
		  		signSrc.append("extra_return_param=").append(extra_return_param).append("&");
		  	}
		  	if (!"".equals(input_charset)) {
		  		signSrc.append("input_charset=").append(input_charset).append("&");
		  	}
		  	if (!"".equals(interface_version)) {
		  		signSrc.append("interface_version=").append(interface_version).append("&");
		  	}
		  	if (!"".equals(merchant_code)) {
		  		signSrc.append("merchant_code=").append(merchant_code).append("&");
		  	}
		  	if(!"".equals(notify_url)) {
		  		signSrc.append("notify_url=").append(notify_url).append("&");
		  	}
		  	if(!"".equals(order_amount)) {
		  		signSrc.append("order_amount=").append(order_amount).append("&");
		  	}
		  	if(!"".equals(order_no)) {
		  		signSrc.append("order_no=").append(order_no).append("&");
		  	}
		  	if(!"".equals(order_time)) {
		  		signSrc.append("order_time=").append(order_time).append("&");
		  	}
		  	if(!"".equals(product_code)) {
		  		signSrc.append("product_code=").append(product_code).append("&");
		  	}
		  	if(!"".equals(product_desc)) {
		  		signSrc.append("product_desc=").append(product_desc).append("&");
		  	}
		  	if(!"".equals(product_name)) {
		  		signSrc.append("product_name=").append(product_name).append("&");
		  	}
		  	if(!"".equals(product_num)) {
		  		signSrc.append("product_num=").append(product_num).append("&");
		  	}
		  	if(!"".equals(return_url)) {
		  		signSrc.append("return_url=").append(return_url).append("&");
		  	}
		  	if(!"".equals(service_type)) {
		  		signSrc.append("service_type=").append(service_type).append("&");
		  	}
		  	if(!"".equals(show_url)) {
		  		signSrc.append("show_url=").append(show_url).append("&");
		  	}
		  	String key = Play.configuration.getProperty("dinpay.md5key");; // <支付密钥> 注:此处密钥必须与商家后台里的密钥一致
		  	signSrc.append("key=").append(key);
		  	String singInfo =signSrc.toString();
		  	String sign = DigestUtils.md5Hex(singInfo.getBytes("UTF-8"));
		  	
		  	if(Dinpay.NTcreat(cust.cust_id, cust.login_name,
		  			merchant_code, new BigDecimal(order_amount), order_time, client_ip, product_name, bank_code,
		  			pointcard_name,  interface_version, sign_type, sign, 0,order_no)){
		    render(sign,merchant_code,bank_code,order_no,order_amount,
		    		service_type,input_charset,notify_url,interface_version,
		    		sign_type,order_time,product_name,
		    		client_ip,extend_param,extra_return_param,
		    		product_code,product_desc,product_num,
		    		return_url,show_url,pointcard_name,cust);
		  	}
		}catch(Exception e){
			e.printStackTrace();
			errorMsg=errorMsg+"订单出现异常。";
			render(""+result_url,momey,errorMsg);
		}
	}
	
	/**
	 * 汇潮支付
	 *   
	<input type="hidden" name="MerNo" value="<? echo $MerNo?>">
    <input type="hidden" name="BillNo" value="<? echo $BillNo?>">
    <input type="hidden" name="Amount" value="<? echo $Amount?>">
    <input type="hidden" name="ReturnURL" value="<? echo $ReturnURL?>" >
    <input type="hidden" name="AdviceURL" value="<? echo $AdviceURL?>" >
    <input type="hidden" name="orderTime" value="<? echo $orderTime?>">
    <input type="hidden" name="defaultBankNumber" value="<? echo $defaultBankNumber?>">
    <input type="hidden" name="SignInfo" value="<? echo $SignInfo?>">
    <input type="hidden" name="Remark" value="<? echo $Remark?>">
    <input type="hidden" name="products" value="<? echo $products?>">
	 * @param momey
	 * @param bank
	 */
	public static void online02(Integer momey,String bank){
		String result_url="/MeiBoPhp/deposit_zx02.html";
		String login_name=session.get("username");
		Customer cust=Customer.getCustomer(login_name);
		String errorMsg="很抱歉，您的在线充值未提交成功，原因是：";
		boolean payOn02=MeiBoService.getOnlinePay02();
		if(!payOn02){
			errorMsg=errorMsg+"该支付通道正在维护中....，请选择其他支付通道。";
			render(""+result_url,momey,errorMsg);
		}
		if(momey==null||momey.intValue()==0){
			errorMsg=errorMsg+"您的存款金额为0元";
			render(""+result_url,momey,errorMsg);
		}
		if(momey.intValue()<100){
			errorMsg=errorMsg+"您的存款金额必须大于100元。";
			render(""+result_url,momey,errorMsg);
		}
		if(momey.intValue()>50000){
			errorMsg=errorMsg+"您的存款金额大于5万元。单笔存款必须不大于5万元。";
			render(""+result_url,momey,errorMsg);
		}
		if(bank==null||"".equals(bank)){
			errorMsg=errorMsg+"您没有选择银行。";
			render(""+result_url,momey,errorMsg);
		}
		if(!cust.online_pay){
			errorMsg=errorMsg+"订单出现异常，请联系客服处理！";
			render(""+result_url,momey,errorMsg);
		}
		String bank_name=PayBank.getBankName(bank);
		if(bank_name.equals("")){
			errorMsg=errorMsg+"您选择的银行，目前暂不支持在线支付。";
			render(""+result_url,momey,errorMsg);
		}
		try{
		      DigestUtils.md5Hex("".getBytes("UTF-8"));
		      String MerNo="25651";
		      String BillNo=OrderNo.createLocalNo("HC");
		      String Amount =momey.intValue()+"";
		      String ReturnURL="http://"+request.current().domain+"/chpay_return.do";
		      String key = "eXasfftg"; 
		  	  String md5src= MerNo +"&"+ BillNo +"&"+ Amount +"&"+ ReturnURL +"&"+ key ;
		  	  String SignInfo = DigestUtils.md5Hex(md5src.getBytes("UTF-8")).toUpperCase();
		  	  String  defaultBankNumber="ICBC";
		  	  String orderTime =DateUtil.getDateString("yyyyMMddHHmmss");
		  	  String products = "Cosmetics";
		  	  String Remark="";
		  	  String AdviceURL="http://p11.8da.ph/chpay.do";
		  	  BigDecimal amount=new BigDecimal(momey);
		  	  if(Hcpay.NTcreat(BillNo,amount, defaultBankNumber, "0", orderTime, "", login_name, cust.cust_id)){
				    render(MerNo,BillNo,amount,ReturnURL,AdviceURL,
				    		orderTime,orderTime,defaultBankNumber,SignInfo,
				    		Remark,products,cust);
		  		
		      }
		}catch(Exception e){
			e.printStackTrace();
			errorMsg=errorMsg+"订单出现异常。";
			render(""+result_url,momey,errorMsg);
		}
	}
	
	
	
	public static void tranfer_game(BigDecimal credit,String plat){
		String login_name=session.get("username");
		Customer cust=Customer.getCustomer(login_name);
		String plat_name="";
		String errorMsg="很抱歉，您的转账未成功，原因是：";
		if(credit==null||credit.floatValue()<=0){
			errorMsg=errorMsg+"转账金额未填写。";
			flash.put("errorMsg", errorMsg);
			render("/MeiBoPhp/tranfer.html",cust,errorMsg);
		}
		if(!("AGIN".equals(plat)||"BBIN".equals(plat)||"PT".equals(plat)||"KG".equals(plat)||"MG".equals(plat)||"VS".equals(plat)||"PP".equals(plat))){
			errorMsg=errorMsg+"请您选择游戏平台。";
			flash.put("errorMsg", errorMsg);
			render("/MeiBoPhp/tranfer.html",cust,errorMsg);
		}
		credit=new BigDecimal(credit.intValue());
		if(credit.intValue()>cust.credit.intValue()){
			errorMsg=errorMsg+"主账户额度不足。";
			flash.put("errorMsg", errorMsg);
			render("/MeiBoPhp/tranfer.html",cust,errorMsg);
		}
		Plat p =null;
		if("AGIN".equals(plat)){
			plat_name="AG厅";
			p=Plat.AG;
		}
		if("BBIN".equals(plat)){
			plat_name="波音厅";
			p=Plat.BBIN;
		}
		if("PT".equals(plat)){
			plat_name="PT厅";
			p=Plat.PT;
		}
		if("KG".equals(plat)){
			plat_name="KG厅";
			p=Plat.KG;
		}
		if("MG".equals(plat)){
			plat_name="MG厅";
			p=Plat.MG;
		}
		if("VS".equals(plat)){
			plat_name="申博厅";
			p=Plat.VS;
		}
		if("PP".equals(plat)){
			plat_name="PP厅";
			p=Plat.PP;
		}
		String ip=IPApp.getIpAddr();
		String order_no =""+System.currentTimeMillis();
		credit=new BigDecimal(credit.intValue()).setScale(2,BigDecimal.ROUND_HALF_EVEN);
		boolean f=PlatService.transferIn(p, login_name, ip, "会员转账","会员转账", login_name, order_no, credit);
		if(f){
			String successMsg="您的转账转向"+plat_name+"申请已处理成功！转账额度为:"+credit.toString();
			cust=Customer.getCustomer(login_name);
			render("/MeiBoPhp/tranfer.html",cust,successMsg);
		}else{
			errorMsg=errorMsg+"系统错误，联系在线客服为您处理。";
			flash.put("errorMsg", errorMsg);
			render("/MeiBoPhp/tranfer.html",cust,errorMsg);
			
		}
		
	}
	
	public static void pttranferin(BigDecimal credit,String username){
		String login_name=username;
		Customer cust=Customer.getCustomer(login_name);
		String plat_name="";
		String errorMsg="很抱歉，您的转账未成功，原因是：";
		if(credit==null||credit.floatValue()<=0){
			errorMsg=errorMsg+"转账金额未填写。";
			flash.put("errorMsg", errorMsg);
			render("/MeiBoPhp/tranfer.html",cust,errorMsg);
		}
		
		credit=new BigDecimal(credit.intValue());
		if(credit.intValue()>cust.credit.intValue()){
			errorMsg=errorMsg+"主账户额度不足。";
			flash.put("errorMsg", errorMsg);
			render("/MeiBoPhp/tranfer.html",cust,errorMsg);
		}
		Plat p =null;
		plat_name="PT厅";
		p=Plat.PT;
		String ip=IPApp.getIpAddr();
		String order_no =""+System.currentTimeMillis();
		credit=new BigDecimal(credit.intValue()).setScale(2,BigDecimal.ROUND_HALF_EVEN);
		boolean f=PlatService.transferIn(p, login_name, ip, "会员转账","会员转账", login_name, order_no, credit);
		if(f){
			String successMsg="您的转账转向"+plat_name+"申请已处理成功！转账额度为:"+credit.toString();
			cust=Customer.getCustomer(login_name);
			render("/MeiBoPhp/pttranfer.html",cust,successMsg);
		}else{
			errorMsg=errorMsg+"系统错误，联系在线客服为您处理。";
			flash.put("errorMsg", errorMsg);
			render("/MeiBoPhp/pttranfer.html",cust,errorMsg);
			
		}
		
	}
	
	public static void pttranferout(BigDecimal credit,String username){
		String login_name=username;
		Customer cust=Customer.getCustomer(login_name);
		String plat_name="";
		String errorMsg="很抱歉，您的转账未成功，原因是：";
		if(credit==null||credit.intValue()<=0){
			errorMsg=errorMsg+"转账金额未填写。";
			flash.put("errorMsg", errorMsg);
			render("/MeiBoPhp/pttranfer.html",cust,errorMsg);
		}
		
		String ip=IPApp.getIpAddr();
		BigDecimal gameCredit=new BigDecimal(0);
		
		String game_name="";
		String game_pwd="";
		
		Plat p=null;
		
		gameCredit=PlatService.balance(Plat.PT, login_name, ip);
		plat_name="PT厅";
		game_name=cust.pt_game;
		game_pwd=cust.pt_pwd;
		p=Plat.PT;
		
		
		
		credit=new BigDecimal(credit.intValue()).setScale(2,BigDecimal.ROUND_HALF_EVEN);
		if(credit.intValue()>gameCredit.intValue()){
			errorMsg=errorMsg+""+plat_name+"游戏额度不足。";
			flash.put("errorMsg", errorMsg);
			render("/MeiBoPhp/pttranfer.html",cust,errorMsg);
		}
		String order_no =""+System.currentTimeMillis();
		credit=new BigDecimal(credit.intValue()).setScale(2,BigDecimal.ROUND_HALF_EVEN);
		boolean f=PlatService.transferOut(p, login_name, ip, "会员转账", "会员转账", login_name, order_no, credit);
		if(f){
			String successMsg="您的转账从"+plat_name+"转向主账户申请已处理成功！转账额度为:"+credit.toString();
			cust=Customer.getCustomer(login_name);
			render("/MeiBoPhp/pttranfer.html",cust,successMsg);
		}else{
			errorMsg=errorMsg+"系统错误，联系在线客服为您处理。";
			flash.put("errorMsg", errorMsg);
			render("/MeiBoPhp/pttranfer.html",cust,errorMsg);
			
		}
	}
	public static void tranfer_local(BigDecimal credit,String plat){
		String login_name=session.get("username");
		Customer cust=Customer.getCustomer(login_name);
		String plat_name="";
		String errorMsg="很抱歉，您的转账未成功，原因是：";
		if(credit==null||credit.intValue()<=0){
			errorMsg=errorMsg+"转账金额未填写。";
			flash.put("errorMsg", errorMsg);
			render("/MeiBoPhp/tranfer.html",cust,errorMsg);
		}
		if(!("AGIN".equals(plat)||"BBIN".equals(plat)||"PT".equals(plat)||"KG".equals(plat)||"MG".equals(plat)||"VS".equals(plat)||"PP".equals(plat))){
			errorMsg=errorMsg+"请您选择游戏平台。";
			flash.put("errorMsg", errorMsg);
			render("/MeiBoPhp/tranfer.html",cust,errorMsg);
		}
		String ip=IPApp.getIpAddr();
		BigDecimal gameCredit=new BigDecimal(0);
		
		String game_name="";
		String game_pwd="";
		
		Plat p=null;
		if("AGIN".equals(plat)){
			gameCredit=PlatService.balance(Plat.AG, login_name, ip);
			plat_name="AG厅";
			game_name=cust.ag_game;
			game_pwd=cust.ag_pwd;
			p=Plat.AG;
		}
		if("BBIN".equals(plat)){
			gameCredit=PlatService.balance(Plat.BBIN, login_name, ip);
			plat_name="波音厅";
			game_name=cust.bbin_game;
			game_pwd=cust.bbin_pwd;
			p=Plat.BBIN;
		}
		if("PT".equals(plat)){
			gameCredit=PlatService.balance(Plat.PT, login_name, ip);
			plat_name="PT厅";
			game_name=cust.pt_game;
			game_pwd=cust.pt_pwd;
			p=Plat.PT;
		}
		if("KG".equals(plat)){
			gameCredit=PlatService.balance(Plat.KG, login_name, ip);
			plat_name="KG厅";
			game_name=cust.kg_game;
			game_pwd=cust.kg_pwd;
			p=Plat.KG;
		}
		
		if("MG".equals(plat)){
			gameCredit=PlatService.balance(Plat.MG, login_name, ip);
			plat_name="MG厅";
			game_name=cust.mg_game;
			game_pwd=cust.mg_pwd;
			p=Plat.MG;
		}
		
		if("VS".equals(plat)){
			gameCredit=PlatService.balance(Plat.VS, login_name, ip);
			plat_name="申博厅";
			game_name=cust.sb_game;
			game_pwd=cust.sb_pwd;
			p=Plat.VS;
		}
		if("PP".equals(plat)){
			gameCredit=PlatService.balance(Plat.PP, login_name, ip);
			plat_name="PP厅";
			game_name=cust.sb_game;
			game_pwd=cust.sb_pwd;
			p=Plat.PP;
		}
		
		credit=new BigDecimal(credit.intValue()).setScale(2,BigDecimal.ROUND_HALF_EVEN);
		if(credit.intValue()>gameCredit.intValue()){
			errorMsg=errorMsg+""+plat_name+"游戏额度不足。";
			flash.put("errorMsg", errorMsg);
			render("/MeiBoPhp/tranfer.html",cust,errorMsg);
		}
		String order_no =""+System.currentTimeMillis();
		credit=new BigDecimal(credit.intValue()).setScale(2,BigDecimal.ROUND_HALF_EVEN);
		boolean f=PlatService.transferOut(p, login_name, ip, "会员转账", "会员转账", login_name, order_no, credit);
		if(f){
			String successMsg="您的转账从"+plat_name+"转向主账户申请已处理成功！转账额度为:"+credit.toString();
			cust=Customer.getCustomer(login_name);
			render("/MeiBoPhp/tranfer.html",cust,successMsg);
		}else{
			errorMsg=errorMsg+"系统错误，联系在线客服为您处理。";
			flash.put("errorMsg", errorMsg);
			render("/MeiBoPhp/tranfer.html",cust,errorMsg);
			
		}
		
	}
	
	/**
	 * 网银充值提交
	 * @param momey
	 * @param bank_id
	 */
	public static void deposit_auto_bank(BigDecimal momey,String bank_id){
		String result_url="/MeiBoPhp/choose_auto_bank.html";
		String login_name=session.get("username");
		Customer cust=Customer.getCustomer(login_name);
		String errorMsg="很抱歉，您的充值未提交成功，原因是：";
		if(momey==null||momey.intValue()==0){
			errorMsg=errorMsg+"您的存款金额为0元";
			render(""+result_url,momey,errorMsg);
		}
		if(momey.intValue()<100){
			if(cust.cust_level==0 || login_name.equals("woody")){
				if(momey.intValue()<3){
					errorMsg=errorMsg+"您的存款金额必须大于3元。";
					render(""+result_url,momey,errorMsg,cust);
				}
			}else{
				errorMsg=errorMsg+"您的存款金额必须大于100元。";
				render(""+result_url,momey,errorMsg);
			}
			
		}
		if(momey.intValue()>50000){
			errorMsg=errorMsg+"您的存款金额大于5万元。单笔存款必须不大于5万元。";
			render(""+result_url,momey,errorMsg);
		}
		if(bank_id==null||"".equals(bank_id)){
			errorMsg=errorMsg+"您没有选择银行。";
			render(""+result_url,momey,errorMsg);
		}
		if(!cust.online_pay){
			errorMsg=errorMsg+"订单出现异常，请联系客服处理！";
			render(""+result_url,momey,errorMsg);
		}
		
		//请求后台拿去卡号
		Result result=DPayService.bank(login_name, momey, bank_id);
		if(result==null){
			errorMsg=errorMsg+"未读取到存款账号，请联系客服处理。！";
			render(""+result_url,momey,errorMsg);
		}
		String exp_date=DateUtil.dateToString(new Date(System.currentTimeMillis()+30*60*1000), "yyyy-MM-dd HH:mm");
		render(momey,bank_id,result,exp_date);
		
	}
	
	/**
	 * 财付通
	 * @param momey
	 */
	public static void deposit_auto_tenpay(BigDecimal momey){
		String result_url="/MeiBoPhp/deposit_tenpay.html";
		String login_name=session.get("username");
		Customer cust=Customer.getCustomer(login_name);
		String errorMsg="很抱歉，您的充值未提交成功，原因是：";
		if(momey==null||momey.intValue()==0){
			errorMsg=errorMsg+"您的存款金额为0元";
			render(""+result_url,momey,errorMsg);
		}
		if(momey.intValue()<100){
			errorMsg=errorMsg+"您的存款金额必须大于100元。";
			render(""+result_url,momey,errorMsg);
		}
		if(momey.intValue()>1000){
			errorMsg=errorMsg+"您的存款金额大于1000元。单笔存款必须不大于1000元。";
			render(""+result_url,momey,errorMsg);
		}
		if(!cust.online_pay){
			errorMsg=errorMsg+"订单出现异常，请联系客服处理！";
			render(""+result_url,momey,errorMsg);
		}
		
		//请求后台拿去卡号
		Result result=DPayService.tenPay(login_name, momey);
		if(result==null){
			errorMsg=errorMsg+"未读取到财付通账号，请联系客服处理。！";
			render(""+result_url,momey,errorMsg);
		}
		String exp_date=DateUtil.dateToString(new Date(System.currentTimeMillis()+30*60*1000), "yyyy-MM-dd HH:mm");
		render(momey,result,exp_date);
		
	}
	
	
	public static void  deposit_auto_alipay(BigDecimal momey){
		
		String result_url="/MeiBoPhp/deposit_alipay_ecode.html";
		String login_name=session.get("username");
		Customer cust=Customer.getCustomer(login_name);
		if(!(cust.alipay_account!=null&&cust.alipay_account.length()>5&&cust.alipay_name!=null&&cust.alipay_name.length()>=1)){
			MeiBoPhp.deposit_alipay_account();
		}
		String errorMsg="很抱歉，您的充值未提交成功，原因是：";
		if(momey==null||momey.intValue()==0){
			errorMsg=errorMsg+"您的存款金额为0元";
			render(""+result_url,momey,errorMsg,cust);
		}
		if(momey.intValue()<100){
			errorMsg=errorMsg+"您的存款金额必须大于100元。";
			render(""+result_url,momey,errorMsg,cust);
		}
		if(momey.intValue()>5000){
			errorMsg=errorMsg+"您的存款金额大于5000元。单笔存款必须不大于5000元。";
			render(""+result_url,momey,errorMsg,cust);
		}
		if(!cust.online_pay){
			errorMsg=errorMsg+"订单出现异常，请联系客服处理！";
			render(""+result_url,momey,errorMsg,cust);
		}
		
		//请求后台拿去卡号
		Result result=DPayService.alipay(login_name, momey);
		if(result==null){
			errorMsg=errorMsg+"未读取到支付宝二维码，请联系客服处理。！";
			render(""+result_url,momey,errorMsg,cust);
		}
		String exp_date=DateUtil.dateToString(new Date(System.currentTimeMillis()+30*60*1000), "yyyy-MM-dd HH:mm");
		render(momey,result,exp_date,cust);
	}
	
	/**
	 * 绑定支付宝账号
	 * @param alipay_name
	 * @param alipay_account
	 */
	public static void deposit_auto_alipay_account(String alipay_name,String alipay_account){
		
		String result_url="/MeiBoPhp/deposit_alipay_account.html";
		String errorMsg="很抱歉，您的绑定支付宝账号未提交成功，原因是：";
		String login_name=session.get("username");
		Customer cust=Customer.getCustomer(login_name);
		if(alipay_name==null||"".equals(alipay_name)||alipay_name.length()<1||alipay_name.length()>30){
			alipay_name=cust.real_name;
		}
		if(alipay_account==null||"".equals(alipay_account)||alipay_account.length()<3||alipay_account.length()>30){
			errorMsg=errorMsg+"请填写正确支付宝账号。";
			render(""+result_url,errorMsg);
		}
	
		boolean b=Customer.updateAlipayAccount(alipay_account, alipay_name, login_name);
		if(!b){
			errorMsg=errorMsg+"绑定失败！";
			render(""+result_url,errorMsg);
		}
		cust=Customer.getCustomer(login_name);
		render(cust);
	}
	
	
	public static void deposit_jbpay_bank(BigDecimal amount){
		String result_url="/MeiBoPhp/choose_jbpay_bank.html";
		String login_name=session.get("username");
		Customer cust=Customer.getCustomer(login_name);
		String errorMsg="很抱歉，您的订单未提交成功，原因是：";
		
		if(amount==null||amount.intValue()==0){
			errorMsg=errorMsg+"您的存款金额为0元";
			render(""+result_url,amount,errorMsg,cust);
		}
		if(amount.intValue()<100){
			errorMsg=errorMsg+"您的存款金额必须大于100元。";
			render(""+result_url,amount,errorMsg,cust);
		}
		if(amount.intValue()>500000){
			errorMsg=errorMsg+"您的存款金额大于500000元。单笔存款必须不大于500000元。";
			render(""+result_url,amount,errorMsg,cust);
		}
		if(!cust.online_pay){
			errorMsg=errorMsg+"订单出现异常，请联系客服处理！";
			render(""+result_url,amount,errorMsg,cust);
		}
		
		Result  result=JbpService.pay(login_name, amount, "8da",request.get().domain);
		render(result);
	}
	
	public static void deposit_weixin(BigDecimal amount,String bank,Integer maxlimit){
		//System.out.println(maxlimit);
		String result_url="/MeiBoPhp/choose_weixin_form.html";
		String login_name=session.get("username");
		Customer cust=Customer.getCustomer(login_name);
		String errorMsg="很抱歉，您的订单未提交成功，原因是：";
		String tongdao = "";
		if(amount==null||amount.intValue()==0){
			errorMsg=errorMsg+"您的存款金额为0元";
			render(""+result_url,amount,errorMsg,cust);
		}
		
		Date now =new Date(System.currentTimeMillis());
		if(now.getTime()>DateUtil.stringToDate("2017-10-18", "yyyy-MM-dd").getTime() &&
	    		  DateUtil.stringToDate("2018-01-19", "yyyy-MM-dd").getTime()>now.getTime() && cust.cust_level==0){
			if(amount.intValue()<58){
				if(cust.cust_level==0 || login_name.equals("woody")){
					if(amount.intValue()<3){
						errorMsg=errorMsg+"您的存款金额必须大于3元。";
						render(""+result_url,amount,errorMsg,cust);
					}
					
				}else{
					errorMsg=errorMsg+"您的存款金额必须大于58元。";
					render(""+result_url,amount,errorMsg,cust);
				}
			}
		}else if(amount.intValue()<100){
			if(cust.cust_level==0 || login_name.equals("woody") ){
				if(amount.intValue()<3){
					errorMsg=errorMsg+"您的存款金额必须大于3元。";
					render(""+result_url,amount,errorMsg,cust);
				}
				
			}else{
				errorMsg=errorMsg+"您的存款金额必须大于100元。";
				render(""+result_url,amount,errorMsg,cust);
			}
			
		}
		
//		if(amount.intValue()<100 && !login_name.equals("lance008")){
//			errorMsg=errorMsg+"您的存款金额必须大于100元。";
//			render(""+result_url,amount,errorMsg,cust);
//		}
		
		if(bank==null||"".equals(bank)){
			errorMsg=errorMsg+"您没有选择通道。";
			render(""+result_url,amount,errorMsg);
		}
		
		if(amount.intValue()>maxlimit ){
			errorMsg=errorMsg+"您的存款金额大于"+maxlimit+"元。单笔存款必须不大于"+maxlimit+"元。";
			render(""+result_url,amount,errorMsg,cust);
		}
		
		
		if(bank.equals("1")){//银宝
			tongdao="1";
//			if(amount.intValue()>50000){
//				errorMsg=errorMsg+"您的存款金额大于50000元。通道一存款必须不大于50000元。";
//				render(""+result_url,amount,errorMsg,cust);
//			}
			Result  result=YinbaoPayService.pay(login_name, amount, "8da",request.get().domain,"WEIXIN",IPApp.getIpAddr());
			render(result,tongdao);
			
		}else if(bank.equals("2")){//讯汇宝
			tongdao="2";
			Result  result=XunhuibaoPayService.pay(login_name, amount,request.get().domain,"2",IPApp.getIpAddr());
			String barCode = result.get("barCode");
			render(result,barCode,tongdao);
			
		}else if(bank.equals("3")){//乐盈
			tongdao="3";
			//Result  result=LypService.pay(login_name, amount, "8da",request.get().domain,"WX",IPApp.getIpAddr(),type);
			//render(result,tongdao);
			
		}else if(bank.equals("4")){//智付
			try{
				tongdao="4";
				DinpayBean dinpay=DinpayRandom.get().random();
			      DigestUtils.md5Hex("".getBytes("UTF-8"));
			      String merchant_code=dinpay.merId;
			      String order_no=OrderNo.createLocalNo("YF");
			      String order_amount=amount.intValue()+"";
			     
			      String notify_url=dinpay.weixinnotify_url;
			      String interface_version="V3.0";
			      
			      String order_time=DateUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss");
			      String product_name="Computer";
				  
			      Result  result=DinPayService.pay(interface_version, merchant_code, notify_url, order_amount, order_no, order_time, product_name);
			      if(result != null){
			    	  String sQrcode = result.get("sQrcode");
			    	  if(sQrcode == null || sQrcode.equals("")){
			    		  errorMsg=errorMsg+"订单出现异常，请联系客服处理！";
			  			  render(""+result_url,amount,errorMsg,cust);
			    	  }
			    	  
			    	  Dinpay.NTcreat(cust.cust_id, cust.login_name,
					  			merchant_code, new BigDecimal(order_amount), order_time, IPApp.getIpAddr(), product_name, "WEIXIN",
					  			"WX",  interface_version, "RSA-S", "", 0,order_no);
			    		  
			    	  QrcodeDis myQrcode = new QrcodeDis();  
			    	  myQrcode.createQRCode(sQrcode, Play.applicationPath.toString()+File.separatorChar+"zhifu"+File.separatorChar+order_no+".png", Play.applicationPath.toString()+File.separatorChar+"public"+File.separatorChar+"images"+File.separatorChar+"dinpay.png", 15);  
						 
			      }
			      render(result,order_no,order_amount,login_name,tongdao);
			      
			}catch(Exception e){
				e.printStackTrace();
				errorMsg=errorMsg+"订单出现异常。";
				render(""+result_url,amount,errorMsg);
			}
		}else if(bank.equals("5")){//讯付通
			tongdao="5";
			Result  result=XunfutongPayService.pay(login_name, amount,request.get().domain,"2",IPApp.getIpAddr());
			String barCode = result.get("barCode");
			render(result,barCode,tongdao);
			
		}else if(bank.equals("6")){//赢通宝
			tongdao="6";
			Result  result=YingtongbaoPayService.pay(login_name, amount,request.get().domain,"2",IPApp.getIpAddr());
			String barCode = result.get("barCode");
			render(result,barCode,tongdao);
			
		}else if(bank.equals("7")){//通汇微信
			tongdao="7";
			Result  result=ThpService.weixinpay(login_name, amount,request.get().domain,"2",IPApp.getIpAddr());
			String barCode = result.get("barCode");
			render(result,barCode,tongdao);
			
		}else if(bank.equals("8")){
			tongdao="8";
			Result  result=XinBeiPayService.pay(login_name, amount,request.get().domain,"100040",IPApp.getIpAddr());
			
			render(result,tongdao);
		}else if(bank.equals("9")){//高通
			//tongdao="9";
			//Result  result=GaoTongService.pay(login_name, amount, "8da",request.get().domain,"WEIXIN",IPApp.getIpAddr());
			//render(result,tongdao);
			
		}else if(bank.equals("10")){//扫码付
			tongdao="10";
			Result  result=SaoMaPayService.pay(login_name, amount,request.get().domain,"0101",IPApp.getIpAddr());
			String barCode = result.get("barCode");
			render(result,barCode,tongdao);
			
		}else if(bank.equals("11")){//3Vpay
			tongdao="11";
			Result  result=SanVPayService.pay(login_name, amount,request.get().domain,"1001",IPApp.getIpAddr());
			String barCode = result.get("barCode");
			render(result,barCode,tongdao);
			
		}else if("12".equals(bank)){
			tongdao="12";
			Result  result=DingYiPayService.pay(login_name, amount,request.get().domain,"1004",IPApp.getIpAddr());
			String ok = result.get("ok");
			if("1".equals(ok)){
				render(result,tongdao);
			}
			//render(result,barCode,tongdao);
		}else if(bank.equals("13")){
			tongdao="13";
			Result  result=ChangFuPayService.pay(login_name, amount,request.get().domain,"2001",IPApp.getIpAddr());
			
			render(result,tongdao);
		}else if(bank.equals("14")){
			tongdao="14";
			Result  result=JinYangPayService.pay(login_name, amount,request.get().domain,"WEIXIN",IPApp.getIpAddr());
			String barCode = result.get("barCode");
			render(result,barCode,tongdao);
		}else if(bank.equals("15")){//讯捷通微信
			tongdao="15";
			Result  result=XunhuibaoPayService.pay(login_name, amount,request.get().domain,"2",IPApp.getIpAddr());
			String barCode = result.get("barCode");
			render(result,barCode,tongdao);
		}else if(bank.equals("16")){//安通微信
			tongdao="16";
			Result  result=PayService.antongPay(login_name, amount, request.get().domain, "weixin", IPApp.getIpAddr());
			render(result,tongdao);
		}else if(bank.equals("17")){//乐付宝微信
			tongdao="17";
			Result  result=PayService.lefubaoPay(login_name, amount, request.get().domain, "weixin", IPApp.getIpAddr());
			render(result,tongdao);
		}
		
			
	}
	
	public static void deposit_yinlian(BigDecimal amount,String bank,Integer maxlimit){
		
		String result_url="/MeiBoPhp/choose_yinlian_form.html";
		String login_name=session.get("username");
		Customer cust=Customer.getCustomer(login_name);
		String errorMsg="很抱歉，您的订单未提交成功，原因是：";
		String tongdao = "";
		if(amount==null||amount.intValue()==0){
			errorMsg=errorMsg+"您的存款金额为0元";
			render(""+result_url,amount,errorMsg,cust);
		}
		
		Date now =new Date(System.currentTimeMillis());
		if(now.getTime()>DateUtil.stringToDate("2017-10-18", "yyyy-MM-dd").getTime() &&
	    		  DateUtil.stringToDate("2018-01-19", "yyyy-MM-dd").getTime()>now.getTime() && cust.cust_level==0){
			if(amount.intValue()<58){
				if(cust.cust_level==0 || login_name.equals("woody")){
					if(amount.intValue()<3){
						errorMsg=errorMsg+"您的存款金额必须大于3元。";
						render(""+result_url,amount,errorMsg,cust);
					}
					
				}else{
					errorMsg=errorMsg+"您的存款金额必须大于58元。";
					render(""+result_url,amount,errorMsg,cust);
				}
			}
		}else if(amount.intValue()<100){
			if(cust.cust_level==0 || login_name.equals("woody") ){
				if(amount.intValue()<3){
					errorMsg=errorMsg+"您的存款金额必须大于3元。";
					render(""+result_url,amount,errorMsg,cust);
				}
				
			}else{
				errorMsg=errorMsg+"您的存款金额必须大于100元。";
				render(""+result_url,amount,errorMsg,cust);
			}
			
		}
		

		
		if(bank==null||"".equals(bank)){
			errorMsg=errorMsg+"您没有选择通道。";
			render(""+result_url,amount,errorMsg);
		}
		
		if(amount.intValue()>maxlimit ){
			errorMsg=errorMsg+"您的存款金额大于"+maxlimit+"元。单笔存款必须不大于"+maxlimit+"元。";
			render(""+result_url,amount,errorMsg,cust);
		}
		
		

		if(bank.equals("1")){
			tongdao="1";
			Result  result=YingtongbaoPayService.pay(login_name, amount,request.get().domain,"32",IPApp.getIpAddr());
			String barCode = result.get("barCode");
			render(result,barCode,tongdao);
		}
		
		if(bank.equals("2")){
			tongdao="2";
			Result  result=XinBeiPayService.pay(login_name, amount,request.get().domain,"100012",IPApp.getIpAddr());				
			render(result,tongdao);
		}
		
		if(bank.equals("3")){//金阳
			tongdao="3";
			Result  result=JinYangPayService.pay(login_name, amount,request.get().domain,"UNIONPAY",IPApp.getIpAddr());
			String barCode = result.get("barCode");
			render(result,barCode,tongdao);
			
		}
		
		if(bank.equals("4")){//汇博
			tongdao="4";
			Result  result=HbpService.qrcodePay(login_name, amount, "8da",request.get().domain,"YLPAY",request.get().remoteAddress);
			String barCode = result.get("barCode");
			render(result,barCode,tongdao);
		}
		if(bank.equals("5")){//星和易通
			tongdao="5";
			Result  result=XingHeYiService.scanpay(login_name, amount,request.get().domain,"4",IPApp.getIpAddr());
			String barCode = result.get("barCode");
			render(result,barCode,tongdao);
			
		}
				
	}
	
	
	public static void deposit_jingdong(BigDecimal amount,String bank,Integer maxlimit){
		
		String result_url="/MeiBoPhp/choose_jingdong_form.html";
		String login_name=session.get("username");
		Customer cust=Customer.getCustomer(login_name);
		String errorMsg="很抱歉，您的订单未提交成功，原因是：";
		String tongdao = "";
		if(amount==null||amount.intValue()==0){
			errorMsg=errorMsg+"您的存款金额为0元";
			render(""+result_url,amount,errorMsg,cust);
		}
		
		Date now =new Date(System.currentTimeMillis());
		if(now.getTime()>DateUtil.stringToDate("2017-10-18", "yyyy-MM-dd").getTime() &&
	    		  DateUtil.stringToDate("2018-01-19", "yyyy-MM-dd").getTime()>now.getTime() && cust.cust_level==0){
			if(amount.intValue()<58){
				if(cust.cust_level==0){
					if(amount.intValue()<3){
						errorMsg=errorMsg+"您的存款金额必须大于3元。";
						render(""+result_url,amount,errorMsg,cust);
					}
				}else{
					errorMsg=errorMsg+"您的存款金额必须大于58元。";
					render(""+result_url,amount,errorMsg,cust);
				}
			}
		}else if(amount.intValue()<100){
			if(cust.cust_level==0){
				if(amount.intValue()<3){
					errorMsg=errorMsg+"您的存款金额必须大于3元。";
					render(""+result_url,amount,errorMsg,cust);
				}
			}else{
				errorMsg=errorMsg+"您的存款金额必须大于100元。";
				render(""+result_url,amount,errorMsg,cust);
			}
		}
		

		
		if(bank==null||"".equals(bank)){
			errorMsg=errorMsg+"您没有选择通道。";
			render(""+result_url,amount,errorMsg);
		}
		
		if(amount.intValue()>maxlimit ){
			errorMsg=errorMsg+"您的存款金额大于"+maxlimit+"元。单笔存款必须不大于"+maxlimit+"元。";
			render(""+result_url,amount,errorMsg,cust);
		}
		
		

		if(bank.equals("1")){
			tongdao="xinbei";
			Result  result=XinBeiPayService.pay(login_name, amount,request.get().domain,"100069",IPApp.getIpAddr());				
			render(result,tongdao);
		}
		
		if("2".equals(bank)){
			tongdao="dingyi";
			Result  result=DingYiPayService.pay(login_name, amount,request.get().domain,"1010",IPApp.getIpAddr());
			render(result,tongdao);
		}
		
		if(bank.equals("3")){
			tongdao="3";
			Result  result=ChangFuPayService.pay(login_name, amount,request.get().domain,"2010",IPApp.getIpAddr());
			
			render(result,tongdao);
		}
		if(bank.equals("4")){//金阳
			tongdao="4";
			Result  result=JinYangPayService.pay(login_name, amount,request.get().domain,"JDPAY",IPApp.getIpAddr());
			String barCode = result.get("barCode");
			render(result,barCode,tongdao);
			
		}
		if(bank.equals("5")){
			tongdao="5";
			Result  result=GaoTongPayService.pay(login_name, amount,"8da",request.get().domain,"JDPAY",IPApp.getIpAddr());
			
			render(result,tongdao);
		}
		
		
				
	}
	
	
	public static void deposit_qq(BigDecimal amount,String bank,Integer maxlimit){
		//System.out.println(maxlimit);
		String result_url="/MeiBoPhp/choose_qq_form.html";
		String login_name=session.get("username");
		Customer cust=Customer.getCustomer(login_name);
		String errorMsg="很抱歉，您的订单未提交成功，原因是：";
		String tongdao = "";
		if(amount==null||amount.intValue()==0){
			errorMsg=errorMsg+"您的存款金额为0元";
			render(""+result_url,amount,errorMsg,cust);
		}
		
		Date now =new Date(System.currentTimeMillis());
		if(now.getTime()>DateUtil.stringToDate("2017-10-18", "yyyy-MM-dd").getTime() &&
	    		  DateUtil.stringToDate("2018-01-19", "yyyy-MM-dd").getTime()>now.getTime() && cust.cust_level==0){
			if(amount.intValue()<58){
				if(cust.cust_level==0 || login_name.equals("woody")){
					if(amount.intValue()<3){
						errorMsg=errorMsg+"您的存款金额必须大于3元。";
						render(""+result_url,amount,errorMsg,cust);
					}
				}else{
					errorMsg=errorMsg+"您的存款金额必须大于58元。";
					render(""+result_url,amount,errorMsg,cust);
				}
			}
		}else if(amount.intValue()<100){
			if(cust.cust_level==0 || login_name.equals("woody")){
				if(amount.intValue()<3){
					errorMsg=errorMsg+"您的存款金额必须大于3元。";
					render(""+result_url,amount,errorMsg,cust);
				}
			}else{
				errorMsg=errorMsg+"您的存款金额必须大于100元。";
				render(""+result_url,amount,errorMsg,cust);
			}
		}
		
//		if(amount.intValue()<100 && !login_name.equals("lance008")){
//			errorMsg=errorMsg+"您的存款金额必须大于100元。";
//			render(""+result_url,amount,errorMsg,cust);
//		}
		
		if(bank==null||"".equals(bank)){
			errorMsg=errorMsg+"您没有选择通道。";
			render(""+result_url,amount,errorMsg);
		}
		
		if(amount.intValue()>maxlimit ){
			errorMsg=errorMsg+"您的存款金额大于"+maxlimit+"元。单笔存款必须不大于"+maxlimit+"元。";
			render(""+result_url,amount,errorMsg,cust);
		}
		
		
		if("1".equals(bank)){
			tongdao="dingyi";
			Result  result=DingYiPayService.pay(login_name, amount,request.get().domain,"1006",IPApp.getIpAddr());
			render(result,tongdao);
		}
		
		if(bank.equals("2")){
			tongdao="xinbei";
			Result  result=XinBeiPayService.pay(login_name, amount,request.get().domain,"100068",IPApp.getIpAddr());				
			render(result,tongdao);
		}
		
		if(bank.equals("3")){//赢通宝
			tongdao="6";
			Result  result=YingtongbaoPayService.pay(login_name, amount,request.get().domain,"8",IPApp.getIpAddr());
			String barCode = result.get("barCode");
			render(result,barCode,tongdao);
			
		}
		if(bank.equals("4")){
			tongdao="4";
			Result  result=ChangFuPayService.pay(login_name, amount,request.get().domain,"2008",IPApp.getIpAddr());
			
			render(result,tongdao);
		}
		
		if(bank.equals("5")){//金阳
			tongdao="5";
			Result  result=JinYangPayService.pay(login_name, amount,request.get().domain,"QQPAY",IPApp.getIpAddr());
			String barCode = result.get("barCode");
			render(result,barCode,tongdao);
			
		}
		
		if(bank.equals("7")){//汇博qq
			tongdao="7";
			Result  result=HbpService.qrcodePay(login_name, amount, "8da",request.get().domain,"QQPAY",request.get().remoteAddress);
			String barCode = result.get("barCode");
			render(result,barCode,tongdao);
		}
		if(bank.equals("8")){//讯捷通qq
			tongdao="8";
			Result  result=XunhuibaoPayService.pay(login_name, amount,request.get().domain,"4",IPApp.getIpAddr());
			String barCode = result.get("barCode");
			render(result,barCode,tongdao);
		}
		if(bank.equals("9")){//剑跃QQ扫码
			tongdao="9";
			Result  result=XingHeYiService.scanpay(login_name, amount,request.get().domain,"3",IPApp.getIpAddr());
			String barCode = result.get("barCode");
			render(result,barCode,tongdao);
		}
		
////		boolean payOn02=MeiBoService.getXinbeiQQ();
////		if(payOn02 && !request.get().domain.contains("668da")){
////			if(amount.intValue()>3000 ){
////				errorMsg=errorMsg+"您的存款金额大于"+3000+"元。单笔存款必须不大于"+3000+"元。";
////				render(""+result_url,amount,errorMsg,cust);
////			}
////			
////			tongdao="xinbei";
////			Result  result=XinBeiPayService.pay(login_name, amount,request.get().domain,"100068",IPApp.getIpAddr());
////			
////			render(result,tongdao);
////			
////		}
////		
////		if(payOn02 && request.get().domain.contains("668da") && (login_name.equals("aa129ss") || login_name.equals("1596746")
////				|| login_name.equals("wen1314")|| login_name.equals("376418434")|| login_name.equals("q79232415")
////				|| login_name.equals("135790")|| login_name.equals("wulai")|| login_name.equals("woody"))){
////			if(amount.intValue()>3000 ){
////				errorMsg=errorMsg+"您的存款金额大于"+3000+"元。单笔存款必须不大于"+3000+"元。";
////				render(""+result_url,amount,errorMsg,cust);
////			}
////			
////			tongdao="xinbei";
////			Result  result=XinBeiPayService.pay(login_name, amount,request.get().domain,"100068",IPApp.getIpAddr());
////			
////			render(result,tongdao);
////		}
//		
//		
////		if(amount.intValue()>3000 ){
////			
////			errorMsg=errorMsg+"您的存款金额大于"+3000+"元。单笔存款必须不大于"+3000+"元。";
////			render(""+result_url,amount,errorMsg,cust);
////		}
//		
//		boolean yingtongbaoFlag=MeiBoService.getDepositwayFlag("yingtongbaoqq",cust.cust_level);
//		boolean dingyiFlag=MeiBoService.getDepositwayFlag("dingyiqq",cust.cust_level);
//		if(amount.intValue()>3000 ){
////			tongdao="gaotong";
////			Result  result=GaoTongPayService.pay(login_name, amount, "8da",request.get().domain,"QQPAY",IPApp.getIpAddr());
////			render(result,tongdao);
//			
//			
//			if(yingtongbaoFlag){
//				tongdao="6";
//				Result  result=YingtongbaoPayService.pay(login_name, amount,request.get().domain,"8",IPApp.getIpAddr());
//				String barCode = result.get("barCode");
//				render(result,barCode,tongdao);
//			}else if(dingyiFlag){
//				if(amount.intValue()>5000 ){
//					errorMsg=errorMsg+"您的存款金额大于"+5000+"元。单笔存款必须不大于"+5000+"元。";
//					render(""+result_url,amount,errorMsg,cust);
//				}
//				tongdao="dingyi";
//				Result  result=DingYiPayService.pay(login_name, amount,request.get().domain,"1006",IPApp.getIpAddr());
//				render(result,tongdao);
//			}else{
//				errorMsg=errorMsg+"您的存款金额大于"+3000+"元。单笔存款必须不大于"+3000+"元。";
//				render(""+result_url,amount,errorMsg,cust);
//			}
//		}else{
//			
//			
//			
//			boolean xinbeiFlag=MeiBoService.getDepositwayFlag("xinbeiqq",cust.cust_level);
//			if(xinbeiFlag){
//				tongdao="xinbei";
//				Result  result=XinBeiPayService.pay(login_name, amount,request.get().domain,"100068",IPApp.getIpAddr());				
//				render(result,tongdao);
//			}
//			
//			if(yingtongbaoFlag){
//				tongdao="6";
//				Result  result=YingtongbaoPayService.pay(login_name, amount,request.get().domain,"8",IPApp.getIpAddr());
//				String barCode = result.get("barCode");
//				render(result,barCode,tongdao);
//			}
//			if(dingyiFlag){
//				if(amount.intValue()>5000 ){
//					errorMsg=errorMsg+"您的存款金额大于"+5000+"元。单笔存款必须不大于"+5000+"元。";
//					render(""+result_url,amount,errorMsg,cust);
//				}
//				tongdao="dingyi";
//				Result  result=DingYiPayService.pay(login_name, amount,request.get().domain,"1006",IPApp.getIpAddr());
//				render(result,tongdao);
//			}
//			
////			Config config = Config.getConfig("xinbeiqq");
////			Calendar cal =  Calendar.getInstance();
//			//boolean depositwayFlag=MeiBoService.getDepositwayFlag("xinbeiqq",cust.cust_level);
//			//if(depositwayFlag ){
////			if(config == null || request.get().domain.contains("668da") || request.get().domain.contains("88d.com")
////					|| request.get().domain.contains("238da.com") || request.get().domain.contains("8da2016.com") ){
//			//if(config == null || (cal.get(Calendar.HOUR_OF_DAY)>23 || cal.get(Calendar.HOUR_OF_DAY)<7) || request.get().domain.contains("668da")){
//				
////				if( request.get().domain.contains("668da")){
////					if(login_name.equals("aa129ss") || login_name.equals("1596746")
////							|| login_name.equals("wen1314")|| login_name.equals("376418434")|| login_name.equals("q79232415")
////							|| login_name.equals("135790")|| login_name.equals("wulai") || login_name.equals("wsx65440") || login_name.equals("wx207314") 
////							|| login_name.equals("rose1979") || login_name.equals("mamadbao") || login_name.equals("gzyjj") 
////							|| login_name.equals("cgdmby") || login_name.equals("pbyympyl") ){
////						
////						tongdao="xinbei";
////						Result  result=XinBeiPayService.pay(login_name, amount,request.get().domain,"100068",IPApp.getIpAddr());
////						
////						render(result,tongdao);
////						
////					}else{
////						tongdao="6";
////						Result  result=YingtongbaoPayService.pay(login_name, amount,request.get().domain,"8",IPApp.getIpAddr());
////						String barCode = result.get("barCode");
////						render(result,barCode,tongdao);
////					}
////				}
////				tongdao="gaotong";
////				Result  result=GaoTongPayService.pay(login_name, amount, "8da",request.get().domain,"QQPAY",IPApp.getIpAddr());
////				render(result,tongdao);
//				
//				
//				
//			//}
////			else{
////				
////				
////				tongdao="6";
////				Result  result=YingtongbaoPayService.pay(login_name, amount,request.get().domain,"8",IPApp.getIpAddr());
////				String barCode = result.get("barCode");
////				render(result,barCode,tongdao);
////				
//////				tongdao="xinbei";
//////				Result  result=XinBeiPayService.pay(login_name, amount,request.get().domain,"100068",IPApp.getIpAddr());
//////				
//////				render(result,tongdao);
////				
////			}
//		}
//		
			
			
		
			
	}
	
	public static void deposit_zhifubao(BigDecimal amount,String bank,Integer type,Integer maxlimit){
		String result_url="/MeiBoPhp/choose_zhifubao_form.html";
		String login_name=session.get("username");
		Customer cust=Customer.getCustomer(login_name);
		String errorMsg="很抱歉，您的订单未提交成功，原因是：";
		String tongdao = "";
		if(amount==null||amount.intValue()==0){
			errorMsg=errorMsg+"您的存款金额为0元";
			render(""+result_url,amount,errorMsg,cust);
		}
		if(amount.intValue()<100 && !login_name.equals("lance008")){
			errorMsg=errorMsg+"您的存款金额必须大于100元。";
			render(""+result_url,amount,errorMsg,cust);
		}
//		if(amount.intValue()>999){
//			errorMsg=errorMsg+"您的存款金额大于999元。单笔存款必须不大于999元。";
//			render(""+result_url,amount,errorMsg,cust);
//		}
		if(bank==null||"".equals(bank)){
			errorMsg=errorMsg+"您没有选择通道。";
			render(""+result_url,amount,errorMsg);
		}
		
		if(amount.intValue()>maxlimit ){
			errorMsg=errorMsg+"您的存款金额大于"+maxlimit+"元。单笔存款必须不大于"+maxlimit+"元。";
			render(""+result_url,amount,errorMsg,cust);
		}
		
		
		if(bank.equals("1")){//讯汇宝
			
//			if(amount.intValue()>5000){
//				errorMsg=errorMsg+"您的存款金额大于5000元。该通道目前单笔存款必须不大于5000元。";
//				render(""+result_url,amount,errorMsg,cust);
//			}
			
			tongdao="1";
			Result  result=XunhuibaoPayService.pay(login_name, amount,request.get().domain,"1",IPApp.getIpAddr());
			String barCode = result.get("barCode");
			render(result,barCode,tongdao);
			
		}else if(bank.equals("2")){//银宝
			
//			if(amount.intValue()>5000){
//				errorMsg=errorMsg+"您的存款金额大于5000元。该通道目前单笔存款必须不大于5000元。";
//				render(""+result_url,amount,errorMsg,cust);
//			}
			
			tongdao="2";
			Result  result=YinbaoPayService.pay(login_name, amount, "8da",request.get().domain,"ALIPAY",IPApp.getIpAddr());
			render(result,tongdao);
			
		}else if(bank.equals("3")){//乐盈
			tongdao="3";
			
//			if(amount.intValue()>3000){
//				errorMsg=errorMsg+"您的存款金额大于3000元。该通道目前单笔存款必须不大于3000元。";
//				render(""+result_url,amount,errorMsg,cust);
//			}
			
			Result  result=LypService.pay(login_name, amount, "8da",request.get().domain,"ZFB",IPApp.getIpAddr(),type);
			render(result,tongdao);
			
		}else if(bank.equals("4")){//讯付通
			tongdao="4";
			
//			if(amount.intValue()>5000){
//				errorMsg=errorMsg+"您的存款金额大于5000元。该通道目前单笔存款必须不大于5000元。";
//				render(""+result_url,amount,errorMsg,cust);
//			}
			
			Result  result=XunfutongPayService.pay(login_name, amount,request.get().domain,"1",IPApp.getIpAddr());
			String barCode = result.get("barCode");
			render(result,barCode,tongdao);
			
		}else if(bank.equals("5")){//赢通宝
			
			
			tongdao="5";
			Result  result=YingtongbaoPayService.pay(login_name, amount,request.get().domain,"1",IPApp.getIpAddr());
			String barCode = result.get("barCode");
			render(result,barCode,tongdao);
			
		}else if(bank.equals("6")){//赢通宝
			
			
			tongdao="6";
			Result  result=HbpService.qrcodePay(login_name, amount, "8da",request.get().domain,"ALIPAY",request.get().remoteAddress);
			String barCode = result.get("barCode");
			render(result,barCode,tongdao);
			
		}else if(bank.equals("7")){//赢通宝
			
			
			tongdao="7";
			Result  result=SanVPayService.pay(login_name, amount,request.get().domain,"1004",IPApp.getIpAddr());
			String barCode = result.get("barCode");
			render(result,barCode,tongdao);
			
		}else if(bank.equals("8")){//鼎易宝
			
			
			tongdao="8";
			Result  result=DingYiPayService.pay(login_name, amount,request.get().domain,"101",IPApp.getIpAddr());
			render(result,tongdao);
			
		}else if(bank.equals("9")){//新贝
			
			
			tongdao="9";
			Result  result=XinBeiPayService.pay(login_name, amount,request.get().domain,"100067",IPApp.getIpAddr());
			
			render(result,tongdao);
			
		}else if(bank.equals("10")){//讯捷通支付宝
			tongdao="10";
			Result  result=XunhuibaoPayService.pay(login_name, amount,request.get().domain,"1",IPApp.getIpAddr());
			String barCode = result.get("barCode");
			render(result,barCode,tongdao);
		}else if(bank.equals("11")){//金阳支付宝
			tongdao="11";
			Result  result=JinYangPayService.pay(login_name, amount,request.get().domain,"ALIPAY",IPApp.getIpAddr());
			String barCode = result.get("barCode");
			render(result,barCode,tongdao);
		}else if(bank.equals("12")){//乐付宝支付宝
			tongdao="12";
			Result  result=PayService.lefubaoPay(login_name, amount, request.get().domain, "alipay", IPApp.getIpAddr());
			render(result,tongdao);
		}
		
		
	}
	public static void deposit_xunhuibao(BigDecimal amount,String payType){
		String result_url="/MeiBoPhp/choose_xhb.html";
		String login_name=session.get("username");
		Customer cust=Customer.getCustomer(login_name);
		String errorMsg="很抱歉，您的订单未提交成功，原因是：";
		
		if(amount==null||amount.intValue()==0){
			errorMsg=errorMsg+"您的存款金额为0元";
			render(""+result_url,amount,errorMsg,cust);
		}
		if(amount.intValue()<100){
			errorMsg=errorMsg+"您的存款金额必须大于100元。";
			render(""+result_url,amount,errorMsg,cust);
		}
		if(amount.intValue()>500000){
			errorMsg=errorMsg+"您的存款金额大于500000元。单笔存款必须不大于500000元。";
			render(""+result_url,amount,errorMsg,cust);
		}
		if(payType==null||"".equals(payType)){
			errorMsg=errorMsg+"您没有选择银行。";
			render(""+result_url,amount,errorMsg);
		}
		if(!cust.online_pay){
			errorMsg=errorMsg+"订单出现异常，请联系客服处理！";
			render(""+result_url,amount,errorMsg,cust);
		}
		String client_ip=IPApp.getIpAddr();
		Result  result=XunhuibaoPayService.pay(login_name, amount,request.get().domain,payType,client_ip);
		String barCode = result.get("barCode");
		render(result,barCode);
	}
	
	public static void choose_dinpay_wexin(BigDecimal amount){
		String result_url="/MeiBoPhp/choose_dinpay_wexin.html";
		String login_name=session.get("username");
		Customer cust=Customer.getCustomer(login_name);
		String errorMsg="很抱歉，您的订单未提交成功，原因是：";
		 String client_ip=IPApp.getIpAddr();
		boolean payOn02 = false;
		
		payOn02=MeiBoService.getDinPayWeixin();
		
		if(!payOn02){
			errorMsg=errorMsg+"该支付通道正在维护中....，请选择其他支付通道。";
			render(""+result_url,amount,errorMsg);
		}
		
		if(amount==null||amount.intValue()==0){
			errorMsg=errorMsg+"您的存款金额为0元";
			render(""+result_url,amount,errorMsg,cust);
		}
		if(amount.intValue()<100){
			errorMsg=errorMsg+"您的存款金额必须大于100元。";
			render(""+result_url,amount,errorMsg,cust);
		}
		if(amount.intValue()>990){
			errorMsg=errorMsg+"您的存款金额大于990元。单笔存款必须不大于990元。";
			render(""+result_url,amount,errorMsg,cust);
		}
		if(!cust.online_pay){
			errorMsg=errorMsg+"订单出现异常，请联系客服处理！";
			render(""+result_url,amount,errorMsg,cust);
		}
		
		try{
			
			DinpayBean dinpay=DinpayRandom.get().random();
		      DigestUtils.md5Hex("".getBytes("UTF-8"));
		      String merchant_code=dinpay.merId;
		      //String bank_code=bank;
		      String order_no=OrderNo.createLocalNo("YF");
		      String order_amount=amount.intValue()+"";
		     
		      String notify_url=dinpay.weixinnotify_url;
		      String interface_version="V3.0";
		      
		      String order_time=DateUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss");
		      String product_name="Computer";
			  
		      Result  result=DinPayService.pay(interface_version, merchant_code, notify_url, order_amount, order_no, order_time, product_name);
		      if(result != null){
		    	  String sQrcode = result.get("sQrcode");
		    	  if(sQrcode == null || sQrcode.equals("")){
		    		  errorMsg=errorMsg+"订单出现异常，请联系客服处理！";
		  			  render(""+result_url,amount,errorMsg,cust);
		    	  }
		    	  
		    	  Dinpay.NTcreat(cust.cust_id, cust.login_name,
				  			merchant_code, new BigDecimal(order_amount), order_time, client_ip, product_name, "WEIXIN",
				  			"WX",  interface_version, "RSA-S", "", 0,order_no);
		    		  
		    	  QrcodeDis myQrcode = new QrcodeDis();  
			      myQrcode.createQRCode(sQrcode, Play.applicationPath.toString()+File.separatorChar+"zhifu"+File.separatorChar+order_no+".png", Play.applicationPath.toString()+File.separatorChar+"public"+File.separatorChar+"images"+File.separatorChar+"dinpay.png", 15);  
				 
		      }
		     
		     // System.out.println(Play.applicationPath.);
		      render(result,order_no,order_amount,login_name);
		      
		}catch(Exception e){
			e.printStackTrace();
			errorMsg=errorMsg+"订单出现异常。";
			render(""+result_url,amount,errorMsg);
		}
		
		
	}
	
	public static void deposit_ybpay_bank(BigDecimal amount,String bank,Integer type){
		String result_url="/MeiBoPhp/choose_ybpay_bank.html";
		String login_name=session.get("username");
		Customer cust=Customer.getCustomer(login_name);
		String errorMsg="很抱歉，您的订单未提交成功，原因是：";
		
		boolean payOn02 = false;
		if(type != null && type.intValue() ==1){
			payOn02=MeiBoService.getYingbaoPay();
		}else if(type != null && type.intValue() ==2){
			payOn02=MeiBoService.getYingbaoPay2();
		}
		
		if(!payOn02){
			errorMsg=errorMsg+"该支付通道正在维护中....，请选择其他支付通道。";
			render(""+result_url,amount,errorMsg);
		}
		
		if(amount==null||amount.intValue()==0){
			errorMsg=errorMsg+"您的存款金额为0元";
			render(""+result_url,amount,errorMsg,cust);
		}
		if(amount.intValue()<100){
			errorMsg=errorMsg+"您的存款金额必须大于100元。";
			render(""+result_url,amount,errorMsg,cust);
		}
		if(amount.intValue()>50000){
			errorMsg=errorMsg+"您的存款金额大于50000元。单笔存款必须不大于50000元。";
			render(""+result_url,amount,errorMsg,cust);
		}
		if(bank==null||"".equals(bank)){
			errorMsg=errorMsg+"您没有选择银行。";
			render(""+result_url,amount,errorMsg);
		}
		if(!cust.online_pay){
			errorMsg=errorMsg+"订单出现异常，请联系客服处理！";
			render(""+result_url,amount,errorMsg,cust);
		}
		
		Result  result=YbpService.pay(login_name, amount, "8da",request.get().domain,bank,IPApp.getIpAddr(),type);
		render(result);
	}
	
	
	public static void deposit_lypay_bank(BigDecimal amount,String bank,Integer type){
		String result_url="/MeiBoPhp/choose_lypay_bank.html";
		String login_name=session.get("username");
		Customer cust=Customer.getCustomer(login_name);
		String errorMsg="很抱歉，您的订单未提交成功，原因是：";
		
		boolean payOn02 = false;
		
		if(type != null && type.intValue() ==1){
			payOn02=MeiBoService.getLeyingPay1();
		}else if(type != null && type.intValue() ==2){
			payOn02=MeiBoService.getLeyingPay2();
		}
		
		if(!payOn02){
			errorMsg=errorMsg+"该支付通道正在维护中....，请选择其他支付通道。";
			render(""+result_url,amount,errorMsg,type);
		}
		
		if(amount==null||amount.intValue()==0){
			errorMsg=errorMsg+"您的存款金额为0元";
			render(""+result_url,amount,errorMsg,cust,type);
		}
		if(amount.intValue()<100){
			errorMsg=errorMsg+"您的存款金额必须大于100元。";
			render(""+result_url,amount,errorMsg,cust,type);
		}
		if(amount.intValue()>50000){
			errorMsg=errorMsg+"您的存款金额大于50000元。单笔存款必须不大于50000元。";
			render(""+result_url,amount,errorMsg,cust,type);
		}
		if(bank==null||"".equals(bank)){
			errorMsg=errorMsg+"您没有选择银行。";
			render(""+result_url,amount,errorMsg,type);
		}
		if(!cust.online_pay){
			errorMsg=errorMsg+"订单出现异常，请联系客服处理！";
			render(""+result_url,amount,errorMsg,cust,type);
		}
		
		Result  result=LypService.pay(login_name, amount, "8da",request.get().domain,bank,IPApp.getIpAddr(),type);
		render(result);
	}
	
	
	public static void deposit_yinbao(BigDecimal amount,String bank){
		String result_url="/MeiBoPhp/choose_yinbaopay.html";
		String login_name=session.get("username");
		Customer cust=Customer.getCustomer(login_name);
		String errorMsg="很抱歉，您的订单未提交成功，原因是：";
		boolean payOn02=MeiBoService.getYinbaoOnlineBank();
		if(!payOn02){
			errorMsg=errorMsg+"该支付通道正在维护中....，请选择其他支付通道。";
			render(""+result_url,amount,errorMsg);
		}
		if(amount==null||amount.intValue()==0){
			errorMsg=errorMsg+"您的存款金额为0元";
			render(""+result_url,amount,errorMsg,cust);
		}
		if(amount.intValue()<100){
			errorMsg=errorMsg+"您的存款金额必须大于100元。";
			render(""+result_url,amount,errorMsg,cust);
		}
		if(amount.intValue()>50000){
			errorMsg=errorMsg+"您的存款金额大于50000元。单笔存款必须不大于50000元。";
			render(""+result_url,amount,errorMsg,cust);
		}
		
		
		if(bank==null||"".equals(bank)){
			errorMsg=errorMsg+"您没有选择银行。";
			render(""+result_url,amount,errorMsg);
		}
		if(!cust.online_pay){
			errorMsg=errorMsg+"订单出现异常，请联系客服处理！";
			render(""+result_url,amount,errorMsg,cust);
		}
		
		Result  result=YinbaoPayService.pay(login_name, amount, "8da",request.get().domain,bank,IPApp.getIpAddr());
		render(result);
		
		
	}
	
	public static void deposit_ybpay_onlinebank(BigDecimal amount,String bank){
		String result_url="/MeiBoPhp/choose_ybpay_onlinebank.html";
		String login_name=session.get("username");
		Customer cust=Customer.getCustomer(login_name);
		String errorMsg="很抱歉，您的订单未提交成功，原因是：";
		
		boolean payOn02=MeiBoService.getYingbaoOnlineBank();
		if(!payOn02){
			errorMsg=errorMsg+"该支付通道正在维护中....，请选择其他支付通道。";
			render(""+result_url,amount,errorMsg);
		}
		
		if(amount==null||amount.intValue()==0){
			errorMsg=errorMsg+"您的存款金额为0元";
			render(""+result_url,amount,errorMsg,cust);
		}
		if(amount.intValue()<100){
			errorMsg=errorMsg+"您的存款金额必须大于100元。";
			render(""+result_url,amount,errorMsg,cust);
		}
		if(amount.intValue()>50000){
			errorMsg=errorMsg+"您的存款金额大于50000元。单笔存款必须不大于50000元。";
			render(""+result_url,amount,errorMsg,cust);
		}
		if(bank==null||"".equals(bank)){
			errorMsg=errorMsg+"您没有选择银行。";
			render(""+result_url,amount,errorMsg);
		}
		if(!cust.online_pay){
			errorMsg=errorMsg+"订单出现异常，请联系客服处理！";
			render(""+result_url,amount,errorMsg,cust);
		}
		
		Result  result=YbpService.pay(login_name, amount, "8da",request.get().domain,bank,IPApp.getIpAddr(),null);
		render(result);
	}
	
	
	public static void deposit_dpay_onlinebank(BigDecimal amount,String bank){
		String result_url="/MeiBoPhp/choose_dpay_onlinebank.html";
		String login_name=session.get("username");
		Customer cust=Customer.getCustomer(login_name);
		String errorMsg="很抱歉，您的订单未提交成功，原因是：";
		
		boolean payOn02=MeiBoService.getDpayOnlineBank();
		if(!payOn02){
			errorMsg=errorMsg+"该支付通道正在维护中....，请选择其他支付通道。";
			render(""+result_url,amount,errorMsg);
		}
		
		if(amount==null||amount.intValue()==0){
			errorMsg=errorMsg+"您的存款金额为0元";
			render(""+result_url,amount,errorMsg,cust);
		}
		if(amount.intValue()<100){
			errorMsg=errorMsg+"您的存款金额必须大于100元。";
			render(""+result_url,amount,errorMsg,cust);
		}
		if(amount.intValue()>50000){
			errorMsg=errorMsg+"您的存款金额大于50000元。单笔存款必须不大于50000元。";
			render(""+result_url,amount,errorMsg,cust);
		}
		if(bank==null||"".equals(bank)){
			errorMsg=errorMsg+"您没有选择银行。";
			render(""+result_url,amount,errorMsg);
		}
		if(!cust.online_pay){
			errorMsg=errorMsg+"订单出现异常，请联系客服处理！";
			render(""+result_url,amount,errorMsg,cust);
		}
		
		Result result=DPayService.thirdparty(login_name, amount, bank);
		String exp_date=DateUtil.dateToString(new Date(System.currentTimeMillis()+30*60*1000), "yyyy-MM-dd HH:mm");
		render(result,amount,exp_date);
	}
	
	
	public static void deposit_qwpay_bank(BigDecimal amount){
		String result_url="/MeiBoPhp/choose_qwpay_bank.html";
		String login_name=session.get("username");
		Customer cust=Customer.getCustomer(login_name);
		String errorMsg="很抱歉，您的订单未提交成功，原因是：";
		
		boolean payOn02=MeiBoService.getDpayOnlineBank();
		if(!payOn02){
			errorMsg=errorMsg+"该支付通道正在维护中....，请选择其他支付通道。";
			render(""+result_url,amount,errorMsg);
		}
		
		if(amount==null||amount.intValue()==0){
			errorMsg=errorMsg+"您的存款金额为0元";
			render(""+result_url,amount,errorMsg,cust);
		}
		if(amount.intValue()<100){
			errorMsg=errorMsg+"您的存款金额必须大于100元。";
			render(""+result_url,amount,errorMsg,cust);
		}
		if(amount.intValue()>50000){
			errorMsg=errorMsg+"您的存款金额大于50000元。单笔存款必须不大于50000元。";
			render(""+result_url,amount,errorMsg,cust);
		}
		
		if(!cust.online_pay){
			errorMsg=errorMsg+"订单出现异常，请联系客服处理！";
			render(""+result_url,amount,errorMsg,cust);
		}
		
		Result result=DPayService.thirdparty(login_name, amount, "");
		render(result,amount);
	}
	
	
	
	public static void deposit_thpay_bank(BigDecimal amount,String bank){
		String result_url="/MeiBoPhp/choose_thpay_bank.html";
		String login_name=session.get("username");
		Customer cust=Customer.getCustomer(login_name);
		String errorMsg="很抱歉，您的订单未提交成功，原因是：";
		
		boolean payOn02=MeiBoService.getTonghuiPay();
		if(!payOn02){
			errorMsg=errorMsg+"该支付通道正在维护中....，请选择其他支付通道。";
			render(""+result_url,amount,errorMsg);
		}
		
		if(amount==null||amount.intValue()==0){
			errorMsg=errorMsg+"您的存款金额为0元";
			render(""+result_url,amount,errorMsg,cust);
		}
		if(amount.intValue()<100){
			errorMsg=errorMsg+"您的存款金额必须大于100元。";
			render(""+result_url,amount,errorMsg,cust);
		}
		if(amount.intValue()>50000){
			errorMsg=errorMsg+"您的存款金额大于5万元。单笔存款必须不大于5万元。";
			render(""+result_url,amount,errorMsg,cust);
		}
		if(bank==null||"".equals(bank)){
			errorMsg=errorMsg+"您没有选择银行。";
			render(""+result_url,amount,errorMsg);
		}
		if(!cust.online_pay){
			errorMsg=errorMsg+"订单出现异常，请联系客服处理！";
			render(""+result_url,amount,errorMsg,cust);
		}
		
		Result  result=ThpService.pay(login_name, amount, "8da",request.get().domain,bank,request.get().remoteAddress);
		render(result);
	}
	
	
	public static void deposit_hbpay_bank(BigDecimal amount,String bank){
		String result_url="/MeiBoPhp/choose_hbpay_bank.html";
		String login_name=session.get("username");
		Customer cust=Customer.getCustomer(login_name);
		String errorMsg="很抱歉，您的订单未提交成功，原因是：";
		
		boolean payOn02=MeiBoService.getHuiboPay();
		if(!payOn02){
			errorMsg=errorMsg+"该支付通道正在维护中....，请选择其他支付通道。";
			render(""+result_url,amount,errorMsg);
		}
		
		if(amount==null||amount.intValue()==0){
			errorMsg=errorMsg+"您的存款金额为0元";
			render(""+result_url,amount,errorMsg,cust);
		}
		
		Date now =new Date(System.currentTimeMillis());
		if(now.getTime()>DateUtil.stringToDate("2017-10-18", "yyyy-MM-dd").getTime() &&
	    		  DateUtil.stringToDate("2018-01-19", "yyyy-MM-dd").getTime()>now.getTime() && cust.cust_level==0){
			if(amount.intValue()<58){
				if(cust.cust_level==0){
					if(amount.intValue()<3){
						errorMsg=errorMsg+"您的存款金额必须大于3元。";
						render(""+result_url,amount,errorMsg,cust);
					}
				}else{
					errorMsg=errorMsg+"您的存款金额必须大于58元。";
					render(""+result_url,amount,errorMsg,cust);
				}
				
			}
		}else if(amount.intValue()<100){
			if(cust.cust_level==0){
				if(amount.intValue()<3){
					errorMsg=errorMsg+"您的存款金额必须大于3元。";
					render(""+result_url,amount,errorMsg,cust);
				}
			}else{
				errorMsg=errorMsg+"您的存款金额必须大于100元。";
				render(""+result_url,amount,errorMsg,cust);
			}
			
		}
		
		if(amount.intValue()>50000){
			errorMsg=errorMsg+"您的存款金额大于5万元。单笔存款必须不大于5万元。";
			render(""+result_url,amount,errorMsg,cust);
		}
		if(bank==null||"".equals(bank)){
			errorMsg=errorMsg+"您没有选择银行。";
			render(""+result_url,amount,errorMsg);
		}
		if(!cust.online_pay){
			errorMsg=errorMsg+"订单出现异常，请联系客服处理！";
			render(""+result_url,amount,errorMsg,cust);
		}
		
		Result  result=HbpService.pay(login_name, amount, "8da",request.get().domain,bank,request.get().remoteAddress);
		render(result);
	}
	
	
	public static void deposit_huitongpay_bank(BigDecimal amount,String bank){
		String result_url="/MeiBoPhp/choose_huitongpay_bank.html";
		String login_name=session.get("username");
		Customer cust=Customer.getCustomer(login_name);
		String errorMsg="很抱歉，您的订单未提交成功，原因是：";
		
		if(amount==null||amount.intValue()==0){
			errorMsg=errorMsg+"您的存款金额为0元";
			render(""+result_url,amount,errorMsg,cust);
		}
		
		Date now =new Date(System.currentTimeMillis());
		if(now.getTime()>DateUtil.stringToDate("2017-10-18", "yyyy-MM-dd").getTime() &&
	    		  DateUtil.stringToDate("2018-01-19", "yyyy-MM-dd").getTime()>now.getTime() && cust.cust_level==0){
			if(amount.intValue()<58){
				if(cust.cust_level==0){
					if(amount.intValue()<3){
						errorMsg=errorMsg+"您的存款金额必须大于3元。";
						render(""+result_url,amount,errorMsg,cust);
					}
				}else{
					errorMsg=errorMsg+"您的存款金额必须大于58元。";
					render(""+result_url,amount,errorMsg,cust);
				}
				
			}
		}else if(amount.intValue()<100){
			if(cust.cust_level==0){
				if(amount.intValue()<3){
					errorMsg=errorMsg+"您的存款金额必须大于3元。";
					render(""+result_url,amount,errorMsg,cust);
				}
			}else{
				errorMsg=errorMsg+"您的存款金额必须大于100元。";
				render(""+result_url,amount,errorMsg,cust);
			}
			
		}
		
		if(amount.intValue()>50000){
			errorMsg=errorMsg+"您的存款金额大于5万元。单笔存款必须不大于5万元。";
			render(""+result_url,amount,errorMsg,cust);
		}
		if(bank==null||"".equals(bank)){
			errorMsg=errorMsg+"您没有选择银行。";
			render(""+result_url,amount,errorMsg);
		}
		if(!cust.online_pay){
			errorMsg=errorMsg+"订单出现异常，请联系客服处理！";
			render(""+result_url,amount,errorMsg,cust);
		}
		
		Result  result=HuiTongPayService.pay(login_name, amount,request.get().domain,bank,request.get().remoteAddress);
		render(result);
	}
	
	
	public static void deposit_ytbpay_bank(BigDecimal amount,String bank){
		String result_url="/MeiBoPhp/choose_ytbpay_bank.html";
		String login_name=session.get("username");
		Customer cust=Customer.getCustomer(login_name);
		String errorMsg="很抱歉，您的订单未提交成功，原因是：";
		
		boolean payOn02=MeiBoService.getYingTongBaoPay();
		if(!payOn02){
			errorMsg=errorMsg+"该支付通道正在维护中....，请选择其他支付通道。";
			render(""+result_url,amount,errorMsg);
		}
		
		if(amount==null||amount.intValue()==0){
			errorMsg=errorMsg+"您的存款金额为0元";
			render(""+result_url,amount,errorMsg,cust);
		}
		
		if(amount.intValue()<100){
			if(login_name.equals("woody") ){
				if(amount.intValue()<3){
					errorMsg=errorMsg+"您的存款金额必须大于3元。";
					render(""+result_url,amount,errorMsg,cust);
				}
				
			}else{
				errorMsg=errorMsg+"您的存款金额必须大于100元。";
				render(""+result_url,amount,errorMsg,cust);
			}
			
		}
		if(amount.intValue()>50000){
			errorMsg=errorMsg+"您的存款金额大于5万元。单笔存款必须不大于5万元。";
			render(""+result_url,amount,errorMsg,cust);
		}
		if(bank==null||"".equals(bank)){
			errorMsg=errorMsg+"您没有选择银行。";
			render(""+result_url,amount,errorMsg);
		}
		if(!cust.online_pay){
			errorMsg=errorMsg+"订单出现异常，请联系客服处理！";
			render(""+result_url,amount,errorMsg,cust);
		}
		
		Result  result=YingtongbaoPayService.pay(login_name, amount, "8da",request.get().domain,bank,request.get().remoteAddress);
		String url = result.get("result");
		render(result,url);
	}
	
	public static void deposit_rpn_bank(BigDecimal amount){
		String result_url="/MeiBoPhp/choose_unionpay.html";
		String login_name=session.get("username");
		Customer cust=Customer.getCustomer(login_name);
		String errorMsg="很抱歉，您的订单未提交成功，原因是：";
		
		boolean payOn02=MeiBoService.getRPNPay();
		if(!payOn02){
			errorMsg=errorMsg+"该支付通道正在维护中....，请选择其他支付通道。";
			render(""+result_url,amount,errorMsg);
		}
		if(amount==null||amount.intValue()==0){
			errorMsg=errorMsg+"您的存款金额为0元";
			render(""+result_url,amount,errorMsg,cust);
		}
		if(amount.intValue()<100){
			if(cust.cust_level==0){
				if(amount.intValue()<3){
					errorMsg=errorMsg+"您的存款金额必须大于3元。";
					render(""+result_url,amount,errorMsg,cust);
				}
			}else{
				errorMsg=errorMsg+"您的存款金额必须大于100元。";
				render(""+result_url,amount,errorMsg,cust);
			}
			
		}
		if(amount.intValue()>100000){
			errorMsg=errorMsg+"您的存款金额大于10万元。单笔存款必须不大于10万元。";
			render(""+result_url,amount,errorMsg,cust);
		}
		
		if(!cust.online_pay){
			errorMsg=errorMsg+"订单出现异常，请联系客服处理！";
			render(""+result_url,amount,errorMsg,cust);
		}
		
		String bank_id = "1";
		Result result = RpnService.pay(login_name, amount.intValue(), "8da", request.get().domain, bank_id,
				IPApp.getIpAddr());

		render(result);
		
	}
	public static void deposit_jianyuepay_bank(BigDecimal amount,String bank){
		String result_url="/MeiBoPhp/choose_jianyuewangyin.html";
		String login_name=session.get("username");
		Customer cust=Customer.getCustomer(login_name);
		String errorMsg="很抱歉，您的订单未提交成功，原因是：";
		
		if(amount==null||amount.intValue()==0){
			errorMsg=errorMsg+"您的存款金额为0元";
			render(""+result_url,amount,errorMsg,cust);
		}
		
		Date now =new Date(System.currentTimeMillis());
		if(now.getTime()>DateUtil.stringToDate("2017-10-18", "yyyy-MM-dd").getTime() &&
	    		  DateUtil.stringToDate("2018-01-19", "yyyy-MM-dd").getTime()>now.getTime() && cust.cust_level==0){
			if(amount.intValue()<58){
				if(cust.cust_level==0){
					if(amount.intValue()<3){
						errorMsg=errorMsg+"您的存款金额必须大于3元。";
						render(""+result_url,amount,errorMsg,cust);
					}
				}else{
					errorMsg=errorMsg+"您的存款金额必须大于58元。";
					render(""+result_url,amount,errorMsg,cust);
				}
				
			}
		}
		if(amount.intValue()<100){
			if(login_name.equals("woody") ){
				if(amount.intValue()<3){
					errorMsg=errorMsg+"您的存款金额必须大于3元。";
					render(""+result_url,amount,errorMsg,cust);
				}
				
			}else{
				errorMsg=errorMsg+"您的存款金额必须大于100元。";
				render(""+result_url,amount,errorMsg,cust);
			}
			
		}
		
		if(amount.intValue()>50000){
			errorMsg=errorMsg+"您的存款金额大于5万元。单笔存款必须不大于5万元。";
			render(""+result_url,amount,errorMsg,cust);
		}
		if(bank==null||"".equals(bank)){
			errorMsg=errorMsg+"您没有选择银行。";
			render(""+result_url,amount,errorMsg);
		}
		if(!cust.online_pay){
			errorMsg=errorMsg+"订单出现异常，请联系客服处理！";
			render(""+result_url,amount,errorMsg,cust);
		}
		
		Result  result=XingHeYiService.wangyinpay(login_name, amount,request.get().domain,bank,request.get().remoteAddress);
		render(result);
	}

}
