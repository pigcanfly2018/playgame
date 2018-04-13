package util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TreeNode{
	public String id;
	public String text;
	public String cls;
	public Boolean expanded=false;
	public Boolean  leaf;
	public Boolean checked;
	public List<TreeNode>children;
	public String url;
	public String code;
	public Map<String,Object>attributes=new HashMap<String,Object>();
	

}
