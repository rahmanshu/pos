package com.trimitrasis.finosapps.Views.PosView.Model;

import com.trimitrasis.finosapps.Connection.Models.CompanyModel;
import com.trimitrasis.finosapps.Connection.Models.UserInfoModel;

import java.io.Serializable;

/**
 * Created by rahman on 07/04/2017.
 */

public class StartOfShiftModel implements Serializable {

    private static final long serialVersionUID = 2843973191941544936L;

    String userId;
    double setoranAwal;
    boolean flag_start;
    boolean flag_end;
    boolean flag_end_day;
    double cash_cashier;
    UserInfoModel userInfoModel;
    String device_id;
    String location;


    public StartOfShiftModel(String userId, double setoranAwal, boolean flag_start, boolean flag_end,
                             boolean flag_end_day, UserInfoModel userInfoModel, String device_id, String location){
        this.userId = userId;
        this.setoranAwal = setoranAwal;
        this.flag_start = flag_start;
        this.flag_end = flag_end;
        this.flag_end_day = flag_end_day;
        this.userInfoModel = userInfoModel;
        this.device_id = device_id;
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public double getSetoranAwal() {
        return setoranAwal;
    }

    public void setSetoranAwal(double setoranAwal) {
        this.setoranAwal = setoranAwal;
    }

    public boolean isFlag_start() {
        return flag_start;
    }

    public void setFlag_start(boolean flag_start) {
        this.flag_start = flag_start;
    }

    public boolean isFlag_end() {
        return flag_end;
    }

    public void setFlag_end(boolean flag_end) {
        this.flag_end = flag_end;
    }

    public boolean isFlag_end_day() {
        return flag_end_day;
    }

    public void setFlag_end_day(boolean flag_end_day) {
        this.flag_end_day = flag_end_day;
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
}
