package com.trimitrasis.finosapps.Views.MasterDataView.Adapter;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.trimitrasis.finosapps.R;
import com.trimitrasis.finosapps.Views.MasterDataView.Models.CustomerModel;
import java.util.ArrayList;

/**
 * Created by rahman on 13/03/2017.
 */

public class MainCustomerAdapter extends RecyclerView.Adapter<MainCustomerAdapter.ViewHolder>{


    ArrayList<CustomerModel> customerModels = new ArrayList<>();
    Context context;
    private CallbackDetailCustomer callbackDetailCustomer;


    public MainCustomerAdapter(Context context, ArrayList<CustomerModel> customerModels){
        this.context = context;
        this.customerModels = customerModels;
        callbackDetailCustomer = (CallbackDetailCustomer) context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_main_customer, null);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final View view = holder.view;
        ((TextView) view.findViewById(R.id.textCustomerName)).setText(customerModels.get(position).getName());
        ((TextView) view.findViewById(R.id.textEmailAddress)).setText(customerModels.get(position).getEmail());
        ((TextView) view.findViewById(R.id.textPhone)).setText(customerModels.get(position).getPhone1());
        view.findViewById(R.id.layoutCustomer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDataDetailCustomer(getCustomerModel(position));
            }
        });
    }


    @Override
    public int getItemCount() {
        return customerModels.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        View view;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
        }
    }




    public interface CallbackDetailCustomer{
        void getDataCustomer(CustomerModel customerModel);
    }

    private void getDataDetailCustomer(CustomerModel customerModel){
        callbackDetailCustomer.getDataCustomer(customerModel);
    }


    private CustomerModel getCustomerModel(int position){
        CustomerModel customerModel = new CustomerModel(
                customerModels.get(position).get_id(),
                customerModels.get(position).getName(),
                customerModels.get(position).getPhone1(),
                customerModels.get(position).getPhone2(),
                customerModels.get(position).getAddress(),
                customerModels.get(position).getFax(),
                customerModels.get(position).getEmail(),
                customerModels.get(position).getRecon_account(),
                customerModels.get(position).getCurrency(),
                customerModels.get(position).getCredit_limit(),
                customerModels.get(position).getPayment_terms(),
                customerModels.get(position).isPkp(),
                customerModels.get(position).getTax_group()
        );
        return customerModel;
    }


}
