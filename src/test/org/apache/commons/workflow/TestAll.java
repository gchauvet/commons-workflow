/*
 * Copyright 1999-2004 The Apache Software Foundation.
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

package org.apache.commons.workflow;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.commons.workflow.base.BaseActivityTestCase;
import org.apache.commons.workflow.base.BaseContextTestCase;
import org.apache.commons.workflow.base.BaseExecuteTestCase;
import org.apache.commons.workflow.core.CoreBlockTestCase;
import org.apache.commons.workflow.core.CoreExecuteTestCase;
import org.apache.commons.workflow.io.IOExecuteTestCase;
import org.apache.commons.workflow.web.WebContextTestCase;
import org.apache.commons.workflow.web.WebExecuteTestCase;

/**
 * Tests for org.apache.commons.workflow
 * 
 * @author Dirk Verbeeck
 * @version $Revision$ $Date$
 */
public class TestAll extends TestCase {

    public TestAll(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite("Tests for org.apache.commons.workflow");
        suite.addTest(BaseActivityTestCase.suite());
        suite.addTest(BaseContextTestCase.suite());
        suite.addTest(BaseExecuteTestCase.suite());
        suite.addTest(CoreBlockTestCase.suite());
        suite.addTest(CoreExecuteTestCase.suite());
        suite.addTest(WebContextTestCase.suite());
        suite.addTest(WebExecuteTestCase.suite());

        // depends on 'ioexecute.url' system property
        // suite.addTest(IOExecuteTestCase.suite());
        return suite;
    }

    public static void main(String[] args) {
        String[] testCaseName = { TestAll.class.getName() };
        junit.textui.TestRunner.main(testCaseName);
    }
}
