package com.trimitrasis.finosapps.Views.PosView.Model;

import java.io.Serializable;

/**
 * Created by rahman on 30/03/2017.
 */

public class RingkasanOrderModel implements Serializable {
    private static final long serialVersionUID = 5102896362616953750L;


    private String itemId;
    private String kodeBarcode;
    private String kodeBarang;
    private String namaBarang;
    private String satuanBarang;
    private double hargaJual;
    private double qty;
    private double diskon;
    private String info;
    private double tax;
    private double standart_cost;
    private boolean flag_qty;
    private boolean flag_bom;
    private String taxGroup;
    private String note;
    private String detailId;


    public RingkasanOrderModel(String itemId, String kodeBarcode, String kodeBarang, String namaBarang, String satuanBarang,
                               double hargaJual, double qty, double diskon, double tax, String info,
                               double standart_cost, boolean flag_qty, boolean flag_bom, String taxGroup, String note, String detailId){

        this.itemId = itemId;
        this.kodeBarcode = kodeBarcode;
        this.kodeBarang = kodeBarang;
        this.namaBarang = namaBarang;
        this.satuanBarang = satuanBarang;
        this.hargaJual = hargaJual;
        this.qty = qty;
        this.diskon = diskon;
        this.tax = tax;
        this.info = info;
        this.standart_cost = standart_cost;
        this.flag_qty = flag_qty;
        this.flag_bom = flag_bom;
        this.taxGroup = taxGroup;
        this.note = note;
        this.detailId = detailId;
    }

    public boolean isFlag_bom() {
        return flag_bom;
    }

    public void setFlag_bom(boolean flag_bom) {
        this.flag_bom = flag_bom;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getDetailId() {
        return detailId;
    }

    public void setDetailId(String detailId) {
        this.detailId = detailId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getTaxGroup() {
        return taxGroup;
    }

    public void setTaxGroup(String taxGroup) {
        this.taxGroup = taxGroup;
    }

    public double getStandart_cost() {
        return standart_cost;
    }

    public void setStandart_cost(double standart_cost) {
        this.standart_cost = standart_cost;
    }

    public boolean isFlag_qty() {
        return flag_qty;
    }

    public void setFlag_qty(boolean flag_qty) {
        this.flag_qty = flag_qty;
    }

    public String getSatuanBarang() {
        return satuanBarang;
    }

    public void setSatuanBarang(String satuanBarang) {
        this.satuanBarang = satuanBarang;
    }

    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getKodeBarcode() {
        return kodeBarcode;
    }

    public void setKodeBarcode(String kodeBarcode) {
        this.kodeBarcode = kodeBarcode;
    }

    public String getKodeBarang() {
        return kodeBarang;
    }

    public void setKodeBarang(String kodeBarang) {
        this.kodeBarang = kodeBarang;
    }

    public String getNamaBarang() {
        return namaBarang;
    }

    public void setNamaBarang(String namaBarang) {
        this.namaBarang = namaBarang;
    }

    public double getHargaJual() {
        return hargaJual;
    }

    public void setHargaJual(double hargaJual) {
        this.hargaJual = hargaJual;
    }

    public double getQty() {
        return qty;
    }

    public void setQty(double qty) {
        this.qty = qty;
    }

    public double getDiskon() {
        return diskon;
    }

    public void setDiskon(double diskon) {
        this.diskon = diskon;
    }
}
