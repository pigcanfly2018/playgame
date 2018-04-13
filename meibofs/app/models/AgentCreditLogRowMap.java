package models;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class AgentCreditLogRowMap implements RowMapper{

	
	public AgentCreditLog mapRow(ResultSet rs, int index) throws SQLException {
		// TODO Auto-generated method stub
		AgentCreditLog bean = new AgentCreditLog();
		
		bean.log_id=rs.getLong("log_id");
		bean.log_type=rs.getString("log_type");
		bean.credit=rs.getBigDecimal("credit");
		bean.login_name=rs.getString("login_name");
		bean.agent_id=rs.getLong("agent_id");
		bean.create_date=rs.getTimestamp("create_date");
		bean.create_user=rs.getString("create_user");
		bean.remarks=rs.getString("remarks");
		bean.order_no=rs.getString("order_no");
		bean.after_credit=rs.getBigDecimal("after_credit");
		bean.org_credit=rs.getBigDecimal("org_credit");
		return bean;
		
	}

}
