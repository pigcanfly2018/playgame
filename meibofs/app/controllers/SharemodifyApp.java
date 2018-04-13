package controllers;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import models.Agent;
import models.CreditFix;
import models.CreditFixRowMap;
import models.Customer;
import models.DictRender;
import models.OrderNo;
import models.ShareModify;
import models.ShareModifyRowMap;

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

public class SharemodifyApp extends Controller {
	
	private static Logger logger = Logger.getLogger(SharemodifyApp.class);
	
	public static void getList(int start, int limit, int page, String sdate,
			String edate, String sort, String queryKey) throws Exception {
		Sqler sql = new Sqler("select * from mb_share_modify");
		Sqler cntsql = new Sqler("select count(1) from mb_share_modify");
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
		List<ShareModify> roleList = Sp.get().getDefaultJdbc().query(PageUtil.page(sql.getSql(), start, limit),
						sql.getParams().toArray(new Object[0]),new ShareModifyRowMap());
		int count = Sp.get().getDefaultJdbc().queryForObject(cntsql.getSql(),cntsql.getParams().toArray(new Object[0]),Integer.class);
		ExtPage p = new ExtPage();
		p.data = JSONResult.conver(roleList, true);
		p.total = count;
		renderJSON(p);
	}
	
	
	public static void saveSharemodify(ShareModify mod) throws Exception {
		if (mod.share_after.intValue() == mod.share_before.intValue()) {
			renderText(JSONResult.failure("修正前后分成一致，无需修正!"));
		}
		
		if (!Agent.NTexist(mod.login_name)) {
			renderText(JSONResult.failure(mod.login_name + "代理用户名不存在!"));
		}
		Agent agent = Agent.getAgent(mod.login_name);
		String user = session.get(Constant.userName);
        
		mod.modify_no=OrderNo.createLocalNo("MO");
		mod.status=1;
		mod.create_user=user;
		mod.agent_id=agent.agent_id;
    	
        if(mod.NTcreat()){
			renderText(JSONResult.success("提交成功!"));
		}
        	
        
        
       
		renderText(JSONResult.failure("提交失败!"));
	}
	
	public static void detail(Long id){
		ShareModify mod=ShareModify.NTget(id);
		if(mod==null){
			 renderText(JSONResult.failure("请求的提案不存在!"));
		}
		DictRender rd =new DictRender();
		render(mod,rd);
	}
	
	public static void cancle(Long id,String remarks){
		ShareModify mod=ShareModify.NTget(id);
		if(mod==null){
			 renderText(JSONResult.failure("请求的提案不存在!"));
		}
	    if(mod.status!=1){
	    	renderText(JSONResult.failure("提案不容许废除!"));
	    }
		String user = session.get(Constant.userName);
		
		if(ShareModify.NTaudit(id, 2, user, remarks)){
			renderText(JSONResult.success("操作成功!")); 
		}
		 
				
	   
	   renderText(JSONResult.failure("提交失败!"));
	
   }
	
	public static void audit(Integer flag,String remarks,Long id){
		ShareModify mod=ShareModify.NTget(id);
		if(mod==null){
			 renderText(JSONResult.failure("请求的提案不存在!"));
		}
		if(mod.status!=1){
			 renderText(JSONResult.failure("操作失败，该提案已经被处理!"));
		}
		String user=session.get(Constant.userName);
		int status=3;
		if(flag==-2){
			status=2;
		}
		if(status==3){
			Agent.modShare(mod.login_name, mod.share_after);
		}
//		if(status==2){
//			if(fix.credit.intValue()<0){
//				Customer cust = Customer.NTgetCustomer(fix.login_name);
//				CustomerService.modCredit(cust.cust_id,
//						CreditLogService.Fix,
//						cust.login_name,
//						new BigDecimal(0).subtract(fix.credit),
//						user, 
//						"拒绝扣点", 
//						fix.fix_no);
//			}
//		}
		
		if(ShareModify.NTaudit(id, status, user, remarks)){
			   renderText(JSONResult.success("操作成功!")); 
		 }
	    
	   renderText(JSONResult.failure("提交失败!"));
	}

}
