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
import org.apache.commons.workflow.Activity;
import org.apache.commons.workflow.Registry;


/**
 * <p><strong>BaseRegistry</strong> is a convenient base class for more
 * sophisticated <code>Registry</code> implementations.</p>
 *
 * @version $Revision$ $Date$
 * @author Craig R. McClanahan
 */

public class BaseRegistry implements Registry {


    // ----------------------------------------------------- Instance Variables


    /**
     * The set of Activity instances registered with this Registry, keyed
     * by identifier.
     */
    protected HashMap activities = new HashMap();


    // ------------------------------------------------------------- Properties


    // --------------------------------------------------------- Public Methods


    /**
     * Add a new Activity to the set of Activity instances known to this
     * Registry.
     *
     * @param activity The new activity to be added
     */
    public void addActivity(Activity activity) {

        activities.put(activity.getId(), activity);

    }


    /**
     * Clear any existing Activity instances registered with this Registry.
     */
    public void clear() {

        activities.clear();

    }


    /**
     * Return the complete set of Activity instances associated with
     * this Activity.  If there are no such registered Activity instances,
     * a zero-length array is returned.
     */
    public Activity[] findActivities() {

        Activity results[] = new Activity[activities.size()];
        return ((Activity[]) activities.values().toArray(results));

    }


    /**
     * Return the registered Activity with the specified identifier, if any;
     * otherwise return <code>null</code>.
     *
     * @param id Identifier of the desired Activity
     */
    public Activity findActivity(String id) {

        return ((Activity) activities.get(id));

    }


    /**
     * Remove the specified Activity from this Registry.
     *
     * @param activity The Activity to be removed
     */
    public void removeActivity(Activity activity) {

        activities.remove(activity.getId());

    }


    // ------------------------------------------------------- Static Variables


    /**
     * The singleton registry instance.
     */
    protected static Registry registry = null;


    // --------------------------------------------------------- Static Methods


    /**
     * Factory method to return a Singleton (per class loader)
     * <code>Registry</code> instance.
     */
    public static Registry getRegistry() {

        if (registry == null)
            registry = new BaseRegistry();
        return (registry);

    }


}
