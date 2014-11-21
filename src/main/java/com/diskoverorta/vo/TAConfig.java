package com.diskoverorta.vo;

import com.diskoverorta.entities.*;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by praveen on 11/11/14.
 */
public class TAConfig
{
    public Map<String,String> analysisConfig = null;
    public Map<String,String> entityConfig = null;
    public Map<String,String> bioentityConfig = null;
    public Map<String,String> outputConfig = null;

    public TAConfig()
    {
        analysisConfig = new TreeMap<String,String>();
        entityConfig = new TreeMap<String,String>();
        bioentityConfig = new TreeMap<String,String>();
        outputConfig = new TreeMap<String,String>();

        analysisConfig.put("Entity","FALSE");

        entityConfig.put("Person","FALSE");
        entityConfig.put("Organization","FALSE");
        entityConfig.put("Location","FALSE");
        entityConfig.put("Date","FALSE");
        entityConfig.put("Time","FALSE");
        entityConfig.put("Currency","FALSE");
        entityConfig.put("Percent","FALSE");
    }
}
