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

package org.apache.commons.workflow.core;


import java.util.EmptyStackException;
import org.apache.commons.workflow.Block;
import org.apache.commons.workflow.BlockState;
import org.apache.commons.workflow.Context;
import org.apache.commons.workflow.Descriptor;
import org.apache.commons.workflow.Iterator;
import org.apache.commons.workflow.Step;
import org.apache.commons.workflow.StepException;
import org.apache.commons.workflow.base.BaseBlock;


/**
 * <p>Repeatedly evaluate the properties specified by the associated
 * Descriptors, and execute the nested Steps if and only if
 * <strong>ALL</strong> of them evaluate to a positive result.
 * To avoid non-deterministic evaluation
 * stack behavior, all of the specified Descriptors are always
 * evaluated exactly once.</p>
 *
 * @version $Revision$ $Date$
 * @author Craig R. McClanahan
 */

public class WhileStep extends BaseBlock implements Iterator {


    // ----------------------------------------------------------= Constructors


    /**
     * Construct a default instance of this Step.
     */
    public WhileStep() {

        super();

    }


    /**
     * Construct an instance of this Step with the specified identifier.
     *
     * @param id Step identifier
     */
    public WhileStep(String id) {

        this(id, null);

    }


    /**
     * Construct a fully configured instance of this Step.
     *
     * @param id Step identifier of this step
     * @param descriptor Initial descriptor to be added
     */
    public WhileStep(String id, Descriptor descriptor) {

        super();
        setId(id);
        if (descriptor != null)
            addDescriptor(descriptor);

    }


    // --------------------------------------------------------- Public Methods


    /**
     * Render a string representation of this Step.
     */
    public String toString() {

        StringBuffer sb = new StringBuffer("<core:while");
        if (getId() != null) {
            sb.append(" id=\"");
            sb.append(getId());
            sb.append("\"");
        }
        sb.append(">");
        Descriptor descriptors[] = findDescriptors();
        for (int i = 0; i < descriptors.length; i++)
            sb.append(descriptors[i]);
        Step steps[] = getSteps();
        for (int i = 0; i < steps.length; i++)
            sb.append(steps[i]);
        sb.append("</core:while>");
        return (sb.toString());

    }


    // ------------------------------------------------------ Protected Methods


    /**
     * Evaluate the condition specified by the Descriptors associated with
     * this Block, and return the resulting boolean value.
     *
     * @param context Context within which to evaluate the descriptors
     */
    protected boolean evaluate(Context context) {

        boolean condition = true;
        Descriptor descriptors[] = findDescriptors();
        for (int i = 0; i < descriptors.length; i++) {
            if (descriptors[i] == null)
                continue;
            if (!descriptors[i].positive(context))
                condition = false;
        }
        return (condition);

    }


    /**
     * Process the initial entry into this Block.
     *
     * @param context Context within which to evaluate the condition
     */
    protected void initial(Context context) {

        if (evaluate(context)) {
            BlockState state = new BlockState(this, true);
            context.pushBlockState(state);
            context.setNextStep(getFirstStep());
        } else {
            context.setNextStep(getNextStep());
        }

    }


    /**
     * Process the return from nested execution of the Steps assocaited
     * with this Block.
     *
     * @param context Context within which to evaluate the condition
     * @param state BlockState for our block
     */
    protected void subsequent(Context context, BlockState state) {


        // Was a "break" Step executed within this Block?
        if (!state.getNest()) {
            context.popBlockState();
            context.setNextStep(getNextStep());
            return;
        }

        // Re-evaluate the loop conditions
        if (evaluate(context)) {
            context.setNextStep(getFirstStep());
        } else {
            context.popBlockState();
            context.setNextStep(getNextStep());
        }
        

    }


}
