package controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jxl.Sheet;
import jxl.Workbook;
import models.CashGift;
import models.Customer;
import models.OrderNo;
import models.WinList;
import models.WinListRowMap;
import play.mvc.Controller;
import util.Constant;
import util.DateUtil;
import util.ExtPage;
import util.JSONResult;
import util.MyRandom;
import util.PageUtil;
import util.Sp;
import util.Sqler;

public class WinListApp extends Controller {
	public static void getList(int start, int limit, int page, String sdate, String edate, String sort, String queryKey)
			throws Exception {
		Sqler sql = new Sqler("select * from mb_win_list");
		Sqler cntsql = new Sqler("select count(1) from mb_win_list");

		if (!PageUtil.blank(queryKey)) {
			sql.and().left().like("win_id", queryKey);
			cntsql.and().left().like("win_id", queryKey);
			sql.or().like("platform", queryKey);
			cntsql.or().like("platform", queryKey);
			sql.or().like("game_name", queryKey);
			cntsql.or().like("game_name", queryKey);
			sql.or().like("login_name", queryKey);
			cntsql.or().like("login_name", queryKey);
			sql.or().like("img_path", queryKey).right();
			cntsql.or().like("img_path", queryKey).right();
		}

		if (!(sdate == null || "".equals(sdate))) {
			sql.and().ebigger("publish_date", sdate + " 00:00:00");
			cntsql.and().ebigger("publish_date", sdate + " 00:00:00");
		}
		if (!(edate == null || "".equals(edate))) {
			sql.and().esmaller("publish_date", edate + " 23:59:59");
			cntsql.and().esmaller("publish_date", edate + " 23:59:59");
		}
		sql.orderBy("publish_status, publish_date desc, upload_date desc");
		
		List<WinList> winList = Sp.get().getDefaultJdbc().query(PageUtil.page(sql.getSql(), start, limit),
				sql.getParams().toArray(new Object[0]), new WinListRowMap());
		int count = Sp.get().getDefaultJdbc().queryForObject(cntsql.getSql(), cntsql.getParams().toArray(new Object[0]),
				Integer.class);
		ExtPage p = new ExtPage();
		p.data = JSONResult.conver(winList, true);
		p.total = count;
		renderJSON(p);
	}

	public static void saveWinList(WinList winlist) throws ParseException {
		if(winlist.upload_date !=null && !winlist.upload_date.isEmpty() && winlist.publish_date !=null && !winlist.publish_date.isEmpty()){
			if(winlist.upload_date.length()<16){
				winlist.upload_date = winlist.upload_date + " 00:00:00";
			}
			if(winlist.publish_date.length()<16){
				winlist.publish_date = winlist.publish_date + " 00:00:00";
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date updateDate = sdf.parse(winlist.upload_date);
			Date publishDate = sdf.parse(winlist.publish_date);
			if(updateDate.after(publishDate)){
				renderText(JSONResult.failure("发布日期不能早于上传日期！"));
			}
		}

		if (winlist.publish_date.isEmpty()) {
			winlist.publish_date = null;
		}else{
			winlist.publish_status = 1;
		}
		
		if ("1".equals(params.get("kact"))) {
			Sqler cntsql = new Sqler("select count(1) from mb_win_list");
			int oriRecordCnt = Sp.get().getDefaultJdbc().queryForObject(cntsql.getSql(), cntsql.getParams().toArray(new Object[0]), Integer.class);
			if(oriRecordCnt>=1000){
				renderText(JSONResult.failure("记录数已经达到1000条，不能再增加，请先删除部分记录!"));
			}
			if (winlist.NTcreat() != null) {
				renderText(JSONResult.success("操作成功!"));
			}
			renderText(JSONResult.failure("操作失败"));
		} else if ("2".equals(params.get("kact"))) {

			if (winlist.NTupdate()) {
				renderText(JSONResult.success("操作成功!"));
			}
			renderText(JSONResult.failure("操作失败"));
		}
	}
	
	
	public static void deleteWins(String winIds){
		if(WinList.NTdeleteWins(winIds)){
			renderText(JSONResult.success("删除成功。"));
		}
		renderText(JSONResult.failure("删除失败。"));
	}	
	
	
	public static void publishWins(String winIds){
		if(WinList.NTPublishWins(winIds)){
			renderText(JSONResult.success("删除成功。"));
		}
		renderText(JSONResult.failure("删除失败。"));
	}	
	
	public static void importExcel(File winlist_file){
		if(!winlist_file.getName().endsWith("xls")){
			renderText(JSONResult.failure("必须是xls格式文件!"));
		}
		
		Sqler cntsql = new Sqler("select count(1) from mb_win_list");
		int oriRecordCnt = Sp.get().getDefaultJdbc().queryForObject(cntsql.getSql(), cntsql.getParams().toArray(new Object[0]), Integer.class);
	
		Workbook workBook=null;
		try {
			InputStream fs = new FileInputStream(winlist_file);;
			workBook =Workbook.getWorkbook(fs);
		} catch (Exception e) {
			renderText(JSONResult.failure("读xls文件出现异常"+e.getMessage()));
		}
		
		Sheet sheet = workBook.getSheet(0);
		List<WinList> winList=new ArrayList<WinList>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		int waitTransferCnt = sheet.getRows()-1;
		if(oriRecordCnt + waitTransferCnt > 1000){
			waitTransferCnt = 1000 - oriRecordCnt;
		}
		
		for (int j=1; j<waitTransferCnt+1; j++) {
			try{
				WinList winDetail = new WinList();
				winDetail.platform = sheet.getCell(0,j).getContents();
				winDetail.game_name = sheet.getCell(1,j).getContents();
				winDetail.login_name = sheet.getCell(2,j).getContents();
				winDetail.win_amount = new Float(sheet.getCell(3,j).getContents());
				winDetail.img_path = sheet.getCell(4,j).getContents();
				winDetail.upload_date = sdf.format(new Date());
				winDetail.publish_date = null;
				winList.add(winDetail);
	        }catch(Exception e){
	        	e.printStackTrace();
	        	renderText(JSONResult.failure("处理"+(j+1)+"行出现异常:"+e.getMessage()));
	        }
		}
		workBook.close();
		winlist_file.delete();
		for(WinList winDetail: winList){ 
			try{
				winDetail.NTcreat();
			}catch(Exception e){
				renderText(JSONResult.failure("导入"+winDetail.login_name+"记录失败:"+e.getMessage()));
			}
		}
		if(waitTransferCnt==0) renderText(JSONResult.failure("记录数已经达到1000条，不能再导入，请先删除部分记录!"));
		else renderText(JSONResult.success("导入成功，共导入"+waitTransferCnt+"条记录!"));
	}
}
