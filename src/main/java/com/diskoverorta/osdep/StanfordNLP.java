package com.diskoverorta.osdep;

import edu.stanford.nlp.dcoref.CorefChain;
import edu.stanford.nlp.dcoref.CorefCoreAnnotations;
import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;

import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.util.*;

/**
 * Created by praveen on 15/10/14.
 */
public class StanfordNLP
{
    public static StanfordCoreNLP pipeline = null;
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
            if(pipeline == null)
            {
                Properties props = new Properties();
                props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
                pipeline = new StanfordCoreNLP(props);
            }


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
    public static void main(String args[]) {
        String sample2 = "Investigation into the Saradha chit fund scam has so far not revealed any terror link with Bangladesh, the Centre said today, days after the BJP had alleged such a connection.\n" +
                "\n" +
                "“The investigation has so far not revealed any such transaction where money was routed to Bangladesh to fund terrorist activities,” Union Minister of State for Personnel Jitendra Singh told the Lok Sabha in a written response.\n" +
                "\n" +
                "BJP chief Amit Shah had alleged that Saradha chit fund money had been used in the October 2, 2014 Bardhaman blast, which is being probed for link with the Jamaat-ul-Mujahideen Bangladesh (JMB) terror outfit.\n" +
                "\n" +
                "“Saradha chit fund money was used in the Burdwan (Bardhaman) blast. The NIA is not being allowed to probe the blast properly. Hurdles are being created. It is being done in order to save TMC leaders who are involved in the blast,” Mr. Shah had said, attacking the Trinamool Congress, at a BJP rally in Kolkata.\n" +
                "\n" +
                "The Union Minister was asked whether the government has sought details of the probe into the Saradha chit fund scam after reports indicated that a part of the money was routed to Bangladesh to fund terror activities. Mr. Singh replied that government had not sought details of the probe.\n" +
                "\n" +
                "To another question on whether the Saradha chief has admitted that he paid large sums to several people to influence the case in his favour, the Minister said, “The matter is under investigation.”";
//        StanfordNLP obj = new StanfordNLP();
//        System.out.println(obj.splitSentencesINDocument(sample2));

    }
}
