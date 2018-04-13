package models;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import util.Sp;

public class WinList {
	public String win_id;
	public String platform;
	public String game_name;
	public String login_name;
	public float win_amount;
	public String img_path;
	public String upload_date;
	public String publish_date;
	public int publish_status;

	public Long NTcreat() {
		final String sql = "insert into mb_win_list (platform,game_name,login_name,win_amount,img_path,upload_date,publish_date,publish_status) values(?,?,?,?,?,?,?,?)";
		final Object[] objects = new Object[] { platform, game_name, login_name, win_amount, img_path, upload_date,	publish_date, publish_status };
		KeyHolder keyHolder = new GeneratedKeyHolder();
		Sp.get().getDefaultJdbc().update(new MyPreparedStatementCreator(sql, objects), keyHolder);
		long keyValue = keyHolder.getKey().longValue();
		return keyValue;
	}

	public boolean NTupdate() {
		String sql = "update mb_win_list set platform=?,game_name=?,login_name=?,win_amount=?,img_path=?,upload_date=?,publish_date=?,publish_status=? where win_id=?";
		try {
			int count = Sp.get().getDefaultJdbc().update(sql,
					new Object[] { platform, game_name, login_name, win_amount, img_path, upload_date, publish_date, publish_status, win_id });
			return count > 0;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}

	public static boolean NTdeleteWins(String winIds) {
		String sql="delete from mb_win_list where win_id in("+winIds+")";
		 int  count= Sp.get().getDefaultJdbc().update(sql);
		 if(count>0)return true;return false;
	}

	public static boolean NTPublishWins(String winIds) {
		String sql="update mb_win_list set publish_date=now(), publish_status=1 where win_id in("+winIds+")";
		 int count= Sp.get().getDefaultJdbc().update(sql);
		 if(count>0)return true;return false;
	}
	
	public static int delOutdatedRecord() {
		String sql="delete from mb_win_list where win_id < (select min(a.win_id) from (select win_id from mb_win_list order by win_id desc limit " + 1000 + ") a)";
		return Sp.get().getDefaultJdbc().update(sql);
	}
}
