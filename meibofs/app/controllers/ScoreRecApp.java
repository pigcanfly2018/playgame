package controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jxl.Sheet;
import jxl.Workbook;
import models.Billboard;
import models.Customer;
import models.ScoreRec;
import models.ScoreRecRowMap;
import play.mvc.Controller;
import service.CustomerService;
import util.DateUtil;
import util.ExtPage;
import util.JSONResult;
import util.PageUtil;
import util.Sp;
import util.Sqler;

public class ScoreRecApp extends Controller{
	
	public static void getAllList(int start,int limit,int page,String sdate,String edate,String sort,String queryKey,String gift_type)throws Exception{
		Sqler sql =new Sqler("select * from mb_score_rec");
		Sqler cntsql =new Sqler("select count(1) from mb_score_rec");
		if(!PageUtil.blank(queryKey)){
			sql.and().left().like("login_name", queryKey).right();
			cntsql.and().left().like("login_name", queryKey).right();
		}
		if(!PageUtil.blank(gift_type)){
			sql.and().left().equals("rec_type", gift_type).right();
			cntsql.and().left().equals("rec_type", gift_type).right();
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
		sql.orberByDesc("create_date");
		List<ScoreRec> roleList=Sp.get().getDefaultJdbc().query(PageUtil.page(sql.getSql(),start,limit),sql.getParams().toArray(new Object[0]),new ScoreRecRowMap());
		int count=Sp.get().getDefaultJdbc().queryForObject(cntsql.getSql(),cntsql.getParams().toArray(new Object[0]),Integer.class);
		ExtPage p =new ExtPage();
		p.data=JSONResult.conver(roleList,true);
		p.total=count; 
		renderJSON(p);
	}
	
	public static void importExcel(File gift_file){
		if(!gift_file.getName().endsWith("xls")){
			renderText(JSONResult.failure("必须是xls格式文件!"));
		}
		 Workbook workBook=null;
		try {
		   InputStream fs = new FileInputStream(gift_file);;
             workBook =Workbook.getWorkbook(fs);
		} catch (Exception e) {
			renderText(JSONResult.failure("读xls文件出现异常"+e.getMessage()));
		}
		 Sheet sheet = workBook.getSheet(0);
		 
		 List<ScoreRec> boardList=new ArrayList<ScoreRec>();
         for (int  j= 0; j < sheet.getRows(); j++) {
        	 if(j==0)continue;
        	 try{
        		 ScoreRec board =new ScoreRec();
        		 
        		 String login_name=sheet.getCell(2,j).getContents();
        		 Customer cust = Customer.getCustomer(login_name);
        		 BigDecimal score=new BigDecimal(sheet.getCell(7,j).getContents());
        		 String no = sheet.getCell(0,j).getContents();
        		 //System.out.println(login_name +"    "+score+"    "+no);
        		 if(score.intValue()>0){
        			 CustomerService.modScore(no, "活力双倍积分", score, login_name, cust.cust_id, "systemwoody");
        		 }
        		 
        	 
//     		 Customer cust=Customer.NTgetCustomerByLoginName(gift.login_name);
//     		 if(cust==null){
//     			renderText(JSONResult.failure("处理"+(j+1)+"行出现异常:"+gift.login_name+"系统不存在该用户。"));
//     		 }
     		 
        		 boardList.add(board);
        	 }catch(Exception e){
        		 e.printStackTrace();
        		 renderText(JSONResult.failure("处理"+(j+1)+"行出现异常:"+e.getMessage()));
        	 }
         }
	     workBook.close();
	     gift_file.delete();
	     
	     
		renderText(JSONResult.success("导入成功，共导入"+boardList.size()+"条记录!"));
		
	}
	

}
