package models;

import java.util.Date;
import java.util.List;

import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import util.Sp;

public class DepositLog {

    public Long log_id;
    public Integer pre_status;
    public Integer after_status;
    public Long deposit_id;
    public String remarks;
    public Date create_date;
    public String create_user;
    public String dep_no;
    
    public  Long NTcreat(){
		  final String sql="insert into  mb_deposit_log (pre_status,after_status,deposit_id,remarks,create_date,create_user,dep_no) values(?,?,?,?,now(),?,?)";
		  final Object[] objects=new Object[]{pre_status,after_status,deposit_id,remarks,create_user,dep_no};
		  KeyHolder keyHolder = new GeneratedKeyHolder();
		  Sp.get().getDefaultJdbc().update(new MyPreparedStatementCreator(sql,objects), keyHolder);
		  return keyHolder.getKey().longValue();
	 }
    
    public  static boolean NTcreat(Integer pre_status,Integer after_status,Long deposit_id,String remarks,String user,String dep_no){
    	  final String sql="insert into  mb_deposit_log (pre_status,after_status,deposit_id,remarks,create_date,create_user,dep_no) values(?,?,?,?,now(),?,?)";
		  final Object[] objects=new Object[]{pre_status,after_status,deposit_id,remarks,user,dep_no};
		  KeyHolder keyHolder = new GeneratedKeyHolder();
		  int result=Sp.get().getDefaultJdbc().update(new MyPreparedStatementCreator(sql,objects), keyHolder);
		  return result>0;
    }
    
    public static List<DepositLog> NTgetLogsByDep(Long deposit_id){
    	String sql="select * from mb_deposit_log where deposit_id =? order by log_id asc ";
		List<DepositLog> funcs=Sp.get().getDefaultJdbc().query(sql,
					new Object[]{deposit_id},new DepositLogRowMap());
    	return funcs;
    	
    }
}
