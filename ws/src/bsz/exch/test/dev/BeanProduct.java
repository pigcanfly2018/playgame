package bsz.exch.test.dev;

import java.io.File;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.FileResourceLoader;

import bsz.exch.utils.NameUtils;

public class BeanProduct {

	
	public static List removeDuplicateWithOrder(List list) { 
        Set set = new HashSet(); 
        List newList = new ArrayList(); 
        for (Iterator iter = list.iterator(); iter.hasNext();) { 
            Object element = iter.next(); 
            if (set.add(element)) 
                newList.add(element); 
        } 
        return newList; 
    } 
	
	public static void g(String tableName)throws Exception{
		ConnectionDB db = new ConnectionDB();
	
		ResultSetMetaData rsd=db.getMeta(tableName);
		int count=rsd.getColumnCount();
		List<BeanField> fieldList =new ArrayList<BeanField>();
		List<String> classList = new ArrayList<String>();
		
		for(int i=0;i<count;i++){
			BeanField field =new BeanField();
			field.column=rsd.getColumnName(i+1);
			field.type=rsd.getColumnTypeName(i+1);
			field.fieldName=NameUtils.getCamelName(field.column);
			field.fieldName=field.column;
			field.className=GetClassName.get(field.type).getSimpleName();
			fieldList.add(field);
			classList.add(GetClassName.get(field.type).getName());
		}
		classList=removeDuplicateWithOrder(classList);
		String cName=NameUtils.getFirstUpperName(NameUtils.getCamelName(tableName));
		try{
			String root = System.getProperty("user.dir")+File.separator+"src";
			FileResourceLoader resourceLoader = new FileResourceLoader(root,"utf-8");
			Configuration cfg = Configuration.defaultConfiguration();
			GroupTemplate gt = new GroupTemplate(resourceLoader, cfg);
			Template t = gt.getTemplate("beans.bl");
			t.binding("fieldList", fieldList);
			t.binding("classList", classList);
			t.binding("className",NameUtils.getFirstUpperName(NameUtils.getCamelName(tableName)));
			String str = t.render();
			FileUtils.writeStringToFile(new File("D:/work/ws/src/com/product/bojin/beans/"+cName+".java"), str, "utf-8");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void gSql(String tableName)throws Exception{
		ConnectionDB db = new ConnectionDB();
		ResultSetMetaData rsd=db.getMeta(tableName);
		int count=rsd.getColumnCount();
		StringBuffer sb =new StringBuffer();
		for(int i=0;i<count;i++){
			sb.append(rsd.getColumnName(i+1).toLowerCase()+",");
		}
		sb.deleteCharAt(sb.length()-1); 
	    System.out.println("select "+sb.toString()+" from "+tableName.toLowerCase());
	}
	public static void main(String[] args)throws Exception{
		
		g("mb_jbp");

	}
}
