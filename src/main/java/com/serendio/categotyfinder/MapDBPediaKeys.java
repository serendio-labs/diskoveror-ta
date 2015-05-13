package com.serendio.categotyfinder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class MapDBPediaKeys {
	
	private Connection c = null;
	private Statement stmt = null;
    private PreparedStatement pstmt = null;
    
    public Set<String> getMapedCategory(String key,List<String> dbPediaCategories){
		Set<String> listOfKeys= new HashSet<String>();
		listOfKeys.add(key);		
		for(String dbCat:dbPediaCategories){
			String[] dbc=dbCat.trim().split(" ");
			for(int i=0;i<dbc.length;i++){
				listOfKeys.add(dbc[i]);
			}
		}		
		Set<String> result = new HashSet<String>();		
		result = getMapedCategory(listOfKeys);
		if(result.isEmpty()){
			result.add(DBPediaParser.NO_DBPEDIA_CATEGORY_MATCH_FOUND_IN_TWEET);
		}
		return result;
	}
	
	public Set<String> getMapedCategory(Set<String> listOfKeys){
		Set<String> cats= new HashSet<String>();
		for(String key: listOfKeys){
			cats.addAll(getMapedCategory(key));
		}
		return cats;
	}
	
	public Set<String> getMapedCategory(String key){		
		Set<String> cats = new HashSet<String>();
		try {
			Class.forName("org.sqlite.JDBC");
		    c = DriverManager.getConnection("jdbc:sqlite:categoryMap.s3db");
		    c.setAutoCommit(false);
		    
		    pstmt = c.prepareStatement("SELECT * FROM TagCategoryMap Where Tag =?");
		    pstmt.setString(1, key.toLowerCase());
		    
		    ResultSet rs = pstmt.executeQuery();
		    
		    while ( rs.next() ) {		    	
		        String value = rs.getString("Categories");			        
		        String[] cValue= value.split("\\|");		        
		        for(int i=0;i<cValue.length;i++){		        	
		        	cats.add(cValue[i]);
		        }
		    }
		    rs.close();
		    pstmt.close();
		    c.close();
		} catch ( Exception e ) {
		    // Consume Exception			
		}		
		return cats;
	}
	
	public List<String> getMappingCategories(){
		List<String> mapCategory=new ArrayList<String>();		
	    try {
	      Class.forName("org.sqlite.JDBC");
	      c = DriverManager.getConnection("jdbc:sqlite:categoryMap.s3db");
	      c.setAutoCommit(false);
	      
	      stmt = c.createStatement();
	      ResultSet rs = stmt.executeQuery( "SELECT * FROM Category;" );	      
	      while ( rs.next() ) {  
	         mapCategory.add(rs.getString("Type"));        
	      }
	      rs.close();
	      stmt.close();
	      c.close();
	    } catch ( Exception e ) {
	      // Consume Exception
	    }
	    return mapCategory;
	}
}
