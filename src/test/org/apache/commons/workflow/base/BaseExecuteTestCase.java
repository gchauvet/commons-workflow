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


import java.util.EmptyStackException;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.commons.workflow.Activity;
import org.apache.commons.workflow.Context;
import org.apache.commons.workflow.Scope;
import org.apache.commons.workflow.Step;
import org.apache.commons.workflow.StepException;


/**
 * <p>Test Case for the BaseActivity class.  These tests exercise
 * basic <code>Activity</code> execution capabilities, without requiring
 * any existing <code>Step</code> implementations.</p>
 *
 * @author Craig R. McClanahan
 * @version $Revision$ $Date$
 */

public class BaseExecuteTestCase extends TestCase {


    // ----------------------------------------------------- Instance Variables


    /**
     * The activity instance we will be testing.
     */
    protected Activity activity = null;


    /**
     * The context instance we will be testing.
     */
    protected Context context = null;


    // ----------------------------------------------------------- Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public BaseExecuteTestCase(String name) {

        super(name);

    }


    // --------------------------------------------------- Overall Test Methods


    /**
     * Set up instance variables required by this test case.
     */
    public void setUp() {

        activity = new BaseActivity();
        context = new BaseContext();

    }


    /**
     * Return the tests included in this test suite.
     */
    public static Test suite() {

        return (new TestSuite(BaseExecuteTestCase.class));

    }


    /**
     * Tear down instance variables required by this test case.
     */
    public void tearDown() {

        context = null;
        activity = null;

    }


    // ------------------------------------------------ Individual Test Methods


    /**
     * Negative test (should throw StepException).
     */
    public void testBaseNegative() {

        activity.addStep(new TestStep("Step 0", false));
        activity.addStep(new TestStep("Step 1", true));
        activity.addStep(new TestStep("Step 2", false));
        context.setActivity(activity);

        TestStep.clearResults();
        try {
            context.execute();
            fail("Should have thrown StepException");
        } catch (StepException e) {
            assertEquals("Executed step 0-1", "Step 0/Step 1/",
                         TestStep.getResults());
            assertEquals("Exception thrown by step 1", "Step 1",
                         e.getStep().getId());
                         
        }

    }


    /**
     * Positive test (should execute the entire Activity).
     */
    public void testBasePositive() {

        activity.addStep(new TestStep("Step 0", false));
        activity.addStep(new TestStep("Step 1", false));
        activity.addStep(new TestStep("Step 2", false));
        context.setActivity(activity);

        TestStep.clearResults();
        try {
            context.execute();
            assertEquals("Executed step 0-2", "Step 0/Step 1/Step 2/",
                         TestStep.getResults());
        } catch (StepException e) {
            fail("Threw StepException");
        }

    }


}
