package util;

import java.util.ArrayList;
import java.util.List;

public class Sqler
{
  private String sql;
  private List<Object> params = new ArrayList();

  public Sqler(String sql) { this.sql = (sql + " where 1=1 "); }

  public Sqler and() {
    this.sql += " and ";
    return this;
  }
  public Sqler or() {
    this.sql += " or ";
    return this;
  }
  public Sqler where() {
    this.sql += " where ";
    return this;
  }

  private String genKey() {
    return "params" + (this.params.size() + 1);
  }

  public Sqler equals(String field, Object value)
  {
    this.sql = (this.sql + " " + field + " = ? ");
    this.params.add(value);
    return this;
  }

  public Sqler notEquals(String field, Object value)
  {
    this.sql = (this.sql + " " + field + " <> ? ");
    this.params.add(value);
    return this;
  }

  public Sqler like(String field, Object value)
  {
    this.sql = (this.sql + " " + field + " like ? ");
    this.params.add("%" + value.toString() + "%");
    return this;
  }

  public Sqler bigger(String field, Object value)
  {
    this.sql = (this.sql + " " + field + " > ? ");
    this.params.add(value);
    return this;
  }

  public Sqler ebigger(String field, Object value)
  {
    this.sql = (this.sql + " " + field + " >= ? ");
    this.params.add(value);
    return this;
  }

  public Sqler smaller(String field, Object value)
  {
    this.sql = (this.sql + " " + field + " < ? ");
    this.params.add(value);
    return this;
  }

  public Sqler esmaller(String field, Object value)
  {
    this.sql = (this.sql + " " + field + " <= ? ");
    this.params.add(value);
    return this;
  }
  public Sqler isNull(String field)
  {
    this.sql = (this.sql + " " + field + " is null ");
    return this;
  }
  public Sqler isNotNull(String field)
  {
    this.sql = (this.sql + " " + field + " is not null ");
    return this;
  }
  public Sqler left()
  {
    this.sql += " (";
    return this;
  }

  public Sqler right()
  {
    this.sql += " )";
    return this;
  }
  
  public Sqler addSql(String sql){
	  this.sql += sql;
	  return this;
  }

  public Sqler orderBy(String fields) {
    this.sql = (this.sql + " order by " + fields);
    return this;
  }

  public Sqler orberByDesc(String field) {
    this.sql = (this.sql + " order by " + field + " desc ");
    return this;
  }
  
  public Sqler addGroupBy(String fields) {
	    this.sql = (this.sql + " group by "+ fields+ " ");
	    return this;
  }
  
  public Sqler orberByAsc(String field) {
    this.sql = (this.sql + " order by " + field + " asc ");
    return this;
  }
  public String getSql() {
    return this.sql;
  }
  public List<Object> getParams() {
    return this.params;
  }
}