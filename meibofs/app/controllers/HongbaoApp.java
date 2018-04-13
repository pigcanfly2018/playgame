package controllers;

import java.util.Date;
import java.util.List;

import models.Hongbao;
import models.HongbaoRowMap;
import models.HuiBo;
import models.HuoliGift;
import models.HuoliGiftRowMap;
import play.mvc.Controller;
import util.DateUtil;
import util.ExtPage;
import util.JSONResult;
import util.PageUtil;
import util.Sp;
import util.Sqler;

public class HongbaoApp extends Controller{

	public static void getAllList(int start,int limit,int page,String sdate,String edate,String sort,String queryKey)throws Exception{
		Sqler sql =new Sqler("select * from mb_hongbao");
		Sqler cntsql =new Sqler("select count(1) from mb_hongbao");
		if(!PageUtil.blank(queryKey)){
			sql.and().left().like("login_name", queryKey).or().like("content", queryKey);
			cntsql.and().left().like("login_name", queryKey).or().like("content", queryKey);
			sql.or().like("gift_id", queryKey).right();
			cntsql.or().like("gift_id", queryKey).right();	
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
		List<Hongbao> roleList=Sp.get().getDefaultJdbc().query(PageUtil.page(sql.getSql(),start,limit),sql.getParams().toArray(new Object[0]),new HongbaoRowMap());
		int count=Sp.get().getDefaultJdbc().queryForObject(cntsql.getSql(),cntsql.getParams().toArray(new Object[0]),Integer.class);
		ExtPage p =new ExtPage();
		p.data=JSONResult.conver(roleList,true);
		p.total=count; 
		renderJSON(p);
	}
	
	public static void success(String idcode){
		

		if(PageUtil.blank(idcode)){
			renderText(JSONResult.failure("非法操作！"));
		}
		
		if(Hongbao.finishHongbao(idcode)){
			renderText(JSONResult.success("更改状态成功!"));
		}else{
			renderText(JSONResult.failure("更改状态失败!"));
		}
		
		
	}
	
	
}
