package models;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class CreditFixRowMap implements RowMapper{

	public CreditFix mapRow(ResultSet rs, int arg1) throws SQLException {
		CreditFix bean =new CreditFix();
		bean.fix_id=rs.getLong("fix_id");
		bean.fix_no=rs.getString("fix_no");
		bean.credit=rs.getBigDecimal("credit");
		bean.cust_id=rs.getLong("cust_id");
		bean.login_name=rs.getString("login_name");
		bean.create_date=rs.getTimestamp("create_date");
		bean.create_user=rs.getString("create_user");
		bean.status=rs.getInt("status");
		bean.remarks=rs.getString("remarks");
		bean.audit_date=rs.getTimestamp("audit_date");
		bean.audit_user=rs.getString("audit_user");
		bean.audit_msg=rs.getString("audit_msg");
		return bean;
	}

}
