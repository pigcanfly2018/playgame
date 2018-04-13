package controllers;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import models.CreditLog;
import models.CreditLogRowMap;
import models.Customer;
import models.Deposit;
import models.DepositLog;
import play.mvc.Controller;
import util.DateUtil;
import util.ExtPage;
import util.JSONResult;
import util.PageUtil;
import util.Sp;
import util.Sqler;

public class CreditLogApp extends Controller{
	
	public static void getList(int start,int limit,int page,String sdate,String edate,String sort,String queryKey)throws Exception{
		Sqler sql =new Sqler("select * from mb_credit_log");
		Sqler cntsql =new Sqler("select count(1) from mb_credit_log");
		if(!PageUtil.blank(queryKey)){
			sql.and().left().equals("login_name", queryKey).or().equals("order_no",queryKey).right();
			cntsql.and().left().equals("login_name", queryKey).or().equals("order_no",queryKey).right();
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
		sql.orberByDesc("log_id");
		List<CreditLog> roleList=Sp.get().getDefaultJdbc().query(PageUtil.page(sql.getSql(),start,limit),sql.getParams().toArray(new Object[0]),new CreditLogRowMap());
		int count=Sp.get().getDefaultJdbc().queryForObject(cntsql.getSql(),cntsql.getParams().toArray(new Object[0]),Integer.class);
		ExtPage p =new ExtPage();
		p.data=JSONResult.conver(roleList,true);
		p.total=count; 
		renderJSON(p);
	}
	
	public static void main(String[] args){
		Sqler sql =new Sqler("select * from mb_credit_logbak");
		sql.and().left().equals("log_type", "自动充值").right();
		sql.orberByDesc("log_id");
		List<CreditLog> roleList=Sp.get().getDefaultJdbc().query(PageUtil.page(sql.getSql(),0,4000),sql.getParams().toArray(new Object[0]),new CreditLogRowMap());
		int i = 0;
		//System.out.println(roleList.size());
		//礼金添加一共242条
		//自动充值一共3488条
		Long no = 2015111523420000110l;
		for(CreditLog log: roleList){
			Sqler newsql =new Sqler("select * from mb_credit_logbak");
			newsql.and().left().equals("log_type", "礼金添加").right();
			newsql.and().left().equals("login_name", log.login_name).right();
			
			newsql.and().left().equals("log_id", (log.log_id+1)).or().equals("log_id", (log.log_id+2)).or().equals("log_id", (log.log_id+3)).or().equals("log_id", (log.log_id+6)).right();
		
			
			newsql.and().left().equals("org_credit", log.after_credit).right();
			newsql.and().left().equals("after_credit", log.org_credit.add(log.credit).add(new BigDecimal(log.credit.divide(new BigDecimal(100)).intValue()))).right();
			newsql.orberByDesc("log_id");
			List<CreditLog> newList=Sp.get().getDefaultJdbc().query(PageUtil.page(newsql.getSql(),0,4000),newsql.getParams().toArray(new Object[0]),new CreditLogRowMap());
			if(newList.size() == 1){
				i++;
			}else{
				Customer customer = Customer.getCustomer(log.login_name);
				Deposit deposit = new Deposit();
				deposit.deposit_date = log.create_date;
				deposit.cust_id=customer.cust_id;
				deposit.login_name = customer.login_name;
				deposit.real_name = customer.real_name;
				deposit.pdage_status=1;
				deposit.status=1;
				deposit.dep_no = "DE"+no;
				no ++ ;
				deposit.create_user = "system";
				deposit.ip = "127.0.0.1";
				deposit.amount = log.credit;
				deposit.bank_name = "支付宝(二维码)";
				deposit.deposit_type = "移动钱包（二维码）充值";
				deposit.poundage = new BigDecimal(log.credit.divide(new BigDecimal(100)).intValue()>2888?2888:log.credit.divide(new BigDecimal(100)).intValue());
				deposit.remarks="DP支付补单";
				Long dep_id=deposit.NTcreat();
				 if(dep_id!=null){
					 DepositLog depositlog =new DepositLog();
					 depositlog.after_status=1;
					 depositlog.create_user="system";
					 depositlog.deposit_id=dep_id;
					 depositlog.dep_no=deposit.dep_no;
					 depositlog.pre_status=0;
					 depositlog.remarks=deposit.remarks;
					 depositlog.NTcreat();
				 }
				 
				 DepositLog.NTcreat(deposit.status,3,dep_id, "DP支付补单", "system",deposit.dep_no); 
				 Deposit.NTchangeStatus(dep_id, 3, 3);
				 if(Deposit.NTgetCount(deposit.cust_id)==1){
					 Deposit.NTrecAuditDate(dep_id);
				 }
			}
			

		}
	}

}
