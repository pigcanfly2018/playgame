package models;

import java.util.Date;
import java.util.List;

import util.Sp;

public class Msg {
	public Long msg_id;
	
	/**
	 * 1 客户开户
	 * 2 客户存款
	 * 3 客户提款
	 * 4 客服提款
	 * 5 客服存款
	 */
    public Integer msg_type;
    public String content;
    public Date create_date;
    public Integer flag;
    
    public static List<Msg> queryNotDo(){
    	String sql="select * from mb_msg where flag=0";
    	List<Msg> roleList=Sp.get().getDefaultJdbc().query(sql,new MsgRowMap());
    	return roleList;
    }
    public static boolean NTcreateMsg(Integer type,String content){
    	String sql="insert into mb_msg(msg_type,content,create_date,flag) values(?,?,now(),0)";
    	return Sp.get().getDefaultJdbc().update(sql,new Object[]{type,content})>0;
    }
    public static boolean NTdo(Long msg_id){
    	String sql="update mb_msg set flag=1 where msg_id=?";
    	return Sp.get().getDefaultJdbc().update(sql,new Object[]{msg_id})>0;
    }
    
    public static List<Msg> NTquerMsgByUser(String user){
    	String sql="select b.msg_id as msg_id,b.msg_type as msg_type ,b.content as content,b.create_date as create_date ,b.flag as flag " +
    			"from mb_user_msg a left join mb_msg b on a.msg_id=b.msg_id where a.notify_flag=0 and a.login_name=? and b.msg_id is not null";
    	List<Msg> roleList=Sp.get().getDefaultJdbc().query(sql,new Object[]{user},new MsgRowMap());
    	return roleList;
    }
    
    

}
