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
 * <p>Push the specified String value onto the top of the evaluation
 * stack.</p>
 *
 * <p>Supported Attributes:</p>
 * <ul>
 * <li><strong>value</strong> - String value to be pushed.</li>
 * </ul>
 *
 * @version $Revision$ $Date$
 * @author Craig R. McClanahan
 */

public class StringStep extends BaseStep {


    // ----------------------------------------------------------= Constructors


    /**
     * Construct a default instance of this Step.
     */
    public StringStep() {

        super();

    }


    /**
     * Construct an instance of this Step with the specified identifier.
     *
     * @param id Step identifier
     */
    public StringStep(String id) {

        super();
        setId(id);

    }


    /**
     * Construct a fully configured instance of this Step.
     *
     * @param id Step identifier
     * @param value String value to be pushed
     */
    public StringStep(String id, String value) {

        super();
        setId(id);
        setValue(value);

    }


    // ------------------------------------------------------------- Properties


    /**
     * The string value to be pushed.
     */
    protected String value = null;

    public String getValue() {
        return (this.value);
    }

    public void setValue(String value) {
        this.value = value;
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

        // Validate that a value has been specified
        if (value == null)
            throw new StepException("No value specified", this);

        // Push the value onto the evaluation stack
        context.push(value);

    }


    /**
     * Render a string representation of this Step.
     */
    public String toString() {

        StringBuffer sb = new StringBuffer("<core:string");
        if (getId() != null) {
            sb.append(" id=\"");
            sb.append(getId());
            sb.append("\"");
        }
        if (getValue() != null) {
            sb.append(" value=\"");
            sb.append(getValue());
            sb.append("\"");
        }
        sb.append("/>");
        return (sb.toString());

    }


}
