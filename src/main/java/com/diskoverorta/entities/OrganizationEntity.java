package com.diskoverorta.entities;

import com.diskoverorta.osdep.OSEntityInterface;
import com.diskoverorta.osdep.StanfordNLP;
import com.diskoverorta.vo.EntityType;
import java.util.List;

/**
 * Created by praveen on 15/10/14.
 */
public class OrganizationEntity extends BaseEntity
{
    public static void main(String args[])
    {
        OrganizationEntity temp = new OrganizationEntity();
        OSEntityInterface os = new StanfordNLP();
        System.out.println(temp.getEntities(os,"Barack Obama is the president of USA"));
    }

    public List<String> getEntities(OSEntityInterface os,String sentence)
    {
        return getEntities(os,sentence, EntityType.ORGANIZATION);
    }

}
