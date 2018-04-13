package models;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class LetterRowMap implements RowMapper{

	public Object mapRow(ResultSet rs, int arg1) throws SQLException {
	     
		Letter letter =new Letter();
		letter.cust_id=rs.getLong("cust_id");
		letter.title=rs.getString("title");
		letter.content=rs.getString("content");
		letter.letter_id=rs.getLong("letter_id");
		letter.create_date=rs.getTimestamp("create_date");
		letter.create_user=rs.getString("create_user");
		letter.read_date=rs.getTimestamp("read_date");
		letter.read_flag=rs.getBoolean("read_flag");
		letter.is_public=rs.getBoolean("is_public");
		letter.login_name=rs.getString("login_name");
		return letter;
	}

}
