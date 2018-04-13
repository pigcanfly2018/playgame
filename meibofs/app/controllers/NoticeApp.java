package controllers;

import java.util.Date;
import java.util.List;

import models.Ad;
import models.Notice;
import models.NoticeRowMap;
import play.mvc.Controller;
import util.Constant;
import util.DateUtil;
import util.ExtPage;
import util.JSONResult;
import util.PageUtil;
import util.Sp;
import util.Sqler;

public class NoticeApp extends Controller {

	public static void getList(int start, int limit, int page, String sdate,
			String edate, String sort, String queryKey) throws Exception {
		Sqler sql = new Sqler("select * from mb_notice");
		Sqler cntsql = new Sqler("select count(1) from mb_notice");
		if (!(sdate == null || "".equals(sdate))) {
			Date date = DateUtil.stringToDate(sdate, "yyyy-MM-dd");
			sql.and().ebigger("create_date", date);
			cntsql.and().ebigger("create_date", date);
		}
		if (!(edate == null || "".equals(edate))) {
			Date date = DateUtil.stringToDate(edate, "yyyy-MM-dd");
			sql.and().esmaller("create_date", date);
			cntsql.and().esmaller("create_date", date);
		}
		sql.orberByDesc("create_date");
		List<Notice> roleList = Sp
				.get()
				.getDefaultJdbc()
				.query(PageUtil.page(sql.getSql(), start, limit),
						sql.getParams().toArray(new Object[0]),
						new NoticeRowMap());
		int count = Sp
				.get()
				.getDefaultJdbc()
				.queryForObject(cntsql.getSql(),
						cntsql.getParams().toArray(new Object[0]),Integer.class);
		ExtPage p = new ExtPage();
		p.data = JSONResult.conver(roleList, true);
		p.total = count;
		renderJSON(p);
	}
	
	public static void saveNotice(Notice notice){
		if("1".equals(params.get("kact"))){
			notice.create_date=new Date(System.currentTimeMillis());
			notice.create_user=session.get(Constant.userName);
			if(notice.NTcreat()!=null){
				renderText(JSONResult.success(notice.title+"广告创建成功!"));
			}else{
				renderText(JSONResult.failure(notice.title+"广告创建失败!"));
			}
		}
		if("2".equals(params.get("kact"))){
			if(notice.NTupdate()){
				renderText(JSONResult.success(notice.title+"广告修改成功!"));
			}else{
				renderText(JSONResult.failure(notice.title+"广告修改失败!"));;
			}
		}
		
	}
	
	public static void deleteNotice(Long idcode){
		if(idcode==null){
			renderText(JSONResult.failure("广告删除失败!无效的操作!"));
		}
		if(Notice.NTdelete(idcode)){
			renderText(JSONResult.success("广告删除成功!"));
		}else{
			renderText(JSONResult.failure("广告删除失败!"));
		}
		
		
	}


}
