package com.trimitrasis.finosapps.Views.SalesQuotationView;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.trimitrasis.finosapps.Connection.ApiConnection;
import com.trimitrasis.finosapps.R;
import com.trimitrasis.finosapps.Views.InvoiceReceiptView.CustomView.ViewDetailItem;
import com.trimitrasis.finosapps.Views.SalesQuotationView.CustomView.ViewItemSalesQuotation;
import com.trimitrasis.finosapps.Views.SalesQuotationView.Models.BranchModel_;
import com.trimitrasis.finosapps.Views.SalesQuotationView.Models.BranchSqModel;
import com.trimitrasis.finosapps.Views.SalesQuotationView.Models.CustRateModel;
import com.trimitrasis.finosapps.Views.SalesQuotationView.Models.CustomerModel_;
import com.trimitrasis.finosapps.Views.SalesQuotationView.Models.CustomerSqModel;
import com.trimitrasis.finosapps.Views.SalesQuotationView.Models.DetailItemSqModel;
import com.trimitrasis.finosapps.Views.SalesQuotationView.Models.ItemConvertionSalesQuotModel;
import com.trimitrasis.finosapps.Views.SalesQuotationView.Models.ItemSalesQuotation;
import com.trimitrasis.finosapps.Views.SalesQuotationView.Models.SalesQuotModel;
import com.trimitrasis.finosapps.Views.SalesQuotationView.Models.TaxGroupCustModel;
import com.trimitrasis.finosapps.Views.SalesQuotationView.Models.TaxGroupModelNew;
import com.trimitrasis.finosapps.Views.SalesQuotationView.Models.TaxModel;
import com.trimitrasis.finosapps.Views.UtilView.Constants;
import com.trimitrasis.finosapps.Views.UtilView.DateUtils;
import com.trimitrasis.finosapps.Views.UtilView.FontUtils;
import com.trimitrasis.finosapps.Views.UtilView.Utils;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * Created by rahman on 06/03/2017.
 */


@EActivity(R.layout.layout_sales_quotation)
public class SalesQuotationActivity extends AppCompatActivity {

    ArrayList<ViewDetailItem> detailItemArrayList;

    @ViewById
    Button btnAddItem;

    @ViewById
    Toolbar toolbar;

    @ViewById
    LinearLayout contentViewDetailItem;

    @ViewById
    TextView quoteDate, validDate;

    @ViewById
    Spinner spinnerCustomer, spinnerBranch,  spinnerPaymentTerm;

    @ViewById
    EditText editTaxGroupCust, editDeliveryFrom, editDeliveryTo, editAddress, editContactNumber, editNotes;

    @ViewById
    TextView textSummaryTotal;

    @ViewById
    EditText editShippingCost;

    ArrayList<DetailItemSqModel> detailItemSqModelArrayList;
    ArrayList<ItemSalesQuotation> itemSalesQuotations;
    ArrayList<ItemConvertionSalesQuotModel> itemConvertionSalesQuotModels;
    ArrayList<ViewItemSalesQuotation> viewItemArrayList;
    ArrayList<TaxGroupCustModel> taxGroupCustModels;
    ArrayList<TaxGroupModelNew> taxGroupModels;
    ArrayList<CustomerSqModel> customerSqModels;
    ArrayList<BranchSqModel> branchNameArr;
    ArrayList<TaxModel> taxModels;
    ArrayList<CustRateModel> custRateModels;

    ArrayList<String> listPaymentTerms = new ArrayList<>();
    ArrayList<String> listTaxGroupString = new ArrayList<>();
    ArrayList<String> listCustomerString;
    ArrayList<String> listBranchString;
    ArrayList<String> listItemString;
    ArrayList<String> listUomItemString;

    String customerIdString, branchIdString;
    String customerNameString, branchString, taxGroupString, itemSalesString, uomItemString;
    String quoteDateString, validDateString, taxItem, spinnerPaymentTermString, taxGroupDescString;
    String taxGroupId_;

    ProgressDialog progressDialog, progressDialogTaxGroup, progressDialogSalesQuotation, progressDialogTax, progressDialogPayTerms;

    Dialog dialogAddItem;
    LayoutInflater inflater;
    Button btnCancelItem, btnSaveItem;
    Spinner spinnerItem,  spinnerUom;
    EditText editQuantity, editPrice, editDiscount, editTotal, editTaxItem;
    ViewItemSalesQuotation viewItemSalesQuotation;
    int increment_sales_item = 0;
    double qtyItem, priceItem, discountItem, totalItem;
    double qtyItemSq, priceItemSq, discountItemSq, totalItemSq;
    double shippingCost = 0; double totBayarItem = 0;
    String date_now, time_now;


    ArrayList<CustomerModel_> customerModelArrayList;
    ArrayList<BranchModel_> branchModelArrayList;

    @AfterViews
    void afterView(){
        detailItemArrayList = new ArrayList<>();
        customView();
        branchNameArr                 = new ArrayList<>();
        listCustomerString            = new ArrayList<>();
        listBranchString              = new ArrayList<>();
        itemSalesQuotations           = new ArrayList<>();
        listItemString                = new ArrayList<>();
        listUomItemString             = new ArrayList<>();
        viewItemArrayList             = new ArrayList<>();
        itemConvertionSalesQuotModels = new ArrayList<>();
        detailItemSqModelArrayList    = new ArrayList<>();
        taxGroupCustModels            = new ArrayList<>();
        customerSqModels              = new ArrayList<>();
        taxModels                     = new ArrayList<>();
        taxGroupModels                = new ArrayList<>();
        custRateModels                = new ArrayList<>();

        customerModelArrayList        = new ArrayList<>();
        branchModelArrayList          = new ArrayList<>();

        inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        getDataCustomerSample();
        getDataCustomer();
        getAllTax();
        getDataPaymentTerms();
        getDataTaxGroup();
        editTotalBayarOnChange();
        getDateTime();
        editDeliveryFrom.setText(Constants.userInfoModel.getComname());
    }


    @Override
    protected void onResume(){
        super.onResume();
    }


    private void customView(){
        Button buttonCancel = (Button) toolbar.findViewById(R.id.btnCancel);
        buttonCancel.setText("Cancel");
        TextView titleFormSales = (TextView) toolbar.findViewById(R.id.titleForm);
        titleFormSales.setText("Sales Quotation");
        Button buttonSave = (Button) toolbar.findViewById(R.id.btnSave);
        buttonSave.setText("Save");
        buttonCancel.setOnClickListener(onClickListener);
        buttonSave.setOnClickListener(onClickListener);
    }



    View.OnClickListener onClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            switch (v.getId()) {
                case R.id.btnCancel:{
                    finish();
                }break;
                case R.id.btnSave:{
                    addDataSalesQuotation();
                }break;
                case R.id.btnSaveItem:{
                    setViewSalesItem();
                    textSummaryTotal.setText(""+ (getSummaryTotalItem() + shippingCost));
                }break;
                case R.id.btnCancelItem:{
                    dialogAddItem.dismiss();
                }break;
            }
        }
    };



    private void spinnerCustomerName(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, listCustomerString){

            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                TextView view = (TextView) super.getView(position, convertView, parent);
                view.setTypeface(FontUtils.getFontHeaderToolbar(SalesQuotationActivity.this));
                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent){
                TextView view = (TextView)super.getDropDownView(position, convertView, parent);
                view.setTypeface(FontUtils.getFontHeaderToolbar(SalesQuotationActivity.this));
                return view;
            }

        };

        spinnerCustomer.setAdapter(adapter);
        spinnerCustomer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
                customerNameString = listCustomerString.get(position);
                getCustomerId(customerNameString);
                getBranch(customerNameString);
                getDataTaxGroupDesc(customerNameString);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }


    private void spinnerBranchName(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, listBranchString){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                TextView view = (TextView) super.getView(position, convertView, parent);
                view.setTypeface(FontUtils.getFontHeaderToolbar(SalesQuotationActivity.this));
                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent){
                TextView view = (TextView)super.getDropDownView(position, convertView, parent);
                view.setTypeface(FontUtils.getFontHeaderToolbar(SalesQuotationActivity.this));
                return view;
            }
        };

        spinnerBranch.setAdapter(adapter);
        spinnerBranch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
                branchString = listBranchString.get(position);
                getBranchId(branchString);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void spinnerUomName(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, listUomItemString){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                TextView view = (TextView) super.getView(position, convertView, parent);
                view.setTypeface(FontUtils.getFontHeaderToolbar(SalesQuotationActivity.this));
                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent){
                TextView view = (TextView)super.getDropDownView(position, convertView, parent);
                view.setTypeface(FontUtils.getFontHeaderToolbar(SalesQuotationActivity.this));
                return view;
            }
        };

        spinnerUom.setAdapter(adapter);
        spinnerUom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
                uomItemString = listUomItemString.get(position);
                getPriceFromUom(itemSalesString, uomItemString);
                getAkumulasiTotalItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent){

            }
        });
    }


    private void spinnerItem(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, listItemString){

            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                TextView view = (TextView) super.getView(position, convertView, parent);
                view.setTypeface(FontUtils.getFontHeaderToolbar(SalesQuotationActivity.this));
                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent){
                TextView view = (TextView)super.getDropDownView(position, convertView, parent);
                view.setTypeface(FontUtils.getFontHeaderToolbar(SalesQuotationActivity.this));
                return view;
            }
        };

        spinnerItem.setAdapter(adapter);
        spinnerItem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
                itemSalesString = listItemString.get(position);
                getDetailItem(itemSalesString);
                getTaxGroupItem(itemSalesString);
                getAkumulasiTotalItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent){

            }
        });
    }


    private void getBranchId(String branchString){
        for(int i = 0; i < branchNameArr.size(); i++){
            if(branchString.equals(branchNameArr.get(i).getBranchName())){
                branchIdString = branchNameArr.get(i).getBranchId();
            }
        }
    }


    private void getCustomerId(String customerName){
        for(int i = 0; i < customerSqModels.size(); i++){
            if(customerName.equals(customerSqModels.get(i).getName())){
                customerIdString = customerSqModels.get(i).getCustId();
            }
        }
    }

    private void getBranch(String customerName){
        listBranchString.clear();
        if(branchNameArr != null){
            for(int i = 0; i < branchNameArr.size(); i++){
                if(customerName.equals(branchNameArr.get(i).getCustName())){
                    listBranchString.add(branchNameArr.get(i).getBranchName());
                }
            }
        }else{
            listBranchString.add("Data Branch kosong!");
        }
        spinnerBranchName();
    }


    private void getDataTaxGroupDesc(String customerName){
        for(int i = 0; i < customerSqModels.size(); i++){
            if(customerSqModels.get(i).getName().equals(customerName)){
                taxGroupString = customerSqModels.get(i).getTax_group();
                getTaxGroup(taxGroupString);
                //cekTaxGroupCustomer(taxGroupString); //new
                break;
            }
        }
    }


    private void getTaxGroupItem(String item_id){
        for(int i = 0; i < itemSalesQuotations.size(); i++){
            if(item_id.equals(itemSalesQuotations.get(i).getId())){
                taxGroupId_ = itemSalesQuotations.get(i).getTax_group();
                getDescTaxGroup(taxGroupId_);
            }
        }
    }


    private void getDescTaxGroup(String taxGroupId){
        String taxGroupName = "";
        for(int i = 0; i < taxGroupModels.size(); i++){
            if(taxGroupId.equals(taxGroupModels.get(i).getIdTaxGroup())){
                taxGroupName = taxGroupModels.get(i).getDescription();
            }
        }
        editTaxItem.setText(taxGroupName);
        editTaxItem.setEnabled(false);
    }


    private void getDetailItem(String item_id){
        listUomItemString.clear();
        for(int i = 0; i < itemConvertionSalesQuotModels.size(); i++){
            if(item_id.equals(itemConvertionSalesQuotModels.get(i).getItem_id())){
                listUomItemString.add(itemConvertionSalesQuotModels.get(i).getUom());
            }
        }
        spinnerUomName();
    }


    private void getPriceFromUom(String item_id, String uom){
        for(int j = 0; j < itemConvertionSalesQuotModels.size(); j++){
            if(item_id.equals(itemConvertionSalesQuotModels.get(j).getItem_id()) && uom.equals(itemConvertionSalesQuotModels.get(j).getUom())){
                editPrice.setText(""+itemConvertionSalesQuotModels.get(j).getHarga_jual());
                editPrice.setEnabled(false);
            }
        }
    }


    //sample
    private void getDataCustomerSample(){

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading..");
        progressDialog.show();

        ApiConnection.getCustomerSqSInterface().getCustomerSalesQuotation(Constants.userInfoModel, new Callback<Response>(){

            @Override
            public void success(Response response, Response response2){

                String rawResponse = ApiConnection.getRawResponse(response);
                JSONObject sqObj = null;

                try{
                    sqObj = new JSONObject(rawResponse);
                    String jStatus    = sqObj.optString("message");
                    JSONArray jResult = sqObj.optJSONArray("result");

                    if(jStatus.equals("success")){

                        customerModelArrayList.clear();
                        branchModelArrayList.clear();

                        int k = 0;
                        for(int i = 0; i < jResult.length(); i++){

                            JSONObject jsonObject = jResult.optJSONObject(i);
                            JSONObject idObj      = (JSONObject) jsonObject.get("_id");
                            String strCustId      = (String) idObj.get("$id");

                            JSONArray  jsonArrBranch = jsonObject.optJSONArray("branch");
                            if(jsonArrBranch != null){
                                for(int j = 0; j < jsonArrBranch.length(); j++){

                                    JSONObject jsonObjectBranch = jsonArrBranch.optJSONObject(j);
                                    JSONObject idObjBranch      = jsonObjectBranch.getJSONObject("branch_id");
                                    String brancId              = (String) idObjBranch.get("$id");

                                    branchModelArrayList.add(k, new BranchModel_(
                                            jsonObject.optJSONObject("_id"),
                                            brancId,
                                            jsonObject.optString("nama"),
                                            jsonObject.optString("address"),
                                            jsonObject.optString("phone1"),
                                            jsonObject.optString("phone2"),
                                            jsonObject.optString("fax"),
                                            jsonObject.optString("email")
                                    ));

                                    k++;
                                }
                            }

                            customerModelArrayList.add(new CustomerModel_(
                                    jsonObject.optJSONObject("_id"),
                                    strCustId,
                                    jsonObject.optString("name"),
                                    jsonObject.optString("phone1"),
                                    jsonObject.optString("phone2"),
                                    jsonObject.optString("address"),
                                    jsonObject.optString("fax"),
                                    jsonObject.optString("email"),
                                    jsonObject.optString("recon_account"),
                                    jsonObject.optString("currency"),
                                    jsonObject.optDouble("credit_limit"),
                                    jsonObject.optString("payment_terms"),
                                    jsonObject.optString("tax_group"),
                                    jsonObject.optString("pkp"),
                                    branchModelArrayList
                            ));


                        }


                        Utils.showLogI("customer model : "+ new Gson().toJson(customerModelArrayList).toString());

                    }

                }catch(JSONException e){
                    e.printStackTrace();
                    progressDialog.dismiss();
                }

                if (progressDialog!=null)progressDialog.dismiss();
            }


            @Override
            public void failure(RetrofitError error){

            }

        });

    }

    //end


    private void getDataCustomer(){

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading..");
        progressDialog.show();

        ApiConnection.getCustomerSqSInterface().getCustomerSalesQuotation(Constants.userInfoModel, new Callback<Response>(){

            @Override
            public void success(Response response, Response response2){

                String rawResponse = ApiConnection.getRawResponse(response);
                JSONObject sqObj = null;

                try{
                    sqObj = new JSONObject(rawResponse);
                    String jStatus    = sqObj.optString("message");
                    JSONArray jResult = sqObj.optJSONArray("result");

                    if(jStatus.equals("success")){

                        listCustomerString.clear();
                        branchNameArr.clear();
                        customerSqModels.clear();

                        int k = 0;
                        for(int i = 0; i < jResult.length(); i++){

                            JSONObject jsonObject = jResult.optJSONObject(i);
                            JSONObject idObj      = (JSONObject) jsonObject.get("_id");
                            String strCustId      = (String)     idObj.get("$id");

                            JSONArray  jsonArrBranch = jsonObject.optJSONArray("branch");
                            if(jsonArrBranch != null){
                                for(int j = 0; j < jsonArrBranch.length(); j++){

                                    JSONObject jsonObjectBranch = jsonArrBranch.optJSONObject(j);
                                    JSONObject idObjBranch      = jsonObjectBranch.getJSONObject("branch_id");
                                    String brancId              = (String) idObjBranch.get("$id");

                                    branchNameArr.add(k, new BranchSqModel(
                                            jsonObject.optJSONObject("_id"),
                                            brancId,
                                            jsonObject.optString("name"),
                                            jsonObjectBranch.optString("nama")
                                    ));

                                    k++;
                                }
                            }

                            customerSqModels.add(new CustomerSqModel(
                                    jsonObject.optJSONObject("_id"),
                                    strCustId,
                                    jsonObject.optString("name"),
                                    jsonObject.optString("phone1"),
                                    jsonObject.optString("phone2"),
                                    jsonObject.optString("address"),
                                    jsonObject.optString("fax"),
                                    jsonObject.optString("email"),
                                    jsonObject.optString("recon_account"),
                                    jsonObject.optString("currency"),
                                    jsonObject.optDouble("credit_limit"),
                                    jsonObject.optString("payment_terms"),
                                    jsonObject.optString("tax_group"),
                                    jsonObject.optString("pkp")
                            ));

                            listCustomerString.add(jsonObject.optString("name"));
                        }

                        spinnerCustomerName();
                    }

                }catch(JSONException e){
                    e.printStackTrace();
                    progressDialog.dismiss();
                }

                if (progressDialog!=null)progressDialog.dismiss();
            }


            @Override
            public void failure(RetrofitError error){

            }

        });

    }


    private void getTaxGroup(final String taxGroupNameString){

        if(taxGroupModels != null){
            for(int i = 0; i < taxGroupModels.size(); i++){
                if(taxGroupNameString.equals(taxGroupModels.get(i).getIdTaxGroup())){
                    taxGroupDescString = taxGroupModels.get(i).getDescription();
                }
            }
            editTaxGroupCust.setText(taxGroupDescString);
            editTaxGroupCust.setEnabled(false);
        }
    }


    private void getAllTax(){

        progressDialogTax = new ProgressDialog(this);
        progressDialogTax.setMessage("Loading..");
        progressDialogTax.show();

        ApiConnection.getTaxInterface().getDataTax(Constants.userInfoModel, new Callback<Response>(){

            @Override
            public void success(Response response, Response response2) {
                String rawResponse = ApiConnection.getRawResponse(response);
                JSONObject sqObj = null;

                try{
                    sqObj = new JSONObject(rawResponse);
                    String jStatus    = sqObj.optString("message");
                    JSONArray jResult = sqObj.optJSONArray("result");

                    if(jStatus.equals("success")){
                        taxModels.clear();
                        if(jResult != null){
                            for(int i = 0; i < jResult.length(); i++){
                                JSONObject jsonObject = jResult.optJSONObject(i);

                                JSONObject idObj  = (JSONObject) jsonObject.get("_id");
                                String strMongoId = (String) idObj.get("$id");

                                taxModels.add(new TaxModel(
                                        jsonObject.optJSONObject("_id"),
                                        strMongoId,
                                        jsonObject.optString("id"),
                                        jsonObject.optString("description"),
                                        jsonObject.optString("rate"),
                                        jsonObject.optString("create_by"),
                                        jsonObject.optString("create_date"),
                                        jsonObject.optString("create_time")
                                ));
                            }
                        }
                    }

                }catch(JSONException e){
                    e.printStackTrace();
                    progressDialogTax.dismiss();
                }

                if (progressDialogTax!=null)progressDialogTax.dismiss();
            }

            @Override
            public void failure(RetrofitError error) {
                if (progressDialogTax!=null)progressDialogTax.dismiss();
            }
        });
    }


    private void getItemSalesQuotation(){

        progressDialogSalesQuotation = new ProgressDialog(this);
        progressDialogSalesQuotation.setMessage("Loading..");
        progressDialogSalesQuotation.show();

        ApiConnection.getItemSalesQuotationInterface().getDataItemSalesQuot(Constants.userInfoModel, new Callback<Response>(){

            @Override
            public void success(Response response, Response response2){

                String rawResponse = ApiConnection.getRawResponse(response);
                JSONObject sqObj = null;

                try{
                    sqObj = new JSONObject(rawResponse);
                    String jStatus    = sqObj.optString("message");
                    JSONArray jResult = sqObj.optJSONArray("result");

                    if(jStatus.equals("success")){

                        itemConvertionSalesQuotModels.clear();
                        itemSalesQuotations.clear();
                        listItemString.clear();

                        int l = 0;
                        for(int i = 0; i < jResult.length(); i++){

                            JSONObject jsonObject      = jResult.optJSONObject(i);
                            JSONArray  jsonItemConvert = jsonObject.optJSONArray("item_convertion");

                            if(jsonItemConvert != null){
                                for(int j = 0; j < jsonItemConvert.length(); j++){
                                    JSONObject jsonObjectItemCon = jsonItemConvert.optJSONObject(j);
                                    itemConvertionSalesQuotModels.add(l, new ItemConvertionSalesQuotModel(
                                            jsonObjectItemCon.optString("uom"),
                                            jsonObjectItemCon.optDouble("uom_convertion"),
                                            jsonObjectItemCon.optDouble("base_uom_convertion"),
                                            jsonObjectItemCon.optDouble("harga_jual"),
                                            jsonObjectItemCon.optString("barcode"),
                                            jsonObject.optString("id")
                                    ));
                                    l++;
                                }
                            }


                            itemSalesQuotations.add(i, new ItemSalesQuotation(
                                    jsonObject.optString("id"),
                                    jsonObject.optString("s_description"),
                                    jsonObject.optString("l_description"),
                                    jsonObject.optString("base_uom"),
                                    jsonObject.optString("item_group"),
                                    jsonObject.optString("item_hierarchy"),
                                    jsonObject.optString("account_class"),
                                    jsonObject.optString("create_by"),
                                    jsonObject.optString("create_date"),
                                    jsonObject.optString("create_time"),
                                    jsonObject.optString("tax_group")
                            ));

                            listItemString.add(jsonObject.optString("id"));
                        }

                        spinnerItem();

                    }

                }catch(JSONException e){
                    e.printStackTrace();
                    progressDialogSalesQuotation.dismiss();
                }

                if (progressDialogSalesQuotation!=null)progressDialogSalesQuotation.dismiss();

            }

            @Override
            public void failure(RetrofitError error) {
                if (progressDialogSalesQuotation!=null)progressDialogSalesQuotation.dismiss();
            }
        });
    }


    private void showDailogItem(){
        dialogAddItem = new Dialog(this);
        View viewsItem = inflater.inflate(R.layout.popup_sales_quotation, null);
        btnCancelItem      = (Button)   viewsItem.findViewById(R.id.btnCancelItem);
        btnSaveItem        = (Button)   viewsItem.findViewById(R.id.btnSaveItem);
        spinnerItem        = (Spinner)  viewsItem.findViewById(R.id.spinnerItem);
        editQuantity       = (EditText) viewsItem.findViewById(R.id.editQuantity);
        spinnerUom         = (Spinner)  viewsItem.findViewById(R.id.spinnerUom);
        editPrice          = (EditText) viewsItem.findViewById(R.id.editPrice);
        editTaxItem        = (EditText) viewsItem.findViewById(R.id.editTaxItem);
        editDiscount       = (EditText) viewsItem.findViewById(R.id.editDiscount);
        editTotal          = (EditText) viewsItem.findViewById(R.id.editTotal);

        btnCancelItem.setOnClickListener(onClickListener);
        btnSaveItem.setOnClickListener(onClickListener);
        dialogAddItem.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogAddItem.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#50000000")));
        dialogAddItem.setContentView(viewsItem);
        dialogAddItem.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialogAddItem.show();
    }


    private void setViewSalesItem(){
        viewItemSalesQuotation = new ViewItemSalesQuotation(this);
        viewItemSalesQuotation.setTextItemName(itemSalesString);
        viewItemSalesQuotation.setTextUom(uomItemString);
        viewItemSalesQuotation.setTextTotal(editTotal.getText().toString());
        viewItemArrayList.add(increment_sales_item, viewItemSalesQuotation);
        contentViewDetailItem.addView(viewItemArrayList.get(increment_sales_item));
        increment_sales_item++;
        dialogAddItem.dismiss();
        addDetailItemToList();
    }


    private void addDetailItemToList(){
        detailItemSqModelArrayList.add(getDetailItemModel());
    }


    private void cekTaxGroupCustomer(String taxItem){
        for(int i = 0; i < taxGroupModels.size(); i++){
            if(taxItem.equals(taxGroupModels.get(i).getDescription())){
                System.out.println("tax group id!!!" + taxGroupModels.get(i).getIdTaxGroup());
            }
        }
    }


    private DetailItemSqModel getDetailItemModel(){
        DetailItemSqModel detailItemSqModel = new DetailItemSqModel(
                itemSalesString,
                qtyItem      = (!editQuantity.getText().toString().equals("")) ? Double.parseDouble(editQuantity.getText().toString()) : 0, uomItemString,
                priceItem    = (!editPrice.getText().toString().equals("")) ? Double.parseDouble(editPrice.getText().toString()) : 0,
                taxItem      = (!taxGroupId_.equals("")) ? String.valueOf(taxGroupId_) : "",
                discountItem = (!editDiscount.getText().toString().equals("")) ? Double.parseDouble(editDiscount.getText().toString()) : 0,
                totalItem    = (!editTotal.getText().toString().equals("")) ? Double.parseDouble(editTotal.getText().toString()) : 0
        );

        return detailItemSqModel;
    }


    private double getSummaryTotalItem(){
        double subTotalBayar = 0;
        if(detailItemSqModelArrayList != null){
            for(int i = 0; i < detailItemSqModelArrayList.size(); i++){
                subTotalBayar = subTotalBayar + detailItemSqModelArrayList.get(i).getTotal();
            }
        }else {
            subTotalBayar = 0;
        }
        return  subTotalBayar;
    }


    @Click
    void quoteDate(){
        DateUtils.showDatePickerDialog(this.getSupportFragmentManager(), new DateUtils.DateDialogPickerListener() {
            @Override
            public void onDatePick(String date, String month, String year) {
                quoteDateString = year + "-" + DateUtils.getMonthNumber(month) + "-" + DateUtils.getDateNumberString(date);
                quoteDate.setText(date + " " + DateUtils.getMonthString(month) + " " + year);
                quoteDate.setTypeface(FontUtils.getFontHeaderToolbar(SalesQuotationActivity.this));
            }
        }, DateUtils.DatePickerFragment.TYPE_BIRTH_DATE);
    }


    @Click
    void validDate(){
        DateUtils.showDatePickerDialog(this.getSupportFragmentManager(), new DateUtils.DateDialogPickerListener() {
            @Override
            public void onDatePick(String date, String month, String year) {
                validDateString = year + "-" + DateUtils.getMonthNumber(month) + "-" + DateUtils.getDateNumberString(date);
                validDate.setText(date + " " + DateUtils.getMonthString(month) + " " + year);
                validDate.setTypeface(FontUtils.getFontHeaderToolbar(SalesQuotationActivity.this));
            }
        }, DateUtils.DatePickerFragment.TYPE_BIRTH_DATE);
    }


    @Click
    void btnAddItem(){
        showDailogItem();
        getItemSalesQuotation();
        editQtyOnChange();
        editDiscountOnChange();
    }


    private void getDataPaymentTerms(){

        progressDialogPayTerms = new ProgressDialog(this);
        progressDialogPayTerms.setMessage("Loading..");
        progressDialogPayTerms.show();

        ApiConnection.getPaymentTermInterface().getDataPaymentTerms(Constants.userInfoModel, new Callback<Response>(){

            @Override
            public void success(Response response, Response response2) {
                String rawResponse = ApiConnection.getRawResponse(response);

                try{
                    JSONObject payObj = null;
                    payObj = new JSONObject(rawResponse);
                    String jStatus    = payObj.optString("message");
                    JSONArray jResult = payObj.optJSONArray("result");

                    if(jStatus.equals("success")){
                        listPaymentTerms.clear();
                        if(jResult != null){
                            for(int i = 0; i < jResult.length(); i++){
                                JSONObject jsonObject = jResult.optJSONObject(i);
                                listPaymentTerms.add(jsonObject.optString("name"));
                            }
                        }
                        spinnerInitPaymentTerms();
                    }

                }catch(JSONException e){
                    e.printStackTrace();
                    progressDialogPayTerms.dismiss();
                }

                if (progressDialogPayTerms!=null)progressDialogPayTerms.dismiss();
            }

            @Override
            public void failure(RetrofitError error) {
                if(progressDialogPayTerms!=null)progressDialogPayTerms.dismiss();
            }
        });
    }



    private void spinnerInitPaymentTerms(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, listPaymentTerms){

            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                TextView view = (TextView) super.getView(position, convertView, parent);
                view.setTypeface(FontUtils.getFontHeaderToolbar(SalesQuotationActivity.this));
                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent){
                TextView view = (TextView)super.getDropDownView(position, convertView, parent);
                view.setTypeface(FontUtils.getFontHeaderToolbar(SalesQuotationActivity.this));
                return view;
            }

        };

        spinnerPaymentTerm.setAdapter(adapter);
        spinnerPaymentTerm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
                spinnerPaymentTermString = listPaymentTerms.get(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent){

            }
        });
    }



    private void getDataTaxGroup(){

        progressDialogTaxGroup = new ProgressDialog(this);
        progressDialogTaxGroup.setMessage("Loading..");
        progressDialogTaxGroup.show();

        ApiConnection.getTaxGroupInterface().getDataTaxGroup(Constants.userInfoModel, new Callback<Response>(){

            @Override
            public void success(Response response, Response response2) {
                String rawResponse = ApiConnection.getRawResponse(response);

                try{
                    JSONObject taxGroupObj = null;
                    taxGroupObj = new JSONObject(rawResponse);
                    String jStatus    = taxGroupObj.optString("message");
                    JSONArray jResult = taxGroupObj.optJSONArray("result");

                    if(jStatus.equals("success")){
                        listTaxGroupString.clear();
                        taxGroupModels.clear();
                        if(jResult != null){
                            for(int i = 0; i < jResult.length(); i++){
                                JSONObject jsonObject = jResult.optJSONObject(i);

                                JSONObject idObj  = (JSONObject) jsonObject.get("_id");
                                String strMongoId = (String)     idObj.get("$id");

                                taxGroupModels.add(new TaxGroupModelNew(
                                        strMongoId,
                                        jsonObject.optString("description")
                                ));

                                listTaxGroupString.add(jsonObject.optString("description"));
                            }
                        }
                    }

                }catch(JSONException e){
                    e.printStackTrace();
                    progressDialogTaxGroup.dismiss();
                }

                if (progressDialogTaxGroup!=null)progressDialogTaxGroup.dismiss();
            }

            @Override
            public void failure(RetrofitError error) {
                if(progressDialogTaxGroup!=null)progressDialogTaxGroup.dismiss();
            }
        });
    }



    public void editTotalBayarOnChange(){
        editShippingCost.addTextChangedListener(new TextWatcher(){

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after){
            }

            @Override
            public void onTextChanged(CharSequence query, int start, int before, int count){

                try{
                    if(query.toString().equals("")){
                        shippingCost = 0;
                    }else{
                        shippingCost = Double.parseDouble(query.toString());
                    }

                    totBayarItem = getSummaryTotalItem() + shippingCost;
                    textSummaryTotal.setText("" + totBayarItem);

                }catch(NumberFormatException e){
                }
            }

            @Override
            public void afterTextChanged(Editable s){

            }
        });
    }


    public void editQtyOnChange(){
        editQuantity.addTextChangedListener(new TextWatcher(){

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after){
            }

            @Override
            public void onTextChanged(CharSequence query, int start, int before, int count){

                try{
                    if(query.toString().equals("")){
                        qtyItemSq = 0;
                    }else{
                        qtyItemSq = Double.parseDouble(query.toString());
                    }

                    priceItemSq = Double.parseDouble(editPrice.getText().toString());
                    totalItemSq = (qtyItemSq * priceItemSq) - discountItemSq;
                    editTotal.setText(""+totalItemSq);
                    editTotal.setEnabled(false);


                }catch(NumberFormatException e){
                }
            }

            @Override
            public void afterTextChanged(Editable s){

            }
        });
    }


    public void editDiscountOnChange(){
        editDiscount.addTextChangedListener(new TextWatcher(){

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after){
            }

            @Override
            public void onTextChanged(CharSequence query, int start, int before, int count){
                try{
                    if(query.toString().equals("")){
                        discountItemSq = 0;
                    }else{
                        discountItemSq = Double.parseDouble(query.toString());
                    }

                    priceItemSq = Double.parseDouble(editPrice.getText().toString());
                    totalItemSq = (qtyItemSq * priceItemSq) - discountItemSq;
                    editTotal.setText(""+totalItemSq);
                    editTotal.setEnabled(false);

                }catch(NumberFormatException e){
                }
            }

            @Override
            public void afterTextChanged(Editable s){

            }
        });
    }


    private void getAkumulasiTotalItem(){
        if(!editPrice.getText().toString().equals("")){
            priceItemSq = Double.parseDouble(editPrice.getText().toString());
        }else{
            priceItemSq = 0;
        }

        if(!editQuantity.getText().toString().equals("")){
            qtyItemSq = Double.parseDouble(editQuantity.getText().toString());
        }else{
            qtyItemSq = 0;
        }

        if(!editDiscount.getText().toString().equals("")){
            discountItemSq = Double.parseDouble(editDiscount.getText().toString());
        }else{
            discountItemSq = 0;
        }

        totalItemSq = (qtyItemSq * priceItemSq) - discountItemSq;
        editTotal.setText(""+totalItemSq);
        editTotal.setEnabled(false);
    }



    private void addDataSalesQuotation(){

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading..");
        progressDialog.show();

        ApiConnection.geInsertSalesQuotationInterface().insertSalesQuotation(getSalesQuotModel(), new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                String rawResponse = ApiConnection.getRawResponse(response);
                JSONObject loginObj = null;
                try {
                    loginObj = new JSONObject(rawResponse);
                    String jStatus  = loginObj.optString("message");

                    if(jStatus.equals("success")){
                        Utils.showToast("Sales Quotation Success!!", SalesQuotationActivity.this);
                    }else{
                        Utils.showToast("Login failed", SalesQuotationActivity.this);
                        if (progressDialog!=null)progressDialog.dismiss();
                    }

                }catch(JSONException e){
                    e.printStackTrace();
                    progressDialog.dismiss();
                }

                if (progressDialog!=null)progressDialog.dismiss();
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }


    private void getDateTime(){
        DateFormat df_date = new SimpleDateFormat("yyyy-MM-dd");
        date_now = df_date.format(new Date());

        DateFormat df_time = new SimpleDateFormat("HH:mm:ss");
        time_now = df_time.format(new Date());
    }



    private SalesQuotModel getSalesQuotModel(){
        SalesQuotModel salesQuotModel = new SalesQuotModel(
                customerIdString,
                customerNameString,
                branchIdString,
                branchString,
                spinnerPaymentTermString,
                quoteDateString,
                validDateString,
                editTaxGroupCust.getText().toString(),
                editDeliveryFrom.getText().toString(),
                editDeliveryTo.getText().toString(),
                editAddress.getText().toString(),
                editContactNumber.getText().toString(),
                editNotes.getText().toString(),
                totBayarItem,
                0,
                Constants.userInfoModel.getEmail(),
                date_now,
                time_now,
                "trans No",
                detailItemSqModelArrayList,
                Constants.userInfoModel
        );

        return salesQuotModel;
    }



}
