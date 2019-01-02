package com.trimitrasis.finosapps.Views.SplitBillView.Adapter;
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
import java.util.List;

/**
 * Created by rahman on 9/26/2017.
 */

public class ListPesananAdapter extends RecyclerView.Adapter<ListPesananAdapter.ViewHolder>{

    Context context;
    List<RingkasanOrderModel> ringkasanOrderModels = new ArrayList<>();
    CallbackListPesanan callbackListPesanan;


    public ListPesananAdapter(Context context, ArrayList<RingkasanOrderModel> ringkasanOrderModels){
        this.context = context;
        this.ringkasanOrderModels = ringkasanOrderModels;
        callbackListPesanan = (CallbackListPesanan) context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(context).inflate(R.layout.row_list_data_pesanan, null);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, final int position){
        final View view = holder.view;
        ((TextView) view.findViewById(R.id.textNamaBarang)).setTypeface(FontUtils.getHelvetica_Neue_LT(context));
        ((TextView) view.findViewById(R.id.textNamaBarang)).setText(ringkasanOrderModels.get(position).getNamaBarang());
        ((TextView) view.findViewById(R.id.textHarga)).setTypeface(FontUtils.getHelvetica_Neue_LT(context));
        ((TextView) view.findViewById(R.id.textHarga)).setText("" + Utils.getCurrencyRupiahTanpaSimbol(ringkasanOrderModels.get(position).getHargaJual()));
        int qtyBarang = (int) ringkasanOrderModels.get(position).getQty();
        ((Button) view.findViewById(R.id.btnQty)).setText("" + qtyBarang);

        ((Button) view.findViewById(R.id.minusQty)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                getDataListPesananBarang(getRingkasanOrderModel(position));
            }
        });
    }


    @Override
    public int getItemCount(){
        return ringkasanOrderModels.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        View view;
        public ViewHolder(View itemView){
            super(itemView);
            view = itemView;
        }
    }

    public interface CallbackListPesanan{
        void getDataListPesanan(RingkasanOrderModel ringkasanOrderModel);
    }

    private void getDataListPesananBarang(RingkasanOrderModel ringkasanOrderModel){
        callbackListPesanan.getDataListPesanan(ringkasanOrderModel);
    }

    private RingkasanOrderModel getRingkasanOrderModel(int position){
        RingkasanOrderModel ringkasanOrderModel = new RingkasanOrderModel(
                ringkasanOrderModels.get(position).getItemId(),
                ringkasanOrderModels.get(position).getKodeBarcode(),
                ringkasanOrderModels.get(position).getKodeBarang(),
                ringkasanOrderModels.get(position).getNamaBarang(),
                ringkasanOrderModels.get(position).getSatuanBarang(),
                ringkasanOrderModels.get(position).getHargaJual(),
                1.0,
                0.0,
                ringkasanOrderModels.get(position).getTax(),
                ringkasanOrderModels.get(position).getInfo(),
                ringkasanOrderModels.get(position).getStandart_cost(),
                ringkasanOrderModels.get(position).isFlag_qty(),
                ringkasanOrderModels.get(position).isFlag_bom(),
                ringkasanOrderModels.get(position).getTaxGroup(),
                "update pesanan",
                ringkasanOrderModels.get(position).getDetailId()
        );

        return ringkasanOrderModel;
    }

}
