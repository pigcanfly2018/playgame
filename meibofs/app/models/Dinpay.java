package models;

import java.math.BigDecimal;
import java.util.Date;

import util.Sp;

public class Dinpay {
	public Long dinpay_id;
    public Long cust_id;
    public String login_name;
    public String merchant_code;
    public BigDecimal order_amount;
    public String order_time;
    public String client_ip;
    public String product_name;
    public String bank_code;
    public String bank_name;
    public String bank_seq_no;
    public String trade_status;
    public String trade_no;
    public String trade_time;
    public Date create_date;
    public String notify_id;
    public String interface_version;
    public String rec_sign_type;
    public String rec_sign;
    public BigDecimal pay_amount;
    public String send_sign_type;
    public String send_sign;
    public String order_no;
    public Boolean finished;
    public Date finished_date;
    public String dep_no;
    public static  boolean NTcreat(Long cust_id,String login_name,String merchant_code,
	  		BigDecimal order_amount,String order_time,String client_ip,String product_name,
	  		String bank_code,String bank_name, String interface_version,
	  		String send_sign_type,String send_sign,Integer trade_status,String order_no){
		  String sql="insert into  mb_dinpay (cust_id,login_name,merchant_code,"
		  		+ "order_amount,order_time,client_ip,product_name,bank_code,bank_name,"
		  		+ "interface_version,send_sign_type,send_sign,trade_status,order_no) "
		  		+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		  int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{cust_id,login_name,merchant_code,
				  order_amount,order_time,client_ip,product_name,bank_code,bank_name,
				  interface_version,
				  send_sign_type,send_sign,trade_status,order_no});
		  return count>0;
   }
    
    public static boolean queryFinished(String order_no){
    	String sql="select count(1) from mb_dinpay where finished=1 and order_no=?";
    	return Sp.get().getDefaultJdbc().queryForObject(sql,new Object[]{order_no},Integer.class)>0;
    	
    }
    
    public static boolean updatefinished(String bank_seq_no,String trade_status,
    		String trade_no,String trade_time,String notify_id,String rec_sign_type,
    		String rec_sign,BigDecimal pay_amount,String order_no){
    	  String sql="update mb_dinpay set bank_seq_no=?,trade_status=?,trade_no=?,trade_time=?,"
    			+ "notify_id=?,rec_sign_type=?,rec_sign=?,pay_amount=?,finished=1,finished_date=now() "
    			+ "where order_no=?";
    	  int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{bank_seq_no,trade_status,
    	    		trade_no,trade_time,notify_id,rec_sign_type,
    	    		rec_sign,pay_amount,order_no});
		  return count>0;
    }
    
    public static boolean updateDepNo(String dep_no,String order_no){
    	  String sql="update mb_dinpay set dep_no=? where order_no=?";
    	  int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{dep_no,order_no});
		  return count>0;
    }
    
    public static Dinpay NTget(String order_no){
  	      String sql ="select * from mb_dinpay where order_no=?";
  	      Dinpay dep=(Dinpay) Sp.get().getDefaultJdbc().queryForObject(sql,
  					new Object[]{order_no},new DinpayRowMap());
  	      return dep;
    }
    
}
