package com.gatech.cs6400.jsf.dao;

import com.gatech.cs6400.jsf.beans.ReleaseRepairBean;
import com.gatech.cs6400.jsf.util.DataConnect;
import com.gatech.cs6400.jsf.util.SessionUtils;

import javax.servlet.http.HttpSession;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReleaseRepairBeanDAO {

    public static List<ReleaseRepairBean> getRepairsScheduled() {

        Connection con = null;
        List<ReleaseRepairBean> list = null;
        try {
            con = DataConnect.getConnection();
            HttpSession session = SessionUtils.getSession();
            String username = (String) session.getAttribute("username");
            PreparedStatement preparedStatement =
                    con.prepareStatement("SELECT resource.ResourceID, resource.ResourceName, resource.Status, repair.StartDate, repair.EndDate" +
                            " FROM resource " +
                            "INNER JOIN repair " +
                            "ON resource.ResourceID = repair.ResourceID " +
                            "WHERE resource.Username = ? AND resource.Status = 'IN REPAIR';");

            preparedStatement.setString(1, username);
            ResultSet rs = preparedStatement.executeQuery();

            list = new ArrayList<ReleaseRepairBean>();

            while (rs.next()) {
                ReleaseRepairBean resourceStatusBean = new ReleaseRepairBean();
                resourceStatusBean.setResourceID(rs.getInt("ResourceID"));
                resourceStatusBean.setResourceDescription(rs.getString("ResourceName"));
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

    public static void updateTable(String table, String ResourceID){


        Connection con = null;
        try {

            con = DataConnect.getConnection();
            PreparedStatement preparedStatement;
            //deletes from repair table
            preparedStatement = con.prepareStatement("DELETE FROM " +table+ " WHERE ResourceID = \""+ResourceID+"\"");
            preparedStatement.executeUpdate();
            //updates resource to Available
            preparedStatement = con.prepareStatement("UPDATE resource SET Status=\"Available\" WHERE ResourceID=?");
            preparedStatement.setString(1, ResourceID);
            preparedStatement.executeUpdate();

        } catch (SQLException ex) {
            System.out.println("Login error -->" + ex.getMessage());
        } finally {
            DataConnect.close(con);
        }
    }
}
