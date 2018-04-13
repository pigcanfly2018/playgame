package models;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class SignRowMap implements RowMapper{

	
	public Sign mapRow(ResultSet rs, int index) throws SQLException {
		// TODO Auto-generated method stub
		
		Sign bean = new Sign();
		//bean.sign_id = rs.getLong("sign_id");
		//bean.cust_id = rs.getLong("cust_id");
		//bean.login_name = rs.getString("login_name");
		//bean.create_date=rs.getTimestamp("create_date");
		bean.sign_date=rs.getString("sign_date");
		
		return bean;
	}

}
