package com.trimitrasis.finosapps.Views.MasterDataView.Models;

import com.trimitrasis.finosapps.Connection.Models.CompanyModel;

import java.io.Serializable;
import java.util.List;

/**
 * Created by rahman on 29/03/2017.
 */

public class ItemModel implements Serializable {

    private static final long serialVersionUID = -6450839818385474281L;
    private Object _id;
    private String id;
    private String s_description;
    private String l_description;
    private String baseUom;
    private String itemGroup;
    private String itemHierarchy;
    private String accountClass;
    private String itemConvertion;
    private String createBy;
    private String createDate;
    private String createTime;
    List<CompanyModel> companyModels;


    public ItemModel(Object _id, String id, String s_description, String l_description, String baseUom,
                    String itemGroup, String itemHierarchy, String accountClass, String itemConvertion, String createBy,
                    String createDate, String createTime, List<CompanyModel> companyModels){

        this._id = _id;
        this.id = id;
        this.s_description = s_description;
        this.l_description = l_description;
        this.baseUom = baseUom;
        this.itemGroup = itemGroup;
        this.itemHierarchy = itemHierarchy;
        this.accountClass = accountClass;
        this.itemConvertion = itemConvertion;
        this.createBy = createBy;
        this.createDate = createDate;
        this.createTime = createTime;
        this.companyModels = companyModels;
    }


    public ItemModel(Object _id, String id, String s_description, String l_description, String baseUom,
                     String itemGroup, String itemHierarchy, String accountClass, String itemConvertion, String createBy,
                     String createDate, String createTime){

        this._id = _id;
        this.id = id;
        this.s_description = s_description;
        this.l_description = l_description;
        this.baseUom = baseUom;
        this.itemGroup = itemGroup;
        this.itemHierarchy = itemHierarchy;
        this.accountClass = accountClass;
        this.itemConvertion = itemConvertion;
        this.createBy = createBy;
        this.createDate = createDate;
        this.createTime = createTime;
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

    public String getS_description() {
        return s_description;
    }

    public void setS_description(String s_description) {
        this.s_description = s_description;
    }

    public String getL_description() {
        return l_description;
    }

    public void setL_description(String l_description) {
        this.l_description = l_description;
    }

    public String getBaseUom() {
        return baseUom;
    }

    public void setBaseUom(String baseUom) {
        this.baseUom = baseUom;
    }

    public String getItemGroup() {
        return itemGroup;
    }

    public void setItemGroup(String itemGroup) {
        this.itemGroup = itemGroup;
    }

    public String getItemHierarchy() {
        return itemHierarchy;
    }

    public void setItemHierarchy(String itemHierarchy) {
        this.itemHierarchy = itemHierarchy;
    }

    public String getAccountClass() {
        return accountClass;
    }

    public void setAccountClass(String accountClass) {
        this.accountClass = accountClass;
    }

    public String getItemConvertion() {
        return itemConvertion;
    }

    public void setItemConvertion(String itemConvertion) {
        this.itemConvertion = itemConvertion;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public List<CompanyModel> getCompanyModels() {
        return companyModels;
    }

    public void setCompanyModels(List<CompanyModel> companyModels) {
        this.companyModels = companyModels;
    }
}
