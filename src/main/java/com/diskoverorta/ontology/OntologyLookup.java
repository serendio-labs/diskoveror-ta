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
package com.diskoverorta.ontology;



import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by naren on 4/2/15.
 */

public class OntologyLookup
{
    static Connection m_con = null;
    static Statement m_stmt = null;
    static Map<String,Set<String>> m_ontologyMap = new HashMap<>();

    public OntologyLookup()
    {
        establishDBConnection();
        m_ontologyMap.put("events",getTermsFromTable("events"));
        m_ontologyMap.put("topics",getTermsFromTable("topics"));
        closeConnection();
    }
    void establishDBConnection()
    {
        try
        {
            Class.forName("org.postgresql.Driver");
            m_con = DriverManager.getConnection("jdbc:postgresql://" + "178.63.22.132" + "/" + "vipin_test","amc_engineer","serendio123");
            m_stmt = m_con.createStatement();
        }
        catch (ClassNotFoundException ex)
        {
            ex.printStackTrace();
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
    }
    Set<String> getTermsFromTable(String kind)
    {
        Set<String> term_set = new HashSet<>();
        try
        {
            String query = "select id,term,kind from legal_taxonomy where kind ilike '%" + kind + "%'";
            ResultSet res = m_stmt.executeQuery(query);
            while(res.next())
            {
                term_set.add(res.getString("term"));
            }
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        return term_set;
    }
    static void closeConnection()
    {
        try
        {
            m_stmt.close();
            m_con.close();
        }
        catch(SQLException ex)
        {
            ex.printStackTrace();
        }
    }

    public Set<String> matchOntologies(String content, String kind)
    {
        Set<String> result_set = new HashSet<>();
        Set<String> terms_set = m_ontologyMap.get(kind);
        // iterate through the values and match each value with the given sentence
        for (String tmp : terms_set)
        {
            // using regex to match exact words
            Pattern p = Pattern.compile(".*\\b" + tmp.toLowerCase() + "\\b.*");
            Matcher m = p.matcher(content.toLowerCase().trim());
            if (m.matches()) {
                // add the matched terms to the set
                result_set.add(tmp);
            }
         //   if(content.toLowerCase().contains(tmp.toLowerCase()) == true)
         //       result_set.add(tmp);
        }
        return result_set;
    }
    public Map<String,String> getDocumentsForCase(String caseno)
    {
        Map<String,String> docMap = new HashMap<>();
        establishDBConnection();
        try
        {
            Statement stat = m_con.createStatement();
            String query = "select id,path from docs where docket_report_id in (select id from docket_report where case_id="+ caseno +") and source='User'";
            ResultSet res = m_stmt.executeQuery(query);
            String basePath = "/home/serendio/myproject/legalitee/tmp/";
            while(res.next())
            {
                String query2 = "select id from jobs where docs_id ="+res.getString("id");
                ResultSet res1 = stat.executeQuery(query2);

                String jobid="";
                if(res1.next())
                {
                    jobid = res1.getString("id");
                    String temp = res.getString("path");
                    temp = temp.substring(temp.lastIndexOf("/")+1, temp.length());
                    docMap.put(temp,jobid);
                }
            }
            closeConnection();
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        return getFilecontents(docMap);
    }
    Map<String,String> getFilecontents(Map<String,String> fileMap)
    {
        Map<String,String> fileOutMap = new HashMap<String,String>();
        try
        {
            establishDBConnection();
            for(String temp : fileMap.keySet())
            {
                String query = "select trimmed_content from docs_trimmed_contents where job_id="+fileMap.get(temp);
                ResultSet res = m_stmt.executeQuery(query);
                if(res.next())
                {
                    fileOutMap.put(temp,res.getString("trimmed_content"));
                }
            }
            closeConnection();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        return fileOutMap;
    }
}
