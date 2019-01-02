package com.trimitrasis.finosapps.Views.PosView;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.speech.RecognizerIntent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.trimitrasis.finosapps.BusStation;
import com.trimitrasis.finosapps.R;
import com.trimitrasis.finosapps.Views.ContentProvider.ItemProvider;
import com.trimitrasis.finosapps.Views.ContentProvider.PromosiBarangProvider;
import com.trimitrasis.finosapps.Views.ContentProvider.PromosiHadiahProvider;
import com.trimitrasis.finosapps.Views.ContentProvider.PromosiProvider;
import com.trimitrasis.finosapps.Views.ContentProvider.TaxGroupProvider;
import com.trimitrasis.finosapps.Views.ContentProvider.TaxProvider;
import com.trimitrasis.finosapps.Views.PosView.Adapter.CreateOrderAdapter;
import com.trimitrasis.finosapps.Views.PosView.Adapter.ListBayarAdapterList;
import com.trimitrasis.finosapps.Views.PosView.Adapter.ListCategoryAdapter;
import com.trimitrasis.finosapps.Views.PosView.Adapter.ListProductAdapterGrid;
import com.trimitrasis.finosapps.Views.PosView.Fragment.ListBayarFragment;
import com.trimitrasis.finosapps.Views.PosView.Fragment.ListProductFragment;
import com.trimitrasis.finosapps.Views.PosView.Fragment.ListPromoFragment;
import com.trimitrasis.finosapps.Views.PosView.Model.InternalProductModel;
import com.trimitrasis.finosapps.Views.PosView.Model.ListCategoryModel;
import com.trimitrasis.finosapps.Views.PosView.Model.ListCekPromo;
import com.trimitrasis.finosapps.Views.PosView.Model.ListPromoBarang;
import com.trimitrasis.finosapps.Views.PosView.Model.ListPromoBarangQty;
import com.trimitrasis.finosapps.Views.PosView.Model.PpnModel;
import com.trimitrasis.finosapps.Views.PosView.Model.RingkasanOrderModel;
import com.trimitrasis.finosapps.Views.UtilView.UtilPos;
import com.trimitrasis.finosapps.Views.UtilView.Utils;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.ViewById;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


/**
 * Created by rahman on 28/03/2017.
 */

@EActivity(R.layout.layout_pos_activity)
public class CreateOrderActivity extends AppCompatActivity implements
                        ListProductAdapterGrid.CallbackListProductGrid,
                        ListBayarAdapterList.CallbackListOrderTab,
                        CreateOrderAdapter.CallbackListBarangHandset,
                        ListBayarFragment.OnDeleteInfoPromoListener,
                        ListCategoryAdapter.CallbackListItemCategotyTab,
                        ListProductFragment.CallbackFragmentProduct {

    @ViewById
    ImageView imgBarcode;

    @ViewById
    Toolbar toolbar;

    @ViewById
    RecyclerView listBuatOrder;

    @ViewById
    EditText editTextSearch;

    @ViewById
    static TextView textTotalBayar;

    @Extra
    ArrayList<RingkasanOrderModel> getRingkasanOrderModels;

    @Extra
    String id_jual;

    @Extra
    String jenis_pajak;

    public static String idJualHold = "";
    public static String jenisPajak = "";
    String key_id_jual = "";

    Dialog dialogSetQtyTab;
    LayoutInflater inflater;
    TextView textNamaBarang;
    EditText editTextQty, editPriceBarang, editNote;
    Button buttonTambahQty, buttonKurangQty;
    Button buttonSetQty, buttonCancel, buttonDelete;
    double qtyBrg, hargaBarang;
    String noteBarang;
    RingkasanOrderModel lisOrderModel;
    ArrayList<InternalProductModel> arrListProductHandset;
    ArrayList<InternalProductModel> internalProductModels;
    RecyclerView.LayoutManager mLayoutManager;

    static FrameLayout frameIconShop;
    static ImageView iconShopingView;
    static TextView unreadNotifTextLayoutView;

    static double totalBayar = 0, totalTemp = 0;
    static double subtotal = 0;
    static int total_qty;
    static ArrayList<RingkasanOrderModel> ringkasanOrderModels;
    public static Context contextOfApplication;
    String infoBay = "";

    static boolean flak_promo = false;
    static int flakCount = 0;  static int totQtyPromo = 0; static int resultPromo = 0;
    static List<ListPromoBarang> listPromoBarangs = new ArrayList<>();
    static List<ListPromoBarangQty> listPromoBarangQtyList = new ArrayList<>();
    static int min = 1000;

    static List<ListCekPromo> listCekPromos = new ArrayList<>();
    static ArrayList<String> listIndexPromo = new ArrayList<String>();
    static int idxPromo = 0; static int qtyListPromo = 0;

    ArrayList<PpnModel> ppnModels;

    static List<String> listRingkasan = new ArrayList<>();
    static List<String> listPromo = new ArrayList<>();

    static double qtyHadiahBef = 0;

    @AfterViews
    void afterView(){
        inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(listBuatOrder == null){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); //tab
            customViewTab();
        }else{
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            arrListProductHandset = new ArrayList<>();
            arrListProductHandset.clear();
            internalProductModels = new ArrayList<>();
            internalProductModels.clear();
            ringkasanOrderModels = new ArrayList<>();
            ringkasanOrderModels.clear();
            ppnModels = new ArrayList<>();
            ppnModels.clear();
            getDataTax();
            declareIconShop();
            updateShopingQty();
            initSetTableView();
            initListAwal();
            addTextListener();
        }

        contextOfApplication = getApplicationContext();
        customView();
        initTabbar();

        if(getRingkasanOrderModels != null){
            showDialogTundaPenjualan();
        }

    }


    private void initTabbar(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_arrow_left);
    }


    private void customView(){
        TextView toolbarView = (TextView) toolbar.findViewById(R.id.headerText);
        if(listBuatOrder == null){
            toolbarView.setText("Point Of Sale"); //tab
        }else{
            toolbarView.setText("POS Mobile");
        }
    }


    private void declareIconShop(){
        frameIconShop              = (FrameLayout) toolbar.findViewById(R.id.frameIcon);
        iconShopingView            = (ImageView)   toolbar.findViewById(R.id.iconShoping);
        unreadNotifTextLayoutView  = (TextView)    toolbar.findViewById(R.id.unreadNotifTextLayout);
        frameIconShop.setOnClickListener(onClickListener);
    }


    private void initSetTableView(){
        listBuatOrder.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        listBuatOrder.setLayoutManager(mLayoutManager);
    }


    private void initListAwal(){
        CreateOrderAdapter adapter = new CreateOrderAdapter(this, getAllProductFromDevice());
        listBuatOrder.setAdapter(adapter);
    }


    private void initListSearch(){
        CreateOrderAdapter adapter = new CreateOrderAdapter(this, internalProductModels);
        listBuatOrder.setAdapter(adapter);
    }


    private void customViewTab(){
        TextView toolbarView    = (TextView) toolbar.findViewById(R.id.headerText);
        ImageView iconHoldSales = (ImageView) toolbar.findViewById(R.id.iconHoldSales);
        toolbarView.setText("Point Of Sale");

        iconHoldSales.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                HoldPenjualanActivity_.intent(CreateOrderActivity.this).start();
                idJualHold = "";
                jenisPajak = "";
                finish();
            }
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if(result != null){

            if(result.getContents() == null){
                Toast.makeText(this, "Dibatalkan", Toast.LENGTH_LONG).show();
            }else{
                if(listBuatOrder == null){ //tab
                    ListProductFragment.setViewBarcode(result.getContents());
                }else{
                    editTextSearch.setText(result.getContents());
                }
            }
        }else{

            super.onActivityResult(requestCode, resultCode, data);
            if(resultCode == -1){
                if (resultCode == RESULT_OK){
                    ArrayList<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    ListProductFragment.setViewBarcode(results.get(0));
                }
            }
        }
    }

    @Click
    void imgBarcode(){
        new IntentIntegrator(this).initiateScan();
    }


    @OptionsItem(android.R.id.home)
    void homeClick(){
        onBackPressed();
        idJualHold = "";
        jenisPajak = "";
    }


    @Override
    public void getListProductTab(InternalProductModel internalProductModel){ //onClickGrid in tab
        addOrderList(internalProductModel, "insert");
    }

    @Override
    public void getListOrderTab(RingkasanOrderModel ringkasanOrderModel){
        showDialogQty(ringkasanOrderModel);
    }

    private void showDialogQty(RingkasanOrderModel ringkasanOrderModel){
        dialogSetQtyTab = new Dialog(this);
        View view = inflater.inflate(R.layout.popup_set_qty, null);
        declareLayout(view);
        setListOrderModel(ringkasanOrderModel);
        setDataDialogOrder(ringkasanOrderModel);
        dialogSetQtyTab.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogSetQtyTab.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#50000000")));
        dialogSetQtyTab.setContentView(view);
        dialogSetQtyTab.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialogSetQtyTab.show();
    }


    public void addTextListener(){
        editTextSearch.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after){
            }


            @Override
            public void onTextChanged(CharSequence query, int start, int before, int count){
                query = query.toString().toLowerCase();
                internalProductModels = new ArrayList<>();
                internalProductModels.clear();
                for(int i = 0; i < arrListProductHandset.size(); i++){
                    final String itemDesc = arrListProductHandset.get(i).getDescription().toLowerCase();
                    final String barcodeCode = arrListProductHandset.get(i).getBarcode().toLowerCase();
                    if(itemDesc.contains(query) || barcodeCode.contains(query)){

                        internalProductModels.add(arrListProductHandset.get(i));
                        if(barcodeCode.equals(query)){
                            insertDataOrderHandset(arrListProductHandset.get(i));
                        }

                    }
                }

                initListSearch();
            }

            @Override
            public void afterTextChanged(Editable s){

            }
        });
    }



    private void declareLayout(View view){
        buttonTambahQty  = (Button) view.findViewById(R.id.tambahQty);
        buttonKurangQty  = (Button) view.findViewById(R.id.kurangQty);
        buttonSetQty     = (Button)    view.findViewById(R.id.buttonSetQty);
        buttonDelete     = (Button)    view.findViewById(R.id.buttonDelete);
        buttonCancel     = (Button)    view.findViewById(R.id.buttonCancel);
        editTextQty      = (EditText)  view.findViewById(R.id.numberText);
        editPriceBarang  = (EditText)  view.findViewById(R.id.editPrice);
        editNote         = (EditText)  view.findViewById(R.id.editNote);
        textNamaBarang   = (TextView)  view.findViewById(R.id.textNamaBarang);
        buttonCancel.setOnClickListener(onClickListener);
        buttonSetQty.setOnClickListener(onClickListener);
        buttonDelete.setOnClickListener(onClickListener);
        buttonKurangQty.setOnClickListener(onClickListener);
        buttonTambahQty.setOnClickListener(onClickListener);
    }


    private void setDataDialogOrder(RingkasanOrderModel ringkasanOrderModel){
        qtyBrg = ringkasanOrderModel.getQty();
        hargaBarang = ringkasanOrderModel.getHargaJual();
        editTextQty.setText(String.valueOf(qtyBrg));
        editPriceBarang.setText(""+hargaBarang);
        textNamaBarang.setText(ringkasanOrderModel.getNamaBarang());
        editNote.setText(ringkasanOrderModel.getNote());

        if(lisOrderModel.isFlag_qty() == false){
            editPriceBarang.setEnabled(true);
        }else if(lisOrderModel.isFlag_qty() == true){
            editPriceBarang.setEnabled(false);
        }

    }


    View.OnClickListener onClickListener = new View.OnClickListener(){
         @Override
        public void onClick(View v){
            switch (v.getId()){
                case R.id.buttonSetQty:{

                    qtyBrg = Double.parseDouble(editTextQty.getText().toString());
                    if(!editPriceBarang.getText().toString().equals("")){
                        hargaBarang = Double.parseDouble(editPriceBarang.getText().toString());
                    }
                    noteBarang = editNote.getText().toString();
                    setQtyListOrder(lisOrderModel, "update");

                }break;
                case R.id.buttonDelete:{
                    setQtyListOrder(lisOrderModel, "delete");
                }break;
                case R.id.buttonCancel:{
                    dialogSetQtyTab.dismiss();
                }break;
                case R.id.tambahQty:{
                        qtyBrg++;
                        editTextQty.setText(String.valueOf(qtyBrg));
                }break;
                case R.id.kurangQty:{
                    if(qtyBrg > 1){
                        qtyBrg--;
                        editTextQty.setText(String.valueOf(qtyBrg));
                    }
                }break;
                case R.id.frameIcon:{

                    String device_id = Utils.getDevId(CreateOrderActivity.this);
                    SimpleDateFormat dateFormatIdJual = new SimpleDateFormat("yyMMddHHmmss");
                    Date date = new Date();
                    key_id_jual = device_id.substring(0, 4).toUpperCase() + "-" + dateFormatIdJual.format(date);

                    if(infoBay.toString().equals("hold")){
                        RingkasanOrderActivity_.intent(CreateOrderActivity.this).jenisPajak(jenisPajak).infoBay(infoBay).idPenjualan(id_jual).ringkasanOrderModels(ringkasanOrderModels).start();
                        jenisPajak = "";
                    }else{
                        RingkasanOrderActivity_.intent(CreateOrderActivity.this).jenisPajak("").infoBay("pos").idPenjualan(key_id_jual).ringkasanOrderModels(ringkasanOrderModels).start();
                    }

                }break;

            }
        }
    };



    private void setListOrderModel(RingkasanOrderModel ringkasanOrderModel){

        lisOrderModel = new RingkasanOrderModel(
                ringkasanOrderModel.getItemId(),
                ringkasanOrderModel.getKodeBarcode(),
                ringkasanOrderModel.getKodeBarang(),
                ringkasanOrderModel.getNamaBarang(),
                ringkasanOrderModel.getSatuanBarang(),
                ringkasanOrderModel.getHargaJual(),
                ringkasanOrderModel.getQty(),
                ringkasanOrderModel.getDiskon(),
                ringkasanOrderModel.getTax(),
                "set order",
                ringkasanOrderModel.getStandart_cost(),
                ringkasanOrderModel.isFlag_qty(),
                ringkasanOrderModel.isFlag_bom(),
                ringkasanOrderModel.getTaxGroup(),
                ringkasanOrderModel.getNote(),
                ringkasanOrderModel.getDetailId()
        );
    }


    private void setQtyListOrder(RingkasanOrderModel ringkasanOrderModel, String status){

        if(ringkasanOrderModel.isFlag_qty() == false){

            RingkasanOrderModel orderModel = new RingkasanOrderModel(
                    ringkasanOrderModel.getItemId(),
                    ringkasanOrderModel.getKodeBarcode(),
                    ringkasanOrderModel.getKodeBarang(),
                    ringkasanOrderModel.getNamaBarang(),
                    ringkasanOrderModel.getSatuanBarang(),
                    hargaBarang,
                    qtyBrg, 0,
                    ringkasanOrderModel.getTax(),
                    status,
                    ringkasanOrderModel.getStandart_cost(),
                    ringkasanOrderModel.isFlag_qty(),
                    ringkasanOrderModel.isFlag_bom(),
                    ringkasanOrderModel.getTaxGroup(),
                    noteBarang,
                    ringkasanOrderModel.getDetailId()
            );

            dialogSetQtyTab.dismiss();
            qtyBrg = 0;
            BusStation.getBus().post(orderModel);

        }else if(ringkasanOrderModel.isFlag_qty() == true){

            RingkasanOrderModel orderModel = new RingkasanOrderModel(
                    ringkasanOrderModel.getItemId(),
                    ringkasanOrderModel.getKodeBarcode(),
                    ringkasanOrderModel.getKodeBarang(),
                    ringkasanOrderModel.getNamaBarang(),
                    ringkasanOrderModel.getSatuanBarang(),
                    ringkasanOrderModel.getHargaJual(),
                    qtyBrg, 0,
                    ringkasanOrderModel.getTax(),
                    status,
                    ringkasanOrderModel.getStandart_cost(),
                    ringkasanOrderModel.isFlag_qty(),
                    ringkasanOrderModel.isFlag_bom(),
                    ringkasanOrderModel.getTaxGroup(),
                    noteBarang,
                    ringkasanOrderModel.getDetailId()
            );

            dialogSetQtyTab.dismiss();
            qtyBrg = 0;
            BusStation.getBus().post(orderModel);
        }
    }


    private void addOrderList(InternalProductModel internalProductModel, String status){
        RingkasanOrderModel orderModel = new RingkasanOrderModel(
                internalProductModel.getItem_id(),
                internalProductModel.getBarcode(),
                internalProductModel.getStock_id(),
                internalProductModel.getDescription(),
                internalProductModel.getUom(),
                internalProductModel.getHarga_jual(),
                1, 0,
                getTax(internalProductModel.getTax_group(),
                internalProductModel.getHarga_jual()),
                status,
                internalProductModel.getStandart_cost(),
                internalProductModel.isFlag_qty(),
                internalProductModel.isFlag_bom(),
                internalProductModel.getTax_group(),
                "",
                internalProductModel.getDetail_id()
        );

        BusStation.getBus().post(orderModel);
    }


    private void addRingkasanHoldSale(RingkasanOrderModel ringkasanOrderModel, String status){
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
                status,
                ringkasanOrderModel.getStandart_cost(),
                ringkasanOrderModel.isFlag_qty(),
                ringkasanOrderModel.isFlag_bom(),
                ringkasanOrderModel.getTaxGroup(),
                ringkasanOrderModel.getNote(),
                ringkasanOrderModel.getDetailId()
        );

        BusStation.getBus().post(orderModel);
    }




    private ArrayList<InternalProductModel> getAllProductFromDevice(){

        arrListProductHandset.clear();
        String stock_id = "", description = "", base_uom = "", item_group = "", item_hierarchy = "";
        String uom = "",  barcode = "", tax_group = "", item_heararchy_cat = "", standart_cost = "";
        String fileExt = "", file_name = "", fullPath = "",  filePath = "", detailId = "", item_id = "";
        double uom_convertion, base_uom_convertion, harga_jual, standart_cost_ = 0;
        int flag_qty_int, flag_sell_int, flag_bom_int;
        boolean flag_quantity = false; boolean flag_billofmat = false;

        String URL = "content://com.trimitrasis.finosapps.ContentProvider.ItemProvider/item";
        Uri uri_ = Uri.parse(URL);
        Cursor c = getContentResolver().query(uri_, null, null , null, "_id");

        if(c.moveToFirst()){
            do{

                item_id             = c.getString(c.getColumnIndex(ItemProvider.KEY_KODE_ITEM));
                stock_id            = c.getString(c.getColumnIndex(ItemProvider.KEY_STOCK_ID));
                description         = c.getString(c.getColumnIndex(ItemProvider.KEY_DESCRIPTION));
                base_uom            = c.getString(c.getColumnIndex(ItemProvider.KEY_BASE_UOM));
                item_group          = c.getString(c.getColumnIndex(ItemProvider.KEY_ITEM_GROUP));
                item_hierarchy      = c.getString(c.getColumnIndex(ItemProvider.KEY_ITEM_HIERARCHY));
                uom                 = c.getString(c.getColumnIndex(ItemProvider.KEY_UOM_NAME));
                uom_convertion      = c.getDouble(c.getColumnIndex(ItemProvider.KEY_UOM_CONVERTION));
                base_uom_convertion = c.getDouble(c.getColumnIndex(ItemProvider.KEY_BASE_UOM_CONVERTION));
                harga_jual          = c.getDouble(c.getColumnIndex(ItemProvider.KEY_HARGA_JUAL));
                barcode             = c.getString(c.getColumnIndex(ItemProvider.KEY_BARCODE));
                tax_group           = c.getString(c.getColumnIndex(ItemProvider.KEY_TAX_GROUP));
                standart_cost       = c.getString(c.getColumnIndex(ItemProvider.KEY_STANDART_COST));
                flag_qty_int        = c.getInt(c.getColumnIndex(ItemProvider.KEY_FLAG_QTY));
                flag_sell_int       = c.getInt(c.getColumnIndex(ItemProvider.KEY_FLAG_SELL));
                flag_bom_int        = c.getInt(c.getColumnIndex(ItemProvider.KEY_FLAG_BOM));
                item_heararchy_cat  = c.getString(c.getColumnIndex(ItemProvider.KEY_ITEM_HIERARCHY_CAT));
                fileExt             = c.getString(c.getColumnIndex(ItemProvider.KEY_FILE_EXT));
                file_name           = c.getString(c.getColumnIndex(ItemProvider.KEY_FILE_NAME));
                fullPath            = c.getString(c.getColumnIndex(ItemProvider.KEY_FULL_PATH));
                filePath            = c.getString(c.getColumnIndex(ItemProvider.KEY_FILE_PATH));
                detailId            = c.getString(c.getColumnIndex(ItemProvider.KEY_DETAIL_ID));
                flag_quantity       = (flag_qty_int == 1) ? true : false;
                flag_billofmat      = (flag_bom_int == 1) ? true : false;
                standart_cost_      = (!standart_cost.toString().equals("")) ? Double.parseDouble(standart_cost) : 0;

                if(flag_sell_int == 1){
                    arrListProductHandset.add(new InternalProductModel(item_id, stock_id, description, base_uom, item_group, item_hierarchy,
                            uom, uom_convertion, base_uom_convertion, harga_jual, barcode, tax_group, standart_cost_,
                            flag_quantity, flag_billofmat, item_heararchy_cat, R.mipmap.image_produk, fileExt, file_name, fullPath, filePath, detailId));
                }

            }while (c.moveToNext());
            c.close();

        }else{
            Utils.showToast("Data Kosong, Lalukan sinkronisasi data terlebih dahulu!", this);
        }

        return  arrListProductHandset;
    }


    @Override
    public void showDialogQtyHandset(InternalProductModel internalProductModel){
        insertDataOrderHandset(internalProductModel);
    }



    private void insertDataOrderHandset(InternalProductModel internalProductModel){

        double totQty = 0; boolean k = true;
        for(int i = 0; i < ringkasanOrderModels.size(); i ++){
            if(ringkasanOrderModels.get(i).getKodeBarcode().equals(internalProductModel.getBarcode())){
                totQty = ringkasanOrderModels.get(i).getQty() + 1;
                ringkasanOrderModels.set(i, new RingkasanOrderModel(
                        internalProductModel.getItem_id(),
                        internalProductModel.getBarcode(),
                        internalProductModel.getStock_id(),
                        internalProductModel.getDescription(),
                        internalProductModel.getUom(),
                        internalProductModel.getHarga_jual(),
                        totQty,
                        ringkasanOrderModels.get(i).getDiskon(),
                        getTax(internalProductModel.getTax_group(),
                        internalProductModel.getHarga_jual()) * totQty,
                        ringkasanOrderModels.get(i).getInfo(),
                        internalProductModel.getStandart_cost(),
                        internalProductModel.isFlag_qty(),
                        internalProductModel.isFlag_bom(),
                        internalProductModel.getTax_group(),
                        "",
                        internalProductModel.getDetail_id()
                ));
                k = false;
                break;
            }
        }


        if(k == true){
            ringkasanOrderModels.add(new RingkasanOrderModel(
                    internalProductModel.getItem_id(),
                    internalProductModel.getBarcode(),
                    internalProductModel.getStock_id(),
                    internalProductModel.getDescription(),
                    internalProductModel.getUom(),
                    internalProductModel.getHarga_jual(),
                    1.0, 0,
                    getTax(internalProductModel.getTax_group(),
                    internalProductModel.getHarga_jual()),
                    "",
                    internalProductModel.getStandart_cost(),
                    internalProductModel.isFlag_qty(),
                    internalProductModel.isFlag_bom(),
                    internalProductModel.getTax_group(),
                    "",
                    internalProductModel.getDetail_id()
            ));
        }

        cekPromoDiskon_test(ringkasanOrderModels);
        total_qty++;
        updateShopingQty();
        textTotalBayar.setText(Utils.getCurrencyRupiah(getTotalBayar() + getTotTaxPpn(jenisPajak)));
    }


    public static void updateShopingQty(){
        if(total_qty == 0){
            frameIconShop.setVisibility(View.GONE);
            iconShopingView.setVisibility(View.GONE);
            unreadNotifTextLayoutView.setVisibility(View.GONE);
        }else{
            frameIconShop.setVisibility(View.VISIBLE);
            iconShopingView.setVisibility(View.VISIBLE);
            unreadNotifTextLayoutView.setVisibility(View.VISIBLE);
            unreadNotifTextLayoutView.setText(String.valueOf(total_qty));
        }
    }


    public static void updateDataOrder(ArrayList<RingkasanOrderModel> arrRingkasanOrderModels){
        ringkasanOrderModels = arrRingkasanOrderModels;
        cekPromoDiskon_test(arrRingkasanOrderModels);
        textTotalBayar.setText(Utils.getCurrencyRupiah(getTotalBayar()));
    }


    public static double getTotalBayar(){
        setNolValue();
        for(int j = 0; j < ringkasanOrderModels.size(); j++){
            totalTemp = ringkasanOrderModels.get(j).getHargaJual() * ringkasanOrderModels.get(j).getQty();
            totalBayar = totalBayar + totalTemp;
        }
        subtotal = (int)(totalBayar / 1.1);
        return totalBayar;
    }


    public static void setNolValue(){
        totalBayar = 0;
        totalTemp = 0;
        subtotal = 0;
    }


    long lastPress;
    @Override
    public void onBackPressed(){
        long currentTime = System.currentTimeMillis();
        if(currentTime - lastPress > 5000){
            Utils.showToastLong("Tekan sekali lagi untuk ke menu utama.",this);
            lastPress = currentTime;
        }else{

            total_qty = 0;
            ringkasanOrderModels = new ArrayList<>();
            ringkasanOrderModels.clear();
            textTotalBayar.setText(Utils.getCurrencyRupiah(getTotalBayar()));

            if(listBuatOrder != null){
                updateShopingQty();
            }

            super.onBackPressed();

        }
    }


    private double getTax(String taxGroup, double price){

        double totTax = 0; double rate = 0; String idtaxGroup = ""; double totRate = 0; double totPrice = 0;
        String URL = "content://com.trimitrasis.finosapps.ContentProvider.TaxProvider/tax";
        Uri uri_   = Uri.parse(URL);
        Cursor c   = getContentResolver().query(uri_, null, null , null, "_id");

        if(c.moveToFirst()){
            do{
                idtaxGroup = c.getString(c.getColumnIndex(TaxProvider.KEY_ID_TAX_GROUP));
                if(idtaxGroup.equals(taxGroup)){
                    rate = Double.parseDouble(c.getString(c.getColumnIndex(TaxProvider.KEY_RATE)));
                    totRate = totRate + rate;
                }
            }while(c.moveToNext());
            c.close();
        }else{
            System.out.println("data tidak ditemukan!");
        }

        totTax = totTax + (totRate/(100+totRate) * price);
        return totTax;
    }



    //new 14-09-2017
    private static void cekPromoDiskon_test(ArrayList<RingkasanOrderModel> arrRingkasanOrderModels){

            String kodePromo = "";
            for(int i = 0; i < arrRingkasanOrderModels.size(); i++){
                String URL = "content://com.trimitrasis.finosapps.Views.ContentProvider.PromoJualProvider/promojual";
                kodePromo = UtilPos.getKodePromoFromBarcode("barcode_barang", URL, contextOfApplication, arrRingkasanOrderModels.get(i).getKodeBarcode());
                if(!kodePromo.toString().equals("")){
                    cekPromo(kodePromo, arrRingkasanOrderModels);
                }else{
                    cekDataPromoHadiah(arrRingkasanOrderModels.get(i), i, arrRingkasanOrderModels);
                }
            }

    }




    private static void cekPromo(String kdPromo, ArrayList<RingkasanOrderModel> ringkasanOrderModels){

        String kode_promo = ""; String jenis_promo = ""; String from_ = ""; String to_ = ""; String barcode = "";
        double qty = 0.0; double discount_amount  = 0.0; double discount_percent = 0.0; String barcode_hadiah = "";
        double qtyBarang = 0; int qtyPromo = 0; int p = 0; double qty_hadiah = 0.0;  double totalDiskonPromo = 0;
        double totDiscPercent = 0; double totalDiscount = 0; double amount_hadiah = 0; double totDiscAmount = 0;
        int totCount = 0; int totItem = 0;

        String URLP = "content://com.trimitrasis.finosapps.Views.ContentProvider.PromoJualProvider/promojual";
        totCount = UtilPos.getJumlahDataFromKodePromo("kode_promo_barang", URLP, contextOfApplication, kdPromo);

        String URL = "content://com.trimitrasis.finosapps.Views.ContentProvider.PromoJualProvider/promojual";
        Uri uri_  = Uri.parse(URL);
        Cursor c_promo = contextOfApplication.getContentResolver().query(uri_, null, "kode_promo_barang " + " = '" +  kdPromo + "'", null, "_id ASC");
        listPromo.clear(); listRingkasan.clear(); listPromoBarangQtyList.clear();

        if(c_promo.moveToFirst()){
            do{
                kode_promo       = (!c_promo.getString(c_promo.getColumnIndex(PromosiProvider.KEY_KODE_PROMO)).equals("") ? c_promo.getString(c_promo.getColumnIndex(PromosiProvider.KEY_KODE_PROMO)) : "");
                jenis_promo      = (!c_promo.getString(c_promo.getColumnIndex(PromosiProvider.KEY_JENIS_PROMO)).equals("") ? c_promo.getString(c_promo.getColumnIndex(PromosiProvider.KEY_JENIS_PROMO)) : "");
                from_            = (!c_promo.getString(c_promo.getColumnIndex(PromosiProvider.KEY_FROM)).equals("") ? c_promo.getString(c_promo.getColumnIndex(PromosiProvider.KEY_FROM)) : "");
                to_              = (!c_promo.getString(c_promo.getColumnIndex(PromosiProvider.KEY_TO)).equals("") ? c_promo.getString(c_promo.getColumnIndex(PromosiProvider.KEY_TO)) : "");
                barcode          = (!c_promo.getString(c_promo.getColumnIndex(PromosiBarangProvider.KEY_BARCODE)).equals("") ? c_promo.getString(c_promo.getColumnIndex(PromosiBarangProvider.KEY_BARCODE)) : "");
                qty              = (c_promo.getString(c_promo.getColumnIndex(PromosiBarangProvider.KEY_QTY_BARANG)) != null) ? Double.parseDouble(c_promo.getString(c_promo.getColumnIndex(PromosiBarangProvider.KEY_QTY_BARANG))) : 0.0;
                discount_amount  = (c_promo.getString(c_promo.getColumnIndex(PromosiBarangProvider.KEY_DISCOUNT_AMOUNT)) != null) ? Double.parseDouble(c_promo.getString(c_promo.getColumnIndex(PromosiBarangProvider.KEY_DISCOUNT_AMOUNT))) : 0.0;
                discount_percent = (c_promo.getString(c_promo.getColumnIndex(PromosiBarangProvider.KEY_DISCOUNT_PERCENT)) != null) ? Double.parseDouble(c_promo.getString(c_promo.getColumnIndex(PromosiBarangProvider.KEY_DISCOUNT_PERCENT))) : 0.0;
                listPromo.add(barcode);
                totItem++;

                Date dateNow = new Date();
                for(int i = 0; i < ringkasanOrderModels.size(); i++){
                    if(barcode.equals(ringkasanOrderModels.get(i).getKodeBarcode())){
                        if(dateNow.after(Utils.getDateRange(from_, -1)) && dateNow.before(Utils.getDateRange(to_, 1))){
                            qtyBarang = ringkasanOrderModels.get(i).getQty() / qty;
                            qtyPromo  = (int) qtyBarang;
                            if(qtyPromo >= 1){

                                listRingkasan.add(ringkasanOrderModels.get(i).getKodeBarcode());
                                if(jenis_promo.equals("DISCOUNT")){

                                    if(discount_amount != 0.0){totDiscAmount = discount_amount * qtyPromo;}
                                    if(discount_percent != 0.0){totDiscPercent = ((ringkasanOrderModels.get(i).getHargaJual()) * (qty * qtyPromo)) * (discount_percent/100);}
                                    totalDiscount = (totDiscAmount + totDiscPercent);

                                }else if(jenis_promo.equals("BUY X GET Y")){

                                    listPromoBarangQtyList.add(new ListPromoBarangQty(kode_promo, barcode, qtyPromo));
                                    if(listPromoBarangQtyList.size() > 0){
                                        for(int j = 0; j < listPromoBarangQtyList.size(); j++){
                                            if(listPromoBarangQtyList.get(j).getBarcodeBarang().equals(barcode)){
                                                listPromoBarangQtyList.set(j, new ListPromoBarangQty(kode_promo, barcode, qtyPromo));
                                            }
                                        }
                                    }

                                    if(totCount == totItem){
                                        for(int k = 0; k < listPromoBarangQtyList.size(); k++){
                                            if(k > 0){
                                                if(listPromoBarangQtyList.get(k).getQtyBarang() < min){
                                                    min = listPromoBarangQtyList.get(k).getQtyBarang();
                                                }
                                            }

                                            if(k == 0){
                                                min = listPromoBarangQtyList.get(k).getQtyBarang();
                                            }
                                        }
                                    }

                                }

                                p = i;
                                totalDiskonPromo = totalDiskonPromo + totalDiscount;

                            }
                        }
                    }
                }

            }while(c_promo.moveToNext());
            c_promo.close();
        }



        boolean result = Arrays.equals(listPromo.toArray(), listRingkasan.toArray());
        if(result == true && (listPromo.size() == listRingkasan.size()) && listPromo.size() != 0){

            if(jenis_promo.toString().equals("DISCOUNT")){

                ringkasanOrderModels.set(p, new RingkasanOrderModel(
                        ringkasanOrderModels.get(p).getItemId(),
                        ringkasanOrderModels.get(p).getKodeBarcode(),
                        ringkasanOrderModels.get(p).getKodeBarang(),
                        ringkasanOrderModels.get(p).getNamaBarang(),
                        ringkasanOrderModels.get(p).getSatuanBarang(),
                        ringkasanOrderModels.get(p).getHargaJual(),
                        ringkasanOrderModels.get(p).getQty(),
                        totalDiskonPromo,
                        ringkasanOrderModels.get(p).getTax(),
                        "set discount",
                        ringkasanOrderModels.get(p).getStandart_cost(),
                        ringkasanOrderModels.get(p).isFlag_qty(),
                        ringkasanOrderModels.get(p).isFlag_bom(),
                        ringkasanOrderModels.get(p).getTaxGroup(),
                        ringkasanOrderModels.get(p).getNote(),
                        ringkasanOrderModels.get(p).getDetailId()
                ));

            }else if(jenis_promo.toString().equals("BUY X GET Y")){

                int totQtyAqum = 0;
                for(int m = 0; m < listPromoBarangQtyList.size(); m++){
                    if(listPromoBarangQtyList.get(m).getKodePromo().equals(kode_promo)){
                        if(listPromoBarangQtyList.get(m).getQtyBarang() >= min){
                            totQtyAqum = totQtyAqum + min;
                        }
                    }
                }

                if(listCekPromos.size() > 0){
                    idxPromo = listIndexPromo.indexOf(kode_promo);
                    if(idxPromo != -1){
                        qtyListPromo = listCekPromos.get(idxPromo).getQtyBarang();
                    }else{
                        qtyListPromo = 0;
                    }
                }else{
                    qtyListPromo = 0;
                }

                if(totQtyAqum%totCount==0 && totQtyAqum > qtyListPromo){

                    if(listCekPromos.size() > 0){

                        int k = 0;
                        for(int i = 0; i < listCekPromos.size(); i++){
                            if(listCekPromos.get(i).getKodePromo().equals(kode_promo)){
                                idxPromo = listIndexPromo.indexOf(kode_promo);
                                listCekPromos.set(idxPromo, new ListCekPromo(kode_promo, totQtyAqum));
                                k = 1;
                                break;
                            }
                        }

                        if(k == 0){
                            listCekPromos.add(new ListCekPromo(kode_promo, totQtyAqum));
                            listIndexPromo.add(kode_promo);
                        }

                    }else{
                        listCekPromos.add(new ListCekPromo(kode_promo, totQtyAqum));
                        listIndexPromo.add(kode_promo);
                    }


                    String URL_HADIAH = "content://com.trimitrasis.finosapps.Views.ContentProvider.PromosiHadiahProvider/promosihadiah";
                    Uri uri_hadiah = Uri.parse(URL_HADIAH);
                    Cursor c_hadiah = contextOfApplication.getContentResolver().query(uri_hadiah, null, "kode_promo_hadiah " + " = '" + kode_promo + "'", null, "_id ASC");

                    if(c_hadiah.moveToFirst()){

                        do{
                            qty_hadiah     = (c_hadiah.getString(c_hadiah.getColumnIndex(PromosiHadiahProvider.KEY_QTY_HADIAH)) != null) ? Double.parseDouble(c_hadiah.getString(c_hadiah.getColumnIndex(PromosiHadiahProvider.KEY_QTY_HADIAH))) : 0;
                            barcode_hadiah = (c_hadiah.getString(c_hadiah.getColumnIndex(PromosiHadiahProvider.KEY_BARCODE)) != null) ? c_hadiah.getString(c_hadiah.getColumnIndex(PromosiHadiahProvider.KEY_BARCODE)) : "";

                            String URLITEM = "content://com.trimitrasis.finosapps.ContentProvider.ItemProvider/item";
                            Uri uri_item   = Uri.parse(URLITEM);
                            Cursor citem   = contextOfApplication.getContentResolver().query(uri_item, null, "barcode " + " = '" + barcode_hadiah + "'", null, "_id");

                            if(citem.moveToFirst()){
                                do{
                                    amount_hadiah       = citem.getDouble(citem.getColumnIndex(ItemProvider.KEY_HARGA_JUAL));
                                    String itemId       = citem.getString(citem.getColumnIndex(ItemProvider.KEY_KODE_ITEM));
                                    String kodeBarang   = citem.getString(citem.getColumnIndex(ItemProvider.KEY_STOCK_ID));
                                    String namaBarang   = citem.getString(citem.getColumnIndex(ItemProvider.KEY_DESCRIPTION));
                                    String satuanBarang = citem.getString(citem.getColumnIndex(ItemProvider.KEY_UOM_NAME));
                                    double standartCost = citem.getDouble(citem.getColumnIndex(ItemProvider.KEY_STANDART_COST));
                                    int flakQty         = citem.getInt(citem.getColumnIndex(ItemProvider.KEY_FLAG_QTY));
                                    int flakBom         = citem.getInt(citem.getColumnIndex(ItemProvider.KEY_FLAG_BOM));
                                    String taxGroup     = citem.getString(citem.getColumnIndex(ItemProvider.KEY_TAX_GROUP));
                                    String detilId      = citem.getString(citem.getColumnIndex(ItemProvider.KEY_DETAIL_ID));
                                    boolean flag_bom    = (flakBom == 1) ? true : false;
                                    boolean flag_quan   = (flakQty == 1) ? true : false;
                                    totalDiscount       = ((amount_hadiah) * (qty_hadiah * (totQtyAqum/totCount)));

                                    int n = 0;
                                    for(int k = 0; k < ringkasanOrderModels.size(); k++){
                                        if(barcode_hadiah.equals(ringkasanOrderModels.get(k).getKodeBarcode())){

                                            if(ringkasanOrderModels.get(k).getQty() > 0){
                                                qtyHadiahBef = ringkasanOrderModels.get(k).getQty() + 1.0;
                                            }else{
                                                qtyHadiahBef = ringkasanOrderModels.get(k).getQty();
                                            }

                                            ringkasanOrderModels.set(k, new RingkasanOrderModel(
                                                    ringkasanOrderModels.get(k).getItemId(),
                                                    ringkasanOrderModels.get(k).getKodeBarcode(),
                                                    ringkasanOrderModels.get(k).getKodeBarang(),
                                                    ringkasanOrderModels.get(k).getNamaBarang(),
                                                    ringkasanOrderModels.get(k).getSatuanBarang(),
                                                    ringkasanOrderModels.get(k).getHargaJual(),
                                                    qtyHadiahBef,
                                                    totalDiscount,
                                                    ringkasanOrderModels.get(k).getTax(),
                                                    "buy x get y",
                                                    ringkasanOrderModels.get(k).getStandart_cost(),
                                                    ringkasanOrderModels.get(k).isFlag_qty(),
                                                    ringkasanOrderModels.get(k).isFlag_bom(),
                                                    ringkasanOrderModels.get(k).getTaxGroup(),
                                                    ringkasanOrderModels.get(k).getNote(),
                                                    ringkasanOrderModels.get(k).getDetailId()
                                            ));

                                            n = 1;
                                            break;
                                        }
                                    }


                                    if(n == 0){
                                        System.out.println("add barang");
                                        ringkasanOrderModels.add(new RingkasanOrderModel(
                                                itemId,
                                                barcode_hadiah,
                                                kodeBarang,
                                                namaBarang,
                                                satuanBarang,
                                                amount_hadiah,
                                                qty_hadiah * (totQtyAqum/totCount),
                                                totalDiscount, 0,
                                                "buy x get y",
                                                standartCost,
                                                flag_quan,
                                                flag_bom,
                                                taxGroup,
                                                "",
                                                detilId));
                                    }


                                }while(citem.moveToNext());
                                citem.close();
                            }


                        } while (c_hadiah.moveToNext());
                        c_hadiah.close();
                    }
                }


            }

        }else if(result == false && (listPromo.size() != listRingkasan.size()) || listRingkasan.size() == 0 || listRingkasan == null){

            if(jenis_promo.toString().equals("BUY X GET Y")){

                String URL_HADIAH = "content://com.trimitrasis.finosapps.Views.ContentProvider.PromosiHadiahProvider/promosihadiah";
                Uri uri_had  = Uri.parse(URL_HADIAH);
                Cursor c_had = contextOfApplication.getContentResolver().query(uri_had, null, "kode_promo_hadiah " + " = '" +  kode_promo + "'", null, "_id ASC");

                if(c_had.moveToFirst()){
                    do{
                        barcode_hadiah = (c_had.getString(c_had.getColumnIndex(PromosiHadiahProvider.KEY_BARCODE)) != null) ? c_had.getString(c_had.getColumnIndex(PromosiHadiahProvider.KEY_BARCODE)) : "";
                        for(int i = 0; i < ringkasanOrderModels.size(); i++){
                            if(ringkasanOrderModels.get(i).getKodeBarcode().equals(barcode_hadiah)){
                                if(ringkasanOrderModels.get(i).getInfo().equals("buy x get y")){
                                    if(ringkasanOrderModels.get(i).getQty() > 1.0){
                                        double qtyMin = ringkasanOrderModels.get(i).getQty() - 1.0;
                                        System.out.println("remove update > 1");
                                        ringkasanOrderModels.set(i, new RingkasanOrderModel(
                                                ringkasanOrderModels.get(i).getItemId(),
                                                ringkasanOrderModels.get(i).getKodeBarcode(),
                                                ringkasanOrderModels.get(i).getKodeBarang(),
                                                ringkasanOrderModels.get(i).getNamaBarang(),
                                                ringkasanOrderModels.get(i).getSatuanBarang(),
                                                ringkasanOrderModels.get(i).getHargaJual(),
                                                qtyMin,
                                                0,
                                                ringkasanOrderModels.get(i).getTax(),
                                                "",
                                                ringkasanOrderModels.get(i).getStandart_cost(),
                                                ringkasanOrderModels.get(i).isFlag_qty(),
                                                ringkasanOrderModels.get(i).isFlag_bom(),
                                                ringkasanOrderModels.get(i).getTaxGroup(),
                                                ringkasanOrderModels.get(i).getNote(),
                                                ringkasanOrderModels.get(i).getDetailId()
                                        ));

                                    }else{
                                        System.out.println("remove , qty == 1");
                                        ringkasanOrderModels.remove(i);
                                    }

                                    if(listCekPromos.size() > 0){
                                        idxPromo = listIndexPromo.indexOf(kode_promo);
                                        if(idxPromo != -1) {
                                            listIndexPromo.remove(idxPromo);
                                            listCekPromos.remove(idxPromo);
                                        }
                                    }

                                }
                            }
                        }
                    }while(c_had.moveToNext());
                    c_had.close();

                }
            }
        }

    }



    private static void cekDataPromoHadiah(RingkasanOrderModel ringkasanOrderModel, int index, ArrayList<RingkasanOrderModel> arrRingkasanOrderModels){

        String kode_promo_hadiah = "";
        String URL_HADIAH = "content://com.trimitrasis.finosapps.Views.ContentProvider.PromosiHadiahProvider/promosihadiah";
        Uri uri_had  = Uri.parse(URL_HADIAH);
        Cursor c_had = contextOfApplication.getContentResolver().query(uri_had, null, "barcode_hadiah " + " = '" +  ringkasanOrderModel.getKodeBarcode() + "'", null, "_id ASC");

        int c = 0;
        if(c_had.moveToFirst()){
            do{
                kode_promo_hadiah = (c_had.getString(c_had.getColumnIndex(PromosiHadiahProvider.KEY_KODE_PROMO)) != null) ? c_had.getString(c_had.getColumnIndex(PromosiHadiahProvider.KEY_KODE_PROMO)) : "";
                String URL = "content://com.trimitrasis.finosapps.Views.ContentProvider.PromoJualProvider/promojual";
                Uri uri_  = Uri.parse(URL);
                Cursor c_promo = contextOfApplication.getContentResolver().query(uri_, null, "kode_promo_barang " + " = '" +  kode_promo_hadiah + "'", null, "_id ASC");

                if(c_promo.moveToFirst()){
                    do{
                        String barcode = (!c_promo.getString(c_promo.getColumnIndex(PromosiBarangProvider.KEY_BARCODE)).equals("") ? c_promo.getString(c_promo.getColumnIndex(PromosiBarangProvider.KEY_BARCODE)) : "");
                        for(int i = 0; i < arrRingkasanOrderModels.size(); i++){
                            if(barcode.equals(arrRingkasanOrderModels.get(i).getKodeBarcode())){
                                c = 1;
                            }
                        }

                    }while(c_promo.moveToNext());
                    c_promo.close();
                }

            }while(c_had.moveToNext());
            c_had.close();
        }


        if(c == 0){
            if(ringkasanOrderModel.getInfo().equals("buy x get y")){
                if(ringkasanOrderModel.getQty() > 1){
                    System.out.println("remove lebih dari 1");
                    double qtyOrder = ringkasanOrderModels.get(index).getQty() - 1.0;
                    arrRingkasanOrderModels.set(index, new RingkasanOrderModel(
                            ringkasanOrderModels.get(index).getItemId(),
                            ringkasanOrderModels.get(index).getKodeBarcode(),
                            ringkasanOrderModels.get(index).getKodeBarang(),
                            ringkasanOrderModels.get(index).getNamaBarang(),
                            ringkasanOrderModels.get(index).getSatuanBarang(),
                            ringkasanOrderModels.get(index).getHargaJual(),
                            qtyOrder,
                            0,
                            ringkasanOrderModels.get(index).getTax(),
                            "",
                            ringkasanOrderModels.get(index).getStandart_cost(),
                            ringkasanOrderModels.get(index).isFlag_qty(),
                            ringkasanOrderModels.get(index).isFlag_bom(),
                            ringkasanOrderModels.get(index).getTaxGroup(),
                            ringkasanOrderModels.get(index).getNote(),
                            ringkasanOrderModels.get(index).getDetailId()
                    ));
                }else{
                    System.out.println("remove sama dengan 1");
                    ringkasanOrderModels.remove(index);
                }

                if(listCekPromos.size() > 0){
                    idxPromo = listIndexPromo.indexOf(kode_promo_hadiah);
                    if(idxPromo != -1) {
                        listIndexPromo.remove(idxPromo);
                        listCekPromos.remove(idxPromo);
                    }
                }

            }
        }
    }


    public static void cekLastDataPromo(String kodePromo,  ArrayList<RingkasanOrderModel> arrRingkasanOrderModels){

        String URL = "content://com.trimitrasis.finosapps.Views.ContentProvider.PromoJualProvider/promojual";
        Uri uri_  = Uri.parse(URL);
        Cursor c_promo = contextOfApplication.getContentResolver().query(uri_, null, "kode_promo_barang " + " = '" +  kodePromo + "'", null, "_id ASC");

        int countBarang = 0; int countPromoBarang = 0;
        if(c_promo.moveToFirst()){
            do{
                countPromoBarang++;
                String barcode = (!c_promo.getString(c_promo.getColumnIndex(PromosiBarangProvider.KEY_BARCODE)).equals("") ? c_promo.getString(c_promo.getColumnIndex(PromosiBarangProvider.KEY_BARCODE)) : "");
                for(int i = 0; i < arrRingkasanOrderModels.size(); i++){
                    if(barcode.equals(arrRingkasanOrderModels.get(i).getKodeBarcode())){
                        countBarang++;
                    }
                }

            }while(c_promo.moveToNext());
            c_promo.close();
        }


        if(countBarang != countPromoBarang){
            if(listCekPromos.size() > 0){
                idxPromo = listIndexPromo.indexOf(kodePromo);
                if(idxPromo != -1){
                    listIndexPromo.remove(idxPromo);
                    listCekPromos.remove(idxPromo);
                }
            }
        }

    }
    //end



    private void showDialogTundaPenjualan(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Tampilkan data tunda penjualan ?")
                .setCancelable(false)
                .setPositiveButton("Ya",new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog,int id){

                        if(listBuatOrder == null){ //tab


                            if(!id_jual.toString().equals("")){
                                idJualHold = id_jual;
                                jenisPajak = jenis_pajak;
                            }

                            for(int i = 0; i < getRingkasanOrderModels.size(); i++){
                                addRingkasanHoldSale(getRingkasanOrderModels.get(i), "");
                            }



                        }else{

                            if(!id_jual.toString().equals("")){
                                infoBay = "hold";
                                jenisPajak = jenis_pajak;
                            }

                            ringkasanOrderModels = getRingkasanOrderModels;
                            updateListOrder();


                        }

                    }
                })

                .setNegativeButton("Tidak",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id){
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }



    @Override
    public void onDeleteInfoPromo() {

        ListPromoFragment promoFragment = (ListPromoFragment) getSupportFragmentManager().findFragmentById(R.id.promoFragment);
        if(promoFragment != null){
            promoFragment.clearInfoPromo();
        }

    }


    @Override
    public void getItemCategoryTab(ListCategoryModel listCategoryModel){
        ListProductFragment productFragment = (ListProductFragment) getSupportFragmentManager().findFragmentById(R.id.productFragment);
        if(productFragment != null){
            productFragment.setCategotyItem(listCategoryModel);
        }
    }


    private void updateListOrder(){

        double totqty = 0;
        cekPromoDiskon_test(ringkasanOrderModels);
        for(int i = 0; i < getRingkasanOrderModels.size(); i++){
            totqty = totqty + getRingkasanOrderModels.get(i).getQty();
        }

        total_qty = (int) totqty;
        updateShopingQty();
        textTotalBayar.setText(Utils.getCurrencyRupiah(getTotalBayar() + getTotTaxPpn(jenisPajak)));
    }



    private double getTotTaxPpn(String taxDesc){
        double totTaxPpn = 0; double rate = 0; double totRate = 0;
        for(int i = 0; i < ppnModels.size(); i++){
            if(taxDesc.equals(ppnModels.get(i).getDescPpn())){

                String URL = "content://com.trimitrasis.finosapps.ContentProvider.TaxProvider/tax";
                Uri uri_   = Uri.parse(URL);
                Cursor c   = getContentResolver().query(uri_, null, "id_tax_group " + " = '" + ppnModels.get(i).getIdPPn() + "'" , null, "_id");

                if(c.moveToFirst()){
                    do{
                        rate = Double.parseDouble(c.getString(c.getColumnIndex(TaxProvider.KEY_RATE)));
                        totRate = totRate + rate;
                    }while(c.moveToNext());
                    c.close();
                    totTaxPpn   = ((totRate/100) * getTotalBayar());
                }
            }
        }

        return totTaxPpn;
    }


    private void getDataTax(){
        String taxGroupName; String idTaxGroup;
        ppnModels.clear();
        String URL = "content://com.trimitrasis.finosapps.ContentProvider.TaxGroupProvider/taxgroup";
        Uri uri_   = Uri.parse(URL);
        Cursor c   = getContentResolver().query(uri_, null, null , null, "_id");

        if(c.moveToFirst()){
            do{
                idTaxGroup   = c.getString(c.getColumnIndex(TaxGroupProvider.KEY_ID_TAX_GROUP));
                taxGroupName = c.getString(c.getColumnIndex(TaxGroupProvider.KEY_DESCRIPTION));
                ppnModels.add(new PpnModel(idTaxGroup, taxGroupName));
            }while(c.moveToNext());
            c.close();
        }else{
            System.out.println("data tidak ditemukan!");
        }

    }


    @Override
    public void setValueDataOrder(InternalProductModel internalProductModel) {
        addOrderList(internalProductModel, "insert");
    }
}





