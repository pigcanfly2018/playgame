package models;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class PicRowMap implements RowMapper{
	public Pic mapRow(ResultSet rs, int arg1) throws SQLException {
		Pic bean =new Pic();
		bean.pic_id=rs.getLong("pic_id");
		bean.pic_data=rs.getString("pic_data");
		bean.create_date=rs.getTimestamp("create_date");
		bean.pic_size=rs.getLong("pic_size");
		bean.ftype=rs.getString("ftype");
		return bean;
	}

}
