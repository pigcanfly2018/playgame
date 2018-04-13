package models;

import java.util.List;

import util.Sp;

public class BbinGame {

	public Long  game_id;
	public String game_name;
	public String game_code;
	public boolean is_hot;
	public boolean is_new;
	public Boolean enable;
	public String Image_File_Name;
	public String game_type;
	public Integer sortpriority;
	public boolean enter_directly;
	
	public static List<BbinGame> getAll(){
		String sql="SELECT game_id,game_name,game_code,is_hot,is_new,enable,Image_File_Name,game_type,sortpriority,enter_directly FROM mb_bbin_game WHERE  enable =1  order by isnull(sortpriority), sortpriority asc";
		List<BbinGame> list=Sp.get().getDefaultJdbc().query(sql,new Object[]{},new BbinGameRowMap());
		return list;
	}

	/*public static boolean NTdelete(Long sb_id){
	    String sql="delete from mb_sb_game  where sb_id=?";
		final Object[] objects=new Object[]{sb_id};
		int count=Sp.get().getDefaultJdbc().update(sql, objects);
		return count>0;
	}
	
	public static boolean update(BbinGame shenbo){
		String sql="update mb_sb_game set game_name=?,game_type=?,image_game_name=?,image_preview_name=?,game_code=?,sortpriority=?,providercode=?,enable=? where sb_id=?";
		
		int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{shenbo.game_name,shenbo.game_type,shenbo.image_game_name,shenbo.image_preview_name,shenbo.game_code,shenbo.sortpriority,shenbo.providercode,shenbo.enable,shenbo.sb_id});
		return count>0;
	}
	
	public static boolean create(BbinGame shenbo){
		String sql="insert into mb_sb_game(game_name,game_type,image_game_name,image_preview_name,sortpriority,game_code,providercode,enable) values(?,?,?,?,?,?,?,?)";
		final Object[] objects=new Object[]{shenbo.game_name,shenbo.game_type,shenbo.image_game_name,shenbo.image_preview_name,shenbo.sortpriority,shenbo.game_code,shenbo.providercode,shenbo.enable};
		KeyHolder keyHolder = new GeneratedKeyHolder();
		int result=Sp.get().getDefaultJdbc().update(new MyPreparedStatementCreator(sql,objects), keyHolder);
		return result>0;
	}
	*/
	
	
}
