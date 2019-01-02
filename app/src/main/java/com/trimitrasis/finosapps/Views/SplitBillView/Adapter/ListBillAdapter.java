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

public class ListBillAdapter extends RecyclerView.Adapter<ListBillAdapter.ViewHolder> {

    Context context;
    List<RingkasanOrderModel> ringkasanOrderModels = new ArrayList<>();

    CallbackListBill callbackListBill;

    public ListBillAdapter(Context context, ArrayList<RingkasanOrderModel> ringkasanOrderModels){
        this.context = context;
        this.ringkasanOrderModels = ringkasanOrderModels;
        callbackListBill = (CallbackListBill) context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(context).inflate(R.layout.row_list_data_bill, null);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final View view = holder.view;

        double totalHarga = ringkasanOrderModels.get(position).getHargaJual() * ringkasanOrderModels.get(position).getQty();
        ((TextView) view.findViewById(R.id.textNamaBarang)).setTypeface(FontUtils.getHelvetica_Neue_LT(context));
        ((TextView) view.findViewById(R.id.textNamaBarang)).setText(ringkasanOrderModels.get(position).getNamaBarang());
        ((TextView) view.findViewById(R.id.textHarga)).setTypeface(FontUtils.getHelvetica_Neue_LT(context));
        ((TextView) view.findViewById(R.id.textHarga)).setText(""+Utils.getCurrencyRupiahTanpaSimbol(totalHarga));
        int qtyBarang = (int) ringkasanOrderModels.get(position).getQty();
        ((Button) view.findViewById(R.id.btnQty)).setText(""+ qtyBarang);


        if(ringkasanOrderModels.get(position).getInfo().equals("buy x get y") || ringkasanOrderModels.get(position).getInfo().equals("set discount")){
            double diskon = ringkasanOrderModels.get(position).getDiskon();
            if(diskon != 0.0){
                ((TextView) view.findViewById(R.id.textDiskon)).setVisibility(View.VISIBLE);
                ((TextView) view.findViewById(R.id.labelDiskon)).setVisibility(View.VISIBLE);

                if (ringkasanOrderModels.get(position).getInfo().equals("buy x get y")) {
                    ((TextView) view.findViewById(R.id.labelDiskon)).setText("HADIAH");
                } else if (ringkasanOrderModels.get(position).getInfo().equals("set discount")) {
                    ((TextView) view.findViewById(R.id.labelDiskon)).setText("LEBIH HEMAT");
                }

                ((TextView) view.findViewById(R.id.textDiskon)).setText(Utils.getCurrencyRupiahTanpaSimbol(diskon));
                ((TextView) view.findViewById(R.id.labelDiskon)).setTypeface(FontUtils.getHelvetica_Neue_LT(context));
                ((TextView) view.findViewById(R.id.textDiskon)).setTypeface(FontUtils.getHelvetica_Neue_LT(context));
                ((TextView) view.findViewById(R.id.labelDiskon)).setBackgroundResource(R.color.gray_line);
                ((TextView) view.findViewById(R.id.textDiskon)).setBackgroundResource(R.color.gray_line);
            }
        }else{
            ((TextView) view.findViewById(R.id.labelDiskon)).setVisibility(View.GONE);
            ((TextView) view.findViewById(R.id.textDiskon)).setVisibility(View.GONE);
        }


        ((Button) view.findViewById(R.id.minusQty)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                getDataListBill_(getRingkasanOrderModel(position));
            }
        });
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

    public interface CallbackListBill{
        void getDataListBill(RingkasanOrderModel ringkasanOrderModel);
    }

    private void getDataListBill_(RingkasanOrderModel ringkasanOrderModel){
        callbackListBill.getDataListBill(ringkasanOrderModel);
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
                "update bill",
                ringkasanOrderModels.get(position).getDetailId()
        );

        return ringkasanOrderModel;
    }



}
