package controllers;

import java.util.Date;
import java.util.List;

import models.CashGift;
import models.Customer;
import models.DictRender;
import models.EggGift;
import models.EggGiftRowMap;
import models.Item;
import play.mvc.Controller;
import service.CreditLogService;
import service.CustomerService;
import util.Constant;
import util.DateUtil;
import util.ExtPage;
import util.JSONResult;
import util.PageUtil;
import util.Sp;
import util.Sqler;

public class EggGiftApp extends Controller{
	
	public static void getList(int start,int limit,int page,String sdate,String edate,String sort,String queryKey,String status_type)throws Exception{
		Sqler sql =new Sqler("select * from mb_egg_gift");
		Sqler cntsql =new Sqler("select count(1) from mb_egg_gift");
		if(!PageUtil.blank(queryKey)){
			sql.and().left().like("login_name", queryKey);
			cntsql.and().left().like("login_name", queryKey);
			sql.or().like("gift_code", queryKey).right();
			cntsql.or().like("gift_code", queryKey).right();
		}
		if(!PageUtil.blank(status_type)){
			sql.and().left().equals("status", status_type).right();
			cntsql.and().left().equals("status", status_type).right();
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
		List<EggGift> roleList=Sp.get().getDefaultJdbc().query(PageUtil.page(sql.getSql(),start,limit),sql.getParams().toArray(new Object[0]),new EggGiftRowMap());
		int count=Sp.get().getDefaultJdbc().queryForObject(cntsql.getSql(),cntsql.getParams().toArray(new Object[0]),Integer.class);
		ExtPage p =new ExtPage();
		p.data=JSONResult.conver(roleList,true);
		p.total=count; 
		renderJSON(p);
	}
	
	
	public static void detail(Long giftId){
		EggGift gift=EggGift.NTget(giftId);
		if(gift==null){
			 renderText(JSONResult.failure("请求的中奖单不存在!"));
		}
		DictRender rd =new DictRender();
		render("/Detail/eggGift.html",gift,rd);
	}
	
	
	public static void audit(Integer flag,String remarks,Long giftId){
		EggGift gift=EggGift.NTget(giftId);
		if(gift==null){
			 renderText(JSONResult.failure("请求的单子不存在!"));
		}
		if(gift.status!=1){
			 renderText(JSONResult.failure("操作失败，该奖品单已经被处理!"));
		}
		Customer cust=Customer.NTgetCustomerByLoginName(gift.login_name);
		if(cust==null){
			 renderText(JSONResult.failure("领奖单的客户不存在!"));
		}
		int status =3;
		if(flag==-2){
			status=2;
		}
		String user=session.get(Constant.userName);
		gift.NTAuditGift(giftId, status, user,remarks);
		if(status==2){
			CustomerService.modScore(gift.gift_code, "领奖失败", gift.score, gift.login_name, gift.cust_id, user);
		}
		renderText(JSONResult.success("操作成功!"));
	}
	
}
