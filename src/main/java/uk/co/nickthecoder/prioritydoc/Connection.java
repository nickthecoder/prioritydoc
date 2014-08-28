/*******************************************************************************
 * Copyright (c) 2014 Nick Robinson
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package uk.co.nickthecoder.prioritydoc;

public class Connection
{
    public String connectionType;

    public String fromName;
    public String toName;

    public String fromLabel;
    public String toLabel;

    public Connection( String connectionType )
    {
        this.connectionType = connectionType;
    }

    public String getConnectionType()
    {
        return this.connectionType;
    }

    public String getFromName()
    {
        return this.fromName;
    }

    public String getToName()
    {
        return this.toName;
    }

    public String getFromLabel()
    {
        return this.fromLabel;
    }

    public String getToLabel()
    {
        return this.toLabel;
    }
}
