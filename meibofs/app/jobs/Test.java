package jobs;

import util.MD5;

public class Test {
	
	public static void main(String[] args){
		System.out.println(MD5.md5("a123b789"));
		String url="<url>http://bank.example.com/pay.do?param1=v1&param2=v2...........</url>";
		System.out.println(url.replaceAll("<url>", "").replaceAll("</url>", ""));
	}

}
