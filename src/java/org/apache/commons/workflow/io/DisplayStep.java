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

package org.apache.commons.workflow.io;


import org.apache.commons.workflow.Context;
import org.apache.commons.workflow.Descriptor;
import org.apache.commons.workflow.StepException;
import org.apache.commons.workflow.base.DescriptorStep;


/**
 * <p>For each associated <code>Descriptor</code>, print the value of the
 * specified Java object to standard output.</p>
 *
 * <p><strong>WARNING</strong> - This will probably be
 * replaced later by a more general purpose input/output mechanism.</p>
 *
 * @version $Revision$ $Date$
 * @author Craig R. McClanahan
 */

public class DisplayStep extends DescriptorStep {


    // ----------------------------------------------------------= Constructors


    /**
     * Construct a default instance of this Step.
     */
    public DisplayStep() {

        super();

    }


    /**
     * Construct an instance of this Step with the specified identifier.
     *
     * @param id Step identifier
     */
    public DisplayStep(String id) {

        super();
        setId(id);

    }


    /**
     * Construct an instance of this Step with the specified identifier
     * and associated Descriptor.
     *
     * @param id Step identifier
     * @param descriptor Initial descriptor
     */
    public DisplayStep(String id, Descriptor descriptor) {

        super();
        setId(id);
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
        Descriptor descriptors[] = findDescriptors();
        for (int i = 0; i < descriptors.length; i++) {
            Object value = descriptors[i].get(context);
            if (value == null)
                throw new StepException
                    ("Cannot retrieve object for " + descriptors[i], this);
            System.out.println(value);
        }

    }


}
