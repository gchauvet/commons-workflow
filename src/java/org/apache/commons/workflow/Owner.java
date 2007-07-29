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
 * <p><strong>Owner</strong> represents the common characteristics of Activities
 * and Blocks (that is, Steps that allow nested Steps, such as those that
 * implementat conditionals and iteration).</p>
 *
 * @version $Revision$ $Date$
 * @author Craig R. McClanahan
 */

public interface Owner {


    // ------------------------------------------------------------- Properties


    /**
     * Return the first Step associated with this Activity or Block.
     */
    public Step getFirstStep();


    /**
     * Return the last Step associated with this Activity or Block.
     */
    public Step getLastStep();


    // --------------------------------------------------------- Public Methods


    /**
     * Add a new Step to the end of the sequence of Steps associated with
     * this Activity or Block.
     *
     * @param step The new step to be added
     */
    public void addStep(Step step);


    /**
     * Clear any existing Steps associated with this Activity or Block.
     */
    public void clearSteps();


    /**
     * Return the identified Step from our current Activity or Block,
     * if it exists.  Otherwise, return <code>null</code>.
     *
     * @param id Identifier of the desired Step
     */
    public Step findStep(String id);


    /**
     * Return the set of Steps associated with this Activity or Block.
     */
    public Step[] getSteps();


    /**
     * Set the set of Steps associated with this Activity or Block,
     * replacing any existing ones.
     *
     * @param steps The new set of steps.
     */
    public void setSteps(Step steps[]);


}
