/*******************************************************************************
 *   Copyright 2015 Serendio Inc. ( http://www.serendio.com/ )
 *   Author - Praveen Jesudhas
 *    
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *******************************************************************************/
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
        analysisConfig.put("Topic","FALSE");
        analysisConfig.put("Sentiment","FALSE");
        analysisConfig.put("Keyword","FALSE");

        //sentimentConfig.put("mainText","");
        sentimentConfig.put("textType","microblogs");
        sentimentConfig.put("title","");
        sentimentConfig.put("middleParas","");
        sentimentConfig.put("lastPara","");
        sentimentConfig.put("topDomain","");
        sentimentConfig.put("subDomain","");

        ontologyConfig.put("Topics","FALSE");
        ontologyConfig.put("Events","FALSE");
        corefConfig.put("person","FALSE");
        corefConfig.put("Organization", "FALSE");
        corefConfig.put("CorefMethod","SUBSTRING");

        entityConfig.put("person","FALSE");
        entityConfig.put("organization","FALSE");
        entityConfig.put("location","FALSE");
        entityConfig.put("date","FALSE");
        entityConfig.put("time","FALSE");
        entityConfig.put("currency","FALSE");
        entityConfig.put("percent","FALSE");
        entityConfig.put("package","StanfordNLP");
    }
}
