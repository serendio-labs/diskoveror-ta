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

    EntityObject()
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
