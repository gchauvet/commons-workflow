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


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.workflow.Context;
import org.apache.commons.workflow.Descriptor;
import org.apache.commons.workflow.StepException;
import org.apache.commons.workflow.base.DescriptorStep;


/**
 * <p>Call the specified method of the specified bean in the specified
 * scope, passing arguments as specified by associated <code>Descriptor</code>
 * objects.  The <strong>first</strong> associated <code>Descriptor</code>
 * identifies the Java object on whom method invocation shall take place.</p>
 *
 * <p><strong>FIXME</strong> - Better way to deal with exceptions???</p>
 *
 * <p>Supported Attributes:</p>
 * <ul>
 * <li><strong>method</strong> - Name of the public method to be called
 *     on the bean specified by either <code>name</code> and
 *     <code>scope</code>, or by <code>xpath</code>.</li>
 * </ul>
 *
 * @version $Revision$ $Date$
 * @author Craig R. McClanahan
 */

public class InvokeStep extends DescriptorStep {


    // ----------------------------------------------------------= Constructors


    /**
     * Construct a default instance of this Step.
     */
    public InvokeStep() {

        super();

    }


    /**
     * Construct an instance of this Step with the specified identifier.
     *
     * @param id Step identifier
     */
    public InvokeStep(String id) {

        super();
        setId(id);

    }


    /**
     * Construct a fully configured instance of this Step.
     *
     * @param id Step identifier
     * @param method Method name
     */
    public InvokeStep(String id, String method) {

        super();
        setId(id);
        setMethod(method);

    }


    /**
     * Construct a fully configured instance of this Step.
     *
     * @param id Step identifier
     * @param method Method name
     * @param descriptor Descriptor for the bean on which to invoke
     */
    public InvokeStep(String id, String method, Descriptor descriptor) {

        super();
        setId(id);
        setMethod(method);
        addDescriptor(descriptor);

    }


    // ------------------------------------------------------------- Properties


    /**
     * The method name to be invoked.
     */
    protected String method = null;

    public String getMethod() {
        return (this.method);
    }

    public void setMethod(String method) {
        this.method = method;
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

        // Identify the object on whom a method is to be invoked
        Descriptor descriptors[] = findDescriptors();
        if (descriptors.length < 1)
            throw new StepException("No object descriptor on which to invoke",
                                    this);
        Object bean = descriptors[0].get(context);
        if (bean == null)
            throw new StepException("No object bean on which to invoke",
                                    this);

        // Assembe arrays of parameter types and values
        Class types[] = new Class[descriptors.length - 1];
        Object values[] = new Object[descriptors.length - 1];
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

        // Identify a compatible method signature on the bean
        Method method = findMethod(bean, this.method, types);
        if (method == null)
            throw new StepException("No available method " +
                                    signature(this.method, types),
                                    this);

        // Invoke the requested method on the requested bean
        // FIXME - better way to deal with exceptions?
        try {
            Class clazz = method.getReturnType();
            if ((clazz != null) && (clazz != Void.TYPE)) {
                Object result = method.invoke(bean, values);
                context.push(result);
            } else {
                method.invoke(bean, values);
            }
        } catch (InvocationTargetException t) {
            Throwable cause = t.getTargetException();
            throw new StepException("Invoke exception", cause, this);
        } catch (Throwable t) {
            throw new StepException("Invoke exception", t, this);
        }

    }


    /**
     * Render a string representation of this Step.
     */
    public String toString() {

        StringBuffer sb = new StringBuffer("<core:invoke");
        if (getId() != null) {
            sb.append(" id=\"");
            sb.append(getId());
            sb.append("\"");
        }
        sb.append(" method=\"");
        sb.append(getMethod());
        sb.append("\"");
        sb.append(">");
        Descriptor descriptors[] = findDescriptors();
        for (int i = 0; i < descriptors.length; i++)
            sb.append(descriptors[i].toString());
        sb.append("</core:invoke>");
        return (sb.toString());

    }


    // ------------------------------------------------------ Protected Methods


    /**
     * Return a <code>Method</code> of the specified <code>Class</code> with
     * the specified method name, that takes the specified parameter types,
     * if there is one.  Otherwise, return <code>null</code>.
     *
     * @param bean Bean on which method searching is to be done
     * @param name Method name to search for
     * @param types Parameter types to search for
     */
    protected Method findMethod(Object bean, String name, Class types[]) {

        try {
            return (bean.getClass().getMethod(name, types));
        } catch (NoSuchMethodException e) {
            return (null);
        }

    }


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
