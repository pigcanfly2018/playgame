package models;

import java.util.List;

import util.Sp;

public class PpGame {

	public Long  game_id;
	public String game_name;
	public String game_type;
	public String Image_File_Name;
	public String sortpriority;
	public String game_code;
	public Boolean is_hot;
	public Boolean is_new;
	public Boolean enable;
	
	
	public static List<PpGame> getAll(){
		String sql="SELECT game_id,game_name,game_code,is_hot,enable,Image_File_Name,game_type,is_new,sortpriority FROM mb_pp_game WHERE  enable =1  order by isnull(sortpriority), sortpriority asc";
		List<PpGame> list=Sp.get().getDefaultJdbc().query(sql,new Object[]{},new PpGameRowMap());
		return list;
	}
	
}
