package models;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class AgentStatRowMap implements RowMapper{

	
	public AgentStat mapRow(ResultSet rs, int index) throws SQLException {
		// TODO Auto-generated method stub
		AgentStat bean =new AgentStat();
		
		bean.agent_id = rs.getLong("agent_id");
		//bean.login_name = rs.getString("login_name");
		bean.stat_date = rs.getString("stat_date");
		
		bean.deposit_collect = rs.getBigDecimal("deposit_collect");
		bean.withdraw_collect = rs.getBigDecimal("withdraw_collect");
		bean.gift_collect = rs.getBigDecimal("gift_collect");
		
		bean.deposit_count = rs.getInt("deposit_count");
		bean.withdraw_count = rs.getInt("withdraw_count");
		bean.gift_count = rs.getInt("gift_count");

		return bean;
		
	}

}
