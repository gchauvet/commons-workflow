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
 * <p>A <strong>Step</strong> represents a single executable action that
 * can occur during the completion of an Activity.  The dynamic execution
 * of the implementation of this Step happens within the <code>execute()</code>
 * method -- everything else about a Step is part of its static definition,
 * which is shared among all users of the owning Activity.</p>
 *
 * @version $Revision$ $Date$
 * @author Craig R. McClanahan
 */

public interface Step {


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


    /**
     * Return the next Step in our associated Activity or Block.
     */
    public Step getNextStep();


    /**
     * Set the next Step in our associated Activity or Block.
     *
     * @param nextStep The new next Step
     */
    public void setNextStep(Step nextStep);


    /**
     * Return the Activity or Block that owns this Step.
     */
    public Owner getOwner();


    /**
     * Set the Activity or Block that owns this Step.
     *
     * @param owner The new owning Activity or Block
     */
    public void setOwner(Owner owner);


    /**
     * Return the previous Step in our associated Activity or Block.
     */
    public Step getPreviousStep();


    /**
     * Set the previous Step in our associated Activity or Block.
     *
     * @param previousStep The new previous Step
     */
    public void setPreviousStep(Step previousStep);


    // --------------------------------------------------------- Public Methods


    /**
     * Perform the executable actions related to this Step, in the context of
     * the specified Context.
     *
     * @param context The Context that is tracking our execution state
     *
     * @exception StepException if a processing error has occurred
     */
    public void execute(Context context) throws StepException;


}
