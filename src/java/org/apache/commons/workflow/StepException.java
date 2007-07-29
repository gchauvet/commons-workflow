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
 * <p>A <strong>StepException</strong> is used to report problems encountered
 * during the dynamic execution of a specific <code>Step</code>.
 *
 * @version $Revision$ $Date$
 * @author Craig R. McClanahan
 */

public class StepException extends WorkflowException {


    // ----------------------------------------------------------- Constructors


    /**
     * Construct an empty StepException.
     */
    public StepException() {

        super();

    }


    /**
     * Construct an empty StepException occurring with the specified Step.
     *
     * @param step Step that caused this StepException
     */
    public StepException(Step step) {

        super();
        this.step = step;

    }


    /**
     * Construct a StepException with the specified message.
     *
     * @param message Message associated with this exception
     */
    public StepException(String message) {

        super(message);

    }


    /**
     * Construct a StepException with the specified message occurring with
     * the specified Step.
     *
     * @param message Message associated with this exception
     * @param step Step that caused this StepException
     */
    public StepException(String message, Step step) {

        super(message);
        this.step = step;

    }


    /**
     * Construct a StepException with the specified underlying cause.
     * [JDK 1.4 compatible]
     *
     * @param cause Underlying root cause
     */
    public StepException(Throwable cause) {

        super();
        this.cause = cause;

    }


    /**
     * Construct a StepException with the specified underlying cause,
     * occurring in the specified Step.
     * [JDK 1.4 compatible]
     *
     * @param cause Underlying root cause
     * @param step Step that caused this StepException
     */
    public StepException(Throwable cause, Step step) {

        super();
        this.cause = cause;
        this.step = step;

    }


    /**
     * Construct a StepException with the specified message and
     * underlying cause.  [JDK 1.4 compatbile]
     *
     * @param message Message associated with this exception
     * @param cause Underlying root cause
     */
    public StepException(String message, Throwable cause) {

        super(message);
        this.cause = cause;

    }


    /**
     * Construct a StepException with the specified message and
     * underlying cause, occurring in the specified Step.  [JDK 1.4 compatbile]
     *
     * @param message Message associated with this exception
     * @param cause Underlying root cause
     * @param step Step that caused this StepException
     */
    public StepException(String message, Throwable cause, Step step) {

        super(message);
        this.cause = cause;
        this.step = step;

    }


    // ------------------------------------------------------------- Properties


    /**
     * The Step that was being executed when the problem occurred.
     */
    protected Step step = null;

    public Step getStep() {
        return (this.step);
    }


    // --------------------------------------------------------- Public Methods


    /**
     * Render a printable version of this exception.
     */
    public String toString() {

        StringBuffer sb = new StringBuffer("StepException[");
        sb.append("message=");
        sb.append(getMessage());
        if (step != null) {
            sb.append(", step=");
            sb.append(step);
        }
        if (cause != null) {
            sb.append(", cause=");
            sb.append(cause);
        }
        sb.append("]");
        return (sb.toString());

    }


}
