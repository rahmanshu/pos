package com.trimitrasis.finosapps.Views.HistoryView.Adapter;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.trimitrasis.finosapps.R;
import com.trimitrasis.finosapps.Views.PosView.Model.RingkasanOrderModel;
import com.trimitrasis.finosapps.Views.UtilView.FontUtils;
import com.trimitrasis.finosapps.Views.UtilView.Utils;
import java.util.ArrayList;

/**
 * Created by rahman on 10/26/2017.
 */

public class RiwayatDetailAdapter extends RecyclerView.Adapter<RiwayatDetailAdapter.ViewHolder>{

    ArrayList<RingkasanOrderModel> ringkasanOrderModels = new ArrayList<>();
    Context context;


    public RiwayatDetailAdapter(Context context, ArrayList<RingkasanOrderModel> ringkasanOrderModels){
        this.context = context;
        this.ringkasanOrderModels = ringkasanOrderModels;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_list_history_detail, null);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final View  view = holder.view;
        int qty = (int) ringkasanOrderModels.get(position).getQty();
        ((TextView) view.findViewById(R.id.textNamaBarang)).setText(ringkasanOrderModels.get(position).getNamaBarang());
        ((TextView) view.findViewById(R.id.textHarga)).setText(""+ Utils.getCurrencyRupiahTanpaSimbol(ringkasanOrderModels.get(position).getHargaJual()));
        ((Button) view.findViewById(R.id.btnQty)).setText(""+qty);
        ((TextView) view.findViewById(R.id.textNamaBarang)).setTypeface(FontUtils.getFontHeaderToolbar(context));
        ((TextView) view.findViewById(R.id.textHarga)).setTypeface(FontUtils.getFontHeaderToolbar(context));
    }


    @Override
    public int getItemCount() {
        return ringkasanOrderModels.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder{
        View view;
        public ViewHolder(View itemView){
            super(itemView);
            view = itemView;
        }
    }

}
