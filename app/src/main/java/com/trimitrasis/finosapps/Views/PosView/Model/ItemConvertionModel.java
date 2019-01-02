package com.trimitrasis.finosapps.Views.PosView.Model;

import java.io.Serializable;

/**
 * Created by rahman on 30/03/2017.
 */

public class ItemConvertionModel implements Serializable {
    private static final long serialVersionUID = -2612466833275155202L;

    private String uom;
    private String uom_convertion;
    private String base_uom_convertion;
    private double harga_jual;
    private String barcode;

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public String getUom_convertion() {
        return uom_convertion;
    }

    public void setUom_convertion(String uom_convertion) {
        this.uom_convertion = uom_convertion;
    }

    public String getBase_uom_convertion() {
        return base_uom_convertion;
    }

    public void setBase_uom_convertion(String base_uom_convertion) {
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
}
