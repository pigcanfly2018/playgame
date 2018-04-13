package models;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class HcpayRowMap implements RowMapper{
	public Hcpay mapRow(ResultSet rs, int arg1) throws SQLException {
		Hcpay bean =new Hcpay();
			bean.gc_pay_id=rs.getLong("gc_pay_id");
			bean.bill_no=rs.getString("bill_no");
			bean.bank_code=rs.getString("bank_code");
			bean.create_date=rs.getTimestamp("create_date");
			bean.amount=rs.getBigDecimal("amount");
			bean.cust_id=rs.getLong("cust_id");
			bean.dep_no=rs.getString("dep_no");
			bean.finish_date=rs.getTimestamp("finish_date");
			bean.login_name=rs.getString("login_name");
			bean.order_time=rs.getString("order_time");
			bean.status=rs.getString("status");
		return bean;
	}
}
