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


import org.apache.commons.digester.Digester;
import org.apache.commons.digester.Rule;
import org.apache.commons.digester.RuleSet;
import org.apache.commons.digester.RuleSetBase;


/**
 * <p><strong>RuleSet</strong> for the basic Activity definitions of the
 * Workflow Management System, typically associated with the <em>base</em>
 * prefix.  This library is normally associated with the namespace URI:</p>
 * <pre>
 *   http://commons.apache.org/workflow/base
 * </pre>
 *
 * <p>This class also serves as a convenience base class for the
 * <code>RuleSet</code> implementations for Step libraries.  Subclasses
 * MUST override the no-arguments constructor to set the correct
 * namespace URI, and MUST override (and replace) the
 * <code>addRuleInstances()</code> method to add the relevant rules
 * for that particular library.
 *
 * @author Craig R. McClanahan
 * @version $Revision$ $Date$
 */

public class BaseRuleSet extends RuleSetBase {


    // ------------------------------------------------------------ Constructor


    /**
     * Construct a default instance of the <code>RuleSet</code>.
     */
    public BaseRuleSet() {

        super();
        setNamespaceURI("http://commons.apache.org/workflow/base");

    }


    // ------------------------------------------------------------- Properties


    /**
     * Set the namespace URI that these rules apply to.  This is only needed
     * if you want to reset the default value created by a subclass.
     *
     * @param namespaceURI The new namespace URI
     */
    public void setNamespaceURI(String namespaceURI) {
        this.namespaceURI = namespaceURI;
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

        digester.addObjectCreate("activity",
                              "org.apache.commons.workflow.base.BaseActivity");
        digester.addSetProperties("activity");

    }


    // ------------------------------------------------------ Protected Methods


    /**
     * Add the standard set of rules for a new Descriptor that should be
     * recognized.
     *
     * @param digester Digester to which we are adding new rules
     * @param element Element name to be matched
     */
    protected void addStandardDescriptor(Digester digester, String element) {

        String pattern = "*/" + element;
        digester.addObjectCreate(pattern,
                            "org.apache.commons.workflow.base.BaseDescriptor");
        digester.addSetProperties(pattern);
        digester.addSetNext(pattern, "addDescriptor",
                            "org.apache.commons.workflow.Descriptor");

    }


    /**
     * Add the standard set of rules for a new Step that should be recognized.
     *
     * @param digester Digester to which we are adding new rules
     * @param element Element name to be matched
     * @param name Fully qualified class name of the implementation class
     */
    protected void addStandardStep(Digester digester, String element,
                                   String name) {

        String pattern = "*/" + element;
        digester.addObjectCreate(pattern, name);
        digester.addSetProperties(pattern);
        digester.addSetNext(pattern, "addStep",
                            "org.apache.commons.workflow.Step");

    }


}
