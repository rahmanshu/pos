package com.trimitrasis.finosapps;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import com.trimitrasis.finosapps.Views.LoginView.LoginActivity_;
import java.util.HashMap;

/**
 * Created by rahman on 10/3/2016.
 */

public class SessionManager {

    private SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context context;
    private static final String PREF_NAME = "pointofsalepref";
    private static final String IS_LOGIN = "IsLoggedIn";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    int PRIVATE_MODE = 0;                                                          //share pref mode


    public SessionManager(Context context){
        this.context = context;
        sharedPreferences = this.context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }


    public void createLoginSession(String username, String password){
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_PASSWORD, password);
        editor.commit();
    }


    public HashMap<String, String> getUserDetail(){
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(KEY_USERNAME, sharedPreferences.getString(KEY_USERNAME, null));
        user.put(KEY_PASSWORD, sharedPreferences.getString(KEY_PASSWORD, null));
        return user;
    }


    public void checkLogin(){
        if(!this.isLoggedIn()){
            Intent i = new Intent(context, LoginActivity_.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
    }


    public void logoutUser(){
        editor.clear();
        editor.commit();
        Intent i = new Intent(context, LoginActivity_.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }


    public boolean isLoggedIn(){
        return sharedPreferences.getBoolean(IS_LOGIN, false);
    }


}
