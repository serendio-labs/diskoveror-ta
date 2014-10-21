package com.diskoverorta.entities;

import com.diskoverorta.osdep.StanfordNLP;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by praveen on 15/10/14.
 */
public class CurrencyEntity implements BaseEntity
{
    public static void main(String args[])
    {
        CurrencyEntity temp = new CurrencyEntity();
        System.out.println(temp.getEntities("Barack Obama is the president of USA"));
    }

    public List<String> getEntities(String sentence)
    {
        StanfordNLP snerTag = new StanfordNLP();
        List<List<CoreLabel>> entityMap =  snerTag.get7NERTaggedOutput(sentence);
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
                if(temp.equals("MONEY") == true)
                    entityList.add(cl.originalText());
            }
        }
        return entityList;
    }
}
