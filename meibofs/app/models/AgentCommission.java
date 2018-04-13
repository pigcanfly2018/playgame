package models;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import util.Sp;

public class AgentCommission {

	public Long com_id;
	
	public Long agent_id;
	public String login_name;
	public Date create_date;
	public String create_user;
	public Integer status;
	
	public Date audit_date;
	public String audit_user;
	
	public BigDecimal credit;
	public BigDecimal finalcredit;
	
	public String audit_msg;
	public String remarks;
	public Integer share;
	public Integer active;
	
	public String start_date;
	public String end_date;
	
	public BigDecimal total_deposit;
	public BigDecimal total_withdraw;
	public BigDecimal total_gift;
	public BigDecimal total_cost;
	
	
	public static boolean NTexist(Long com_id) {
		String sql = "select count(1) from mb_agent_commission  where com_id =?";
		int count = Sp.get().getDefaultJdbc()
				.queryForObject(sql, new Object[] { com_id },Integer.class);
		return count > 0;
	}
	
	public static AgentCommission NTgetCommission(Long com_id){
		  String sql="select * from mb_agent_commission where com_id =?";
		  List<AgentCommission> list=Sp.get().getDefaultJdbc().query(sql,new Object[]{com_id},new AgentCommissionRowMap());
		  if(list.size()>0){
			  return list.get(0);
		  }
		  return null;
	}
	
	public static boolean update(AgentCommission commission){
		String sql="update mb_agent_commission set total_cost=?,remarks=? where com_id=?";
		
		int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{commission.total_cost,commission.remarks,commission.com_id});
		return count>0;
	}
	
	public static boolean  NTaudit(Long com_id,Integer status,String audit_user,String remarks,BigDecimal credit){
	      String sql ="update mb_agent_commission set status=?,audit_user=?,audit_date=now(),audit_msg=?,finalcredit=? where com_id=?";
	      int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{status,audit_user,remarks,credit,com_id});
		  return count>0;
	 }
	
	
	
	
}
