package com.trimitrasis.finosapps.Views.SalesQuotationView.Models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by rahman on 11/23/2017.
 */

public class CustomerModel_ implements Serializable {

    Object _id;
    String custId;
    String name;
    String phone1;
    String phone2;
    String address;
    String fax;
    String email;
    String recon_account;
    String currency;
    double credit_limit;
    String payment_terms;
    String tax_group;
    String pkp;
    List<BranchModel_> branchModelList;

    public CustomerModel_(Object _id, String custId, String name, String phone1, String phone2, String address, String fax,
                           String email, String recon_account, String currency, double credit_limit,
                           String payment_terms, String tax_group, String pkp, List<BranchModel_> branchModelList){

        this._id = _id;
        this.custId = custId;
        this.name = name;
        this.phone1 = phone1;
        this.phone2 = phone2;
        this.address = address;
        this.fax = fax;
        this.email = email;
        this.recon_account = recon_account;
        this.currency = currency;
        this.credit_limit = credit_limit;
        this.payment_terms = payment_terms;
        this.tax_group = tax_group;
        this.pkp = pkp;
        this.branchModelList = branchModelList;
    }


    public Object get_id() {
        return _id;
    }

    public void set_id(Object _id) {
        this._id = _id;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone1() {
        return phone1;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRecon_account() {
        return recon_account;
    }

    public void setRecon_account(String recon_account) {
        this.recon_account = recon_account;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public double getCredit_limit() {
        return credit_limit;
    }

    public void setCredit_limit(double credit_limit) {
        this.credit_limit = credit_limit;
    }

    public String getPayment_terms() {
        return payment_terms;
    }

    public void setPayment_terms(String payment_terms) {
        this.payment_terms = payment_terms;
    }

    public String getTax_group() {
        return tax_group;
    }

    public void setTax_group(String tax_group) {
        this.tax_group = tax_group;
    }

    public String getPkp() {
        return pkp;
    }

    public void setPkp(String pkp) {
        this.pkp = pkp;
    }

    public List<BranchModel_> getBranchModelList() {
        return branchModelList;
    }

    public void setBranchModelList(List<BranchModel_> branchModelList) {
        this.branchModelList = branchModelList;
    }
}
