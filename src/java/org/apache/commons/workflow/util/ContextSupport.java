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
import org.apache.commons.workflow.Context;
import org.apache.commons.workflow.ContextEvent;
import org.apache.commons.workflow.ContextListener;
import org.apache.commons.workflow.Step;
import org.apache.commons.workflow.StepException;


/**
 * <strong>ContextSupport</strong> is a convenience class for managing the
 * firing of <code>ContextEvents</code> to registered
 * <code>ContextListeners</code>.
 *
 * @version $Revision$ $Date$
 * @author Craig R. McClanahan
 */

public class ContextSupport {


    // ----------------------------------------------------------- Constructors


    /**
     * Construct a new ContextSupport object associated with the
     * specified Context.
     *
     * @param context Context for whom we will fire events
     */
    public ContextSupport(Context context) {

        super();
        this.context = context;

    }


    // ----------------------------------------------------- Instance Variables


    /**
     * The set of registered <code>ContextListener</code> event listeners.
     */
    protected ContextListener listeners[] = new ContextListener[0];


    /**
     * The <code>Context</code> for whom we will fire events.
     */
    protected Context context = null;


    // ------------------------------------------------- Event Listener Methods


    /**
     * Add a listener that is notified each time beans are added,
     * replaced, or removed in this context.
     *
     * @param listener The ContextListener to be added
     */
    public void addContextListener(ContextListener listener) {

      synchronized (listeners) {
          ContextListener results[] =
            new ContextListener[listeners.length + 1];
          System.arraycopy(listeners, 0, results, 0, listeners.length);
          results[listeners.length] = listener;
          listeners = results;
      }

    }


    /**
     * Remove a listener that is notified each time beans are added,
     * replaced, or removed in this context.
     *
     * @param listener The ContextListener to be removed
     */
    public void removeContextListener(ContextListener listener) {

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
            ContextListener results[] =
              new ContextListener[listeners.length - 1];
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
     * Fire a <code>afterActivity</code> event to all registered listeners.
     *
     * @param step Step that was executed last
     */
    public void fireAfterActivity(Step step) {

        if (listeners.length == 0)
            return;
        ContextEvent event = new ContextEvent(context, step);
        ContextListener interested[] = null;
        synchronized (listeners) {
            interested = (ContextListener[]) listeners.clone();
        }
        for (int i = 0; i < interested.length; i++)
            interested[i].afterActivity(event);

    }


    /**
     * Fire a <code>afterActivity</code> event to all registered listeners.
     *
     * @param step Step that was executed last
     * @param exception StepException thrown by the last Step
     */
    public void fireAfterActivity(Step step, StepException exception) {

        if (listeners.length == 0)
            return;
        ContextEvent event = new ContextEvent(context, step, exception);
        ContextListener interested[] = null;
        synchronized (listeners) {
            interested = (ContextListener[]) listeners.clone();
        }
        for (int i = 0; i < interested.length; i++)
            interested[i].afterActivity(event);

    }


    /**
     * Fire a <code>afterStep</code> event to all registered listeners.
     *
     * @param step Step that was executed
     */
    public void fireAfterStep(Step step) {

        if (listeners.length == 0)
            return;
        ContextEvent event = new ContextEvent(context, step);
        ContextListener interested[] = null;
        synchronized (listeners) {
            interested = (ContextListener[]) listeners.clone();
        }
        for (int i = 0; i < interested.length; i++)
            interested[i].afterStep(event);

    }


    /**
     * Fire a <code>afterStep</code> event to all registered listeners.
     *
     * @param step Step that was executed
     * @param exception StepException thrown by the executed step
     */
    public void fireAfterStep(Step step, StepException exception) {

        if (listeners.length == 0)
            return;
        ContextEvent event = new ContextEvent(context, step, exception);
        ContextListener interested[] = null;
        synchronized (listeners) {
            interested = (ContextListener[]) listeners.clone();
        }
        for (int i = 0; i < interested.length; i++)
            interested[i].afterStep(event);

    }


    /**
     * Fire a <code>beforeActivity</code> event to all registered listeners.
     *
     * @param step Step that will be executed first
     */
    public void fireBeforeActivity(Step step) {

        if (listeners.length == 0)
            return;
        ContextEvent event = new ContextEvent(context, step);
        ContextListener interested[] = null;
        synchronized (listeners) {
            interested = (ContextListener[]) listeners.clone();
        }
        for (int i = 0; i < interested.length; i++)
            interested[i].beforeActivity(event);

    }


    /**
     * Fire a <code>beforeStep</code> event to all registered listeners.
     *
     * @param step Step that is about to be executed
     */
    public void fireBeforeStep(Step step) {

        if (listeners.length == 0)
            return;
        ContextEvent event = new ContextEvent(context, step);
        ContextListener interested[] = null;
        synchronized (listeners) {
            interested = (ContextListener[]) listeners.clone();
        }
        for (int i = 0; i < interested.length; i++)
            interested[i].beforeStep(event);

    }


}
