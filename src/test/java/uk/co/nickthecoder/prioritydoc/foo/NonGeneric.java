package uk.co.nickthecoder.prioritydoc.foo;

import java.util.List;
import java.util.Map;

public class NonGeneric extends Generic<String>
{
    public NonGeneric()
    {
    }

    public boolean testMe( String me )
    {
        return true;
    }
}

