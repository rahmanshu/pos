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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.trimitrasis.finosapps.Connection.ApiConnection;
import com.trimitrasis.finosapps.Connection.Models.CompanyModel;
import com.trimitrasis.finosapps.R;
import com.trimitrasis.finosapps.Views.MasterDataView.Adapter.MainCustomerAdapter;
import com.trimitrasis.finosapps.Views.MasterDataView.CustomView.ViewBranchItem;
import com.trimitrasis.finosapps.Views.MasterDataView.Models.BranchModel;
import com.trimitrasis.finosapps.Views.MasterDataView.Models.CustomerModel;
import com.trimitrasis.finosapps.Views.MasterDataView.Models.TaxGroupModel;
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
import java.util.List;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * Created by rahman on 13/03/2017.
 */


@EActivity(R.layout.layout_main_customer)
public class MainCustomerActivity extends AppCompatActivity implements
                                            MainCustomerAdapter.CallbackDetailCustomer,
                                            ViewBranchItem.CallbackBranchItem{

    ArrayList<String> listCurrency = new ArrayList<>();
    ArrayList<String> listPaymentTerms = new ArrayList<>();
    String spinnerCurrencyString = "";
    String spinnerPaymentTermString = "";
    boolean isPkp = false;
    Object idCustomer;
    List<CompanyModel> companyModels;

    @ViewById
    Toolbar toolbar;

    @ViewById
    RecyclerView listCustomer;

    RecyclerView.LayoutManager mLayoutManager;

    @ViewById
    EditText editSearchBilling;

    ArrayList<CustomerModel> mainCustomerModels;

    ProgressDialog progressDialog, progressDialogPayTerms, progressDialogCurrency, progressDialogTaxGroup;

    Dialog dialogCustomer, dialogBranchItem;
    LayoutInflater inflater;
    View toolbar_popup;

    TextView titleForm;
    EditText editNamaCustomer, editPhone1Customer, editPhone2Customer, editAddressCustomer, editFaxCustomer, editEmailCustomer;
    EditText editNamaBranch, editAddressBranch, editPhone1Branch, editPhone2Branch, editFaxBranch, editEmailBranch;
    EditText editReconCustomer, editCreditLimit;

    Spinner spinnerCurrency, spinnerPaymentTerm, spinnerTaxGroup;
    CheckBox checkPkpCustomer;
    LinearLayout contentViewBranchItem;
    ViewBranchItem viewBranchItem;
    Button btnAddBranch, btnClearBranch;
    ArrayList<ViewBranchItem> viewBranchItemArrayList;

    List<BranchModel> branchModels;
    List<BranchModel> arrBranchModels;
    ArrayList<TaxGroupModel> taxGroupModels;
    ArrayList<String> listTaxGroup = new ArrayList<>();
    String spinnerTaxGroupString;
    String taxGroupIdString, taxGroupNameString;

    Button buttonCancelCust, buttonUpdateCust, btnDeleteBranch, buttonSaveBranch, buttonCancelPopup;
    double creditLimit = 0;
    int positionBranch = 0;
    int indexSpinnerCurrency = 0;
    int indexSpinnerPayTerm = 0;
    int indexSpinnerTaxGroup = 0;

    Dialog dialogAddBranch;
    Button btnCancelBranch, btnSaveBranch;
    EditText namaBranch, addressBranch, phone1Branch, phone2Branch, faxBranch, emailBranch;


    @AfterViews
    void afterView(){
        mainCustomerModels = new ArrayList<>();
        mainCustomerModels.clear();
        companyModels = new ArrayList<>();
        companyModels.clear();
        viewBranchItemArrayList = new ArrayList<>();
        viewBranchItemArrayList.clear();
        branchModels = new ArrayList<>();
        branchModels.clear();
        arrBranchModels = new ArrayList<>();
        arrBranchModels.clear();
        taxGroupModels = new ArrayList<>();
        customView();
        initSetTableView();
        getViewCustomer();
        getDataPaymentTerms();
        getDataCurrency();
        getDataTaxGroup();
        inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    protected void onResume(){
        super.onResume();
        if(Constants.flakAddCustomer == 1){
            getViewCustomer();
            Constants.flakAddCustomer = 0;
        }
    }



    private void initSetTableView(){
        listCustomer.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        listCustomer.setLayoutManager(mLayoutManager);
    }


    private void initListSearch(){
        MainCustomerAdapter adapter = new MainCustomerAdapter(this, mainCustomerModels);
        listCustomer.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


    private void customView(){
        Button buttonCancel = (Button) toolbar.findViewById(R.id.btnCancel);
        Button buttonNew = (Button) toolbar.findViewById(R.id.btnSave);
        buttonCancel.setText("Cancel");
        buttonNew.setText("New");
        TextView titleToolbar = (TextView) toolbar.findViewById(R.id.titleForm);
        titleToolbar.setText("Customer");
        buttonCancel.setOnClickListener(onClickListener);
        buttonNew.setOnClickListener(onClickListener);
    }



    View.OnClickListener onClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            switch (v.getId()){
                case R.id.btnCancel:{
                    finish();
                }break;
                case R.id.btnSave:{
                    MasterCustomerActivity_.intent(MainCustomerActivity.this).start();
                }break;
                case R.id.btnUpdate:{   //buttonCancelCust
                    dialogCustomer.dismiss();
                }break;
                case R.id.btnDelete:{   //alias buttonUpdateCust
                    getUpdateCustomer();
                }break;
                case R.id.btnAddBranch:{
                    showDailogAddBranch();
                }break;
                case R.id.btnClearBranch:{
                    showDialogPopupClearBranch();
                }break;
                case R.id.btnDeleteBranch:{
                    deleteBranch();
                }break;
                case R.id.btnSaveUpdateBranch:{
                    updateBranch();
                }break;
                case  R.id.btnCancelPopup:{
                    dialogBranchItem.dismiss();
                }break;
                case R.id.btnSaveBranch:{
                    if(isValidInputBranch()){
                        addRowBranch();
                    }
                }break;
                case R.id.btnCancelBranch:{
                    dialogAddBranch.dismiss();
                }break;
            }
        }
    };



    private void getViewCustomer(){

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading..");
        progressDialog.show();
        ApiConnection.getViewDataCustomerInterface().getViewDataCustomer(Constants.userInfoModel,new Callback<Response>() {
            @Override
            public void success(Response responsFinos, Response response){
                String rawResponse = ApiConnection.getRawResponse(response);
                JSONObject vendorObj = null;
                try {
                    vendorObj = new JSONObject(rawResponse);
                    String jStatus  = vendorObj.optString("message");
                    JSONArray jResult = vendorObj.optJSONArray("result");

                    if(jStatus.equals("success")){

                        mainCustomerModels.clear();

                        int k = 0;
                        for(int i = 0; i < jResult.length(); i ++){

                            JSONObject jsonObject   = jResult.optJSONObject(i);
                            JSONArray jsonArrBranch = jsonObject.optJSONArray("branch");

                            if(jsonArrBranch != null){
                                for(int j = 0; j < jsonArrBranch.length(); j++){
                                    JSONObject jsonObjectBranch = jsonArrBranch.optJSONObject(j);

                                    branchModels.add(k, new BranchModel(
                                            jsonObject.optJSONObject("_id"),
                                            jsonObjectBranch.optString("nama"),
                                            jsonObjectBranch.optString("address"),
                                            jsonObjectBranch.optString("phone1"),
                                            jsonObjectBranch.optString("phone2"),
                                            jsonObjectBranch.optString("fax"),
                                            jsonObjectBranch.optString("email")));
                                    k++;
                                }
                            }


                            mainCustomerModels.add(i, new CustomerModel(
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
                                    )
                            );

                        }

                        initListSearch();

                    }else if(jStatus.equals("empty")){
                        Utils.showToast("Belum ada data yang ditambahkan!", MainCustomerActivity.this);
                        if (progressDialog!=null)progressDialog.dismiss();
                    }else if(jStatus.equals("failed")){
                        Utils.showToast("Error.!", MainCustomerActivity.this);
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


    @Override
    public void getDataCustomer(CustomerModel customerModel) {
        showDailogCustomer(customerModel);
    }


    private void showDailogCustomer(CustomerModel customerModel){
        dialogCustomer = new Dialog(this);
        View viewsCustomer = inflater.inflate(R.layout.popup_dialog_customer, null);
        declareLayout(viewsCustomer);
        setDataCustomer(customerModel);
        addListenerOnChkPkp();
        setFontDialog();
        dialogCustomer.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogCustomer.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#50000000")));
        dialogCustomer.setContentView(viewsCustomer);
        dialogCustomer.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialogCustomer.show();
        buttonCancelCust.setOnClickListener(onClickListener);
        buttonUpdateCust.setOnClickListener(onClickListener);
    }


    private void declareLayout(View viewsCustomer){
        toolbar_popup         = (View)         viewsCustomer.findViewById(R.id.toolbar_popup);
        buttonCancelCust      = (Button)       toolbar_popup.findViewById(R.id.btnUpdate);
        buttonUpdateCust      = (Button)       toolbar_popup.findViewById(R.id.btnDelete);
        titleForm             = (TextView)     toolbar_popup.findViewById(R.id.titleForm);
        editNamaCustomer      = (EditText)     viewsCustomer.findViewById(R.id.editNamaCustomer);
        editPhone1Customer    = (EditText)     viewsCustomer.findViewById(R.id.editPhone1Customer);
        editPhone2Customer    = (EditText)     viewsCustomer.findViewById(R.id.editPhone2Customer);
        editAddressCustomer   = (EditText)     viewsCustomer.findViewById(R.id.editAddressCustomer);
        editFaxCustomer       = (EditText)     viewsCustomer.findViewById(R.id.editFaxCustomer);
        editEmailCustomer     = (EditText)     viewsCustomer.findViewById(R.id.editEmailCustomer);
        editReconCustomer     = (EditText)     viewsCustomer.findViewById(R.id.editReconCustomer);
        editCreditLimit       = (EditText)     viewsCustomer.findViewById(R.id.editCreditLimit);
        spinnerCurrency       = (Spinner)      viewsCustomer.findViewById(R.id.spinnerCurrency);
        spinnerPaymentTerm    = (Spinner)      viewsCustomer.findViewById(R.id.spinnerPaymentTerm);
        spinnerTaxGroup       = (Spinner)      viewsCustomer.findViewById(R.id.spinnerTaxGroup);
        checkPkpCustomer      = (CheckBox)     viewsCustomer.findViewById(R.id.checkPkpCustomer);
        btnAddBranch          = (Button)       viewsCustomer.findViewById(R.id.btnAddBranch);
        btnClearBranch        = (Button)       viewsCustomer.findViewById(R.id.btnClearBranch);
        contentViewBranchItem = (LinearLayout) viewsCustomer.findViewById(R.id.contentViewBranchItem);
        titleForm.setText("Customer");
        btnClearBranch.setText("Clear Branch");
        btnAddBranch.setText("Add Branch");
        buttonCancelCust.setText("Cancel");
        buttonUpdateCust.setText("Update");
        btnClearBranch.setOnClickListener(onClickListener);
        btnAddBranch.setOnClickListener(onClickListener);
    }


    private void setDataCustomer(CustomerModel customerModel){

        idCustomer = customerModel.get_id();
        editNamaCustomer.setText(customerModel.getName());
        editPhone1Customer.setText(customerModel.getPhone1());
        editPhone2Customer.setText(customerModel.getPhone2());
        editAddressCustomer.setText(customerModel.getAddress());
        editFaxCustomer.setText(customerModel.getFax());
        editEmailCustomer.setText(customerModel.getEmail());
        editReconCustomer.setText(customerModel.getRecon_account());
        editCreditLimit.setText(String.valueOf(customerModel.getCredit_limit()));
        indexSpinnerPayTerm  = listPaymentTerms.indexOf(customerModel.getPayment_terms());
        indexSpinnerCurrency = listCurrency.indexOf(customerModel.getCurrency());
        getTaxGroupName(customerModel.getTax_group());

        spinnerInitCurrency();
        spinnerInitPaymentTerms();
        spinnerInitTaxGroup();

        if(customerModel.isPkp() == true){
            checkPkpCustomer.setChecked(true);
        }else if(customerModel.isPkp() == false){
            checkPkpCustomer.setChecked(false);
        }

        //show data branch
        if(branchModels != null){
            arrBranchModels.clear();
            for(int i = 0; i < branchModels.size(); i++){
                if(branchModels.get(i).get_id().equals(customerModel.get_id())){
                    BranchModel branchModel = new BranchModel(
                            null,
                            branchModels.get(i).getName(),
                            branchModels.get(i).getAddress(),
                            branchModels.get(i).getPhone1(),
                            branchModels.get(i).getPhone2(),
                            branchModels.get(i).getFax(),
                            branchModels.get(i).getEmail()
                    );
                    arrBranchModels.add(branchModel);
                }
            }

            setViewBranchItem(arrBranchModels);
        }
    }


    private void setFontDialog(){
        buttonCancelCust.setTypeface(FontUtils.getHelvetica_Neue_LT(this));
        buttonUpdateCust.setTypeface(FontUtils.getHelvetica_Neue_LT(this));
        editNamaCustomer.setTypeface(FontUtils.getHelvetica_Neue_LT(this));
        editPhone1Customer.setTypeface(FontUtils.getHelvetica_Neue_LT(this));
        editPhone2Customer.setTypeface(FontUtils.getHelvetica_Neue_LT(this));
        editAddressCustomer.setTypeface(FontUtils.getHelvetica_Neue_LT(this));
        editFaxCustomer.setTypeface(FontUtils.getHelvetica_Neue_LT(this));
        editEmailCustomer.setTypeface(FontUtils.getHelvetica_Neue_LT(this));
        editReconCustomer.setTypeface(FontUtils.getHelvetica_Neue_LT(this));
        editCreditLimit.setTypeface(FontUtils.getHelvetica_Neue_LT(this));
    }


    private void setViewBranchItem(List<BranchModel> branchModel){
        revomeBranchItem();
        arrBranchModels = branchModel;
        for(int i = 0; i < arrBranchModels.size(); i++){
            viewBranchItem = new ViewBranchItem(this, arrBranchModels.get(i), i);
            viewBranchItem.setTextNameBranch(arrBranchModels.get(i).getName());
            viewBranchItem.setTextBranchEmail(arrBranchModels.get(i).getEmail());
            viewBranchItem.setTextBranchPhone(arrBranchModels.get(i).getPhone1());
            viewBranchItemArrayList.add(i, viewBranchItem);
            contentViewBranchItem.addView(viewBranchItemArrayList.get(i));
        }
    }

    private void revomeBranchItem(){
        viewBranchItemArrayList.clear();
        contentViewBranchItem.removeAllViews();
    }


    private void spinnerInitTaxGroup(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, listTaxGroup){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                TextView view = (TextView) super.getView(position, convertView, parent);
                view.setTypeface(FontUtils.getFontHeaderToolbar(MainCustomerActivity.this));
                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent){
                TextView view = (TextView)super.getDropDownView(position, convertView, parent);
                view.setTypeface(FontUtils.getFontHeaderToolbar(MainCustomerActivity.this));
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
                view.setTypeface(FontUtils.getFontHeaderToolbar(MainCustomerActivity.this));
                return view;
            }
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent){
                TextView view = (TextView)super.getDropDownView(position, convertView, parent);
                view.setTypeface(FontUtils.getFontHeaderToolbar(MainCustomerActivity.this));
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

    private void getTaxGroupName(String idTaxGroup){
        for(int i = 0; i < taxGroupModels.size(); i++){
            if(idTaxGroup.equals(taxGroupModels.get(i).get_id())){
                taxGroupNameString = taxGroupModels.get(i).getDescription();
            }
        }
        indexSpinnerTaxGroup = listTaxGroup.indexOf(taxGroupNameString);
    }


    public void addListenerOnChkPkp(){
        checkPkpCustomer.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(((CheckBox) v).isChecked()){
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
                view.setTypeface(FontUtils.getFontHeaderToolbar(MainCustomerActivity.this));
                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent){
                TextView view = (TextView)super.getDropDownView(position, convertView, parent);
                view.setTypeface(FontUtils.getFontHeaderToolbar(MainCustomerActivity.this));
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


    private CustomerModel getDataCustomerModel(){
        CustomerModel customerModel = new CustomerModel(
                idCustomer,
                editNamaCustomer.getText().toString(),
                editPhone1Customer.getText().toString(),
                editPhone2Customer.getText().toString(),
                editAddressCustomer.getText().toString(),
                editFaxCustomer.getText().toString(),
                editEmailCustomer.getText().toString(),
                editReconCustomer.getText().toString(),
                spinnerCurrencyString,
                creditLimit = (!editCreditLimit.getText().toString().equals("")) ? Double.parseDouble(editCreditLimit.getText().toString()) : 0,
                spinnerPaymentTermString,
                isPkp,
                taxGroupIdString,
                arrBranchModels,
                Constants.userInfoModel);
                return customerModel;
    }


    private void getUpdateCustomer(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading..");
        progressDialog.show();
        ApiConnection.getEditCustomerInterface().editCustomerInterface(getDataCustomerModel(), new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                String rawResponse = ApiConnection.getRawResponse(response);
                JSONObject vendorObj = null;
                try{
                    vendorObj = new JSONObject(rawResponse);
                    String jStatus  = vendorObj.optString("message");
                    JSONArray jResult = vendorObj.optJSONArray("result");

                    if(jStatus.equals("success")){
                        mainCustomerModels.clear();
                        int k = 0;
                        for(int i = 0; i < jResult.length(); i ++){
                            JSONObject jsonObject   = jResult.optJSONObject(i);
                            JSONArray jsonArrBranch = jsonObject.optJSONArray("branch"); //new

                            if(jsonArrBranch != null){
                                for(int j = 0; j < jsonArrBranch.length(); j++){
                                    JSONObject jsonObjectBranch = jsonArrBranch.optJSONObject(j);
                                    branchModels.add(k, new BranchModel(
                                            jsonObject.optJSONObject("_id"),
                                            jsonObjectBranch.optString("nama"),
                                            jsonObjectBranch.optString("address"),
                                            jsonObjectBranch.optString("phone1"),
                                            jsonObjectBranch.optString("phone2"),
                                            jsonObjectBranch.optString("fax"),
                                            jsonObjectBranch.optString("email")));
                                    k++;
                                }
                            }

                            mainCustomerModels.add(new CustomerModel(
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
                        Utils.showToast("Data Customer Sukses Diupdate!!", MainCustomerActivity.this);
                        dialogCustomer.dismiss();
                    }else{
                        Utils.showToast("Data Customer gagal Diupdate", MainCustomerActivity.this);
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


    private void showDialogDeleteCustomer(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Apakah anda yakin remove data customer ?").setCancelable(false).setPositiveButton("Ya",new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog,int id) {
                getDeleteCustomer();
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



    private void showDialogPopupClearBranch(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Apakah anda yakin remove data branch ?").setCancelable(false).setPositiveButton("Ya",new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog,int id) {
                revomeBranchItem();
                arrBranchModels.clear();
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


    private void getDeleteCustomer(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading..");
        progressDialog.show();
        ApiConnection.getDeleteCustomerInterface().deleteCustomerInterface(getDataCustomerModel(), new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                String rawResponse = ApiConnection.getRawResponse(response);
                JSONObject vendorObj = null;
                try{
                    vendorObj = new JSONObject(rawResponse);
                    String jStatus  = vendorObj.optString("message");
                    JSONArray jResult = vendorObj.optJSONArray("result");

                    if(jStatus.equals("success")){
                        mainCustomerModels.clear();
                        for(int i = 0; i < jResult.length(); i ++){
                            JSONObject jsonObject = jResult.optJSONObject(i);
                            mainCustomerModels.add(new CustomerModel(
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
                        Utils.showToast("Data Customer berhasil Didelete!", MainCustomerActivity.this);
                        dialogCustomer.dismiss();
                    }else{
                        Utils.showToast("Data Customer gagal Didelete!", MainCustomerActivity.this);
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


    @Override
    public void callBranchItem(BranchModel branchModel, int position){
        showDailogBranchItem(branchModel, position);
    }


    private void showDailogBranchItem(BranchModel branchModel, int position){
        dialogBranchItem = new Dialog(this);
        View viewsItemBranch = inflater.inflate(R.layout.popup_dialog_branch_item, null);
        declareLayoutBranch(viewsItemBranch);
        positionBranch = position;
        setDataBranch(branchModel);
        setFontDialogBranch();
        dialogBranchItem.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogBranchItem.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#50000000")));
        dialogBranchItem.setContentView(viewsItemBranch);
        dialogBranchItem.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialogBranchItem.show();
    }


    private void declareLayoutBranch(View viewsItemBranch){
        toolbar_popup         = (View)     viewsItemBranch.findViewById(R.id.toolbar_popup);
        btnDeleteBranch       = (Button)   viewsItemBranch.findViewById(R.id.btnDeleteBranch);
        buttonSaveBranch      = (Button)   viewsItemBranch.findViewById(R.id.btnSaveUpdateBranch);
        buttonCancelPopup     = (Button)   toolbar_popup.findViewById(R.id.btnCancelPopup);
        titleForm             = (TextView) viewsItemBranch.findViewById(R.id.titleFormPopup);
        editNamaBranch        = (EditText) viewsItemBranch.findViewById(R.id.editNamaBranch);
        editAddressBranch     = (EditText) viewsItemBranch.findViewById(R.id.editAddressBranch);
        editPhone1Branch      = (EditText) viewsItemBranch.findViewById(R.id.editPhone1Branch);
        editPhone2Branch      = (EditText) viewsItemBranch.findViewById(R.id.editPhone2Branch);
        editFaxBranch         = (EditText) viewsItemBranch.findViewById(R.id.editFaxBranch);
        editEmailBranch       = (EditText) viewsItemBranch.findViewById(R.id.editEmailBranch);
        btnDeleteBranch.setOnClickListener(onClickListener);
        buttonSaveBranch.setOnClickListener(onClickListener);
        buttonCancelPopup.setOnClickListener(onClickListener);
        btnDeleteBranch.setText("Delete");
        titleForm.setText("Update Branch");
    }


    private void setFontDialogBranch(){
        btnDeleteBranch.setTypeface(FontUtils.getHelvetica_Neue_LT(this));
        buttonSaveBranch.setTypeface(FontUtils.getHelvetica_Neue_LT(this));
        editNamaBranch.setTypeface(FontUtils.getHelvetica_Neue_LT(this));
        editAddressBranch.setTypeface(FontUtils.getHelvetica_Neue_LT(this));
        editPhone1Branch.setTypeface(FontUtils.getHelvetica_Neue_LT(this));
        editPhone2Branch.setTypeface(FontUtils.getHelvetica_Neue_LT(this));
        editFaxBranch.setTypeface(FontUtils.getHelvetica_Neue_LT(this));
        editEmailBranch.setTypeface(FontUtils.getHelvetica_Neue_LT(this));
    }


    private void setDataBranch(BranchModel branch){
        editNamaBranch.setText(branch.getName());
        editAddressBranch.setText(branch.getAddress());
        editPhone1Branch.setText(branch.getPhone1());
        editPhone2Branch.setText(branch.getPhone2());
        editFaxBranch.setText(branch.getFax());
        editEmailBranch.setText(branch.getEmail());
    }


    private void updateBranch(){
        BranchModel branchModel = new BranchModel(
                null,
                editNamaBranch.getText().toString(),
                editAddressBranch.getText().toString(),
                editPhone1Branch.getText().toString(),
                editPhone2Branch.getText().toString(),
                editFaxBranch.getText().toString(),
                editEmailBranch.getText().toString()
        );

        arrBranchModels.set(positionBranch, branchModel);
        setViewBranchItem(arrBranchModels);
        dialogBranchItem.dismiss();
    }


    private void deleteBranch(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Apakah anda yakin remove data branch ?").setCancelable(false).setPositiveButton("Ya",new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog,int id) {

                arrBranchModels.remove(positionBranch);
                setViewBranchItem(arrBranchModels);
                dialogBranchItem.dismiss();
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
            public void success(Response response, Response response2){
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
                        if(jResult != null){
                            for(int i = 0; i < jResult.length(); i++){
                                JSONObject jsonObject = jResult.optJSONObject(i);

                                JSONObject idObj  = (JSONObject) jsonObject.get("_id");
                                String strMongoId = (String)     idObj.get("$id");

                                taxGroupModels.add(new TaxGroupModel(strMongoId, jsonObject.optString("description")));
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



    private void showDailogAddBranch(){
        dialogAddBranch = new Dialog(this);
        View viewsBranch = inflater.inflate(R.layout.popup_add_branch, null);
        btnCancelBranch    = (Button)   viewsBranch.findViewById(R.id.btnCancelBranch);
        btnSaveBranch      = (Button)   viewsBranch.findViewById(R.id.btnSaveBranch);
        namaBranch         = (EditText) viewsBranch.findViewById(R.id.editNamaBranch);
        addressBranch      = (EditText) viewsBranch.findViewById(R.id.editAdressBranch);
        phone1Branch       = (EditText) viewsBranch.findViewById(R.id.editPhone1Branch);
        phone2Branch       = (EditText) viewsBranch.findViewById(R.id.editPhone2Branch);
        faxBranch          = (EditText) viewsBranch.findViewById(R.id.editFaxBranch);
        emailBranch        = (EditText) viewsBranch.findViewById(R.id.editEmailBranch);

        btnCancelBranch.setOnClickListener(onClickListener);
        btnSaveBranch.setOnClickListener(onClickListener);
        dialogAddBranch.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogAddBranch.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#50000000")));
        dialogAddBranch.setContentView(viewsBranch);
        dialogAddBranch.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialogAddBranch.show();
    }


    private void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    private boolean isValidInputBranch(){
        if (namaBranch.getText().toString().isEmpty() || namaBranch.getText() == null){
            showToast("Branch name cannot be empty!");
            return false;
        }else if (addressBranch.getText().toString().isEmpty() || addressBranch.getText() == null){
            showToast("Address Branch cannot be empty!");
            return false;
        }else if (phone1Branch.getText().toString().isEmpty() || phone1Branch.getText() == null){
            showToast("Phone 1 Branch cannot be empty!");
            return false;
        }else return true;
    }


    private void addRowBranch(){
        BranchModel branchModel = new BranchModel(
                null,
                namaBranch.getText().toString(),
                addressBranch.getText().toString(),
                phone1Branch.getText().toString(),
                phone2Branch.getText().toString(),
                faxBranch.getText().toString(),
                emailBranch.getText().toString()
        );

        arrBranchModels.add(branchModel);
        setViewBranchItem(arrBranchModels);
        dialogAddBranch.dismiss();
    }



}
