package com.gatech.cs6400.jsf.beans;

import com.gatech.cs6400.jsf.dao.IncidentDAO;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import java.io.Serializable;
import java.util.Date;


@ManagedBean
@RequestScoped
public class Incident implements Serializable {

    private int id;
    private String description;
    private Date date;
    private String username;
    private String latitude;
    private String longitude;
    private boolean displayError;
    private String errorMessage;

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public boolean isDisplayError() {
        return displayError;
    }

    public void setDisplayError(boolean displayError) {
        this.displayError = displayError;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /*
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    */

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public Date getDate() {
        return this.date;
    }
    public void setDate(Date date) {
        this.date = date;
    }

    public String saveIncident(){
        //System.out.println("Save Incident...");
        if (this.date == null ||
                this.description.equals("") ||
                this.longitude.equals("") ||
                this.latitude.equals("")
/*                || Long.parseLong(this.latitude) > 90 ||
                Long.parseLong(this.latitude) < -90 ||
                Long.parseLong(this.longitude) > 180 ||
                Long.parseLong(this.longitude) < -180
*/)        {
            errorMessage = "You are missing a required value or your longitude/latitude exceeds allowed bounds.";
            displayError = true;
            return "addEmergencyIncident";
        } else {
            errorMessage = "";
            displayError = false;
            this.id = IncidentDAO.insertResource(this.description, this.date, this.latitude, this.longitude);
            return "emrsMenu";
        }
    }

}
