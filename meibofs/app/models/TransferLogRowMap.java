package models;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class TransferLogRowMap implements RowMapper{

	
	public TransferLog mapRow(ResultSet rs, int index) throws SQLException {
		TransferLog bean =new TransferLog();
		bean.transfer_id=rs.getLong("transfer_id");
		bean.cust_id=rs.getLong("cust_id");
		bean.bill_no=rs.getString("bill_no");
		bean.login_name=rs.getString("login_name");
		bean.transer_from=rs.getString("transer_from");
		bean.transer_to=rs.getString("transer_to");
		bean.credit=rs.getBigDecimal("credit");
		bean.create_date=rs.getTimestamp("create_date");
		bean.create_user=rs.getString("create_user");
		bean.status=rs.getInt("status");
		bean.finish_date=rs.getTimestamp("finish_date");
		bean.remarks=rs.getString("remarks");
		return bean;
	}

}
