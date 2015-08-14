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
package com.diskoverorta.entities;

import com.diskoverorta.osdep.OSEntityInterface;
import com.diskoverorta.osdep.OpenNLP;
import com.diskoverorta.osdep.StanfordNLP;
import com.diskoverorta.vo.EntityObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by praveen on 17/10/14.
 */
public class EntityManager
{
    //to do : Get all selected entities at runtime and process only them, Make use of array of baseentities
    static StanfordNLP nlpStanford = null;
    public EntityManager()
    {
        if(nlpStanford==null)
        {
            nlpStanford = new StanfordNLP();
        }
    }

    public static void main(String args[])
    {
        String str = "India’s indigenously developed nuclear capable sub-sonic cruise missile ‘Nirbhay’, which can strike targets more than 700 km away, was on Friday test-fired from a test range at Chandipur in Odisha.“The missile was test-fired from a mobile launcher positioned at launch pad 3 of the Integrated Test Range at about 10.03 hours,” said an official soon after the flight took off from the launch ground.“Flight details will be available after data retrieved from radars and telemetry points, monitoring the trajectories, are analysed,” the official said. It is the second test of the sub-sonic long range cruise missile ‘Nirbhay’ from the ITR. The maiden flight, conducted on March 12, 2013 could not achieve all the desired parameters as “the flight had to be terminated mid-way when deviations were observed from its intended course,” sources said. India has in its arsenal the 290-km range supersonic “BrahMos” cruise missile which is jointly developed by India and Russia. But ‘Nirbhay’ with long range capability is a different kind of missile being developed by the Defence Research and Development Organisation (DRDO).";
        EntityManager temp = new EntityManager();
        System.out.println(temp.getALLDocumentEntitiesINJSON(str));
    }

    public String getALLDocumentEntitiesINJSON(String sDoc)
    {
        List<EntityObject> allEntities = getALLEntitiesForDocument(sDoc);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonOutput = gson.toJson(allEntities);
        return jsonOutput;
    }

    List<EntityObject> getALLEntitiesForDocument(String sDoc)
    {
        List<EntityObject> allEntities = new ArrayList<EntityObject>();
        List<String> sentences = nlpStanford.splitSentencesINDocument(sDoc);
        for(String temp : sentences)
        {
            allEntities.add(getALLEntitiesForSentence(temp));
        }
        return allEntities;
    }

    EntityObject getALLEntitiesForSentence(String sSentence)
    {
        Map<String,String> entityConfig = new HashMap<String,String>();
        entityConfig.put("person","TRUE");
        entityConfig.put("organization","TRUE");
        entityConfig.put("location","TRUE");
        entityConfig.put("date","TRUE");
        entityConfig.put("time","TRUE");
        entityConfig.put("currency","TRUE");
        entityConfig.put("percent","TRUE");
        entityConfig.put("package","TRUE");

        return getSelectedEntitiesForSentence(sSentence,entityConfig);
    }

    public EntityObject getSelectedEntitiesForSentence(String sSentence,Map<String,String> entityConfig)
    {
        OSEntityInterface osdep = null;
        EntityObject entities = new EntityObject();

        if((entityConfig.containsKey("package") == true) && entityConfig.get("package").equals("StanfordNLP") == true )
            osdep = new StanfordNLP();
        else
            osdep = new OpenNLP();

        if(entityConfig.get("person")== "TRUE")
            entities.person = (new PersonEntity()).getEntities(osdep,sSentence);

        if(entityConfig.get("organization")== "TRUE")
            entities.organization = (new OrganizationEntity()).getEntities(osdep,sSentence);

        if(entityConfig.get("location")== "TRUE")
            entities.location = (new LocationEntity()).getEntities(osdep,sSentence);

        if(entityConfig.get("date")== "TRUE")
            entities.date = (new DateEntity()).getEntities(osdep,sSentence);

        if(entityConfig.get("time")== "TRUE")
            entities.time = (new TimeEntity()).getEntities(osdep,sSentence);

        if(entityConfig.get("currency")== "TRUE")
            entities.currency = (new CurrencyEntity()).getEntities(osdep,sSentence);

        if(entityConfig.get("percent")== "TRUE")
            entities.percent = (new PercentEntity()).getEntities(osdep,sSentence);

        return entities;
    }
}
