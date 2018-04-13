package models;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class AgentWithdrawRowMap implements RowMapper{
	public AgentWithdraw mapRow(ResultSet rs, int index) throws SQLException {
		AgentWithdraw bean =new AgentWithdraw();
		bean.awithdraw_id=rs.getLong("awithdraw_id");
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
		bean.wit_cnt=rs.getInt("wit_cnt");
		bean.agent_id=rs.getLong("agent_id");
		bean.awit_no=rs.getString("awit_no");
		bean.locked=rs.getBoolean("locked");
		return bean;
	}
}
