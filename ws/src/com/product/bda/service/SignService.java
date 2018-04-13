package com.product.bda.service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

import bsz.exch.utils.LogHelper;

/**
 * 签到
 * @author 8da
 *
 */
public class SignService {
	
	  private static Logger logger=Logger.getLogger(SignService.class);
		
	    private JdbcTemplate template;
		private String datasource;
			
		public SignService(JdbcTemplate template,String datasource){
				this.template=template;
				this.datasource=datasource;
		}
		
		
		
		
		public boolean signToday(String login_name,Long cust_Id){
			 String sql="select count(1) from mb_sign where login_name =? and  sign_date >= CURDATE()";
        	 Object [] objs =new Object[]{login_name};
        	 logger.info(LogHelper.dbLogObj(datasource,sql, Arrays.asList(objs)));
             boolean b= template.queryForObject(sql,objs,Integer.class)>0;
             int i=0;
             if(!b){
            	sql="INSERT INTO mb_sign (login_name,cust_id,create_date,sign_date) VALUES(?,?,NOW(),NOW())";
            	objs =new Object[]{login_name,cust_Id};
            	logger.info(LogHelper.dbLogObj(datasource,sql, Arrays.asList(objs)));
            	i=template.update(sql, objs);
             }
             return i>0;
		}
		
		/**
		 * 查询是否签到了7天
		 * @param login_name
		 * @return
		 */
		public boolean sign7day(String login_name){
			 String sql="SELECT DATE(sign_date) AS sign_date FROM mb_sign WHERE sign_date>DATE_SUB(CURDATE(),INTERVAL 7 DAY) and login_name=?  GROUP BY DATE(sign_date)";
			 Object [] objs =new Object[]{login_name};
        	 logger.info(LogHelper.dbLogObj(datasource,sql, Arrays.asList(objs)));
        	 List<Map<String,Object>> list= template.queryForList(sql,objs);
        	 if(list.size()>=7)return true;return false;
		}
		
		/**
		 * 获取最近一天
		 * @param login_name
		 * @return
		 */
		public String signLastReward(String login_name){
			 String sql="SELECT date(MAX(reward_date)) as reward_date  FROM  mb_sign_reward WHERE login_name= ? ";
			 Object [] objs =new Object[]{login_name};
			 logger.info(LogHelper.dbLogObj(datasource,sql, Arrays.asList(objs)));
			 List<Map<String,Object>> list=template.queryForList(sql,objs);
			 if(list.size()>0){
				 if(list.get(0).get("reward_date")==null)return null;
				 return list.get(0).get("reward_date").toString();
			 }
			 return null;
		}
		
		public boolean reward(String login_name,Long cust_id,Integer cust_level, BigDecimal amount){
			 String sql="insert into mb_sign_reward (login_name,cust_id,reward_date,amount,cust_level,create_date) values(?,?,now(),?,?,now()) ";
			 Object [] objs =new Object[]{login_name,cust_id,amount,cust_level};
			 logger.info(LogHelper.dbLogObj(datasource,sql, Arrays.asList(objs)));
			 return template.update(sql, objs)>0;
		}
		
		
		

}
