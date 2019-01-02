package com.trimitrasis.finosapps.Views.SalesQuotationView.Models;

import java.io.Serializable;

/**
 * Created by rahman on 30/04/2017.
 */

public class CustRateModel implements Serializable {
    private static final long serialVersionUID = 3409045723616199913L;

    String custId;
    String desc;
    String taxId;
    String rate;

    public CustRateModel(String custId, String desc, String taxId, String rate){
        this.custId = custId;
        this.desc = desc;
        this.taxId = taxId;
        this.rate = rate;
    }


    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTaxId() {
        return taxId;
    }

    public void setTaxId(String taxId) {
        this.taxId = taxId;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }
}
