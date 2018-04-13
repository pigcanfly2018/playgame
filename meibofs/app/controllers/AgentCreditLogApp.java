package controllers;

import java.util.Date;
import java.util.List;

import models.AgentCreditLog;
import models.AgentCreditLogRowMap;
import models.CreditLog;
import models.CreditLogRowMap;
import play.mvc.Controller;
import util.DateUtil;
import util.ExtPage;
import util.JSONResult;
import util.PageUtil;
import util.Sp;
import util.Sqler;

public class AgentCreditLogApp extends Controller{

	public static void getList(int start,int limit,int page,String sdate,String edate,String sort,String queryKey)throws Exception{
		Sqler sql =new Sqler("select * from mb_agent_credit_log");
		Sqler cntsql =new Sqler("select count(1) from mb_agent_credit_log");
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
		List<AgentCreditLog> roleList=Sp.get().getDefaultJdbc().query(PageUtil.page(sql.getSql(),start,limit),sql.getParams().toArray(new Object[0]),new AgentCreditLogRowMap());
		int count=Sp.get().getDefaultJdbc().queryForObject(cntsql.getSql(),cntsql.getParams().toArray(new Object[0]),Integer.class);
		ExtPage p =new ExtPage();
		p.data=JSONResult.conver(roleList,true);
		p.total=count; 
		renderJSON(p);
	}
	
}
