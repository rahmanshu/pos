package com.trimitrasis.finosapps.Views.InvoiceReceiptView.InputItemView.Models;

import java.io.Serializable;

/**
 * Created by rahman on 07/03/2017.
 */

public class BillingItemModel implements Serializable {

    String id;
    String desc;
    String price;



    public BillingItemModel(String id, String desc, String price){
        this.id    = id;
        this.desc  = desc;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
