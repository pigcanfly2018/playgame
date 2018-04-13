package models;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class HongbaoRowMap implements RowMapper{
	public Hongbao mapRow(ResultSet rs, int index) throws SQLException {
		Hongbao bean =new Hongbao();
		bean.mb_hongbao_id=rs.getLong("mb_hongbao_id");
		bean.cust_id=rs.getLong("cust_id");
		bean.gift_id=rs.getLong("gift_id");
		bean.login_name=rs.getString("login_name");
		bean.create_date=rs.getTimestamp("create_date");
		bean.credit=rs.getBigDecimal("credit");
		bean.status=rs.getInt("status");
		bean.content=rs.getString("content");
		bean.level=rs.getString("level");
		bean.gift_no=rs.getString("gift_no");
		
		return bean;
	}
}
