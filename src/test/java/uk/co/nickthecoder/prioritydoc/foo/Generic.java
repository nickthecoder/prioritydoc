package uk.co.nickthecoder.prioritydoc.foo;

import java.util.List;
import java.util.Map;

/**
 * Testing some tags :
 * <p>
 * a plain link : {@linkplain Foo}
 * </p>
 * <p>
 * Same code : {@code if (a > 1) // do nothing } }
 * </p>
 * <p>
 * Literal text : {@literal some <literal> text }
 * </p>
 * <p>
 * Author tag : {@author nick}
 * 
 * @param <T>
 */
public class Generic<T>
{
    protected T me;

    /**
     * value = {@value #one}
     */
    protected int one = 1;

    protected Map<String, List<String>> stringToListOfStrings;

    protected Map<String, List<String>>[] arrayOfStringToListOfStrings;

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
