package controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import models.CashGift;
import models.CashGiftRowMap;
import models.Customer;
import models.CustomerStatus;
import models.DictRender;
import models.Item;
import models.OrderNo;
import play.mvc.Controller;
import service.CreditLogService;
import service.CustomerService;
import util.Constant;
import util.DateUtil;
import util.ExtPage;
import util.JSONResult;
import util.MyRandom;
import util.PageUtil;
import util.Sp;
import util.Sqler;

public class CashGiftApp extends Controller{
	
	public static void getList(int start,int limit,int page,String sdate,String edate,String sort,String queryKey)throws Exception{
		Sqler sql =new Sqler("select * from mb_cash_gift");
		Sqler cntsql =new Sqler("select count(1) from mb_cash_gift");
		sql.and().left().left().equals("status", 1).right();
		cntsql.and().left().left().like("status", 1).right();
		
	
		sql.or().left().equals("transferflag", 1);
		sql.and().equals("status", 3).right().right();
		cntsql.or().left().equals("transferflag", 1);
		cntsql.and().equals("status", 3).right().right();
		
		if(!PageUtil.blank(queryKey)){
			sql.and().left().like("login_name", queryKey);
			cntsql.and().left().like("login_name", queryKey);
			sql.or().like("real_name", queryKey);
			cntsql.or().like("real_name", queryKey);
			sql.or().like("gift_no", queryKey);
			cntsql.or().like("gift_no", queryKey);
			sql.or().like("gift_code", queryKey);
			cntsql.or().like("gift_code", queryKey);
			sql.or().like("gift_type", queryKey).right();
			cntsql.or().like("gift_type", queryKey).right();
		}
		if(!(sdate==null||"".equals(sdate))){
			Date date =DateUtil.stringToDate(sdate, "yyyy-MM-dd");
			sql.and().ebigger("cs_date",date);
			cntsql.and().ebigger("cs_date",date);
		}
		if(!(edate==null||"".equals(edate))){
			Date date =DateUtil.stringToDate(edate, "yyyy-MM-dd");
			sql.and().esmaller("cs_date",date);
			cntsql.and().esmaller("cs_date",date);
		}
		sql.orberByDesc("cs_date");
		List<CashGift> roleList=Sp.get().getDefaultJdbc().query(PageUtil.page(sql.getSql(),start,limit),sql.getParams().toArray(new Object[0]),new CashGiftRowMap());
		int count=Sp.get().getDefaultJdbc().queryForObject(cntsql.getSql(),cntsql.getParams().toArray(new Object[0]),Integer.class);
		ExtPage p =new ExtPage();
		p.data=JSONResult.conver(roleList,true);
		p.total=count; 
		renderJSON(p);
	}
	
	public static void getAllList(int start,int limit,int page,String sdate,String edate,String sort,String queryKey,String gift_type)throws Exception{
		Sqler sql =new Sqler("select * from mb_cash_gift");
		Sqler cntsql =new Sqler("select count(1) from mb_cash_gift");
		if(!PageUtil.blank(queryKey)){
			sql.and().left().like("login_name", queryKey);
			cntsql.and().left().like("login_name", queryKey);
			sql.or().like("real_name", queryKey);
			cntsql.or().like("real_name", queryKey);
			sql.or().like("gift_no", queryKey);
			cntsql.or().like("gift_no", queryKey);
			sql.or().like("gift_code", queryKey).right();
			cntsql.or().like("gift_code", queryKey).right();
			
			
			
		}
		if(!PageUtil.blank(gift_type)){
			sql.and().left().equals("gift_type", gift_type).right();
			cntsql.and().left().equals("gift_type", gift_type).right();
		}
		
		if(!(sdate==null||"".equals(sdate))){
			Date date =DateUtil.stringToDate(sdate, "yyyy-MM-dd");
			sql.and().ebigger("cs_date",date);
			cntsql.and().ebigger("cs_date",date);
		}
		if(!(edate==null||"".equals(edate))){
			Date date =DateUtil.stringToDate(edate, "yyyy-MM-dd");
			sql.and().esmaller("cs_date",date);
			cntsql.and().esmaller("cs_date",date);
		}
		sql.orberByDesc("cs_date");
		List<CashGift> roleList=Sp.get().getDefaultJdbc().query(PageUtil.page(sql.getSql(),start,limit),sql.getParams().toArray(new Object[0]),new CashGiftRowMap());
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
		 List<CashGift> giftList=new ArrayList<CashGift>();
		 String giftCode=MyRandom.getRandom(8);
         for (int  j= 0; j < sheet.getRows(); j++) {
        	 if(j==0)continue;
        	 try{
        	 CashGift gift =new CashGift();
        	 gift.gift_code="a"+giftCode;
        	 gift.login_name=sheet.getCell(0,j).getContents();
        	 gift.deposit_credit=new BigDecimal(sheet.getCell(1,j).getContents());
        	 gift.valid_credit=new BigDecimal(sheet.getCell(2,j).getContents());
        	 gift.net_credit=new BigDecimal(sheet.getCell(3,j).getContents());
        	 gift.rate=Float.valueOf(sheet.getCell(4,j).getContents());
        	 gift.payout=new BigDecimal(sheet.getCell(5,j).getContents());
        	 gift.gift_type=sheet.getCell(6,j).getContents();
        	 gift.status=1;
        	 gift.gift_no=OrderNo.createLocalNo("GI");
     		 Customer cust=Customer.NTgetCustomerByLoginName(gift.login_name);
     		 if(cust==null){
     			renderText(JSONResult.failure("处理"+(j+1)+"行出现异常:"+gift.login_name+"系统不存在该用户。"));
     		 }
     		 gift.cust_id=cust.cust_id;
        	 gift.cs_date=new Date(System.currentTimeMillis());
        	 gift.real_name=cust.real_name;
        	 gift.cust_level=cust.cust_level;
           	 gift.kh_date=cust.create_date;
        	 gift.create_user=session.get(Constant.userName);
        	 gift.create_date=new Date(System.currentTimeMillis());
        	 giftList.add(gift);
        	 }catch(Exception e){
        		 e.printStackTrace();
        		 renderText(JSONResult.failure("处理"+(j+1)+"行出现异常:"+e.getMessage()));
        	 }
         }
	     workBook.close();
	     gift_file.delete();
	     for(CashGift gift:giftList){ 
	    	 try{
	    	    gift.NTcreat();
	    	 }catch(Exception e){
	    		 renderText(JSONResult.failure("导入"+gift.login_name+"记录失败:"+e.getMessage()));
	    	 }
	     }
		renderText(JSONResult.success("导入成功，共导入"+giftList.size()+"条记录!"));
		
	}
	
	
	public static void detail(Long giftId){
		CashGift gift=CashGift.NTgetGift(giftId);
		if(gift==null){
			renderText("不存在该礼金提案");
		}
		DictRender rd=new DictRender();
		render(gift,rd);
	}
	
	/**
	 * 
	 */
	public static void deleteGift(Long giftId){
		CashGift gift=CashGift.NTgetGift(giftId);
		if(gift==null){
			renderText(JSONResult.failure("不存在该礼金提案"));
		}
		if(gift.status!=1){
			renderText(JSONResult.failure("该礼金提案不容许删除。只能删除未处理的礼金提案。"));
		}
		if(CashGift.NTdeleteGift(giftId)){
			renderText(JSONResult.success("删除成功。"));
		}
		renderText(JSONResult.failure("删除失败。"));
	}
	
	public static void deleteGifts(String giftId){
		List<CashGift> gifts= CashGift.NTgetGifts(giftId);
		if(gifts==null){
			renderText(JSONResult.failure("不存在该礼金提案"));
		}
		for(CashGift gift:gifts){
			if(gift.status!=1){
				renderText(JSONResult.failure("该礼金提案不容许删除。只能删除未处理的礼金提案。"));
			}
		}
		
		if(CashGift.NTdeleteGifts(giftId)){
			renderText(JSONResult.success("删除成功。"));
		}
		renderText(JSONResult.failure("删除失败。"));
	}
	
	public static void completeGift(Integer flag,String remarks,Long giftId){
		CashGift gift=CashGift.NTgetGift(giftId);
		if(gift==null){
			renderText(JSONResult.failure("不存在该礼金提案"));
		}
		if(gift.status!=3){
			renderText(JSONResult.failure("该礼金活动提案不符合完成条件。只能完结已通过的礼金提案。"));
		}
		
		if(CashGift.NTcompleteGift(giftId,flag,remarks)){
			CustomerStatus cs = CustomerStatus.NTgetCustomerByName(gift.login_name);
			if(cs != null){
				cs.resetTransferFlag();
				renderText(JSONResult.success("申请完结成功。"));
			}
			
		}
		renderText(JSONResult.failure("变更状态失败。"));
	}
	
	public static void batchDelete(String gift_code){
		int count=CashGift.NTdeleteGiftBycode(gift_code);
		if(count>0){
			renderText(JSONResult.success("删除成功。共删除"+count+"条记录。"));
		}else{
			renderText(JSONResult.failure("删除失败。没有找到符合条件的记录。"));
		}
	}
	
	/**
	 * 批量审核..
	 * @param gift_code
	 * @param flag
	 * @param remarks
	 */
	public static void batchAudit(String gift_code,Integer flag,String remarks){
		String user=session.get(Constant.userName);
		List<CashGift>  cashList=CashGift.NTgetGiftsBycode(gift_code);
		int count =0;
		for(CashGift gift:cashList){
			CashGift gift0= CashGift.NTgetGift(gift.gift_id);
			if(gift0.status!=1){
				continue;
			}
			int status =3;
			if(flag==2){
				status=2;
			}
			gift.NTAuditGift(gift0.gift_id, status, user,remarks);
			if(status==3){
				CustomerService.modCredit(gift0.cust_id,CreditLogService.Gift, 
						gift.login_name,gift.payout,user, "添加礼金"+gift.gift_type, gift.gift_no);
			}
			count++;
		}
		if(count>0){
			renderText(JSONResult.success("审核成功。共审核"+count+"条记录。"));
		}else{
			renderText(JSONResult.failure("审核失败。没有找到符合条件的记录。"));
		}
	}
	
	public static void audit(Integer flag,String remarks,Long giftId){
		CashGift gift=CashGift.NTgetGift(giftId);

		if(gift==null){
			 renderText(JSONResult.failure("请求的提款提案不存在!"));
		}
		if(gift.status!=1){
			 renderText(JSONResult.failure("操作失败，该礼金提案已经被处理!"));
		}
		Customer cust=Customer.NTgetCustomerByLoginName(gift.login_name);
		if(cust==null){
			 renderText(JSONResult.failure("提案的客户不存在!"));
		}
		int status =3;
		if(flag==-2){
			status=2;
		}
		String user=session.get(Constant.userName);
		List<Item> items=Item.NTbygroupCode("gift_type");
		boolean hasType=false;
		for(Item item:items){
			if(item.itemvalue.equals(gift.gift_type)){
				hasType=true;
				break;
			}
		}
		if(!hasType){
			renderText(JSONResult.failure(gift.gift_type+"该礼金类型不存在!"));
		}
		
		if(status==3){
			Item item = Item.getItemByItemname(gift.gift_type);
			if(item != null){
				
				boolean bl = CustomerService.createOrderNumber(giftId+"");
				
				gift.NTAuditGift(giftId, status, user,remarks);
				gift.NTupdate(status, gift.gift_no);//修改申请订单状态
				CustomerService.modCredit(cust.cust_id,CreditLogService.Gift, 
							gift.login_name,gift.payout,user, "添加礼金"+gift.gift_type, gift.gift_no);
				
				
			}else{
				renderText(JSONResult.failure(gift.gift_type+"该字典名不存在"));
			}	
		}else{
			gift.NTupdate(status, gift.gift_no);//修改申请订单状态
			gift.NTAuditGift(giftId, status, user,remarks);
		}
		renderText(JSONResult.success("操作成功!"));
	}
	/**
	 * 添加礼金
	 * @param gift
	 */
	public static void saveGift(CashGift gift){
		 Customer cust=Customer.NTgetCustomerByLoginName(gift.login_name);
 		 if(cust==null){
 			renderText(JSONResult.failure("系统不存在该用户:"+gift.login_name+"。"));
 		 }
 		if("1".equals(params.get("kact"))){
 			gift.gift_code=MyRandom.getRandom(8);
	 		 gift.status=1;
	 		 gift.cs_date=new Date(System.currentTimeMillis());
	    	 gift.real_name=cust.real_name;
	    	 gift.cust_level=cust.cust_level;
	       	 gift.kh_date=cust.create_date;
	         gift.cust_id=cust.cust_id;
	    	 gift.create_user=session.get(Constant.userName);
	    	 gift.create_date=new Date(System.currentTimeMillis());
	    	 gift.gift_no=OrderNo.createLocalNo("GI");
 	    	 if(gift.NTcreat()!=null){
 	    		 renderText(JSONResult.success("操作成功!"));
 	    	 }
 	    	 renderText(JSONResult.failure("操作失败"));
 		}else if("2".equals(params.get("kact"))){
 			
 			 if(gift.NTupdate()){
 	    		 renderText(JSONResult.success("操作成功!"));
 	    	 }
 	    	 renderText(JSONResult.failure("操作失败"));
 		}
 		
	}
	
	public static void exportLastWeekExcel(String sdate,String edate,String sort,String queryKey){
		Sqler sql =new Sqler("select * from mb_cash_gift");
		Sqler cntsql =new Sqler("select count(1) from mb_cash_gift");
		if(!PageUtil.blank(queryKey)){
			sql.and().left().like("login_name", queryKey).right();
			cntsql.and().left().like("login_name", queryKey).right();
		}
		//if(!(sdate==null||"".equals(sdate))){
		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -7);
		cal.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
		cal.set(Calendar.HOUR_OF_DAY, 12);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
			
		//Date startdate =DateUtil.stringToDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cal.getTime()), "yyyy-MM-dd HH:mm:ss");
		sql.and().ebigger("create_date",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cal.getTime()));
		cntsql.and().ebigger("create_date",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cal.getTime()));
			
		//}
		cal.add(Calendar.DATE, 7);
		
		//Date enddate =DateUtil.stringToDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cal.getTime()), "yyyy-MM-dd HH:mm:ss");
		sql.and().smaller("create_date",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cal.getTime()));
		cntsql.and().smaller("create_date",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cal.getTime()));
		
		sql.orberByDesc("create_date");
		//System.out.println(sql.getSql());
		int count=Sp.get().getDefaultJdbc().queryForObject(cntsql.getSql(),cntsql.getParams().toArray(new Object[0]),Integer.class);
		if(count>50000){
			renderText("导出记录超过5万条，系统仅支持导出小于5万条记录,请缩短范围再导出。");
		}
		DictRender rd=new DictRender();
		try{
			
		List<CashGift> gashList=Sp.get().getDefaultJdbc().query(sql.getSql(),sql.getParams().toArray(new Object[0]),new CashGiftRowMap());
		WorkbookSettings ws = new WorkbookSettings();
		ws.setLocale(new Locale("cn", "ZH"));
		WritableWorkbook workbook = Workbook.createWorkbook(response.out);
		WritableSheet sheet = workbook.createSheet("礼金表格", 0);
		/**
		礼金日期:	11/24 12:20	上传编号:	10004898	礼单类型:	保险礼金
		用户名:	sjwangsss	客户姓名:	王娟娟	客户等级:	1
		开户时间:	2013/11/12	派彩额度:	102.1	当前状态:	2
		有效投注额:	5775	输赢额度:	-2552.5	比例:	0.04
		提交人员:	vicky	审核时间:	11/24 12:22:19	审核人员:	kenneth
		存款金额:	4000	审核备注:	k
		 */
		response.setHeader("content-disposition", "attachment; filename=CashGift_LastWeek.xls");
		response.setHeader("Content-Type", "application/excel");
        sheet.addCell(new Label(0, 0,"提交时间"));
        sheet.addCell(new Label(1, 0,"上传编号"));
        sheet.addCell(new Label(2, 0,"礼单类型"));
        sheet.addCell(new Label(3, 0,"用户名"));
        sheet.addCell(new Label(4, 0,"客户等级"));
        sheet.addCell(new Label(5, 0,"开户时间"));
        sheet.addCell(new Label(6, 0,"派彩额度"));
        sheet.addCell(new Label(7, 0,"当前状态"));
        sheet.addCell(new Label(8, 0,"有效投注额"));
        sheet.addCell(new Label(9, 0,"输赢额度"));
        sheet.addCell(new Label(10, 0,"存款金额"));
        sheet.addCell(new Label(11, 0,"提交人员"));
        sheet.addCell(new Label(12, 0,"审核时间"));
        sheet.addCell(new Label(13, 0,"审核人员"));
        sheet.addCell(new Label(14, 0,"审核备注"));
       
        for(int i=0;i< gashList.size();i++){
        	  CashGift r= gashList.get(i);
        	  sheet.addCell(new Label(0, i+1,DateUtil.dateToString(r.create_date, "yyyy-MM-dd HH:mm:ss")));
              sheet.addCell(new Label(1, i+1,String.valueOf(r.gift_code)));
              sheet.addCell(new Label(2, i+1,r.gift_type));
              sheet.addCell(new Label(3, i+1,r.login_name));
              sheet.addCell(new Label(4, i+1,rd.dictName("cust_level",""+r.cust_level)));
              sheet.addCell(new Label(5, i+1,DateUtil.dateToString(r.kh_date, "yyyy-MM-dd HH:mm:ss")));
              sheet.addCell(new Label(6, i+1,r.payout.toPlainString()));
              sheet.addCell(new Label(7, i+1,rd.status(r.status, "cashgift")));
              sheet.addCell(new Label(8, i+1,r.valid_credit.toPlainString()));
              sheet.addCell(new Label(9, i+1,r.net_credit.toPlainString()));
              sheet.addCell(new Label(10,i+1,r.deposit_credit == null ? "":r.deposit_credit.toPlainString()));
              sheet.addCell(new Label(11,i+1,r.create_user));
              sheet.addCell(new Label(12,i+1,DateUtil.dateToString(r.audit_date, "yyyy-MM-dd HH:mm:ss")));
              sheet.addCell(new Label(13,i+1,r.audit_user));
              sheet.addCell(new Label(14,i+1,r.audit_msg));
        }
        workbook.write();
		workbook.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void exportExcel(String sdate,String edate,String sort,String queryKey){
		Sqler sql =new Sqler("select * from mb_cash_gift");
		Sqler cntsql =new Sqler("select count(1) from mb_cash_gift");
		if(!PageUtil.blank(queryKey)){
			sql.and().left().like("login_name", queryKey).right();
			cntsql.and().left().like("login_name", queryKey).right();
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
		System.out.println(sql.getSql());
		int count=Sp.get().getDefaultJdbc().queryForObject(cntsql.getSql(),cntsql.getParams().toArray(new Object[0]),Integer.class);
		if(count>50000){
			renderText("导出记录超过5万条，系统仅支持导出小于5万条记录,请缩短范围再导出。");
		}
		DictRender rd=new DictRender();
		try{
			
		List<CashGift> gashList=Sp.get().getDefaultJdbc().query(sql.getSql(),sql.getParams().toArray(new Object[0]),new CashGiftRowMap());
		WorkbookSettings ws = new WorkbookSettings();
		ws.setLocale(new Locale("cn", "ZH"));
		WritableWorkbook workbook = Workbook.createWorkbook(response.out);
		WritableSheet sheet = workbook.createSheet("礼金表格", 0);
		/**
		礼金日期:	11/24 12:20	上传编号:	10004898	礼单类型:	保险礼金
		用户名:	sjwangsss	客户姓名:	王娟娟	客户等级:	1
		开户时间:	2013/11/12	派彩额度:	102.1	当前状态:	2
		有效投注额:	5775	输赢额度:	-2552.5	比例:	0.04
		提交人员:	vicky	审核时间:	11/24 12:22:19	审核人员:	kenneth
		存款金额:	4000	审核备注:	k
		 */
		response.setHeader("content-disposition", "attachment; filename=CashGift_"+sdate+"_"+edate+".xls");
		response.setHeader("Content-Type", "application/excel");
        sheet.addCell(new Label(0, 0,"提交时间"));
        sheet.addCell(new Label(1, 0,"上传编号"));
        sheet.addCell(new Label(2, 0,"礼单类型"));
        sheet.addCell(new Label(3, 0,"用户名"));
        sheet.addCell(new Label(4, 0,"客户等级"));
        sheet.addCell(new Label(5, 0,"开户时间"));
        sheet.addCell(new Label(6, 0,"派彩额度"));
        sheet.addCell(new Label(7, 0,"当前状态"));
        sheet.addCell(new Label(8, 0,"有效投注额"));
        sheet.addCell(new Label(9, 0,"输赢额度"));
        sheet.addCell(new Label(10, 0,"存款金额"));
        sheet.addCell(new Label(11, 0,"提交人员"));
        sheet.addCell(new Label(12, 0,"审核时间"));
        sheet.addCell(new Label(13, 0,"审核人员"));
        sheet.addCell(new Label(14, 0,"审核备注"));
       
        for(int i=0;i< gashList.size();i++){
        	  CashGift r= gashList.get(i);
        	  sheet.addCell(new Label(0, i+1,DateUtil.dateToString(r.create_date, "yyyy-MM-dd HH:mm:ss")));
              sheet.addCell(new Label(1, i+1,String.valueOf(r.gift_code)));
              sheet.addCell(new Label(2, i+1,r.gift_type));
              sheet.addCell(new Label(3, i+1,r.login_name));
              sheet.addCell(new Label(4, i+1,rd.dictName("cust_level",""+r.cust_level)));
              sheet.addCell(new Label(5, i+1,DateUtil.dateToString(r.kh_date, "yyyy-MM-dd HH:mm:ss")));
              sheet.addCell(new Label(6, i+1,r.payout.toPlainString()));
              sheet.addCell(new Label(7, i+1,rd.status(r.status, "cashgift")));
              sheet.addCell(new Label(8, i+1,r.valid_credit.toPlainString()));
              sheet.addCell(new Label(9, i+1,r.net_credit.toPlainString()));
              sheet.addCell(new Label(10,i+1,r.deposit_credit == null ? "":r.deposit_credit.toPlainString()));
              sheet.addCell(new Label(11,i+1,r.create_user));
              sheet.addCell(new Label(12,i+1,DateUtil.dateToString(r.audit_date, "yyyy-MM-dd HH:mm:ss")));
              sheet.addCell(new Label(13,i+1,r.audit_user));
              sheet.addCell(new Label(14,i+1,r.audit_msg));
        }
        workbook.write();
		workbook.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
