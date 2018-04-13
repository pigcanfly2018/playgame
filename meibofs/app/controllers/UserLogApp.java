package controllers;

import java.util.Date;
import java.util.List;

import models.UserLog;
import models.UserLogRowMap;
import models.Withdraw;
import models.WithdrawRowMap;

import org.apache.log4j.Logger;

import play.mvc.Controller;
import play.mvc.With;
import util.DateUtil;
import util.ExtPage;
import util.JSONResult;
import util.PageUtil;
import util.Sp;
import util.Sqler;

@With(value = {AjaxSecure.class})
public class UserLogApp extends Controller{
	
	private static Logger logger=Logger.getLogger(UserLogApp.class);
	public static void getList(int start,int limit,int page,String sdate,String edate,String sort,String queryKey)throws Exception{
		Sqler sql =new Sqler("select * from mb_user_log");
		Sqler cntsql =new Sqler("select count(1) from mb_user_log");
		
		if(!PageUtil.blank(queryKey)){
			sql.and().left().like("op_user", queryKey);
			cntsql.and().left().like("op_user", queryKey);
			sql.or().like("log_msg", queryKey).right();
			cntsql.or().like("log_msg", queryKey).right();
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
		//sql.and().addSql("datediff(curdate(),create_date)<90");
		//cntsql.and().addSql("datediff(curdate(),create_date)<90");
		sql.orberByDesc("create_date");
		
		List<UserLog> roleList=Sp.get().getDefaultJdbc().query(PageUtil.page(sql.getSql(),start,limit),sql.getParams().toArray(new Object[0]),new UserLogRowMap());
		int count=Sp.get().getDefaultJdbc().queryForObject(cntsql.getSql(),cntsql.getParams().toArray(new Object[0]),Integer.class);
		ExtPage p =new ExtPage();
		p.data=JSONResult.conver(roleList,true);
		p.total=count; 
		renderJSON(p);
	}

}
