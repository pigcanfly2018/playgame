package models;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class EggGiftRowMap implements RowMapper{
	public EggGift mapRow(ResultSet rs, int index) throws SQLException {
		EggGift bean =new EggGift();
		bean.gift_id=rs.getLong("gift_id");
		bean.gift_code=rs.getString("gift_code");
		bean.cust_id=rs.getLong("cust_id");
		bean.login_name=rs.getString("login_name");
		bean.real_name=rs.getString("real_name");
		bean.trophy_code=rs.getString("trophy_code");
		bean.trophy_name=rs.getString("trophy_name");
		bean.trophy_count=rs.getInt("trophy_count");
		bean.create_date=rs.getTimestamp("create_date");
		bean.create_user=rs.getString("create_user");
		bean.status=rs.getInt("status");
		bean.remark=rs.getString("remark");
		bean.audit_user=rs.getString("audit_user");
		bean.audit_date=rs.getTimestamp("audit_date");
		bean.ref_gift_code=rs.getString("ref_gift_code");
		bean.trophy_id=rs.getLong("trophy_id");
		bean.score=rs.getBigDecimal("score");
		bean.cost=rs.getBigDecimal("cost");
		bean.trophy_type=rs.getString("trophy_type");
		return bean;
	}
}
