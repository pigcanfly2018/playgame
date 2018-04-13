package models;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import util.Sp;

public class Letter {
	
	public Long letter_id;
	public String title;
	public String content;
	public Long cust_id;
	public Boolean read_flag;
	public Date read_date;
	public Date create_date;
	public String create_user;
	public Boolean is_public;
	public String login_name;

	public static long NTcreat(Long cust_id,String login_name,String title,String content,String create_user,Boolean is_public){
		  String sql="insert into mb_letter (cust_id,title,content,"
		  		+ "create_user,create_date,read_flag,is_public,login_name) "
		  		+ "values(?,?,?,?,now(),0,?,?)";
		  Object[] objs=new Object[]{cust_id,title,content,create_user,is_public,login_name};
		  KeyHolder keyHolder = new GeneratedKeyHolder();
		  Sp.get().getDefaultJdbc().update(new MyPreparedStatementCreator(sql,objs), keyHolder);
		  return keyHolder.getKey().longValue();
   }
	
	
	 public static Letter NTgetLetter(Long letter_id){
		  String sql="select * from mb_letter where letter_id =?";
		  List<Letter> list=Sp.get().getDefaultJdbc().query(sql,new Object[]{letter_id},new LetterRowMap());
		  if(list.size()>0){
			  return list.get(0);
		  }
		  return null;
	 }
	 
	 public static Letter NTgetLetter(Long letter_id,Long cust_id){
		  String sql="select * from mb_letter where letter_id =? and cust_id=? ";
		  List<Letter> list=Sp.get().getDefaultJdbc().query(sql,new Object[]{letter_id,cust_id},new LetterRowMap());
		  if(list.size()>0){
			  return list.get(0);
		  }
		  return null;
	 }
	 
	 public static int NTgetLetterCnt(Long cust_id){
		  String sql="select count(1) from mb_letter where  cust_id=? and read_flag=0 and  is_public =1 ";
		  return Sp.get().getDefaultJdbc().queryForObject(sql,new Object[]{cust_id},Integer.class);
	 }
	 
	 public static boolean NTdelLetter(Long letter_id){
		 String sql="delete from mb_letter where letter_id = ?";
		 int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{letter_id});
		 return count>0;
	 }
	 
	 
	 public static boolean NTmodLetter(String title,String content,Boolean is_public,Long letter_id){
		 String sql="update mb_letter set title=?,content=?,is_public=?  where letter_id=?";
		 int count=Sp.get().getDefaultJdbc().update(sql,
					new Object[]{title,content,is_public,letter_id});
		 return count>0;
	 }
	 
	 public static boolean NTreadLetter(Long letter_id){
		 String sql="update mb_letter set read_flag=?,read_date=now() where letter_id=?";
		 int count=Sp.get().getDefaultJdbc().update(sql,
					new Object[]{true,letter_id});
		 return count>0;
	 }
	 

}
