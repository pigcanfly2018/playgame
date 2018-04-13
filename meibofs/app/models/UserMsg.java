package models;

import java.util.Date;
import java.util.List;

import util.Sp;

public class UserMsg {
	public Long um_id;
	public String login_name;
	public Long msg_id;
	public Integer notify_flag;
	public Date create_date;
	
	 public static List<UserMsg> queryByUser(String login_name){
    	String sql="select * from mb_user_msg where login_name=?";
    	List<UserMsg> roleList=Sp.get().getDefaultJdbc().query(sql,new Object[]{login_name},new UserMsgRowMap());
    	return roleList;
	 }
	 public static int queryByUserCnt(String login_name){
    	String sql="select count(1) from mb_user_msg where login_name=?";
    	return Sp.get().getDefaultJdbc().queryForObject(sql,new Object[]{login_name},Integer.class);
	 }
	 
	 public static boolean NTcreateMsgs(Long msg_id,String funccode){
		 String sql="insert into mb_user_msg(login_name,msg_id,notify_flag,create_date) select " +
		 		"loginname,?, 0,now() from mb_user a inner join mb_role_func b on " +
		 		"a.rolecode=b.rolecode where b.funccode=?";
		 return Sp.get().getDefaultJdbc().update(sql,new Object[]{msg_id,funccode})>0;
	 }
	   public static boolean NTdo(String login_name ){
	    	String sql="update mb_user_msg set notify_flag=1 where login_name=?";
	    	return Sp.get().getDefaultJdbc().update(sql,new Object[]{login_name})>0;
	    }

}
