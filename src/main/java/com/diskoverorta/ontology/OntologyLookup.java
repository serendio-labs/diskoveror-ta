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
    Connection m_con = null;
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
//            String sdoc = Files.toString(new File("dbconnection.prop"), Charset.defaultCharset());
//            String docs[] = sdoc.split("\n");
//            for(int i = 0; i < docs.length; i++)
//            {
//                String options[] = docs[i].split(" : ");
//                if(options[0].equals("Database Address") == true)
//                    ipAddress = options[1];
//                if(options[0].equals("Database Name") == true)
//                    dbName = options[1];
//                if(options[0].equals("Username") == true)
//                    username = options[1];
//                if(options[0].equals("Password") == true)
//                    password = options[1];
//            }
//            if((ipAddress == null) || (dbName == null) || (username == null) || (password == null))
//            {
//                System.out.println("Database details not present.");
//                return;
//            }
            Class.forName("org.postgresql.Driver");
            m_con = DriverManager.getConnection("jdbc:postgresql://" + "178.63.22.132" + "/" + "vipin_test","amc_engineer","serendio123");
            m_stmt = m_con.createStatement();
        }
//        catch (IOException ex)
//        {
//            ex.printStackTrace();
//        }
        catch (ClassNotFoundException ex)
        {
            ex.printStackTrace();
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
    }

    void insertFuzzySets(String sent1No,String sent2No,ArrayList<String> verb1,ArrayList<String> verb2,String simScore)
    {

        String verb1_list = "";
        String verb2_list = "";
        String valid = "false";
        for(int i = 0;(verb1!=null)&&(i < verb1.size());i++)
        {
            if(i == (verb1.size()-1))
                verb1_list = verb1_list + "\""+verb1.get(i)+"\"";
            else
                verb1_list = verb1_list + "\""+verb1.get(i)+"\""+",";
        }
        for(int i = 0;(verb2!=null)&&(i < verb2.size());i++)
        {
            if(i == (verb2.size()-1))
                verb2_list = verb2_list + "\""+verb2.get(i)+"\"";
            else
                verb2_list = verb2_list + "\""+verb2.get(i)+"\""+",";
        }

        if(simScore.length() > 4)
            simScore = simScore.substring(0,3);

        if(Double.parseDouble(simScore) >= 0.5)
            valid = "true";

        verb1_list = verb1_list.replace("'"," ");
        verb2_list = verb2_list.replace("'"," ");



        String query = "insert into fuzzy_group (sentences,sentence1_verbs,sentence2_verbs,similarity_score,ismatch) values('{"+sent1No+","+sent2No+"}','{"+verb1_list+"}','{"+verb2_list+"}',"+simScore+","+valid+")";
        try
        {
            m_stmt.executeUpdate(query);
        }
        catch(SQLException ex)
        {
            ex.printStackTrace();
        }
    }
    void insertFuzzySets1(String sent1No,String sent2No,String simScore)
    {

        String valid = "false";

        if(simScore.length() > 4)
            simScore = simScore.substring(0,3);

        if(Double.parseDouble(simScore) >= 0.5)
            valid = "true";


        String query = "insert into fuzzy_group (sentences,similarity_score) values('{"+sent1No+","+sent2No+"}',"+simScore+")";
        try
        {
            m_stmt.executeUpdate(query);
        }
        catch(SQLException ex)
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
    void closeConnection()
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
        return result_map;

    }

    public static void main(String args[])
    {
        OntologyLookup db = new OntologyLookup();
        db.establishDBConnection();
        System.out.println(db.getTermsFromTable("topics"));

    }

}
