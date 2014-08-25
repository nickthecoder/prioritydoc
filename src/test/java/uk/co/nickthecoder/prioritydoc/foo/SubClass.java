package uk.co.nickthecoder.prioritydoc.foo;

import uk.co.nickthecoder.prioritydoc.Generator;
import uk.co.nickthecoder.prioritydoc.Options;

import com.sun.javadoc.RootDoc;

/**
 * This does nothing, its only here so that I have a sub-class to play around with while I develop the program.
 * @see Generator My super class
 * @deprecated
 */
public class SubClass extends Generator
{
    /**
     * Static field  should be separate from non-static fields.
     * @see #BAR Another static field
     * @deprecated
     * @priority 5
     */
    public static final String FOO = "foo";
    
    public static final String BAR = "bar";
        
    /**
     * This is a static method. I want it to appear separate from the other methods.
     * @see #bar() another static method
     * @deprecated
     * @priority 5
     */
    public static void foo()
    {
    }
    
    /**
     * @priority 2
     */
    public static void bar()
    {
    }
    
    /**
     * @priority 1
     */
    public static void helloWorld()
    {
    }
    
    /**
     * @priority 1
     */
    public static void helloAgain()
    {
    }
    
    public SubClass( RootDoc root, Options options )
        throws Exception
    {
        super( root, options );
    }
    
    /**
     * This is the only method declared in this class, the rest are declared in Generator.
     * @throws Exception once in a blue moon.
     * @see Generator#generate() super method
     * @priority 1
     */
    public void generate()
        throws Exception
    {
        super.generate();
    }
    
    /**
     * the super class has a DIFFERENT copy method, with different parameters,
     * so both methods must be listed.
     */
    public void copy( int a, int b )
    {
    }
    
    /**
     * @priority 1
     */
    public void one()
    {
    }

    /**
     * @priority 2
     */
    public void two()
    {
    }

    /**
     * @priority 3
     */
    public void three()
    {
    }

    /**
     * @priority 4
     */
    public void four()
    {
    }

    /**
     * @priority 5
     */
    public void five()
    {
    }

}

