package com.trimitrasis.finosapps.Views.PosView.Model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by rahman on 30/03/2017.
 */

public class ProductModel implements Serializable {
    private static final long serialVersionUID = -2834341120214060609L;

    private String id;
    private String s_description;
    private String l_description;
    private String base_uom;
    private String item_group;
    private String hierarchy;
    private String account_class;
    private String create_by;
    private String create_date;
    private String create_time;
    List<ItemConvertionModel> itemConvertionModels;

    public ProductModel(String id, String s_description, String l_description, String base_uom, String item_group,
                        String hierarchy, String account_class, String create_by, String create_date, String create_time,
                        List<ItemConvertionModel> itemConvertionModels){
        this.id = id;
        this.s_description = s_description;
        this.l_description = l_description;
        this.base_uom = base_uom;
        this.item_group = item_group;
        this.hierarchy = hierarchy;
        this.account_class = account_class;
        this.create_by = create_by;
        this.create_date = create_date;
        this.create_time = create_time;
        this.itemConvertionModels = itemConvertionModels;
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

    public String getHierarchy() {
        return hierarchy;
    }

    public void setHierarchy(String hierarchy) {
        this.hierarchy = hierarchy;
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

    public List<ItemConvertionModel> getItemConvertionModels() {
        return itemConvertionModels;
    }

    public void setItemConvertionModels(List<ItemConvertionModel> itemConvertionModels) {
        this.itemConvertionModels = itemConvertionModels;
    }
}
