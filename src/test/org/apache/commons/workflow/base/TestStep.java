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


import org.apache.commons.workflow.Context;
import org.apache.commons.workflow.StepException;


/**
 * Test implementation of the <code>Step</code> interface.
 *
 * @author Craig R. McClanahan
 * @version $Revision$ $Date$
 */

public class TestStep extends BaseStep {


    // ----------------------------------------------------------- Constructors


    /**
     * Construct a TestStep instance with default property values (required
     * by the API contract for Step implementations.
     */
    public TestStep() {

        super();

    }


    /**
     * Construct a TestStep instance with the specified property values.
     *
     * @param id Identifier of this step
     */
    public TestStep(String id) {

        this(id, false);

    }


    /**
     * Construct a TestStep instance with the specified property values.
     *
     * @param id Identifier of this step
     * @param fail Should we always throw a StepException?
     */
    public TestStep(String id, boolean fail) {

        super();
        setId(id);
        setFail(fail);

    }


    // ------------------------------------------------------------- Properties


    /**
     * Should our execute() method always fail and throw an exception?
     */
    protected boolean fail = false;

    public boolean getFail() {
        return (this.fail);
    }

    public void setFail(boolean fail) {
        this.fail = fail;
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

        // Record the execution of this Step in the execution trace
        results.append(id);
        results.append('/');

        // Throw an exception if we are configured to do so
        if (fail)
            throw new StepException("StepException[" + id + "]", this);

    }



    // --------------------------------------------------------- Static Methods


    /**
     * An accumulator for execution trace results.
     */
    protected static StringBuffer results = new StringBuffer();


    /**
     * Clear the results accumulator.
     */
    public static void clearResults() {

        results = new StringBuffer();

    }


    /**
     * Return the accumulated execution results string.
     */
    public static String getResults() {

        return (results.toString());

    }



}
