package models;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class XinBeiRowMap implements RowMapper{

	public XinBei mapRow(ResultSet rs, int index) throws SQLException {
		
		XinBei bean = new XinBei();
		bean.xbp_id=rs.getLong("xbp_id");
		bean.pay_id=rs.getString("pay_id");
		bean.login_name=rs.getString("login_name");
		bean.amount=rs.getBigDecimal("amount");
		bean.payip=rs.getString("payip");
		bean.pay_method=rs.getString("pay_method");
		bean.remark=rs.getString("remark");
		bean.create_date=rs.getTimestamp("create_date");
		bean.order_no=rs.getString("order_no");
		bean.real_amount=rs.getBigDecimal("real_amount");
		bean.state=rs.getInt("state");
		bean.modify_time=rs.getTimestamp("modify_time");
		bean.payer_name=rs.getString("payer_name");
		bean.real_pay_method=rs.getString("real_pay_method");
		bean.finished_date=rs.getTimestamp("finished_date");
		return bean;
	}
}
