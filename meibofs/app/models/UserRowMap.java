package models;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class UserRowMap  implements RowMapper{
	public User mapRow(ResultSet rs, int index) throws SQLException {
		User user =new User();
		user.loginname=rs.getString("loginname");
		//user.password=rs.getString("password");
		user.flag=rs.getBoolean("flag");
		user.issuper=rs.getBoolean("issuper");
		user.lastloginip=rs.getString("lastloginip");
		user.rolecode=rs.getString("rolecode");
		user.createdate=rs.getTimestamp("createdate");
		user.createuser=rs.getString("createuser");
		user.lastlogindate=rs.getTimestamp("lastlogindate");
		user.realname=rs.getString("realname");
		return user;
	}
}
