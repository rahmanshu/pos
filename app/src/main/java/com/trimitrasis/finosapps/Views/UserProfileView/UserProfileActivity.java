package com.trimitrasis.finosapps.Views.UserProfileView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.trimitrasis.finosapps.Connection.ApiConnection;
import com.trimitrasis.finosapps.Connection.Models.UserInfoModel;
import com.trimitrasis.finosapps.R;
import com.trimitrasis.finosapps.Views.UtilView.FontUtils;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by rahman on 14/03/2017.
 */


@EActivity(R.layout.layout_profile_activity)
public class UserProfileActivity extends AppCompatActivity {

    @ViewById
    Toolbar toolbar;

    @Extra
    UserInfoModel userInfoModel;

    @ViewById
    EditText editFirstName, editLastName, editEmail, editPassword, editPhone1, editPhone2;

    @AfterViews
    void afterView(){
        customView();
        getDataUser();
    }


    private void customView(){
        Button buttonCancel = (Button) toolbar.findViewById(R.id.btnCancel);
        buttonCancel.setText("Cancel");
        Button buttonSave = (Button) toolbar.findViewById(R.id.btnSave);
        buttonSave.setText("Save");
        TextView textTitle = (TextView) toolbar.findViewById(R.id.titleForm);
        textTitle.setText("User Profile");
        buttonCancel.setOnClickListener(onClickListener);
        buttonSave.setOnClickListener(onClickListener);
        buttonCancel.setTypeface(FontUtils.getHelvetica_Neue_LT(this));
        buttonSave.setTypeface(FontUtils.getHelvetica_Neue_LT(this));
        textTitle.setTypeface(FontUtils.getHelvetica_Neue_LT(this));
        editFirstName.setTypeface(FontUtils.getHelvetica_Neue_LT(this));
        editLastName.setTypeface(FontUtils.getHelvetica_Neue_LT(this));
        editEmail.setTypeface(FontUtils.getHelvetica_Neue_LT(this));
        editPassword.setTypeface(FontUtils.getHelvetica_Neue_LT(this));
        editPhone1.setTypeface(FontUtils.getHelvetica_Neue_LT(this));
        editPhone2.setTypeface(FontUtils.getHelvetica_Neue_LT(this));
    }



    View.OnClickListener onClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            switch (v.getId()) {
                case R.id.btnCancel:{
                    finish();
                }break;
                    case R.id.btnSave:{
                }break;
            }
        }
    };



    private void getDataUser(){
        ApiConnection.getDataProfileInterface().getProfile(userInfoModel, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                String rawResponse = ApiConnection.getRawResponse(response);
                JSONObject profileObj = null;

                try{

                    profileObj = new JSONObject(rawResponse);
                    String jStatus    = profileObj.optString("message");
                    JSONArray jListProfile  = profileObj.getJSONArray("list_user");

                    if(jStatus.equals("success")){
                        for(int i = 0; i < jListProfile.length(); i ++){
                            JSONObject jsonObject = jListProfile.optJSONObject(i);
                            editFirstName.setText(jsonObject.optString("f_name"));
                            editLastName.setText(jsonObject.optString("l_name"));
                            editEmail.setText(jsonObject.optString("email"));
                            editPassword.setText(jsonObject.optString("password"));
                            editPhone1.setText(jsonObject.optString("phone1"));
                            editPhone2.setText(jsonObject.optString("phone2"));
                        }
                    }

                }catch(JSONException e){
                    e.printStackTrace();
                }

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }


}
