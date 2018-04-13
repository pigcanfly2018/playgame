package com.product.bda.service;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import bsz.exch.core.MyPreparedStatementCreator;
import bsz.exch.utils.LogHelper;

public class WithdrawService {
	
	private static Logger logger=Logger.getLogger(WithdrawService.class);
	
	
	private JdbcTemplate template;
	
	private String datasource;
	
	public WithdrawService(JdbcTemplate template,String datasource){
		this.template=template;
		this.datasource=datasource;
	}
	
	/**
	 * 查询成功提款次数
	 * @return
	 */
	public int querySuccessWithdrawCount(String login_name){
		String sql="select count(1) from mb_withdraw where login_name =? and (status =5)";
		List<String> list =new ArrayList<String>();
		list.add(login_name);
		logger.info(LogHelper.dbLog(datasource,sql, list));
		Integer count=template.queryForObject(sql,list.toArray(new String[0]),Integer.class);
		return count;
	}
	
	/**
	 * 查询未处理提款数量
	 */
	public int queryUndoWithdrawCount(String login_name){
		String sql="select count(1) from mb_withdraw where login_name =? and (status =1 or status =3)";
		List<String> list =new ArrayList<String>();
		list.add(login_name);
		logger.info(LogHelper.dbLog(datasource,sql, list));
		Integer count=template.queryForObject(sql,list.toArray(new String[0]),Integer.class);
		return count;
	}
	
	/**
	 * 创建日志
	 * @param pre_status
	 * @param after_status
	 * @param withdraw_id
	 * @param remarks
	 * @param user
	 * @return
	 */
    public  boolean createWithdrawLog(Integer pre_status,Integer after_status,Long withdraw_id,String remarks,String user){
  	  final String sql="insert into  mb_withdraw_log (pre_status,after_status,withdraw_id,remarks,create_date,create_user) values(?,?,?,?,now(),?)";
		  final Object[] objects=new Object[]{pre_status,after_status,withdraw_id,remarks,user};
		  KeyHolder keyHolder = new GeneratedKeyHolder();
		  int result=template.update(new MyPreparedStatementCreator(sql,objects), keyHolder);
		  return result>0;
    }
    
	
	/**
	 * 创建提款提案(自动写创建日志)
	 * @param login_name
	 * @return
	 */
	public Long createWithdraw(String order_no,String login_name,BigDecimal amount,String bank_name,
			String account,String account_type,String bank_dot,String remarks,String create_user){
		int count=querySuccessWithdrawCount(login_name);
		String sql="";
		List<Object> list0 =new ArrayList<Object>();
		if(count>0){
			 sql="insert into  mb_withdraw (wit_no,login_name,real_name,amount,bank_name,"
					+ "account_name,account,account_type,location,remarks,create_date,"
					+ "create_user,status,cust_id,wit_cnt) "
					+ "select ?,login_name,real_name,?,bank_name,"
					+ "real_name,account,account_type,bank_dot,?,now(),"
					+ "?,1,cust_id,? from mb_customer where login_name =?";
			
			list0.add(order_no);
			list0.add(amount);
			list0.add(remarks);
			list0.add(create_user);
			list0.add(count);
			list0.add(login_name);
		}else{
			   sql="insert into  mb_withdraw (wit_no,login_name,real_name,amount,bank_name,"
						+ "account_name,account,account_type,location,remarks,create_date,"
						+ "create_user,status,cust_id,wit_cnt) "
						+ "select ?,login_name,real_name,?,?,"
						+ "real_name,?,?,?,?,now(),"
						+ "login_name,1,cust_id,? from mb_customer where login_name =?";
				list0.add(order_no);
				list0.add(amount);
				list0.add(bank_name);
				list0.add(account);
				list0.add(account_type);
				list0.add(bank_dot);
				list0.add(remarks);
				//list0.add(create_user);
				list0.add(count);
				list0.add(login_name);
		}
		KeyHolder keyHolder = new GeneratedKeyHolder();
		logger.info(LogHelper.dbLogObj(datasource,sql, list0));
	    int cnt=template.update(new MyPreparedStatementCreator(sql,list0.toArray(new Object[0])), keyHolder);
	    if(cnt>0){
	    	long withdraw_id=keyHolder.getKey().longValue();
	    	createWithdrawLog(0,1,withdraw_id,remarks,create_user);
	    	return withdraw_id;
	   }
	    return null;
	}
	
    
	
	public void deleteWithdraw(Long withdraw_id){
		String sql="delete from mb_withdraw where withdraw_id =?";
		Object[] objs =new Object[]{withdraw_id};
		logger.info(LogHelper.dbLogObj(datasource,sql, Arrays.asList(objs)));
		template.update(sql,objs);
		sql="delete from mb_withdraw_log where withdraw_id =?";
		template.update(sql,objs);
	}
	
	/**
	 * 获取可以支付的提款
	 * @param withdraw_id
	 * @param login_name
	 * @return
	 */
	public Withdraw getCanPayWithdraw(String wit_no,String login_name){
		String sql="select * from mb_withdraw where login_name =? and wit_no =? and status = 6 and locked=0";
		List<Object> list =new ArrayList<Object>();
		list.add(login_name);
		list.add(wit_no);
		logger.info(LogHelper.dbLogObj(datasource,sql, list));
		List<Withdraw> custList=template.query(sql,list.toArray(new Object[0]),new  RowMapper<Withdraw>(){
			public Withdraw mapRow(ResultSet rs, int arg1)
					throws SQLException {
				Withdraw wit =new Withdraw();
				wit.withdraw_id=rs.getLong("withdraw_id");
				wit.wit_no=rs.getString("wit_no");
				wit.login_name=rs.getString("login_name");
				wit.amount=rs.getBigDecimal("amount");
				wit.account_name=rs.getString("account_name");
				wit.account=rs.getString("account");
				wit.status=rs.getInt("status");
				wit.location=rs.getString("location");
				wit.bank_id=rs.getString("bank_id");
				wit.bank_name=rs.getString("bank_name");
				return wit;
			}
		});
		if(custList.size()>0)
		return custList.get(0);
		return null;
	}
	/**
	 * 更新支付状态 ->自动支付中
	 * @param withdraw_id
	 * @return
	 */
	public boolean updateWithdrawDpayStatus7(Long withdraw_id){
		String sql="update mb_withdraw set status = 7 where withdraw_id =? and status =6 ";
		Object[] objs =new Object[]{withdraw_id};
		logger.info(LogHelper.dbLogObj(datasource,sql, Arrays.asList(objs)));
		boolean f=(template.update(sql,objs)>0);
		if(f){
			createWithdrawLog(6,7,withdraw_id,"进入自动支付流程","系统操作");
		    return f;
		}
		return false;
		
	}
	/**
	 * 自动支付中-->自动支付失败
	 * @param withdraw_id
	 * @return
	 */
	public boolean updateWithdrawDpayStatus8(Long withdraw_id,String error_msg){
		String sql="update mb_withdraw set status = 8,error_msg=?  where withdraw_id =? and status =7";
		Object[] objs =new Object[]{withdraw_id,error_msg};
		logger.info(LogHelper.dbLogObj(datasource,sql, Arrays.asList(objs)));
		boolean f=template.update(sql,objs)>0;
		if(f){
			createWithdrawLog(7,8,withdraw_id,"进入自动支付流程","系统操作");
		    return f;
		}
		return false;
		
	}
	/**
	 * 更新订单号
	 * @param withdraw_id
	 * @return
	 */
	public boolean updateWithdrawDpayInfo(Long withdraw_id ,String dpay_no){
		String sql="update mb_withdraw set dpay_no = ? where withdraw_id =? and status =7";
		Object[] objs =new Object[]{dpay_no,withdraw_id};
		logger.info(LogHelper.dbLogObj(datasource,sql, Arrays.asList(objs)));
		boolean f=template.update(sql,objs)>0;
		return f;
	}
	
	/**
	 * 通过订单查询ID
	 * @param withdraw_id
	 * @param dpay_no
	 * @return
	 */
	public Long queryWithdrawIdByOrderNo(String dpay_no){
		String sql="select * from mb_withdraw where dpay_no =?";
		List<Object> list =new ArrayList<Object>();
		list.add(dpay_no);
		logger.info(LogHelper.dbLogObj(datasource,sql, list));
		List<Withdraw> custList=template.query(sql,list.toArray(new Object[0]),new  RowMapper<Withdraw>(){
			public Withdraw mapRow(ResultSet rs, int arg1)
					throws SQLException {
				Withdraw wit =new Withdraw();
				wit.withdraw_id=rs.getLong("withdraw_id");
				wit.wit_no=rs.getString("wit_no");
				wit.login_name=rs.getString("login_name");
				wit.amount=rs.getBigDecimal("amount");
				wit.account_name=rs.getString("account_name");
				wit.account=rs.getString("account");
				wit.status=rs.getInt("status");
				wit.location=rs.getString("location");
				wit.bank_id=rs.getString("bank_id");
				wit.bank_name=rs.getString("bank_name");
				return wit;
			}
		});
		if(custList.size()>0)return custList.get(0).withdraw_id;
		return null;
	}
	
	/**
	 * 更新支付结果
	 * @param withdraw_id
	 * @param dpay_no
	 * @return
	 */
	public boolean updateWithdrawDpayResult(int status,BigDecimal dpay_exact_mount,
			BigDecimal exact_charge,String error_msg, String dpay_no){
		Long withdraw_id=queryWithdrawIdByOrderNo(dpay_no);
		if(status==9){ 
			String sql="update mb_withdraw set status=?,dpay_exact_mount=?,error_msg =?,exact_charge=?,"
					+ "pay_date=now(),dpay_pay_date=now() where dpay_no =? and status = 7";
			Object[] objs =new Object[]{status,dpay_exact_mount,error_msg,exact_charge,dpay_no};
			logger.info(LogHelper.dbLogObj(datasource,sql, Arrays.asList(objs)));
			boolean f=(template.update(sql,objs)>0);
			if(f){
				createWithdrawLog(7,status,withdraw_id,"支付成功","系统操作");
			    return f;
			}
		}else{
			String sql="update mb_withdraw set status=?,dpay_exact_mount=?,error_msg =?,"
					+ "exact_charge=?,dpay_pay_date=now() where dpay_no =? and status = 7";
			Object[] objs =new Object[]{status,dpay_exact_mount,error_msg,exact_charge,dpay_no};
			logger.info(LogHelper.dbLogObj(datasource,sql, Arrays.asList(objs)));
			boolean f=(template.update(sql,objs)>0);
			if(f){
				String l ="";
				if("10".equals(status)){
					l="部分支付成功";
				}
				if("8".equals(status)){
					l="支付失败";
				}
				createWithdrawLog(7,status,withdraw_id,l,"系统操作");
			    return f;
			}	
		}
		return false;
	}
	
	

}
