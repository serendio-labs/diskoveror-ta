package com.diskoverorta.coreference;

import com.diskoverorta.vo.EntityObject;
import com.google.common.io.Resources;
import edu.stanford.nlp.dcoref.CorefChain;
import edu.stanford.nlp.dcoref.CorefCoreAnnotations;
import com.diskoverorta.entities.EntityManager;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.diskoverorta.utils.WriteToCSV;
import com.diskoverorta.utils.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.diskoverorta.osdep.StanfordNLP;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.util.CoreMap;

/**
 * Created by naren on 21/1/15.
 */
public class Mentions {

    static StanfordNLP nlpStanford = new StanfordNLP();

    public static Map<String, Set<String>> getMentions(Set<String> entities,String text) {

        // create an empty Annotation just with the given text
        Annotation document = new Annotation(text);

        // run all Annotators on this text
        nlpStanford.pipeline.annotate(document);

        Map<Integer, CorefChain> graph = document.get(CorefCoreAnnotations.CorefChainAnnotation.class);
        Map<String, Set<String>> mp = new HashMap<>();
        for (Integer temp : graph.keySet()) {

            CorefChain c = graph.get(temp);
            CorefChain.CorefMention cm = c.getRepresentativeMention();

            List<CorefChain.CorefMention> cms = c.getMentionsInTextualOrder();

            List<String> cms_parsed = new ArrayList<>();

            // remove "in Sentences"
            for (CorefChain.CorefMention each : cms) {

                Pattern p = Pattern.compile("\"([^\"]*)\"");
                Matcher m = p.matcher(each.toString());
                while (m.find()) {
                    cms_parsed.add(m.group(1));
                }
            }

            //put it into a map container.
            Set<String> tmp = new HashSet<>();
            if(entities.contains(cms_parsed.get(0).trim().toLowerCase())) { //filters out the key that matches entity

                for (String element : cms_parsed.subList(1, cms.size())) {
//                    if(entities.contains(element.trim().toLowerCase()))   //filters out the values that matches entity
                    tmp.add(element);
                }
                mp.put(cms_parsed.get(0), tmp);    //take the first element as key and rest (tmp) as values

            }

        }

        return mp;
    }

    public static Set<String> getEntity(String content) {

        EntityManager entity = new EntityManager();
        Map<String,String> entityConfig = new HashMap<>();

        entityConfig.put("Person","TRUE");
        entityConfig.put("Organization","TRUE");

        EntityObject en = entity.getSelectedEntitiesForSentence(content, entityConfig);
        Set<String> entities = new HashSet<>();

        // Add both person and organisation entity to a list
        List<String> entities_raw = en.organization;
//        entities_raw.addAll(en.organization);


        //trim and lower case
        for(String element : entities_raw)
        {
            entities.add(element.trim().toLowerCase());

        }
        System.out.println("Total entities" + entities);
       return entities;

    }

    public static void main(String args[])
    {

//        String content = "Teresa H Meng founded Atheros communications. \nMeng served on the board of Atheros. \nCharles barratt was the CEO of Atheros inc., Barratt and Meng were directors of Atheros.";


        //Write to CSV
        WriteToCSV csv = new WriteToCSV();
        try {

//            URL url = Resources.getResource("/home/Desktop/Kreiger_sample.txt");
//            String content = Resources.toString(url, Charsets.UTF_8);
            String content = Files.toString(new File("/home/naren/Desktop/kreiger1_trimmed.txt"), Charsets.UTF_8);
            Map<String, Set<String>> mentions_map = getMentions(getEntity(content), content);

            // Using sub-string methods from utils and passing only entity set
//            EntityUtils alias = new EntityUtils();
//            Set<String> en_set = getEntity(content);
//            Map<String, Set<String>> mentions_map = alias.getAliasMapFromSet(en_set);

            System.out.println("Mentions: ");
            //System.out.println(mentions_map);
            for(String temp : mentions_map.keySet())
            {
                if(!mentions_map.get(temp).isEmpty() && !mentions_map.get(temp).contains(temp)) {
                    System.out.println(temp + " : " + mentions_map.get(temp));
                }
            }

            csv.writeMapAsCSV(mentions_map);
        }
        catch (IOException e) {
            e.printStackTrace();
            System.out.println("Cannot write to CSV");
        }


    }


}
