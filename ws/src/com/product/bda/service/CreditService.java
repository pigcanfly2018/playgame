package com.product.bda.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import bsz.exch.core.MyPreparedStatementCreator;
import bsz.exch.utils.LogHelper;

public class CreditService {
	
	
    private static Logger logger=Logger.getLogger(CreditService.class);
	
	private JdbcTemplate template;
	private String datasource;
	
	public CreditService(JdbcTemplate template,String datasource){
		this.template=template;
		this.datasource=datasource;
	}
	
    public  boolean creditLog(String log_type,BigDecimal credit,String login_name,Long cust_id,
    		String user,String remarks,String order_no,BigDecimal org_credit,BigDecimal after_credit,Long parent_id){
  	  final String sql="insert into  mb_credit_log (log_type,credit,login_name,cust_id,create_date,"
  	  		+ "create_user,remarks,order_no,org_credit,after_credit,parent_id) values(?,?,?,?,now(),?,?,?,?,?,?)";
  	  final Object[] objects=new Object[]{log_type,credit,login_name,cust_id,user,remarks,order_no,org_credit,after_credit,parent_id};
	  KeyHolder keyHolder = new GeneratedKeyHolder();
	  logger.info(LogHelper.dbLogObj(datasource,sql,Arrays.asList(objects)));
	  int result=template.update(new MyPreparedStatementCreator(sql,objects), keyHolder);
	  return result>0;
    }  
	   
	/**
	 * 增加额度（记录日志）
	 * @return
	 */
	public boolean add(String login_name,BigDecimal credit,String log_type,String remarks,String create_user,String order_no){
		CustomerService custService  =new CustomerService(template,datasource);
		Customer  customer=custService.getCustomer(login_name);
		BigDecimal  preCredit=customer.credit;
		String sql="update mb_customer set credit = (credit+?) where login_name =? ";
		List<Object> list =new ArrayList<Object>();
		list.add(credit);
		list.add(login_name);
		logger.info(LogHelper.dbLogObj(datasource,sql, list));
		int count=template.update(sql,list.toArray(new Object[0]));
		if(count>0){
			//增加额度记录
			customer=custService.getCustomer(login_name);
			BigDecimal  afterCredit=customer.credit;
			if(!this.creditLog(log_type, credit, login_name, customer.cust_id, create_user, remarks, order_no, preCredit, afterCredit,customer.parent_id)){
				logger.error("Credit Lost! --[login_name="+login_name+",type=add,credit="+credit+",order_no="+order_no+"]");
			}
		}
		return count>0;
	}
	
	
	/**
	 * 增加额度（记录日志）
	 * @return
	 */
	public boolean addforgametransfer(String login_name,BigDecimal credit,String log_type,String remarks,String create_user,String order_no){
		CustomerService custService  =new CustomerService(template,datasource);
		Customer  customer=custService.getCustomer(login_name);
		BigDecimal  preCredit=customer.credit;
		String sql="update mb_customer set credit = (credit+?) where login_name =? ";
		List<Object> list =new ArrayList<Object>();
		list.add(credit);
		list.add(login_name);
		logger.info(LogHelper.dbLogObj(datasource,sql, list));
		int count=template.update(sql,list.toArray(new Object[0]));
		if(count>0){
			//增加额度记录
			customer=custService.getCustomer(login_name);
			BigDecimal  afterCredit=customer.credit;
			if(!this.creditLog(log_type, credit, login_name, customer.cust_id, create_user, remarks, order_no, preCredit, afterCredit,null)){
				logger.error("Credit Lost! --[login_name="+login_name+",type=add,credit="+credit+",order_no="+order_no+"]");
			}
		}
		return count>0;
	}
	
	
	/**
	 * 减少额度
	 * @param login_name
	 * @param credit
	 * @param log_type
	 * @param remarks
	 * @param create_user
	 * @param order_no
	 * @return
	 */
	public boolean subtract(String login_name,BigDecimal credit,String log_type,String remarks,String create_user,String order_no){
		CustomerService custService  =new CustomerService(template,datasource);
		Customer  customer=custService.getCustomer(login_name);
		BigDecimal  preCredit=customer.credit;
		String sql="update mb_customer set credit = (credit-?) where login_name =? ";
		List<Object> list =new ArrayList<Object>();
		list.add(credit);
		list.add(login_name);
		logger.info(LogHelper.dbLogObj(datasource,sql, list));
		int count=template.update(sql,list.toArray(new Object[0]));
		if(count>0){
			//增加额度记录
			customer=custService.getCustomer(login_name);
			BigDecimal  afterCredit=customer.credit;
			if(!this.creditLog(log_type, credit, login_name, customer.cust_id, create_user, remarks, order_no, preCredit, afterCredit,null)){
				logger.error("Credit Lost! --[login_name="+login_name+",type=subtract,credit="+credit+",order_no="+order_no+"]");
			}
		}
		return count>0;
	}
	
	public boolean subtractforwithdraw(String login_name,BigDecimal credit,String log_type,String remarks,String create_user,String order_no){
		CustomerService custService  =new CustomerService(template,datasource);
		Customer  customer=custService.getCustomer(login_name);
		BigDecimal  preCredit=customer.credit;
		String sql="update mb_customer set credit = (credit-?) where login_name =? ";
		List<Object> list =new ArrayList<Object>();
		list.add(credit);
		list.add(login_name);
		logger.info(LogHelper.dbLogObj(datasource,sql, list));
		int count=template.update(sql,list.toArray(new Object[0]));
		if(count>0){
			//增加额度记录
			customer=custService.getCustomer(login_name);
			BigDecimal  afterCredit=customer.credit;
			if(!this.creditLog(log_type, new BigDecimal(0).subtract(credit), login_name, customer.cust_id, create_user, remarks, order_no, preCredit, afterCredit,null)){
				logger.error("Credit Lost! --[login_name="+login_name+",type=subtract,credit="+credit+",order_no="+order_no+"]");
			}
		}
		return count>0;
	}
	
	
	
	
	

}
