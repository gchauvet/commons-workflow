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
import java.util.EmptyStackException;
import org.apache.commons.workflow.Activity;
import org.apache.commons.workflow.Block;
import org.apache.commons.workflow.BlockState;
import org.apache.commons.workflow.Context;
import org.apache.commons.workflow.Owner;
import org.apache.commons.workflow.Step;
import org.apache.commons.workflow.StepException;


/**
 * <p><strong>BaseBlock</strong> is a convenient base class for more
 * sophisticated <code>Block</code> implementations.  It includes management
 * of the static relationships of nested Steps for this Step (each of which
 * could conceptually also be a Block and have its own nested Steps).</p>
 *
 * @version $Revision$ $Date$
 * @author Craig R. McClanahan
 */

public abstract class BaseBlock extends DescriptorStep implements Block {


    // ----------------------------------------------------- Instance Variables


    /**
     * The first Step associated with this Block.
     */
    protected Step firstStep = null;


    /**
     * The last Step associated with this Block.
     */
    protected Step lastStep = null;


    // ------------------------------------------------------------- Properties


    /**
     * Return the first Step associated with this Block.
     */
    public Step getFirstStep() {

        return (this.firstStep);

    }


    /**
     * Return the last Step associated with this Activity.
     */
    public Step getLastStep() {

        return (this.lastStep);

    }


    // ---------------------------------------------------------- Owner Methods


    /**
     * <p>Add a new Step to the end of the sequence of Steps associated with
     * this Block.</p>
     *
     * <p><strong>IMPLEMENTATION NOTE</strong> - The last nested Step is looped
     * back to the owning Block in order to support the execution flow of
     * control required by our BaseContext.</p>
     *
     * @param step The new step to be added
     */
    public void addStep(Step step) {

        step.setOwner(this);
        if (firstStep == null) {
            step.setPreviousStep(null);
            step.setNextStep(this);
            firstStep = step;
            lastStep = step;
        } else {
            step.setPreviousStep(lastStep);
            step.setNextStep(this);
            lastStep.setNextStep(step);
            lastStep = step;
        }

    }


    /**
     * Clear any existing Steps associated with this Block.
     */
    public void clearSteps() {

        Step current = firstStep;
        while ((current != null) && (current != this)) {
            Step next = current.getNextStep();
            if (current instanceof Block)
                ((Block) current).clearSteps();
            current.setOwner(null);
            current.setPreviousStep(null);
            current.setNextStep(null);
            current = next;
        }
        firstStep = null;
        lastStep = null;

    }


    /**
     * Return the identified Step from this Block, if it exists.
     * Otherwise, return <code>null</code>.
     *
     * @param id Identifier of the desired Step
     */
    public Step findStep(String id) {

        Step currentStep = getFirstStep();
        while (currentStep != null) {
            if (id.equals(currentStep.getId()))
                return (currentStep);
            if (currentStep == lastStep)
                break;
            currentStep = currentStep.getNextStep();
        }
        return (null);

    }


    /**
     * Return the set of Steps associated with this Block.
     */
    public Step[] getSteps() {

        ArrayList list = new ArrayList();
        Step currentStep = firstStep;
        while (currentStep != null) {
            list.add(currentStep);
            if (currentStep == lastStep)
                break;
            currentStep = currentStep.getNextStep();
        }
        Step steps[] = new Step[list.size()];
        return ((Step[]) list.toArray(steps));

    }


    /**
     * Set the set of Steps associated with this Block, replacing any
     * existing ones.
     *
     * @param steps The new set of steps.
     */
    public void setSteps(Step steps[]) {

        clearSteps();
        for (int i = 0; i < steps.length; i++)
            addStep(steps[i]);

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
    public void execute(Context context) throws StepException {

        BlockState state = state(context);
        if (state == null)
            initial(context);
        else
            subsequent(context, state);

    }


    // ------------------------------------------------------ Protected Methods


    /**
     * <p>Evaluate the condition specified by the Descriptors associated with
     * this Block, and return the resulting boolean value.  The default
     * implementation returns <code>false</code> unconditionally.</p>
     *
     * @param context Context within which to evaluate the descriptors
     */
    protected boolean evaluate(Context context) {

        return (false);

    }


    /**
     * <p>Process the initial entry into this Block.  The default
     * implementation unconditionally skips the nested Steps.</p>
     *
     * @param context Context within which to evaluate the condition
     */
    protected void initial(Context context) {

        context.setNextStep(getNextStep());

    }


    /**
     * <p>Peek at the top <code>BlockState</code> element on the stack
     * maintained by our <code>Context</code>, and return it.  If there
     * is no such top element, return <code>null</code> instead.</p>
     *
     * @param context Context within which to evaluate the current BlockState
     */
    protected BlockState state(Context context) {

        try {
            BlockState state = context.peekBlockState();
            if (this == state.getBlock())
                return (state);
        } catch (EmptyStackException e) {
            ;
        }
        return (null);

    }


    /**
     * <p>Process the return from nested execution of the Steps associated
     * with this Block.  The default implementation unconditionally
     * proceeds to the next Step at the current nesting level, without
     * iterating again.</p>
     *
     * @param context Context within which to evaluate the condition
     * @param state BlockState for our block
     */
    protected void subsequent(Context context, BlockState state) {

        context.popBlockState();
        context.setNextStep(getNextStep());

    }


}
