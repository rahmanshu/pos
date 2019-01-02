package com.trimitrasis.finosapps.Views.SplitBillView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import com.trimitrasis.finosapps.ActivityResultBill;
import com.trimitrasis.finosapps.ActivityResultPesanan;
import com.trimitrasis.finosapps.R;
import com.trimitrasis.finosapps.Views.PosView.Model.RingkasanOrderModel;
import com.trimitrasis.finosapps.Views.SplitBillView.Adapter.ListBillAdapter;
import com.trimitrasis.finosapps.Views.SplitBillView.Adapter.ListPesananAdapter;
import com.trimitrasis.finosapps.Views.SplitBillView.Fragment.listBillFragment;
import com.trimitrasis.finosapps.Views.SplitBillView.Fragment.listPesananFragment;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.ViewById;
import java.util.ArrayList;
import de.greenrobot.event.EventBus;


/**
 * Created by rahman on 9/26/2017.
 */


@EActivity(R.layout.layout_split_bill)
public class SplitBillMenuActivity extends AppCompatActivity implements
        ListPesananAdapter.CallbackListPesanan,
        ListBillAdapter.CallbackListBill,
        listBillFragment.CallbackCheckListPesanan{

    @ViewById
    Toolbar toolbar;

    @Extra
    ArrayList<RingkasanOrderModel> getRingkasanOrderModels;

    @Extra
    String infoBayar;

    @Extra
    String idJual;

    @Extra
    String jenisPajak;

    public static String infoPembayaran_ = "";
    public static String idPenjualan_    = "";
    public static String jenisPajak_     = "";

    @AfterViews
    void afterView(){
        initTabbar();
        customView();

        if(getRingkasanOrderModels != null){
            insertDataSplit();
        }

        jenisPajak_     = jenisPajak;
        idPenjualan_    = idJual;
        infoPembayaran_ = infoBayar;
    }


    @OptionsItem(android.R.id.home)
    void homeClick(){
        onBackPressed();
        jenisPajak_     = "";
        idPenjualan_    = "";
        infoPembayaran_ = "";
    }


    private void customView(){
        TextView toolbarView = (TextView) toolbar.findViewById(R.id.headerText);
        toolbarView.setText("Split Bill");
    }


    private void initTabbar(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_arrow_left);
    }


    private void insertDataSplit(){
        for(int i = 0; i < getRingkasanOrderModels.size(); i++){
            addRingkasanSplitBill(getRingkasanOrderModels.get(i));
        }
    }


    private void addRingkasanSplitBill(RingkasanOrderModel ringkasanOrderModel){
        RingkasanOrderModel orderModel = new RingkasanOrderModel(
                ringkasanOrderModel.getItemId(),
                ringkasanOrderModel.getKodeBarcode(),
                ringkasanOrderModel.getKodeBarang(),
                ringkasanOrderModel.getNamaBarang(),
                ringkasanOrderModel.getSatuanBarang(),
                ringkasanOrderModel.getHargaJual(),
                ringkasanOrderModel.getQty(),
                ringkasanOrderModel.getDiskon(),
                ringkasanOrderModel.getTax(),
                ringkasanOrderModel.getInfo(),
                ringkasanOrderModel.getStandart_cost(),
                ringkasanOrderModel.isFlag_qty(),
                ringkasanOrderModel.isFlag_bom(),
                ringkasanOrderModel.getTaxGroup(),
                ringkasanOrderModel.getNote(),
                ringkasanOrderModel.getDetailId()
        );

        EventBus.getDefault().post(new ActivityResultPesanan(orderModel));
    }


    @Override
    public void getDataListPesanan(RingkasanOrderModel ringkasanOrderModel){
        EventBus.getDefault().post(new ActivityResultPesanan(ringkasanOrderModel));
        EventBus.getDefault().post(new ActivityResultBill(ringkasanOrderModel));
    }


    @Override
    public void getDataListBill(RingkasanOrderModel ringkasanOrderModel){

        if(!ringkasanOrderModel.getInfo().equals("buy x get y")){
            EventBus.getDefault().post(new ActivityResultPesanan(ringkasanOrderModel));
            EventBus.getDefault().post(new ActivityResultBill(ringkasanOrderModel));
        }

    }


    @Override
    public void checkListPesanan(){
        listPesananFragment listPesananFragment = (listPesananFragment) getSupportFragmentManager().findFragmentById(R.id.pesananFragment);
        if(listPesananFragment != null){
            listPesananFragment.checkListPesanan();
        }
    }




}
