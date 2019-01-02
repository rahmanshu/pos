package com.trimitrasis.finosapps.Views.MasterDataView.Models;
import com.trimitrasis.finosapps.Connection.Models.CompanyModel;
import com.trimitrasis.finosapps.Connection.Models.UserInfoModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rahman on 10/03/2017.
 */

public class CustomerModel implements Serializable {

    private static final long serialVersionUID = -2464686137264868127L;

    private Object _id;
    private String name;
    private String phone1;
    private String phone2;
    private String address;
    private String fax;
    private String email;
    private String recon_account;
    private String currency;
    private double credit_limit;
    private String payment_terms;
    private boolean pkp;
    private String tax_group;
    List<BranchModel> branchModels;
    UserInfoModel userInfoModel;


    public CustomerModel(Object _id, String name, String phone1, String phone2, String address, String fax,
                         String email, String recon_account, String currency, double credit_limit,
                         String payment_terms, boolean pkp, String tax_group, List<BranchModel> branchModels, UserInfoModel userInfoModel){

        this._id = _id;
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
        this.pkp = pkp;
        this.tax_group = tax_group;
        this.branchModels = branchModels;
        this.userInfoModel = userInfoModel;
    }


    public CustomerModel(Object _id, String name, String phone1, String phone2, String address, String fax,
                         String email, String recon_account, String currency, double credit_limit,
                         String payment_terms, boolean pkp, String tax_group){

        this._id = _id;
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
        this.pkp = pkp;
        this.tax_group = tax_group;
    }

    public CustomerModel(List<BranchModel> branchModels){
        this.branchModels = branchModels;
    }

    public CustomerModel(Object _id, String name, String phone1, String phone2, String address, String fax,
                         String email, String recon_account, String currency, double credit_limit,
                         String payment_terms, boolean pkp, List<BranchModel> branchModels){

        this._id = _id;
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
        this.pkp = pkp;
        this.branchModels = branchModels;
    }

    public UserInfoModel getUserInfoModel() {
        return userInfoModel;
    }

    public void setUserInfoModel(UserInfoModel userInfoModel) {
        this.userInfoModel = userInfoModel;
    }

    public String getName(){
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

    public String getPayment_term() {
        return payment_terms;
    }

    public void setPayment_term(String payment_terms) {
        this.payment_terms = payment_terms;
    }

    public boolean isPkp() {
        return pkp;
    }

    public void setPkp(boolean pkp) {
        this.pkp = pkp;
    }


    public Object get_id() {
        return _id;
    }

    public void set_id(Object _id) {
        this._id = _id;
    }

    public String getPayment_terms() {
        return payment_terms;
    }

    public void setPayment_terms(String payment_terms) {
        this.payment_terms = payment_terms;
    }


    public List<BranchModel> getBranchModels() {
        return branchModels;
    }

    public void setBranchModels(List<BranchModel> branchModels) {
        this.branchModels = branchModels;
    }

    public String getTax_group() {
        return tax_group;
    }

    public void setTax_group(String tax_group) {
        this.tax_group = tax_group;
    }
}
