package models;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class PpGameRowMap implements RowMapper{

	public PpGame mapRow(ResultSet rs, int arg1) throws SQLException{
		
		PpGame bean = new PpGame();
		
		bean.game_id=rs.getLong("game_id");
		bean.game_name=rs.getString("game_name");
		bean.game_type=rs.getString("game_type");
		bean.Image_File_Name=rs.getString("Image_File_Name");
		bean.sortpriority=rs.getString("sortpriority");
		bean.game_code=rs.getString("game_code");
		
		bean.is_hot=rs.getBoolean("is_hot");
		bean.is_new=rs.getBoolean("is_new");
		bean.enable=rs.getBoolean("enable");
		
		return bean;
		
	}
}
