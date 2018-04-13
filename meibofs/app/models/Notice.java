package models;

import java.util.Date;

import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import util.Sp;

public class Notice {
    public Long notice_id;
    public String title;
    public String content;
    public Date start_date;
    public Date end_date;
    public String create_user;
    public Date create_date;
    public Integer priority;
    public Boolean available;
    public  Long NTcreat(){
		  final String sql="insert into  mb_notice (title,content,start_date,end_date,create_user,create_date,priority,available) values(?,?,?,?,?,?,?,?)";
		  final Object[] objects=new Object[]{title,content,start_date,end_date,create_user,create_date,priority,available,};
		  KeyHolder keyHolder = new GeneratedKeyHolder();
		  Sp.get().getDefaultJdbc().update(new MyPreparedStatementCreator(sql,objects), keyHolder);
		  return keyHolder.getKey().longValue();
	 }
    
	 public  boolean NTupdate(){
		  String sql="update mb_notice set title=?,content=?,start_date=?,end_date=?,priority=?,available=? where notice_id=?";
		  int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{title,content,start_date,end_date,priority,available,notice_id});
		  return count>0;
	 }
	 
	 public static boolean NTdelete(Long notice_id){
		  String sql="delete from mb_notice where notice_id=?";
		  int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{notice_id});
		  return count>0;
	 }
}

