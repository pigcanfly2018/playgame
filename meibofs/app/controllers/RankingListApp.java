package controllers;

import java.util.Date;
import java.util.List;

import models.EggTrophy;
import models.RankingList;
import models.RankingListRowMap;
import play.mvc.Controller;
import util.DateUtil;
import util.ExtPage;
import util.JSONResult;
import util.PageUtil;
import util.Sp;
import util.Sqler;

public class RankingListApp extends Controller{
	
	public static void getList(int start,int limit,int page,String sdate,String edate,String sort,String queryKey)throws Exception{
		Sqler sql =new Sqler("select * from mb_ranking_list");
		Sqler cntsql =new Sqler("select count(1) from mb_ranking_list");
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
		List<RankingList> roleList=Sp.get().getDefaultJdbc().query(PageUtil.page(sql.getSql(),start,limit),sql.getParams().toArray(new Object[0]),new RankingListRowMap());
		int count=Sp.get().getDefaultJdbc().queryForObject(cntsql.getSql(),cntsql.getParams().toArray(new Object[0]),Integer.class);
		ExtPage p =new ExtPage();
		p.data=JSONResult.conver(roleList,true);
		p.total=count; 
		renderJSON(p);
	}
  
	public static void saveRankingList(RankingList ranking_list){
		if("1".equals(params.get("kact"))){
	
			if(ranking_list.NTcreat()){
				renderText(JSONResult.success(ranking_list.trophy_name+"创建成功!"));
			}else{
				renderText(JSONResult.failure(ranking_list.trophy_name+"创建失败!"));
			}
		}else{
			
			if(ranking_list.NTupdate()){
				renderText(JSONResult.success(ranking_list.trophy_name+"修改成功!"));
			}else{
				renderText(JSONResult.failure(ranking_list.trophy_name+"修改失败!"));
			}
		}
	}
	
	public static void deleteRankingList(Long idcode){
		
		if(idcode==null){
			renderText(JSONResult.failure("非法操作！"));
		}

		if(RankingList.NTdelete(idcode)){
			renderText(JSONResult.success("删除奖品成功!"));
		}else{
			renderText(JSONResult.failure("删除奖品失败!"));
		}
	}


}
