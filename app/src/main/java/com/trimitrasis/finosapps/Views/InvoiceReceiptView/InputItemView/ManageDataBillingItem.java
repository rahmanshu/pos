package com.trimitrasis.finosapps.Views.InvoiceReceiptView.InputItemView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

/**
 * Created by rahman on 07/03/2017.
 */

public class ManageDataBillingItem {

    private SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context context;
    private static final String PREF_NAME = "pointofsalepref";
    private static final String IS_LOGIN = "IsLoggedIn";
    public static final String KEY_KODE_ITEM = "item";
    public static final String KEY_HARGA_ITEM = "price";
    int PRIVATE_MODE = 0;

    public ManageDataBillingItem(Context context){
        this.context = context;
        sharedPreferences = this.context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }


    public void saveBillingItem(String kodeBarang, String hargaBarang){
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_KODE_ITEM, kodeBarang);
        editor.putString(KEY_HARGA_ITEM, hargaBarang);
        editor.commit();
    }


    public HashMap<String, String> getDataBillingItem(){
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(KEY_KODE_ITEM, sharedPreferences.getString(KEY_KODE_ITEM, null));
        user.put(KEY_HARGA_ITEM, sharedPreferences.getString(KEY_HARGA_ITEM, null));
        return user;
    }


    public boolean isLoggedIn(){
        return sharedPreferences.getBoolean(IS_LOGIN, false);
    }


}
