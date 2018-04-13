package models;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class CustformRowMap implements RowMapper{
	public Custform mapRow(ResultSet rs, int index) throws SQLException {
		Custform bean =new Custform();
		bean.form_id=rs.getLong("form_id");
		bean.cust_id=rs.getLong("cust_id");
		bean.login_name=rs.getString("login_name");
		bean.real_name=rs.getString("real_name");
		bean.org_real_name=rs.getString("org_real_name");
		bean.phone=rs.getString("phone");
		bean.org_phone=rs.getString("org_phone");
		bean.email=rs.getString("email");
		bean.org_email=rs.getString("org_email");
		bean.qq=rs.getString("qq");
		bean.org_qq=rs.getString("org_qq");
		bean.bank_name=rs.getString("bank_name");
		bean.org_bank_name=rs.getString("org_bank_name");
		bean.account=rs.getString("account");
		bean.org_account=rs.getString("org_account");
		bean.bank_dot=rs.getString("bank_dot");
		bean.org_bank_dot=rs.getString("org_bank_dot");
		bean.reason=rs.getString("reason");
		bean.account_type=rs.getString("account_type");
		bean.org_account_type=rs.getString("org_account_type");
		bean.create_date=rs.getTimestamp("create_date");
		bean.create_user=rs.getString("create_user");
		bean.audit_date=rs.getTimestamp("audit_date");
		bean.audit_user=rs.getString("audit_user");
		bean.status=rs.getInt("status");
		bean.remarks=rs.getString("remarks");
		return bean;
	}
}
