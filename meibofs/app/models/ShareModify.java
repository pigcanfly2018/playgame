package models;

import java.math.BigDecimal;
import java.util.Date;

import util.Sp;

public class ShareModify {

	public Long modify_id;
    public String modify_no;
    public Integer share_before;
    public Integer share_after;
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
		  String sql="insert into  mb_share_modify (modify_no,share_before,share_after,agent_id,login_name,create_date,create_user,status,remarks,audit_date,audit_user,audit_msg) values(?,?,?,?,?,now(),?,?,?,?,?,?)";
		  int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{modify_no,share_before,share_after,agent_id,login_name,create_user,status,remarks,audit_date,audit_user,audit_msg});
		  return count>0;
	 }
	 public  boolean NTupdate(){
		  String sql="update mb_share_modify set modify_no=?,share_before=?,share_after=?,agent_id=?,login_name=?,create_date=?,create_user=?,status=?,remarks=?,audit_date=?,audit_user=?,audit_msg=? where modify_id=?";
		  int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{modify_no,share_before,share_after,agent_id,login_name,create_date,create_user,status,remarks,audit_date,audit_user,audit_msg,modify_id});
		  return count>0;
	 }
	 public static ShareModify NTget(Long modify_id){
	      String sql ="select * from mb_share_modify where modify_id=?";
	      ShareModify dep=(ShareModify) Sp.get().getDefaultJdbc().queryForObject(sql,
					new Object[]{modify_id},new ShareModifyRowMap());
	      return dep;
	 }
	 
	 
	    
	 
	 public static boolean  NTaudit(Long modify_id,Integer status,String audit_user,String remarks){
	      String sql ="update mb_share_modify set status=?,audit_user=?,audit_date=now(),audit_msg=? where modify_id=?";
	      int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{status,audit_user,remarks,modify_id});
		  return count>0;
	 }
	 
    
}
