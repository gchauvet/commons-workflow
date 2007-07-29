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

package org.apache.commons.workflow.web;


import java.io.IOException;
import java.util.EmptyStackException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.apache.commons.workflow.Context;
import org.apache.commons.workflow.Step;
import org.apache.commons.workflow.StepException;
import org.apache.commons.workflow.base.BaseStep;


/**
 * <p>Unconditionally transfer control to the step that is identified by
 * a request parameter with the specified name.</p>
 *
 * <p>Supported Attributes:</p>
 * <ul>
 * <li><strong>step</strong> - Name of a request parameter, included on the
 *     current request, that contains the identifier of the Step (within the
 *     current Activity) to which control should be transferred.  If not
 *     specified, a request parameter named <code>step</code> is used.</li>
 * </ul>
 *
 * @version $Revision$ $Date$
 * @author Craig R. McClanahan
 */

public class GotoStep extends BaseStep {


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

        super();
        setId(id);

    }


    /**
     * Construct a fully configured instance of this Step.
     *
     * @param id Step identifier
     * @param step Request parameter name containing our step identifier
     */
    public GotoStep(String id, String step) {

        super();
        setId(id);
        setStep(step);

    }


    // ------------------------------------------------------------- Properties


    /**
     * The request parameter containing the identifier of the Step to which
     * control should be transferred.
     */
    protected String step = "step";

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

        // Make sure our executing Context is a WebContext
        if (!(context instanceof WebContext))
            throw new StepException("Execution context is not a WebContext",
                                    this);
        WebContext webContext = (WebContext) context;

        // Locate the step identifier to which we will transfer control
        ServletRequest request = webContext.getServletRequest();
        String id = request.getParameter(step);
        if (id == null)
            throw new StepException("No request parameter '" + step + "'",
                                    this);


        // Locate the step to which we will transfer control
        Step next = getOwner().findStep(id);
        if (next == null)
            throw new StepException("Cannot find step '" + id + "'", this);

        // Tell our Context to transfer control
        context.setNextStep(next);

    }


    /**
     * Render a string representation of this Step.
     */
    public String toString() {

        StringBuffer sb = new StringBuffer("<web:goto");
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
