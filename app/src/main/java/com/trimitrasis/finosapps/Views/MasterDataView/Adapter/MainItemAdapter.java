package com.trimitrasis.finosapps.Views.MasterDataView.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.trimitrasis.finosapps.R;
import com.trimitrasis.finosapps.Views.MasterDataView.Models.ItemModel;
import java.util.ArrayList;

/**
 * Created by rahman on 29/03/2017.
 */

public class MainItemAdapter extends RecyclerView.Adapter<MainItemAdapter.ViewHolder>{

    ArrayList<ItemModel> itemModels = new ArrayList<>();
    Context context;

    private CallbackDetailItem callbackDetailItem;

    public MainItemAdapter(Context context, ArrayList<ItemModel> itemModels){
        this.context = context;
        this.itemModels = itemModels;
        callbackDetailItem = (CallbackDetailItem) context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_main_article, null);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final View view = holder.view;
        ((TextView) view.findViewById(R.id.textId)).setText(itemModels.get(position).getId());
        ((TextView) view.findViewById(R.id.textShortDesc)).setText(itemModels.get(position).getS_description());
        ((TextView) view.findViewById(R.id.textCreateDate)).setText(itemModels.get(position).getCreateDate());
        ((TextView) view.findViewById(R.id.textAccountClass)).setText(itemModels.get(position).getAccountClass());
        view.findViewById(R.id.layoutArticle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDataDetailItem(getItemModel(position));
            }
        });
    }


    @Override
    public int getItemCount() {
        return itemModels.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        View view;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
        }
    }



    public interface CallbackDetailItem{
        void getDataitem(ItemModel itemModel);
    }

    private void getDataDetailItem(ItemModel itemModel){
        callbackDetailItem.getDataitem(itemModel);
    }


    private ItemModel getItemModel(int position){
        ItemModel itemModel = new ItemModel(
                itemModels.get(position).get_id(),
                itemModels.get(position).getId(),
                itemModels.get(position).getS_description(),
                itemModels.get(position).getL_description(),
                itemModels.get(position).getBaseUom(),
                itemModels.get(position).getItemGroup(),
                itemModels.get(position).getItemHierarchy(),
                itemModels.get(position).getAccountClass(),
                itemModels.get(position).getItemConvertion(),
                itemModels.get(position).getCreateBy(),
                itemModels.get(position).getCreateDate(),
                itemModels.get(position).getCreateTime()
        );

        return itemModel;
    }




}
