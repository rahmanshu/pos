package com.trimitrasis.finosapps.Views.PosView.CustomView;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.trimitrasis.finosapps.R;

/**
 * Created by rahman on 11/06/2017.
 */

public class ViewItemPrintPos extends LinearLayout {

    TextView textNamaBarang;
    TextView textQty;
    TextView textHargaJual;
    TextView textSubTotal;

    public ViewItemPrintPos(Context context) {
        super(context);
        init(0);
    }

    private void init(final int index){
        inflate(getContext(), R.layout.list_item_pos, this);
        this.textNamaBarang  = (TextView) findViewById(R.id.textNamaBarang);
        this.textQty         = (TextView) findViewById(R.id.textQty);
        this.textHargaJual   = (TextView) findViewById(R.id.textHargaJual);
        this.textSubTotal    = (TextView) findViewById(R.id.textSubTotal);
    }

    public TextView getTextNamaBarang() {
        return textNamaBarang;
    }

    public void setTextNamaBarang(String textNamaBarang) {
        this.textNamaBarang.setText(textNamaBarang);
    }

    public TextView getTextQty() {
        return textQty;
    }

    public void setTextQty(String textQty) {
        this.textQty.setText(textQty);
    }

    public TextView getTextHargaJual() {
        return textHargaJual;
    }

    public void setTextHargaJual(String textHargaJual) {
        this.textHargaJual.setText(textHargaJual);
    }

    public TextView getTextSubTotal() {
        return textSubTotal;
    }

    public void setTextSubTotal(String textSubTotal) {
        this.textSubTotal.setText(textSubTotal);
    }
}
