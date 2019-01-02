package com.trimitrasis.finosapps.Views.DeliveryOrderView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.trimitrasis.finosapps.R;
import com.trimitrasis.finosapps.Views.InvoiceReceiptView.CustomView.ViewDetailItem;
import com.trimitrasis.finosapps.Views.InvoiceReceiptView.InputItemView.BillingItemActivity_;
import com.trimitrasis.finosapps.Views.InvoiceReceiptView.InputItemView.ManageDataBillingItem;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by rahman on 27/03/2017.
 */

@EActivity(R.layout.layout_delivery_order)
public class DeliveryOrderActivity  extends AppCompatActivity{

    ArrayList<ViewDetailItem> detailItemArrayList;

    @ViewById
    Button btnAddItem;

    @ViewById
    Toolbar toolbar;

    @ViewById
    LinearLayout contentViewDetailItem;

    @ViewById
    SlidingUpPanelLayout panelLayout;

    @ViewById
    TextView textRemoveJumlah;

    @ViewById
    TextView textTambahJumlah;

    @ViewById
    ListView listJumlahData;

    ViewDetailItem viewDetailItem;

    int incremen_detail_item = 0;

    ManageDataBillingItem manageDataBillingItem;

    ArrayList<String> listJumlah;
    ArrayAdapter<String> arrAdapter;


    @AfterViews
    void afterView(){
        panelLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
        detailItemArrayList = new ArrayList<>();
        manageDataBillingItem = new ManageDataBillingItem(getApplicationContext());
        customView();
    }



    @Override
    protected void onResume(){
        super.onResume();
        HashMap<String, String> item = manageDataBillingItem.getDataBillingItem();
        String kode_item = item.get(manageDataBillingItem.KEY_KODE_ITEM);
        String price = item.get(manageDataBillingItem.KEY_HARGA_ITEM);
        setViewDetailItem(kode_item, price);
    }


    @Click
    void btnAddItem(){
        BillingItemActivity_.intent(this).start();
    }


    @Click
    void contentViewDetailItem(){
        panelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
        initListJumlahPesan();
    }


    private void customView(){
        Button buttonCancel = (Button) toolbar.findViewById(R.id.btnCancel);
        buttonCancel.setText("Cancel");
        TextView titleFormSales = (TextView) toolbar.findViewById(R.id.titleForm);
        titleFormSales.setText("Delivery Order");
        Button buttonSave = (Button) toolbar.findViewById(R.id.btnSave);
        buttonSave.setText("Save");
        buttonCancel.setOnClickListener(onClickListener);
        buttonSave.setOnClickListener(onClickListener);
    }


    private void setViewDetailItem(String kodeBarang, String hargaBarang){
        viewDetailItem = new ViewDetailItem(this);
        viewDetailItem.setTextItemId(kodeBarang);
        viewDetailItem.setTextQty("3");
        viewDetailItem.setTextPrice(hargaBarang);
        detailItemArrayList.add(incremen_detail_item, viewDetailItem);
        contentViewDetailItem.addView(detailItemArrayList.get(incremen_detail_item));
        incremen_detail_item++ ;
    }

    @Click
    void textRemoveJumlah(){
        panelLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
    }


    @Click
    void textTambahJumlah(){
        panelLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
    }


    public void initListJumlahPesan(){
        listJumlah = new ArrayList<String>();
        for(int i = 0; i <= 100; ++i){
            if(i != 0){
                listJumlah.add(String.valueOf(i));
            }
        }
        arrAdapter = new ArrayAdapter<String>(DeliveryOrderActivity.this, android.R.layout.simple_list_item_1, listJumlah);
        listJumlahData.setAdapter(arrAdapter);
    }


    View.OnClickListener onClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            switch (v.getId()) {
                case R.id.btnCancel:{
                    finish();
                }break;
                case R.id.btnSave:{
                }break;
            }
        }
    };




}
