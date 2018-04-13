package controllers;

import java.io.File;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;

import com.ws.service.BankInfo;

import models.CashGift;
import models.Customer;
import models.Deposit;
import models.DepositLog;
import models.DepositRowMap;
import models.DictRender;
import models.DpayDepositExp;
import models.DpayDepositExpRowMap;
import models.Hongbao;
import models.Letter;
import models.Msg;
import models.OrderNo;
import models.Pic;
import models.User;
import play.mvc.Controller;
import play.mvc.With;
import play.mvc.Http.Response;
import service.CreditLogService;
import service.CustomerService;
import util.Base64File;
import util.Constant;
import util.DateUtil;
import util.ExtPage;
import util.JSONResult;
import util.MyRandom;
import util.PageUtil;
import util.Sp;
import util.Sqler;

@With(value = {AjaxSecure.class})
public class DpayDepositExpApp extends Controller{

	private static Logger logger=Logger.getLogger(DpayDepositExpApp.class);
	
	public static void getList(int start,int limit,int page,String sdate,String edate,String sort,String queryKey)throws Exception{
		Sqler sql =new Sqler("select * from mb_dpay_deposit_exp");
		Sqler cntsql =new Sqler("select count(1) from mb_dpay_deposit_exp");
		sql.and().left().notEquals("flag", 2).right();
		cntsql.and().left().notEquals("flag", 2).right();
		sql.and().left().notEquals("flag", -1).right();
		cntsql.and().left().notEquals("flag", -1).right();
		
		if(!PageUtil.blank(queryKey)){
			sql.and().left().like("login_name", queryKey);
			cntsql.and().left().like("login_name", queryKey);
			
//			sql.or().like("exception_order_num", queryKey);
//			cntsql.or().like("exception_order_num", queryKey);
			
			sql.or().like("exception_order_num", queryKey).right();
			cntsql.or().like("exception_order_num", queryKey).right();
		}
		if(!(sdate==null||"".equals(sdate))){
			Date date =DateUtil.stringToDate(sdate, "yyyy-MM-dd");
			sql.and().ebigger("create_date",date);
			cntsql.and().ebigger("create_date",date);
		}
		if(!(edate==null||"".equals(edate))){
			Date date =DateUtil.stringToDate(edate, "yyyy-MM-dd");
			sql.and().esmaller("create_date",date);
			cntsql.and().esmaller("create_date",date);
		}
		
		
//		sql.and().addSql("datediff(curdate(),create_date)<90");
//		cntsql.and().addSql("datediff(curdate(),create_date)<90");
		sql.orberByDesc("create_date");
		List<DpayDepositExp> roleList=Sp.get().getDefaultJdbc().query(PageUtil.page(sql.getSql(),start,limit),sql.getParams().toArray(new Object[0]),new DpayDepositExpRowMap());
		int count=Sp.get().getDefaultJdbc().queryForObject(cntsql.getSql(),cntsql.getParams().toArray(new Object[0]),Integer.class);
		ExtPage p =new ExtPage();
		p.data=JSONResult.conver(roleList,true);
		p.total=count; 
		renderJSON(p);
	}
	
	
	public static void getAllList(int start,int limit,int page,String sdate,String edate,String sort,String queryKey)throws Exception{
		Sqler sql =new Sqler("select * from mb_dpay_deposit_exp");
		Sqler cntsql =new Sqler("select count(1) from mb_dpay_deposit_exp");
		if(!PageUtil.blank(queryKey)){
			sql.and().left().like("login_name", queryKey);
			cntsql.and().left().like("login_name", queryKey);
			
			sql.or().like("exception_order_num", queryKey).right();
			cntsql.or().like("exception_order_num", queryKey).right();
		}
		if(!(sdate==null||"".equals(sdate))){
			Date date =DateUtil.stringToDate(sdate, "yyyy-MM-dd");
			sql.and().ebigger("create_date",date);
			cntsql.and().ebigger("create_date",date);
		}
		if(!(edate==null||"".equals(edate))){
			Date date =DateUtil.stringToDate(edate, "yyyy-MM-dd");
			sql.and().esmaller("create_date",date);
			cntsql.and().esmaller("create_date",date);
		}
		
//		if(User.NTcountByRole(session.get(Constant.userName), "F030102")>0){
//			sql.and().addSql("datediff(curdate(),create_date)<90");
//			cntsql.and().addSql("datediff(curdate(),create_date)<90");
//		}else{
//			sql.and().addSql("datediff(curdate(),create_date)<30");
//			cntsql.and().addSql("datediff(curdate(),create_date)<30");
//		}
		
		sql.orberByDesc("create_date");
		List<DpayDepositExp> roleList=Sp.get().getDefaultJdbc().query(PageUtil.page(sql.getSql(),start,limit),sql.getParams().toArray(new Object[0]),new DpayDepositExpRowMap());
		int count=Sp.get().getDefaultJdbc().queryForObject(cntsql.getSql(),cntsql.getParams().toArray(new Object[0]),Integer.class);
		ExtPage p =new ExtPage();
		p.data=JSONResult.conver(roleList,true);
		p.total=count; 
		renderJSON(p);
	}
	
	public static void showPic(Long id){
		Pic info=Pic.NTget(id);
		if(info==null){
			 renderText("不存在该图片!");
		}
		response.setHeader("Cache-Control", "max-age=1");
		response.setHeader("content-Type", "image/jpg");
		try{
		Response.current().out.write(Base64File.fileDecode(info.pic_data));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void applyDpayDepositExp(DpayDepositExp dpayDepositExp,File picfile,File picfile2){
		
		String user=session.get(Constant.userName);
		 Customer customer=Customer.NTgetCustomerByLoginName(dpayDepositExp.login_name);
		 if(customer==null){
			renderText(JSONResult.failure(dpayDepositExp.login_name+"用户名不存在!"));
		 }
		 
		 if(picfile!=null&&!picfile.getName().endsWith("jpg")){
	        	String errorMsg="提交失败：截图的文件格式必须是jpg格式。";
	        	renderText(JSONResult.failure(errorMsg));
	     }
		 if(picfile!=null&&picfile.length()>200*1024){
			 String errorMsg="提交失败：截图的文件必须小于200KB。";
			 renderText(JSONResult.failure(errorMsg));
		 }
		 
		 if(picfile!=null){
			 Pic info =new Pic();
			 info.pic_data=Base64File.fileEncode(picfile);
			 info.ftype=picfile.getName();
			 info.pic_size=picfile.length();
			 Long pic_id =info.NTcreat();
			 if(pic_id==null){
					String errorMsg="提交失败：保存附件失败，请重试或者联系客服!";
					renderText(JSONResult.failure(errorMsg));
			 }
			 dpayDepositExp.pic_id=pic_id;
		 }
		 
		 if(picfile2!=null){
			 Pic info =new Pic();
			 info.pic_data=Base64File.fileEncode(picfile2);
			 info.ftype=picfile2.getName();
			 info.pic_size=picfile2.length();
			 Long pic_id =info.NTcreat();
			 if(pic_id==null){
					String errorMsg="提交失败：保存附件失败，请重试或者联系客服!";
					renderText(JSONResult.failure(errorMsg));
			 }
			 dpayDepositExp.pic2_id=pic_id;
		 }
		 
		 dpayDepositExp.action_user2 = user;
		 
		 dpayDepositExp.flag = 1;
		 
		 dpayDepositExp.apply(dpayDepositExp);
		 
		 renderText(JSONResult.success("申请认领成功!"));
		 
	}
	
	public static void audit(Integer pdage_status,Integer flag,String remarks,Long request_id){
		
		if(request_id==null){
			renderText(JSONResult.failure("审核失败!无效的操作!"));
		}
		
		if(flag==null){
			 renderText(JSONResult.failure("请选择存款是否通过!"));
		}
		
		if(flag==-2){
			if(PageUtil.blank(remarks)){
				renderText(JSONResult.failure("请填写存款提案不通过理由!"));
			}
		}
		
		DpayDepositExp dpay=DpayDepositExp.NTget(request_id);
		if(dpay==null){
			 renderText(JSONResult.failure("请求的提案不存在!"));
		}
		
		if(dpay.flag!=1){
			renderText(JSONResult.failure("提案已经被处理,请刷新重试!"));
		}
		
		int pdage_flag=3;
		int status_flag=3;
		if(pdage_status==-2){pdage_flag=4;}
		if(flag==-2){status_flag=4;}
		
		BigDecimal total =new  BigDecimal(0);
		if(status_flag==3){total=total.add(dpay.amount);};
		
		String user=session.get(Constant.userName);
		logger.info("start to process request_id:   "+request_id);
		if(status_flag==3){
			dpay.action_user = user;
			dpay.flag = 2;
			dpay.audit(dpay);
			
			Customer customer = Customer.getCustomer(dpay.login_name);
			Deposit deposit = new Deposit();
			deposit.deposit_date = dpay.create_date;
			deposit.cust_id=customer.cust_id;
			deposit.login_name = customer.login_name;
			deposit.real_name = customer.real_name;
			deposit.pdage_status=1;
			deposit.status=1;
			deposit.dep_no=OrderNo.createLocalNo("DE");
			deposit.create_user=user;
			deposit.ip=request.remoteAddress;
			deposit.pic_id=dpay.pic_id;
			deposit.amount =  dpay.amount;
			deposit.bank_name = BankInfo.getBank(dpay.receiving_bank).getBank_name() ;
			deposit.location = dpay.area;
			deposit.account_name = dpay.receiving_account_name;
			deposit.deposit_type = dpay.channel;
			Date now =new Date(System.currentTimeMillis());
			if(customer.promo_flag != null && customer.promo_flag){
				
				if(now.getTime()>DateUtil.stringToDate("2017-12-01", "yyyy-MM-dd").getTime() &&
			    		  DateUtil.stringToDate("2018-01-01", "yyyy-MM-dd").getTime()>now.getTime()){
					float rate = 0;
					if(deposit.amount.intValue()<5000){
		        		 
			        	 rate = 1;
			        	 
					 }else if(deposit.amount.intValue() >= 5000 && deposit.amount.intValue()<10000){
						 
			        	 rate = 1.8f;
			        	 
					 }else if(deposit.amount.intValue() >= 10000 && deposit.amount.intValue()<30000){
						 
			        	 rate = 2.5f;
			        	 
					 }else if(deposit.amount.intValue() >= 30000 && deposit.amount.intValue()<50000){
						 
			        	 rate = 3.8f;
			        	 
					 }else if(deposit.amount.intValue() >= 50000 ){
						 
			        	 rate = 5;
			        	 
					 }
					deposit.poundage = new BigDecimal(dpay.amount.divide(new BigDecimal(100)).multiply(new BigDecimal(rate)).intValue()>2888?2888:dpay.amount.divide(new BigDecimal(100)).multiply(new BigDecimal(rate)).intValue());
					
	        	 }else{
	        		 deposit.poundage = new BigDecimal(dpay.amount.divide(new BigDecimal(100)).intValue()>2888?2888:dpay.amount.divide(new BigDecimal(100)).intValue());
	        	 }
				
				
			}else{
				deposit.poundage = new BigDecimal(0);
			}
			
			deposit.remarks = remarks;
			 Long dep_id=deposit.NTcreat();
			 if(dep_id!=null){
				 DepositLog log =new DepositLog();
				 log.after_status=1;
				 log.create_user=user;
				 log.deposit_id=dep_id;
				 log.dep_no=deposit.dep_no;
				 log.pre_status=0;
				 log.remarks=deposit.remarks;
				 log.NTcreat();
			 }
			 
			 
			 
			DepositLog.NTcreat(deposit.status,status_flag,dep_id, remarks, user,deposit.dep_no); 
			//DepositLog.NTcreat(3,status_flag,dep_id, remarks, user,deposit.dep_no);
			Deposit.NTchangeStatus(dep_id, status_flag, pdage_flag);
			
			CustomerService.modCredit(deposit.cust_id, CreditLogService.Depoist, deposit.login_name,total, user,"存款", deposit.dep_no);
			
			if(Deposit.NTgetCount(deposit.cust_id)==1){
				Deposit.NTrecAuditDate(dep_id);
				if(deposit.amount.intValue() >= 100){
					Customer.NTmodCustlevelFirst(deposit.cust_id, 1);
					
					StringBuffer sb =new StringBuffer();
					sb.append("<p>尊敬的客户，您好，恭喜您已经升级为星级一，为了让您有更好的游戏体验，我们为您提供了更优更快的网站线路，请您牢记我们的会员网址：<span>www.268da.com</span> ，祝您生活愉快，盈利多多!</p>");
					sb.append("<p style=\"text-align:right\">八达国际客服部上</p>");
				    Letter.NTcreat(deposit.cust_id, deposit.login_name, "八达国际娱乐城会员网址：www.268da.com", sb.toString(), deposit.login_name, true);
				}
				
			    
			}
			
			
			
			if(customer.promo_flag != null && customer.promo_flag){
				CustomerService.modScore(deposit.dep_no, "存款积分", new BigDecimal(deposit.amount.intValue()/100.0), deposit.login_name, deposit.cust_id, user);
				int dcount=Deposit.NTgetCountToday(deposit.cust_id);
			    if(dcount==1){
			    	 CustomerService.modScore(deposit.dep_no, "签到积分", new BigDecimal(1), deposit.login_name, deposit.cust_id, user);
			    }
			    
			    
			    
			      
			}
			
		    
		    /**光棍节5连礼---start--*/
//		      Date depositdate = deposit.create_date;
//				 if((depositdate.getTime()>DateUtil.stringToDate("2016-11-01", "yyyy-MM-dd").getTime()&&
//							DateUtil.stringToDate("2016-11-11", "yyyy-MM-dd").getTime()>depositdate.getTime())){
//					 dcount=Deposit.NTgetCountToday(deposit.cust_id);
//						     if(dcount<=5){
//							     String giftCode=MyRandom.getRandom(8);
//								 CashGift gift =new CashGift();
//					        	 gift.gift_code=giftCode;
//					        	 gift.login_name=customer.login_name;
//					        	 gift.deposit_credit=deposit.amount;
//					        	 gift.valid_credit=new BigDecimal(0);
//					        	 gift.net_credit=new BigDecimal(0);
//					        	 gift.rate=Float.valueOf(2);
//					        	 Integer rate = 2;
//					        	 float f=deposit.amount.floatValue()*2/100;
//					        	 if(dcount==5){
//					        		 gift.rate=Float.valueOf(3);
//					        		 f=deposit.amount.floatValue()*3/100;
//					        		 rate = 3;
//					        	 }
//					        	 
//					        	 if(f>1111){
//					        		 f=1111;
//					        	 }
//					        	 gift.payout=new BigDecimal(f);
//					        	 gift.gift_type="光棍节存款优惠";
//					        	 gift.status=1;
//					        	 gift.gift_no=OrderNo.createLocalNo("GI");
//					     		 gift.cust_id=customer.cust_id;
//					        	 gift.cs_date=new Date(System.currentTimeMillis());
//					        	 gift.real_name=customer.real_name;
//					        	 gift.cust_level=customer.cust_level;
//					           	 gift.kh_date=customer.create_date;
//					        	 gift.create_user=user;
//					        	 gift.create_date=new Date(System.currentTimeMillis());
//					        	 Long giftId=gift.NTcreat();
//					        	 gift.NTAuditGift(giftId, 3, user,"系统审核通过-"+deposit.dep_no+" |"+rate);
//					        	 CustomerService.modCredit(customer.cust_id,CreditLogService.Gift, 
//					 					gift.login_name,gift.payout,user, "添加礼金"+gift.gift_type+" |"+rate+"%", gift.gift_no);
//						     }
//						     
//				 }
		    
		    
		  //存款红利
		  //  if(!dpay.receiving_bank.equals("30") && !dpay.receiving_bank.equals("31")&& !dpay.receiving_bank.equals("40")&& !dpay.receiving_bank.equals("41")){
		    	if(pdage_flag==3){
		    		if(customer.promo_flag != null && customer.promo_flag){
		    			
			        	 
			        	 
			        	 if(now.getTime()>DateUtil.stringToDate("2017-12-01", "yyyy-MM-dd").getTime() &&
					    		  DateUtil.stringToDate("2018-01-01", "yyyy-MM-dd").getTime()>now.getTime()){
								 //dcount=Deposit.NTgetCountToday(deposit.cust_id);
								    //Customer customer=Customer.NTgetCustomerByLoginName(deposit.login_name);
									    // if(dcount<=5){
										     String giftCode=MyRandom.getRandom(8);
											 CashGift gift =new CashGift();
								        	 gift.gift_code=giftCode;
								        	 gift.login_name=customer.login_name;
								        	 gift.deposit_credit=deposit.amount;
								        	 gift.valid_credit=new BigDecimal(0);
								        	 gift.net_credit=new BigDecimal(0);
								        	 float rate = 0;
								        	 float f = 0.0f;
								        	 if(deposit.amount.intValue()<5000){
								        		 gift.rate=Float.valueOf(1);
									        	 rate = 1;
									        	 f=deposit.amount.floatValue()*1/100;
											 }else if(deposit.amount.intValue() >= 5000 && deposit.amount.intValue()<10000){
												 gift.rate=Float.valueOf(1.8f);
									        	 rate = 1.8f;
									        	 f=deposit.amount.floatValue()*1.8f/100;
											 }else if(deposit.amount.intValue() >= 10000 && deposit.amount.intValue()<30000){
												 gift.rate=Float.valueOf(2.5f);
									        	 rate = 2.5f;
									        	 f=deposit.amount.floatValue()*2.5f/100;
											 }else if(deposit.amount.intValue() >= 30000 && deposit.amount.intValue()<50000){
												 gift.rate=Float.valueOf(3.8f);
									        	 rate = 3.8f;
									        	 f=deposit.amount.floatValue()*3.8f/100;
											 }else if(deposit.amount.intValue() >= 50000 ){
												 gift.rate=Float.valueOf(5);
									        	 rate = 5;
									        	 f=deposit.amount.floatValue()*5/100;
									        	 if(f>2888){
									        		 f=2888;
									        	 }
											 }
								        	 gift.payout=new BigDecimal(f);
								        	 gift.gift_type="年终现金大回馈 ";
								        	 gift.status=1;
								        	 gift.gift_no=OrderNo.createLocalNo("GI");
								     		 gift.cust_id=customer.cust_id;
								        	 gift.cs_date=new Date(System.currentTimeMillis());
								        	 gift.real_name=customer.real_name;
								        	 gift.cust_level=customer.cust_level;
								           	 gift.kh_date=customer.create_date;
								        	 gift.create_user=user;
								        	 gift.create_date=new Date(System.currentTimeMillis());
								        	 Long giftId=gift.NTcreat();
								        	 gift.NTAuditGift(giftId, 3, user,"系统审核通过-"+deposit.dep_no+" |"+rate);
								        	 CustomerService.modCredit(customer.cust_id,CreditLogService.Gift, 
								 					gift.login_name,gift.payout,user, "添加礼金"+gift.gift_type+" |"+rate+"%", gift.gift_no);
									    // }
									     
							 }else{
								 
								 
								 String giftCode=MyRandom.getRandom(8);
								 CashGift gift =new CashGift();
					        	 gift.gift_code=giftCode;
					        	 gift.login_name=customer.login_name;
					        	 gift.deposit_credit=deposit.amount;
					        	 gift.valid_credit=new BigDecimal(0);
					        	 gift.net_credit=new BigDecimal(0);
					        	 
					        	 if((now.getTime()>DateUtil.stringToDate("2017-04-04", "yyyy-MM-dd").getTime()&&
							    		  DateUtil.stringToDate("2017-05-01", "yyyy-MM-dd").getTime()>now.getTime())){
					        		 gift.rate=Float.valueOf(1.5f);
					        	 }else{
					        		 gift.rate=Float.valueOf(1); 
					        	 }
					        	 
					        	 gift.payout=deposit.poundage;
					        	 gift.gift_type="转账红利";
					        	 gift.status=1;
					        	 gift.gift_no=OrderNo.createLocalNo("GI");
					     		 gift.cust_id=customer.cust_id;
					        	 gift.cs_date=new Date(System.currentTimeMillis());
					        	 gift.real_name=customer.real_name;
					        	 gift.cust_level=customer.cust_level;
					           	 gift.kh_date=customer.create_date;
					        	 gift.create_user=session.get(Constant.userName);
					        	 gift.create_date=new Date(System.currentTimeMillis());
					        	 Long giftId=gift.NTcreat();
					        	 gift.NTAuditGift(giftId, 3, user,remarks);
					        	 CustomerService.modCredit(customer.cust_id,CreditLogService.Gift, 
					 					gift.login_name,gift.payout,user, "添加礼金"+gift.gift_type, gift.gift_no);
					        	 
							 }
			        	 
			        	 
		    		}
					
				}
		    	
		    	
		    	  //Date depositdate = deposit.create_date;
		    	 
			      
		   // }
			
			
			
		}
		
		
		renderText(JSONResult.success("操作成功!"));
		
	}
	
	
	public static void detail(Long request_id){
		DpayDepositExp dpay=DpayDepositExp.NTget(request_id);
		if(dpay==null){
			 renderText(JSONResult.failure("请求的提案不存在!"));
		}
		dpay.receiving_bank = BankInfo.getBank(dpay.receiving_bank).getBank_name() ;
		dpay.poundage = new BigDecimal(dpay.amount.divide(new BigDecimal(100)).intValue()>2888?2888:dpay.amount.divide(new BigDecimal(100)).intValue());
		DictRender rd =new DictRender();
		render(dpay,rd);
	}
	
	public static void cancle(Long idcode){
		
		DpayDepositExp dpay=DpayDepositExp.NTget(idcode);
		if(dpay==null){
			 renderText(JSONResult.failure("请求的提案不存在!"));
		}
		
		if(dpay.NTcancle(dpay)){
			renderText(JSONResult.success("操作成功!"));
		}else{
			renderText(JSONResult.failure("操作失败!"));
		}
	}
	
	
	
}
