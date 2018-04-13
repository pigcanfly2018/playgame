package service;

import java.util.List;

import models.RankingList;

public class ListService {
	
	public static List factory(String key){
		if("ranking_list".equals(key)){
			return RankingList.rankingList();
		}
		return null;
	}

}
