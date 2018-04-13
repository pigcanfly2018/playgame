package models;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class PayOnlineRowMap implements RowMapper{

	public PayOnline mapRow(ResultSet rs, int arg1) throws SQLException {
		// TODO Auto-generated method stub
		
		PayOnline bean = new PayOnline();
		bean.payonline_id = rs.getLong("payonline_id");
		bean.name = rs.getString("name");
		bean.value = rs.getString("value");
		bean.submit_value = rs.getString("submit_value");
		bean.return_value = rs.getString("return_value");
		bean.notify_value = rs.getString("notify_value");
		bean.valuedescribe = rs.getString("valuedescribe");
		bean.req_referer = rs.getString("req_referer");
		return bean;
	}

}
