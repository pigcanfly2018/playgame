package controllers;

import groovy.lang.Closure;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;

import com.ws.service.BankInfo;
import com.ws.service.TokenService;

import models.Ad;
import models.AgGame;
import models.Bank;
import models.BbinGame;
import models.Billboard;
import models.Config;
import models.Customer;
import models.Deposit;
import models.Discount;
import models.Hongbao;
import models.HuiBo;
import models.MessageBoard;
import models.MgGame;
import models.Notice;
import models.PpGame;
import models.PtGame;
import models.SbGame;
import models.WinList;
import play.Play;
import play.cache.Cache;
import play.mvc.Http.Request;
import play.templates.FastTags;
import play.templates.GroovyTemplate.ExecutableTemplate;
import service.ListService;
import service.MeiBoService;
import util.DictRender;

@FastTags.Namespace("mb") 
public class MyTag extends FastTags {

	/**
	 * 静态资源
	 */
	public static void _static(Map<?, ?> args, Closure body, PrintWriter out, 
	         ExecutableTemplate template, int fromLine){
		String name=(String)args.get("arg");
		if(name==null)name="";
		if(play.Play.mode.isDev()){
			//out.print(name);
			out.print(Play.configuration.get("static.url")+name);
		}else{
			out.print(Play.configuration.get("static.url")+name);
		 }
	}
	
	public static void _cust(Map<?, ?> args, Closure body, PrintWriter out, 
	         ExecutableTemplate template, int fromLine){
		String name=(String)args.get("uname");
		 body.setProperty("_cust", MeiBoService.getCustomerByUserName(name));
		 body.call();
	}
	
	public static void _showThBycustLevel(Map<?, ?> args, Closure body, PrintWriter out, 
	         ExecutableTemplate template, int fromLine){
		String name=(String)args.get("uname");
		Customer customer = MeiBoService.getCustomerByUserName(name);
		body.setProperty("_showtonghui", customer.cust_level>=2);
		 body.call();
	}
	
	public static void _showNewDepositwayBycustLevel(Map<?, ?> args, Closure body, PrintWriter out, 
	         ExecutableTemplate template, int fromLine){
		String name=(String)args.get("uname");
		String sortpriority=(String)args.get("sortpriority");
		Customer customer = MeiBoService.getCustomerByUserName(name);
		Config config = Config.getConfig(sortpriority, "%"+customer.cust_level+"%");
		if(config == null){
			body.setProperty("_depositwayFlag", false);
		}else{
			body.setProperty("_depositwayFlag", true);
			body.setProperty("_para", config.para);
		}
		
		body.call();
		
		
	}
	
	public static void _showDepositwayBycustLevel(Map<?, ?> args, Closure body, PrintWriter out, 
	         ExecutableTemplate template, int fromLine){
		String name=(String)args.get("uname");
		String config_name=(String)args.get("config_name");
		Customer customer = MeiBoService.getCustomerByUserName(name);
		if(config_name.equals("rpnpay")){
			Boolean flag = HuiBo.NTExist(name);
			if(flag ){
				body.setProperty("_depositwayFlag", false);
				 body.call();
			}else{
				boolean depositwayFlag=MeiBoService.getDepositwayFlag(config_name,customer.cust_level);
				body.setProperty("_depositwayFlag", depositwayFlag);
				 body.call();
			}
		}else if(config_name.equals("xinbeiwexin")){
			Request request=(Request)args.get("request_name");
			System.out.println(request.get().domain +request.get().domain.contains("668da"));
			if(request.get().domain.contains("668da")){
				body.setProperty("_depositwayFlag", false);
				 body.call();
			}else{
				boolean depositwayFlag=MeiBoService.getDepositwayFlag(config_name,customer.cust_level);
				body.setProperty("_depositwayFlag", depositwayFlag);
				 body.call();
				
			}
		}else if(config_name.equals("huibowangyin")){
			Request request=(Request)args.get("request_name");
	
			if(request.get().domain.contains("668da")){
				body.setProperty("_depositwayFlag", false);
				 body.call();
			}else{
				boolean depositwayFlag=MeiBoService.getDepositwayFlag(config_name,customer.cust_level);
				body.setProperty("_depositwayFlag", depositwayFlag);
				 body.call();
				
			}
		}else if(config_name.equals("xinbeiqq")){
			Request request=(Request)args.get("request_name");
			//System.out.println(request.get().domain +request.get().domain.contains("668da"));
			
			boolean depositwayFlag=MeiBoService.getDepositwayFlag(config_name,customer.cust_level);
			body.setProperty("_depositwayFlag", depositwayFlag);
			 body.call();
			 
		}else if(config_name.equals("dingyiwexin")){
			boolean depositwayFlag=MeiBoService.getDepositwayFlag(config_name,customer.cust_level);
			body.setProperty("_depositwayFlag", depositwayFlag);
			 body.call();
			 
		}else if(config_name.equals("dingyibaoalipay")){
			boolean depositwayFlag=MeiBoService.getDepositwayFlag(config_name,customer.cust_level);
			body.setProperty("_depositwayFlag", depositwayFlag);
			 body.call();
			 
		}else if(config_name.equals("gaotongqqpay") || config_name.equals("gaotongwexinpay") || config_name.equals("gaotongjdpay") || "bruce".equals(name)){
			boolean depositwayFlag=MeiBoService.getDepositwayFlag(config_name,customer.cust_level);
			body.setProperty("_depositwayFlag", depositwayFlag);
			 body.call();
		}else if(config_name.equals("qq_saoma")){
			boolean depositwayFlag=MeiBoService.getDepositwayFlag(config_name,customer.cust_level);
			body.setProperty("_depositwayFlag", depositwayFlag);
			 body.call();
		}else if(config_name.equals("xingheyiwexinpay")){
			boolean depositwayFlag=MeiBoService.getDepositwayFlag(config_name,customer.cust_level);
			body.setProperty("_depositwayFlag", depositwayFlag);
			 body.call();			
		}else if(config_name.equals("jinyangmobileqq") || config_name.equals("jinyangmobilewexin")){
			boolean depositwayFlag=MeiBoService.getDepositwayFlag(config_name,customer.cust_level);
			body.setProperty("_depositwayFlag", depositwayFlag);
			 body.call();
		}else{
			boolean depositwayFlag=MeiBoService.getDepositwayFlag(config_name,customer.cust_level);
			body.setProperty("_depositwayFlag", depositwayFlag);
			 body.call();
		}
		
	}
	
	/**
	 * 客户等级
	 * @param args
	 * @param body
	 * @param out
	 * @param template
	 * @param fromLine
	 */
	public static void _custLevel(Map<?, ?> args, Closure body, PrintWriter out, 
	         ExecutableTemplate template, int fromLine){
		Integer cust_level=(Integer)args.get("level");
		if(cust_level<0){
			out.print("");return;
		}
		DictRender rd=new DictRender();
		String level=rd.dictName("cust_level", cust_level);
		out.print(level);
	}
	
	
	

	/**
	 * 首页广告图
	 * @param args
	 * @param body
	 * @param out
	 * @param template
	 * @param fromLine
	 */
	public static void _ad(Map<?, ?> args, Closure body, PrintWriter out, 
	         ExecutableTemplate template, int fromLine){
		List<Ad> ads=MeiBoService.getAds();
		for(Ad ad:ads){
			 body.setProperty("_ad", ad);
			 body.call();
		}
		 
	}
	
	public static void _yingtongbaowangyin(Map<?, ?> args, Closure body, PrintWriter out, 
	         ExecutableTemplate template, int fromLine){
		String data =(String)args.get("data");
		
		out.print(data);
	}
	
	/**
	 * 
	 * @param args
	 * @param body
	 * @param out
	 * @param template
	 * @param fromLine
	 */
	public static void _notice(Map<?, ?> args, Closure body, PrintWriter out, 
	         ExecutableTemplate template, int fromLine){
		List<Notice> noticeList=MeiBoService.getNotices();
		for(Notice notice:noticeList){
			 body.setProperty("_notice", notice);
			 body.call();
		}
	}
	
//	
	public static void _discount(Map<?, ?> args, Closure body, PrintWriter out, 
	         ExecutableTemplate template, int fromLine){
		List<Discount> discountList=MeiBoService.getDiscounts();
		for(Discount discount:discountList){
			 body.setProperty("_discount", discount);
			 body.call();
		}
	}
	
	
	public static void _billboard(Map<?, ?> args, Closure body, PrintWriter out, 
	         ExecutableTemplate template, int fromLine){
		List<Billboard> billboardList=MeiBoService.getBillboardList();
		for(Billboard billboard:billboardList){
			 body.setProperty("_billboard", billboard);
			 body.call();
		}
	}
	
	
	public static void _hotDiscount(Map<?, ?> args, Closure body, PrintWriter out, 
	         ExecutableTemplate template, int fromLine){
		 Discount discount=MeiBoService.getDiscount();
		 if(discount!=null){
			 body.setProperty("_discount", discount);
			 body.call();
		 }
	}
	
	
	public static void _payOn01(Map<?, ?> args, Closure body, PrintWriter out, 
	         ExecutableTemplate template, int fromLine){
		boolean payOn01=MeiBoService.getOnlinePay01();
		 body.setProperty("_payOn01", payOn01);
		 body.call();
	}
	
	public static void _speedPay(Map<?, ?> args, Closure body, PrintWriter out, 
	         ExecutableTemplate template, int fromLine){
		boolean speedPay=MeiBoService.getSpeedPay();
		 body.setProperty("_speedPay", speedPay);
		 body.call();
	}
	
	public static void _yingbaoPay(Map<?, ?> args, Closure body, PrintWriter out, 
	         ExecutableTemplate template, int fromLine){
		boolean yingbaoPay=MeiBoService.getYingbaoPay();
		 body.setProperty("_yingbaoPay", yingbaoPay);
		 body.call();
	}
	
	public static void _wuyiTimes(Map<?, ?> args, Closure body, PrintWriter out, 
	         ExecutableTemplate template, int fromLine){
		
		String name=(String)args.get("uname");
		
		Customer customer = MeiBoService.getCustomerByUserName(name);
		BigDecimal sum = Deposit.NTgetSumToday(customer.cust_id);
		
		int cishu = 0;
		if(sum == null || sum.intValue()< 1000){
			
		}else if(sum.intValue()>= 1000 && sum.intValue()< 5000){
			cishu = 1;
		}else if(sum.intValue()>= 5000 && sum.intValue()< 10000){
			cishu = 2;
		}else if(sum.intValue()>= 10000 && sum.intValue()< 50000){
			cishu = 4;
		}else if(sum.intValue()>= 50000){
			cishu = 6;
		}
		//System.out.println("cishu"+sum.intValue());
		int yiling = 0;
		if(cishu != 0){
			 yiling=Hongbao.NTgetCountToday(customer.cust_id);
		}
		
		cishu = cishu-yiling;
		 
		 body.setProperty("_cishu", cishu);
		 body.call();
	}
	
	public static void _jvbaoPay(Map<?, ?> args, Closure body, PrintWriter out, 
	         ExecutableTemplate template, int fromLine){
		boolean jvbaoPay=MeiBoService.getJvbaoPay();
		 body.setProperty("_jvbaoPay", jvbaoPay);
		 body.call();
	}
	
	public static void _tonghuiPay(Map<?, ?> args, Closure body, PrintWriter out, 
	         ExecutableTemplate template, int fromLine){
		boolean tonghuiPay=MeiBoService.getTonghuiPay();
		 body.setProperty("_tonghuiPay", tonghuiPay);
		 body.call();
	}
	
	public static void _tenPay(Map<?, ?> args, Closure body, PrintWriter out, 
	         ExecutableTemplate template, int fromLine){
		boolean tenPay=MeiBoService.getTenpay();
		 body.setProperty("_tenPay", tenPay);
		 body.call();
	}
	
	public static void _aliPay(Map<?, ?> args, Closure body, PrintWriter out, 
	         ExecutableTemplate template, int fromLine){
		boolean aliPay=MeiBoService.getAlipay();
		 body.setProperty("_aliPay", aliPay);
		 body.call();
	}
	
	public static void _payPoint(Map<?, ?> args, Closure body, PrintWriter out, 
	         ExecutableTemplate template, int fromLine){
		boolean payPoint=MeiBoService.getPointPay();
		 body.setProperty("_payPoint", payPoint);
		 body.call();
	}
	
	public static void _payOn02(Map<?, ?> args, Closure body, PrintWriter out, 
	         ExecutableTemplate template, int fromLine){
		boolean payOn02=MeiBoService.getOnlinePay02();
		 body.setProperty("_payOn02", payOn02);
		 body.call();
	}
	
	public static void _getBanks(Map<?, ?> args, Closure body, PrintWriter out, 
	         ExecutableTemplate template, int fromLine){
		Integer cust_level=Integer.parseInt(String.valueOf(args.get("level")));
		String login_name = (String)args.get("loginname");
		Bank bank0 = MeiBoService.getSpecifiedBank(login_name, "工行") ;
		Bank bank1 = MeiBoService.getSpecifiedBank(login_name, "农行");
		Bank bank2 = MeiBoService.getSpecifiedBank(login_name, "招行");
		
		Boolean bankpay=Boolean.valueOf(String.valueOf(args.get("bankpay")));
		if(bankpay){
			if(bank0 == null){
				bank0= MeiBoService.getBank(cust_level, "工行");
			}
			if(bank1 == null){
				bank1= MeiBoService.getBank(cust_level, "农行");
			}
			if(bank2 == null){
				bank2= MeiBoService.getBank(cust_level, "招行");
			}
		}
		
		body.setProperty("_bank0", bank0);
		body.setProperty("_bank1", bank1);
		body.setProperty("_bank2", bank2);
		 body.call();
	}
	
	public static void _getNewBanks(Map<?, ?> args, Closure body, PrintWriter out, 
	         ExecutableTemplate template, int fromLine){
		Integer cust_level=Integer.parseInt(String.valueOf(args.get("level")));
		String login_name = (String)args.get("loginname");
		Bank bank0 = MeiBoService.getSpecifiedBank(login_name, "工行") ;
		Bank bank1 = MeiBoService.getSpecifiedBank(login_name, "农行");
		Bank bank2 = MeiBoService.getSpecifiedBank(login_name, "招行");
		
		Boolean bankpay=Boolean.valueOf(String.valueOf(args.get("bankpay")));
		if(bankpay){
			
			Bank bank = MeiBoService.getBank(cust_level);
			if (bank != null){
				if(bank.bank_name.equals("工行")){
					bank0 = bank;
				}else if(bank.bank_name.equals("农行")){
					bank1 = bank;
				}else if(bank.bank_name.equals("招行")){
					bank2 = bank;
				}
			}
			
//			if(bank0 == null){
//				bank0= MeiBoService.getBank(cust_level, "工行");
//			}
//			if(bank1 == null){
//				bank1= MeiBoService.getBank(cust_level, "农行");
//			}
//			if(bank2 == null){
//				bank2= MeiBoService.getBank(cust_level, "招行");
//			}
		}
		
		body.setProperty("_bank0", bank0);
		body.setProperty("_bank1", bank1);
		body.setProperty("_bank2", bank2);
		 body.call();
	}
	
	
	public static void _getMaxlimit(Map<?, ?> args, Closure body, PrintWriter out, 
	         ExecutableTemplate template, int fromLine){
		int yinbaolimit = 0;
		int xunhuibaolimit = 0;
		int leyinglimit = 0;
		int dinpaylimit = 0;
		int xunfutonglimit = 0;
		int yingtongbaolimit = 0;
		int tonghuilimit = 0;
		int huibolimit = 0;
		int xinbeilimit = 0;
		int gaotonglimit = 0;
		int saomalimit = 0;
		int sanvlimit = 0;
		int dingyilimit=0;
		int changfulimit=0;
		int xingheyilimit = 0;
		int jinyanglimit = 0;
		int huitianlimit = 0;
		int antonglimit = 0;
		int lefubaolimit = 0;
		int huitlimit = 0;
		int jianyuelimit = 0;
		List<Config> list = null;
		String channelname = (String)args.get("channelname");
		if(channelname.equals("wexin")){
			list = Config.getOpenWeiXin();
		}
		
		if(channelname.equals("ali")){
			list = Config.getOpenZhifubao();
		}
		
		if(channelname.equals("qq")){
			list = Config.getOpenQQ();
		}
		if(channelname.equals("jd")){
			list = Config.getOpenJd();
		}
		
		for(Config fig : list){
			if(fig.config_name.equals("yinbaowexin") || fig.config_name.equals("yinbaoalipay") ){
				yinbaolimit = fig.maxlimit;
			}else if(fig.config_name.equals("xunhuibaowexin") || fig.config_name.equals("xunhuibaoalipay")){
				xunhuibaolimit = fig.maxlimit;
			}else if(fig.config_name.startsWith("leyingwexin") || fig.config_name.startsWith("leyingalipay") ){
				leyinglimit = fig.maxlimit;
			}else if(fig.config_name.equals("dinpay_wexin") ){
				dinpaylimit = fig.maxlimit;
			}else if(fig.config_name.equals("xunfutongwexin") || fig.config_name.equals("xunfutongalipay")){
				xunfutonglimit = fig.maxlimit;
			}else if(fig.config_name.equals("yingtongbaowexin") || fig.config_name.equals("yingtongbaoalipay") || fig.config_name.equals("yingtongbaoqq")  ){
				yingtongbaolimit = fig.maxlimit;
			}else if(fig.config_name.equals("tonghuiwexin")){
				tonghuilimit = fig.maxlimit;
			}else if(fig.config_name.equals("huiboalipay")){
				huibolimit = fig.maxlimit;
			}else if(fig.config_name.equals("xinbeiwexin") || fig.config_name.equals("xinbeialipay") || fig.config_name.equals("xinbeiqq")  ){
				xinbeilimit = fig.maxlimit;
			}else if(fig.config_name.equals("saomafuwexin")){
				saomalimit = fig.maxlimit;
			}else if(fig.config_name.equals("3vwexin") || fig.config_name.equals("3valipay") ){
				sanvlimit = fig.maxlimit;
			}else if(fig.config_name.equals("dingyiwexin") || fig.config_name.equals("dingyibaoalipay")|| fig.config_name.equals("dingyiqq") ){
				dingyilimit = fig.maxlimit;
			}else if(fig.config_name.equals("gaotongqqpay") || fig.config_name.equals("gaotongjdpay")|| fig.config_name.equals("gaotongwexinpay")){
				gaotonglimit = fig.maxlimit;
			}else if(fig.config_name.equals("xingheyiwexinpay") || fig.config_name.equals("xingheyitongqqpay") || fig.config_name.equals("xingheyialipay")){
				xingheyilimit = fig.maxlimit;
			}else if(fig.config_name.equals("changfuqq") || fig.config_name.equals("changfuwexin")){
				changfulimit = fig.maxlimit;
			}else if(fig.config_name.equals("jinyangmobilewexin") || fig.config_name.equals("jinyangmobileqq")){
				jinyanglimit = fig.maxlimit;
			}else if(fig.config_name.equals("huitianmobilewexin") || fig.config_name.equals("huitianmobileqq")){
				huitianlimit = fig.maxlimit;
			}else if(fig.config_name.equals("antongmobilewexin") || fig.config_name.equals("antongmobileali")){
				antonglimit = fig.maxlimit;
			}else if( fig.config_name.equals("lefubaomobileali")){
				lefubaolimit = fig.maxlimit;
			}else if( fig.config_name.equals("huitmobilewexin") || fig.config_name.equals("huitmobileqq")){
				huitlimit = fig.maxlimit;
			}else if( fig.config_name.equals("jianyuemobileqq") ){
				jianyuelimit = fig.maxlimit;
			}
			
			
		}
		
		body.setProperty("_yinbaolimit", yinbaolimit);
		body.setProperty("_xunhuibaolimit", xunhuibaolimit);
		body.setProperty("_leyinglimit", leyinglimit);
		body.setProperty("_dinpaylimit", dinpaylimit);
		body.setProperty("_xunfutonglimit", xunfutonglimit);
		body.setProperty("_yingtongbaolimit", yingtongbaolimit);
		body.setProperty("_tonghuilimit", tonghuilimit);
		body.setProperty("_huibolimit", huibolimit);
		body.setProperty("_xinbeilimit", xinbeilimit);
		body.setProperty("_gaotonglimit", gaotonglimit);
		body.setProperty("_saomalimit", saomalimit);
		body.setProperty("_sanvlimit", sanvlimit);
		body.setProperty("_dingyilimit", dingyilimit);
		body.setProperty("_changfulimit", changfulimit);
		body.setProperty("_xingheyilimit",xingheyilimit);
		body.setProperty("_jinyanglimit",jinyanglimit);
		body.setProperty("_huitianlimit",huitianlimit);
		body.setProperty("_antonglimit", antonglimit);
		body.setProperty("_lefubaolimit", lefubaolimit);
		body.setProperty("_huitlimit", huitlimit);
		body.setProperty("_jianyuelimit", jianyuelimit);
		body.call();
	}
	
	
	public static void _getNewMaxlimit(Map<?, ?> args, Closure body, PrintWriter out, 
	         ExecutableTemplate template, int fromLine){
		int onelimit = 0;
		int twolimit = 0;
		int threelimit = 0;
		int fourlimit = 0;
		
		
		List<Config> list = null;
		String channelname = (String)args.get("channelname");
		if(channelname.equals("wexin")){
			list = Config.getOpenWeiXin();
		}
		
		if(channelname.equals("ali")){
			list = Config.getOpenZhifubao();
		}
		
		if(channelname.equals("qq")){
			list = Config.getOpenQQ();
		}
		if(channelname.equals("jd") || channelname.equals("jingdong")){
			list = Config.getOpenJd();
		}
		if(channelname.equals("yinlian")){
			list = Config.getOpenYinLian();
		}
		
		for(Config fig : list){
			fig.sortpriority = StringUtils.trimToEmpty(fig.sortpriority);
			
			if(fig.sortpriority.equals("微信1") || fig.sortpriority.equalsIgnoreCase("qq1")  || fig.sortpriority.equalsIgnoreCase("京东1") || fig.sortpriority.equalsIgnoreCase("支付宝1") || fig.sortpriority.equalsIgnoreCase("银联1")){
				onelimit = fig.maxlimit;
			}else if(fig.sortpriority.equals("微信2")  || fig.sortpriority.equalsIgnoreCase("qq2")  || fig.sortpriority.equalsIgnoreCase("京东2") || fig.sortpriority.equalsIgnoreCase("支付宝2") || fig.sortpriority.equalsIgnoreCase("银联2")){
				twolimit = fig.maxlimit;
			}else if(fig.sortpriority.equals("微信3")   || fig.sortpriority.equalsIgnoreCase("qq3")  || fig.sortpriority.equalsIgnoreCase("京东3") || fig.sortpriority.equalsIgnoreCase("支付宝3") || fig.sortpriority.equalsIgnoreCase("银联3")){
				threelimit = fig.maxlimit;
			}else if(fig.sortpriority.equals("微信4")  || fig.sortpriority.equalsIgnoreCase("qq4")  || fig.sortpriority.equalsIgnoreCase("京东4") || fig.sortpriority.equalsIgnoreCase("支付宝4") || fig.sortpriority.equalsIgnoreCase("银联4")){
				fourlimit = fig.maxlimit;
			}
			
		}
		
		body.setProperty("_onelimit", onelimit);
		body.setProperty("_twolimit", twolimit);
		body.setProperty("_threelimit", threelimit);
		body.setProperty("_fourlimit", fourlimit);
		body.call();
	}
	
	
	
	public static void _forward(Map<?, ?> args, Closure body, PrintWriter out, 
	         ExecutableTemplate template, int fromLine){
		String url=(String)args.get("url");
		String login_name=(String)args.get("uname");
		Integer mode=(Integer)args.get("mode");
		String token =null;
		if(login_name!=null){
			token=TokenService.get(login_name);
		}
		/*if(mode!=null && !"".equals(mode)){
			url=url+"&mode="+mode;
		}*/
		if(token==null){
		  out.print(Play.configuration.get("static.pt")+"url="+url);
		}else{
			 out.print(Play.configuration.get("static.pt")+"token="+token+"&login_name="+login_name+"&url="+url);
		}
	}
	
	
//	

//	
//	public static void _page(Map<?, ?> args, Closure body, PrintWriter out, 
//	         ExecutableTemplate template, int fromLine){
//		String key=(String)args.get("key");
//		Integer page =(Integer)args.get("page");
//		Integer size =(Integer)args.get("size");
//		Integer total=ShenboService.getListCnt(key);
//        Integer first=1;
//        Integer last=(total%size==0)?(total/size):((total/size)+1);
//        Integer current=page;
//        Integer pre=(current>first)?(current-1):first;
//        Integer next=(last>current)?(current+1):last;
//        out.print("<div class=\"page\">");
//        if(first!=last){
//	        out.print("<a href=\"?page=1\"><span class=\"page_first\" page=\"1\">首页</span></a>");
//	        if(pre!=first){
//	          out.print("<a href=\"?page="+pre+"\"><span class=\"page_pre\" page=\""+pre+"\">上一页</span></a>");
//	        }
//	        if(next!=last){
//	           out.print("<a href=\"?page="+next+"\"><span class=\"page_next\" page=\""+next+"\">下一页</span></a>");
//	         }
//	        out.print("<a href=\"?page="+last+"\"><span class=\"page_last\" page=\""+last+"\">末页</span></a>");
//        }
//        out.print("<span class=\"page_current\">第"+current+"页</span>");
//        out.print("<span class=\"page_size\">每页"+size+"条</span>");
//        out.print("<span class=\"page_total\">共"+total+"条</span>");
//        out.print("</div>");
//        
//	}
	public static void _msg(Map<?, ?> args, Closure body, PrintWriter out, 
	         ExecutableTemplate template, int fromLine){
		Integer page =(Integer)args.get("page");
		Integer size =(Integer)args.get("size");
		List<MessageBoard> ads=MeiBoService.getMessageBoard(page-1, size);
		for(MessageBoard ad:ads){
			 body.setProperty("_msg", ad);
			 body.call();
		}
	}
	
	
	public static void _queryList(Map<?, ?> args, Closure body, PrintWriter out, 
	         ExecutableTemplate template, int fromLine){
		List depositList =(List)args.get("queryList");
		if(depositList.size()==0){
			out.print("<tr><td colspan=5>暂无记录</td></tr>");
		}else{
			for(Object dep:depositList){
				 body.setProperty("_obj", dep);
				 body.call();
			}
		}
	}
	
	public static void _queryListNoMes(Map<?, ?> args, Closure body, PrintWriter out, 
	         ExecutableTemplate template, int fromLine){
		List depositList =(List)args.get("queryList");
		if(depositList.size()==0){
			out.print("");
		}else{
			for(Object dep:depositList){
				 body.setProperty("_obj", dep);
				 body.call();
			}
		}
	}
	public static void _page(Map<?, ?> args, Closure body, PrintWriter out, 
	         ExecutableTemplate template, int fromLine){
		Integer count =(Integer)args.get("count");
		Integer page =(Integer)args.get("page");
		String start_date =(String)args.get("start_date");
		String end_date =(String)args.get("end_date");
		int maxPage=(count%10)==0?count/10:(count/10+1);
		if(count==0){maxPage=1;page=1;}
		if(page>maxPage)page=maxPage;
		out.print("<span class=\"disabled\">"+count+"条记录</span>");
		if(page==1){
		    out.print("<span class=\"disabled\"> |&lt; </span>");
		}else{
			out.print("<a href=\"?page=1&start_date="+start_date+"&end_date="+end_date+"\"> |&lt; </a>");
		}
		if(page!=1){
			out.print("<a href=\"?page="+(page-1)+"&start_date="+start_date+"&end_date="+end_date+"\"> &lt; </a>");
		}
		out.print("<span class=\"current\">"+page+"</span>");
		if(page!=maxPage){
			out.print("<a href=\"?page="+(page+1)+"&start_date="+start_date+"&end_date="+end_date+"\"> &gt; </a>");
		}
		if(page==maxPage){
			out.print("<span class=\"disabled\"> &gt;| </span>");
		}else{
			out.print("<a href=\"?page="+maxPage+"&start_date="+start_date+"&end_date="+end_date+"\"> &gt;| </a>");
		}
		out.print("<span class=\"disabled\">共计"+maxPage+"页</span>");
	}
	
	public static void _ag(Map<?, ?> args, Closure body, PrintWriter out, 
	         ExecutableTemplate template, int fromLine){
		boolean ag_flag=MeiBoService.getAgStatus();
		 body.setProperty("_ag_flag", ag_flag);
		 body.call();
	}
	
	public static void _bbin(Map<?, ?> args, Closure body, PrintWriter out, 
	         ExecutableTemplate template, int fromLine){
		boolean bbin_flag=MeiBoService.getBbinStatus();
		 body.setProperty("_bbin_flag", bbin_flag);
		 body.call();
	}
	
	public static void _sb(Map<?, ?> args, Closure body, PrintWriter out, 
	         ExecutableTemplate template, int fromLine){
		boolean sb_flag=MeiBoService.getSbStatus();
		 body.setProperty("_sb_flag", sb_flag);
		 body.call();
	}
	
	public static void _ptg(Map<?, ?> args, Closure body, PrintWriter out, 
	         ExecutableTemplate template, int fromLine){
		 boolean pt_flag=MeiBoService.getPtStatus();
		 body.setProperty("_pt_flag", pt_flag);
		 body.call();
	}
	
	public static void _kg(Map<?, ?> args, Closure body, PrintWriter out, 
	         ExecutableTemplate template, int fromLine){
		 boolean kg_flag=MeiBoService.getKgStatus();
		 body.setProperty("_kg_flag", kg_flag);
		 body.call();
	}
	
	public static void _openfire(Map<?, ?> args, Closure body, PrintWriter out, 
	         ExecutableTemplate template, int fromLine){
		 boolean openfire_flag=MeiBoService.getPpenfireStatus();
		 body.setProperty("_openfire_flag", openfire_flag);
		 body.call();
	}
	
	public static void _callPhone(Map<?, ?> args, Closure body, PrintWriter out, 
	         ExecutableTemplate template, int fromLine){
		 boolean call_flag=MeiBoService.getcallPhoneStatus();
		 
		 body.setProperty("_call_flag", call_flag);
		 body.call();
	}
	
	public static void _list(Map<?, ?> args, Closure body, PrintWriter out, 
	         ExecutableTemplate template, int fromLine){
		String key =(String)args.get("key");
		List list=ListService.factory(key);
		for(Object dep:list){
			 body.setProperty("_obj", dep);
			 body.call();
		}
	}
	//处理for循环
	public static void _myforeach(Map<?, ?> args, Closure body, PrintWriter out, 
	         ExecutableTemplate template, int fromLine){
		 String name=(String)args.get("uname");
		 Customer result = MeiBoService.getCustomerByUserName(name);
		 
		Integer count =result.cust_level;
		if(count>=1 && count<=6){
			for(int i=0;i<count;i++){
				 body.setProperty("_obj", i);
				 body.call();
			}
		}
		
		
	}
	
	public static void _hideMid(Map<?, ?> args, Closure body, PrintWriter out, 
	         ExecutableTemplate template, int fromLine){
		String key =(String)args.get("key");
		if(key.length()>=4){
			out.print(key.substring(0, 2)+"***"+key.substring(key.length()-1, key.length()));
		}else{
			out.print("*******");
		}
	}
	
	public static void _pt(Map<?, ?> args, Closure body, PrintWriter out, 
	         ExecutableTemplate template, int fromLine){
		List<PtGame> ptGame=MeiBoService.getPtGames();
		for(PtGame pt:ptGame){
			 body.setProperty("_pt",pt);
			 body.call();
		}
		/*
		String keys =(String)args.get("keys");
		String[] key  =keys.split(",");
		for(String k:key){
			String[] item=k.split(":");
			String ptcode=item[0];
			 body.setProperty("_ptcode", ptcode);
			 body.setProperty("_show", item[1]);
			 body.setProperty("_css", item[2]);
			 body.setProperty("_fcode", item[3]);
			 body.setProperty("_title", item[4]);
			 body.call();
		}*/
	}
	
	private static List<List<List<PtGame>>> getPages(List<PtGame> ptGames){
		int i=0;
		List<List<PtGame>> rows =new ArrayList<List<PtGame>>();
		List<PtGame> list =new ArrayList<PtGame>();
		for(PtGame pt:ptGames){
			 if(pt.cn_name==null||"".equals(pt.cn_name)){
					pt.cn_name=pt.game_name;
			 }
			 i++;
			 list.add(pt);
			 if(i%3==0){
				 rows.add(list);
				 list =new ArrayList<PtGame>(); 
			 }
		}
		if(list.size()!=0){
			rows.add(list);
		}
		List<List<List<PtGame>>> pages =new ArrayList<List<List<PtGame>>>();
		int j=0;
		List<List<PtGame>> page =new ArrayList<List<PtGame>>();
        for(List<PtGame> t:rows){
			j++;
			page.add(t);
			if(j%3==0){
				pages.add(page);
				page =new ArrayList<List<PtGame>>();
			}
		}
        if(page.size()!=0){
        	pages.add(page);
        }
        return pages;
	}
	
	public static void _pt_pool(Map<?, ?> args, Closure body, PrintWriter out, 
	         ExecutableTemplate template, int fromLine){
		List<PtGame> ptGame=MeiBoService.getPtGames();
		List<PtGame> ptGames =new ArrayList<PtGame>();
		String type=(String)args.get("type");
		if("pool".equals(type)){
			for(PtGame pt:ptGame){
				if(!(pt.progressive==null||"".equals(pt.progressive))){
					ptGames.add(pt);
				}
			}
		}
		if("all".equals(type)){
			ptGames=ptGame;
		}
		
		if("hot".equals(type)){
			for(PtGame pt:ptGame){
				if(pt.hot){
					ptGames.add(pt);
				}
			}
		}
		if("slot".equals(type)){
			for(PtGame pt:ptGame){
				if("Slot Machines".equals(pt.game_type)){
					ptGames.add(pt);
				}
			}
		}
		if("scratchcards".equals(type)){
			for(PtGame pt:ptGame){
				if("Scratchcards".equals(pt.game_type)){
					ptGames.add(pt);
				}
			}
		}
		if("pokers".equals(type)){
			for(PtGame pt:ptGame){
				if("Video Pokers".equals(pt.game_type)){
					ptGames.add(pt);
				}
			}
		}
		if("card".equals(type)){
			for(PtGame pt:ptGame){
				if("Table and Card Games".equals(pt.game_type)){
					ptGames.add(pt);
				}
			}
		}
		if("live".equals(type)){
			for(PtGame pt:ptGame){
				if("Live Games".equals(pt.game_type)){
					ptGames.add(pt);
				}
			}
		}
		
        body.setProperty("_pages",getPages(ptGames));
        body.call();
	}
	
	public static void _ag_pool(Map<?, ?> args, Closure body, PrintWriter out, 
	         ExecutableTemplate template, int fromLine){
		List<AgGame> agGame=MeiBoService.getAgGames();
		List<AgGame> agGames =new ArrayList<AgGame>();
		String type=(String)args.get("type");
		
		if("all".equals(type)){
			agGames=agGame;
		}
		
		if("hot".equals(type)){
			for(AgGame ag:agGame){
				if(ag.hot){
					agGames.add(ag);
				}
			}
		}
		
		if("new".equals(type)){
			for(AgGame ag:agGame){
				if(ag.is_new){
					agGames.add(ag);
				}
			}
		}
		
		if("slot".equals(type)){
			for(AgGame ag:agGame){
				if("slot".equals(ag.game_type)){
					agGames.add(ag);
				}
			}
		}
		
		if("tablegame".equals(type)){
			for(AgGame ag:agGame){
				if("tablegame".equals(ag.game_type)){
					agGames.add(ag);
				}
			}
		}
		
		if("videopoker".equals(type)){
			for(AgGame ag:agGame){
				if("videopoker".equals(ag.game_type)){
					agGames.add(ag);
				}
			}
		}
		
		if("yoplay".equals(type)){
			for(AgGame ag:agGame){
				if("yoplay".equals(ag.game_type)){
					agGames.add(ag);
				}
			}
		}
		
       body.setProperty("_pages",getAgPages(agGames));
       body.call();
	}
	
	
	public static void _sb_pool(Map<?, ?> args, Closure body, PrintWriter out, 
	         ExecutableTemplate template, int fromLine){
		List<SbGame> sbGame=MeiBoService.getShenBoGames();
		List<SbGame> sbGames =new ArrayList<SbGame>();
		String type=(String)args.get("type");
		
		if("all".equals(type)){
			sbGames=sbGame;
		}
		
      body.setProperty("_pages",getSbPages(sbGames));
      body.call();
	}
	
	
	
	public static void _pp_pool(Map<?, ?> args, Closure body, PrintWriter out, 
	         ExecutableTemplate template, int fromLine){
		List<PpGame> games=MeiBoService.getPpGames();
		
		List<PpGame> ppGames =new ArrayList<PpGame>();
		String type=(String)args.get("type");
		
		if("all".equals(type)){
			ppGames=games;
		}
		
		
		
     body.setProperty("_pages",getPpPages(ppGames));
     body.call();
	}
	
	public static void _mg_pool(Map<?, ?> args, Closure body, PrintWriter out, 
	         ExecutableTemplate template, int fromLine){
		List<MgGame> mgGame=MeiBoService.getMgGames();
		
		List<MgGame> mgGames =new ArrayList<MgGame>();
		String type=(String)args.get("type");
		
		if("all".equals(type)){
			mgGames=mgGame;
		}
		
		if("hot".equals(type)){
			for(MgGame mg:mgGame){
				if(mg.is_hot){
					mgGames.add(mg);
				}
			}
		}
		
		if("new".equals(type)){
			for(MgGame mg:mgGame){
				if(mg.is_new){
					mgGames.add(mg);
				}
			}
		}
		
		if("html5".equals(type)){
			for(MgGame mg:mgGame){
				if(mg.is_pchtml5){
					mgGames.add(mg);
				}
			}
		}
		
		if("slot".equals(type)){
			for(MgGame mg:mgGame){
				if("SLOTS".equals(mg.Category)){
					mgGames.add(mg);
				}
			}
		}
		
		if("tablegame".equals(type)){
			for(MgGame mg:mgGame){
				if("TABLE GAMES".equals(mg.Category)){
					mgGames.add(mg);
				}
			}
		}
		
		if("scratchcard".equals(type)){
			for(MgGame mg:mgGame){
				if("SCRATCH CARD".equals(mg.Category)){
					mgGames.add(mg);
				}
			}
		}
		
		if("videopoker".equals(type)){
			for(MgGame mg:mgGame){
				if("VIDEO POKER".equals(mg.Category)){
					mgGames.add(mg);
				}
			}
		}
		
		if("others".equals(type)){
			for(MgGame mg:mgGame){
				if("Others".equals(mg.Category)){
					mgGames.add(mg);
				}
			}
		}
		
      body.setProperty("_pages",getMgPages(mgGames));
      body.call();
	}
	
	private static List<List<List<AgGame>>> getAgPages(List<AgGame> agGames){
		int i=0;
		List<List<AgGame>> rows =new ArrayList<List<AgGame>>();
		List<AgGame> list =new ArrayList<AgGame>();
		for(AgGame ag:agGames){
			 i++;
			 list.add(ag);
			 if(i%5==0){
				 rows.add(list);
				 list =new ArrayList<AgGame>(); 
			 }
		}
		if(list.size()!=0){
			rows.add(list);
		}
		List<List<List<AgGame>>> pages =new ArrayList<List<List<AgGame>>>();
		int j=0;
		List<List<AgGame>> page =new ArrayList<List<AgGame>>();
        for(List<AgGame> t:rows){
			j++;
			page.add(t);
			if(j%5==0){
				pages.add(page);
				page =new ArrayList<List<AgGame>>();
			}
		}
        if(page.size()!=0){
        	pages.add(page);
        }
        return pages;
	}
	
	
	private static List<List<List<SbGame>>> getSbPages(List<SbGame> sbGames){
		int i=0;
		List<List<SbGame>> rows =new ArrayList<List<SbGame>>();
		List<SbGame> list =new ArrayList<SbGame>();
		for(SbGame ag:sbGames){
			 i++;
			 list.add(ag);
			 if(i%5==0){
				 rows.add(list);
				 list =new ArrayList<SbGame>(); 
			 }
		}
		if(list.size()!=0){
			rows.add(list);
		}
		List<List<List<SbGame>>> pages =new ArrayList<List<List<SbGame>>>();
		int j=0;
		List<List<SbGame>> page =new ArrayList<List<SbGame>>();
        for(List<SbGame> t:rows){
			j++;
			page.add(t);
			if(j%4==0){
				pages.add(page);
				page =new ArrayList<List<SbGame>>();
			}
		}
        if(page.size()!=0){
        	pages.add(page);
        }
        return pages;
	}
	
	private static List<List<List<PpGame>>> getPpPages(List<PpGame> ppGames){
		int i=0;
		List<List<PpGame>> rows =new ArrayList<List<PpGame>>();
		List<PpGame> list =new ArrayList<PpGame>();
		for(PpGame ag:ppGames){
			 i++;
			 list.add(ag);
			 if(i%5==0){
				 rows.add(list);
				 list =new ArrayList<PpGame>(); 
			 }
		}
		if(list.size()!=0){
			rows.add(list);
		}
		List<List<List<PpGame>>> pages =new ArrayList<List<List<PpGame>>>();
		int j=0;
		List<List<PpGame>> page =new ArrayList<List<PpGame>>();
        for(List<PpGame> t:rows){
			j++;
			page.add(t);
			if(j%5==0){
				pages.add(page);
				page =new ArrayList<List<PpGame>>();
			}
		}
        if(page.size()!=0){
        	pages.add(page);
        }
        return pages;
	}
	
	private static List<List<List<MgGame>>> getMgPages(List<MgGame> mgGames){
		int i=0;
		List<List<MgGame>> rows =new ArrayList<List<MgGame>>();
		List<MgGame> list =new ArrayList<MgGame>();
		for(MgGame ag:mgGames){
			 i++;
			 list.add(ag);
			 if(i%5==0){
				 rows.add(list);
				 list =new ArrayList<MgGame>(); 
			 }
		}
		if(list.size()!=0){
			rows.add(list);
		}
		List<List<List<MgGame>>> pages =new ArrayList<List<List<MgGame>>>();
		int j=0;
		List<List<MgGame>> page =new ArrayList<List<MgGame>>();
        for(List<MgGame> t:rows){
			j++;
			page.add(t);
			if(j%5==0){
				pages.add(page);
				page =new ArrayList<List<MgGame>>();
			}
		}
        if(page.size()!=0){
        	pages.add(page);
        }
        return pages;
	}
	
	
	public static void _pt_page(Map<?, ?> args, Closure body, PrintWriter out, 
	         ExecutableTemplate template, int fromLine){
		List<List<List<PtGame>>> pages =(List<List<List<PtGame>>>)args.get("pages");
		int p=0;
		for(List<List<PtGame>> page :pages){
			 p++;
			 body.setProperty("_page",page);
			 body.setProperty("_p",p);
		     body.call();
		}
	}
	
	public static void _ag_page(Map<?, ?> args, Closure body, PrintWriter out, 
	         ExecutableTemplate template, int fromLine){
		List<List<List<AgGame>>> pages =(List<List<List<AgGame>>>)args.get("pages");
		
		int p=0;
		for(List<List<AgGame>> page :pages){
			 p++;
			 body.setProperty("_page",page);
			 body.setProperty("_p",p);
		     body.call();
		}
	}
	
	public static void _shenbo_page(Map<?, ?> args, Closure body, PrintWriter out, 
	         ExecutableTemplate template, int fromLine){
		List<List<List<SbGame>>> pages =(List<List<List<SbGame>>>)args.get("pages");
		
		int p=0;
		for(List<List<SbGame>> page :pages){
			 p++;
			 body.setProperty("_page",page);
			 body.setProperty("_p",p);
		     body.call();
		}
	}
	
	
	public static void _mg_page(Map<?, ?> args, Closure body, PrintWriter out, 
	         ExecutableTemplate template, int fromLine){
		List<List<List<MgGame>>> pages =(List<List<List<MgGame>>>)args.get("pages");
		
		int p=0;
		for(List<List<MgGame>> page :pages){
			 p++;
			 body.setProperty("_page",page);
			 body.setProperty("_p",p);
		     body.call();
		}
	}
	
	public static void _pp_page(Map<?, ?> args, Closure body, PrintWriter out, 
	         ExecutableTemplate template, int fromLine){
		List<List<List<PpGame>>> pages =(List<List<List<PpGame>>>)args.get("pages");
		
		int p=0;
		for(List<List<PpGame>> page :pages){
			 p++;
			 body.setProperty("_page",page);
			 body.setProperty("_p",p);
		     body.call();
		}
	}
	
	public static void _pp_cell(Map<?, ?> args, Closure body, PrintWriter out, 
	         ExecutableTemplate template, int fromLine){
		List<PpGame> row =(List<PpGame>)args.get("row");
		for(PpGame cell :row){
			 body.setProperty("_cell",cell);
			 body.setProperty("_jpg","/public/images/ppgames/"+cell.Image_File_Name);
		     body.call();
		}
	}
	
	public static void _pp_row(Map<?, ?> args, Closure body, PrintWriter out, 
	         ExecutableTemplate template, int fromLine){
		List<List<PpGame>> page =(List<List<PpGame>>)args.get("page");
		for(List<PpGame> row :page){
			 body.setProperty("_row",row);
		     body.call();
		}
	}
	
	public static void _mg_row(Map<?, ?> args, Closure body, PrintWriter out, 
	         ExecutableTemplate template, int fromLine){
		List<List<MgGame>> page =(List<List<MgGame>>)args.get("page");
		for(List<MgGame> row :page){
			 body.setProperty("_row",row);
		     body.call();
		}
	}
	
	
	
	public static void _mg_cell(Map<?, ?> args, Closure body, PrintWriter out, 
	         ExecutableTemplate template, int fromLine){
		List<MgGame> row =(List<MgGame>)args.get("row");
		for(MgGame cell :row){
			 body.setProperty("_cell",cell);
			 body.setProperty("_jpg","/public/images/mggames/"+cell.Image_File_Name);
		     body.call();
		}
	}
	
	
	public static void _sb_row(Map<?, ?> args, Closure body, PrintWriter out, 
	         ExecutableTemplate template, int fromLine){
		List<List<SbGame>> page =(List<List<SbGame>>)args.get("page");
		for(List<SbGame> row :page){
			 body.setProperty("_row",row);
		     body.call();
		}
	}
	
	public static void _sb_cell(Map<?, ?> args, Closure body, PrintWriter out, 
	         ExecutableTemplate template, int fromLine){
		List<SbGame> row =(List<SbGame>)args.get("row");
		for(SbGame cell :row){
			 body.setProperty("_cell",cell);
			 body.setProperty("_jpg","/public/images/sbgames/"+cell.image_game_name);
			 body.setProperty("_previewjpg","/public/images/sbgames/preview/"+cell.image_preview_name);
		     body.call();
		}
	}
	
	
	public static void _ag_row(Map<?, ?> args, Closure body, PrintWriter out, 
	         ExecutableTemplate template, int fromLine){
		List<List<AgGame>> page =(List<List<AgGame>>)args.get("page");
		for(List<AgGame> row :page){
			 body.setProperty("_row",row);
		     body.call();
		}
	}
	
	public static void _ag_cell(Map<?, ?> args, Closure body, PrintWriter out, 
	         ExecutableTemplate template, int fromLine){
		List<AgGame> row =(List<AgGame>)args.get("row");
		for(AgGame cell :row){
			 body.setProperty("_cell",cell);
			 body.setProperty("_jpg","/public/images/aggames/"+cell.Image_File_Name);
		     body.call();
		}
	}
	
	public static void _pt_row(Map<?, ?> args, Closure body, PrintWriter out, 
	         ExecutableTemplate template, int fromLine){
		List<List<PtGame>> page =(List<List<PtGame>>)args.get("page");
		for(List<PtGame> row :page){
			 body.setProperty("_row",row);
		     body.call();
		}
	}
	
	public static void _pt_cell(Map<?, ?> args, Closure body, PrintWriter out, 
	         ExecutableTemplate template, int fromLine){
		List<PtGame> row =(List<PtGame>)args.get("row");
		for(PtGame cell :row){
			 body.setProperty("_cell",cell);
			 body.setProperty("_jpg","/public/images/Egame/ptgames/"+cell.game_code+".jpg");
		     body.call();
		}
	}
	
	
	public static void _module(Map<?, ?> args, Closure body, PrintWriter out, 
	         ExecutableTemplate template, int fromLine){
		List<PtGame> row =(List<PtGame>)args.get("row");
		for(PtGame cell :row){
			 body.setProperty("_cell",cell);
			 body.setProperty("_jpg","/public/images/Egame/ptgames/"+cell.game_code+".jpg");
		     body.call();
		}
	}
	
	public static void _bank(Map<?, ?> args, Closure body, PrintWriter out, 
	         ExecutableTemplate template, int fromLine){
		String bank_id = (String)args.get("bank_id");
		BankInfo bank =BankInfo.getBank(bank_id);
		if(bank!=null)
			 body.setProperty("_bank",bank);
		  body.call();
	}
	//======================华丽的分割线=======================
	public static void _pt_hot_pool(Map<?, ?> args, Closure body, PrintWriter out, 
	         ExecutableTemplate template, int fromLine){
		List<PtGame> ptGame=MeiBoService.getPtGames();
		String type=(String)args.get("type");
		int i=0;
		if("hot".equals(type)){
			for(PtGame pt:ptGame){
				if(pt.hot && i<12){
					i++;
					body.setProperty("_pt_hots",pt);
				    body.call();
				}
			}
		}
      
	}
	public static void _mg_hot_pool(Map<?, ?> args, Closure body, PrintWriter out, 
	         ExecutableTemplate template, int fromLine){
		List<MgGame> mgGame=MeiBoService.getMgGames();
		String type=(String)args.get("type");
		int i=0;
		if("hot".equals(type)){
			for(MgGame mg:mgGame){
				if(mg.is_hot && i<12){
					i++;
					body.setProperty("_mg_hots",mg);
				    body.call();
				}
			}
		}
	}
	
	public static void _ag_hot_pool(Map<?, ?> args, Closure body, PrintWriter out, 
	         ExecutableTemplate template, int fromLine){
		List<AgGame> agGame=MeiBoService.getAgGames();
		String type=(String)args.get("type");
		int i=0;
		if("hot".equals(type)){
			for(AgGame ag:agGame){
				if(ag.hot && i<12){
					i++;
					body.setProperty("_ag_hots",ag);
				    body.call();
				}
			}
		}
      
	}
	
	public static void _sb_hot_pool(Map<?, ?> args, Closure body, PrintWriter out, 
	         ExecutableTemplate template, int fromLine){
		List<SbGame> sbGame=MeiBoService.getShenBoGames();
		String type=(String)args.get("type");
		int i=0;
		if("all".equals(type)){
			for(SbGame sb:sbGame){
				if(i<12){
					i++;
				     body.setProperty("_sb_hots",sb);
				     body.call();
				}
			}
		}
		
	}
	
	public static void _bbin_hot_pool(Map<?, ?> args, Closure body, PrintWriter out, 
	         ExecutableTemplate template, int fromLine){
		List<BbinGame> bbinGame=MeiBoService.getBbinGames();
		String type=(String)args.get("type");
		int i=0;
		if("hot".equals(type)){
			//bbinGames=bbinGame;
			for(BbinGame bbin:bbinGame){
				if(i<12 && bbin.enter_directly){
					i++;
					 body.setProperty("_bbin_hots",bbin);
					 body.call();
				}
			}
		}
	}
	
	public static void _winning_pool(Map<?, ?> args, Closure body, PrintWriter out, 
	         ExecutableTemplate template, int fromLine){
 		List winninginfo = getWinList();
		StringBuffer sb = new StringBuffer();
		 for(int j=0;j<winninginfo.size();j++){
			 sb.append(winninginfo.get(j));
		 }
		 
		body.setProperty("_winlist",sb.toString());
		body.call();
	}
	
	
	public static List getWinList(){
		List winListInfo = new ArrayList();
		List<WinList> winList=MeiBoService.getWinList();
		for(int i=0; i<winList.size(); i++){
			String ori_login_name = winList.get(i).login_name;
			String login_name = "";
			
			if(ori_login_name.length()>=4){
				login_name = ori_login_name.substring(0, 2) + "***" + ori_login_name.substring(ori_login_name.length()-2);
			}
			else if (ori_login_name.length()==3){
				login_name = ori_login_name.substring(0, 2) + "***" + ori_login_name.substring(2);
			}
			else{
				login_name = ori_login_name.substring(0, 2) + "***";
			}
			
			String winListItem = "<li><a href=\"javascript:void(0);\" class=\"m_game\" style=\"background: url('"+Play.configuration.get("static.url");
			winListItem += winList.get(i).img_path +"') no-repeat center ;background-size:cover; \"></a>";
			winListItem += "<div class='m_text'><span>"+winList.get(i).platform+"</span>";
			winListItem += "<span class='g_name'>"+winList.get(i).game_name+"</span><span class='user_name'>";
			winListItem += login_name+"</span><span class='win'>赢了"+winList.get(i).win_amount+"元</span></div></li>";
			winListInfo.add(winListItem);
		}
		Collections.shuffle(winListInfo);
		return winListInfo;
	}	
	
	
	
	
	public static List getWinList2(){
		List winninginfo = new ArrayList();
		List<PtGame> ptGame=MeiBoService.getPtGames();
		List<MgGame> mgGame=MeiBoService.getMgGames();
		List<AgGame> agGame=MeiBoService.getAgGames();
		List<SbGame> sbGame=MeiBoService.getShenBoGames();
		List<BbinGame> bbinGame=MeiBoService.getBbinGames();
		
		for(int i=0;i<10;i++){
			
			int count = getLottery();
			if(count==300000){
				//pt
				String ptinfo ="<li><a href=\"javascript:void(0);\" class=\"m_game\" style=\"background: url('"+Play.configuration.get("static.url")+"/public/images/Egame/ptgames/"+ptGame.get(i).game_code+".jpg') no-repeat center ;background-size:cover; \"></a>"+
					       "<div class='m_text'><span class='g_name'>"+ptGame.get(i).cn_name+"</span><span class='user_name'>"+
					       getStringRandom(2)+"***"+(new Random().nextInt(100)+1)+"</span><span class='win'>赢了"+(new Random().nextInt(1000000)+300000)+"元</span></div></li>";
				winninginfo.add(ptinfo);
			
			//mg
				String mginfo ="<li><a href=\"javascript:void(0);\" class=\"m_game\" style=\"background: url('"+Play.configuration.get("static.url")+"/public/images/mggames/"+mgGame.get(i).Image_File_Name+"') no-repeat center ;background-size:cover; \"></a>"+
					       "<div class='m_text'><span class='g_name'>"+mgGame.get(i).CHINESE_SIMP_Game_Name+"</span><span class='user_name'>"+
					       getStringRandom(2)+"***"+(new Random().nextInt(100)+1)+"</span><span class='win'>赢了"+(new Random().nextInt(1000000)+300000)+"元</span></div></li>";
				winninginfo.add(mginfo);

			//ag
				String aginfo ="<li><a href=\"javascript:void(0);\" class=\"m_game\" style=\"background: url('"+Play.configuration.get("static.url")+"/public/images/aggames/"+agGame.get(i).Image_File_Name+"') no-repeat center ;background-size:cover; \"></a>"+
					       "<div class='m_text'><span class='g_name'>"+agGame.get(i).game_name+"</span><span class='user_name'>"+
					       getStringRandom(2)+"***"+(new Random().nextInt(100)+1)+"</span><span class='win'>赢了"+(new Random().nextInt(1000000)+300000)+"元</span></div></li>";
				winninginfo.add(aginfo);
			
			
			//sb
			
				String sbinfo ="<li><a href=\"javascript:void(0);\" class=\"m_game\" style=\"background: url('"+Play.configuration.get("static.url")+"/public/images/sbgames/"+sbGame.get(i).image_game_name+"') no-repeat center ;background-size:cover; \"></a>"+
					       "<div class='m_text'><span class='g_name'>"+sbGame.get(i).game_name+"</span><span class='user_name'>"+
					       getStringRandom(2)+"***"+(new Random().nextInt(100)+1)+"</span><span class='win'>赢了"+(new Random().nextInt(1000000)+300000)+"元</span></div></li>";
				winninginfo.add(sbinfo);
			
			
			//bbin
			
				String bbinfo ="<li><a href=\"javascript:void(0);\" class=\"m_game\" style=\"background: url('"+Play.configuration.get("static.url")+"/public/images/bbingames/"+bbinGame.get(i).Image_File_Name+"') no-repeat center ;background-size:cover; \"></a>"+
					       "<div class='m_text'><span class='g_name'>"+bbinGame.get(i).game_name+"</span><span class='user_name'>"+
					       getStringRandom(2)+"***"+(new Random().nextInt(100)+1)+"</span><span class='win'>赢了"+(new Random().nextInt(1000000)+300000)+"元</span></div></li>";
				winninginfo.add(bbinfo);
			}else if(count==200000){
				String ptinfo ="<li><a href=\"javascript:void(0);\" class=\"m_game\" style=\"background: url('"+Play.configuration.get("static.url")+"/public/images/Egame/ptgames/"+ptGame.get(i).game_code+".jpg') no-repeat center ;background-size:cover; \"></a>"+
					       "<div class='m_text'><span class='g_name'>"+ptGame.get(i).cn_name+"</span><span class='user_name'>"+
					       getStringRandom(2)+"***"+(new Random().nextInt(100)+1)+"</span><span class='win'>赢了"+(new Random().nextInt(300000)+200000)+"元</span></div></li>";
				winninginfo.add(ptinfo);
			
			//mg
				String mginfo ="<li><a href=\"javascript:void(0);\" class=\"m_game\" style=\"background: url('"+Play.configuration.get("static.url")+"/public/images/mggames/"+mgGame.get(i).Image_File_Name+"') no-repeat center ;background-size:cover; \"></a>"+
					       "<div class='m_text'><span class='g_name'>"+mgGame.get(i).CHINESE_SIMP_Game_Name+"</span><span class='user_name'>"+
					       getStringRandom(2)+"***"+(new Random().nextInt(100)+1)+"</span><span class='win'>赢了"+(new Random().nextInt(300000)+200000)+"元</span></div></li>";
				winninginfo.add(mginfo);

			//ag
				String aginfo ="<li><a href=\"javascript:void(0);\" class=\"m_game\" style=\"background: url('"+Play.configuration.get("static.url")+"/public/images/aggames/"+agGame.get(i).Image_File_Name+"') no-repeat center ;background-size:cover; \"></a>"+
					       "<div class='m_text'><span class='g_name'>"+agGame.get(i).game_name+"</span><span class='user_name'>"+
					       getStringRandom(2)+"***"+(new Random().nextInt(100)+1)+"</span><span class='win'>赢了"+(new Random().nextInt(300000)+200000)+"元</span></div></li>";
				winninginfo.add(aginfo);
			
			
			//sb
			
				String sbinfo ="<li><a href=\"javascript:void(0);\" class=\"m_game\" style=\"background: url('"+Play.configuration.get("static.url")+"/public/images/sbgames/"+sbGame.get(i).image_game_name+"') no-repeat center ;background-size:cover; \"></a>"+
					       "<div class='m_text'><span class='g_name'>"+sbGame.get(i).game_name+"</span><span class='user_name'>"+
					       getStringRandom(2)+"***"+(new Random().nextInt(100)+1)+"</span><span class='win'>赢了"+(new Random().nextInt(300000)+200000)+"元</span></div></li>";
				winninginfo.add(sbinfo);
			
			
			//bbin
			
				String bbinfo ="<li><a href=\"javascript:void(0);\" class=\"m_game\" style=\"background: url('"+Play.configuration.get("static.url")+"/public/images/bbingames/"+bbinGame.get(i).Image_File_Name+"') no-repeat center ;background-size:cover; \"></a>"+
					       "<div class='m_text'><span class='g_name'>"+bbinGame.get(i).game_name+"</span><span class='user_name'>"+
					       getStringRandom(2)+"***"+(new Random().nextInt(100)+1)+"</span><span class='win'>赢了"+(new Random().nextInt(300000)+200000)+"元</span></div></li>";
				winninginfo.add(bbinfo);
			}else if(count==100000){
				String ptinfo ="<li><a href=\"javascript:void(0);\" class=\"m_game\" style=\"background: url('"+Play.configuration.get("static.url")+"/public/images/Egame/ptgames/"+ptGame.get(i).game_code+".jpg') no-repeat center ;background-size:cover; \"></a>"+
					       "<div class='m_text'><span class='g_name'>"+ptGame.get(i).cn_name+"</span><span class='user_name'>"+
					       getStringRandom(2)+"***"+(new Random().nextInt(100)+1)+"</span><span class='win'>赢了"+(new Random().nextInt(200000)+100000)+"元</span></div></li>";
				winninginfo.add(ptinfo);
			
			//mg
				String mginfo ="<li><a href=\"javascript:void(0);\" class=\"m_game\" style=\"background: url('"+Play.configuration.get("static.url")+"/public/images/mggames/"+mgGame.get(i).Image_File_Name+"') no-repeat center ;background-size:cover; \"></a>"+
					       "<div class='m_text'><span class='g_name'>"+mgGame.get(i).CHINESE_SIMP_Game_Name+"</span><span class='user_name'>"+
					       getStringRandom(2)+"***"+(new Random().nextInt(100)+1)+"</span><span class='win'>赢了"+(new Random().nextInt(200000)+100000)+"元</span></div></li>";
				winninginfo.add(mginfo);

			//ag
				String aginfo ="<li><a href=\"javascript:void(0);\" class=\"m_game\" style=\"background: url('"+Play.configuration.get("static.url")+"/public/images/aggames/"+agGame.get(i).Image_File_Name+"') no-repeat center ;background-size:cover; \"></a>"+
					       "<div class='m_text'><span class='g_name'>"+agGame.get(i).game_name+"</span><span class='user_name'>"+
					       getStringRandom(2)+"***"+(new Random().nextInt(100)+1)+"</span><span class='win'>赢了"+(new Random().nextInt(200000)+100000)+"元</span></div></li>";
				winninginfo.add(aginfo);
			
			
			//sb
			
				String sbinfo ="<li><a href=\"javascript:void(0);\" class=\"m_game\" style=\"background: url('"+Play.configuration.get("static.url")+"/public/images/sbgames/"+sbGame.get(i).image_game_name+"') no-repeat center ;background-size:cover; \"></a>"+
					       "<div class='m_text'><span class='g_name'>"+sbGame.get(i).game_name+"</span><span class='user_name'>"+
					       getStringRandom(2)+"***"+(new Random().nextInt(100)+1)+"</span><span class='win'>赢了"+(new Random().nextInt(200000)+100000)+"元</span></div></li>";
				winninginfo.add(sbinfo);
			
			
			//bbin
			
				String bbinfo ="<li><a href=\"javascript:void(0);\" class=\"m_game\" style=\"background: url('"+Play.configuration.get("static.url")+"/public/images/bbingames/"+bbinGame.get(i).Image_File_Name+"') no-repeat center ;background-size:cover; \"></a>"+
					       "<div class='m_text'><span class='g_name'>"+bbinGame.get(i).game_name+"</span><span class='user_name'>"+
					       getStringRandom(2)+"***"+(new Random().nextInt(100)+1)+"</span><span class='win'>赢了"+(new Random().nextInt(200000)+100000)+"元</span></div></li>";
				winninginfo.add(bbinfo);
			}else if(count==80000){
				String ptinfo ="<li><a href=\"javascript:void(0);\" class=\"m_game\" style=\"background: url('"+Play.configuration.get("static.url")+"/public/images/Egame/ptgames/"+ptGame.get(i).game_code+".jpg') no-repeat center ;background-size:cover; \"></a>"+
					       "<div class='m_text'><span class='g_name'>"+ptGame.get(i).cn_name+"</span><span class='user_name'>"+
					       getStringRandom(2)+"***"+(new Random().nextInt(100)+1)+"</span><span class='win'>赢了"+(new Random().nextInt(100000)+80000)+"元</span></div></li>";
				winninginfo.add(ptinfo);
			
			//mg
				String mginfo ="<li><a href=\"javascript:void(0);\" class=\"m_game\" style=\"background: url('"+Play.configuration.get("static.url")+"/public/images/mggames/"+mgGame.get(i).Image_File_Name+"') no-repeat center ;background-size:cover; \"></a>"+
					       "<div class='m_text'><span class='g_name'>"+mgGame.get(i).CHINESE_SIMP_Game_Name+"</span><span class='user_name'>"+
					       getStringRandom(2)+"***"+(new Random().nextInt(100)+1)+"</span><span class='win'>赢了"+(new Random().nextInt(100000)+80000)+"元</span></div></li>";
				winninginfo.add(mginfo);

			//ag
				String aginfo ="<li><a href=\"javascript:void(0);\" class=\"m_game\" style=\"background: url('"+Play.configuration.get("static.url")+"/public/images/aggames/"+agGame.get(i).Image_File_Name+"') no-repeat center ;background-size:cover; \"></a>"+
					       "<div class='m_text'><span class='g_name'>"+agGame.get(i).game_name+"</span><span class='user_name'>"+
					       getStringRandom(2)+"***"+(new Random().nextInt(100)+1)+"</span><span class='win'>赢了"+(new Random().nextInt(100000)+80000)+"元</span></div></li>";
				winninginfo.add(aginfo);
			
			
			//sb
			
				String sbinfo ="<li><a href=\"javascript:void(0);\" class=\"m_game\" style=\"background: url('"+Play.configuration.get("static.url")+"/public/images/sbgames/"+sbGame.get(i).image_game_name+"') no-repeat center ;background-size:cover; \"></a>"+
					       "<div class='m_text'><span class='g_name'>"+sbGame.get(i).game_name+"</span><span class='user_name'>"+
					       getStringRandom(2)+"***"+(new Random().nextInt(100)+1)+"</span><span class='win'>赢了"+(new Random().nextInt(100000)+80000)+"元</span></div></li>";
				winninginfo.add(sbinfo);
			
			
			//bbin
			
				String bbinfo ="<li><a href=\"javascript:void(0);\" class=\"m_game\" style=\"background: url('"+Play.configuration.get("static.url")+"/public/images/bbingames/"+bbinGame.get(i).Image_File_Name+"') no-repeat center ;background-size:cover; \"></a>"+
					       "<div class='m_text'><span class='g_name'>"+bbinGame.get(i).game_name+"</span><span class='user_name'>"+
					       getStringRandom(2)+"***"+(new Random().nextInt(100)+1)+"</span><span class='win'>赢了"+(new Random().nextInt(100000)+80000)+"元</span></div></li>";
				winninginfo.add(bbinfo);
			}else if(count==50000){
				String ptinfo ="<li><a href=\"javascript:void(0);\" class=\"m_game\" style=\"background: url('"+Play.configuration.get("static.url")+"/public/images/Egame/ptgames/"+ptGame.get(i).game_code+".jpg') no-repeat center ;background-size:cover; \"></a>"+
					       "<div class='m_text'><span class='g_name'>"+ptGame.get(i).cn_name+"</span><span class='user_name'>"+
					       getStringRandom(2)+"***"+(new Random().nextInt(100)+1)+"</span><span class='win'>赢了"+(new Random().nextInt(80000)+50000)+"元</span></div></li>";
				winninginfo.add(ptinfo);
			
			//mg
				String mginfo ="<li><a href=\"javascript:void(0);\" class=\"m_game\" style=\"background: url('"+Play.configuration.get("static.url")+"/public/images/mggames/"+mgGame.get(i).Image_File_Name+"') no-repeat center ;background-size:cover; \"></a>"+
					       "<div class='m_text'><span class='g_name'>"+mgGame.get(i).CHINESE_SIMP_Game_Name+"</span><span class='user_name'>"+
					       getStringRandom(2)+"***"+(new Random().nextInt(100)+1)+"</span><span class='win'>赢了"+(new Random().nextInt(80000)+50000)+"元</span></div></li>";
				winninginfo.add(mginfo);

			//ag
				String aginfo ="<li><a href=\"javascript:void(0);\" class=\"m_game\" style=\"background: url('"+Play.configuration.get("static.url")+"/public/images/aggames/"+agGame.get(i).Image_File_Name+"') no-repeat center ;background-size:cover; \"></a>"+
					       "<div class='m_text'><span class='g_name'>"+agGame.get(i).game_name+"</span><span class='user_name'>"+
					       getStringRandom(2)+"***"+(new Random().nextInt(100)+1)+"</span><span class='win'>赢了"+(new Random().nextInt(80000)+50000)+"元</span></div></li>";
				winninginfo.add(aginfo);
			//sb
			
				String sbinfo ="<li><a href=\"javascript:void(0);\" class=\"m_game\" style=\"background: url('"+Play.configuration.get("static.url")+"/public/images/sbgames/"+sbGame.get(i).image_game_name+"') no-repeat center ;background-size:cover; \"></a>"+
					       "<div class='m_text'><span class='g_name'>"+sbGame.get(i).game_name+"</span><span class='user_name'>"+
					       getStringRandom(2)+"***"+(new Random().nextInt(100)+1)+"</span><span class='win'>赢了"+(new Random().nextInt(80000)+50000)+"元</span></div></li>";
				winninginfo.add(sbinfo);
			//bbin
				String bbinfo ="<li><a href=\"javascript:void(0);\" class=\"m_game\" style=\"background: url('"+Play.configuration.get("static.url")+"/public/images/bbingames/"+bbinGame.get(i).Image_File_Name+"') no-repeat center ;background-size:cover; \"></a>"+
					       "<div class='m_text'><span class='g_name'>"+bbinGame.get(i).game_name+"</span><span class='user_name'>"+
					       getStringRandom(2)+"***"+(new Random().nextInt(100)+1)+"</span><span class='win'>赢了"+(new Random().nextInt(80000)+50000)+"元</span></div></li>";
				winninginfo.add(bbinfo);
			}else if(count==10000){
				String ptinfo ="<li><a href=\"javascript:void(0);\" class=\"m_game\" style=\"background: url('"+Play.configuration.get("static.url")+"/public/images/Egame/ptgames/"+ptGame.get(i).game_code+".jpg') no-repeat center ;background-size:cover; \"></a>"+
					       "<div class='m_text'><span class='g_name'>"+ptGame.get(i).cn_name+"</span><span class='user_name'>"+
					       getStringRandom(2)+"***"+(new Random().nextInt(100)+1)+"</span><span class='win'>赢了"+(new Random().nextInt(50000)+10000)+"元</span></div></li>";
				winninginfo.add(ptinfo);
			
			//mg
				String mginfo ="<li><a href=\"javascript:void(0);\" class=\"m_game\" style=\"background: url('"+Play.configuration.get("static.url")+"/public/images/mggames/"+mgGame.get(i).Image_File_Name+"') no-repeat center ;background-size:cover; \"></a>"+
					       "<div class='m_text'><span class='g_name'>"+mgGame.get(i).CHINESE_SIMP_Game_Name+"</span><span class='user_name'>"+
					       getStringRandom(2)+"***"+(new Random().nextInt(100)+1)+"</span><span class='win'>赢了"+(new Random().nextInt(50000)+10000)+"元</span></div></li>";
				winninginfo.add(mginfo);

			//ag
				String aginfo ="<li><a href=\"javascript:void(0);\" class=\"m_game\" style=\"background: url('"+Play.configuration.get("static.url")+"/public/images/aggames/"+agGame.get(i).Image_File_Name+"') no-repeat center ;background-size:cover; \"></a>"+
					       "<div class='m_text'><span class='g_name'>"+agGame.get(i).game_name+"</span><span class='user_name'>"+
					       getStringRandom(2)+"***"+(new Random().nextInt(100)+1)+"</span><span class='win'>赢了"+(new Random().nextInt(50000)+10000)+"元</span></div></li>";
				winninginfo.add(aginfo);
			
			
			//sb
			
				String sbinfo ="<li><a href=\"javascript:void(0);\" class=\"m_game\" style=\"background: url('"+Play.configuration.get("static.url")+"/public/images/sbgames/"+sbGame.get(i).image_game_name+"') no-repeat center ;background-size:cover; \"></a>"+
					       "<div class='m_text'><span class='g_name'>"+sbGame.get(i).game_name+"</span><span class='user_name'>"+
					       getStringRandom(2)+"***"+(new Random().nextInt(100)+1)+"</span><span class='win'>赢了"+(new Random().nextInt(50000)+10000)+"元</span></div></li>";
				winninginfo.add(sbinfo);
			
			
			//bbin
			
				String bbinfo ="<li><a href=\"javascript:void(0);\" class=\"m_game\" style=\"background: url('"+Play.configuration.get("static.url")+"/public/images/bbingames/"+bbinGame.get(i).Image_File_Name+"') no-repeat center ;background-size:cover; \"></a>"+
					       "<div class='m_text'><span class='g_name'>"+bbinGame.get(i).game_name+"</span><span class='user_name'>"+
					       getStringRandom(2)+"***"+(new Random().nextInt(100)+1)+"</span><span class='win'>赢了"+(new Random().nextInt(50000)+10000)+"元</span></div></li>";
				winninginfo.add(bbinfo);
			}else if(count==5000){
				String ptinfo ="<li><a href=\"javascript:void(0);\" class=\"m_game\" style=\"background: url('"+Play.configuration.get("static.url")+"/public/images/Egame/ptgames/"+ptGame.get(i).game_code+".jpg') no-repeat center ;background-size:cover; \"></a>"+
					       "<div class='m_text'><span class='g_name'>"+ptGame.get(i).cn_name+"</span><span class='user_name'>"+
					       getStringRandom(2)+"***"+(new Random().nextInt(100)+1)+"</span><span class='win'>赢了"+(new Random().nextInt(10000)+5000)+"元</span></div></li>";
				winninginfo.add(ptinfo);
			
			//mg
				String mginfo ="<li><a href=\"javascript:void(0);\" class=\"m_game\" style=\"background: url('"+Play.configuration.get("static.url")+"/public/images/mggames/"+mgGame.get(i).Image_File_Name+"') no-repeat center ;background-size:cover; \"></a>"+
					       "<div class='m_text'><span class='g_name'>"+mgGame.get(i).CHINESE_SIMP_Game_Name+"</span><span class='user_name'>"+
					       getStringRandom(2)+"***"+(new Random().nextInt(100)+1)+"</span><span class='win'>赢了"+(new Random().nextInt(10000)+5000)+"元</span></div></li>";
				winninginfo.add(mginfo);

			//ag
				String aginfo ="<li><a href=\"javascript:void(0);\" class=\"m_game\" style=\"background: url('"+Play.configuration.get("static.url")+"/public/images/aggames/"+agGame.get(i).Image_File_Name+"') no-repeat center ;background-size:cover; \"></a>"+
					       "<div class='m_text'><span class='g_name'>"+agGame.get(i).game_name+"</span><span class='user_name'>"+
					       getStringRandom(2)+"***"+(new Random().nextInt(100)+1)+"</span><span class='win'>赢了"+(new Random().nextInt(10000)+5000)+"元</span></div></li>";
				winninginfo.add(aginfo);
			
			
			//sb
			
				String sbinfo ="<li><a href=\"javascript:void(0);\" class=\"m_game\" style=\"background: url('"+Play.configuration.get("static.url")+"/public/images/sbgames/"+sbGame.get(i).image_game_name+"') no-repeat center ;background-size:cover; \"></a>"+
					       "<div class='m_text'><span class='g_name'>"+sbGame.get(i).game_name+"</span><span class='user_name'>"+
					       getStringRandom(2)+"***"+(new Random().nextInt(100)+1)+"</span><span class='win'>赢了"+(new Random().nextInt(10000)+5000)+"元</span></div></li>";
				winninginfo.add(sbinfo);
			
			
			//bbin
			
				String bbinfo ="<li><a href=\"javascript:void(0);\" class=\"m_game\" style=\"background: url('"+Play.configuration.get("static.url")+"/public/images/bbingames/"+bbinGame.get(i).Image_File_Name+"') no-repeat center ;background-size:cover; \"></a>"+
					       "<div class='m_text'><span class='g_name'>"+bbinGame.get(i).game_name+"</span><span class='user_name'>"+
					       getStringRandom(2)+"***"+(new Random().nextInt(100)+1)+"</span><span class='win'>赢了"+(new Random().nextInt(10000)+5000)+"元</span></div></li>";
				winninginfo.add(bbinfo);
			}else if(count==0){
				String ptinfo ="<li><a href=\"javascript:void(0);\" class=\"m_game\" style=\"background: url('"+Play.configuration.get("static.url")+"/public/images/Egame/ptgames/"+ptGame.get(i).game_code+".jpg') no-repeat center ;background-size:cover; \"></a>"+
					       "<div class='m_text'><span class='g_name'>"+ptGame.get(i).cn_name+"</span><span class='user_name'>"+
					       getStringRandom(2)+"***"+(new Random().nextInt(100)+1)+"</span><span class='win'>赢了"+(new Random().nextInt(5000)+1)+"元</span></div></li>";
				winninginfo.add(ptinfo);
			
			//mg
				String mginfo ="<li><a href=\"javascript:void(0);\" class=\"m_game\" style=\"background: url('"+Play.configuration.get("static.url")+"/public/images/mggames/"+mgGame.get(i).Image_File_Name+"') no-repeat center ;background-size:cover; \"></a>"+
					       "<div class='m_text'><span class='g_name'>"+mgGame.get(i).CHINESE_SIMP_Game_Name+"</span><span class='user_name'>"+
					       getStringRandom(2)+"***"+(new Random().nextInt(100)+1)+"</span><span class='win'>赢了"+(new Random().nextInt(5000)+1)+"元</span></div></li>";
				winninginfo.add(mginfo);

			//ag
				String aginfo ="<li><a href=\"javascript:void(0);\" class=\"m_game\" style=\"background: url('"+Play.configuration.get("static.url")+"/public/images/aggames/"+agGame.get(i).Image_File_Name+"') no-repeat center ;background-size:cover; \"></a>"+
					       "<div class='m_text'><span class='g_name'>"+agGame.get(i).game_name+"</span><span class='user_name'>"+
					       getStringRandom(2)+"***"+(new Random().nextInt(100)+1)+"</span><span class='win'>赢了"+(new Random().nextInt(5000)+1)+"元</span></div></li>";
				winninginfo.add(aginfo);
			
			
			//sb
			
				String sbinfo ="<li><a href=\"javascript:void(0);\" class=\"m_game\" style=\"background: url('"+Play.configuration.get("static.url")+"/public/images/sbgames/"+sbGame.get(i).image_game_name+"') no-repeat center ;background-size:cover; \"></a>"+
					       "<div class='m_text'><span class='g_name'>"+sbGame.get(i).game_name+"</span><span class='user_name'>"+
					       getStringRandom(2)+"***"+(new Random().nextInt(100)+1)+"</span><span class='win'>赢了"+(new Random().nextInt(5000)+1)+"元</span></div></li>";
				winninginfo.add(sbinfo);
			
			
			//bbin
			
				String bbinfo ="<li><a href=\"javascript:void(0);\" class=\"m_game\" style=\"background: url('"+Play.configuration.get("static.url")+"/public/images/bbingames/"+bbinGame.get(i).Image_File_Name+"') no-repeat center ;background-size:cover; \"></a>"+
					       "<div class='m_text'><span class='g_name'>"+bbinGame.get(i).game_name+"</span><span class='user_name'>"+
					       getStringRandom(2)+"***"+(new Random().nextInt(100)+1)+"</span><span class='win'>赢了"+(new Random().nextInt(5000)+1)+"元</span></div></li>";
				winninginfo.add(bbinfo);
			}
		
				
		}
		
		 Collections.shuffle(winninginfo);
		 return winninginfo;
	}
	 private static List<MyTag> splitList =new  ArrayList<MyTag>();
	 public String id;
	 public String count;
	 public Integer rate;
	 public MyTag(String id,String count,Integer rate){
		 this.id=id;
		 this.count=count;
		 this.rate=rate;
	 }
	static {
		splitList.add(new MyTag("1","300000", 1));//30W~100W
		splitList.add(new MyTag("2","200000", 3));//20W~30W
		splitList.add(new MyTag("3","100000", 5));//10W~20W
		splitList.add(new MyTag("4","80000", 10));//8W~10W
		splitList.add(new MyTag("5","50000", 15));//5W~8W
		splitList.add(new MyTag("6","10000", 35));//1W~5W
		splitList.add(new MyTag("7","5000", 20));//5000~1W
		splitList.add(new MyTag("8","0", 11));//0~5000
    }
	
	public static Integer getLottery(){
		MyTag mt = lottery();
		return Integer.valueOf(mt.count);
	}
	
	public static MyTag lottery(){
		
    	int i=new Random().nextInt(100)+1;
    	int p=0;
    	for(int j=0;j<splitList.size();j++){
    		p=p+splitList.get(j).rate;
    		if(i<=p){
    			return splitList.get(j);
    		}
    	}
    	
    	return null;
    }
	
	//生成随机数字和字母,  
    public static String getStringRandom(int length) {  
          
        String val = "";  
        Random random = new Random();  
          
        //参数length，表示生成几位随机数  
        for(int i = 0; i < length; i++) {  
              
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";  
            //输出字母还是数字  
            if( "char".equalsIgnoreCase(charOrNum) ) {  
                //输出是大写字母还是小写字母  
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;  
                val += (char)(random.nextInt(26) + temp);  
            } else if( "num".equalsIgnoreCase(charOrNum) ) {  
                val += String.valueOf(random.nextInt(10));  
            }  
        }  
        return val.toLowerCase();  
    }
    public static void main(String[] args){
    	//System.out.println(new Random().nextInt(100)+1);
    	//System.out.println("==="+getStringRandom(2));
    	
	Map<String,Integer> map =new HashMap<String,Integer>();
    	
    	for(int i=0;i<10000000;i++){
    		MyTag t=lottery();
    		if(map.containsKey(t.id)){
    			map.put(t.id, map.get(t.id)+1);
    		}else{
    			map.put(t.id, 1);
    			
    		}
    	}
    	
    	for (String key : map.keySet()) {
    		   System.out.println("key= "+ key + " and value= " + ((float)map.get(key))/100000 );
      }
    }
   
    
}