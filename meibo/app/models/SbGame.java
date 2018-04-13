package models;

import java.util.List;

import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import util.Sp;

public class SbGame {

	public Long  sb_id;
	public String game_name;
	public String game_type;
	public String image_game_name;
	public String image_preview_name;
	public Integer sortpriority;
	public String game_code;
	public String providercode;
	public Boolean enable;
	
	public static boolean NTdelete(Long sb_id){
	    String sql="delete from mb_sb_game  where sb_id=?";
		final Object[] objects=new Object[]{sb_id};
		int count=Sp.get().getDefaultJdbc().update(sql, objects);
		return count>0;
	}
	
	public static boolean update(SbGame shenbo){
		String sql="update mb_sb_game set game_name=?,game_type=?,image_game_name=?,image_preview_name=?,game_code=?,sortpriority=?,providercode=?,enable=? where sb_id=?";
		
		int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{shenbo.game_name,shenbo.game_type,shenbo.image_game_name,shenbo.image_preview_name,shenbo.game_code,shenbo.sortpriority,shenbo.providercode,shenbo.enable,shenbo.sb_id});
		return count>0;
	}
	
	public static boolean create(SbGame shenbo){
		String sql="insert into mb_sb_game(game_name,game_type,image_game_name,image_preview_name,sortpriority,game_code,providercode,enable) values(?,?,?,?,?,?,?,?)";
		final Object[] objects=new Object[]{shenbo.game_name,shenbo.game_type,shenbo.image_game_name,shenbo.image_preview_name,shenbo.sortpriority,shenbo.game_code,shenbo.providercode,shenbo.enable};
		KeyHolder keyHolder = new GeneratedKeyHolder();
		int result=Sp.get().getDefaultJdbc().update(new MyPreparedStatementCreator(sql,objects), keyHolder);
		return result>0;
	}
	
	
	public static List<SbGame> getAll(){
		String sql="SELECT sb_id,game_name,game_type,image_game_name,image_preview_name,sortpriority,game_code,providercode,enable FROM mb_sb_game WHERE  enable =1  order by isnull(sortpriority), sortpriority asc";
		List<SbGame> list=Sp.get().getDefaultJdbc().query(sql,new Object[]{},new SbGameRowMap());
		return list;
	}
	
	
}
