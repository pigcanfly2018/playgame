package com.product.bda.service;

import java.util.Arrays;





import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import bsz.exch.utils.LogHelper;

public class CashGiftService {

	private static Logger logger=Logger.getLogger(CashGiftService.class);
	
	private JdbcTemplate template;
	private String datasource;
	
	public CashGiftService(JdbcTemplate template, String datasource) {
		this.template = template;
		this.datasource = datasource;
	}
	
	public  Long NTcreat(CashGift gift){
		  final String sql="insert into  mb_cash_gift (gift_no,kh_date,login_name,real_name,cust_level,gift_type,gift_code,deposit_credit,net_credit,valid_credit,rate,payout,cs_date,remarks,create_date,create_user,status,audit_date,audit_user,audit_msg,cust_id) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		  final Object[] objects=new Object[]{gift.gift_no,gift.kh_date,gift.login_name,gift.real_name,gift.cust_level,gift.gift_type,gift.gift_code,gift.deposit_credit,gift.net_credit,gift.valid_credit,gift.rate,gift.payout,gift.cs_date,gift.remarks,gift.create_date,gift.create_user,gift.status,gift.audit_date,gift.audit_user,gift.audit_msg,gift.cust_id};
		  KeyHolder keyHolder = new GeneratedKeyHolder();
		  logger.info(LogHelper.dbLogObj(datasource,sql,Arrays.asList(objects)));
		  template.update(new bsz.exch.core.MyPreparedStatementCreator(sql,objects), keyHolder);
		  return keyHolder.getKey().longValue();
	}
	
	public  boolean NTAuditGift(Long id,Integer flag,String user,String remarks){
	 	 String sql="update mb_cash_gift set audit_date=now(),status=?,audit_user=?,audit_msg=?  where gift_id=?";
	 	 int count=template.update(sql, new Object[]{flag,user,remarks,id});
	 	 return count>0;
	  }
	
	
	
}
