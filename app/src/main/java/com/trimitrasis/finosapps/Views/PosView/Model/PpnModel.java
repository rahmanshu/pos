package com.trimitrasis.finosapps.Views.PosView.Model;

import java.io.Serializable;

/**
 * Created by rahman on 03/07/2017.
 */

public class PpnModel implements Serializable {

    private static final long serialVersionUID = 5551732103096237257L;

    String idPPn;
    String descPpn;

    public PpnModel(String idPpn, String descPpn){
        this.idPPn = idPpn;
        this.descPpn = descPpn;
    }

    public String getIdPPn() {
        return idPPn;
    }

    public void setIdPPn(String idPPn) {
        this.idPPn = idPPn;
    }

    public String getDescPpn() {
        return descPpn;
    }

    public void setDescPpn(String descPpn) {
        this.descPpn = descPpn;
    }
}
