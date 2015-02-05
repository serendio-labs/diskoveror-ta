package com.diskoverorta.vo;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Created by praveen on 4/2/15.
 */
public class LegalObject
{
    public String sentenceText = null;
    public EntityObject entities= null;
    public Map<String,String> personAlias = null;
    public Map<String,String> orgAlias = null;
    public Set<String> events = null;
    public Set<String> topics = null;

    public LegalObject()
    {
        entities = new EntityObject();
        personAlias = new TreeMap<>();
        orgAlias = new TreeMap<>();
        events = new TreeSet<>();
        topics = new TreeSet<>();
    }
}
