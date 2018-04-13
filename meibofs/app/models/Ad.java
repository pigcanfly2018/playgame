package models;

import java.util.Date;

import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import util.Sp;

public class Ad {
    public Long ad_id;
    public String ad_title;
    public String ad_describe;
    public String pic_url;
    public String target_url;
    public Date create_date;
    public String start_date;
    public String end_date;
    public String create_user;
    public Boolean available;
    public Integer priority;
    public Boolean mobile_flag;
    public  Long NTcreat(){
		  final String sql="insert into  mb_ad (ad_title,ad_describe,pic_url,target_url,create_date,start_date,end_date,create_user,available,priority,mobile_flag) values(?,?,?,?,?,?,?,?,?,?,?)";
		  final Object[] objects=new Object[]{ad_title,ad_describe,pic_url,target_url,create_date,start_date,end_date,create_user,available,priority,mobile_flag};
		  KeyHolder keyHolder = new GeneratedKeyHolder();
		  Sp.get().getDefaultJdbc().update(new MyPreparedStatementCreator(sql,objects), keyHolder);
		  return keyHolder.getKey().longValue();
	 }
	 public  boolean NTupdate(){
		  String sql="update mb_ad set ad_title=?,ad_describe=?,pic_url=?,target_url=?,start_date=?,end_date=?,available=?,priority=?,mobile_flag=? where ad_id=?";
		  int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{ad_title,ad_describe,pic_url,target_url,start_date,end_date,available,priority,mobile_flag,ad_id});
		  return count>0;
	 }
	 
	 public static boolean NTdelete(Long ad_id){
		 String sql="delete from mb_ad where ad_id=?";
		 int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{ad_id});
		 return count>0;
	 }
}
