package models;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class TongHuiRowMap implements RowMapper{
	
	public TongHui mapRow(ResultSet rs, int index) throws SQLException{
		TongHui bean = new TongHui();
		
		bean.thp_id=rs.getLong("thp_id");
		bean.pay_id=rs.getString("pay_id");
		bean.login_name=rs.getString("login_name");
		bean.amount=rs.getBigDecimal("amount");
		bean.product_name=rs.getString("product_name");
		bean.bank_code=rs.getString("bank_code");
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
