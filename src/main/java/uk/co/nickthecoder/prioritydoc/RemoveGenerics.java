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

import java.util.List;

import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;

public class RemoveGenerics implements TemplateMethodModel
{
    public static final RemoveGenerics instance = new RemoveGenerics();

    public Object exec(List arguments)
        throws TemplateModelException
    {
        if ( arguments.size() != 1 ) {
            throw new TemplateModelException( "Expected 1 parameter, found " + arguments.size() );
        }
        
        String arg;
        try {
            arg = (String) (arguments.get(0));
        } catch (Exception e) {
            throw new TemplateModelException( "Expected String, found " + arguments.get(0).getClass().getName() );
        }
        
        return removeGenerics( arg );
    }

    public static String removeGenerics( String source )
    {
        StringBuffer result = new StringBuffer();
        
        int from = 0;
        int next;
        while ((next = source.indexOf( '<', from )) >= 0) {
            result.append( source.substring( from, next ) );
            from = source.indexOf( '>', next ) + 1;
            if ( from == 0 ) {
                return source; // Malformed, so replace nothing.
            }
        }
        result.append( source.substring( from ) );
        
        return result.toString();
    }  
}

