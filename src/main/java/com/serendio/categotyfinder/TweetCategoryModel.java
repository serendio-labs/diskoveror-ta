package com.serendio.categotyfinder;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class TweetCategoryModel {
	private Set<String> categories;
	private Map<String,List<String>> dbPediaCategory;
	
	public Set<String> getCategories() {
		return categories;
	}
	public void setCategories(Set<String> categories) {
		this.categories = categories;
	}
	public Map<String, List<String>> getDbPediaCategory() {
		return dbPediaCategory;
	}
	public void setDbPediaCategory(Map<String, List<String>> dbPediaCategory) {
		this.dbPediaCategory = dbPediaCategory;
	}
	
	
}
