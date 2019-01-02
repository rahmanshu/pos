package com.trimitrasis.finosapps.Views.SalesQuotationView.CustomView;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.trimitrasis.finosapps.R;

/**
 * Created by rahman on 06/03/2017.
 */

public class ViewItemSalesQuotation extends LinearLayout {

    TextView textItemName;
    TextView textUom;
    TextView textTotal;
    View lineItem;

    public ViewItemSalesQuotation(Context context){
        super(context);
        init(0);
    }


    private void init(final int index){
        inflate(getContext(), R.layout.list_item_sales_quotation, this);
        this.textItemName = (TextView) findViewById(R.id.textItemName);
        this.textUom = (TextView) findViewById(R.id.textUom);
        this.textTotal = (TextView) findViewById(R.id.textTotal);
        this.lineItem = (View) findViewById(R.id.lineItem);
    }

    public TextView getTextItemName(){
        return textItemName;
    }

    public void setTextItemName(String textItemName){
        this.textItemName.setText(textItemName);
    }

    public TextView getTextUom() {
        return textUom;
    }

    public void setTextUom(String textUom) {
        this.textUom.setText(textUom);
    }

    public TextView getTextTotal() {
        return textTotal;
    }

    public void setTextTotal(String textTotal) {
        this.textTotal.setText(textTotal);
    }

    public View getLineItem() {
        return lineItem;
    }

    public void setLineItem(View lineItem) {
        this.lineItem = lineItem;
    }


}
