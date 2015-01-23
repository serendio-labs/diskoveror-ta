package com.diskoverorta.coreference;

import com.diskoverorta.vo.EntityObject;
import com.google.common.io.Resources;
import edu.stanford.nlp.dcoref.CorefChain;
import edu.stanford.nlp.dcoref.CorefCoreAnnotations;
import com.diskoverorta.entities.EntityManager;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.diskoverorta.utils.WriteToCSV;

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
        // read some text in the text variable
//        String text = "Teresa h meng founded Atheros communications. Meng served on the board of Atheros. Charles barratt was the CEO of Atheros inc., Barratt and Meng were directors of Atheros.";

        // create an empty Annotation just with the given text
        Annotation document = new Annotation(text);

        // run all Annotators on this text
        nlpStanford.pipeline.annotate(document);

        // these are all the sentences in this document
        // a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
        //List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);

        Map<Integer, CorefChain> graph = document.get(CorefCoreAnnotations.CorefChainAnnotation.class);
        Map<String, Set<String>> mp = new HashMap<>();
        for (Integer temp : graph.keySet()) {

            CorefChain c = graph.get(temp);
            System.out.println("ClusterId: " + temp);
            CorefChain.CorefMention cm = c.getRepresentativeMention();

            List<CorefChain.CorefMention> cms = c.getMentionsInTextualOrder();
            List<String> cms_parsed = new ArrayList<>();

            // remove "in Sentences"
            for (CorefChain.CorefMention each : cms) {

                Pattern p = Pattern.compile("\"([^\"]*)\"");
                Matcher m = p.matcher(each.toString());
                while (m.find()) {
//                    System.out.println(m.group(1));
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

//        EntityObject en = entity.getSelectedEntitiesForSentence("Teresa H Meng founded Atheros communications. Meng served on the board of Atheros. Charles barratt was the CEO of Atheros inc., Barratt and Meng were directors of Atheros.", entityConfig);
        EntityObject en = entity.getSelectedEntitiesForSentence(content, entityConfig);
//        System.out.println(en.person);
//        System.out.println(en.organization);
        Set<String> entities = new HashSet<>();

        // Add both person and organisation entity to a list
        List<String> entities_raw = en.person;
        entities_raw.addAll(en.organization);


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

        String content = "Teresa H Meng founded Atheros communications. \nMeng served on the board of Atheros. \nCharles barratt was the CEO of Atheros inc., Barratt and Meng were directors of Atheros.";
        Map<String, Set<String>> mentions_map = getMentions(getEntity(content), content);
        System.out.println("Mentions: ");
        System.out.println(mentions_map);

        //Write to CSV
        WriteToCSV csv = new WriteToCSV();
        try {
            csv.writeMapAsCSV(mentions_map);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Cannot write to CSV");
        }


    }


}
