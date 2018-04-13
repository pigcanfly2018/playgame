package com.product.bda.service;

import java.util.Arrays;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import bsz.exch.core.MyPreparedStatementCreator;
import bsz.exch.utils.LogHelper;

public class OrderNumberService {

	private static Logger logger=Logger.getLogger(OrderNumberService.class);
	
	private JdbcTemplate template;
	private String datasource;
	
	public OrderNumberService(JdbcTemplate template,String datasource){
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
	public  Long createOrderNumber(String ordernumber){
		  String sql="insert into mb_ordernumber(ordernumber) values(?)";
		  Object[] objs=new Object[]{ordernumber};
		  KeyHolder keyHolder = new GeneratedKeyHolder();
		  logger.info(LogHelper.dbLogObj(datasource,sql,Arrays.asList(objs)));
		  template.update(new MyPreparedStatementCreator(sql,objs), keyHolder);
		  return null;	
	}
}
