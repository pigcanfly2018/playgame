package com.product.bda.service;

import java.math.BigDecimal;
import java.util.Arrays;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

import bsz.exch.utils.LogHelper;

public class HongBaoService {

	private static Logger logger=Logger.getLogger(HongBaoService.class);
	
    private JdbcTemplate template;
	private String datasource;
		
	public HongBaoService(JdbcTemplate template,String datasource){
			this.template=template;
			this.datasource=datasource;
	}
	
	public boolean createHongbao(Long cust_id,String login_name,BigDecimal credit,String order_no,String gift_no){
		
		 String sql="insert into  mb_hongbao (cust_id,login_name,create_date,credit,status,content,gift_no) values(?,?,now(),?,0,?,?)";
		 Object [] objs =new Object[]{cust_id,login_name,credit,order_no,gift_no};
		 logger.info(LogHelper.dbLogObj(datasource,sql, Arrays.asList(objs)));
	     int i=template.update(sql, objs);
	        
	     return i>0;
		 
	}
	
	
}
