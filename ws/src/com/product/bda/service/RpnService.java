package com.product.bda.service;

import java.math.BigDecimal;
import java.util.Arrays;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import bsz.exch.core.MyPreparedStatementCreator;
import bsz.exch.utils.LogHelper;

public class RpnService {

	private static Logger logger=Logger.getLogger(RpnService.class);
	private JdbcTemplate template;
	private String datasource;
	
	public RpnService(JdbcTemplate template,String datasource){
		this.template=template;
		this.datasource=datasource;
	}
	
	public  Long createRpn(String order_id,String login_name,
			String amount,String ip,
			String bank_id,String remark,String return_url){
		  String sql="insert into mb_rpn(order_id,login_name,amount,customer_ip,bank_id,remark,"
		  		+ "create_date,state,return_url) values(?,?,?,?,?,?,now(),0,?)";
		  Object[] objs=new Object[]{order_id,login_name,amount,ip,bank_id,remark,return_url};
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
	public boolean isNotDoRpn(String order_id){
		 String sql="select count(1) from mb_rpn where state= 0 and order_id =? ";
		 Object[] objs=new Object[]{order_id};
		 logger.info(LogHelper.dbLogObj(datasource,sql,Arrays.asList(objs)));
		 int count=template.queryForObject(sql, objs, Integer.class);
		 return count>0;
	}
	
	
	/**
	 * 更新订单状态
	 * @return
	 */
	public boolean updaeteRpn(String order_id,String rpn_order_no,String state,BigDecimal real_amount){
		 String sql="update mb_rpn set rpn_order_no=?,real_amount=?, state=?,  "
		 		+ "modify_time=now(), finished_date =now() where order_id=? ";
		 Object[] objs=new Object[]{rpn_order_no,real_amount.multiply(new BigDecimal(0.985)),state,order_id};
		 logger.info(LogHelper.dbLogObj(datasource,sql,Arrays.asList(objs)));
		 return  template.update(sql,objs)>0;
	}
	
	public String queryLoginName(String order_id){
		 String sql="select login_name from mb_rpn  where order_id=? ";
		 Object[] objs=new Object[]{order_id};
		 logger.info(LogHelper.dbLogObj(datasource,sql,Arrays.asList(objs)));
		 return  template.queryForObject(sql, objs, String.class);
	}
	
	
	
}
