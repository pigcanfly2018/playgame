package com.product.bda.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HitMole {

	public String id;
    public String trophy_name;
    public String trophy_code;
    public Integer trophy_count;
    public String trophy_type;
    public  BigDecimal cost;
    public List lis;
    public int rate;
    
    private static List li1 = new ArrayList();
    private static List li2 = new ArrayList();
    private static List li3 = new ArrayList();
    private static List li4 = new ArrayList();
    private static List li5 = new ArrayList();
    private static List li6 = new ArrayList();
    private static List li7 = new ArrayList();
    static{
    	li1.add(0.1);li1.add(0.18);li1.add(0.2);li1.add(0.26);li1.add(0.3);li1.add(0.38);
    	li1.add(0.4);li1.add(0.46);li1.add(0.5);li1.add(0.58);
    	
    	li2.add(0.6);li2.add(0.66);li2.add(0.68);li2.add(0.7);li2.add(0.76);li2.add(0.8);
    	li2.add(0.88);li2.add(0.9);li2.add(0.96);li2.add(1.0);
    	
    	li3.add(1.1);li3.add(1.15);li3.add(1.18);li3.add(1.2);li3.add(1.26);li3.add(1.3);
    	li3.add(1.38);li3.add(1.4);li3.add(1.46);li3.add(1.5);
    	
    	li4.add(1.6);li4.add(1.66);li4.add(1.68);li4.add(1.7);li4.add(1.78);li4.add(1.8);
    	li4.add(1.88);li4.add(1.9);li4.add(1.96);li4.add(2.0);
    	
    	li5.add(2.1);li5.add(2.18);li5.add(2.2);li5.add(2.26);li5.add(2.3);li5.add(2.38);
    	li5.add(1.88);li5.add(1.9);li5.add(1.96);li5.add(2.0);
    	
    	li6.add(2.6);li6.add(2.66);li6.add(2.68);li6.add(2.7);li6.add(2.76);li6.add(2.8);
    	li6.add(2.88);li6.add(2.9);li6.add(2.96);li6.add(3.0);
    	
    	li7.add(3.1);li7.add(3.18);li7.add(3.2);li7.add(3.26);li7.add(3.3);li7.add(3.38);
    	li7.add(3.4);li7.add(3.46);li7.add(3.5);li7.add(3.58);

    }
    
    public static String luckLevel(){
    	Collections.shuffle(li1);
	    return String.valueOf(li1.get(0));
    }
    
    public static String luckLevel1(){
    	List lis = new ArrayList();
    	lis.addAll(li1);
    	lis.addAll(li2);
    	Collections.shuffle(lis);
    	return String.valueOf(lis.get(0));
    }
    public static String luckLevel2(){
    	List lis = new ArrayList();
    	lis.addAll(li1);
    	lis.addAll(li2);
    	lis.addAll(li3);
    	Collections.shuffle(lis);
    	return String.valueOf(lis.get(0));
    }
    public static String luckLevel3(){
    	List lis = new ArrayList();
    	lis.addAll(li1);
    	lis.addAll(li2);
    	lis.addAll(li3);
    	lis.addAll(li4);
    	Collections.shuffle(lis);
    	return String.valueOf(lis.get(0));
    }
    public static String luckLevel4(){
    	List lis = new ArrayList();
    	lis.addAll(li1);
    	lis.addAll(li2);
    	lis.addAll(li3);
    	lis.addAll(li4);
    	lis.addAll(li5);
    	Collections.shuffle(lis);
    	return String.valueOf(lis.get(0));
    }
    public static String luckLevel5(){
    	List lis = new ArrayList();
    	lis.addAll(li1);
    	lis.addAll(li2);
    	lis.addAll(li3);
    	lis.addAll(li4);
    	lis.addAll(li5);
    	lis.addAll(li6);
    	Collections.shuffle(lis);
    	return String.valueOf(lis.get(0));
    }
    
    public static String luckLevel6(){
    	List lis = new ArrayList();
    	lis.addAll(li1);
    	lis.addAll(li2);
    	lis.addAll(li3);
    	lis.addAll(li4);
    	lis.addAll(li5);
    	lis.addAll(li6);
    	lis.addAll(li7);
    	Collections.shuffle(lis);
    	return String.valueOf(lis.get(0));
    }
    public static void main(String[] args){
    	int a=0;
    	int b=0;
    	
    	/*for(int i=0;i<20000;i++){
    		
    		if(Double.compare(Double.parseDouble(luckLevel2().lis.get(0).toString()), Double.parseDouble(1.9+""))==1){
    			a++;
    		}else{
    			b++;
    		}
    	}
    	System.out.println(((float)a)/20000+"==="+((float)b)/20000);*/
    	/*Map<String,Integer> map =new HashMap<String,Integer>();
    	
    	for(int i=0;i<1000000;i++){
    		OpenRedEnvelopes t=luck2();
    		if(map.containsKey(t.id)){
    			map.put(t.id, map.get(t.id)+1);
    		}else{
    			map.put(t.id, 1);
    		} 
    	}
    	
    	for (String key : map.keySet()) {
    		   System.out.println("key= "+ key + " and value= " + ((float)map.get(key))/10000);
      }*/
    	//System.out.println(luckLevel());


    	 /* List arrlist = new ArrayList();
          
          // populate the list
          arrlist.add("A");
          arrlist.add("B");
          arrlist.add("C");  
          
          System.out.println("Initial collection: "+arrlist);
          
          // shuffle the list
          Collections.shuffle(arrlist);
          
          System.out.println("Final collection after shuffle: "+Collections.shuffle(arrlist));*/
    	/*int hongbaocount = 1;
    	for(int i=1;i<hongbaocount;i++){
			//开始插入抽奖结果。
    		System.out.println(1);
    	}*/
    	
    	
    /*	Map<String,Integer> map =new HashMap<String,Integer>();
    	
    	for(int i=0;i<1000000;i++){
    		String t=luckLevel3();
    		if(map.containsKey(t)){
    			map.put(t, map.get(t)+1);
    		}else{
    			map.put(t, 1);
    		} 
    	}
    	
    	for (String key : map.keySet()) {
    		   System.out.println("key= "+ key + " and value= " + ((float)map.get(key))/10000);
      }*/
    	
    }
    


}
