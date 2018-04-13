package models;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import util.Sp;

public class SignReward {

	public Long reward_id;
	
	public Long cust_id;
	
	public String login_name;
	
	public String reward_date;
	
	public BigDecimal amount;
	
	public Date create_date;
	
	public Integer cust_level;
	
	public static SignReward getLatSignReward(String login_name){
		  String sql="select date(reward_date) as reward_date from mb_sign_reward where login_name =? order by reward_date desc limit 1";
		  List<SignReward> list=Sp.get().getDefaultJdbc().query(sql,new Object[]{login_name},new SignRewardRowMap());
		  if(list.size()>0){
			  return list.get(0);
		  }
		  return null;
	}
	
	public static BigDecimal NTgetSum(String login_name,Date startDate){
		  String sql="SELECT SUM(amount) from mb_sign_reward where login_name =? and create_date > ?";
		  return (BigDecimal) Sp.get().getDefaultJdbc().queryForObject(sql,new Object[]{login_name,startDate},new BigDecimalRowMap());
	  }
	
	public static boolean reward(String login_name,Long cust_id,Integer cust_level, BigDecimal amount,Date signDate){
		 String sql="insert into mb_sign_reward (login_name,cust_id,reward_date,amount,cust_level,create_date) values(?,?,?,?,?,now()) ";
		 int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{login_name,cust_id,signDate,amount,cust_level});
		 return count>0;
	}
	
}
