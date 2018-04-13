package models;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class CustomerViewRowMap implements RowMapper{
	public CustomerView mapRow(ResultSet rs, int index) throws SQLException {
		CustomerView bean =new CustomerView();
		bean.login_name=rs.getString("login_name");
		bean.phone=rs.getString("phone");
		bean.real_name=rs.getString("real_name");
		bean.qq=rs.getString("qq");
		bean.create_date=rs.getTimestamp("create_date");
		bean.deposit_cnt=rs.getInt("deposit_cnt");
		bean.deposit_amount=rs.getBigDecimal("deposit_amount");
		bean.last_deposit_date=rs.getTimestamp("last_deposit_date");
		bean.withdraw_cnt=rs.getInt("withdraw_cnt");
		bean.withdraw_amount=rs.getBigDecimal("withdraw_amount");
		bean.last_withdraw_date=rs.getTimestamp("last_withdraw_date");
		bean.login_date=rs.getTimestamp("login_date");
		
		return bean;
	}
}

