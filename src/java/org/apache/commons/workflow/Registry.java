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
 * <p>A <strong>Registry</strong> is a Singleton that provides registration
 * and lookup facilities for {@link Activity} instances.  All Activity
 * instances registered in a Registry must have unique <code>id</code>
 * properties, but lookup facilities may be used to select zero or more
 * Activities that match particular patterns.  This can be used, for example,
 * to select a Locale-specific version of a particular Activity.</p>
 *
 * <p><strong>FIXME</strong> - Initial version of this interface does not
 * have any of the fancy lookup capabilities allued to above.</p>
 *
 * @author Craig R. McClanahan
 * @version $Revision$ $Date$
 */

public interface Registry {


    // ------------------------------------------------------------- Properties


    // --------------------------------------------------------- Public Methods


    /**
     * Add a new Activity to the set of Activity instances known to this
     * Registry.
     *
     * @param activity The new activity to be added
     */
    public void addActivity(Activity activity);


    /**
     * Clear any existing Activity instances registered with this Registry.
     */
    public void clear();


    /**
     * Return the complete set of Activity instances associated with
     * this Activity.  If there are no such registered Activity instances,
     * a zero-length array is returned.
     */
    public Activity[] findActivities();


    /**
     * Return the registered Activity with the specified identifier, if any;
     * otherwise return <code>null</code>.
     *
     * @param id Identifier of the desired Activity
     */
    public Activity findActivity(String id);


    /**
     * Remove the specified Activity from this Registry.
     *
     * @param activity The Activity to be removed
     */
    public void removeActivity(Activity activity);


}
