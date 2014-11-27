package com.diskoverorta.utils;

import com.diskoverorta.vo.EntityMap;
import com.diskoverorta.vo.EntityObject;

import java.util.*;

/**
 * Created by praveen on 25/11/14.
 */
public class EntityUtils
{
    public static EntityMap extractEntityMap(EntityObject eObj)
    {
        EntityMap eMap = new EntityMap();
        eMap.personCount = getListFrequency(eObj.person);
        eMap.organizationCount = getListFrequency(eObj.organization);
        eMap.locationCount = getListFrequency(eObj.location);
        eMap.personAlias = getAliasMap(eMap.personCount);
        eMap.organizationAlias = getAliasMap(eMap.organizationCount);
        eMap.locationAlias = getAliasMap(eMap.locationCount);

        return eMap;
    }
    public static Map<String,Integer> getListFrequency(List<String> elist)
    {
        Map<String,Integer> tempMap = new TreeMap<String,Integer>();
        for(int i = 0 ; i < elist.size() ; i++)
        {
            String temp  = elist.get(i).toLowerCase().trim();

            if(tempMap.containsKey(temp))
            {
                Integer val = tempMap.get(temp);
                tempMap.put(temp,val+1);
            }
            else
                tempMap.put(temp,1);
        }
        return tempMap;
    }
    public static Map<String,Set<String>> getAliasMap(Map<String,Integer> eMap)
    {
        Map<String,Set<String>> aliasMap = new TreeMap<String,Set<String>>();
        Set<String> entitySet = eMap.keySet();
        for(String temp : entitySet)
        {
            Set<String> aliasSet = new TreeSet<String>();
            String key = temp;
            for(String temp1 : entitySet)
            {
                if((key.contains(temp1) == true)&&(key.equals(temp1)==false))
                {
                    aliasSet.add(temp1);
                }
                else if((temp1.contains(key) == true)&&(temp1.equals(key)==false))
                {
                    key = temp1;
                    aliasSet.add(key);
                }
            }
            if(aliasMap.containsKey(key) == false)
                aliasMap.put(key,aliasSet);
        }
        return aliasMap;
    }
}
