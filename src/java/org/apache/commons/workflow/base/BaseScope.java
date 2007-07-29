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


import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.apache.commons.jxpath.JXPathIntrospector;
import org.apache.commons.jxpath.MapDynamicPropertyHandler;
import org.apache.commons.workflow.Scope;
import org.apache.commons.workflow.ScopeListener;
import org.apache.commons.workflow.util.ScopeSupport;


/**
 * <strong>BaseScope</strong> is a basic <code>Scope</code> implementation
 * that maintains its bean collection in an in-memory HashMap.  This can
 * also serve as a convenient base class for more sophisticated
 * <code>Scope</code> implementations.
 *
 * <p><strong>WARNING</strong> - No synchronization is performed within this
 * class.  If it is used in a multiple thread environment, callers must
 * take suitable precations.</p>
 *
 * @version $Revision$ $Date$
 * @author Craig R. McClanahan
 */

public class BaseScope implements Scope {


    // -------------------------------------------------- Static Initialization


    /**
     * Register ourselves with JXPathIntrospector as an instance of a
     * dynamic class (in JXPath terminology).
     */
    static {
        JXPathIntrospector.registerDynamicClass
            (BaseScope.class, MapDynamicPropertyHandler.class);
    }


    // ----------------------------------------------------- Instance Variables


    /**
     * The HashMap that contains our registered keys and beans.
     */
    protected HashMap map = new HashMap();


    /**
     * The event listener support object for this <code>Scope</code>.
     */
    protected ScopeSupport support = new ScopeSupport(this);


    // ------------------------------------------------------------ Map Methods


    /**
     * Remove all beans from this Map and call <code>scopeCleared() on
     * all registered <code>ScopeListeners</code>.
     */
    public void clear() {

        map.clear();
        support.fireScopeCleared();

    }


    /**
     * Return <code>true</code> if this map contains the specified key.
     *
     * @param key Key to be looked up
     */
    public boolean containsKey(Object key) {

        return (map.containsKey(key));

    }


    /**
     * Return <code>true</code> if this map contains the specified value.
     *
     * @param value Value to be looked up
     */
    public boolean containsValue(Object value) {

        return (map.containsValue(value));

    }


    /**
     * Return a set view of the mappings contained in this map.
     */
    public Set entrySet() {

        return (map.entrySet());

    }


    /**
     * Compare the specified object with this map for equality.
     *
     * @param object Object to be compared
     */
    public boolean equals(Object object) {

        return (map.equals(object));

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

        return (map.get(key));

    }


    /**
     * Return the hash code value for this map.
     */
    public int hashCode() {

        return (map.hashCode());

    }


    /**
     * Return <code>true</code> if this map is empty.
     */
    public boolean isEmpty() {

        return (map.isEmpty());

    }


    /**
     * Return a set view of the keys contained in this map.
     */
    public Set keySet() {

        return (map.keySet());

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

        Object old = get(key);
        if (map.containsKey(key)) {
            map.put(key, bean);
            support.fireBeanReplaced(key, old);
        } else {
            map.put(key, bean);
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

        if (map.containsKey(key)) {
            Object old = map.remove(key);
            support.fireBeanRemoved(key, old);
            return (old);
        }
        return (null);

    }


    /**
     * Return the number of key-value mappings in this map.
     */
    public int size() {

        return (map.size());

    }


    /**
     * Return a Collection view of the values contained in this map.
     */
    public Collection values() {

        return (map.values());

    }


    // ------------------------------------------------- Event Listener Methods


    /**
     * Add a listener that is notified each time beans are added,
     * replaced, or removed in this scope.
     *
     * @param listener The ScopeListener to be added
     */
    public void addScopeListener(ScopeListener listener) {

        support.addScopeListener(listener);

    }


    /**
     * Remove a listener that is notified each time beans are added,
     * replaced, or removed in this scope.
     *
     * @param listener The ScopeListener to be removed
     */
    public void removeScopeListener(ScopeListener listener) {

        support.removeScopeListener(listener);

    }


}
