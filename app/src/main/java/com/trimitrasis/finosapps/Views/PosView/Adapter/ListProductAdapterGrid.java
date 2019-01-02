package com.trimitrasis.finosapps.Views.PosView.Adapter;
import android.content.Context;
import android.os.Environment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.trimitrasis.finosapps.R;
import com.trimitrasis.finosapps.Views.PosView.Model.InternalProductModel;
import com.trimitrasis.finosapps.Views.UtilView.Constants;
import com.trimitrasis.finosapps.Views.UtilView.FontUtils;
import com.trimitrasis.finosapps.Views.UtilView.Utils;
import java.util.List;

/**
 * Created by rahman on 30/03/2017.
 */

public class ListProductAdapterGrid extends RecyclerView.Adapter<ListProductAdapterGrid.MyViewHolder> {


    private Context mContext;
    private List<InternalProductModel> createArrProd;
    CallbackListProductGrid listProductGrid;


    public class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView namaBarang, hargaBarang, kodeBarcode;
        public ImageView thumbnail;
        public CardView card_view;


        public MyViewHolder(View view){
            super(view);
            namaBarang      = (TextView) view.findViewById(R.id.textNamaBarang);
            hargaBarang     = (TextView) view.findViewById(R.id.textHarga);
            thumbnail       = (ImageView) view.findViewById(R.id.imgItem);
            card_view       = (CardView) view.findViewById(R.id.card_view);
            kodeBarcode     = (TextView) view.findViewById(R.id.textKodeBarcode);
        }
    }


    public ListProductAdapterGrid(Context mContext, List<InternalProductModel> createArr){
        this.mContext = mContext;
        this.createArrProd = createArr;
        listProductGrid = (CallbackListProductGrid) mContext;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_product_grid, parent, false);
        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position){

        InternalProductModel internalProductModel = createArrProd.get(position);
        holder.hargaBarang.setTypeface(FontUtils.getHelvetica_Neue_LT(mContext));
        holder.kodeBarcode.setTypeface(FontUtils.getHelvetica_Neue_LT(mContext));
        holder.namaBarang.setText(internalProductModel.getDescription());
        holder.hargaBarang.setText(Utils.getCurrencyRupiah(internalProductModel.getHarga_jual()) + " / "+internalProductModel.getUom());
        holder.kodeBarcode.setText("BARCODE : " + internalProductModel.getBarcode());


        if(!internalProductModel.getFile_name().equals("")){
            Glide.with(mContext).load(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/"+
                    Constants.userInfoModel.getDbname()+"/"+internalProductModel.getFile_name()+"."+internalProductModel.getFile_ext())
                    .error(R.mipmap.image_produk)
                    .into(holder.thumbnail);
        }else{
            Glide.with(mContext).load(R.mipmap.image_produk).into(holder.thumbnail);
        }


        holder.thumbnail.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                getDataGridProduct(getInternalProductModel(position));
            }
        });
    }


    @Override
    public int getItemCount(){
        return createArrProd.size();
    }

    public interface CallbackListProductGrid{
        void getListProductTab(InternalProductModel internalProductModel);
    }

    private void getDataGridProduct(InternalProductModel internalProductModel){
        listProductGrid.getListProductTab(internalProductModel);
    }


    private InternalProductModel getInternalProductModel(int position){
        InternalProductModel internalProductModel = new InternalProductModel(
                createArrProd.get(position).getItem_id(),
                createArrProd.get(position).getStock_id(),
                createArrProd.get(position).getDescription(),
                createArrProd.get(position).getBase_uom(),
                createArrProd.get(position).getItem_group(),
                createArrProd.get(position).getItem_hierarchy(),
                createArrProd.get(position).getUom(),
                createArrProd.get(position).getUom_convertion(),
                createArrProd.get(position).getBase_uom_convertion(),
                createArrProd.get(position).getHarga_jual(),
                createArrProd.get(position).getBarcode(),
                createArrProd.get(position).getTax_group(),
                createArrProd.get(position).getStandart_cost(),
                createArrProd.get(position).isFlag_qty(),
                createArrProd.get(position).isFlag_bom(),
                createArrProd.get(position).getItem_hierarchy_ancestor(),
                R.mipmap.image_produk,
                createArrProd.get(position).getFile_ext(),
                createArrProd.get(position).getFile_name(),
                createArrProd.get(position).getFull_path(),
                createArrProd.get(position).getFile_path(),
                createArrProd.get(position).getDetail_id()

        );

        return internalProductModel;
    }

}
