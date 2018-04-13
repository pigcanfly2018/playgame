package service;

import java.math.BigDecimal;

import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import models.Customer;
import models.MyPreparedStatementCreator;
import util.MD5;
import util.Sp;

public class CustomerService{
	
	 public static boolean modCredit(Long cust_id,String log_type,String login_name,BigDecimal credit,String user,String remarks,String order_no){
		 Customer customer=Customer.getCustomer(login_name);
		 BigDecimal org_credit = customer.credit;
		 boolean b=Customer.modCredit(cust_id, credit);
		 customer=Customer.getCustomer(login_name);
		 BigDecimal after_credit= customer.credit;
		 if(b)
		 CreditLogService.NTcreat(log_type, credit, login_name, cust_id, user, remarks, order_no, org_credit, after_credit,customer.parent_id);
         return b;
	 }
	 
	 public static boolean modScore(String rec_code,String rec_type,BigDecimal score,String login_name,Long cust_id,String user){
		 boolean b=Customer.modScore(cust_id, score);
		 if(b){
			 Customer customer=Customer.getCustomer(login_name);
			 BigDecimal cur_score= customer.score;
			 ScoreLogService.NTcreat(rec_code, rec_type, score, login_name, cust_id, user,cur_score);
		 }
         return b;
	 }
	 
	 
	 public static boolean createOrderNumber(String ordernumber){
	  	  final String sql="insert into mb_ordernumber(ordernumber) values(?)";
	  	  final Object[] objs=new Object[]{ordernumber};
		  KeyHolder keyHolder = new GeneratedKeyHolder();
		  int result=Sp.get().getDefaultJdbc().update(new MyPreparedStatementCreator(sql,objs), keyHolder);
		  return result>0;
	 }
	 
	 
}