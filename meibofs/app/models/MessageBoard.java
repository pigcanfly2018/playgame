package models;

import java.util.Date;

import util.Sp;

/**
 * 留言板
 * @author lance
 */
public class MessageBoard {
	public Long msg_id;
    public String cust_name;
    public String question;
    public String reply;
    public Date create_date;
    public String create_user;
    public String reply_user;
    public Date reply_date;
    public Date show_date;
    
    public  boolean NTcreat(){
		  String sql="insert into  mb_message_board (cust_name,question,reply,create_date," +
		  		"create_user,reply_user,reply_date,show_date) values(?,?,?,now(),?,?,now(),?)";
		  int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{cust_name,question,reply,
				  create_user,reply_user,show_date});
		  return count>0;
	 }
	 public  boolean NTupdate(){
		  String sql="update mb_message_board set cust_name=?,question=?,reply=?,reply_user=?,reply_date=now()," +
		  		"show_date=?  where msg_id=?";
		  int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{cust_name,question,reply,
				  reply_user,show_date,msg_id});
		  return count>0;
	 }
	 public static boolean NTdelete(Long id){
		  String sql="delete from  mb_message_board where msg_id=?";
		  int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{id});
		  return count>0;
	 }
    
}
