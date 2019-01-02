package com.trimitrasis.finosapps.Views.PosView.Model;

import java.io.Serializable;

/**
 * Created by rahman on 20/06/2017.
 */

public class ListCategoryModel implements Serializable {

    private static final long serialVersionUID = -7909481943671660558L;

    String idCategory;
    String nameCategory;


    public ListCategoryModel(String idCategory, String nameCategory){
        this.idCategory = idCategory;
        this.nameCategory = nameCategory;
    }

    public String getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(String idCategory) {
        this.idCategory = idCategory;
    }

    public String getNameCategory() {
        return nameCategory;
    }

    public void setNameCategory(String nameCategory) {
        this.nameCategory = nameCategory;
    }
}
