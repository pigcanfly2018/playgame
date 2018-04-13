package models;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class MgGameRowMap implements RowMapper{

	
	public MgGame mapRow(ResultSet rs, int arg1) throws SQLException {

		MgGame bean=new MgGame();
		
		bean.mg_id = rs.getLong("mg_id");
		bean.Category=rs.getString("Category");
		bean.Game_caegory=rs.getString("Game_caegory");
		bean.Sub_category=rs.getString("Sub_category");
		bean.GameName=rs.getString("GameName");
		bean.CHINESE_SIMP_Game_Name=rs.getString("CHINESE_SIMP_Game_Name");
		bean.CHINESE_TRAD_Game_Name=rs.getString("CHINESE_TRAD_Game_Name");
		bean.Image_File_Name=rs.getString("Image_File_Name");
		bean.PlayCheck=rs.getString("PlayCheck");
		bean.New_Game=rs.getString("New_Game");
		bean.iCafe_Chinese_Simp_zh=rs.getString("iCafe-Chinese_Simp_zh");
		bean.iCafe_Chinese_Trad_zh_tw=rs.getString("iCafe-Chinese_Trad_zh-tw");
		bean.Translations=rs.getString("Translations");
		bean.GameCode=rs.getString("GameCode");
		
		bean.ModuleID = rs.getInt("ModuleID");	
		bean.ClientID = rs.getInt("ClientID");	
		bean.MinBet_CNY = rs.getInt("MinBet-CNY");	
		bean.MaxBet_CNY = rs.getInt("MaxBet-CNY");
		
		bean.Indonesian_id=rs.getString("Indonesian_id");
		bean.Vietnamese_vi=rs.getString("Vietnamese_vi");
		bean.French_fr=rs.getString("French_fr");
		bean.German_de=rs.getString("German_de");
		bean.Greek_el=rs.getString("Greek_el");
		bean.Italian_it=rs.getString("Italian_it");
		bean.Japanese_ja=rs.getString("Japanese_ja");
		bean.Korean_ko=rs.getString("Korean_ko");
		bean.Russian_ru=rs.getString("Russian_ru");
		bean.Spanish_es=rs.getString("Spanish_es");
		bean.Turkish_tr=rs.getString("Turkish_tr");
		bean.Portuguese_pt_br=rs.getString("Portuguese_pt-br");
		bean.HM_game=rs.getString("HM_game");
		bean.temporarily_removed=rs.getString("temporarily_removed");
		
		bean.sortpriority = rs.getInt("sortpriority");
		bean.is_hot = rs.getBoolean("is_hot");
		bean.is_new = rs.getBoolean("is_new");
		bean.enable = rs.getBoolean("enable");
		bean.is_html5 = rs.getBoolean("is_html5");
		
		bean.is_pchtml5 = rs.getBoolean("is_pchtml5");
		bean.pchtml5code=rs.getString("pchtml5code");
		bean.mobilehtml5code=rs.getString("mobilehtml5code");
		
		
		return bean;
	}

}
