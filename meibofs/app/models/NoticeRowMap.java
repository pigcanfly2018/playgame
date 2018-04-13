package models;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class NoticeRowMap  implements RowMapper{
	public Notice mapRow(ResultSet rs, int index) throws SQLException {
		Notice bean =new Notice();
		bean.notice_id=rs.getLong("notice_id");
		bean.title=rs.getString("title");
		bean.content=rs.getString("content");
		bean.start_date=rs.getTimestamp("start_date");
		bean.end_date=rs.getTimestamp("end_date");
		bean.create_user=rs.getString("create_user");
		bean.create_date=rs.getTimestamp("create_date");
		bean.priority=rs.getInt("priority");
		bean.available=rs.getBoolean("available");
		return bean;
	}
}