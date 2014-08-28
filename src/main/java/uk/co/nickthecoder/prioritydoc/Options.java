/*
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package uk.co.nickthecoder.prioritydoc;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sun.javadoc.DocErrorReporter;

/**
 * Parses the command line arguments.
 * <ul>
 * <li><b>-link baseURL :</b> Link to external javadoc apis.</li>
 * <li><b>-linkoffline baseURL packageListURL :</b> Link to external javadoc apis.</li>
 * <li><b>-title text :</b> The text for the html's title tag, and also for the deading of the main index page.</li>
 * <li><b>-windowtitle :</b> Synonym for -title.</li>
 * <li><b>-doctitle :</b> Synonym for -title.</li>
 * <li><b>-usecookies :</b> The generated html will use cookies to store the preferred priority level.</li>
 * <li><b>-overwriteresources :</b> Icon images, style sheets and javascript files will overwrite any existing files with the same name.</li>
 * <li><b>-diagram filename :</b> Will generate a class diagram. For details of the file format, see {@link DiagramReader}</li>
 * <li><b>-mainpackage packagename :</b> The main index will include the comment text from the specified package.
 * <li><b>-quiet :</b></li>
 * <li><b>-q : </b>Synonym for -quiet</li>
 * </ul>
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
    protected Map<String, String> linkedPackages;

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

    public List<String> diagrams = new ArrayList<String>();

    public String mainPackage = null;

    public boolean quiet = false;

    /**
     * @see PriorityDoc#optionLength(String)
     */
    public int optionLength( String option )
    {
        option = option.toLowerCase();

        if (option.equals("-d")) {
            return 2;
        } else if (option.equals("-link")) {
            return 2;
        } else if (option.equals("-linkoffline")) {
            return 3;
        } else if (option.equals("-title") || option.equals("-windowtitle") || (option.equals("-doctitle"))) {
            return 2;
        } else if (option.equals("-usecookies")) {
            return 1;
        } else if (option.equals("-overwriteresources")) {
            return 1;
        } else if (option.equals("-diagram")) {
            return 2;
        } else if (option.equals("-mainpackage")) {
            return 2;
        } else if (option.equals("-quiet") || option.equals("-q")) {
            return 1;
        }
        return 0;
    }

    public void read( String[][] options )
        throws Exception
    {
        this.linkedPackages = new HashMap<String, String>();

        for (String[] option : options) {
            String name = option[0].toLowerCase();
            if (name == null) {
                continue;
            }

            if (name.equals("-d")) {
                this.destinationDirectory = option[1];
            } else if (name.equals("-link")) {
                link(option[1]);
            } else if (name.equals("-linkoffline")) {
                linkOffline(option[1], option[2]);
            } else if (name.equals("-title") || name.equals("-windowtitle") || name.equals("-doctitle")) {
                this.title = option[1];
            } else if (name.equals("-usecookies")) {
                this.useCookies = true;
            } else if (name.equals("-overwriteresources")) {
                this.overwriteResources = true;
            } else if (name.equals("-diagram")) {
                this.diagrams.add(option[1]);
            } else if (name.equals("-mainpackage")) {
                this.mainPackage = option[1];
            } else if (name.equals("-quiet") || name.equals("-q")) {
                this.quiet = true;
            }
        }
    }

    /**
     * @see PriorityDoc#validOptions(String[][],DocErrorReporter)
     */
    public boolean valid( String[][] options, DocErrorReporter reporter )
    {
        /*
        System.out.println("Checking options : ");
        for (String[] option : options) {
            for (String str : option) {
                System.out.println(str);
            }
            System.out.println();
        }
        */
        return true;
    }

    protected void link( String url )
        throws Exception
    {
        linkOffline(url, url + "/package-list");
    }

    protected void linkOffline( String baseURL, String packageListURL )
        throws Exception
    {
        if (!baseURL.endsWith("/")) {
            baseURL = baseURL + "/";
        }

        BufferedReader in = null;
        URLConnection uc = null;
        try {
            URL url = new URL(packageListURL);
            uc = url.openConnection();
            uc.connect();
            in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
            String line = in.readLine();
            while (line != null) {
                this.linkedPackages.put(line, baseURL);
                line = in.readLine();
            }
        } finally {
            try {
                in.close();
            } catch (Exception e) {
            }
        }
    }

    /**
     * param packageName The fully qualified package name. Not null or "".
     * return The base url for the javadocs if that package is linked, otherwise null.
     */
    public String getJavadocsForPackage( String packageName )
    {
        return this.linkedPackages.get(packageName);
    }

    /**
     * Simple Getter
     * 
     * @see #title
     */
    public String getTitle()
    {
        return title;
    }

    /**
     * Simple getter
     * 
     * @see #useCookies
     */
    public boolean getUseCookies()
    {
        return this.useCookies;
    }

}
