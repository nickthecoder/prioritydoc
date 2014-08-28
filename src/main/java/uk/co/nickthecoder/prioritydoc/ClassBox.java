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

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.ProgramElementDoc;
import com.sun.javadoc.Tag;

public class ClassBox
{
    public int x;

    public int y;

    private ClassDoc classDoc;

    public String name;

    private List<FieldDoc> fields;

    private List<MethodDoc> methods;

    public int getX()
    {
        return this.x;
    }

    public int getY()
    {
        return this.y;
    }

    public String getName()
    {
        return this.name;
    }

    public ClassDoc getClassDoc()
    {
        return this.classDoc;
    }

    public void setClassDoc( ClassDoc classDoc )
    {
        this.classDoc = classDoc;

        this.methods = new ArrayList<MethodDoc>();
        this.fields = new ArrayList<FieldDoc>();

        for (MethodDoc method : this.classDoc.methods()) {
            if (getPriority(method) == 1) {
                this.methods.add(method);
            }
        }
        for (FieldDoc field : this.classDoc.fields()) {
            if (getPriority(field) == 1) {
                this.fields.add(field);
            }
        }
    }

    protected int getPriority( ProgramElementDoc doc )
    {
        for (Tag tag : doc.tags("priority)")) {
            String text = tag.text();
            try {
                return Integer.parseInt(text);
            } catch (Exception e) {
                // Do nothing.
            }
        }
        return 1;
    }

    public List<FieldDoc> getFields()
    {
        return this.fields;
    }

    public List<MethodDoc> getMethods()
    {
        return this.methods;
    }
}
