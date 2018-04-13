package service;

import java.math.BigDecimal;

import models.Customer;
import util.MD5;

public class CustomerService{
	
	/**
	 * 创建客户
	 * @param cust
	 * @return
	 */
	 public static Customer createCustomer(Customer cust){
		 if(Customer.createCustomer(cust)){
			 Customer cust1=Customer.getCustomer(cust.login_name);
			 Customer.logLogin(cust.reg_ip, cust1.cust_id);
			 return cust1;
		 }
	    return null;
	 }
	
     /**
      * 	
      * @param loginName
      * @param pwd
      * @param ip
      * @return
      */
	 public static Customer login(String loginName,String pwd,String ip){
			pwd=MD5.md5(pwd);
			Customer customer=Customer.getCustomer(loginName);
			if(customer==null) return null;
			if(customer.login_pwd==null) return null;
			if(customer.login_pwd.equals(pwd)){
				if(customer.flag){
					Customer.logLogin(ip, customer.cust_id);
				}
				return customer;
			}
			return null;
		}
	 
	 public static boolean modfiPwd(String login_name,String pwd,String newpwd){
			Customer customer=Customer.getCustomer(login_name);
			pwd=MD5.md5(pwd);
			if(!customer.login_pwd.equals(pwd)){
				return false;
			}
			newpwd=MD5.md5(newpwd);
			return Customer.NTmodfyPwd(customer.login_name,newpwd);
	 }
	
	 public static boolean modfiBank(String login_name,String bank_name,String account,String bank_dot,String account_type){
			Customer customer=Customer.getCustomer(login_name);
			if(customer!=null){
				return Customer.NTmodfyBank(login_name, bank_name, account, account_type, bank_dot);
			}
			return false;
	 }
	 
	 public static boolean modCredit(Long cust_id,String log_type,String login_name,
			 BigDecimal credit,String user,String remarks,String order_no){
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
	 
}