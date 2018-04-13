package models;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class MoleHitRowMap implements RowMapper {
	public MoleHit mapRow(ResultSet rs, int index) throws SQLException {
		MoleHit bean =new MoleHit();
		bean.id=rs.getLong("id");
		bean.login_name=rs.getString("login_name");
		bean.cust_id=rs.getLong("cust_id");
		bean.create_date=rs.getTimestamp("create_date");
		bean.hitcount=rs.getInt("hitcount");
		
		return bean;
	}
}
