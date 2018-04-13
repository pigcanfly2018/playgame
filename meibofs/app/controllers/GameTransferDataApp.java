package controllers;

import java.util.Date;
import java.util.List;

import play.mvc.Controller;
import models.GameTransferData;
import models.GameTransferDataRowMap;
import util.DateUtil;
import util.ExtPage;
import util.JSONResult;
import util.PageUtil;
import util.Sp;
import util.Sqler;

public class GameTransferDataApp extends Controller{

	public static void getList(int start,int limit,int page,String sdate,String edate,String sort,String queryKey,String status)throws Exception{
		Sqler sql =new Sqler("select * from game_transfer_data");
		Sqler cntsql =new Sqler("select count(1) from game_transfer_data");
		if(!PageUtil.blank(queryKey)){
			sql.and().left().like("game_name", queryKey).or().equals("bill_no",queryKey).or().equals("ref_order_no",queryKey).right();
			cntsql.and().left().like("game_name", queryKey).or().equals("bill_no",queryKey).or().equals("ref_order_no",queryKey).right();
		}
		
		if(!PageUtil.blank(status)){
			sql.and().left().equals("flag", Integer.parseInt(status)).right();
			cntsql.and().left().equals("flag", Integer.parseInt(status)).right();
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
		List<GameTransferData> roleList=Sp.get().getDefaultJdbc().query(PageUtil.page(sql.getSql(),start,limit),sql.getParams().toArray(new Object[0]),new GameTransferDataRowMap());
		
		int count=Sp.get().getDefaultJdbc().queryForObject(cntsql.getSql(),cntsql.getParams().toArray(new Object[0]),Integer.class);
		ExtPage p =new ExtPage();
		p.data=JSONResult.conver(roleList,true);
		p.total=count; 
		renderJSON(p);
	}
	
}
