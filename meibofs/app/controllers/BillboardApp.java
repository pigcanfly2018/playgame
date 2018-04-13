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
import play.mvc.Controller;
import models.Billboard;
import models.BillboardRowMap;
import models.CashGift;
import models.CashGiftRowMap;
import models.Customer;
import models.OrderNo;
import util.Constant;
import util.DateUtil;
import util.ExtPage;
import util.JSONResult;
import util.MyRandom;
import util.PageUtil;
import util.Sp;
import util.Sqler;

public class BillboardApp extends Controller{

	
	public static void getList(int start,int limit,int page,String sdate,String edate,String sort,String queryKey)throws Exception{
		Sqler sql =new Sqler("select * from mb_billboard");
		Sqler cntsql =new Sqler("select count(1) from mb_billboard");
		
		
		
		
		
		
		if(!PageUtil.blank(queryKey)){
			sql.and().left().like("login_name", queryKey).right();
			cntsql.and().left().like("login_name", queryKey).right();
			
		}else if((sdate==null||"".equals(sdate)) && (edate==null||"".equals(edate))){
			sql.and().left().equals("flag", 0).right();
			cntsql.and().left().like("flag", 0).right();
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
		sql.orberByDesc("bet_amount");
		List<Billboard> roleList=Sp.get().getDefaultJdbc().query(PageUtil.page(sql.getSql(),start,50),sql.getParams().toArray(new Object[0]),new BillboardRowMap());
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
		 List<Billboard> boardList=new ArrayList<Billboard>();
         for (int  j= 0; j < sheet.getRows(); j++) {
        	 if(j==0)continue;
        	 try{
        		 Billboard board =new Billboard();
        	
        		 board.login_name=sheet.getCell(0,j).getContents();
        		 board.bet_amount=new BigDecimal(sheet.getCell(1,j).getContents());
        	 
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
	     
	     Billboard.NTUpdateBillboard();
	     
	     for(Billboard board:boardList){ 
	    	 try{
	    		 board.NTcreat();
	    	 }catch(Exception e){
	    		 renderText(JSONResult.failure("导入"+board.login_name+"记录失败:"+e.getMessage()));
	    	 }
	     }
		renderText(JSONResult.success("导入成功，共导入"+boardList.size()+"条记录!"));
		
	}
	
	
	
	
}
