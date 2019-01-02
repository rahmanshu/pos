package com.trimitrasis.finosapps.Views.PosView.Model;

import java.io.Serializable;

/**
 * Created by rahman on 9/20/2017.
 */

public class ListPromoBarangQty implements Serializable {

    private static final long serialVersionUID = 4242024899083607275L;

    String kodePromo;
    String barcodeBarang;
    int qtyBarang;

    public  ListPromoBarangQty(String kodePromo, String barcodeBarang, int qtyBarang){
        this.kodePromo = kodePromo;
        this.barcodeBarang = barcodeBarang;
        this.qtyBarang = qtyBarang;
    }

    public String getKodePromo() {
        return kodePromo;
    }

    public void setKodePromo(String kodePromo) {
        this.kodePromo = kodePromo;
    }

    public String getBarcodeBarang() {
        return barcodeBarang;
    }

    public void setBarcodeBarang(String barcodeBarang) {
        this.barcodeBarang = barcodeBarang;
    }

    public int getQtyBarang() {
        return qtyBarang;
    }

    public void setQtyBarang(int qtyBarang) {
        this.qtyBarang = qtyBarang;
    }
}
