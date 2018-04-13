package service;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import models.Customer;
import models.Deposit;
import models.DepositLog;
import models.Dinpay;
import models.Letter;
import models.OrderNo;

import org.apache.commons.io.FileUtils;

import util.DateUtil;
import controllers.IPApp;

public class EventService {
	
	public static Deposit createDeposit( Customer cust,Date deposit_date,String ip,BigDecimal amount,BigDecimal poundage,
			String bank_name,String account_name,String deposit_type,String order_no){
		 Deposit deposit =new Deposit();
		 deposit.deposit_date=deposit_date; 
		 deposit.cust_id=cust.cust_id;
		 deposit.pdage_status=1;
		 deposit.status=1;
		 deposit.create_user=cust.login_name;
		 deposit.create_date=new Date(System.currentTimeMillis());
		 deposit.ip=ip;
		 deposit.amount=amount;
		 deposit.poundage=poundage;
		 deposit.bank_name=bank_name;
		 deposit.account_name=account_name;
		 deposit.deposit_type=deposit_type;
		 deposit.order_no=order_no;
		 deposit.location="";
		 deposit.remarks="";
		 deposit.login_name=cust.login_name;
		 deposit.real_name=cust.real_name;
		 deposit.dep_no=OrderNo.createLocalNo("DE");
		 Long dep_id=deposit.NTcreat();
		 if(dep_id!=null){
			 DepositLog log =new DepositLog();
			 log.after_status=1;
			 log.create_user="system";
			 log.deposit_id=dep_id;
			 log.pre_status=0;
			 log.dep_no=deposit.dep_no;
			 log.create_date=new Date(System.currentTimeMillis());
			 log.remarks="系统创建";
			 log.NTcreat();
		 }
		 deposit.deposit_id=dep_id;
		 return deposit;
	}
	
	/**
	 * 当客户存款成功。
	 * @param cust 客户，
	 * @param amount 存款金额
	 */
	public static void depositSuccess(Customer cust,Deposit deposit,BigDecimal amount ,String bill_no){
		    if(Deposit.NTgetCount(cust.cust_id)==1){
				Deposit.NTrecAuditDate(deposit.deposit_id);
				Customer.NTmodCustlevelFirst(cust.cust_id, 1);
				StringBuffer sb =new StringBuffer();
				sb.append("<p>尊敬的客户，您好，恭喜您已经升级为星级一，为了让您有更好的游戏体验，我们为您提供了更优更快的网站线路，请您牢记我们的会员网址：<span>www.588da.com</span> ，祝您生活愉快，盈利多多!</p>");
				sb.append("<p style=\"text-align:right\">八达国际客服部上</p>");
			    Letter.NTcreat(cust.cust_id, cust.login_name, "八达国际娱乐城会员网址：www.588da.com", sb.toString(), cust.login_name, true);
				
			}
		     Dinpay.updateDepNo(deposit.dep_no,bill_no); 
		     Calendar calendar = Calendar.getInstance();
		     if(calendar.getTimeInMillis()<=1441036799979l){ //积分翻倍活动持续到2015年8月31号23点59分59秒
		    	 CustomerService.modScore(deposit.dep_no, "存款积分(翻倍)", new BigDecimal(deposit.amount.intValue()*2/100.0), deposit.login_name, deposit.cust_id, deposit.login_name);
		     }else{
		    	 CustomerService.modScore(deposit.dep_no, "存款积分", new BigDecimal(deposit.amount.intValue()/100.0), deposit.login_name, deposit.cust_id, deposit.login_name);
		     }
		    
		     int dcount=Deposit.NTgetCountToday(deposit.cust_id);
		     if(dcount==1){
		    	 CustomerService.modScore(deposit.dep_no, "签到积分", new BigDecimal(1), deposit.login_name, deposit.cust_id, deposit.login_name);
		     }
		     /**5连礼---start---*
		     dcount=Deposit.NTgetCountMoreThan500Today(deposit.cust_id);
		     if(dcount<=5&&deposit.amount.intValue()>=500){
			     String giftCode=MyRandom.getRandom(8);
				 CashGift gift =new CashGift();
		    	 gift.gift_code=giftCode;
		    	 gift.login_name=cust.login_name;
		    	 gift.deposit_credit=deposit.amount;
		    	 gift.valid_credit=new BigDecimal(0);
		    	 gift.net_credit=new BigDecimal(0);
		    	 gift.rate=Float.valueOf(dcount);
		    	 float f=deposit.amount.floatValue()*dcount/100;
		    	 if(f>1800){
		    		 f=1800;
		    	 }
		    	 gift.payout=new BigDecimal(f);
		    	 gift.gift_type="充值5连礼";
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
		    	 gift.NTAuditGift(giftId, 3, "系统","系统审核通过-"+deposit.dep_no+" |"+dcount);
		    	 CustomerService.modCredit(cust.cust_id,CreditLogService.Gift, 
							gift.login_name,gift.payout,"系统", "添加礼金"+gift.gift_type+" |"+dcount+"%", gift.gift_no);
		     }
		     /**5连礼---end---**/
		
		
	}
	
	public static void main(String[] args)throws Exception{
		String word = FileUtils.readFileToString(new File("e://pinying.txt"));
    	String[] words = word.split("\n");
    	List<String> list =new ArrayList<String>();
    	StringBuffer sb=new StringBuffer();
    	for(String w:words){
    		for(String s:words){
    			list.add(w+s);
    			sb.append(w+s+"\n");
    		}
    	}
    	FileUtils.writeStringToFile(new File("e://pingyin1.txt"), sb.toString(), "utf-8");
		
	}
}
