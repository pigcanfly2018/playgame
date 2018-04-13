package models;

import java.util.Date;
import java.util.List;

import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import util.Sp;

public class AgentWithdrawLog {

	 public Long alog_id;
     public Integer pre_status;
     public Integer after_status;
     public Long awithdraw_id;
     public String remarks;
     public Date create_date;
     public String create_user;
     public String awit_no;
     
     
     public static List<AgentWithdrawLog> NTgetLogsByWit(Long awithdraw_id){
	     	String sql="select * from mb_agent_withdraw_log where awithdraw_id =? ";
	 		List<AgentWithdrawLog> funcs=Sp.get().getDefaultJdbc().query(sql,
	 					new Object[]{awithdraw_id},new AgentWithdrawLogRowMap());
	     	return funcs;
	     	
	 }
     
     public  static boolean NTcreat(Integer pre_status,Integer after_status,Long awithdraw_id,String remarks,String user){
   	  final String sql="insert into  mb_agent_withdraw_log (pre_status,after_status,awithdraw_id,remarks,create_date,create_user) values(?,?,?,?,now(),?)";
		  final Object[] objects=new Object[]{pre_status,after_status,awithdraw_id,remarks,user};
		  KeyHolder keyHolder = new GeneratedKeyHolder();
		  int result=Sp.get().getDefaultJdbc().update(new MyPreparedStatementCreator(sql,objects), keyHolder);
		  return result>0;
   }
     
}
