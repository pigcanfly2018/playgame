package models;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class BettimeRowMap implements RowMapper{

	public Bettime mapRow(ResultSet rs, int index) throws SQLException {
		Bettime bean =new Bettime();
		bean.id=rs.getInt("id");
		bean.platform=rs.getString("platform");
		bean.date=rs.getString("date");
		bean.time=rs.getString("time");
		return bean;
	}
	
}
