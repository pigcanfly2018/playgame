package models;

import java.util.Date;

import util.Sp;

public class Sign {

	public Long sign_id;
	
	public Long cust_id;
	
	public String login_name;
	
	public Date create_date;
	
	public Date sign_date;
	
	public  boolean NTcreat(){
		  String sql="insert into mb_sign (cust_id,login_name,create_date,sign_date) values(?,?,now(),now())";
		  int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{cust_id,login_name});
		  return count>0;
	
	}
	
	public static int NTgetTodayCount(String login_name){
		  String sql ="select count(1) from mb_sign where login_name=? and create_date>CURDATE() ";
		  int count=Sp.get().getDefaultJdbc().queryForObject(sql, new Object[]{login_name},Integer.class);
		  return count;
 }
	
}
