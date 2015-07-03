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
import com.diskoverorta.osdep.StanfordNLP;
import com.diskoverorta.vo.EntityType;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by praveen on 15/10/14.
 */
public class LocationEntity extends BaseEntity
{
    public static void main(String args[])
    {
        LocationEntity temp = new LocationEntity();
        OSEntityInterface os = new StanfordNLP();
        System.out.println(temp.getEntities(os,"Barack Obama is the president of USA"));
    }

    public List<String> getEntities(OSEntityInterface os,String sentence)
    {
        return getEntities(os,sentence, EntityType.LOCATION);
    }
}
