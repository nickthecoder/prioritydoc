/*
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package uk.co.nickthecoder.prioritydoc;

import com.sun.javadoc.RootDoc;
import com.sun.javadoc.DocErrorReporter;

import com.sun.javadoc.LanguageVersion;

/**
 * Contains the static entry point for this doclet.
 * Uses {@link Generator} to generate the documentation.
*/
public class PriorityDoc
{
    /**
     * This is a bit of a bodge to overcome the fact that this class is just a bunch of static methods, 
     * and not object oriented. Grrr.
     * This Options instance is used only for its optionLength method, and never holds any option data.
     */
    private static Options options = new Options();

    /**
     * Which version of the java language do I support?
     * Use LanguageVersion.JAVA_1_5 to support generic types, annotations, enums, and varArgs.
     * @return Hard coded to LanguageVersion.JAVA_1_5
     */
    public static LanguageVersion languageVersion()
    {
        return LanguageVersion.JAVA_1_5;
    }
    
    public static int optionLength(String option)
    {
        return options.optionLength( option );
    }
    
    public static boolean validOptions(String[][] opts, DocErrorReporter reporter)
    {
        System.out.println( "Valid options?" );
        return PriorityDoc.options.valid( opts, reporter );
    }
    
    public static boolean start(RootDoc root)
        throws Exception
    {
        Options options = new Options();
        options.read( root.options() );

        Generator generator = new Generator(root, options);
        generator.generate();
        return true;
    }

}

