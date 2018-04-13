package controllers;

import java.io.File;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import models.CashGift;
import models.CreditLog;
import models.Customer;
import models.Deposit;
import models.DepositLog;
import models.DepositRowMap;
import models.DictRender;
import models.Hongbao;
import models.Letter;
import models.Msg;
import models.OrderNo;
import models.Pic;
import models.Sign;
import models.User;
import play.mvc.Controller;
import play.mvc.Http.Response;
import play.mvc.With;
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
public class DepositApp extends Controller{
	
	public static void getList(int start,int limit,int page,String sdate,String edate,String sort,String queryKey)throws Exception{
		Sqler sql =new Sqler("select * from mb_deposit");
		Sqler cntsql =new Sqler("select count(1) from mb_deposit");
		sql.and().left().equals("status", 1).right();
		cntsql.and().left().like("status", 1).right();
		if(!PageUtil.blank(queryKey)){
			sql.and().left().like("login_name", queryKey);
			cntsql.and().left().like("login_name", queryKey);
			
			sql.or().like("dep_no", queryKey);
			cntsql.or().like("dep_no", queryKey);
			
			sql.or().like("real_name", queryKey).right();
			cntsql.or().like("real_name", queryKey).right();
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
		sql.and().addSql("datediff(curdate(),create_date)<90");
		cntsql.and().addSql("datediff(curdate(),create_date)<90");
		sql.orberByDesc("create_date");
		List<Deposit> roleList=Sp.get().getDefaultJdbc().query(PageUtil.page(sql.getSql(),start,limit),sql.getParams().toArray(new Object[0]),new DepositRowMap());
		int count=Sp.get().getDefaultJdbc().queryForObject(cntsql.getSql(),cntsql.getParams().toArray(new Object[0]),Integer.class);
		ExtPage p =new ExtPage();
		p.data=JSONResult.conver(roleList,true);
		p.total=count; 
		renderJSON(p);
	}
	
	public static void getAllList(int start,int limit,int page,String sdate,String edate,String sort,String queryKey)throws Exception{
		Sqler sql =new Sqler("select * from mb_deposit");
		Sqler cntsql =new Sqler("select count(1) from mb_deposit");
		if(!PageUtil.blank(queryKey)){
			sql.and().left().like("login_name", queryKey);
			cntsql.and().left().like("login_name", queryKey);
			
			sql.or().like("dep_no", queryKey);
			cntsql.or().like("dep_no", queryKey);
			
			sql.or().like("bank_name", queryKey);
			cntsql.or().like("bank_name", queryKey);
			
			sql.or().like("real_name", queryKey).right();
			cntsql.or().like("real_name", queryKey).right();
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
		
		if(User.NTcountByRole(session.get(Constant.userName), "F030102")>0){
			sql.and().addSql("datediff(curdate(),create_date)<90");
			cntsql.and().addSql("datediff(curdate(),create_date)<90");
		}else{
			sql.and().addSql("datediff(curdate(),create_date)<30");
			cntsql.and().addSql("datediff(curdate(),create_date)<30");
		}
		
		sql.orberByDesc("create_date");
		List<Deposit> roleList=Sp.get().getDefaultJdbc().query(PageUtil.page(sql.getSql(),start,limit),sql.getParams().toArray(new Object[0]),new DepositRowMap());
		int count=Sp.get().getDefaultJdbc().queryForObject(cntsql.getSql(),cntsql.getParams().toArray(new Object[0]),Integer.class);
		ExtPage p =new ExtPage();
		p.data=JSONResult.conver(roleList,true);
		p.total=count; 
		renderJSON(p);
	}
	public static void saveDeposit(Deposit deposit,File picfile){
		     String user=session.get(Constant.userName);
			 Customer customer=Customer.NTgetCustomerByLoginName(deposit.login_name);
			 if(customer==null){
				renderText(JSONResult.failure(deposit.login_name+"用户名不存在!"));
			 }
			 if(deposit.amount.intValue()>1000000){
				 renderText(JSONResult.failure("存款金额无法大于100万!"));
			 }
			 
			 Date now =new Date(System.currentTimeMillis());
				BigDecimal poundage = new BigDecimal(0);
//				if(now.getTime()>DateUtil.stringToDate("2017-06-12", "yyyy-MM-dd").getTime() &&
//			    		  DateUtil.stringToDate("2017-06-26", "yyyy-MM-dd").getTime()>now.getTime()){
//					 if(deposit.amount.intValue() <=10000){
//						 poundage=new BigDecimal(deposit.amount.intValue()/100.0*3);
//					 }else if(deposit.amount.intValue() > 10000 && deposit.amount.intValue()<=30000){
//						 poundage=new BigDecimal(deposit.amount.intValue()/100.0*3.5);
//					 }else if(deposit.amount.intValue() > 30000 && deposit.amount.intValue()<=50000){
//						 poundage=new BigDecimal(deposit.amount.intValue()/100.0*4);
//					 }else if(deposit.amount.intValue() > 50000 ){
//						 poundage=new BigDecimal(deposit.amount.intValue()/100.0*5);
//					 }
//					 //poundage=new BigDecimal(deposit.amount.intValue()/100.0*1.5);
//				}else{
//					 poundage=new BigDecimal(deposit.amount.intValue()/100.0);
//				}
				
				if(now.getTime()>DateUtil.stringToDate("2017-12-01", "yyyy-MM-dd").getTime() &&
			    		  DateUtil.stringToDate("2018-01-01", "yyyy-MM-dd").getTime()>now.getTime()){
					 if(deposit.amount.intValue()<5000){
						 poundage=new BigDecimal(deposit.amount.intValue()/100.0*1);
					 }else if(deposit.amount.intValue() >= 5000 && deposit.amount.intValue()<10000){
						 poundage=new BigDecimal(deposit.amount.intValue()/100.0*1.8);
					 }else if(deposit.amount.intValue() >= 10000 && deposit.amount.intValue()<30000){
						 poundage=new BigDecimal(deposit.amount.intValue()/100.0*2.5);
					 }else if(deposit.amount.intValue() >= 30000 && deposit.amount.intValue()<50000){
						 poundage=new BigDecimal(deposit.amount.intValue()/100.0*3.8);
					 }else if(deposit.amount.intValue() >= 50000 ){
						 poundage=new BigDecimal(deposit.amount.intValue()/100.0*5);
					 }
				}else{
					 poundage=new BigDecimal(deposit.amount.intValue()/100.0);
				}
				
			// BigDecimal poundage=new BigDecimal(deposit.amount.intValue()/100.0);
			 if(poundage.intValue()>2888)poundage=new BigDecimal(2888);
			 deposit.poundage=poundage;
			 if(deposit.poundage.add(deposit.amount).intValue()==0){
				 renderText(JSONResult.failure("存款金额必须大于0元!"));
			 }
			 if(picfile!=null&&!picfile.getName().endsWith("jpg")){
		        	String errorMsg="提交失败：截图的文件格式必须是jpg格式。";
		        	renderText(JSONResult.failure(errorMsg));
		     }
	        if(picfile!=null&&picfile.length()>200*1024){
	        	String errorMsg="提交失败：截图的文件必须小于200KB。";
	        	renderText(JSONResult.failure(errorMsg));
	        }
	        if(deposit.is_sb!=null&&deposit.is_sb){
	        	if(deposit.sb_game==null||deposit.sb_game.length()<3){
	        		String errorMsg="提交失败：你需要填写有效的申博账户名。";
		        	renderText(JSONResult.failure(errorMsg));
	        	}
	        }
			 String deposit_date=params.get("deposit.deposit_date");
			 deposit.deposit_date=new Date(DateUtil.stringToDate(deposit_date, "yyyy-MM-dd HH:mm").getTime()); 
			 deposit.cust_id=customer.cust_id;
			 deposit.pdage_status=1;
			 deposit.status=1;
			 deposit.dep_no=OrderNo.createLocalNo("DE");
			 deposit.create_user=user;
			 deposit.ip=request.remoteAddress;
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
				 deposit.pic_id=pic_id;
			 }
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
				 Msg.NTcreateMsg(4, "客户"+customer.real_name+"存款"+deposit.amount+"元，请及时处理!");
			 }
		     renderText(JSONResult.success("创建存款提案成功!"));
		
	}
	
	public static void detail(Long request_id){
		Deposit deposit=Deposit.NTget(request_id);
		if(deposit==null){
			 renderText(JSONResult.failure("请求的存款提案不存在!"));
		}
		List<DepositLog> logList=DepositLog.NTgetLogsByDep(request_id);
		DictRender rd =new DictRender();
		render(deposit,logList,rd);
	}
	
	public static void audit(Integer pdage_status,Integer flag,String remarks,Long request_id){
		if(flag==null){
			 renderText(JSONResult.failure("请选择存款是否通过!"));
		}
		if(pdage_status==null){
			 renderText(JSONResult.failure("请选择手续费是否通过!"));
		}
	
		if(pdage_status==-2||flag==-2){
			if(PageUtil.blank(remarks)){
				renderText(JSONResult.failure("请填写存款提案不通过理由!"));
			}
		}
		Deposit deposit=Deposit.NTget(request_id);
		if(deposit==null){
			 renderText(JSONResult.failure("请求的存款提案不存在!"));
		}
		if(deposit.status!=1){
			renderText(JSONResult.failure("存款提案已经被处理,请刷新重试!"));
		}
		int pdage_flag=3;
		int status_flag=3;
		if(pdage_status==-2){pdage_flag=4;}
		if(flag==-2){status_flag=4;}
		
		BigDecimal total =new  BigDecimal(0);
		if(status_flag==3){total=total.add(deposit.amount);};
		String user=session.get(Constant.userName);
		DepositLog.NTcreat(deposit.status,status_flag,deposit.deposit_id, remarks, user,deposit.dep_no);
		Deposit.NTchangeStatus(deposit.deposit_id, status_flag, pdage_flag);
		Date now =new Date(System.currentTimeMillis());
		if(status_flag==3){
			
			Customer customer=Customer.NTgetCustomerByLoginName(deposit.login_name);
			
			     CustomerService.modCredit(deposit.cust_id, CreditLogService.Depoist, deposit.login_name,total, user,"存款", deposit.dep_no);
			    if(Deposit.NTgetCount(deposit.cust_id)==1){ //第一次存款
					Deposit.NTrecAuditDate(deposit.deposit_id);
					
					if(deposit.amount.intValue() >= 100){
						Customer.NTmodCustlevelFirst(deposit.cust_id, 1);
						
						StringBuffer sb =new StringBuffer();
						sb.append("<p>尊敬的客户，您好，恭喜您已经升级为星级一，为了让您有更好的游戏体验，我们为您提供了更优更快的网站线路，请您牢记我们的会员网址：<span>www.268da.com</span> ，祝您生活愉快，盈利多多!</p>");
						sb.append("<p style=\"text-align:right\">八达国际客服部上</p>");
					    Letter.NTcreat(deposit.cust_id, deposit.login_name, "八达国际娱乐城会员网址：www.268da.com", sb.toString(), deposit.login_name, true);
					}
					
				    
			}else{//多次存款,少于100，等级一直是0，如果某次存款达到100以上
				if(customer.cust_level == 0){
					if(deposit.amount.intValue() >= 100){
						Customer.NTmodCustlevelFirst(deposit.cust_id, 1);
						
						StringBuffer sb =new StringBuffer();
						sb.append("<p>尊敬的客户，您好，恭喜您已经升级为星级一，为了让您有更好的游戏体验，我们为您提供了更优更快的网站线路，请您牢记我们的会员网址：<span>www.268da.com</span> ，祝您生活愉快，盈利多多!</p>");
						sb.append("<p style=\"text-align:right\">八达国际客服部上</p>");
					    Letter.NTcreat(deposit.cust_id, deposit.login_name, "八达国际娱乐城会员网址：www.268da.com", sb.toString(), deposit.login_name, true);
					}
				}
			}
			    
			
			int dcount = 0;
			
			
			
			if(customer.promo_flag != null && customer.promo_flag){
				Calendar calendar = Calendar.getInstance();
				if(calendar.getTimeInMillis()<=1441036799979l){ //积分翻倍活动持续到2015年8月31号23点59分59秒
					 CustomerService.modScore(deposit.dep_no, "存款积分(翻倍)", new BigDecimal(deposit.amount.intValue()*2/100.0), deposit.login_name, deposit.cust_id, user);
				}else{
					if(deposit.amount.intValue() >=100){
						CustomerService.modScore(deposit.dep_no, "存款积分", new BigDecimal(deposit.amount.intValue()/100.0), deposit.login_name, deposit.cust_id, user);
					}
					 
				}   
			   
			    dcount=Deposit.NTgetCountToday(deposit.cust_id);
			    if(dcount==1){
			    	if(deposit.amount.intValue() >=100){
			    		CustomerService.modScore(deposit.dep_no, "签到积分", new BigDecimal(1), deposit.login_name, deposit.cust_id, user);
			    	}
			    	 
			    }
			    
			    
			  //2018拆红包
				if((now.getTime()>DateUtil.stringToDate("2018-03-01 10:00:00", "yyyy-MM-dd HH:mm:ss").getTime() &&
						now.getTime()<DateUtil.stringToDate("2018-03-15 18:00:00", "yyyy-MM-dd HH:mm:ss").getTime()) || user.equals("bruce")){
					 /*double []level1 = {1.88,2.88,3.88,4.88,5.88,6.88};
				    	double []level2 = {8.88,9.88,10.88,11.88,12.88,13.88,14.88,15.88};
				    	double []level3 = {16.0,18.0,19.0,20.0,25.0,28.0,30.0,36.0,38.0,40.0,46.0,48.0,50.0};
				    	double []level4 = {56.0,58.0,59.0,60.0,66.0,68.0,69.0,76.0,78.0,79.0,86.0,88.0,89.0,96.0,
				    			98.0,99.0};
				    	
				    	double []level5 = {100.0,105.0,106.0,108.0,109.0,115.0,116.0,118.0,119.0,125.0,126.0,128.0,
				    			129.0,135.0,136.0,138.0,139.0};
				    	
				    	double []level6 = {140.0,146.0,148.0,150.0,156.0,158.0,160.0,166.0,168.0,170.0,176.0,178.0,
				    			180.0,186.0,188.0,190.0,196.0,199.0};
				    	
				    	double []level7 = {200.0,205.0,206.0,208.0,209.0,215.0,216.0,218.0,219.0,225.0,226.0,228.0,
				    			229.0,235.0,236.0,238.0,239.0};
				    	
				    	double []level8 = {300.0,305.0,306.0,308.0,309.0,315.0,316.0,318.0,319.0,325.0,326.0,328.0,
				    			329.0,335.0,336.0,338.0,339.0};
				    	
				    	double []level9 = {400.0,405.0,406.0,408.0,409.0,415.0,416.0,418.0,419.0,425.0,426.0,428.0,
				    			429.0,435.0,436.0,438.0,439.0};
				    	
				    	double []level10 = {500.0,505.0,506.0,508.0,509.0,515.0,516.0,518.0,519.0,525.0,526.0,528.0,
				    			529.0,535.0,536.0,538.0,539.0};
				    	
				    	double []level11 = {600.0,605.0,606.0,608.0,609.0,615.0,616.0,618.0,619.0,625.0,626.0,628.0,
				    			629.0,635.0,636.0,638.0,639.0};
				    	
				    	double []level12 = {700.0,705.0,506.0,508.0,509.0,515.0,516.0,518.0,519.0,525.0,526.0,528.0,
				    			529.0,535.0,536.0,538.0,539.0};
				    	
				    	double []level13 = {800.0,805.0,806.0,808.0,809.0,815.0,816.0,818.0,819.0,825.0,826.0,828.0,
				    			829.0,835.0,836.0,838.0,839.0,888.0};
				    	
				    	double []level14 = {918.0,928.0,958.0,968.0,988.0,999.0,1018.0,1118.0,1558.0,1668.0,1888.0,1988.0,
				    			2108.0,2118.0};*/
					//2018元宵拆宝箱
					String []level1 = {"5","6","7","8","9","10","11","12","13","14","15"};
					String []level2 = {"16","17","18","19","20","21","22","23","24","25"};
			    	String []level3 = {"30","35","36","38","40","45","46","48","50"};
			    	String []level4 = {"60","66","70","76","80","88","90","98","100"};
			    	
			    	String []level5 = {"110","120","130","140","150"};
			    	
			    	String []level6 = {"160","170","180","190","200"};
			    	
			    	String []level7 = {"210","220","230","240","250"};
			    	
			    	String []level8 = {"260","270","280","290","300","310","320","330","340","350"};
			    	
			    	String []level9 = {"360","380","400","420","450","460","480","500"};
			    	
			    	String []level10 = {"550","660","770","880","990","1000"};
			    	
			    	String []level11 = {"10元充值卡"};
			    	
			    	String []level12 = {"20元充值卡"};
			    	
			    	String []level13 = {"30元充值卡"};
			    	
			    	String []level14 = {"50元充值卡"};
			    	
			    	String []level15 = {"100元充值卡"};
			    	
			    	String []level16 = {"200元充值卡"};
			    	
			    	String []level17 = {"500元充值卡"};
			    	
			    	String []level18 = {"2500元以内品牌手机"};
			    	
			    	String []level19 = {"3500元以内品牌手机"};
			    	
			    	String []level20 = {"5000元以内品牌手机"};
			    	
			    	String []level21 = {"iPhone8 64GB手机"};
			    	
			    	
			    	
			    	
			    	
			    	
				    	if(deposit.amount.intValue()>=200 && deposit.amount.intValue() <1000){//level 1
			    			int n = new Random().nextInt(100);
			    			if(n < 80){
			    				int index=(int)(Math.random()*level1.length);
					    		String result = new String(level1[index]);
					    		Hongbao.createHongbao(customer.cust_id, customer.login_name,new BigDecimal(0) ,result, deposit.dep_no+"("+deposit.amount.toString()+")");
			    			}else if(n>=80 && n < 90){
			    				int index=(int)(Math.random()*level11.length);
			    				String result = new String(level11[index]);
					    		Hongbao.createHongbao(customer.cust_id, customer.login_name,new BigDecimal(0) ,result, deposit.dep_no+"("+deposit.amount.toString()+")");
			    			}else{
			    				int index=(int)(Math.random()*level12.length);
			    				String result = new String(level12[index]);
					    		Hongbao.createHongbao(customer.cust_id, customer.login_name,new BigDecimal(0) ,result, deposit.dep_no+"("+deposit.amount.toString()+")");
			    		
			    			}
				    	
				    	}else if(deposit.amount.intValue()>=1000 && deposit.amount.intValue() < 2000){//level 2
				    		int n = new Random().nextInt(100);
			    			if(n < 20){
			    				int index=(int)(Math.random()*level1.length);
					    		String result = new String(level1[index]);
					    		Hongbao.createHongbao(customer.cust_id, customer.login_name,new BigDecimal(0) ,result, deposit.dep_no+"("+deposit.amount.toString()+")");
			    			}else if(n>=20 && n < 70){
			    				int index=(int)(Math.random()*level2.length);
			    				String result = new String(level2[index]);
					    		Hongbao.createHongbao(customer.cust_id, customer.login_name,new BigDecimal(0) ,result, deposit.dep_no+"("+deposit.amount.toString()+")");
			    			}else if(n>=70 && n < 80){
			    				int index=(int)(Math.random()*level11.length);
			    				String result = new String(level11[index]);
					    		Hongbao.createHongbao(customer.cust_id, customer.login_name,new BigDecimal(0) ,result, deposit.dep_no+"("+deposit.amount.toString()+")");
			    		
			    			}else{
			    				int index=(int)(Math.random()*level12.length);
			    				String result = new String(level12[index]);
					    		Hongbao.createHongbao(customer.cust_id, customer.login_name,new BigDecimal(0) ,result, deposit.dep_no+"("+deposit.amount.toString()+")");
			    		
			    			}
				    	}else if(deposit.amount.intValue()>=2000 && deposit.amount.intValue() < 5000){//level 3
				    		int n = new Random().nextInt(100);
			    			if(n < 20){
			    				int index=(int)(Math.random()*level2.length);
					    		String result = new String(level2[index]);
					    		Hongbao.createHongbao(customer.cust_id, customer.login_name,new BigDecimal(0) ,result, deposit.dep_no+"("+deposit.amount.toString()+")");
			    			}else if(n>=20 && n < 40){
			    				int index=(int)(Math.random()*level3.length);
			    				String result = new String(level3[index]);
					    		Hongbao.createHongbao(customer.cust_id, customer.login_name,new BigDecimal(0) ,result, deposit.dep_no+"("+deposit.amount.toString()+")");
			    			}else if(n>=40 && n < 50){
			    				int index=(int)(Math.random()*level4.length);
			    				String result = new String(level4[index]);
					    		Hongbao.createHongbao(customer.cust_id, customer.login_name,new BigDecimal(0) ,result, deposit.dep_no+"("+deposit.amount.toString()+")");
			    		
			    			}else if(n>=50 && n < 70){
			    				int index=(int)(Math.random()*level13.length);
			    				String result = new String(level13[index]);
					    		Hongbao.createHongbao(customer.cust_id, customer.login_name,new BigDecimal(0) ,result, deposit.dep_no+"("+deposit.amount.toString()+")");
			    		
			    			}else if(n>=70 && n < 90){
			    				int index=(int)(Math.random()*level14.length);
			    				String result = new String(level14[index]);
					    		Hongbao.createHongbao(customer.cust_id, customer.login_name,new BigDecimal(0) ,result, deposit.dep_no+"("+deposit.amount.toString()+")");
			    			}else{
			    				int index=(int)(Math.random()*level15.length);
			    				String result = new String(level15[index]);
					    		Hongbao.createHongbao(customer.cust_id, customer.login_name,new BigDecimal(0) ,result, deposit.dep_no+"("+deposit.amount.toString()+")");
			    			}
				    	}else if(deposit.amount.intValue()>=5000 && deposit.amount.intValue() < 10000){//level 4
				    		int n = new Random().nextInt(100);
			    			if(n < 10){
			    				int index=(int)(Math.random()*level4.length);
					    		String result = new String(level4[index]);
					    		Hongbao.createHongbao(customer.cust_id, customer.login_name,new BigDecimal(0) ,result, deposit.dep_no+"("+deposit.amount.toString()+")");
			    			}else if(n>=10 && n < 40){
			    				int index=(int)(Math.random()*level5.length);
			    				String result = new String(level5[index]);
					    		Hongbao.createHongbao(customer.cust_id, customer.login_name,new BigDecimal(0) ,result, deposit.dep_no+"("+deposit.amount.toString()+")");
			    			}else if(n>=40 && n < 60){
			    				int index=(int)(Math.random()*level6.length);
			    				String result = new String(level6[index]);
					    		Hongbao.createHongbao(customer.cust_id, customer.login_name,new BigDecimal(0) ,result, deposit.dep_no+"("+deposit.amount.toString()+")");
			    		
			    			}else if(n>=60 && n < 70){
			    				int index=(int)(Math.random()*level7.length);
			    				String result = new String(level7[index]);
					    		Hongbao.createHongbao(customer.cust_id, customer.login_name,new BigDecimal(0) ,result, deposit.dep_no+"("+deposit.amount.toString()+")");
			    		
			    			}else if(n>=70 && n < 80){
			    				int index=(int)(Math.random()*level15.length);
			    				String result = new String(level15[index]);
					    		Hongbao.createHongbao(customer.cust_id, customer.login_name,new BigDecimal(0) ,result, deposit.dep_no+"("+deposit.amount.toString()+")");
			    			}else{
			    				int index=(int)(Math.random()*level16.length);
			    				String result = new String(level16[index]);
					    		Hongbao.createHongbao(customer.cust_id, customer.login_name,new BigDecimal(0) ,result, deposit.dep_no+"("+deposit.amount.toString()+")");
			    			}
				    	}else if(deposit.amount.intValue()>=10000 && deposit.amount.intValue() < 50000){//level 5
				    		int n = new Random().nextInt(100);
			    			if(n < 25){
			    				int index=(int)(Math.random()*level6.length);
					    		String result = new String(level6[index]);
					    		Hongbao.createHongbao(customer.cust_id, customer.login_name,new BigDecimal(0) ,result, deposit.dep_no+"("+deposit.amount.toString()+")");
			    			}else if(n>=25 && n < 40){
			    				int index=(int)(Math.random()*level7.length);
			    				String result = new String(level7[index]);
					    		Hongbao.createHongbao(customer.cust_id, customer.login_name,new BigDecimal(0) ,result, deposit.dep_no+"("+deposit.amount.toString()+")");
			    			}else if(n>=40 && n < 55){
			    				int index=(int)(Math.random()*level8.length);
			    				String result = new String(level8[index]);
					    		Hongbao.createHongbao(customer.cust_id, customer.login_name,new BigDecimal(0) ,result, deposit.dep_no+"("+deposit.amount.toString()+")");
			    		
			    			}else if(n>=55 && n < 65){
			    				int index=(int)(Math.random()*level9.length);
			    				String result = new String(level9[index]);
					    		Hongbao.createHongbao(customer.cust_id, customer.login_name,new BigDecimal(0) ,result, deposit.dep_no+"("+deposit.amount.toString()+")");
			    		
			    			}else if(n>=65 && n < 90){
			    				int index=(int)(Math.random()*level16.length);
			    				String result = new String(level16[index]);
					    		Hongbao.createHongbao(customer.cust_id, customer.login_name,new BigDecimal(0) ,result, deposit.dep_no+"("+deposit.amount.toString()+")");
			    			}else{
			    				int index=(int)(Math.random()*level17.length);
			    				String result = new String(level17[index]);
					    		Hongbao.createHongbao(customer.cust_id, customer.login_name,new BigDecimal(0) ,result, deposit.dep_no+"("+deposit.amount.toString()+")");
			    			}
				    	}else if(deposit.amount.intValue()>=50000 && deposit.amount.intValue() < 100000){//level 6
				    		int n = new Random().nextInt(100);
			    			if(n < 10){
			    				int index=(int)(Math.random()*level8.length);
					    		String result = new String(level8[index]);
					    		Hongbao.createHongbao(customer.cust_id, customer.login_name,new BigDecimal(0) ,result, deposit.dep_no+"("+deposit.amount.toString()+")");
			    			}else if(n>=10 && n < 40){
			    				int index=(int)(Math.random()*level9.length);
			    				String result = new String(level9[index]);
					    		Hongbao.createHongbao(customer.cust_id, customer.login_name,new BigDecimal(0) ,result, deposit.dep_no+"("+deposit.amount.toString()+")");
			    			}else if(n>=40 && n < 70){
			    				int index=(int)(Math.random()*level10.length);
			    				String result = new String(level10[index]);
					    		Hongbao.createHongbao(customer.cust_id, customer.login_name,new BigDecimal(0) ,result, deposit.dep_no+"("+deposit.amount.toString()+")");
			    		
			    			}else if(n>=70 && n < 80){
			    				
			    				int index=(int)(Math.random()*level17.length);
			    				String result = new String(level17[index]);
					    		Hongbao.createHongbao(customer.cust_id, customer.login_name,new BigDecimal(0) ,result, deposit.dep_no+"("+deposit.amount.toString()+")");
			    			}else{
			    				int index=(int)(Math.random()*level18.length);
			    				String result = new String(level18[index]);
					    		Hongbao.createHongbao(customer.cust_id, customer.login_name,new BigDecimal(0) ,result, deposit.dep_no+"("+deposit.amount.toString()+")");
			    			}
				    	}else if(deposit.amount.intValue()>=100000){//level 7
				    		int n = new Random().nextInt(100);
			    			if(n < 10){
			    				int index=(int)(Math.random()*level9.length);
					    		String result = new String(level9[index]);
					    		Hongbao.createHongbao(customer.cust_id, customer.login_name,new BigDecimal(0) ,result, deposit.dep_no+"("+deposit.amount.toString()+")");
			    			}else if(n>=10 && n < 40){
			    				int index=(int)(Math.random()*level10.length);
			    				String result = new String(level10[index]);
					    		Hongbao.createHongbao(customer.cust_id, customer.login_name,new BigDecimal(0) ,result, deposit.dep_no+"("+deposit.amount.toString()+")");
			    			}else if(n>=40 && n < 50){
			    				int index=(int)(Math.random()*level17.length);
			    				String result = new String(level17[index]);
					    		Hongbao.createHongbao(customer.cust_id, customer.login_name,new BigDecimal(0) ,result, deposit.dep_no+"("+deposit.amount.toString()+")");
			    		
			    			}else if(n>=50 && n < 65){
			    				
			    				int index=(int)(Math.random()*level18.length);
			    				String result = new String(level18[index]);
					    		Hongbao.createHongbao(customer.cust_id, customer.login_name,new BigDecimal(0) ,result, deposit.dep_no+"("+deposit.amount.toString()+")");
			    			}else if(n>=65 && n < 80){
			    				
			    				int index=(int)(Math.random()*level19.length);
			    				String result = new String(level19[index]);
					    		Hongbao.createHongbao(customer.cust_id, customer.login_name,new BigDecimal(0) ,result, deposit.dep_no+"("+deposit.amount.toString()+")");
			    			}else if(n>=80 && n < 90){
			    				
			    				int index=(int)(Math.random()*level20.length);
			    				String result = new String(level20[index]);
					    		Hongbao.createHongbao(customer.cust_id, customer.login_name,new BigDecimal(0) ,result, deposit.dep_no+"("+deposit.amount.toString()+")");
			    			}else{
			    				int index=(int)(Math.random()*level21.length);
			    				String result = new String(level21[index]);
					    		Hongbao.createHongbao(customer.cust_id, customer.login_name,new BigDecimal(0) ,result, deposit.dep_no+"("+deposit.amount.toString()+")");
			    			}
				}
			   
		    	
				}	
				
		    	
		    	
		    	
		    	
			    
//			    if(deposit.amount.doubleValue()>= 300 && Sign.NTgetTodayCount(deposit.login_name)==0  ){
//			    	Sign sign = new Sign();
//			    	
//			    	sign.cust_id = customer.cust_id;
//			    	sign.login_name = customer.login_name;
//			    	sign.sign_date = deposit.create_date;
//			    	sign.NTcreat();
//			    }
			    
			    
			    
			    
//			    if((now.getTime()>DateUtil.stringToDate("2017-04-10", "yyyy-MM-dd").getTime()&&
//			    		  DateUtil.stringToDate("2017-04-24", "yyyy-MM-dd").getTime()>now.getTime())){
//						 //dcount=Deposit.NTgetCountToday(deposit.cust_id);
//						    //Customer customer=Customer.NTgetCustomerByLoginName(deposit.login_name);
//							    // if(dcount<=5){
//								     String giftCode=MyRandom.getRandom(8);
//									 CashGift gift =new CashGift();
//						        	 gift.gift_code=giftCode;
//						        	 gift.login_name=customer.login_name;
//						        	 gift.deposit_credit=deposit.amount;
//						        	 gift.valid_credit=new BigDecimal(0);
//						        	 gift.net_credit=new BigDecimal(0);
//						        	 gift.rate=Float.valueOf(3);
//						        	 Integer rate = 3;
//						        	 float f=deposit.amount.floatValue()*3/100;
////						        	 if(dcount==5){
////						        		 gift.rate=Float.valueOf(3);
////						        		 f=deposit.amount.floatValue()*3/100;
////						        		 rate = 3;
////						        	 }
////						        	 
////						        	 if(f>1111){
////						        		 f=1111;
////						        	 }
//						        	 gift.payout=new BigDecimal(f);
//						        	 gift.gift_type="存送3%";
//						        	 gift.status=1;
//						        	 gift.gift_no=OrderNo.createLocalNo("GI");
//						     		 gift.cust_id=customer.cust_id;
//						        	 gift.cs_date=new Date(System.currentTimeMillis());
//						        	 gift.real_name=customer.real_name;
//						        	 gift.cust_level=customer.cust_level;
//						           	 gift.kh_date=customer.create_date;
//						        	 gift.create_user=user;
//						        	 gift.create_date=new Date(System.currentTimeMillis());
//						        	 Long giftId=gift.NTcreat();
//						        	 gift.NTAuditGift(giftId, 3, user,"系统审核通过-"+deposit.dep_no+" |"+rate);
//						        	 CustomerService.modCredit(customer.cust_id,CreditLogService.Gift, 
//						 					gift.login_name,gift.payout,user, "添加礼金"+gift.gift_type+" |"+rate+"%", gift.gift_no);
//							    // }
//							     
//					 }
			      
			      
			}
			
		    
		    
	       	 /**5连礼---start--*/
		      
//		      if((now.getTime()>DateUtil.stringToDate("2015-04-22", "yyyy-MM-dd").getTime()&&
//						DateUtil.stringToDate("2015-04-29", "yyyy-MM-dd").getTime()>now.getTime())){
//						    dcount=Deposit.NTgetCountMoreThan500Today(deposit.cust_id);
//						   // Customer customer=Customer.NTgetCustomerByLoginName(deposit.login_name);
//							     if(dcount<=5&&deposit.amount.intValue()>=500){
//								     String giftCode=MyRandom.getRandom(8);
//									 CashGift gift =new CashGift();
//						        	 gift.gift_code=giftCode;
//						        	 gift.login_name=customer.login_name;
//						        	 gift.deposit_credit=deposit.amount;
//						        	 gift.valid_credit=new BigDecimal(0);
//						        	 gift.net_credit=new BigDecimal(0);
//						        	 gift.rate=Float.valueOf(dcount);
//						        	 float f=deposit.amount.floatValue()*dcount/100;
//						        	 if(f>1800){
//						        		 f=1800;
//						        	 }
//						        	 gift.payout=new BigDecimal(f);
//						        	 gift.gift_type="充值5连礼";
//						        	 gift.status=1;
//						        	 gift.gift_no=OrderNo.createLocalNo("GI");
//						     		 gift.cust_id=customer.cust_id;
//						        	 gift.cs_date=new Date(System.currentTimeMillis());
//						        	 gift.real_name=customer.real_name;
//						        	 gift.cust_level=customer.cust_level;
//						           	 gift.kh_date=customer.create_date;
//						        	 gift.create_user=user;
//						        	 gift.create_date=new Date(System.currentTimeMillis());
//						        	 Long giftId=gift.NTcreat();
//						        	 gift.NTAuditGift(giftId, 3, user,"系统审核通过-"+deposit.dep_no+" |"+dcount);
//						        	 CustomerService.modCredit(customer.cust_id,CreditLogService.Gift, 
//						 					gift.login_name,gift.payout,user, "添加礼金"+gift.gift_type+" |"+dcount+"%", gift.gift_no);
//							     }
//		      }
			     /**5连礼---end---**/
		      
		      
		      
				 
				 
				 
		     
		    
		}else{
			//发送站内信
			StringBuffer sb =new StringBuffer();
			sb.append("<p>尊敬的客户，您有一笔存款没有处理成功。存款单号为"+deposit.dep_no+"，提交时间："+DateUtil.dateToString(deposit.create_date, "yyyy-MM-dd HH:mm:ss")+"，金额为"+deposit.amount+"，原因是："+remarks+"</p>");
			sb.append("<p style=\"text-align:right\">八达国际客服部上</p>");
		    Letter.NTcreat(deposit.cust_id, deposit.login_name, "存款金额为"+deposit.amount+"的存款未处理成功", sb.toString(), deposit.login_name, true);
			
		}
		//存款红利
		if(pdage_flag==3){
			 String giftCode=MyRandom.getRandom(8);
			 Customer customer=Customer.NTgetCustomerByLoginName(deposit.login_name);
			 if(customer.promo_flag != null && customer.promo_flag){
				 CashGift gift =new CashGift();
	        	 gift.gift_code=giftCode;
	        	 gift.login_name=customer.login_name;
	        	 gift.deposit_credit=deposit.amount;
	        	 gift.valid_credit=new BigDecimal(0);
	        	 gift.net_credit=new BigDecimal(0);
	        	 gift.rate = Float.valueOf(0);
	        	 if(now.getTime()>DateUtil.stringToDate("2017-12-01", "yyyy-MM-dd").getTime() &&
			    		  DateUtil.stringToDate("2018-01-01", "yyyy-MM-dd").getTime()>now.getTime()){
	        		 if(deposit.amount.intValue() <5000){
	        			 gift.rate=Float.valueOf(1); 
					 }else if(deposit.amount.intValue() >=5000 && deposit.amount.intValue() <10000){
						 gift.rate=Float.valueOf(1.8f); 
					 }else if(deposit.amount.intValue() >=10000 && deposit.amount.intValue() <30000){
						 gift.rate=Float.valueOf(2.5f); 
					 }else if(deposit.amount.intValue() >=30000 && deposit.amount.intValue() <50000){
						 gift.rate=Float.valueOf(3.8f); 
					 }else if(deposit.amount.intValue() >=50000){
						 gift.rate=Float.valueOf(5f); 
					 }
	        		 
	        		 gift.gift_type="年终现金大回馈";
	        		 
	        	 }else{
	        		 gift.rate=Float.valueOf(1); 
	        		 gift.gift_type="转账红利";
	        	 }
	        	// gift.rate=Float.valueOf(1);
	        	 gift.payout=deposit.poundage;
	        	 
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
		renderText(JSONResult.success("操作成功!"));
		}

	public static void cancle(String remarks,Long request_id){
		String user=session.get(Constant.userName);
		Deposit deposit=Deposit.NTget(request_id);
		if(deposit==null){
			 renderText(JSONResult.failure("请求的存款提案不存在!"));
		}
		if(deposit.status!=1){
			renderText(JSONResult.failure("该存款提案已经被处理，请刷新查看!"));
		}
		DepositLog.NTcreat(deposit.status, 2, deposit.deposit_id, remarks, user,deposit.dep_no);
		Deposit.NTchangeStatus(deposit.deposit_id, 2, 2);
		
		//发送站内信
		StringBuffer sb =new StringBuffer();
		sb.append("<p>尊敬的客户，您有一笔存款没有处理成功。存款单号为"+deposit.dep_no+"，提交时间："+DateUtil.dateToString(deposit.create_date, "yyyy-MM-dd HH:mm:ss")+"，金额为"+deposit.amount+"，原因是："+remarks+"</p>");
		sb.append("<p style=\"text-align:right\">八达国际客服部上</p>");
	    Letter.NTcreat(deposit.cust_id, deposit.login_name, "存款金额为"+deposit.amount+"的存款未处理成功", sb.toString(), deposit.login_name, true);
	    
		renderText(JSONResult.success("操作成功!"));
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
	
	public static void exportExcel(String sdate,String edate,String queryKey){
		Sqler sql =new Sqler("select * from mb_deposit");
		Sqler cntsql =new Sqler("select count(1) from mb_deposit");
		if(!PageUtil.blank(queryKey)){
			sql.and().left().like("login_name", queryKey);
			cntsql.and().left().like("login_name", queryKey);
			
			sql.or().like("dep_no", queryKey);
			cntsql.or().like("dep_no", queryKey);
			
			sql.or().like("bank_name", queryKey);
			cntsql.or().like("bank_name", queryKey);
			
			sql.or().like("real_name", queryKey).right();
			cntsql.or().like("real_name", queryKey).right();
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
		sql.orderBy(" bank_name desc,status desc,create_date desc");
		int count=Sp.get().getDefaultJdbc().queryForObject(cntsql.getSql(),cntsql.getParams().toArray(new Object[0]),Integer.class);
		if(count>50000){
			renderText("导出记录超过5万条，系统仅支持导出小于5万条记录,请缩短范围再导出。");
		}
        System.out.println(count);
		DictRender rd=new DictRender();
		try{
		List<Deposit> depositList=Sp.get().getDefaultJdbc().query(sql.getSql(),sql.getParams().toArray(new Object[0]),new DepositRowMap());
		WorkbookSettings ws = new WorkbookSettings();
		ws.setLocale(new Locale("cn", "ZH"));
		WritableWorkbook workbook = Workbook.createWorkbook(response.out);
		WritableSheet sheet = workbook.createSheet("存款订单", 0);
		response.setHeader("content-disposition", "attachment; filename=de_"+sdate+"_"+edate+".xls");
		response.setHeader("Content-Type", "application/excel");
		sheet.addCell(new Label(0, 0,"单号"));
		sheet.addCell(new Label(1, 0,"第三方单号"));
        sheet.addCell(new Label(2, 0,"创建时间"));
        sheet.addCell(new Label(3, 0,"用户名"));
        sheet.addCell(new Label(4, 0,"真实姓名"));
        sheet.addCell(new Label(5, 0,"存款金额"));
        sheet.addCell(new Label(6, 0,"红利金额"));
        sheet.addCell(new Label(7, 0,"红利状态"));
        sheet.addCell(new Label(8, 0,"存款银行"));
        sheet.addCell(new Label(9, 0,"存款方式"));
        sheet.addCell(new Label(10, 0,"收款人"));
        sheet.addCell(new Label(11, 0,"提交人"));
        sheet.addCell(new Label(12, 0,"状态"));
        sheet.addCell(new Label(13, 0,"操作人"));
        sheet.addCell(new Label(14, 0,"操作时间"));
        sheet.addCell(new Label(15, 0,"操作耗时"));
        sheet.addCell(new Label(16, 0,"操作备注"));
        //
        for(int i=0;i<depositList.size();i++){
        	  Deposit r=depositList.get(i);
        	  sheet.addCell(new Label(0, i+1,r.dep_no));
              sheet.addCell(new Label(1, i+1,r.order_no));
              sheet.addCell(new Label(2, i+1,DateUtil.dateToString(r.create_date, "yyyy-MM-dd HH:mm:ss")));
              sheet.addCell(new Label(3, i+1,r.login_name));
              sheet.addCell(new Label(4, i+1,r.real_name));
              sheet.addCell(new Label(5, i+1,r.amount.toPlainString()));
              sheet.addCell(new Label(6, i+1,r.poundage.toPlainString()));
              sheet.addCell(new Label(7, i+1,rd.dictName("pdage_flag", r.pdage_status)));
              sheet.addCell(new Label(8, i+1,r.bank_name));
              sheet.addCell(new Label(9, i+1,r.deposit_type));
              sheet.addCell(new Label(10,i+1,r.account_name));
              sheet.addCell(new Label(11,i+1,r.create_user));
              sheet.addCell(new Label(12,i+1,rd.dictName("deposit_flag", r.status)));
      		  List<DepositLog> logList=DepositLog.NTgetLogsByDep(r.deposit_id);
              for(int k=0;k<logList.size();k++){
            	    DepositLog log =logList.get(k);
            	    if(log.after_status==2||log.after_status==3||log.after_status==4){
	            	    sheet.addCell(new Label(13, i+1,log.create_user));
	                    sheet.addCell(new Label(14, i+1,rd.date(log.create_date,"yyyy-MM-dd HH:mm:ss")));
	                    sheet.addCell(new Label(15, i+1,""+(log.create_date.getTime()-r.create_date.getTime())/1000));
	                    sheet.addCell(new Label(16, i+1,log.remarks));
            	    }
              }
        }
        workbook.write();
	    workbook.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static void main(String[] args){
		
		Map<String,Integer> map =new HashMap<String,Integer>();
    	
    	for(int i=0;i<10000000;i++){
    		String mmm = test(100000);
    		if(map.containsKey(mmm)){
    			map.put(mmm, map.get(mmm)+1);
    		}else{
    			map.put(mmm, 1);
    			
    		}
    	}
    	
    	for (String key : map.keySet()) {
    		   System.out.println("key= "+ key + " and value= " + ((float)map.get(key))/100000 );
      }

		
	}
	
		public static String test(int money){

			String []level1 = {"5","6","7","8","9","10","11","12","13","14","15"};
			String []level2 = {"16","17","18","19","20","21","22","23","24","25"};
	    	String []level3 = {"30","35","36","38","40","45","46","48","50"};
	    	String []level4 = {"60","66","70","76","80","88","90","98","100"};
	    	
	    	String []level5 = {"110","120","130","140","150"};
	    	
	    	String []level6 = {"160","170","180","190","200"};
	    	
	    	String []level7 = {"210","220","230","240","250"};
	    	
	    	String []level8 = {"260","270","280","290","300","310","320","330","340","350"};
	    	
	    	String []level9 = {"360","380","400","420","450","460","480","500"};
	    	
	    	String []level10 = {"550","660","770","880","990","1000"};
	    	
	    	String []level11 = {"10元充值卡"};
	    	
	    	String []level12 = {"20元充值卡"};
	    	
	    	String []level13 = {"30元充值卡"};
	    	
	    	String []level14 = {"50元充值卡"};
	    	
	    	String []level15 = {"100元充值卡"};
	    	
	    	String []level16 = {"200元充值卡"};
	    	
	    	String []level17 = {"500元充值卡"};
	    	
	    	String []level18 = {"2500元以内品牌手机"};
	    	
	    	String []level19 = {"3500元以内品牌手机"};
	    	
	    	String []level20 = {"5000元以内品牌手机"};
	    	
	    	String []level21 = {"iPhone8 64GB手机"};
	    	
	    	String result= "";
		    	if(money>=200 && money <1000){//level 1
	    			int n = new Random().nextInt(100);
	    			if(n < 80){
	    				int index=(int)(Math.random()*level1.length);
			    		 result = new String(level1[index]);
	    			}else if(n>=80 && n < 90){
	    				int index=(int)(Math.random()*level11.length);
	    				 result = new String(level11[index]);
	    			}else{
	    				int index=(int)(Math.random()*level12.length);
	    				 result = new String(level12[index]);
	    		
	    			}
		    	
		    	}else if(money>=1000 && money < 2000){//level 2
		    		int n = new Random().nextInt(100);
	    			if(n < 20){
	    				int index=(int)(Math.random()*level1.length);
			    		 result = new String(level1[index]);
	    			}else if(n>=20 && n < 70){
	    				int index=(int)(Math.random()*level2.length);
	    				 result = new String(level2[index]);
	    			}else if(n>=70 && n < 80){
	    				int index=(int)(Math.random()*level11.length);
	    				 result = new String(level11[index]);
	    		
	    			}else{
	    				int index=(int)(Math.random()*level12.length);
	    				 result = new String(level12[index]);
	    		
	    			}
		    	}else if(money>=2000 && money < 5000){//level 3
		    		int n = new Random().nextInt(100);
	    			if(n < 20){
	    				int index=(int)(Math.random()*level2.length);
			    		 result = new String(level2[index]);
	    			}else if(n>=20 && n < 40){
	    				int index=(int)(Math.random()*level3.length);
	    				 result = new String(level3[index]);
	    			}else if(n>=40 && n < 50){
	    				int index=(int)(Math.random()*level4.length);
	    				 result = new String(level4[index]);
	    		
	    			}else if(n>=50 && n < 70){
	    				int index=(int)(Math.random()*level13.length);
	    				 result = new String(level13[index]);
	    		
	    			}else if(n>=70 && n < 90){
	    				int index=(int)(Math.random()*level14.length);
	    				 result = new String(level14[index]);
	    			}else{
	    				int index=(int)(Math.random()*level15.length);
	    				 result = new String(level15[index]);
	    			}
		    	}else if(money>=5000 && money < 10000){//level 4
		    		int n = new Random().nextInt(100);
	    			if(n < 10){
	    				int index=(int)(Math.random()*level4.length);
			    		 result = new String(level4[index]);
	    			}else if(n>=10 && n < 40){
	    				int index=(int)(Math.random()*level5.length);
	    				 result = new String(level5[index]);
	    			}else if(n>=40 && n < 60){
	    				int index=(int)(Math.random()*level6.length);
	    				 result = new String(level6[index]);
	    		
	    			}else if(n>=60 && n < 70){
	    				int index=(int)(Math.random()*level7.length);
	    				 result = new String(level7[index]);
	    		
	    			}else if(n>=70 && n < 80){
	    				int index=(int)(Math.random()*level15.length);
	    				 result = new String(level15[index]);
	    			}else{
	    				int index=(int)(Math.random()*level16.length);
	    				 result = new String(level16[index]);
	    			}
		    	}else if(money>=10000 && money < 50000){//level 5
		    		int n = new Random().nextInt(100);
	    			if(n < 25){
	    				int index=(int)(Math.random()*level6.length);
			    		 result = new String(level6[index]);
	    			}else if(n>=25 && n < 40){
	    				int index=(int)(Math.random()*level7.length);
	    				 result = new String(level7[index]);
	    			}else if(n>=40 && n < 55){
	    				int index=(int)(Math.random()*level8.length);
	    				 result = new String(level8[index]);
	    		
	    			}else if(n>=55 && n < 65){
	    				int index=(int)(Math.random()*level9.length);
	    				 result = new String(level9[index]);
	    		
	    			}else if(n>=65 && n < 90){
	    				int index=(int)(Math.random()*level16.length);
	    				 result = new String(level16[index]);
	    			}else{
	    				int index=(int)(Math.random()*level17.length);
	    				 result = new String(level17[index]);
	    			}
		    	}else if(money>=50000 && money < 100000){//level 6
		    		int n = new Random().nextInt(100);
	    			if(n < 10){
	    				int index=(int)(Math.random()*level8.length);
			    		 result = new String(level8[index]);
	    			}else if(n>=10 && n < 40){
	    				int index=(int)(Math.random()*level9.length);
	    				 result = new String(level9[index]);
	    			}else if(n>=40 && n < 70){
	    				int index=(int)(Math.random()*level10.length);
	    				 result = new String(level10[index]);
	    		
	    			}else if(n>=70 && n < 80){
	    				
	    				int index=(int)(Math.random()*level17.length);
	    				 result = new String(level17[index]);
	    			}else{
	    				int index=(int)(Math.random()*level18.length);
	    				 result = new String(level18[index]);
	    			}
		    	}else if(money>=100000){//level 7
		    		int n = new Random().nextInt(100);
	    			if(n < 10){
	    				int index=(int)(Math.random()*level9.length);
			    		 result = new String(level9[index]);
	    			}else if(n>=10 && n < 40){
	    				int index=(int)(Math.random()*level10.length);
	    				 result = new String(level10[index]);
	    			}else if(n>=40 && n < 50){
	    				int index=(int)(Math.random()*level17.length);
	    				 result = new String(level17[index]);
	    		
	    			}else if(n>=50 && n < 65){
	    				
	    				int index=(int)(Math.random()*level18.length);
	    				 result = new String(level18[index]);
	    			}else if(n>=65 && n < 80){
	    				
	    				int index=(int)(Math.random()*level19.length);
	    				 result = new String(level19[index]);
	    			}else if(n>=80 && n < 90){
	    				
	    				int index=(int)(Math.random()*level20.length);
	    				 result = new String(level20[index]);
	    			}else{
	    				int index=(int)(Math.random()*level21.length);
	    				 result = new String(level21[index]);
	    			}
	    			
		    	}
		    	return result;
		}
	}
