package com.gatech.cs6400.jsf.beans;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpSession;
import com.gatech.cs6400.jsf.dao.LoginDAO;
import com.gatech.cs6400.jsf.util.SessionUtils;

@ManagedBean
@SessionScoped
public class Login implements Serializable {

    private static final long serialVersionUID = 1094801825228386363L;

    private String pwd;
    private String msg;
    private String user;
    private String detail1,
                   detail2;
    private boolean displayError;
    private String errorMessage;

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getDetail1() {
        return detail1;
    }

    public void setDetail1(String detail1) {
        this.detail1 = detail1;
    }

    public String getDetail2() {
        return detail2;
    }

    public void setDetail2(String detail2) {
        this.detail2 = detail2;
    }

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

    //validate login
    public String validateUsernamePassword() {
        if (this.user.equals("") || this.pwd.equals("")){
            this.errorMessage = "Please enter values for both the username and password.";
            displayError = true;
            return "login";
        } else {
            errorMessage = "";
            displayError = false;
            String details;
            boolean valid = LoginDAO.validate(user, pwd);
            if (valid) {
                HttpSession session = SessionUtils.getSession();
                session.setAttribute("username", user);
                details = LoginDAO.getDetails(user);
                String delims = "[#]";
                String[] tokens = details.split(delims);
                this.detail1 = tokens[0];
                this.detail2 = tokens[1];
                addMessage("Welcome " + this.user);
                return "menu";
            } else {
                this.errorMessage = "Issues with this username and password combination please try again.";
                displayError = true;
                return "login";
            }
        }
    }

    //logout event, invalidate session
    public String logout() {
        HttpSession session = SessionUtils.getSession();
        session.invalidate();
        return "login";
    }

    public void addMessage(String summary) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary,  null);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

}