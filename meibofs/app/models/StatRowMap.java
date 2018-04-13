package models;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import util.DateUtil;

public class StatRowMap implements RowMapper{
	public Stat mapRow(ResultSet rs, int index) throws SQLException {
		Stat bean =new Stat();
		bean.stat_id=rs.getLong("stat_id");
		bean.stat_date=rs.getDate("stat_date");
		bean.cust_count1=rs.getInt("cust_count1");
		bean.cust_count2=rs.getInt("cust_count2");
		bean.deposit_count=rs.getBigDecimal("deposit_count");
		bean.poundage_count=rs.getBigDecimal("poundage_count");
		bean.withdraw_count=rs.getBigDecimal("withdraw_count");
		bean.gift_count=rs.getBigDecimal("gift_count");
		bean.stat_mon=DateUtil.dateToString(bean.stat_date, "yyyyMM");
		return bean;
	}
}
