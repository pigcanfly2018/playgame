package models;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class JvBaoRowMap implements RowMapper{

	public JvBao mapRow(ResultSet rs, int index) throws SQLException {
		JvBao bean = new JvBao();
		bean.jbp_id=rs.getLong("jbp_id");
		bean.pay_id=rs.getString("pay_id");
		bean.login_name=rs.getString("login_name");
		bean.amount=rs.getBigDecimal("amount");
		bean.goods_name=rs.getString("goods_name");
		bean.partner_id=rs.getString("partner_id");
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
