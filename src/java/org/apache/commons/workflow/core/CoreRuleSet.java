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


import org.apache.commons.digester.Digester;
import org.apache.commons.workflow.base.BaseRuleSet;


/**
 * <p><strong>RuleSet</strong> for the Step definitions supported by the
 * <em>core</em> library.  This library is normally associated with the
 * following namespace URI:</p>
 * <pre>
 *   http://commons.apache.org/workflow/core
 * </pre>
 *
 * @author Craig R. McClanahan
 * @author Preston Sheldon
 * @version $Revision$ $Date$
 */

public class CoreRuleSet extends BaseRuleSet {


    // ------------------------------------------------------------ Constructor


    /**
     * Construct a default instance of the <code>RuleSet</code>.
     */
    public CoreRuleSet() {

        super();
        setNamespaceURI("http://commons.apache.org/workflow/core");

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
        addStandardStep(digester, "and",
                        "org.apache.commons.workflow.core.AndStep");
        addStandardStep(digester, "break",
                        "org.apache.commons.workflow.core.BreakStep");
        addStandardStep(digester, "call",
                        "org.apache.commons.workflow.core.CallStep");
        addStandardStep(digester, "construct",
                        "org.apache.commons.workflow.core.ConstructStep");
        addStandardStep(digester, "duplicate",
                        "org.apache.commons.workflow.core.DuplicateStep");
        addStandardStep(digester, "exit",
                        "org.apache.commons.workflow.core.ExitStep");
        addStandardStep(digester, "get",
                        "org.apache.commons.workflow.core.GetStep");
        addStandardStep(digester, "goto",
                        "org.apache.commons.workflow.core.GotoStep");
        addStandardStep(digester, "if",
                        "org.apache.commons.workflow.core.IfStep");
        addStandardStep(digester, "ifAny",
                        "org.apache.commons.workflow.core.IfAnyStep");
        addStandardStep(digester, "ifNot",
                        "org.apache.commons.workflow.core.IfNotStep");
        addStandardStep(digester, "ifNotAny",
                        "org.apache.commons.workflow.core.IfNotAnyStep");
        addStandardStep(digester, "invoke",
                        "org.apache.commons.workflow.core.InvokeStep");
        addStandardStep(digester, "load",
                        "org.apache.commons.workflow.core.LoadStep");
        addStandardStep(digester, "notAnd",
                        "org.apache.commons.workflow.core.NotAndStep");
        addStandardStep(digester, "notOr",
                        "org.apache.commons.workflow.core.NotOrStep");
        addStandardStep(digester, "or",
                        "org.apache.commons.workflow.core.OrStep");
        addStandardStep(digester, "pop",
                        "org.apache.commons.workflow.core.PopStep");
        addStandardStep(digester, "put",
                        "org.apache.commons.workflow.core.PutStep");
        addStandardStep(digester, "remove",
                        "org.apache.commons.workflow.core.RemoveStep");
        addStandardStep(digester, "string",
                        "org.apache.commons.workflow.core.StringStep");
        addStandardStep(digester, "suspend",
                        "org.apache.commons.workflow.core.SuspendStep");
        addStandardStep(digester, "swap",
                        "org.apache.commons.workflow.core.SwapStep");
        addStandardStep(digester, "while",
                        "org.apache.commons.workflow.core.WhileStep");
        addStandardStep(digester, "whileAny",
                        "org.apache.commons.workflow.core.WhileAnyStep");
        addStandardStep(digester, "whileNot",
                        "org.apache.commons.workflow.core.WhileNotStep");
        addStandardStep(digester, "whileNotAny",
                        "org.apache.commons.workflow.core.WhileNotAnyStep");

        // Add rules for all variations on descriptors being matched
        addStandardDescriptor(digester, "bean");         // For invoke
        addStandardDescriptor(digester, "class");        // For construct
        addStandardDescriptor(digester, "descriptor");   // Standard version

    }


}
