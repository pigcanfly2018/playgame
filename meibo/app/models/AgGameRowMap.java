package models;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class AgGameRowMap implements RowMapper{

	public AgGame mapRow(ResultSet rs, int arg1) throws SQLException {
		
		AgGame bean = new AgGame();
		
		bean.ag_id=rs.getLong("ag_id");
		bean.game_name=rs.getString("game_name");
		bean.game_type=rs.getString("game_type");
		bean.Image_File_Name=rs.getString("Image_File_Name");
		bean.sortpriority=rs.getString("sortpriority");
		bean.game_code=rs.getString("game_code");
		
		bean.hot=rs.getBoolean("hot");
		bean.is_new=rs.getBoolean("is_new");
		bean.enable=rs.getBoolean("enable");
		
		return bean;
	}
}
