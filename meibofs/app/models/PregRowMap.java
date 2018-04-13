package models;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class PregRowMap implements RowMapper{
	public Preg mapRow(ResultSet rs, int arg1) throws SQLException {
		Preg bean =new Preg();
		bean.preg_id=rs.getLong("preg_id");
		bean.login_name=rs.getString("login_name");
		bean.bat_code=rs.getString("bat_code");
		bean.agent_id=rs.getLong("agent_id");
		try{
		 bean.agent_name=rs.getString("agent_name");
		}catch(Exception e){}
		return bean;
		
	}

}
