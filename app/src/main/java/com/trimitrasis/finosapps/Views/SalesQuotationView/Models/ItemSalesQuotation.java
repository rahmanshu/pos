package com.trimitrasis.finosapps.Views.SalesQuotationView.Models;

import java.io.Serializable;

/**
 * Created by rahman on 20/04/2017.
 */

public class ItemSalesQuotation implements Serializable {
    private static final long serialVersionUID = -6242521038016288140L;

    String id;
    String s_description;
    String l_description;
    String base_uom;
    String item_group;
    String item_hierarchy;
    String account_class;
    String create_by;
    String create_date;
    String create_time;
    String tax_group;

    public ItemSalesQuotation(String id,
            String s_description,
            String l_description,
            String base_uom,
            String item_group,
            String item_hierarchy,
            String account_class,
            String create_by,
            String create_date,
            String create_time,
            String tax_group){

        this.id = id;
        this.s_description = s_description;
        this.l_description = l_description;
        this.base_uom = base_uom;
        this.item_group = item_group;
        this.item_hierarchy = item_hierarchy;
        this.account_class = account_class;
        this.create_by = create_by;
        this.create_date = create_date;
        this.create_time = create_time;
        this.tax_group = tax_group;
    }

    public String getTax_group() {
        return tax_group;
    }

    public void setTax_group(String tax_group) {
        this.tax_group = tax_group;
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

    public String getBase_uom() {
        return base_uom;
    }

    public void setBase_uom(String base_uom) {
        this.base_uom = base_uom;
    }

    public String getItem_group() {
        return item_group;
    }

    public void setItem_group(String item_group) {
        this.item_group = item_group;
    }

    public String getItem_hierarchy() {
        return item_hierarchy;
    }

    public void setItem_hierarchy(String item_hierarchy) {
        this.item_hierarchy = item_hierarchy;
    }

    public String getAccount_class() {
        return account_class;
    }

    public void setAccount_class(String account_class) {
        this.account_class = account_class;
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
