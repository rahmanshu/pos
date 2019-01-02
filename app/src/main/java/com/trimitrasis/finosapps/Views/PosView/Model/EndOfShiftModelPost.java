package com.trimitrasis.finosapps.Views.PosView.Model;

import com.trimitrasis.finosapps.Connection.Models.UserInfoModel;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by rahman on 31/05/2017.
 */

public class EndOfShiftModelPost implements Serializable {

    private static final long serialVersionUID = -7461620365906006489L;

    double cash_cashier;
    UserInfoModel userInfoModel;
    ArrayList<TransaksiPenjualanModel> transModel;
    ArrayList<TransaksiJualBayarModel> transJualBayarModel;

    public EndOfShiftModelPost(double cash_cashier, UserInfoModel userInfoModel,
                               ArrayList<TransaksiPenjualanModel> transModel, ArrayList<TransaksiJualBayarModel> transJualBayarModel){
        this.cash_cashier = cash_cashier;
        this.userInfoModel = userInfoModel;
        this.transModel = transModel;
        this.transJualBayarModel = transJualBayarModel;
    }

    public ArrayList<TransaksiJualBayarModel> getTransJualBayarModel() {
        return transJualBayarModel;
    }

    public void setTransJualBayarModel(ArrayList<TransaksiJualBayarModel> transJualBayarModel) {
        this.transJualBayarModel = transJualBayarModel;
    }

    public double getCash_cashier() {
        return cash_cashier;
    }

    public void setCash_cashier(double cash_cashier) {
        this.cash_cashier = cash_cashier;
    }

    public UserInfoModel getUserInfoModel() {
        return userInfoModel;
    }

    public void setUserInfoModel(UserInfoModel userInfoModel) {
        this.userInfoModel = userInfoModel;
    }

    public ArrayList<TransaksiPenjualanModel> getTransModel() {
        return transModel;
    }

    public void setTransModel(ArrayList<TransaksiPenjualanModel> transModel) {
        this.transModel = transModel;
    }
}
