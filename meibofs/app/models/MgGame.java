package models;

import java.io.File;
import java.util.Date;
import java.util.List;

import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import util.Sp;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

public class MgGame {
	
	public Long  mg_id;
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
	public Integer sortpriority;
	
	public Boolean is_pchtml5;
	public String pchtml5code;
	public String mobilehtml5code;
	
	public static boolean NTdelete(Long mg_id){
	    String sql="delete from mb_mg_flash_game  where mg_id=?";
		final Object[] objects=new Object[]{mg_id};
		int count=Sp.get().getDefaultJdbc().update(sql, objects);
		return count>0;
	}
	
	public static boolean update(MgGame mg){
		
		if(mg.sortpriority != null && mg.sortpriority.intValue()==0){
			mg.sortpriority = null;
		}
		
		String sql="update mb_mg_flash_game set Category=?,GameName=?,CHINESE_SIMP_Game_Name=?,Image_File_Name=?,GameCode=?,sortpriority=?,is_hot=?,is_new=?,enable=?,is_html5=?,is_pchtml5=?,pchtml5code=?,mobilehtml5code=? where mg_id=?";
		
		int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{mg.Category,mg.GameName,mg.CHINESE_SIMP_Game_Name,mg.Image_File_Name,mg.GameCode,mg.sortpriority,mg.is_hot,mg.is_new,mg.enable,mg.is_html5,mg.is_pchtml5,mg.pchtml5code,mg.mobilehtml5code,mg.mg_id});
		return count>0;
	}
	
	public static boolean create(MgGame mg){
		String sql="insert into mb_mg_flash_game(Category,GameName,CHINESE_SIMP_Game_Name,Image_File_Name,GameCode,sortpriority,is_hot,is_new,enable,is_html5,is_pchtml5,pchtml5code,mobilehtml5code) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
		final Object[] objects=new Object[]{mg.Category,mg.GameName,mg.CHINESE_SIMP_Game_Name,mg.Image_File_Name,mg.GameCode,mg.sortpriority,mg.is_hot,mg.is_new,mg.enable,mg.is_html5,mg.is_pchtml5,mg.pchtml5code,mg.mobilehtml5code};
		KeyHolder keyHolder = new GeneratedKeyHolder();
		int result=Sp.get().getDefaultJdbc().update(new MyPreparedStatementCreator(sql,objects), keyHolder);
		return result>0;
	}
	

}
