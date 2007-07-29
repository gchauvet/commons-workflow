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
import org.apache.commons.workflow.Context;
import org.apache.commons.workflow.Descriptor;
import org.apache.commons.workflow.Descriptors;
import org.apache.commons.workflow.Step;
import org.apache.commons.workflow.StepException;


/**
 * <p><strong>DescriptorStep</strong> is a convenient base class for more
 * sophisticated <code>Step</code> implementations that already support
 * the APIs provided by <code>BaseStep</code>, and also implement the
 * <code>Descriptors</code> interafce.
 *
 * @version $Revision$ $Date$
 * @author Craig R. McClanahan
 */

public abstract class DescriptorStep extends BaseStep implements Descriptors {


    // ----------------------------------------------------- Instance Variables


    /**
     * The list of <code>Descriptor</code> objects associated with this
     * <code>Step</code>.
     */
    protected ArrayList descriptors = new ArrayList();


    // --------------------------------------------------------- Public Methods


    /**
     * Add a new <code>Descriptor</code> to the set associated with
     * this object.
     *
     * @param descriptor The Descriptor to be added
     */
    public void addDescriptor(Descriptor descriptor) {

        descriptors.add(descriptor);

    }


    /**
     * Return the set of <code>Descriptor</code> objects associated with
     * this object, in the order that they were originally added.
     */
    public Descriptor[] findDescriptors() {

        Descriptor results[] = new Descriptor[descriptors.size()];
        return ((Descriptor[]) descriptors.toArray(results));

    }


    /**
     * Remove an existing <code>Descriptor</code> from the set associated
     * with this object.
     *
     * @param descriptor The Descriptor to be removed
     */
    public void removeDescriptor(Descriptor descriptor) {

        descriptors.remove(descriptor);

    }


}
