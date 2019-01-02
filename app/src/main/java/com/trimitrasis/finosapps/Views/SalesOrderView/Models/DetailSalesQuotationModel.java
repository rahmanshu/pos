package com.trimitrasis.finosapps.Views.SalesOrderView.Models;
import java.io.Serializable;

/**
 * Created by rahman on 03/05/2017.
 */


public class DetailSalesQuotationModel implements Serializable {

    private static final long serialVersionUID = -9135026659177942594L;
    Object _id;
    Object itemId;
    String itemIdString;
    String description;
    double qty;
    String uom;
    double price;
    double discount;
    double total;
    String transNo;


    public  DetailSalesQuotationModel(Object _id, Object itemId,  String itemIdString, String description, double qty, String uom, double price, double discount, double total, String transNo){
        this._id = _id;
        this.itemId = itemId;
        this.itemIdString = itemIdString;
        this.description = description;
        this.qty = qty;
        this.uom = uom;
        this.price = price;
        this.discount = discount;
        this.total = total;
        this.transNo = transNo;
    }


    public  DetailSalesQuotationModel(String itemIdString, String description, double qty, String uom, double price, double discount, double total, String transNo){
        this.itemIdString = itemIdString;
        this.description = description;
        this.qty = qty;
        this.uom = uom;
        this.price = price;
        this.discount = discount;
        this.total = total;
        this.transNo = transNo;
    }

    public String getTransNo() {
        return transNo;
    }

    public void setTransNo(String transNo) {
        this.transNo = transNo;
    }

    public Object get_id() {
        return _id;
    }

    public void set_id(Object _id) {
        this._id = _id;
    }

    public String getItemIdString() {
        return itemIdString;
    }

    public void setItemIdString(String itemIdString) {
        this.itemIdString = itemIdString;
    }

    public Object getItemId() {
        return itemId;
    }

    public void setItemId(Object itemId) {
        this.itemId = itemId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

}
