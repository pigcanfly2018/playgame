package models;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import util.Sp;

public class Withdraw {
	       public Long withdraw_id;
	       public String login_name;
	       public String real_name;
	       public BigDecimal amount;
	       public String bank_name;
	       public String account_name;
	       public String account;
	       public String account_type;
	       public String location;
	       public String remarks;
	       public Date create_date;
	       public String create_user;
	       public Integer status;
	       public Long cust_id;
	       public String wit_no;
	       public Boolean locked;
	       public Integer wit_cnt;
	       public Integer cust_level;
	       
	       public  Long NTcreat(){
			  final String sql="insert into  mb_withdraw (wit_no,login_name,real_name,amount,bank_name,account_name,account,account_type,location,remarks,create_date,create_user,status,cust_id,wit_cnt) values(?,?,?,?,?,?,?,?,?,?,now(),?,?,?,?)";
			  final Object[] objects=new Object[]{wit_no,login_name,real_name,amount,bank_name,account_name,account,account_type,location,remarks,create_user,status,cust_id,wit_cnt};
			  KeyHolder keyHolder = new GeneratedKeyHolder();
			  Sp.get().getDefaultJdbc().update(new MyPreparedStatementCreator(sql,objects), keyHolder);
			  return keyHolder.getKey().longValue();
		    }
	       
	       public static Withdraw NTget(Long withdraw_id){
	 	      String sql ="select * from mb_withdraw where withdraw_id=?";
	 	     Withdraw dep=(Withdraw) Sp.get().getDefaultJdbc().queryForObject(sql,
	 					new Object[]{withdraw_id},new WithdrawRowMap());
	 	      return dep;
	        }
	       
	       public static boolean NTchangeStatus(Long withdraw_id,Integer status){
	    		  String sql ="update mb_withdraw set status=? where withdraw_id=?";
	    		  int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{status,withdraw_id});
	    		  return count>0;
	    	}
	       
	       public static int NTgetCount(Long cust_id){
	    		  String sql ="select count(1) from mb_withdraw where cust_id=? and status =5";
	    		  int count=Sp.get().getDefaultJdbc().queryForObject(sql, new Object[]{cust_id},Integer.class);
	    		  return count;
	    	}
	   
	       public static boolean NTrecPayDate(Long withdraw_id){
	    		  String sql ="update mb_withdraw set pay_date=now() where withdraw_id=? and status=5";
	    		  int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{withdraw_id});
	    		  return count>0;
	    	}
	       
	       public static boolean locked(Long withdraw_id){
	    	   String sql="update mb_withdraw set locked =1 where withdraw_id=?";
	    	   int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{withdraw_id});
	    	   return count>0;
	       }
	       public static boolean unLocked(Long withdraw_id){
	    	   String sql="update mb_withdraw set locked =0 where withdraw_id=?";
	    	   int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{withdraw_id});
	    	   return count>0;
	       }

}
