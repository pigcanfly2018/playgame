package com.product.bda.service;

import java.math.BigDecimal;
import java.util.Arrays;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import bsz.exch.core.MyPreparedStatementCreator;
import bsz.exch.utils.LogHelper;

public class HbpService {

	
private static Logger logger=Logger.getLogger(HbpService.class);
	
	private JdbcTemplate template;
	private String datasource;
			
	public HbpService(JdbcTemplate template,String datasource){
		this.template=template;
		this.datasource=datasource;
	}
	
	public  Long createHbp(String pay_id,String login_name,
			String amount,String customer_ip,
			String bank_code,String remark,String return_url){
		  String sql="insert into mb_hbp(pay_id,login_name,amount,customer_ip,bank_code,remark,"
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
	public boolean isNotDoHB(String order_id){
		 String sql="select count(1) from mb_hbp where state= 0 and pay_id =? ";
		 Object[] objs=new Object[]{order_id};
		 logger.info(LogHelper.dbLogObj(datasource,sql,Arrays.asList(objs)));
		 int count=template.queryForObject(sql, objs, Integer.class);
		 return count>0;
	}
	
	/**
	 * 更新订单状态
	 * @return
	 */
	public boolean updaeteHB(String order_id,String state,BigDecimal real_amount){
		 String sql="update mb_hbp set real_amount=?, state=?,  "
		 		+ "modify_time=now(), finished_date =now() where pay_id=? ";
		 Object[] objs=new Object[]{real_amount.multiply(new BigDecimal(0.99)),state,order_id};
		 logger.info(LogHelper.dbLogObj(datasource,sql,Arrays.asList(objs)));
		 return  template.update(sql,objs)>0;
	}
	
	public String queryLoginName(String order_id){
		 String sql="select login_name from mb_hbp  where pay_id=? ";
		 Object[] objs=new Object[]{order_id};
		 logger.info(LogHelper.dbLogObj(datasource,sql,Arrays.asList(objs)));
		 return  template.queryForObject(sql, objs, String.class);
	}
	
	public String queryPayType(String pay_id){
		 String sql="select bank_code from mb_hbp  where pay_id=? ";
		 Object[] objs=new Object[]{pay_id};
		 logger.info(LogHelper.dbLogObj(datasource,sql,Arrays.asList(objs)));
		 return  template.queryForObject(sql, objs, String.class);
	}
	
	
}
