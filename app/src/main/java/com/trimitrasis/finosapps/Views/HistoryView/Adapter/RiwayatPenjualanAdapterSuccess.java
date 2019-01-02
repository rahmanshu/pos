package com.trimitrasis.finosapps.Views.HistoryView.Adapter;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.trimitrasis.finosapps.R;
import com.trimitrasis.finosapps.Views.PosView.Model.TransaksiPenjualanModel;
import com.trimitrasis.finosapps.Views.UtilView.FontUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rahman on 10/24/2017.
 */

public class RiwayatPenjualanAdapterSuccess extends RecyclerView.Adapter<RiwayatPenjualanAdapterSuccess.ViewHolder>  {

    Context context;
    List<TransaksiPenjualanModel> transaksiPenjualanModels = new ArrayList<>();
    CallbackListRiwayatSuccess callbackListRiwayat;


    public RiwayatPenjualanAdapterSuccess(Context context, ArrayList<TransaksiPenjualanModel> transaksiPenjualanModels){
        this.context = context;
        this.transaksiPenjualanModels = transaksiPenjualanModels;
        callbackListRiwayat = (CallbackListRiwayatSuccess) context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_riwayat_jual_new,null);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }



    @Override
    public void onBindViewHolder(ViewHolder holder, final int position){
        View view = holder.view;

        ((TextView) view.findViewById(R.id.textTanggal)).setText(transaksiPenjualanModels.get(position).getTanggal());
        ((TextView) view.findViewById(R.id.textIdJual)).setText(transaksiPenjualanModels.get(position).getId_jual());
        ((TextView) view.findViewById(R.id.textStatus)).setText("SUCCESS");
        ((TextView) view.findViewById(R.id.textIdJual)).setTypeface(FontUtils.getFontHeaderToolbar(context));
        ((TextView) view.findViewById(R.id.textTanggal)).setTypeface(FontUtils.getFontHeaderToolbar(context));
        ((TextView) view.findViewById(R.id.textStatus)).setTypeface(FontUtils.getFontHeaderToolbar(context), Typeface.BOLD);

        view.findViewById(R.id.layoutItem).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                callRiwayatPenjualan(transaksiPenjualanModels.get(position));
            }
        });

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


    public interface CallbackListRiwayatSuccess{
        void showListRiwayatSuccess(TransaksiPenjualanModel transaksiPenjualanModel);
    }

    private void callRiwayatPenjualan(TransaksiPenjualanModel transaksiPenjualanModel){
        callbackListRiwayat.showListRiwayatSuccess(transaksiPenjualanModel);
    }


}
