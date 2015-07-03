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
package com.diskoverorta.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import com.diskoverorta.entities.*;
import org.json.simple.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

public class JsonConvertor {

    public String JsonConvertor()
    {

        PersonEntity p = new PersonEntity();
        PercentEntity pe = new PercentEntity();
        DateEntity d = new DateEntity();
        TimeEntity t = new TimeEntity();
        CurrencyEntity m = new CurrencyEntity();
        LocationEntity l = new LocationEntity();
        OrganizationEntity o = new OrganizationEntity();

        JSONObject obj = new JSONObject();
/*
        obj.put("PERSON", p.Person().replace("\n", ", "));
        obj.put("Percent", pe.Percent().replace("\n", ", "));
        obj.put("Date", d.Date().replace("\n", ", "));
        obj.put("Time", t.Time().replace("\n", ", "));
        obj.put("Money", m.Money().replace("\n", ", "));
        obj.put("Location", l.Location().replace("\n", ", "));
        obj.put("Organization", o.Organization().replace("\n", ", "));
*/
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonOutput = gson.toJson(obj);
        System.out.print(jsonOutput);

        return jsonOutput;
    }

    public static void main(String[] args) throws IOException {
        JsonConvertor js = new JsonConvertor();
        js.JsonConvertor();
        BufferedWriter bf = new BufferedWriter(new FileWriter("/home/serendio/jaroutput-json.txt"));
        bf.write(js.JsonConvertor());
        bf.newLine();
        bf.flush();
        bf.close();
    }
}
