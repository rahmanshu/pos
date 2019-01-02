package com.trimitrasis.finosapps.Views.PosView;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.trimitrasis.finosapps.Connection.ApiConnection;
import com.trimitrasis.finosapps.R;
import com.trimitrasis.finosapps.Views.PosView.Model.StartOfShiftModel;
import com.trimitrasis.finosapps.Views.PrintView.PrintStartOfShiftActivity_;
import com.trimitrasis.finosapps.Views.UtilView.Constants;
import com.trimitrasis.finosapps.Views.UtilView.Utils;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by rahman on 08/06/2017.
 */

@EActivity(R.layout.layout_start_of_shift)
public class StartOfShiftNewActivity extends AppCompatActivity {

    public static final int STARTSHIFT_FINISH_CODE = 10;

    @ViewById
    Toolbar toolbar;

    @ViewById
    EditText editIdKasir, editIdMobile, editSetotanAwal;

    @ViewById
    Button btnStartEndShift;

    @ViewById
    EditText textLocation;

    ProgressDialog progressDialog;

    @OnActivityResult(STARTSHIFT_FINISH_CODE)
    void onFinishStartOfShift(){
        finish();
    }

    @AfterViews
    void afterView(){
        initTabbar();
        customView();
        cekStartOfDay();
        setoranAwanOnChange();
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
        toolbarView.setText("Start Of Shift");
        editIdMobile.setText(Utils.getDevId(StartOfShiftNewActivity.this));
        editIdMobile.setEnabled(false);
        editIdKasir.setText(Constants.userInfoModel.getEmail());
        editIdKasir.setEnabled(false);
        textLocation.setText(Constants.userInfoModel.getLocationName());
        textLocation.setEnabled(false);
        editSetotanAwal.setFocusableInTouchMode(true);
        editSetotanAwal.requestFocus();
    }



    @Click
    void btnStartEndShift(){
        if(isValidInput()){
            if(Utils.cek_status((this)))
            {

                int orientation=this.getResources().getConfiguration().orientation;
                if(orientation== Configuration.ORIENTATION_PORTRAIT){
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }else{
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                }

                startOfShift();
            }else{
                showDialogConnection("Error Connection!");
            }

        }
    }


    private boolean isValidInput(){
        if (editSetotanAwal.getText().toString().isEmpty() || editSetotanAwal.getText() == null || editSetotanAwal.getText().toString().equals("")){
            Utils.showToast("Setoran Awal tidak boleh kosong!", this);
            return false;
        }else return true;
    }


    private void cekStartOfDay(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading..");
        progressDialog.show();
        ApiConnection.getCheckStartOfDayInterface().getCheckStartOfDay(getStartOfShift(), new Callback<Response>() {
            @Override
            public void success(Response response, Response response2){
                String rawResponse = ApiConnection.getRawResponse(response);
                JSONObject sosObj = null;
                try{
                    sosObj = new JSONObject(rawResponse);
                    String jStatus  = sosObj.optString("message");
                    if(jStatus.equals("available")){
                        CreateOrderActivity_.intent(StartOfShiftNewActivity.this).startForResult(STARTSHIFT_FINISH_CODE);
                    }else if(jStatus.equals("not available sos")){
                        showDialogStartOfShift("Anda belum melakukan Start Of Shift!!", false);
                    }else if(jStatus.equals("not available sod")){
                        showDialogCallStartOfDay("Silahkan lakukan Start Of Day terlebih dahulu!!");
                    }

                }catch(JSONException e){
                    e.printStackTrace();
                    progressDialog.dismiss();
                }

                if(progressDialog!=null)progressDialog.dismiss();
            }


            @Override
            public void failure(RetrofitError error) {
                if (progressDialog!=null)progressDialog.dismiss();
            }

        });
    }


    private void startOfShift(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading..");
        progressDialog.show();
        ApiConnection.getStartOfShiftInterface().getStartOfShift(getStartOfShift(), new Callback<Response>() {
            @Override
            public void success(Response response, Response response2){
                String rawResponse = ApiConnection.getRawResponse(response);
                JSONObject sosObj = null;
                try{
                    sosObj = new JSONObject(rawResponse);
                    String jStatus  = sosObj.optString("message");
                    if(jStatus.equals("available sos")){
                        showDialogStartOfShift("Anda sudah melakukan Start Of Shift!", true);
                    }else if(jStatus.equals("success")){
                        showDialogPrintStartOfShift("Success Start Of Shift!");
                    }else if(jStatus.equals("not available sod")){
                        showDialogCallStartOfDay("Silahkan lakukan Start Of Day!");
                    }
                }catch(JSONException e){
                    e.printStackTrace();
                    progressDialog.dismiss();
                }

                if(progressDialog!=null)progressDialog.dismiss();
            }

            @Override
            public void failure(RetrofitError error){
                if (progressDialog!=null)progressDialog.dismiss();
            }

        });
    }


    private StartOfShiftModel getStartOfShift(){

        StartOfShiftModel startOfShiftModel = new StartOfShiftModel(
                Constants.userInfoModel.getEmail(),
                getSetoranAwalKasir(),
                true,
                false,
                false,
                Constants.userInfoModel,
                Utils.getDevId(this),
                Constants.userInfoModel.getLocation()
        );

        return startOfShiftModel;
    }


    private void showDialogCallStartOfDay(String message){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Yes",new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog,int id){
                        StartOfDayActivity_.intent(StartOfShiftNewActivity.this).start();
                        dialog.dismiss();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    private void showDialogPrintStartOfShift(String message){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Yes",new DialogInterface.OnClickListener(){

                    public void onClick(DialogInterface dialog,int id){
                        Constants.endOfShiftModel.setSetoranAwal(getSetoranAwalKasir());
                        CreateOrderActivity_.intent(StartOfShiftNewActivity.this).start();
                        PrintStartOfShiftActivity_.intent(StartOfShiftNewActivity.this).setoranAwal_(getSetoranAwalKasir()).start();
                        finish();
                        dialog.dismiss();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    private void showDialogStartOfShift(String message, final boolean status){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Yes",new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog,int id){
                        if(status == true){
                            CreateOrderActivity_.intent(StartOfShiftNewActivity.this).start();
                            finish();
                        }
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


    private double getSetoranAwalKasir(){
        double set_awal = 0;
        if(editSetotanAwal.getText().toString().equals("")){
            set_awal = 0;
        }else{

            String setoranAwalString = editSetotanAwal.getText().toString();
            if(setoranAwalString.contains(",")) {
                setoranAwalString = setoranAwalString.replaceAll(",", "");
            }
            set_awal = Double.parseDouble(setoranAwalString);

        }
        return set_awal;
    }


    public void setoranAwanOnChange(){
        editSetotanAwal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after){

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count){

            }


            @Override
            public void afterTextChanged(Editable s){
                editSetotanAwal.removeTextChangedListener(this);

                try{
                    String originalString = s.toString();
                    Long longval;
                    if(originalString.contains(",")) {
                        originalString = originalString.replaceAll(",", "");
                    }
                    longval = Long.parseLong(originalString);
                    DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                    formatter.applyPattern("#,###,###,###");
                    String formattedString = formatter.format(longval);

                    editSetotanAwal.setText(formattedString);
                    editSetotanAwal.setSelection(editSetotanAwal.getText().length());
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }

                editSetotanAwal.addTextChangedListener(this);
            }
        });
    }










}
