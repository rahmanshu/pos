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
 * Created by rahman on 10/25/2017.
 */

public class RiwayatPenjualanAdapterVoid extends RecyclerView.Adapter<RiwayatPenjualanAdapterVoid.ViewHolder>   {

    Context context;
    List<TransaksiPenjualanModel> transaksiPenjualanModels = new ArrayList<>();
    CallbackListRiwayatVoid callbackListRiwayatVoid;


    public RiwayatPenjualanAdapterVoid(Context context, ArrayList<TransaksiPenjualanModel> transaksiPenjualanModels){
        this.context = context;
        this.transaksiPenjualanModels = transaksiPenjualanModels;
        callbackListRiwayatVoid = (CallbackListRiwayatVoid) context;
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
        ((TextView) view.findViewById(R.id.textStatus)).setText("VOID");
        ((TextView) view.findViewById(R.id.textIdJual)).setTypeface(FontUtils.getFontHeaderToolbar(context));
        ((TextView) view.findViewById(R.id.textTanggal)).setTypeface(FontUtils.getFontHeaderToolbar(context));
        ((TextView) view.findViewById(R.id.textStatus)).setTypeface(FontUtils.getFontHeaderToolbar(context), Typeface.BOLD);

        view.findViewById(R.id.layoutItem).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                callRiwayatPenjualanVoid(transaksiPenjualanModels.get(position));
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


    public interface CallbackListRiwayatVoid{
        void showListRiwayatVoid(TransaksiPenjualanModel transaksiPenjualanModel);
    }

    private void callRiwayatPenjualanVoid(TransaksiPenjualanModel transaksiPenjualanModel){
        callbackListRiwayatVoid.showListRiwayatVoid(transaksiPenjualanModel);
    }



}
