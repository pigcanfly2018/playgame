package models;

import java.util.Date;
import java.util.List;

import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import util.Sp;

public class WithdrawLog {
	       public Long log_id;
	       public Integer pre_status;
	       public Integer after_status;
	       public Long withdraw_id;
	       public String remarks;
	       public Date create_date;
	       public String create_user;
	       public String wit_no;

	       public  static boolean NTcreat(Integer pre_status,Integer after_status,Long withdraw_id,String remarks,String user){
	    	  final String sql="insert into  mb_withdraw_log (pre_status,after_status,withdraw_id,remarks,create_date,create_user) values(?,?,?,?,now(),?)";
			  final Object[] objects=new Object[]{pre_status,after_status,withdraw_id,remarks,user};
			  KeyHolder keyHolder = new GeneratedKeyHolder();
			  int result=Sp.get().getDefaultJdbc().update(new MyPreparedStatementCreator(sql,objects), keyHolder);
			  return result>0;
	    }
	     
	     public static List<WithdrawLog> NTgetLogsByWit(Long withdraw_id){
	     	String sql="select * from mb_withdraw_log where withdraw_id =? order by log_id asc";
	 		List<WithdrawLog> funcs=Sp.get().getDefaultJdbc().query(sql,
	 					new Object[]{withdraw_id},new WithdrawLogRowMap());
	     	return funcs;
	     	
	     }


}
