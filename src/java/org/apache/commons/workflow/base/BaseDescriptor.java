/*
 * Copyright 1999-2001,2004 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */ 

package org.apache.commons.workflow.base;


import java.util.EmptyStackException;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.workflow.Context;
import org.apache.commons.workflow.Descriptor;


/**
 * <p>Basic implementation of the <strong>Descriptor</strong> interface.</p>
 *
 * @version $Revision$ $Date$
 * @author Craig R. McClanahan
 */

public class BaseDescriptor implements Descriptor {


    // ----------------------------------------------------------- Constructors


    /**
     * Create an instance with default values.
     */
    public BaseDescriptor() {

        super();

    }


    /**
     * Create an instance with the specified values.
     *
     * @param xpath The XPath reference expression
     */
    public BaseDescriptor(String xpath) {

        this(null, null, xpath, null);

    }


    /**
     * Create an instance with the specified values.
     *
     * @param xpath The XPath reference expression
     * @param type The expected class of this object
     */
    public BaseDescriptor(String xpath, Class type) {

        this(null, null, xpath, type);

    }


    /**
     * Create an instance with the specified values.
     *
     * @param name The object name
     * @param scope The object scope
     */
    public BaseDescriptor(String name, String scope) {

        this(name, scope, null, null);

    }


    /**
     * Create an instance with the specified values.
     *
     * @param name The object name
     * @param scope The object scope
     * @param type The expected class of this object
     */
    public BaseDescriptor(String name, String scope, Class type) {

        this(name, scope, null, type);

    }


    /**
     * Create an instance with the specified values.
     *
     * @param name The object name
     * @param scope The object scope
     * @param xpath The XPath reference expression
     * @param type The expected class
     */
    public BaseDescriptor(String name, String scope,
                          String xpath, Class type) {

        super();
        setName(name);
        setScope(scope);
        setXpath(xpath);
        setType(type);

    }


    // ----------------------------------------------------- Instance Variables


    /**
     * The name of the Java object (in some scope).
     */
    protected String name = null;


    /**
     * The scope of the Java object.
     */
    protected String scope = null;


    /**
     * The optional Java class expected by this Descriptor.
     */
    protected Class type = null;


    /**
     * The XPath expression used to access the Java object.
     */
    protected String xpath = null;


    // ------------------------------------------------------------- Properties


    /**
     * Return the name of the Java object (in some scope) referenced by
     * this Descriptor.
     */
    public String getName() {

        return (this.name);

    }


    /**
     * Set the name of the Java object (in some scope) referenced by
     * this Descriptor.
     *
     * @param name The new object name
     */
    public void setName(String name) {

        this.name = name;

    }


    /**
     * Return the scope of the Java object referenced by this Descriptor.
     */
    public String getScope() {

        return (this.scope);

    }


    /**
     * Set the scope of the Java object referenced by this Descriptor.
     *
     * @param scope The new scope name
     */
    public void setScope(String scope) {

        this.scope = scope;

    }


    /**
     * Return the optional Java class expected by this Descriptor.
     */
    public Class getType() {

        return (this.type);

    }


    /**
     * Set the optional Java class expected by this Descriptor.
     *
     * @param type The new expected type
     */
    public void setType(Class type) {

        this.type = type;

    }


    /**
     * Return the XPath expression used to access the Java object
     * referenced by this Descriptor.
     */
    public String getXpath() {

        return (this.xpath);

    }


    /**
     * Set the XPath expression used to access the Java object
     * referenced by this Descriptor.
     *
     * @param xpath The new XPath expression
     */
    public void setXpath(String xpath) {

        this.xpath = xpath;

    }


    // --------------------------------------------------------- Public Methods


    /**
     * Return the value specified by this Descriptor from the specified
     * Context.  If there is no such value, return <code>null</code>.
     *
     * @param context Context from which to retrieve this value
     */
    public Object get(Context context) {

        if (xpath != null) {
            JXPathContext jpc = context.getJXPathContext();
            jpc.setLenient(true);
            return (jpc.getValue("local/" + xpath));
        } else if (name != null) {
            if (scope == null)
                return (context.get(name));
            else {
                int scopeId = context.getScopeId(scope);
                return (context.get(name, scopeId));
            }
        } else {
            try {
                return (context.pop());
            } catch (EmptyStackException e) {
                return (null);
            }
        }

    }


    /**
     * <p>Call <code>get()</code> to retrieve the value specified by this
     * Descriptor, and then return <code>true</code> if this value represents
     * a positive result; otherwise return <code>false</code>.  A positive
     * result depends on the data type of the retrieved value:</p>
     * <ul>
     * <li>Value returned is a <code>String</code> of length greater than 0.
     *     </li>
     * <li>Value is a Boolean "true"</code>.</li>
     * <li>Value is a numeric primitive (byte, char, float, double, int,
     *     long, short), or an Object wrapper for one of these types, and
     *     the corresponding value is non-zero.</li>
     * <li>Value returned is a non-null Object.</li>
     * </ul>
     *
     * @param context Context from which to retrieve this value
     */
    public boolean positive(Context context) {

        Object value = get(context);
        if (value == null)
            return (false);
        else if (value instanceof Boolean)
            return (((Boolean) value).booleanValue());
        else if (value instanceof Byte)
            return (((Byte) value).byteValue() != (byte) 0);
        else if (value instanceof Character)
            return (((Character) value).charValue() != (char) 0);
        else if (value instanceof Double)
            return (((Double) value).doubleValue() != (double) 0.0);
        else if (value instanceof Float)
            return (((Double) value).floatValue() != (float) 0.0);
        else if (value instanceof Integer)
            return (((Integer) value).intValue() != (int) 0);
        else if (value instanceof Long)
            return (((Long) value).longValue() != (long) 0);
        else if (value instanceof Short)
            return (((Short) value).shortValue() != (short) 0);
        else if (value instanceof String)
            return (((String) value).length() > 0);
        else
            return (true); // Non-null object


    }


    /**
     * Store the value into the destination specified by this Descriptor
     * in the specified Context, replacing any existing value.
     *
     * @param context Context into which to store this value
     * @param value Object value to be stored
     */
    public void put(Context context, Object value) {

        if (xpath != null) {
            JXPathContext jpc = context.getJXPathContext();
            jpc.setValue("local/" + xpath, value);
        } else {
            if (scope == null)
                context.put(name, value);
            else {
                int scopeId = context.getScopeId(scope);
                context.put(name, value, scopeId);
            }
        }

    }


    /**
     * Remove any existing value associated with this Descriptor from the
     * specified Context.
     *
     * @param context Context from which to remove this value.
     */
    public void remove(Context context) {

        if (xpath != null) {
            throw new IllegalStateException("Cannot 'remove' throw xpath");
        } else {
            if (scope == null)
                context.remove(name);
            else {
                int scopeId = context.getScopeId(scope);
                context.remove(name, scopeId);
            }
        }

    }


    /**
     * Render a printable version of this object.
     */
    public String toString() {

        StringBuffer sb = new StringBuffer("<descriptor");
        if (xpath != null) {
            sb.append(" xpath=\"");
            sb.append(xpath);
            sb.append("\"");
        }
        if (name != null) {
            sb.append(" name=\"");
            sb.append(name);
            sb.append("\"");
        }
        if (scope != null) {
            sb.append(" scope=\"");
            sb.append(scope);
            sb.append("\"");
        }
        sb.append("/>");
        return (sb.toString());

    }


}
