package com.diskoverorta.vo;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Created by praveen on 25/11/14.
 */
public class EntityMap
{
    public Map<String,Integer> personCount = null;
    public Map<String,Integer> organizationCount = null;
    public Map<String,Integer> locationCount = null;

    public Map<String,Set<String>> personAlias = null;
    public Map<String,Set<String>> organizationAlias = null;
    public Map<String,Set<String>> locationAlias = null;

    public EntityMap()
    {
        personCount = new TreeMap<String,Integer>();
        organizationCount = new TreeMap<String,Integer>();
        locationCount = new TreeMap<String,Integer>();

        personAlias = new TreeMap<String,Set<String>>();
        organizationAlias = new TreeMap<String,Set<String>>();
        locationAlias = new TreeMap<String,Set<String>>();
    }
}
