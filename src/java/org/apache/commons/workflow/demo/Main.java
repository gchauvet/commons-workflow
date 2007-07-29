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

package org.apache.commons.workflow.demo;


import java.io.File;
import org.apache.commons.digester.Digester;
import org.apache.commons.workflow.Activity;
import org.apache.commons.workflow.Context;
import org.apache.commons.workflow.ContextEvent;
import org.apache.commons.workflow.ContextListener;
import org.apache.commons.workflow.base.BaseContext;
import org.apache.commons.workflow.base.BaseRuleSet;
import org.apache.commons.workflow.core.CoreRuleSet;
import org.apache.commons.workflow.io.IoRuleSet;
import org.apache.commons.workflow.web.WebRuleSet;


/**
 * <p>Demonstration program to illustrate how the Workflow Management System
 * is utilized.  It accepts a command line argument to an XML file that
 * contains an <code>&lt;activity&gt;</code> to be parsed and then executed.
 * If no file is specified, a default XML document will be utilized.</p>
 *
 * <p><strong>WARNING</strong> - This program is for illustration only while
 * the workflow management system is being developed, and it will not be
 * part of the final package.  Indeed, much of its functionality (such as
 * the parsing of XML files describing activities) will need to be encapsulated
 * inside the system, in ways that support the per-namespace step definitions
 * as outlined in the original proposal.</p>
 *
 * <p><strong>WARNING</strong> - This code depends on the version of Digester
 * currently in SVN - the 1.0 released version will not work correctly with
 * namespace prefixes.</p>
 *
 * <p><strong>WARNING</strong> - The namespace URLs in the example XML document
 * are not official - they are simply used to satisfy the syntax requirements
 * of the XML parser.  Official namespace URIs <em>will</em> be required for a
 * formal release of this technology, because that is key to the extensibility
 * of Step implementations.</p>
 *
 * @version $Revision$ $Date$
 * @author Craig R. McClanahan
 */

public class Main implements ContextListener {


    // ----------------------------------------------------------- Main Program


    /**
     * The main program for the demo.
     *
     * @param args Command line arguments
     */
    public static void main(String args[]) {

        // Make sure there is a filename argument
        if (args.length != 1) {
            System.out.println("Usage:  java org.apache.commons.workflow.demo.Main {XML-file}");
            System.exit(1);
        }

        // Construct and utilize a new instance of this class
        Main main = new Main();
        main.process(args[0]);

    }


    // ----------------------------------------------------- Instance Variables


    /**
     * The Activity constructed by our Digester.
     */
    protected Activity activity = null;


    /**
     * The Digester used to process input files.
     */
    protected Digester digester = null;


    // ------------------------------------------------------------ Constructor


    /**
     * Construct a new instance of this class to process input files.
     */
    public Main() {

        super();
        digester = createDigester();

    }


    // -------------------------------------------------------- Support Methods


    /**
     * <p>Create a Digester instance that knows how to parse activities using
     * the <code>core</code> and <code>io</code> built-in Steps.</p>
     *
     * <p><strong>WARNING</strong> - This will ultimately be abstracted into
     * a mechanism to register the set of rule definitions associated with
     * a namespace.</p>
     */
    protected Digester createDigester() {

        // Construct and configure a new Digester instance
        Digester digester = new Digester();
        //        digester.setDebug(999);
        digester.setNamespaceAware(true);
        digester.setValidating(false);
        digester.push(this);

        // Add rules to recognize the built-in steps that we know about
        BaseRuleSet brs = new BaseRuleSet();
        digester.addRuleSet(brs);
        digester.addRuleSet(new CoreRuleSet());
        digester.addRuleSet(new IoRuleSet());

        // Add a rule to register the Activity being created
        digester.setRuleNamespaceURI(brs.getNamespaceURI());
        digester.addSetNext("activity", "setActivity",
                            "org.apache.commons.workflow.Activity");

        // Return the completed instance
        return (digester);

    }


    /**
     * Save the Activity that our Digester has constructed.
     *
     * @param activity The newly constructed Activity
     */
    public void setActivity(Activity activity) {

        this.activity = activity;

    }


    /**
     * Process the specified file.
     *
     * @param pathname Pathname of the specified XML file containing
     *  an activity definition
     */
    public void process(String pathname) {

        // Parse the activity definition
        try {
            System.out.println("Main:  Parsing activity file " + pathname);
            digester.parse(new File(pathname));
        } catch (Throwable t) {
            t.printStackTrace(System.out);
            return;
        }

        // Create a context and execute this activity
        try {
            System.out.println("Main:  Executing parsed activity");
            Context context = new BaseContext();
            context.setActivity(activity);
            context.addContextListener(this);
            context.execute();
        } catch (Throwable t) {
            t.printStackTrace(System.out);
            return;
        }

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

        System.out.println("CL: afterActivity()");

    }


    /**
     * Invoked immediately after the specified Step was executed.
     *
     * @param event The <code>ContextEvent</code> that has occurred
     */
    public void afterStep(ContextEvent event) {

        System.out.println("CL: afterStep(" + event.getStep().getId() + ")");

    }


    /**
     * Invoked immediately before execution of the related Activity has
     * started.  The Step included in this event will be the first one
     * to be executed.
     *
     * @param event The <code>ContextEvent</code> that has occurred
     */
    public void beforeActivity(ContextEvent event) {

        System.out.println("CL: beforeActivity()");

    }


    /**
     * Invoked immediately before the specified Step is executed.
     *
     * @param event The <code>ContextEvent</code> that has occurred
     */
    public void beforeStep(ContextEvent event) {

        System.out.println("CL: beforeStep(" + event.getStep().getId() + ")");

    }


}
