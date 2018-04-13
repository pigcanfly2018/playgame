package com.yeargift;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class YearGiftCustBeanRowMapper implements RowMapper{
	
	/**
	 * SELECT a.cust_id cust_id,login_name,cust_level,sum_credit,cnt,current_credit,current_cnt FROM mb_customer a 
		LEFT JOIN (SELECT cust_id ,SUM(credit) sum_credit,COUNT(1) cnt  FROM mb_year_gift GROUP BY cust_id) b ON a.cust_id =b.cust_id 
		LEFT JOIN (SELECT cust_id ,SUM(credit) current_credit,COUNT(1) current_cnt  FROM mb_year_gift WHERE mb_year_gift.day= ? GROUP BY cust_id) c ON a.cust_id =c.cust_id 
		WHERE a.cust_level>0 
	 */
	public YearGiftCustBean mapRow(ResultSet rs, int index) throws SQLException {
		YearGiftCustBean bean =new YearGiftCustBean();
		bean.cust_id=rs.getLong("cust_id");
		bean.cust_level=rs.getInt("cust_level");
		bean.login_name=rs.getString("login_name");
		bean.sum_credit=rs.getBigDecimal("sum_credit");
		bean.cnt=rs.getInt("cnt");
		bean.current_cnt=rs.getInt("current_cnt");
		bean.current_credit=rs.getBigDecimal("current_credit");
		return bean;
	}
}
