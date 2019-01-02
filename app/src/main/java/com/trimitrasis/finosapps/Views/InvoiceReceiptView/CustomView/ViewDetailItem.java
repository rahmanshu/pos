package com.trimitrasis.finosapps.Views.InvoiceReceiptView.CustomView;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.trimitrasis.finosapps.R;

/**
 * Created by rahman on 02/03/2017.
 */

public class ViewDetailItem extends LinearLayout{

    TextView textQty;
    TextView textItemId;
    TextView textPrice;
    View lineDetailItem;

    public ViewDetailItem(Context context) {
        super(context);
        init(0);
    }

    private void init(final int index){

        inflate(getContext(), R.layout.layout_detail_item, this);
        this.textQty = (TextView) findViewById(R.id.textQty);
        this.textItemId = (TextView) findViewById(R.id.textItemId);
        this.textPrice = (TextView) findViewById(R.id.textPrice);
        this.lineDetailItem = (View) findViewById(R.id.lineDetailItem);

    }

    public TextView getTextQty() {
        return textQty;
    }

    public void setTextQty(String textQty) {
        this.textQty.setText(textQty);
    }

    public TextView getTextItemId() {
        return textItemId;
    }

    public void setTextItemId(String textItemId) {
        this.textItemId.setText(textItemId);
    }

    public TextView getTextPrice() {
        return textPrice;
    }

    public void setTextPrice(String textPrice) {
        this.textPrice.setText(textPrice);
    }

    public View getLineDetailItem() {
        return lineDetailItem;
    }

    public void setLineDetailItem(View lineDetailItem) {
        this.lineDetailItem = lineDetailItem;
    }
}
