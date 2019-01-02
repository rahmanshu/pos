package com.trimitrasis.finosapps.Views.PosView.Model;

import java.io.Serializable;

/**
 * Created by rahman on 04/04/2017.
 */

public class TundaJualDetailModel implements Serializable {

    private static final long serialVersionUID = -7920436421988210932L;
    double id_jual_item;
    double id_jual;
    String item_code;
    String description;
    double harga;
    double harga_member;
    double qty;
    String satuan;
    double discount;
    double total;
    String promo;
    String disc_percent;
    String barcode;
    double disc_amount;
    String tax_type;
    double hpp;

    public TundaJualDetailModel(double id_jual_item,
            double id_jual,
            String item_code,
            String description,
            double harga,
            double harga_member,
            double qty,
            String satuan,
            double discount,
            double total,
            String promo,
            String disc_percent,
            String barcode,
            double disc_amount,
            String tax_type,
            double hpp){

        this.id_jual = id_jual;
        this.item_code = item_code;
        this.description = description;
        this.harga = harga;
        this.harga_member = harga_member;
        this.qty = qty;
        this.satuan = satuan;
        this.discount = discount;
        this.total = total;
        this.promo = promo;
        this.disc_percent = disc_percent;
        this.barcode = barcode;
        this.disc_amount = disc_amount;
        this.tax_type = tax_type;
        this.hpp = hpp;

    }




    public double getId_jual_item() {
        return id_jual_item;
    }

    public void setId_jual_item(double id_jual_item) {
        this.id_jual_item = id_jual_item;
    }

    public double getId_jual() {
        return id_jual;
    }

    public void setId_jual(double id_jual) {
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

    public double getHarga_member() {
        return harga_member;
    }

    public void setHarga_member(double harga_member) {
        this.harga_member = harga_member;
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

    public String getDisc_percent() {
        return disc_percent;
    }

    public void setDisc_percent(String disc_percent) {
        this.disc_percent = disc_percent;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
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

    public double getHpp() {
        return hpp;
    }

    public void setHpp(double hpp) {
        this.hpp = hpp;
    }
}
