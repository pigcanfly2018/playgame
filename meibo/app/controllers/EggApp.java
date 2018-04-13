package controllers;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import models.CashGift;
import models.Customer;
import models.EggConfig;
import models.EggGift;
import models.EggTrophy;
import models.OrderNo;
import models.RankingList;
import models.RankingListRowMap;
import play.mvc.Controller;
import service.CreditLogService;
import service.CustomerService;
import util.GiftRandom;
import util.JSONResult;
import util.MyRandom;
import util.Sp;

public class EggApp extends Controller{
	
	
	public static void isLogin(){
		String login_name=session.get("username");
		Customer cust =Customer.getCustomer(login_name);
		if(cust==null){
			renderText(JSONResult.failure(""));
		}
		renderText(JSONResult.success(""));
	}
	public static void getUserScore(){
		String login_name=session.get("username");
		Customer cust =Customer.getCustomer(login_name);
		if(cust==null){
			renderText(JSONResult.success("0"));
		}
		renderText(JSONResult.success(cust.score.toPlainString()));
	}
	
	public static void eggInit(){
		renderText("{\"egg1\":"+EggConfig.egg1+",\"egg2\":"+EggConfig.egg2+",\"egg3\":"+EggConfig.egg3+"}");
	}
	
	
	public static void getLottery(String egg){
		if((!"金蛋".equals(egg))&&(!"银蛋".equals(egg))&&(!"白蛋".equals(egg))){
			renderText("{\"flag\":0,\"messag\":\"你当前砸蛋环境存在问题，请联系客服处理!\"}");
		}
		String login_name=session.get("username");
		BigDecimal score=new BigDecimal(EggConfig.egg1);
		int rate=EggConfig.rate1;
		if("金蛋".equals(egg)){
			score=new BigDecimal(EggConfig.egg3);
			rate=EggConfig.rate3;
		}
		if("银蛋".equals(egg)){
			score=new BigDecimal(EggConfig.egg2);
			rate=EggConfig.rate2;
		}
		Customer cust =Customer.getCustomer(login_name);
		if(cust==null){
			renderText("{\"flag\":0,\"messag\":\"您没有登录!\"}");
		}
		if(cust.score.intValue()<score.intValue()){
			renderText("{\"flag\":0,\"messag\":\"您好，您的积分不足，无法参与砸蛋!\"}");
		}
		EggTrophy et0=EggTrophy.getRandomDefault(egg);
		if(et0==null){
			renderText("{\"flag\":0,\"messag\":\"您好，奖品池没有奖品啦!\"}");
		}
		EggTrophy et1=EggTrophy.getRandom(egg);
		if(et1==null){
			renderText("{\"flag\":0,\"messag\":\"您好，奖品池没有奖品啦!\"}");
		}
		String gift_code=OrderNo.createLocalNo("EG");
		if(CustomerService.modScore(gift_code, "砸蛋("+egg+")", new BigDecimal(0).subtract(score), login_name, cust.cust_id, login_name)){
			EggTrophy et=et1;
			if(GiftRandom.isLuck(rate)){
				if(et.cost.intValue()>score.intValue()*5){
					et=EggTrophy.getRandom(egg);
				}
			}else{
				et=et0;
			}
			Long eggGiftId=EggGift.NTcreat(gift_code, cust.cust_id, login_name, cust.real_name, et.trophy_code,
					et.trophy_name, et.trophy_count, 1, 
					null, score, et.cost, et.trophy_type);
			if("点卡类".equals(et.trophy_type)){
				 String giftCode=MyRandom.getRandom(8);
				 CashGift gift =new CashGift();
	        	 gift.gift_code=giftCode;
	        	 gift.login_name=cust.login_name;
	        	 gift.deposit_credit=new BigDecimal(0);
	        	 gift.valid_credit=new BigDecimal(0);
	        	 gift.net_credit=new BigDecimal(0);
	        	 gift.rate=Float.valueOf(1);
	        	 gift.payout=et.cost;
	        	 gift.gift_type="抽奖点卡";
	        	 gift.status=1;
	        	 gift.gift_no=OrderNo.createLocalNo("GI");
	     		 gift.cust_id=cust.cust_id;
	        	 gift.cs_date=new Date(System.currentTimeMillis());
	        	 gift.real_name=cust.real_name;
	        	 gift.cust_level=cust.cust_level;
	           	 gift.kh_date=cust.create_date;
	        	 gift.create_user=cust.login_name;
	        	 gift.create_date=new Date(System.currentTimeMillis());
	        	 Long giftId=gift.NTcreat();
	        	 gift.NTAuditGift(giftId, 3, "系统",gift_code);
	        	 CustomerService.modCredit(cust.cust_id,CreditLogService.Gift, 
	 					gift.login_name,gift.payout, "系统", "添加礼金"+gift.gift_type, gift.gift_no);
	        	 EggGift.NTAuditGift(eggGiftId, 3, "系统", gift.gift_no);
			}
			if(et.cost.intValue()>=500){
				RankingList rank =new RankingList();
				rank.login_name=cust.login_name;
				rank.trophy_name=et.trophy_name;
				rank.trophy_code=et.trophy_code;
				rank.trophy_count=et.trophy_count;
				rank.cost=et.cost;
				rank.cust_id=cust.cust_id;
				rank.NTcreat();
			}
			
			renderText("{\"flag\":1,\"messag\":{\"code\":\""+gift_code+"\",\"name\":\""+et.trophy_name+"\",\"count\":"+et.trophy_count+"}}");
		}
		renderText("{\"flag\":0,\"messag\":\"您的积分不足，无法参与砸蛋!\"}");
	}
	
	
	
	
	

}
