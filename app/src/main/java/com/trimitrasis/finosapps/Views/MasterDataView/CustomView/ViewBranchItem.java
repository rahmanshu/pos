package com.trimitrasis.finosapps.Views.MasterDataView.CustomView;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.trimitrasis.finosapps.R;
import com.trimitrasis.finosapps.Views.MasterDataView.Models.BranchModel;
import com.trimitrasis.finosapps.Views.MasterDataView.Models.CustomerModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rahman on 10/03/2017.
 */

public class ViewBranchItem extends LinearLayout {

    TextView textNameBranch;
    TextView textBranchEmail;
    TextView textBranchPhone;
    LinearLayout layoutBranch;

    private CallbackBranchItem callbackBranchItem;

    public ViewBranchItem(Context context, BranchModel branchModel, int position){
        super(context);
        init(0, branchModel, position);
        callbackBranchItem = (CallbackBranchItem) context;
    }


    private void init(final int index, final BranchModel branchModel, final int position){
        inflate(getContext(), R.layout.list_item_branch, this);
        this.textNameBranch  = (TextView) findViewById(R.id.textNameBranch);
        this.textBranchEmail = (TextView) findViewById(R.id.textBranchEmail);
        this.textBranchPhone = (TextView) findViewById(R.id.textBranchPhone);
        this.layoutBranch    = (LinearLayout) findViewById(R.id.layoutBranch);

        layoutBranch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view){
                getDataBranchItem(branchModel, position);
            }
        });
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

    public interface CallbackBranchItem{
        void callBranchItem(BranchModel branchModel, int position);
    }

    private void getDataBranchItem(BranchModel branchModel, int position){
        callbackBranchItem.callBranchItem(branchModel, position);
    }


}
