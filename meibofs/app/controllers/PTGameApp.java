package controllers;

import java.util.Date;
import java.util.List;

import models.Ad;
import models.PtGame;
import models.PtGameRowMap;
import play.mvc.Controller;
import util.Constant;
import util.DateUtil;
import util.ExtPage;
import util.JSONResult;
import util.PageUtil;
import util.Sp;
import util.Sqler;

public class PTGameApp extends Controller{

	public static void getList(int start,int limit,int page,String sdate,String edate,String sort,String queryKey)throws Exception{
		Sqler sql =new Sqler("select * from pt_game");
		Sqler cntsql =new Sqler("select count(1) from pt_game");
		if(!PageUtil.blank(queryKey)){
			sql.and().left().like("game_name", queryKey).or().like("cn_name", queryKey).right();
			cntsql.and().left().like("game_name", queryKey).or().like("cn_name", queryKey).right();
		}
		sql.orberByDesc("pt_id");
		List<PtGame> roleList=Sp.get().getDefaultJdbc().query(PageUtil.page(sql.getSql(),0,300),sql.getParams().toArray(new Object[0]),new PtGameRowMap());
		int count=Sp.get().getDefaultJdbc().queryForObject(cntsql.getSql(),cntsql.getParams().toArray(new Object[0]),Integer.class);
		ExtPage p =new ExtPage();
		p.data=JSONResult.conver(roleList,true);
		p.total=count; 
		renderJSON(p);
	}
  
public static void savePtGame(PtGame pt){
		if("1".equals(params.get("kact"))){
			pt.client="√";
			pt.flash="√";
			pt.mobile="√";
			if(pt.create(pt)){
				renderText(JSONResult.success(pt.game_name+"PT游戏创建成功!"));
				}else{
					renderText(JSONResult.failure(pt.game_name+"PT游戏创建失败!"));
			   }
		}
		if("2".equals(params.get("kact"))){
			if(pt.update(pt)){
				renderText(JSONResult.success(pt.game_name+"PT游戏修改成功!"));
				}else{
					renderText(JSONResult.failure(pt.game_name+"PT游戏修改失败!"));
			   }
		}
		
	}
	
	public static void deletePtGame(Long idcode){
		if(idcode==null){
			renderText(JSONResult.failure("PT游戏删除失败!无效的操作!"));
		}
		if(PtGame.NTdelete(idcode)){
			renderText(JSONResult.success("PT游戏删除成功!"));
		}else{
			renderText(JSONResult.failure("PT游戏删除失败!"));
		}
	}
	
	
	
}
