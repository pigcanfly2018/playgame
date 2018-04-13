package models;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class UserMsgRowMap  implements RowMapper{
	public UserMsg mapRow(ResultSet rs, int index) throws SQLException {
		UserMsg msg =new UserMsg();
		msg.um_id=rs.getLong("um_id");
		msg.login_name=rs.getString("login_name");
		msg.notify_flag=rs.getInt("notify_flag");
		msg.msg_id=rs.getLong("msg_id");
		msg.create_date=rs.getDate("create_date");
		return msg;
	}
}