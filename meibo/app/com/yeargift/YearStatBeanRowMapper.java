package com.yeargift;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class YearStatBeanRowMapper implements RowMapper{

	
	public YearStatBean mapRow(ResultSet rs, int arg1) throws SQLException {
		YearStatBean bean  =new YearStatBean();
		bean.count=rs.getInt("cnt");
		bean.credit=rs.getBigDecimal("credit")==null?new BigDecimal(0):rs.getBigDecimal("credit");	
		return bean;
	}

}
