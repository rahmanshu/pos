package com.trimitrasis.finosapps.Views.PosView.Model;

import java.io.Serializable;

/**
 * Created by rahman on 09/06/2017.
 */

public class HeaderHoldSalesModel implements Serializable {

    private static final long serialVersionUID = -2778971253000129872L;

    String id_jual;
    String tanggal;
    double total;
    String kasir;
    String nomo_meja;
    String jenis_pajak;
    double pajak_ppn;

    public HeaderHoldSalesModel(String id_jual, String tanggal, double total, String kasir, String nomor_meja, String jenis_pajak, double pajak_ppn){
        this.id_jual = id_jual;
        this.tanggal = tanggal;
        this.total = total;
        this.kasir = kasir;
        this.nomo_meja = nomor_meja;
        this.jenis_pajak = jenis_pajak;
        this.pajak_ppn = pajak_ppn;
    }

    public String getJenis_pajak() {
        return jenis_pajak;
    }

    public void setJenis_pajak(String jenis_pajak) {
        this.jenis_pajak = jenis_pajak;
    }

    public double getPajak_ppn() {
        return pajak_ppn;
    }

    public void setPajak_ppn(double pajak_ppn) {
        this.pajak_ppn = pajak_ppn;
    }

    public String getNomo_meja() {
        return nomo_meja;
    }

    public void setNomo_meja(String nomo_meja) {
        this.nomo_meja = nomo_meja;
    }

    public String getId_jual() {
        return id_jual;
    }

    public void setId_jual(String id_jual) {
        this.id_jual = id_jual;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getKasir() {
        return kasir;
    }

    public void setKasir(String kasir) {
        this.kasir = kasir;
    }
}
