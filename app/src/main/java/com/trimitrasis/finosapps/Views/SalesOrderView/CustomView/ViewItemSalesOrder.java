package com.trimitrasis.finosapps.Views.SalesOrderView.CustomView;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.trimitrasis.finosapps.R;
import com.trimitrasis.finosapps.Views.MasterDataView.Models.BranchModel;
import com.trimitrasis.finosapps.Views.SalesOrderView.Models.DetailSalesQuotationModel;

/**
 * Created by rahman on 04/05/2017.
 */

public class ViewItemSalesOrder extends LinearLayout {

    TextView textItemName;
    TextView textQty;
    TextView textTotal;
    TextView textDiscount;
    View lineItem;
    LinearLayout layoutDetailItem;

    CallbackDetailItem callbackDetailItem;

    public ViewItemSalesOrder(Context context, final DetailSalesQuotationModel salesQuotationModel, final int position){
        super(context);
        init(0, salesQuotationModel, position);
        callbackDetailItem = (CallbackDetailItem) context;
    }

    private void init(final int index, final DetailSalesQuotationModel salesQuotationModel, final int position){
        inflate(getContext(), R.layout.list_item_sales_order, this);
        this.textItemName = (TextView) findViewById(R.id.textItemName);
        this.textQty = (TextView) findViewById(R.id.textQty);
        this.textDiscount = (TextView) findViewById(R.id.textDiscount);
        this.textTotal = (TextView) findViewById(R.id.textTotal);
        this.lineItem = (View) findViewById(R.id.lineItem);
        this.layoutDetailItem = (LinearLayout) findViewById(R.id.layoutDetailItem);

        layoutDetailItem.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                getDataDetailItem(salesQuotationModel, position);
            }
        });
    }

    public interface CallbackDetailItem{
        void callDetailItem(DetailSalesQuotationModel detailSalesQuotationModel, int position);
    }

    private void getDataDetailItem(DetailSalesQuotationModel detailSalesQuotationModel, int position){
        callbackDetailItem.callDetailItem(detailSalesQuotationModel, position);
    }

    public TextView getTextItemName() {
        return textItemName;
    }

    public void setTextItemName(String textItemName) {
        this.textItemName.setText(textItemName);
    }

    public TextView getTextQty() {
        return textQty;
    }

    public void setTextQty(String textQty) {
        this.textQty.setText(textQty);
    }


    public TextView getTextTotal() {
        return textTotal;
    }

    public void setTextTotal(String textTotal) {
        this.textTotal.setText(textTotal);
    }

    public TextView getTextDiscount() {
        return textDiscount;
    }

    public void setTextDiscount(String textDiscount) {
        this.textDiscount.setText(textDiscount);
    }

    public View getLineItem() {
        return lineItem;
    }

    public void setLineItem(View lineItem) {
        this.lineItem = lineItem;
    }
}
