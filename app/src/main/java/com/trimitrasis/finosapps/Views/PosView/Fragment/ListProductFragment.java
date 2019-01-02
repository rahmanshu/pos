package com.trimitrasis.finosapps.Views.PosView.Fragment;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.speech.RecognizerIntent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.zxing.integration.android.IntentIntegrator;
import com.trimitrasis.finosapps.R;
import com.trimitrasis.finosapps.Views.ContentProvider.ItemProvider;
import com.trimitrasis.finosapps.Views.PosView.Adapter.ListProductAdapterGrid;
import com.trimitrasis.finosapps.Views.PosView.Model.InternalProductModel;
import com.trimitrasis.finosapps.Views.PosView.Model.ListCategoryModel;
import com.trimitrasis.finosapps.Views.UtilView.Utils;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by rahman on 29/03/2017.
 */


@EFragment(R.layout.fragment_column_product)
public class ListProductFragment extends Fragment {

    @ViewById
    ImageView btnBarcode;

    @ViewById
    RecyclerView listProduct;

    @ViewById
    static EditText searchProduct;

    private final int REQ_CODE_SPEECH_INPUT = 100;


    @ViewById
    ImageButton btnSpeech;


    RecyclerView.LayoutManager mLayoutManager;
    ArrayList<InternalProductModel> arrInternalProduct;
    ArrayList<InternalProductModel> arrProductModels;

    CallbackFragmentProduct callbackFragmentProduct;

    @AfterViews
    void afterView(){
        arrInternalProduct = new ArrayList<>();
        arrInternalProduct.clear();
        initSetTableView();
        initListAwal();
        addTextListener();
        getAllProductFromDevice();

        callbackFragmentProduct = (CallbackFragmentProduct) getActivity();
    }

    private void initSetTableView(){
        listProduct.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        listProduct.setLayoutManager(mLayoutManager);
    }


    private void initListAwal(){
        ListProductAdapterGrid adapterGrid = new ListProductAdapterGrid(getActivity(), arrInternalProduct);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 3);
        listProduct.setLayoutManager(mLayoutManager);
        listProduct.setItemAnimator(new DefaultItemAnimator());
        listProduct.setAdapter(adapterGrid);
    }


    private void initSearch(){
        ListProductAdapterGrid adapterGrid = new ListProductAdapterGrid(getActivity(), arrProductModels);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 3);
        listProduct.setLayoutManager(mLayoutManager);
        listProduct.setItemAnimator(new DefaultItemAnimator());
        listProduct.setAdapter(adapterGrid);
    }


    public static void setViewBarcode(String result){
        try{

            searchProduct.setText(result);

        }catch (NullPointerException e){
            e.getMessage();
        }
    }



    @Click
    void btnSpeech(){
        promptSpeechInput();
    }



    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 1000);
        intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS, 1000);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "finos corporation");

        try{
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        }catch(ActivityNotFoundException a) {
            Toast.makeText(getActivity(), "Sorry, your device doesn\'t support", Toast.LENGTH_SHORT).show();
        }
    }



    @Click
    void btnBarcode(){
        new IntentIntegrator(getActivity()).initiateScan();
    }


    public void addTextListener(){
        searchProduct.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after){
            }

            @Override
            public void onTextChanged(CharSequence query, int start, int before, int count){
                query = query.toString().toLowerCase();
                arrProductModels = new ArrayList<>();
                arrProductModels.clear();

                for(int i = 0; i < arrInternalProduct.size(); i++){

                    final String itemDesc   = arrInternalProduct.get(i).getDescription().toLowerCase();
                    final String barcode    = arrInternalProduct.get(i).getBarcode().toLowerCase();

                    if(barcode.contains(query) || itemDesc.contains(query)){
                        InternalProductModel internalProductModel = new InternalProductModel(
                                arrInternalProduct.get(i).getItem_id(),
                                arrInternalProduct.get(i).getStock_id(),
                                arrInternalProduct.get(i).getDescription(),
                                arrInternalProduct.get(i).getBase_uom(),
                                arrInternalProduct.get(i).getItem_group(),
                                arrInternalProduct.get(i).getItem_hierarchy(),
                                arrInternalProduct.get(i).getUom(),
                                arrInternalProduct.get(i).getUom_convertion(),
                                arrInternalProduct.get(i).getBase_uom_convertion(),
                                arrInternalProduct.get(i).getHarga_jual(),
                                arrInternalProduct.get(i).getBarcode(),
                                arrInternalProduct.get(i).getTax_group(),
                                arrInternalProduct.get(i).getStandart_cost(),
                                arrInternalProduct.get(i).isFlag_qty(),
                                arrInternalProduct.get(i).isFlag_bom(),
                                arrInternalProduct.get(i).getItem_hierarchy_ancestor(),
                                R.mipmap.image_produk,
                                arrInternalProduct.get(i).getFile_ext(),
                                arrInternalProduct.get(i).getFile_name(),
                                arrInternalProduct.get(i).getFull_path(),
                                arrInternalProduct.get(i).getFile_path(),
                                arrInternalProduct.get(i).getDetail_id()
                        );
                        arrProductModels.add(internalProductModel);

                        if(barcode.equals(query) || itemDesc.equals(query)){
                            setDataOrder(internalProductModel);
                            searchProduct.setText("");
                        }

                    }
                }

                initSearch();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

        });
    }


    public interface CallbackFragmentProduct{
        void setValueDataOrder(InternalProductModel internalProductModel);
    }

    private void setDataOrder(InternalProductModel internalProductModel){
        callbackFragmentProduct.setValueDataOrder(internalProductModel);
    }

    private void getAllProductFromDevice(){
        arrInternalProduct.clear();
        String stock_id = "", description = "", base_uom = "", item_group = "", item_hierarchy = "", uom = "";
        String barcode = "", tax_group = "", item_hierarchy_cat = "", standart_cost = "";
        String fileExt = "", file_name = "", fullPath = "",  filePath = "", detailId = "", item_id = "";
        double uom_convertion, base_uom_convertion, harga_jual, standart_cost_;
        int flag_qty, flag_sell, flag_bom;
        boolean flag_quantity = false; boolean flag_billofmat = false;
        String URL = "content://com.trimitrasis.finosapps.ContentProvider.ItemProvider/item";
        Uri uri_ = Uri.parse(URL);
        Cursor c = getActivity().getContentResolver().query(uri_, null, null , null, "_id");

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
                flag_qty            = c.getInt(c.getColumnIndex(ItemProvider.KEY_FLAG_QTY));
                flag_sell           = c.getInt(c.getColumnIndex(ItemProvider.KEY_FLAG_SELL));
                flag_bom            = c.getInt(c.getColumnIndex(ItemProvider.KEY_FLAG_BOM));
                item_hierarchy_cat  = c.getString(c.getColumnIndex(ItemProvider.KEY_ITEM_HIERARCHY_CAT));
                fileExt             = c.getString(c.getColumnIndex(ItemProvider.KEY_FILE_EXT));
                file_name           = c.getString(c.getColumnIndex(ItemProvider.KEY_FILE_NAME));
                fullPath            = c.getString(c.getColumnIndex(ItemProvider.KEY_FULL_PATH));
                filePath            = c.getString(c.getColumnIndex(ItemProvider.KEY_FILE_PATH));
                detailId            = c.getString(c.getColumnIndex(ItemProvider.KEY_DETAIL_ID));
                flag_quantity       = (flag_qty == 1) ? true : false;
                flag_billofmat      = (flag_bom == 1) ? true : false;
                standart_cost_      = (!standart_cost.toString().equals("")) ? Double.parseDouble(standart_cost) : 0;

                if(flag_sell == 1){
                    arrInternalProduct.add(new InternalProductModel(item_id, stock_id, description, base_uom, item_group, item_hierarchy, uom,
                            uom_convertion, base_uom_convertion, harga_jual, barcode, tax_group, standart_cost_,
                            flag_quantity, flag_billofmat, item_hierarchy_cat, R.mipmap.image_produk, fileExt, file_name, fullPath, filePath, detailId));
                }


            }while (c.moveToNext());

        }else{
            Utils.showToast("Data Kosong, Lalukan sinkronisasi data terlebih dahulu!", getActivity());
        }

        initListAwal();

    }



    public void setCategotyItem(ListCategoryModel listCategoryModel){

        arrInternalProduct.clear();
        int flag_qty, flag_sell, flag_bom;
        String fileExt = "", file_name = "", fullPath = "",  filePath = "", detailId = "", item_id = "";
        String stock_id = "", description = "", base_uom = "", item_group = "", item_hierarchy = "";
        String uom = "",  barcode = "", tax_group = "", item_hierarchy_cat = "";
        double uom_convertion, base_uom_convertion, harga_jual, standart_cost;
        boolean flag_quantity = false; boolean flag_billofmat = false;
        String URL = "content://com.trimitrasis.finosapps.ContentProvider.ItemProvider/item";
        Uri uri_ = Uri.parse(URL);
        Cursor c;

        if(listCategoryModel.getIdCategory().toString().equals("all")){
            c = getActivity().getContentResolver().query(uri_, null, null, null, "_id");
        }else{
            c = getActivity().getContentResolver().query(uri_, null, "item_hierarchy_ancestor " + " = '" +  listCategoryModel.getIdCategory() + "'" , null, "_id");
        }

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
                standart_cost       = c.getDouble(c.getColumnIndex(ItemProvider.KEY_STANDART_COST));
                flag_qty            = c.getInt(c.getColumnIndex(ItemProvider.KEY_FLAG_QTY));
                flag_sell           = c.getInt(c.getColumnIndex(ItemProvider.KEY_FLAG_SELL));
                flag_bom            = c.getInt(c.getColumnIndex(ItemProvider.KEY_FLAG_BOM));
                item_hierarchy_cat  = c.getString(c.getColumnIndex(ItemProvider.KEY_ITEM_HIERARCHY_CAT));
                fileExt             = c.getString(c.getColumnIndex(ItemProvider.KEY_FILE_EXT));
                file_name           = c.getString(c.getColumnIndex(ItemProvider.KEY_FILE_NAME));
                fullPath            = c.getString(c.getColumnIndex(ItemProvider.KEY_FULL_PATH));
                filePath            = c.getString(c.getColumnIndex(ItemProvider.KEY_FILE_PATH));
                detailId            = c.getString(c.getColumnIndex(ItemProvider.KEY_DETAIL_ID));
                flag_quantity       = (flag_qty == 1) ? true : false;
                flag_billofmat      = (flag_bom == 1) ? true : false;

                if(flag_sell == 1){
                    arrInternalProduct.add(new InternalProductModel(item_id, stock_id, description, base_uom, item_group, item_hierarchy,
                            uom, uom_convertion, base_uom_convertion, harga_jual, barcode, tax_group, standart_cost, flag_quantity, flag_billofmat,
                            item_hierarchy_cat, R.mipmap.image_produk, fileExt, file_name, fullPath, filePath, detailId));
                }


            }while(c.moveToNext());

        }else{
            //Utils.showToast("Data Kosong, Lalukan sinkronisasi data terlebih dahulu!", getActivity());
        }

        initListAwal();

    }


}
