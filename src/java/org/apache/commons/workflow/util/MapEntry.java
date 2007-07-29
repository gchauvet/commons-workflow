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

package org.apache.commons.workflow.util;


import java.util.Map;


/**
 * General purpose implementation of the <strong>Map.Entry</strong>
 * interface for use in returning results from <code>entrySet()</code>
 * methods on <code>Map</code> implementations.
 *
 * @version $Revision$ $Date$
 * @author Craig R. McClanahan
 */

public class MapEntry implements Map.Entry {


    // ----------------------------------------------------------- Constructors


    /**
     * Construct a new MapEntry based on the specified parameters.
     *
     * @param key Key for this entry
     * @param value Value for this entry
     */
    public MapEntry(Object key, Object value) {

        super();
        setKey(key);
        setValue(value);

    }



    // ------------------------------------------------------------- Properties


    /**
     * The key for this entry.
     */
    protected Object key = null;

    public Object getKey() {
        return (this.key);
    }

    public void setKey(Object key) {
        this.key = key;
    }


    /**
     * The value for for this entry.
     */
    protected Object value = null;

    public Object getValue() {
        return (this.value);
    }

    public Object setValue(Object value) {
        Object old = this.value;
        this.value = value;
        return (old);
    }


}
