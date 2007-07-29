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


import java.util.EmptyStackException;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.collections.ArrayStack;
import org.apache.commons.workflow.Context;
import org.apache.commons.workflow.Scope;
import org.apache.commons.workflow.base.BaseContext;


/**
 * <p><strong>WebContext</strong> is a specialized <code>Context</code>
 * implementation appropriate for web applications that run on top of a
 * Servlet 2.2 (or later) container.  It exposes the attributes associated
 * with requests, sessions, and the servlet context as <code>Scopes</code>
 * within the workflow management system.</p>
 *
 * @version $Revision$ $Date$
 * @author Craig R. McClanahan
 */

public class WebContext extends BaseContext {


    // ----------------------------------------------------- Manifest Constants


    /**
     * The scope identifier for the scope associated with the current
     * servlet request.
     */
    public static final int REQUEST_SCOPE = 2;


    /**
     * The scope name for the scope associated with the current
     * servlet request.
     */
    public static final String REQUEST_SCOPE_NAME = "request";


    /**
     * The scope identifier for the scope associated with the current
     * HTTP session.
     */
    public static final int SESSION_SCOPE = 4;


    /**
     * The scope name for the scope associated with the current
     * HTTP session.
     */
    public static final String SESSION_SCOPE_NAME = "session";


    /**
     * The scope identifier for the scope associated with the current
     * servlet context.
     */
    public static final int APPLICATION_SCOPE = 6;


    /**
     * The scope name for the scope associated with the current
     * servlet context.
     */
    public static final String APPLICATION_SCOPE_NAME = "application";


    // ------------------------------------------------------------- Properties


    /**
     * The HttpSession that provides our associated "session" scope.
     */
    protected HttpSession httpSession = null;

    public HttpSession getHttpSession() {

        return (this.httpSession);

    }

    public void setHttpSession(HttpSession httpSession) {

        this.httpSession = httpSession;
        Scope oldScope = getScope(SESSION_SCOPE);
        if ((oldScope != null) && (oldScope instanceof HttpSessionScope))
            ((HttpSessionScope) oldScope).setHttpSession(httpSession);
        else
            addScope(SESSION_SCOPE, SESSION_SCOPE_NAME,
                     new HttpSessionScope(httpSession));

    }


    /**
     * The ServletContext that provides our associated "application" scope.
     */
    protected ServletContext servletContext = null;

    public ServletContext getServletContext() {

        return (this.servletContext);

    }

    public void setServletContext(ServletContext servletContext) {

        this.servletContext = servletContext;
        Scope oldScope = getScope(APPLICATION_SCOPE);
        if ((oldScope != null) && (oldScope instanceof ServletContextScope))
            ((ServletContextScope) oldScope).setServletContext(servletContext);
        else
            addScope(APPLICATION_SCOPE, APPLICATION_SCOPE_NAME,
                     new ServletContextScope(servletContext));

    }


    /**
     * The ServletRequest that provides our associated "request" scope.
     */
    protected ServletRequest servletRequest = null;

    public ServletRequest getServletRequest() {

        return (this.servletRequest);

    }

    public void setServletRequest(ServletRequest servletRequest) {

        this.servletRequest = servletRequest;
        Scope oldScope = getScope(REQUEST_SCOPE);
        if ((oldScope != null) && (oldScope instanceof ServletRequestScope))
            ((ServletRequestScope) oldScope).setServletRequest(servletRequest);
        else
            addScope(REQUEST_SCOPE, REQUEST_SCOPE_NAME,
                     new ServletRequestScope(servletRequest));

        // FIXME - Consider exposing the cookies and headers from
        // a request as "read only" scopes

    }


    /**
     * The ServletResponse we should pass on to any request dispatcher
     * delegations.
     */
    protected ServletResponse servletResponse = null;

    public ServletResponse getServletResponse() {

        return (this.servletResponse);

    }

    public void setServletResponse(ServletResponse servletResponse) {

        this.servletResponse = servletResponse;

    }


}
