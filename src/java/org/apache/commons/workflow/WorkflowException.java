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

package org.apache.commons.workflow;


/**
 * <p>A <strong>WorkflowException</strong> is the base class for exceptions
 * related to the workflow engine framework.</p>
 *
 * @version $Revision$ $Date$
 * @author Craig R. McClanahan
 */

public class WorkflowException extends Exception {


    // ----------------------------------------------------------- Constructors


    /**
     * Construct an empty WorkflowException.
     */
    public WorkflowException() {

        super();

    }


    /**
     * Construct a WorkflowException with the specified message.
     *
     * @param message Message associated with this exception
     */
    public WorkflowException(String message) {

        super(message);

    }


    /**
     * Construct a WorkflowException with the specified underlying cause.
     * [JDK 1.4 compatible]
     *
     * @param cause Underlying root cause
     */
    public WorkflowException(Throwable cause) {

        super();
        this.cause = cause;

    }


    /**
     * Construct a WorkflowException with the specified message and
     * underlying cause.  [JDK 1.4 compatbile]
     *
     * @param message Message associated with this exception
     * @param cause Underlying root cause
     */
    public WorkflowException(String message, Throwable cause) {

        super(message);
        this.cause = cause;

    }


    // ------------------------------------------------------------- Properties


    /**
     * The underlying cause exception (if any).
     */
    protected Throwable cause = null;

    public Throwable getCause() {
        return (this.cause);
    }


}



