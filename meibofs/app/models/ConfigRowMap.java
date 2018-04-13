package models;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class ConfigRowMap implements RowMapper{
	public Config mapRow(ResultSet rs, int arg1) throws SQLException {
		Config bean =new Config();
		bean.config_id=rs.getLong("config_id");
		bean.config_name=rs.getString("config_name");
		bean.config_value=rs.getString("config_value");
		bean.config_describe=rs.getString("config_describe");
		
		bean.maxlimit = rs.getInt("maxlimit") == 0 ? null:rs.getInt("maxlimit");
		bean.config_level=rs.getString("config_level");
		bean.sortpriority = rs.getString("sortpriority") ;
		return bean;
	}
}