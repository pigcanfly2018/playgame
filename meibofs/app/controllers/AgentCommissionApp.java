package controllers;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

import play.mvc.Controller;
import models.Agent;
import models.AgentCommission;
import models.AgentCommissionRowMap;
import models.AgentCreditLog;
import models.AgentCreditLogRowMap;
import models.DictRender;
import models.OrderNo;
import models.ShareModify;
import service.AgentService;
import service.CreditLogService;
import util.Constant;
import util.DateUtil;
import util.ExtPage;
import util.JSONResult;
import util.PageUtil;
import util.Sp;
import util.Sqler;

public class AgentCommissionApp extends Controller{
	
	public static void getList(int start,int limit,int page,String sdate,String edate,String sort,String queryKey)throws Exception{
		Sqler sql =new Sqler("select * from mb_agent_commission");
		Sqler cntsql =new Sqler("select count(1) from mb_agent_commission");
		if(!PageUtil.blank(queryKey)){
			sql.and().left().equals("login_name", queryKey).right();
			cntsql.and().left().equals("login_name", queryKey).right();
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
		sql.orberByDesc("com_id");
		List<AgentCommission> roleList=Sp.get().getDefaultJdbc().query(PageUtil.page(sql.getSql(),start,limit),sql.getParams().toArray(new Object[0]),new AgentCommissionRowMap());
		
		
		int count=Sp.get().getDefaultJdbc().queryForObject(cntsql.getSql(),cntsql.getParams().toArray(new Object[0]),Integer.class);
		ExtPage p =new ExtPage();
		p.data=JSONResult.conver(roleList,true);
		p.total=count; 
		renderJSON(p);
	}
	
	public static void saveTotalcost(AgentCommission agentCommission) throws Exception {
		
		//System.out.println(agentCommission.com_id);
		if (!AgentCommission.NTexist(agentCommission.com_id)) {
			renderText(JSONResult.failure("代理佣金记录不存在!"));
		}
		
        	
		if(agentCommission.update(agentCommission)){
			renderText(JSONResult.success("投入金额修改成功!"));
			}else{
				renderText(JSONResult.failure("投入金额修改失败!"));
		 }
        
       
		renderText(JSONResult.failure("提交失败!"));
	}
	
	
	public static void detail(Long id){
		AgentCommission commission=AgentCommission.NTgetCommission(id);
		if(commission==null){
			 renderText(JSONResult.failure("请求的佣金记录不存在!"));
		}
		DictRender rd =new DictRender();
		render(commission,rd);
	}
	
	public static void audit(Integer flag,String remarks,Long id){
		AgentCommission commission=AgentCommission.NTgetCommission(id);
		if(commission==null){
			 renderText(JSONResult.failure("请求的佣金记录不存在!"));
		}
		
		if(commission.status != 0 ){
			 renderText(JSONResult.failure("操作失败，该提案已经被处理!"));
		}
		
		String user=session.get(Constant.userName);
		
		int status=1;
		if(flag==-2){
			status=-1;
		}
		
		if(status==1){ //审核通过
			
			Agent agent=Agent.NTgetAgent(commission.agent_id);
			
			BigDecimal credit = new BigDecimal(0);
			
			BigDecimal cost = commission.total_cost;
			if(cost == null){
				cost = new BigDecimal(0);
			}
			
			
//			credit = (commission.total_deposit.subtract(commission.total_withdraw));
//					credit = 	credit.multiply(new BigDecimal(0.9));
//			
//			credit = credit.subtract((commission.total_gift.multiply(new BigDecimal(0.1))));
//			
//			credit = credit.subtract(commission.total_cost);
			
			cost = cost.multiply((new BigDecimal(commission.share).divide(new BigDecimal(100))));
			
			credit = commission.credit.subtract(cost);
			
			commission.finalcredit  = credit;
			
			
			if(AgentCommission.NTaudit(id, status, user, remarks,credit)){
				AgentService.modCredit(agent.agent_id,CreditLogService.Commission, 
						commission.login_name,commission.finalcredit,user, "代理佣金", id.toString());
				
				Agent.NTupdateCommission_date(commission.end_date, agent.login_name);
				   renderText(JSONResult.success("操作成功!")); 
			 }
		}
		
		if(AgentCommission.NTaudit(id, status, user, remarks,null)){
			   renderText(JSONResult.success("操作成功!")); 
		 }
		
		renderText(JSONResult.failure("提交失败!"));
		
		
	}

}
