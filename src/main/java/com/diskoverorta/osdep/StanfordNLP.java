package com.diskoverorta.osdep;

import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;

import java.net.URL;
import java.util.List;

/**
 * Created by praveen on 15/10/14.
 */
public class StanfordNLP
{   
    //URL url = StanfordNERTagger.class.getClassLoader().getResource("english.muc.7class.distsim.crf.ser.gz");
	String StanfordNERClassifier = "english.muc.7class.distsim.crf.ser.gz";
    static AbstractSequenceClassifier<CoreLabel> nerClassifier = null;

    public StanfordNLP()
    {
        try {
            if (nerClassifier == null)
                nerClassifier = CRFClassifier.getClassifier(StanfordNERClassifier);
        }catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
    public List<List<CoreLabel>> getNERTaggedOutput(String str)
    {
        return nerClassifier.classify(str);
    }
}
