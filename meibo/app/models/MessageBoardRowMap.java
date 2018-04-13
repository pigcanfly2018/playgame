package models;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class MessageBoardRowMap implements RowMapper{
	public MessageBoard mapRow(ResultSet rs, int index) throws SQLException {
		MessageBoard bean =new MessageBoard();
		bean.msg_id=rs.getLong("msg_id");
		bean.cust_name=rs.getString("cust_name");
		bean.question=rs.getString("question");
		bean.reply=rs.getString("reply");
		bean.create_date=rs.getTimestamp("create_date");
		bean.create_user=rs.getString("create_user");
		bean.reply_user=rs.getString("reply_user");
		bean.reply_date=rs.getTimestamp("reply_date");
		bean.show_date=rs.getTimestamp("show_date");
		return bean;
	}
}

