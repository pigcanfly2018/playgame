package models;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class SignRewardRowMap implements RowMapper{

	
	public SignReward mapRow(ResultSet rs, int index) throws SQLException {
		// TODO Auto-generated method stub
		
		SignReward bean = new SignReward();
		
		//bean.reward_id = rs.getLong("reward_id");
		
		//bean.cust_id = rs.getLong("cust_id");
		//bean.login_name = rs.getString("login_name");
		//bean.create_date=rs.getTimestamp("create_date");
		bean.reward_date=rs.getString("reward_date");
		
		//bean.cust_level = rs.getInt("cust_level");
		//bean.amount = rs.getBigDecimal("amount");
		
		return bean;
	}

}
