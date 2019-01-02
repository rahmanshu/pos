package com.trimitrasis.finosapps.Views.MasterDataView;
import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.trimitrasis.finosapps.Connection.ApiConnection;
import com.trimitrasis.finosapps.Connection.Models.CompanyModel;
import com.trimitrasis.finosapps.Connection.Models.ResponsFinos;
import com.trimitrasis.finosapps.R;
import com.trimitrasis.finosapps.Views.MasterDataView.Models.CurrencyModel;
import com.trimitrasis.finosapps.Views.UtilView.Constants;
import com.trimitrasis.finosapps.Views.UtilView.FontUtils;
import com.trimitrasis.finosapps.Views.UtilView.Utils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by rahman on 16/03/2017.
 */


@EActivity(R.layout.layout_currency_activity)
public class MasterCurrencyActivity extends AppCompatActivity {

    @ViewById
    Toolbar toolbar;

    @ViewById
    EditText editCurrency, editDesc, editSymbol;

    @ViewById
    TextView labelCurrency, labelDesc, labelSymbol;

    ProgressDialog progressDialog;

    @AfterViews
    void afterView(){
        progressDialog = new ProgressDialog(this);
        customView();
    }


    private void customView(){
        labelDesc.setTypeface(FontUtils.getHelvetica_Neue_LT(this));
        labelSymbol.setTypeface(FontUtils.getHelvetica_Neue_LT(this));
        labelCurrency.setTypeface(FontUtils.getHelvetica_Neue_LT(this));
        editCurrency.setTypeface(FontUtils.getHelvetica_Neue_LT(this));
        editDesc.setTypeface(FontUtils.getHelvetica_Neue_LT(this));
        editSymbol.setTypeface(FontUtils.getHelvetica_Neue_LT(this));

        Button buttonCancel = (Button) toolbar.findViewById(R.id.btnCancel);
        buttonCancel.setText("Cancel");
        buttonCancel.setTypeface(FontUtils.getHelvetica_Neue_LT(this));
        Button buttonSave = (Button) toolbar.findViewById(R.id.btnSave);
        buttonSave.setText("Save");
        buttonSave.setTypeface(FontUtils.getHelvetica_Neue_LT(this));
        TextView textTitle = (TextView) toolbar.findViewById(R.id.titleForm);
        textTitle.setText("Currency");
        textTitle.setTypeface(FontUtils.getHelvetica_Neue_LT(this));
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
                    if(isValidInput()){
                        sendDataCurrency();
                    }

                }break;
            }
        }
    };




    private boolean isValidInput(){
        if (editCurrency.getText().toString().isEmpty() || editCurrency.getText() == null){
            showToast("Currency cannot be empty!");
            return false;
        }else if (editDesc.getText().toString().isEmpty() || editDesc.getText() == null){
            showToast("Description cannot be empty!");
            return false;
        }else if (editSymbol.getText().toString().isEmpty() || editSymbol.getText() == null){
            showToast("Symbol cannot be empty!");
            return false;
        }else return true;
    }


    private void sendDataCurrency(){
        progressDialog.setMessage("Loading..");
        progressDialog.show();
        ApiConnection.getCurrencyInterface().getDataCurrency(Constants.userInfoModel, new Callback<ResponsFinos>(){
            @Override
            public void success(ResponsFinos responseFinos, Response response){
                if(responseFinos.getMessage().equals("success")){
                    Utils.showToast("Data Sukses Disimpan!!", MasterCurrencyActivity.this);
                }else{
                    Utils.showToast(responseFinos.getMessage(), MasterCurrencyActivity.this);
                    if (progressDialog!=null)progressDialog.dismiss();
                }
                if (progressDialog!=null)progressDialog.dismiss();
            }

            @Override
            public void failure(RetrofitError error){
                if (progressDialog!=null)progressDialog.dismiss();
            }

        });
    }




    private void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


}
