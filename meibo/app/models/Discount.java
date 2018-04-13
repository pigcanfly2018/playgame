package models;

import java.io.Serializable;
import java.util.Date;

public class Discount implements Serializable{
	 public Long discount_id;
     public String b_url;
     public String s_url;
     public String title;
     public String content;
     public Boolean available;
     public Date create_date;
     public String create_user;
     public String summary;
     public Integer priority;
     public Boolean is_hot;
     
	 
}
