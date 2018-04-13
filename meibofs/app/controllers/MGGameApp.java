package controllers;

import java.util.List;

import models.AgGame;
import models.AgGameRowMap;
import models.MgGame;
import models.MgGameRowMap;
import play.mvc.Controller;
import util.ExtPage;
import util.JSONResult;
import util.PageUtil;
import util.Sp;
import util.Sqler;

public class MGGameApp extends Controller{
	
	public static void getList(int start,int limit,int page,String sdate,String edate,String sort,String queryKey)throws Exception{
		Sqler sql =new Sqler("select * from mb_mg_flash_game");
		Sqler cntsql =new Sqler("select count(1) from mb_mg_flash_game");
		if(!PageUtil.blank(queryKey)){
			sql.and().left().like("CHINESE_SIMP_Game_Name", queryKey).or().like("GameCode", queryKey).right();
			cntsql.and().left().like("CHINESE_SIMP_Game_Name", queryKey).or().like("GameCode", queryKey).right();
		}
		sql.orberByDesc("mg_id");
		List<MgGame> roleList=Sp.get().getDefaultJdbc().query(PageUtil.page(sql.getSql(),0,300),sql.getParams().toArray(new Object[0]),new MgGameRowMap());
		int count=Sp.get().getDefaultJdbc().queryForObject(cntsql.getSql(),cntsql.getParams().toArray(new Object[0]),Integer.class);
		ExtPage p =new ExtPage();
		p.data=JSONResult.conver(roleList,true);
		p.total=count; 
		renderJSON(p);
	}
	
	
	public static void saveMgGame(MgGame mg){
		if("1".equals(params.get("kact"))){
			if(mg.create(mg)){
				renderText(JSONResult.success(mg.CHINESE_SIMP_Game_Name+"MG游戏创建成功!"));
				}else{
					renderText(JSONResult.failure(mg.CHINESE_SIMP_Game_Name+"MG游戏创建失败!"));
			   }
		}
		if("2".equals(params.get("kact"))){
			if(mg.update(mg)){
				renderText(JSONResult.success(mg.CHINESE_SIMP_Game_Name+"MG游戏修改成功!"));
				}else{
					renderText(JSONResult.failure(mg.CHINESE_SIMP_Game_Name+"MG游戏修改失败!"));
			   }
		}
		
	}
	
	
	public static void deleteMgGame(Long idcode){
		if(idcode==null){
			renderText(JSONResult.failure("MG游戏删除失败!无效的操作!"));
		}
		if(MgGame.NTdelete(idcode)){
			renderText(JSONResult.success("MG游戏删除成功!"));
		}else{
			renderText(JSONResult.failure("MG游戏删除失败!"));
		}
	}
	

}
