package com.diskoverorta.osdep;

import com.diskoverorta.vo.EntityType;

import java.util.List;

/**
 * Created by praveen on 6/4/15.
 */
public interface OSEntityInterface
{
    public List<String> getEntities(EntityType entityCat,String text);
}
