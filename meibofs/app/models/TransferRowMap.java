package models;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class TransferRowMap implements RowMapper{
	public Transfer mapRow(ResultSet rs, int index) throws SQLException {
		Transfer bean =new Transfer();
		bean.transfer_id=rs.getLong("transfer_id");
		bean.cust_id=rs.getLong("cust_id");
		bean.transfer_no=rs.getString("transfer_no");
		bean.login_name=rs.getString("login_name");
		bean.transfer_type=rs.getString("transfer_type");
		bean.credit=rs.getBigDecimal("credit");
		bean.create_date=rs.getTimestamp("create_date");
		bean.create_user=rs.getString("create_user");
		bean.status=rs.getInt("status");
		bean.audit_date=rs.getTimestamp("audit_date");
		bean.audit_user=rs.getString("audit_user");
		bean.audit_msg=rs.getString("audit_msg");
		return bean;
	}
}

