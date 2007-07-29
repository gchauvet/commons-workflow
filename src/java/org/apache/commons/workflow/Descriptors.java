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
 * <p>Implementing <strong>Descriptors</strong> indicates that the corresponding
 * object has an associated list of <code>Descriptor</code> objects associated
 * with it, which can be manipulated through the methods defined in this
 * interface.  The documentation for each implementing object will describe
 * the semantics of associated <code>Descriptor</code> object list.
 * </p>
 *
 * @version $Revision$ $Date$
 * @author Craig R. McClanahan
 */

public interface Descriptors {


    // --------------------------------------------------------- Public Methods


    /**
     * Add a new <code>Descriptor</code> to the set associated with
     * this object.
     *
     * @param descriptor The Descriptor to be added
     */
    public void addDescriptor(Descriptor descriptor);


    /**
     * Return the set of <code>Descriptor</code> objects associated with
     * this object, in the order that they were originally added.
     */
    public Descriptor[] findDescriptors();


    /**
     * Remove an existing <code>Descriptor</code> from the set associated
     * with this object.
     *
     * @param descriptor The Descriptor to be removed
     */
    public void removeDescriptor(Descriptor descriptor);


}
