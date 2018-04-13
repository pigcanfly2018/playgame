package models;

import java.util.Date;

import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import util.Sp;

public class Notice {
    public Long notice_id;
    public String title;
    public String content;
    public Date start_date;
    public Date end_date;
    public String create_user;
    public Date create_date;
    public Integer priority;
    public Boolean available;
 
}

