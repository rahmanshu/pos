package com.trimitrasis.finosapps.Views.PosView.Model;

import com.trimitrasis.finosapps.Connection.Models.CompanyModel;

import java.io.Serializable;

/**
 * Created by rahman on 17/05/2017.
 */

public class UpdateSyncModel implements Serializable {

    private static final long serialVersionUID = -2398125427760328259L;
    String typeSync;
    CompanyModel companyModel;

    public UpdateSyncModel(String typeSync, CompanyModel companyModel){
        this.typeSync = typeSync;
        this.companyModel = companyModel;
    }

    public String getTypeSync() {
        return typeSync;
    }

    public void setTypeSync(String typeSync) {
        this.typeSync = typeSync;
    }

    public CompanyModel getCompanyModel() {
        return companyModel;
    }

    public void setCompanyModel(CompanyModel companyModel) {
        this.companyModel = companyModel;
    }
}
