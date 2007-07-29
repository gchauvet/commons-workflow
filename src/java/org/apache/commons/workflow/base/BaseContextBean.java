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

package org.apache.commons.workflow.base;


import java.util.HashMap;
import java.util.Map;
import org.apache.commons.workflow.Context;


/**
 * Custom JavaBean representing the properties of a <code>BaseContext</code>
 * that are made visible via JPath.
 */

public class BaseContextBean {

    private BaseContext baseContext = null;

    private HashMap scopes = null;

    public BaseContextBean(BaseContext baseContext) {
        super();
        this.baseContext = baseContext;
    }

    public Map getLocal() {
        return (baseContext.getScope(Context.LOCAL_SCOPE));
    }

}
