package models;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class BillboardRowMap implements RowMapper{

	public Billboard mapRow(ResultSet rs, int index) throws SQLException {
		
		Billboard bean = new Billboard();
		
		bean.board_id=rs.getLong("board_id");
		
		String loginname = rs.getString("login_name");
		
		bean.login_name=loginname.substring(0,3)+"****"+loginname.substring(loginname.length()-1, loginname.length());
		
		bean.bet_amount=rs.getBigDecimal("bet_amount");
		
		bean.create_date=rs.getTimestamp("create_date");
		
		bean.flag=rs.getInt("flag");
		
		return bean;
	}
	
	
}
