package controllers;

import java.util.Date;
import java.util.List;

import models.Ad;
import models.AdRowMap;
import play.mvc.Controller;
import util.Constant;
import util.DateUtil;
import util.ExtPage;
import util.JSONResult;
import util.PageUtil;
import util.Sp;
import util.Sqler;

public class AdApp extends Controller{
	
	public static void getList(int start,int limit,int page,String sdate,String edate,String sort,String queryKey)throws Exception{
		Sqler sql =new Sqler("select * from mb_ad");
		Sqler cntsql =new Sqler("select count(1) from mb_ad");
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
		List<Ad> roleList=Sp.get().getDefaultJdbc().query(PageUtil.page(sql.getSql(),start,limit),sql.getParams().toArray(new Object[0]),new AdRowMap());
		int count=Sp.get().getDefaultJdbc().queryForObject(cntsql.getSql(),cntsql.getParams().toArray(new Object[0]),Integer.class);
		ExtPage p =new ExtPage();
		p.data=JSONResult.conver(roleList,true);
		p.total=count; 
		renderJSON(p);
	}
  
public static void saveAd(Ad ad){
		if("1".equals(params.get("kact"))){
			ad.create_date=new Date(System.currentTimeMillis());
			ad.create_user=session.get(Constant.userName);
			if(ad.NTcreat()!=null){
				renderText(JSONResult.success(ad.ad_title+"广告创建成功!"));
			}else{
				renderText(JSONResult.failure(ad.ad_title+"广告创建失败!"));
			}
		}
		if("2".equals(params.get("kact"))){
			if(ad.NTupdate()){
				renderText(JSONResult.success(ad.ad_title+"广告修改成功!"));
			}else{
				renderText(JSONResult.failure(ad.ad_title+"广告修改失败!"));
			}
		}
		
	}
	
	public static void deleteAd(Long idcode){
		if(idcode==null){
			renderText(JSONResult.failure("广告删除失败!无效的操作!"));
		}
		if(Ad.NTdelete(idcode)){
			renderText(JSONResult.success("广告删除成功!"));
		}else{
			renderText(JSONResult.failure("广告删除失败!"));
		}
		
		
	}

}
