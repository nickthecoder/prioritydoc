package uk.co.nickthecoder.prioritydoc.foo;

public class NonGeneric extends Generic<String>
{
    public NonGeneric()
    {
    }

    @Override
    public boolean testMe( String me )
    {
        return true;
    }
}
