package models;

import java.util.Date;

import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import util.Sp;

public class Bank {
	public Long bank_id;
    public String bank_name;
    public String account;
    public String account_name;
    public String bank_dot;
    public Integer cust_level;
    public String remarks;
    public String create_user;
    public Date create_date;

}
