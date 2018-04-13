package controllers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import models.CustomerView;
import models.CustomerViewRowMap;
import models.Deposit;
import models.DepositLog;
import models.DepositRowMap;
import models.DictRender;
import play.mvc.Controller;
import util.DateUtil;
import util.ExtPage;
import util.JSONResult;
import util.PageUtil;
import util.Sp;
import util.Sqler;

public class CustomerViewApp extends Controller{
	
	public static void getList(int start,int limit,int page,String sdate,String edate,String sort,String queryKey)throws Exception{
		if(PageUtil.blank(queryKey)){
			ExtPage p =new ExtPage();
			p.data=JSONResult.conver(new ArrayList<CustomerView>(),true);
			p.total=0; 
			renderJSON(p);
		}
		String sqls="SELECT a.login_name login_name,a.real_name real_name,a.login_date login_date,a.phone phone,a.qq qq,a.create_date create_date,b.cnt  deposit_cnt,b.amount deposit_amount,b.last_deposit_date last_deposit_date,d.cnt withdraw_cnt,d.amount withdraw_amount,d.last_withdraw_date last_withdraw_date FROM (SELECT login_name,real_name,phone,qq,create_date,login_date FROM mb_customer WHERE cust_level >0) a LEFT JOIN  (SELECT COUNT(1) cnt ,SUM(amount) amount,MAX(create_date) last_deposit_date, login_name  FROM mb_deposit WHERE STATUS=3 GROUP BY  login_name) b ON a.login_name =b.login_name LEFT JOIN (SELECT COUNT(1) cnt ,SUM(amount) amount,MAX(create_date) last_withdraw_date, login_name  FROM mb_withdraw WHERE STATUS=5 GROUP BY  login_name) d ON a.login_name =d.login_name ";
		String sqlcn="SELECT count(1) FROM (SELECT login_name,real_name,phone,qq,create_date FROM mb_customer WHERE cust_level >0) a LEFT JOIN  (SELECT COUNT(1) cnt ,SUM(amount) amount,MAX(create_date) last_deposit_date, login_name  FROM mb_deposit WHERE STATUS=3 GROUP BY  login_name) b ON a.login_name =b.login_name LEFT JOIN (SELECT COUNT(1) cnt ,SUM(amount) amount,MAX(create_date) last_withdraw_date, login_name  FROM mb_withdraw WHERE STATUS=5 GROUP BY  login_name) d ON a.login_name =d.login_name";
		Sqler sql =new Sqler(sqls);
		Sqler cntsql =new Sqler(sqlcn);
//		String sql0="SELECT COUNT(1) cnt ,SUM(amount) amount,MAX(create_date) last_deposit_date, login_name  FROM mb_deposit WHERE STATUS=3  GROUP BY  login_name";
//		String sql1="SELECT COUNT(1) cnt ,SUM(amount) amount,MAX(create_date) last_withdraw_date, login_name  FROM mb_withdraw WHERE STATUS=5 GROUP BY  login_name";
		if(!PageUtil.blank(queryKey)){
			sql.and().left().like("a.login_name", queryKey).right();
			cntsql.and().left().like("a.login_name", queryKey).right();
		}
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
		sql.orberByDesc("last_deposit_date");
		List<CustomerView> roleList=Sp.get().getDefaultJdbc().query(PageUtil.page(sql.getSql(),start,limit),sql.getParams().toArray(new Object[0]),new CustomerViewRowMap());
		int count=Sp.get().getDefaultJdbc().queryForObject(cntsql.getSql(),cntsql.getParams().toArray(new Object[0]),Integer.class);
		ExtPage p =new ExtPage();
		p.data=JSONResult.conver(roleList,true);
		p.total=count; 
		renderJSON(p);
	}
	
	public static void getAllList(int start,int limit,int page,String sdate,String edate,String sort,String queryKey,String status)throws Exception{
		
//		if(PageUtil.blank(queryKey)){
//			ExtPage p =new ExtPage();
//			p.data=JSONResult.conver(new ArrayList<CustomerView>(),true);
//			p.total=0; 
//			renderJSON(p);
//		}
		System.out.println(status);
		String sqls="SELECT a.login_name login_name,a.real_name real_name,a.login_date login_date,a.phone phone,a.qq qq,a.create_date create_date,b.cnt  deposit_cnt,b.amount deposit_amount,b.last_deposit_date last_deposit_date,d.cnt withdraw_cnt,d.amount withdraw_amount,d.last_withdraw_date last_withdraw_date,b.amount-d.amount as profit_amount FROM (SELECT login_name,real_name,phone,qq,create_date,login_date FROM mb_customer WHERE cust_level >0) a LEFT JOIN  (SELECT COUNT(1) cnt ,SUM(amount) amount,MAX(create_date) last_deposit_date, login_name  FROM mb_deposit WHERE STATUS=3 GROUP BY  login_name) b ON a.login_name =b.login_name LEFT JOIN (SELECT COUNT(1) cnt ,SUM(amount) amount,MAX(create_date) last_withdraw_date, login_name  FROM mb_withdraw WHERE STATUS=5 GROUP BY  login_name) d ON a.login_name =d.login_name ";
		String sqlcn="SELECT count(1) FROM (SELECT login_name,real_name,phone,qq,create_date FROM mb_customer WHERE cust_level >0) a LEFT JOIN  (SELECT COUNT(1) cnt ,SUM(amount) amount,MAX(create_date) last_deposit_date, login_name  FROM mb_deposit WHERE STATUS=3 GROUP BY  login_name) b ON a.login_name =b.login_name LEFT JOIN (SELECT COUNT(1) cnt ,SUM(amount) amount,MAX(create_date) last_withdraw_date, login_name  FROM mb_withdraw WHERE STATUS=5 GROUP BY  login_name) d ON a.login_name =d.login_name";
		Sqler sql =new Sqler(sqls);
		Sqler cntsql =new Sqler(sqlcn);
//		String sql0="SELECT COUNT(1) cnt ,SUM(amount) amount,MAX(create_date) last_deposit_date, login_name  FROM mb_deposit WHERE STATUS=3  GROUP BY  login_name";
//		String sql1="SELECT COUNT(1) cnt ,SUM(amount) amount,MAX(create_date) last_withdraw_date, login_name  FROM mb_withdraw WHERE STATUS=5 GROUP BY  login_name";
		if(!PageUtil.blank(queryKey)){
			sql.and().left().like("a.login_name", queryKey).right();
			cntsql.and().left().like("a.login_name", queryKey).right();
		}
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
		if(status == null || status.equals("")){
			sql.orberByDesc("last_deposit_date");
		}else if(status.equals("1")){
			sql.orberByAsc("create_date");
		}else if(status.equals("2")){
			sql.orberByDesc("create_date");
		}else if(status.equals("4")){
			sql.orberByDesc("profit_amount");
		}else if(status.equals("3")){
			sql.orberByDesc("-profit_amount");
		}
		
		List<CustomerView> roleList=Sp.get().getDefaultJdbc().query(PageUtil.page(sql.getSql(),start,limit),sql.getParams().toArray(new Object[0]),new CustomerViewRowMap());
//		for(CustomerView view:roleList){
//			System.out.println(view.deposit_amount.subtract(view.withdraw_amount));
//		}
		int count=Sp.get().getDefaultJdbc().queryForObject(cntsql.getSql(),cntsql.getParams().toArray(new Object[0]),Integer.class);
		ExtPage p =new ExtPage();
		p.data=JSONResult.conver(roleList,true);
		p.total=count; 
		renderJSON(p);
	}
	
	
	public static void exportExcel(String sdate,String edate,String sort,String queryKey,String status){
		String sqls="SELECT a.login_name login_name,a.real_name real_name,a.login_date login_date,a.phone phone,a.qq qq,a.create_date create_date,b.cnt  deposit_cnt,b.amount deposit_amount,b.last_deposit_date last_deposit_date,d.cnt withdraw_cnt,d.amount withdraw_amount,d.last_withdraw_date last_withdraw_date,b.amount-d.amount as profit_amount FROM (SELECT login_name,real_name,phone,qq,create_date,login_date FROM mb_customer WHERE cust_level >0) a LEFT JOIN  (SELECT COUNT(1) cnt ,SUM(amount) amount,MAX(create_date) last_deposit_date, login_name  FROM mb_deposit WHERE STATUS=3 GROUP BY  login_name) b ON a.login_name =b.login_name LEFT JOIN (SELECT COUNT(1) cnt ,SUM(amount) amount,MAX(create_date) last_withdraw_date, login_name  FROM mb_withdraw WHERE STATUS=5 GROUP BY  login_name) d ON a.login_name =d.login_name ";
		String sqlcn="SELECT count(1) FROM (SELECT login_name,real_name,phone,qq,create_date FROM mb_customer WHERE cust_level >0) a LEFT JOIN  (SELECT COUNT(1) cnt ,SUM(amount) amount,MAX(create_date) last_deposit_date, login_name  FROM mb_deposit WHERE STATUS=3 GROUP BY  login_name) b ON a.login_name =b.login_name LEFT JOIN (SELECT COUNT(1) cnt ,SUM(amount) amount,MAX(create_date) last_withdraw_date, login_name  FROM mb_withdraw WHERE STATUS=5 GROUP BY  login_name) d ON a.login_name =d.login_name";
		Sqler sql =new Sqler(sqls);
		Sqler cntsql =new Sqler(sqlcn);
//		String sql0="SELECT COUNT(1) cnt ,SUM(amount) amount,MAX(create_date) last_deposit_date, login_name  FROM mb_deposit WHERE STATUS=3  GROUP BY  login_name";
//		String sql1="SELECT COUNT(1) cnt ,SUM(amount) amount,MAX(create_date) last_withdraw_date, login_name  FROM mb_withdraw WHERE STATUS=5 GROUP BY  login_name";
		if(!PageUtil.blank(queryKey)){
			sql.and().left().like("a.login_name", queryKey).right();
			cntsql.and().left().like("a.login_name", queryKey).right();
		}
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
		if(status == null || status.equals("")){
			sql.orberByDesc("last_deposit_date");
		}else if(status.equals("1")){
			sql.orberByAsc("create_date");
		}else if(status.equals("2")){
			sql.orberByDesc("create_date");
		}else if(status.equals("4")){
			sql.orberByDesc("profit_amount");
		}else if(status.equals("3")){
			sql.orberByDesc("-profit_amount");
		}
		
		int count=Sp.get().getDefaultJdbc().queryForObject(cntsql.getSql(),cntsql.getParams().toArray(new Object[0]),Integer.class);
		if(count>50000){
			renderText("导出记录超过5万条，系统仅支持导出小于5万条记录,请缩短范围再导出。");
		}
        System.out.println(count);
		DictRender rd=new DictRender();
		try{
		List<CustomerView> viewList=Sp.get().getDefaultJdbc().query(sql.getSql(),sql.getParams().toArray(new Object[0]),new CustomerViewRowMap());
		WorkbookSettings ws = new WorkbookSettings();
		ws.setLocale(new Locale("cn", "ZH"));
		WritableWorkbook workbook = Workbook.createWorkbook(response.out);
		WritableSheet sheet = workbook.createSheet("客户统计", 0);
		response.setHeader("content-disposition", "attachment; filename=customerTongji_"+sdate+"_"+edate+".xls");
		response.setHeader("Content-Type", "application/excel");
		sheet.addCell(new Label(0, 0,"注册时间"));
		sheet.addCell(new Label(1, 0,"用户名"));
        sheet.addCell(new Label(2, 0,"真实姓名"));
        sheet.addCell(new Label(3, 0,"最后登录"));
        sheet.addCell(new Label(4, 0,"存款次数"));
        sheet.addCell(new Label(5, 0,"存款总额"));
        sheet.addCell(new Label(6, 0,"最后存款"));
        sheet.addCell(new Label(7, 0,"提款次数"));
        sheet.addCell(new Label(8, 0,"提款总额"));
        sheet.addCell(new Label(9, 0,"最后提款"));
        sheet.addCell(new Label(10, 0,"盈亏情况"));
        //
        for(int i=0;i<viewList.size();i++){
        	CustomerView r=viewList.get(i);
        	if(r.deposit_amount == null){
        		r.deposit_amount = new BigDecimal(0);
        	}
        	if(r.withdraw_amount == null){
        		r.withdraw_amount = new BigDecimal(0);
        	}
        	  sheet.addCell(new Label(0, i+1,DateUtil.dateToString(r.create_date, "yyyy-MM-dd HH:mm:ss")));
              sheet.addCell(new Label(1, i+1,r.login_name));
              sheet.addCell(new Label(2, i+1,r.real_name));
              sheet.addCell(new Label(3, i+1,DateUtil.dateToString(r.login_date, "yyyy-MM-dd HH:mm:ss")));
              sheet.addCell(new Label(4, i+1,String.valueOf(r.deposit_cnt)));
              sheet.addCell(new Label(5, i+1,r.deposit_amount.toPlainString()));
              sheet.addCell(new Label(6, i+1,DateUtil.dateToString(r.last_deposit_date, "yyyy-MM-dd HH:mm:ss")));
              sheet.addCell(new Label(4, i+1,String.valueOf(r.withdraw_cnt)));
              sheet.addCell(new Label(8, i+1,r.withdraw_amount.toPlainString()));
              sheet.addCell(new Label(9, i+1,DateUtil.dateToString(r.last_withdraw_date, "yyyy-MM-dd HH:mm:ss")));
              sheet.addCell(new Label(10,i+1,(r.deposit_amount.subtract(r.withdraw_amount)).toPlainString()));
             
        }
        workbook.write();
	    workbook.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	

}
