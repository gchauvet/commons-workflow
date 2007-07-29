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


import java.util.Map;


/**
 * <p>A <strong>Scope</strong> is a collection of arbitrary Java objects,
 * keyed by String-valued names.  Specialized workflow implementations
 * will register their own <code>Scope</code> implementations to connect
 * the workflow engine processing to their own execution environments.
 * For example, a web layer implementation would most likely adapt
 * Scopes to the request attributes, session attributes, and servlet
 * context attributes provided by the Servlet API.</p>
 *
 * <p>A Scope implements the API contracts of <code>java.util.Map</code>
 * with the following additional rules:</p>
 * <ul>
 * <li>Keys must be of the <code>java.lang.String</code></li>
 * <li>Null keys are not allowed</li>
 * <li>Null beans are not allowed</li>
 * </ul>
 *
 * @version $Revision$ $Date$
 * @author Craig R. McClanahan
 */

public interface Scope extends Map {


    // ------------------------------------------------------------ Map Methods


    /**
     * Remove all beans from this Map and call <code>scopeCleared() on
     * all registered <code>ScopeListeners</code>.
     */
    public void clear();


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
    public Object put(String key, Object bean);


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
    public Object remove(String key);


    // ------------------------------------------------- Event Listener Methods


    /**
     * Add a listener that is notified each time beans are added,
     * replaced, or removed in this scope.
     *
     * @param listener The ScopeListener to be added
     */
    public void addScopeListener(ScopeListener listener);


    /**
     * Remove a listener that is notified each time beans are added,
     * replaced, or removed in this scope.
     *
     * @param listener The ScopeListener to be removed
     */
    public void removeScopeListener(ScopeListener listener);


}
