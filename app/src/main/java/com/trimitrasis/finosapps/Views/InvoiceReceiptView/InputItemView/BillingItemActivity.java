package com.trimitrasis.finosapps.Views.InvoiceReceiptView.InputItemView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.trimitrasis.finosapps.R;
import com.trimitrasis.finosapps.Views.InvoiceReceiptView.InputItemView.Adapter.BillingItemAdapter;
import com.trimitrasis.finosapps.Views.InvoiceReceiptView.InputItemView.Models.BillingItemModel;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import java.util.ArrayList;

/**
 * Created by rahman on 07/03/2017.
 */

@EActivity(R.layout.layout_billing_item)
public class BillingItemActivity extends AppCompatActivity implements BillingItemAdapter.CallbackBillingItem {

    @ViewById
    Toolbar toolbar;

    @ViewById
    RecyclerView listBilingItem;

    RecyclerView.LayoutManager mLayoutManager;

    @ViewById
    EditText editSearchBilling;

    ArrayList<BillingItemModel> billingItemModels;

    ManageDataBillingItem manageDataBillingItem;

    @AfterViews
    void afterView(){
        billingItemModels = new ArrayList<>();
        billingItemModels.clear();
        customView();
        initSetTableView();
        getBillingItemModels();
        manageDataBillingItem = new ManageDataBillingItem(getApplicationContext());
    }


    private void initSetTableView(){
        listBilingItem.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        listBilingItem.setLayoutManager(mLayoutManager);
    }

    private void initListSearch(){
        BillingItemAdapter adapter = new BillingItemAdapter(this, billingItemModels);
        listBilingItem.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void customView(){
        Button buttonCancel = (Button) toolbar.findViewById(R.id.btnCancel);
        buttonCancel.setText("Cancel");
        TextView titleToolbar = (TextView) toolbar.findViewById(R.id.titleToolbar);
        titleToolbar.setText("Item Billing");
        buttonCancel.setOnClickListener(onClickListener);
    }

    private void getBillingItemModels(){
        billingItemModels.add(new BillingItemModel("B001", "Laptop acer", "10000"));
        billingItemModels.add(new BillingItemModel("B002", "Laptop acer", "10000"));
        billingItemModels.add(new BillingItemModel("B003", "Laptop acer", "10000"));
        billingItemModels.add(new BillingItemModel("B004", "Laptop acer", "10000"));
        billingItemModels.add(new BillingItemModel("B005", "Laptop acer", "10000"));
        billingItemModels.add(new BillingItemModel("B006", "Laptop acer", "10000"));
        billingItemModels.add(new BillingItemModel("B007", "Laptop acer", "10000"));
        initListSearch();
    }

    @Override
    public void getDataBillItem(String kodeBarang, String hargaBarang){
        manageDataBillingItem.saveBillingItem(kodeBarang, hargaBarang);
        finish();
    }


    View.OnClickListener onClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            switch (v.getId()) {
                case R.id.btnCancel:{
                    finish();
                }break;

            }
        }
    };



}
