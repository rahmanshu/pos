package com.trimitrasis.finosapps.Views.MasterDataView;
import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.trimitrasis.finosapps.Connection.ApiConnection;
import com.trimitrasis.finosapps.Connection.Models.CompanyModel;
import com.trimitrasis.finosapps.R;
import com.trimitrasis.finosapps.Views.MasterDataView.Adapter.MainCurrencyAdapter;
import com.trimitrasis.finosapps.Views.MasterDataView.Models.MainCurrencyModel;
import com.trimitrasis.finosapps.Views.UtilView.Constants;
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


@EActivity(R.layout.layout_main_currency)
public class MainCurrencyActivity extends AppCompatActivity {


    @ViewById
    Toolbar toolbar;

    @ViewById
    RecyclerView listCurrency;

    RecyclerView.LayoutManager mLayoutManager;

    @ViewById
    EditText editSearch;

    ArrayList<MainCurrencyModel> mainCurrencyModels;

    ProgressDialog progressDialog;

    @AfterViews
    void afterView(){
        mainCurrencyModels = new ArrayList<>();
        mainCurrencyModels.clear();
        customView();
        initSetTableView();
        getViewCurrency();
    }


    private void initSetTableView(){
        listCurrency.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        listCurrency.setLayoutManager(mLayoutManager);
    }

    private void initListSearch(){
        MainCurrencyAdapter adapter = new MainCurrencyAdapter(this, mainCurrencyModels);
        listCurrency.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


    private void customView(){
        Button buttonCancel = (Button) toolbar.findViewById(R.id.btnCancel);
        Button buttonNew = (Button) toolbar.findViewById(R.id.btnSave);
        buttonCancel.setText("Cancel");
        buttonNew.setText("New");
        TextView titleToolbar = (TextView) toolbar.findViewById(R.id.titleForm);
        titleToolbar.setText("Currency");
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
                    MasterCurrencyActivity_.intent(MainCurrencyActivity.this).start();
                }break;

            }
        }
    };


    private void getViewCurrency(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading..");
        progressDialog.show();
        ApiConnection.getViewDataCurrencyInterface().getViewDataCurrency(Constants.userInfoModel,new Callback<Response>() {
            @Override
            public void success(Response responsFinos, Response response) {

                String rawResponse = ApiConnection.getRawResponse(response);
                JSONObject loginObj = null;
                try {
                    loginObj = new JSONObject(rawResponse);
                    String jStatus  = loginObj.optString("message");
                    JSONArray jResult = loginObj.optJSONArray("result");

                    if(jStatus.equals("success")){
                        for(int i = 0; i < jResult.length(); i ++){
                            JSONObject jsonObject = jResult.optJSONObject(i);

                            mainCurrencyModels.add(new MainCurrencyModel(
                                    jsonObject.optString("id"),
                                    jsonObject.optString("description"),
                                    jsonObject.optString("symbol"))
                            );
                        }

                        initListSearch();

                    }else{
                        Utils.showToast("Login failed", MainCurrencyActivity.this);
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



}
