package com.trimitrasis.finosapps.Views.PosView.Model;

import java.io.Serializable;

/**
 * Created by rahman on 17/04/2017.
 */

public class TransaksiDetailJualModel implements Serializable {

    private static final long serialVersionUID = 7353293383956734488L;
    String id_jual;
    String item_code;
    String description;
    double harga;
    double hargaMember;
    double qty;
    String satuan;
    double discount;
    double total;
    String promo;
    String barcode;
    double disc_percent;
    double disc_amount;
    String tax_type;
    double hargaHpp;

    public TransaksiDetailJualModel(String id_jual,
            String item_code,
            String description,
            double harga,
            double hargaMember,
            double qty,
            String satuan,
            double discount,
            double total,
            String promo,
            String barcode,
            double disc_percent,
            double disc_amount,
            String tax_type,
            double hargaHpp){

        this.id_jual = id_jual;
        this.item_code = item_code;
        this.description = description;
        this.harga = harga;
        this.hargaMember = hargaMember;
        this.qty = qty;
        this.satuan = satuan;
        this.discount = discount;
        this.total = total;
        this.promo = promo;
        this.barcode = barcode;
        this.disc_percent = disc_percent;
        this.disc_amount = disc_amount;
        this.tax_type = tax_type;
        this.hargaHpp = hargaHpp;
    }

    public String getId_jual() {
        return id_jual;
    }

    public void setId_jual(String id_jual) {
        this.id_jual = id_jual;
    }

    public String getItem_code() {
        return item_code;
    }

    public void setItem_code(String item_code) {
        this.item_code = item_code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getHarga() {
        return harga;
    }

    public void setHarga(double harga) {
        this.harga = harga;
    }

    public double getHargaMember() {
        return hargaMember;
    }

    public void setHargaMember(double hargaMember) {
        this.hargaMember = hargaMember;
    }

    public double getQty() {
        return qty;
    }

    public void setQty(double qty) {
        this.qty = qty;
    }

    public String getSatuan() {
        return satuan;
    }

    public void setSatuan(String satuan) {
        this.satuan = satuan;
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

    public String getPromo() {
        return promo;
    }

    public void setPromo(String promo) {
        this.promo = promo;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public double getDisc_percent() {
        return disc_percent;
    }

    public void setDisc_percent(double disc_percent) {
        this.disc_percent = disc_percent;
    }

    public double getDisc_amount() {
        return disc_amount;
    }

    public void setDisc_amount(double disc_amount) {
        this.disc_amount = disc_amount;
    }

    public String getTax_type() {
        return tax_type;
    }

    public void setTax_type(String tax_type) {
        this.tax_type = tax_type;
    }

    public double getHargaHpp() {
        return hargaHpp;
    }

    public void setHargaHpp(double hargaHpp) {
        this.hargaHpp = hargaHpp;
    }
}
