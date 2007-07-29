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
import org.apache.commons.workflow.BlockState;
import org.apache.commons.workflow.Context;
import org.apache.commons.workflow.Iterator;
import org.apache.commons.workflow.Step;
import org.apache.commons.workflow.StepException;
import org.apache.commons.workflow.base.BaseStep;


/**
 * <p>Locate the closest surrounding Iterator, set the nesting control
 * to <code>false</code>, and transfer control to the Iterator.</p>
 *
 * @version $Revision$ $Date$
 * @author Craig R. McClanahan
 */

public class BreakStep extends BaseStep {


    // ----------------------------------------------------------= Constructors


    /**
     * Construct a default instance of this Step.
     */
    public BreakStep() {

        super();

    }


    /**
     * Construct an instance of this Step with the specified identifier.
     *
     * @param id Step identifier
     */
    public BreakStep(String id) {

        super();
        setId(id);

    }


    // --------------------------------------------------------- Public Methods


    /**
     * Perform the executable actions related to this Step, in the context of
     * the specified Context.
     *
     * @param context The Context that is tracking our execution state
     *
     * @exception StepException if a processing error has occurred
     */
    public void execute(Context context) throws StepException {

        // Locate the closest surrounding Iterator's BlockState
        BlockState state = null;
        while (true) {
            try {
                state = context.peekBlockState();
                if (state.getBlock() instanceof Iterator)
                    break;
                context.popBlockState();
                continue;
            } catch (EmptyStackException e) {
                throw new StepException("Must be nested in an Iterator block",
                                        this);
            }
        }

        // Set the nesting repeat indicator, and transfer control
        state.setNest(false);
        context.setNextStep(state.getBlock());

    }


    /**
     * Render a string representation of this Step.
     */
    public String toString() {

        StringBuffer sb = new StringBuffer("<core:break");
        if (getId() != null) {
            sb.append(" id=\"");
            sb.append(getId());
            sb.append("\"");
        }
        sb.append("/>");
        return (sb.toString());

    }


}
