package models;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.RowMapper;

public class WinListRowMap implements RowMapper{
	public WinList mapRow(ResultSet rs, int index) throws SQLException {
		WinList bean =new WinList();
		bean.win_id=rs.getString("win_id");
		bean.platform=rs.getString("platform");
		bean.game_name=rs.getString("game_name");
		bean.login_name=rs.getString("login_name");
		bean.win_amount=rs.getFloat("win_amount");
		bean.img_path=rs.getString("img_path");
		bean.upload_date=rs.getString("upload_date");
		bean.publish_date=rs.getString("publish_date");
		bean.publish_status=rs.getInt("publish_status");
		return bean;
	}
}
