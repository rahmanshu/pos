package com.trimitrasis.finosapps.Views.SalesQuotationView.Models;

import java.io.Serializable;

/**
 * Created by rahman on 21/04/2017.
 */

public class DetailItemSqModel implements Serializable {

    private static final long serialVersionUID = 4141164177482297136L;


    String itemName;
    double qty;
    String uom;
    double price;
    String tax;
    double discount;
    double total;

    public DetailItemSqModel(String itemName, double qty, String uom, double price, String tax, double discount, double total){
        this.itemName = itemName;
        this.qty = qty;
        this.uom = uom;
        this.price = price;
        this.tax = tax;
        this.discount = discount;
        this.total = total;
    }


    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public double getQty() {
        return qty;
    }

    public void setQty(double qty) {
        this.qty = qty;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }


    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }
}
