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
 * <p>An <strong>Activity</strong> represents an ordered sequence of Steps
 * that comprise the executable actions necessary to accomplish a business
 * task.  The dynamic execution of the Steps owned by this Activity happens
 * within the <code>execute()</code> method of a <code>Context</code> that
 * is utilizing this <code>Activity</code> definition.</p>
 *
 * @version $Revision$ $Date$
 * @author Craig R. McClanahan
 */

public interface Activity extends Owner {


    // ------------------------------------------------------------- Properties


    /**
     * Return the unique identifier (within this Activity) of this Step.
     */
    public String getId();


    /**
     * Set the unique identifier (within this Activity) of this Step.
     *
     * @param id The new unique identifier
     */
    public void setId(String id);


    // --------------------------------------------------------- Public Methods


}
