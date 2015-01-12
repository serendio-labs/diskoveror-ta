package com.diskoverorta.vo;

/**
 * Created by praveen on 11/11/14.
 */
public class SentenceObject
{
    public String sentenceText = null;
    public EntityObject entities= null;
    public SentenceObject()
    {
        entities = new EntityObject();
    }
}
