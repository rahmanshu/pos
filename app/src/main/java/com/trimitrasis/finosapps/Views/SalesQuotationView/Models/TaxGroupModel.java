package com.trimitrasis.finosapps.Views.SalesQuotationView.Models;

import java.io.Serializable;

/**
 * Created by rahman on 20/04/2017.
 */

public class TaxGroupModel implements Serializable {

    private static final long serialVersionUID = 3612524009465690326L;
    Object _id;
    String taxId;
    String taxGroupName;
    String taxTypeName;

    public TaxGroupModel(Object _id, String taxId, String taxGroupName, String taxTypeName){
        this._id = _id;
        this.taxId = taxId;
        this.taxGroupName = taxGroupName;
        this.taxTypeName = taxTypeName;
    }

    public Object get_id() {
        return _id;
    }

    public void set_id(Object _id) {
        this._id = _id;
    }

    public String getTaxId() {
        return taxId;
    }

    public void setTaxId(String taxId) {
        this.taxId = taxId;
    }

    public String getTaxGroupName() {
        return taxGroupName;
    }

    public void setTaxGroupName(String taxGroupName) {
        this.taxGroupName = taxGroupName;
    }

    public String getTaxTypeName() {
        return taxTypeName;
    }

    public void setTaxTypeName(String taxTypeName) {
        this.taxTypeName = taxTypeName;
    }
}
