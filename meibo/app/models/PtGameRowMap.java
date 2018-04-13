package models;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class PtGameRowMap implements RowMapper{

	public PtGame mapRow(ResultSet rs, int arg1) throws SQLException {
		PtGame ptGame=new PtGame();
		ptGame.pt_id=rs.getLong("pt_id");
		ptGame.game_name=rs.getString("game_name");
		ptGame.game_type=rs.getString("game_type");
		ptGame.progressive=rs.getString("progressive");
		ptGame.branded=rs.getString("branded");
		ptGame.game_code=rs.getString("game_code");
		ptGame.client=rs.getString("client");
		ptGame.flash=rs.getString("flash");
		ptGame.mobile=rs.getString("mobile");
		ptGame.cn_name=rs.getString("cn_name");
		ptGame.pool_name=rs.getString("pool_name");
		ptGame.hot=rs.getBoolean("hot");
		ptGame.recommend=rs.getBoolean("recommend");
		return ptGame;
	}
	
	

}
