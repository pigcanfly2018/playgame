package models;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class DepositRowMap implements RowMapper{

	public Deposit mapRow(ResultSet rs, int index) throws SQLException {
		Deposit bean =new Deposit();
		bean.deposit_id=rs.getLong("deposit_id");
		bean.dep_no=rs.getString("dep_no");
		bean.cust_id=rs.getLong("cust_id");
		bean.login_name=rs.getString("login_name");
		bean.real_name=rs.getString("real_name");
		bean.amount=rs.getBigDecimal("amount");
		bean.poundage=rs.getBigDecimal("poundage");
		bean.pdage_status=rs.getInt("pdage_status");
		bean.status=rs.getInt("status");
		bean.bank_name=rs.getString("bank_name");
		bean.account_name=rs.getString("account_name");
		bean.deposit_type=rs.getString("deposit_type");
		bean.location=rs.getString("location");
		bean.deposit_date=rs.getTimestamp("deposit_date");
		bean.remarks=rs.getString("remarks");
		bean.pic_id=rs.getLong("pic_id");
		bean.create_date=rs.getTimestamp("create_date");
		bean.create_user=rs.getString("create_user");
		bean.ip=rs.getString("ip");
		bean.audit_date=rs.getTimestamp("audit_date");
		bean.sb_game=rs.getString("sb_game");
		bean.is_sb=rs.getBoolean("is_sb");
		bean.order_no=rs.getString("order_no");
		bean.locked=rs.getBoolean("locked");
		bean.locked_date=rs.getTimestamp("locked_date");
		return bean;
	}
}
