package com.product.bda.service;


import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import bsz.exch.core.MyPreparedStatementCreator;




public class LetterService {
	 private static Logger logger=Logger.getLogger(LetterService.class);
		
	    private JdbcTemplate template;
		private String datasource;
			
		public LetterService(JdbcTemplate template,String datasource){
				this.template=template;
				this.datasource=datasource;
		}
		
	
	
	public  long create(Long cust_id,String login_name,String title,String content,String create_user,Boolean is_public){
		  String sql="insert into mb_letter (cust_id,title,content,"
		  		+ "create_user,create_date,read_flag,is_public,login_name) "
		  		+ "values(?,?,?,?,now(),0,?,?)";
		  Object[] objs=new Object[]{cust_id,title,content,create_user,is_public,login_name};
		  KeyHolder keyHolder = new GeneratedKeyHolder();
		  template.update(new MyPreparedStatementCreator(sql,objs), keyHolder);
		  return keyHolder.getKey().longValue();
     }
	

}
