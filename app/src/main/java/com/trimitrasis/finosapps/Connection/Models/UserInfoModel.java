package com.trimitrasis.finosapps.Connection.Models;
import java.io.Serializable;

/**
 * Created by rahman on 23/05/2017.
 */

public class UserInfoModel implements Serializable {

    private static final long serialVersionUID = 4337167328146548017L;

    String email;
    String fname;
    String comp;
    String devid;
    String dbname;
    String comname;
    String currency;
    String token;
    String location;
    String locationName;
    String locationAddress;


    public UserInfoModel(){

    }

    public UserInfoModel(String email, String fname, String comp, String devid, String dbname, String comname, String currency, String token, String location, String locationName, String locationAddress){

        this.email = email;
        this.fname = fname;
        this.comp = comp;
        this.devid = devid;
        this.dbname = dbname;
        this.comname = comname;
        this.currency = currency;
        this.token = token;
        this.location = location;
        this.locationName = locationName;
        this.locationAddress = locationAddress;
    }

    public String getLocationAddress() {
        return locationAddress;
    }

    public void setLocationAddress(String locationAddress) {
        this.locationAddress = locationAddress;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }


    public String getComp() {
        return comp;
    }

    public void setComp(String comp) {
        this.comp = comp;
    }

    public String getDevid() {
        return devid;
    }

    public void setDevid(String devid) {
        this.devid = devid;
    }

    public String getDbname() {
        return dbname;
    }

    public void setDbname(String dbname) {
        this.dbname = dbname;
    }

    public String getComname() {
        return comname;
    }

    public void setComname(String comname) {
        this.comname = comname;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
