package controllers;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import jobs.Years1Job;
import net.sf.oval.internal.Log;
import models.Customer;
import models.YearGift;
import play.mvc.Controller;
import service.CreditLogService;
import service.CustomerService;
import util.DateUtil;
import util.GiftRandom;
import util.JSONResult;

import com.yeargift.YearGiftCache;
import com.yeargift.YearGiftCustBean;

public class YearGiftApp extends Controller {
	
	static Log log =Log.getLog(Years1Job.class);	
	
	//任务会重置该值 不在(08:18-08:33 或者 20:18-20:33) 则为0
	public static int stop =1;
	
	public static double[] cc= new double[]{0.88,1.28,1.88,2.18,2.88,3.18,3.88,5.88,5.18,6.18,6.28,6.58,6.38,6.88,8.18,8.28,8.38,8.88,9.18,9.88,10.18,10.88,11.18,11.88,15.18,15.88,18.18,18.88};
	
	static int romRand(int length){
		return (int)(Math.random()*length);
	}
	
	private static boolean activeTime(){
		Date now =new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(now);
		int month=c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
		int hours=c.get(Calendar.HOUR_OF_DAY);
		int minutes=c.get(Calendar.MINUTE);
		if(month!=7)return false;
		if(!(hours==8||hours==20))return false;
		if(day<10||day>28)return false;
		if(minutes<18||minutes>33)return false;
		return true;	
	}
	
	public static void index(){
		String login_name=session.get("username");
		YearGiftCustBean  bean=YearGiftCache.get().getCustBean(login_name);
		if(bean==null){
			 bean =new YearGiftCustBean();
			 
		}
		long serverTime=System.currentTimeMillis();
		int stop1=YearGiftApp.stop;
		float sum_credit =0.0F;
		float all_credit =0.0F;
		int all_count =0;
		float current_day_credit=0.0F;
		int current_day_count =0;
		
		//获取随机加的数字
		int a0=bean.all_count%10+1+bean.all_count%5;
		int a2=bean.current_day_count==0?0:(bean.current_day_count%10+1+bean.all_count%5);
		if(bean.all_count==0){
			sum_credit=(bean.sum_credit==null?new BigDecimal("0"):bean.sum_credit).floatValue();
			all_credit=new BigDecimal(800*10000).subtract(bean.all_credit.multiply(new BigDecimal(25))).floatValue();
			all_count=0;
			current_day_credit=(bean.current_day_credit==null?new BigDecimal("0"):bean.current_day_credit).multiply(new BigDecimal(25)).floatValue();
			current_day_count=0;
			render(bean,serverTime,stop1,sum_credit,all_credit,all_count,current_day_credit,current_day_count);
		}
		if(bean.all_count==bean.current_day_count){
			a0=8;
			sum_credit=(bean.sum_credit==null?new BigDecimal("0"):bean.sum_credit).floatValue();
			all_credit=new BigDecimal(800*10000).subtract(bean.all_credit.multiply(new BigDecimal(25))).floatValue();
			all_count=bean.all_count*25+a0;
			current_day_credit=(bean.current_day_credit==null?new BigDecimal("0"):bean.current_day_credit).multiply(new BigDecimal(25)).floatValue();
			current_day_count=(bean.current_day_count*25+a0);
			render(bean,serverTime,stop1,sum_credit,all_credit,all_count,current_day_credit,current_day_count);
		}else{
			sum_credit=(bean.sum_credit==null?new BigDecimal("0"):bean.sum_credit).floatValue();
			all_credit=new BigDecimal(800*10000).subtract(bean.all_credit.multiply(new BigDecimal(25))).floatValue();
			all_count=bean.all_count*25+a0;
			current_day_credit=(bean.current_day_credit==null?new BigDecimal("0"):bean.current_day_credit).multiply(new BigDecimal(25)).floatValue();
			current_day_count=(bean.current_day_count*25+a2);
			render(bean,serverTime,stop1,sum_credit,all_credit,all_count,current_day_credit,current_day_count);
		}
	}

	public static void loot(){
		String login_name=session.get("username");
		if(login_name==null||"".equals(login_name)){
			renderText(JSONResult.failure("您还没登录，赶快去登录参与吧!"));
		}
		log.info(DateUtil.dateToString(new Date(System.currentTimeMillis()), "yyyy-MM-dd HH:mm:ss")+" "+login_name+" loot!");
		if(!activeTime()){
			renderText(JSONResult.failure("活动还没开始，8月10号到28号每天08:18和20:18开始哦"));
		}
		if(stop==1){
			renderText(JSONResult.failure("活动已结束。下一轮再来抢吧!"));
		}
		YearGiftCustBean  bean=YearGiftCache.get().getCustBean(login_name);
		if(bean==null){
			renderText(JSONResult.failure("该活动只限于星级一及以上的玩家哦，只要成功存款一笔即可升级。"));
		}
		Date now =new Date(System.currentTimeMillis());
		if(bean.last_loot_date!=null){
			if(now.getTime()-bean.last_loot_date.getTime()<4*1000){
				renderText(JSONResult.failure("很遗憾，本次没有抢到红包，不要气馁。"));  
			}
		}
		//进入抢红包环节
		YearGiftCache.get().setLastLootDate(login_name);
		if(!GiftRandom.isLuck(10)){
			   renderText(JSONResult.failure("很遗憾，本次没有抢到红包，不要气馁。"));
		}
		//获取当前已派送
		BigDecimal totalCredit=YearGiftCustBean.current_day_credit;
		BigDecimal credit =new BigDecimal(5000);
		if(credit.subtract(totalCredit).floatValue()>0){
			//中奖
			BigDecimal c =new BigDecimal(cc[romRand(cc.length)]);
			Customer cust=Customer.getCustomer(login_name);
			String uuid =UUID.randomUUID().toString();
			Long gift_id=YearGift.create(cust.cust_id, login_name, c,uuid,0);
			boolean b=CustomerService.modCredit(cust.cust_id,CreditLogService.Gift,cust.login_name, c,login_name,"抢红包:"+uuid, uuid);
			int flag=2;
			if(b)flag=1;
			YearGift.finishedGift(gift_id, flag);
			renderText(JSONResult.success("恭喜您，您抢到了"+c.floatValue()+"元的红包!"));
		}else{
			//已派完
			stop=1;
			renderText(JSONResult.failure("活动已结束。下一轮再来抢吧!"));
		}
	}
	//前台刷新数据
	public static void getData(){
		String login_name=session.get("username");
		YearGiftCustBean  bean=YearGiftCache.get().getCustBean(login_name);
		if(bean==null){
			 bean =new YearGiftCustBean();
		}
		//获取随机加的数字
		int a0=bean.all_count%10+1+bean.all_count%5;
		int a2=bean.current_day_count==0?0:(bean.current_day_count%10+1+bean.all_count%5);
		
		
		StringBuilder sb =new StringBuilder();
		if(bean.all_count==0){
			sb.append(stop+"|");
			sb.append((bean.sum_credit==null?new BigDecimal("0"):bean.sum_credit).floatValue()+"|");
			sb.append(new BigDecimal(800*10000).subtract(bean.all_credit.multiply(new BigDecimal(25))).floatValue()+"|");
			sb.append(0+"|");
			sb.append((bean.current_day_credit==null?new BigDecimal("0"):bean.current_day_credit).multiply(new BigDecimal(25)).floatValue()+"|");
			sb.append((0*10));
			renderText(JSONResult.success(sb.toString()));
		}
		if(bean.all_count==bean.current_day_count){
			a0=8;
			sb.append(stop+"|");
			sb.append((bean.sum_credit==null?new BigDecimal("0"):bean.sum_credit).floatValue()+"|");
			sb.append(new BigDecimal(800*10000).subtract(bean.all_credit.multiply(new BigDecimal(25))).floatValue()+"|");
			sb.append((bean.all_count*25+a0)+"|");
			sb.append((bean.current_day_credit==null?new BigDecimal("0"):bean.current_day_credit).multiply(new BigDecimal(25)).floatValue()+"|");
			sb.append((bean.current_day_count*25+a0)+"");
		}else{
			sb.append(stop+"|");
			sb.append((bean.sum_credit==null?new BigDecimal("0"):bean.sum_credit).floatValue()+"|");
			sb.append(new BigDecimal(800*10000).subtract(bean.all_credit.multiply(new BigDecimal(25))).floatValue()+"|");
			sb.append((bean.all_count*25+a0)+"|");
			sb.append((bean.current_day_credit==null?new BigDecimal("0"):bean.current_day_credit).multiply(new BigDecimal(25)).floatValue()+"|");
			sb.append((bean.current_day_count*25+a2));
		}
		renderText(JSONResult.success(sb.toString()));
	}
	
	
	
	
}
