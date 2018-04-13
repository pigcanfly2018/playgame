package controllers;

import java.util.Date;
import java.util.List;

import models.Bank;
import models.BankRowMap;
import models.UserLog;
import play.mvc.Controller;
import util.Constant;
import util.DateUtil;
import util.ExtPage;
import util.JSONResult;
import util.PageUtil;
import util.Sp;
import util.Sqler;

public class BankApp extends Controller{
	
	public static void getList(int start,int limit,int page,String sdate,String edate,String sort,String queryKey)throws Exception{
		Sqler sql =new Sqler("select * from mb_bank");
		Sqler cntsql =new Sqler("select count(1) from mb_bank");
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
		sql.orberByDesc("create_date");
		List<Bank> roleList=Sp.get().getDefaultJdbc().query(PageUtil.page(sql.getSql(),start,limit),sql.getParams().toArray(new Object[0]),new BankRowMap());
		int count=Sp.get().getDefaultJdbc().queryForObject(cntsql.getSql(),cntsql.getParams().toArray(new Object[0]),Integer.class);
		ExtPage p =new ExtPage();
		p.data=JSONResult.conver(roleList,true);
		p.total=count; 
		renderJSON(p);
	}
	
	public static void saveBank(Bank bank){
		
		UserLog log = new UserLog();
		log.op_user = session.get(Constant.userName);
		
		
	 if("1".equals(params.get("kact"))){
		if(bank.NTcreat()!=0){
			
			log.log_msg ="创建银行卡:"+bank.account_name;
			log.NTcreat();
			renderText(JSONResult.success("保存成功!"));
		}else{
			renderText(JSONResult.failure("保存失败!"));
		}
	 }else{
		 if(bank.NTupdate()){
			 log.log_msg ="更新银行卡"+bank.account_name+"信息,状态改为:"+bank.available;
			 log.NTcreat();
				renderText(JSONResult.success("保存成功!"));
			}else{
				renderText(JSONResult.failure("保存失败!"));
			}
	 }
	}
	
	public static void deleteBank(Long idcode){
		if(Bank.deleteBank(idcode)){
			renderText(JSONResult.success("删除成功!"));
		}else{
			renderText(JSONResult.failure("删除失败!"));
		}
		
	}

}
