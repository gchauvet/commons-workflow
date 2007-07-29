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

package org.apache.commons.workflow;


/**
 * <p>A <strong>Descriptor</strong> is a description of the mechanism by which
 * an arbitrary Java object (typically a JavaBean) is referenced.  The
 * following reference methods are supported, and are processed in the
 * order specified here:</p>
 * <ul>
 * <li>If the <code>xpath</code> property is set, it is used as an XPath
 *     expression identifying the requested object.</li>
 * <li>If the <code>name</code> (and optional <code>scope</code>) properties
 *     are specified, they are used to select a particular named bean,
 *     optionally found in a particular named scope.</li>
 * <li>If none of the conditions above are satisfied, the top object on the
 *     evaluation stack of our current <code>Context</code> is consumed.</li>
 * </ul>
 *
 * @version $Revision$ $Date$
 * @author Craig R. McClanahan
 */

public interface Descriptor {


    // ------------------------------------------------------------- Properties


    /**
     * Return the name of the Java object (in some scope) referenced by
     * this Descriptor.
     */
    public String getName();


    /**
     * Set the name of the Java object (in some scope) referenced by
     * this Descriptor.
     *
     * @param name The new object name
     */
    public void setName(String name);


    /**
     * Return the scope of the Java object referenced by this Descriptor.
     */
    public String getScope();


    /**
     * Set the scope of the Java object referenced by this Descriptor.
     *
     * @param scope The new scope name
     */
    public void setScope(String scope);


    /**
     * Return the optional Java class expected by this Descriptor.
     */
    public Class getType();


    /**
     * Set the optional Java class expected by this Descriptor.
     *
     * @param type The new expected type
     */
    public void setType(Class type);


    /**
     * Return the XPath expression used to access the Java object
     * referenced by this Descriptor.
     */
    public String getXpath();


    /**
     * Set the XPath expression used to access the Java object
     * referenced by this Descriptor.
     *
     * @param xpath The new XPath expression
     */
    public void setXpath(String xpath);


    // --------------------------------------------------------- Public Methods


    /**
     * Return the value specified by this Descriptor from the specified
     * Context.  If there is no such value, return <code>null</code>.
     *
     * @param context Context from which to retrieve this value
     */
    public Object get(Context context);


    /**
     * <p>Call <code>get()</code> to retrieve the value specified by this
     * Descriptor, and then return <code>true</code> if this value represents
     * a positive result; otherwise return <code>false</code>.</p>
     *
     * @param context Context from which to retrieve this value
     */
    public boolean positive(Context context);


    /**
     * Store the value into the destination specified by this Descriptor
     * in the specified Context, replacing any existing value.
     *
     * @param context Context into which to store this value
     * @param value Object value to be stored
     */
    public void put(Context context, Object value);


    /**
     * Remove any existing value associated with this Descriptor from the
     * specified Context.
     *
     * @param context Context from which to remove this value.
     */
    public void remove(Context context);


}
