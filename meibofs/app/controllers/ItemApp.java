package controllers;

import java.util.Date;
import java.util.List;

import models.Item;
import models.ItemRowMap;
import play.mvc.Controller;
import play.mvc.With;
import util.Constant;
import util.DateUtil;
import util.ExtPage;
import util.JSONResult;
import util.PageUtil;
import util.Sp;
import util.Sqler;
@With(value = {AjaxSecure.class})
public class ItemApp extends Controller{
	
	public static void getList(int start,int limit,int page,String sdate,String edate,String sort,String queryKey)throws Exception{
		Sqler sql =new Sqler("select * from mb_item");
		Sqler cntsql =new Sqler("select count(1) from mb_item");
		if(!PageUtil.blank(queryKey)){
			sql.and().left().like("itemcode", queryKey);
			cntsql.and().left().like("itemcode", queryKey);
			sql.or().like("groupcode", queryKey);
			cntsql.or().like("groupcode", queryKey);
			sql.or().like("itemname", queryKey).right();
			cntsql.or().like("itemname", queryKey).right();
		}
		if(!(sdate==null||"".equals(sdate))){
			Date date =DateUtil.stringToDate(sdate, "yyyy-MM-dd");
			sql.and().ebigger("createDate",date);
			cntsql.and().ebigger("createDate",date);
		}
		if(!(edate==null||"".equals(edate))){
			Date date =DateUtil.stringToDate(edate, "yyyy-MM-dd");
			sql.and().esmaller("createDate",date);
			cntsql.and().esmaller("createDate",date);
		}
		sql.orberByDesc("createDate");
		List<Item> roleList=Sp.get().getDefaultJdbc().query(PageUtil.page(sql.getSql(),start,limit),sql.getParams().toArray(new Object[0]),new ItemRowMap());
		int count=Sp.get().getDefaultJdbc().queryForObject(cntsql.getSql(),cntsql.getParams().toArray(new Object[0]),Integer.class);
		ExtPage p =new ExtPage();
		p.data=JSONResult.conver(roleList,true);
		p.total=count; 
		renderJSON(p);
	}
	
	
	/**
	 * 保存一个字典
	 * @param user
	 */
	public static void saveItem(Item item){
		if("1".equals(params.get("kact"))){
			if(item.NTexits(item.itemcode)>0){
				renderText(JSONResult.failure(item.itemcode+"该编码已经存在"));
			}
			
			if(item.NTNameexits(item.itemname)>0){
				renderText(JSONResult.failure(item.itemname+"该字典名已经存在"));
			}
			
			item.createdate=new Date(System.currentTimeMillis());
			item.createuser=session.get(Constant.userName);
			if(item.NTcreat()){
				renderText(JSONResult.success(item.itemcode+"字典数据创建成功!"));
			}else{
				renderText(JSONResult.failure(item.itemcode+"字典数据创建失败!"));
			}
		}
		
		if("2".equals(params.get("kact"))){
			if(item.NTexits(item.itemcode)==0){
				renderText(JSONResult.failure(item.itemcode+"该编码不存在"));
			}
			if(item.NTupdate()){
				renderText(JSONResult.success(item.itemcode+"字典数据更新成功!"));
			}else{
				renderText(JSONResult.failure(item.itemcode+"字典数据更新失败!"));
			}
		}
	} 
	
	public static void deleteItem(String idcode){
		String itemcode=idcode;
		if(PageUtil.blank(itemcode)){
			renderText(JSONResult.failure("非法操作！"));
		}
		Item item =new Item();
		item.itemcode=itemcode;
		if(item.NTdelete(itemcode)){
			renderText(JSONResult.success("删除字典数据成功!"));
		}else{
			renderText(JSONResult.failure("删除字典数据失败!"));
		}
	}

}
