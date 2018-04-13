package controllers;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import models.BetRecord;
import models.BetRecordRowMap;
import models.DictRender;
import play.mvc.Controller;
import util.DateUtil;
import util.ExtPage;
import util.JSONResult;
import util.PageUtil;
import util.Sp;
import util.Sqler;

public class BetRecApp extends Controller{

	
	public static void getAllList(int start,int limit,int page,String sdate,String edate,String sort,String queryKey)throws Exception{
		Sqler sql =new Sqler("select * from mb_betdata_gather");
		Sqler cntsql =new Sqler("select count(1) from mb_betdata_gather");
		if(!PageUtil.blank(queryKey)){
			
			sql.and().left().like("login_name", queryKey);
			cntsql.and().left().like("login_name", queryKey);
			
			sql.or().like("platform", queryKey).right();
			cntsql.or().like("platform", queryKey).right();
			
			
			
		}
		
		
		if(!(sdate==null||"".equals(sdate))){
			Date date =DateUtil.stringToDate(sdate, "yyyy-MM-dd");
			sql.and().ebigger("bet_date",date);
			cntsql.and().ebigger("bet_date",date);
		}
		if(!(edate==null||"".equals(edate))){
			Date date =DateUtil.stringToDate(edate, "yyyy-MM-dd");
			sql.and().esmaller("bet_date",date);
			cntsql.and().esmaller("bet_date",date);
		}
		sql.orberByDesc("create_date");
		List<BetRecord> roleList=Sp.get().getDefaultJdbc().query(PageUtil.page(sql.getSql(),start,limit),sql.getParams().toArray(new Object[0]),new BetRecordRowMap());
		
		int count=Sp.get().getDefaultJdbc().queryForObject(cntsql.getSql(),cntsql.getParams().toArray(new Object[0]),Integer.class);
		ExtPage p =new ExtPage();
		p.data=JSONResult.conver(roleList,true);
		p.total=count; 
		renderJSON(p);
	}
	
	
	public static void exportExcel(String sdate,String edate,String queryKey){
		Sqler sql =new Sqler("select * from mb_betdata_gather");
		Sqler cntsql =new Sqler("select count(1) from mb_betdata_gather");
		if(!PageUtil.blank(queryKey)){
			sql.and().left().like("login_name", queryKey);
			cntsql.and().left().like("login_name", queryKey);
			
			sql.or().like("platform", queryKey).right();
			cntsql.or().like("platform", queryKey).right();
		}

		if(!(sdate==null||"".equals(sdate))){
			Date date =DateUtil.stringToDate(sdate, "yyyy-MM-dd");
			sql.and().ebigger("bet_date",date);
			cntsql.and().ebigger("bet_date",date);
		}
		if(!(edate==null||"".equals(edate))){
			Date date =DateUtil.stringToDate(edate, "yyyy-MM-dd");
			sql.and().esmaller("bet_date",date);
			cntsql.and().esmaller("bet_date",date);
		}
		sql.orberByDesc("create_date");
		
		int count=Sp.get().getDefaultJdbc().queryForObject(cntsql.getSql(),cntsql.getParams().toArray(new Object[0]),Integer.class);
		if(count>50000){
			renderText("导出记录超过5万条，系统仅支持导出小于5万条记录,请缩短范围再导出。");
		}
		DictRender rd=new DictRender();
		try{
		List<BetRecord> roleList=Sp.get().getDefaultJdbc().query(sql.getSql(),sql.getParams().toArray(new Object[0]),new BetRecordRowMap());
		WorkbookSettings ws = new WorkbookSettings();
		ws.setLocale(new Locale("cn", "ZH"));
		WritableWorkbook workbook = Workbook.createWorkbook(response.out);
		response.setHeader("content-disposition", "attachment; filename=bet_"+sdate+"_"+edate+".xls");
		response.setHeader("Content-Type", "application/excel");
		
		WritableSheet sheet = workbook.createSheet("投注记录", 0);
		sheet.addCell(new Label(0, 0,"用户名"));
		sheet.addCell(new Label(1, 0,"投注总额"));
        sheet.addCell(new Label(2, 0,"投注平台"));
        sheet.addCell(new Label(3, 0,"投注时间"));
       
        
        int k1=1,k2=1,k3=1;
        for(int i=0;i<roleList.size();i++){
        	BetRecord r=roleList.get(i);
        	//支付通过
        	
        		sheet.addCell(new Label(0, k1,r.login_name));
        		sheet.addCell(new Label(1, k1,r.validBetAmount.toPlainString()));
        		sheet.addCell(new Label(2, k1,r.platform));
                sheet.addCell(new Label(3, k1,DateUtil.dateToString(r.bet_date, "yyyy-MM-dd")));
                
                k1++;
        	
        	
        	
        }
        workbook.write();
	    workbook.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
}
