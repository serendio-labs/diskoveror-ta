package com.diskoverorta.entities;

import com.diskoverorta.osdep.OSEntityInterface;
import com.diskoverorta.vo.EntityType;
import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;

import java.util.List;

/**
 * Created by praveen on 15/10/14.
 */
public class BaseEntity
{
    public List<String> getEntities(OSEntityInterface os,String sentence,EntityType ent)
    {
        List<String> temp = null;
        if(os !=null)
            temp = os.getEntities(ent,sentence);
        return temp;
    }
}
