package com.diskoverorta.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by praveen on 11/11/14.
 */
public class DocumentObject
{
    String docText = null;
    public EntityObject entities = null;
    public EntityMap entitiesMeta = null;

    public DocumentObject()
    {
        entities = new EntityObject();
        entitiesMeta = new EntityMap();
    }
}
