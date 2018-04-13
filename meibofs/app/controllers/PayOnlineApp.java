package controllers;

import java.util.List;

import models.PayOnline;
import models.PayOnlineRowMap;
import play.mvc.Controller;
import util.ExtPage;
import util.JSONResult;
import util.PageUtil;
import util.Sp;
import util.Sqler;

public class PayOnlineApp extends Controller{

	public static void getList(int start,int limit,int page,String sdate,String edate,String sort,String queryKey)throws Exception{
		Sqler sql =new Sqler("select * from mb_payonline");
		Sqler cntsql =new Sqler("select count(1) from mb_payonline");
		List<PayOnline> roleList=Sp.get().getDefaultJdbc().query(PageUtil.page(sql.getSql(),start,limit),sql.getParams().toArray(new Object[0]),new PayOnlineRowMap());
		int count=Sp.get().getDefaultJdbc().queryForObject(cntsql.getSql(),cntsql.getParams().toArray(new Object[0]),Integer.class);
		ExtPage p =new ExtPage();
		p.data=JSONResult.conver(roleList,true);
		p.total=count; 
		renderJSON(p);
	}
	
	public static void saveConfig(PayOnline payOnline){
		if("1".equals(params.get("kact"))){
			if(payOnline.NTexits(payOnline.name)){
				renderText(JSONResult.failure(payOnline.name+"已经存在"));
			}
			if(payOnline.NTcreat()){
				renderText(JSONResult.success(payOnline.name+"创建成功!"));
			}else{
				renderText(JSONResult.failure(payOnline.name+"创建失败!"));
			}
		}
		
		if("2".equals(params.get("kact"))){
			if(!payOnline.NTexits(payOnline.name)){
				renderText(JSONResult.failure(payOnline.name+"不存在"));
			}
			if(payOnline.NTupdate()){
				renderText(JSONResult.success(payOnline.name+"修改成功!"));
			}else{
				renderText(JSONResult.failure(payOnline.name+"修改失败!"));
			}
		}
	}
	
}
