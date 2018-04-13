package models;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class AgentRowMap implements RowMapper{
	public Agent mapRow(ResultSet rs, int index) throws SQLException {
		Agent bean =new Agent();
		bean.agent_id=rs.getLong("agent_id");
		bean.login_name=rs.getString("login_name");
		bean.login_pwd=rs.getString("login_pwd");
		bean.real_name=rs.getString("real_name");
		bean.phone=rs.getString("phone");
		bean.email=rs.getString("email");
		bean.qq=rs.getString("qq");
		bean.reg_ip=rs.getString("reg_ip");
		bean.reg_code = rs.getString("reg_code");
		bean.create_date=rs.getTimestamp("create_date");
		bean.create_user=rs.getString("create_user");
		bean.audit_user = rs.getString("audit_user");
		bean.audit_msg = rs.getString("audit_msg");
		bean.audit_date = rs.getTimestamp("audit_date");
		
		bean.lock_date = rs.getTimestamp("lock_date");
		bean.lock_user = rs.getString("lock_user");
		bean.lock_msg = rs.getString("lock_msg");
		
		bean.last_login_ip=rs.getString("last_login_ip");
		bean.last_login_date=rs.getTimestamp("last_login_date");
		
		bean.credit=rs.getBigDecimal("credit");
		bean.flag=rs.getInt("flag");
		
		bean.parent_id=rs.getLong("parent_id");
		
		bean.remarks=rs.getString("remarks");
		bean.bank_name=rs.getString("bank_name");
		bean.account_type=rs.getString("account_type");
		bean.bank_dot=rs.getString("bank_dot");
		bean.account=rs.getString("account");
		
		
		
		return bean;
	}

}
