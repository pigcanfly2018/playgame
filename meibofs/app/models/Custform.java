package models;

import java.util.Date;
import java.util.List;

import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import util.Sp;

public class Custform {
	 public Long form_id;
     public Long cust_id;
     public String login_name;
     public String real_name;
     public String org_real_name;
     public String phone;
     public String org_phone;
     public String email;
     public String org_email;
     public String qq;
     public String org_qq;
     
     /**
      * 0 待审核
      * 2 审核通过
      * -1 取消
      * -2 审核不通过
      */
     public Integer status;
     public String bank_name;
     public String org_bank_name;
     public String account_type;
     public String org_account_type;
     public String account;
     public String org_account;
     public String bank_dot;
     public String org_bank_dot;
     
     public String reason;
     public Date create_date;
     public String create_user;
     public String audit_user;
     public Date audit_date;
     public String remarks;
     
     public  Long NTcreat(){
		  final String sql="insert into  mb_custform (cust_id,login_name,real_name,org_real_name,phone,org_phone,email,org_email,qq,org_qq,bank_name,org_bank_name,account,org_account,bank_dot,org_bank_dot,reason,account_type,org_account_type,create_date,create_user,status) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,now(),?,?)";
		  final Object[] objects=new Object[]{cust_id,login_name,real_name,org_real_name,phone,org_phone,email,org_email,qq,org_qq,bank_name,org_bank_name,account,org_account,bank_dot,org_bank_dot,reason,account_type,org_account_type,create_user,status};
		  KeyHolder keyHolder = new GeneratedKeyHolder();
		  Sp.get().getDefaultJdbc().update(new MyPreparedStatementCreator(sql,objects), keyHolder);
		  return keyHolder.getKey().longValue();
	 }
     
     public static Custform NTfindById(Long id){
		  String sql="select * from  mb_custform where form_id =?";
		  List<Custform> form=Sp.get().getDefaultJdbc().query(sql,new Object[]{id},new CustformRowMap());
		  if(form.size()>0){
			  return form.get(0);
		  }
		  return null;
	 }
     public static  boolean NTupdateFlag(Integer flag,String audit_remarks,String audituser,Long custform_id){
		  String sql="update mb_custform set status=?,remarks=?,audit_date=now(),audit_user=? where form_id=?";
		  int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{flag,audit_remarks,audituser,custform_id});
		  return count>0;
	 }
}
