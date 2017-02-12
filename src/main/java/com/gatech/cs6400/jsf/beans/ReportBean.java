package com.gatech.cs6400.jsf.beans;

import com.gatech.cs6400.jsf.dao.ReportDAO;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.List;

@ManagedBean
@ViewScoped

public class ReportBean implements Serializable  {

    private String esfID;
    private String esfDescription;
    private String totalResources;
    private String resourcesInUse;

    @PostConstruct
    public void init(){

    }

    public String getEsfID() { return esfID; }
    public void setEsfID(String esfID) { this.esfID = esfID;}

    public String getEsfDescription() { return esfDescription; }
    public void setEsfDescription(String esfDescription) { this.esfDescription = esfDescription; }

    public String getTotalResources() { return totalResources; }
    public void setTotalResources(String totalResources) { this.totalResources = totalResources; }

    public String getResourcesInUse() { return resourcesInUse; }
    public void setResourcesInUse(String resourcesInUse) { this.resourcesInUse = resourcesInUse; }

    public List<ReportBean> getAllESF() {
        List<ReportBean> allESF = ReportDAO.getAllESF();
        return allESF;
    }
}
