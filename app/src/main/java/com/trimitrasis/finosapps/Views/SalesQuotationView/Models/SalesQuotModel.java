package com.trimitrasis.finosapps.Views.SalesQuotationView.Models;

import com.trimitrasis.finosapps.Connection.Models.CompanyModel;
import com.trimitrasis.finosapps.Connection.Models.UserInfoModel;

import java.io.Serializable;
import java.util.List;

/**
 * Created by rahman on 02/05/2017.
 */

public class SalesQuotModel implements Serializable {

    String customerId;
    String customerName;
    String branchId;
    String branchName;
    String paymentTerm;
    String quoteDate;
    String validDate;
    String tax;
    String deliveryFrom;
    String deliveryTo;
    String address;
    String contactNumber;
    String notes;
    double amount;
    double discount;
    String createBy;
    String createDate;
    String createTime;
    String transNo;
    List<DetailItemSqModel> detailItemSqModels;
    UserInfoModel userInfoModel;


    public SalesQuotModel(String customerId, String customerName, String branchId, String branchName,
                          String paymentTerm, String quoteDate, String validDate, String tax, String deliveryFrom,
                          String deliveryTo, String address, String contactNumber, String notes, double amount,
                          double discount, String createBy, String createDate, String createTime, String transNo,
                          List<DetailItemSqModel> detailItemSqModels, UserInfoModel userInfoModel){

            this.customerId = customerId;
            this.customerName = customerName;
            this.branchId = branchId;
            this.branchName = branchName;
            this.paymentTerm = paymentTerm;
            this.quoteDate = quoteDate;
            this.validDate = validDate;
            this.tax = tax;
            this.deliveryFrom = deliveryFrom;
            this.deliveryTo = deliveryTo;
            this.discount = discount;
            this.address = address;
            this.contactNumber = contactNumber;
            this.notes = notes;
            this.amount = amount;
            this.createBy = createBy;
            this.createDate = createDate;
            this.createTime = createTime;
            this.transNo = transNo;
            this.detailItemSqModels = detailItemSqModels;
            this.userInfoModel = userInfoModel;
    }

    public UserInfoModel getUserInfoModel() {
        return userInfoModel;
    }

    public void setUserInfoModel(UserInfoModel userInfoModel) {
        this.userInfoModel = userInfoModel;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getPaymentTerm() {
        return paymentTerm;
    }

    public void setPaymentTerm(String paymentTerm) {
        this.paymentTerm = paymentTerm;
    }

    public String getQuoteDate() {
        return quoteDate;
    }

    public void setQuoteDate(String quoteDate) {
        this.quoteDate = quoteDate;
    }

    public String getValidDate() {
        return validDate;
    }

    public void setValidDate(String validDate) {
        this.validDate = validDate;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getDeliveryFrom() {
        return deliveryFrom;
    }

    public void setDeliveryFrom(String deliveryFrom) {
        this.deliveryFrom = deliveryFrom;
    }

    public String getDeliveryTo() {
        return deliveryTo;
    }

    public void setDeliveryTo(String deliveryTo) {
        this.deliveryTo = deliveryTo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
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

    public String getTransNo() {
        return transNo;
    }

    public void setTransNo(String transNo) {
        this.transNo = transNo;
    }

    public List<DetailItemSqModel> getDetailItemSqModels() {
        return detailItemSqModels;
    }

    public void setDetailItemSqModels(List<DetailItemSqModel> detailItemSqModels) {
        this.detailItemSqModels = detailItemSqModels;
    }
}
