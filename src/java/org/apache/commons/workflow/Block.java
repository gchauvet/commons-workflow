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
 * <p>A <strong>Block</strong> is a Step that can include nested Steps.
 * It is used to create Step implementations supporting conditional
 * execution and iteration.</p>
 *
 * <p><strong>DESIGN NOTES</strong> - The <code>execute()</code> method will
 * be called when the Block is first encountered in the normal flow of
 * execution (as with any other Step), and each time the execution of the
 * nested Steps associated with this Block.  The Block implementation is
 * responsible for satisfying the following contract requirements:</p>
 * <ul>
 * <li>Determine whether this is a first-time entry, or a repeated entry
 *     after execution of the nested Steps for this Block:
 *     <ul>
 *     <li>Peek at the top item on the <code>BlockState</code> stack maintained
 *         for us by the current <code>Context</code>.</li>
 *     <li>If the top item exists, and has a <code>block</code> property
 *         equal to the current Block, this is a repeated entry after execution
 *         of the nested Steps for this Block.</li>
 *     <li>If there is no top element, or the top element is for a different
 *         <code>Block</code>, this is the initial entry from the preceeding
 *         Step in the current Activity (or Block).</li>
 *     </ul>
 * <li>When this is the initial entry into the Block, make a decision about
 *     whether the nested Steps should be executed:
 *     <ul>
 *     <li>If the nested Steps <strong>SHOULD</strong> be executed, create a
 *         new <code>BlockState</code> object (associated with this
 *         <code>Block</code>) and push it onto the <code>BlockState</code>
 *         stack.  Then, set the next Step to be the first nested Step
 *         for this Block.</li>
 *     <li>If the nested Steps <strong>SHOULD NOT</strong> be executed,
 *         simply return.  The next Step will have already been set to the
 *         next Step of the current Activity (or Block) at the same nesting
 *         level.</li>
 *     </ul></li>
 *  <li>When this is a repeated entry into the Block, make a decision about
 *      whether the nested Steps should be executed again:
 *      <ul>
 *      <li>If the nested Steps <strong>SHOULD</strong> be executed again,
 *          set the next Step to be the first nested Step for this Block.</li>
 *      <li>If the nested Steps <strong>SHOULD NOT</strong> be executed again,
 *          pop our <code>BlockState</code> object off of the stack
 *          maintained by our <code>Context</code>.  Then, set the next Step
 *          to the <code>nextStep</code> property of this Block, which will
 *          cause execution to flow past the Block.</li>
 *      </ul>
 * </ul>
 *
 * @version $Revision$ $Date$
 * @author Craig R. McClanahan
 */

public interface Block extends Owner, Step {


    // --------------------------------------------------------- Public Methods


}
