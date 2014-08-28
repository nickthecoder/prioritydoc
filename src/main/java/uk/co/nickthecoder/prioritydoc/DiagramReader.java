/*******************************************************************************
 * Copyright (c) 2014 Nick Robinson
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package uk.co.nickthecoder.prioritydoc;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Iterator;

public class DiagramReader
{
    private File source;

    private Diagram diagram;

    private String[] connectionTags = { "association", "generalisation", "realisation", "dependency", "aggregation", "composition" };

    public Diagram read( File filename )
        throws Exception
    {
        this.source = filename;

        Reader reader = new InputStreamReader(new FileInputStream(this.source));

        XMLTag root = XMLTag.openDocument(reader);
        XMLTag diagramTag = root.getTag("diagram");

        this.diagram = new Diagram();
        this.diagram.title = diagramTag.getAttribute("title");
        this.diagram.filename = diagramTag.getAttribute("filename");
        this.diagram.width = diagramTag.getOptionalIntAttribute("width", 1024);
        this.diagram.height = diagramTag.getOptionalIntAttribute("height", 1024);

        this.diagram.imageFilename = diagramTag.getOptionalAttribute("image", null);
        this.diagram.thumbnailFilename = diagramTag.getOptionalAttribute("thumbnail", null);

        this.diagram.thumbnailWidth = diagramTag.getOptionalIntAttribute("thumbnailWidth", 150);
        this.diagram.thumbnailHeight = diagramTag.getOptionalIntAttribute("thumbnailHeight", 150);

        for (Iterator<XMLTag> i = diagramTag.getTags("class"); i.hasNext();) {
            XMLTag classTag = i.next();
            readClass(classTag);
        }

        for (String tagName : this.connectionTags) {
            for (Iterator<XMLTag> i = diagramTag.getTags(tagName); i.hasNext();) {
                XMLTag connectionTag = i.next();
                readConnection(connectionTag, tagName);
            }
        }

        return this.diagram;
    }

    private void readClass( XMLTag classTag )
        throws Exception
    {
        ClassBox classBox = new ClassBox();
        classBox.name = classTag.getAttribute("name");
        classBox.x = classTag.getIntAttribute("x");
        classBox.y = classTag.getIntAttribute("y");
        this.diagram.add(classBox);
    }

    private void readConnection( XMLTag connectionTag, String tagName )
        throws Exception
    {
        Connection connection = new Connection(tagName);
        connection.fromName = connectionTag.getAttribute("from");
        connection.toName = connectionTag.getAttribute("to");
        connection.fromLabel = connectionTag.getOptionalAttribute("fromLabel", "");
        connection.toLabel = connectionTag.getOptionalAttribute("toLabel", "");
        this.diagram.add(connection);

    }
}
