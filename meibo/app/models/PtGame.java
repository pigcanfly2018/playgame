package models;

import java.util.List;

import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import util.Sp;

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
	
	public static boolean create(PtGame pt){
		String sql="insert into pt_game(game_name,game_type,progressive,branded,game_code,client,flash,mobile) values(?,?,?,?,?,?,?,?)";
		final Object[] objects=new Object[]{pt.game_name,pt.game_type,pt.progressive,pt.branded,pt.game_code,pt.client,pt.flash,pt.mobile};
		KeyHolder keyHolder = new GeneratedKeyHolder();
		int result=Sp.get().getDefaultJdbc().update(new MyPreparedStatementCreator(sql,objects), keyHolder);
		return result>0;
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
		String sql="select * from  pt_game where flash='√' and enable is true order by recommend desc,pt_id asc";
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
	
	
	
	public static void main(String[]args)throws Exception{
		//impot();
		
	}

}
