package com.diskoverorta;

import com.diskoverorta.entities.BaseEntity;
import com.diskoverorta.entities.PersonEntity;

/**
 * Created by praveen on 15/10/14.
 */
public class DTAEngineExample
{
    public static void main(String args[])
    {
        String exSentence = "Barack Obama is the president of USA";
        BaseEntity ex = new PersonEntity();
        System.out.println("Input Sentence : "+exSentence);
        System.out.println("Person Names : "+ ex.getEntities(exSentence));
    }
}
