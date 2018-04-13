package controllers;

import java.util.List;

import models.SbGame;
import models.SbGameRowMap;
import play.mvc.Controller;
import util.ExtPage;
import util.JSONResult;
import util.PageUtil;
import util.Sp;
import util.Sqler;

public class SBGameApp extends Controller{
	
	public static void getList(int start,int limit,int page,String sdate,String edate,String sort,String queryKey)throws Exception{
		Sqler sql =new Sqler("select * from mb_sb_game");
		Sqler cntsql =new Sqler("select count(1) from mb_sb_game");
		if(!PageUtil.blank(queryKey)){
			sql.and().left().like("game_name", queryKey).or().like("game_code", queryKey).right();
			cntsql.and().left().like("game_name", queryKey).or().like("game_code", queryKey).right();
		}
		sql.orberByDesc("sb_id");
		List<SbGame> roleList=Sp.get().getDefaultJdbc().query(PageUtil.page(sql.getSql(),0,300),sql.getParams().toArray(new Object[0]),new SbGameRowMap());
		int count=Sp.get().getDefaultJdbc().queryForObject(cntsql.getSql(),cntsql.getParams().toArray(new Object[0]),Integer.class);
		ExtPage p =new ExtPage();
		p.data=JSONResult.conver(roleList,true);
		p.total=count; 
		renderJSON(p);
	}
	
	
	public static void saveGame(SbGame sb){
		if("1".equals(params.get("kact"))){
			if(sb.create(sb)){
				renderText(JSONResult.success(sb.game_name+"申博游戏创建成功!"));
				}else{
					renderText(JSONResult.failure(sb.game_name+"申博游戏创建失败!"));
			   }
		}
		if("2".equals(params.get("kact"))){
			if(sb.update(sb)){
				renderText(JSONResult.success(sb.game_name+"申博游戏修改成功!"));
				}else{
					renderText(JSONResult.failure(sb.game_name+"申博游戏修改失败!"));
			   }
		}
		
	}
	
	
	public static void deleteGame(Long idcode){
		if(idcode==null){
			renderText(JSONResult.failure("申博游戏删除失败!无效的操作!"));
		}
		if(SbGame.NTdelete(idcode)){
			renderText(JSONResult.success("申博游戏删除成功!"));
		}else{
			renderText(JSONResult.failure("申博游戏删除失败!"));
		}
	}

}
