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

public class PtGame {
	public Long  pt_id;
	public String game_name;
	public String game_type;
	public String progressive;
	public String branded;
	public String game_code;
	public String client;
	public String flash;
	public String mobile;
	public String cn_name;
	public String pool_name;
	public Boolean hot;
	public Boolean recommend;
	public Boolean enable;
	
	public static boolean create(PtGame pt){
		String sql="insert into pt_game(game_name,game_type,progressive,branded,game_code,client,flash,mobile,cn_name,pool_name,hot,recommend,enable) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
		final Object[] objects=new Object[]{pt.game_name,pt.game_type,pt.progressive,pt.branded,pt.game_code,pt.client,pt.flash,pt.mobile,pt.cn_name,pt.pool_name,pt.hot,pt.recommend,pt.enable};
		KeyHolder keyHolder = new GeneratedKeyHolder();
		int result=Sp.get().getDefaultJdbc().update(new MyPreparedStatementCreator(sql,objects), keyHolder);
		return result>0;
	}
	
	public static boolean update(PtGame pt){
		String sql="update pt_game set game_name=?,game_type=?,progressive=?,game_code=?,cn_name=?,pool_name=?,hot=?,recommend=?,enable=? where pt_id=?";
		
			int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{pt.game_name,pt.game_type,pt.progressive,pt.game_code,pt.cn_name,pt.pool_name,pt.hot,pt.recommend,pt.enable,pt.pt_id});
			  return count>0;
	}
	
	public static boolean NTdelete(Long pt_id){
		    String sql="delete from pt_game  where pt_id=?";
			final Object[] objects=new Object[]{pt_id};
			int count=Sp.get().getDefaultJdbc().update(sql, objects);
			 return count>0;
	}
	public static PtGame getById(Long id){
		String sql="select * from  pt_game where pt_id=?";
		List<PtGame> list=Sp.get().getDefaultJdbc().query(sql,new Object[]{id},new PtGameRowMap());
		if(list.size()>0){
		    return list.get(0);
		}
		return null;
	}
	public static PtGame getByCode(String code){
		String sql="select * from  pt_game where game_code=?";
		List<PtGame> list=Sp.get().getDefaultJdbc().query(sql,new Object[]{code},new PtGameRowMap());
		if(list.size()>0){
		    return list.get(0);
		}
		return null;
	}
	public static List<PtGame> getAll(){
		String sql="select * from  pt_game order by recommend desc,pt_id asc";
		List<PtGame> list=Sp.get().getDefaultJdbc().query(sql,new Object[]{},new PtGameRowMap());
		return list;
	}
	public static List<PtGame> getAllFlash(){
		String sql="select * from  pt_game where flash='√' order by recommend desc,pt_id asc";
		List<PtGame> list=Sp.get().getDefaultJdbc().query(sql,new Object[]{},new PtGameRowMap());
		return list;
	}
	public static List<PtGame> getAllProgressive(){
		String sql="select * from  pt_game where progressive is not null and flash='√' order by recommend desc, pt_id asc";
		List<PtGame> list=Sp.get().getDefaultJdbc().query(sql,new Object[]{},new PtGameRowMap());
		return list;
	}
	public static List<PtGame> getByGameType(String game_type){
		String sql="select * from  pt_game where game_type=? and flash='√' order by recommend desc, pt_id asc";
		List<PtGame> list=Sp.get().getDefaultJdbc().query(sql,new Object[]{game_type},new PtGameRowMap());
		return list;
	}
	
	public static void impot()throws Exception{
		Workbook rwb = Workbook.getWorkbook(new File("d:/1.xls"));
		Sheet rs = rwb.getSheet(0);
		int rsRows = rs.getRows();
		for(int i=0;i<rsRows;i++){
			Cell[] cell = rs.getRow(i);
			PtGame game =new PtGame();
			game.game_name=cell[0].getContents();
			game.game_type=cell[1].getContents();	
			game.progressive=cell[2].getContents();	
			game.branded=cell[3].getContents();	
			game.game_code=cell[4].getContents();	
			game.client=cell[5].getContents();	
			game.flash=cell[6].getContents();	
			game.mobile=cell[7].getContents();	
			create(game);
		}
		
	}
	
	public static void main(String[]args)throws Exception{
		impot();
		
	}

}
