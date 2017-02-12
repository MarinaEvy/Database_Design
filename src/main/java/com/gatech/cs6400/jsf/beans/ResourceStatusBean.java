package com.gatech.cs6400.jsf.beans;

import com.gatech.cs6400.jsf.dao.ResourceStatusDAO;
import com.gatech.cs6400.jsf.util.SessionUtils;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@ManagedBean
@ViewScoped
public class ResourceStatusBean implements Serializable {


    private int id;
    private String resourceName;
    private String incident;
    private String owner;
    private String requester;
    private String startDate;
    private String endDate;
    private String status;
    private String incidentID;
    private Date returnDate;
    private boolean pastDue;
    public String getIncidentID() {
        return incidentID;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public void setIncidentID(String incidentID) {
        this.incidentID = incidentID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getIncident() {
        return incident;
    }

    public void setIncident(String incident) {
        this.incident = incident;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getRequester() {
        return requester;
    }

    public void setRequester(String requester) {
        this.requester = requester;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public List<ResourceStatusBean> getResourceInUse() {
       return ResourceStatusDAO.getResourceInUse();
    }

    public List<ResourceStatusBean> getResourceRequestedByMe() {
        List<ResourceStatusBean> allESF = ResourceStatusDAO.getResourceRequestedByMe();
        return allESF;
    }

    public List<ResourceStatusBean> getResourcesRequestedForMe() {
        List<ResourceStatusBean> allESF = ResourceStatusDAO.getResourcesRequestedForMe();
        return allESF;
    }

    public List<ResourceStatusBean> getRepairsScheduled() {
        List<ResourceStatusBean> allESF = ResourceStatusDAO.getRepairsScheduled();
        return allESF;
    }

    public void returnResource(){
    //    System.out.println("returnResource:"+id+":"+incidentID+":"+status);
        ResourceStatusDAO.updateTable("returndeploy", Integer.toString(id), incidentID, status, null);
        HttpSession session = SessionUtils.getSession();
        startDate = session.getAttribute("newStartDate").toString();
        endDate = session.getAttribute("newEndDate").toString();
    }
    public void cancelRequest(){
     //   System.out.println("cancelRequest:"+id+":"+incidentID+":"+status);
        ResourceStatusDAO.updateTable("request", Integer.toString(id), incidentID, status, null);
    }
    public void deployRequest(){
     //   System.out.println("deployRequest:"+id+":"+incidentID);
        ResourceStatusDAO.updateTable("deploy", Integer.toString(id), incidentID, status, returnDate);
    }
    public void rejectRequest(){
     //   System.out.println("rejectRequest:"+id+":"+incidentID);
        ResourceStatusDAO.updateTable("request", Integer.toString(id), incidentID, null, null);
    }
    public void cancelRepair(){
     //   System.out.println("cancelRepair:"+id+":"+status);
        ResourceStatusDAO.updateTable("repair", Integer.toString(id), incidentID, status, null);
    }

    public boolean getAlreadyDeployed(){
        return !ResourceStatusDAO.alreadyDeployed(Integer.toString(id));
    }

    public boolean isInRepair(){
        return !this.status.equals("IN REPAIR");
    }

    public boolean getPastDue(){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            //System.out.println(id);
            //System.out.println(df.parse(endDate).after(new java.util.Date()));
            //returns true if the end date is after todays date
            return df.parse(endDate).after(new java.util.Date());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return true;
    }
}
