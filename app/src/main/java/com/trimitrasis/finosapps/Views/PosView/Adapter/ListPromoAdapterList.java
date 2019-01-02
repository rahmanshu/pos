package com.trimitrasis.finosapps.Views.PosView.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.trimitrasis.finosapps.R;
import com.trimitrasis.finosapps.Views.UtilView.FontUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rahman on 08/06/2017.
 */

public class ListPromoAdapterList extends RecyclerView.Adapter<ListPromoAdapterList.ViewHolder> {


    Context context;
    List<String> arrPromoBarang = new ArrayList<>();


    public ListPromoAdapterList(Context context, List<String> arrPromoBarang){
        this.context = context;
        this.arrPromoBarang = arrPromoBarang;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_list_data_promo, null);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ListPromoAdapterList.ViewHolder holder, int position){
        View view = holder.view;

        ((TextView) view.findViewById(R.id.textPromoBarang)).setTypeface(FontUtils.getHelvetica_Neue_LT(context));

        ((TextView) view.findViewById(R.id.textPromoBarang)).setText(arrPromoBarang.get(position).toString());

        /*
        String jenisPromo = arrPromoBarang.get(position).getJenisPromo();
        if(jenisPromo.equals("DISCOUNT")){

            ((TextView) view.findViewById(R.id.textPromoBarang)).setText("Dengan membeli " +
                    arrPromoBarang.get(position).getItemName() + " dengan jumlah " + arrPromoBarang.get(position).getQtyBarang()
                    + " " + arrPromoBarang.get(position).getSatuanbarang() + ", Anda lebih hemat sebesar " + Utils.getCurrencyRupiah(arrPromoBarang.get(position).getTotalDiskon()));

        }else if(jenisPromo.equals("BUY X GET Y")){

            String nama_barang_hadiah = " Anda mendapatkan " + arrPromoBarang.get(position).getItemNameHadiah() + " ";

            ((TextView) view.findViewById(R.id.textPromoBarang)).setText("Dengan membeli " +
                    arrPromoBarang.get(position).getItemName() + " dengan jumlah " + arrPromoBarang.get(position).getQtyBarang()
                    + " " + arrPromoBarang.get(position).getSatuanbarang() + nama_barang_hadiah +  arrPromoBarang.get(position).getQtyHadiah()
                    + " " +  arrPromoBarang.get(position).getSatuanHadiah());
        }
        */

    }


    @Override
    public int getItemCount() {
        return arrPromoBarang.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder{
        View view;
        public ViewHolder(View itemView){
            super(itemView);
            view = itemView;
        }
    }

}
