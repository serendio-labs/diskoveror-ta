package com.serendio.categotyfinder;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import java.net.URL;
import java.net.URLConnection;

import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class DBPediaParser {
	public static final String DBPEDIA_URL="http://lookup.dbpedia.org/api/search.asmx/KeywordSearch?QueryString=";
	public static final String DBPEDIA_CATEGORY_LEBEL="Category";
	public static final String NO_DBPEDIA_CATEGORY_MATCH_FOUND_IN_TWEET = "DBPedia match not Found in Category. Cannot be classified.";
	
	public List<String> getCategoriesFromDBPedia(String keyword){
		List<String> categories=new ArrayList<String>();
		try{
			Document doc =getDocumentFromDBPedia(keyword);
			NodeList nList = doc.getElementsByTagName(DBPEDIA_CATEGORY_LEBEL);
			for (int temp = 0; temp < nList.getLength(); temp++) {				 
				Node nNode = nList.item(temp);		 
				//System.out.println("\nCurrent Element :" + nNode.getNodeName());		 
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {		 
					Element eElement = (Element) nNode;
					String cat=eElement.getElementsByTagName("Label").item(0).getTextContent();
					categories.add(cat);
					//System.out.println("Level : " + cat);							 
				}
			}
		}catch(Exception e){
			//Consume the exception
		}	
		return categories;
	}
	
	
	
	private Document getDocumentFromDBPedia(String keyword)throws Exception{
		URL url = new URL(DBPEDIA_URL+keyword);		
		URLConnection connection = url.openConnection();
		Document doc = parseXML(connection.getInputStream());
		doc.getDocumentElement().normalize();
		return doc;
	}
	
	private Document parseXML(InputStream stream)throws Exception{
		DocumentBuilderFactory objDocumentBuilderFactory = null;
		DocumentBuilder objDocumentBuilder = null;
		Document doc = null;
		try{
		   objDocumentBuilderFactory = DocumentBuilderFactory.newInstance();
		   objDocumentBuilder = objDocumentBuilderFactory.newDocumentBuilder();
           doc = objDocumentBuilder.parse(stream);
        }
        catch(Exception ex){
        	throw ex;
		}       
        return doc;
    }
}
