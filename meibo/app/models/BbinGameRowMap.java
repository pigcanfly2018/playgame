package models;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class BbinGameRowMap implements RowMapper{
	public Long  game_id;
	public String game_name;
	public String game_code;
	public boolean is_hot;
	public boolean is_new;
	public Boolean enable;
	public String Image_File_Name;
	public String game_type;
	public Integer sortpriority;
	public boolean enter_directly;
	
	public BbinGame mapRow(ResultSet rs, int arg1) throws SQLException {
		BbinGame bean = new BbinGame();
		
		bean.game_id=rs.getLong("game_id");
		bean.game_name=rs.getString("game_name");
		bean.game_code=rs.getString("game_code");
		bean.is_hot=rs.getBoolean("is_hot");
		bean.is_new=rs.getBoolean("is_new");
		bean.enable=rs.getBoolean("enable");
		bean.Image_File_Name=rs.getString("Image_File_Name");
		bean.game_type=rs.getString("game_type");
		bean.sortpriority=rs.getInt("sortpriority");
		bean.enter_directly=rs.getBoolean("enter_directly");
		
		return bean;
	}

}
