package com.diskoverorta.osdep;

import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.process.DocumentPreprocessor;

import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by praveen on 15/10/14.
 */
public class StanfordNLP
{   
    static AbstractSequenceClassifier<CoreLabel> ner7Classifier = null;
    static AbstractSequenceClassifier<CoreLabel> ner3Classifier = null;
    //URL url = StanfordNERTagger.class.getClassLoader().getResource("english.muc.7class.distsim.crf.ser.gz");
	String ner7classifierName = "english.muc.7class.distsim.crf.ser.gz";
    String ner3classifierName = "english.all.3class.distsim.crf.ser.gz";

    public StanfordNLP()
    {
        try {
            if (ner7Classifier == null)
                ner7Classifier = CRFClassifier.getClassifier(ner7classifierName);
            if(ner3Classifier == null)
                ner3Classifier = CRFClassifier.getClassifier(ner3classifierName);
        }catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
    public List<List<CoreLabel>> get7NERTaggedOutput(String str)
    {
        return ner7Classifier.classify(str);
    }

    public List<List<CoreLabel>> get3NERTaggedOutput(String str)
    {
        return ner3Classifier.classify(str);
    }

    public List<String> splitSentencesINDocument(String sDoc)
    {
        Reader reader = new StringReader(sDoc);
        DocumentPreprocessor dp = new DocumentPreprocessor(reader);
        List<String> sentenceList = new ArrayList<String>();
        Iterator<List<HasWord>> it = dp.iterator();

        while (it.hasNext())
        {
            StringBuilder sentenceSb = new StringBuilder();
            List<HasWord> sentence = it.next();
            for (HasWord token : sentence)
            {
                if(sentenceSb.length()>1)
                {
                    sentenceSb.append(" ");
                }
                sentenceSb.append(token);
            }
            sentenceList.add(sentenceSb.toString().trim());
        }
        return sentenceList;
    }
}
