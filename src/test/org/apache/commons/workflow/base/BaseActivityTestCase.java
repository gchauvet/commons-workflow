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
import org.apache.commons.workflow.Scope;
import org.apache.commons.workflow.Step;


/**
 * <p>Test Case for the BaseActivity class.  These tests exercise those
 * features of Activity and Step implementations that do not perform any
 * Step execution (and therefore require a Context).</p>
 *
 * @author Craig R. McClanahan
 * @version $Revision$ $Date$
 */

public class BaseActivityTestCase extends TestCase {


    // ----------------------------------------------------- Instance Variables


    /**
     * The activity instance we will be testing.
     */
    protected Activity activity = null;


    // ----------------------------------------------------------- Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public BaseActivityTestCase(String name) {

        super(name);

    }


    // --------------------------------------------------- Overall Test Methods


    /**
     * Set up instance variables required by this test case.
     */
    public void setUp() {

        activity = new BaseActivity();

    }


    /**
     * Return the tests included in this test suite.
     */
    public static Test suite() {

        return (new TestSuite(BaseActivityTestCase.class));

    }


    /**
     * Tear down instance variables required by this test case.
     */
    public void tearDown() {

        activity = null;

    }


    // ------------------------------------------------ Individual Test Methods


    /**
     * Test default properties upon creation time.
     */
    public void testDefaultProperties() {

        assertNull("firstStep should be null", activity.getFirstStep());
        assertNull("lastStep should be null", activity.getLastStep());
        Step steps[] = activity.getSteps();
        if (steps == null)
            assertNotNull("steps[] should be not null", steps);
        else
            assertEquals("steps[] should be zero length", 0, steps.length);

    }


    /**
     * Test adding a group of steps.
     */
    public void testGroupSteps() {

        Step step0 = new TestStep("Step 0");
        Step step1 = new TestStep("Step 1");
        Step step2 = new TestStep("Step 2");
        Step steps[] = new Step[] { step0, step1, step2 };
        activity.setSteps(steps);
        assertEquals("step0 belongs to activity",
                     (Activity) step0.getOwner(), activity);
        assertEquals("step1 belongs to activity",
                     (Activity) step1.getOwner(), activity);
        assertEquals("step2 belongs to activity",
                     (Activity) step2.getOwner(), activity);

        assertEquals("firstStep should be step 0", step0,
                     activity.getFirstStep());
        assertEquals("lastStep should be step 2", step2,
                     activity.getLastStep());
        assertNull("step0 previous step should be null",
                   step0.getPreviousStep());
        assertEquals("step0 next step should be step 1",
                     step1, step0.getNextStep());
        assertEquals("step1 previous step should be step 0",
                     step0, step1.getPreviousStep());
        assertEquals("step1 next step should be step2",
                     step2, step1.getNextStep());
        assertEquals("step2 previous step should be step1",
                     step1, step2.getPreviousStep());
        assertNull("step2 next step should be null",
                   step2.getNextStep());
        steps = activity.getSteps();
        assertEquals("steps.length should be 3", 3, steps.length);
        assertEquals("steps[0] should be step 0", step0, steps[0]);
        assertEquals("steps[1] should be step 1", step1, steps[1]);
        assertEquals("steps[2] should be step 2", step2, steps[2]);

        activity.clearSteps();
        assertNull("firstStep should be null", activity.getFirstStep());
        assertNull("lastStep should be null", activity.getLastStep());
        steps = activity.getSteps();
        assertEquals("steps.length should be zero", 0, steps.length);

    }


    /**
     * Test adding individual steps.
     */
    public void testIndividualSteps() {

        Step steps[] = null;

        Step step0 = new TestStep("Step 0");
        activity.addStep(step0);
        assertEquals("step0 belongs to activity",
                     (Activity) step0.getOwner(), activity);
        assertEquals("firstStep should be step 0", step0,
                     activity.getFirstStep());
        assertEquals("lastStep should be step 0", step0,
                     activity.getLastStep());
        assertNull("step0 previous step should be null",
                   step0.getPreviousStep());
        assertNull("step0 next step should be null",
                   step0.getNextStep());
        steps = activity.getSteps();
        assertEquals("steps.length should be 1", 1, steps.length);
        assertEquals("steps[0] should be step 0", step0, steps[0]);

        Step step1 = new TestStep("Step 1");
        activity.addStep(step1);
        assertEquals("step1 belongs to activity",
                     (Activity) step1.getOwner(), activity);
        assertEquals("firstStep should be step 0", step0,
                     activity.getFirstStep());
        assertEquals("lastStep should be step 1", step1,
                     activity.getLastStep());
        assertNull("step0 previous step should be null",
                   step0.getPreviousStep());
        assertEquals("step0 next step should be step 1",
                     step1, step0.getNextStep());
        assertEquals("step1 previous step should be step 0",
                     step0, step1.getPreviousStep());
        assertNull("step1 next step should be null",
                   step1.getNextStep());
        steps = activity.getSteps();
        assertEquals("steps.length should be 2", 2, steps.length);
        assertEquals("steps[0] should be step 0", step0, steps[0]);
        assertEquals("steps[1] should be step 1", step1, steps[1]);

        Step step2 = new TestStep("Step 2");
        activity.addStep(step2);
        assertEquals("step2 belongs to activity",
                     (Activity) step2.getOwner(), activity);
        assertEquals("firstStep should be step 0", step0,
                     activity.getFirstStep());
        assertEquals("lastStep should be step 2", step2,
                     activity.getLastStep());
        assertNull("step0 previous step should be null",
                   step0.getPreviousStep());
        assertEquals("step0 next step should be step 1",
                     step1, step0.getNextStep());
        assertEquals("step1 previous step should be step 0",
                     step0, step1.getPreviousStep());
        assertEquals("step1 next step should be step2",
                     step2, step1.getNextStep());
        assertEquals("step2 previous step should be step1",
                     step1, step2.getPreviousStep());
        assertNull("step2 next step should be null",
                   step2.getNextStep());
        steps = activity.getSteps();
        assertEquals("steps.length should be 3", 3, steps.length);
        assertEquals("steps[0] should be step 0", step0, steps[0]);
        assertEquals("steps[1] should be step 1", step1, steps[1]);
        assertEquals("steps[2] should be step 2", step2, steps[2]);

        activity.clearSteps();
        assertNull("firstStep should be null", activity.getFirstStep());
        assertNull("lastStep should be null", activity.getLastStep());
        steps = activity.getSteps();
        assertEquals("steps.length should be zero", 0, steps.length);

    }

}
