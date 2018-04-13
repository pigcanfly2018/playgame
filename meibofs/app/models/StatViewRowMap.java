package models;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import util.DateUtil;

public class StatViewRowMap implements RowMapper{
	public StatView mapRow(ResultSet rs, int index) throws SQLException {
		StatView bean =new StatView();
		bean.index_date=rs.getDate("index_date");
		bean.account_cnt=rs.getInt("account_cnt");
		bean.account_deposit_cnt=rs.getInt("account_deposit_cnt");
		bean.deposit_sum=rs.getBigDecimal("deposit_sum")==null?new BigDecimal(0):rs.getBigDecimal("deposit_sum");
		bean.withdraw_sum=rs.getBigDecimal("withdraw_sum")==null?new BigDecimal(0):rs.getBigDecimal("withdraw_sum");
		bean.deposit_cnt=rs.getInt("deposit_cnt");
		bean.withdraw_cnt=rs.getInt("withdraw_cnt");
		bean.payout=rs.getBigDecimal("payout")==null?new BigDecimal(0):rs.getBigDecimal("payout");
		bean.profit=bean.deposit_sum.subtract(bean.withdraw_sum);
		bean.stat_mon=DateUtil.dateToString(bean.index_date, "yyyy-MM");
		return bean;
	}
}
