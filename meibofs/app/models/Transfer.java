package models;

import java.math.BigDecimal;
import java.util.Date;

import util.Sp;

public class Transfer {
	public Long transfer_id;
    public Long cust_id;
    public String transfer_no;
    public String login_name;
    public String transfer_type;
    public BigDecimal credit;
    public Date create_date;
    public String create_user;
    public Integer status;
    public Date audit_date;
    public String audit_user;
    public String audit_msg;
    public String remarks;
    
     public  boolean NTcreat(){
		  String sql="insert into  mb_transfer (cust_id,transfer_no,login_name,transfer_type,credit,create_date,create_user,status,audit_date,audit_user,audit_msg,remarks) values(?,?,?,?,?,now(),?,?,?,?,?,?)";
		  int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{cust_id,transfer_no,login_name,transfer_type,credit,create_user,status,audit_date,audit_user,audit_msg,remarks});
		  return count>0;
	 }
     
	 public static Transfer NTget(Long transfer_id){
	      String sql ="select * from mb_transfer where transfer_id=?";
	      Transfer dep=(Transfer) Sp.get().getDefaultJdbc().queryForObject(sql,new Object[]{transfer_id},new TransferRowMap());
	      return dep;
    }
	 
	 public static boolean  NTaudit(Long transfer_id,Integer status,String audit_user,String remarks){
	      String sql ="update mb_transfer set status=?,audit_user=?,audit_date=now(),audit_msg=? where transfer_id=?";
	      int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{status,audit_user,remarks,transfer_id});
		  return count>0;
   }


}
