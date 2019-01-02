package com.trimitrasis.finosapps.Views.PosView.Model;

import java.io.Serializable;

/**
 * Created by rahman on 18/04/2017.
 */

public class TransaksiPenjualanModel implements Serializable {
    private static final long serialVersionUID = 1991335801783308199L;

    String id_jual;
    String id_member;
    String id_kasir;
    String tanggal;
    String time;
    double subTotal;
    double tax;
    double total_discount;
    double total_bayar;
    String shift_id;
    String id_reference;
    double kembalian;

    String id_jual_detail;
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
    String location;
    String detailId;



    public TransaksiPenjualanModel(String id_jual,
                                   String id_member,
                                   String id_kasir,
                                   String tanggal,
                                   String time,
                                   double subTotal,
                                   double tax,
                                   double total_discount,
                                   double total_bayar,
                                   String shift_id,
                                   String id_reference,
                                   double kembalian,
                                   String id_jual_detail,
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
                                   double hargaHpp,
                                   String location,
                                   String detailId){

        this.id_jual = id_jual;
        this.id_member = id_member;
        this.id_kasir = id_kasir;
        this.tanggal = tanggal;
        this.time = time;
        this.subTotal = subTotal;
        this.tax = tax;
        this.total_discount = total_discount;
        this.total_bayar = total_bayar;
        this.shift_id = shift_id;
        this.id_reference = id_reference;
        this.kembalian = kembalian;
        this.id_jual_detail = id_jual_detail;
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
        this.location = location;
        this.detailId = detailId;

    }


    public TransaksiPenjualanModel(){
    }


    public String getId_jual() {
        return id_jual;
    }

    public void setId_jual(String id_jual) {
        this.id_jual = id_jual;
    }

    public String getId_member() {
        return id_member;
    }

    public void setId_member(String id_member) {
        this.id_member = id_member;
    }

    public String getId_kasir() {
        return id_kasir;
    }

    public void setId_kasir(String id_kasir) {
        this.id_kasir = id_kasir;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }

    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }

    public double getTotal_discount() {
        return total_discount;
    }

    public void setTotal_discount(double total_discount) {
        this.total_discount = total_discount;
    }

    public double getTotal_bayar() {
        return total_bayar;
    }

    public void setTotal_bayar(double total_bayar) {
        this.total_bayar = total_bayar;
    }


    public String getShift_id() {
        return shift_id;
    }

    public void setShift_id(String shift_id) {
        this.shift_id = shift_id;
    }

    public String getId_reference() {
        return id_reference;
    }

    public void setId_reference(String id_reference) {
        this.id_reference = id_reference;
    }

    public double getKembalian() {
        return kembalian;
    }

    public void setKembalian(double kembalian) {
        this.kembalian = kembalian;
    }

    public String getId_jual_detail() {
        return id_jual_detail;
    }

    public void setId_jual_detail(String id_jual_detail) {
        this.id_jual_detail = id_jual_detail;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDetailId() {
        return detailId;
    }

    public void setDetailId(String detailId) {
        this.detailId = detailId;
    }
}
