package com.trimitrasis.finosapps.Views.MasterDataView;
import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.trimitrasis.finosapps.Connection.ApiConnection;
import com.trimitrasis.finosapps.R;
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


@EActivity(R.layout.layout_master_vendor)
public class MasterVendorActivity extends AppCompatActivity{

    @ViewById
    Toolbar toolbar;

    @ViewById
    EditText editNamaVendor, editPhone1Vendor, editPhone2Vendor, editAddressVendor;

    @ViewById
    EditText editFaxVendor, editEmailVendor, editReconVendor, editCreditLimit;

    @ViewById
    Spinner spinnerCurrency, spinnerPaymentTerm, spinnerTaxGroup;

    @ViewById
    TextView labelNama, labelPhone1, labelPhone2, labelAddress, labelFax, labelEmail, labelRecon;

    @ViewById
    TextView labelCurrency, labelCreditLimit, labelPaymentTerm, labelPkp, textInformation;

    @ViewById
    CheckBox checkPkpCustomer;

    ArrayList<VendorModel> vendorModels;

    ProgressDialog progressDialog,  progressDialogPayTerms, progressDialogCurrency, progressDialogTaxGroup;
    ArrayList<String> listCurrency = new ArrayList<>();
    ArrayList<String> listPaymentTerms = new ArrayList<>();
    String spinnerCurrencyString;
    String spinnerPaymentTermString;
    boolean isPkp = false;
    double creditLimit = 0;

    ArrayList<String> listTaxGroup = new ArrayList<>();
    ArrayList<TaxGroupModel> taxGroupModels;
    String spinnerTaxGroupString;
    String taxGroupIdString;

    @AfterViews
    void afterView(){
        progressDialog = new ProgressDialog(this);
        vendorModels = new ArrayList<>();
        taxGroupModels = new ArrayList<>();
        customView();
        getDataPaymentTerms();
        getDataCurrency();
        getDataTaxGroup();
        addListenerOnChkPkp();
    }



    private void customView(){
        setFontText();
        textInformation.setText("Add Vendor");
        Button buttonCancel = (Button) toolbar.findViewById(R.id.btnCancel);
        buttonCancel.setText("Cancel");
        buttonCancel.setTypeface(FontUtils.getHelvetica_Neue_LT(this));
        Button buttonSave = (Button) toolbar.findViewById(R.id.btnSave);
        buttonSave.setText("Save");
        buttonSave.setTypeface(FontUtils.getHelvetica_Neue_LT(this));
        TextView textTitle = (TextView) toolbar.findViewById(R.id.titleForm);
        textTitle.setText("Vendor");
        textTitle.setTypeface(FontUtils.getHelvetica_Neue_LT(this));
        buttonCancel.setOnClickListener(onClickListener);
        buttonSave.setOnClickListener(onClickListener);
    }



    private void setFontText(){
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
        editNamaVendor.setTypeface(FontUtils.getHelvetica_Neue_LT(this));
        editPhone1Vendor.setTypeface(FontUtils.getHelvetica_Neue_LT(this));
        editPhone2Vendor.setTypeface(FontUtils.getHelvetica_Neue_LT(this));
        editAddressVendor.setTypeface(FontUtils.getHelvetica_Neue_LT(this));
        editFaxVendor.setTypeface(FontUtils.getHelvetica_Neue_LT(this));
        editEmailVendor.setTypeface(FontUtils.getHelvetica_Neue_LT(this));
        editReconVendor.setTypeface(FontUtils.getHelvetica_Neue_LT(this));
        editCreditLimit.setTypeface(FontUtils.getHelvetica_Neue_LT(this));
        textInformation.setTypeface(FontUtils.getHelvetica_Neue_LT(this));
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
                        addDataVendor();
                    }

                }break;

            }
        }
    };







    private VendorModel getVendorModels(){
        VendorModel vendorModel = new VendorModel(
                null,
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
        Utils.showLogI("vendor model : "+ new Gson().toJson(vendorModel).toString());
        return vendorModel;
    }



    private boolean isValidInput(){
        if (editNamaVendor.getText().toString().isEmpty() || editNamaVendor.getText() == null){
            showToast("Vendor name cannot be empty!");
            return false;
        }else if (editPhone1Vendor.getText().toString().isEmpty() || editPhone1Vendor.getText() == null){
            showToast("Phone cannot be empty!");
            return false;
        }else if (editAddressVendor.getText().toString().isEmpty() || editAddressVendor.getText() == null){
            showToast("Address cannot be empty!");
            return false;
        }else if (editEmailVendor.getText().toString().isEmpty() || editEmailVendor.getText() == null){
            showToast("Email cannot be empty!");
            return false;
        }else return true;
    }


    private void addDataVendor(){
        progressDialog.setMessage("Loading..");
        progressDialog.show();
        ApiConnection.getVendorInterface().getVendor(getVendorModels(), new Callback<Response>(){
            @Override
            public void success(Response responseFinos, Response response){

                String rawResponse = ApiConnection.getRawResponse(response);
                JSONObject loginObj = null;
                try {
                    loginObj = new JSONObject(rawResponse);
                    String jStatus  = loginObj.optString("message");

                    if(jStatus.equals("success")){
                        Utils.showToast("Data Vendor Sukses Disimpan!!", MasterVendorActivity.this);
                        Constants.flakAddVendor = 1;
                        finish();
                    }else{
                        Utils.showToast("Data Vendor gagal disimpan!", MasterVendorActivity.this);
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



    private void spinnerInitTaxGroup(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, listTaxGroup){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                TextView view = (TextView) super.getView(position, convertView, parent);
                view.setTypeface(FontUtils.getFontHeaderToolbar(MasterVendorActivity.this));
                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent){
                TextView view = (TextView)super.getDropDownView(position, convertView, parent);
                view.setTypeface(FontUtils.getFontHeaderToolbar(MasterVendorActivity.this));
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

    private void spinnerInitCurrency(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, listCurrency){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                TextView view = (TextView) super.getView(position, convertView, parent);
                view.setTypeface(FontUtils.getFontHeaderToolbar(MasterVendorActivity.this));
                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent){
                TextView view = (TextView)super.getDropDownView(position, convertView, parent);
                view.setTypeface(FontUtils.getFontHeaderToolbar(MasterVendorActivity.this));
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
                view.setTypeface(FontUtils.getFontHeaderToolbar(MasterVendorActivity.this));
                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent){
                TextView view = (TextView)super.getDropDownView(position, convertView, parent);
                view.setTypeface(FontUtils.getFontHeaderToolbar(MasterVendorActivity.this));
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
                }else{
                    isPkp = false;
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
