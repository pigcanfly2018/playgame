package models;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class AdRowMap implements RowMapper{
	public Ad mapRow(ResultSet rs, int index) throws SQLException {
		Ad bean =new Ad();
		bean.ad_id=rs.getLong("ad_id");
		bean.ad_title=rs.getString("ad_title");
		bean.ad_describe=rs.getString("ad_describe");
		bean.pic_url=rs.getString("pic_url");
		bean.target_url=rs.getString("target_url");
		bean.create_date=rs.getDate("create_date");
		bean.start_date=rs.getDate("start_date");
		bean.end_date=rs.getDate("end_date");
		bean.create_user=rs.getString("create_user");
		bean.available=rs.getBoolean("available");
		bean.priority=rs.getInt("priority");
		return bean;
	}
}