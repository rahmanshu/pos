package com.trimitrasis.finosapps.Views.InvoiceReceiptView.InputItemView.Adapter;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.trimitrasis.finosapps.R;
import com.trimitrasis.finosapps.Views.InvoiceReceiptView.InputItemView.Models.BillingItemModel;
import java.util.ArrayList;

/**
 * Created by rahman on 07/03/2017.
 */

public class BillingItemAdapter extends RecyclerView.Adapter<BillingItemAdapter.ViewHolder> {

    ArrayList<BillingItemModel> billingItemModels = new ArrayList<>();
    Context context;
    private CallbackBillingItem callbackBillingItem;

    public BillingItemAdapter(Context context, ArrayList<BillingItemModel> billingItemModels){
        this.context = context;
        this.billingItemModels = billingItemModels;
        callbackBillingItem    = (CallbackBillingItem) context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_bill_item, null);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final View view = holder.view;
        ((TextView) view.findViewById(R.id.kodeItem)).setText(billingItemModels.get(position).getId());
        ((TextView) view.findViewById(R.id.itemDesc)).setText(billingItemModels.get(position).getDesc());
        ((TextView) view.findViewById(R.id.itemPrice)).setText(billingItemModels.get(position).getPrice());

        view.findViewById(R.id.layoutItem).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                getBilingItem(billingItemModels.get(position).getId(), billingItemModels.get(position).getPrice());
            }
        });
    }


    @Override
    public int getItemCount() {
        return billingItemModels.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        View view;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
        }
    }

    public interface CallbackBillingItem{
        void getDataBillItem(String kodeBarang, String hargaBarang);
    }

    private void getBilingItem(String kodeBarang, String hargabarang){
        callbackBillingItem.getDataBillItem(kodeBarang, hargabarang);
    }

}
