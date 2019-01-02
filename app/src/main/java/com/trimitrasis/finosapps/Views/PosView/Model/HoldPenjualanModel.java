package com.trimitrasis.finosapps.Views.PosView.Model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by rahman on 04/04/2017.
 */

public class HoldPenjualanModel implements Serializable {
    private static final long serialVersionUID = 1858242967483593046L;

    Object _id;
    double id_jual;
    String id_kasir;
    String tanggal;
    double subtotal;
    double tax;
    double discount;
    double total;
    String note;
    boolean flag_delete;
    List<TundaJualDetailModel> tundaJualDetailModels;

    public HoldPenjualanModel(Object _id,
            double id_jual,
            String id_kasir,
            String tanggal,
            double subtotal,
            double tax,
            double discount,
            double total,
            String note,
            boolean flag_delete,
            List<TundaJualDetailModel> tundaJualDetailModels){

        this._id = _id;
        this.id_jual = id_jual;
        this.id_kasir = id_kasir;
        this.tanggal = tanggal;
        this.subtotal = subtotal;
        this.tax = tax;
        this.discount = discount;
        this.total = total;
        this.note = note;
        this.flag_delete = flag_delete;
    }

    public Object get_id() {
        return _id;
    }

    public void set_id(Object _id) {
        this._id = _id;
    }

    public double getId_jual() {
        return id_jual;
    }

    public void setId_jual(double id_jual) {
        this.id_jual = id_jual;
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

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public boolean isFlag_delete() {
        return flag_delete;
    }

    public void setFlag_delete(boolean flag_delete) {
        this.flag_delete = flag_delete;
    }

    public List<TundaJualDetailModel> getTundaJualDetailModels() {
        return tundaJualDetailModels;
    }

    public void setTundaJualDetailModels(List<TundaJualDetailModel> tundaJualDetailModels) {
        this.tundaJualDetailModels = tundaJualDetailModels;
    }
}
