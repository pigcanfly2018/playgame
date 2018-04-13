package models;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class RoleFuncRowMap implements RowMapper{
	public RoleFunc mapRow(ResultSet rs, int index) throws SQLException {
		RoleFunc bean =new RoleFunc();
		bean.rolecode=rs.getString("rolecode");
		bean.funccode=rs.getString("funccode");
		bean.createdate=rs.getTimestamp("createdate");
		bean.createuser=rs.getString("createuser");
		return bean;
	}
}
