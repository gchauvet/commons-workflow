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


import org.apache.commons.workflow.Activity;
import org.apache.commons.workflow.Context;
import org.apache.commons.workflow.Owner;
import org.apache.commons.workflow.Step;
import org.apache.commons.workflow.StepException;


/**
 * <p><strong>BaseStep</strong> is a convenient base class for more sophisticated
 * <code>Step</code> implementations.  It includes management of the static
 * relationships of Steps to each other, and to their owning Activity, but
 * requires the implementation to provide an <code>execute()</code> method.
 *
 * @version $Revision$ $Date$
 * @author Craig R. McClanahan
 */

public abstract class BaseStep implements Step {


    // ----------------------------------------------------- Instance Variables


    /**
     * The unique identifier (within this Activity) of this Step.
     */
    protected String id = null;


    /**
     * The next Step in our associated Activity.
     */
    protected Step nextStep = null;


    /**
     * The Activity or Block that owns this Step.
     */
    protected Owner owner = null;



    /**
     * The previous Step in our associated Activity.
     */
    protected Step previousStep = null;



    // ------------------------------------------------------------- Properties


    /**
     * Return the unique identifier (within this Activity or Block)
     * of this Step.
     */
    public String getId() {

        return (this.id);

    }


    /**
     * Set the unique identifier (within this Activity or Block)
     * of this Step.
     *
     * @param id The new unique identifier
     */
    public void setId(String id) {

        this.id = id;

    }


    /**
     * Return the next Step in our associated Activity or Block.
     */
    public Step getNextStep() {

        return (this.nextStep);

    }


    /**
     * Set the next Step in our associated Activity or Block.
     *
     * @param nextStep The new next Step
     */
    public void setNextStep(Step nextStep) {

        this.nextStep = nextStep;

    }


    /**
     * Return the Activity or Block that owns this Step.
     */
    public Owner getOwner() {

        return (this.owner);

    }


    /**
     * Set the Activity or Block that owns this Step.
     *
     * @param owner The new owning Activity or Block
     */
    public void setOwner(Owner owner) {

        this.owner = owner;

    }


    /**
     * Return the previous Step in our associated Activity or Block.
     */
    public Step getPreviousStep() {

        return (this.previousStep);

    }


    /**
     * Set the previous Step in our associated Activity or Block.
     *
     * @param previousStep The new previous Step
     */
    public void setPreviousStep(Step previousStep) {

        this.previousStep = previousStep;

    }


    // --------------------------------------------------------- Public Methods


    /**
     * Perform the executable actions related to this Step, in the context of
     * the specified Context.
     *
     * @param context The Context that is tracking our execution state
     *
     * @exception StepException if a processing error has occurred
     */
    public abstract void execute(Context context) throws StepException;


}
