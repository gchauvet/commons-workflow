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


import org.apache.commons.digester.Digester;
import org.apache.commons.workflow.base.BaseRuleSet;


/**
 * <p><strong>RuleSet</strong> for the Step definitions supported by the
 * <em>web</em> library.  This library is normally associated with the
 * following namespace URI:</p>
 * <pre>
 *   http://commons.apache.org/workflow/web
 * </pre>
 *
 * @author Craig R. McClanahan
 * @version $Revision$ $Date$
 */

public class WebRuleSet extends BaseRuleSet {


    // ------------------------------------------------------------ Constructor


    /**
     * Construct a default instance of the <code>RuleSet</code>.
     */
    public WebRuleSet() {

        super();
        setNamespaceURI("http://commons.apache.org/workflow/web");

    }


    // --------------------------------------------------------- Public Methods


    /**
     * <p>Add the set of Rule instances defined in this RuleSet to the
     * specified <code>Digester</code> instance, associating them with
     * our namespace URI (if any).  This method should only be called
     * by a Digester instance.</p>
     *
     * @param digester Digester instance to which the new Rule instances
     *  should be added.
     */
    public void addRuleInstances(Digester digester) {

        // Add rules for each Step defined in this package
        addStandardStep(digester, "forward",
                        "org.apache.commons.workflow.web.ForwardStep");
        addStandardStep(digester, "goto",
                        "org.apache.commons.workflow.web.GotoStep");
        if (isServlet23()) {
            addStandardStep(digester, "include",
                            "org.apache.commons.workflow.web.IncludeStep23");
        }
        addStandardStep(digester, "populate",
                        "org.apache.commons.workflow.web.PopulateStep");

        // Add rules for all variations on descriptors being matched
        addStandardDescriptor(digester, "descriptor");   // Standard version

    }


    // ------------------------------------------------------ Protected Methods


    /**
     * Are we executing in a Servlet 2.3 (or later) environment?
     */
    protected boolean isServlet23() {

        try {
            Class.forName("javax.servlet.Filter");  // 2.3-or-later class
            return (true);
        } catch (ClassNotFoundException e) {
            return (false);
        }

    }


}
