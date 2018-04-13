package models;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import util.Sp;

public class AgentCreditLog {

	public Long log_id;
    public String log_type;
    public BigDecimal credit;
    public String login_name;
    public Long agent_id;
    public Date create_date;
    public String create_user;
    public String remarks;
    public String order_no;
    public BigDecimal org_credit;
    public BigDecimal after_credit;
    public static boolean NTcreat(String log_type,BigDecimal credit,String login_name,
  		  Long agent_id,String user,String remarks,String order_no,String org_credit){
  	  final String sql="insert into  mb_agent_credit_log (log_type,credit,login_name,agent_id,create_date,create_user,remarks,order_no,org_credit) values(?,?,?,?,?,?,?,?,?)";
  	  final Object[] objects=new Object[]{log_type,credit,login_name,agent_id,new Date(System.currentTimeMillis()),user,remarks,order_no,org_credit};
		  KeyHolder keyHolder = new GeneratedKeyHolder();
		  int result=Sp.get().getDefaultJdbc().update(new MyPreparedStatementCreator(sql,objects), keyHolder);
		  return result>0;
    }
    
}
