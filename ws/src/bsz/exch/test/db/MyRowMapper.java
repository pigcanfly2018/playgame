package bsz.exch.test.db;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.RowMapper;

public class  MyRowMapper<T> implements RowMapper<T>{
	private Class<T> c;
	public MyRowMapper(Class <T>c){
		this.c=c;
	}
	private  ResultSetMetaData  meta;
	private List<RowBean> columnList =new ArrayList<RowBean>();
	
	public T mapRow(ResultSet rs, int index) throws SQLException {
		if(meta==null) {
			meta=rs.getMetaData();
			int size =meta.getColumnCount();
		    Field[] fields = c.getFields();  
			for(int i=0;i<size;i++){
				String columnName=meta.getColumnName(i+1);
				 for (Field field : fields) {  
					 if(StringUtils.equalsIgnoreCase(NameDb.getColumnName(field.getName()), columnName)){
						 RowBean bean =new RowBean();
						 bean.dbfieldname=columnName;
						 bean.field=field;
						 columnList.add(bean);
					 }
				 }
			}
		}
		
		T o=null;
		try {
			 o =c.newInstance();
			 for (RowBean bean : columnList) {  
				    Field field =bean.field; 
	                //修改相应filed的权限  
	                boolean accessFlag = field.isAccessible();  
	                field.setAccessible(true);  
	                Object value = rs.getObject(bean.dbfieldname);  
	                value = value==null?"":value;  
	                setFieldValue(o, field, value);  
	                //恢复相应field的权限  
	                field.setAccessible(accessFlag);  
	            }  
			 
			 
			 
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return o;
	}
	
	 public static void setFieldValue(Object form, Field field, Object value) {  
		  try {  
              field.set(form, (Object) value);  
          } catch (IllegalAccessException e) {  
              e.printStackTrace();  
          }  
	    }  
}
