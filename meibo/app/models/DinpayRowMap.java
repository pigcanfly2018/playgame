package models;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.RowMapper;

public class DinpayRowMap implements RowMapper{
	public Dinpay mapRow(ResultSet rs, int index) throws SQLException {
		Dinpay bean =new Dinpay();
		bean.dinpay_id=rs.getLong("dinpay_id");
		bean.cust_id=rs.getLong("cust_id");
		bean.login_name=rs.getString("login_name");
		bean.merchant_code=rs.getString("merchant_code");
		bean.order_no=rs.getString("order_no");
		bean.order_amount=rs.getBigDecimal("order_amount");
		bean.order_time=rs.getString("order_time");
		bean.client_ip=rs.getString("client_ip");
		bean.product_name=rs.getString("product_name");
		bean.bank_code=rs.getString("bank_code");
		bean.bank_name=rs.getString("bank_name");
		bean.bank_seq_no=rs.getString("bank_seq_no");
		bean.trade_status=rs.getString("trade_status");
		bean.trade_no=rs.getString("trade_no");
		bean.trade_time=rs.getString("trade_time");
		bean.create_date=rs.getTimestamp("create_date");
		bean.notify_id=rs.getString("notify_id");
		bean.interface_version=rs.getString("interface_version");
		bean.rec_sign_type=rs.getString("rec_sign_type");
		bean.rec_sign=rs.getString("rec_sign");
		bean.pay_amount=rs.getBigDecimal("pay_amount");
		bean.send_sign_type=rs.getString("send_sign_type");
		bean.send_sign=rs.getString("send_sign");
		bean.finished=rs.getBoolean("finished");
		bean.finished_date=rs.getTimestamp("finished_date");
		bean.dep_no=rs.getString("dep_no");
		return bean;
	}
}
