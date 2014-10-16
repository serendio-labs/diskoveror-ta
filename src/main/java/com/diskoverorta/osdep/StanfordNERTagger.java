package com.diskoverorta.osdep;

import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;

import java.util.List;

/**
 * Created by praveen on 15/10/14.
 */
public class StanfordNERTagger
{   
    URL url = StanfordNERTagger.class.getClassLoader().getResource("english.muc.7class.distsim.crf.ser.gz"); 
	String s = url.getPath();
    
    static AbstractSequenceClassifier<CoreLabel> nerClassifier = null;

    public StanfordNERTagger()
    {
        try {
            if (nerClassifier == null)
                nerClassifier = CRFClassifier.getClassifier(s);
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
