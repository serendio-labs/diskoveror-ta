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
    static Map<String,Set<String>> m_ontologyMap = new HashMap<>();

    OntologyLookup()
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
        }
        return result_set;
    }

}
