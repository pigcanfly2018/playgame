package util;


public class PageUtil {

	public static String page(String sql,int start,int limit){
		
		/**
		 * oracle ----
	
		StringBuffer pgsql = new StringBuffer(" select * from ( ");
		pgsql.append(" select t.* ,rownum num from ( ");
		pgsql.append(sql);
		pgsql.append("　) t where rownum <= " + (start+limit));
		pgsql.append(" ) where　num > " + start);
		*/
		StringBuffer pgsql = new StringBuffer("");
		pgsql.append(sql);
		pgsql.append(" limit "+limit +" offset "+start);
		System.out.println(pgsql.toString());
		return pgsql.toString();
	}
	
	public static boolean blank(String key){
		if(key==null)return true;
		if(key.trim().equals("")) return true;
		return false;
	}
	

	public static void main(String []args){
		System.out.println(page("select * from jb_user",0,2));
	}
}
