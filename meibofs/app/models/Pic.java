package models;

import java.util.Date;
import java.util.List;

import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import util.Sp;

public class Pic {
	 public Long pic_id;
     public String pic_data;
     public Date create_date;
     public Long pic_size;
     public String ftype;
     public  Long NTcreat(){
		  final String sql="insert into  mb_pic (pic_data,create_date,pic_size,ftype) values(?,?,?,?)";
		  final Object[] objects=new Object[]{pic_data,create_date,pic_size,ftype};
		  KeyHolder keyHolder = new GeneratedKeyHolder();
		  Sp.get().getDefaultJdbc().update(new MyPreparedStatementCreator(sql,objects), keyHolder);
		  return keyHolder.getKey().longValue();
	 }
 	 public static Pic NTget(Long id){
		String sql="select * from mb_pic where pic_id=? ";
		List<Pic> infos=Sp.get().getDefaultJdbc().query(sql,new Object[]{id},new PicRowMap());
		if(infos.size()>0){
			return infos.get(0);
		}
		return null;
	}
     
     
     
     
}
