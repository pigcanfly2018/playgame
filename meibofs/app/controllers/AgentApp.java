package controllers;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import models.Agent;
import models.AgentRowMap;
import models.Customer;
import models.CustomerRowMap;
import models.DictRender;
import models.Letter;
import models.Role;
import models.User;
import models.UserRowMap;
import models.Withdraw;
import models.WithdrawLog;
import play.mvc.Controller;
import play.mvc.With;
import service.CreditLogService;
import service.CustomerService;
import util.Constant;
import util.DateUtil;
import util.ExtPage;
import util.IPSeeker;
import util.JSONResult;
import util.MD5;
import util.MyRandom;
import util.PageUtil;
import util.Sp;
import util.Sqler;

@With(value = {AjaxSecure.class})
public class AgentApp extends Controller{

	public static void getList(int start,int limit,int page,String sdate,String edate,String sort,String queryKey)throws Exception{
		Sqler sql =new Sqler("select * from mb_agent");
		Sqler cntsql =new Sqler("select count(1) from mb_agent");
		if(!PageUtil.blank(queryKey)){
			
			sql.and().left().like("login_name", queryKey);
			cntsql.and().left().like("login_name", queryKey);
				
			sql.or().like("real_name", queryKey);
			cntsql.or().like("real_name", queryKey);
				
			sql.or().like("qq", queryKey);
			cntsql.or().like("qq", queryKey);
				
			sql.or().like("reg_ip", queryKey);
			cntsql.or().like("reg_ip", queryKey);
				
			sql.or().like("phone", queryKey).right();
			cntsql.or().like("phone", queryKey).right();
			
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
		//IPSeeker seeker = IPSeeker.getInstance();
		List<Agent> roleList=Sp.get().getDefaultJdbc().query(PageUtil.page(sql.getSql(),start,limit),sql.getParams().toArray(new Object[0]),new AgentRowMap());
		
		Sqler countSubsql =null;
		for(Agent c:roleList){
			countSubsql =new Sqler("select * from mb_customer");
//			c.ip_addr=seeker.getAddress(c.reg_ip);
			countSubsql.and().equals("parent_id", c.agent_id);
			
			List<Customer> list=Sp.get().getDefaultJdbc().query(PageUtil.page(countSubsql.getSql(),0,100000),countSubsql.getParams().toArray(new Object[0]),new CustomerRowMap());
			int count = 0;
			for(Customer cust : list){
				if(cust.first_deposit_date != null){
					count ++;
				}
			}
			
			c.active = count;
			c.subcount =list.size();
			//System.out.println("sql   "+c.subcount);
		}
		
		int count=Sp.get().getDefaultJdbc().queryForObject(cntsql.getSql(),cntsql.getParams().toArray(new Object[0]),Integer.class);
		ExtPage p =new ExtPage();
		p.data=JSONResult.conver(roleList,true);
		p.total=count; 
		renderJSON(p);
	}
	
	
	public static void saveAgent(Agent agent){
		
		if("1".equals(params.get("kact"))){
			if(agent.NTexist(agent.login_name)){
				renderText(JSONResult.failure(agent.login_name+"用户名已经存在!"));
			}
			if(agent.real_name==null||agent.real_name.trim().equals("")){
				renderText(JSONResult.failure("请填写真实姓名!"));
			}
			agent.create_user=session.get(Constant.userName);
			agent.create_date=new Date(System.currentTimeMillis());
			agent.reg_ip=request.remoteAddress;
			agent.flag=1;
			agent.login_pwd=MD5.md5(agent.login_pwd);
			
			agent.credit=new BigDecimal(0);
			agent.reg_code=MyRandom.getRandomPureChar(1)+agent.login_name;
			
			agent.NTcreat();
			renderText(JSONResult.success("开户成功!"));
		}
		
	}
	
	public static void saveAgent2(Agent agent){
		
		Agent ang=Agent.NTgetAgent(agent.agent_id);
		if(ang==null){
			renderText(JSONResult.failure("不存在该代理!"));
		}
		
		//System.out.println(ang);
		Agent.NTmodInfo2(agent.email, agent.qq, 
				agent.remarks, agent.bank_name, 
				agent.account, agent.account_type,
				agent.bank_dot,
				agent.agent_id);
		renderText(JSONResult.success("修改成功!"));
	}
	
	
	
	public static void validatorUser(String login_name){
		if(Agent.NTexist(login_name)){
			renderText(JSONResult.failure("用户名:"+login_name+"已经存在!"));
		}
		renderText(JSONResult.success("成功!"));
		
	}
	
	public static void validatorRealName(String real_name){
		if(Agent.NTexitsRealName(real_name)){
			renderText(JSONResult.failure("真实名:"+real_name+"已经存在!"));
		}
		renderText(JSONResult.success("成功!"));
		
	}
	
	
	public static void validatorQQ(String qq){
		if("000000".equals(qq)){
			renderText(JSONResult.success("成功!"));
		}
		if(Agent.NTexitsQQ(qq)){
			renderText(JSONResult.failure("QQ:"+qq+"已经存在!"));
		}
		renderText(JSONResult.success("成功!"));
	}
	
	public static void validatorPhone(String phone){
		if(Agent.NTexitsPhone(phone)){
			renderText(JSONResult.failure("电话:"+phone+"已经存在!"));
		}
		renderText(JSONResult.success("成功!"));
	}
	
	
	
	public static void detail(Long agent_id){
		Agent agent=Agent.NTgetAgent(agent_id);
		if(agent==null){
			renderText("不存在该代理资料!");
		}
		DictRender rd=new DictRender();
		
		render(agent,rd);
		
	}
	
	
	/**
	 * 审核(通过与不通过)
	 * @param request_id
	 * @param remarks
	 */
	public static void audit(Integer flag,String remarks,Long agent_id){
		Agent agent=Agent.NTget(agent_id);
		if(agent==null){
			 renderText(JSONResult.failure("请求的代理帐号不存在!"));
		}
		if(agent.flag ==3){
			renderText(JSONResult.failure("该代理已是正常状态，您目前无法操作!"));
		}else if(agent.flag ==4){
			renderText(JSONResult.failure("该代理已是冻结状态，您目前无法操作!"));
		}
		
		String user=session.get(Constant.userName);
		//int status=3;
		if(flag==-2){
			//status=2;
			//CustomerService.modCredit(cust.cust_id,CreditLogService.Withdraw, 
				//	withdraw.login_name,withdraw.amount,user, "拒绝提款", withdraw.wit_no);
			//提款不成功 发送站内信
			StringBuffer sb =new StringBuffer();
			sb.append("<p>尊敬的客户，您的申请代理请求没有审核通过。，原因是："+remarks+"</p>");
			sb.append("<p style=\"text-align:right\">八达国际代理部上</p>");
			Agent.modifyFlag(user, agent_id, remarks, 2,new Date(System.currentTimeMillis()));
			
		    Letter.NTcreatAgent(agent_id, agent.login_name, "代理申请未通过审核", sb.toString(), user, true);
		}else if(flag==2){
			StringBuffer sb =new StringBuffer();
			sb.append("<p>尊敬的客户，您的申请代理请求已通过审核。</p>");
			sb.append("<p style=\"text-align:right\">八达国际代理部上</p>");
			Agent.modifyFlag(user, agent_id, remarks, 3,new Date(System.currentTimeMillis()));
			Letter.NTcreatAgent(agent_id, agent.login_name, "代理申请通过审核", sb.toString(), user, true);
		}
		//WithdrawLog.NTcreat(withdraw.status,status, withdraw.withdraw_id, remarks, user);
		//Withdraw.NTchangeStatus(withdraw.withdraw_id, status);
		renderText(JSONResult.success("操作成功!"));
		
	}
	
	public static void locked(Integer flag,Long agent_id,String remarks){
		
		Agent agent=Agent.NTget(agent_id);
		if(agent==null){
			 renderText(JSONResult.failure("请求的代理帐号不存在!"));
		}
		String user=session.get(Constant.userName);
		if(flag==-2){
			Agent.unLocked(user,agent_id,remarks,3,new Date(System.currentTimeMillis()));
			//WithdrawLog.NTcreat(withdraw.status,withdraw.status, withdraw.withdraw_id, "[解锁/clear]:"+remarks, user);
		}else{
			Agent.locked(user,agent_id,remarks,4,new Date(System.currentTimeMillis()));
			//WithdrawLog.NTcreat(withdraw.status,withdraw.status, withdraw.withdraw_id, "[锁/locked]:"+remarks, user);
		}
		renderText(JSONResult.success("操作成功!"));
	
	}
	
	public static void getSubcustomerList(int start,int limit,int page,String sdate,String edate,String sort,String queryKey)throws Exception{
		Sqler sql =new Sqler("select * from mb_customer");
		Sqler cntsql =new Sqler("select count(1) from mb_customer");
		if(!PageUtil.blank(queryKey)){
			
				Agent agent = Agent.getAgent(queryKey);
				
				if(agent != null){
					sql.and().left().equals("parent_id", agent.agent_id).right();
					cntsql.and().left().equals("parent_id", agent.agent_id).right();
					
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
					IPSeeker seeker = IPSeeker.getInstance();
					List<Customer> roleList=Sp.get().getDefaultJdbc().query(PageUtil.page(sql.getSql(),start,limit),sql.getParams().toArray(new Object[0]),new CustomerRowMap());
					for(Customer c:roleList){
						c.ip_addr=seeker.getAddress(c.reg_ip);
						
					}
					
					int count=Sp.get().getDefaultJdbc().queryForObject(cntsql.getSql(),cntsql.getParams().toArray(new Object[0]),Integer.class);
					ExtPage p =new ExtPage();
					p.data=JSONResult.conver(roleList,true);
					p.total=count; 
					renderJSON(p);
				}
				
				
		}
		
		
		
	}
	
	public static void resetPwd(String idcode){
		String loginname=idcode;
		System.out.println(loginname+"               ddd");
		if(PageUtil.blank(loginname)){
			renderText(JSONResult.failure("非法操作！"));
		}
		Agent agent = Agent.getAgent(idcode);
		agent.login_pwd = MD5.md5("88888888");
		
		if(agent.NTupdatePwd()){
			renderText(JSONResult.success("密码设置成功!用户名:"+loginname+",密码为:88888888"));
		}else{
			renderText(JSONResult.failure("密码设置失败!"));
		}
		
		
	}
	
}
