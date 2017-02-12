package com.gatech.cs6400.jsf.dao;


import com.gatech.cs6400.jsf.beans.ResourceSearchBean;
import com.gatech.cs6400.jsf.util.DataConnect;
import com.gatech.cs6400.jsf.util.SessionUtils;

import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ResourceSearchDAO {

    public static ArrayList<SelectItem> allSearchESF;
    static{

        allSearchESF = new ArrayList<SelectItem>();
        Connection con = null;
        try {
            con = DataConnect.getConnection();
            PreparedStatement preparedStatement =
                    con.prepareStatement("SELECT * FROM esf");
            ResultSet rs = preparedStatement.executeQuery();

            //adds empty value for none selected
            allSearchESF.add( new SelectItem("-1",""));


            while (rs.next()) {
                allSearchESF.add( new SelectItem(Integer.toString(rs.getInt(1)),
                        Integer.toString(rs.getInt(1)) + ":" + rs.getString(2)));
            }

        } catch (SQLException ex) {
            System.out.println("Login error -->" + ex.getMessage());
        } finally {
            DataConnect.close(con);
        }
    }
    public static ArrayList<SelectItem> getAllSearchESF()
    {
        return allSearchESF;
    }

    public static String getIncidentDescription (String target){
        String temp = null;
        Connection con = null;
        try {
            con = DataConnect.getConnection();
            PreparedStatement preparedStatement =
                    con.prepareStatement("SELECT Description, Latitude, Longitude  FROM incident WHERE IncidentID = ?;");
            preparedStatement.setString(1, target);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                temp = rs.getString(1);
                SessionUtils.getSession().setAttribute("selectIncidentLat", rs.getString(2));
                //System.out.println("Lat:"+rs.getString(2));
                SessionUtils.getSession().setAttribute("selectIncidentLon", rs.getString(3));
                //System.out.println("Lon:"+rs.getString(3));
            }

            return temp;

        } catch (SQLException ex) {
            System.out.println("Login error -->" + ex.getMessage());
        } finally {
            DataConnect.close(con);
        }

        return null;
    }

    public static List<ResourceSearchBean> getTargetSearch (String target){
        //System.out.println("TARGET:"+target);

        List<ResourceSearchBean> list = null;

        Connection con = null;
        try {
            con = DataConnect.getConnection();
            PreparedStatement preparedStatement =
                    con.prepareStatement( target +";");
            //System.out.println("SUBMITTED QUERY:"+ target +";");
            ResultSet rs = preparedStatement.executeQuery();

            list = new ArrayList<ResourceSearchBean>();

            while (rs.next()) {

                ResourceSearchBean resourceSearchBean = new ResourceSearchBean();
                resourceSearchBean.setId(rs.getInt("resourceID"));
                resourceSearchBean.setOwner(rs.getString("Username"));
                resourceSearchBean.setDescription(rs.getString("ResourceName"));
                resourceSearchBean.setCost("$" + rs.getString("Dollar")
                                            + " per " + rs.getString("Per"));
                resourceSearchBean.setStatus(rs.getString("Status"));
                resourceSearchBean.setLatitude(rs.getString("Latitude"));
                resourceSearchBean.setLongitude(rs.getString("Longitude"));
                resourceSearchBean.setDistance(rs.getLong("distance"));

                if (rs.getString("Status").equals("Available")){
                    resourceSearchBean.setAvailable(getNextAvailable(rs.getInt("resourceID")));
                    HttpSession session = SessionUtils.getSession();
                    String username = (String) session.getAttribute("username");
                    if(rs.getString("Username").equals(username)){
                        resourceSearchBean.setDeployEnabled(!rs.getString("Status").equals("IN USE") &&
                                !rs.getString("Status").equals("IN REPAIR"));
                        resourceSearchBean.setRepairEnabled(!rs.getString("Status").equals("IN REPAIR")&&
                                (rs.getString("Status").equals("IN USE") ||
                                rs.getString("Status").equals("Available")));
                        resourceSearchBean.setRequestEnabled(false);
                    } else {
                        resourceSearchBean.setDeployEnabled(false);
                        resourceSearchBean.setRepairEnabled(false);
                        resourceSearchBean.setRequestEnabled(true);
                    }

                }else {
                    resourceSearchBean.setAvailable(getNextAvailable(rs.getInt("resourceID")));
                }

/*
                if (rs.getString("Status").equals("IN REPAIR") || rs.getString("Status").equals("IN USE")){
                    resourceSearchBean.setDeployEnabled(false);
                    resourceSearchBean.setRepairEnabled(false);
                    resourceSearchBean.setRequestEnabled(false);
                }
*/

                list.add(resourceSearchBean);

            }

        } catch (SQLException ex) {
            System.out.println("Login error -->" + ex.getMessage());
        } finally {
            DataConnect.close(con);
        }

        return list;
    }

    public static  ArrayList<SelectItem> getOwnerIncidentSearch (String user){

        ArrayList<SelectItem> ownerIncidentSearch = new ArrayList<SelectItem>();
        Connection con = null;
        try {

            con = DataConnect.getConnection();
            PreparedStatement preparedStatement =
                    con.prepareStatement("SELECT IncidentID, Description from incident WHERE Username =\""
                            + user +"\";");
            ResultSet rs = preparedStatement.executeQuery();


            ownerIncidentSearch.add( new SelectItem("-1",""));

            while (rs.next()) {
                ownerIncidentSearch.add( new SelectItem(Integer.toString(rs.getInt(1)),rs.getString(2)));
            }

        } catch (SQLException ex) {
            System.out.println("Login error -->" + ex.getMessage());
        } finally {
            DataConnect.close(con);
        }
        return ownerIncidentSearch;
    }

    public static void insertRequest(String ResourceID, String IncidentID, Date ReturnDate){
        Connection con = null;
        try {
            con = DataConnect.getConnection();
            PreparedStatement preparedStatement =
                    con.prepareStatement("INSERT INTO request (ResourceID, IncidentID, ReturnDate ) VALUES (?,?,?)",
                            Statement.RETURN_GENERATED_KEYS);
            // Retrieves username from Session
            preparedStatement.setString(1, ResourceID);
            preparedStatement.setString(2, IncidentID);
            preparedStatement.setDate(3, ReturnDate);
            preparedStatement.executeUpdate();
            return;

        } catch (SQLException ex) {
            System.out.println("Login error -->" + ex.getMessage());
            return;
        } finally {
            DataConnect.close(con);
        }
    }

    public static void updateDeploy(String ResourceID, Date date, Date targetDate) {
        Connection con = null;
        try {
            con = DataConnect.getConnection();
            PreparedStatement preparedStatement =
                    con.prepareStatement("INSERT INTO returndeploy (ResourceID, IncidentID, DeployDate, ReturnDate ) VALUES (?,?,?,?)");

            preparedStatement.setString(1, ResourceID);
            preparedStatement.setString(2, (String) SessionUtils.getSession().getAttribute("selectIncident"));
            preparedStatement.setDate(3, date);
            preparedStatement.setDate(4, targetDate);
            preparedStatement.executeUpdate();

            preparedStatement = con.prepareStatement("UPDATE resource " +
                    "SET Status=\"IN USE\" WHERE ResourceID=?");
            preparedStatement.setInt(1, Integer.parseInt(ResourceID));
            preparedStatement.executeUpdate();
            return;

        } catch (SQLException ex) {
            System.out.println("Login error -->" + ex.getMessage());
            return;
        } finally {
            DataConnect.close(con);
        }
    }

    private static String getNextAvailable(int ResourceID){

        Connection con = null;
        try {

            Date availableDate = null;
            Date date = new Date(System.currentTimeMillis()-24*60*60*1000);

            con = DataConnect.getConnection();
            PreparedStatement preparedStatement =
                    con.prepareStatement("SELECT ReturnDate FROM returndeploy WHERE ResourceID = " + ResourceID +";");
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                if (rs.getDate("ReturnDate").getTime()> date.getTime()){
                    //System.out.println("STILL DEPLOYED:"+ResourceID);
                    availableDate = rs.getDate("ReturnDate");
                }
            }

            preparedStatement =
                    con.prepareStatement("SELECT Duration, StartDate FROM repair WHERE ResourceID = " + ResourceID +";");
            rs = preparedStatement.executeQuery();
            preparedStatement =
                    con.prepareStatement("SELECT Status FROM resource WHERE ResourceID = " + ResourceID +";");
            ResultSet rs0 = preparedStatement.executeQuery();
            if (rs0.next()) if (rs0.getString("Status").equals("Available"))  return "NOW";
            while (rs.next()) {
                if (availableDate == null) {
                    availableDate = new java.sql.Date(rs.getDate("StartDate").getTime());
                }
                if (rs.getDate("StartDate").getTime()> date.getTime()){
                    //System.out.println("STILL REPAIRED:"+ResourceID);
                    availableDate = new Date(availableDate.getTime()+ rs.getLong("Duration")*24*60*60*1000);
                    //System.out.println(availableDate);
                }
            }
            if (availableDate == null) return "NOW";
            return availableDate.toString();

        } catch (SQLException ex) {
            System.out.println("Login error -->" + ex.getMessage());
        } finally {
            DataConnect.close(con);
        }
        return "TODO";
    }

    public static void updateRepair(String ResourceID, String duration) {

        Connection con = null;
        try {

            Date endDate;
            Date startDate = null;

            con = DataConnect.getConnection();
            PreparedStatement preparedStatement =
                    con.prepareStatement("SELECT ReturnDate FROM returndeploy WHERE ResourceID = " + ResourceID +";");
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()){
               //System.out.println("STILL DEPLOYED:"+ResourceID);
                startDate = new Date(rs.getDate("ReturnDate").getTime());
            }else {
                startDate = new Date(System.currentTimeMillis());
            }


            endDate = new Date(startDate.getTime()+Long.parseLong(duration)*24*60*60*1000);
            preparedStatement =
                    con.prepareStatement("SELECT Status FROM resource WHERE ResourceID =" + ResourceID +";");
            rs = preparedStatement.executeQuery();

            if (rs.next()) {
                if (!rs.getString("Status").equals("IN USE")){

                    preparedStatement = con.prepareStatement("UPDATE resource " +
                            "SET Status=\"IN REPAIR\" WHERE ResourceID=?");
                    preparedStatement.setString(1, ResourceID);
                    preparedStatement.executeUpdate();
                }

                preparedStatement =
                        con.prepareStatement("INSERT INTO repair (ResourceID, duration, StartDate, EndDate ) VALUES (?,?,?,?)");

                preparedStatement.setString(1, ResourceID);
                preparedStatement.setString(2, duration);
                preparedStatement.setDate(3, startDate);
                preparedStatement.setDate(4, endDate);
                preparedStatement.executeUpdate();
            }




        } catch (SQLException ex) {
            System.out.println("Login error -->" + ex.getMessage());
        } finally {
            DataConnect.close(con);
        }
    }
}
