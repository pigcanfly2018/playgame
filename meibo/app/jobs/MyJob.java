package jobs;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import models.CashGift;
import models.Customer;
import models.Deposit;
import models.FullWeek;
import models.Hongbao;
import models.OrderNo;
import play.jobs.Every;
import play.jobs.Job;
import service.CreditLogService;
import service.CustomerService;
import util.MyRandom;

//@Every("60min")  
public class MyJob extends Job {
	private static Logger logger=Logger.getLogger(MyJob.class);
	@Override
	public void doJob()throws Exception{
		
		
		String startDate="2018-02-05 00:00:00";
		String endDate="2018-03-05 23:59:59";
		Date date = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		
		int hours=c.get(Calendar.HOUR_OF_DAY);
		
		int week = c.get(Calendar.DAY_OF_WEEK);//周一表示一周的第二天
		//System.out.println(c.getTime());
		//System.out.println("year="+year+",day="+day+",hours="+hours+",week="+week);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		
		Date enddateFormat=null;
		Date startFormat=null;
		try {
			 startFormat = df.parse(startDate);
			enddateFormat = df.parse(endDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<FullWeek> lis = null;
		int count=0;
		Date current = new Date();
		if(week==2 && current.before(enddateFormat) && current.after(startFormat) && hours==10){
			logger.info("满周奖任务开始。。。");
			c.add(Calendar.DATE, -7);
			String startdate = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime())+" 00:00:00";
			c.add(Calendar.DATE, 6);
			String enddate = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime())+" 23:59:59";
			System.out.println("startdate="+startdate+", enddate="+enddate);
			
			 lis = Deposit.getFullWeek(startdate, enddate);
			if(lis!=null){
				System.out.println("一共处理"+lis.size()+"条数据。。。");
				count =lis.size();
				for(FullWeek li:lis){
					
					Customer cust=Customer.getCustomer(li.login_name);
					
					String giftCode=MyRandom.getRandom(8);
					 CashGift gift =new CashGift();
		        	 gift.gift_code=giftCode;
		        	 gift.login_name=cust.login_name;
		        	 gift.deposit_credit=new BigDecimal(0);
		        	 gift.valid_credit=new BigDecimal(0);
		        	 gift.net_credit=new BigDecimal(0);
		        	 gift.rate=Float.valueOf(1);
		        	 gift.payout=getWeekDeposit(li.weekamount);
		        	 gift.gift_type="满周奖";
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
		        	 gift.NTAuditGift(giftId, 3, "系统","系统审核通过");
		        	 CustomerService.modCredit(cust.cust_id,CreditLogService.Gift, 
		 					gift.login_name,gift.payout,"系统", "系统审核通过", gift.gift_no);
		        	 logger.info("处理结束"+lis.size());
				}
				
			}
		}
		logger.info("满周奖任务结束。。。一共处理了"+count+"条数据");
		
	}
	
	public static void main(String[] args){
		String startDate="2018-02-05 00:00:00";
		String endDate="2018-03-05 23:59:59";
		Date date = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		
		int hours=c.get(Calendar.HOUR_OF_DAY);
		
		int week = c.get(Calendar.DAY_OF_WEEK);//周一表示一周的第二天
		//System.out.println(c.getTime());
		//System.out.println("year="+year+",day="+day+",hours="+hours+",week="+week);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		
		Date enddateFormat=null;
		Date startFormat=null;
		try {
			 startFormat = df.parse(startDate);
			enddateFormat = df.parse(endDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<FullWeek> lis = null;
		int count=0;
		Date current = new Date();
		if(week==2 && current.before(enddateFormat) && current.after(startFormat) ){
			logger.info("满周奖任务开始。。。");
			c.add(Calendar.DATE, -7);
			String startdate = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime())+" 00:00:00";
			c.add(Calendar.DATE, 6);
			String enddate = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime())+" 23:59:59";
			System.out.println("startdate="+startdate+", enddate="+enddate);
			
			 lis = Deposit.getFullWeek(startdate, enddate);
			if(lis!=null){
				System.out.println("一共处理"+lis.size()+"条数据。。。");
				count =lis.size();
				for(FullWeek li:lis){
					
					Customer cust=Customer.getCustomer(li.login_name);
//					if(li.login_name.equals("hkh520520") || li.login_name.equals("lj888999") || li.login_name.equals("kk168888")
//							|| li.login_name.equals("313858203") || li.login_name.equals("cyw888999") || li.login_name.equals("544383960")
//							 || li.login_name.equals("yin0212")){
//						continue;
//					}
					
					String giftCode=MyRandom.getRandom(8);
					 CashGift gift =new CashGift();
		        	 gift.gift_code=giftCode;
		        	 gift.login_name=cust.login_name;
		        	 gift.deposit_credit=new BigDecimal(0);
		        	 gift.valid_credit=new BigDecimal(0);
		        	 gift.net_credit=new BigDecimal(0);
		        	 gift.rate=Float.valueOf(1);
		        	 gift.payout=getWeekDeposit(li.weekamount);
		        	 gift.gift_type="满周奖";
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
		        	 gift.NTAuditGift(giftId, 3, "系统","系统审核通过");
		        	 CustomerService.modCredit(cust.cust_id,CreditLogService.Gift, 
		 					gift.login_name,gift.payout,"系统", "系统审核通过", gift.gift_no);
		        	 logger.info("处理结束"+lis.size());
				}
				
			}
		}
		logger.info("满周奖任务结束。。。一共处理了"+count+"条数据");
	}
	
	private static BigDecimal getWeekDeposit(int weekamount) {
		// TODO Auto-generated method stub
		int result=0;
		if(weekamount>=100 && weekamount<300 ){
			result = 8;
		}else if(weekamount>=300 && weekamount<500){
			result = 18;
		}else if(weekamount>=500 && weekamount<1000){
			result = 28;
		}else if(weekamount>=1000 && weekamount<5000){
			result = 38;
		}else if(weekamount>=5000 && weekamount<10000){
			result = 58;
		}else if(weekamount>=10000 && weekamount<50000){
			result = 88;
		}else if(weekamount>=50000 && weekamount<100000){
			result = 188;
		}else if(weekamount>=100000 && weekamount<500000){
			result = 388;
		}else if(weekamount>=500000 && weekamount<1000000){
			result = 888;
		}else if(weekamount>=1000000 && weekamount<5000000){
			result = 1888;
		}else if(weekamount>=10000000 && weekamount<50000000){
			result = 8888;
		}else if(weekamount>=50000000 && weekamount<100000000){
			result = 18888;
		}else if(weekamount>=100000000){
			result = 88888;
		}
		return BigDecimal.valueOf(result);
	}

}
