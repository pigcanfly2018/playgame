package controllers;

import java.util.Date;
import java.util.List;

import models.EggTrophy;
import models.EggTrophyRowMap;
import models.Func;
import play.mvc.Controller;
import util.DateUtil;
import util.ExtPage;
import util.JSONResult;
import util.PageUtil;
import util.Sp;
import util.Sqler;

public class EggTrophyApp extends Controller{
	
	
	public static void getList(int start,int limit,int page,String sdate,String edate,String sort,String queryKey)throws Exception{
		Sqler sql =new Sqler("select * from mb_egg_trophy");
		Sqler cntsql =new Sqler("select count(1) from mb_egg_trophy");
		if(!PageUtil.blank(queryKey)){
			sql.and().left().like("trophy_code", queryKey);
			cntsql.and().left().like("trophy_code", queryKey);
			sql.or().like("egg", queryKey);
			cntsql.or().like("egg", queryKey);
			sql.or().like("trophy_name", queryKey).right();
			cntsql.or().like("trophy_name", queryKey).right();
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
		sql.orderBy("cost desc");
		List<Func> roleList=Sp.get().getDefaultJdbc().query(PageUtil.page(sql.getSql(),start,limit),sql.getParams().toArray(new Object[0]),new EggTrophyRowMap());
		int count=Sp.get().getDefaultJdbc().queryForObject(cntsql.getSql(),cntsql.getParams().toArray(new Object[0]),Integer.class);
		ExtPage p =new ExtPage();
		p.data=JSONResult.conver(roleList,true);
		p.total=count;
		renderJSON(p);
	}
	
	
	public static void saveTrophy(EggTrophy trophy){
		if("1".equals(params.get("kact"))){
			if(trophy.NTexits(trophy.trophy_code)){
				renderText(JSONResult.failure(trophy.trophy_code+"已经存在!"));
			}
			if(trophy.NTcreat()){
				renderText(JSONResult.success(trophy.trophy_name+"创建成功!"));
			}else{
				renderText(JSONResult.failure(trophy.trophy_name+"创建失败!"));
			}
		}else{
			if(trophy.NTexits(trophy.trophy_code,trophy.trophy_id)){
				renderText(JSONResult.failure(trophy.trophy_code+"已经存在!"));
			}
			if(trophy.NTupdate()){
				renderText(JSONResult.success(trophy.trophy_name+"修改成功!"));
			}else{
				renderText(JSONResult.failure(trophy.trophy_name+"修改失败!"));
			}
		}
	}
	
	public static void deleteTrophy(Long idcode){
		
		if(idcode==null){
			renderText(JSONResult.failure("非法操作！"));
		}

		if(EggTrophy.NTdelete(idcode)){
			renderText(JSONResult.success("删除奖品成功!"));
		}else{
			renderText(JSONResult.failure("删除奖品失败!"));
		}
	}

}
