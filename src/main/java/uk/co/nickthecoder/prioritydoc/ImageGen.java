/*******************************************************************************
 * Copyright (c) 2014 Nick Robinson
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package uk.co.nickthecoder.prioritydoc;

import java.io.File;
import java.net.URL;

/**
 * Uses cutycapt to convert a html document to a png, and uses image magick to create thumbnails.
 * It is assumed that both of those programs are on your path, and also that you have an X server running.
 * 
 * If you run this headless, you will need to start a lightweight X server such as xvfb.
 */
public class ImageGen
{
    public void htmlToPng( URL url, File output )
        throws Exception
    {
        String[] command = { "cutycapt", "--url=" + url.toString(), "--out=" + output.getPath() };
        run(command);
    }

    public void thumbnail( File source, File dest, int maxX, int maxY )
        throws Exception
    {
        String[] command = { "convert", source.getPath(), "-thumbnail", maxX + "x" + maxY, dest.getPath() };
        run(command);
    }

    public void run( String[] command )
        throws Exception
    {
        if (Runtime.getRuntime().exec(command).waitFor() != 0) {
            throw new Exception("Command returned non-zero exit status : " + toString(command));
        }
    }

    public String toString( String[] command )
    {
        StringBuffer buffer = new StringBuffer();
        for (String arg : command) {
            buffer.append("'").append(arg).append("' ");
        }
        return buffer.toString();
    }
}
