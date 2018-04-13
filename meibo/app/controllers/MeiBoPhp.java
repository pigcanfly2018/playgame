package controllers;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.ws.service.JinYangPayService;
import com.ws.service.PlatService;
import com.yeargift.YearGiftCache;
import com.yeargift.YearGiftCustBean;

import bsz.exch.service.Result;
import models.Bank;
import models.CashGift;
import models.CashGiftRowMap;
import models.Config;
import models.Customer;
import models.Deposit;
import models.DepositRowMap;
import models.EggGift;
import models.EggGiftRowMap;
import models.Hongbao;
import models.HongbaoRowMap;
import models.HuoliGift;
import models.Item;
import models.ItemRowMap;
import models.Letter;
import models.LetterRowMap;
import models.OrderNo;
import models.ScoreRec;
import models.ScoreRecRowMap;
import models.Withdraw;
import models.WithdrawRowMap;
import models.YearGift;
import models.YearGiftRowMap;
import play.Play;
import play.mvc.Before;
import play.mvc.Controller;
import service.CreditLogService;
import service.CustomerService;
import service.MeiBoService;
import util.DateUtil;
import util.DictRender;
import util.IPSeeker;
import util.JSONResult;
import util.MD5;
import util.MyRandom;
import util.PageUtil;
import util.Sp;
import util.Sqler;

public class MeiBoPhp extends Controller{
	@Before
	 public static void before(){
		if(IPApp.isxss()){
			renderText("您的输入存在非法字符!");
		}
		
		if(IPApp.fitter()){
			render("/errors/error.html");
		}
		
		if(session.get("username")==null){
			
			render("MeiBo/index.html");
		}
	}
	
	
	public static void user(){
		
		String login_name=session.get("username");
		Customer cust=Customer.getCustomer(login_name);
		
		int hongbaoflag = 0;
		int num = 0;
		Date now =new Date(System.currentTimeMillis());
		
		if((now.getTime()>DateUtil.stringToDate("2018-03-01 10:00:00", "yyyy-MM-dd HH:mm:ss").getTime()&&
				DateUtil.stringToDate("2018-03-16 23:59:59", "yyyy-MM-dd HH:mm:ss").getTime()>now.getTime() )
				 || "woody".equals(login_name) || "miffy2017".equals(login_name)){
			if(cust.cust_level==null||cust.cust_level.intValue()==0 ||!cust.promo_flag){
				
			}else{
				List<Hongbao> hongbaoList=Hongbao.NTgetLeft(cust.cust_id);
				if(hongbaoList.size() > 0){
					hongbaoflag = 1;
					num = hongbaoList.size();
				}
				
			}
			
		}
		
		/*String startDate="2017-09-16 00:00:00";
		String endDate="2017-10-31 23:59:59";
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Boolean show = false;
		try {
			Date start = df.parse(startDate);
			Date enddate = df.parse(endDate);
			Date current = new Date();
			if("woody".equals(login_name)&&current.before(start)){
				
				Result hitinfo = MoleHitService.checkIfHit(login_name, "2017-09-15 00:00:00", endDate);
				if(hitinfo!=null && hitinfo.get("id")==null){
					show=true;
				}
			}
			
			if(current.after(start) && current.before(enddate)){
				//获取砸中次数
				Result hitinfo = MoleHitService.checkIfHit(login_name, startDate, endDate);
				if(hitinfo!=null && hitinfo.get("id")==null && cust.promo_flag){
					show=true;
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		*/
		render(login_name,cust,hongbaoflag,num);
		
//		String login_name=session.get("username");
//		Customer cust=Customer.getCustomer(login_name);
//		int huoliflag = 0;
//		Date now =new Date(System.currentTimeMillis());
//		
//		
//		
//		if((!cust.promo_flag||now.getTime()<DateUtil.stringToDate("2017-05-25", "yyyy-MM-dd").getTime()||now.getTime()>DateUtil.stringToDate("2017-07-01", "yyyy-MM-dd").getTime()) ){
//			
//			render(login_name,cust,huoliflag);
//		}else{
//			huoliflag = 1;
//		}
//		
//		//获取用户签到记录2017-06-01日至2017-06-30日23:59
//		
//		
//		//String sql ="select sum(amount) from mb_deposit where login_name=? and status=3 and create_date >= ? and create_date < ? ";
//		//Object[] objs = new Object[]{login_name,"2016-08-15 00:00:00","2016-08-21 23:59:59"};
//		//BigDecimal deposit = Deposit.NTgetSumNewYear(cust.cust_id);
//		
//		List<Sign> list = Sign.getAll(login_name);
//		List signdate = new ArrayList();
//		//System.out.println(list.get(0).sign_date);
//		for(int i=0;i<list.size();i++){
//			signdate.add(list.get(i).sign_date);
//		}
//		
//		Calendar cal =  Calendar.getInstance();
//		cal.set(2017, 04, 31,0,0,0);
//		String record =  "";
//		long count = 0;
//		//获取最后一次领奖时间
//		SignReward reward = SignReward.getLatSignReward(login_name);
//		
//		for(int i=0;i<30;i++){
//			cal.add(Calendar.DATE, 1);
//			String nday = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
//			if(signdate.contains(nday)){
//				record  = record+"_"+(i+1);
//				count = count +1;
//				
//			}else{
//				record  = record+"_";
//				if(now.getTime()>cal.getTime().getTime() ){
//					count = 0;
//				}
//			}
//		}
//		
//		if(count>4 && reward != null){
//			String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
//			
//			long l;
//			try {
//				l = new SimpleDateFormat("yyyy-MM-dd").parse(today).getTime()-(new SimpleDateFormat("yyyy-MM-dd").parse(reward.reward_date).getTime());
//				
//				long day=l/(24*60*60*1000);
//				System.out.println(count+"    "+day);
//				count = day;
//			} catch (ParseException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//		}
//		
//		
//		
//		//System.out.println(record);
//		
//		
//		
//		render(login_name,cust,huoliflag,record,count);
//	
	}
	
	public static void signinCalendar(){
		render();
	}
	
	public static void newyear(){
		String login_name=session.get("username");
		Customer cust=Customer.getCustomer(login_name);
		
		Date now =new Date(System.currentTimeMillis());
		if((now.getTime()>DateUtil.stringToDate("2018-03-01 10:00:00", "yyyy-MM-dd HH:mm:ss").getTime()&&
				DateUtil.stringToDate("2018-03-16 23:59:00", "yyyy-MM-dd HH:mm:ss").getTime()>now.getTime()) || "woody".equals(login_name) || "miffy2017".equals(login_name)){
			if(cust.cust_level==null||cust.cust_level.intValue()==0 ||!cust.promo_flag ){  //星级0的不送
				renderText(JSONResult.success("0"));
			}else{
				//获取红包信息
				List<Hongbao> hongbaoList=Hongbao.NTgetLeft(cust.cust_id);
				BigDecimal credit=new BigDecimal(0);
				BigDecimal o= new BigDecimal(0);
				if(hongbaoList.size() > 0){
					
					if(cust.cust_level==null||cust.cust_level.intValue()==0){  //星级0的不送
						credit=new BigDecimal(0);
						renderText(JSONResult.success("0"));
					}
					
					/**
					 * 元宵节拆红包开始
					 */
					Hongbao hb =hongbaoList.get(0);
					
					String content = hb.content;
					if(!hb.content.contains("手机") && !hb.content.contains("充值卡")){
						content = content+"元点卡";
					}
					
					Long eggGiftId=EggGift.NTcreat(hb.gift_no, cust.cust_id, cust.login_name, cust.real_name, hb.gift_no,
							content, 1, 1, 
							null, new BigDecimal(0), new BigDecimal(0), "元宵节拆宝箱");
					if(!hb.content.contains("手机") && !hb.content.contains("充值卡")){
						 String giftCode=MyRandom.getRandom(8);
						 CashGift gift =new CashGift();
			        	 gift.gift_code=giftCode;
			        	 gift.login_name=cust.login_name;
			        	 gift.deposit_credit=new BigDecimal(0);
			        	 gift.valid_credit=new BigDecimal(0);
			        	 gift.net_credit=new BigDecimal(0);
			        	 gift.rate=Float.valueOf(1);
			        	 gift.payout=new BigDecimal(hb.content);
			        	 gift.gift_type="抽奖点卡";
			        	 gift.status=1;
			        	 gift.gift_no=OrderNo.createLocalNo("YuanXiao");
			     		 gift.cust_id=cust.cust_id;
			        	 gift.cs_date=new Date(System.currentTimeMillis());
			        	 gift.real_name=cust.real_name;
			        	 gift.cust_level=cust.cust_level;
			           	 gift.kh_date=cust.create_date;
			        	 gift.create_user=cust.login_name;
			        	 gift.create_date=new Date(System.currentTimeMillis());
			        	 Long giftId=gift.NTcreat();
			        	 gift.NTAuditGift(giftId, 3, "系统",hb.gift_no);
			        	 CustomerService.modCredit(cust.cust_id,CreditLogService.Gift, 
			 					gift.login_name,new BigDecimal(hb.content), "系统", "添加礼金"+gift.gift_type, gift.gift_no);
			        	 EggGift.NTAuditGift(eggGiftId, 3, "系统", gift.gift_no);
					}
					Hongbao.finishHongbao(hb.gift_no, hb.mb_hongbao_id);
					
						
					String dianquanflag="";
					if(hb.content.contains("手机")){
						 dianquanflag="3";
					}else if(hb.content.contains("充值卡")){
						dianquanflag="2";
					}else{
						dianquanflag="1";
					}
					renderText("{success:true,message:{dianquanflag:'"+dianquanflag+"',gift_flag:'"+hb.content+"'}}");
				}
				
				
			}
			renderText(JSONResult.success("0"));
		}
		
		
		
		
	}
	
	public static void logout(){
		session.remove("username");
		MeiBo.index();
	}
	
	
	public static void withdraw(){
		String login_name=session.get("username");
		Customer cust=Customer.getCustomer(login_name);
		boolean showBank=false;
		if(cust.account==null||cust.account.length()<7){
			showBank=true;
		}
		String errorMsg="温馨提示:";
		if(Withdraw.NTgetNoDoCount(cust.cust_id)>0){
			errorMsg=errorMsg+"您存在尚未处理的提款请求，请等待该提款处理完毕再提交。";
			render(errorMsg,showBank,cust);
		}
		render(showBank,cust);
	}
	
	public static void password(){
		String login_name=session.get("username");
		Customer customer=Customer.getCustomer(login_name);
		render(customer);
	}
	
	public static void mgalias(){
		String login_name=session.get("username");
		Customer customer=Customer.getCustomer(login_name);
		boolean ok = true;
		if(customer.mg_alias == null || customer.mg_alias.equals(""))
			ok = false;
		render(customer,ok);
	}
	
	public static void contact(){
		String login_name=session.get("username");
		Customer customer=Customer.getCustomer(login_name);
		customer.real_name=customer.real_name.substring(0,1)+"******";
		customer.phone=customer.phone.substring(0,4)+"******";
		if(customer.qq!=null&&customer.qq.length()>5){
			customer.qq=customer.qq.substring(0,5)+"******";
		}
		if(customer.email!=null&&customer.email.length()>5){
			customer.email=customer.email.substring(0,5)+"******";
		}
		render(customer);
	}
	public static void bank(){
		String login_name=session.get("username");
		Customer customer=Customer.getCustomer(login_name);
		boolean ok=false;
		if(customer!=null&&customer.account!=null&&customer.account.length()>5){
		     customer.account=customer.account.substring(0, 5)+"**********";
		     ok=true;
		}
		if(customer.bank_dot!=null&&customer.bank_dot.length()>2){
			customer.bank_dot=customer.bank_dot.substring(0, 2)+"**********";
		}
		render(customer,ok);
	}
	
	public static void deposit(){
		render();
	}
	
	
	public static void choose_online(){
		render();
	}
	
	public static void choose_onlinepay(){
		render();
	}
	
	public static void choose_point(){
		render();
	}
	
	public static void ptAutoLogin(){
		String username=params.get("username");
		String code=params.get("code");
		String login_name = username.substring(3).toLowerCase();
		Customer cust=Customer.getCustomer(login_name);
		boolean flag = PlatService.ptAutoLogin(username, code);
		if(flag){
			render("/MeiBoPhp/pttranfer.html",cust);
		}else{
			render("/errors/404.html");
		}
		
	}
	
	
	public static void deposit_rg01(){
		String login_name=session.get("username");
		Customer cust=Customer.getCustomer(login_name);
		String errorMsg="温馨提示：";
		if(Deposit.NTgetNoDoCnt(cust.cust_id)>0){
			 errorMsg=errorMsg+"您存在尚未处理的存款，请处理完毕后再提交。";
		     render(errorMsg);
		 }
		render(cust);
	}
	public static void deposit_bank(){
		String login_name=session.get("username");
		Customer cust=Customer.getCustomer(login_name);
		render(cust);
	}
	/**
	 * 提交人工存款信息
	 */
	public static void deposit_form(Long bank_id){
		String login_name=session.get("username");
		Customer cust=Customer.getCustomer(login_name);
		Bank bank=MeiBoService.getBank(bank_id);
		if(bank==null){
			deposit_bank();
		}
		//Bank bank0=MeiBoService.getBank(cust.cust_level, bank.bank_name);
		Bank bank0 = MeiBoService.getSpecifiedBank(login_name, bank.bank_name) ;
		if(bank0 == null){
			bank0= MeiBoService.getBank(cust.cust_level, bank.bank_name);
		}
		if(!bank.account.equals(bank0.account)){
			deposit_bank();
		}
		String bank_name=bank.bank_name;
		String account_name=bank.account_name;
		String errorMsg="温馨提示：";
		if(Deposit.NTgetNoDoCnt(cust.cust_id)>0){
			 errorMsg=errorMsg+"您存在尚未处理的存款，请处理完毕后再提交。";
			 render(errorMsg,cust,bank_name,account_name);
		 }
		render(errorMsg,cust,bank_name,account_name);
	}
	
	public static void deposit_alipay(){
		String login_name=session.get("username");
		Customer cust=Customer.getCustomer(login_name);
		Bank bank=null;
		if(cust.alipay_flag){
			bank=MeiBoService.getBank(cust.cust_level, "支付宝");
		}
		render(cust,bank);
	}
	
	public static void deposit_quickside(BigDecimal amount,String bank){
		
		String result_url="/MeiBoPhp/deposit_quickside.html";
		String errorMsg="很抱歉，您的订单未提交成功，原因是：";
		
		
		
		String error="0";
		String login_name=session.get("username");
		Customer cust=Customer.getCustomer(login_name);
		
		if(amount==null||amount.intValue()==0){
			error = "1";
			errorMsg=errorMsg+"您的存款金额为0元";
			render(""+result_url,amount,errorMsg,cust,error);
		}
		if(amount.intValue()<100){
			error = "1";
			errorMsg=errorMsg+"您的存款金额必须大于100元。";
			render(""+result_url,amount,errorMsg,cust,error);
		}
		
		
		/*if(bank.equals("1")){
			//boolean payOn02=MeiBoService.getXunhuibaoZhifubaoPay();
			Config config = Config.getConfig("yingtongbaoalipay");
			if(config == null){
				error = "1";
				errorMsg=errorMsg+"支付宝通道正在维护中....，请选择其他支付通道。";
				render(""+result_url,amount,errorMsg,cust,error);
			}
			
			if(amount.intValue()>config.maxlimit){
				error = "1";
				errorMsg=errorMsg+"您的存款金额大于"+config.maxlimit+"元。单笔存款必须不大于"+config.maxlimit+"元。";
				render(""+result_url,amount,errorMsg,cust,error);
			}
		}*/
		
//		if(bank.equals("1")){
//			//boolean payOn02=MeiBoService.getXunhuibaoZhifubaoPay();
//			Config config = Config.getConfig("huiboalipay");
//			if(config == null){
//				error = "1";
//				errorMsg=errorMsg+"支付宝通道正在维护中....，请选择其他支付通道。";
//				render(""+result_url,amount,errorMsg,cust,error);
//			}
//			
//			if(amount.intValue()>config.maxlimit){
//				error = "1";
//				errorMsg=errorMsg+"您的存款金额大于"+config.maxlimit+"元。单笔存款必须不大于"+config.maxlimit+"元。";
//				render(""+result_url,amount,errorMsg,cust,error);
//			}
//		}
		
		if(bank.equals("2")){
			//boolean payOn02=MeiBoService.getXunhuibaoWeixinPay();
			Config config = Config.getConfig("jinyangwexin");
			if(config == null){
				error = "1";
				errorMsg=errorMsg+"微信通道正在维护中....，请选择其他支付通道。";
				render(""+result_url,amount,errorMsg,cust,error);
			}
			
			if(amount.intValue()>config.maxlimit){
				error = "1";
				errorMsg=errorMsg+"您的存款金额大于"+config.maxlimit+"元。单笔存款必须不大于"+config.maxlimit+"元。";
				render(""+result_url,amount,errorMsg,cust,error);
			}
			
		}
		if(bank.equals("5")){
			//boolean payOn02=MeiBoService.getXunhuibaoWeixinPay();
			Config config = Config.getConfig("jinyangqq");
			if(config == null){
				error = "1";
				errorMsg=errorMsg+"QQ通道正在维护中....，请选择其他支付通道。";
				render(""+result_url,amount,errorMsg,cust,error);
			}
			
			if(amount.intValue()>config.maxlimit){
				error = "1";
				errorMsg=errorMsg+"您的存款金额大于"+config.maxlimit+"元。单笔存款必须不大于"+config.maxlimit+"元。";
				render(""+result_url,amount,errorMsg,cust,error);
			}
			
		}
		
//		if(bank.equals("2")){
//			//boolean payOn02=MeiBoService.getXunhuibaoWeixinPay();
//			Config config = Config.getConfig("saomafuwexin");
//			if(config == null){
//				error = "1";
//				errorMsg=errorMsg+"微信通道正在维护中....，请选择其他支付通道。";
//				render(""+result_url,amount,errorMsg,cust,error);
//			}
//			
//			if(amount.intValue()>config.maxlimit){
//				error = "1";
//				errorMsg=errorMsg+"您的存款金额大于"+config.maxlimit+"元。单笔存款必须不大于"+config.maxlimit+"元。";
//				render(""+result_url,amount,errorMsg,cust,error);
//			}
//			
//		}
		
		/*if(bank.equals("3")){
			//boolean payOn02=MeiBoService.getXunhuibaoWeixinPay();
			Config config = Config.getConfig("tonghuiwexin");
			if(config == null){
				error = "1";
				errorMsg=errorMsg+"微信通道正在维护中....，请选择其他支付通道。";
				render(""+result_url,amount,errorMsg,cust,error);
			}
			
			if(amount.intValue()>config.maxlimit){
				error = "1";
				errorMsg=errorMsg+"您的存款金额大于"+config.maxlimit+"元。单笔存款必须不大于"+config.maxlimit+"元。";
				render(""+result_url,amount,errorMsg,cust,error);
			}
			
		}*/
		
//		if(bank.equals("1")){//
//			
//			Result  result=YingtongbaoPayService.pay(login_name, amount,request.get().domain,bank,IPApp.getIpAddr());
//			String barCode = result.get("barCode");
//			render(result,barCode,errorMsg,error);
//			
//		}
		String tongdao = "";
		if(bank.equals("2")){//金阳微信
			
			Result  result=JinYangPayService.pay(login_name, amount,request.get().domain,"WEIXIN",IPApp.getIpAddr());
			String barCode = result.get("barCode");
			render(result,barCode,tongdao);
			
		}
		if(bank.equals("5")){ //金阳QQ
			
			Result  result=JinYangPayService.pay(login_name, amount,request.get().domain,"QQPAY",IPApp.getIpAddr());
			String barCode = result.get("barCode");
			render(result,barCode,tongdao);
			
		}
		
		
//		if(bank.equals("1")){//
//			
//			Result  result=HbpService.qrcodePay(login_name, amount, "8da",request.get().domain,"ALIPAY",request.get().remoteAddress);
//			String barCode = result.get("barCode");
//			render(result,barCode,errorMsg,error);
//			
//		}
		
		/*if(bank.equals("1")){//
			
			Result  result=YingtongbaoPayService.pay(login_name, amount,request.get().domain,bank,IPApp.getIpAddr());
			String barCode = result.get("barCode");
			render(result,barCode,errorMsg,error);
			
		}*/
		
		/*if(bank.equals("2")){
			
			Result  result=YingtongbaoPayService.pay(login_name, amount,request.get().domain,bank,IPApp.getIpAddr());
			String barCode = result.get("barCode");
			render(result,barCode,errorMsg,error);
			
		}
		*/
//		if(bank.equals("2")){//扫码付
//			
//			Result  result=SaoMaPayService.pay(login_name, amount,request.get().domain,"0101",IPApp.getIpAddr());
//			String barCode = result.get("barCode");
//			render(result,barCode,errorMsg,error);
//			
//		}
		
		/*if(bank.equals("3")){//通汇微信
			
			Result  result=ThpService.weixinpay(login_name, amount,request.get().domain,bank,IPApp.getIpAddr());
			String barCode = result.get("barCode");
			render(result,barCode,errorMsg,error);
			
		}*/

		
	}
	
	public static void deposit_alipay_form(Long bank_id){
		String login_name=session.get("username");
		Customer cust=Customer.getCustomer(login_name);
		if(!cust.alipay_flag){
			deposit_alipay();
		}
		Bank bank=MeiBoService.getBank(bank_id);
		if(bank==null){
			deposit_alipay();
		}
		Bank bank0=MeiBoService.getBank(cust.cust_level, bank.bank_name);
		if(!bank.account.equals(bank0.account)){
			deposit_alipay();
		}
		String bank_name=bank.bank_name;
		String account_name=bank.account_name;
		String errorMsg="温馨提示：";
		if(Deposit.NTgetNoDoCnt(cust.cust_id)>0){
			 errorMsg=errorMsg+"您存在尚未处理的存款，请处理完毕后再提交。";
			 render(errorMsg,cust,bank_name,account_name);
		 }
		render(errorMsg,cust,bank_name,account_name);
	}
	
	
	/*在线支付01*/
	public static void deposit_zx01(){
		render();
	}
	
	public static void choose_dinpay_wexin(){
		render();
	}
	
	public static void choose_yinbaopay(){
		render();
	}
	
	public static void choose_xhb(){
		render();
	}
	
	/*点卡支付01*/
	public static void deposit_point(){
		render();
	}
	
	public static void deposit_zx02(){
		render();
	}
	
	public static void getBank(){
		render();
	}
	
	public static void deposit_list(Integer page,String start_date,String end_date){
		if(start_date!=null){
			if(!DateUtil.isDate(start_date, "yyyy-MM-dd HH:mm"))
			  start_date=null;
		}
		if(end_date!=null){
			if(!DateUtil.isDate(end_date, "yyyy-MM-dd HH:mm"))
				end_date=null;
		}
		String login_name=session.get("username");
		Customer cust=Customer.getCustomer(login_name);
		Sqler sql =new Sqler("select * from mb_deposit");
		Sqler cntsql =new Sqler("select count(1) from mb_deposit");
		
		sql.and().left().equals("status", 1).or().equals("status", 3).right();
		cntsql.and().left().equals("status", 1).or().equals("status", 3).right();
		
		sql.and().left().equals("cust_id", cust.cust_id).right();
		cntsql.and().left().equals("cust_id", cust.cust_id).right();
		
		if(!(start_date==null||"".equals(start_date))){
			sql.and().ebiggerByDate("create_date",start_date,"%Y-%m-%d %H:%i");
			cntsql.and().ebiggerByDate("create_date",start_date,"%Y-%m-%d %H:%i");
		}
		if(!(end_date==null||"".equals(end_date))){
			sql.and().esmallerByDate("create_date",end_date,"%Y-%m-%d %H:%i");
			cntsql.and().esmallerByDate("create_date",end_date,"%Y-%m-%d %H:%i");
		}
		
		sql.and().ebiggerByDate("create_date",DateUtil.lastMonth(),"%Y-%m-%d %H:%i:%s");
		cntsql.and().ebiggerByDate("create_date",DateUtil.lastMonth(),"%Y-%m-%d %H:%i:%s");
		
		sql.orberByDesc("create_date");
		if(page==null)page=1;
		int count=Sp.get().getDefaultJdbc().queryForObject(cntsql.getSql(),cntsql.getParams().toArray(new Object[0]),Integer.class);
		int maxPage=(count%10)==0?count/10:(count/10+1);
		if(page>maxPage)page=maxPage;
		if(page<=0)page=1;
		List<Deposit> depositList=Sp.get().getDefaultJdbc().query(PageUtil.page(sql.getSql(),(page-1)*10,10),sql.getParams().toArray(new Object[0]),new DepositRowMap());
		DictRender dict =new DictRender();
		render(depositList,dict,page,count,maxPage,start_date,end_date);
	}
	
	public static void withdraw_list(Integer page,String start_date,String end_date){
		if(start_date!=null){
			if(!DateUtil.isDate(start_date, "yyyy-MM-dd HH:mm"))
			  start_date=null;
		}
		if(end_date!=null){
			if(!DateUtil.isDate(end_date, "yyyy-MM-dd HH:mm"))
				end_date=null;
		}
		String login_name=session.get("username");
		Customer cust=Customer.getCustomer(login_name);
		Sqler sql =new Sqler("select * from mb_withdraw");
		Sqler cntsql =new Sqler("select count(1) from mb_withdraw");
		sql.and().left().equals("status", 1).or().equals("status", 3).or().equals("status", 5).right();
		cntsql.and().left().equals("status", 1).or().equals("status", 3).or().equals("status", 5).right();
		sql.and().left().equals("cust_id", cust.cust_id).right();
		cntsql.and().left().equals("cust_id", cust.cust_id).right();
		
		if(!(start_date==null||"".equals(start_date))){
			sql.and().ebiggerByDate("create_date",start_date,"%Y-%m-%d %H:%i");
			cntsql.and().ebiggerByDate("create_date",start_date,"%Y-%m-%d %H:%i");
		}
		if(!(end_date==null||"".equals(end_date))){
			sql.and().esmallerByDate("create_date",end_date,"%Y-%m-%d %H:%i");
			cntsql.and().esmallerByDate("create_date",end_date,"%Y-%m-%d %H:%i");
		}
		
		sql.and().ebiggerByDate("create_date",DateUtil.lastMonth(),"%Y-%m-%d %H:%i:%s");
		cntsql.and().ebiggerByDate("create_date",DateUtil.lastMonth(),"%Y-%m-%d %H:%i:%s");
		
		sql.orberByDesc("create_date");
		if(page==null)page=1;
		int count=Sp.get().getDefaultJdbc().queryForObject(cntsql.getSql(),cntsql.getParams().toArray(new Object[0]),Integer.class);
		int maxPage=(count%10)==0?count/10:(count/10+1);
		if(page>maxPage)page=maxPage;
		if(page<=0)page=1;
		List<Withdraw> withdrawList=Sp.get().getDefaultJdbc().query(PageUtil.page(sql.getSql(),(page-1)*10,10),sql.getParams().toArray(new Object[0]),new WithdrawRowMap());
		DictRender dict =new DictRender();
		render(withdrawList,dict,page,count,maxPage,start_date,end_date);
	}
	public static void gift_list(Integer page,String start_date,String end_date){
		if(start_date!=null){
			if(!DateUtil.isDate(start_date, "yyyy-MM-dd HH:mm"))
			  start_date=null;
		}
		if(end_date!=null){
			if(!DateUtil.isDate(end_date, "yyyy-MM-dd HH:mm"))
				end_date=null;
		}
		String login_name=session.get("username");
		Customer cust=Customer.getCustomer(login_name);
		Sqler sql =new Sqler("select * from mb_cash_gift");
		Sqler cntsql =new Sqler("select count(1) from mb_cash_gift");
		sql.and().left().equals("status", 1).or().equals("status", 3).right();
		cntsql.and().left().equals("status", 1).or().equals("status", 3).right();
		sql.and().left().equals("cust_id", cust.cust_id).right();
		cntsql.and().left().equals("cust_id", cust.cust_id).right();
		
		if(!(start_date==null||"".equals(start_date))){
			sql.and().ebiggerByDate("create_date",start_date,"%Y-%m-%d %H:%i");
			cntsql.and().ebiggerByDate("create_date",start_date,"%Y-%m-%d %H:%i");
		}
		if(!(end_date==null||"".equals(end_date))){
			sql.and().esmallerByDate("create_date",end_date,"%Y-%m-%d %H:%i");
			cntsql.and().esmallerByDate("create_date",end_date,"%Y-%m-%d %H:%i");
		}
		sql.orberByDesc("create_date");
		if(page==null)page=1;
		int count=Sp.get().getDefaultJdbc().queryForObject(cntsql.getSql(),cntsql.getParams().toArray(new Object[0]),Integer.class);
		int maxPage=(count%10)==0?count/10:(count/10+1);
		if(page>maxPage)page=maxPage;
		if(page<=0)page=1;
		List<CashGift> giftList=Sp.get().getDefaultJdbc().query(PageUtil.page(sql.getSql(),(page-1)*10,10),sql.getParams().toArray(new Object[0]),new CashGiftRowMap());
		DictRender dict =new DictRender();
		render(giftList,dict,page,count,maxPage,start_date,end_date);
	}

	public static void letter_list(Integer page,String start_date,String end_date){
		if(start_date!=null){
			if(!DateUtil.isDate(start_date, "yyyy-MM-dd HH:mm"))
			  start_date=null;
		}
		if(end_date!=null){
			if(!DateUtil.isDate(end_date, "yyyy-MM-dd HH:mm"))
				end_date=null;
		}
		String login_name=session.get("username");
		Customer cust=Customer.getCustomer(login_name);
		Sqler sql =new Sqler("select * from mb_letter");
		Sqler cntsql =new Sqler("select count(1) from mb_letter");
		
		sql.and().left().equals("is_public", 1).right();
		cntsql.and().left().equals("is_public", 1).right();
		
		sql.and().left().equals("cust_id", cust.cust_id).right();
		cntsql.and().left().equals("cust_id", cust.cust_id).right();
		
		if(!(start_date==null||"".equals(start_date))){
			sql.and().ebiggerByDate("create_date",start_date,"%Y-%m-%d %H:%i");
			cntsql.and().ebiggerByDate("create_date",start_date,"%Y-%m-%d %H:%i");
		}
		if(!(end_date==null||"".equals(end_date))){
			sql.and().esmallerByDate("create_date",end_date,"%Y-%m-%d %H:%i");
			cntsql.and().esmallerByDate("create_date",end_date,"%Y-%m-%d %H:%i");
		}
		sql.orberByDesc("create_date");
		if(page==null)page=1;
		int count=Sp.get().getDefaultJdbc().queryForObject(cntsql.getSql(),cntsql.getParams().toArray(new Object[0]),Integer.class);
		int maxPage=(count%10)==0?count/10:(count/10+1);
		if(page>maxPage)page=maxPage;
		if(page<=0)page=1;
		List<Deposit> letterList=Sp.get().getDefaultJdbc().query(PageUtil.page(sql.getSql(),(page-1)*10,10),sql.getParams().toArray(new Object[0]),new LetterRowMap());
		DictRender dict =new DictRender();
		render(letterList,dict,page,count,maxPage,start_date,end_date);
	}
	
	public static void discount(){
		String sql ="select itemname,itemcode,itemvalue,groupcode,platform,frozenflag,createuser,createdate,startdate, enddate,pcflag,mobileflag"
				+ ",actbill,moneyflag from mb_item where  NOW() between startdate AND enddate and  groupcode='gift_type' and pcflag=1 ORDER BY groupcode";
		
		List<Item> _selfdic=Sp.get().getDefaultJdbc().query(sql,new ItemRowMap());
		
		String login_name=session.get("username");
		//获取提交记录
		String selfsql = "select * from mb_cash_gift where login_name='"+login_name+ "' order by  create_date desc limit 0,8";
		
		List<CashGift> _selflist = Sp.get().getDefaultJdbc().query(selfsql, new CashGiftRowMap());
		
		render(_selfdic,_selflist);
	}
	
	
	public static void letter_show(Long id){
		
		String login_name=session.get("username");
		Customer cust=Customer.getCustomer(login_name);
		Letter letter=Letter.NTgetLetter(id,cust.cust_id);
		render(letter);
	}
	
	
	public static void score_list(Integer page,String start_date,String end_date){
		if(start_date!=null){
			if(!DateUtil.isDate(start_date, "yyyy-MM-dd HH:mm"))
			  start_date=null;
		}
		if(end_date!=null){
			if(!DateUtil.isDate(end_date, "yyyy-MM-dd HH:mm"))
				end_date=null;
		}
		String login_name=session.get("username");
		Customer cust=Customer.getCustomer(login_name);
		Sqler sql =new Sqler("select * from mb_score_rec");
		Sqler cntsql =new Sqler("select count(1) from mb_score_rec");
		sql.and().left().equals("cust_id", cust.cust_id).right();
		cntsql.and().left().equals("cust_id", cust.cust_id).right();
		
		if(!(start_date==null||"".equals(start_date))){
			sql.and().ebiggerByDate("create_date",start_date,"%Y-%m-%d %H:%i");
			cntsql.and().ebiggerByDate("create_date",start_date,"%Y-%m-%d %H:%i");
		}
		if(!(end_date==null||"".equals(end_date))){
			sql.and().esmallerByDate("create_date",end_date,"%Y-%m-%d %H:%i");
			cntsql.and().esmallerByDate("create_date",end_date,"%Y-%m-%d %H:%i");
		}
		sql.orberByDesc("create_date");
		if(page==null)page=1;
		int count=Sp.get().getDefaultJdbc().queryForObject(cntsql.getSql(),cntsql.getParams().toArray(new Object[0]),Integer.class);
		int maxPage=(count%10)==0?count/10:(count/10+1);
		if(page>maxPage)page=maxPage;
		if(page<=0)page=1;
		List<ScoreRec> scoreList=Sp.get().getDefaultJdbc().query(PageUtil.page(sql.getSql(),(page-1)*10,10),sql.getParams().toArray(new Object[0]),new ScoreRecRowMap());
		DictRender dict =new DictRender();
		render(scoreList,dict,page,count,maxPage,start_date,end_date);
	}
	
	public static void egg_list(Integer page,String start_date,String end_date){
		if(start_date!=null){
			if(!DateUtil.isDate(start_date, "yyyy-MM-dd HH:mm"))
			  start_date=null;
		}
		if(end_date!=null){
			if(!DateUtil.isDate(end_date, "yyyy-MM-dd HH:mm"))
				end_date=null;
		}
		String login_name=session.get("username");
		Customer cust=Customer.getCustomer(login_name);
		Sqler sql =new Sqler("select * from mb_egg_gift");
		Sqler cntsql =new Sqler("select count(1) from mb_egg_gift");
		sql.and().left().equals("cust_id", cust.cust_id).right();
		cntsql.and().left().equals("cust_id", cust.cust_id).right();
		
		if(!(start_date==null||"".equals(start_date))){
			sql.and().ebiggerByDate("create_date",start_date,"%Y-%m-%d %H:%i");
			cntsql.and().ebiggerByDate("create_date",start_date,"%Y-%m-%d %H:%i");
		}
		if(!(end_date==null||"".equals(end_date))){
			sql.and().esmallerByDate("create_date",end_date,"%Y-%m-%d %H:%i");
			cntsql.and().esmallerByDate("create_date",end_date,"%Y-%m-%d %H:%i");
		}
		sql.orberByDesc("create_date");
		if(page==null)page=1;
		int count=Sp.get().getDefaultJdbc().queryForObject(cntsql.getSql(),cntsql.getParams().toArray(new Object[0]),Integer.class);
		int maxPage=(count%10)==0?count/10:(count/10+1);
		if(page>maxPage)page=maxPage;
		if(page<=0)page=1;
		List<EggGift> eggList=Sp.get().getDefaultJdbc().query(PageUtil.page(sql.getSql(),(page-1)*10,10),sql.getParams().toArray(new Object[0]),new EggGiftRowMap());
		DictRender dict =new DictRender();
		render(eggList,dict,page,count,maxPage,start_date,end_date);
	}
	
	
	public static void pt_game(Integer mode,String ptcode){
		
		String login_name=session.get("username");
		Customer cust=Customer.getCustomer(login_name);
		if(cust!=null){
			
			
		}
		String u="";
		String t="";
		
		render(mode,ptcode);
	}
	
	public static void tranfer(){
		String login_name=session.get("username");
		Customer cust=Customer.getCustomer(login_name);
		render(cust);
	}
	
	
	public static void hongbao(){
		String login_name=session.get("username");
		String msg=flash.get("msg");
		 if(msg==null){msg="";};
		 
		 
		if(login_name==null ||login_name.equals("")){
			msg="请你登录后再领取红包";
			render("/MeiBoPhp/hongbaonologin.html",msg);
		}
		Customer cust=Customer.getCustomer(login_name);
		 
		render(cust,msg);
	}
	
	public static void newyearredbag(){
		String login_name=session.get("username");
		Customer cust=Customer.getCustomer(login_name);
		int huoliflag = 0;
		int hongbaoflag = 0;
		Date now =new Date(System.currentTimeMillis());
				
		if((now.getTime()>DateUtil.stringToDate("2018-01-01", "yyyy-MM-dd").getTime()&&
				DateUtil.stringToDate("2018-01-15", "yyyy-MM-dd").getTime()>now.getTime())){
			
		}else{
			if(cust.cust_level==null||cust.cust_level.intValue()==0 ||!cust.promo_flag){
				
			}else{
				
				List<Hongbao> hongbaoList=Hongbao.NTgetToday(cust.cust_id);
				if(hongbaoList.size()==0){
					hongbaoflag = 1;
				}
			}
			
		}
		
		if((!cust.promo_flag||now.getTime()<DateUtil.stringToDate("2018-01-01", "yyyy-MM-dd").getTime()||now.getTime()>DateUtil.stringToDate("2018-01-15", "yyyy-MM-dd").getTime()) && !login_name.equals("woody")){
			
			render(login_name,cust,huoliflag,hongbaoflag);
		}
		
		//获取用户存款总数2018-01-01日至2018-01-12日23:59
		BigDecimal deposit = Deposit.NTgetSumNewYear(cust.cust_id);
		int deposittotalvalue = 0;
		if(deposit != null){
			deposittotalvalue = deposit.intValue();
		}
		int leveloneflag = 0; // 0表示未达到条件,1表示达到条件未领取,2表示已经领取  
		int leveltwoflag = 0;
		int levelthreeflag = 0;
		int levelfourflag = 0;
		int levelfiveflag = 0;
		int levelsixflag = 0;
		int levelsevenflag = 0;
		int leveleightflag = 0;
		if(deposittotalvalue >= 1000){
			int count = HuoliGift.getLevelCount(login_name, 1);
			if(count==0){
				leveloneflag = 1;
			}else{
				leveloneflag = 2;
			}
		}
		
		if(deposittotalvalue >= 5000){
			int count = HuoliGift.getLevelCount(login_name, 2);
			if(count==0){
				leveltwoflag = 1;
			}else{
				leveltwoflag = 2;
			}
		}
		if(deposittotalvalue >= 10000){
			int count = HuoliGift.getLevelCount(login_name, 3);
			if(count==0){
				levelthreeflag = 1;
			}else{
				levelthreeflag = 2;
			}
		}
		if(deposittotalvalue >= 50000){
			int count = HuoliGift.getLevelCount(login_name, 4);
			if(count==0){
				levelfourflag = 1;
			}else{
				levelfourflag = 2;
			}
		}
		if(deposittotalvalue >= 100000){
			int count = HuoliGift.getLevelCount(login_name, 5);
			if(count==0){
				levelfiveflag = 1;
			}else{
				levelfiveflag = 2;
			}
		}
		if(deposittotalvalue >= 300000){
			int count = HuoliGift.getLevelCount(login_name, 6);
			if(count==0){
				levelsixflag = 1;
			}else{
				levelsixflag = 2;
			}
		}
		
		if(deposittotalvalue >= 600000){
			int count = HuoliGift.getLevelCount(login_name, 7);
			if(count==0){
				levelsevenflag = 1;
			}else{
				levelsevenflag = 2;
			}
		}
		
		if(deposittotalvalue >= 1000000){
			int count = HuoliGift.getLevelCount(login_name, 8);
			if(count==0){
				leveleightflag = 1;
			}else{
				leveleightflag = 2;
			}
		}
		huoliflag =1;
		render(login_name,cust,leveloneflag,leveltwoflag,levelthreeflag,levelfourflag,levelfiveflag,levelsixflag,levelsevenflag,leveleightflag,deposittotalvalue,huoliflag,hongbaoflag);

	}
	
	public static void hongbao_1(){
		String login_name=session.get("username");
		Customer cust=Customer.getCustomer(login_name);
		Date now =new Date(System.currentTimeMillis());
		if(!(now.getTime()>DateUtil.stringToDate("2017-01-27", "yyyy-MM-dd").getTime()&&
				DateUtil.stringToDate("2017-02-12", "yyyy-MM-dd").getTime()>now.getTime())){
			flash.put("msg", "尊敬的客户，您好，领红包活动开放时间为年三十至正月十五！");
			hongbao();	
		}
		
		if(cust.cust_level==null||cust.cust_level.intValue()==0){  //星级0的不送
			hongbao();
		}else{
			List<Hongbao> hongbaoList=Hongbao.NTgetToday(cust.cust_id);
			BigDecimal credit=new BigDecimal(0);
			BigDecimal o= new BigDecimal(0);
			BigDecimal[] vc=new BigDecimal[]{new BigDecimal(1),new BigDecimal(2),new BigDecimal(3)};
			if(hongbaoList.size()==0){
				//未存款客户
				if(cust.cust_level==null||cust.cust_level.intValue()==0){  //星级0的不送
					credit=new BigDecimal(0);
					hongbao();
				}else{
					o=Deposit.NTgetSumLastDay(cust.cust_id);
					if(o==null||o.longValue()==0){
						credit=new BigDecimal(0.88);
					}else{
						credit=o.divide(new BigDecimal(100)).multiply(new BigDecimal(6));//昨天存款总额的6%
						//credit=new BigDecimal((credit.longValue()/10)*10+8);
						if(credit.longValue()<=10){
							credit=	new BigDecimal(8);
						}else if(credit.longValue()>10 && credit.longValue()<=18 ){
							credit=	new BigDecimal(12.88);
						}else if(credit.longValue()>18 && credit.longValue()<=58.88 ){
							credit=	new BigDecimal(18);
						}else if(credit.longValue()>58.88 && credit.longValue()<=120 ){
							credit=	new BigDecimal(58.88);
						}else if(credit.longValue()>120 && credit.longValue()<=300 ){
							credit=	new BigDecimal(88.88);
						}else if(credit.longValue()>300 && credit.longValue()<=600 ){
							credit=	new BigDecimal(188.88);
						}else if(credit.longValue()>600 && credit.longValue()<=1200  ){
							credit=	new BigDecimal(288.88);
						}else if(credit.longValue()>1200 && credit.longValue()<=3000  ){
							credit=	new BigDecimal(388.88);
						}else if(credit.longValue()>3000 && credit.longValue()<=6000  ){
							credit=	new BigDecimal(588.88);
						}else if(credit.longValue()>6000 ){
							credit=	new BigDecimal(888.88);
						}
						
					}
					
					BigDecimal credit2=new BigDecimal(0);
					BigDecimal p =Deposit.NTgetStatCust(cust.cust_id);
					if(p.longValue()>=150*10000){
						credit2=new BigDecimal(888.88);
					}
					if(p.longValue()<150*10000&&p.longValue()>=80*10000){ 
						credit2=new BigDecimal(588.88);
					}
					if(p.longValue()<80*10000&&p.longValue()>=30*10000){
						credit2=new BigDecimal(388.88);
					}
					if(p.longValue()<30*10000&&p.longValue()>=15*10000){
						credit2=new BigDecimal(288.88);
					}
					if(p.longValue()<15*10000&&p.longValue()>=5*10000){
						credit2=new BigDecimal(188.88);
					}
					if(p.longValue()<5*10000&&p.longValue()>=1*10000){
						credit2=new BigDecimal(88.88);
					}
					if(p.longValue()<1*10000&&p.longValue()>=0.5*10000){
						credit2=new BigDecimal(58.88);
					}
					
					if(p.longValue()<0.5*10000&&p.longValue()>=0){
						credit2=new BigDecimal(38.88);
					}
					if(p.longValue()<0){
						if(o.divide(new BigDecimal(100)).multiply(new BigDecimal(6)).longValue()>60){
							credit2 = new BigDecimal(18.88);
						}else{
							credit2 = new BigDecimal(8.88);
						}
					}
					
					if(credit.longValue()>=credit2.longValue()){
						credit=credit2;
					}
				}
				
				// credit=credit.add(new BigDecimal(0.88));
				 Hongbao hb =new Hongbao();
				 hb.credit=credit;
				 hb.cust_id=cust.cust_id;
				 hb.login_name=cust.login_name;
				 hb.status=0;
				 Long  hongbao_id=Hongbao.createHongbao(hb);
				 String giftCode=MyRandom.getRandom(8);
				 CashGift gift =new CashGift();
	        	 gift.gift_code=giftCode;
	        	 gift.login_name=cust.login_name;
	        	 gift.deposit_credit=o;
	        	 gift.valid_credit=new BigDecimal(0);
	        	 gift.net_credit=new BigDecimal(0);
	        	 gift.rate=Float.valueOf(5);
	        	 gift.payout=credit;
	        	 gift.gift_type="春节领红包";
	        	 gift.status=1;
	        	 gift.gift_no=OrderNo.createLocalNo("GI");
	     		 gift.cust_id=cust.cust_id;
	        	 gift.cs_date=new Date(System.currentTimeMillis());
	        	 gift.real_name=cust.real_name;
	        	 gift.cust_level=cust.cust_level;
	           	 gift.kh_date=cust.create_date;
	        	 gift.create_user="系统";
	        	 gift.create_date=new Date(System.currentTimeMillis());
	        	 Long giftId=gift.NTcreat();
	        	 gift.NTAuditGift(giftId, 3, "系统","系统审核通过-昨日存款："+o);
	        	 if(CustomerService.modCredit(cust.cust_id,CreditLogService.Gift, 
	 					gift.login_name,gift.payout,"系统", "系统审核通过-昨日存款："+o, gift.gift_no)){
	        		 Hongbao.finishHongbao(giftId, hongbao_id);
	        	 }
	        	 hongbaoList=Hongbao.NTgetToday(cust.cust_id);
			}
			
			Hongbao hongbao =hongbaoList.get(0);
			
			render(cust,hongbao);
		}
		
		
	}
	
	public static void online01(){
		deposit_zx01();
	}

	
	public static void years(Integer page,String start_date,String end_date){
		if(start_date!=null){
			if(!DateUtil.isDate(start_date, "yyyy-MM-dd HH:mm"))
			  start_date=null;
		}
		if(end_date!=null){
			if(!DateUtil.isDate(end_date, "yyyy-MM-dd HH:mm"))
				end_date=null;
		}
		String login_name=session.get("username");
		Customer cust=Customer.getCustomer(login_name);
		Sqler sql =new Sqler("select * from mb_year_gift");
		Sqler cntsql =new Sqler("select count(1) from mb_year_gift");
		sql.and().left().equals("cust_id", cust.cust_id).right();
		cntsql.and().left().equals("cust_id", cust.cust_id).right();
		
		if(!(start_date==null||"".equals(start_date))){
			sql.and().ebiggerByDate("create_date",start_date,"%Y-%m-%d %H:%i");
			cntsql.and().ebiggerByDate("create_date",start_date,"%Y-%m-%d %H:%i");
		}
		if(!(end_date==null||"".equals(end_date))){
			sql.and().esmallerByDate("create_date",end_date,"%Y-%m-%d %H:%i");
			cntsql.and().esmallerByDate("create_date",end_date,"%Y-%m-%d %H:%i");
		}
		sql.orberByDesc("create_date");
		if(page==null)page=1;
		int count=Sp.get().getDefaultJdbc().queryForObject(cntsql.getSql(),cntsql.getParams().toArray(new Object[0]),Integer.class);
		int maxPage=(count%10)==0?count/10:(count/10+1);
		if(page>maxPage)page=maxPage;
		if(page<=0)page=1;
		List<YearGift> giftList=Sp.get().getDefaultJdbc().query(PageUtil.page(sql.getSql(),(page-1)*10,10),sql.getParams().toArray(new Object[0]),new YearGiftRowMap());
		DictRender dict =new DictRender();
		
		YearGiftCustBean  bean=YearGiftCache.get().getCustBean(login_name);
		if(bean==null){
			 bean =new YearGiftCustBean();
			 bean.sum_credit=new BigDecimal(0);
			 bean.cnt=0;
		}
		if(bean.sum_credit==null){
			bean.sum_credit=new BigDecimal(0);
		}
		
		render(giftList,dict,page,count,maxPage,start_date,end_date,bean);

	}
	
	
	public static void newyearhongbao(Integer page,String start_date,String end_date){
		if(start_date!=null){
			if(!DateUtil.isDate(start_date, "yyyy-MM-dd HH:mm"))
			  start_date=null;
		}
		if(end_date!=null){
			if(!DateUtil.isDate(end_date, "yyyy-MM-dd HH:mm"))
				end_date=null;
		}
		String login_name=session.get("username");
		Customer cust=Customer.getCustomer(login_name);
		Sqler sql =new Sqler("select * from mb_hongbao");
		Sqler cntsql =new Sqler("select count(1) from mb_hongbao");
		sql.and().left().equals("cust_id", cust.cust_id);
		sql.and().equals("status",1).right();
		
		cntsql.and().left().equals("cust_id", cust.cust_id);
		cntsql.and().equals("status",1).right();
		
		if(!(start_date==null||"".equals(start_date))){
			sql.and().ebiggerByDate("create_date",start_date,"%Y-%m-%d %H:%i");
			cntsql.and().ebiggerByDate("create_date",start_date,"%Y-%m-%d %H:%i");
		}
		if(!(end_date==null||"".equals(end_date))){
			sql.and().esmallerByDate("create_date",end_date,"%Y-%m-%d %H:%i");
			cntsql.and().esmallerByDate("create_date",end_date,"%Y-%m-%d %H:%i");
		}
		sql.orberByDesc("create_date");
		if(page==null)page=1;
		int count=Sp.get().getDefaultJdbc().queryForObject(cntsql.getSql(),cntsql.getParams().toArray(new Object[0]),Integer.class);
		int maxPage=(count%10)==0?count/10:(count/10+1);
		if(page>maxPage)page=maxPage;
		if(page<=0)page=1;
		List<Hongbao> giftList=Sp.get().getDefaultJdbc().query(PageUtil.page(sql.getSql(),(page-1)*10,10),sql.getParams().toArray(new Object[0]),new HongbaoRowMap());
		DictRender dict =new DictRender();
		
		
		render(giftList,dict,page,count,maxPage,start_date,end_date);

	}
	
	public static void cschat(){
		IPSeeker seeker = IPSeeker.getInstance();
		String ip=IPApp.getIpAddr();
		String address=seeker.getAddress(ip==null?"":ip);
		StringBuffer sb=new StringBuffer();
		String userName=session.get("username");
		if(userName==null||"".equals(userName)){
			int id=session.getId().hashCode();
			userName="游客_"+id;
		}else{
			Customer cust=Customer.getCustomer(userName);
			userName=cust.login_name+"_"+cust.real_name;
		}
		String chatID=""+System.currentTimeMillis();
	//	String workgroup="demo@workgroup.openfire.localdomain";
	//	String url="http://127.0.0.1:8080/webchat/queue.jsp";
		
		String workgroup=Play.configuration.getProperty("live800.workgroup");
		String username=URLEncoder.encode(userName);
		String IPaddr=URLEncoder.encode(IPApp.getIpAddr()+"/"+address);
		String key=MD5.md5(chatID+Play.configuration.getProperty("live800.key"));
		String url=Play.configuration.getProperty("live800.queue");;
		render(url,chatID,workgroup,username,IPaddr,key);
	}
	
	public static void choose_auto_bank(){
		render();
		
	}
	
	public static void deposit_tenpay(){
		render();
		
	}
	
	//完善支付宝界面
	public static void deposit_alipay_account(){
		render();
	}
	
	//支付宝充值
	public static void deposit_alipay_ecode(){
		String userName=session.get("username");
		Customer cust=Customer.getCustomer(userName);
		if(cust==null){
			MeiBo.index();
		}
		if(!(cust.alipay_account!=null&&cust.alipay_account.length()>5&&cust.alipay_name!=null&&cust.alipay_name.length()>=1)){
			deposit_alipay_account();

		}
		render(cust);
	}
	/**
	 * 聚宝支付
	 */
	public static void choose_jbpay_bank(){
		render();
	}
	
	public static void choose_weixin_form(){
		String userName=session.get("username");
		Customer cust=Customer.getCustomer(userName);
		String show = "0";
		if(cust.cust_level>0){
			show = "1";
		}
		render(show);
	}
	
	public static void choose_qq_form(){
		render();
	}
	
	public static void choose_jingdong_form(){
		render();
	}
	
	public static void choose_yinlian_form(){
		render();
	}
	
	public static void choose_zhifubao_form(){
		render();
	}
	
	/**
	 * 盈宝支付
	 */
	public static void choose_ybpay_bank(Integer type){
		render(type);
	}
	
	/**
	 * 乐盈支付
	 */
	public static void choose_lypay_bank(Integer type){
		render(type);
	}
	
	/**
	 * 盈宝网银支付
	 */
	public static void choose_dpay_onlinebank(){
		render();
	}
	
	/**
	 * 盈宝网银支付
	 */
	public static void choose_ybpay_onlinebank(){
		render();
	}
	
	/**
	 * 通汇支付
	 */
	public static void choose_thpay_bank(){
		render();
	}
	
	/**
	 * 汇博支付
	 */
	public static void choose_hbpay_bank(){
		render();
	}
	
	/**
	 * 汇通支付
	 */
	public static void choose_huitongpay_bank(){
		render();
	}
	
	/**
	 * 金阳网银支付
	 */
	public static void choose_jinyangwangyin(){
		render();
	}
	
	/**
	 * 盈通宝网银支付
	 */
	public static void choose_ytbpay_bank(){
		render();
	}
	
	/**
	 * RPN银联支付
	 */
	public static void choose_unionpay(){
		render();
	}
	
	/**
	 * 剑跃网银支付
	 */
	public static void choose_jianyuewangyin(){
		render();
	}
	
	/*public static void settlement(){
		Date date = new Date();
		Calendar c = Calendar.getInstance(Locale.CHINA);
		c.setTime(date);
		//DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

		String login_name=session.get("username");
		String ag_sb_bbin_kg_mg_pp_name="daw"+login_name;
		String pt_name="daw"+login_name.toUpperCase();
		
		String ag_live_startdate = getMondayOfThisWeek()+" 00:00:00";
		String ag_ele_startdate = getMondayOfThisWeek()+" 00:00:00";
		
		String mg_ele_startdate = getMondayOfThisWeek()+" 00:00:00";
		
		String pt_ele_startdate = getMondayOfThisWeek()+" 00:00:00";
		String pt_live_startdate = getMondayOfThisWeek()+" 00:00:00";
		
		String bbin_ele_startdate = getMondayOfThisWeek()+" 00:00:00";
		String bbin_live_startdate = getMondayOfThisWeek()+" 00:00:00";
		String bbin_sport_startdate = getMondayOfThisWeek()+" 00:00:00";
		
		String sb_live_startdate = getMondayOfThisWeek()+" 00:00:00";
		String sb_ele_startdate = getMondayOfThisWeek()+" 00:00:00";
		
		
		//查询最后一次结算时间 、如果第一次结算。那就视为本周一开始
		CashGift ag_live_ = CashGift.getEndsettlement(login_name,"AG视讯");
		if(ag_live_!=null){
			ag_live_startdate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(ag_live_.create_date);
		}
		
		//判断是否有未处理的订单
		CashGift gf = new CashGift();
		gf.gift_type="AG视讯";
		gf.login_name=login_name;
		if(CashGift.checkSelf(gf)){
			renderText(JSONResult.failure("您存在未审核的申请活动，请等待客服处理。"));
		}
		String enddate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(c.getTime());
		
		//System.out.println("startdate="+startdate+",enddate="+enddate);   //gameCat (游戏类型 LIVE/ELECT/SPORTS)
		render(ag_live_startdate);
		
	}*/
	/*public static void main(String[] args){
		Date now =new Date(System.currentTimeMillis());
		//System.out.println(now.getTime()>DateUtil.stringToDate("2018-01-01", "yyyy-MM-dd").getTime()&&
		//		DateUtil.stringToDate("2018-01-12", "yyyy-MM-dd").getTime()>now.getTime());
		
		//System.out.println(DateUtil.stringToDate("2018-01-12", "yyyy-MM-dd").getTime());
		
		
		String WSServiceUrl = "http://192.168.1.115:8087/betrewardservice";
		Date date = new Date();
		Calendar c = Calendar.getInstance(Locale.CHINA);
		c.setTime(date);
		//DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

		String startdate = "2018-01-15 00:00:00";//getMondayOfThisWeek()+" 00:00:00";
		//查询最后一次结算时间 、如果第一次结算。那就视为本周一开始
		CashGift cg = CashGift.getEndsettlement("dawyuan2008","AG视讯");
		if(cg!=null){
			startdate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cg.create_date);
		}
		
		//判断是否有未处理的订单
		CashGift gf = new CashGift();
		gf.gift_type="AG视讯";
		gf.login_name="dawyuan2008";
		if(CashGift.checkSelf(gf)){
			renderText(JSONResult.failure("您存在未审核的申请活动，请等待客服处理。"));
		}
		String enddate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(c.getTime());
		
		
//		String param = "beginTime::" + startdate + ";endTime::" + enddate;  
		//"playerName::" + playerName + ";gameCat::" + gameCat + ";beginTime::" + beginTime + ";endTime::" + endTime;
		System.out.println("startdate="+startdate+",enddate="+enddate);   //gameCat (游戏类型 LIVE/ELECT/SPORTS)
		String param = "playerName::dawzhsl321;gameCat::LIVE;beginTime::"+startdate+";endTime::"+enddate;	
		JSONObject result = null;
			 //result = BetRewardWSInterface.execute(WSServiceUrl, "PT", "BUSINESS", "BETAMOUNTSUM", param);
		result = JSONObject.fromString(BetRewardStatic.betRewardServerAG.execute("BUSINESS", "BETAMOUNTSUM", param));
			
		System.out.println("result="+result);
		String res = result.getString("code");
		boolean show = false;
		if("0".equals(res)){
			JSONArray array = (JSONArray) result.get("dataLs");
			if(array.length()>0){
				
				JSONArray al = (JSONArray) array.get(0);
				String gift_no = String.valueOf(al.get(0));
				String playname = String.valueOf(al.get(1));
				Integer custlevel = Integer.valueOf((String) al.get(2));
				String gift_type = String.valueOf(al.get(3));
				String create_date = String.valueOf(al.get(5));
				String valid_credit = String.valueOf( al.get(6));
				String rate = al.get(7)+"";
				String payout = String.valueOf(al.get(8));
				
				System.out.println("gift_no="+gift_no);
				System.out.println("playname="+playname);
				System.out.println("custlevel="+custlevel);
				System.out.println("gift_type="+gift_type);
				System.out.println("create_date="+create_date);
				System.out.println("valid_credit="+valid_credit);
				System.out.println("rate="+rate);
				System.out.println("payout="+payout);
				
				if(custlevel>=4 && Double.valueOf(payout)>=1.0){
					show = true;
				}
				
			}
			
		}
		
	}*/
	/**
	  * 得到本周周一
	  * 
	  * @return yyyy-MM-dd
	  */
	 public static String getMondayOfThisWeek() {
		 DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	  Calendar c = Calendar.getInstance();
	  int day_of_week = c.get(Calendar.DAY_OF_WEEK) - 1;
	  if (day_of_week == 0)
	   day_of_week = 7;
	  c.add(Calendar.DATE, -day_of_week + 1);
	  return df.format(c.getTime());
	 }
	 public static void main(String[] argsa){
		 Date now =new Date(System.currentTimeMillis());
		 System.out.println(now.getTime()>DateUtil.stringToDate("2018-02-14 10:00:00", "yyyy-MM-dd HH:mm:ss").getTime()&&
				DateUtil.stringToDate("2018-02-24 00:00:00", "yyyy-MM-dd HH:mm:ss").getTime()>now.getTime());
	 }
}
