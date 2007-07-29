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
 * <p><strong>BlockState</strong> represents the current dynamic execution
 * state of a <code>Block</code> that is executing nested <code>Steps</code>.
 * This class will serve for most <code>Block</code> implementations, but
 * may be subclassed for <code>Blocks</code> with a requirement to maintain
 * additional state information (such as a "for" loop that needs to keep
 * track of the starting and ending indexes, and the iteration counter).</p>
 *
 * @author Craig R. McClanahan
 * @version $Revision$ $Date$
 */

public class BlockState {


    // ----------------------------------------------------------- Constructors


    /**
     * Construct a new <code>BlockState</code> instance, associated with the
     * specified <code>block</code>, and with the specified initial value
     * for the <code>nest</code> property.
     *
     * @param block The <code>block</code> whose state this object represents
     * @param nest The initial state of the <code>nest</code> property
     */
    public BlockState(Block block, boolean nest) {

        super();
        this.block = block;
        this.nest = nest;

    }


    // ------------------------------------------------------------- Properties


    /**
     * The <code>Block</code> whose state is represented by this object.
     */
    protected Block block = null;

    public Block getBlock() {
        return (this.block);
    }


    /**
     * Should we execute the nested <code>Steps</code> of this
     * <code>Block</code> again when the current execution finishes?
     */
    protected boolean nest = false;

    public boolean getNest() {
        return (this.nest);
    }

    public void setNest(boolean nest) {
        this.nest = nest;
    }


    // --------------------------------------------------------- Public Methods


    /**
     * Render a String version of this object.
     */
    public String toString() {

        StringBuffer sb = new StringBuffer("BlockState[block=");
        sb.append(block);
        sb.append(", nest=");
        sb.append(nest);
        sb.append("]");
        return (sb.toString());

    }

}
