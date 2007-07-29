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
import org.apache.commons.workflow.Context;
import org.apache.commons.workflow.ContextEvent;
import org.apache.commons.workflow.ContextListener;
import org.apache.commons.workflow.Scope;
import org.apache.commons.workflow.Step;
import org.apache.commons.workflow.StepException;
import org.apache.commons.workflow.base.BaseActivity;
import org.apache.commons.workflow.base.BaseContext;
import org.apache.commons.workflow.base.BaseDescriptor;
import org.apache.commons.workflow.base.BaseScope;
import org.apache.commons.workflow.base.Employee;


/**
 * <p>Test Case for the <code>core</code> library of <code>Step</code>
 * implementations.</p>
 *
 * @author Craig R. McClanahan
 * @version $Revision$ $Date$
 */

public class CoreExecuteTestCase extends TestCase
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
    public CoreExecuteTestCase(String name) {

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

        return (new TestSuite(CoreExecuteTestCase.class));

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
     * Call a nested Activity.
     */
    public void testCallNested() {

        // Configure the steps in this activity
        activity.addStep(new StringStep("01", "Original activity"));
        activity.addStep(new SuspendStep("02"));
        // Manually push a nested activity here
        activity.addStep(new CallStep("03"));
        /*
        activity.addStep(new DuplicateStep("04"));
        activity.addStep(new PopStep("05"));
        */

        // Execute the activity and validate results #1
        try {
            context.execute();
            assertEquals("Trail contents",
                         "beforeActivity()/" +
                         "beforeStep(01)/afterStep(01)/" +
                         "beforeStep(02)/afterStep(02)/" +
                         "afterActivity()/",
                         trail.toString());
            assertTrue("Stack is not empty",
                       !context.isEmpty());
            assertEquals("Top of stack string",
                         "Original activity",
                         (String) context.peek());
            assertTrue("Context was suspended",
                       context.getSuspend());
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

        // Manually push a nested Activity onto the evaluation stack
        Activity nested = new BaseActivity();
        nested.addStep(new StringStep("11", "Nested activity"));
        nested.addStep(new SwapStep("12"));
        context.push(nested);

        // Execute the activity and validate results #2
        try {
            context.execute();
            assertEquals("Trail contents",
                         "beforeActivity()/" +
                         "beforeStep(03)/afterStep(03)/" +
                         "beforeStep(11)/afterStep(11)/" +
                         "beforeStep(12)/afterStep(12)/" +
                         /*
                         "beforeStep(04)/afterStep(04)/" +
                         "beforeStep(05)/afterStep(05)/" +
                         */
                         "afterActivity()/",
                         trail.toString());
            assertTrue("Stack is not empty",
                       !context.isEmpty());
            assertEquals("Top of stack string",
                         "Original activity",
                         (String) context.pop());
            assertEquals("Bottom of stack string",
                         "Nested activity",
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
        }

    }


    /**
     * Test class loading and object construction.
     */
    public void testConstructor() {

        // Configure the steps in this activity
        context.push(new Integer(54321));         // Second argument
        context.push("Constructor String");       // First argument
        activity.addStep
            (new LoadStep("01", "org.apache.commons.workflow.core.TestBean"));
        activity.addStep(new SuspendStep("02"));
        ConstructStep construct =
            new ConstructStep("03", new BaseDescriptor());  // Class from stack
        construct.addDescriptor(new BaseDescriptor());      // Arg0 from stack
        construct.addDescriptor(new BaseDescriptor());      // Arg1 from stack
        activity.addStep(construct);

        // Execute the activity and validate results
        try {
            context.execute();
            assertEquals("Trail contents",
                         "beforeActivity()/" +
                         "beforeStep(01)/afterStep(01)/" +
                         "beforeStep(02)/afterStep(02)/" +
                         "afterActivity()/",
                         trail.toString());
            assertTrue("Stack is not empty",
                       !context.isEmpty());
            assertTrue("Context was suspended",
                       context.getSuspend());
            assertTrue("Top object is a Class",
                       context.peek() instanceof java.lang.Class);
            Class clazz = (Class) context.peek();
            assertEquals("Top object is the correct Class",
                         "org.apache.commons.workflow.core.TestBean",
                         clazz.getName());
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

        // Resume execution after suspension
        try {
            context.execute();
            assertEquals("Trail contents",
                         "beforeActivity()/" +
                         "beforeStep(03)/afterStep(03)/" +
                         "afterActivity()/",
                         trail.toString());
            assertTrue("Stack is not empty",
                       !context.isEmpty());
            assertTrue("Context was not suspended",
                       !context.getSuspend());
            Object top = context.pop();
            assertTrue("Created object is of correct Class",
                       top instanceof TestBean);
            TestBean bean = (TestBean) top;
            assertEquals("Configured stringProperty",
                         "Constructor String",
                         bean.getStringProperty());
            assertEquals("Configured intProperty",
                         54321, bean.getIntProperty());
            assertTrue("Stack is now empty",
                       context.isEmpty());
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
     * Control flow options.
     */
    public void testControlFlow() {

        // Configure the steps in this activity
        activity.addStep(new GotoStep("01", "03"));
        activity.addStep(new StringStep("02", "Should not be executed"));
        activity.addStep(new SuspendStep("03"));
        activity.addStep(new StringStep("04", "Only after resume"));

        // Execute the activity and validate results
        try {
            context.execute();
            assertEquals("Trail contents",
                         "beforeActivity()/" +
                         "beforeStep(01)/afterStep(01)/" +
                         "beforeStep(03)/afterStep(03)/" +
                         "afterActivity()/",
                         trail.toString());
            assertTrue("Stack is empty",
                       context.isEmpty());
            assertTrue("Context was suspended",
                       context.getSuspend());
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

        // Resume execution after suspension
        try {
            context.execute();
            assertEquals("Trail contents",
                         "beforeActivity()/" +
                         "beforeStep(04)/afterStep(04)/" +
                         "afterActivity()/",
                         trail.toString());
            assertTrue("Stack is not empty",
                       !context.isEmpty());
            assertTrue("Context was not suspended",
                       !context.getSuspend());
            assertEquals("Popped value is 'Only after resume'",
                         "Only after resume",
                         (String) context.pop());
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
     * Test invoking arbitrary methods with various combinations of
     * parameter mechanisms.
     */
    public void testInvoke() {

        // Stash a bean in local scope to play with
        TestBean bean = new TestBean();
        context.put("bean", bean);

        // Configure the steps in this activity
        activity.addStep(new InvokeStep("01", "getStringProperty",
                                        new BaseDescriptor("bean")));
        activity.addStep(new SuspendStep("02"));
        activity.addStep(new StringStep("03", "New String Value"));
        InvokeStep step04 =
            new InvokeStep("04", "setStringProperty",
                           new BaseDescriptor("bean"));
        step04.addDescriptor(new BaseDescriptor()); // Consume top of stack
        activity.addStep(step04);
        activity.addStep(new SuspendStep("04"));
        activity.addStep(new InvokeStep("05", "getStringProperty",
                                        new BaseDescriptor("bean")));
        activity.addStep(new SuspendStep("06"));

        // Execute the activity and validate results #1
        try {
            context.execute();
            assertTrue("Context is suspended",
                       context.getSuspend());
            assertTrue("Stack is not empty",
                       !context.isEmpty());
            Object top = context.pop();
            assertTrue("Stack is empty",
                       context.isEmpty());
            assertTrue("getStringProperty() returned a String",
                       top instanceof String);
            assertEquals("getStringProperty() old value",
                         bean.getStringProperty(), (String) top);
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

        // Execute the activity and validate results #2
        try {
            context.execute();
            assertTrue("Context is suspended",
                       context.getSuspend());
            assertTrue("Stack is empty",
                       context.isEmpty());
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

        // Execute the activity and validate results #3
        try {
            context.execute();
            assertTrue("Context is suspended",
                       context.getSuspend());
            assertTrue("Stack is not empty",
                       !context.isEmpty());
            Object top = context.pop();
            assertTrue("Stack is empty",
                       context.isEmpty());
            assertTrue("getStringProperty() returned a String",
                       top instanceof String);
            assertEquals("getStringProperty() new value",
                         bean.getStringProperty(), (String) top);
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
     * Test access to variables in various scopes.
     */
    public void testScopeAccess() {

        // Configure an extra scope and put one object in each scope
        Scope extra = new BaseScope();
        context.addScope(Context.LOCAL_SCOPE + 1, "extra", extra);
        context.put("local0", "This is local0");
        context.put("extra0", "This is extra0", Context.LOCAL_SCOPE + 1);

        // Configure the steps in this activity
        activity.addStep(new GetStep("01",
                                     new BaseDescriptor("local0", "local")));
        activity.addStep(new GetStep("02",
                                     new BaseDescriptor("extra0", "extra")));
        activity.addStep(new SuspendStep("03"));
        // NOTE: test will clear the stack
        activity.addStep(new GetStep("04",
                                     new BaseDescriptor("local0")));
        activity.addStep(new GetStep("05",
                                     new BaseDescriptor("extra/extra0")));
        activity.addStep(new SuspendStep("06"));
        // NOTE: test will clear the stack
        activity.addStep(new StringStep("07", "New local0"));
        activity.addStep(new PutStep("08",
                                     new BaseDescriptor("local0", "local")));
        activity.addStep(new StringStep("09", "New extra0"));
        activity.addStep(new PutStep("10",
                                     new BaseDescriptor("extra0", "extra")));
        activity.addStep(new GetStep("11",
                                     new BaseDescriptor("local0", "local")));
        activity.addStep(new GetStep("12",
                                     new BaseDescriptor("extra0", "extra")));
        activity.addStep(new SuspendStep("13"));
        // NOTE: test will clear the stack
        activity.addStep(new GetStep("14",
                                     new BaseDescriptor("local0")));
        activity.addStep(new GetStep("15",
                                     new BaseDescriptor("extra/extra0")));
        activity.addStep(new SuspendStep("16"));
        // NOTE: test will clear the stack
        activity.addStep(new RemoveStep("17",
                                        new BaseDescriptor("local0", "local")));
        activity.addStep(new RemoveStep("18",
                                        new BaseDescriptor("extra0", "extra")));
        activity.addStep(new GetStep("19",
                                     new BaseDescriptor("local0", "local")));
        // NOTE: previous step should throw StepException
        activity.addStep(new GetStep("20",
                                     new BaseDescriptor("extra0", "local")));
        // NOTE: previous step should throw StepException
        activity.addStep(new GetStep("21",
                                     new BaseDescriptor("local0")));
        // NOTE: previous step should throw StepException
        activity.addStep(new GetStep("22",
                                     new BaseDescriptor("extra/extra0")));
        // NOTE: previous step should throw StepException

        // Execute the activity and validate results #1
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
            assertEquals("Popped value is 'This is extra0'",
                         "This is extra0",
                         (String) context.pop());
            assertTrue("Stack is not empty",
                       !context.isEmpty());
            assertEquals("Popped value is 'This is local0'",
                         "This is local0",
                         (String) context.pop());
            assertTrue("Stack is empty",
                       context.isEmpty());
            assertTrue("Context was suspended",
                       context.getSuspend());
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

        // Execute the activity and validate results #2
        try {
            context.execute();
            assertEquals("Trail contents",
                         "beforeActivity()/" +
                         "beforeStep(04)/afterStep(04)/" +
                         "beforeStep(05)/afterStep(05)/" +
                         "beforeStep(06)/afterStep(06)/" +
                         "afterActivity()/",
                         trail.toString());
            assertTrue("Stack is not empty",
                       !context.isEmpty());
            assertEquals("Popped value is 'This is extra0'",
                         "This is extra0",
                         (String) context.pop());
            assertTrue("Stack is not empty",
                       !context.isEmpty());
            assertEquals("Popped value is 'This is local0'",
                         "This is local0",
                         (String) context.pop());
            assertTrue("Stack is empty",
                       context.isEmpty());
            assertTrue("Context was suspended",
                       context.getSuspend());
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

        // Execute the activity and validate results #3
        try {
            context.execute();
            assertEquals("Trail contents",
                         "beforeActivity()/" +
                         "beforeStep(07)/afterStep(07)/" +
                         "beforeStep(08)/afterStep(08)/" +
                         "beforeStep(09)/afterStep(09)/" +
                         "beforeStep(10)/afterStep(10)/" +
                         "beforeStep(11)/afterStep(11)/" +
                         "beforeStep(12)/afterStep(12)/" +
                         "beforeStep(13)/afterStep(13)/" +
                         "afterActivity()/",
                         trail.toString());
            assertTrue("Stack is not empty",
                       !context.isEmpty());
            assertEquals("Popped value is 'New extra0'",
                         "New extra0",
                         (String) context.pop());
            assertTrue("Stack is not empty",
                       !context.isEmpty());
            assertEquals("Popped value is 'New local0'",
                         "New local0",
                         (String) context.pop());
            assertTrue("Stack is empty",
                       context.isEmpty());
            assertTrue("Context was suspended",
                       context.getSuspend());
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

        // Execute the activity and validate results #4
        try {
            context.execute();
            assertEquals("Trail contents",
                         "beforeActivity()/" +
                         "beforeStep(14)/afterStep(14)/" +
                         "beforeStep(15)/afterStep(15)/" +
                         "beforeStep(16)/afterStep(16)/" +
                         "afterActivity()/",
                         trail.toString());
            assertTrue("Stack is not empty",
                       !context.isEmpty());
            assertEquals("Popped value is 'New extra0'",
                         "New extra0",
                         (String) context.pop());
            assertTrue("Stack is not empty",
                       !context.isEmpty());
            assertEquals("Popped value is 'New local0'",
                         "New local0",
                         (String) context.pop());
            assertTrue("Stack is empty",
                       context.isEmpty());
            assertTrue("Context was suspended",
                       context.getSuspend());
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

        // Execute the activity and validate results #5
        try {
            context.execute();
            fail("Should have thrown StepException");
        } catch (StepException e) {
            // Expected result
            assertEquals("Trail contents",
                         "beforeActivity()/" +
                         "beforeStep(17)/afterStep(17)/" +
                         "beforeStep(18)/afterStep(18)/" +
                         "beforeStep(19)/afterStep(19)/" +
                         "afterActivity()/",
                         trail.toString());
            assertTrue("Context was not suspended",
                       !context.getSuspend());
        } catch (Throwable e) {
            e.printStackTrace();
            fail("Threw exception " + e);
        }

        // Execute the activity and validate results #6
        try {
            context.execute();
            fail("Should have thrown StepException");
        } catch (StepException e) {
            // Expected result
            assertEquals("Trail contents",
                         "beforeActivity()/" +
                         "beforeStep(20)/afterStep(20)/" +
                         "afterActivity()/",
                         trail.toString());
            assertTrue("Context was not suspended",
                       !context.getSuspend());
        } catch (Throwable e) {
            e.printStackTrace();
            fail("Threw exception " + e);
        }

        // Execute the activity and validate results #7
        try {
            context.execute();
            fail("Should have thrown StepException");
        } catch (StepException e) {
            // Expected result
            assertEquals("Trail contents",
                         "beforeActivity()/" +
                         "beforeStep(21)/afterStep(21)/" +
                         "afterActivity()/",
                         trail.toString());
            assertTrue("Context was not suspended",
                       !context.getSuspend());
        } catch (Throwable e) {
            e.printStackTrace();
            fail("Threw exception " + e);
        }

        // Execute the activity and validate results #8
        try {
            context.execute();
            fail("Should have thrown StepException");
        } catch (StepException e) {
            // Expected result
            assertEquals("Trail contents",
                         "beforeActivity()/" +
                         "beforeStep(22)/afterStep(22)/" +
                         "afterActivity()/",
                         trail.toString());
            assertTrue("Context was not suspended",
                       !context.getSuspend());
        } catch (Throwable e) {
            e.printStackTrace();
            fail("Threw exception " + e);
        }

    }


    /**
     * Simplest possible activity execution.
     */
    public void testSimplestPossible() {

        // Configure the steps in this activity
        activity.addStep(new ExitStep("01"));

        // Execute the activity and validate results
        try {
            context.execute();
            assertEquals("Trail contents",
                         "beforeActivity()/" +
                         "beforeStep(01)/afterStep(01)/" +
                         "afterActivity()/",
                         trail.toString());
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
     * Stack manipulation steps.
     */
    public void testStackManipulation() {

        // Configure the steps in this activity
        activity.addStep(new StringStep("01", "First Value"));
        activity.addStep(new DuplicateStep("02"));
        activity.addStep(new StringStep("03", "Second Value"));
        activity.addStep(new SwapStep("04"));
        activity.addStep(new PopStep("05"));

        // Execute the activity and validate results
        try {
            context.execute();
            assertEquals("Trail contents",
                         "beforeActivity()/" +
                         "beforeStep(01)/afterStep(01)/" +
                         "beforeStep(02)/afterStep(02)/" +
                         "beforeStep(03)/afterStep(03)/" +
                         "beforeStep(04)/afterStep(04)/" +
                         "beforeStep(05)/afterStep(05)/" +
                         "afterActivity()/",
                         trail.toString());
            assertTrue("Stack not empty",
                       !context.isEmpty());
            assertEquals("Popped value is 'Second Value'",
                         "Second Value",
                         (String) context.pop());
            assertTrue("Stack still not empty",
                       !context.isEmpty());
            assertEquals("Popped value is 'First Value'",
                         "First Value",
                         (String) context.pop());
            assertTrue("Stack is now empty",
                       context.isEmpty());
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
     * Test getting and setting beans through XPath expressions.
     */
    public void testXpathBean() {

        // Configure an extra scope and put one object in each scope
        Scope extra = new BaseScope();
        context.addScope(Context.LOCAL_SCOPE + 1, "extra", extra);

        // Push a bean onto the evaluation stack
        Employee employee = new Employee();
        context.push(employee);

        // Configure the steps in this activity
        activity.addStep(new PutStep("01",
                                     new BaseDescriptor("extra/employee")));
        activity.addStep(new SuspendStep("02"));
        activity.addStep(new GetStep("03",
                                     new BaseDescriptor("extra/employee")));
        activity.addStep(new SuspendStep("04"));

        // Execute the activity and validate results #1
        try {
            context.execute();
            assertEquals("Trail contents 1",
                         "beforeActivity()/" +
                         "beforeStep(01)/afterStep(01)/" +
                         "beforeStep(02)/afterStep(02)/" +
                         "afterActivity()/",
                         trail.toString());
            assertTrue("Context is suspended",
                       context.getSuspend());
            assertTrue("Stack is empty",
                       context.isEmpty());
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

        // Execute the activity and validate results #2
        try {
            context.execute();
            assertEquals("Trail contents 2",
                         "beforeActivity()/" +
                         "beforeStep(03)/afterStep(03)/" +
                         "beforeStep(04)/afterStep(04)/" +
                         "afterActivity()/",
                         trail.toString());
            assertTrue("Context is suspended",
                       context.getSuspend());
            assertTrue("Stack is not empty",
                       !context.isEmpty());
            Object top = context.peek();
            assertNotNull("Top object is not null", top);
            assertTrue("Top object is an Employee",
                       top instanceof Employee);
            assertEquals("Top object is our employee",
                         employee, (Employee) top);
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
     * Test setting and getting properties via XPath expressions.
     */
    public void testXpathProperties() {

        // Stash a bean in local scope to play with
        Employee employee = new Employee();
        context.put("employee", employee);

        // Configure the steps in this activity
        activity.addStep(new GetStep("01",
                                     new BaseDescriptor("employee/firstName")));
        activity.addStep(new SuspendStep("02"));
        // Test will remove the retrieved String value
        activity.addStep(new StringStep("03", "The New First Name"));
        activity.addStep(new PutStep("04",
                                     new BaseDescriptor("employee/firstName")));
        activity.addStep(new SuspendStep("05"));

        // Execute the activity and validate results #1
        try {
            context.execute();
            assertEquals("Trail contents 1",
                         "beforeActivity()/" +
                         "beforeStep(01)/afterStep(01)/" +
                         "beforeStep(02)/afterStep(02)/" +
                         "afterActivity()/",
                         trail.toString());
            assertTrue("Context is suspended",
                       context.getSuspend());
            assertTrue("Stack is not empty",
                       !context.isEmpty());
            Object top = context.pop();
            assertTrue("Stack is empty",
                       context.isEmpty());
            assertTrue("employee/firstName returned a String",
                       top instanceof String);
            assertEquals("employee/firstName old value",
                         employee.getFirstName(), (String) top);
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

        // Execute the activity and validate results #2
        try {
            context.execute();
            assertEquals("Trail contents 2",
                         "beforeActivity()/" +
                         "beforeStep(03)/afterStep(03)/" +
                         "beforeStep(04)/afterStep(04)/" +
                         "beforeStep(05)/afterStep(05)/" +
                         "afterActivity()/",
                         trail.toString());
            assertTrue("Context is suspended",
                       context.getSuspend());
            assertTrue("Stack is empty",
                       context.isEmpty());
            assertEquals("employee/firstName new value",
                         "The New First Name",
                         employee.getFirstName());
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
