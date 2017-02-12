package com.gatech.cs6400.jsf.beans;

import com.gatech.cs6400.jsf.dao.ReleaseRepairBeanDAO;
import com.gatech.cs6400.jsf.dao.ReportDAO;
import com.gatech.cs6400.jsf.dao.ResourceStatusDAO;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@ManagedBean
@ViewScoped

public class ReleaseRepairBean implements Serializable  {

    private int resourceID;
    private String resourceDescription;
    private String startDate;
    private String endDate;

    @PostConstruct
    public void init(){

    }


    public List<ReportBean> getAllESF() {
        List<ReportBean> allESF = ReportDAO.getAllESF();
        return allESF;
    }

    public int getResourceID() {
        return resourceID;
    }

    public void setResourceID(int resourceID) {
        this.resourceID = resourceID;
    }

    public String getResourceDescription() {
        return resourceDescription;
    }

    public void setResourceDescription(String resourceDescription) {
        this.resourceDescription = resourceDescription;
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

    public List<ReleaseRepairBean> getInRepair() {
        return ReleaseRepairBeanDAO.getRepairsScheduled();
    }

    public boolean getPastDue(){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return df.parse(endDate).after(new java.util.Date());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return true;
    }

    public void returnResource(){
        //   System.out.println("cancelRepair:"+id+":"+status);
        ReleaseRepairBeanDAO.updateTable("repair", Integer.toString(resourceID));
    }
}
