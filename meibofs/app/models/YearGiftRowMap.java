package models;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.RowMapper;

public class YearGiftRowMap implements RowMapper{
	public YearGift mapRow(ResultSet rs, int index) throws SQLException {
		YearGift bean =new YearGift();
		bean.year_gift_id=rs.getLong("year_gift_id");
		
		bean.login_name=rs.getString("login_name");
		bean.day=rs.getString("day");
		bean.cust_id = rs.getLong("cust_id");
		
		bean.credit=rs.getBigDecimal("credit");
		
		bean.gift_no=rs.getString("gift_no");
		bean.create_date=rs.getTimestamp("create_date");
		
		
		bean.flag = rs.getInt("flag");
		return bean;
	}
}
