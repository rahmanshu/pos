package com.trimitrasis.finosapps.Views.HomeView;

import android.graphics.Typeface;

import java.io.Serializable;

/**
 * Created by rahman on 03/03/2017.
 */

public class SidebarObj implements Serializable {

    private static final long serialVersionUID = 2738153419337579218L;

    int resIcon;
    String label;
    Typeface typeface;

    public SidebarObj(int resIcon, String label, Typeface typeface) {
        this.resIcon = resIcon;
        this.label = label;
        this.setTypeface(typeface);
    }

    public Typeface getTypeface() {
        return typeface;
    }

    public void setTypeface(Typeface typeface) {
        this.typeface = typeface;
    }

    public int getResIcon() {
        return resIcon;
    }

    public void setResIcon(int resIcon) {
        this.resIcon = resIcon;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
