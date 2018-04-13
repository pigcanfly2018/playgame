package models;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import util.Sp;

public class AgentWithdraw {
	public Long awithdraw_id;
    public String login_name;
    public String real_name;
    public BigDecimal amount;
    public String bank_name;
    public String account_name;
    public String account;
    public String account_type;
    public String location;
    public String remarks;
    public Date create_date;
    public String create_user;
    public Integer status;
    public Long agent_id;
    public String awit_no;
    public Boolean locked;
    public Integer wit_cnt;
    
    public  Long NTcreat(){
		  final String sql="insert into  mb_agent_withdraw (awit_no,login_name,real_name,amount,bank_name,account_name,account,account_type,location,remarks,create_date,create_user,status,agent_id) values(?,?,?,?,?,?,?,?,?,?,now(),?,?,?)";
		  final Object[] objects=new Object[]{awit_no,login_name,real_name,amount,bank_name,account_name,account,account_type,location,remarks,create_user,status,agent_id};
		  KeyHolder keyHolder = new GeneratedKeyHolder();
		  Sp.get().getDefaultJdbc().update(new MyPreparedStatementCreator(sql,objects), keyHolder);
		  return keyHolder.getKey().longValue();
	    }
    
    public static AgentWithdraw NTget(Long awithdraw_id){
	      String sql ="select * from mb_agent_withdraw where awithdraw_id=?";
	     AgentWithdraw dep=(AgentWithdraw) Sp.get().getDefaultJdbc().queryForObject(sql,
					new Object[]{awithdraw_id},new AgentWithdrawRowMap());
	      return dep;
    }
    
    public static boolean NTchangeStatus(Long awithdraw_id,Integer status){
		  String sql ="update mb_agent_withdraw set status=? where awithdraw_id=?";
		  int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{status,awithdraw_id});
		  return count>0;
	}
    
    public static int NTgetCount(Long agent_id){
		  String sql ="select count(1) from mb_agent_withdraw where agent_id=? and status =5";
		  int count=Sp.get().getDefaultJdbc().queryForObject(sql, new Object[]{agent_id},Integer.class);
		  return count;
	}
    
    public static boolean NTrecPayDate(Long awithdraw_id){
		  String sql ="update mb_agent_withdraw set pay_date=now() where awithdraw_id=? and status=5";
		  int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{awithdraw_id});
		  return count>0;
	}
    
}
