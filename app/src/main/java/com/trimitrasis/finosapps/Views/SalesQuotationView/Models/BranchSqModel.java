package com.trimitrasis.finosapps.Views.SalesQuotationView.Models;

import com.trimitrasis.finosapps.Views.MasterDataView.Models.BranchModel;

import java.io.Serializable;

/**
 * Created by rahman on 20/04/2017.
 */

public class BranchSqModel implements Serializable {

    private static final long serialVersionUID = 4523043149983932660L;

    Object _id;
    String branchId;
    String custName;
    String branchName;


    public BranchSqModel(Object _id, String branchId, String custName, String branchName){
        this.branchId = branchId;
        this._id = _id;
        this.custName = custName;
        this.branchName = branchName;
    }

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public Object get_id(){
        return _id;
    }

    public void set_id(Object _id){
        this._id = _id;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName){
        this.branchName = branchName;
    }


}
