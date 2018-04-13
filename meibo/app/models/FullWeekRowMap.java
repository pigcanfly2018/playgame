package models;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class FullWeekRowMap implements RowMapper{
	public FullWeek mapRow(ResultSet rs, int index) throws SQLException {
		FullWeek bean =new FullWeek();
		bean.login_name=rs.getString("login_name");
		bean.weekamount=rs.getInt("weekamount");
		return bean;
	}
}