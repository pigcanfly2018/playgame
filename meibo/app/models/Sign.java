package models;

import java.util.Date;
import java.util.List;

import util.Sp;

public class Sign {

	public Long sign_id;
	
	public Long cust_id;
	
	public String login_name;
	
	public Date create_date;
	
	public String sign_date;
	
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
	
	public static List<Sign> getAll(String login_name){
		String sql="select date(sign_date) as sign_date from  mb_sign where login_name=? and sign_date >= '2017-06-01 00:00:00' and sign_date <= '2017-06-30 23:59:59'";
		List<Sign> list=Sp.get().getDefaultJdbc().query(sql,new Object[]{login_name},new SignRowMap());
		return list;
	}
	
}
