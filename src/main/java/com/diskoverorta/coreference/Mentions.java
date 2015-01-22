package com.diskoverorta.coreference;

import com.diskoverorta.vo.EntityObject;
import edu.stanford.nlp.dcoref.CorefChain;
import edu.stanford.nlp.dcoref.CorefCoreAnnotations;
import com.diskoverorta.entities.EntityManager;

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

    public static void getMentions(List<String> entities ) {
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
        for (Integer temp : graph.keySet()) {

            CorefChain c = graph.get(temp);
            System.out.println("ClusterId: " + temp);
            CorefChain.CorefMention cm = c.getRepresentativeMention();

            List<CorefChain.CorefMention> cms = c.getMentionsInTextualOrder();
            List<String> cms_parsed = new ArrayList<String>();

            // remove "in Sentences"
            for (CorefChain.CorefMention each : cms) {
//
//                String s = each.toString().split("\"")[0];
//                cms_parsed.add(s);

//                System.out.println(each.toString());
                Pattern p = Pattern.compile("\"([^\"]*)\"");
                Matcher m = p.matcher(each.toString());
                while (m.find()) {

//                    System.out.println(m.group(1));
                    cms_parsed.add(m.group(1));
                }
            }
            

            Map<String, Set<String>> mp = new HashMap<>();
            Set tmp = new HashSet();
            for (String element : cms_parsed.subList(1, cms.size())) {
                tmp.add(element);
            }
            mp.put(cms_parsed.get(0), tmp);

            System.out.println("Mentions:  ");
//            System.out.println(cms);
            System.out.println(mp);
            System.out.println(" ");
        }
    }

    public static List<String> getEntity() {

        EntityManager entity = new EntityManager();
        Map<String,String> entityConfig = new HashMap<>();
        entityConfig.put("Person","TRUE");
        entityConfig.put("Organization","TRUE");

        EntityObject en = entity.getSelectedEntitiesForSentence("Teresa H Meng founded Atheros communications. Meng served on the board of Atheros. Charles barratt was the CEO of Atheros inc., Barratt and Meng were directors of Atheros.", entityConfig);
        System.out.println(en.person);
        System.out.println(en.organization);
        List<String> entities = en.person;
        entities.addAll(en.organization);
        // process the organisation entity string
//        for(String element : en.organization)
//        {
//            String s = element.split(" ")[0];
//            entities.add(s);
//        }

        System.out.println("Total entities" + entities);
        return entities;

    }

    public static void main(String args[])
    {
        getMentions(getEntity());

    }


}
