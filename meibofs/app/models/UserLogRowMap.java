package models;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class UserLogRowMap implements RowMapper{
	
	public UserLog mapRow(ResultSet rs, int index) throws SQLException {
		UserLog bean =new UserLog();
		bean.log_id=rs.getLong("log_id");
		bean.op_user=rs.getString("op_user");
		bean.create_date=rs.getTimestamp("create_date");
		bean.log_msg=rs.getString("log_msg");
		return bean;
	}
}
