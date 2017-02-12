package com.gatech.cs6400.jsf.beans;

import com.gatech.cs6400.jsf.dao.ResourceSearchDAO;
import com.gatech.cs6400.jsf.util.DataConnect;
import com.gatech.cs6400.jsf.util.SessionUtils;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@ManagedBean
@SessionScoped

public class ResourceSearchBean implements Serializable{


    private String keyword = "";
    private String primaryESF= "-1";
    private String location = "";
    private String incident = "-1";
    private String incidentDescription;
    private String description;
    private String owner;
    private String cost;
    private String status;
    private String available;
    private String duration;
    private String Latitude = "0";
    private String Longitude = "0";
    private Long distance;
    private Date date;
    private int id;
    private boolean requestEnabled = false;
    private boolean deployEnabled =false;
    private boolean repairEnabled = false;
    private boolean incidentEnabled = false;
    private ArrayList<SelectItem> ownerIncidentSearch;


    private boolean displayError;
    private String errorMessage;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTargetResource() {
        HttpSession session = SessionUtils.getSession();
        return (String) session.getAttribute("targetResource");
    }

    public String getTargetResourceDescription() {
        HttpSession session = SessionUtils.getSession();
        return (String) session.getAttribute("targetResourceDescription");
    }

    public void setTargetResource(String targetResource) {
        HttpSession session = SessionUtils.getSession();
        session.setAttribute("targetResource", targetResource);
        session.setAttribute("targetResourceDescription", description);
    }

    public boolean isDisplayError() {
        return displayError;
    }

    public void setDisplayError(boolean displayError) {
        this.displayError = displayError;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getIncidentDescription() {
        return incidentDescription;
    }

    public void setIncidentDescription(String incidentDescription) {
        this.incidentDescription = incidentDescription;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isRequestEnabled() { return requestEnabled;  }

    public void setRequestEnabled(boolean requestEnabled) {
        this.requestEnabled = requestEnabled;
    }

    public boolean isDeployEnabled() {
        return deployEnabled;
    }

    public void setDeployEnabled(boolean deployEnabled) {
        this.deployEnabled = deployEnabled;
    }

    public boolean isRepairEnabled() {
        HttpSession session = SessionUtils.getSession();
        String username = (String) session.getAttribute("username");
        Connection con = null;
        try {
            con = DataConnect.getConnection();
            PreparedStatement preparedStatement =
                    con.prepareStatement( "SELECT Status FROM resource WHERE ResourceID = "+id+";");
            ResultSet rs = preparedStatement.executeQuery();

            if(rs.next()&& owner.equals(username)) return (rs.getString("Status").equals("Available") || rs.getString("Status").equals("IN USE"));

        } catch (SQLException ex) {
            System.out.println("Login error -->" + ex.getMessage());
        } finally {
            DataConnect.close(con);
        }
        return false;
    }

    public void setRepairEnabled(boolean repairEnabled) {
       this.repairEnabled = repairEnabled;
    }

    public boolean isIncidentEnabled() {
        HttpSession session = SessionUtils.getSession();
        incidentEnabled = !session.getAttribute("selectIncident").equals("-1");   //changes the flag to false if nothing is selected
        return incidentEnabled;
    }

    public void setIncidentEnabled(boolean incidentEnabled) {
        this.incidentEnabled = incidentEnabled;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getPrimaryESF() {
        return primaryESF;
    }

    public void setPrimaryESF(String primaryESF) {
        this.primaryESF = primaryESF;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getIncident() {
        HttpSession session = SessionUtils.getSession();
        return (String) session.getAttribute("selectIncidentDescription");

    }

    public Long getDistance() {
        return distance;
    }

    public void setDistance(Long distance) {
        this.distance = distance;
    }

    public void setIncident(String incident) {
        this.incident = incident;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<SelectItem> getAllSearchESF(){
        return ResourceSearchDAO.getAllSearchESF();
    }
/*
    public ArrayList<SelectItem> getAllSearchIncident(){
        return ResourceSearchDAO.getAllSearchIncident();
    }
*/
    public List<ResourceSearchBean> getSearchResults(){
        String defaultQuery = "SELECT resourceID, ResourceName, Username, Dollar, Per, Status, Latitude, Longitude, " +
                "(((acos(sin(("+Latitude+"*pi()/180)) * sin((resource.Latitude*pi()/180))+cos(("+Latitude+"*pi()/180)) * cos((resource.Latitude*pi()/180)) * cos((("+Longitude+"- resource.Longitude)*pi()/180))))*180/pi())*60*1.1515*1.609344) as distance" +
                " from resource ";
/*
        SessionUtils.getSession().getAttribute("selectKeyword");
        SessionUtils.getSession().getAttribute("selectPrimaryESF");
        SessionUtils.getSession().getAttribute("selectLocation");
 */
        keyword=(String)SessionUtils.getSession().getAttribute("selectKeyword");
        primaryESF=(String)SessionUtils.getSession().getAttribute("selectPrimaryESF");
        location=(String)SessionUtils.getSession().getAttribute("selectLocation");
        Latitude=(String) SessionUtils.getSession().getAttribute("selectIncidentLat");
        Longitude=(String) SessionUtils.getSession().getAttribute("selectIncidentLon");

        String target = "";
        if ( !keyword.equals("") || !primaryESF.equals("-1") || !location.equals("") || !incident.equals("-1"))
        {
            if (!incident.equals("-1")){
                target += " WHERE ResourceID NOT IN (SELECT ResourceID FROM returnDeploy WHERE IncidentID = "+incident+
                        " AND Returned = 'true') ";
            }
            if (!keyword.equals("")){
                if (!target.equals("")) target += " AND "; else target += " WHERE ";
                target += " (ResourceID IN (SELECT ResourceID FROM capabilities WHERE Capability LIKE '%"+keyword+"%') " +
                        "OR ResourceID IN (SELECT ResourceID FROM resource WHERE ResourceName LIKE '%"+keyword+"%') " +
                        "OR ResourceID IN (SELECT ResourceID FROM resource WHERE Model LIKE '%"+keyword+"%')) ";
            }

            if (!primaryESF.equals("-1")){
                if (!target.equals("")) target += " AND "; else target += " WHERE ";
                target += "( ResourceID IN (SELECT ResourceID FROM resource WHERE PrimaryESF = " +
                        primaryESF + ") " +
                        "OR ResourceID IN (SELECT ResourceID FROM secondaryesf WHERE ESF_ID = " +
                        primaryESF + " ) )";
            }

            //SELECT resource.ResourceID,(((acos(sin((45*pi()/180)) * sin((resource.Latitude*pi()/180))+cos((45*pi()/180)) * cos((resource.Latitude*pi()/180)) * cos(((45- resource.Longitude)*pi()/180))))*180/pi())*60*1.1515*1.609344) as distance
            //FROM resource;


            if (!location.equals("")){
                defaultQuery = "SELECT resourceID, ResourceName, Username, Dollar, Per, Status, Latitude, Longitude, " +
                        "(((acos(sin(("+ SessionUtils.getSession().getAttribute("selectIncidentLat") +"*pi()/180)) * sin((resource.Latitude*pi()/180))+cos(("+SessionUtils.getSession().getAttribute("selectIncidentLat") +"*pi()/180)) * cos((resource.Latitude*pi()/180)) * cos((("+SessionUtils.getSession().getAttribute("selectIncidentLon")+"- resource.Longitude)*pi()/180))))*180/pi())*60*1.1515*1.609344) as distance" +
                        " from resource ";
                target += " having distance < "+ location;
            }


        }
        return ResourceSearchDAO.getTargetSearch(defaultQuery + target);
    }

    public ArrayList<SelectItem> getOwnerIncidentSearch(){
        String user;
        HttpSession session = SessionUtils.getSession();
        user = (String) session.getAttribute("username");
        return ResourceSearchDAO.getOwnerIncidentSearch(user);
    }

    public String getButtonRequestAction() {
        //System.out.println("+++++++++++++++++++++++++++++++++Request triggered:"+id+":"+SessionUtils.getSession().getAttribute("selectIncident"));
        setTargetResource(Integer.toString(id));
        return "searchResourcesAddRequest";
    }
    public String getButtonDeployAction() {
       // System.out.println("+++++++++++++++++++++++++++++++++Deploy triggered:"+id);
        setTargetResource(Integer.toString(id));
        return "searchResourcesDeploy";
    }
    public String getButtonRepairAction() {
       // System.out.println("+++++++++++++++++++++++++++++++++Repair triggered:"+id);
        setTargetResource(Integer.toString(id));
        return "searchResourcesRepair";
    }
    public String getButtonRequestSelect(){
        java.sql.Date targetDate = new java.sql.Date(date.getTime());
        //System.out.println("+++++++++++++++++++++++++++++++++Request Select triggered:"+SessionUtils.getSession().getAttribute("targetResource")+":"+
        //        SessionUtils.getSession().getAttribute("selectIncident"));
        ResourceSearchDAO.insertRequest((String) SessionUtils.getSession().getAttribute("targetResource"),
                (String) SessionUtils.getSession().getAttribute("selectIncident"),
                targetDate);
        return "emrsMenu";
    }
    public String getButtonDeploySelect(){
        java.util.Date today = new java.util.Date();
        java.sql.Date targetDate = new java.sql.Date(date.getTime());
        //System.out.println("+++++++++++++++++++++++++++++++++Deploy Select triggered:"+SessionUtils.getSession().getAttribute("targetResource")+":"+
         //       SessionUtils.getSession().getAttribute("selectIncident"));

        ResourceSearchDAO.updateDeploy((String) SessionUtils.getSession().getAttribute("targetResource"),new java.sql.Date(today.getTime()), targetDate);
        return "emrsMenu";
    }
    public String getButtonRepairSelect(){
    //    System.out.println("+++++++++++++++++++++++++++++++++Repair Select triggered:"+SessionUtils.getSession().getAttribute("targetResource"));

        ResourceSearchDAO.updateRepair((String) SessionUtils.getSession().getAttribute("targetResource"), duration);
        return "emrsMenu";
    }
    public String getButtonResults(){
        //private String location = "";
        //private String incident = "-1";
        if (!location.equals("")&&incident.equals("-1")){
            System.out.println("NOT INCIDENT SELECTED");
            this.errorMessage = "Please Select an Incident to use the distance option";
            displayError = true;
            return "searchResources";
        }else displayError = false;
        SessionUtils.getSession().setAttribute("selectKeyword", keyword);
        SessionUtils.getSession().setAttribute("selectPrimaryESF", primaryESF);
        SessionUtils.getSession().setAttribute("selectLocation", location);
        SessionUtils.getSession().setAttribute("selectIncident", incident);
        SessionUtils.getSession().setAttribute("selectIncidentDescription", ResourceSearchDAO.getIncidentDescription(incident));
    //    System.out.println("+++++++++++++++++++++++++++++++++Repair Select triggered:"+SessionUtils.getSession().getAttribute("selectIncident"));
    //    System.out.println("+++++++++++++++++++++++++++++++++Repair Select triggered:"+SessionUtils.getSession().getAttribute("selectIncident"));
        return "searchResourcesResults";
    }

}
