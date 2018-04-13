package controllers;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import com.ws.service.PlatService;
import com.ws.service.SiteService;

import bsz.exch.service.Plat;
import models.Bank;
import models.BigDecimalRowMap;
import models.CashGift;
import models.Customer;
import models.Deposit;
import models.Hongbao;
import models.HuoliGift;
import models.Letter;
import models.MessageBoard;
import models.MessageBoardRowMap;
import models.OrderNo;
import models.Sign;
import models.SignReward;
import play.cache.Cache;
import play.mvc.Before;
import play.mvc.Controller;
import service.CreditLogService;
import service.CustomerService;
import service.MeiBoOpService;
import service.MeiBoService;
import util.DateUtil;
import util.JSONResult;
import util.MyRandom;
import util.PageUtil;
import util.Sp;

public class Ajax extends Controller{
	
	@Before
	 public static void before(){
		if(IPApp.isxss()){
			renderText("您的输入存在非法字符!");
		}
	}
	
	 public static void getMoreMsg(int page){
	    	String sql="select * from mb_message_board where reply_date is not null order by show_date desc";
	    	String sqlcnt="select count(1) from mb_message_board where reply_date is not null";
	    	int count=Sp.get().getDefaultJdbc().queryForObject(sqlcnt,new Object[]{},Integer.class);
	    	try{
		    	if(count<page*10){
		    		List<MessageBoard> messageList =new ArrayList<MessageBoard>();
		    		renderText(JSONResult.conver(messageList,true));
		    	}else{
		    		List<MessageBoard> messageList=Sp.get().getDefaultJdbc().query(PageUtil.page(sql,page*10,10),new Object[]{},new MessageBoardRowMap());
		    		renderJSON(JSONResult.conver(messageList,true));
		    	}
	    	}catch(Exception e){
	    		
	    	}
	    }
	 
	 public static void saveMsg(){
	    	MessageBoard mb=new MessageBoard();
	    	mb.cust_name=params.get("cust_name");
	    	mb.question=params.get("question");
	    	String  code=params.get("code");
	        String veriCode=(String)Cache.get("veriCode" + session.getId());
			if (veriCode==null||(!validation.equals(code.toLowerCase(),veriCode.toLowerCase()).ok)) {
				String errorMsg="验证码错误。";
				renderText(JSONResult.failure(errorMsg));
			}
			if(mb.cust_name==null||mb.cust_name.length()>30){
				String errorMsg="名字不可超出30字符!";
				renderText(JSONResult.failure(errorMsg));
			}
			if(mb.question==null||mb.question.length()>200){
				String errorMsg="名字不可超出200字符!";
				renderText(JSONResult.failure(errorMsg));
			}
			if(mb.question.length()<10){
				String errorMsg="留言最少字符为10字符!";
				renderText(JSONResult.failure(errorMsg));
			}
			if(mb.question.contains("'")){
				String errorMsg="问题里包含有特殊字符。请确认要全角符号！";
				renderText(JSONResult.failure(errorMsg));
			}
			mb.create_user=mb.cust_name;
			if(MeiBoOpService.createMsg(mb)){
				renderText(JSONResult.success(""));
			}
			renderText(JSONResult.failure("提交失败!"));
	    }
	 
	 public static void selfAppSave(){
		 String login_name=session.get("username");
		
	    	CashGift gift=new CashGift();
	    	/*sa.login_name=login_name;
	    	sa.gift_type=params.get("itemname");
	    	sa.status=1;
	    	sa.gift_no=OrderNo.createLocalNo("GI");
	    	sa.remarks=params.get("remark");
			if (sa.gift_type==null) {
				String errorMsg="优惠类型不为空。";
				renderText(JSONResult.failure(errorMsg));
			}*/
	    	if (params.get("itemname")==null) {
				String errorMsg="优惠类型不为空。";
				renderText(JSONResult.failure(errorMsg));
			}
			gift.gift_code=MyRandom.getRandom(8);//上传编号
			  gift.status=1;
			  gift.cs_date=new Date(System.currentTimeMillis());
			  gift.login_name=login_name;
			  gift.gift_type=params.get("itemname");
			  gift.create_user=login_name;
		      gift.create_date=new Date(System.currentTimeMillis());
		      gift.gift_no=OrderNo.createLocalNo("GI");
		      gift.remarks=params.get("remark");
		      BigDecimal deposit = new BigDecimal(0);
		      if(params.get("moneyflag")!=null && !"".equals(params.get("moneyflag"))){
		    	   deposit = new BigDecimal(params.get("moneyflag"));
		      }
		     
		      gift.deposit_credit=deposit;
		      Customer customer = MeiBoService.getCustomerByUserName(login_name);
		      gift.cust_id=customer.cust_id;
		      gift.real_name=customer.real_name;
		      gift.cust_level=customer.cust_level;
		      gift.kh_date=customer.create_date;
			//判断活动是否过期
			Boolean dics = SiteService.getdic(gift.gift_type);
			if(dics){
				String errorMsg="很遗憾，活动已结束";
				renderText(JSONResult.failure(errorMsg));
			}
			
			//判断是否有优惠活动在申请中
			if(CashGift.checkSelf(gift)){
				renderText(JSONResult.failure("您存在未审核的申请活动，请等待客服处理。"));
			}
			Long giftId=gift.NTcreat();
			if(giftId>0){
				
				renderText(JSONResult.success(""));
			}
			renderText(JSONResult.failure("提交失败!"));
	    }
	 
	 public static void checkLoginName(String username){
			if(MeiBoService.NTexist(username)){
				renderText(JSONResult.failure("用户名"+username+"已被注册"));
			}
			renderText(JSONResult.success(""));
	 }
	 
	 public static void wuyiGift(){
		 String login_name=session.get("username");
		 
		 Customer customer = MeiBoService.getCustomerByUserName(login_name);
			BigDecimal sum = Deposit.NTgetSumToday(customer.cust_id);
			Date now =new Date(System.currentTimeMillis());
			if((!customer.promo_flag||now.getTime()<DateUtil.stringToDate("2017-05-01", "yyyy-MM-dd").getTime()||now.getTime()>DateUtil.stringToDate("2017-05-08", "yyyy-MM-dd").getTime()) && !(login_name.equals("lance008")  )){
				
				renderText(JSONResult.failure("亲，活动尚未开始或已经结束！"));
			}
			int cishu = 0;
			if(sum == null || sum.intValue()< 1000){
				renderText(JSONResult.failure("亲，您今日存款不到1000元,请你再接再厉！"));
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
			if(cishu > 0){
				 yiling=Hongbao.NTgetCountToday(customer.cust_id);
				 cishu = cishu-yiling;
			}
			
			if(cishu <=0){
				renderText(JSONResult.failure("亲，抽奖次数已用光,请你明日再接再厉！"));
			}
			
			List<Hongbao> list = Hongbao.NTgetAll(customer.cust_id);
			BigDecimal wuyisum = Deposit.NTgetSumWuyi(customer.cust_id);
			if(wuyisum != null && wuyisum.intValue()>300000){
				Hongbao hongbao = Hongbao.NTgetSpecialOne(customer.cust_id, "a");
				if(hongbao == null){
					String result = "a";
					String prize = "0";
					hongbao =new Hongbao();
					hongbao.credit=new BigDecimal(0);
					hongbao.content="DW手表";
					hongbao.level =result;
					hongbao.cust_id=customer.cust_id;
					hongbao.login_name=customer.login_name;
					hongbao.status=0;
					Hongbao.createHongbao(hongbao);
					renderText(JSONResult.success(prize));
				}
				
			}
			
			if(wuyisum != null && wuyisum.intValue()>600000){
				Hongbao hongbao = Hongbao.NTgetSpecialOne(customer.cust_id, "c");
				if(hongbao == null){
					String result = "c";
					String prize = "2";
					hongbao =new Hongbao();
					hongbao.credit=new BigDecimal(0);
					hongbao.content="周大福金牌";
					hongbao.level =result;
					hongbao.cust_id=customer.cust_id;
					hongbao.login_name=customer.login_name;
					hongbao.status=0;
					Hongbao.createHongbao(hongbao);
					renderText(JSONResult.success(prize));
				}
				
			}
			
			if(yiling == 0){
				//第一次
				String result = getPrizeLocation(list,1,0);
				String prize = ""+stringtranslate(result);
					Hongbao hb =new Hongbao();
					 if(prize.equals("1")){
						 hb.credit=new BigDecimal(18);
						 hb.content="18元筹码";
					 }else if(prize.equals("3")){
						 hb.credit=new BigDecimal(58);
						 hb.content="58元筹码";
					 }
					 hb.level =result;
					 hb.cust_id=customer.cust_id;
					 hb.login_name=customer.login_name;
					 hb.status=0;
					 Long  hongbao_id=Hongbao.createHongbao(hb);
					 String giftCode=MyRandom.getRandom(8);
					 CashGift gift =new CashGift();
		        	 gift.gift_code=giftCode;
		        	 gift.login_name=customer.login_name;
		        	 gift.deposit_credit=sum;
		        	 gift.valid_credit=new BigDecimal(0);
		        	 gift.net_credit=new BigDecimal(0);
		        	 gift.rate=Float.valueOf(5);
		        	 if(prize.equals("1")){
		        		 gift.payout=new BigDecimal(18);
					 }else if(prize.equals("3")){
						 gift.payout=new BigDecimal(58);
					 }
		        	 
		        	 gift.gift_type="五一幸运大转轮";
		        	 gift.status=1;
		        	 gift.gift_no=OrderNo.createLocalNo("GI");
		     		 gift.cust_id=customer.cust_id;
		        	 gift.cs_date=new Date(System.currentTimeMillis());
		        	 gift.real_name=customer.real_name;
		        	 gift.cust_level=customer.cust_level;
		           	 gift.kh_date=customer.create_date;
		        	 gift.create_user="系统";
		        	 gift.create_date=new Date(System.currentTimeMillis());
		        	 Long giftId=gift.NTcreat();
		        	 gift.NTAuditGift(giftId, 3, "系统","系统审核通过-今日存款："+sum);
		        	 if(CustomerService.modCredit(customer.cust_id,CreditLogService.Gift, 
		 					gift.login_name,gift.payout,"系统", "系统审核通过-今日存款："+sum, gift.gift_no)){
		        		 Hongbao.finishHongbao(gift.gift_no, hongbao_id);
		        	 }
		        	 
				
				
				renderText(JSONResult.success(prize));
			}else if(yiling == 1){
				//第2次
				String result = getPrizeLocation(list,2,0);
				String prize = ""+stringtranslate(result);
				
					Hongbao hb =new Hongbao();
					 if(prize.equals("5")){
						 hb.credit=new BigDecimal(88);
						 hb.content="88元筹码";
					 }else if(prize.equals("3")){
						 hb.credit=new BigDecimal(58);
						 hb.content="58元筹码";
					 }else if(prize.equals("8")){
						 hb.credit=new BigDecimal(0);
						 hb.content="20积分";
					 }
					 hb.level =result;
					 hb.cust_id=customer.cust_id;
					 hb.login_name=customer.login_name;
					 hb.status=0;
					 Long  hongbao_id=Hongbao.createHongbao(hb);
					 if(!prize.equals("8")){
						 String giftCode=MyRandom.getRandom(8);
						 CashGift gift =new CashGift();
			        	 gift.gift_code=giftCode;
			        	 gift.login_name=customer.login_name;
			        	 gift.deposit_credit=sum;
			        	 gift.valid_credit=new BigDecimal(0);
			        	 gift.net_credit=new BigDecimal(0);
			        	 gift.rate=Float.valueOf(5);
			        	 if(prize.equals("5")){
			        		 gift.payout=new BigDecimal(88);
						 }else if(prize.equals("3")){
							 gift.payout=new BigDecimal(58);
						 }
			        	 
			        	 gift.gift_type="五一幸运大转轮";
			        	 gift.status=1;
			        	 gift.gift_no=OrderNo.createLocalNo("GI");
			     		 gift.cust_id=customer.cust_id;
			        	 gift.cs_date=new Date(System.currentTimeMillis());
			        	 gift.real_name=customer.real_name;
			        	 gift.cust_level=customer.cust_level;
			           	 gift.kh_date=customer.create_date;
			        	 gift.create_user="系统";
			        	 gift.create_date=new Date(System.currentTimeMillis());
			        	 Long giftId=gift.NTcreat();
			        	 gift.NTAuditGift(giftId, 3, "系统","系统审核通过-今日存款："+sum);
			        	 if(CustomerService.modCredit(customer.cust_id,CreditLogService.Gift, 
			 					gift.login_name,gift.payout,"系统", "系统审核通过-今日存款："+sum, gift.gift_no)){
			        		 Hongbao.finishHongbao(gift.gift_no, hongbao_id);
			        	 }
					 }else{
						 String uuid =OrderNo.createLocalNo("WY");
						 CustomerService.modScore(uuid, "五一转轮礼包", new BigDecimal(20), login_name, customer.cust_id, customer.login_name);
						 Hongbao.finishHongbao("", hongbao_id);
					 }
					 
		        	 
				
				
				renderText(JSONResult.success(prize));
			}
			
			else if(yiling == 2){
				//第3次
				String result = getPrizeLocation(list,3,0);
				String prize = ""+stringtranslate(result);
				
					Hongbao hb =new Hongbao();
					 if(prize.equals("7")){
						 hb.credit=new BigDecimal(188);
						 hb.content="188元筹码";
					 }else if(prize.equals("6")){
						 hb.credit=new BigDecimal(0);
						 hb.content="三只松鼠大礼包";
					 }
					 hb.level =result;
					 hb.cust_id=customer.cust_id;
					 hb.login_name=customer.login_name;
					 hb.status=0;
					 Long  hongbao_id=Hongbao.createHongbao(hb);
					 if(prize.equals("7")){
						 String giftCode=MyRandom.getRandom(8);
						 CashGift gift =new CashGift();
			        	 gift.gift_code=giftCode;
			        	 gift.login_name=customer.login_name;
			        	 gift.deposit_credit=sum;
			        	 gift.valid_credit=new BigDecimal(0);
			        	 gift.net_credit=new BigDecimal(0);
			        	 gift.rate=Float.valueOf(5);
			        	 gift.payout=new BigDecimal(188);
			        	 
			        	 gift.gift_type="五一幸运大转轮";
			        	 gift.status=1;
			        	 gift.gift_no=OrderNo.createLocalNo("GI");
			     		 gift.cust_id=customer.cust_id;
			        	 gift.cs_date=new Date(System.currentTimeMillis());
			        	 gift.real_name=customer.real_name;
			        	 gift.cust_level=customer.cust_level;
			           	 gift.kh_date=customer.create_date;
			        	 gift.create_user="系统";
			        	 gift.create_date=new Date(System.currentTimeMillis());
			        	 Long giftId=gift.NTcreat();
			        	 gift.NTAuditGift(giftId, 3, "系统","系统审核通过-今日存款："+sum);
			        	 if(CustomerService.modCredit(customer.cust_id,CreditLogService.Gift, 
			 					gift.login_name,gift.payout,"系统", "系统审核通过-今日存款："+sum, gift.gift_no)){
			        		 Hongbao.finishHongbao(gift.gift_no, hongbao_id);
			        	 }
					 }
					 
		        	 
				
				
				renderText(JSONResult.success(prize));
			}else if(yiling == 3){
				//第4次
				String result = getPrizeLocation(list,4,0);
				String prize = ""+stringtranslate(result);
				
					Hongbao hb =new Hongbao();
					 if(prize.equals("1")){
						 hb.credit=new BigDecimal(18);
						 hb.content="18元筹码";
					 }else if(prize.equals("10")){
						 hb.credit=new BigDecimal(0);
						 hb.content="存送5%";
					 }else if(prize.equals("8")){
						 hb.credit=new BigDecimal(0);
						 hb.content="20积分";
					 }
					 hb.level =result;
					 hb.cust_id=customer.cust_id;
					 hb.login_name=customer.login_name;
					 hb.status=0;
					 Long  hongbao_id=Hongbao.createHongbao(hb);
					 if(prize.equals("1")){
						 String giftCode=MyRandom.getRandom(8);
						 CashGift gift =new CashGift();
			        	 gift.gift_code=giftCode;
			        	 gift.login_name=customer.login_name;
			        	 gift.deposit_credit=sum;
			        	 gift.valid_credit=new BigDecimal(0);
			        	 gift.net_credit=new BigDecimal(0);
			        	 gift.rate=Float.valueOf(5);
			        	 gift.payout=new BigDecimal(18);
			        	 
			        	 gift.gift_type="五一幸运大转轮";
			        	 gift.status=1;
			        	 gift.gift_no=OrderNo.createLocalNo("GI");
			     		 gift.cust_id=customer.cust_id;
			        	 gift.cs_date=new Date(System.currentTimeMillis());
			        	 gift.real_name=customer.real_name;
			        	 gift.cust_level=customer.cust_level;
			           	 gift.kh_date=customer.create_date;
			        	 gift.create_user="系统";
			        	 gift.create_date=new Date(System.currentTimeMillis());
			        	 Long giftId=gift.NTcreat();
			        	 gift.NTAuditGift(giftId, 3, "系统","系统审核通过-今日存款："+sum);
			        	 if(CustomerService.modCredit(customer.cust_id,CreditLogService.Gift, 
			 					gift.login_name,gift.payout,"系统", "系统审核通过-今日存款："+sum, gift.gift_no)){
			        		 Hongbao.finishHongbao(gift.gift_no, hongbao_id);
			        	 }
					 }else if(prize.equals("8")){
						 String uuid =OrderNo.createLocalNo("WY");
						 CustomerService.modScore(uuid, "五一转轮礼包", new BigDecimal(20), login_name, customer.cust_id, customer.login_name);
						 Hongbao.finishHongbao("", hongbao_id);
					 }
				renderText(JSONResult.success(prize));
			}else if(yiling == 4){
				//第5次
				String result = getPrizeLocation(list,5,0);
				String prize = ""+stringtranslate(result);
				
					Hongbao hb =new Hongbao();
					 if(prize.equals("9")){
						 hb.credit=new BigDecimal(288);
						 hb.content="288元筹码";
					 }else if(prize.equals("11")){
						 hb.credit=new BigDecimal(388);
						 hb.content="388元筹码";
					 }else if(prize.equals("6")){
						 hb.credit=new BigDecimal(0);
						 hb.content="三只松鼠大礼包";
					 }else if(prize.equals("4")){
						 hb.credit=new BigDecimal(0);
						 hb.content="0.15洗码";
					 }
					 hb.level =result;
					 hb.cust_id=customer.cust_id;
					 hb.login_name=customer.login_name;
					 hb.status=0;
					 Long  hongbao_id=Hongbao.createHongbao(hb);
					 if(prize.equals("9") || prize.equals("11")){
						 String giftCode=MyRandom.getRandom(8);
						 CashGift gift =new CashGift();
			        	 gift.gift_code=giftCode;
			        	 gift.login_name=customer.login_name;
			        	 gift.deposit_credit=sum;
			        	 gift.valid_credit=new BigDecimal(0);
			        	 gift.net_credit=new BigDecimal(0);
			        	 gift.rate=Float.valueOf(5);
			        	 if(prize.equals("9")){
			        		 gift.payout=new BigDecimal(288);
						 }else if(prize.equals("11")){
							 gift.payout=new BigDecimal(388);
						 }
			        	 //gift.payout=new BigDecimal(188);
			        	 
			        	 gift.gift_type="五一幸运大转轮";
			        	 gift.status=1;
			        	 gift.gift_no=OrderNo.createLocalNo("GI");
			     		 gift.cust_id=customer.cust_id;
			        	 gift.cs_date=new Date(System.currentTimeMillis());
			        	 gift.real_name=customer.real_name;
			        	 gift.cust_level=customer.cust_level;
			           	 gift.kh_date=customer.create_date;
			        	 gift.create_user="系统";
			        	 gift.create_date=new Date(System.currentTimeMillis());
			        	 Long giftId=gift.NTcreat();
			        	 gift.NTAuditGift(giftId, 3, "系统","系统审核通过-今日存款："+sum);
			        	 if(CustomerService.modCredit(customer.cust_id,CreditLogService.Gift, 
			 					gift.login_name,gift.payout,"系统", "系统审核通过-今日存款："+sum, gift.gift_no)){
			        		 Hongbao.finishHongbao(gift.gift_no, hongbao_id);
			        	 }
					 }
				renderText(JSONResult.success(prize));
			}else if(yiling == 5){
				//第6次
				String result = getPrizeLocation(list,6,0);
				String prize = ""+stringtranslate(result);
				
					Hongbao hb =new Hongbao();
					 if(prize.equals("9")){
						 hb.credit=new BigDecimal(288);
						 hb.content="288元筹码";
					 }else if(prize.equals("7")){
						 hb.credit=new BigDecimal(188);
						 hb.content="188元筹码";
					 }else if(prize.equals("1")){
						 hb.credit=new BigDecimal(18);
						 hb.content="18元筹码";
					 }else if(prize.equals("8")){
						 hb.credit=new BigDecimal(0);
						 hb.content="20积分";
					 }else if(prize.equals("10")){
						 hb.credit=new BigDecimal(0);
						 hb.content="存送5%";
					 }
					 hb.level =result;
					 hb.cust_id=customer.cust_id;
					 hb.login_name=customer.login_name;
					 hb.status=0;
					 Long  hongbao_id=Hongbao.createHongbao(hb);
					 if(prize.equals("9") || prize.equals("7") || prize.equals("1")){
						 String giftCode=MyRandom.getRandom(8);
						 CashGift gift =new CashGift();
			        	 gift.gift_code=giftCode;
			        	 gift.login_name=customer.login_name;
			        	 gift.deposit_credit=sum;
			        	 gift.valid_credit=new BigDecimal(0);
			        	 gift.net_credit=new BigDecimal(0);
			        	 gift.rate=Float.valueOf(5);
			        	 if(prize.equals("9")){
			        		 gift.payout=new BigDecimal(288);
						 }else if(prize.equals("7")){
							 gift.payout=new BigDecimal(188);
						 }else if(prize.equals("1")){
							 gift.payout=new BigDecimal(18);
						 }
			        	 //gift.payout=new BigDecimal(188);
			        	 
			        	 gift.gift_type="五一幸运大转轮";
			        	 gift.status=1;
			        	 gift.gift_no=OrderNo.createLocalNo("GI");
			     		 gift.cust_id=customer.cust_id;
			        	 gift.cs_date=new Date(System.currentTimeMillis());
			        	 gift.real_name=customer.real_name;
			        	 gift.cust_level=customer.cust_level;
			           	 gift.kh_date=customer.create_date;
			        	 gift.create_user="系统";
			        	 gift.create_date=new Date(System.currentTimeMillis());
			        	 Long giftId=gift.NTcreat();
			        	 gift.NTAuditGift(giftId, 3, "系统","系统审核通过-今日存款："+sum);
			        	 if(CustomerService.modCredit(customer.cust_id,CreditLogService.Gift, 
			 					gift.login_name,gift.payout,"系统", "系统审核通过-今日存款："+sum, gift.gift_no)){
			        		 Hongbao.finishHongbao(gift.gift_no, hongbao_id);
			        	 }
					 }else if(prize.equals("8")){
						 String uuid =OrderNo.createLocalNo("WY");
						 CustomerService.modScore(uuid, "五一转轮礼包", new BigDecimal(20), login_name, customer.cust_id, customer.login_name);
						 Hongbao.finishHongbao("", hongbao_id);
					 }
				renderText(JSONResult.success(prize));
			}
			
			
			
			
			renderText(JSONResult.success("1"));
		 
	 }
	 
	 public static String getPrizeLocation(List<Hongbao> alllist,int level,int yiling){
		 Random ra =new Random();
		 
		 if(level == 1){
			 String full = "bbbbbdd";
			 for(Hongbao gift : alllist){
				full = full.replaceFirst(gift.level, "");
			 }
			 return String.valueOf(full.charAt(ra.nextInt(full.length())));
		 }else if(level == 2){
			 String full = "mmmiiff";
			 for(Hongbao gift : alllist){
				full = full.replaceFirst(gift.level, "");
			 }
			 return String.valueOf(full.charAt(ra.nextInt(full.length())));
		 }else if(level == 3){
			 String full = "hhhhhhg";
			 for(Hongbao gift : alllist){
				full = full.replaceFirst(gift.level, "");
			 }
			 return String.valueOf(full.charAt(ra.nextInt(full.length())));
		 }else if(level == 4){
			 String full = "nnnppkk";
			 for(Hongbao gift : alllist){
				full = full.replaceFirst(gift.level, "");
			 }
			 return String.valueOf(full.charAt(ra.nextInt(full.length())));
		 }else if(level == 5){
			 String full = "jjlltej";
			 for(Hongbao gift : alllist){
				full = full.replaceFirst(gift.level, "");
			 }
			 return String.valueOf(full.charAt(ra.nextInt(full.length())));
		 }else if(level == 6){
			 String full = "qqrsuvs";
			 for(Hongbao gift : alllist){
				full = full.replaceFirst(gift.level, "");
			 }
			 return String.valueOf(full.charAt(ra.nextInt(full.length())));
		 }
			 
		 
		 
		 
		 return "b"; 
		 
	 }
	 
	 public static int stringtranslate(String str){
		 System.out.println(str);
		 if(str.equals("b") || str.equals("n") || str.equals("r")){
			 return 1;
		 }else if(str.equals("d") || str.equals("m")){
			 return 3;
		 }else if(str.equals("f")){
			 return 5;
		 }else if(str.equals("i") || str.equals("p") || str.equals("s")){
			 return 8;
		 }else if(str.equals("h") || str.equals("u")){
			 return 7;
		 }else if(str.equals("g") || str.equals("t")){
			 return 6;
		 }else if(str.equals("k") || str.equals("q")){
			 return 10;
		 }else if(str.equals("j") || str.equals("v")){
			 return 9;
		 }else if(str.equals("l")){
			 return 11;
		 }else if(str.equals("e")){
			 return 4;
		 }
		 
		 return 1;
	 }
	 
	 public static void checkPhone(String phone){
	    	if(MeiBoService.NTexistPhone(phone)){
				renderText(JSONResult.failure("手机号码"+phone+"已被注册"));
			}
			renderText(JSONResult.success(""));
		}
	 
	 
	 public static void login(){
	    	String login_name=params.get("loginName");
			String password=params.get("pwd");
			String code=params.get("code");
			if(login_name==null||"".equals(login_name)){
				renderText(JSONResult.failure("登录失败，请填写您的用户名!"));
			}
			if(password==null||"".equals(password)){
				renderText(JSONResult.failure("登录失败，请填写您的登录密码!"));
			}
			if(code==null||"".equals(code)){
				renderText(JSONResult.failure("登录失败，请填写校验码!"));
			}
			String veriCode=(String)Cache.get("veriCode" + session.getId());
			if (veriCode==null||(!validation.equals(code.toLowerCase(),veriCode.toLowerCase()).ok)) {
				renderText(JSONResult.failure("登录失败，校验码错误!"));
			}
			String ip=IPApp.getIpAddr();
			Customer customer=CustomerService.login(login_name, password,ip);
			if(customer==null){
				renderText(JSONResult.failure("登录失败，用户名和密码错误 !"));
			}
			if(!customer.flag){
				renderText(JSONResult.failure("登录失败，账户已经被禁用 !"));
			}
			Cache.set("user"+ session.getId(), customer);
			session.put("username", customer.login_name);
			Long userId=customer.cust_id;
			String name=customer.login_name+customer.real_name; 
			String memo="";
			try {
				String infoValue=URLEncoder.encode(URLEncoder.encode("userId="+userId+"&name="+name+"&memo="+memo, "UTF-8"));
				session.put("infoValue", infoValue);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String user_agent=request.headers.get("user-agent")==null?"":request.headers.get("user-agent").toString();
			MeiBoOpService.logCust(customer.cust_id, customer.login_name, 2, ip, user_agent, "客户登录。");
			renderText(JSONResult.success(""));
		}
	 
	 /*
	 public static void getTotalCredit(){
			String login_name=session.get("username");
			Customer cust=Customer.getCustomer(login_name);
			if(cust==null)  renderText(JSONResult.success(new BigDecimal(0).toString()));
			if(cust.credit==null)cust.credit=new BigDecimal(0);
			String ip=IPApp.getIpAddr();
			BigDecimal aginCredit=GameService.getAginBalance(cust,ip);
			BigDecimal bbinCredit=GameService1.getBbinBalance(cust,ip);
			BigDecimal ptCredit=GameService1.getPtBalance(cust,ip);
			BigDecimal totalCredit=cust.credit.add(aginCredit).add(bbinCredit).add(ptCredit);
			renderText(JSONResult.success(totalCredit.toString()));
		 
	 }*/
	 public static void getBank(String bank_name){
			String login_name=session.get("username");
			Customer cust=Customer.getCustomer(login_name);
			if(cust==null)  renderText(JSONResult.failure("不存在匹配相同的银行。"));
			Bank bank=MeiBoService.getBank(cust.cust_level, bank_name);
			if(bank==null){
				renderText(JSONResult.failure("不存在匹配相同的银行!"));
			}else{
				renderText("{success:true,message:{account_name:'"+bank.account_name
						+"',account:'"+bank.account+"',location:'"+bank.bank_dot+"'}}");
			}
		}
	 
	 public static void getLetter(Long id){
		 String login_name=session.get("username");
		 Customer cust=Customer.getCustomer(login_name);
		 Letter letter=Letter.NTgetLetter(id, cust.cust_id);
		 if(letter!=null){
			 if(!letter.read_flag){
				 Letter.NTreadLetter(letter.letter_id);
			 }
		 }
		renderJSON(letter);
	 }
	 
	 public static void getNoReadCnt(){
		 String login_name=session.get("username");
		 Customer cust=Customer.getCustomer(login_name);
		 renderText(JSONResult.success(Letter.NTgetLetterCnt(cust.cust_id)+""));
	 }
	 
	 public static void getApplyZhongqiu(Integer level){
		 String login_name=session.get("username");
		 Customer cust=Customer.getCustomer(login_name);
		 
		 
//			//获取用户当天存款总数
			String sql ="select sum(amount) from mb_deposit where login_name=? and status=3 and create_date >= ? and create_date < ? ";
			Object[] objs = new Object[]{login_name,DateUtil.getTodayStarttime(),DateUtil.getTodayEndtime()};
			BigDecimal deposit = (BigDecimal)Sp.get().getDefaultJdbc().queryForObject(sql,objs,new BigDecimalRowMap());
			int deposittotalvalue;
			if(deposit == null){
				deposittotalvalue= 0;
			}else{
				deposittotalvalue = deposit.intValue();
			}
			
			if(deposittotalvalue < 1000){
				
			}else{
				int count = HuoliGift.getLevelCountByToday(login_name, level,DateUtil.getTodayStarttime(),DateUtil.getTodayEndtime());
				String uuid =OrderNo.createLocalNo("ZQ");
				String content = "";
				BigDecimal c = new BigDecimal(0);
				if(level == 0){
					
				}else if(level == 1 && deposittotalvalue>=1000){
					content="客户领取开门大吉红包";
					c= new BigDecimal(18);
					//score = new BigDecimal(10);;
				}else if(level == 2 && deposittotalvalue>=5000 ){
					content="客户领取金口大开红包";
					c= new BigDecimal(58);
					//score = new BigDecimal(20);;
				}else if(level == 3 && deposittotalvalue>=10000 ){
					content="客户领取花开富贵红包";
					c= new BigDecimal(88);
					//score = new BigDecimal(30);;
				}else if(level == 4 && deposittotalvalue>=50000 ){
					content="客户领取可爱月兔红包";
					c= new BigDecimal(288);
					//score = new BigDecimal(58);;
				}else if(level == 5 && deposittotalvalue>=100000 ){
					content="客户领取嫦娥奔月红包";
					c= new BigDecimal(688);
					//score = new BigDecimal(160);;
				}
				if(count==0){
					Long gift_id= HuoliGift.create(cust.cust_id, login_name, content, uuid, 0, level);
					boolean b=CustomerService.modCredit(cust.cust_id,CreditLogService.Gift,cust.login_name, c,login_name,"中秋活动礼包:"+uuid, uuid);
					
					int flag=2;
					if(b)flag=1;
					HuoliGift.finishedGift(gift_id, flag);
					
				}
			}
		 
		 //renderText(JSONResult.success(Letter.NTgetLetterCnt(cust.cust_id)+""));
	 }
	 
	 public static void getU(){
		 String login_name=session.get("username");
		 Customer cust=Customer.getCustomer(login_name);
		 if(cust!=null){
		    renderText(JSONResult.success(cust.pt_game+"|"+cust.pt_pwd));
		 }else{
			 renderText(JSONResult.failure(""));
		 }
	 }
	 
	 public static void getPlatCredit(String plat){
			String login_name=session.get("username");
			Customer cust=Customer.getCustomer(login_name);
			String ip=IPApp.getIpAddr();
			if(cust==null || cust.credit==null)  renderText(JSONResult.success(new BigDecimal(0).toString()));
			if("02".equals(plat)){
				BigDecimal balance=PlatService.balance(Plat.AG, cust.login_name, ip);
				renderText(JSONResult.success(balance.toString()));
			}
			if("03".equals(plat)){
				BigDecimal balance=PlatService.balance(Plat.BBIN, cust.login_name, ip);
				renderText(JSONResult.success(balance.toString()));
			}
			if("04".equals(plat)){
				BigDecimal balance=PlatService.balance(Plat.PT, cust.login_name, ip);
				renderText(JSONResult.success(balance.toString()));
			}
			if("05".equals(plat)){
				BigDecimal balance=PlatService.balance(Plat.KG, cust.login_name, ip);
				renderText(JSONResult.success(balance.toString()));
			}
			if("06".equals(plat)){
				BigDecimal balance=PlatService.balance(Plat.MG, cust.login_name, ip);
				renderText(JSONResult.success(balance.toString()));
			}
			if("07".equals(plat)){
				BigDecimal balance=PlatService.balance(Plat.VS, cust.login_name, ip);
				renderText(JSONResult.success(balance.toString()));
			}if("08".equals(plat)){
				BigDecimal balance=PlatService.balance(Plat.PP, cust.login_name, ip);
				renderText(JSONResult.success(balance.toString()));
			}
			renderText(JSONResult.success(new BigDecimal(0).toString()));
	 }
	 
	 public static void transferInCredit(String plat){
		 
		 String login_name=session.get("username");
		 Customer cust=Customer.getCustomer(login_name);
		 String ip=IPApp.getIpAddr();
		 if(cust==null || cust.credit==null || cust.credit.intValue()<1) 
			 renderText(JSONResult.failure("您的额度不足1元，请充值后再使用此功能,谢谢"));
		 Plat p =null;
		 String plat_name="";
		 if("02".equals(plat)){
			 plat_name="AG厅";
			 p=Plat.AG;
		 }
		 if("03".equals(plat)){
				plat_name="波音厅";
				p=Plat.BBIN;
		 }
		if("04".equals(plat)){
				plat_name="PT厅";
				p=Plat.PT;
		}
		if("05".equals(plat)){
				plat_name="KG厅";
				p=Plat.KG;
		}
		if("06".equals(plat)){
				plat_name="MG厅";
				p=Plat.MG;
		}
		if("07".equals(plat)){
				plat_name="申博厅";
				p=Plat.VS;
		}if("08".equals(plat)){
			plat_name="PP厅";
			p=Plat.PP;
		}	
		
		String order_no =""+System.currentTimeMillis();
		BigDecimal credit=new BigDecimal(cust.credit.intValue()).setScale(2,BigDecimal.ROUND_HALF_EVEN);
		boolean f=PlatService.transferIn(p, login_name, ip, "会员转账","会员转账", login_name, order_no, credit);
		if(f){
			String successMsg="您的转账转向"+plat_name+"申请已处理成功！转账额度为:"+credit.toString();
			renderText(JSONResult.success(successMsg));
		}else{
			renderText(JSONResult.failure("系统错误，联系在线客服为您处理。"));
		}
	 }
	 
	 public static void callPhone(String callphone){
			
			
			if(callphone == null || callphone.equals("") || callphone.length()<10){
				//BigDecimal balance=PlatService.balance(Plat.MG, cust.login_name, ip);
				//renderText(JSONResult.success(balance.toString()));
				renderText(JSONResult.success("请输入正确的电话号码"));
			}else{
				boolean flag = PlatService.call(callphone);
				if(flag){
					
					String login_name=session.get("username");
					if(login_name == null)
						login_name="";
					Customer cust = Customer.getCustomerByPhone(callphone);
					if(cust == null){
						MeiBoService.creatphonerecord(login_name,callphone);
					}else{
						MeiBoService.creatphonerecord(cust.login_name,callphone);
					}
					
					renderText(JSONResult.success("请稍等,正在为您拨打电话"));
				}else{
					renderText(JSONResult.success("电话繁忙,请你稍后再试"));
				}
				
			}
			
	 }
	 
	 
	 public static void getSignReward(){
		 
		 String login_name=session.get("username");
		 
		 Customer customer = MeiBoService.getCustomerByUserName(login_name);
		 Date now =new Date(System.currentTimeMillis());
			
		 if((!customer.promo_flag||now.getTime()<DateUtil.stringToDate("2017-06-01", "yyyy-MM-dd").getTime()||now.getTime()>DateUtil.stringToDate("2017-07-01", "yyyy-MM-dd").getTime()) && !(login_name.equals("lance008")  )){
				
				renderText(JSONResult.failure("亲，活动尚未开始或已经结束！"));
		 }
		 
		 List<Sign> list = Sign.getAll(login_name);
			List signdate = new ArrayList();
			//System.out.println(list.get(0).sign_date);
			for(int i=0;i<list.size();i++){
				signdate.add(list.get(i).sign_date);
			}
			
			Calendar cal =  Calendar.getInstance();
			cal.set(2017, 04, 31,0,0,0);
			String record =  "";
			long count = 0;//从最近一次领取奖励的那天算起  连续存款总天数
			long totalcount = 0;//连续存款的总天数
			//获取最后一次领奖时间
			SignReward reward = SignReward.getLatSignReward(login_name);
			
			for(int i=0;i<30;i++){
				cal.add(Calendar.DATE, 1);
				String nday = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
				if(signdate.contains(nday)){
					record  = record+"_"+(i+1);
					count = count +1;
					
				}else{
					record  = record+"_";
					if(now.getTime()>cal.getTime().getTime() ){
						count = 0;
					}
				}
			}
			
			totalcount  = count ;
			if(count>4 && reward != null){
				String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
				
				long l;
				try {
					l = new SimpleDateFormat("yyyy-MM-dd").parse(today).getTime()-(new SimpleDateFormat("yyyy-MM-dd").parse(reward.reward_date).getTime());
					long day=l/(24*60*60*1000);
					count = day;
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
			if(count>4){
				
				Calendar nowcal =  Calendar.getInstance();
				
				Calendar startcal =  Calendar.getInstance();
				startcal.set(Calendar.HOUR, 0);
				startcal.set(Calendar.MINUTE, 0);
				startcal.set(Calendar.SECOND, 0);
				startcal.add(Calendar.DATE, -(int)totalcount);
				
				BigDecimal bonus  = SignReward.NTgetSum(login_name, startcal.getTime());
				if(bonus ==null){
					bonus = new BigDecimal(0);
				}
				
				//SignReward signReward =new SignReward();
				
				CashGift gift =new CashGift();
				 if(totalcount>=5 && totalcount< 10){
					 nowcal.add(Calendar.DATE, 5-(int)count);
					 count = 5;
					 //nowcal.gett
					 SignReward.reward(login_name, customer.cust_id, customer.cust_level, new BigDecimal(28),nowcal.getTime());
					 gift.payout=new BigDecimal(28);
				 }else if(totalcount>=10 && totalcount< 15){
					 if(count<10){
						 nowcal.add(Calendar.DATE, 5-(int)count);
					 }else{
						 nowcal.add(Calendar.DATE, 10-(int)count);
					 }
					 
					 count = 10;
					 SignReward.reward(login_name, customer.cust_id, customer.cust_level, new BigDecimal(76-bonus.intValue()),nowcal.getTime());
					 gift.payout=new BigDecimal(76-bonus.intValue());
				 }else if(totalcount>=15 && totalcount< 20){
					 if(count<10){
						 nowcal.add(Calendar.DATE, 5-(int)count);
					 }else if(count >10 && count<15){
						 nowcal.add(Calendar.DATE, 10-(int)count);
					 }else{
						 nowcal.add(Calendar.DATE, 15-(int)count);
					 }
					 
					 count = 15;
					 SignReward.reward(login_name, customer.cust_id, customer.cust_level, new BigDecimal(144-bonus.intValue()),nowcal.getTime());
					 gift.payout=new BigDecimal(144-bonus.intValue());
				 }else if(totalcount>=20 && totalcount< 25){
					 
					 if(count<10){
						 nowcal.add(Calendar.DATE, 5-(int)count);
					 }else if(count >10 && count<15){
						 nowcal.add(Calendar.DATE, 10-(int)count);
					 }else if(count >15 && count<20){
						 nowcal.add(Calendar.DATE, 15-(int)count);
					 }else{
						 nowcal.add(Calendar.DATE, 20-(int)count);
					 }
					 
					 
					 count = 20;
					 SignReward.reward(login_name, customer.cust_id, customer.cust_level, new BigDecimal(232-bonus.intValue()),nowcal.getTime());
					 gift.payout=new BigDecimal(232-bonus.intValue());
				 }else if(totalcount>=25 && totalcount< 30){
					 
					 if(count<10){
						 nowcal.add(Calendar.DATE, 5-(int)count);
					 }else if(count >10 && count<15){
						 nowcal.add(Calendar.DATE, 10-(int)count);
					 }else if(count >15 && count<20){
						 nowcal.add(Calendar.DATE, 15-(int)count);
					 }else if(count >20 && count<25){
						 nowcal.add(Calendar.DATE, 20-(int)count);
					 }else{
						 
						 nowcal.add(Calendar.DATE, 25-(int)count);
					 }
					 
					 
					 count = 25;
					 SignReward.reward(login_name, customer.cust_id, customer.cust_level, new BigDecimal(350-bonus.intValue()),nowcal.getTime());
					 gift.payout=new BigDecimal(350-bonus.intValue());
				 }else if(totalcount==30 ){
					 count = 30;
					 SignReward.reward(login_name, customer.cust_id, customer.cust_level, new BigDecimal(518-bonus.intValue()),nowcal.getTime());
					 gift.payout=new BigDecimal(518-bonus.intValue());
				 }
				 
				
					 String giftCode=MyRandom.getRandom(8);
					 
		        	 gift.gift_code=giftCode;
		        	 gift.login_name=customer.login_name;
		        	 gift.deposit_credit=new BigDecimal(0);
		        	 gift.valid_credit=new BigDecimal(0);
		        	 gift.net_credit=new BigDecimal(0);
		        	 gift.rate=Float.valueOf(1);
		        	 
		        	 
		        	 gift.gift_type="六月签到礼金";
		        	 gift.status=1;
		        	 gift.gift_no=OrderNo.createLocalNo("GI");
		     		 gift.cust_id=customer.cust_id;
		        	 gift.cs_date=new Date(System.currentTimeMillis());
		        	 gift.real_name=customer.real_name;
		        	 gift.cust_level=customer.cust_level;
		           	 gift.kh_date=customer.create_date;
		        	 gift.create_user="系统";
		        	 gift.create_date=new Date(System.currentTimeMillis());
		        	 Long giftId=gift.NTcreat();
		        	 gift.NTAuditGift(giftId, 3, "系统","系统审核通过-签到天数："+count);
		        	 if(CustomerService.modCredit(customer.cust_id,CreditLogService.Gift, 
		 					gift.login_name,gift.payout,"系统", "系统审核通过-签到天数："+count, gift.gift_no)){
		        		 
		        	 }
		        	 
			
		 
		        	 renderText(JSONResult.success("恭喜您成功领取签到天数"+count+"天的签到奖励,请再接再厉"));
			}
			
			renderText(JSONResult.success("您尚未达成领取条件,请你再接再厉"));
	 } 
}
