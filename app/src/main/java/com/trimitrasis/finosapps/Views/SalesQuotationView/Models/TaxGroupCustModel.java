package com.trimitrasis.finosapps.Views.SalesQuotationView.Models;

import java.io.Serializable;

/**
 * Created by rahman on 21/04/2017.
 */

public class TaxGroupCustModel implements Serializable {

    private static final long serialVersionUID = -6791323936411761655L;
    Object _id;
    String id;
    String description;
    String create_by;
    String create_date;
    String create_time;

    public TaxGroupCustModel(Object _id, String id, String description, String create_by, String create_date, String create_time){
        this._id = _id;
        this.id = id;
        this.description = description;
        this.create_by = create_by;
        this.create_date = create_date;
        this.create_time = create_time;
    }

    public Object get_id() {
        return _id;
    }

    public void set_id(Object _id) {
        this._id = _id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreate_by() {
        return create_by;
    }

    public void setCreate_by(String create_by) {
        this.create_by = create_by;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }


}
