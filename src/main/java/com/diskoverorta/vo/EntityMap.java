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
