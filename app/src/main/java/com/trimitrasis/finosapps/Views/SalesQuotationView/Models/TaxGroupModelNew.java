package com.trimitrasis.finosapps.Views.SalesQuotationView.Models;

import java.io.Serializable;

/**
 * Created by rahman on 28/04/2017.
 */

public class TaxGroupModelNew implements Serializable {


    private static final long serialVersionUID = 7154458782562915930L;
    String idTaxGroup;
    String description;

    public TaxGroupModelNew(String idTaxGroup, String description){
        this.idTaxGroup = idTaxGroup;
        this.description = description;
    }

    public String getIdTaxGroup() {
        return idTaxGroup;
    }

    public void setIdTaxGroup(String idTaxGroup) {
        this.idTaxGroup = idTaxGroup;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
