package com.trimitrasis.finosapps.Views.SalesQuotationView.Models;

import java.io.Serializable;

/**
 * Created by rahman on 25/04/2017.
 */

public class TaxModel implements Serializable {

    private static final long serialVersionUID = -6285979721469464851L;
    Object _id;
    String _objId;
    String id;
    String description;
    String rate;
    String create_by;
    String create_date;
    String create_time;

    public TaxModel(Object _id, String _objId, String id, String description, String rate, String create_by, String create_date, String create_time){
        this._objId = _objId;
        this._id = _id;
        this.id = id;
        this.description = description;
        this.rate = rate;
        this.create_by = create_by;
        this.create_date = create_date;
        this.create_time = create_time;
    }

    public String get_objId() {
        return _objId;
    }

    public void set_objId(String _objId) {
        this._objId = _objId;
    }

    public Object get_id(){
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

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
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
