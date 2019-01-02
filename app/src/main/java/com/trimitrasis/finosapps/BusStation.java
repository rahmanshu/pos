package com.trimitrasis.finosapps;

import com.squareup.otto.Bus;

/**
 * Created by rahman on 30/03/2017.
 */

public class BusStation{
    private static Bus bus = new Bus();
    public static Bus getBus(){
        return bus;
    }
}
