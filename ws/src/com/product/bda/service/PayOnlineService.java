package com.product.bda.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import bsz.exch.utils.LogHelper;

public class PayOnlineService {

	private static Logger logger=Logger.getLogger(PayOnlineService.class);
	
	private JdbcTemplate template;
	private String datasource;
	
	
	public PayOnlineService(JdbcTemplate template, String datasource) {
		this.template = template;
		this.datasource = datasource;
	}
	
	public PayOnline getPayOnline(String name,String product){
		String sql="select * from mb_payonline where name =? and value='开' and product = ?  ";
		List<Object> list =new ArrayList<Object>();
		list.add(name);
		list.add(product);
		logger.info(LogHelper.dbLogObj(datasource,sql, list));
		List<PayOnline> paylist = template.query(sql,list.toArray(new Object[0]),new  RowMapper<PayOnline>(){
			public PayOnline mapRow(ResultSet rs, int arg1)
					throws SQLException{
				PayOnline bean = new PayOnline();
				bean.payonline_id = rs.getLong("payonline_id");
				bean.name = rs.getString("name");
				bean.value = rs.getString("value");
				bean.submit_value = rs.getString("submit_value");
				bean.return_value = rs.getString("return_value");
				bean.notify_value = rs.getString("notify_value");
				bean.valuedescribe = rs.getString("valuedescribe");
				bean.req_referer = rs.getString("req_referer");
				return bean;
			}
		});
		
		return paylist.get(0);
		
	}
	
	
	
}
