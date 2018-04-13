package models;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class HuoliGiftRowMap implements RowMapper{

	
	public HuoliGift mapRow(ResultSet rs, int index) throws SQLException {

		HuoliGift bean = new HuoliGift();
		bean.huoli_gift_id = rs.getLong("huoli_gift_id");
		bean.login_name=rs.getString("login_name");
		
		bean.cust_id = rs.getLong("cust_id");
		
		bean.content=rs.getString("content");
		
		bean.gift_no=rs.getString("gift_no");
		bean.create_date=rs.getTimestamp("create_date");
		
		bean.level = rs.getInt("level");
		
		bean.flag = rs.getInt("flag");
		return bean;
	}

}
