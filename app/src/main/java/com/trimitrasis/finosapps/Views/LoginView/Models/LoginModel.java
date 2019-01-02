package com.trimitrasis.finosapps.Views.LoginView.Models;

/**
 * Created by rahman on 02/03/2017.
 */

public class LoginModel {

    String jenis = "login";
    String tgl;
    String deviceid;
    String username;
    String password;
    String email;


    public LoginModel(String tgl, String deviceid, String pass, String email){
        this.tgl  = tgl;
        this.deviceid = deviceid;
        this.password = pass;
        this.email = email;
    }


}
