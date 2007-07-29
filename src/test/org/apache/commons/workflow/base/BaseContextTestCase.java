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
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.workflow.Context;
import org.apache.commons.workflow.Scope;


/**
 * <p>Test Case for the BaseContext class.  These tests exercise those
 * functions that are not dependent upon an associated Activity.</p>
 *
 * @author Craig R. McClanahan
 * @version $Revision$ $Date$
 */

public class BaseContextTestCase extends TestCase {


    // ----------------------------------------------------- Instance Variables


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
    public BaseContextTestCase(String name) {

        super(name);

    }


    // -------------------------------------------------- Overall Test Methods


    /**
     * Set up instance variables required by this test case.
     */
    public void setUp() {

        context = new BaseContext();

    }


    /**
     * Return the tests included in this test suite.
     */
    public static Test suite() {

        return (new TestSuite(BaseContextTestCase.class));

    }


    /**
     * Tear down instance variables required by this test case.
     */
    public void tearDown() {

        context = null;

    }



    // ------------------------------------------------ Individual Test Methods


    /**
     * Add a new scope and test direct access to it.
     */
    public void testAddScope() {

        assertNull("Should not find 'extra' scope by id",
                   context.getScope(Context.LOCAL_SCOPE + 1));
        assertNull("Should not find 'extra' scope by name",
                   context.getScope("extra"));
        Scope scope = new BaseScope();
        context.addScope(Context.LOCAL_SCOPE + 1, "extra", scope);
        assertEquals("Should look up 'extra' scope id",
                     context.getScopeId("extra"),
                     Context.LOCAL_SCOPE + 1);
        assertNotNull("Should find 'extra' scope by id",
                      context.getScope(Context.LOCAL_SCOPE + 1));
        assertNotNull("Should find 'extra' scope by name",
                      context.getScope("extra"));
        assertEquals("Should match 'extra' scope by id", scope,
                     context.getScope(Context.LOCAL_SCOPE + 1));
        assertEquals("Should match 'extra' scope by name", scope,
                     context.getScope("extra"));
        assertEquals("Should find 'extra' scope in 'local' scope", scope,
                     context.get("extra", Context.LOCAL_SCOPE));
        commonDirectScope(Context.LOCAL_SCOPE + 1);

    }


    /**
     * Test default access to LOCAL scope.
     */
    public void testDefaultScope() {

        assertTrue("Should not contain A #1",
                   !context.contains("BaseContextTestCase.A"));
        context.put("BaseContextTestCase.A", "A Value");
        assertTrue("Should contain A #1",
                   context.contains("BaseContextTestCase.A"));
        assertEquals("Should get A #1", "A Value",
                     (String) context.get("BaseContextTestCase.A"));

        assertTrue("Should not contain B #2",
                   !context.contains("BaseContextTestCase.B"));
        context.put("BaseContextTestCase.B", "B Value");
        assertTrue("Should contain B #2",
                   context.contains("BaseContextTestCase.B"));
        assertEquals("Should get B #2", "B Value",
                     (String) context.get("BaseContextTestCase.B"));
        assertTrue("Should contain A #2",
                   context.contains("BaseContextTestCase.A"));
        assertEquals("Should get A #2", "A Value",
                     (String) context.get("BaseContextTestCase.A"));

        context.remove("BaseContextTestCase.B");
        assertTrue("Should not contain B #3",
                   !context.contains("BaseContextTestCase.B"));
        assertTrue("Should contain A #3",
                   context.contains("BaseContextTestCase.A"));
        assertEquals("Should get A #3", "A Value",
                     (String) context.get("BaseContextTestCase.A"));

        context.remove("BaseContextTestCase.A");
        assertTrue("Should not contain A #4",
                   !context.contains("BaseContextTestCase.A"));
        assertTrue("Should not contain B #4",
                   !context.contains("BaseContextTestCase.B"));

    }


    /**
     * Test direct access to LOCAL scope.
     */
    public void testDirectScope() {

        // Common directed tests
        commonDirectScope(Context.LOCAL_SCOPE);

        // Implicit --> Explicit visibility
        assertTrue("Should not contain C #1",
                   !context.contains("BaseContextTestCase.C",
                                     Context.LOCAL_SCOPE));
        context.put("BaseContextTestCase.C", "C Value");
        assertTrue("Should contain C #1",
                   context.contains("BaseContextTestCase.C",
                                    Context.LOCAL_SCOPE));
        assertEquals("Should get C #1", "C Value",
                     (String) context.get("BaseContextTestCase.C",
                                          Context.LOCAL_SCOPE));
        context.remove("BaseContextTestCase.C");
        assertTrue("Should not contain C #1",
                   !context.contains("BaseContextTestCase.C",
                                     Context.LOCAL_SCOPE));

        // Explicit --> Implicit visibility
        assertTrue("Should not contain D #2",
                   !context.contains("BaseContextTestCase.D"));
        context.put("BaseContextTestCase.D", "D Value", Context.LOCAL_SCOPE);
        assertTrue("Should contain D #2",
                   context.contains("BaseContextTestCase.D"));
        assertEquals("Should get D #2", "D Value",
                     (String) context.get("BaseContextTestCase.D"));
        context.remove("BaseContextTestCase.D", Context.LOCAL_SCOPE);
        assertTrue("Should not contain D #2",
                   !context.contains("BaseContextTestCase.D"));

    }


    /**
     * <p>Test the returned JXPathContext for accessing items in the unified
     * namespace.</p>
     *
     * <p><strong>WARNING:</strong> - Dependent upon how the Scopes associated
     * with our Context are mapped to JXPathContext objects.</p>
     */
    public void testJXPathContext() {

        // Set up an extra scope and put one object in each scope
        Scope extra = new BaseScope();
        context.addScope(Context.LOCAL_SCOPE + 1, "extra", extra);
        context.put("local0", "This is local0");
        context.put("extra0", "This is extra0", Context.LOCAL_SCOPE + 1);

        // Validate access using Context access methods
        assertEquals("Context can find extra",
                     extra,
                     context.get("extra", Context.LOCAL_SCOPE));
        assertEquals("Context can find local0",
                     "This is local0",
                     context.get("local0", Context.LOCAL_SCOPE));
        assertEquals("Context can find extra0",
                     "This is extra0",
                     context.get("extra0", Context.LOCAL_SCOPE + 1));

        // Extract the associated JXPathContext
        JXPathContext jpc = context.getJXPathContext();
        assertNotNull("Context can find JXPathContext", jpc);
        assertNotNull("JXPathContext can find local",
                      jpc.getValue("local"));
        assertNotNull("JXPathContext can find extra",
                          jpc.getValue("local/extra"));

        // Validate access using JXPath access methods
        assertEquals("JXPathContext can find local0",
                     "This is local0",
                     (String) jpc.getValue("local/local0"));
        assertEquals("JXPathContext can find extra0",
                     "This is extra0",
                     (String) jpc.getValue("local/extra/extra0"));

    }


    /**
     * Test basic JXPath functionality that we rely on.
     */
    public void testJXPathFunctionality() {

        Employee emp = new Employee("Test First Name", "Test Last Name");
        Address homeAddress = new Address();
        homeAddress.setZipCode("HmZip");
        emp.addAddress("home", homeAddress);
        Address officeAddress = new Address();
        officeAddress.setZipCode("OfZip");
        emp.addAddress("office", officeAddress);

        JXPathContext jpc = JXPathContext.newContext(emp);
        assertNotNull("Can create JXPathContext", jpc);

        assertEquals("Get firstName", "Test First Name",
                     (String) jpc.getValue("firstName"));
        assertEquals("Get lastName", "Test Last Name",
                     (String) jpc.getValue("lastName"));
        assertNotNull("Get addresses", jpc.getValue("addresses"));
        assertEquals("Get addresses/home/zipCode",
                     "HmZip",
                     (String) jpc.getValue("addresses/home/zipCode"));
        assertEquals("Get addresses/office/zipCode",
                     "OfZip",
                     (String) jpc.getValue("addresses/office/zipCode"));

    }


    /**
     * Test the basic manipulation of the associated LOCAL scope.
     */
    public void testLocalScope() {

        commonExplicitScope("local", Context.LOCAL_SCOPE);

    }


    /**
     * Test the basic stack mechanisms.
     */
    public void testStackMethods() {

        Object value = null;

        // New stack must be empty
        assertTrue("New stack is empty", context.isEmpty());
        try {
            value = context.peek();
            fail("Did not throw EmptyStackException on empty peek()");
        } catch (EmptyStackException e) {
            ; // Expected result
        }
        try {
            value = context.pop();
            fail("Did not throw EmptyStackException on empty pop()");
        } catch (EmptyStackException e) {
            ; // Expected result
        }

        // Test pushing and popping activities
        context.push("First Item");
        value = context.peek();
        assertNotNull("Peeked first item is not null", value);
        assertEquals("Peeked first item value",
                     "First Item", (String) value);

        context.push("Second Item");
        value = context.peek();
        assertNotNull("Peeked second item is not null", value);
        assertEquals("Peeked second item value",
                     "Second Item", (String) value);

        value = context.pop();
        assertNotNull("Popped second item is not null", value);
        assertEquals("Popped second item value",
                     "Second Item", (String) value);

        value = context.peek();
        assertNotNull("Remaining item is not null", value);
        assertEquals("Remaining item value", "First Item", (String) value);

        // Cleared stack is empty
        context.push("Dummy Item");
        context.clear();
        assertTrue("Cleared stack is empty", context.isEmpty());
        try {
            value = context.peek();
            fail("Did not throw EmptyStackException on empty peek()");
        } catch (EmptyStackException e) {
            ; // Expected result
        }
        try {
            value = context.pop();
            fail("Did not throw EmptyStackException on empty pop()");
        } catch (EmptyStackException e) {
            ; // Expected result
        }

    }


    // ------------------------------------------------------ Protected Methods


    /**
     * Common base tests for direct access to a Scope associated with
     * this Context.
     *
     * @param id Scope identifier
     */
    protected void commonDirectScope(int id) {

        assertTrue("Should not contain A #1",
                   !context.contains("BaseContextTestCase.A", id));
        context.put("BaseContextTestCase.A", "A Value", id);
        assertTrue("Should contain A #1",
                   context.contains("BaseContextTestCase.A", id));
        assertEquals("Should get A #1", "A Value",
                     (String) context.get("BaseContextTestCase.A", id));

        assertTrue("Should not contain B #2",
                   !context.contains("BaseContextTestCase.B", id));
        context.put("BaseContextTestCase.B", "B Value", id);
        assertTrue("Should contain B #2",
                   context.contains("BaseContextTestCase.B", id));
        assertEquals("Should get B #2", "B Value",
                     (String) context.get("BaseContextTestCase.B", id));
        assertTrue("Should contain A #2",
                   context.contains("BaseContextTestCase.A", id));
        assertEquals("Should get A #2", "A Value",
                     (String) context.get("BaseContextTestCase.A", id));

        context.remove("BaseContextTestCase.B", id);
        assertTrue("Should not contain B #3",
                   !context.contains("BaseContextTestCase.B", id));
        assertTrue("Should contain A #3",
                   context.contains("BaseContextTestCase.A", id));
        assertEquals("Should get A #3", "A Value",
                     (String) context.get("BaseContextTestCase.A", id));

        context.remove("BaseContextTestCase.A", id);
        assertTrue("Should not contain A #4",
                   !context.contains("BaseContextTestCase.A", id));
        assertTrue("Should not contain B #4",
                   !context.contains("BaseContextTestCase.B", id));

    }


    /**
     * Common base tests for a Scope associated with this Context.
     *
     * @param name Name of the scope to be tested
     * @param id Expected scope identifier
     */
    protected void commonExplicitScope(String name, int id) {

        // Verify that we can retrieve the specified scope
        int scopeId = context.getScopeId(name);
        assertEquals("Scope identifier for " + name,
                     id, scopeId);
        if (scopeId < 0)
            return;
        Scope scope = context.getScope(scopeId);
        assertNotNull("Found scope " + name, scope);
        if (scope == null)
               return;
        assertNull("get(null) returns null", scope.get(null));
        scope.clear();

        // Perform explicit tests on this scope
        assertTrue("Should not contain A #1",
                   !scope.containsKey("BaseContextTestCase.A"));
        scope.put("BaseContextTestCase.A", "A Value");
        assertTrue("Should contain A #1",
                   scope.containsKey("BaseContextTestCase.A"));
        assertEquals("Should get A #1", "A Value",
                     (String) scope.get("BaseContextTestCase.A"));

        assertTrue("Should not contain B #2",
                   !scope.containsKey("BaseContextTestCase.B"));
        scope.put("BaseContextTestCase.B", "B Value");
        assertTrue("Should contain B #2",
                   scope.containsKey("BaseContextTestCase.B"));
        assertEquals("Should get B #2", "B Value",
                     (String) scope.get("BaseContextTestCase.B"));
        assertTrue("Should contain A #2",
                   scope.containsKey("BaseContextTestCase.A"));
        assertEquals("Should get A #2", "A Value",
                     (String) scope.get("BaseContextTestCase.A"));

        scope.remove("BaseContextTestCase.B");
        assertTrue("Should not contain B #3",
                   !scope.containsKey("BaseContextTestCase.B"));
        assertTrue("Should contain A #3",
                   scope.containsKey("BaseContextTestCase.A"));
        assertEquals("Should get A #3", "A Value",
                     (String) scope.get("BaseContextTestCase.A"));

        scope.remove("BaseContextTestCase.A");
        assertTrue("Should not contain A #4",
                   !scope.containsKey("BaseContextTestCase.A"));
        assertTrue("Should not contain B #4",
                   !scope.containsKey("BaseContextTestCase.B"));

    }


}
