package models;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class BetRecordRowMap implements RowMapper{
	public BetRecord  mapRow(ResultSet rs, int index) throws SQLException {
		BetRecord record = new BetRecord();
		
		record.dataId = rs.getLong("dataId");
		record.login_name = rs.getString("login_name");
		record.validBetAmount = rs.getBigDecimal("validBetAmount");
		record.platform = rs.getString("platform");
		record.create_date=rs.getTimestamp("create_date");
		record.agent_id = rs.getLong("agent_id");
		record.bet_date = rs.getTimestamp("bet_date");
		return record;
	}
}
