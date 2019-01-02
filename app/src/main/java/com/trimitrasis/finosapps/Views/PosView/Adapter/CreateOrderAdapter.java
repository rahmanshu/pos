package com.trimitrasis.finosapps.Views.PosView.Adapter;
import android.content.Context;
import android.graphics.Color;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import java.util.List;
import com.trimitrasis.finosapps.R;
import com.trimitrasis.finosapps.Views.PosView.Model.InternalProductModel;
import com.trimitrasis.finosapps.Views.UtilView.Constants;
import com.trimitrasis.finosapps.Views.UtilView.Utils;


/**
 * Created by rahman on 10/6/2016.
 */
public class CreateOrderAdapter  extends RecyclerView.Adapter<CreateOrderAdapter.ViewHolder>{

    Context context;
    List<InternalProductModel> internalProductModels = new ArrayList<>();

    CallbackListBarangHandset callbackListBarangHandset;
    private int selectedPosition = 0;

    private  SparseBooleanArray selectedItems;

    public CreateOrderAdapter(Context context, ArrayList<InternalProductModel> internalProductModels){
        this.context = context;
        this.internalProductModels = internalProductModels;
        callbackListBarangHandset = (CallbackListBarangHandset) context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_createorder_layout,null);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final View view = holder.view;
        ((TextView) view.findViewById(R.id.textHarga)).setText(Utils.getCurrencyRupiah(internalProductModels.get(position).getHarga_jual()));
        ((TextView) view.findViewById(R.id.textNamaBarang)).setText(internalProductModels.get(position).getDescription());

        if(internalProductModels.get(position).getStock_id().toString().equals(internalProductModels.get(position).getFile_name())){
            Glide.with(context).load(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/"+
                    Constants.userInfoModel.getDbname()+"/"+internalProductModels.get(position).getFile_name()
                    +"."+internalProductModels.get(position).getFile_ext())
                    .error(R.mipmap.image_produk)
                    .into((ImageView) view.findViewById(R.id.imgBarang));
        }else{
            Glide.with(context).load(R.mipmap.image_produk).into((ImageView) view.findViewById(R.id.imgBarang));
        }



        view.findViewById(R.id.rowLayout).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                callInterfaceListBarang(getListOrder(position));
            }
        });

    }


    @Override
    public int getItemCount(){
        return internalProductModels.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        View view;

        public ViewHolder(View itemView){
            super(itemView);
            view = itemView;
        }

    }


    public interface CallbackListBarangHandset{
        void showDialogQtyHandset(InternalProductModel internalProductModel);
    }

    private void callInterfaceListBarang(InternalProductModel internalProductModel){
        callbackListBarangHandset.showDialogQtyHandset(internalProductModel);
    }


    private InternalProductModel getListOrder(int position){
        InternalProductModel internalProductModel = new InternalProductModel(
                internalProductModels.get(position).getItem_id(),
                internalProductModels.get(position).getStock_id(),
                internalProductModels.get(position).getDescription(),
                internalProductModels.get(position).getBase_uom(),
                internalProductModels.get(position).getItem_group(),
                internalProductModels.get(position).getItem_hierarchy(),
                internalProductModels.get(position).getUom(),
                internalProductModels.get(position).getUom_convertion(),
                internalProductModels.get(position).getBase_uom_convertion(),
                internalProductModels.get(position).getHarga_jual(),
                internalProductModels.get(position).getBarcode(),
                internalProductModels.get(position).getTax_group(),
                internalProductModels.get(position).getStandart_cost(),
                internalProductModels.get(position).isFlag_qty(),
                internalProductModels.get(position).isFlag_bom(),
                internalProductModels.get(position).getItem_hierarchy_ancestor(),
                R.mipmap.image_produk,
                internalProductModels.get(position).getFile_ext(),
                internalProductModels.get(position).getFile_name(),
                internalProductModels.get(position).getFull_path(),
                internalProductModels.get(position).getFile_path(),
                internalProductModels.get(position).getDetail_id()
        );

        System.out.println("tax nyee : " + internalProductModels.get(position).getTax_group());
        return internalProductModel;
    }



}
