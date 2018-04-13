package models;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class FuncRowMap implements RowMapper{
	
	public Func mapRow(ResultSet rs, int index) throws SQLException {
		Func bean =new Func();
		bean.funccode=rs.getString("funccode");
		bean.funcname=rs.getString("funcname");
		bean.pfunccode=rs.getString("pfunccode");
		bean.isgroup=rs.getBoolean("isgroup");
		bean.createdate=rs.getTimestamp("createdate");
		bean.createuser=rs.getString("createuser");
		bean.url=rs.getString("url");
		bean.isbut=rs.getBoolean("isbut");
		bean.icon=rs.getString("icon");
		return bean;

	}
}
