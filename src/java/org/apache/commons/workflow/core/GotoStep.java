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
import org.apache.commons.workflow.base.DescriptorStep;


/**
 * <p>Unconditionally transfer control to the specified step.</p>
 *
 * <p>Supported Attributes:</p>
 * <ul>
 * <li><strong>step</strong> - Identifier of the Step to which control
 *     should be transferred.</li>
 * </ul>
 *
 * @version $Revision$ $Date$
 * @author Craig R. McClanahan
 */

public class GotoStep extends DescriptorStep {


    // ----------------------------------------------------------= Constructors


    /**
     * Construct a default instance of this Step.
     */
    public GotoStep() {

        super();

    }


    /**
     * Construct an instance of this Step with the specified identifier.
     *
     * @param id Step identifier
     */
    public GotoStep(String id) {

        this(id, null);

    }


    /**
     * Construct a fully configured instance of this Step.
     *
     * @param id Step identifier of this step
     * @param step Step identifier to which control should be redirected
     */
    public GotoStep(String id, String step) {

        super();
        setId(id);
        setStep(step);

    }


    // ------------------------------------------------------------- Properties


    /**
     * The identifier of the Step to which control should be transferred.
     */
    protected String step = null;

    public String getStep() {
        return (this.step);
    }

    public void setStep(String step) {
        this.step = step;
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

        // Locate the step to which we will transfer control
        Step next = getOwner().findStep(this.step);
        if (next == null)
            throw new StepException("Cannot find step '" + step + "'", this);

        // Tell our Context to transfer control
        context.setNextStep(next);

    }


    /**
     * Render a string representation of this Step.
     */
    public String toString() {

        StringBuffer sb = new StringBuffer("<core:goto");
        if (getId() != null) {
            sb.append(" id=\"");
            sb.append(getId());
            sb.append("\"");
        }
        sb.append(" step=\"");
        sb.append(getStep());
        sb.append("\"/>");
        return (sb.toString());

    }


}
