package models;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.springframework.jdbc.core.RowMapper;

public class ScoreRecRowMap implements RowMapper{
	public ScoreRec mapRow(ResultSet rs, int index) throws SQLException {

		ScoreRec sr=new ScoreRec();
		sr.rec_id=rs.getLong("rec_id");
		sr.rec_code=rs.getString("rec_code");
		sr.cust_id=rs.getLong("cust_id");
		sr.login_name=rs.getString("login_name");
		sr.rec_type=rs.getString("rec_type");
		sr.score=rs.getBigDecimal("score");
		sr.create_date=rs.getTimestamp("create_date");
		sr.create_user=rs.getString("create_user");
		sr.cur_score=rs.getBigDecimal("cur_score");
        return sr;
	}

}
