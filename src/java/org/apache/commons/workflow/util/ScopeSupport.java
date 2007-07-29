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

package org.apache.commons.workflow.util;


import java.util.Map;
import org.apache.commons.workflow.Scope;
import org.apache.commons.workflow.ScopeEvent;
import org.apache.commons.workflow.ScopeListener;


/**
 * <strong>ScopeSupport</strong> is a convenience class for managing the
 * firing of <code>ScopeEvents</code> to registered
 * <code>ScopeListeners</code>.
 *
 * @version $Revision$ $Date$
 * @author Craig R. McClanahan
 */

public class ScopeSupport {


    // ----------------------------------------------------------- Constructors


    /**
     * Construct a new ScopeSupport object associated with the specified Scope.
     *
     * @param scope Scope for whom we will fire events
     */
    public ScopeSupport(Scope scope) {

        super();
        this.scope = scope;

    }


    // ----------------------------------------------------- Instance Variables


    /**
     * The set of registered <code>ScopeListener</code> event listeners.
     */
    protected ScopeListener listeners[] = new ScopeListener[0];


    /**
     * The <code>Scope</code> for whom we will fire events.
     */
    protected Scope scope = null;


    // ------------------------------------------------- Event Listener Methods


    /**
     * Add a listener that is notified each time beans are added,
     * replaced, or removed in this scope.
     *
     * @param listener The ScopeListener to be added
     */
    public void addScopeListener(ScopeListener listener) {

      synchronized (listeners) {
          ScopeListener results[] =
            new ScopeListener[listeners.length + 1];
          System.arraycopy(listeners, 0, results, 0, listeners.length);
          results[listeners.length] = listener;
          listeners = results;
      }

    }


    /**
     * Remove a listener that is notified each time beans are added,
     * replaced, or removed in this scope.
     *
     * @param listener The ScopeListener to be removed
     */
    public void removeScopeListener(ScopeListener listener) {

        synchronized (listeners) {
            int n = -1;
            for (int i = 0; i < listeners.length; i++) {
                if (listeners[i] == listener) {
                    n = i;
                    break;
                }
            }
            if (n < 0)
                return;
            ScopeListener results[] =
              new ScopeListener[listeners.length - 1];
            int j = 0;
            for (int i = 0; i < listeners.length; i++) {
                if (i != n)
                    results[j++] = listeners[i];
            }
            listeners = results;
        }

    }


    // --------------------------------------------------- Event Firing Methods


    /**
     * Fire a <code>beanAdded()</code> event to all registered listeners.
     *
     * @param key Key of the bean that was added
     * @param value Value of the bean that was added
     */
    public void fireBeanAdded(String key, Object value) {

        if (listeners.length == 0)
            return;
        ScopeEvent event = new ScopeEvent(scope, key, value);
        ScopeListener interested[] = null;
        synchronized (listeners) {
            interested = (ScopeListener[]) listeners.clone();
        }
        for (int i = 0; i < interested.length; i++)
            interested[i].beanAdded(event);

    }


    /**
     * Fire a <code>beanRemoved()</code> event to all registered listeners.
     *
     * @param key Key of the bean that was removed
     * @param value Value of the bean that was removed
     */
    public void fireBeanRemoved(String key, Object value) {

        if (listeners.length == 0)
            return;
        ScopeEvent event = new ScopeEvent(scope, key, value);
        ScopeListener interested[] = null;
        synchronized (listeners) {
            interested = (ScopeListener[]) listeners.clone();
        }
        for (int i = 0; i < interested.length; i++)
            interested[i].beanRemoved(event);

    }


    /**
     * Fire a <code>beanReplaced()</code> event to all registered listeners.
     *
     * @param key Key of the bean that was replaced
     * @param value Old value of the bean that was replaced
     */
    public void fireBeanReplaced(String key, Object value) {

        if (listeners.length == 0)
            return;
        ScopeEvent event = new ScopeEvent(scope, key, value);
        ScopeListener interested[] = null;
        synchronized (listeners) {
            interested = (ScopeListener[]) listeners.clone();
        }
        for (int i = 0; i < interested.length; i++)
            interested[i].beanReplaced(event);

    }


    /**
     * Fire a <code>scopeCleared()</code> event to all registered listeners.
     */
    public void fireScopeCleared() {

        if (listeners.length == 0)
            return;
        ScopeEvent event = new ScopeEvent(scope, null, null);
        ScopeListener interested[] = null;
        synchronized (listeners) {
            interested = (ScopeListener[]) listeners.clone();
        }
        for (int i = 0; i < interested.length; i++)
            interested[i].scopeCleared(event);

    }


}
