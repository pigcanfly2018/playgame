package controllers;

import java.util.Date;
import java.util.List;

import models.Ad;
import models.Discount;
import models.DiscountRowMap;
import play.mvc.Controller;
import util.Constant;
import util.DateUtil;
import util.ExtPage;
import util.JSONResult;
import util.PageUtil;
import util.Sp;
import util.Sqler;

public class DiscountApp extends Controller{
	
	public static void getList(int start,int limit,int page,String sdate,String edate,String sort,String queryKey)throws Exception{
		Sqler sql =new Sqler("select * from mb_discount");
		Sqler cntsql =new Sqler("select count(1) from mb_discount");
		
		if(!PageUtil.blank(queryKey)){
			sql.and().left().like("title", queryKey).right();
			cntsql.and().left().like("title", queryKey).right();
			
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
		List<Discount> roleList=Sp.get().getDefaultJdbc().query(PageUtil.page(sql.getSql(),start,limit),sql.getParams().toArray(new Object[0]),new DiscountRowMap());
		int count=Sp.get().getDefaultJdbc().queryForObject(cntsql.getSql(),cntsql.getParams().toArray(new Object[0]),Integer.class);
		ExtPage p =new ExtPage();
		p.data=JSONResult.conver(roleList,true);
		p.total=count; 
		renderJSON(p);
	}
	
	public static void saveDiscount(Discount discount){
		if("1".equals(params.get("kact"))){
			discount.create_date=new Date(System.currentTimeMillis());
			discount.create_user=session.get(Constant.userName);
			if(discount.NTcreat()!=null){
				renderText(JSONResult.success(discount.title+"优惠创建成功!"));
			}else{
				renderText(JSONResult.failure(discount.title+"优惠创建失败!"));
			}
		}
		if("2".equals(params.get("kact"))){
			if(discount.NTupdate()){
				renderText(JSONResult.success(discount.title+"优惠修改成功!"));
			}else{
				renderText(JSONResult.failure(discount.title+"优惠修改失败!"));
			}
		}
	}
	
	public static void deleteDiscount(Long idcode){
		if(idcode==null){
			renderText(JSONResult.failure("优惠删除失败!无效的操作!"));
		}
		if(Discount.NTdelete(idcode)){
			renderText(JSONResult.success("优惠删除成功!"));
		}else{
			renderText(JSONResult.failure("优惠删除失败!"));
		}
	}
}
