package com.gatech.cs6400.jsf.beans;

import com.gatech.cs6400.jsf.dao.ResourceDAO;

import javax.annotation.PostConstruct;
import javax.faces.bean.*;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@ManagedBean
@RequestScoped
public class ResourceBean implements Serializable {

    private String primaryESF="1";
    private String[]additionalESF;
    private String resourceName;
    private String model;
    private String latitude;
    private String longitude;
    private String cost;
    private String unit;
    private List<String> capabilities = new ArrayList<String>();
    private List<SelectItem> secondaryESFList;
    private String newCapability;
    private int id;
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

    private boolean displayError;

    @PostConstruct
    public void init(){
        //System.out.println("Resource Bean Created:");
        //excludes the first element in the list
        this.secondaryESFList = ResourceDAO.getSecondaryESFList(primaryESF);
    }

    public String[] getAdditionalESF() {return additionalESF;}

    public void setAdditionalESF(String[] additionalESF) {this.additionalESF=additionalESF;}

    public String getPrimaryESF() {
        return primaryESF;
    }

    public void setPrimaryESF(String primaryESF) {
        this.primaryESF = primaryESF;
    }

    public ArrayList<SelectItem> getAllESF() {

        ArrayList<SelectItem> allESF = ResourceDAO.getAllESF();

        return allESF;
    }

    public ArrayList<SelectItem> getAllUnit() {

        ArrayList<SelectItem> allUnit = ResourceDAO.getAllUnit();

        return allUnit;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

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

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public List<String> getCapabilities(){
        return this.capabilities;
    }

    public void setCapabilities(List<String> capabilities){
        this.capabilities = capabilities;
    }

    public void addCapability(){
        //System.out.println("NEW CAPABILITY:" + newCapability);
        this.capabilities.add(newCapability);

    }

    public String getNewCapability() {
        return newCapability;
    }

    public void setNewCapability(String newCapability) {
        this.newCapability = newCapability;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int saveResource(){
        //System.out.println("Save Resource...");
        if(this.resourceName.equals("") ||
                this.cost.equals("") ||
                this.longitude.equals("") ||
                this.latitude.equals("")
               /* ||
                Integer.parseInt(this.latitude) > 90 ||
                Integer.parseInt(this.latitude) < -90 ||
                Integer.parseInt(this.longitude) > 180 ||
                Integer.parseInt(this.longitude) < -180
                */
                ) {
            errorMessage = "You are missing a required value or your longitude/latitude exceeds allowed bounds.";
            displayError = true;
            return 0;
        } else {
            errorMessage = "";
            displayError = false;
            this.id = ResourceDAO.insertResource(this.resourceName, this.model, this.latitude, this.longitude,
                    this.primaryESF,
                    this.cost,
                    this.unit);

            //For every additional ESF selected adding to secondaryesf Table
            for (String i : this.additionalESF) {
                //System.out.println("INSERTING ADDITIONAL ESF:"+i + " WITH RESOURCE ID " + this.id);
                //ResourceDAO.insertSecondaryESF(Integer.parseInt(i),this.id);
                ResourceDAO.insertTarget("secondaryesf", "ESF_ID", i, this.id);
            }

            for (String entry : this.capabilities) {
                ResourceDAO.insertTarget("capabilities", "Capability", entry, this.id);
                //System.out.println("Key = " + this.id + ", Value = " + entry);
            }

            return this.id;
        }
    }

    public List<SelectItem> getSecondaryESFList() {
        return secondaryESFList;
    }

    public void setSecondaryESFList(List<SelectItem> secondaryESF) {
        this.secondaryESFList = secondaryESF;
    }

    public void primaryESFChange(ValueChangeEvent event) {
        Object value = event.getNewValue();
        if (value != null) {
            setSecondaryESFList(ResourceDAO.getSecondaryESFList(value.toString()));
            return;
        }
    }

}
