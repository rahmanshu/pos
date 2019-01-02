package com.trimitrasis.finosapps.Views.PosView.Adapter;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.trimitrasis.finosapps.R;
import com.trimitrasis.finosapps.Views.PosView.Model.RingkasanOrderModel;
import java.util.ArrayList;


/**
 * Created by rahman on 09/06/2017.
 */

public class ListDetailHoldSalesAdapter  extends RecyclerView.Adapter<ListDetailHoldSalesAdapter.ViewHolder> {


    ArrayList<RingkasanOrderModel> ringkasanOrderModels = new ArrayList<>();
    Context context;


    public ListDetailHoldSalesAdapter(Context context, ArrayList<RingkasanOrderModel> ringkasanOrderModels){
        this.context = context;
        this.ringkasanOrderModels = ringkasanOrderModels;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_hold_sales_barang, null);
        ViewHolder vh = new  ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final View view = holder.view;
        ((TextView) view.findViewById(R.id.textNamaBarang)).setText("Nama barang - " + ringkasanOrderModels.get(position).getNamaBarang());
        ((TextView) view.findViewById(R.id.textKodeBarang)).setText("Kode Barang - " + ringkasanOrderModels.get(position).getKodeBarang());
        ((TextView) view.findViewById(R.id.textQty)).setText(ringkasanOrderModels.get(position).getQty() + " / " + ringkasanOrderModels.get(position).getSatuanBarang());
        ((TextView) view.findViewById(R.id.textHarga)).setText(""+ringkasanOrderModels.get(position).getHargaJual());
    }


    @Override
    public int getItemCount() {
        return ringkasanOrderModels.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        View view;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
        }
    }



}
