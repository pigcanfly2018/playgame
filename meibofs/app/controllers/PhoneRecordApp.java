package controllers;

import java.util.Date;
import java.util.List;

import play.mvc.Controller;
import models.PhoneRecord;
import models.PhoneRecordRowMap;
import util.DateUtil;
import util.ExtPage;
import util.JSONResult;
import util.PageUtil;
import util.Sp;
import util.Sqler;

public class PhoneRecordApp extends Controller{

	public static void getAllList(int start,int limit,int page,String sdate,String edate,String sort,String queryKey)throws Exception{
		Sqler sql =new Sqler("select * from mb_cus_phonerecord");
		Sqler cntsql =new Sqler("select count(1) from mb_cus_phonerecord");
		if(!PageUtil.blank(queryKey)){
			sql.and().left().like("login_name", queryKey).right();
			cntsql.and().left().like("login_name", queryKey).right();
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
		
		sql.and().addSql("TIMESTAMPDIFF(MINUTE,create_date,now())<30");
		cntsql.and().addSql("TIMESTAMPDIFF(MINUTE,create_date,now())<30");
		
		sql.orberByDesc("create_date");
		List<PhoneRecord> roleList=Sp.get().getDefaultJdbc().query(PageUtil.page(sql.getSql(),start,limit),sql.getParams().toArray(new Object[0]),new PhoneRecordRowMap());
		int count=Sp.get().getDefaultJdbc().queryForObject(cntsql.getSql(),cntsql.getParams().toArray(new Object[0]),Integer.class);
		ExtPage p =new ExtPage();
		p.data=JSONResult.conver(roleList,true);
		p.total=count; 
		renderJSON(p);
	}
	
}