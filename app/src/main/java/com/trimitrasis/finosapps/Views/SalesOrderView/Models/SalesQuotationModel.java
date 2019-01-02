package com.trimitrasis.finosapps.Views.SalesOrderView.Models;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rahman on 03/05/2017.
 */

public class SalesQuotationModel implements Serializable {

    private static final long serialVersionUID = -5389394631390139661L;
    Object _id;
    String idSalesQuot;
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
    String transNo;
    List<DetailSalesQuotationModel> detailSalesQuotationModels;
    //ArrayList<ArrayList<DetailSalesQuotationModel>> detailSqArray;
    //DetailSalesQuotationModel detailSalesQuotationModel;

    public SalesQuotationModel(Object _id, String idSalesQuot, String customerId, String customerName, String branchId,
                               String branchName, String paymentTerm, String quoteDate, String validDate,
                               String tax, String deliveryFrom, String deliveryTo, String address,
                               String contactNumber, String notes, double amount, double discount,
                               String transNo){

            this._id = _id;
            this.idSalesQuot = idSalesQuot;
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
            this.address = address;
            this.contactNumber = contactNumber;
            this.notes = notes;
            this.amount = amount;
            this.discount = discount;
            this.transNo = transNo;
    }


    public Object get_id() {
        return _id;
    }

    public void set_id(Object _id) {
        this._id = _id;
    }

    public String getIdSalesQuot() {
        return idSalesQuot;
    }

    public void setIdSalesQuot(String idSalesQuot) {
        this.idSalesQuot = idSalesQuot;
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

    public String getTransNo() {
        return transNo;
    }

    public void setTransNo(String transNo) {
        this.transNo = transNo;
    }

}
