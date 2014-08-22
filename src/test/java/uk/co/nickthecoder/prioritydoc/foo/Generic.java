package uk.co.nickthecoder.prioritydoc.foo;

import java.util.List;
import java.util.Map;

public class Generic<T>
{
    protected T me;

    protected Map<String,List<String>> stringToListOfStrings;

    protected Map<String,List<String>>[] arrayOfStringToListOfStrings;

    public Generic()
    {
    }

    public T getMe()
    {
        return me;
    }
    
    public void setMe( T me )
    {
        this.me = me;
    }
    
    public boolean testMe( T me )
    {
        return true;
    }
}

