package com.trimitrasis.finosapps.Views.MasterDataView.Models;

import java.io.Serializable;

/**
 * Created by rahman on 10/03/2017.
 */

public class BranchModel  implements Serializable{

    private static final long serialVersionUID = -5071692230431937441L;
    private Object _id;
    private String customerName;
    private String name;
    private String address;
    private String phone1;
    private String phone2;
    private String fax;
    private String email;

    public BranchModel(Object _id, String name, String address, String phone1, String phone2, String fax, String email){
        set_id(_id);
        setName(name);
        setAddress(address);
        setPhone1(phone1);
        setPhone2(phone2);
        setFax(fax);
        setEmail(email);
    }


    public BranchModel(Object _id, String customerName, String name, String address, String phone1, String phone2, String fax, String email){
        set_id(_id);
        setCustomerName(customerName);
        setName(name);
        setAddress(address);
        setPhone1(phone1);
        setPhone2(phone2);
        setFax(fax);
        setEmail(email);
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Object get_id() {
        return _id;
    }

    public void set_id(Object _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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
}
