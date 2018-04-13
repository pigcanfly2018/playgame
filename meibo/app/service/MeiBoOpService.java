package service;

import models.Customer;
import models.MessageBoard;
import util.Sp;

public class MeiBoOpService {
	
	/**
	 * 创建留言
	 * @param mb
	 * @return
	 */
	public static boolean createMsg(MessageBoard mb){
		      String sql="insert into  mb_message_board (cust_name,question,reply,create_date," +
			  		"create_user,reply_user,reply_date,show_date) values(?,?,?,now(),?,?,?,now())";
			  int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{mb.cust_name,mb.question,mb.reply,
					  mb.create_user,mb.reply_user,mb.show_date});
			  return count>0;
	}
	
	 
	 /**
	  * @param login_name 
	  * @param log_type
	  * @param ip
	  * @param user_agent
	  * @param log_msg
	  */
	 public static boolean logCust(Long cust_id,String login_name,Integer log_type,String ip,String user_agent,String log_msg){
		 String sql="insert into mb_cust_log (log_type,log_msg,ip,user_agent,create_date,cust_id,login_name) "
		 		+ "values(?,?,?,?,now(),?,?)";
		 int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{log_type,log_msg,ip,user_agent,cust_id,login_name});
		 return count>0;
	 }
	 
	 /**
	  * 记录登录信息
	  */
	 public static void logLogin(){
		 
		 
	 }
	 
	 
	

}
