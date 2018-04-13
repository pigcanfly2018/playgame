package models;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import util.DateUtil;
import util.Sp;

public class HuoliGift {

	public Long huoli_gift_id;
	
	public Long cust_id;
	
	public String login_name;
	
	public String content;
	
	public Date create_date;
	
	public String gift_no;
	
	public Integer flag;
	
	public Integer level;
	
	
	public static Long create(Long cust_id,String login_name,String content,String no,Integer flag,Integer level){
	     
	      String sql="insert into  mb_huoli_gift (cust_id,login_name,content,create_date,gift_no,flag,level) values(?,?,?,now(),?,?,?)";
		  Object[] objects =new Object[]{cust_id,login_name,content,no,0,level};
		  KeyHolder keyHolder = new GeneratedKeyHolder();
		  Sp.get().getDefaultJdbc().update(new MyPreparedStatementCreator(sql,objects), keyHolder);
		  return keyHolder.getKey().longValue();
	}

	public static boolean finishedGift(Long huoli_gift_id,Integer flag){
		String sql="update mb_huoli_gift set flag=? where huoli_gift_id=? ";
		int c=Sp.get().getDefaultJdbc().update(sql, new Object[]{flag,huoli_gift_id});
		return c>0;
	}
	
	public static int getLevelCount(String login_name,Integer level){
    	String sql="SELECT COUNT(1) FROM mb_huoli_gift WHERE login_name=? and level = ? ";
    	int count=Sp.get().getDefaultJdbc().queryForObject(sql, new Object[]{login_name,level},Integer.class);
		return count;
    }
	
	public static int getLevelCountByToday(String login_name,Integer level,String start,String end){
    	String sql="SELECT COUNT(1) FROM mb_huoli_gift WHERE login_name=? and level = ? and create_date >= ? and create_date < ? ";
    	int count=Sp.get().getDefaultJdbc().queryForObject(sql, new Object[]{login_name,level,start,end},Integer.class);
		return count;
    }
	

	
}
