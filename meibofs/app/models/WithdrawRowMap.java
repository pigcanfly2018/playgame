package models;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class WithdrawRowMap implements RowMapper{
	
	public Withdraw mapRow(ResultSet rs, int index) throws SQLException {
		Withdraw bean =new Withdraw();
		bean.withdraw_id=rs.getLong("withdraw_id");
		bean.login_name=rs.getString("login_name");
		bean.real_name=rs.getString("real_name");
		bean.amount=rs.getBigDecimal("amount");
		bean.bank_name=rs.getString("bank_name");
		bean.account_name=rs.getString("account_name");
		bean.account=rs.getString("account");
		bean.account_type=rs.getString("account_type");
		bean.location=rs.getString("location");
		bean.remarks=rs.getString("remarks");
		bean.create_date=rs.getTimestamp("create_date");
		bean.create_user=rs.getString("create_user");
		bean.status=rs.getInt("status");
		bean.cust_id=rs.getLong("cust_id");
		bean.wit_no=rs.getString("wit_no");
		bean.locked=rs.getBoolean("locked");
		bean.wit_cnt=rs.getInt("wit_cnt");
		return bean;
	}
}
