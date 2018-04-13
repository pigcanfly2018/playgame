package models;

import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import util.Sp;

public class AgGame {

	public Long  ag_id;
	public String game_name;
	public String game_type;
	public String Image_File_Name;
	public Integer sortpriority;
	public String game_code;
	public Boolean hot;
	public Boolean is_new;
	public Boolean enable;
	
	
	public static boolean NTdelete(Long ag_id){
	    String sql="delete from mb_ag_game  where ag_id=?";
		final Object[] objects=new Object[]{ag_id};
		int count=Sp.get().getDefaultJdbc().update(sql, objects);
		return count>0;
	}
	
	public static boolean update(AgGame ag){
		String sql="update mb_ag_game set game_name=?,game_type=?,Image_File_Name=?,game_code=?,sortpriority=?,hot=?,is_new=?,enable=? where ag_id=?";
		
		int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{ag.game_name,ag.game_type,ag.Image_File_Name,ag.game_code,ag.sortpriority,ag.hot,ag.is_new,ag.enable,ag.ag_id});
		return count>0;
	}
	
	public static boolean create(AgGame ag){
		String sql="insert into mb_ag_game(game_name,game_type,Image_File_Name,sortpriority,game_code,hot,is_new,enable) values(?,?,?,?,?,?,?,?)";
		final Object[] objects=new Object[]{ag.game_name,ag.game_type,ag.Image_File_Name,ag.sortpriority,ag.game_code,ag.hot,ag.is_new,ag.enable};
		KeyHolder keyHolder = new GeneratedKeyHolder();
		int result=Sp.get().getDefaultJdbc().update(new MyPreparedStatementCreator(sql,objects), keyHolder);
		return result>0;
	}
	
	
}
