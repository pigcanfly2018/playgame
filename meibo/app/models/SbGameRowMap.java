package models;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class SbGameRowMap implements RowMapper{
	
	public SbGame mapRow(ResultSet rs, int arg1) throws SQLException {
		SbGame bean = new SbGame();
		
		bean.sb_id=rs.getLong("sb_id");
		bean.game_name=rs.getString("game_name");
		bean.game_type=rs.getString("game_type");
		bean.image_game_name=rs.getString("image_game_name");
		bean.image_preview_name=rs.getString("image_preview_name");
		bean.sortpriority=rs.getInt("sortpriority");
		bean.game_code=rs.getString("game_code");
		
		bean.providercode=rs.getString("providercode");
		bean.enable=rs.getBoolean("enable");
		
		return bean;
	}

}
