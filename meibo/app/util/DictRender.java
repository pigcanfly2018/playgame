package util;

import java.util.Date;
import java.util.List;

import models.Item;

public class DictRender {
	static List<Item> items;
	public String dictName(String groupcode,Object v){
		if(v==null){return "";}
		String value=v.toString();
		if(items==null)items=Item.NTgetAll();
		for(Item item:items){
		  if(item.groupcode.equals(groupcode)){
			  if(item.itemvalue.equals(value.toString())){
				  return item.itemname;
			  }
		  }
		}
		return value.toString();
	}
	public String date(Date date,String formate){
		if(date==null){return "";}
		return DateUtil.dateToString(date, formate);
	}
}
