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


import java.io.InputStream;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.digester.Digester;
import org.apache.commons.workflow.Activity;
import org.apache.commons.workflow.Context;
import org.apache.commons.workflow.ContextEvent;
import org.apache.commons.workflow.ContextListener;
import org.apache.commons.workflow.Step;
import org.apache.commons.workflow.StepException;
import org.apache.commons.workflow.base.BaseRuleSet;
import org.apache.commons.workflow.core.CoreRuleSet;
import org.apache.commons.workflow.io.IoRuleSet;
import org.apache.commons.workflow.web.WebContext;
import org.apache.commons.workflow.web.WebRuleSet;


/**
 * <p>Demonstration servlet that illustrates one way that workflow support can
 * be integrated into web applications (or web services) without any
 * dependency on application frameworks.  For this implementation, a servlet
 * <em>definition</em> (plus one or more servlet <em>mappings</em>) will be
 * associated with each <code>Activity</code> supported by this web
 * application.</p>
 *
 * <p>Initialization parameters (defaults in square brackets):</p>
 * <ul>
 * <li><strong>activity</strong> - Context-relative resource path to the
 *     definition file for the Activity that is supported by this servlet.</li>
 * <li><strong>attribute</strong> - Name of the session attribute under
 *     which our current <code>Context</code> implementation is stored.
 *     [org.apache.commons.workflow.web.CONTEXT]</li>
 * <li><strong>debug</strong> - The debugging detail level for this
 *     servlet, which controls how much information is logged.  [0]</li>
 * <li><strong>detail</strong> - The debugging detail level for the Digester
 *     we utilize in <code>initMapping()</code>, which logs to System.out
 *     instead of the servlet log.  [0]</li>
 * </ul>
 *
 * @author Craig R. McClanahan
 * @version $Revision$ $Date$
 */


public class ActivityServlet extends HttpServlet implements ContextListener {


    // ----------------------------------------------------- Instance Variables


    /**
     * The <code>Activity</code> that is supported by this servlet instance.
     */
    private Activity activity = null;


    /**
     * Name of the session attribute under which our current
     * <code>Context</code> is stored.
     */
    private String attribute = "org.apache.commons.workflow.CONTEXT";


    /**
     * The debugging detail level for this servlet.
     */
    private int debug = 0;


    /**
     * The debugging detail level for our Digester.
     */
    private int detail = 0;


    // --------------------------------------------------------- Public Methods


    /**
     * Perform a graceful shutdown of this servlet instance.
     */
    public void destroy() {

        ; // No processing required

    }


    /**
     * Process a GET transaction.
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are processing
     *
     * @exception IOException if an input/output exception occurs
     * @exception ServletException if a servlet exception occurs
     */
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
        throws IOException, ServletException {

        doPost(request, response);

    }


    /**
     * Process a POST transaction.
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are processing
     *
     * @exception IOException if an input/output exception occurs
     * @exception ServletException if a servlet exception occurs
     */
    public void doPost(HttpServletRequest request,
                       HttpServletResponse response)
        throws IOException, ServletException {

        // Acquire or create the current Context for this user
        HttpSession session = request.getSession(true);
        WebContext context = (WebContext)
            session.getAttribute(attribute);
        if (context == null) {
            if (debug >= 1)
                log("{" + session.getId() + "} Creating new Context");
            context = new WebContext();
            context.setActivity(activity);
            context.setHttpSession(session);
            context.setServletContext(getServletContext());
            if (debug >= 3)
                context.addContextListener(this);
            session.setAttribute(attribute, context);
        }                


        // Execute the next stage of the current Activity
        synchronized (context) {

            // If we are not already executing our associated Activity, call it
            if (!activity.equals(context.getActivity())) {
                if (debug >= 2)
                    log("{" + session.getId() + "} calling Activity " +
                        activity.getId());
                context.call(activity);
            }

            // Associate our context with the current request and response
            context.setServletRequest(request);
            context.setServletResponse(response);

            // Execute our activity until suspended or ended
            try {
                if (debug >= 2)
                    log("{" + session.getId() + "} executing Activity " +
                        activity.getId());
                context.execute();
            } catch (StepException e) {
                if (e.getCause() == null)
                    throw new ServletException(e.getMessage(), e);
                else
                    throw new ServletException(e.getMessage(), e.getCause());
            }

        }

    }


    /**
     * Perform a graceful startup of this servlet instance.
     *
     * @exception ServletException if we cannot process the activity
     *  definition file for this activity
     */
    public void init() throws ServletException {

        // Record the debugging detail level settings
        String debug = getServletConfig().getInitParameter("debug");
        if (debug != null) {
            try {
                this.debug = Integer.parseInt(debug);
            } catch (NumberFormatException e) {
                throw new UnavailableException
                    ("Debug initialization parameter must be an integer");
            }
        }
        String detail = getServletConfig().getInitParameter("detail");
        if (detail != null) {
            try {
                this.detail = Integer.parseInt(detail);
            } catch (NumberFormatException e) {
                throw new UnavailableException
                    ("Detail initialization parameter must be an integer");
            }
        }

        // Record the attribute name for our current Context
        String attribute = getServletConfig().getInitParameter("attribute");
        if (attribute != null)
            this.attribute = attribute;

        // Parse the activity definition file for our Activity
        String path = getServletConfig().getInitParameter("activity");
        if (path == null)
            throw new UnavailableException
                ("Must specify an 'activity' attribute");
        parse(path);
        if (activity == null)
            throw new UnavailableException("No activity defined in resource "
                                           + path);

    }


    /**
     * Set the <code>Activity</code> associated with this instance.
     *
     * @param activity The new associated Activity
     */
    public void setActivity(Activity activity) {

        this.activity = activity;

    }


    // ------------------------------------------------ ContextListener Methods


    /**
     * Invoked immediately after execution of the related Activity has
     * been completed normally, been suspended, or been aborted by
     * the throwing of a StepException.  The Step included in this event
     * will be the last one to be executed.
     *
     * @param event The <code>ContextEvent</code> that has occurred
     */
    public void afterActivity(ContextEvent event) {

        WebContext context = (WebContext) event.getContext();
        HttpSession session = context.getHttpSession();
        StringBuffer sb = new StringBuffer("{");
        sb.append(session.getId());
        sb.append("} afterActivity");
        log(sb.toString());

    }



    /**
     * Invoked immediately after the specified Step was executed.
     *
     * @param event The <code>ContextEvent</code> that has occurred
     */
    public void afterStep(ContextEvent event) {

        WebContext context = (WebContext) event.getContext();
        HttpSession session = context.getHttpSession();
        StringBuffer sb = new StringBuffer("{");
        sb.append(session.getId());
        sb.append("} afterStep ");
        sb.append(event.getStep());
        if (context.getSuspend())
            sb.append(" (Suspended)");
        if (context.getNextStep() == null)
            sb.append(" (Finished)");
        log(sb.toString());
        if (event.getException() != null)
            log("-->Step threw exception", event.getException());

    }


    /**
     * Invoked immediately before execution of the related Activity has
     * started.  The Step included in this event will be the first one
     * to be executed.
     *
     * @param event The <code>ContextEvent</code> that has occurred
     */
    public void beforeActivity(ContextEvent event) {

        WebContext context = (WebContext) event.getContext();
        HttpSession session = context.getHttpSession();
        StringBuffer sb = new StringBuffer("{");
        sb.append(session.getId());
        sb.append("} beforeActivity");
        log(sb.toString());

    }


    /**
     * Invoked immediately before the specified Step is executed.
     *
     * @param event The <code>ContextEvent</code> that has occurred
     */
    public void beforeStep(ContextEvent event) {

        WebContext context = (WebContext) event.getContext();
        HttpSession session = context.getHttpSession();
        StringBuffer sb = new StringBuffer("{");
        sb.append(session.getId());
        sb.append("} beforeStep ");
        sb.append(event.getStep());
        log(sb.toString());

    }


    // -------------------------------------------------------- Private Methods


    /**
     * Parse the specified activity definition file for this instance.
     *
     * @param path Context-relative resource path of the activity
     *  definition file
     *
     * @exception ServletException on any processing error in parsing
     */
    private void parse(String path) throws ServletException {

        // Get an input source for the specified path
        InputStream is =
            getServletContext().getResourceAsStream(path);
        if (is == null)
            throw new UnavailableException("Cannot access resource " +
                                           path);

        // Configure a Digester instance to parse our definition file
        Digester digester = new Digester();
        digester.setNamespaceAware(true);
        digester.setValidating(false);
        digester.push(this);

        // Add rules to recognize the built-in steps that we know about
        BaseRuleSet brs = new BaseRuleSet();
        digester.addRuleSet(brs);
        digester.addRuleSet(new CoreRuleSet());
        digester.addRuleSet(new IoRuleSet());
        digester.addRuleSet(new WebRuleSet());

        // Add a rule to register the Activity being created
        digester.setRuleNamespaceURI(brs.getNamespaceURI());
        digester.addSetNext("activity", "setActivity",
                            "org.apache.commons.workflow.Activity");

        // Parse the activity definition file
        try {
            digester.parse(is);
        } catch (Throwable t) {
            log("Cannot parse resource " + path, t);
            throw new UnavailableException("Cannot parse resource " + path);
        } finally {
            try {
                is.close();
            } catch (Throwable u) {
                ;
            }
        }

    }


}
