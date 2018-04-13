package controllers;

import java.io.File;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import com.ws.service.MoleHitService;

import bsz.exch.service.Result;
import bsz.exch.service.Task;
import bsz.exch.utils.DateUtil;
import groovy.lang.Closure;
import groovy.transform.TimedInterrupt;
import models.Customer;
import play.cache.Cache;
import play.jobs.Every;
import play.mvc.Before;
import play.mvc.Controller;
import play.templates.GroovyTemplate.ExecutableTemplate;
import service.CustomerService;
import util.JSONResult;
import util.MD5;

public class MyActive extends Controller {
	 
	@Before
	 public static void before(){
		if(IPApp.isxss()){
			renderText("您的输入存在非法字符!");
		}
	}
	public static void index(){
		render("/MyActive/index.html");

	}
	/*public static void hit(){
		render("/MyActive/hit.html");
	}*/
	/*public static void reg() throws ParseException{
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd"); 
		Date dateAfter=df.parse("2017-6-17");
	    Date dateBefor=df.parse("2017-8-19");
	    Date dateCurrent=df.parse(df.format(new Date()));
	    Boolean flag=false;
	    int remainReg=0;
	    //获取当天注册量
	    if(dateCurrent.after(dateAfter) && dateCurrent.before(dateBefor)){
	    	flag=true;
	    	if(new Date().getHours()<10){
	    		flag=false;
	    	}else{
	    		 int regcount = CustomerService.getCurrentRegcount();
	    		    int h = new Date().getHours();
	    		    int m = new Date().getMinutes();
	    		    int totalmin = h*60+m;
	    		    Date no=df.parse("2017-07-27");
	    		    if(dateCurrent.getDate()==no.getDate()){
	    		    	remainReg=0;
	    		    	flag=true;
	    		    }else{
	    		    	 remainReg = 500-regcount;
	    		  	   if(remainReg<=0)remainReg=0;
	    		    }
	    	}
	    }
	    
	   
	   
		render("/MyActive/reg.html",flag,remainReg,dateAfter,dateBefor);
	}*/
	
	/*public static void registered(){
		
		String login_name=params.get("loginName");
		String real_name =params.get("realName");
		String login_pwd=params.get("pwd");
		//String login_repwd=params.get("confirm_pwd");
		String vericode =params.get("veriCode");
		String veriCode=(String)Cache.get("veriCode2" + session.getId());
		String phone = params.get("phonename");
		String qq = params.get("qq");
		int ifsuccess = 0;
		//String email = params.get("email");
		
		String errorMsg ="";
		errorMsg="恭喜你!注册成功";
		render("/BoJin/registered.html", errorMsg,login_name,dataList);
		
		int regcount = CustomerService.getCurrentRegcount();
		if(regcount>500){
			errorMsg="真遗憾，今天名额已满，明天再来吧。";
			 render("/BoJin/registered.html", errorMsg,ifsuccess);
		}
		int h = new Date().getHours();
		if(h<10){
			errorMsg="请勿非法操作。";
			 render("/BoJin/registered.html", errorMsg,ifsuccess);
		}
		
		String ip=IPApp.getIpAddr();
		 if(CustomerService.getRegCntToday(ip)>0){
			 errorMsg="注册失败，系统检测到您频繁注册，<br>如您切实有意向开户，请联系在线客服为您开立账户!";
			 render("/BoJin/registered.html", errorMsg,ifsuccess);
		 }
		 
		 
		if(login_name==null||"".equals(login_name)){
			 errorMsg="注册失败，请填写您的用户名!";
			 render("/BoJin/registered.html", errorMsg,ifsuccess);
		}
		if(CustomerService.NTLoginnameexist(login_name)){
			errorMsg="注册失败，你的用户名被别人抢先一步注册了!";
			render("/BoJin/registered.html", errorMsg,ifsuccess);
		}
		
		if(real_name==null){
			 errorMsg="注册失败，真实姓名不为空!";
			render("/BoJin/registered.html", errorMsg,ifsuccess);
		}
		if(login_pwd==null|| 6>login_pwd.length() || login_pwd.length()>16){
			 errorMsg="注册失败，请填写您的密码不符合要求!";
			render("/BoJin/registered.html", errorMsg,ifsuccess);
		}
		if(vericode==null || (!validation.equals(login_pwd,login_repwd).ok)){
			errorMsg="注册失败，两次密码不一致";
			 render("/BoJin/registered.html", errorMsg,dataList,ifsuccess);
		}
		
		if(phone==null||phone.length()!=11||!validation.phone(phone).ok){
			 errorMsg="注册失败，您的手机号码不符合规则!";
			render("/BoJin/registered.html", errorMsg,ifsuccess);
		 }
		 if(CustomerService.NTPhoneexist(phone)){
			 errorMsg="注册失败，您的手机号码已经被占用!";
			 render("/BoJin/registered.html", errorMsg,ifsuccess);
		 }
		 if(!validation.email(email).ok){
			 errorMsg="注册失败，您的email不符合规则!";
			 render("/BoJin/registered.html", errorMsg);
		 }
		 if("".equals(qq) || qq==null){
			 errorMsg="注册失败，qq不为空!";
			 render("/BoJin/registered.html", errorMsg,ifsuccess);
		 }
		 
		 if(vericode==null || "".equals(vericode)){
			 errorMsg="注册失败，验证码不为空";
			 render("/BoJin/registered.html", errorMsg,ifsuccess);
		 }
		 
		if(!validation.equals(veriCode.toLowerCase(),vericode.toLowerCase()).ok){
			errorMsg="注册失败，验证码有误";
			 render("/BoJin/registered.html", errorMsg,ifsuccess);
		}
		
		login_pwd=MD5.md5(login_pwd);
		String user_agent=request.headers.get("user-agent")==null?"":request.headers.get("user-agent").toString();
	  
		boolean createFlag = CustomerService.createCustomer(login_name.trim(),login_pwd,real_name,qq,phone,user_agent,ip);

		if(createFlag){
			session.put("username",login_name.trim());
			errorMsg="恭喜你!注册成功";
 			Boolean res=false;//是否可以抽奖
			//Date now =new Date(System.currentTimeMillis());
			//if((now.getTime()>DateUtil. ("2017-04-11", "yyyy-MM-dd").getTime())){
				//判断是否需要抽奖
				String regiest_flag = CustomerService.checkLotteryAuth(login_name);
				System.out.println(regiest_flag+"====");
				
				if(regiest_flag!=null){
					//renderText(JSONResult.success("regiestLottery.html"));
					//render("/BoJin/regiestLottery.html" );
					res=true;
				}
			//}
			
			//获取一条优惠信息
			ifsuccess = 1 ;
			render("/BoJin/registered.html", errorMsg,login_name,ifsuccess,res);
		}else{
			errorMsg="注册失败，请联系客服。";
			render("/BoJin/registered.html",errorMsg,ifsuccess);
		}
		
	}*/
	
	public static void doHit(){
		String login_name=session.get("username");
		Customer cust=Customer.getCustomer(login_name);
		if(login_name==null){
			render("BoJin/index.html");
		}
		String counttime=params.get("counttime");
		if(counttime!=null && Integer.parseInt(counttime)>3){
			renderText(JSONResult.failure("请勿非法操作。"));
		}
		String startDate="2017-09-16 00:00:00";
		String endDate="2017-10-31 23:59:59";
		
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		try {
			Date start = df.parse(startDate);
			Date enddate = df.parse(endDate);
			Date current = new Date();
			if("woody".equals(login_name)&&current.before(start)){
				Result hitinfo = MoleHitService.checkIfHit(login_name, "2017-09-15 00:00:00", endDate);
				if(hitinfo!=null && hitinfo.get("id")==null){
					String giftinfo = MoleHitService.hit(counttime,login_name);
					
					renderText(JSONResult.success(giftinfo));
				}
				renderText(JSONResult.success("\\n今天的次数已用完，明天再来吧"));
			}
			
			if(current.after(start) && current.before(enddate)){
				//获取砸中次数
				Result hitinfo = MoleHitService.checkIfHit(login_name, startDate, endDate);
				if(hitinfo!=null && hitinfo.get("id")==null && cust.promo_flag){
					String giftinfo = MoleHitService.hit(counttime,login_name);
					
					renderText(JSONResult.success(giftinfo));
				}
				renderText(JSONResult.success("\\n今天的次数已用完，明天再来吧"));
			}else{
				renderText(JSONResult.failure("请在活动时间操作。"));
			}
				
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
	}
	
	@SuppressWarnings("deprecation")
	public static void main(String[] rags) throws ParseException{
		/*SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd"); 
		Date dateAfter=df.parse("2017-6-11");
	    Date dateBefor=df.parse("2017-6-19");
	    Date dateCurrent=df.parse(df.format(new Date()));
	   
	    String a = String.valueOf(new Date().getHours());
	    String b = String.valueOf(new Date().getMinutes());
	    System.out.println(a+b);*/
		int h = new Date().getHours();
		System.out.println(h);
	}

}
