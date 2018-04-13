package util;

import java.util.ArrayList;
import java.util.List;


public class MyTreeUtil {
	
	/**
	 * 往上升成一棵树
	 */
	public static TreeNode getTreeUp(TreeNode node,MyTree tree){
		MyTree parent=(MyTree)tree.getParent();
		node.expanded=true;
		if(parent ==null){
			 return  node;
		}else{
			TreeNode n =parent.convertNode();
			List<TreeNode> childs =new ArrayList<TreeNode>();
			childs.add(node);
			n.children=childs;
			return getTreeUp(n,parent);
		}
	}
	
	public static void getModelTree(TreeNode node,MyTree tree,boolean expAll,boolean checkAll){
		if(tree.isLeaf()){
			return;
		}else{
			List<TreeNode> childs =new ArrayList<TreeNode>();
			List<MyTree> nodess= tree.getChilds(false);
			for(MyTree tn:nodess){
				TreeNode n=tn.convertNode();
               if(checkAll){
					n.checked=false;
				}
               if(expAll){
            	   n.expanded=true;
               }
				childs.add(n);
				
				getModelTree(n,tn,expAll,checkAll);
			}
			node.children=childs;
		}
	}
	public static void getModelTree(TreeNode node,MyTree tree){
		if(tree.isLeaf()){
			return;
		}else{
			List<TreeNode> childs =new ArrayList<TreeNode>();
			List<MyTree> nodess= tree.getChilds(false);
			for(MyTree tn:nodess){
				TreeNode n=tn.convertNode();
				childs.add(n);
				getModelTree(n,tn);
			}
			node.children=childs;
		}
	}
	public static void getModelTree(TreeNode node,MyTree tree,TreeHandler handler){
		if(tree.isLeaf()){
			return;
		}else{
			List<TreeNode> childs =new ArrayList<TreeNode>();
			List<MyTree> nodess= tree.getChilds(false);
			for(MyTree tn:nodess){
				TreeNode n=tn.convertNode();
				handler.handle(tn, n);
				childs.add(n);
				getModelTree(n,tn,handler);
			}
			node.children=childs;
		}
	}
	
	public static void getModelTree2(TreeNode node,MyTree tree,TreeHandler handler,boolean includeBut){
		if(tree.isLeaf()){
			return;
		}else{
			List<TreeNode> childs =new ArrayList<TreeNode>();
			List<MyTree> nodess= tree.getChilds(includeBut);
			for(MyTree tn:nodess){
				TreeNode n=tn.convertNode();
				handler.handle(tn, n);
				childs.add(n);
				getModelTree2(n,tn,handler,includeBut);
			}
			node.children=childs;
		}
	}

	
	
	
	
}
