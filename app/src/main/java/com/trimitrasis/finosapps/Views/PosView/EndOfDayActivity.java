package com.trimitrasis.finosapps.Views.PosView;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.trimitrasis.finosapps.Connection.ApiConnection;
import com.trimitrasis.finosapps.R;
import com.trimitrasis.finosapps.Views.PrintView.PrintEndOfDayActivity_;
import com.trimitrasis.finosapps.Views.UtilView.Constants;
import com.trimitrasis.finosapps.Views.UtilView.Utils;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by rahman on 10/04/2017.
 */


@EActivity(R.layout.layout_end_of_day_activity)
public class EndOfDayActivity extends AppCompatActivity {

    ProgressDialog progressDialog;

    @ViewById
    Toolbar toolbar;

    @ViewById
    Button btnEndOfDay;

    @ViewById
    EditText textDeviceId, textLocation;

    @AfterViews
    void afterView(){
        initTabbar();
        customView();
    }

    private void initTabbar(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_arrow_left);
    }

    @OptionsItem(android.R.id.home)
    void homeClick(){
        onBackPressed();
    }

    private void customView(){
        TextView toolbarView = (TextView) toolbar.findViewById(R.id.headerText);
        toolbarView.setText("End Of Day");
        textLocation.setText(Constants.userInfoModel.getLocationName());
        textLocation.setEnabled(false);
        textDeviceId.setText(Constants.userInfoModel.getDevid());
        textDeviceId.setEnabled(false);
        btnEndOfDay.setFocusableInTouchMode(true);
        btnEndOfDay.requestFocus();
    }

    @Click
    void btnEndOfDay(){

        if(Utils.cek_status((this))){

            int orientation=this.getResources().getConfiguration().orientation;
            if(orientation== Configuration.ORIENTATION_PORTRAIT){
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }else{
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }

            cekEndOfDay();
        }else{
            showDialogConnection("Error Connection!");
        }
    }


    private void cekEndOfDay(){

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading..");
        progressDialog.show();
        ApiConnection.getEndOfDayInterface().getEndOfDay(Constants.userInfoModel, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2){
                String rawResponse = ApiConnection.getRawResponse(response);
                JSONObject sosObj = null;
                try{
                    sosObj = new JSONObject(rawResponse);
                    String jStatus  = sosObj.optString("message");
                    JSONArray jResult = sosObj.optJSONArray("data_");

                    if(jStatus.equals("success")){
                        showDialogEndOfDay("End Of Day Berhasil!");
                    }else if(jStatus.equals("failed eod")){
                        showDialogEndOfDay("End Of Day Failed!!");
                    }else if(jStatus.equals("user not yet eos")){
                        if(jResult != null){
                            for(int i = 0; i < jResult.length(); i ++){
                                JSONObject jsonObject = jResult.optJSONObject(i);
                                String kasir_ = jsonObject.optString("kasir");
                                showDialogKasir("INFO : User  " + kasir_ + "  belum melakukan End Of Shift, silahkan lakukan End Of Shift terlebih dahulu!!");
                            }
                        }
                    }else if(jStatus.equals("not yet eod")){
                        showDialogEndOfDay("Lakukan Start Of Day Terlebih dahulu!");
                    }else if(jStatus.equals("not yet sos")){
                        showDialogEndOfDay("Anda belum melakukan Start Of Shift!");
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


    private void showDialogEndOfDay(final String message){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Yes",new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog,int id){
                        if(message.equals("End Of Day Berhasil!")){
                            PrintEndOfDayActivity_.intent(EndOfDayActivity.this).start();
                        }
                        dialog.dismiss();
                        finish();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }



    private void showDialogKasir(String message){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Yes",new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog,int id){
                        dialog.dismiss();
                    }
                });


        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void showDialogConnection(String message){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Yes",new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog,int id){
                        dialog.dismiss();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}
