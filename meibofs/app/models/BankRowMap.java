package models;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class BankRowMap implements RowMapper{
	public Bank mapRow(ResultSet rs, int index) throws SQLException {
		Bank bean =new Bank();
		bean.bank_id=rs.getLong("bank_id");
		bean.bank_name=rs.getString("bank_name");
		bean.account=rs.getString("account");
		bean.account_name=rs.getString("account_name");
		bean.bank_dot=rs.getString("bank_dot");
		bean.cust_level=rs.getInt("cust_level");
		bean.remarks=rs.getString("remarks");
		bean.create_user=rs.getString("create_user");
		bean.specifiedname = rs.getString("specifiedname");
		bean.create_date=rs.getTimestamp("create_date");
		bean.available=rs.getBoolean("available");
		return bean;
	}
}



