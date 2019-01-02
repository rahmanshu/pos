package com.trimitrasis.finosapps.Views.UtilView;

import java.io.Serializable;

/**
 * Created by rahman on 06/03/2017.
 */

public class UserModel implements Serializable {

    private static final long serialVersionUID = 4908862653704862938L;

    public static final int USER_DID_NOT_REGISTERED = 0;
    public static final int USER_DID_NOT_ACTIVATED = 1;
    public static final int USER_REGISTERED = 2;
    public static final int USER_DID_NOT_CHECKED = -1;
    int statusId = USER_DID_NOT_CHECKED;

    String idUser = "";
    String nameUser = "";
    String pinUser = "";
    String token = "";
    String f_name = "";
    String l_name = "";
    String phone1 = "";
    String phone2 = "";
    String deviceId = "";

    public UserModel(String idUser, String nameUser, String pinUser, String token, String f_name, String l_name, String phone1, String phone2, String deviceId){
        this.idUser = idUser;
        this.nameUser = nameUser;
        this.pinUser = pinUser;
        this.token = token;
        this.f_name = f_name;
        this.l_name = l_name;
        this.phone1 = phone1;
        this.phone2 = phone2;
        this.deviceId = deviceId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    public String getPinUser() {
        return pinUser;
    }

    public void setPinUser(String pinUser) {
        this.pinUser = pinUser;
    }
}
