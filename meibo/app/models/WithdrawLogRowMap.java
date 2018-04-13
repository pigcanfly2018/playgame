package models;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class WithdrawLogRowMap implements RowMapper{
	
	public WithdrawLog mapRow(ResultSet rs, int index) throws SQLException {
		WithdrawLog bean =new WithdrawLog();
		bean.log_id=rs.getLong("log_id");
		bean.pre_status=rs.getInt("pre_status");
		bean.after_status=rs.getInt("after_status");
		bean.withdraw_id=rs.getLong("withdraw_id");
		bean.remarks=rs.getString("remarks");
		bean.create_date=rs.getTimestamp("create_date");
		bean.create_user=rs.getString("create_user");
		bean.wit_no=rs.getString("wit_no");
		return bean;
	}
}