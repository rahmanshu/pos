package com.trimitrasis.finosapps.Views.PosView.Model;

import com.trimitrasis.finosapps.Connection.Models.UserInfoModel;


import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by rahman on 09/06/2017.
 */

public class SyncTransModel implements Serializable {

    private static final long serialVersionUID = -2405239538729658649L;
    UserInfoModel userInfoModel;
    ArrayList<TransaksiPenjualanModel> transModel;
    ArrayList<TransaksiJualBayarModel> transJualBayarModel;

    public SyncTransModel(UserInfoModel userInfoModel, ArrayList<TransaksiPenjualanModel> transModel, ArrayList<TransaksiJualBayarModel> transJualBayarModel){
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
