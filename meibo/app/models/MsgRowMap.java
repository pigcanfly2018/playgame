package models;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class MsgRowMap implements RowMapper{
	
	public Msg mapRow(ResultSet rs, int index) throws SQLException {
		Msg bean =new Msg();
		bean.msg_id=rs.getLong("msg_id");
		bean.content=rs.getString("content");
		bean.create_date=rs.getTimestamp("create_date");
		bean.flag=rs.getInt("flag");
		bean.msg_type=rs.getInt("msg_type");
		return bean;
	}

	}