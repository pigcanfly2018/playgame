package controllers;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import models.CreditFix;
import models.Customer;
import models.DictRender;
import models.OrderNo;
import models.Transfer;
import models.TransferRowMap;
import play.mvc.Controller;
import util.Constant;
import util.DateUtil;
import util.ExtPage;
import util.JSONResult;
import util.PageUtil;
import util.Sp;
import util.Sqler;

public class TransferApp extends Controller{
	public static void getList(int start, int limit, int page, String sdate,String edate, String sort,
			String queryKey) throws Exception {
		Sqler sql = new Sqler("select * from mb_transfer");
		Sqler cntsql = new Sqler("select count(1) from mb_transfer");
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
		List<Transfer> roleList = Sp.get().getDefaultJdbc().query(PageUtil.page(sql.getSql(), start, limit),
						sql.getParams().toArray(new Object[0]),new TransferRowMap());
		int count = Sp.get().getDefaultJdbc().queryForObject(cntsql.getSql(),
				cntsql.getParams().toArray(new Object[0]),Integer.class);
		ExtPage p = new ExtPage();
		p.data = JSONResult.conver(roleList, true);
		p.total = count;
		renderJSON(p);
	}

	public static void saveTransfer(Transfer transfer) throws Exception {
		if (transfer.credit.intValue() == 0) {
			renderText(JSONResult.failure("转账额度为0，无需转账!"));
		}
		if (transfer.credit.intValue() < 0) {
			renderText(JSONResult.failure("转账额度不可为负数!"));
		}
		if (!Customer.NTexist(transfer.login_name)) {
			renderText(JSONResult.failure(transfer.login_name + "用户名不存在!"));
		}
		Customer cust = Customer.NTgetCustomer(transfer.login_name);
		String user = session.get(Constant.userName);
		transfer.transfer_no=OrderNo.createLocalNo("TR");
		transfer.status=1;
		transfer.create_user=user;
		transfer.cust_id=cust.cust_id;
        if(transfer.NTcreat()){
			renderText(JSONResult.success("提交成功!"));
		}
		renderText(JSONResult.failure("提交失败!"));
	}
	
	public static void detail(Long id){
		Transfer transfer=Transfer.NTget(id);
		if(transfer==null){
			 renderText(JSONResult.failure("请求的转账单不存在!"));
		}
		DictRender rd =new DictRender();
		render("/Detail/transfer.html",transfer,rd);
	}
	
	public static void cancle(Long id,String remarks){
		Transfer transfer=Transfer.NTget(id);
	    if(transfer==null){
	        renderText(JSONResult.failure("提案不存在!"));
	    }
	    if(transfer.status!=1){
	    	renderText(JSONResult.failure("提案不容许废除!"));
	    }
		String user = session.get(Constant.userName);
	   if(Transfer.NTaudit(id, 2, user, remarks)){
		   renderText(JSONResult.success("操作成功!")); 
	    }
	   renderText(JSONResult.failure("提交失败!"));
	
   }
	
	public static void audit(Integer flag,String remarks,Long id){
		Transfer transfer=Transfer.NTget(id);
		if(transfer==null){
			 renderText(JSONResult.failure("请求的提案不存在!"));
		}
		if(transfer.status!=1){
			 renderText(JSONResult.failure("操作失败，该提案已经被处理!"));
		}
		String user=session.get(Constant.userName);
		int status=3;
		if(flag==-2){
			status=2;
		}
	    if(Transfer.NTaudit(id, status, user, remarks)){
		   renderText(JSONResult.success("操作成功!")); 
	    }
	   renderText(JSONResult.failure("提交失败!"));
	}
	

}
