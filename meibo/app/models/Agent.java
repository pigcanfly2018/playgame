package models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;










import util.MD5;
import util.Sp;


public class Agent implements Serializable{

	public Long agent_id;
    public String login_name;
    public String login_pwd;
    public String real_name;
    public String phone;
    public String email;
    public String qq;
    public int flag;
    public String reg_ip;
    public Date create_date;
    public String create_user;
    public Date audit_date;
    public String audit_user;
    public String audit_msg;
    public Date lock_date;
    public String lock_user;
    public String lock_msg;
    public String last_login_ip;
    public String reg_code;
    public Date last_login_date;
    public BigDecimal credit;
    public Long parent_id;
    public String remarks;
    public String bank_name;
    public String account_type;
    public String bank_dot;
    public String account;
    public String advantage;
    
    //辅助字段
    public int subcount;
	
	 
	 public static Agent getAgentByReg_code(String reg_code){
		  String sql="select * from mb_agent where reg_code =?";
		  List<Agent> list=Sp.get().getDefaultJdbc().query(sql,new Object[]{reg_code},new AgentRowMap());
		  if(list.size()>0){
			  return list.get(0);
		  }
		  return null;
	 }
	
	
	
	
	
    
    
    
    
    
	 
	 
	 
    
    
}
