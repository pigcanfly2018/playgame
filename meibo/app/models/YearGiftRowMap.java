package models;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class YearGiftRowMap implements RowMapper{
	
	public YearGift mapRow(ResultSet rs, int index) throws SQLException {
		YearGift bean =new YearGift();
		bean.create_date=rs.getTimestamp("create_date");
		bean.credit=rs.getBigDecimal("credit");
		bean.cust_id=rs.getLong("cust_id");
		bean.day=rs.getString("day");
		bean.flag=rs.getInt("flag");
		bean.gift_no=rs.getString("gift_no");
		bean.login_name=rs.getString("login_name");
		bean.year_gift_id=rs.getLong("year_gift_id");
		return bean;
	}
}

