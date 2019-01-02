package com.trimitrasis.finosapps.Views.SalesQuotationView.Models;

import java.io.Serializable;

/**
 * Created by rahman on 20/04/2017.
 */

public class ItemConvertionSalesQuotModel implements Serializable {
    private static final long serialVersionUID = 3800192600969702957L;

    String uom;
    double uom_convertion;
    double base_uom_convertion;
    double harga_jual;
    String barcode;
    String item_id;

    public ItemConvertionSalesQuotModel(String uom,
            double uom_convertion,
            double base_uom_convertion,
            double harga_jual,
            String barcode,
            String item_id){

        this.uom = uom;
        this.uom_convertion = uom_convertion;
        this.base_uom_convertion = base_uom_convertion;
        this.harga_jual = harga_jual;
        this.barcode = barcode;
        this.item_id = item_id;

    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public double getUom_convertion() {
        return uom_convertion;
    }

    public void setUom_convertion(double uom_convertion) {
        this.uom_convertion = uom_convertion;
    }

    public double getBase_uom_convertion() {
        return base_uom_convertion;
    }

    public void setBase_uom_convertion(double base_uom_convertion) {
        this.base_uom_convertion = base_uom_convertion;
    }

    public double getHarga_jual() {
        return harga_jual;
    }

    public void setHarga_jual(double harga_jual) {
        this.harga_jual = harga_jual;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }
}
