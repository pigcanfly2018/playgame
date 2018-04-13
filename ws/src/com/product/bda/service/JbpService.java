package com.product.bda.service;

import java.math.BigDecimal;
import java.util.Arrays;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import bsz.exch.core.MyPreparedStatementCreator;
import bsz.exch.utils.LogHelper;


	

public class JbpService {
	 private static Logger logger=Logger.getLogger(JbpService.class);
		
	    private JdbcTemplate template;
		private String datasource;
			
		public JbpService(JdbcTemplate template,String datasource){
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
		public  Long createJbp(String pay_id,String login_name,
				String amount,String goods_name,String partner_id,
				String pay_method,String remark,String return_url){
			  String sql="insert into mb_jbp(pay_id,login_name,amount,goods_name,partner_id,pay_method,remark,"
			  		+ "create_date,state,return_url) values(?,?,?,?,?,?,?,now(),0,?)";
			  Object[] objs=new Object[]{pay_id,login_name,amount,goods_name,partner_id,pay_method,remark,return_url};
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
		public boolean isNotDoJdp(String pay_id){
			 String sql="select count(1) from mb_jbp where state= 0 and pay_id =?";
			 Object[] objs=new Object[]{pay_id};
			 logger.info(LogHelper.dbLogObj(datasource,sql,Arrays.asList(objs)));
			 int count=template.queryForObject(sql, objs, Integer.class);
			 return count>0;
		}
		
		/**
		 * 更新订单状态
		 * @return
		 */
		public boolean updaeteJdp(String pay_id,String orderNo,String state,
				String modifyTime,String payMethodType,BigDecimal amount){
			 String sql="update mb_jbp set order_no=?,real_amount=?, state=?,  "
			 		+ "modify_time=?, real_pay_method=?,  finished_date =now() where pay_id=? ";
			 Object[] objs=new Object[]{orderNo,amount,state,modifyTime,payMethodType,pay_id};
			 logger.info(LogHelper.dbLogObj(datasource,sql,Arrays.asList(objs)));
			 return  template.update(sql,objs)>0;
		}
		
		public String queryLoginName(String pay_id){
			 String sql="select login_name from mb_jbp  where pay_id=? ";
			 Object[] objs=new Object[]{pay_id};
			 logger.info(LogHelper.dbLogObj(datasource,sql,Arrays.asList(objs)));
			 return  template.queryForObject(sql, objs, String.class);
		}
		
		
		

}
