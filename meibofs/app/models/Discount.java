package models;

import java.util.Date;

import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import util.JSONResult;
import util.Sp;

public class Discount {
	 public Long discount_id;
     public String b_url;
     public String s_url;
     public String title;
     public String content;
     public Boolean available;
     public Date create_date;
     public String start_date;
     public String end_date;
     public String create_user;
     public String summary;
     public Integer priority;
     public Boolean is_hot;
     public Boolean mobile_flag;
     public  Long NTcreat(){
		  final String sql="insert into  mb_discount (b_url,s_url,title,summary,content,available,create_date,create_user,priority,is_hot,start_date,end_date) "
		  		+ "values(?,?,?,?,?,?,?,?,?,?,?,?)";
		  final Object[] objects=new Object[]{b_url,s_url,title,summary,content,available,create_date,create_user,priority,is_hot,start_date,end_date};
		  KeyHolder keyHolder = new GeneratedKeyHolder();
		  Sp.get().getDefaultJdbc().update(new MyPreparedStatementCreator(sql,objects), keyHolder);
		  return keyHolder.getKey().longValue();
	 }
     
     
	 public  boolean NTupdate(){
		  String sql="update mb_discount set b_url=?,s_url=?,title=?,summary=?,content=?,available=?,priority=?,is_hot=?,mobile_flag=?,start_date=?,end_date=? where discount_id=?";
		  int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{b_url,s_url,title,summary,content,available,priority,is_hot,mobile_flag,start_date,end_date,discount_id});
		  return count>0;
	 }
	 
	 public static boolean NTdelete(Long discount_id){
		 String sql="delete from mb_discount where discount_id=?";
		 int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{discount_id});
		 return count>0;
	 }
	 
}
