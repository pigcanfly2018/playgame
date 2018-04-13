 package bsz.exch.cache;
 

 import java.io.File;
import java.io.FilenameFilter;
import java.util.Hashtable;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import bsz.exch.core.InterFace;
 
 public class ConfigCache{
	 
   private static final Logger logger = LoggerFactory.getLogger(ConfigCache.class);
   
   private  Hashtable<String,InterFace> funcList =new Hashtable<String,InterFace>();
   
   private File file;
   
   private ConfigCache(){
	   
   }
   private static ConfigCache config = new ConfigCache();
   public static ConfigCache instance(){
	   if(config==null){
		   config = new ConfigCache();
	   }
	   return config;
   }
   
   public void load(File file){
	   File[] files=file.listFiles();
	   for(File f:files){
		   load1(f);
	   }
	   config.file=file;
   }
   
	public  void load1(File path){
		try{
			String[] allfiles = path.list(new ConfigFilenameFilter());
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	        DocumentBuilder db = dbf.newDocumentBuilder(); 
			for (int i = 0; (allfiles != null) && (i < allfiles.length); i++) {
	           Document document = db.parse(path.getPath() +File.separatorChar+ allfiles[i]);
	           logger.info("加载接口文件："+allfiles[i]);
	           NodeList nodeList= document.getElementsByTagName("interface");
	           for(int j=0;j<nodeList.getLength();j++){
	           	Node node=nodeList.item(j);
	            if (node instanceof Element) {
	            	Element e =(Element)node;
	            	NodeList nodeList0=e.getChildNodes();
	            	InterFace inter=parseInterface(nodeList0);
	            	if(inter!=null){
	            		config.funcList.put(inter.getId(), inter);
	            		logger.info("加载接口："+inter.getId());
	            	}
	            }
	           }
			}
		}catch(Exception e){
			logger.error("加载接口文件出现异常："+e.getMessage());
			e.printStackTrace();
		}
		
	}
	
	
	public void reload(){
		config.funcList=new Hashtable<String,InterFace>();
		config.load(config.file);
	}
	
	public InterFace getInterface(String id){
		return config.funcList.get(id);
	}
	
	private  InterFace parseInterface(NodeList nodeList0){
		InterFace inter =new InterFace();
		for(int k=0;k<nodeList0.getLength();k++){
    		Node n =nodeList0.item(k);
    		if(!n.getNodeName().startsWith("#text")){
    			if("id".equals(n.getNodeName())){
    				inter.setId(n.getTextContent());
    			}
    			
    			if("name".equals(n.getNodeName())){
    				inter.setName(n.getTextContent());
    			}
    			
    			
    			if("type".equals(n.getNodeName())){
    				inter.setType(n.getTextContent());
    			}
    			
    			if("service".equals(n.getNodeName())){
    				inter.setService(n.getTextContent());
    			}
    			
    			if("statement".equals(n.getNodeName())){
    				inter.setStatement(n.getTextContent());
    			}
    			
    			if("reqen".equals(n.getNodeName())){
    				String data=n.getTextContent();
    				String[] array=data.split("[|]{1,1}");
    				for(String s:array){
    				  inter.addReqEn(s);
    				}
    			}
    			
    			if("reqcn".equals(n.getNodeName())){
    				String data=n.getTextContent();
    				String[] array=data.split("[|]{1,1}");
    				for(String s:array){
    				  inter.addReqCn(s);
    				}
    			}
    			
    			if("replyen".equals(n.getNodeName())){
    				String data=n.getTextContent();
    				String[] array=data.split("[|]{1,1}");
    				for(String s:array){
    				  inter.addReplyEn(s);
    				}
    			}
    			
    			if("replycn".equals(n.getNodeName())){
    				String data=n.getTextContent();
    				String[] array=data.split("[|]{1,1}");
    				for(String s:array){
    				  inter.addReplyCn(s);
    				}
    			}
    			
    			if("datasource".equals(n.getNodeName())){
    				inter.setDataSource(n.getTextContent());
    			}
    			
    			if("timeout".equals(n.getNodeName())){
    				inter.setTimeout(n.getTextContent());
    			}
    			
    			if("explain".equals(n.getNodeName())){
    				inter.setExplain(n.getTextContent());
    			}
    			
    			if("disabled".equals(n.getNodeName())){
    				inter.setDisabled(n.getTextContent());
    			}
    			

    			if("roles".equals(n.getNodeName())){
    				inter.setRoles(n.getTextContent());
    			}
    			
    			if("product".equals(n.getNodeName())){
    				inter.setProduct(n.getTextContent());
    			}
    		}
    	}
		return inter;
	}

	
	static class ConfigFilenameFilter implements FilenameFilter {
		public boolean accept(File dir, String name) {
			if (name.endsWith("-config.xml")) {
				return true;
			}
			return false;
		}
	}
	
	public static void main(String[] args)throws Exception{
		ConfigCache parser =new ConfigCache();
		//parser.load("interface\\");
	}
   
	
	public Hashtable<String,InterFace> getInterfaces(){
		return config.funcList;
	}

 }
