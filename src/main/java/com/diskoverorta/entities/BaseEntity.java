package com.diskoverorta.entities;

import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;

import java.util.List;

/**
 * Created by praveen on 15/10/14.
 */
public interface BaseEntity
{
    public List<String> getEntities(String sentence);
    public List<String> getEntities(List<List<CoreLabel>> sentTags);
}
