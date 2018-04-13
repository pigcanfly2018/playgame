package bsz.exch.test.db;

public class SqlBag {
	
	public String sql;
	public Object[] params;
	public String pk;
	
	public SqlBag (String sql,Object[] params,String pk){
		this.sql=sql;
		this.params=params;
		this.pk=pk;
	}
	
	

}
