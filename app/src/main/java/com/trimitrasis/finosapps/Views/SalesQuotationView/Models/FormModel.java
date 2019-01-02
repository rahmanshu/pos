package com.trimitrasis.finosapps.Views.SalesQuotationView.Models;
import com.trimitrasis.finosapps.Connection.Models.CompanyModel;
import java.io.Serializable;

/**
 * Created by rahman on 15/03/2017.
 */

public class FormModel implements Serializable{

    String object;
    CompanyModel companyModel;

    public FormModel(String object, CompanyModel companyModel){
        this.object = object;
        this.companyModel = companyModel;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public CompanyModel getCompanyModel() {
        return companyModel;
    }

    public void setCompanyModel(CompanyModel companyModel) {
        this.companyModel = companyModel;
    }
}
