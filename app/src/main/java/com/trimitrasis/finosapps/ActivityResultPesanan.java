package com.trimitrasis.finosapps;

import com.trimitrasis.finosapps.Views.PosView.Model.RingkasanOrderModel;

/**
 * Created by rahman on 10/4/2017.
 */

public class ActivityResultPesanan {

    RingkasanOrderModel ringkasanOrderModel;

    public ActivityResultPesanan(RingkasanOrderModel ringkasanOrderModel){
        this.ringkasanOrderModel = ringkasanOrderModel;
    }

    public RingkasanOrderModel getRingkasanOrderModel() {
        return ringkasanOrderModel;
    }

    public void setRingkasanOrderModel(RingkasanOrderModel ringkasanOrderModel) {
        this.ringkasanOrderModel = ringkasanOrderModel;
    }
}
