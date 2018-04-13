package models;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class PhoneRecordRowMap implements RowMapper{

	
	public PhoneRecord mapRow(ResultSet rs, int index) throws SQLException {

		PhoneRecord bean = new PhoneRecord();
		
		bean.Id = rs.getLong("Id");
		bean.login_name = rs.getString("login_name");
		bean.content = rs.getString("content");
		bean.create_date=rs.getTimestamp("create_date");
		
		return bean;
	}

}
