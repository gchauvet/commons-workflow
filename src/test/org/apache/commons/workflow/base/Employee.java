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
import org.apache.commons.workflow.Scope;

/**
 * Bean for JPath testing.
 */

public class Employee {

    public Employee() {
        this("My First Name", "My Last Name");
    }

    public Employee(String firstName, String lastName) {
        super();
        setFirstName(firstName);
        setLastName(lastName);
    }

    private HashMap addresses = new HashMap();
    /*
    private Scope addresses = new BaseScope();
    */
    public void addAddress(String key, Address address) {
        addresses.put(key, address);
    }
    public HashMap getAddresses() {
        return ((HashMap) addresses);
    }
    public void removeAddress(String key) {
        addresses.remove(key);
    }

    private Address baseAddress = null;
    public Address getBaseAddress() {
        return (this.baseAddress);
    }
    public void setBaseAddress(Address baseAddress) {
        this.baseAddress = baseAddress;
    }

    private String firstName = null;
    public String getFirstName() {
        return (this.firstName);
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    private String lastName = null;
    public String getLastName() {
        return (this.lastName);
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer("Employee[");
        sb.append("firstName=");
        sb.append(firstName);
        sb.append(", lastName=");
        sb.append(lastName);
        sb.append("]");
        return (sb.toString());
    }

}
