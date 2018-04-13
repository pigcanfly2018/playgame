package models;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class ShareModifyRowMap implements RowMapper{

	
	public ShareModify mapRow(ResultSet rs, int arg1) throws SQLException {


		ShareModify bean =new ShareModify();
		bean.modify_id=rs.getLong("modify_id");
		bean.modify_no=rs.getString("modify_no");
		bean.share_before=rs.getInt("share_before");
		bean.share_after=rs.getInt("share_after");
		bean.agent_id=rs.getLong("agent_id");
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
