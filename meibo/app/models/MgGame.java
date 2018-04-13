package models;

import java.io.File;
import java.util.Date;
import java.util.List;

import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import util.Sp;

public class MgGame {
	
	public String Category;
	public String Game_caegory;
	public String Sub_category;
	public String GameName;
	public String CHINESE_SIMP_Game_Name;
	public String CHINESE_TRAD_Game_Name;
	public String Image_File_Name;
	public String PlayCheck;
	public String New_Game;
	public String iCafe_Chinese_Simp_zh;
	public String iCafe_Chinese_Trad_zh_tw;
	public String Translations;
	public String GameCode;
	
	public Integer ModuleID;
	public Integer ClientID;
	public Integer MinBet_CNY;
	public Integer MaxBet_CNY;
	
	public String Indonesian_id;
	public String Vietnamese_vi;
	public String French_fr;
	public String German_de;
	public String Greek_el;
	public String Italian_it;
	public String Japanese_ja;
	public String Korean_ko;
	public String Russian_ru;
	public String Spanish_es;
	public String Turkish_tr;
	public String Portuguese_pt_br;
	public String HM_game;
	public String temporarily_removed;
	
	public Boolean is_hot;
	public Boolean is_new;
	public Boolean enable;
	public Boolean is_html5;
	public Boolean is_pchtml5;
	public String pchtml5code;
	public String mobilehtml5code;
	
	
	public static List<MgGame> getAll(){
		String sql="SELECT * FROM mb_mg_flash_game WHERE  enable =1  order by isnull(sortpriority), sortpriority asc";
		List<MgGame> list=Sp.get().getDefaultJdbc().query(sql,new Object[]{},new MgGameRowMap());
		return list;
	}
	

}
