package com.product.bda.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import bsz.exch.core.MyPreparedStatementCreator;
import bsz.exch.utils.LogHelper;

public class PayService {
	private static Logger logger=Logger.getLogger(PayService.class);
	
	private JdbcTemplate template;
	private String datasource;
			
	public PayService(JdbcTemplate template,String datasource){
		this.template=template;
		this.datasource=datasource;
	}
	
	/**
	 * 创建订单
	 * @param pay_id
	 * @param login_name
	 * @param amount
	 * @param goods_name
	 * @param partner_id
	 * @param pay_method
	 * @param remark
	 * @param create_date
	 * @param order_no
	 * @return
	 */
	public  Long createOrder(String tongdao,String pay_id,String login_name,
			String amount,String ip,
			String pay_method,String remark,String return_url){
		
		 if(tongdao.equals("antong")){
			 String sql="insert into mb_ybp(pay_id,login_name,amount,payip,pay_method,remark,"
				  		+ "create_date,state,return_url) values(?,?,?,?,?,?,now(),0,?)";
				  Object[] objs=new Object[]{pay_id,login_name,amount,ip,pay_method,remark,return_url};
				  KeyHolder keyHolder = new GeneratedKeyHolder();
				  logger.info(LogHelper.dbLogObj(datasource,sql,Arrays.asList(objs)));
				  template.update(new MyPreparedStatementCreator(sql,objs), keyHolder);
				  return keyHolder.getKey().longValue(); 
		 }else if(tongdao.equals("lefubao")){
			 String sql="insert into mb_ybp(pay_id,login_name,amount,payip,pay_method,remark,"
				  		+ "create_date,state,return_url) values(?,?,?,?,?,?,now(),0,?)";
				  Object[] objs=new Object[]{pay_id,login_name,amount,ip,pay_method,remark,return_url};
				  KeyHolder keyHolder = new GeneratedKeyHolder();
				  logger.info(LogHelper.dbLogObj(datasource,sql,Arrays.asList(objs)));
				  template.update(new MyPreparedStatementCreator(sql,objs), keyHolder);
				  return keyHolder.getKey().longValue(); 
		 }else if(tongdao.equals("huitpay")){
			 String sql="insert into mb_xftp(pay_id,login_name,amount,payip,pay_method,remark,"
				  		+ "create_date,state,return_url) values(?,?,?,?,?,?,now(),0,?)";
				  Object[] objs=new Object[]{pay_id,login_name,amount,ip,pay_method,remark,return_url};
				  KeyHolder keyHolder = new GeneratedKeyHolder();
				  logger.info(LogHelper.dbLogObj(datasource,sql,Arrays.asList(objs)));
				  template.update(new MyPreparedStatementCreator(sql,objs), keyHolder);
				  return keyHolder.getKey().longValue(); 
		 }
		 
		 return null;
		  	
	}
	
	/**
	 * 查询是否已处理
	 * @param pay_id
	 * @return
	 */
	public boolean isNotDone(String tongdao,String pay_id,String login_name){
		if(tongdao.equals("antong")){
			String sql="select count(1) from mb_ybp where state= 0 and pay_id =? and login_name=? ";
			 Object[] objs=new Object[]{pay_id,login_name};
			 logger.info(LogHelper.dbLogObj(datasource,sql,Arrays.asList(objs)));
			 int count=template.queryForObject(sql, objs, Integer.class);
			 return count>0;
		}else if(tongdao.equals("lefubao")){
			String sql="select count(1) from mb_ybp where state= 0 and pay_id =? and login_name=? ";
			 Object[] objs=new Object[]{pay_id,login_name};
			 logger.info(LogHelper.dbLogObj(datasource,sql,Arrays.asList(objs)));
			 int count=template.queryForObject(sql, objs, Integer.class);
			 return count>0;
		}else if(tongdao.equals("huitpay")){
			String sql="select count(1) from mb_xftp where state= 0 and pay_id =? and login_name=? ";
			 Object[] objs=new Object[]{pay_id,login_name};
			 logger.info(LogHelper.dbLogObj(datasource,sql,Arrays.asList(objs)));
			 int count=template.queryForObject(sql, objs, Integer.class);
			 return count>0;
		}
		
		return false;
		 
	}
	
	
	/**
	 * 更新订单状态
	 * @return
	 */
	public boolean updaeteStatus(String tongdao,String pay_id,String orderNo,String state,BigDecimal amount,String channel){
		
		if(tongdao.equals("antong")){
			String sql="update mb_ybp set order_no=?,real_amount=?, state=?,  "
			 		+ "modify_time=now(),real_pay_method=?,real_amount=?,  finished_date =now() where pay_id=? ";
			 Object[] objs=new Object[]{orderNo,amount,state,channel,amount.multiply(new BigDecimal(0.987)).setScale(2, RoundingMode.HALF_UP),pay_id};
			 logger.info(LogHelper.dbLogObj(datasource,sql,Arrays.asList(objs)));
			 return  template.update(sql,objs)>0;
		}else if(tongdao.equals("lefubao")){
			String sql="update mb_ybp set order_no=?,real_amount=?, state=?,  "
			 		+ "modify_time=now(),real_pay_method=pay_method,real_amount=?,  finished_date =now() where pay_id=? ";
			 Object[] objs=new Object[]{orderNo,amount,state,amount.multiply(new BigDecimal(0.987)).setScale(2, RoundingMode.HALF_UP),pay_id};
			 logger.info(LogHelper.dbLogObj(datasource,sql,Arrays.asList(objs)));
			 return  template.update(sql,objs)>0;
		}else if(tongdao.equals("huitpay")){
			String sql="update mb_xftp set order_no=?,real_amount=?, state=?,  "
			 		+ "modify_time=now(),real_pay_method=pay_method,real_amount=?,  finished_date =now() where pay_id=? ";
			 Object[] objs=new Object[]{orderNo,amount,state,amount.multiply(new BigDecimal(0.987)).setScale(2, RoundingMode.HALF_UP),pay_id};
			 logger.info(LogHelper.dbLogObj(datasource,sql,Arrays.asList(objs)));
			 return  template.update(sql,objs)>0;
		}
		 
		return false;
	}
	
	public String queryLoginName(String tongdao,String pay_id){
		 
		if(tongdao.equals("lefubao")){
			String sql="select login_name from mb_ybp  where pay_id=? ";
			 Object[] objs=new Object[]{pay_id};
			 logger.info(LogHelper.dbLogObj(datasource,sql,Arrays.asList(objs)));
			 return  template.queryForObject(sql, objs, String.class);
		}else if(tongdao.equals("huitpay")){
			String sql="select login_name from mb_xftp  where pay_id=? ";
			 Object[] objs=new Object[]{pay_id};
			 logger.info(LogHelper.dbLogObj(datasource,sql,Arrays.asList(objs)));
			 return  template.queryForObject(sql, objs, String.class);
		}
		return null;
	}
	
	public String queryPayType(String tongdao,String pay_id){
		
		if(tongdao.equals("lefubao")){
			String sql="select pay_method from mb_ybp  where pay_id=? ";
			 Object[] objs=new Object[]{pay_id};
			 logger.info(LogHelper.dbLogObj(datasource,sql,Arrays.asList(objs)));
			 return  template.queryForObject(sql, objs, String.class);
		}else if(tongdao.equals("huitpay")){
			String sql="select pay_method from mb_xftp  where pay_id=? ";
			 Object[] objs=new Object[]{pay_id};
			 logger.info(LogHelper.dbLogObj(datasource,sql,Arrays.asList(objs)));
			 return  template.queryForObject(sql, objs, String.class);
		}
		
		return "";
		
	}
	
	
	
		
}
