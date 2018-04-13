package controllers;

import java.util.List;

import models.Config;
import models.ConfigRowMap;
import play.mvc.Controller;
import util.ExtPage;
import util.JSONResult;
import util.PageUtil;
import util.Sp;
import util.Sqler;

public class ConfigApp extends Controller{
	
	public static void getList(int start,int limit,int page,String sdate,String edate,String sort,String queryKey)throws Exception{
		Sqler sql =new Sqler("select * from mb_config");
		Sqler cntsql =new Sqler("select count(1) from mb_config");
		List<Config> roleList=Sp.get().getDefaultJdbc().query(PageUtil.page(sql.getSql(),start,limit),sql.getParams().toArray(new Object[0]),new ConfigRowMap());
		int count=Sp.get().getDefaultJdbc().queryForObject(cntsql.getSql(),cntsql.getParams().toArray(new Object[0]),Integer.class);
		ExtPage p =new ExtPage();
		p.data=JSONResult.conver(roleList,true);
		p.total=count; 
		renderJSON(p);
	}
	
	public static void saveConfig(Config config){
		if("1".equals(params.get("kact"))){
			if(config.NTexits(config.config_name)){
				renderText(JSONResult.failure(config.config_name+"已经存在"));
			}
			if(config.NTcreat()){
				renderText(JSONResult.success(config.config_name+"创建成功!"));
			}else{
				renderText(JSONResult.failure(config.config_name+"创建失败!"));
			}
		}
		
		if("2".equals(params.get("kact"))){
			if(!config.NTexits(config.config_name)){
				renderText(JSONResult.failure(config.config_name+"不存在"));
			}
			if(config.NTupdate()){
				renderText(JSONResult.success(config.config_name+"修改成功!"));
			}else{
				renderText(JSONResult.failure(config.config_name+"修改失败!"));
			}
		}
	}
	

}
