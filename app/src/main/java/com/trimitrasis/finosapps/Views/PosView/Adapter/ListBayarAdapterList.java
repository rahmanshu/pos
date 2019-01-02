package com.trimitrasis.finosapps.Views.PosView.Adapter;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.trimitrasis.finosapps.R;
import com.trimitrasis.finosapps.Views.PosView.Model.RingkasanOrderModel;
import com.trimitrasis.finosapps.Views.UtilView.FontUtils;
import com.trimitrasis.finosapps.Views.UtilView.Utils;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rahman on 30/03/2017.
 */

public class ListBayarAdapterList extends RecyclerView.Adapter<ListBayarAdapterList.ViewHolder>{

    Context context;
    List<RingkasanOrderModel> ringkasanOrderModels = new ArrayList<>();
    CallbackListOrderTab callbackListOrderTab;


    public ListBayarAdapterList(Context context, ArrayList<RingkasanOrderModel> ringkasanOrderModels){
        this.context = context;
        this.ringkasanOrderModels = ringkasanOrderModels;
        callbackListOrderTab = (CallbackListOrderTab) context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(context).inflate(R.layout.row_list_data_order, null);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, final int position){
        View view = holder.view;
        ((TextView) view.findViewById(R.id.textHarga)).setTypeface(FontUtils.getHelvetica_Neue_LT(context), Typeface.BOLD);

        double totalHarga = ringkasanOrderModels.get(position).getHargaJual() * ringkasanOrderModels.get(position).getQty();
        ((TextView) view.findViewById(R.id.textHarga)).setText(Utils.getCurrencyRupiahTanpaSimbol(totalHarga));
        int qtyInt = (int) ringkasanOrderModels.get(position).getQty();
        ((TextView) view.findViewById(R.id.textQty)).setText(" X " + qtyInt);

        if(ringkasanOrderModels.get(position).getInfo().equals("buy x get y") || ringkasanOrderModels.get(position).getInfo().equals("set discount")){
            ((TextView) view.findViewById(R.id.textNamaBarang)).setText(ringkasanOrderModels.get(position).getNamaBarang());

            double diskon = ringkasanOrderModels.get(position).getDiskon();
            ((TextView) view.findViewById(R.id.labelDiskon)).setVisibility(View.VISIBLE);
            ((TextView) view.findViewById(R.id.textDiskon)).setVisibility(View.VISIBLE);
            ((TextView) view.findViewById(R.id.textEmpty)).setVisibility(View.VISIBLE);

            if(ringkasanOrderModels.get(position).getInfo().equals("buy x get y")){
                ((TextView) view.findViewById(R.id.labelDiskon)).setText("HADIAH");
            }else if(ringkasanOrderModels.get(position).getInfo().equals("set discount")){
                ((TextView) view.findViewById(R.id.labelDiskon)).setText("LEBIH HEMAT");
            }

            ((TextView) view.findViewById(R.id.textDiskon)).setText(Utils.getCurrencyRupiahTanpaSimbol(diskon));
            ((TextView) view.findViewById(R.id.labelDiskon)).setTypeface(FontUtils.getHelvetica_Neue_LT(context));
            ((TextView) view.findViewById(R.id.textDiskon)).setTypeface(FontUtils.getHelvetica_Neue_LT(context));
            ((TextView) view.findViewById(R.id.labelDiskon)).setBackgroundResource(R.color.gray_line);
            ((TextView) view.findViewById(R.id.textDiskon)).setBackgroundResource(R.color.gray_line);

        }else{
            ((TextView) view.findViewById(R.id.textNamaBarang)).setText(ringkasanOrderModels.get(position).getNamaBarang());
            ((TextView) view.findViewById(R.id.labelDiskon)).setVisibility(View.GONE);
            ((TextView) view.findViewById(R.id.textDiskon)).setVisibility(View.GONE);
            ((TextView) view.findViewById(R.id.textEmpty)).setVisibility(View.GONE);
        }


        view.findViewById(R.id.rowLayout).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                getRingkasanOrder(getRingkasanOrderModel(position));
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

    public interface CallbackListOrderTab{
        void getListOrderTab(RingkasanOrderModel ringkasanOrderModel);
    }

    private void getRingkasanOrder(RingkasanOrderModel ringkasanOrderModel){
        callbackListOrderTab.getListOrderTab(ringkasanOrderModel);
    }


    private RingkasanOrderModel getRingkasanOrderModel(int position){
        RingkasanOrderModel ringkasanOrderModel = new RingkasanOrderModel(
                ringkasanOrderModels.get(position).getItemId(),
                ringkasanOrderModels.get(position).getKodeBarcode(),
                ringkasanOrderModels.get(position).getKodeBarang(),
                ringkasanOrderModels.get(position).getNamaBarang(),
                ringkasanOrderModels.get(position).getSatuanBarang(),
                ringkasanOrderModels.get(position).getHargaJual(),
                ringkasanOrderModels.get(position).getQty(),
                ringkasanOrderModels.get(position).getDiskon(),
                ringkasanOrderModels.get(position).getTax(),
                ringkasanOrderModels.get(position).getInfo(),
                ringkasanOrderModels.get(position).getStandart_cost(),
                ringkasanOrderModels.get(position).isFlag_qty(),
                ringkasanOrderModels.get(position).isFlag_bom(),
                ringkasanOrderModels.get(position).getTaxGroup(),
                ringkasanOrderModels.get(position).getNote(),
                ringkasanOrderModels.get(position).getDetailId()
        );

        return ringkasanOrderModel;
    }


}
