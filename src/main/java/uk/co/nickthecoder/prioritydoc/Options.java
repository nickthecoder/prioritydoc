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

import java.util.Map;
import java.util.HashMap;

import java.io.*;
import java.net.*;

import com.sun.javadoc.DocErrorReporter;

/**
 * Holds reads and stores options data, which is specified on the command line.
 */
public class Options
{
    /**
     * Command line argument -d
     */
    public String destinationDirectory = ".";

    /**
     * Maps the name of a package to the base url for its javadocs.
     */
    protected Map<String,String> linkedPackages;

    /**
     * The title of your project. It will appear in title tags, as well as on the main index page.
     */
    public String title = "";

    /**
     * Should cookies be used to remember the priority level, and if pages should be expanded?
     */
    public boolean useCookies = false;
    
    /**
     * Should resources (such as .cc and .js files) overwrite existing files?
     */
    public boolean overwriteResources = false;
    
    /**
     * @see PriorityDoc#optionLength(String)
     */
    public int optionLength(String option)
    {
    
        if (option.equals("-d")) {
            return 2;
        } else if (option.equals("-link")) {
            return 2;
        } else if (option.equals("-linkoffline")) {
            return 3;
        } else if ( option.equals("-title") || option.equals("-windowTitle") ) {
            return 2;
        } else if ( option.equals("-usecookies" ) ) {
            return 1;
        } else if ( option.equals("-overwriteresources" ) ) {
            return 1;
        }
        return 0;
    }

    public void read( String[][] options )
        throws Exception
    {
        this.linkedPackages = new HashMap<String,String>();
        
        
        for (String[] option : options ) {
            String name = option[0];
            if (name == null) {
                continue;
            }
            
            if ( name.equals("-d") ) {
                this.destinationDirectory = option[1];
            } else if ( name.equals("-link") ) {
                link( option[1] );
            } else if ( name.equals("-linkoffline") ) {
                linkOffline( option[1], option[2] );
            } else if ( name.equals("-title") || name.equals("-windowTitle") ) {
                this.title = option[1];
            } else if ( name.equals("-usecookies") ) {
                this.useCookies = true;
            } else if ( name.equals("-overwriteresources") ) {
                this.overwriteResources = true;
            }
        }
    }
  
    /**
     * @see PriorityDoc#validOptions(String[][],DocErrorReporter)
     */
    public boolean valid(String[][] options, DocErrorReporter reporter)
    {
        return true;
    }
    
    protected void link( String url )
        throws Exception
    {
        linkOffline( url, url + "/package-list" );
    }


    protected void linkOffline( String baseURL, String packageListURL )
        throws Exception
    {
        if (! baseURL.endsWith("/")) {
            baseURL = baseURL + "/";
        }
        
        BufferedReader in = null;
        URLConnection uc = null;
        try {
            URL url = new URL( packageListURL );
            uc = url.openConnection();
            uc.connect();
            in = new BufferedReader( new InputStreamReader( uc.getInputStream() ) );
            String line = in.readLine();
            while( line != null ) {
                this.linkedPackages.put( line, baseURL );
                line = in.readLine();
            }
        } finally {
            try { in.close(); } catch (Exception e) {}
        }
    }
    

    /**
     * param packageName The fully qualified package name. Not null or "".
     * return The base url for the javadocs if that package is linked, otherwise null. 
     */
    public String getJavadocsForPackage( String packageName )
    {
        return this.linkedPackages.get( packageName );
    }

    /**
     * Simple Getter
     * @see #title
     */
    public String getTitle()
    {
        return title;
    }
    
    /**
     * Simple getter
     * @see #useCookies
     */
    public boolean getUseCookies()
    {
        return this.useCookies;
    }
}

