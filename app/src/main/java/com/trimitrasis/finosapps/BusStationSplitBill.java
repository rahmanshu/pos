package com.trimitrasis.finosapps;

import com.squareup.otto.Bus;

/**
 * Created by rahman on 9/26/2017.
 */

public class BusStationSplitBill {
    private static Bus busSplit = new Bus();
    public static Bus getBus(){
        return busSplit;
    }
}
