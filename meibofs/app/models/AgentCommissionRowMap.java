package models;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.springframework.jdbc.core.RowMapper;

public class AgentCommissionRowMap implements RowMapper{

	
	public AgentCommission mapRow(ResultSet rs, int index) throws SQLException {

		AgentCommission bean = new AgentCommission();
		
		bean.com_id  = rs.getLong("com_id");
		
		bean.agent_id = rs.getLong("agent_id");
		bean.login_name = rs.getString("login_name");
		bean.create_date = rs.getTimestamp("create_date");
		bean.create_user=rs.getString("create_user");
		bean.status = rs.getInt("status");
		
		bean.audit_date= rs.getTimestamp("audit_date");
		bean.audit_user = rs.getString("audit_user");;
		
		bean.credit = rs.getBigDecimal("credit");
		bean.finalcredit = rs.getBigDecimal("finalcredit");
		
		bean.audit_msg = rs.getString("audit_msg");;
		bean.remarks = rs.getString("remarks");
		
		bean.share = rs.getInt("share");
		bean.active = rs.getInt("active");
		
		bean.start_date = rs.getString("start_date");
		bean.end_date = rs.getString("end_date");
		
		
		bean.total_deposit = rs.getBigDecimal("total_deposit");
		bean.total_withdraw = rs.getBigDecimal("total_withdraw");
		bean.total_gift = rs.getBigDecimal("total_gift");
		bean.total_cost = rs.getBigDecimal("total_cost");
		
		return bean;
	}

}
