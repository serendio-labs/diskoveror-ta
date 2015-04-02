package com.diskoverorta.vo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by praveen on 29/3/15.
 */
public class KlusterData {
    public Map<Integer,String> wordIndex = null;
    public Map<String,Double[]> featureMap = null;
    public Map<Integer,List<String>> clusterMap = null;
    public Map<Integer,String> labelMap =null;
    public KlusterData()
    {
        wordIndex = new HashMap<Integer, String>();
        featureMap = new HashMap<String, Double[]>();
        clusterMap = new HashMap<Integer,List<String>>();
        labelMap = new HashMap<Integer,String>();
    }
}
