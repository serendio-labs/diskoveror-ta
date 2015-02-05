package com.diskoverorta.ontology;

import com.diskoverorta.vo.TAConfig;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by naren on 4/2/15.
 */
public class OntologyManager {

    static OntologyLookup ontology = new OntologyLookup();


    public static void main(String args[])
    {
        TAConfig config = new TAConfig();
        config.ontologyConfig.put("topics","TRUE");
        config.ontologyConfig.put("events","TRUE");
        String content = "Lawyer interrograted the victim as per interrogatories which was Petty offense as the victim had already went" +
                "through a Mental health treatment suggested by Senior Judge. The victim cannot bear or visualize lecture" +
                "given by the Interrogatories. Any how doctor told the nurse to monitor the status. Though the doctor will manage" +
                "to Manufacture the Master Drug. Bankruptcy petition and Bankruptcy trustee are not events.";
        System.out.println(getOntologyForSelectedTerms(content, config.ontologyConfig));

    }
    public static Map<String, Set<String>> getOntologyForSelectedTerms(String content, Map<String, String> ontologyConfig)
    {
        // gets the ontology for given terms like events or topics
        Map<String, Set<String>> ontology_map = new HashMap<>();

        if ((ontologyConfig.get("Topics") != null) && (ontologyConfig.get("Topics") == "TRUE"))
        {
            ontology_map.put("Topics",ontology.matchOntologies(content,"topics"));
        }
        if ((ontologyConfig.get("Events") != null) && (ontologyConfig.get("Events") == "TRUE"))
        {
            ontology_map.put("Events",ontology.matchOntologies(content,"topics"));
        }
        return ontology_map;
    }

}
