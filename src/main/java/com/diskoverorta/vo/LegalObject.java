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

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Created by praveen on 4/2/15.
 */
public class LegalObject
{
    public String sentence = null;
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
