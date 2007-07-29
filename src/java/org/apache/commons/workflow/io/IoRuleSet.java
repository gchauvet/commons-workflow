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


package org.apache.commons.workflow.io;


import org.apache.commons.digester.Digester;
import org.apache.commons.workflow.base.BaseRuleSet;


/**
 * <p><strong>RuleSet</strong> for the Step definitions supported by the
 * <em>io</em> library.  This library is normally associated with the
 * following namespace URI:</p>
 * <pre>
 *   http://jakarta.apache.org/commons/workflow/io
 * </pre>
 *
 * @author Craig R. McClanahan
 * @version $Revision$ $Date$
 */

public class IoRuleSet extends BaseRuleSet {


    // ------------------------------------------------------------ Constructor


    /**
     * Construct a default instance of the <code>RuleSet</code>.
     */
    public IoRuleSet() {

        super();
        setNamespaceURI("http://jakarta.apache.org/commons/workflow/io");

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
        addStandardStep(digester, "display",
                        "org.apache.commons.workflow.io.DisplayStep");
        addStandardStep(digester, "get",
                        "org.apache.commons.workflow.io.GetStep");
        addStandardStep(digester, "peek",
                        "org.apache.commons.workflow.io.PeekStep");
        addStandardStep(digester, "read",
                        "org.apache.commons.workflow.io.ReadStep");
        addStandardStep(digester, "write",
                        "org.apache.commons.workflow.io.WriteStep");

        // Add rules for all variations on descriptors being matched
        addStandardDescriptor(digester, "descriptor");   // Standard version

    }


}
