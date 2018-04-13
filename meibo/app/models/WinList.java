package models;

import java.util.Date;
import java.util.List;

import util.Sp;

public class WinList {
	public String win_id;
	public String platform;
	public String game_name;
	public String login_name;
	public float win_amount;
	public String img_path;
	public Date upload_date;
	public Date publish_date;
	public int publish_status;

	public static List<WinList> getNewPublished(){
		String sql="SELECT win_id,platform,game_name,login_name,win_amount,img_path,upload_date,publish_date,publish_status FROM mb_win_list ";
		sql += "WHERE publish_date<now() and publish_status=1 order by publish_date desc limit 100";
		List<WinList> list=Sp.get().getDefaultJdbc().query(sql,new Object[]{},new WinListRowMap());
		return list;
	}
}
