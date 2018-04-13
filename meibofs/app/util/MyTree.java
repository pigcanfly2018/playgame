package util;

import java.util.List;


public interface MyTree <T>{
	
	/**
	 * 是否为叶子节点
	 * @return
	 */
	public boolean isLeaf();
	/**
	 * 转化成node的方法
	 * @return
	 */
	public TreeNode convertNode();
	
	/**
	 * 获取childs
	 * @return
	 */
	public List<T> getChilds(boolean includeBut);
	
    
	public T getParent();
	
}
