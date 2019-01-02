package com.trimitrasis.finosapps.Views.LoginView;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.trimitrasis.finosapps.Connection.ApiConnection;
import com.trimitrasis.finosapps.Connection.Models.UserInfoModel;
import com.trimitrasis.finosapps.MainActivity_;
import com.trimitrasis.finosapps.R;
import com.trimitrasis.finosapps.SessionManager;
import com.trimitrasis.finosapps.Views.ContentProvider.LoginProvider;
import com.trimitrasis.finosapps.Views.LoginView.Models.LoginModel;
import com.trimitrasis.finosapps.Views.UtilView.Constants;
import com.trimitrasis.finosapps.Views.UtilView.FontUtils;
import com.trimitrasis.finosapps.Views.UtilView.Utils;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by rahman on 01/03/2017.
 */

@EActivity(R.layout.layout_login_activity_fix)
public class LoginActivity extends AppCompatActivity {

    @ViewById
    EditText password;

    @ViewById
    AutoCompleteTextView username;

    @ViewById
    Button btnLogin;

    @ViewById
    Button btnRegister;

    ProgressDialog progressDialog;
    SessionManager sessionManager;

    @AfterViews
    void afterView(){
        System.out.println("cek cekin...");
        getUsername();
        progressDialog = new ProgressDialog(this);
        customView();
        sessionManager = new SessionManager(getApplicationContext());
    }



    private void getUsername(){

        String usernameCache = "";
        String URL = "content://com.trimitrasis.finosapps.Views.ContentProvider.LoginProvider/login";
        Uri uri = Uri.parse(URL);
        Cursor c_login = this.getContentResolver().query(uri, null, null, null, "_id");

        if(c_login.moveToFirst()){
            do{
                usernameCache = (c_login.getString(c_login.getColumnIndex(LoginProvider.KEY_USERNAME)) != null) ? c_login.getString(c_login.getColumnIndex(LoginProvider.KEY_USERNAME)) : "";
            } while (c_login.moveToNext());
            c_login.close();
        }

        String[] arrUsername = {usernameCache};
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, arrUsername);
        username.setAdapter(adapter);
        username.setThreshold(1);
    }



    @Click
    void btnLogin(){
        if (isValidInput()){
            progressDialog.setMessage("Loading..");
            progressDialog.show();
            doLogin();
        }
    }


    private void customView(){
        btnLogin.setText(R.string.label_button_login);
        btnRegister.setText(R.string.label_button_register);
        setFontLogin();
    }


    private void setFontLogin(){
        username.setTypeface(FontUtils.getHelvetica_Neue_LT(this));
        password.setTypeface(FontUtils.getHelvetica_Neue_LT(this));
        btnLogin.setTypeface(FontUtils.getHelvetica_Neue_LT(this));
        btnRegister.setTypeface(FontUtils.getHelvetica_Neue_LT(this));
    }

    private void doLogin(){
        ApiConnection.getLoginDeviceInterface().loginDevice(getLoginModel(), new Callback<Response>(){
            @Override
            public void success(Response response, Response response2){

                String rawResponse = ApiConnection.getRawResponse(response);
                JSONObject loginObj = null;

                try{

                    loginObj = new JSONObject(rawResponse);
                    String jStatus    = loginObj.optString("message");
                    String jToken     = loginObj.optString("token");
                    JSONObject jUser  = loginObj.optJSONObject("user");

                    if(jStatus.equals("success")){

                        Constants.userInfoModel = new UserInfoModel(
                                jUser.optString("email"),
                                jUser.optString("fname"),
                                jUser.optString("comp"),
                                Utils.getDevId(LoginActivity.this),
                                jUser.optString("dbname"),
                                jUser.optString("comname"),
                                jUser.optString("currency"),
                                jToken,
                                jUser.optString("location"),
                                jUser.optString("location_name"),
                                jUser.optString("location_address")
                        );


                        Utils.saveDataUser(LoginActivity.this, Constants.userInfoModel);
                        sessionManager.createLoginSession(username.getText().toString(), password.getText().toString());
                        finish();
                        MainActivity_.intent(LoginActivity.this).userInfoModel(Constants.userInfoModel).start();
                        insertUsernameAutoComplete(username.getText().toString());

                    }else if(jStatus.equals("username not found!")){
                        Utils.showToast("User name not found.", LoginActivity.this);
                        if (progressDialog!=null)progressDialog.dismiss();
                    }else if(jStatus.equals("username is not POS user!")){
                        Utils.showToast("username is not POS user!", LoginActivity.this);
                        if (progressDialog!=null)progressDialog.dismiss();
                    }else if(jStatus.equals("location is not maintained!")){
                        Utils.showToast("location is not maintained!", LoginActivity.this);
                        if (progressDialog!=null)progressDialog.dismiss();
                    }else{
                        Utils.showToast("Login Failed", LoginActivity.this);
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
                Utils.showToast("Error Connection.", LoginActivity.this);
                if (progressDialog!=null)progressDialog.dismiss();
            }
        });
    }


    private LoginModel getLoginModel(){
        LoginModel loginModel = new LoginModel(
                Utils.getCurrentDate("dd-MM-yyyy hh:mm:ss"),
                Utils.getDevId(this),
                Utils.getEncriptedPin(password.getText().toString()),
                username.getText().toString()
        );
        Utils.showLogI("login model : "+ new Gson().toJson(loginModel).toString());
        return loginModel;
    }


    private boolean isValidInput(){
        if (username.getText().toString().isEmpty() || username.getText() == null || username.getText().toString().equals("")){
            showToast("Username cannot be empty!");
            return false;
        }else if (password.getText().toString().isEmpty() || password.getText() == null || password.getText().toString().equals("")){
            showToast("Password cannot be empty!");
            return false;
        }else return true;
    }


    private void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    private void insertUsernameAutoComplete(String username){
        ContentValues values = new ContentValues();
        values.put(LoginProvider.KEY_USERNAME, username);
        Uri uri = getContentResolver().insert(LoginProvider.CONTENT_URI, values);
    }


    @Click
    void btnRegister(){
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://app.finos.id/login/register"));
        startActivity(i);
    }

}
