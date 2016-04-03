/*******************************************************************************
 * Copyright (c) 2014 Nick Robinson
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package uk.co.nickthecoder.prioritydoc;

import java.util.ArrayList;
import java.util.List;

public class Diagram
{
    public String title = "";

    public int width = 1000;
    public int height = 1000;

    public List<ClassBox> classBoxes = new ArrayList<ClassBox>();

    public List<Connection> connections = new ArrayList<Connection>();

    public String filename = "diagram.html";

    public String imageFilename = null;

    public String thumbnailFilename = null;

    public int thumbnailWidth = 150;

    public int thumbnailHeight = 150;

    
    public void add( ClassBox classBox )
    {
        this.classBoxes.add(classBox);
    }

    public void add( Connection connection )
    {
        this.connections.add(connection);
    }

    public String getTitle()
    {
        return this.title;
    }

    public int getWidth()
    {
        return width;

    }

    public int getHeight()
    {
        return this.height;
    }

    public List<ClassBox> getClassBoxes()
    {
        return this.classBoxes;
    }

    public List<Connection> getConnections()
    {
        return this.connections;
    }
}
