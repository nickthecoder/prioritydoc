/*******************************************************************************
 * Copyright (c) 2014 Nick Robinson
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package uk.co.nickthecoder.prioritydoc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import uk.co.nickthecoder.prioritydoc.Generator.NameComparator;

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

    public int priority = 1;
    
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

    private int missingMethodCount;
    private int missingFieldCount;
    
    public int getMissingMethodCount()
    {
        return this.missingMethodCount;
    }
    public int getMissingFieldCount()
    {
        return this.missingFieldCount;
    }
    
    public void setClassDoc( ClassDoc classDoc )
    {
        this.classDoc = classDoc;

        this.methods = new ArrayList<MethodDoc>();
        this.fields = new ArrayList<FieldDoc>();

        for (MethodDoc method : this.classDoc.methods()) {
            if (getPriority(method) <= this.priority) {
                this.methods.add(method);
            } else {
                this.missingMethodCount += 1;
            }
        }
        
        for (FieldDoc field : this.classDoc.fields()) {
            if (getPriority(field) <= this.priority) {
                this.fields.add(field);
            } else {
                this.missingFieldCount += 1;
            }
        }

        Collections.sort( this.methods, NameComparator.instance );
        Collections.sort( this.fields, NameComparator.instance );
    }

    protected int getPriority( ProgramElementDoc doc )
    {
        for (Tag tag : doc.tags("priority")) {
            String text = tag.text();
            try {
                return Integer.parseInt(text);
            } catch (Exception e) {
                // Do nothing.
            }
        }
        
        if (doc.isPrivate() || (doc.tags("deprecated").length > 0)) {
          return 5;
        }
        if (doc.isPackagePrivate()) {
            return 4;
        }
        if (doc.isProtected()) {
            return 3;
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
