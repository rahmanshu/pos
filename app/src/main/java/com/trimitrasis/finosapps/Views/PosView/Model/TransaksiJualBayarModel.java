package com.trimitrasis.finosapps.Views.PosView.Model;

import com.trimitrasis.finosapps.Views.ContentProvider.TransJualBayarProvider;

import java.io.Serializable;

/**
 * Created by rahman on 03/06/2017.
 */

public class TransaksiJualBayarModel implements Serializable {
    private static final long serialVersionUID = -8580945363874957307L;

    String id_jual_bayar;
    String tipe;
    String bank_id;
    String bank_name;
    String no_kartu;
    int status_sinc;
    double total_bayar;
    String location;
    double kembalian;

    public double getKembalian() {
        return kembalian;
    }

    public void setKembalian(double kembalian) {
        this.kembalian = kembalian;
    }

    public TransaksiJualBayarModel(String id_jual_bayar, String tipe, String bank_id, String bank_name, String no_kartu, int status_sinc, double total_bayar, String location, double kembalian){
        this.id_jual_bayar = id_jual_bayar;
        this.tipe = tipe;
        this.bank_id = bank_id;
        this.bank_name = bank_name;
        this.no_kartu = no_kartu;
        this.status_sinc = status_sinc;
        this.total_bayar = total_bayar;
        this.location = location;
        this.kembalian = kembalian;
    }

    public String getId_jual_bayar() {
        return id_jual_bayar;
    }

    public void setId_jual_bayar(String id_jual_bayar) {
        this.id_jual_bayar = id_jual_bayar;
    }

    public String getTipe() {
        return tipe;
    }

    public void setTipe(String tipe) {
        this.tipe = tipe;
    }

    public String getBank_id() {
        return bank_id;
    }

    public void setBank_id(String bank_id) {
        this.bank_id = bank_id;
    }

    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    public String getNo_kartu() {
        return no_kartu;
    }

    public void setNo_kartu(String no_kartu) {
        this.no_kartu = no_kartu;
    }

    public int getStatus_sinc() {
        return status_sinc;
    }

    public void setStatus_sinc(int status_sinc) {
        this.status_sinc = status_sinc;
    }

    public double getTotal_bayar() {
        return total_bayar;
    }

    public void setTotal_bayar(double total_bayar) {
        this.total_bayar = total_bayar;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
