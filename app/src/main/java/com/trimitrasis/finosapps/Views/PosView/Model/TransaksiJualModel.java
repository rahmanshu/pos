package com.trimitrasis.finosapps.Views.PosView.Model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by rahman on 17/04/2017.
 */

public class TransaksiJualModel implements Serializable {

    private static final long serialVersionUID = -4339210716882547893L;

    String id_jual;
    String id_member;
    String id_kasir;
    String tanggal;
    double subTotal;
    double tax;
    double total_discount;
    double total_bayar;
    boolean flag_delete;
    String shift_id;
    String id_reference;
    double kembalian;
    List<TransaksiDetailJualModel> transaksiDetailJualModels;

    public TransaksiJualModel(String id_jual,
            String id_member,
            String id_kasir,
            String tanggal,
            double subTotal,
            double tax,
            double total_discount,
            double total_bayar,
            boolean flag_delete,
            String shift_id,
            String id_reference,
            double kembalian,
            List<TransaksiDetailJualModel> transaksiDetailJualModels){

        this.id_jual = id_jual;
        this.id_member = id_member;
        this.id_kasir = id_kasir;
        this.tanggal = tanggal;
        this.subTotal = subTotal;
        this.tax = tax;
        this.total_discount = total_discount;
        this.total_bayar = total_bayar;
        this.flag_delete = flag_delete;
        this.shift_id = shift_id;
        this.id_reference = id_reference;
        this.kembalian = kembalian;
        this.transaksiDetailJualModels = transaksiDetailJualModels;
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

    public boolean isFlag_delete() {
        return flag_delete;
    }

    public void setFlag_delete(boolean flag_delete) {
        this.flag_delete = flag_delete;
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


    public List<TransaksiDetailJualModel> getTransaksiDetailJualModels() {
        return transaksiDetailJualModels;
    }

    public void setTransaksiDetailJualModels(List<TransaksiDetailJualModel> transaksiDetailJualModels) {
        this.transaksiDetailJualModels = transaksiDetailJualModels;
    }
}
