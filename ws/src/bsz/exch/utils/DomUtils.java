package bsz.exch.utils;

import java.io.IOException;

import org.apache.log4j.Logger;

import bsz.exch.game.AginApi;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.ParsingException;

public class DomUtils {
	
	
	private static Logger logger=Logger.getLogger(AginApi.class);
	
	public static Document String2Doc(String xml){
		Builder builder = new Builder();
		Document doc =null;
		try {
			doc = builder.build(xml,null);
		} catch (ParsingException | IOException e) {
			logger.error("xml转换doc出错 - xml="+xml);
			e.printStackTrace();
		}
		return doc;
		
	}

}
