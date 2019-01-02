package com.trimitrasis.finosapps.Views.UserProfileView.Models;

import java.io.Serializable;

/**
 * Created by rahman on 14/03/2017.
 */

public class UserProfile implements Serializable {
    private static final long serialVersionUID = 6432192000289169803L;


    String email;
    String password;
    String token;
    String f_name;
    String l_name;
    String phone1;
    String phone2;


    public  UserProfile(String email, String password, String token, String f_name, String l_name, String phone1, String phone2){
        this.email = email;
        this.password = password;
        this.token = token;
        this.f_name = f_name;
        this.l_name = l_name;
        this.phone1 = phone1;
        this.phone2 = phone2;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getF_name() {
        return f_name;
    }

    public void setF_name(String f_name) {
        this.f_name = f_name;
    }

    public String getL_name() {
        return l_name;
    }

    public void setL_name(String l_name) {
        this.l_name = l_name;
    }

    public String getPhone1() {
        return phone1;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }
}
