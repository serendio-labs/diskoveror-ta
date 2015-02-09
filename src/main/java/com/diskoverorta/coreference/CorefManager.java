package com.diskoverorta.coreference;


import com.diskoverorta.utils.WriteToCSV;
import com.diskoverorta.vo.TAConfig;
import com.google.common.base.Charsets;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by naren on 28/1/15.
 */
public class CorefManager {

    public static void main(String args[])
    {
     /*   TAConfig config = new TAConfig();
        config.analysisConfig.put("Coref","TRUE");
        config.corefConfig.put("Person","TRUE");
        config.corefConfig.put("Organization", "FALSE");
        config.corefConfig.put("CorefMethod","SUBSTRING");
        String text = "Teresa H Meng founded Atheros communications. Atheros communications is also called as atheros inc. or simply atheros. \nMeng served on the board of Atheros. \nCharles barratt was the CEO of Atheros inc., Barratt and Meng were directors of Atheros.";
        try {
            CorefManager coref = new CorefManager();
            String content = Files.toString(new File("/home/naren/Desktop/kreiger1_trimmed.txt"), Charsets.UTF_8);
            Map<String, Set<String>> coref_map = coref.getCorefForSelectedEntites(content, config.corefConfig);
            for(String temp : coref_map.keySet())
            {
                if(!coref_map.get(temp).isEmpty() && !coref_map.get(temp).contains(temp)) {
                    System.out.println(temp + " : " + coref_map.get(temp));
                }
            }
            WriteToCSV csv = new WriteToCSV();
            csv.writeMapAsCSV(coref_map);
        } catch (IOException e) {
            e.printStackTrace();
        }
*/

    }

    public Map<String,Map<String,Set<String>>> getCorefForSelectedEntites(String content,Set<String> pEntities,Set<String> oEntities, Map<String,String> corefConfig)
    {
        Map<String,Map<String, Set<String>>> coref_map = new HashMap<>();
        //check for NPE
        String CorefMethod = "";
        if(corefConfig.get("CorefMethod") != null) {
            CorefMethod = corefConfig.get("CorefMethod");
        }
        else
        {
            CorefMethod = "SUBSTRING";
        }

        if((corefConfig.get("Person")!=null)&&(corefConfig.get("Person")== "TRUE"))
        {
            PersonCoref coref = new PersonCoref();
            coref_map.put("Person",coref.getPersonCoref(content,pEntities,CorefMethod));
        }
        if((corefConfig.get("Organization")!=null) && (corefConfig.get("Organization")== "TRUE"))
        {
            OrganizationCoref coref = new OrganizationCoref();
            coref_map.put("Organization",coref.getOrganizationCoref(content,oEntities,CorefMethod));
        }
        return coref_map;

    }


}
