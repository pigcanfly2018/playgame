package com.product.bda.service;

import java.math.BigDecimal;
import java.util.Arrays;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import bsz.exch.core.MyPreparedStatementCreator;
import bsz.exch.utils.LogHelper;


/**
 * DPAY的服务方法
 * @author robin
 *
 */
public class DPayService {
	
    private static Logger logger=Logger.getLogger(DPayService.class);
	private JdbcTemplate template;
	private String datasource;
	public DPayService(JdbcTemplate template,String datasource){
		this.template=template;
		this.datasource=datasource;
	}
	
	/**
	 * 创建存款订单
	 * @return
	 */
	public Long createDepositOrder(String company_id,String bank_id,String bank_code,
			String order_no,String amount,String login_name,
			String pay_bank_id,String pay_bank_code,String mode,
			String group_id,String web_url,String memo,String note,
			String note_model,String terminal){
		  String sql="insert into mb_dpay_deposit(company_id,bank_id,bank_code,order_no,amount,login_name,"
				+ "pay_bank_id,pay_bank_code,mode,group_id,web_url,memo,note,note_model,terminal,create_date,status) values("
				+ "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,now(),0)";
		  Object[] objs=new Object[]{company_id,bank_id,bank_code,order_no,amount,login_name,
				  pay_bank_id,pay_bank_code,mode,group_id,web_url,memo,note,note_model,terminal};
		  KeyHolder keyHolder = new GeneratedKeyHolder();
		  logger.info(LogHelper.dbLogObj(datasource,sql,Arrays.asList(objs)));
		  template.update(new MyPreparedStatementCreator(sql,objs), keyHolder);
		  return keyHolder.getKey().longValue();	
	}
	
    /**
     * 更新响应信息
     * @return
     */
	public boolean addDepositResult(Long dp_pay_id,String bank_card_num,String bank_acc_name,String amount,
			String email,String company_order_num,String datetime,String note,String mownecum_order_num,
			String status,String mode,String issuing_bank_address,String rec_break_url,String collection_bank_id,
			String error_msg){
		  String sql="update mb_dpay_deposit set rec_bank_account =? , rec_bank_account_name= ?,rec_amount=?,"
		  		+ "rec_email=?,rec_order_no=?,rec_date_time=?,rec_note=?,rec_pay_no=?,rec_status=?,rec_error_msg=?,"
		  		+ "rec_mode=?,rec_bank_dot=?,rec_break_url=?,rec_bank_code=?,status=1 where dp_pay_id=?";
		  Object[] objs=new Object[]{bank_card_num,bank_acc_name,amount,email,company_order_num,datetime,note,mownecum_order_num,
				  status,error_msg,mode,issuing_bank_address,rec_break_url,collection_bank_id,dp_pay_id};
		  logger.info(LogHelper.dbLogObj(datasource,sql,Arrays.asList(objs)));
		  int c= template.update(sql, objs);
		  return c>0;
	}
	
	public boolean addErrorDepositResult(Long dp_pay_id,String status,String error_msg){
		  String sql="update mb_dpay_deposit set rec_status=?,rec_error_msg=?,status=-1,finished_date=now()  where dp_pay_id=?";
		  Object[] objs=new Object[]{status,error_msg,dp_pay_id};
		  logger.info(LogHelper.dbLogObj(datasource,sql,Arrays.asList(objs)));
		  int c= template.update(sql, objs);
		  return c>0;
	}
	
	/**
	 * 寻找账号
	 * @param order_no
	 * @return
	 */
	public String findDepositLoginName(String order_no){
		 String sql="select login_name from  mb_dpay_deposit where order_no =? limit 1";
		 Object[] objs=new Object[]{order_no};
		 logger.info(LogHelper.dbLogObj(datasource,sql,Arrays.asList(objs)));
		 String login_name= template.queryForObject(sql, objs, String.class);
		 return login_name;
	}
	
	/**
	 * 寻找未支付的平台订单号
	 * @param order_no
	 * @return
	 */
	public String findDepositPlatNo(String order_no){
		 String sql="select rec_pay_no from  mb_dpay_deposit where order_no =? limit 1";
		 Object[] objs=new Object[]{order_no};
		 logger.info(LogHelper.dbLogObj(datasource,sql,Arrays.asList(objs)));
		 String rec_order_no= template.queryForObject(sql, objs, String.class);
		 return rec_order_no;
	}
	/**
	 * 订单已取消
	 * @param order_no
	 * @return
	 */
	public boolean cancleDeposit(String order_no){
		 String sql="update  mb_dpay_deposit set status =-1,finished_date=now() where  order_no=? and status = 1";
		 Object[] objs=new Object[]{order_no};
		 logger.info(LogHelper.dbLogObj(datasource,sql,Arrays.asList(objs)));
		 int c= template.update(sql, objs);
		 return c>0;
	}
	
	/**
	 * 充值成功提醒
	 * @param order_no
	 * @return
	 */
	public boolean notifyDeposit(String nfy_pay_time,String nfy_bank_id,String nfy_amount,String nfy_order_no,
			String nfy_pay_no,String nfy_pay_account,String nfy_pay_name,String nfy_channel,String nfy_area,String nfy_fee,
			String nfy_charge,String nfy_deposit_mode,String nfy_base_info){
		  String sql="update mb_dpay_deposit set nfy_pay_time=?,nfy_bank_id=?,nfy_amount=?,nfy_order_no=?"
		  		+ ",nfy_pay_no=?,nfy_pay_account=?,nfy_pay_name=?,nfy_channel=?,nfy_area=?,nfy_fee=?,"
		  		+ "nfy_charge=?,nfy_deposit_mode=?,nfy_base_info=?,nfy_date=now(),finished_date=now(),status=2 "
		  		+ "where order_no=? and status = 1";
		 Object[] objs=new Object[]{nfy_pay_time,nfy_bank_id,nfy_amount,nfy_order_no,
					nfy_pay_no,nfy_pay_account,nfy_pay_name,nfy_channel,nfy_area,nfy_fee,
					nfy_charge,nfy_deposit_mode,nfy_base_info,nfy_order_no};
		 logger.info(LogHelper.dbLogObj(datasource,sql,Arrays.asList(objs)));
		 int c= template.update(sql, objs);
		 return c>0;
	}
	
	/**
	 * 充值异常提醒
	 * @param order_no
	 * @return
	 */
	public long notifyFalseDeposit(String exception_order_num,String company_id,String exact_payment_bank,
			String pay_card_name,String pay_card_num,String receiving_bank,String receiving_account_name,String channel,
			String note,String area,String exact_time,String amount,String fee,String transaction_charge,String base_info){
		 String sql="insert into mb_dpay_deposit_exp(exception_order_num,company_id,exact_payment_bank,pay_card_name,"
				+ "pay_card_num,receiving_bank,receiving_account_name,channel,note,area,exact_time,amount,fee,transaction_charge,"
				+ "base_info,create_date) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,now())";
		 Object[] objs=new Object[]{exception_order_num,company_id,exact_payment_bank,
				 pay_card_name,pay_card_num,receiving_bank,receiving_account_name,channel,
				 note,area,exact_time,amount,fee,transaction_charge,base_info};
		  KeyHolder keyHolder = new GeneratedKeyHolder();
		  logger.info(LogHelper.dbLogObj(datasource,sql,Arrays.asList(objs)));
		  template.update(new MyPreparedStatementCreator(sql,objs), keyHolder);
		  return keyHolder.getKey().longValue();	
	}
	/**
	 * 查询异常提醒是否入库
	 * @param exception_order_num
	
	 * @return
	 */
	public boolean isHasFalseDeposit(String exception_order_num){
		 String sql="select count(1) from  mb_dpay_deposit_exp where exception_order_num=?";
		 Object[] objs=new Object[]{exception_order_num};

		 logger.info(LogHelper.dbLogObj(datasource,sql,Arrays.asList(objs)));
		 int cnt= template.queryForObject(sql, objs, Integer.class);
		return cnt>0;	
	}
	
	/**
	 * 
     *  创建提款订单
	 * @return
	 */
	public Long createWithdrawOrder(String company_id,String bank_id,String bank_code,String order_no,BigDecimal amount,
			String card_num,String card_name,String login_name,
			String issue_bank_name,String issue_bank_address,String memo){
		  String sql="insert into mb_dpay_withdraw(company_id,bank_id,bank_code,order_no,amount,card_num,card_name,login_name,"
				+ "issue_bank_name,issue_bank_address,memo,status,error_msg,create_date) values("
				+ "?,?,?,?,?,?,?,?,?,?,?,0,'',now())";
		  Object[] objs=new Object[]{company_id,bank_id,bank_code,order_no,amount,card_num,card_name,login_name,
				  issue_bank_name,issue_bank_address,memo};
		  KeyHolder keyHolder = new GeneratedKeyHolder();
		  logger.info(LogHelper.dbLogObj(datasource,sql,Arrays.asList(objs)));
		  template.update(new MyPreparedStatementCreator(sql,objs), keyHolder);
		  return keyHolder.getKey().longValue();	  
	} 
	/**
	 * 创建订单失败
	 * @return
	 */
	public boolean createFalseWithdraw(String error_msg,Long dp_pay_id){
		  String sql="update mb_dpay_withdraw set status = -1,error_msg =? where dp_pay_id =? and status=0 ";
		  Object[] objs=new Object[]{error_msg,dp_pay_id};
		  logger.info(LogHelper.dbLogObj(datasource,sql,Arrays.asList(objs)));
			 int c= template.update(sql, objs);
			 return c>0;	  
	}
	/**
	 * 创建订单成功
	 * @param error_msg
	 * @param dp_pay_id
	 * mownecum_order_num
		company_order_num
		status
		error_msg
		transaction_charge

	 * @return
	 */
	public boolean createTrueWithdraw(String mownecum_order_num,String transaction_charge,Long dp_pay_id){
		  String sql="update mb_dpay_withdraw set status = 1, rec_pay_no=?,transaction_charge=?  where dp_pay_id =? and status=0 ";
		  Object[] objs=new Object[]{mownecum_order_num,transaction_charge,dp_pay_id};
		  logger.info(LogHelper.dbLogObj(datasource,sql,Arrays.asList(objs)));
			 int c= template.update(sql, objs);
			 return c>0;	  
	} 

    /**
     * 确认提款信息
     * @param company_order_num
     * @param amount
     * @param card_num
     * @param card_name
     * @param company_user
     * @return
     */
	public boolean confirmWithdraw(String company_order_num,String amount,String card_num,String card_name,String company_user){
		  String sql="update mb_dpay_withdraw set status = 2,first_date=now()  where order_no =? and status=1 and amount=? and login_name=? and card_num=? and card_name=?";
		  Object[] objs=new Object[]{company_order_num,amount,company_user,card_num,card_name};
		  logger.info(LogHelper.dbLogObj(datasource,sql,Arrays.asList(objs)));
		  int c= template.update(sql, objs);
		  return c>0;	  
	} 
	
	
	public boolean payResultWithdraw(String company_order_num,String exact_amount,String rec_detail,
			String rec_second_status,String rec_second_error_msg,String exact_transaction_charge){
		  String status ="-1";
		  if("1".equals(rec_second_status)){
			  status="3";
		  }
		  String sql="update mb_dpay_withdraw set status = ?, second_date=now(),exact_amount=?,rec_detail=?,"
		  		+ "rec_second_status=?,rec_second_error_msg=?,exact_transaction_charge =?  where order_no =? and status=2";
		  Object[] objs=new Object[]{status,exact_amount,rec_detail,rec_second_status,rec_second_error_msg,exact_transaction_charge,company_order_num};
		  logger.info(LogHelper.dbLogObj(datasource,sql,Arrays.asList(objs)));
		  int c= template.update(sql, objs);
		  return c>0;	  
	} 
	
	/**
	 * 查询平台提款订单号
	 * @param order_no
	 * @return
	 */
	public String findWithdrawPlatNo(String order_no){
		 String sql="select rec_pay_no from  mb_dpay_withdraw where order_no =? limit 1";
		 Object[] objs=new Object[]{order_no};
		 logger.info(LogHelper.dbLogObj(datasource,sql,Arrays.asList(objs)));
		 String rec_order_no= template.queryForObject(sql, objs, String.class);
		 return rec_order_no;
	}

}
