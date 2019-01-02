package com.trimitrasis.finosapps.Views.PosView.Adapter;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.trimitrasis.finosapps.R;
import com.trimitrasis.finosapps.Views.PosView.Model.HeaderHoldSalesModel;
import com.trimitrasis.finosapps.Views.UtilView.Utils;
import java.util.ArrayList;

/**
 * Created by rahman on 09/06/2017.
 */

public class ListHoldSalesAdapter extends RecyclerView.Adapter<ListHoldSalesAdapter.ViewHolder>{

    ArrayList<HeaderHoldSalesModel> headerHoldSalesModels = new ArrayList<>();
    Context context;
    CallbackHoldSales callbackHoldSales;
    CallbackCetakPesanan callbackCetakPesanan;


    public ListHoldSalesAdapter(Context context, ArrayList<HeaderHoldSalesModel> headerHoldSalesModels){
        this.context = context;
        this.headerHoldSalesModels = headerHoldSalesModels;
        callbackHoldSales = (CallbackHoldSales) context;
        callbackCetakPesanan = (CallbackCetakPesanan) context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_hold_sales, null);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final View view = holder.view;

        ((TextView) view.findViewById(R.id.textNomerMeja)).setText(headerHoldSalesModels.get(position).getNomo_meja());
        ((TextView) view.findViewById(R.id.textKasir)).setText(headerHoldSalesModels.get(position).getKasir());
        ((TextView) view.findViewById(R.id.textTanggal)).setText(headerHoldSalesModels.get(position).getTanggal());
        ((TextView) view.findViewById(R.id.textTotal)).setText(Utils.getCurrencyRupiahTanpaSimbol(headerHoldSalesModels.get(position).getTotal() + headerHoldSalesModels.get(position).getPajak_ppn()));
        ((TextView) view.findViewById(R.id.textIdJual)).setText(headerHoldSalesModels.get(position).getId_jual());

        view.findViewById(R.id.btnCetak).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                cetakPesanan(getHoldSalesModel(position));
            }
        });

        view.findViewById(R.id.btnDetail).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                getHoldSales(getHoldSalesModel(position));
            }
        });
    }


    @Override
    public int getItemCount() {
        return headerHoldSalesModels.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        View view;
        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
        }
    }

    public interface CallbackHoldSales{
        void getDataHoldSales(HeaderHoldSalesModel holdSalesModel);
    }

    private void getHoldSales(HeaderHoldSalesModel holdSalesModel){
        callbackHoldSales.getDataHoldSales(holdSalesModel);
    }

    public interface CallbackCetakPesanan{
        void getCetakPesanan(HeaderHoldSalesModel holdSalesModel);
    }

    private void cetakPesanan(HeaderHoldSalesModel holdSalesModel){
        callbackCetakPesanan.getCetakPesanan(holdSalesModel);
    }

    private HeaderHoldSalesModel getHoldSalesModel(int position){
        HeaderHoldSalesModel holdSalesModel = new HeaderHoldSalesModel(
                headerHoldSalesModels.get(position).getId_jual(),
                headerHoldSalesModels.get(position).getTanggal(),
                headerHoldSalesModels.get(position).getTotal(),
                headerHoldSalesModels.get(position).getKasir(),
                headerHoldSalesModels.get(position).getNomo_meja(),
                headerHoldSalesModels.get(position).getJenis_pajak(),
                headerHoldSalesModels.get(position).getPajak_ppn()
        );

        return holdSalesModel;
    }


}
