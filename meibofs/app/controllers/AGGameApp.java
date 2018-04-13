package controllers;

import java.util.List;

import models.AgGame;
import models.AgGameRowMap;
import models.PtGame;
import play.mvc.Controller;
import util.ExtPage;
import util.JSONResult;
import util.PageUtil;
import util.Sp;
import util.Sqler;

public class AGGameApp extends Controller{

	public static void getList(int start,int limit,int page,String sdate,String edate,String sort,String queryKey)throws Exception{
		Sqler sql =new Sqler("select * from mb_ag_game");
		Sqler cntsql =new Sqler("select count(1) from mb_ag_game");
		if(!PageUtil.blank(queryKey)){
			sql.and().left().like("game_name", queryKey).or().like("game_code", queryKey).right();
			cntsql.and().left().like("game_name", queryKey).or().like("game_code", queryKey).right();
		}
		sql.orberByDesc("ag_id");
		List<AgGame> roleList=Sp.get().getDefaultJdbc().query(PageUtil.page(sql.getSql(),0,300),sql.getParams().toArray(new Object[0]),new AgGameRowMap());
		int count=Sp.get().getDefaultJdbc().queryForObject(cntsql.getSql(),cntsql.getParams().toArray(new Object[0]),Integer.class);
		ExtPage p =new ExtPage();
		p.data=JSONResult.conver(roleList,true);
		p.total=count; 
		renderJSON(p);
	}
	
	
	public static void saveAgGame(AgGame ag){
		if("1".equals(params.get("kact"))){
			if(ag.create(ag)){
				renderText(JSONResult.success(ag.game_name+"AG游戏创建成功!"));
				}else{
					renderText(JSONResult.failure(ag.game_name+"AG游戏创建失败!"));
			   }
		}
		if("2".equals(params.get("kact"))){
			if(ag.update(ag)){
				renderText(JSONResult.success(ag.game_name+"AG游戏修改成功!"));
				}else{
					renderText(JSONResult.failure(ag.game_name+"AG游戏修改失败!"));
			   }
		}
		
	}
	
	
	public static void deleteAgGame(Long idcode){
		if(idcode==null){
			renderText(JSONResult.failure("AG游戏删除失败!无效的操作!"));
		}
		if(AgGame.NTdelete(idcode)){
			renderText(JSONResult.success("AG游戏删除成功!"));
		}else{
			renderText(JSONResult.failure("AG游戏删除失败!"));
		}
	}
	
	
}
