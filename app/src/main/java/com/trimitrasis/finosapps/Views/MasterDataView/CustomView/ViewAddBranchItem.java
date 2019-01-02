package com.trimitrasis.finosapps.Views.MasterDataView.CustomView;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.trimitrasis.finosapps.R;

/**
 * Created by rahman on 26/04/2017.
 */

public class ViewAddBranchItem extends LinearLayout {

    TextView textNameBranch;
    TextView textBranchEmail;
    TextView textBranchPhone;
    LinearLayout layoutBranch;


    public ViewAddBranchItem(Context context){
        super(context);
        init(0);
    }


    private void init(final int index){
        inflate(getContext(), R.layout.list_item_branch, this);
        this.textNameBranch  = (TextView) findViewById(R.id.textNameBranch);
        this.textBranchEmail = (TextView) findViewById(R.id.textBranchEmail);
        this.textBranchPhone = (TextView) findViewById(R.id.textBranchPhone);
        this.layoutBranch    = (LinearLayout) findViewById(R.id.layoutBranch);
    }


    public TextView getTextNameBranch(){
        return textNameBranch;
    }

    public void setTextNameBranch(String textNameBranch){
        this.textNameBranch.setText(textNameBranch);
    }

    public TextView getTextBranchEmail(){
        return textBranchEmail;
    }

    public void setTextBranchEmail(String textBranchEmail){
        this.textBranchEmail.setText(textBranchEmail);
    }

    public TextView getTextBranchPhone(){
        return textBranchPhone;
    }

    public void setTextBranchPhone(String textBranchPhone){
        this.textBranchPhone.setText(textBranchPhone);
    }

}
