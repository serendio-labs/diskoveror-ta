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
