package models;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class AgentWithdrawLogRowMap implements RowMapper{

	public AgentWithdrawLog mapRow(ResultSet rs, int index) throws SQLException {
		AgentWithdrawLog bean =new AgentWithdrawLog();
		bean.alog_id=rs.getLong("alog_id");
		bean.pre_status=rs.getInt("pre_status");
		bean.after_status=rs.getInt("after_status");
		bean.awithdraw_id=rs.getLong("awithdraw_id");
		bean.remarks=rs.getString("remarks");
		bean.create_date=rs.getTimestamp("create_date");
		bean.create_user=rs.getString("create_user");
		bean.awit_no=rs.getString("awit_no");
		return bean;
	}
}
