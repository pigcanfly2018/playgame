package controllers;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import models.CreditFix;
import models.CreditFixRowMap;
import models.CreditLog;
import models.Custform;
import models.Customer;
import models.CustomerStatus;
import models.Deposit;
import models.DepositLog;
import models.DictRender;
import models.OrderNo;
import models.Withdraw;
import models.WithdrawLog;

import org.apache.log4j.Logger;

import com.ws.service.PlatService;

import play.mvc.Controller;
import service.CreditLogService;
import service.CustomerService;
import util.Constant;
import util.DateUtil;
import util.ExtPage;
import util.JSONResult;
import util.PageUtil;
import util.Sp;
import util.Sqler;

public class CreditfixApp extends Controller {
	private static Logger logger = Logger.getLogger(CreditfixApp.class);

	public static void getList(int start, int limit, int page, String sdate,
			String edate, String sort, String queryKey) throws Exception {
		Sqler sql = new Sqler("select * from mb_credit_fix");
		Sqler cntsql = new Sqler("select count(1) from mb_credit_fix");
		if (!PageUtil.blank(queryKey)) {
			sql.and().left().like("login_name", queryKey).right();
			cntsql.and().left().like("login_name", queryKey).right();
		}
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
		List<CreditFix> roleList = Sp.get().getDefaultJdbc().query(PageUtil.page(sql.getSql(), start, limit),
						sql.getParams().toArray(new Object[0]),new CreditFixRowMap());
		int count = Sp.get().getDefaultJdbc().queryForObject(cntsql.getSql(),cntsql.getParams().toArray(new Object[0]),Integer.class);
		ExtPage p = new ExtPage();
		p.data = JSONResult.conver(roleList, true);
		p.total = count;
		renderJSON(p);
	}

	public static void saveCreditfix(CreditFix fix,Boolean fix_action) throws Exception {
		if (fix.credit.intValue() == 0) {
			renderText(JSONResult.failure("修正额度为0，无需修正!"));
		}
		if (fix.credit.intValue() < 0) {
			renderText(JSONResult.failure("修正额度不可为负数!"));
		}
		if (!Customer.NTexist(fix.login_name)) {
			renderText(JSONResult.failure(fix.login_name + "用户名不存在!"));
		}
		Customer cust = Customer.NTgetCustomer(fix.login_name);
		String user = session.get(Constant.userName);
        if(!fix_action){
        	BigDecimal zore = new BigDecimal(0);
        	fix.credit=zore.subtract(fix.credit);
        }
        fix.fix_no=OrderNo.createLocalNo("FI");
        fix.status=1;
        fix.create_user=user;
        fix.cust_id=cust.cust_id;
    	String ip=IpApp.getIpAddr();
    	
        if(fix.credit.intValue()<0){
        	if(cust.credit==null)cust.credit=new BigDecimal(0);
        	PlatService.transferOutAll(cust.login_name, ip, "额度修正", fix.remarks, user, ""+System.currentTimeMillis(), null);
        	cust = Customer.NTgetCustomer(fix.login_name);
    		BigDecimal totalCredit=cust.credit;
    		if(totalCredit.intValue()<(-fix.credit.intValue())){
    			renderText(JSONResult.failure("客户余额不足，客户余额为:"+totalCredit+"。"));
    		}
    		   	
        	if(CustomerService.modCredit(cust.cust_id,
					CreditLogService.Fix,
					cust.login_name,
					fix.credit,
					user, 
					"扣除点数", 
					fix.fix_no)){
        		 if(fix.NTcreat()){
        				renderText(JSONResult.success("提交成功!"));
        			}
        	}
        }else{
        	if(fix.NTcreat()){
				renderText(JSONResult.success("提交成功!"));
			}
        	
        }
        
       
		renderText(JSONResult.failure("提交失败!"));
	}
	
	public static void detail(Long id){
		CreditFix fix=CreditFix.NTget(id);
		if(fix==null){
			 renderText(JSONResult.failure("请求的提案不存在!"));
		}
		DictRender rd =new DictRender();
		render(fix,rd);
	}
	
	public static void cancle(Long id,String remarks){
		CreditFix fix=CreditFix.NTget(id);
	    if(fix==null){
	        renderText(JSONResult.failure("提案不存在!"));
	    }
	    if(fix.status!=1){
	    	renderText(JSONResult.failure("提案不容许废除!"));
	    }
		String user = session.get(Constant.userName);
		Customer cust = Customer.NTgetCustomer(fix.login_name);
		
		 if(fix.credit.intValue()<0){
			if(CustomerService.modCredit(cust.cust_id,
					CreditLogService.Fix,
					cust.login_name,
					new BigDecimal(0).subtract(fix.credit),
					user, 
					"废除扣点", 
					fix.fix_no)){
				      if(CreditFix.NTaudit(id, 2, user, remarks)){
					   renderText(JSONResult.success("操作成功!")); 
				       }
					}
		 }else{
			 if(CreditFix.NTaudit(id, 2, user, remarks)){
				   renderText(JSONResult.success("操作成功!")); 
			    }
		 }
				
	   
	   renderText(JSONResult.failure("提交失败!"));
	
   }
	
	public static void audit(Integer flag,String remarks,Long id){
		CreditFix fix=CreditFix.NTget(id);
		if(fix==null){
			 renderText(JSONResult.failure("请求的提案不存在!"));
		}
		if(fix.status!=1){
			 renderText(JSONResult.failure("操作失败，该提案已经被处理!"));
		}
		String user=session.get(Constant.userName);
		int status=3;
		if(flag==-2){
			status=2;
		}
		if(status==3){
			if(fix.credit.intValue()>0){
				Customer cust = Customer.NTgetCustomer(fix.login_name);
				CustomerService.modCredit(cust.cust_id,
						CreditLogService.Fix,
						cust.login_name,
						fix.credit,
						user, 
						"同意加点", 
						fix.fix_no);
			}
		}
		if(status==2){
			if(fix.credit.intValue()<0){
				Customer cust = Customer.NTgetCustomer(fix.login_name);
				CustomerService.modCredit(cust.cust_id,
						CreditLogService.Fix,
						cust.login_name,
						new BigDecimal(0).subtract(fix.credit),
						user, 
						"拒绝扣点", 
						fix.fix_no);
			}
		}
		if(CreditFix.NTaudit(id, status, user, remarks)){
			   renderText(JSONResult.success("操作成功!")); 
		 }
	    
	   renderText(JSONResult.failure("提交失败!"));
	}
}
