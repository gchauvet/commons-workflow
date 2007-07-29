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




/**
 * <p>JavaBean used in the demonstration web application.</p>
 *
 * @version $Revision$ $Date$
 * @author Craig R. McClanahan
 */

public class DemoBean {


    // ------------------------------------------------------------- Properties


    private String firstName = "";

    public String getFirstName() {
        return (this.firstName);
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }


    private String lastName = "";

    public String getLastName() {
        return (this.lastName);
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    private String favoriteCar = "";

    public String getFavoriteCar() {
        return (this.favoriteCar);
    }

    public void setFavoriteCar(String favoriteCar) {
        this.favoriteCar = favoriteCar;
    }


    private String favoriteCity = "";

    public String getFavoriteCity() {
        return (this.favoriteCity);
    }

    public void setFavoriteCity(String favoriteCity) {
        this.favoriteCity = favoriteCity;
    }


    private String favoriteSport = "";

    public String getFavoriteSport() {
        return (this.favoriteSport);
    }

    public void setFavoriteSport(String favoriteSport) {
        this.favoriteSport = favoriteSport;
    }


    private String favoriteTeam = "";

    public String getFavoriteTeam() {
        return (this.favoriteTeam);
    }

    public void setFavoriteTeam(String favoriteTeam) {
        this.favoriteTeam = favoriteTeam;
    }


    public void reset() {
        this.firstName = "";
        this.lastName = "";
        this.favoriteCar = "";
        this.favoriteCity = "";
        this.favoriteSport = "";
        this.favoriteTeam = "";
    }


}
