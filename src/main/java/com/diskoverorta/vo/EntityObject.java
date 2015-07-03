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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by praveen on 17/10/14.
 */
public class EntityObject
{
    public List<String> currency = null;
    public List<String> date = null;
    public List<String> location = null;
    public List<String> organization = null;
    public List<String> percent = null;
    public List<String> person = null;
    public List<String> time = null;

    public EntityObject()
    {
        currency = new ArrayList<String>();
        date = new ArrayList<String>();
        location = new ArrayList<String>();
        organization = new ArrayList<String>();
        percent = new ArrayList<String>();
        person = new ArrayList<String>();
        time = new ArrayList<String>();
    }
}
