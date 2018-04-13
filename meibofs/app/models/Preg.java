package models;

import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import util.Sp;

public class Preg {
	
	public Long preg_id;
	public String login_name;
	public String bat_code;
	public Long agent_id;
	public String agent_name;
	
	  public  Long NTcreat(){
		  final String sql="insert into  sb_preg (login_name,bat_code,agent_id) values(?,?,?)";
		  final Object[] objects=new Object[]{login_name,bat_code,agent_id};
		  KeyHolder keyHolder = new GeneratedKeyHolder();
		  Sp.get().getDefaultJdbc().update(new MyPreparedStatementCreator(sql,objects), keyHolder);
		  return keyHolder.getKey().longValue();
     }
	  
	  public static boolean NTdelete(Long id){
		 	 String sql="delete from sb_preg where preg_id=?";
		 	 int  count= Sp.get().getDefaultJdbc().update(sql,new Object[]{id});
		 	 if(count>0)return true;return false;
	  }
		  
	  public static boolean NTdeleteBycode(String code){
	 	 String sql="delete from sb_preg where bat_code=?";
	 	 int  count= Sp.get().getDefaultJdbc().update(sql,new Object[]{code});
	 	 return count>0;
	  }

	 public static boolean NTexist(String login_name){
		   String sql="select count(1) from sb_preg where login_name=?";
		   int count=Sp.get().getDefaultJdbc().queryForObject(sql,new Object[]{login_name},Integer.class);
		   return count>0;
	 }
	
	
}
