package models;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import util.Sp;

public class TransferLog {
	public Long transfer_id;
    public Long cust_id;
    public BigDecimal credit;
    public String transer_from;
    public String transer_to;
    public Integer status;
    public String bill_no;
    public Date create_date;
    public String create_user;
    public String remarks;
    public String login_name;
    public Date finish_date;
	public  Long NTcreat(){
		  String sql="insert into  mb_transfer_log (cust_id,credit,transer_from,transer_to,"
		  		+ "status,bill_no,create_date,create_user,remarks,login_name,finish_date) "
		  		+ "values(?,?,?,?,?,?,?,?,?,?,?)";
		  Object[] objects=new Object[]{cust_id,credit,transer_from,transer_to,status,bill_no,create_date,create_user,remarks,login_name,finish_date};
		  KeyHolder keyHolder = new GeneratedKeyHolder();
		  Sp.get().getDefaultJdbc().update(new MyPreparedStatementCreator(sql,objects), keyHolder);
		  return keyHolder.getKey().longValue();
	}
	public static Long start(Long cust_id,BigDecimal credit,String transer_from,String transer_to,
			String bill_no,String create_user,String remarks,String login_name){
		     String sql="insert into  mb_transfer_log (cust_id,credit,transer_from,transer_to,"
			  		+ "status,bill_no,create_date,create_user,remarks,login_name) "
			  		+ "values(?,?,?,?,?,?,now(),?,?,?)";
			  Object[] objects=new Object[]{cust_id,credit,transer_from,transer_to,0,bill_no,create_user,remarks,login_name};
			  KeyHolder keyHolder = new GeneratedKeyHolder();
			  Sp.get().getDefaultJdbc().update(new MyPreparedStatementCreator(sql,objects), keyHolder);
			  return keyHolder.getKey().longValue();
	}
	
	public static boolean finish(Long transfer_id,Integer status){
		      String sql="update mb_transfer_log set status=? ,finish_date=now()  where transfer_id=?";
			  Object[] objects=new Object[]{status,transfer_id};
			  int count=Sp.get().getDefaultJdbc().update(sql, objects);
			 return count>0;
	}

}
