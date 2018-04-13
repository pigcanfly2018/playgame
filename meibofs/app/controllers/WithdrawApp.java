package controllers;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import models.Customer;
import models.CustomerStatus;
import models.DictRender;
import models.Letter;
import models.Msg;
import models.OrderNo;
import models.User;
import models.Withdraw;
import models.WithdrawLog;
import models.WithdrawRowMap;

import org.apache.log4j.Logger;

import play.mvc.Controller;
import play.mvc.With;
import service.CreditLogService;
import service.CustomerService;
import util.Constant;
import util.DateUtil;
import util.ExtPage;
import util.JSONResult;
import util.PageUtil;
import util.Sp;
import util.Sqler;
import bsz.exch.service.Plat;

import com.ws.service.PlatService;
@With(value = {AjaxSecure.class})
public class WithdrawApp extends Controller{
	
	private static Logger logger=Logger.getLogger(WithdrawApp.class);
	public static void getList(int start,int limit,int page,String sdate,String edate,String sort,String queryKey)throws Exception{
		Sqler sql =new Sqler("select * from mb_withdraw");
		Sqler cntsql =new Sqler("select count(1) from mb_withdraw");
		sql.and().left().equals("status", 1).or().equals("status", 3).right();
		cntsql.and().left().like("status", 1).or().equals("status", 3).right();
		if(!PageUtil.blank(queryKey)){
			sql.and().left().like("login_name", queryKey);
			cntsql.and().left().like("login_name", queryKey);
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
		List<Withdraw> roleList=Sp.get().getDefaultJdbc().query(PageUtil.page(sql.getSql(),start,limit),sql.getParams().toArray(new Object[0]),new WithdrawRowMap());
		for(Withdraw withdraw:roleList){
			withdraw.cust_level = Customer.getCustomer(withdraw.login_name).cust_level;
		}
		int count=Sp.get().getDefaultJdbc().queryForObject(cntsql.getSql(),cntsql.getParams().toArray(new Object[0]),Integer.class);
		ExtPage p =new ExtPage();
		p.data=JSONResult.conver(roleList,true);
		p.total=count; 
		renderJSON(p);
	}
	public static void getAllList(int start,int limit,int page,String sdate,String edate,String sort,String queryKey)throws Exception{
		Sqler sql =new Sqler("select * from mb_withdraw");
		Sqler cntsql =new Sqler("select count(1) from mb_withdraw");
		if(!PageUtil.blank(queryKey)){
			sql.and().left().equals("login_name", queryKey);
			cntsql.and().left().equals("login_name", queryKey);
    		sql.or().equals("wit_no", queryKey);
			cntsql.or().equals("wit_no", queryKey);
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
		
		if(User.NTcountByRole(session.get(Constant.userName), "F030202")>0){
			//sql.and().addSql("datediff(curdate(),create_date)<90");
			//cntsql.and().addSql("datediff(curdate(),create_date)<90");
		}else{
			sql.and().addSql("datediff(curdate(),create_date)<30");
			cntsql.and().addSql("datediff(curdate(),create_date)<30");
		}
		sql.orberByDesc("create_date");
		List<Withdraw> roleList=Sp.get().getDefaultJdbc().query(PageUtil.page(sql.getSql(),start,limit),sql.getParams().toArray(new Object[0]),new WithdrawRowMap());
		int count=Sp.get().getDefaultJdbc().queryForObject(cntsql.getSql(),cntsql.getParams().toArray(new Object[0]),Integer.class);
		ExtPage p =new ExtPage();
		p.data=JSONResult.conver(roleList,true);
		p.total=count; 
		renderJSON(p);
	}
	public static void saveWithdraw(Withdraw withdraw){
		if(withdraw.amount.intValue()<100){
			renderText(JSONResult.failure("提款金额最少为100元!"));
		}
		Customer cust=Customer.NTgetCustomerByLoginName(withdraw.login_name);
		if(cust==null){
			renderText(JSONResult.failure(withdraw.login_name+"用户名不存在!"));
		}
		withdraw.wit_cnt=Withdraw.NTgetCount(cust.cust_id);
		String ip=IpApp.getIpAddr();
		if(cust.credit==null)cust.credit=new BigDecimal(0);
		
		
		CustomerStatus cs =  CustomerStatus.NTgetCustomerByName(cust.login_name);

		BigDecimal aginCredit=PlatService.balance(Plat.AG, cust.login_name, ip);
		BigDecimal bbinCredit=PlatService.balance(Plat.BBIN, cust.login_name, ip);
		BigDecimal ptCredit=PlatService.balance(Plat.PT, cust.login_name, ip);
		BigDecimal kgCredit=PlatService.balance(Plat.KG, cust.login_name, ip);
		BigDecimal mgCredit=PlatService.balance(Plat.MG, cust.login_name, ip);
		BigDecimal sbCredit=PlatService.balance(Plat.VS, cust.login_name, ip);
		
		BigDecimal totalCredit=cust.credit.add(aginCredit).add(bbinCredit).add(ptCredit).add(kgCredit).add(mgCredit).add(sbCredit);
		
		if(totalCredit.floatValue()<withdraw.amount.floatValue()){
			renderText(JSONResult.failure("客户余额不足，客户余额为:"+totalCredit+"。"));
		}
		String user=session.get(Constant.userName);
		withdraw.wit_no=OrderNo.createLocalNo("WI");
		withdraw.status=1;
		withdraw.real_name=cust.real_name;
		withdraw.cust_id=cust.cust_id;
		withdraw.create_date=new Date(System.currentTimeMillis());
		withdraw.create_user=user;
		
		PlatService.transferOutAll(cust.login_name, ip, "提款", "", user, withdraw.wit_no, null);
		
		cust=Customer.NTgetCustomerByLoginName(withdraw.login_name);
		if(cust.credit.floatValue()<withdraw.amount.floatValue()){
			renderText(JSONResult.failure("客户本地余额不足，客户本地余额为:"+cust.credit+"。"));
		}
		if(CustomerService.modCredit(cust.cust_id,CreditLogService.Withdraw, withdraw.login_name,
				new BigDecimal(0).subtract(withdraw.amount),
				user, "客户提款", withdraw.wit_no)){
			try{
				Long withdraw_id=withdraw.NTcreat();
				WithdrawLog.NTcreat(0, 1, withdraw_id, withdraw.remarks, user);
				 Msg.NTcreateMsg(5, "客户"+cust.real_name+"提款"+withdraw.amount+"元，请及时处理!");
			     renderText(JSONResult.success("创建提案成功!"));
			}catch(Exception e){
				System.out.println("创建提款提案失败!");
			}
			renderText(JSONResult.failure("创建提款提案失败。"));
		}
		renderText(JSONResult.failure("创建提款提案失败，扣款不成功。"));

	
	}
	public static void detail(Long request_id){
		Withdraw withdraw=Withdraw.NTget(request_id);
		if(withdraw==null){
			 renderText(JSONResult.failure("请求的提款提案不存在!"));
		}
		List<WithdrawLog> logList=WithdrawLog.NTgetLogsByWit(withdraw.withdraw_id);
		DictRender rd=new DictRender();
		render(withdraw,logList,rd);
	}
	
	public static void cancle(Long request_id,String remarks){
		Withdraw withdraw=Withdraw.NTget(request_id);
		if(withdraw==null){
			 renderText(JSONResult.failure("请求的提款提案不存在!"));
		}
		if(withdraw.locked){
			renderText(JSONResult.failure("该提案已经被锁，您目前无法操作该提案!"));
		}
		if(withdraw.status!=1){
			 renderText(JSONResult.failure("操作失败，该提案已经被处理!"));
		}
		Customer cust=Customer.NTgetCustomer(withdraw.cust_id);
		if(cust==null){
			 renderText(JSONResult.failure("提案的客户不存在!"));
		}
		String user=session.get(Constant.userName);
	
		if(CustomerService.modCredit(cust.cust_id,CreditLogService.Withdraw, 
				withdraw.login_name,withdraw.amount,user, "取消提款", withdraw.wit_no)){
			WithdrawLog.NTcreat(withdraw.status, 2, withdraw.withdraw_id, remarks, user);
			Withdraw.NTchangeStatus(withdraw.withdraw_id, 2);
			
			//提款不成功 发送站内信
			StringBuffer sb =new StringBuffer();
			sb.append("<p>尊敬的客户，您有一笔提款没有处理成功。提款单号为"+withdraw.wit_no+"，提交时间："+DateUtil.dateToString(withdraw.create_date, "yyyy-MM-dd HH:mm:ss")+"，金额为"+withdraw.amount+"，原因是："+remarks+"</p>");
			sb.append("<p style=\"text-align:right\">八达国际客服部上</p>");
		    Letter.NTcreat(withdraw.cust_id, withdraw.login_name, "提款金额为"+withdraw.amount+"的提款未处理成功", sb.toString(), withdraw.login_name, true);
	
		    
			renderText(JSONResult.success("取消提案成功!"));
		}
		 renderText(JSONResult.failure("操作失败!"));
	
	}
	
	/**
	 * 审核(通过与不通过)
	 * @param request_id
	 * @param remarks
	 */
	public static void audit(Integer flag,String remarks,Long request_id){
		Withdraw withdraw=Withdraw.NTget(request_id);
		if(withdraw==null){
			 renderText(JSONResult.failure("请求的提款提案不存在!"));
		}
		if(withdraw.locked){
			renderText(JSONResult.failure("该提案已经被锁，您目前无法操作该提案!"));
		}
		if(withdraw.status!=1){
			 renderText(JSONResult.failure("操作失败，该提案已经被处理!"));
		}
		Customer cust=Customer.NTgetCustomer(withdraw.cust_id);
		if(cust==null){
			 renderText(JSONResult.failure("提案的客户不存在!"));
		}
		String user=session.get(Constant.userName);
		int status=3;
		if(flag==-2){
			status=2;
			CustomerService.createOrderNumber(withdraw.wit_no);
			CustomerService.modCredit(cust.cust_id,CreditLogService.Withdraw, 
					withdraw.login_name,withdraw.amount,user, "拒绝提款", withdraw.wit_no);
			//提款不成功 发送站内信
			StringBuffer sb =new StringBuffer();
			sb.append("<p>尊敬的客户，您有一笔提款没有处理成功。提款单号为"+withdraw.wit_no+"，提交时间："+DateUtil.dateToString(withdraw.create_date, "yyyy-MM-dd HH:mm:ss")+"，金额为"+withdraw.amount+"，原因是："+remarks+"</p>");
			sb.append("<p style=\"text-align:right\">八达国际客服部上</p>");
		    Letter.NTcreat(withdraw.cust_id, withdraw.login_name, "提款金额为"+withdraw.amount+"的提款未处理成功", sb.toString(), withdraw.login_name, true);
		}
		WithdrawLog.NTcreat(withdraw.status,status, withdraw.withdraw_id, remarks, user);
		Withdraw.NTchangeStatus(withdraw.withdraw_id, status);
		renderText(JSONResult.success("操作成功!"));
		
	}

	/**
	 * 支付
	 * @param request_id
	 * @param remarks
	 */
	public static void pay(Integer flag,Long request_id,String remarks){
		Withdraw withdraw=Withdraw.NTget(request_id);
		if(withdraw==null){
			 renderText(JSONResult.failure("请求的提款提案不存在!"));
		}
		if(withdraw.locked){
			renderText(JSONResult.failure("该提案已经被锁，您目前无法操作该提案!"));
		}
		if(withdraw.status!=3){
			 renderText(JSONResult.failure("操作失败，该提案已经被处理!"));
		}
		Customer cust=Customer.NTgetCustomer(withdraw.cust_id);
		if(cust==null){
			 renderText(JSONResult.failure("提案的客户不存在!"));
		}
		String user=session.get(Constant.userName);
		int status=5;
		if(flag==-2){
			status=4;
			CustomerService.modCredit(cust.cust_id,CreditLogService.Withdraw, 
					withdraw.login_name,withdraw.amount,user, "拒绝支付", withdraw.wit_no);
			//提款不成功 发送站内信
			StringBuffer sb =new StringBuffer();
			sb.append("<p>尊敬的客户，您有一笔提款没有处理成功。提款单号为"+withdraw.wit_no+"，提交时间："+DateUtil.dateToString(withdraw.create_date, "yyyy-MM-dd HH:mm:ss")+"，金额为"+withdraw.amount+"，原因是："+remarks+"</p>");
			sb.append("<p style=\"text-align:right\">八达国际客服部上</p>");
		    Letter.NTcreat(withdraw.cust_id, withdraw.login_name, "提款金额为"+withdraw.amount+"的提款未处理成功", sb.toString(), withdraw.login_name, true);
		}
		WithdrawLog.NTcreat(withdraw.status,status, withdraw.withdraw_id, remarks, user);
		Withdraw.NTchangeStatus(withdraw.withdraw_id, status);
		if(status==5){
			if(cust.account==null||cust.account.length()<7){
				Customer.NTmodCustBank(withdraw.bank_name, withdraw.account_type, withdraw.location,
						withdraw.account, cust.cust_id);
			}
			if(Withdraw.NTgetCount(withdraw.cust_id)==1){
				Withdraw.NTrecPayDate(withdraw.withdraw_id);
			}
		}
		renderText(JSONResult.success("操作成功!"));
	}
	
	public static void locked(Integer flag,Long request_id,String remarks){
		Withdraw withdraw=Withdraw.NTget(request_id);
		if(withdraw==null){
			 renderText(JSONResult.failure("请求的提款提案不存在!"));
		}
		String user=session.get(Constant.userName);
		if(flag==-2){
			Withdraw.unLocked(request_id);
			WithdrawLog.NTcreat(withdraw.status,withdraw.status, withdraw.withdraw_id, "[解锁/clear]:"+remarks, user);
		}else{
			Withdraw.locked(request_id);
			WithdrawLog.NTcreat(withdraw.status,withdraw.status, withdraw.withdraw_id, "[锁/locked]:"+remarks, user);
		}
		renderText(JSONResult.success("操作成功!"));
		
		
	}
	public static void exportExcel(String sdate,String edate,String sort,String queryKey){
		Sqler sql =new Sqler("select * from mb_withdraw");
		Sqler cntsql =new Sqler("select count(1) from mb_withdraw");
		if(!PageUtil.blank(queryKey)){
			sql.and().left().like("login_name", queryKey);
			cntsql.and().left().like("login_name", queryKey);
			sql.or().like("real_name", queryKey);
			cntsql.or().like("real_name", queryKey);
			sql.or().like("phone", queryKey).right();
			cntsql.or().like("phone", queryKey).right();
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
		sql.orderBy("status desc ,create_date desc");
		
		int count=Sp.get().getDefaultJdbc().queryForObject(cntsql.getSql(),cntsql.getParams().toArray(new Object[0]),Integer.class);
		if(count>50000){
			renderText("导出记录超过5万条，系统仅支持导出小于5万条记录,请缩短范围再导出。");
		}
		DictRender rd=new DictRender();
		try{
		List<Withdraw> withdrawList=Sp.get().getDefaultJdbc().query(sql.getSql(),sql.getParams().toArray(new Object[0]),new WithdrawRowMap());
		WorkbookSettings ws = new WorkbookSettings();
		ws.setLocale(new Locale("cn", "ZH"));
		WritableWorkbook workbook = Workbook.createWorkbook(response.out);
		response.setHeader("content-disposition", "attachment; filename=wi_"+sdate+"_"+edate+".xls");
		response.setHeader("Content-Type", "application/excel");
		
		WritableSheet sheet = workbook.createSheet("成功支付", 0);
		sheet.addCell(new Label(0, 0,"单号"));
        sheet.addCell(new Label(1, 0,"创建时间"));
        sheet.addCell(new Label(2, 0,"用户名"));
        sheet.addCell(new Label(3, 0,"真实姓名"));
        sheet.addCell(new Label(4, 0,"提款金额"));
        sheet.addCell(new Label(5, 0,"银行"));
        sheet.addCell(new Label(6, 0,"当前状态"));
        sheet.addCell(new Label(7, 0,"提交人"));
        sheet.addCell(new Label(8, 0,"提款备注"));
        sheet.addCell(new Label(9, 0,"提款次数"));
        
        sheet.addCell(new Label(10,0, "审核时间"));
        sheet.addCell(new Label(11,0,"审核人员"));
        sheet.addCell(new Label(12,0,"审核耗时（秒）"));
        sheet.addCell(new Label(13,0,"审核备注"));
        sheet.addCell(new Label(14, 0,"支付时间"));
        sheet.addCell(new Label(15, 0,"支付人员"));
        sheet.addCell(new Label(16, 0,"支付耗时（秒）"));
        sheet.addCell(new Label(17, 0,"支付备注"));
        
        WritableSheet sheet1 = workbook.createSheet("支付拒绝", 1);
        sheet1.addCell(new Label(0, 0,"单号"));
        sheet1.addCell(new Label(1, 0,"创建时间"));
        sheet1.addCell(new Label(2, 0,"用户名"));
        sheet1.addCell(new Label(3, 0,"真实姓名"));
        sheet1.addCell(new Label(4, 0,"提款金额"));
        sheet1.addCell(new Label(5, 0,"银行"));
        sheet1.addCell(new Label(6, 0,"当前状态"));
        sheet1.addCell(new Label(7, 0,"提交人"));
        sheet1.addCell(new Label(8, 0,"提款备注"));
        sheet1.addCell(new Label(9, 0,"提款次数"));
        sheet1.addCell(new Label(10,0,"审核时间"));
        sheet1.addCell(new Label(11,0,"审核人员"));
        sheet1.addCell(new Label(12,0,"耗时（秒）"));
        sheet1.addCell(new Label(13,0,"审核备注"));
        sheet1.addCell(new Label(14, 0,"拒绝时间"));
        sheet1.addCell(new Label(15, 0,"拒绝人员"));
        sheet1.addCell(new Label(16, 0,"耗时（秒）"));
        sheet1.addCell(new Label(17, 0,"拒绝备注"));
        
        WritableSheet sheet2 = workbook.createSheet("审核拒绝", 2);
        sheet2.addCell(new Label(0, 0,"单号"));
        sheet2.addCell(new Label(1, 0,"创建时间"));
        sheet2.addCell(new Label(2, 0,"用户名"));
        sheet2.addCell(new Label(3, 0,"真实姓名"));
        sheet2.addCell(new Label(4, 0,"提款金额"));
        sheet2.addCell(new Label(5, 0,"银行"));
        sheet2.addCell(new Label(6, 0,"当前状态"));
        sheet2.addCell(new Label(7, 0,"提交人"));
        sheet2.addCell(new Label(8, 0,"提款备注"));
        sheet1.addCell(new Label(9, 0,"提款次数"));
        sheet2.addCell(new Label(10,0, "拒绝时间"));
        sheet2.addCell(new Label(11,0,"拒绝人员"));
        sheet2.addCell(new Label(12,0,"耗时（秒）"));
        sheet2.addCell(new Label(13,0,"拒绝备注"));
        
        int k1=1,k2=1,k3=1;
        for(int i=0;i<withdrawList.size();i++){
        	Withdraw r=withdrawList.get(i);
        	//支付通过
        	if(r.status==5){
        		List<WithdrawLog> logList=WithdrawLog.NTgetLogsByWit(r.withdraw_id);
        		sheet.addCell(new Label(0, k1,r.wit_no));
                sheet.addCell(new Label(1, k1,DateUtil.dateToString(r.create_date, "yyyy-MM-dd HH:mm:ss")));
                sheet.addCell(new Label(2, k1,r.login_name));
                sheet.addCell(new Label(3, k1,r.real_name));
                sheet.addCell(new Label(4, k1,r.amount.toPlainString()));
                sheet.addCell(new Label(5, k1,r.bank_name));
                sheet.addCell(new Label(6, k1,rd.dictName("withdraw_flag", r.status)));
                sheet.addCell(new Label(7, k1,r.create_user));
                sheet.addCell(new Label(8, k1,r.remarks));
                sheet.addCell(new Label(9, k1,""+r.wit_cnt));
                for(WithdrawLog log:logList){
                	if(log.after_status==3){
		                sheet.addCell(new Label(10, k1,DateUtil.dateToString(log.create_date, "yyyy-MM-dd HH:mm:ss")));
		                sheet.addCell(new Label(11,k1,log.create_user));
		                sheet.addCell(new Label(12,k1,""+(log.create_date.getTime()-r.create_date.getTime())/1000));
		                sheet.addCell(new Label(13,k1,log.remarks));
                	}
                	if(log.after_status==5){
		                sheet.addCell(new Label(14,k1,DateUtil.dateToString(log.create_date, "yyyy-MM-dd HH:mm:ss")));
		                sheet.addCell(new Label(15,k1,log.create_user));
		                sheet.addCell(new Label(16,k1,""+(log.create_date.getTime()-r.create_date.getTime())/1000));
		                sheet.addCell(new Label(17,k1,log.remarks));
                	}
                	
                }
                k1++;
        	}
        	if(r.status==4){
        		List<WithdrawLog> logList=WithdrawLog.NTgetLogsByWit(r.withdraw_id);
        		sheet1.addCell(new Label(0, k2,r.wit_no));
        		sheet1.addCell(new Label(1, k2,DateUtil.dateToString(r.create_date, "yyyy-MM-dd HH:mm:ss")));
        		sheet1.addCell(new Label(2, k2,r.login_name));
        		sheet1.addCell(new Label(3, k2,r.real_name));
        		sheet1.addCell(new Label(4, k2,r.amount.toPlainString()));
        		sheet1.addCell(new Label(5, k2,r.bank_name));
        		sheet1.addCell(new Label(6, k2,rd.dictName("withdraw_flag", r.status)));
        		sheet1.addCell(new Label(7, k2,r.create_user));
        		sheet1.addCell(new Label(8, k2,r.remarks));
        		sheet1.addCell(new Label(9, k2,""+r.wit_cnt));
                for(WithdrawLog log:logList){
                	if(log.after_status==3){
                		sheet1.addCell(new Label(10, k2,DateUtil.dateToString(log.create_date, "yyyy-MM-dd HH:mm:ss")));
                		sheet1.addCell(new Label(11,k2,log.create_user));
                		sheet1.addCell(new Label(12,k2,""+(log.create_date.getTime()-r.create_date.getTime())/1000));
                		sheet1.addCell(new Label(13,k2,log.remarks));
                	}
                	if(log.after_status==4){
                		sheet1.addCell(new Label(14,k2,DateUtil.dateToString(log.create_date, "yyyy-MM-dd HH:mm:ss")));
                		sheet1.addCell(new Label(15,k2,log.create_user));
                		sheet1.addCell(new Label(16,k2,""+(log.create_date.getTime()-r.create_date.getTime())/1000));
                		sheet1.addCell(new Label(17,k2,log.remarks));
                	}
                }
                k2++;
        	}
        	if(r.status==2){
        		List<WithdrawLog> logList=WithdrawLog.NTgetLogsByWit(r.withdraw_id);
        		sheet2.addCell(new Label(0, k3,r.wit_no));
        		sheet2.addCell(new Label(1, k3,DateUtil.dateToString(r.create_date, "yyyy-MM-dd HH:mm:ss")));
        		sheet2.addCell(new Label(2, k3,r.login_name));
        		sheet2.addCell(new Label(3, k3,r.real_name));
        		sheet2.addCell(new Label(4, k3,r.amount.toPlainString()));
        		sheet2.addCell(new Label(5, k3,r.bank_name));
        		sheet2.addCell(new Label(6, k3,rd.dictName("withdraw_flag", r.status)));
        		sheet2.addCell(new Label(7, k3,r.create_user));
        		sheet2.addCell(new Label(8, k3,r.remarks));
        		sheet2.addCell(new Label(9, k3,""+r.wit_cnt));
                for(WithdrawLog log:logList){
                	if(log.after_status==2){
                		sheet2.addCell(new Label(10, k3,DateUtil.dateToString(log.create_date, "yyyy-MM-dd HH:mm:ss")));
                		sheet2.addCell(new Label(11,k3,log.create_user));
                		sheet2.addCell(new Label(12,k3,""+(log.create_date.getTime()-r.create_date.getTime())/1000));
                		sheet2.addCell(new Label(13,k3,log.remarks));
                	}
                }
                k3++;
        	}
        }
        workbook.write();
	    workbook.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
}
