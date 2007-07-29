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


import java.io.File;
import java.util.EmptyStackException;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.commons.workflow.Activity;
import org.apache.commons.workflow.Context;
import org.apache.commons.workflow.ContextEvent;
import org.apache.commons.workflow.ContextListener;
import org.apache.commons.workflow.Scope;
import org.apache.commons.workflow.Step;
import org.apache.commons.workflow.StepException;
import org.apache.commons.workflow.base.BaseActivity;
import org.apache.commons.workflow.base.BaseContext;
import org.apache.commons.workflow.base.BaseScope;
import org.apache.commons.workflow.core.PopStep;
import org.apache.commons.workflow.core.StringStep;


/**
 * <p>Test Case for the <code>io</code> library of <code>Step</code>
 * implementations.</p>
 *
 * @author Craig R. McClanahan
 * @version $Revision$ $Date$
 */

public class IOExecuteTestCase extends TestCase
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
    public IOExecuteTestCase(String name) {

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

        return (new TestSuite(IOExecuteTestCase.class));

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
     * Get and display steps.  This test depends on a URL being specified
     * by the <code>ioexecute.url</code> system property, and that this
     * URL can be successfully retrieved via a <code>URLConnection</code>.
     * No validation of the retrieved content is performed - it is merely
     * displayed to system output with the <code>peek</code> command.
     */
    public void testGetDisplay() {

        // Identify the URL path we will be accessing
        String url = System.getProperty("ioexecute.url");
        assertNotNull("The 'ioexecute.url' system property is set", url);

        // Configure the steps in this activity
        activity.addStep(new GetStep("01", url));
        activity.addStep(new PeekStep("02"));
        activity.addStep(new PopStep("03"));

        // Execute the activity and validate results
        try {
            context.execute();
            assertEquals("Trail contents",
                         "beforeActivity()/" +
                         "beforeStep(01)/afterStep(01)/" +
                         "beforeStep(02)/afterStep(02)/" +
                         "beforeStep(03)/afterStep(03)/" +
                         "afterActivity()/",
                         trail.toString());
            assertTrue("Stack is empty",
                       context.isEmpty());
            assertTrue("Context was not suspended",
                       !context.getSuspend());
        } catch (StepException e) {
            e.printStackTrace(System.out);
            if (e.getCause() != null) {
                System.out.println("ROOT CAUSE");
                e.getCause().printStackTrace(System.out);
            }
            fail("Threw StepException " + e);
        } catch (Throwable e) {
            e.printStackTrace();
            fail("Threw exception " + e);
        }

    }


    /**
     * Read and write steps.
     */
    public void testReadWrite() {

        // Identify a temporary file that we can manipulate
        File file = null;
        String path = null;
        try {
            file = File.createTempFile("IOExecuteTestCase", ".txt");
            path = file.getAbsolutePath();
            file.delete();
        } catch (Throwable t) {
            fail("File create threw " + t);
        } 

        // Configure the steps in this activity
        activity.addStep
            (new StringStep("01", "This is a test.  It is only a test."));
        activity.addStep(new WriteStep("02", null, path));
        activity.addStep(new ReadStep("03", null, path));

        // Execute the activity and validate results
        try {
            context.execute();
            assertEquals("Trail contents",
                         "beforeActivity()/" +
                         "beforeStep(01)/afterStep(01)/" +
                         "beforeStep(02)/afterStep(02)/" +
                         "beforeStep(03)/afterStep(03)/" +
                         "afterActivity()/",
                         trail.toString());
            assertTrue("Stack is not empty",
                       !context.isEmpty());
            assertEquals("Retrieved written content",
                         "This is a test.  It is only a test.",
                         (String) context.pop());
            assertTrue("Stack is empty",
                       context.isEmpty());
            assertTrue("Context was not suspended",
                       !context.getSuspend());
        } catch (StepException e) {
            e.printStackTrace(System.out);
            if (e.getCause() != null) {
                System.out.println("ROOT CAUSE");
                e.getCause().printStackTrace(System.out);
            }
            fail("Threw StepException " + e);
        } catch (Throwable e) {
            e.printStackTrace();
            fail("Threw exception " + e);
        } finally {
            file.delete();
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
