package models;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class DiscountRowMap implements RowMapper{

	public Discount mapRow(ResultSet rs, int arg1) throws SQLException {
		Discount bean =new Discount();
		bean.discount_id=rs.getLong("discount_id");
		bean.b_url=rs.getString("b_url");
		bean.s_url=rs.getString("s_url");
		bean.title=rs.getString("title");
		
		bean.summary=rs.getString("summary");
		bean.content=rs.getString("content");
		bean.available=rs.getBoolean("available");
		bean.create_date=rs.getTimestamp("create_date");
		bean.create_user=rs.getString("create_user");
		bean.priority=rs.getInt("priority");
		bean.is_hot=rs.getBoolean("is_hot");
		
		return bean;
	}
	

}
