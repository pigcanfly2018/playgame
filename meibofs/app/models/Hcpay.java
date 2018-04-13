package models;

import java.math.BigDecimal;
import java.util.Date;

import util.Sp;

public class Hcpay {
	  public Long gc_pay_id;
	  public String bill_no;
	  public BigDecimal amount;
	  public String bank_code;
	  public Date create_date;
	  public String status;
	  public String order_time;
	  public Date finish_date;
	  public String dep_no;
	  public String login_name;
	  public Long cust_id;
	  
	  
	  public static  boolean NTcreat(String bill_no,BigDecimal amount,String bank_code,String status,
			  String order_time,String dep_no, String login_name,Long cust_id){
			  String sql="insert into  mb_hc_pay (bill_no,amount,bank_code,"
			  		+ "status,order_time,dep_no,login_name,cust_id,create_date) "
			  		+ "values(?,?,?,?,?,?,?,?,now())";
			  int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{bill_no,amount,bank_code,
					  status,order_time,dep_no,login_name,cust_id});
			  return count>0;
	   }
	  
	   
	  
	  
	  
}
