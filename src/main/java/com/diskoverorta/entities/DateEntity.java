package com.diskoverorta.entities;

import com.diskoverorta.osdep.StanfordNLP;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by praveen on 15/10/14.
 */
public class DateEntity implements BaseEntity
{
    public static void main(String args[])
    {
        DateEntity temp = new DateEntity();
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
            for (int i = 0; i < lcl.size(); i++)
            {
                String key = lcl.get(i).get(CoreAnnotations.AnswerAnnotation.class);
                if(key.equals("DATE") == true)
                {
                    String temp1 = "";
                    while(key.equals("DATE") == true)
                    {
                        temp1 = temp1 + lcl.get(i).originalText() + " ";
                        i++;
                        if(i < lcl.size())
                            key = lcl.get(i).get(CoreAnnotations.AnswerAnnotation.class);
                        else
                            break;
                    }
                    temp1 = temp1.trim();
                    entityList.add(temp1);
                }
            }
        }
        return entityList;
    }
}
