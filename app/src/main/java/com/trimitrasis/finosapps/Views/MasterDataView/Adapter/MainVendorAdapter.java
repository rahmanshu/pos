package com.trimitrasis.finosapps.Views.MasterDataView.Adapter;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.trimitrasis.finosapps.R;
import com.trimitrasis.finosapps.Views.MasterDataView.Models.VendorModel;
import com.trimitrasis.finosapps.Views.UtilView.FontUtils;
import java.util.ArrayList;

/**
 * Created by rahman on 16/03/2017.
 */

public class MainVendorAdapter extends RecyclerView.Adapter<MainVendorAdapter.ViewHolder>{

    ArrayList<VendorModel> mainVendorModels = new ArrayList<>();
    Context context;
    private CallbackDetailVendor callbackDetailVendor;


    public MainVendorAdapter(Context context, ArrayList<VendorModel> mainVendorModels){
        this.context = context;
        this.mainVendorModels = mainVendorModels;
        callbackDetailVendor = (CallbackDetailVendor) context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_main_vendor, null);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final View view = holder.view;
        ((TextView) view.findViewById(R.id.textCustomerName)).setText(mainVendorModels.get(position).getName());
        ((TextView) view.findViewById(R.id.textEmailAddress)).setText(mainVendorModels.get(position).getEmail());
        ((TextView) view.findViewById(R.id.textPhone)).setText(mainVendorModels.get(position).getPhone1());
        ((TextView) view.findViewById(R.id.textCustomerName)).setTypeface(FontUtils.getHelvetica_Neue_LT(context), Typeface.BOLD);
        ((TextView) view.findViewById(R.id.textEmailAddress)).setTypeface(FontUtils.getHelvetica_Neue_LT(context));
        ((TextView) view.findViewById(R.id.textPhone)).setTypeface(FontUtils.getHelvetica_Neue_LT(context));
        view.findViewById(R.id.layoutVendor).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDataDetailVendor(getVendorModel(position));
            }
        });

    }


    @Override
    public int getItemCount() {
        return mainVendorModels.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        View view;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
        }
    }



    public interface CallbackDetailVendor{
        void getDataVendor(VendorModel vendorModel);
    }

    private void getDataDetailVendor(VendorModel vendorModel){
        callbackDetailVendor.getDataVendor(vendorModel);
    }


    private VendorModel getVendorModel(int position){
        VendorModel vendorModel = new VendorModel(
                mainVendorModels.get(position).get_id(),
                mainVendorModels.get(position).getName(),
                mainVendorModels.get(position).getPhone1(),
                mainVendorModels.get(position).getPhone2(),
                mainVendorModels.get(position).getAddress(),
                mainVendorModels.get(position).getFax(),
                mainVendorModels.get(position).getEmail(),
                mainVendorModels.get(position).getRecon_account(),
                mainVendorModels.get(position).getCurrency(),
                mainVendorModels.get(position).getCredit_limit(),
                mainVendorModels.get(position).getPayment_terms(),
                mainVendorModels.get(position).isPkp(),
                mainVendorModels.get(position).getTax_group()

        );

        return vendorModel;
    }



}
