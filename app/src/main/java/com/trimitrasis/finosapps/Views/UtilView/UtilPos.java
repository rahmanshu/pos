package com.trimitrasis.finosapps.Views.UtilView;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.trimitrasis.finosapps.Views.ContentProvider.PromosiBarangProvider;
import com.trimitrasis.finosapps.Views.ContentProvider.PromosiProvider;
import com.trimitrasis.finosapps.Views.PosView.Model.RingkasanOrderModel;

import java.util.ArrayList;

/**
 * Created by rahman on 10/10/2017.
 */

public class UtilPos {


    public static double getTotalDiscount(ArrayList<RingkasanOrderModel> orderModels){
        double totDiscount = 0;
        for(int i = 0; i < orderModels.size(); i++){
            totDiscount = totDiscount + orderModels.get(i).getDiskon();
        }
        return totDiscount;
    }



    public static double getTotalBayar(ArrayList<RingkasanOrderModel> orderModels){
        double totBayar = 0; double totBayarTemp = 0; double totDiscount = 0; double totalBayar = 0;
        for(int j = 0; j < orderModels.size(); j++){
            totDiscount   = totDiscount + orderModels.get(j).getDiskon();
            totBayarTemp  = orderModels.get(j).getHargaJual() * orderModels.get(j).getQty();
            totBayar      = totBayar + totBayarTemp;
        }

        totalBayar = totBayar - totDiscount;
        return totalBayar;
    }


    public static double getTotalPajak(ArrayList<RingkasanOrderModel> orderModels){
        double totPajak = 0;
        for(int i = 0; i < orderModels.size(); i++){
            totPajak = totPajak + orderModels.get(i).getTax();
        }
        return totPajak;
    }



    public static String getKodePromoFromBarcode(String clauseWhere, String URL, Context context, String condition){
        String url = ""; String kodePromo = "";
        url = URL;
        Uri uri_  = Uri.parse(url);
        Cursor c_pro =  context.getContentResolver().query(uri_, null, clauseWhere + " = '" +  condition + "'", null, "_id ASC");

        if(c_pro.moveToFirst()){
            do{
                kodePromo = (!c_pro.getString(c_pro.getColumnIndex(PromosiProvider.KEY_KODE_PROMO)).equals("") ? c_pro.getString(c_pro.getColumnIndex(PromosiProvider.KEY_KODE_PROMO)) : "");
            }while(c_pro.moveToNext());
            c_pro.close();
        }
        return kodePromo;
    }


    public static int getJumlahDataFromKodePromo(String clauseWhere, String URL, Context context, String condition){
        String url = ""; int jumlahData = 0; url = URL;
        Uri uri_  = Uri.parse(url);
        Cursor c_pro =  context.getContentResolver().query(uri_, null, clauseWhere + " = '" +  condition + "'", null, "_id ASC");

        if(c_pro.moveToFirst()){
            do{
                jumlahData++;
            }while(c_pro.moveToNext());
            c_pro.close();
        }
        return jumlahData;
    }









}
