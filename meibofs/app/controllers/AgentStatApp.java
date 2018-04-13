package controllers;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import models.Agent;
import models.AgentRowMap;
import models.AgentStat;
import models.AgentStatRowMap;
import models.Customer;
import models.CustomerRowMap;
import play.mvc.Controller;
import play.mvc.With;
import util.DateUtil;
import util.ExtPage;
import util.JSONResult;
import util.PageUtil;
import util.Sp;
import util.Sqler;

@With(value = {AjaxSecure.class})
public class AgentStatApp extends Controller{

	public static void getList(int start,int limit,int page,String sdate,String edate,String sort,String queryKey)throws Exception{


		Sqler sql =new Sqler("select sum(deposit_collect) as deposit_collect,sum(withdraw_collect) as withdraw_collect ,sum(gift_collect) as gift_collect,"
				+ "sum(deposit_count) as deposit_count,sum(withdraw_count) as withdraw_count,sum(gift_count) as gift_count,agent_id,stat_date from mb_stat  ");
		//Sqler cntsql =new Sqler("select count(*) from (select count(DISTINCT stat_date) from mb_stat group by stat_date,agent_id) stat ");
		Sqler cntsql =new Sqler("select count(DISTINCT stat_date) from mb_stat  ");
		//String sqlString="";
		
		if(!PageUtil.blank(queryKey)){
			
			Agent agent = Agent.getAgent(queryKey);
			if(agent != null){
				sql.and().left().equals("agent_id", agent.agent_id).right();
				cntsql.and().left().equals("agent_id", agent.agent_id).right();
				//sqlString = "";
			}
			
			
		}
		
		sql.and().left().isNotNull("agent_id").right();
		cntsql.and().left().isNotNull("agent_id").right();
		
		
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
		
		sql.addGroupBy("stat_date,agent_id");
		cntsql.addGroupBy("stat_date,agent_id");
		
		sql.orberByDesc("create_date");
		
		
		
		List<AgentStat> roleList=Sp.get().getDefaultJdbc().query(PageUtil.page(sql.getSql(),start,limit),sql.getParams().toArray(new Object[0]),new AgentStatRowMap());
		
		HashMap<Long,String> agentmap = new HashMap<Long,String>();
		for(AgentStat stat :roleList){
			//System.out.println("stat.agent_id  " + stat.deposit_collect);
			if(agentmap.containsKey(stat.agent_id)){
				stat.agent_name = agentmap.get(stat.agent_id);
			}else{
				Agent agent = Agent.NTgetAgent(stat.agent_id);
				stat.agent_name = agent.login_name;
				agentmap.put(stat.agent_id, agent.login_name);
				
			}
			
			
		}
		
	   // System.out.println("select count(*) from ( "+cntsql.getSql() +" ) stat");
	   // System.out.println(cntsql.getParams());
		int count=Sp.get().getDefaultJdbc().queryForObject("select count(*) from ( "+cntsql.getSql() +" ) stat",cntsql.getParams().toArray(new Object[0]),Integer.class);
		ExtPage p =new ExtPage();
		p.data=JSONResult.conver(roleList,true);
		//p.total = 0;
		p.total=count; 
		renderJSON(p);
	}
	
}
