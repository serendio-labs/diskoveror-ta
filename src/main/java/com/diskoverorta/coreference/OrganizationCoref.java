package com.diskoverorta.coreference;

import com.diskoverorta.entities.EntityManager;
import com.diskoverorta.osdep.StanfordNLP;
import com.diskoverorta.utils.EntityUtils;
import com.diskoverorta.vo.EntityObject;
import edu.stanford.nlp.dcoref.CorefChain;
import edu.stanford.nlp.dcoref.CorefCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by naren on 28/1/15.
 */
public class OrganizationCoref {
    static StanfordNLP nlpStanford = new StanfordNLP();

    public static Set<String> getOrganizationEntity(String content) {

        EntityManager entity = new EntityManager();
        Map<String,String> entityConfig = new HashMap<>();

        entityConfig.put("Person","TRUE");
        entityConfig.put("Organization","TRUE");

        EntityObject en = entity.getSelectedEntitiesForSentence(content, entityConfig);
        Set<String> entities = new HashSet<>();
        // Add Organization entity to a list
        List<String> entities_raw = en.organization;
        //trim and lower case
        for(String element : entities_raw)
        {
            entities.add(element.trim().toLowerCase());
        }
        System.out.println("Organization entities : " + entities);
        return entities;

    }

    public static Map<String, Set<String>> getMentions(String text) {

        Set<String> entities =  getOrganizationEntity(text);

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

    public Map<String, Set<String>> getSubStringAlias(String content){


        // Using sub-string methods from utils and passing only entity set
        EntityUtils alias = new EntityUtils();
        Set<String> en_set = getOrganizationEntity(content);
        Map<String, Set<String>> mentions_map = alias.getAliasMapFromSet(en_set);
        return mentions_map;

    }

    public Map<String, Set<String>> getOrganizationCoref(String content, String corefMethod) {

        if( corefMethod == "SUBSTRING")
            return getSubStringAlias(content);
        else
            return getMentions(content);

    }
}
