package controllers;

import java.util.Date;
import java.util.List;

import models.HuoliGift;
import models.HuoliGiftRowMap;
import play.mvc.Controller;
import util.DateUtil;
import util.ExtPage;
import util.JSONResult;
import util.PageUtil;
import util.Sp;
import util.Sqler;

public class HuoliGiftApp extends Controller{

	public static void getAllList(int start,int limit,int page,String sdate,String edate,String sort,String queryKey,String flag)throws Exception{
		Sqler sql =new Sqler("select * from mb_huoli_gift");
		Sqler cntsql =new Sqler("select count(1) from mb_huoli_gift");
		if(!PageUtil.blank(queryKey)){
			sql.and().left().like("login_name", queryKey);
			cntsql.and().left().like("login_name", queryKey);
			sql.or().like("gift_no", queryKey).right();
			cntsql.or().like("gift_no", queryKey).right();	
		}
		
		if(!PageUtil.blank(flag)){
			sql.and().left().equals("level", flag).right();
			cntsql.and().left().equals("level", flag).right();
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
		sql.orberByDesc("create_date");
		List<HuoliGift> roleList=Sp.get().getDefaultJdbc().query(PageUtil.page(sql.getSql(),start,limit),sql.getParams().toArray(new Object[0]),new HuoliGiftRowMap());
		int count=Sp.get().getDefaultJdbc().queryForObject(cntsql.getSql(),cntsql.getParams().toArray(new Object[0]),Integer.class);
		ExtPage p =new ExtPage();
		p.data=JSONResult.conver(roleList,true);
		p.total=count; 
		renderJSON(p);
	}
	
}
