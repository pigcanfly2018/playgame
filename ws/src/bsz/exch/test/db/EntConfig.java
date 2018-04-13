package bsz.exch.test.db;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class EntConfig {

	private Class<?> eclass;

	//默认所有字段
	private List<SqlField> queryFields  = new ArrayList<SqlField>();
	
	//黑名单字段
	private List<SqlField> blackFields  = new ArrayList<SqlField>();
	
	//更新字段
	private List<SqlField> updateFields = new ArrayList<SqlField>();
	
	//条件字段
	private LinkedList<SqlField> whereFields  = new LinkedList<SqlField>();
	
	//排序字段
	private List<SqlField> orderFields  = new ArrayList<SqlField>();
	
	private String pk;
	
	
	public String getPk() {
		return pk;
	}

	private EntConfig(Class<?> eclass){
		this.eclass=eclass;
		pk= NameDb.getPKName(eclass);
	}
	
	public Class getEclass(){
		return eclass;
	}
	
	/**
	 * 创建实例
	 * @param eclass
	 * @return
	 */
	public static EntConfig create(Class<?> eclass){
		EntConfig entity =new EntConfig(eclass);
		Field[] fields =eclass.getFields();
		for(Field field:fields){
			entity.queryFields.add(new SqlField(field.getName(),null,null,SqlField.Query));
		}
		//entity.pk=NameHandler.getPKName(eclass);
		return entity;
	}
	
	/**
	 * 创建实例
	 * @param o
	 * @return
	 */
	public static EntConfig from(Object o){
		EntConfig entity =new EntConfig(o.getClass());
		Field[] fields =o.getClass().getFields();
		for(Field field:fields){
			entity.queryFields.add(new SqlField(field.getName(),null,null,SqlField.Query));
			Object v = get(field,o);
			if(v!=null){
				   entity.updateFields.add(new SqlField(field.getName(),null,new Object[]{v},SqlField.UPDATE));
			}
		}
		return entity;
	}
	
    //加字段入黑名单
	public EntConfig black(String ...fields){
		for(String s:fields){
			this.blackFields.add(new SqlField(s,null,null,SqlField.NONE));
		}
		return this;
	}
	
	//更新字段
	public EntConfig set(String field ,Object o){
		this.updateFields.add(new SqlField(field,null,new Object[]{o},SqlField.UPDATE));
		return this;
	}

	//where
	public EntConfig where(String sql ,Object...o){
		this.whereFields.add(new SqlField(sql,null,o,SqlField.WHERE).link(" where "));
		return this;
	}
	public EntConfig whereId(Long id){
		this.whereFields.add(new SqlField(pk +" = ? ",null,new Object[]{id},SqlField.WHERE).link(" where "));
		return this;
	}
	
	
	//升序
	public EntConfig asc(String field){
		this.orderFields.add(new SqlField(field,"asc",null,SqlField.ORDER));
		return this;
	}
	//降序
	public EntConfig desc(String field){
		this.orderFields.add(new SqlField(field,"desc",null,SqlField.ORDER));
		return this;
	}
	public static Object get(Field f,Object o){
		Object c=null;
		try {
			c=f.get(o);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block 
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return c;
	}

	public List<SqlField> getQueryFields() {
		return queryFields;
	}

	public List<SqlField> getBlackFields() {
		return blackFields;
	}

	public List<SqlField> getUpdateFields() {
		return updateFields;
	}

	public LinkedList<SqlField> getWhereFields() {
		return whereFields;
	}

	public List<SqlField> getOrderFields() {
		return orderFields;
	}
	 
	
	
	
	
	
	
	
	
	
	
	

}
