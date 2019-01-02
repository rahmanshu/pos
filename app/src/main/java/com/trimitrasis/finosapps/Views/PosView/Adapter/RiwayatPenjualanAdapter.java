package com.trimitrasis.finosapps.Views.PosView.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.trimitrasis.finosapps.R;
import com.trimitrasis.finosapps.Views.PosView.Model.RingkasanOrderModel;
import com.trimitrasis.finosapps.Views.PosView.Model.TransaksiPenjualanModel;
import com.trimitrasis.finosapps.Views.UtilView.FontUtils;
import com.trimitrasis.finosapps.Views.UtilView.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rahman on 19/04/2017.
 */

public class RiwayatPenjualanAdapter extends RecyclerView.Adapter<RiwayatPenjualanAdapter.ViewHolder> {

    Context context;
    List<TransaksiPenjualanModel> transaksiPenjualanModels = new ArrayList<>();


    public RiwayatPenjualanAdapter(Context context, ArrayList<TransaksiPenjualanModel> transaksiPenjualanModels){
        this.context = context;
        this.transaksiPenjualanModels = transaksiPenjualanModels;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_riwayat_jual,null);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }



    @Override
    public void onBindViewHolder(ViewHolder holder, final int position){
        View view = holder.view;

        ((TextView) view.findViewById(R.id.textTanggal)).setText(transaksiPenjualanModels.get(position).getTanggal());
        ((TextView) view.findViewById(R.id.textKasir)).setText(transaksiPenjualanModels.get(position).getId_kasir());
        ((TextView) view.findViewById(R.id.textIdJual)).setText(transaksiPenjualanModels.get(position).getId_jual());
        ((TextView) view.findViewById(R.id.textTotal)).setText(Utils.getCurrencyRupiahTanpaSimbol(transaksiPenjualanModels.get(position).getTotal_bayar()));
        ((TextView) view.findViewById(R.id.textIdJual)).setTypeface(FontUtils.getFontHeaderToolbar(context));
        ((TextView) view.findViewById(R.id.textTanggal)).setTypeface(FontUtils.getFontHeaderToolbar(context));
        ((TextView) view.findViewById(R.id.textKasir)).setTypeface(FontUtils.getFontHeaderToolbar(context));
        ((TextView) view.findViewById(R.id.textTotal)).setTypeface(FontUtils.getFontHeaderToolbar(context));

    }


    @Override
    public int getItemCount(){
        return transaksiPenjualanModels.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder{

        View view;

        public ViewHolder(View itemView){
            super(itemView);
            view = itemView;
        }

    }


}
