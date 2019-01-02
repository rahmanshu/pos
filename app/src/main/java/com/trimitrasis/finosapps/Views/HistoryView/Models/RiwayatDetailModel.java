package com.trimitrasis.finosapps.Views.HistoryView.Models;

import java.io.Serializable;

/**
 * Created by rahman on 10/24/2017.
 */

public class RiwayatDetailModel implements Serializable {

    String namaBarang;
    double qty;
    double harga;

    public RiwayatDetailModel(String namaBarang, double qty, double harga){
        this.namaBarang = namaBarang;
        this.qty = qty;
        this.harga = harga;
    }

    public String getNamaBarang() {
        return namaBarang;
    }

    public void setNamaBarang(String namaBarang) {
        this.namaBarang = namaBarang;
    }

    public double getQty() {
        return qty;
    }

    public void setQty(double qty) {
        this.qty = qty;
    }

    public double getHarga() {
        return harga;
    }

    public void setHarga(double harga) {
        this.harga = harga;
    }
}
