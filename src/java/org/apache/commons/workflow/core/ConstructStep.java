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


import java.lang.reflect.Constructor;
import org.apache.commons.workflow.Context;
import org.apache.commons.workflow.Descriptor;
import org.apache.commons.workflow.StepException;
import org.apache.commons.workflow.base.DescriptorStep;


/**
 * <p>Create a new object of the specified class, using the constructor that
 * accepts the arguments specified by the associated <code>Descriptor</code>
 * objects.  The <strong>first</strong> descriptor must identify the
 * <code>java.lang.Class</code> object to be used to construct the new
 * object.</p>
 *
 * <p><strong>FIXME</strong> - Constructors that take primitive arguments are
 * not recognized and matched up to the wrapper classes.</p>
 *
 * @version $Revision$ $Date$
 * @author Craig R. McClanahan
 */

public class ConstructStep extends DescriptorStep {


    // ----------------------------------------------------------= Constructors


    /**
     * Construct a default instance of this Step.
     */
    public ConstructStep() {

        super();

    }


    /**
     * Construct an instance of this Step with the specified identifier.
     *
     * @param id Step identifier
     */
    public ConstructStep(String id) {

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
    public ConstructStep(String id, Descriptor descriptor) {

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

        // Identify the Class object to be used for object construction
        Descriptor descriptors[] = findDescriptors();
        if (descriptors.length < 1)
            throw new StepException
                ("No descriptor for Class to construct", this);
        Class clazz = null;
        try {
            clazz = (Class) descriptors[0].get(context);
            if (clazz == null)
                throw new StepException
                    ("No Class selected by first descriptor", this);
        } catch (ClassCastException e) {
            throw new StepException
                ("First descriptor does not select a Class", this);
        }

        // Assemble arrays of the argument types and values
        Class[] types = new Class[descriptors.length - 1];
        Object[] values = new Object[descriptors.length - 1];
        for (int i = 1; i < descriptors.length; i++) {
            values[i-1] = descriptors[i].get(context);
            types[i-1] = descriptors[i].getType();
            if (types[i-1] == null) {
                if (values[i-1] == null)
                    types[i-1] = Object.class;
                else
                    types[i-1] = values[i-1].getClass();
            }
        }

        // Find a constructor that accepts this set of types
        Constructor constructor = null;
        try {
            constructor = clazz.getConstructor(types);
        } catch (NoSuchMethodException e) {
            throw new StepException
                ("Cannot find constructor for " +
                 signature(clazz.getName(), types), this);
        }

        // Invoke the constructor to create a new object
        Object object = null;
        try {
            object = constructor.newInstance(values);
        } catch (Throwable t) {
            throw new StepException
                ("Exception from constructor " +
                 signature(clazz.getName(), types), t, this);
        }

        // Push the new object onto the evaluation stack and return
        context.push(object);

    }


    /**
     * Render a string representation of this Step.
     */
    public String toString() {

        StringBuffer sb = new StringBuffer("<core:construct");
        if (getId() != null) {
            sb.append(" id=\"");
            sb.append(getId());
            sb.append("\"");
        }
        sb.append(">");
        Descriptor descriptors[] = findDescriptors();
        for (int i = 0; i < descriptors.length; i++)
            sb.append(descriptors[i].toString());
        sb.append("</core:construct>");
        return (sb.toString());

    }


    // ------------------------------------------------------ Protected Methods


    /**
     * Return a method signature useful in debugging and exception messages.
     *
     * @param name Method name
     * @param types Parameter types
     */
    protected String signature(String name, Class types[]) {

        StringBuffer sb = new StringBuffer(name);
        sb.append('(');
        for (int i = 0; i < types.length; i++) {
            if (i > 0)
                sb.append(',');
            sb.append(types[i].getName());
        }
        sb.append(')');
        return (sb.toString());

    }


}
