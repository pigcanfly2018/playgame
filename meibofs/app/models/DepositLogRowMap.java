package models;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.RowMapper;

public class DepositLogRowMap implements RowMapper{
	
	public DepositLog mapRow(ResultSet rs, int index) throws SQLException {
		DepositLog bean =new DepositLog();
		bean.log_id=rs.getLong("log_id");
		bean.pre_status=rs.getInt("pre_status");
		bean.after_status=rs.getInt("after_status");
		bean.deposit_id=rs.getLong("deposit_id");
		bean.remarks=rs.getString("remarks");
		bean.create_date=rs.getTimestamp("create_date");
		bean.create_user=rs.getString("create_user");
		bean.dep_no=rs.getString("dep_no");
		return bean;
	}

}
