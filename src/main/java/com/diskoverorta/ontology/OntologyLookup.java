package com.diskoverorta.ontology;

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

    OntologyLookup()
    {
        establishDBConnection();
    }

    void establishDBConnection()
    {
        String ipAddress = null;
        String dbName = null;
        String username = null;
        String password = null;
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


    static Map<String, Set<String>> getTermsFromTable(String kind)
    {
        Map<String, Set<String>> doc_Sentences = new HashMap<>();
        try
        {
            String query = "select id,term,kind from legal_taxonomy where kind ilike '%" + kind + "%'";
            //String q_events = "select id,term,kind from legal_taxonomy where kind ilike '%events%'";

            ResultSet res = m_stmt.executeQuery(query);
//            ResultSet events = m_stmt.executeQuery(q_events);
            Set<String> term_set = new HashSet<>();
            // add the terms into a set
            while(res.next())
            {
                term_set.add(res.getString("term"));
//                doc_Sentences.put(res.getString("kind"),res.getString("term"));
            }
            // put it inside the map
            doc_Sentences.put(kind, term_set);
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        return doc_Sentences;
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

    public static Map<String, Set<String>> getTerms(String content, String term)
    {
        OntologyLookup db = new OntologyLookup();
        db.establishDBConnection();
        Set<String> result = new HashSet<>();
        Map<String, Set<String>> result_map = new HashMap<>();
        Map<String, Set<String>> terms_set =  db.getTermsFromTable(term);
        for(String key : terms_set.keySet()) {
            Set<String> values = terms_set.get(key);
            for (String tmp : values) {
                // using regex to match exact words
                Pattern p = Pattern.compile(".*\\b" + tmp.toLowerCase() + "\\b.*");
                Matcher m = p.matcher(content.toLowerCase().trim());
                if (m.matches()) {
                    result.add(tmp);
                }
            }
            result_map.put(key, result);
        }
        closeConnection();
        return result_map;

    }

    public static void main(String args[])
    {
        OntologyLookup db = new OntologyLookup();
        db.establishDBConnection();
        System.out.println(db.getTermsFromTable("topics"));

    }

}
