package com.trimitrasis.finosapps.Views.MasterDataView.Models;

import java.io.Serializable;

/**
 * Created by rahman on 16/03/2017.
 */

public class MainCurrencyModel implements Serializable {
    private static final long serialVersionUID = -7146561799237333117L;

    String currency;
    String description;
    String symbol;

    public MainCurrencyModel(String currency, String description, String symbol){
        this.currency = currency;
        this.description = description;
        this.symbol = symbol;
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
}
