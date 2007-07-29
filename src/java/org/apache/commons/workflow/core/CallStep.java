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
import org.apache.commons.workflow.Activity;
import org.apache.commons.workflow.Context;
import org.apache.commons.workflow.StepException;
import org.apache.commons.workflow.base.BaseStep;


/**
 * <p>Pop the top value from the evaluation stack, which must be an
 * <code>Activity</code>, and initiate a "subroutine call" to execute
 * this Activity before resuming the current one.</p>
 *
 * <p><strong>NOTE</strong> - The means by which the Activity on the top
 * of the stack was acquired is NOT mandated by this Step implementation.</p>
 *
 * @version $Revision$ $Date$
 * @author Craig R. McClanahan
 */

public class CallStep extends BaseStep {


    // ----------------------------------------------------------= Constructors


    /**
     * Construct a default instance of this Step.
     */
    public CallStep() {

        super();

    }


    /**
     * Construct an instance of this Step with the specified identifier.
     *
     * @param id Step identifier
     */
    public CallStep(String id) {

        super();
        setId(id);

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

        // Pop the evaluation stack
        Object value = null;
        try {
            value = context.pop();
        } catch (EmptyStackException e) {
            throw new StepException("Evaluation stack is empty", e, this);
        }

        // Call the requested Activity 
        if (!(value instanceof Activity))
            throw new StepException("Top of stack is not an Activity", this);
        context.call((Activity) value);

    }


}
