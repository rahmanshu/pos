package com.trimitrasis.finosapps.Views.PosView.Adapter;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.trimitrasis.finosapps.R;
import com.trimitrasis.finosapps.Views.PosView.Model.ListCategoryModel;
import com.trimitrasis.finosapps.Views.PosView.Model.RingkasanOrderModel;
import com.trimitrasis.finosapps.Views.UtilView.FontUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rahman on 20/06/2017.
 */

public class ListCategoryAdapter  extends RecyclerView.Adapter<ListCategoryAdapter.ViewHolder> {


    Context context;
    List<ListCategoryModel> listCategoryModels = new ArrayList<>();
    private int selectedPosition = 0;

    CallbackListItemCategotyTab listItemCategotyTab;

    public ListCategoryAdapter(Context context, ArrayList<ListCategoryModel> listCategoryModels){
        this.context = context;
        this.listCategoryModels = listCategoryModels;
        listItemCategotyTab = (CallbackListItemCategotyTab) context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(context).inflate(R.layout.row_list_data_category, null);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }



    @Override
    public void onBindViewHolder(ViewHolder holder, final int position){
        final View view = holder.view;
        ((TextView) view.findViewById(R.id.textNamaCategory)).setTypeface(FontUtils.getHelvetica_Neue_LT(context));
        ((TextView) view.findViewById(R.id.textNamaCategory)).setText(listCategoryModels.get(position).getNameCategory());

        if(selectedPosition == position){
            ((LinearLayout) view.findViewById(R.id.rowLayout)).setBackgroundColor(ContextCompat.getColor(context, R.color.orange_main));
        }else{
            ((LinearLayout) view.findViewById(R.id.rowLayout)).setBackgroundColor(ContextCompat.getColor(context, R.color.white_soft));
        }

        view.findViewById(R.id.rowLayout).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                notifyItemChanged(selectedPosition);
                selectedPosition = position;
                notifyItemChanged(selectedPosition);
                getCategoryItem(getListItemCategoryModel(position));

            }
        });

    }


    @Override
    public int getItemCount(){
        return listCategoryModels.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder{
        View view;
        public ViewHolder(View itemView){
            super(itemView);
            view = itemView;
        }
    }


    public interface CallbackListItemCategotyTab{
        void getItemCategoryTab(ListCategoryModel listCategoryModel);
    }

    private void getCategoryItem(ListCategoryModel listCategoryModel){
        listItemCategotyTab.getItemCategoryTab(listCategoryModel);
    }



    private ListCategoryModel getListItemCategoryModel(int position){
        ListCategoryModel listCategoryModel = new ListCategoryModel(
                listCategoryModels.get(position).getIdCategory(),
                listCategoryModels.get(position).getNameCategory()
        );

        return listCategoryModel;
    }




}
