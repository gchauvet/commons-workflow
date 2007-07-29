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

package org.apache.commons.workflow.web;


import java.util.Enumeration;
import java.util.HashMap;
import javax.servlet.ServletRequest;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.workflow.Context;
import org.apache.commons.workflow.Descriptor;
import org.apache.commons.workflow.StepException;
import org.apache.commons.workflow.base.DescriptorStep;


/**
 * <p>For each associated <code>Descriptor</code>, populate the properties
 * of the bean specified by that descriptor from the request parameters of
 * the current request.</p>
 *
 * @version $Revision$ $Date$
 * @author Craig R. McClanahan
 */

public class PopulateStep extends DescriptorStep {


    // ----------------------------------------------------------= Constructors


    /**
     * Construct a default instance of this Step.
     */
    public PopulateStep() {

        super();

    }


    /**
     * Construct an instance of this Step with the specified identifier.
     *
     * @param id Step identifier
     */
    public PopulateStep(String id) {

        super();
        setId(id);

    }


    /**
     * Construct an instance of this Step with the specified identifier
     * and associated Descriptor.
     *
     * @param id Step identifier
     * @param descriptor Initial descriptor
     */
    public PopulateStep(String id, Descriptor descriptor) {

        super();
        setId(id);
        addDescriptor(descriptor);

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

        // Make sure our executing Context is a WebContext
        if (!(context instanceof WebContext))
            throw new StepException("Execution context is not a WebContext",
                                    this);
        WebContext webContext = (WebContext) context;
        ServletRequest request = webContext.getServletRequest();

        // Prepare a Map of our request parameter names and values
        // (in Servlet 2.3 we would just call request.getParameterMap())
        HashMap map = new HashMap();
        Enumeration names = request.getParameterNames();
        while (names.hasMoreElements()) {
            String name = (String) names.nextElement();
            map.put(name, request.getParameterValues(name));
        }

        // Process all associated descriptors
        Descriptor descriptors[] = findDescriptors();
        for (int i = 0; i < descriptors.length; i++) {
            Object value = descriptors[i].get(context);
            if (value == null)
                throw new StepException
                    ("Cannot retrieve object for " + descriptors[i], this);
            try {
                BeanUtils.populate(value, map);
            } catch (Throwable t) {
                throw new StepException("Populate exception", t, this);
            }
        }

    }


    /**
     * Render a string representation of this Step.
     */
    public String toString() {

        StringBuffer sb = new StringBuffer("<web:populate");
        if (getId() != null) {
            sb.append(" id=\"");
            sb.append(getId());
            sb.append("\"");
        }
        sb.append(">");
        Descriptor descriptors[] = findDescriptors();
        for (int i = 0; i < descriptors.length; i++)
            sb.append(descriptors[i].toString());
        sb.append("</web:populate>");
        return (sb.toString());

    }


}
