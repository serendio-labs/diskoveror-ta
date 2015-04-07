package com.diskoverorta;

import com.diskoverorta.entities.BaseEntity;
import com.diskoverorta.entities.PersonEntity;
import com.diskoverorta.osdep.StanfordNLP;

/**
 * Created by praveen on 15/10/14.
 */
public class DTAEngineExample
{
    public static void main(String args[])
    {
        String exSentence = "Barack Obama is the president of USA";
        PersonEntity ex = new PersonEntity();
        System.out.println("Input Sentence : "+exSentence);
        System.out.println("Person Names : "+ ex.getEntities(new StanfordNLP(),exSentence));
    }
}
