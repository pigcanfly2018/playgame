package models;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class RpnRowMap implements RowMapper{
	public RPN mapRow(ResultSet rs, int index) throws SQLException {
		RPN bean = new RPN();
		bean.rpn_id=rs.getLong("rpn_id");
		bean.order_id=rs.getString("order_id");
		bean.login_name=rs.getString("login_name");
		bean.amount=rs.getBigDecimal("amount");
		bean.customer_ip=rs.getString("customer_ip");
		bean.bank_id=rs.getString("bank_id");
		bean.remark=rs.getString("remark");
		bean.create_date=rs.getTimestamp("create_date");
		bean.rpn_order_no=rs.getString("rpn_order_no");
		bean.real_amount=rs.getBigDecimal("real_amount");
		bean.state=rs.getInt("state");
		bean.modify_time=rs.getTimestamp("modify_time");
		bean.payer_name=rs.getString("payer_name");
		bean.real_pay_method=rs.getString("real_pay_method");
		bean.finished_date=rs.getTimestamp("finished_date");
		return bean;
	}
}
