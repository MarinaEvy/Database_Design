package com.gatech.cs6400.jsf.dao;

import com.gatech.cs6400.jsf.beans.ResourceStatusBean;
import com.gatech.cs6400.jsf.util.DataConnect;
import com.gatech.cs6400.jsf.util.SessionUtils;

import javax.servlet.http.HttpSession;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ResourceStatusDAO {
    public static List<ResourceStatusBean> getResourceInUse() {

        Connection con = null;
        List<ResourceStatusBean> list = null;
        try {
            con = DataConnect.getConnection();
            HttpSession session = SessionUtils.getSession();
            String username = (String) session.getAttribute("username");
            PreparedStatement preparedStatement =
                    con.prepareStatement("SELECT resource.ResourceID, incident.IncidentID, resource.ResourceName, resource.Username, resource.Status, returndeploy.DeployDate, returndeploy.ReturnDate,incident.Description" +
                            " FROM resource" +
                            " INNER JOIN returnDeploy" +
                            " ON resource.ResourceID = returnDeploy.ResourceID" +
                            " INNER JOIN incident" +
                            " ON returnDeploy.IncidentID = incident.IncidentID" +
                            " WHERE incident.Username = ? AND resource.Status = 'IN USE';");

            preparedStatement.setString(1, username);
            ResultSet rs = preparedStatement.executeQuery();

            list = new ArrayList<ResourceStatusBean>();

            while (rs.next()) {
                ResourceStatusBean resourceStatusBean = new ResourceStatusBean();
                resourceStatusBean.setId(rs.getInt("ResourceID"));
                resourceStatusBean.setIncidentID(rs.getString("IncidentID"));
                resourceStatusBean.setStatus(rs.getString("Status"));
                resourceStatusBean.setResourceName(rs.getString("ResourceName"));
                resourceStatusBean.setIncident(rs.getString("Description"));
                resourceStatusBean.setOwner(rs.getString("Username"));
                resourceStatusBean.setStartDate(rs.getString("DeployDate"));
                resourceStatusBean.setEndDate(rs.getString("ReturnDate"));
                list.add(resourceStatusBean);
            }


        } catch (SQLException ex) {
            System.out.println("Login error -->" + ex.getMessage());
        } finally {
            DataConnect.close(con);
        }
        return list;
    }

    public static List<ResourceStatusBean> getResourceRequestedByMe() {

        Connection con = null;
        List<ResourceStatusBean> list = null;
        try {
            con = DataConnect.getConnection();
            HttpSession session = SessionUtils.getSession();
            String username = (String) session.getAttribute("username");
            PreparedStatement preparedStatement =
                    con.prepareStatement("SELECT resource.ResourceID, " +
                            "       resource.ResourceName, " +
                            "       resource.Username, " +
                            "           incident.Description, " +
                            "           incident.IncidentID," +
                            "           resource.Status, " +
                            "           users.UNAME, " +
                            "           request.ReturnDate " +
                            "FROM   resource " +
                            "        INNER JOIN users " +
                            "               ON resource.username = users.username " +
                            "      INNER JOIN request " +
                            "               ON resource.resourceid = request.resourceid " +
                            "       INNER JOIN incident " +
                            "               ON request.IncidentID = incident.IncidentID" +
                            " WHERE  incident.Username = ?" +
                            "       AND NOT EXISTS (SELECT 1 " +
                            "                       FROM   returnDeploy " +
                            "                       WHERE  incident.IncidentID = returnDeploy.IncidentID " +
                            "                              AND resource.ResourceID = returnDeploy.ResourceID);");

            preparedStatement.setString(1, username);
            ResultSet rs = preparedStatement.executeQuery();

            list = new ArrayList<ResourceStatusBean>();

            while (rs.next()) {
                ResourceStatusBean resourceStatusBean = new ResourceStatusBean();
                resourceStatusBean.setId(rs.getInt("ResourceID"));
                resourceStatusBean.setIncidentID(rs.getString("IncidentID"));
                resourceStatusBean.setStatus(rs.getString("Status"));
                resourceStatusBean.setResourceName(rs.getString("ResourceName"));
                resourceStatusBean.setIncident(rs.getString("Description"));
                resourceStatusBean.setOwner(rs.getString("Username"));
                resourceStatusBean.setEndDate(rs.getString("ReturnDate"));
                list.add(resourceStatusBean);
            }


        } catch (SQLException ex) {
            System.out.println("Login error -->" + ex.getMessage());
        } finally {
            DataConnect.close(con);
        }
        return list;
    }

    public static List<ResourceStatusBean> getResourcesRequestedForMe() {

        Connection con = null;
        List<ResourceStatusBean> list = null;
        try {
            con = DataConnect.getConnection();
            HttpSession session = SessionUtils.getSession();
            String username = (String) session.getAttribute("username");
            PreparedStatement preparedStatement =
                    con.prepareStatement(" SELECT resource.ResourceID, incident.IncidentID, resource.ResourceName, incident.Username , incident.Description, request.ReturnDate" +
                            " FROM resource" +
                            " INNER JOIN request" +
                            " ON resource.ResourceID = request.ResourceID" +
                            " INNER JOIN incident" +
                            " ON request.IncidentID = incident.IncidentID" +
                            " WHERE resource.Username =\"" + username + "\";");

            ResultSet rs = preparedStatement.executeQuery();

            list = new ArrayList<ResourceStatusBean>();

            while (rs.next()) {
                ResourceStatusBean resourceStatusBean = new ResourceStatusBean();
                resourceStatusBean.setId(rs.getInt("ResourceID"));
                resourceStatusBean.setIncidentID(rs.getString("IncidentID"));
                resourceStatusBean.setResourceName(rs.getString("ResourceName"));
                resourceStatusBean.setIncident(rs.getString("Description"));
                resourceStatusBean.setRequester(rs.getString("Username"));
                resourceStatusBean.setReturnDate(rs.getDate("ReturnDate"));
                list.add(resourceStatusBean);
            }


        } catch (SQLException ex) {
            System.out.println("Login error -->" + ex.getMessage());
        } finally {
            DataConnect.close(con);
        }
        return list;
    }

    public static List<ResourceStatusBean> getRepairsScheduled() {

        Connection con = null;
        List<ResourceStatusBean> list = null;
        try {
            con = DataConnect.getConnection();
            HttpSession session = SessionUtils.getSession();
            String username = (String) session.getAttribute("username");
            PreparedStatement preparedStatement =
                    con.prepareStatement("SELECT resource.ResourceID, resource.ResourceName, resource.Status, repair.StartDate, repair.EndDate" +
                            " FROM resource " +
                            "INNER JOIN repair " +
                            "ON resource.ResourceID = repair.ResourceID " +
                            "WHERE resource.Username = ?;");

            preparedStatement.setString(1, username);
            ResultSet rs = preparedStatement.executeQuery();

            list = new ArrayList<ResourceStatusBean>();

            while (rs.next()) {
                ResourceStatusBean resourceStatusBean = new ResourceStatusBean();
                resourceStatusBean.setId(rs.getInt("ResourceID"));
                resourceStatusBean.setStatus(rs.getString("Status"));
                resourceStatusBean.setResourceName(rs.getString("ResourceName"));
                resourceStatusBean.setStartDate(rs.getString("StartDate"));
                resourceStatusBean.setEndDate(rs.getString("EndDate"));
                list.add(resourceStatusBean);
            }


        } catch (SQLException ex) {
            System.out.println("Login error -->" + ex.getMessage());
        } finally {
            DataConnect.close(con);
        }
        return list;
    }

    public static void updateTable(String table, String ResourceID, String IncidentID, String status, Date ReturnDate) {


        Connection con = null;
        try {

            con = DataConnect.getConnection();

            PreparedStatement preparedStatement;

            if (table.equals("deploy")) {
                preparedStatement = con.prepareStatement("INSERT INTO returndeploy (ResourceID, IncidentID, DeployDate, ReturnDate, Returned) VALUES (?,?,?,?,?)");
                preparedStatement.setString(1, ResourceID);
                preparedStatement.setString(2, IncidentID);
                preparedStatement.setDate(3, new Date(System.currentTimeMillis()));
                preparedStatement.setDate(4, ReturnDate);
                preparedStatement.setString(5, "false");
                preparedStatement.executeUpdate();

                preparedStatement = con.prepareStatement("DELETE FROM request WHERE ResourceID = \"" + ResourceID + "\" AND IncidentID = \"" + IncidentID + "\"");
                preparedStatement.executeUpdate();

                preparedStatement = con.prepareStatement("UPDATE resource SET Status=\"IN USE\" WHERE ResourceID=?");
                preparedStatement.setString(1, ResourceID);
                preparedStatement.executeUpdate();

                return;

            }

            if (!table.equals("repair")) //returndeploy
                preparedStatement = con.prepareStatement("UPDATE " + table + " SET Returned = \"true\" WHERE ResourceID = \"" + ResourceID + "\"");
            else
                preparedStatement = con.prepareStatement("DELETE FROM " + table + " WHERE ResourceID = \"" + ResourceID + "\"");


            preparedStatement.executeUpdate();


            //checks if there is a repair request waiting and if so, switches to repair
            if (status.equals("IN USE")) {
                preparedStatement =
                        con.prepareStatement("SELECT * FROM repair WHERE ResourceID = ?");

                preparedStatement.setString(1, ResourceID);
                ResultSet rs = preparedStatement.executeQuery();
                if (rs.next()) {
                    Date StartDate = new Date(System.currentTimeMillis());

                    Date EndDate = new Date(StartDate.getTime() + rs.getLong("Duration") * 24 * 60 * 60 * 1000);
                    System.out.println(StartDate+":"+EndDate);
                    preparedStatement = con.prepareStatement("UPDATE repair SET StartDate=?, EndDate=?  WHERE ResourceID=?");

                    preparedStatement.setDate(1, StartDate);
                    preparedStatement.setDate(2, EndDate);
                    preparedStatement.setString(3, ResourceID);
                    preparedStatement.executeUpdate();

                    preparedStatement = con.prepareStatement("UPDATE resource SET Status=\"IN REPAIR\" WHERE ResourceID=?");
                    preparedStatement.setString(1, ResourceID);
                    preparedStatement.executeUpdate();
                    HttpSession session = SessionUtils.getSession();
                    session.setAttribute("newStartDate",StartDate);
                    session.setAttribute("newEndDate",EndDate);


                    return;
                }
            }

            // if not a request, will change "IN REPAIR" or "IN USE" to available
            if (!table.equals("request") && !status.equals("Available")) {

                preparedStatement = con.prepareStatement("UPDATE resource SET Status=\"Available\" WHERE ResourceID=?");
                preparedStatement.setString(1, ResourceID);
                preparedStatement.executeUpdate();

            }

        } catch (SQLException ex) {
            System.out.println("Login error -->" + ex.getMessage());
        } finally {
            DataConnect.close(con);
        }
    }

    public static boolean alreadyDeployed(String id) {
        Connection con = null;
        try {
            con = DataConnect.getConnection();
            PreparedStatement preparedStatement =
                    con.prepareStatement("SELECT * " +
                            "FROM resource WHERE ResourceID = ?;");
            preparedStatement.setString(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) return !rs.getString("Status").equals("Available");
            return true;

        } catch (SQLException ex) {
            System.out.println("Login error -->" + ex.getMessage());
        } finally {
            DataConnect.close(con);
        }
        return false;
    }
}
