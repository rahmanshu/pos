package com.trimitrasis.finosapps.Views.MasterDataView.Models;

import com.trimitrasis.finosapps.Connection.Models.CompanyModel;

import java.io.Serializable;

/**
 * Created by rahman on 16/03/2017.
 */

public class CurrencyModel implements Serializable {
    private static final long serialVersionUID = 8008425846141004962L;

    String currency;
    String description;
    String symbol;
    CompanyModel companyModel;

    public CurrencyModel(String currency, String description, String symbol, CompanyModel companyModel){
        this.currency = currency;
        this.description = description;
        this.symbol = symbol;
        this.companyModel = companyModel;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public CompanyModel getCompanyModel() {
        return companyModel;
    }

    public void setCompanyModel(CompanyModel companyModel) {
        this.companyModel = companyModel;
    }
}
