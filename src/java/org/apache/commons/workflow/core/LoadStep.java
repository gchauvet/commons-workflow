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
import org.apache.commons.workflow.StepException;
import org.apache.commons.workflow.base.BaseStep;


/**
 * <p>Load a class with the specified name from the specified class loader,
 * and push the corresponding <code>java.lang.Class</code> object onto the
 * evaluation stack.
 *
 * <p>Supported Attributes:</p>
 * <ul>
 * <li><strong>name</strong> - The fully qualified name of the Java class
 *     to be loaded.  If not specified, the top of the evaluation stack
 *     is popped, converted to a String (if necessary), and used as the
 *     name of the class to be loaded.</li>
 * <li><strong>thread</strong> - Should the class be loaded from the current
 *     Thread's context class loader?  (Default is to load from the same
 *     class loader that loaded this class).</li>
 * </ul>
 *
 * @version $Revision$ $Date$
 * @author Craig R. McClanahan
 */

public class LoadStep extends BaseStep {


    // ----------------------------------------------------------= Constructors


    /**
     * Construct a default instance of this Step.
     */
    public LoadStep() {

        super();

    }


    /**
     * Construct an instance of this Step with the specified identifier.
     *
     * @param id Step identifier
     */
    public LoadStep(String id) {

        super();
        setId(id);

    }


    /**
     * Construct a fully configured instance of this Step.
     *
     * @param id Step identifier
     * @param name Class name
     */
    public LoadStep(String id, String name) {

        super();
        setId(id);
        setName(name);

    }


    /**
     * Construct a fully configured instance of this Step.
     *
     * @param id Step identifier
     * @param name Class name
     * @param thread Load from thread context class loader?
     */
    public LoadStep(String id, String name, boolean thread) {

        super();
        setId(id);
        setName(name);
        setThread(thread);

    }


    // ------------------------------------------------------------- Properties


    /**
     * The class name to be loaded.
     */
    protected String name = null;

    public String getName() {
        return (this.name);
    }

    public void setName(String name) {
        this.name = name;
    }


    /**
     * Load from the thread context class loader?
     */
    protected boolean thread = false;

    public boolean getThread() {
        return (this.thread);
    }

    public void setThread(boolean thread) {
        this.thread = thread;
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

        // Acquire the class loader we will be using
        ClassLoader classLoader = null;
        if (thread)
            classLoader = Thread.currentThread().getContextClassLoader();
        else
            classLoader = this.getClass().getClassLoader();
        if (classLoader == null)
            throw new StepException
                ("No thread context class loader is available");

        // Calculate the name of the class to be loaded
        String className = getName();
        if (className == null)
            className = context.pop().toString();

        // Load the specified class
        Class clazz = null;
        try {
            clazz = classLoader.loadClass(className);
        } catch (Throwable t) {
            throw new StepException
                ("Exception from loadClass()", t, this);
        }

        // Push the new Class onto the evaluation stack and return
        context.push(clazz);

    }


    /**
     * Render a string representation of this Step.
     */
    public String toString() {

        StringBuffer sb = new StringBuffer("<core:load");
        if (getId() != null) {
            sb.append(" id=\"");
            sb.append(getId());
            sb.append("\"");
        }
        if (getName() != null) {
            sb.append(" name=\"");
            sb.append(getName());
            sb.append("\"");
        }
        sb.append(" thread=\"");
        sb.append(getThread());
        sb.append("\"/>");
        return (sb.toString());

    }


}
