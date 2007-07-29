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


import java.util.EventListener;


/**
 * A <strong>ContextListener</strong> registers its interest in receiving
 * <code>ContextEvent</code> notifications when the occur on a particular
 * <code>Context</code> of interest.
 *
 * @version $Revision$ $Date$
 * @author Craig R. McClanahan
 */

public interface ContextListener extends EventListener {


    // --------------------------------------------------------- Public Methods


    /**
     * Invoked immediately after execution of the related Activity has
     * been completed normally, been suspended, or been aborted by
     * the throwing of a StepException.  The Step included in this event
     * will be the last one to be executed.
     *
     * @param event The <code>ContextEvent</code> that has occurred
     */
    public void afterActivity(ContextEvent event);



    /**
     * Invoked immediately after the specified Step was executed.
     *
     * @param event The <code>ContextEvent</code> that has occurred
     */
    public void afterStep(ContextEvent event);


    /**
     * Invoked immediately before execution of the related Activity has
     * started.  The Step included in this event will be the first one
     * to be executed.
     *
     * @param event The <code>ContextEvent</code> that has occurred
     */
    public void beforeActivity(ContextEvent event);


    /**
     * Invoked immediately before the specified Step is executed.
     *
     * @param event The <code>ContextEvent</code> that has occurred
     */
    public void beforeStep(ContextEvent event);


}
