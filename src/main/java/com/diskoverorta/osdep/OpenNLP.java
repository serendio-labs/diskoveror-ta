package com.diskoverorta.osdep;

/**
 * Created by praveen on 6/4/15.
 */

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.diskoverorta.vo.EntityType;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.Span;

public class OpenNLP implements OSEntityInterface
{
    static SentenceDetectorME sdetector = null;
    static Tokenizer tokenizer = null;
    static NameFinderME personFinder = null;
    static NameFinderME orgFinder = null;
    static NameFinderME locFinder = null;
    static NameFinderME moneyFinder = null;
    static NameFinderME dateFinder = null;
    static NameFinderME timeFinder = null;
    static NameFinderME percentFinder = null;

    public OpenNLP()
    {
        try {
            if (sdetector == null)
                sdetector = new SentenceDetectorME(new SentenceModel(new FileInputStream("opennlpmodel/en-sent.bin")));
            if (tokenizer == null)
                tokenizer = new TokenizerME(new TokenizerModel(new FileInputStream("opennlpmodel/en-token.bin")));
        }catch(IOException ex)
        {
            ex.printStackTrace();
        }
    }

    public List<String> splitSentencesINDocument(String document)
    {
        return Arrays.asList(sdetector.sentDetect(document));
    }

    NameFinderME getNameFinderModel(EntityType entityCat)
    {
        NameFinderME temp = null;
        if(entityCat == EntityType.PERSON)
        {
            if(personFinder == null)
                personFinder = createNameFinderObject("opennlpmodel/en-ner-person.bin");
            temp = personFinder;
        }
        else if(entityCat == EntityType.ORGANIZATION)
        {
            if(orgFinder == null)
                orgFinder = createNameFinderObject("opennlpmodel/en-ner-organization.bin");
            temp =  orgFinder;
        }
        else if(entityCat == EntityType.LOCATION)
        {
            if(locFinder == null)
                locFinder = createNameFinderObject("opennlpmodel/en-ner-location.bin");
            temp =  locFinder;
        }
        else if(entityCat == EntityType.MONEY)
        {
            if(moneyFinder == null)
                moneyFinder = createNameFinderObject("en-ner-money.bin");
            temp =  moneyFinder;
        }
        else if(entityCat == EntityType.DATE)
        {
            if(dateFinder == null)
                dateFinder = createNameFinderObject("opennlpmodel/en-ner-date.bin");
            temp =  dateFinder;
        }
        else if(entityCat == EntityType.TIME)
        {
            if(timeFinder == null)
                timeFinder = createNameFinderObject("opennlpmodel/en-ner-time.bin");
            temp =  timeFinder;
        }
        else if(entityCat == EntityType.PERCENT)
        {
            if(percentFinder == null)
                percentFinder = createNameFinderObject("opennlpmodel/en-ner-percentage.bin");
            temp =  percentFinder;
        }
        return temp;
    }

    NameFinderME createNameFinderObject(String path)
    {
        NameFinderME temp = null;
        try{
            temp = new NameFinderME(new TokenNameFinderModel(new FileInputStream(path)));
        }catch(IOException ex)
        {
            ex.printStackTrace();
        }
        return temp;
    }

    @Override
    public List<String> getEntities(EntityType entityCat, String text)
    {
        NameFinderME temp = getNameFinderModel(entityCat);
        List<String> entityList = new ArrayList<String>();
        String [] tokens=null;
        tokens = tokenizer.tokenize(text);

        Span nameSpans[] = temp.find(tokens);

        for(Span s: nameSpans)
        {
            StringBuilder sb = new StringBuilder();
            for(int i=s.getStart();i<s.getEnd();i++){
                sb.append(tokens[i]+" ");
            }
            sb.deleteCharAt(sb.length()-1);
            entityList.add(sb.toString());
        }
        return entityList;
    }
}

