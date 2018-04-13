package models;

import java.util.Date;

public class Ad {
    public Long ad_id;
    public String ad_title;
    public String ad_describe;
    public String pic_url;
    public String target_url;
    public Date create_date;
    public Date start_date;
    public Date end_date;
    public String create_user;
    public Boolean available;
    public Integer priority;
}
