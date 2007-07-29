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

package org.apache.commons.workflow.web;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.servlet.ServletContext;
import org.apache.commons.workflow.base.BaseScope;
import org.apache.commons.workflow.util.MapEntry;


/**
 * <strong>ServletContextScope</strong> is a specialized <code>Scope</code>
 * implementation corresponding the the attributes of a specified
 * <code>ServletContext</code>.
 *
 * @version $Revision$ $Date$
 * @author Craig R. McClanahan
 */

public class ServletContextScope extends BaseScope {


    // ----------------------------------------------------------- Constructors


    /**
     * Construct a new ServletContextScope with no attached ServletContext.
     */
    public ServletContextScope() {

        super();

    }


    /**
     * Construct a ServletContextScope associated with the specified
     * ServletContext.
     *
     * @param servletContext The associated ServletContext
     */
    public ServletContextScope(ServletContext servletContext) {

        super();
        setServletContext(servletContext);

    }


    // ------------------------------------------------------------- Properties


    /**
     * The servlet context with which we are associated.
     */
    protected ServletContext servletContext = null;

    public ServletContext getServletContext() {
        return (this.servletContext);
    }

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }


    // ------------------------------------------------------------ Map Methods


    /**
     * Remove all beans from this Map and call <code>scopeCleared() on
     * all registered <code>ScopeListeners</code>.
     */
    public void clear() {

        // Accumulate a list of the elements to be cleared
        Enumeration names = servletContext.getAttributeNames();
        ArrayList list = new ArrayList();
        while (names.hasMoreElements()) {
            list.add((String) names.nextElement());
        }

        // Erase the accumulated elements
        int n = list.size();
        for (int i = 0; i < n; i++) {
            servletContext.removeAttribute((String) list.get(i));
        }
        support.fireScopeCleared();

    }


    /**
     * Return <code>true</code> if this map contains the specified key.
     *
     * @param key Key to be looked up
     */
    public boolean containsKey(Object key) {

        return (servletContext.getAttribute((String) key) != null);

    }


    /**
     * Return <code>true</code> if this map contains the specified value.
     *
     * @param value Value to be looked up
     */
    public boolean containsValue(Object value) {

        // Check all existing attributes for a match
        Enumeration names = servletContext.getAttributeNames();
        while (names.hasMoreElements()) {
            String name = (String) names.nextElement();
            if (value.equals(servletContext.getAttribute(name)))
                return (true);
        }
        return (false);

    }


    /**
     * Return a set view of the mappings contained in this map.
     */
    public Set entrySet() {

        HashSet results = new HashSet();
        Enumeration names = servletContext.getAttributeNames();
        while (names.hasMoreElements()) {
            String name = (String) names.nextElement();
            results.add(new MapEntry(name, servletContext.getAttribute(name)));
        }
        return (results);

    }


    /**
     * Compare the specified object with this map for equality.
     *
     * @param object Object to be compared
     */
    public boolean equals(Object object) {

        return (servletContext.equals(object));

    }


    /**
     * Return the value to which this map maps the specified key.
     *
     * @param key Key to be looked up
     */
    public Object get(Object key) {

        return (get((String) key));

    }


    /**
     * Return the value to which this map maps the specified key.
     *
     * @param key Key to be looked up
     */
    public Object get(String key) {

        return (servletContext.getAttribute(key));

    }


    /**
     * Return the hash code value for this map.
     */
    public int hashCode() {

        return (servletContext.hashCode());

    }


    /**
     * Return <code>true</code> if this map is empty.
     */
    public boolean isEmpty() {

        // Check all existing attributes
        Enumeration names = servletContext.getAttributeNames();
        while (names.hasMoreElements())
            return (true);
        return (false);

    }


    /**
     * Return a set view of the keys contained in this map.
     */
    public Set keySet() {

        HashSet results = new HashSet();
        Enumeration names = servletContext.getAttributeNames();
        while (names.hasMoreElements())
            results.add(names.nextElement());
        return (results);

    }


    /**
     * Add or replace the bean associated with the specified key.
     *
     * @param key Key with which the new value should be associated
     *  (cannot be null)
     * @param bean Bean to be associated with this key (cannot be null)
     */
    public Object put(Object key, Object bean) {

        return (put((String) key, bean));

    }


    /**
     * Add the specified bean, associated with the specified key, to this
     * scope and replace any previous bean associated with this key.  If
     * the bean was added, call <code>beanAdded()</code> on all registered
     * listeners after the add is done.  If an old bean was replaced,
     * call <code>beanReplaced()</code> (passing the old value in the event)
     * on all registered <code>ScopeListeners</code> after the removal
     * is done.  If a bean was replaced, the old value is also returned;
     * otherwise <code>null</code> is returned.
     *
     * @param key Key with which the new value should be associated
     *  (cannot be null)
     * @param bean Bean to be associated with this key (cannot be null)
     *
     * @exception IllegalArgumentException if <code>key</code> or
     *  <code>bean</code> is null
     */
    public Object put(String key, Object bean) {

        if (key == null)
            throw new IllegalArgumentException("Key cannot be null");
        if (bean == null)
            throw new IllegalArgumentException("Value cannot be null");

        Object old = servletContext.getAttribute(key);
        if (old != null) {
            servletContext.setAttribute(key, bean);
            support.fireBeanReplaced(key, old);
        } else {
            servletContext.setAttribute(key, bean);
            support.fireBeanAdded(key, bean);
        }
        return (old);
            
    }


    /**
     * Copy all of the mappings from the specified map into this map,
     * firing appropriate <code>beanAdded()</code> and
     * <code>beanReplaced()</code> events along the way.
     *
     * @param in Map whose contents are to be added
     */
    public void putAll(Map in) {

        Iterator keys = in.keySet().iterator();
        while (keys.hasNext()) {
            Object key = keys.next();
            put(key, in.get(key));
        }

    }


    /**
     * Remove the bean associated with the specified key (if any), and return
     * the old value if removed.
     *
     * @param key Key of the bean to remove (cannot be null)
     */
    public Object remove(Object key) {

        return (remove((String) key));

    }



    /**
     * Remove the bean associated with the specified key (if any).  If such
     * a bean is found and removed, call <code>beanRemoved()</code> on all
     * registered <code>ScopeListeners</code> after the removal is done.
     * Return the old value (if any); otherwise return <code>null</code>.
     *
     * @param key Key of the bean to remove (cannot be null)
     *
     * @exception IllegalArgumentException if <code>key</code> is null
     */
    public Object remove(String key) {

        Object old = servletContext.getAttribute(key);
        if (old != null) {
            support.fireBeanRemoved(key, old);
            return (old);
        }
        return (null);

    }


    /**
     * Return the number of key-value mappings in this map.
     */
    public int size() {

        Enumeration names = servletContext.getAttributeNames();
        int n = 0;
        while (names.hasMoreElements()) {
            n++;
        }
        return (n);

    }


    /**
     * Return a Collection view of the values contained in this map.
     */
    public Collection values() {

        ArrayList results = new ArrayList();
        Enumeration names = servletContext.getAttributeNames();
        while (names.hasMoreElements()) {
            String name = (String) names.nextElement();
            results.add(servletContext.getAttribute(name));
        }
        return (results);

    }


}
