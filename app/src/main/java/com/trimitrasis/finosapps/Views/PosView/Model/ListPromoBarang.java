package com.trimitrasis.finosapps.Views.PosView.Model;

import java.io.Serializable;

/**
 * Created by rahman on 9/19/2017.
 */

public class ListPromoBarang implements Serializable {

    private static final long serialVersionUID = -160974427747565406L;
    String kodePromo;
    int countBarang;

    public ListPromoBarang(String kodePromo, int countBarang){
        this.kodePromo = kodePromo;
        this.countBarang = countBarang;
    }

    public String getKodePromo() {
        return kodePromo;
    }

    public void setKodePromo(String kodePromo) {
        this.kodePromo = kodePromo;
    }

    public int getCountBarang() {
        return countBarang;
    }

    public void setCountBarang(int countBarang) {
        this.countBarang = countBarang;
    }
}
