package controllers;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import models.Agent;
import models.AgentWithdraw;
import models.AgentWithdrawLog;
import models.AgentWithdrawRowMap;
import models.Customer;
import models.DictRender;
import models.Letter;
import models.Msg;
import models.OrderNo;
import models.Withdraw;
import models.WithdrawLog;
import models.WithdrawRowMap;

import org.apache.log4j.Logger;

import play.mvc.Controller;
import play.mvc.With;
import service.AgentCreditLogService;
import service.AgentService;
import service.CreditLogService;
import service.CustomerService;
import util.Constant;
import util.DateUtil;
import util.ExtPage;
import util.JSONResult;
import util.PageUtil;
import util.Sp;
import util.Sqler;
import util.Sqler;
@With(value = {AjaxSecure.class})
public class AgentWithdrawApp extends Controller{
	
	private static Logger logger=Logger.getLogger(AgentWithdrawApp.class);
	public static void getList(int start,int limit,int page,String sdate,String edate,String sort,String queryKey)throws Exception{
		Sqler sql =new Sqler("select * from mb_agent_withdraw");
		Sqler cntsql =new Sqler("select count(1) from mb_agent_withdraw");
		sql.and().left().equals("status", 1).or().equals("status", 3).right();
		cntsql.and().left().like("status", 1).or().equals("status", 3).right();
		if(!PageUtil.blank(queryKey)){
			sql.and().left().like("login_name", queryKey);
			cntsql.and().left().like("login_name", queryKey);
			sql.or().like("real_name", queryKey).right();
			cntsql.or().like("real_name", queryKey).right();
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
		List<AgentWithdraw> roleList=Sp.get().getDefaultJdbc().query(PageUtil.page(sql.getSql(),start,limit),sql.getParams().toArray(new Object[0]),new AgentWithdrawRowMap());
		int count=Sp.get().getDefaultJdbc().queryForObject(cntsql.getSql(),cntsql.getParams().toArray(new Object[0]),Integer.class);
		ExtPage p =new ExtPage();
		p.data=JSONResult.conver(roleList,true);
		p.total=count; 
		renderJSON(p);
	}
	
	public static void detail(Long request_id){
		AgentWithdraw withdraw=AgentWithdraw.NTget(request_id);
		if(withdraw==null){
			 renderText(JSONResult.failure("请求的提款提案不存在!"));
		}
		List<AgentWithdrawLog> logList=AgentWithdrawLog.NTgetLogsByWit(withdraw.awithdraw_id);
		DictRender rd=new DictRender();
		render(withdraw,logList,rd);
	}
	
	public static void cancle(Long request_id,String remarks){
		AgentWithdraw withdraw=AgentWithdraw.NTget(request_id);
		if(withdraw==null){
			 renderText(JSONResult.failure("请求的提款提案不存在!"));
		}
		if(withdraw.locked){
			renderText(JSONResult.failure("该提案已经被锁，您目前无法操作该提案!"));
		}
		if(withdraw.status!=1){
			 renderText(JSONResult.failure("操作失败，该提案已经被处理!"));
		}
		Agent agent=Agent.NTgetAgent(withdraw.agent_id);
		if(agent==null){
			 renderText(JSONResult.failure("提案的代理不存在!"));
		}
		String user=session.get(Constant.userName);
	
		if(AgentService.modCredit(agent.agent_id,AgentCreditLogService.CancleWithdraw, 
				withdraw.login_name,withdraw.amount,user, "取消提款", withdraw.awit_no)){
			AgentWithdrawLog.NTcreat(withdraw.status, 2, withdraw.awithdraw_id, remarks, user);
			AgentWithdraw.NTchangeStatus(withdraw.awithdraw_id, 2);
			
			//提款不成功 发送站内信
//			StringBuffer sb =new StringBuffer();
//			sb.append("<p>尊敬的客户，您有一笔提款没有处理成功。提款单号为"+withdraw.wit_no+"，提交时间："+DateUtil.dateToString(withdraw.create_date, "yyyy-MM-dd HH:mm:ss")+"，金额为"+withdraw.amount+"，原因是："+remarks+"</p>");
//			sb.append("<p style=\"text-align:right\">八达国际客服部上</p>");
//		    Letter.NTcreat(withdraw.cust_id, withdraw.login_name, "提款金额为"+withdraw.amount+"的提款未处理成功", sb.toString(), withdraw.login_name, true);
//	
		    
			renderText(JSONResult.success("取消提案成功!"));
		}
		 renderText(JSONResult.failure("操作失败!"));
	
	}
	
	
	public static void saveWithdraw(AgentWithdraw withdraw){
		if(withdraw.amount.intValue()<100){
			renderText(JSONResult.failure("提款金额最少为100元!"));
		}
		Agent agent=Agent.getAgent(withdraw.login_name);
		if(agent==null){
			renderText(JSONResult.failure(withdraw.login_name+"代理不存在!"));
		}
		String ip=IpApp.getIpAddr();
		if(agent.credit==null)agent.credit=new BigDecimal(0);
		
		
		BigDecimal totalCredit=agent.credit;
		if(totalCredit.floatValue()<withdraw.amount.floatValue()){
			renderText(JSONResult.failure("代理余额不足，代理余额为:"+totalCredit+"。"));
		}
		String user=session.get(Constant.userName);
		withdraw.awit_no=OrderNo.createLocalNo("AWI");
		withdraw.status=1;
		withdraw.real_name=agent.real_name;
		withdraw.agent_id=agent.agent_id;
		withdraw.create_date=new Date(System.currentTimeMillis());
		withdraw.create_user=user;
		
		
		
		if(AgentService.modCredit(agent.agent_id,AgentCreditLogService.Withdraw, withdraw.login_name,
				new BigDecimal(0).subtract(withdraw.amount),
				user, "代理提款", withdraw.awit_no)){
			try{
				Long withdraw_id=withdraw.NTcreat();
				WithdrawLog.NTcreat(0, 1, withdraw_id, withdraw.remarks, user);
				// Msg.NTcreateMsg(5, "客户"+cust.real_name+"提款"+withdraw.amount+"元，请及时处理!");
			     renderText(JSONResult.success("创建提案成功!"));
			}catch(Exception e){
				System.out.println("创建提款提案失败!");
			}
			renderText(JSONResult.failure("创建提款提案失败。"));
		}
		renderText(JSONResult.failure("创建提款提案失败，扣款不成功。"));

	
	}
	
	
	/**
	 * 审核(通过与不通过)
	 * @param request_id
	 * @param remarks
	 */
	public static void audit(Integer flag,String remarks,Long request_id){
		AgentWithdraw withdraw=AgentWithdraw.NTget(request_id);
		if(withdraw==null){
			 renderText(JSONResult.failure("请求的提款提案不存在!"));
		}
		if(withdraw.locked){
			renderText(JSONResult.failure("该提案已经被锁，您目前无法操作该提案!"));
		}
		if(withdraw.status!=1){
			 renderText(JSONResult.failure("操作失败，该提案已经被处理!"));
		}
		Agent agent=Agent.NTgetAgent(withdraw.agent_id);
		if(agent==null){
			 renderText(JSONResult.failure("提案的代理不存在!"));
		}
		String user=session.get(Constant.userName);
		int status=3;
		if(flag==-2){
			status=2;
			AgentService.modCredit(agent.agent_id,CreditLogService.CancleWithdraw, 
					withdraw.login_name,withdraw.amount,user, "拒绝提款", withdraw.awit_no);
			//提款不成功 发送站内信
//			StringBuffer sb =new StringBuffer();
//			sb.append("<p>尊敬的客户，您有一笔提款没有处理成功。提款单号为"+withdraw.wit_no+"，提交时间："+DateUtil.dateToString(withdraw.create_date, "yyyy-MM-dd HH:mm:ss")+"，金额为"+withdraw.amount+"，原因是："+remarks+"</p>");
//			sb.append("<p style=\"text-align:right\">八达国际客服部上</p>");
//		    Letter.NTcreat(withdraw.cust_id, withdraw.login_name, "提款金额为"+withdraw.amount+"的提款未处理成功", sb.toString(), withdraw.login_name, true);
		}
		AgentWithdrawLog.NTcreat(withdraw.status,status, withdraw.awithdraw_id, remarks, user);
		AgentWithdraw.NTchangeStatus(withdraw.awithdraw_id, status);
		renderText(JSONResult.success("操作成功!"));
		
	}
	
	
	/**
	 * 支付
	 * @param request_id
	 * @param remarks
	 */
	public static void pay(Integer flag,Long request_id,String remarks){
		AgentWithdraw withdraw=AgentWithdraw.NTget(request_id);
		if(withdraw==null){
			 renderText(JSONResult.failure("请求的提款提案不存在!"));
		}
		if(withdraw.locked){
			renderText(JSONResult.failure("该提案已经被锁，您目前无法操作该提案!"));
		}
		if(withdraw.status!=3){
			 renderText(JSONResult.failure("操作失败，该提案已经被处理!"));
		}
		Agent agent=Agent.NTgetAgent(withdraw.agent_id);
		if(agent==null){
			 renderText(JSONResult.failure("提案的代理不存在!"));
		}
		String user=session.get(Constant.userName);
		int status=5;
		if(flag==-2){
			status=4;
			AgentService.modCredit(agent.agent_id,CreditLogService.CancleWithdraw, 
					withdraw.login_name,withdraw.amount,user, "拒绝支付", withdraw.awit_no);
			//提款不成功 发送站内信
//			StringBuffer sb =new StringBuffer();
//			sb.append("<p>尊敬的客户，您有一笔提款没有处理成功。提款单号为"+withdraw.wit_no+"，提交时间："+DateUtil.dateToString(withdraw.create_date, "yyyy-MM-dd HH:mm:ss")+"，金额为"+withdraw.amount+"，原因是："+remarks+"</p>");
//			sb.append("<p style=\"text-align:right\">八达国际客服部上</p>");
//		    Letter.NTcreat(withdraw.cust_id, withdraw.login_name, "提款金额为"+withdraw.amount+"的提款未处理成功", sb.toString(), withdraw.login_name, true);
		}
		AgentWithdrawLog.NTcreat(withdraw.status,status, withdraw.awithdraw_id, remarks, user);
		AgentWithdraw.NTchangeStatus(withdraw.awithdraw_id, status);
		if(status==5){
			if(agent.account==null||agent.account.length()<7){
				Agent.NTmodAgentBank(withdraw.bank_name, withdraw.account_type, withdraw.location,
						withdraw.account, agent.agent_id);
			}
			if(AgentWithdraw.NTgetCount(withdraw.agent_id)==1){
				AgentWithdraw.NTrecPayDate(withdraw.awithdraw_id);
			}
		}
		renderText(JSONResult.success("操作成功!"));
	}
	
	public static void getAllList(int start,int limit,int page,String sdate,String edate,String sort,String queryKey)throws Exception{
		Sqler sql =new Sqler("select * from mb_agent_withdraw");
		Sqler cntsql =new Sqler("select count(1) from mb_agent_withdraw");
		if(!PageUtil.blank(queryKey)){
			sql.and().left().equals("login_name", queryKey);
			cntsql.and().left().equals("login_name", queryKey);
    		sql.or().equals("awit_no", queryKey);
			cntsql.or().equals("awit_no", queryKey);
			sql.or().like("real_name", queryKey).right();
			cntsql.or().like("real_name", queryKey).right();
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
		List<AgentWithdraw> roleList=Sp.get().getDefaultJdbc().query(PageUtil.page(sql.getSql(),start,limit),sql.getParams().toArray(new Object[0]),new AgentWithdrawRowMap());
		int count=Sp.get().getDefaultJdbc().queryForObject(cntsql.getSql(),cntsql.getParams().toArray(new Object[0]),Integer.class);
		ExtPage p =new ExtPage();
		p.data=JSONResult.conver(roleList,true);
		p.total=count; 
		renderJSON(p);
	}
	

}
