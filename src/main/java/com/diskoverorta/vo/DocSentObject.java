package com.diskoverorta.vo;

import edu.stanford.nlp.ling.Sentence;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by praveen on 11/11/14.
 */
public class DocSentObject
{
    public List<SentenceObject> docSentences = null;
    public DocSentObject()
    {
        docSentences = new ArrayList<SentenceObject>();
    }
}
