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
 * <p>An <strong>Iterator</strong> is a <code>Block</code> that conditionally
 * repeats the nested Steps associated with the Block more than once (such as
 * a Block that implements "while" or "for" functionality).  This is a tagging
 * interface only - it is useful to help Steps that implement "break" or
 * "continue" type processing to detect whether they have been invoked
 * correctly (nested inside an Iterator) or not.</p>
 *
 * @version $Revision$ $Date$
 * @author Craig R. McClanahan
 */

public interface Iterator extends Block {


    // No extra functionality is defined


}
