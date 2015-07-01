package com.diskoverorta.entities;

import com.diskoverorta.osdep.OSEntityInterface;
import com.diskoverorta.vo.EntityType;


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
