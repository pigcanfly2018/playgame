package bsz.exch.test.db;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import bsz.exch.dbeans.WsUser;

public class SqlBuild {
	
	public static NameDb nameDb=new NameDb();

	public static SqlBag insert(EntConfig config){
		String pk=nameDb.getPKName(config.getEclass());
		String sql="insert into "+nameDb.getTableName(config.getEclass());
		List<Object> params =new ArrayList<Object>();
		StringBuilder sb0 =new StringBuilder();
		StringBuilder sb1 =new StringBuilder();
		sb0.append("(");
		sb1.append("(");
		List<SqlField> fields =config.getUpdateFields();
		if(fields.size()>0){
			for(SqlField field:fields){
				if(!StringUtils.equalsIgnoreCase(nameDb.getColumnName(field.name), pk)){
					sb0.append(nameDb.getColumnName(field.name)+",");
					sb1.append("?,");
					params.add(field.values[0]);
				}
			}
			sb0.deleteCharAt(sb0.length() - 1);
			sb1.deleteCharAt(sb1.length() - 1);
		}
		sb0.append(") ");
		sb1.append(") ");
		sql=sql+sb0.toString()+"values "+sb1.toString();
		return new SqlBag(sql,params.toArray(),pk);
	}
	
	public static SqlBag update(EntConfig config){
		String pk=nameDb.getPKName(config.getEclass());
		String sql="update "+nameDb.getTableName(config.getEclass());
		List<Object> params =new ArrayList<Object>();
		StringBuilder sb0 =new StringBuilder(" set ");
		List<SqlField> fields =config.getUpdateFields();
		if(fields.size()>0){
			for(SqlField field:fields){
				if(!StringUtils.equalsIgnoreCase(nameDb.getColumnName(field.name), pk)){
					sb0.append(nameDb.getColumnName(field.name)+"=?,");
					params.add(field.values[0]);
				}
			}
			sb0.deleteCharAt(sb0.length() - 1);
		}
		List<SqlField> wheres =config.getWhereFields();
		if(wheres.size()>0){
			for(SqlField field:wheres){
				if(field.type==SqlField.WHERE){
					sb0.append(" where "+nameDb.getColumnName(field.name));
					for(Object o :field.values){
						params.add(o);
					}
				}
			}
		}else{
			for(SqlField field:fields){
				if(StringUtils.equalsIgnoreCase(nameDb.getColumnName(field.name), pk)){
					sb0.append(" where "+nameDb.getColumnName(field.name)+"=?");
					params.add(field.values[0]);
				}
			}
		}
		sql=sql+sb0.toString();
		return new SqlBag(sql,params.toArray(),pk);
	}
	

	public static SqlBag query(EntConfig config){
		String pk=nameDb.getPKName(config.getEclass());
		String sql="select ";
		List<SqlField> queryList=config.getQueryFields();
		List<SqlField> blackList=config.getBlackFields();
		List<Object> params =new ArrayList<Object>();
		
		StringBuilder sb0 =new StringBuilder();
		if(queryList.size()>0){
			for(SqlField sf:queryList){
				boolean fitter =false;
				for(SqlField sf0:blackList){
					if(StringUtils.equalsIgnoreCase(sf0.name, sf.name)){
						fitter=true;
						break;
					}
				}
				if(!fitter){
					sb0.append(nameDb.getColumnName(sf.name)+",");
				}
			}
			sb0.deleteCharAt(sb0.length() - 1);
		}
		sql=sql+sb0.toString() +" from "+nameDb.getTableName(config.getEclass());
		
		sb0=new StringBuilder();
		List<SqlField> wheres =config.getWhereFields();
		if(wheres.size()>0){
			for(SqlField field:wheres){
				if(field.type==SqlField.WHERE){
					sb0.append(" where "+nameDb.getColumnName(field.name));
					for(Object o :field.values){
						params.add(o);
					}
				}
			}
		}
		sql=sql+sb0.toString();
		
		List<SqlField> orders =config.getOrderFields();
		if(orders.size()>0){
			sql=sql+" order by ";
			for(SqlField sf :orders){
				sql=sql+nameDb.getColumnName(sf.name)+" "+sf.op;
			}
		}else{
			sql=sql+" order by "+pk+" desc ";
		}
		
		return new SqlBag(sql,params.toArray(),pk);
	}
	
	public static SqlBag delete(EntConfig config){
		String pk=nameDb.getPKName(config.getEclass());
		String sql="delete from "+nameDb.getTableName(config.getEclass())+" ";
		List<SqlField> queryList=config.getQueryFields();
		List<SqlField> blackList=config.getBlackFields();
		List<Object> params =new ArrayList<Object>();
		
		StringBuilder sb0=new StringBuilder();
		List<SqlField> wheres =config.getWhereFields();
		if(wheres.size()>0){
			for(SqlField field:wheres){
				if(field.type==SqlField.WHERE){
					sb0.append(" where "+nameDb.getColumnName(field.name));
					for(Object o :field.values){
						params.add(o);
					}
				}
			}
		}
		sql=sql+sb0.toString();
		
		return new SqlBag(sql,params.toArray(),pk);
	}
	
	public static void main(String []args){
		EntConfig config= EntConfig.create(WsUser.class).set("UserName", "aa").set("UserPwd", "tuu");
		WsUser user =new WsUser();
		user.userName="uououo";
		user.userPwd="pwd";
		user.wsUserId=1L;
		
		EntConfig config0= EntConfig.create(WsUser.class).set("UserName", "aa").set("UserPwd", "tuu");
		config0=config0.where("userName=? and user_pwd =? ","44","dd");
		
		SqlBag  sb =SqlBuild.update(config0);
		
		System.out.println(SqlBuild.insert(config).sql);
		System.out.println(SqlBuild.insert(EntConfig.from(user)).sql);
		
		System.out.println(SqlBuild.update(EntConfig.from(user)).sql);
		System.out.println(SqlBuild.update(config0).sql+" | "+sb.params.length);
		
		System.out.println(SqlBuild.query(config0).sql+" | "+SqlBuild.query(config0).params.length);
		System.out.println(SqlBuild.delete(config0).sql+" | "+SqlBuild.delete(config0).params.length);
		
	}
	

}
