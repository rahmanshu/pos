package com.trimitrasis.finosapps.Views.PosView.Model;
import java.io.Serializable;

/**
 * Created by rahman on 30/03/2017.
 */

public class InternalProductModel implements Serializable {
    private static final long serialVersionUID = -4401152634436846853L;

    private String item_id;
    private String stock_id;
    private String description;
    private String base_uom;
    private String item_group;
    private String item_hierarchy;
    private String uom;
    private double uom_convertion;
    private double base_uom_convertion;
    private double harga_jual;
    private String barcode;
    private String tax_group;
    int imgProduk;
    double standart_cost;
    boolean flag_qty;
    boolean flag_bom;
    private String item_hierarchy_ancestor;
    private String file_ext;
    private String file_name;
    private String full_path;
    private String file_path;
    private String detail_id;



    public InternalProductModel(String item_id, String stock_id, String description, String base_uom, String item_group, String item_hierarchy,
                                String uom, double uom_convertion, double base_uom_convertion, double harga_jual, String barcode,
                                String tax_group, double standart_cost, boolean flag_qty, boolean flag_bom, String item_hierarchy_ancestor, int imageProduk,
                                String file_ext, String file_name, String full_path, String file_path, String detail_id){
        this.item_id = item_id;
        this.stock_id = stock_id;
        this.description = description;
        this.base_uom = base_uom;
        this.item_group = item_group;
        this.item_hierarchy = item_hierarchy;
        this.uom = uom;
        this.uom_convertion = uom_convertion;
        this.base_uom_convertion = base_uom_convertion;
        this.harga_jual = harga_jual;
        this.barcode = barcode;
        this.tax_group = tax_group;
        this.standart_cost = standart_cost;
        this.flag_qty = flag_qty;
        this.flag_bom = flag_bom;
        this.item_hierarchy_ancestor = item_hierarchy_ancestor;
        this.imgProduk = imageProduk;
        this.file_ext = file_ext;
        this.file_name = file_name;
        this.full_path = full_path;
        this.file_path = file_path;
        this.detail_id = detail_id;
    }


    public boolean isFlag_bom() {
        return flag_bom;
    }

    public void setFlag_bom(boolean flag_bom) {
        this.flag_bom = flag_bom;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getDetail_id() {
        return detail_id;
    }

    public void setDetail_id(String detail_id) {
        this.detail_id = detail_id;
    }

    public String getItem_hierarchy_ancestor() {
        return item_hierarchy_ancestor;
    }

    public void setItem_hierarchy_ancestor(String item_hierarchy_ancestor) {
        this.item_hierarchy_ancestor = item_hierarchy_ancestor;
    }

    public double getStandart_cost() {
        return standart_cost;
    }

    public void setStandart_cost(double standart_cost) {
        this.standart_cost = standart_cost;
    }

    public boolean isFlag_qty() {
        return flag_qty;
    }

    public void setFlag_qty(boolean flag_qty) {
        this.flag_qty = flag_qty;
    }

    public String getTax_group() {
        return tax_group;
    }

    public void setTax_group(String tax_group) {
        this.tax_group = tax_group;
    }

    public String getStock_id() {
        return stock_id;
    }

    public void setStock_id(String stock_id) {
        this.stock_id = stock_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public double getUom_convertion() {
        return uom_convertion;
    }

    public void setUom_convertion(double uom_convertion) {
        this.uom_convertion = uom_convertion;
    }

    public double getBase_uom_convertion() {
        return base_uom_convertion;
    }

    public void setBase_uom_convertion(double base_uom_convertion) {
        this.base_uom_convertion = base_uom_convertion;
    }

    public double getHarga_jual() {
        return harga_jual;
    }

    public void setHarga_jual(double harga_jual) {
        this.harga_jual = harga_jual;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public int getImgProduk() {
        return imgProduk;
    }

    public void setImgProduk(int imgProduk) {
        this.imgProduk = imgProduk;
    }

    public String getFile_ext() {
        return file_ext;
    }

    public void setFile_ext(String file_ext) {
        this.file_ext = file_ext;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getFull_path() {
        return full_path;
    }

    public void setFull_path(String full_path) {
        this.full_path = full_path;
    }

    public String getFile_path() {
        return file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }
}
