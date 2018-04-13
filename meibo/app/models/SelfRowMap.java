package models;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class SelfRowMap implements RowMapper{

	public Object mapRow(ResultSet rs, int arg1) throws SQLException {
	     
		SelfApp self =new SelfApp();
		self.id= rs.getInt("id");
		self.login_name=rs.getString("login_name");
		self.app_name=rs.getString("app_name");
		self.create_time=rs.getTimestamp("create_time");
		self.status=rs.getInt("status");
		self.gift_no=rs.getString("gift_no");
		self.remark=rs.getString("remark");
		return self;
	}

}
