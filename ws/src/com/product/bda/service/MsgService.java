package com.product.bda.service;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

public class MsgService {
	
	   private static Logger logger=Logger.getLogger(MsgService.class);
		
		private JdbcTemplate template;
		private String datasource;
		public MsgService(JdbcTemplate template,String datasource){
			this.template=template;
			this.datasource=datasource;
		}
		
		 public void createMsg(Integer type,String content){
		    	String sql="insert into mb_msg(msg_type,content,create_date,flag) values(?,?,now(),0)";
		    	template.update(sql,new Object[]{type,content});
		    }
	

}
