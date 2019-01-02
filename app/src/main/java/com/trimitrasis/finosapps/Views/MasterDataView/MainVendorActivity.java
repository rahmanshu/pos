package com.trimitrasis.finosapps.Views.MasterDataView;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import com.trimitrasis.finosapps.Connection.ApiConnection;
import com.trimitrasis.finosapps.R;
import com.trimitrasis.finosapps.Views.MasterDataView.Adapter.MainVendorAdapter;
import com.trimitrasis.finosapps.Views.MasterDataView.Models.TaxGroupModel;
import com.trimitrasis.finosapps.Views.MasterDataView.Models.VendorModel;
import com.trimitrasis.finosapps.Views.UtilView.Constants;
import com.trimitrasis.finosapps.Views.UtilView.FontUtils;
import com.trimitrasis.finosapps.Views.UtilView.Utils;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by rahman on 16/03/2017.
 */


@EActivity(R.layout.layout_main_vendor)
public class MainVendorActivity  extends AppCompatActivity implements MainVendorAdapter.CallbackDetailVendor{

    ArrayList<String> listCurrency = new ArrayList<>();
    ArrayList<String> listPaymentTerms = new ArrayList<>();
    String spinnerCurrencyString = "";
    String spinnerPaymentTermString = "";
    boolean isPkp = false;
    Object idVendor;

    View toolbar_popup;

    @ViewById
    Toolbar toolbar;

    @ViewById
    RecyclerView listVendor;

    RecyclerView.LayoutManager mLayoutManager;

    @ViewById
    EditText editSearchVendor;

    ArrayList<VendorModel> mainVendorModels;

    ProgressDialog progressDialog, progressDialogPayTerms, progressDialogCurrency, progressDialogTaxGroup;

    Dialog dialogVendor;
    LayoutInflater inflater;

    EditText editNamaVendor, editPhone1Vendor, editPhone2Vendor, editAddressVendor, editFaxVendor, editEmailVendor;
    EditText editReconVendor, editCreditLimit;
    Spinner spinnerCurrency, spinnerPaymentTerm, spinnerTaxGroup;
    CheckBox checkPkpVendor;
    TextView titleForm;

    Button buttonCancelPopup, buttonUpdatePopup, btnDeleteVendor;
    double creditLimit = 0;


    Button buttonCancel;
    Button buttonNew;
    TextView titleToolbar;
    int indexSpinnerCurrency = 0;
    int indexSpinnerPayTerm = 0;
    int indexSpinnerTaxGroup = 0;
    String taxGroupNameString;

    ArrayList<String> listTaxGroup = new ArrayList<>();
    ArrayList<TaxGroupModel> taxGroupModels;
    String spinnerTaxGroupString;
    String taxGroupIdString;

    @AfterViews
    void afterView(){
        mainVendorModels = new ArrayList<>();
        mainVendorModels.clear();
        taxGroupModels = new ArrayList<>();
        customView();
        initSetTableView();
        getViewVendor();
        getDataTaxGroup();
        getDataPaymentTerms();
        getDataCurrency();
        inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    protected void onResume(){
        super.onResume();
        if(Constants.flakAddVendor == 1){
            getViewVendor();
            Constants.flakAddVendor = 0;
        }
    }




    private void initSetTableView(){
        listVendor.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        listVendor.setLayoutManager(mLayoutManager);
    }

    private void initListSearch(){
        MainVendorAdapter adapter = new MainVendorAdapter(this, mainVendorModels);
        listVendor.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void customView(){
        buttonCancel = (Button) toolbar.findViewById(R.id.btnCancel);
        buttonNew = (Button) toolbar.findViewById(R.id.btnSave);
        buttonCancel.setText("Cancel");
        buttonNew.setText("New");
        titleToolbar = (TextView) toolbar.findViewById(R.id.titleForm);
        titleToolbar.setText("Vendor");
        editSearchVendor.setTypeface(FontUtils.getHelvetica_Neue_LT(this));
        buttonCancel.setTypeface(FontUtils.getHelvetica_Neue_LT(this));
        buttonNew.setTypeface(FontUtils.getHelvetica_Neue_LT(this));
        titleToolbar.setTypeface(FontUtils.getHelvetica_Neue_LT(this));
        buttonCancel.setOnClickListener(onClickListener);
        buttonNew.setOnClickListener(onClickListener);
    }


    View.OnClickListener onClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            switch (v.getId()) {
                case R.id.btnCancel:{
                    finish();
                }break;
                case R.id.btnSave:{
                    MasterVendorActivity_.intent(MainVendorActivity.this).start();
                }break;
                case R.id.btnUpdate:{
                    dialogVendor.dismiss();
                }break;
                case R.id.btnDelete:{
                    getUpdateVendor();
                }break;
                case R.id.btnDeleteVendor:{
                    showDialogDeleteVendor();
                }break;

            }
        }
    };



    private void getViewVendor(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading..");
        progressDialog.show();
        ApiConnection.getViewDataVendorInterface().getViewDataVendor(Constants.userInfoModel,new Callback<Response>() {
            @Override
            public void success(Response responsFinos, Response response) {
                String rawResponse = ApiConnection.getRawResponse(response);
                JSONObject vendorObj = null;
                try {
                    vendorObj = new JSONObject(rawResponse);
                    String jStatus  = vendorObj.optString("message");
                    JSONArray jResult = vendorObj.optJSONArray("result");
                    if(jStatus.equals("success")){
                        mainVendorModels.clear();
                        for(int i = 0; i < jResult.length(); i ++){
                            JSONObject jsonObject = jResult.optJSONObject(i);
                                     mainVendorModels.add(new VendorModel(
                                             jsonObject.optJSONObject("_id"),
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
                                             jsonObject.optBoolean("pkp"),
                                             jsonObject.optString("tax_group")));
                        }

                        initListSearch();

                    }else if(jStatus.equals("empty")){
                        Utils.showToast("Belum ada data yang ditambahkan!", MainVendorActivity.this);
                        if (progressDialog!=null)progressDialog.dismiss();
                    }else if(jStatus.equals("failed")){
                        Utils.showToast("Error.!", MainVendorActivity.this);
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
                if (progressDialog!=null)progressDialog.dismiss();
            }
        });
    }


    private void showDailogVendor(VendorModel vendorModel){
        dialogVendor = new Dialog(this);
        View viewsVendor = inflater.inflate(R.layout.popup_dialog_vendor, null);
        declareLayout(viewsVendor);
        setDataVendor(vendorModel);
        addListenerOnChkPkp();
        setFontDialog();
        dialogVendor.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogVendor.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#50000000")));
        dialogVendor.setContentView(viewsVendor);
        dialogVendor.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialogVendor.show();
        buttonCancelPopup.setOnClickListener(onClickListener);
        buttonUpdatePopup.setOnClickListener(onClickListener);
        btnDeleteVendor.setOnClickListener(onClickListener);
    }



    @Override
    public void getDataVendor(VendorModel vendorModel){
        showDailogVendor(vendorModel);
    }


    private void setDataVendor(VendorModel vendorModel){
        idVendor = vendorModel.get_id();
        editNamaVendor.setText(vendorModel.getName());
        editPhone1Vendor.setText(vendorModel.getPhone1());
        editPhone2Vendor.setText(vendorModel.getPhone2());
        editAddressVendor.setText(vendorModel.getAddress());
        editFaxVendor.setText(vendorModel.getFax());
        editEmailVendor.setText(vendorModel.getEmail());
        editReconVendor.setText(vendorModel.getRecon_account());
        editCreditLimit.setText(String.valueOf(vendorModel.getCredit_limit()));
        indexSpinnerPayTerm  = listPaymentTerms.indexOf(vendorModel.getPayment_terms());
        indexSpinnerCurrency = listCurrency.indexOf(vendorModel.getCurrency());
        getTaxGroupName(vendorModel.getTax_group());
        spinnerInitTaxGroup();
        spinnerInitCurrency();
        spinnerInitPaymentTerms();

        if(vendorModel.isPkp() == true){
            checkPkpVendor.setChecked(true);
        }else if(vendorModel.isPkp() == false){
            checkPkpVendor.setChecked(false);
        }

    }

    private void getTaxGroupName(String idTaxGroup){
        for(int i = 0; i < taxGroupModels.size(); i++){
            if(idTaxGroup.equals(taxGroupModels.get(i).get_id())){
                taxGroupNameString = taxGroupModels.get(i).getDescription();
            }
        }
        indexSpinnerTaxGroup = listTaxGroup.indexOf(taxGroupNameString);
    }


    private void declareLayout(View viewsVendor){
        toolbar_popup      = (View)     viewsVendor.findViewById(R.id.toolbar_popup);
        buttonCancelPopup  = (Button)   toolbar_popup.findViewById(R.id.btnUpdate);
        buttonUpdatePopup  = (Button)   toolbar_popup.findViewById(R.id.btnDelete);
        btnDeleteVendor    = (Button)   viewsVendor.findViewById(R.id.btnDeleteVendor);
        editNamaVendor     = (EditText) viewsVendor.findViewById(R.id.editNamaVendor);
        editPhone1Vendor   = (EditText) viewsVendor.findViewById(R.id.editPhone1Vendor);
        editPhone2Vendor   = (EditText) viewsVendor.findViewById(R.id.editPhone2Vendor);
        editAddressVendor  = (EditText) viewsVendor.findViewById(R.id.editAddressVendor);
        editFaxVendor      = (EditText) viewsVendor.findViewById(R.id.editFaxVendor);
        editEmailVendor    = (EditText) viewsVendor.findViewById(R.id.editEmailVendor);
        editReconVendor    = (EditText) viewsVendor.findViewById(R.id.editReconVendor);
        editCreditLimit    = (EditText) viewsVendor.findViewById(R.id.editCreditLimit);
        spinnerCurrency    = (Spinner)  viewsVendor.findViewById(R.id.spinnerCurrency);
        spinnerPaymentTerm = (Spinner)  viewsVendor.findViewById(R.id.spinnerPaymentTerm);
        spinnerTaxGroup    = (Spinner)  viewsVendor.findViewById(R.id.spinnerTaxGroup);
        checkPkpVendor     = (CheckBox) viewsVendor.findViewById(R.id.checkPkpVendor);
        titleForm          = (TextView) viewsVendor.findViewById(R.id.titleForm);
    }


    private void setFontDialog(){
        buttonCancelPopup.setTypeface(FontUtils.getHelvetica_Neue_LT(this));
        buttonUpdatePopup.setTypeface(FontUtils.getHelvetica_Neue_LT(this));
        editNamaVendor.setTypeface(FontUtils.getHelvetica_Neue_LT(this));
        editPhone1Vendor.setTypeface(FontUtils.getHelvetica_Neue_LT(this));
        editPhone2Vendor.setTypeface(FontUtils.getHelvetica_Neue_LT(this));
        editAddressVendor.setTypeface(FontUtils.getHelvetica_Neue_LT(this));
        editFaxVendor.setTypeface(FontUtils.getHelvetica_Neue_LT(this));
        editEmailVendor.setTypeface(FontUtils.getHelvetica_Neue_LT(this));
        editReconVendor.setTypeface(FontUtils.getHelvetica_Neue_LT(this));
        editCreditLimit.setTypeface(FontUtils.getHelvetica_Neue_LT(this));
        buttonCancelPopup.setText("Cancel");
        buttonUpdatePopup.setText("Update");
        titleForm.setText("Update");
    }


    private void spinnerInitTaxGroup(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, listTaxGroup){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                TextView view = (TextView) super.getView(position, convertView, parent);
                view.setTypeface(FontUtils.getFontHeaderToolbar(MainVendorActivity.this));
                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent){
                TextView view = (TextView)super.getDropDownView(position, convertView, parent);
                view.setTypeface(FontUtils.getFontHeaderToolbar(MainVendorActivity.this));
                return view;
            }
        };

        spinnerTaxGroup.setAdapter(adapter);
        spinnerTaxGroup.setSelection(indexSpinnerTaxGroup);
        spinnerTaxGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
                spinnerTaxGroupString = listTaxGroup.get(position);
                getIdTaxGroup(spinnerTaxGroupString);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent){

            }
        });
    }

    private void spinnerInitCurrency(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, listCurrency){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                TextView view = (TextView) super.getView(position, convertView, parent);
                view.setTypeface(FontUtils.getFontHeaderToolbar(MainVendorActivity.this));
                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent){
                TextView view = (TextView)super.getDropDownView(position, convertView, parent);
                view.setTypeface(FontUtils.getFontHeaderToolbar(MainVendorActivity.this));
                return view;
            }
        };

        spinnerCurrency.setAdapter(adapter);
        spinnerCurrency.setSelection(indexSpinnerCurrency);
        spinnerCurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
                spinnerCurrencyString = listCurrency.get(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent){

            }
        });

    }


    private void getIdTaxGroup(String taxGroupName){
        for(int i = 0; i < taxGroupModels.size(); i++){
            if(taxGroupName.equals(taxGroupModels.get(i).getDescription())){
                taxGroupIdString = taxGroupModels.get(i).get_id();
            }
        }
    }


    public void addListenerOnChkPkp(){
        checkPkpVendor.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (((CheckBox) v).isChecked()){
                    isPkp = true;
                }else{
                    isPkp = false;
                }
            }
        });
    }


    private void spinnerInitPaymentTerms(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, listPaymentTerms){

            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                TextView view = (TextView) super.getView(position, convertView, parent);
                view.setTypeface(FontUtils.getFontHeaderToolbar(MainVendorActivity.this));
                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent){
                TextView view = (TextView)super.getDropDownView(position, convertView, parent);
                view.setTypeface(FontUtils.getFontHeaderToolbar(MainVendorActivity.this));
                return view;
            }

        };

        spinnerPaymentTerm.setAdapter(adapter);
        spinnerPaymentTerm.setSelection(indexSpinnerPayTerm);
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


    private VendorModel getDataVendorModel(){
        VendorModel vendorModel = new VendorModel(
                idVendor,
                editNamaVendor.getText().toString(),
                editPhone1Vendor.getText().toString(),
                editPhone2Vendor.getText().toString(),
                editAddressVendor.getText().toString(),
                editFaxVendor.getText().toString(),
                editEmailVendor.getText().toString(),
                editReconVendor.getText().toString(),
                spinnerCurrencyString,
                creditLimit = (!editCreditLimit.getText().toString().equals("")) ? Double.parseDouble(editCreditLimit.getText().toString()) : 0,
                spinnerPaymentTermString,
                isPkp,
                taxGroupIdString,
                Constants.userInfoModel);
                //Utils.showLogI("customer model : "+ new Gson().toJson(vendorModel).toString());
        return vendorModel;
    }



    private void getUpdateVendor(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading..");
        progressDialog.show();
        ApiConnection.getEditVendorInterface().editVendorInterface(getDataVendorModel(), new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                String rawResponse = ApiConnection.getRawResponse(response);
                JSONObject vendorObj = null;
                try{
                    vendorObj = new JSONObject(rawResponse);
                    String jStatus  = vendorObj.optString("message");
                    JSONArray jResult = vendorObj.optJSONArray("result");

                    if(jStatus.equals("success")){
                        mainVendorModels.clear();
                        for(int i = 0; i < jResult.length(); i ++){
                            JSONObject jsonObject = jResult.optJSONObject(i);
                            mainVendorModels.add(new VendorModel(
                                    jsonObject.optJSONObject("_id"),
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
                                    jsonObject.optBoolean("pkp"),
                                    jsonObject.optString("tax_group")
                            ));
                        }
                        Utils.showToast("Data Vendor Sukses Diupdate!!", MainVendorActivity.this);
                        dialogVendor.dismiss();
                    }else{
                        Utils.showToast("Data Vendor gagal Diupdate", MainVendorActivity.this);
                        if (progressDialog!=null)progressDialog.dismiss();
                    }

                    initListSearch();

                }catch(JSONException e){
                    e.printStackTrace();
                    progressDialog.dismiss();
                }

                if (progressDialog!=null)progressDialog.dismiss();
            }

            @Override
            public void failure(RetrofitError error) {
                progressDialog.dismiss();
            }
        });
    }



    private void getDeleteVendor(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading..");
        progressDialog.show();
        ApiConnection.getDeleteVendorInterface().deleteVendorInterface(getDataVendorModel(), new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                String rawResponse = ApiConnection.getRawResponse(response);
                JSONObject vendorObj = null;
                try{
                    vendorObj = new JSONObject(rawResponse);
                    String jStatus  = vendorObj.optString("message");
                    JSONArray jResult = vendorObj.optJSONArray("result");

                    if(jStatus.equals("success")){
                        mainVendorModels.clear();
                        for(int i = 0; i < jResult.length(); i ++){
                            JSONObject jsonObject = jResult.optJSONObject(i);
                            mainVendorModels.add(new VendorModel(
                                    jsonObject.optJSONObject("_id"),
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
                                    jsonObject.optBoolean("pkp"),
                                    jsonObject.optString("tax_group")));
                        }
                        Utils.showToast("Data Vendor berhasil Didelete!", MainVendorActivity.this);
                        dialogVendor.dismiss();
                    }else{
                        Utils.showToast("Data Vendor gagal Didelete!", MainVendorActivity.this);
                        if (progressDialog!=null)progressDialog.dismiss();
                    }

                    initListSearch();

                }catch(JSONException e){
                    e.printStackTrace();
                    progressDialog.dismiss();
                }

                if (progressDialog!=null)progressDialog.dismiss();
            }

            @Override
            public void failure(RetrofitError error) {
                progressDialog.dismiss();
            }
        });
    }



    private void showDialogDeleteVendor(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Apakah anda yakin remove data vendor ?")
                .setCancelable(false).setPositiveButton("Ya",new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog,int id) {
                        getDeleteVendor();
                        dialog.dismiss();
                    }
                })

                .setNegativeButton("Tidak",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
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


    private void getDataCurrency(){

        progressDialogCurrency = new ProgressDialog(this);
        progressDialogCurrency.setMessage("Loading..");
        progressDialogCurrency.show();
        ApiConnection.getAllCurrencyInterface().getAllCurrency(Constants.userInfoModel, new Callback<Response>(){

            @Override
            public void success(Response response, Response response2) {
                String rawResponse = ApiConnection.getRawResponse(response);

                try{
                    JSONObject currencyObj = null;
                    currencyObj = new JSONObject(rawResponse);
                    String jStatus    = currencyObj.optString("message");
                    JSONArray jResult = currencyObj.optJSONArray("result");

                    if(jStatus.equals("success")){
                        listCurrency.clear();
                        if(jResult != null){
                            for(int i = 0; i < jResult.length(); i++){
                                JSONObject jsonObject = jResult.optJSONObject(i);
                                listCurrency.add(jsonObject.optString("id"));
                            }
                        }
                    }

                }catch(JSONException e){
                    e.printStackTrace();
                    progressDialogCurrency.dismiss();
                }

                if (progressDialogCurrency!=null)progressDialogCurrency.dismiss();
            }

            @Override
            public void failure(RetrofitError error) {
                if(progressDialogCurrency!=null)progressDialogCurrency.dismiss();
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
                        listTaxGroup.clear();
                        taxGroupModels.clear();
                        if(jResult != null){
                            for(int i = 0; i < jResult.length(); i++){
                                JSONObject jsonObject = jResult.optJSONObject(i);

                                JSONObject idObj  = (JSONObject) jsonObject.get("_id");
                                String strMongoId = (String)     idObj.get("$id");

                                taxGroupModels.add(new TaxGroupModel(
                                        strMongoId,
                                        jsonObject.optString("description")
                                ));

                                listTaxGroup.add(jsonObject.optString("description"));
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



}
