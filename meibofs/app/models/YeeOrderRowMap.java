package models;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.springframework.jdbc.core.RowMapper;

public class YeeOrderRowMap implements RowMapper{
	public Object mapRow(ResultSet rs, int arg1) throws SQLException {
		YeeOrder yee=new YeeOrder();
		yee.create_date=rs.getTimestamp("create_date");
		yee.login_name=rs.getString("login_name");
		yee.phone=rs.getString("phone");
		yee.order_no=rs.getString("order_no");
		yee.credit=rs.getBigDecimal("credit");
		yee.frpid=rs.getString("frpid");
	    yee.pay_credit=rs.getBigDecimal("pay_credit");
	    yee.target_fee=rs.getBigDecimal("target_fee");
	    yee.done_bs=rs.getBoolean("done_bs");
	    yee.pay_date=rs.getString("pay_date");  
		return yee;
	}
}
