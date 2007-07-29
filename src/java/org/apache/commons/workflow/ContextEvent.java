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


import java.util.EventObject;


/**
 * A <strong>ContextEvent</strong> provides notification to a
 * <code>ContextListener</code> that a specified event has occurred for
 * the specified context.
 *
 * @version $Revision$ $Date$
 * @author Craig R. McClanahan
 */

public class ContextEvent extends EventObject {


    // ----------------------------------------------------------- Constructors


    /**
     * Construct a new immutable ContextEvent.
     *
     * @param context Context in which this event occurred
     */
    public ContextEvent(Context context) {

        this(context, null, null);

    }
        

    /**
     * Construct a new immutable ContextEvent.
     *
     * @param context Context upon which this event occurred
     * @param step Step this event is associated with (if any)
     */
    public ContextEvent(Context context, Step step) {

        this(context, step, null);

    }


    /**
     * Construct a new immutable ContextEvent.
     *
     *
     * @param context Context upon which this event occurred
     * @param step Step this event is associated with (if any)
     * @param exception StepException that was thrown
     *  (<code>afterStep()</code> and <code>afterActivity()</code> only)
     */
    public ContextEvent(Context context, Step step, StepException exception) {

        super(context);
        this.context = context;
        this.step = step;
        this.exception = exception;

    }
        

    // ------------------------------------------------------------- Properties


    /**
     * The <code>Context</code> upon which this event occurred.
     */
    protected Context context = null;

    public Context getContext() {

        return (this.context);

    }


    /**
     * The <code>StepException</code> that caused this event.
     */
    protected StepException exception = null;

    public StepException getException() {

        return (this.exception);

    }


    /**
     * The <code>Step</code> upon which this event occurred.  For
     * <code>beanReplaced</code> events, this will be the previous step.
     */
    protected Step step = null;

    public Step getStep() {

        return (this.step);

    }


}
