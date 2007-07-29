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


import java.util.EventObject;


/**
 * A <strong>ScopeEvent</strong> provides notification to a
 * <code>ScopeListener</code> that a specified event has occurred for
 * the specified scope.
 *
 * @version $Revision$ $Date$
 * @author Craig R. McClanahan
 */

public class ScopeEvent extends EventObject {


    // ----------------------------------------------------------- Constructors


    /**
     * Construct a new immutable ScopeEvent.
     *
     * @param scope Scope in which this event occurred
     * @param key Bean key on which this event occurred
     * @param value Bean value on which this event occurred
     */
    public ScopeEvent(Scope scope, String key, Object value) {

        super(scope);
        this.scope = scope;
        this.key = key;
        this.value = value;

    }
        

    // ------------------------------------------------------------- Properties


    /**
     * The bean key upon which this event occurred.
     */
    protected String key = null;

    public String getKey() {

        return (this.key);

    }


    /**
     * The <code>Scope</code> upon which this event occurred.
     */
    protected Scope scope = null;

    public Scope getScope() {

        return (this.scope);

    }


    /**
     * The bean value upon which this event occurred.  For
     * <code>beanReplaced</code> events, this will be the previous value.
     */
    protected Object value = null;

    public Object getValue() {

        return (this.value);

    }


}
