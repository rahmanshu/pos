package com.trimitrasis.finosapps.Views.MasterDataView.Models;

import java.io.Serializable;

/**
 * Created by rahman on 27/04/2017.
 */

public class TaxGroupModel implements Serializable {
    private static final long serialVersionUID = -7679802472188383716L;

    String _id;
    String description;

    public TaxGroupModel(String _id, String desc){
        this._id = _id;
        this.description = desc;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
