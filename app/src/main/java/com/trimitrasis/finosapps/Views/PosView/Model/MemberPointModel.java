package com.trimitrasis.finosapps.Views.PosView.Model;

import com.trimitrasis.finosapps.Connection.Models.UserInfoModel;

import java.io.Serializable;

/**
 * Created by rahman on 04/07/2017.
 */

public class MemberPointModel implements Serializable {

    private static final long serialVersionUID = -4795153296177876190L;

    String memberId;
    UserInfoModel userInfoModel;
    String idRef;
    double point;
    String createBy;

    public MemberPointModel(String memberId, UserInfoModel userInfoModel){
        this.memberId = memberId;
        this.userInfoModel = userInfoModel;
    }


    public MemberPointModel(String memberId, UserInfoModel userInfoModel, String idRef, double point, String createBy){
        this.memberId = memberId;
        this.userInfoModel = userInfoModel;
        this.idRef = idRef;
        this.point = point;
        this.createBy = createBy;
    }


    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public UserInfoModel getUserInfoModel() {
        return userInfoModel;
    }

    public void setUserInfoModel(UserInfoModel userInfoModel) {
        this.userInfoModel = userInfoModel;
    }
}
