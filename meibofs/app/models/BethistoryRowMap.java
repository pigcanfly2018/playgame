package models;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class BethistoryRowMap implements RowMapper{

	public Bethistory mapRow(ResultSet rs, int index) throws SQLException {
		Bethistory bean =new Bethistory();
		bean.id=rs.getLong("id");
		bean.filename=rs.getString("filename");
		bean.modifytime = rs.getLong("modifytime");
		return bean;
	}
}
