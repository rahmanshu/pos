package com.trimitrasis.finosapps.Views.MasterDataView;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
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
import com.google.gson.Gson;
import com.trimitrasis.finosapps.Connection.ApiConnection;
import com.trimitrasis.finosapps.Connection.Models.CompanyModel;
import com.trimitrasis.finosapps.R;
import com.trimitrasis.finosapps.Views.MasterDataView.CustomView.ViewAddBranchItem;
import com.trimitrasis.finosapps.Views.MasterDataView.CustomView.ViewBranchItem;
import com.trimitrasis.finosapps.Views.MasterDataView.Models.BranchModel;
import com.trimitrasis.finosapps.Views.MasterDataView.Models.CustomerModel;
import com.trimitrasis.finosapps.Views.MasterDataView.Models.TaxGroupModel;
import com.trimitrasis.finosapps.Views.UtilView.Constants;
import com.trimitrasis.finosapps.Views.UtilView.FontUtils;
import com.trimitrasis.finosapps.Views.UtilView.Utils;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
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
 * Created by rahman on 09/03/2017.
 */

@EActivity(R.layout.layout_master_customer)
public class MasterCustomerActivity extends AppCompatActivity {

    @ViewById
    Toolbar toolbar;

    @ViewById
    EditText editNamaCustomer, editPhone1Customer, editPhone2Customer, editAddressCustomer;

    @ViewById
    EditText editFaxCustomer, editEmailCustomer, editReconCustomer, editCreditLimit;

    @ViewById
    Spinner spinnerCurrency, spinnerPaymentTerm, spinnerTaxGroup;

    @ViewById
    TextView labelNama, labelPhone1, labelPhone2, labelAddress, labelFax, labelEmail, labelRecon;

    @ViewById
    TextView labelCurrency, labelCreditLimit, labelPaymentTerm, labelPkp, textAddCustomer;

    @ViewById
    Button btnAddBranch;

    @ViewById
    LinearLayout contentViewBranchItem;

    @ViewById
    CheckBox checkPkpCustomer;

    @ViewById
    View lineTopCust, lineButtomCust;

    Button btnCancelBranch, btnSaveBranch;
    EditText namaBranch, addressBranch, phone1Branch, phone2Branch, faxBranch, emailBranch;
    Dialog dialogAddBranch;
    LayoutInflater inflater;
    ViewAddBranchItem viewBranchItem;
    ArrayList<ViewAddBranchItem> viewBranchItemArrayList;
    int incremen_brach_item = 0;
    List<BranchModel> branchModels;
    ArrayList<CustomerModel> customerModels;

    String branchName;
    ProgressDialog progressDialog;
    ArrayList<String> listCurrency = new ArrayList<>();
    ArrayList<String> listPaymentTerms = new ArrayList<>();
    ArrayList<String> listTaxGroup = new ArrayList<>();
    ArrayList<TaxGroupModel> taxGroupModels;

    String spinnerCurrencyString;
    String spinnerPaymentTermString;
    String spinnerTaxGroupString;
    String taxGroupIdString;
    boolean isPkp = false;
    double creditLimit = 0;

    ProgressDialog progressDialogPayTerms, progressDialogCurrency, progressDialogTaxGroup;


    @AfterViews
    void afterView(){
        progressDialog = new ProgressDialog(this);
        viewBranchItemArrayList = new ArrayList<>();
        branchModels = new ArrayList<>();
        customerModels = new ArrayList<>();
        taxGroupModels = new ArrayList<>();
        inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        customView();
        getDataCurrency();
        getDataPaymentTerms();
        getDataTaxGroup();
        addListenerOnChkPkp();
    }


    private void customView(){
        labelNama.setTypeface(FontUtils.getHelvetica_Neue_LT(this));
        labelPhone1.setTypeface(FontUtils.getHelvetica_Neue_LT(this));
        labelPhone2.setTypeface(FontUtils.getHelvetica_Neue_LT(this));
        labelAddress.setTypeface(FontUtils.getHelvetica_Neue_LT(this));
        labelFax.setTypeface(FontUtils.getHelvetica_Neue_LT(this));
        labelEmail.setTypeface(FontUtils.getHelvetica_Neue_LT(this));
        labelRecon.setTypeface(FontUtils.getHelvetica_Neue_LT(this));
        labelCurrency.setTypeface(FontUtils.getHelvetica_Neue_LT(this));
        labelCreditLimit.setTypeface(FontUtils.getHelvetica_Neue_LT(this));
        labelPaymentTerm.setTypeface(FontUtils.getHelvetica_Neue_LT(this));
        labelPkp.setTypeface(FontUtils.getHelvetica_Neue_LT(this));
        textAddCustomer.setTypeface(FontUtils.getHelvetica_Neue_LT(this));
        textAddCustomer.setText("Add Customer");
        btnAddBranch.setTypeface(FontUtils.getHelvetica_Neue_LT(this));

        Button buttonCancel = (Button) toolbar.findViewById(R.id.btnCancel);
        buttonCancel.setText("Cancel");
        buttonCancel.setTypeface(FontUtils.getHelvetica_Neue_LT(this));
        Button buttonSave = (Button) toolbar.findViewById(R.id.btnSave);
        buttonSave.setText("Save");
        buttonSave.setTypeface(FontUtils.getHelvetica_Neue_LT(this));
        TextView textTitle = (TextView) toolbar.findViewById(R.id.titleForm);
        textTitle.setText("Customer");
        textTitle.setTypeface(FontUtils.getHelvetica_Neue_LT(this));
        buttonCancel.setOnClickListener(onClickListener);
        buttonSave.setOnClickListener(onClickListener);
    }


    private void showLineCust(){
        lineButtomCust.setVisibility(View.VISIBLE);
        lineTopCust.setVisibility(View.VISIBLE);
    }


    View.OnClickListener onClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            switch (v.getId()) {
                case R.id.btnCancel:{
                    finish();
                }break;
                case R.id.btnSave:{

                    if(isValidInput()){
                        //getCompanyModel();
                        sendDataCustomer();
                    }

                }break;
                case R.id.btnCancelBranch:{
                    dialogAddBranch.dismiss();
                }break;
                case R.id.btnSaveBranch:{

                    if(isValidInputBranch()){
                        branchName = namaBranch.getText().toString();
                        setViewBranchItem(branchName);
                        dialogAddBranch.dismiss();
                    }

                }break;
            }
        }
    };



    @Click
    void btnAddBranch(){
        showDailogBranch();
    }


    private void showDailogBranch(){
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



    private void setViewBranchItem(String nama){
        viewBranchItem = new ViewAddBranchItem(this);
        viewBranchItem.setTextNameBranch(nama);
        viewBranchItemArrayList.add(incremen_brach_item, viewBranchItem);
        contentViewBranchItem.addView(viewBranchItemArrayList.get(incremen_brach_item));
        getBranchModel();
        incremen_brach_item++;
        if(branchModels != null){
            showLineCust();
        }
    }


    private void getBranchModel(){
        BranchModel branchModel = new BranchModel(
                null,
                namaBranch.getText().toString(),
                addressBranch.getText().toString(),
                phone1Branch.getText().toString(),
                phone2Branch.getText().toString(),
                faxBranch.getText().toString(),
                emailBranch.getText().toString()
        );
        branchModels.add(branchModel);
    }






    private CustomerModel getCustomerModels(){
        CustomerModel customerModel = new CustomerModel(
                null,
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
                branchModels,
                Constants.userInfoModel);
                Utils.showLogI("customer model : "+ new Gson().toJson(customerModel).toString());
                return customerModel;
    }


    private boolean isValidInput(){
        if (editNamaCustomer.getText().toString().isEmpty() || editNamaCustomer.getText() == null){
            showToast("Customer name cannot be empty!");
            return false;
        }else if (editPhone1Customer.getText().toString().isEmpty() || editPhone1Customer.getText() == null){
            showToast("Phone cannot be empty!");
            return false;
        }else if (editAddressCustomer.getText().toString().isEmpty() || editAddressCustomer.getText() == null){
            showToast("Address cannot be empty!");
            return false;
        }else if (editEmailCustomer.getText().toString().isEmpty() || editEmailCustomer.getText() == null){
            showToast("Email cannot be empty!");
            return false;
        }else return true;
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


    private void sendDataCustomer(){
        progressDialog.setMessage("Loading..");
        progressDialog.show();
        ApiConnection.getCustomerInterface().getCustomer(getCustomerModels(), new Callback<Response>(){
            @Override
            public void success(Response responseFinos, Response response){
                String rawResponse = ApiConnection.getRawResponse(response);
                JSONObject loginObj = null;
                try {
                    loginObj = new JSONObject(rawResponse);
                    String jStatus  = loginObj.optString("message");

                    if(jStatus.equals("success")){
                        Utils.showToast("Data Customer Sukses Disimpan!!", MasterCustomerActivity.this);
                        Constants.flakAddCustomer = 1;
                        finish();
                    }else{
                        Utils.showToast("Data Customer gagal disimpan!", MasterCustomerActivity.this);
                        if (progressDialog!=null)progressDialog.dismiss();
                    }

                }catch(JSONException e){
                    e.printStackTrace();
                    progressDialog.dismiss();
                }

                if (progressDialog!=null)progressDialog.dismiss();
            }

            @Override
            public void failure(RetrofitError error){
                if (progressDialog!=null)progressDialog.dismiss();
            }

        });
    }



    private void spinnerInitCurrency(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, listCurrency){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                TextView view = (TextView) super.getView(position, convertView, parent);
                view.setTypeface(FontUtils.getFontHeaderToolbar(MasterCustomerActivity.this));
                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent){
                TextView view = (TextView)super.getDropDownView(position, convertView, parent);
                view.setTypeface(FontUtils.getFontHeaderToolbar(MasterCustomerActivity.this));
                return view;
            }
        };

        spinnerCurrency.setAdapter(adapter);
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


    private void spinnerInitPaymentTerms(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, listPaymentTerms){

            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                TextView view = (TextView) super.getView(position, convertView, parent);
                view.setTypeface(FontUtils.getFontHeaderToolbar(MasterCustomerActivity.this));
                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent){
                TextView view = (TextView)super.getDropDownView(position, convertView, parent);
                view.setTypeface(FontUtils.getFontHeaderToolbar(MasterCustomerActivity.this));
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


    private void spinnerInitTaxGroup(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, listTaxGroup){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                TextView view = (TextView) super.getView(position, convertView, parent);
                view.setTypeface(FontUtils.getFontHeaderToolbar(MasterCustomerActivity.this));
                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent){
                TextView view = (TextView)super.getDropDownView(position, convertView, parent);
                view.setTypeface(FontUtils.getFontHeaderToolbar(MasterCustomerActivity.this));
                return view;
            }
        };

        spinnerTaxGroup.setAdapter(adapter);
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


    private void getIdTaxGroup(String taxGroupName){
        for(int i = 0; i < taxGroupModels.size(); i++){
            if(taxGroupName.equals(taxGroupModels.get(i).getDescription())){
                taxGroupIdString = taxGroupModels.get(i).get_id();
            }
        }
    }


    public void addListenerOnChkPkp(){
        checkPkpCustomer.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (((CheckBox) v).isChecked()){
                    isPkp = true;
                }
            }
        });
    }


    private void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
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
                        spinnerInitCurrency();
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

                                taxGroupModels.add(new TaxGroupModel(
                                        strMongoId,
                                        jsonObject.optString("description")
                                ));

                                listTaxGroup.add(jsonObject.optString("description"));
                            }
                        }
                        spinnerInitTaxGroup();
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
