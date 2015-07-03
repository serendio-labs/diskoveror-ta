/*******************************************************************************
 *   Copyright 2015 Serendio Inc. ( http://www.serendio.com/ )
 *   Author - Praveen Jesudhas
 *    
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *******************************************************************************/

package com.diskoverorta.coreference;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;


/**
 * Command-line interface to the engine.
 */
public class Duke {
  private static Properties properties;

  public static void main(String[] argv) throws IOException 
  {
	
	      main_(argv);

  }
  
  
// Function to compare two strings
  public static double checkNames(String name1, String name2)
  {
	
  FamilyCommaGivenCleaner fam_obj = new FamilyCommaGivenCleaner();
  String a = fam_obj.clean(name1);
  String b = fam_obj.clean(name2);
  
  LowerCaseNormalizeCleaner low_obj = new LowerCaseNormalizeCleaner();
  String c = low_obj.clean(a);
  String d = fam_obj.clean(b);
  
  
  JaroWinklerTokenized obj = new JaroWinklerTokenized();
  double e = obj.compare(c, d);
  
  return e;
  
  }
  
  public static HashMap<String, Set<String>> listCompare(List allNames, List data)
  {
	
	  /*
	  //String info0 = (String) allNames.get(0);
	  for(Object info0: allNames)
		  System.out.println(info0);
	  
	  for(Object info1: data)
	  {
		  HashMap<String,String> map = (HashMap<String, String>) info1;
		  System.out.println(map.get("status"));
	  }
	  
	  */
	  
	  
	  for (int i = 0; i < allNames.size(); i++) 
	  {
		  for (int j = i+1; j < allNames.size(); j++) 
		  {
			  
			  int flag = 0;
			  int a = i + 1;
			  int b = j + 1;
			  String one = allNames.get(i).toString();
			  String two = allNames.get(j).toString();
			  
			  double result = checkNames(one,two);
			  
			  if ( result > 0.86 )
			  {
				  flag = 1;
			  }
			  
			  if (flag==1)
			  {
				  //System.out.println("Match " + result);
		    	  //System.out.println(one + ", " + two);
				
				  HashMap<String,String> map1 = (HashMap<String, String>) data.get(i); 
				  HashMap<String,String> map2 = (HashMap<String, String>) data.get(j);
				  
				  if(map1.get("status")=="F" && map2.get("status")=="F")
				  {
					  keepCount.gcount = keepCount.gcount + 1;
					  String z = Integer.toString(keepCount.gcount);
					  map1.put("group", z);
					  map1.put("status", "T");
					  map2.put("group", z);
					  map2.put("status", "T");
					  //System.out.println("Updated :" + a + " and " + b );
				  }
				  
				  else if(map1.get("status")=="T" && map2.get("status")=="F")
				  {
					  map2.put("group", map1.get("group"));
					  map2.put("status", "T");
					  //System.out.println("Updated :" + b + " with " + a );
				  }
				  
				  else if(map1.get("status")=="F" && map2.get("status")=="T")
				  {
					  map1.put("group", map2.get("group"));
					  map1.put("status", "T");
					  //System.out.println("Updated :" + a + " with " + b);
				  }
				  
				  System.out.println(" ");  
			  }
		  }
	   }
	  
	  HashMap<String, Set<String>> catchReturn = new HashMap<String, Set<String>>();
	  catchReturn = makeFinalMap(data);
	  return catchReturn;
  }
  
  
public static HashMap<String, Set<String>> makeFinalMap(List sortedData)
  {
	  int c = keepCount.gcount;
	  ArrayList<ArrayList<String>> Groups = new ArrayList<ArrayList<String>>();
      HashMap<String, Set<String>> mapOfList = new HashMap<String, Set<String>>();
      
	  for(int i = 1; i < c + 1; i++)
	  {
		  int j = 0;
		  ArrayList<String> sameGroup = new ArrayList<String>();
		  int countInput = sortedData.size();
		  
		  while (j < countInput)
		  {
			  HashMap<String,String> map4 = (HashMap<String, String>) sortedData.get(j);
			  if(Integer.parseInt(map4.get("group"))==i)
			  {
				  sameGroup.add(map4.get("name"));
			  }
			  j = j + 1;
		  }
		  Groups.add(sameGroup);  
	  }
	  
	  for (ArrayList<String> list1: Groups) 
	    {
		    //System.out.println(" Next Group ");
		    String list2[] = new String[list1.size()];
		    list1.toArray(list2);
		    String longestString = getLongestString(list2);
		    //System.out.format("longest string: '%s'\n", longestString);
		    for (Iterator<String> iter = list1.listIterator(); iter.hasNext(); ) 
		    {
		        String a = iter.next();
		        if (a == longestString) 
		        {
		            iter.remove();
		        }
		    }
		    Set<String> foo = new HashSet<>(list1);
		    mapOfList.put(longestString, foo);
	    }
	  
	  /*
	  System.out.println(" ");
	  System.out.println(" ");
	  System.out.println(" ");
	  System.out.println(" Displaying The Map ");
	  
	  Set setOfKeys = mapOfList.keySet();
	  Iterator iterator = setOfKeys.iterator();
	  
	  while (iterator.hasNext()) 
	  {
		  String key = (String) iterator.next();
		  System.out.println(" ");
		  System.out.println(" ");
		  System.out.println("Entity is : " + key );
		  System.out.println(" ");
		  List matchedList = mapOfList.get(key);
		  int fcount = 0;
		  
		  System.out.println("Related Entities are :" );
		  
		  for(int i = 0; i < matchedList.size(); i++)
		  {
			  fcount = fcount + 1;
			  System.out.println(matchedList.get(i));
		  }
		  System.out.println(" ");
	  }
	  
	  */
	  
	  return mapOfList;
  }
  
  public static String getLongestString(String[] array) {
      int maxLength = 0;
      String longestString = null;
      for (String s : array) {
          if (s.length() > maxLength) {
              maxLength = s.length();
              longestString = s;
          }
      }
      return longestString;
  }
  
  public static void display(List data)
  {
	  System.out.println(" ");
	  int fcount = 0;
	  for(int i = 0; i < data.size(); i++)
	  {
		  fcount = fcount + 1;
		  System.out.println(data.get(i));
	  }
	  System.out.println(fcount);
	  System.out.println(" ");
  }
  
  
  public static class keepCount {
	  public static int gcount=0;
  }
  
  
  // Function to get Coreference
  public static HashMap<String,Set<String>> getCoref(Set<String> entitySet) 
  {
	  keepCount.gcount = 0;
	  List<String> entityList = new ArrayList<String>(entitySet);
	  List<HashMap<String, String>> mapList= new ArrayList<HashMap<String, String>>();
	  HashMap<String, Set<String>> entityMap = new HashMap<String, Set<String>>();
      
      int count = 0;
      
	  for(int i = 0; i < entityList.size(); i++)
	  {
		  HashMap<String, String> map= new HashMap<String, String>();
		  map.put("name", entityList.get(i));
          map.put("status", "F");
          map.put("group", "0");
          mapList.add(count, map);
          count = count + 1;
	  }
	  
	  entityMap = listCompare(entityList,mapList);
	  return entityMap;
	  
  }
  
  
  public static void main_(String[] argv) throws IOException 
  {
	  /*
	  Sample Example with Entity List as exampleNames.csv
	  CSVReader reader = new CSVReader(new FileReader("/home/itachi/Serendio /Duke/duke-1.1/NewTry/names3.csv"));
	  List<String> restAPIbig = new ArrayList<String>();
	  
      String[] row;
      int count = 0;
      
      while ((row = reader.readNext()) != null) 
      {
             restAPIbig.add(row[0]);
             System.out.println(row[0]);
             map.put("name", row[0]);
             map.put("status", "F");
             map.put("group", "0");
             data.add(count, map);
             count = count + 1;
             
      }
	  
    getCoref(restAPIbig);
    
    */
     System.out.println("Total Groups are " + keepCount.gcount);
    
  }
}