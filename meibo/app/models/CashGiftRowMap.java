package models;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.RowMapper;

public class CashGiftRowMap implements RowMapper{
	public CashGift mapRow(ResultSet rs, int index) throws SQLException {
		CashGift bean =new CashGift();
		bean.gift_id=rs.getLong("gift_id");
		bean.kh_date=rs.getTimestamp("kh_date");
		bean.login_name=rs.getString("login_name");
		bean.real_name=rs.getString("real_name");
		bean.cust_level=rs.getInt("cust_level");
		bean.gift_type=rs.getString("gift_type");
		bean.gift_code=rs.getString("gift_code");
		bean.deposit_credit=rs.getBigDecimal("deposit_credit");
		bean.net_credit=rs.getBigDecimal("net_credit");
		bean.valid_credit=rs.getBigDecimal("valid_credit");
		bean.rate=rs.getFloat("rate");
		bean.payout=rs.getBigDecimal("payout");
		bean.cs_date=rs.getTimestamp("cs_date");
		bean.remarks=rs.getString("remarks");
		bean.create_date=rs.getTimestamp("create_date");
		bean.create_user=rs.getString("create_user");
		bean.status=rs.getInt("status");
		bean.audit_date=rs.getTimestamp("audit_date");
		bean.audit_user=rs.getString("audit_user");
		bean.audit_msg=rs.getString("audit_msg");
		bean.cust_id=rs.getLong("cust_id");
		bean.gift_no=rs.getString("gift_no");
		return bean;
	}
}
