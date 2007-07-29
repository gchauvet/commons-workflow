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


import org.apache.commons.workflow.Context;
import org.apache.commons.workflow.Descriptor;
import org.apache.commons.workflow.Step;
import org.apache.commons.workflow.StepException;


/**
 * <p>Evaluate properties specified by the associated Descriptors, and
 * transfer control to the specified step if ALL of them are
 * <code>true</code> (if boolean) or not null (if Object).  To avoid
 * non-deterministic evaluation stack behavior, all of the specified
 * Descriptors are always evaluated.</p>
 *
 * <p>Supported Attributes:</p>
 * <ul>
 * <li><strong>step</strong> - Identifier of the Step to which control
 *     should be transferred if the condition is met.</li>
 * </ul>
 *
 * @version $Revision$ $Date$
 * @author Craig R. McClanahan
 */

public class AndStep extends GotoStep {


    // ----------------------------------------------------------= Constructors


    /**
     * Construct a default instance of this Step.
     */
    public AndStep() {

        super();

    }


    /**
     * Construct an instance of this Step with the specified identifier.
     *
     * @param id Step identifier
     */
    public AndStep(String id) {

        this(id, null, null);

    }


    /**
     * Construct a fully configured instance of this Step.
     *
     * @param id Step identifier of this step
     * @param step Step identifier to which control should be redirected
     */
    public AndStep(String id, String step) {

        this(id, step, null);

    }


    /**
     * Construct a fully configured instance of this Step.
     *
     * @param id Step identifier of this step
     * @param step Step identifier to which control should be redirected
     * @param descriptor Initial descriptor to be added
     */
    public AndStep(String id, String step, Descriptor descriptor) {

        super();
        setId(id);
        setStep(step);
        addDescriptor(descriptor);

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

        // Process all associated descriptors
        boolean condition = true;
        Descriptor descriptors[] = findDescriptors();
        for (int i = 0; i < descriptors.length; i++) {
            Object value = descriptors[i].get(context);
            if (value == null)
                condition = false;
            else if ((value instanceof Boolean) &&
                     !((Boolean) value).booleanValue())
                condition = false;
        }

        // Conditionally forward control to the specified step
        if (condition) {
            Step next = getOwner().findStep(this.step);
            if (next == null)
                throw new StepException("Cannot find step '" + step + "'",
                                        this);
            context.setNextStep(next);
        }
                
    }


}
