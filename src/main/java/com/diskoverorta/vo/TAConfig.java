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
    public Map<String,String> sentimentConfig = null;
    public Map<String,String> bioentityConfig = null;
    public Map<String,String> outputConfig = null;
    public Map<String,String> corefConfig = null;
    public Map<String,String> ontologyConfig = null;

    public TAConfig()
    {
        analysisConfig = new TreeMap<String,String>();
        sentimentConfig = new TreeMap<String,String>();
        entityConfig = new TreeMap<String,String>();
        bioentityConfig = new TreeMap<String,String>();
        outputConfig = new TreeMap<String,String>();
        corefConfig = new TreeMap<String,String>();
        ontologyConfig = new TreeMap<String,String>();
        analysisConfig.put("Entity","FALSE");
        analysisConfig.put("Coref","FALSE");
        analysisConfig.put("Category","FALSE");
        analysisConfig.put("Sentiment","FALSE");

        //sentimentConfig.put("mainText","");
        sentimentConfig.put("textType","microblogs");
        sentimentConfig.put("title","");
        sentimentConfig.put("middleParas","");
        sentimentConfig.put("lastPara","");
        sentimentConfig.put("topDomain","");
        sentimentConfig.put("subDomain","");

        ontologyConfig.put("Topics","FALSE");
        ontologyConfig.put("Events","FALSE");
        corefConfig.put("Person","FALSE");
        corefConfig.put("Organization", "FALSE");
        corefConfig.put("CorefMethod","SUBSTRING");

        entityConfig.put("Person","FALSE");
        entityConfig.put("Organization","FALSE");
        entityConfig.put("Location","FALSE");
        entityConfig.put("Date","FALSE");
        entityConfig.put("Time","FALSE");
        entityConfig.put("Currency","FALSE");
        entityConfig.put("Percent","FALSE");
        entityConfig.put("Package","StanfordNLP");
    }
}
