package com.trimitrasis.finosapps.Views.MasterDataView.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.trimitrasis.finosapps.R;
import com.trimitrasis.finosapps.Views.MasterDataView.Models.MainCurrencyModel;
import java.util.ArrayList;

/**
 * Created by rahman on 16/03/2017.
 */

public class MainCurrencyAdapter extends RecyclerView.Adapter<MainCurrencyAdapter.ViewHolder>{

    ArrayList<MainCurrencyModel> mainCurrencyModels = new ArrayList<>();
    Context context;


    public MainCurrencyAdapter(Context context, ArrayList<MainCurrencyModel> mainCurrencyModels){
        this.context = context;
        this.mainCurrencyModels = mainCurrencyModels;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_main_currency, null);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final View view = holder.view;
        ((TextView) view.findViewById(R.id.textCurrency)).setText(mainCurrencyModels.get(position).getCurrency());
        ((TextView) view.findViewById(R.id.textDescription)).setText(mainCurrencyModels.get(position).getDescription());
        ((TextView) view.findViewById(R.id.textSymbol)).setText(mainCurrencyModels.get(position).getSymbol());
    }


    @Override
    public int getItemCount() {
        return mainCurrencyModels.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        View view;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
        }
    }





}
