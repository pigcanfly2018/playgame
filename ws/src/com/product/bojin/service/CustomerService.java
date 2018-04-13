package com.product.bojin.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

import bsz.exch.utils.LogHelper;

public class CustomerService {
	
    private static Logger logger=Logger.getLogger(CustomerService.class);
		
    private JdbcTemplate template;
	private String datasource;
		
	public CustomerService(JdbcTemplate template,String datasource){
			this.template=template;
			this.datasource=datasource;
	}
	
	/**
	 * 用户登陆
	 * @param login_name 用户名
	 * @param login_pwd  密码
	 * @return
	 */
	public boolean login(String login_name,String login_pwd){
		String sql="select count(1) from mb_customer where login_name =? and login_pwd=? and flag=1";
		List<String> list =new ArrayList<String>();
		list.add(login_name);
		list.add(login_pwd);
		logger.info(LogHelper.dbLog(datasource,sql, list));
		Integer count=template.queryForObject(sql,list.toArray(new String[0]),Integer.class);
		return count>0;
	}
	
	/**
	 * 记录登陆信息
	 * @param login_name
	 * @param login_ip
	 */
	public void recordLoginInfo(String login_name,String login_ip,String user_agent){
		String sql="update mb_customer set login_ip=?,login_date=now(),login_times=login_times+1 where login_name=? ";
		List<String> list =new ArrayList<String>();
		list =new ArrayList<String>();
		list.add(login_ip);
		list.add(login_name);
		logger.info(LogHelper.dbLog(datasource,sql, list));
		int flag=template.update(sql,list.toArray(new String[0]));
		
	    sql="insert into mb_cust_log (log_type,log_msg,ip,user_agent,create_date,cust_id,login_name) select 2,'客户登录',?,?,now(),cust_id,login_name from mb_customer where login_name =?";
		list =new ArrayList<String>();
		list.add(login_ip);
		list.add(user_agent);
		list.add(login_name);
		logger.info(LogHelper.dbLog(datasource,sql, list));
		template.update(sql,list.toArray(new String[0]));
	}
	

}
