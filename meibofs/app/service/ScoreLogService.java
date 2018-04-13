package service;

import java.math.BigDecimal;

import models.MyPreparedStatementCreator;

import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import util.Sp;

public class ScoreLogService {
	//rec_id  rec_code  cust_id  login_name  rec_type   score  create_date  create_user
	public static boolean NTcreat(String rec_code,String rec_type,BigDecimal score,String login_name,Long cust_id,String user,BigDecimal cur_score){
  	  final String sql="insert into  mb_score_rec (rec_code,cust_id,login_name,rec_type,score,create_date,create_user,cur_score) values(?,?,?,?,?,now(),?,?)";
  	  final Object[] objects=new Object[]{rec_code,cust_id,login_name,rec_type,score,user,cur_score};
	  KeyHolder keyHolder = new GeneratedKeyHolder();
	  int result=Sp.get().getDefaultJdbc().update(new MyPreparedStatementCreator(sql,objects), keyHolder);
	  return result>0;
    }  

}
