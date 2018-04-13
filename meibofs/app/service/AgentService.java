package service;

import java.math.BigDecimal;

import models.Agent;
import models.Customer;

public class AgentService {

	public static boolean modCredit(Long agent_id,String log_type,String login_name,BigDecimal credit,String user,String remarks,String order_no){
		 Agent agent=Agent.getAgent(login_name);
		 BigDecimal org_credit = agent.credit;
		 boolean b=Agent.modCredit(agent_id, credit);
		 agent=Agent.getAgent(login_name);
		 BigDecimal after_credit= agent.credit;
		 if(b)
			 AgentCreditLogService.NTcreat(log_type, credit, login_name, agent_id, user, remarks, order_no, org_credit, after_credit);
        return b;
	 }
}
