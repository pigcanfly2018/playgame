package models;

import java.math.BigDecimal;
import java.util.Date;

import util.Sp;

public class AgentCreditFix {
	public Long fix_id;
    public String fix_no;
    public BigDecimal credit;
    public Long agent_id;
    public String login_name;
    public Date create_date;
    public String create_user;
    public Integer status;
    public String remarks;
    public Date audit_date;
    public String audit_user;
    public String audit_msg;
    public  boolean NTcreat(){
		  String sql="insert into  mb_agent_credit_fix (fix_no,credit,agent_id,login_name,create_date,create_user,status,remarks,audit_date,audit_user,audit_msg) values(?,?,?,?,now(),?,?,?,?,?,?)";
		  int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{fix_no,credit,agent_id,login_name,create_user,status,remarks,audit_date,audit_user,audit_msg});
		  return count>0;
	 }
	 public  boolean NTupdate(){
		  String sql="update mb_agent_credit_fix set fix_no=?,credit=?,agent_id=?,login_name=?,create_date=?,create_user=?,status=?,remarks=?,audit_date=?,audit_user=?,audit_msg=? where fix_id=?";
		  int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{fix_no,credit,agent_id,login_name,create_date,create_user,status,remarks,audit_date,audit_user,audit_msg,fix_id});
		  return count>0;
	 }
	 public static AgentCreditFix NTget(Long fix_id){
	      String sql ="select * from mb_agent_credit_fix where fix_id=?";
	      AgentCreditFix dep=(AgentCreditFix) Sp.get().getDefaultJdbc().queryForObject(sql,
					new Object[]{fix_id},new AgentCreditFixRowMap());
	      return dep;
     }
	 
	 public static boolean  NTaudit(Long fix_id,Integer status,String audit_user,String remarks){
	      String sql ="update mb_agent_credit_fix set status=?,audit_user=?,audit_date=now(),audit_msg=? where fix_id=?";
	      int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{status,audit_user,remarks,fix_id});
		  return count>0;
    }
}
