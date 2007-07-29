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


package org.apache.commons.workflow.web;


import java.util.EmptyStackException;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.commons.workflow.Activity;
import org.apache.commons.workflow.Context;
import org.apache.commons.workflow.Scope;
import org.apache.commons.workflow.Step;
import org.apache.commons.workflow.StepException;
import org.apache.commons.workflow.base.BaseActivity;
import org.apache.commons.workflow.base.BaseExecuteTestCase;
import org.apache.commons.workflow.base.TestStep;


/**
 * <p>Test Case for the BaseActivity class.  These tests exercise
 * basic <code>Activity</code> execution capabilities, without requiring
 * any existing <code>Step</code> implementations, against a
 * <code>WebContext</code> instance.</p>
 *
 * @author Craig R. McClanahan
 * @version $Revision$ $Date$
 */

public class WebExecuteTestCase extends BaseExecuteTestCase {


    // ----------------------------------------------------- Instance Variables


    // ----------------------------------------------------------- Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public WebExecuteTestCase(String name) {

        super(name);

    }


    // --------------------------------------------------- Overall Test Methods


    /**
     * Set up instance variables required by this test case.
     */
    public void setUp() {

        activity = new BaseActivity();
        context = new WebContext();

    }


    /**
     * Return the tests included in this test suite.
     */
    public static Test suite() {

        return (new TestSuite(WebExecuteTestCase.class));

    }


    /**
     * Tear down instance variables required by this test case.
     */
    public void tearDown() {

        activity = null;
        context = null;

    }


    // ------------------------------------------------ Individual Test Methods


}
