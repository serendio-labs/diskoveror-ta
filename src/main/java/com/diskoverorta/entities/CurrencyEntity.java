package com.diskoverorta.entities;

import com.diskoverorta.osdep.StanfordNERTagger;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by praveen on 15/10/14.
 */
public class CurrencyEntity implements BaseEntity
{
    public List<String> getEntities(String sentence)
    {
        StanfordNERTagger snerTag = new StanfordNERTagger();
        List<List<CoreLabel>> entityMap =  snerTag.getNERTaggedOutput(sentence);
        return getEntities(entityMap);
    }

    public List<String> getEntities(List<List<CoreLabel>> sentTags)
    {
        List<String> entityList = new ArrayList<String>();
        for (List<CoreLabel> lcl : sentTags)
        {
            for (CoreLabel cl : lcl)
            {
                String temp = cl.get(CoreAnnotations.AnswerAnnotation.class);
                if(temp.equals("CURRENCY") == true)
                    entityList.add(cl.originalText());
            }
        }
        return entityList;
    }
    public static void main(String args[])
    {
        CurrencyEntity temp = new CurrencyEntity();
        System.out.println(temp.getEntities("Barack Obama is the president of USA"));
    }
}
