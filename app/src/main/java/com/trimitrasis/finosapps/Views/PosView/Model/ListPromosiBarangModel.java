package com.trimitrasis.finosapps.Views.PosView.Model;

import java.io.Serializable;

/**
 * Created by rahman on 9/29/2017.
 */

public class ListPromosiBarangModel implements Serializable {


    private static final long serialVersionUID = 1200333109506998895L;
    String kodePromo;

    public ListPromosiBarangModel(String kodePromo){
        this.kodePromo = kodePromo;
    }

    public String getKodePromo() {
        return kodePromo;
    }

    public void setKodePromo(String kodePromo) {
        this.kodePromo = kodePromo;
    }
}
