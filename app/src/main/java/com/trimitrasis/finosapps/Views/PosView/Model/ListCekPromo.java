package com.trimitrasis.finosapps.Views.PosView.Model;

import java.io.Serializable;

/**
 * Created by rahman on 10/18/2017.
 */

public class ListCekPromo implements Serializable{

    String kodePromo;
    int qtyBarang;

    public ListCekPromo(String kodePromo, int qtyBarang){
        this.kodePromo = kodePromo;
        this.qtyBarang = qtyBarang;
    }

    public String getKodePromo() {
        return kodePromo;
    }

    public void setKodePromo(String kodePromo) {
        this.kodePromo = kodePromo;
    }

    public int getQtyBarang() {
        return qtyBarang;
    }

    public void setQtyBarang(int qtyBarang) {
        this.qtyBarang = qtyBarang;
    }
}
