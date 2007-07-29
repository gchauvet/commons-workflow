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


import java.util.ArrayList;
import org.apache.commons.workflow.Activity;
import org.apache.commons.workflow.Step;


/**
 * <p><strong>BaseActivity</strong> is a convenient base class for more
 * sophisticated <code>Activity</code> implementations.  It includes
 * management of the static relationships of Steps to each other as part
 * of an owning Activity.</p>
 *
 * @version $Revision$ $Date$
 * @author Craig R. McClanahan
 */

public class BaseActivity implements Activity {


    // ----------------------------------------------------- Instance Variables


    /**
     * The first Step associated with this Activity.
     */
    protected Step firstStep = null;


    /**
     * The unique identifier of this Activity.
     */
    protected String id = null;


    /**
     * The last Step associated with this Activity.
     */
    protected Step lastStep = null;


    // ------------------------------------------------------------- Properties


    /**
     * Return the first Step associated with this Activity.
     */
    public Step getFirstStep() {

        return (this.firstStep);

    }


    /**
     * Return the unique identifier of this Activity.
     */
    public String getId() {

        return (this.id);

    }


    /**
     * Set the unique identifier of this Activity.
     *
     * @param id The new unique identifier
     */
    public void setId(String id) {

        this.id = id;

    }


    /**
     * Return the last Step associated with this Activity.
     */
    public Step getLastStep() {

        return (this.lastStep);

    }


    // ---------------------------------------------------------- Owner Methods


    /**
     * Add a new Step to the end of the sequence of Steps associated with
     * this Activity.
     *
     * @param step The new step to be added
     */
    public void addStep(Step step) {

        step.setOwner(this);
        if (firstStep == null) {
            step.setPreviousStep(null);
            step.setNextStep(null);
            firstStep = step;
            lastStep = step;
        } else {
            step.setPreviousStep(lastStep);
            step.setNextStep(null);
            lastStep.setNextStep(step);
            lastStep = step;
        }

    }


    /**
     * Clear any existing Steps associated with this Activity.
     */
    public void clearSteps() {

        Step current = firstStep;
        while (current != null) {
            Step next = current.getNextStep();
            current.setOwner(null);
            current.setPreviousStep(null);
            current.setNextStep(null);
            current = next;
        }
        firstStep = null;
        lastStep = null;

    }


    /**
     * Return the identified Step from this Activity, if it exists.
     * Otherwise, return <code>null</code>.
     *
     * @param id Identifier of the desired Step
     */
    public Step findStep(String id) {

        Step current = getFirstStep();
        while (current != null) {
            if (id.equals(current.getId()))
                return (current);
            current = current.getNextStep();
        }
        return (null);

    }


    /**
     * Return the set of Steps associated with this Activity.
     */
    public Step[] getSteps() {

        ArrayList list = new ArrayList();
        Step currentStep = firstStep;
        while (currentStep != null) {
            list.add(currentStep);
            currentStep = currentStep.getNextStep();
        }
        Step steps[] = new Step[list.size()];
        return ((Step[]) list.toArray(steps));

    }


    /**
     * Set the set of Steps associated with this Activity, replacing any
     * existing ones.
     *
     * @param steps The new set of steps.
     */
    public void setSteps(Step steps[]) {

        clearSteps();
        for (int i = 0; i < steps.length; i++)
            addStep(steps[i]);

    }


}
