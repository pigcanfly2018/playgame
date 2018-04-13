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

public class ThpService {

	private static Logger logger=Logger.getLogger(ThpService.class);
	
	private JdbcTemplate template;
	private String datasource;
			
	public ThpService(JdbcTemplate template,String datasource){
		this.template=template;
		this.datasource=datasource;
	}
	
	
	/**
	 * 创建订单
	 * @param pay_id
	 * @param login_name
	 * @param amount
	 * @param customer_ip
	 * @param partner_id
	 * @param pay_method
	 * @param remark
	 * @param create_date
	 * @param order_no
	 * @return
	 */
	public  Long createThp(String pay_id,String login_name,
			String amount,String customer_ip,
			String bank_code,String remark,String return_url){
		  String sql="insert into mb_thp(pay_id,login_name,amount,customer_ip,bank_code,remark,"
		  		+ "create_date,state,return_url) values(?,?,?,?,?,?,now(),0,?)";
		  Object[] objs=new Object[]{pay_id,login_name,amount,customer_ip,bank_code,remark,return_url};
		  KeyHolder keyHolder = new GeneratedKeyHolder();
		  logger.info(LogHelper.dbLogObj(datasource,sql,Arrays.asList(objs)));
		  template.update(new MyPreparedStatementCreator(sql,objs), keyHolder);
		  return keyHolder.getKey().longValue();	
	}
	
	/**
	 * 查询是否已处理
	 * @param pay_id
	 * @return
	 */
	public boolean isNotDoThp(String pay_id,String login_name){
		 String sql="select count(1) from mb_thp where state= 0 and pay_id =? and login_name=? ";
		 Object[] objs=new Object[]{pay_id,login_name};
		 logger.info(LogHelper.dbLogObj(datasource,sql,Arrays.asList(objs)));
		 int count=template.queryForObject(sql, objs, Integer.class);
		 return count>0;
	}
	
	/**
	 * 更新订单状态
	 * @return
	 */
	public boolean updaeteThp(String order_no,String trade_no,String state,BigDecimal amount,String login_name){
		 String sql="update mb_thp set order_no=?,real_amount=?, state=?,  "
		 		+ "modify_time=now(), finished_date =now() where pay_id=? and login_name=? ";
		 Object[] objs=new Object[]{trade_no,amount.multiply(new BigDecimal(0.98)).setScale(2, RoundingMode.HALF_UP),state,order_no,login_name};
		 logger.info(LogHelper.dbLogObj(datasource,sql,Arrays.asList(objs)));
		 return  template.update(sql,objs)>0;
	}
	
	public String queryLoginName(String pay_id){
		 String sql="select login_name from mb_thp  where pay_id=? ";
		 Object[] objs=new Object[]{pay_id};
		 logger.info(LogHelper.dbLogObj(datasource,sql,Arrays.asList(objs)));
		 return  template.queryForObject(sql, objs, String.class);
	}
	
	
	
}
