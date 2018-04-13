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
	  public Boolean finished;
	  
	  
	  public static  boolean NTcreat(String bill_no,BigDecimal amount,String bank_code,String status,
			  String order_time,String dep_no, String login_name,Long cust_id){
			  String sql="insert into  mb_hc_pay (bill_no,amount,bank_code,"
			  		+ "status,order_time,dep_no,login_name,cust_id,create_date,finished) "
			  		+ "values(?,?,?,?,?,?,?,?,now(),0)";
			  int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{bill_no,amount,bank_code,
					  status,order_time,dep_no,login_name,cust_id});
			  return count>0;
	   }
	  
	  
	  public static boolean queryFinished(String order_no){
	    	String sql="select count(1) from mb_hc_pay where finished=1 and bill_no=?";
	    	return Sp.get().getDefaultJdbc().queryForObject(sql,new Object[]{order_no},Integer.class)>0;
	   }
	    
	    public static boolean updatefinished(String status,String bill_no){
	    	  String sql="update mb_hc_pay set finish_date=now(),status=?,finished=1 where bill_no=?";
	    	  int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{status,bill_no});
			  return count>0;
	    }
	    
	    public static boolean updateDepNo(String dep_no,String bill_no){
	    	  String sql="update mb_hc_pay set dep_no=? where bill_no=?";
	    	  int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{dep_no,bill_no});
			  return count>0;
	    }
	    
	    public static Hcpay NTget(String bill_no){
	  	      String sql ="select * from mb_hc_pay where bill_no=?";
	  	      Hcpay dep=(Hcpay) Sp.get().getDefaultJdbc().queryForObject(sql,
	  					new Object[]{bill_no},new HcpayRowMap());
	  	      return dep;
	    }
	    
	  
	   
	  
	  
	  
}
