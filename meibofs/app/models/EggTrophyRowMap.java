package models;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class EggTrophyRowMap implements RowMapper{
	public EggTrophy mapRow(ResultSet rs, int index) throws SQLException {
		EggTrophy bean =new EggTrophy();
		bean.trophy_id=rs.getLong("trophy_id");
		bean.trophy_name=rs.getString("trophy_name");
		bean.trophy_code=rs.getString("trophy_code");
		bean.trophy_count=rs.getInt("trophy_count");
		bean.egg=rs.getString("egg");
		bean.is_default=rs.getBoolean("is_default");
		bean.enable=rs.getBoolean("enable");
		bean.cost=rs.getBigDecimal("cost");
		bean.trophy_type=rs.getString("trophy_type");
		return bean;
	}
}

