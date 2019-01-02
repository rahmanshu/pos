package com.trimitrasis.finosapps.Views.PosView.Model;

import com.trimitrasis.finosapps.Connection.Models.CompanyModel;
import com.trimitrasis.finosapps.Connection.Models.UserInfoModel;

import java.io.Serializable;

/**
 * Created by rahman on 07/04/2017.
 */

public class StartOfDayModel implements Serializable {
    private static final long serialVersionUID = 1572023289487166138L;
    String userId;
    String location;
    UserInfoModel userInfoModel;

    public StartOfDayModel(String userId, String location, UserInfoModel userInfoModel){
        this.userId = userId;
        this.location = location;
        this.userInfoModel = userInfoModel;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public UserInfoModel getUserInfoModel() {
        return userInfoModel;
    }

    public void setUserInfoModel(UserInfoModel userInfoModel) {
        this.userInfoModel = userInfoModel;
    }
}
