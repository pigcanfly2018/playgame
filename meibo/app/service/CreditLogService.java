package service;

import java.math.BigDecimal;
import java.util.Date;

import models.MyPreparedStatementCreator;

import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import util.Sp;

public class CreditLogService {
	
	public static String API_IN="API转入";
	public static String API_OUT="API转出";
    public static String Depoist="存款";
    public static String Withdraw="提款";
    public static String Fix="额度修正";
    public static String Gift="礼金添加";
    
	
    public static boolean NTcreat(String log_type,BigDecimal credit,String login_name,Long cust_id,
    		String user,String remarks,String order_no,BigDecimal org_credit,BigDecimal after_credit,Long parent_id){
  	  final String sql="insert into  mb_credit_log (log_type,credit,login_name,cust_id,create_date,"
  	  		+ "create_user,remarks,order_no,org_credit,after_credit,parent_id) values(?,?,?,?,now(),?,?,?,?,?,?)";
  	  final Object[] objects=new Object[]{log_type,credit,login_name,cust_id,user,remarks,order_no,org_credit,after_credit,parent_id};
	  KeyHolder keyHolder = new GeneratedKeyHolder();
	  int result=Sp.get().getDefaultJdbc().update(new MyPreparedStatementCreator(sql,objects), keyHolder);
	  return result>0;
    }  

}
