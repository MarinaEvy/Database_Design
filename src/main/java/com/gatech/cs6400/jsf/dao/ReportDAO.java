package com.gatech.cs6400.jsf.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.gatech.cs6400.jsf.beans.ReportBean;
import com.gatech.cs6400.jsf.util.DataConnect;
import com.gatech.cs6400.jsf.util.SessionUtils;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;

public class ReportDAO {

    public static List<ReportBean> getAllESF() {

        Connection con = null;
        List<ReportBean> list = null;
        try {
            con = DataConnect.getConnection();
            HttpSession session = SessionUtils.getSession();
            String username = (String) session.getAttribute("username");
            PreparedStatement preparedStatement =
                    con.prepareStatement("SELECT esf.esf_id, \n" +
                            "       esf.description, \n" +
                            "       COALESCE(x.cnt, 0)  AS 'Total Resources', \n" +
                            "       COALESCE(y.cnt, 0) AS 'Resources in Use' \n" +
                            "FROM   esf \n" +
                            "       LEFT JOIN resource \n" +
                            "              ON esf.esf_id = resource.PrimaryESF\n" +
                            "       LEFT JOIN returnDeploy \n" +
                            "              ON resource.resourceID= returnDeploy.resourceID \n" +
                            "       LEFT OUTER JOIN (SELECT PrimaryESF, \n" +
                            "                               Count(*) cnt \n" +
                            "                        FROM   resource \n" +
                            "                        WHERE  Username = '" + username + "' \n" +
                            "                        GROUP  BY PrimaryESF) x \n" +
                            "                    ON esf.esf_id = x.PrimaryESF\n" +
                            "       LEFT OUTER JOIN (SELECT PrimaryESF, \n" +
                            "                               Count(*) cnt \n" +
                            "                        FROM   resource \n" +
                            "                        WHERE  ( status = 'IN USE' AND \n" +
                            "                                 Username = '" + username + "' ) \n" +
                            "                        GROUP  BY PrimaryESF) y \n" +
                            "                    ON esf.esf_id = y.PrimaryESF \n" +
                            "GROUP BY esf.ESF_ID \n" +
                            "UNION ALL \n" +
                            "SELECT null, \n" +
                            "'Totals', \n" +
                            "(SELECT COUNT(ResourceID) FROM emrs.resource WHERE Username = '" + username + "'), \n" +
                            "(SELECT COUNT(ResourceID) FROM emrs.resource WHERE Username = '" + username + "' AND status = 'IN USE');");
            ResultSet rs = preparedStatement.executeQuery();

            list = new ArrayList<ReportBean>();

            while (rs.next()) {
                ReportBean reportBean = new ReportBean();
                reportBean.setEsfID(rs.getString("esf_id"));
                reportBean.setEsfDescription(rs.getString("description"));
                reportBean.setTotalResources(rs.getString("Total Resources"));
                reportBean.setResourcesInUse(rs.getString("Resources in Use"));
                list.add(reportBean);
            }


        } catch (SQLException ex) {
            System.out.println("Login error -->" + ex.getMessage());
        } finally {
            DataConnect.close(con);
        }
        return list;
    }




}
