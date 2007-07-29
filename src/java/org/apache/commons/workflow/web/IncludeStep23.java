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
import org.apache.commons.workflow.StepException;
import org.apache.commons.workflow.base.BaseStep;
import org.apache.commons.workflow.util.WorkflowUtils;


/**
 * <p>Perform a <code>RequestDispatcher.include()</code> operation on the
 * specified context relative path, and push the response data (as a String
 * onto the evaluation stack.</p>
 *
 * <p>Supported Attributes:</p>
 * <ul>
 * <li><strong>page</strong> - Context relative URL (starting with a slash)
 *     of the application resource to be retrieved, or omitted to pop a
 *     computed String value from the top of the evaluation stack.</li>
 * </ul>
 *
 * <p><strong>WARNING</strong> - This implementation requires a Servlet 2.3
 * based container, because it uses the new response wrapper facilities.</p>
 *
 * <p><strong>DESIGN QUESTION - What about binary content?</strong></p>
 *
 * @version $Revision$ $Date$
 * @author Craig R. McClanahan
 */

public class IncludeStep23 extends BaseStep {


    // ----------------------------------------------------------= Constructors


    /**
     * Construct a default instance of this Step.
     */
    public IncludeStep23() {

        super();

    }


    /**
     * Construct an instance of this Step with the specified identifier.
     *
     * @param id Step identifier
     */
    public IncludeStep23(String id) {

        super();
        setId(id);

    }


    /**
     * Construct a fully configured instance of this Step.
     *
     * @param id Step identifier
     * @param page Context-relative url
     */
    public IncludeStep23(String id, String page) {

        super();
        setId(id);
        setPage(page);

    }


    // ------------------------------------------------------------- Properties


    /**
     * The context-relative URL (starting with '/') of the resource to be
     * retrieved.
     */
    protected String page = null;

    public String getPage() {
        return (this.page);
    }

    public void setPage(String page) {
        this.page = page;
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

        // Get the actual resource reference we will be using
        String resource = page;
        if (resource == null) {
            try {
                resource = (String) webContext.pop();
            } catch (EmptyStackException e) {
                throw new StepException("Evaluation stack is empty", this);
            }
        }

        // Create a request dispatcher and response wrapper for this resource
        RequestDispatcher rd =
            webContext.getServletContext().getRequestDispatcher(resource);
        if (rd == null)
            throw new StepException("No request dispatcher for '" +
                                    resource + "'", this);
        ServletRequest request = webContext.getServletRequest();
        ServletResponse response =
            new IncludeResponse23(webContext.getServletResponse());

        // Request the included resource
        String content = null;
        try {
            rd.include(request, response);
            content = ((IncludeResponse23) response).getContent();
        } catch (IOException e) {
            throw new StepException("IOException including '" +
                                    resource + "'", e, this);
        } catch (ServletException e) {
            throw new StepException("ServletException including '" +
                                    resource + "'", e, this);
        }

        // Push the resulting String onto the evaluation stack
        webContext.push(content);

    }


    /**
     * Render a string representation of this Step.
     */
    public String toString() {

        StringBuffer sb = new StringBuffer("<web:include");
        if (getId() != null) {
            sb.append(" id=\"");
            sb.append(getId());
            sb.append("\"");
        }
        sb.append(" page=\"");
        sb.append(getPage());
        sb.append("\"/>");
        return (sb.toString());

    }


}
