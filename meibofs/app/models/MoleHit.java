package models;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import util.Sp;
public class MoleHit {
    public Long id;
    public String login_name;
    public Long cust_id;
    public Date create_date;
    public int hitcount;
    

  
  public static List<MoleHit> getHitInfo(){
		 String sql="select * from mb_molehit_active";
 	 List<MoleHit> giftList= Sp.get().getDefaultJdbc().query(sql, new MoleHitRowMap());
 	 
 	 return giftList;
}



}

