package models;

import java.util.Date;

import util.Sp;

public class UserLog {

	public Long log_id;
	public String op_user;
	public  Date create_date;
	public String log_msg;
	
	public  boolean NTcreat(){
		  String sql="insert into mb_user_log(op_user,create_date,log_msg)" +
		 		" values(?,now(),?)";
		  int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{op_user,log_msg});
		  return count>0;
	 }
	
}
