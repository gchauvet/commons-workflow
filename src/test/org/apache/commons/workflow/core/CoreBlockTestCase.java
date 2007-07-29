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


import java.util.EmptyStackException;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.commons.workflow.Activity;
import org.apache.commons.workflow.Block;
import org.apache.commons.workflow.Context;
import org.apache.commons.workflow.ContextEvent;
import org.apache.commons.workflow.ContextListener;
import org.apache.commons.workflow.Descriptor;
import org.apache.commons.workflow.Scope;
import org.apache.commons.workflow.Step;
import org.apache.commons.workflow.StepException;
import org.apache.commons.workflow.base.BaseActivity;
import org.apache.commons.workflow.base.BaseContext;
import org.apache.commons.workflow.base.BaseDescriptor;
import org.apache.commons.workflow.base.BaseScope;
import org.apache.commons.workflow.base.Employee;


/**
 * <p>Test Case for the <code>core</code> library of <code>Block</code>
 * implementations the support conditional and iterated execution of
 * nexted Steps.</p>
 *
 * @author Craig R. McClanahan
 * @version $Revision$ $Date$
 */

public class CoreBlockTestCase extends TestCase
    implements ContextListener {


    // ----------------------------------------------------- Instance Variables


    /**
     * The Activity we will use to contain the Steps that we will execute.
     */
    protected Activity activity = null;


    /**
     * The Context we will use to execute the Activity under test.
     */
    protected Context context = null;


    /**
     * The trail of execution, as recorded by our ContextListener methods.
     */
    protected StringBuffer trail = new StringBuffer();


    // ----------------------------------------------------------- Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public CoreBlockTestCase(String name) {

        super(name);

    }


    // --------------------------------------------------- Overall Test Methods


    /**
     * Set up instance variables required by this test case.
     */
    public void setUp() {

        context = new BaseContext();
        activity = new BaseActivity();
        context.setActivity(activity);
        context.addContextListener(this);
    }


    /**
     * Return the tests included in this test suite.
     */
    public static Test suite() {

        return (new TestSuite(CoreBlockTestCase.class));

    }


    /**
     * Tear down instance variables required by this test case.
     */
    public void tearDown() {

        context.removeContextListener(this);
        activity = null;
        context = null;

    }


    // ------------------------------------------------ Individual Test Methods


    /**
     * Test "IfStep".
     */
    public void testIf() {

        // Configure the steps of this activity, which will assume
        // that two boolean values have been pushed onto the evaluation
        // stack before execution
        IfStep ifStep = new IfStep("01");
        ifStep.addDescriptor(new BaseDescriptor());
        ifStep.addDescriptor(new BaseDescriptor());
        ifStep.addStep(new StringStep("02", "If Executed"));
        activity.addStep(ifStep);
        activity.addStep(new StringStep("03", "If Completed"));

        // Test with various combinations of boolean arguments
        commonIfTest(true, true, true);
        commonIfTest(true, false, false);
        commonIfTest(false, true, false);
        commonIfTest(false, false, false);

    }


    /**
     * Test "IfAnyStep".
     */
    public void testIfAny() {

        // Configure the steps of this activity, which will assume
        // that two boolean values have been pushed onto the evaluation
        // stack before execution
        IfAnyStep ifStep = new IfAnyStep("01");
        ifStep.addDescriptor(new BaseDescriptor());
        ifStep.addDescriptor(new BaseDescriptor());
        ifStep.addStep(new StringStep("02", "If Executed"));
        activity.addStep(ifStep);
        activity.addStep(new StringStep("03", "If Completed"));

        // Test with various combinations of boolean arguments
        commonIfTest(true, true, true);
        commonIfTest(true, false, true);
        commonIfTest(false, true, true);
        commonIfTest(false, false, false);

    }


    /**
     * Test "IfNotStep".
     */
    public void testIfNot() {

        // Configure the steps of this activity, which will assume
        // that two boolean values have been pushed onto the evaluation
        // stack before execution
        IfNotStep ifStep = new IfNotStep("01");
        ifStep.addDescriptor(new BaseDescriptor());
        ifStep.addDescriptor(new BaseDescriptor());
        ifStep.addStep(new StringStep("02", "If Executed"));
        activity.addStep(ifStep);
        activity.addStep(new StringStep("03", "If Completed"));

        // Test with various combinations of boolean arguments
        commonIfTest(true, true, false);
        commonIfTest(true, false, true);
        commonIfTest(false, true, true);
        commonIfTest(false, false, true);

    }


    /**
     * Test "IfNotAnyStep".
     */
    public void testIfNotAny() {

        // Configure the steps of this activity, which will assume
        // that two boolean values have been pushed onto the evaluation
        // stack before execution
        IfNotAnyStep ifStep = new IfNotAnyStep("01");
        ifStep.addDescriptor(new BaseDescriptor());
        ifStep.addDescriptor(new BaseDescriptor());
        ifStep.addStep(new StringStep("02", "If Executed"));
        activity.addStep(ifStep);
        activity.addStep(new StringStep("03", "If Completed"));

        // Test with various combinations of boolean arguments
        commonIfTest(true, true, false);
        commonIfTest(true, false, false);
        commonIfTest(false, true, false);
        commonIfTest(false, false, true);

    }


    // -------------------------------------------------------- Private Methods


    /**
     * Common testing of "if" blocks with two boolean parameters and an
     * indication of the expected result.
     *
     * @param first First boolean parameter
     * @param second Second boolean parameter
     * @param result Should the nested block be executed?
     */
    private void commonIfTest(boolean first, boolean second, boolean result) {

        try {

            // Set up the execution environment
            context.clear();
            context.clearBlockState();
            context.push(new Boolean(second));
            context.push(new Boolean(first));

            // Execute the requested activity
            context.execute();

            // Validate the results
            if (result) {
                assertEquals("Executed nested step",
                             "beforeActivity()/" +
                             "beforeStep(01)/afterStep(01)/" +
                             "beforeStep(02)/afterStep(02)/" +
                             "beforeStep(01)/afterStep(01)/" +
                             "beforeStep(03)/afterStep(03)/" +
                             "afterActivity()/",
                             trail.toString());
            } else {
                assertEquals("Skipped nested step",
                             "beforeActivity()/" +
                             "beforeStep(01)/afterStep(01)/" +
                             "beforeStep(03)/afterStep(03)/" +
                             "afterActivity()/",
                             trail.toString());
            }
            assertTrue("Context is not suspended",
                       !context.getSuspend());
            assertTrue("Evaluation Stack is not empty",
                       !context.isEmpty());
            assertTrue("BlockState Stack is empty",
                       context.isEmptyBlockState());
            Object top = context.pop();
            assertTrue("Completed message is a String",
                       top instanceof String);
            assertEquals("Completed message is correct",
                         "If Completed", (String) top);
            if (result) {
                assertTrue("Evaluation Stack is not empty",
                           !context.isEmpty());
                top = context.pop();
                assertTrue("Executed message is a String",
                           top instanceof String);
                assertEquals("Executed message is correct",
                             "If Executed", (String) top);
            }
            assertTrue("Evaluation Stack is empty",
                       context.isEmpty());
        } catch (StepException e) {
            e.printStackTrace(System.out);
            if (e.getCause() != null) {
                System.out.println("ROOT CAUSE");
                e.getCause().printStackTrace(System.out);
            }
            fail("Threw StepException " + e);
        } catch (Throwable t) {
            t.printStackTrace(System.out);
            fail("Threw Exception " + t);
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

        trail.append("afterActivity()/");

    }


    /**
     * Invoked immediately after the specified Step was executed.
     *
     * @param event The <code>ContextEvent</code> that has occurred
     */
    public void afterStep(ContextEvent event) {

        trail.append("afterStep(");
        trail.append(event.getStep().getId());
        trail.append(")");
        /*
        StepException exception = event.getException();
        if (exception != null) {
            trail.append("-");
            trail.append(exception.toString());
        }
        */
        trail.append("/");

    }


    /**
     * Invoked immediately before execution of the related Activity has
     * started.  The Step included in this event will be the first one
     * to be executed.
     *
     * @param event The <code>ContextEvent</code> that has occurred
     */
    public void beforeActivity(ContextEvent event) {

        trail.setLength(0);
        trail.append("beforeActivity()/");

    }


    /**
     * Invoked immediately before the specified Step is executed.
     *
     * @param event The <code>ContextEvent</code> that has occurred
     */
    public void beforeStep(ContextEvent event) {

        trail.append("beforeStep(");
        trail.append(event.getStep().getId());
        trail.append(")/");

    }


}
