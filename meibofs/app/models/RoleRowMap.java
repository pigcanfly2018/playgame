package models;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.RowMapper;

public class RoleRowMap implements RowMapper{
	public Role mapRow(ResultSet rs, int index) throws SQLException {
		Role bean =new Role();
		bean.rolecode=rs.getString("rolecode");
		bean.rolename=rs.getString("rolename");
		bean.createdate=rs.getTimestamp("createdate");
		bean.createuser=rs.getString("createuser");
		return bean;
	}
}
