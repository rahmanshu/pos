package com.trimitrasis.finosapps.Connection.Models;

import java.io.Serializable;

/**
 * Created by rahman on 13/03/2017.
 */

public class CompanyModel implements Serializable {


    private static final long serialVersionUID = 763946797230875605L;
    String nameCompany;
    String emailCompany;
    String countryCompany;
    String currencyCompany;
    String dbName;
    String dbSize;


    public CompanyModel(String nameCompany, String emailCompany, String countryCompany, String currencyCompany, String dbName, String dbSize){
        this.nameCompany = nameCompany;
        this.emailCompany = emailCompany;
        this.countryCompany = countryCompany;
        this.currencyCompany = currencyCompany;
        this.dbName = dbName;
        this.dbSize = dbSize;
    }



    public String getNameCompany() {
        return nameCompany;
    }

    public void setNameCompany(String nameCompany) {
        this.nameCompany = nameCompany;
    }

    public String getEmailCompany() {
        return emailCompany;
    }

    public void setEmailCompany(String emailCompany) {
        this.emailCompany = emailCompany;
    }

    public String getCountryCompany() {
        return countryCompany;
    }

    public void setCountryCompany(String countryCompany) {
        this.countryCompany = countryCompany;
    }

    public String getCurrencyCompany() {
        return currencyCompany;
    }

    public void setCurrencyCompany(String currencyCompany) {
        this.currencyCompany = currencyCompany;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getDbSize() {
        return dbSize;
    }

    public void setDbSize(String dbSize) {
        this.dbSize = dbSize;
    }
}
