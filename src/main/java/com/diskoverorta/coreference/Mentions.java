package com.diskoverorta.coreference;

import com.diskoverorta.vo.EntityObject;
import com.google.common.io.Resources;
import edu.stanford.nlp.dcoref.CorefChain;
import edu.stanford.nlp.dcoref.CorefCoreAnnotations;
import com.diskoverorta.entities.EntityManager;
import com.google.common.base.Charsets;
import com.google.common.io.Files;

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

    public static void getMentions(Set<String> entities ) {
        // read some text in the text variable
        String text = "Teresa h meng founded Atheros communications. Meng served on the board of Atheros. Charles barratt was the CEO of Atheros inc., Barratt and Meng were directors of Atheros.";

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
            List<String> cms_parsed = new ArrayList<String>();

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

//            if(entities.contains(cms_parsed.get(0).trim().toLowerCase())) {
                Set<String> tmp = new HashSet<>();
                for (String element : cms_parsed.subList(1, cms.size())) {
//                    if(entities.contains(element.trim().toLowerCase()))
                    tmp.add(element);
                }
                mp.put(cms_parsed.get(0), tmp);

//            }


//            System.out.println(cms);
//            System.out.println("Mentions:  ");
//            System.out.println(mp);

        }
        System.out.println("Mentions:  ");
        System.out.println(mp);
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
        List<String> entities_raw = en.person;
        entities_raw.addAll(en.organization);

        // process the organisation entity string
//        for(String element : en.organization)
//        {
//            String s = element.split(" ")[0];
//            entities.add(s);
//        }

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
        String content = new String();
        try {
//             URL url = Resources.getResource("/home/Desktop/Kreiger_sample.txt");
//             content = Resources.toString(url, Charsets.UTF_8);
             content = Files.toString(new File("/home/naren/Desktop/Kreiger_sample.txt"), Charsets.UTF_8);
            System.out.println(content);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("File not found");
        }
        getMentions(getEntity(content));
//        getEntity();

    }


}
