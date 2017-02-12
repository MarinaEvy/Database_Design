package com.gatech.cs6400.jsf.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.gatech.cs6400.jsf.util.DataConnect;

public class LoginDAO {

    public static boolean validate(String user, String password) {
        System.out.println("LOGIN DAO:"+ user +":"+password);
        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = DataConnect.getConnection();
            ps = con.prepareStatement("Select Username, password from Users where Username = ? and password = ?");
            ps.setString(1, user);
            ps.setString(2, password);
            System.out.println(user+":"+password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                //result found, means valid inputs
                return true;
            }
        } catch (SQLException ex) {
            System.out.println("Login error -->" + ex.getMessage());
            return false;
        } finally {
            DataConnect.close(con);
        }
        return false;
    }

    public static String getDetails(String user) {
        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = DataConnect.getConnection();
            ps = con.prepareStatement("SELECT users.Username, " +
                    "municipality.PopulationSize, " +
                    "governmentAgency.Jurisdiction, " +
                    "company.Location, " +
                    "individual.JobTitle, " +
                    "individual.DateHired " +
                    "FROM users " +
                    "LEFT OUTER JOIN municipality ON " +
                    "users.Username = municipality.Username " +
                    "LEFT OUTER JOIN governmentAgency ON " +
                    "users.Username = governmentAgency.Username " +
                    "LEFT OUTER JOIN company ON " +
                    "users.Username = company.Username " +
                    "LEFT OUTER JOIN individual ON " +
                    "users.Username = individual.Username " +
                    "WHERE users.Username = ?");
            ps.setString(1, user);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                if (rs.getString("PopulationSize")!=null)
                {
                    return "Population Size: #" + rs.getString("PopulationSize");
                }else if (rs.getString("Jurisdiction")!=null)
                {
                    return "Jurisdiction:#" + rs.getString("Jurisdiction");
                }else if (rs.getString("Location")!=null)
                {
                    return "Location:#" + rs.getString("Location");
                }else if (rs.getString("JobTitle")!=null)
                {
                    return "Job Title:" + rs.getString("JobTitle") + "#" +"Date Hired:" + rs.getString("DateHired");
                }
            }
        } catch (SQLException ex) {
            System.out.println("Login error -->" + ex.getMessage());
            return null;
        } finally {
            DataConnect.close(con);
        }
        return null;
    }
}
