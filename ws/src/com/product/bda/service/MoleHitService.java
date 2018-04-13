package com.product.bda.service;

import java.util.Arrays;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import bsz.exch.core.MyPreparedStatementCreator;
import bsz.exch.utils.LogHelper;

public class MoleHitService {
	
    private static Logger logger=Logger.getLogger(MoleHitService.class);
		
    private JdbcTemplate template;
	private String datasource;
		
	public MoleHitService(JdbcTemplate template,String datasource){
			this.template=template;
			this.datasource=datasource;
	}
	
	public Boolean updateFlag(String login_name, String level) {
		// TODO Auto-generated method stub
		String sql="update mb_wuyi_active set flag = 1 where login_name=? and level=?";
		 Object[] objs=new Object[]{login_name,level};
		 logger.info(LogHelper.dbLogObj(datasource,sql,Arrays.asList(objs)));
		 int f=template.update(sql,objs);
		 return f>0;
	}

	public int doHitActive(String login_name, Long cust_id, int hittime) {
		// TODO Auto-generated method stub
		//不管是否砸中，创建一条记录
		return insertHitrecord(login_name,hittime,cust_id);
		
	}
	
	public int insertHitrecord(String login_name
			, int hittime,Long cust_id){
		String sql ="insert into mb_molehit_active(login_name,cust_id,create_date,hitcount) "
				+ "values(?,?,now(),?)";
		
		Object[] objs=new Object[]{login_name,cust_id,hittime} ;
		  KeyHolder keyHolder = new GeneratedKeyHolder();
		  logger.info(LogHelper.dbLogObj(datasource,sql, Arrays.asList(objs)));
		  int cnt = template.update(new MyPreparedStatementCreator(sql,objs), keyHolder);
		  if(cnt>0){
			  return keyHolder.getKey().intValue();
		  }
		  return -1;
	}

}
