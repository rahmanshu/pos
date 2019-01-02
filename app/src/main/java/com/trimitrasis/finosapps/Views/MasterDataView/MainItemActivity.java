package com.trimitrasis.finosapps.Views.MasterDataView;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.trimitrasis.finosapps.Connection.ApiConnection;
import com.trimitrasis.finosapps.Connection.Models.CompanyModel;
import com.trimitrasis.finosapps.R;
import com.trimitrasis.finosapps.Views.MasterDataView.Adapter.MainItemAdapter;
import com.trimitrasis.finosapps.Views.MasterDataView.Models.ItemModel;
import com.trimitrasis.finosapps.Views.UtilView.Constants;
import com.trimitrasis.finosapps.Views.UtilView.FontUtils;
import com.trimitrasis.finosapps.Views.UtilView.Utils;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * Created by rahman on 24/03/2017.
 */

@EActivity(R.layout.layout_main_item)
public class MainItemActivity extends AppCompatActivity implements MainItemAdapter.CallbackDetailItem {

    @ViewById
    Toolbar toolbar;

    @ViewById
    RecyclerView listItem;

    RecyclerView.LayoutManager mLayoutManager;

    ProgressDialog progressDialog;

    ArrayList<ItemModel> itemModels;

    Dialog dialogItem;
    LayoutInflater inflater;

    EditText editId, editShortDesc, editLongDesc, editBaseUom, editItemGroup, editItemHierarchy;
    EditText editAccountClass, editItemConvertion, editCreateBy, editCreateDate, editCreateTime;
    View toolbar_popup;
    Button buttonUpdate, buttonDelete;
    TextView titleForm;

    @AfterViews
    void afterView(){
        itemModels = new ArrayList<>();
        itemModels.clear();
        customView();
        initSetTableView();
        getViewItem();
        inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    private void customView(){
        Button buttonCancel      = (Button) toolbar.findViewById(R.id.btnCancel);
        Button buttonNew         = (Button) toolbar.findViewById(R.id.btnSave);
        TextView titleToolbar    = (TextView) toolbar.findViewById(R.id.titleForm);
        buttonCancel.setText("Cancel");
        buttonNew.setText("New");
        titleToolbar.setText("Item");
        buttonNew.setVisibility(View.GONE);
        buttonCancel.setOnClickListener(onClickListener);
    }


    private void initSetTableView(){
        listItem.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        listItem.setLayoutManager(mLayoutManager);
    }

    private void initListSearch(){
        MainItemAdapter adapter = new MainItemAdapter(this, itemModels);
        listItem.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    View.OnClickListener onClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            switch (v.getId()){
                case R.id.btnCancel:{
                    finish();
                }break;
                case R.id.btnUpdate:{
                    finish();
                }break;
            }
        }
    };




    private void getViewItem(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading..");
        progressDialog.show();
        ApiConnection.getViewDataItemInterface().getAllDataProduct(Constants.userInfoModel,new Callback<Response>() {
            @Override
            public void success(Response responsFinos, Response response) {

                String rawResponse = ApiConnection.getRawResponse(response);
                JSONObject loginObj = null;
                try {
                    loginObj = new JSONObject(rawResponse);
                    String jStatus  = loginObj.optString("message");
                    JSONArray jResult = loginObj.optJSONArray("result");

                    if(jStatus.equals("success")){
                        itemModels.clear();
                        for(int i = 0; i < jResult.length(); i ++){
                            JSONObject jsonObject = jResult.optJSONObject(i);
                            itemModels.add(new ItemModel(
                                    jsonObject.optJSONObject("_id"),
                                    jsonObject.optString("id"),
                                    jsonObject.optString("s_description"),
                                    jsonObject.optString("l_description"),
                                    jsonObject.optString("base_uom"),
                                    jsonObject.optString("item_group"),
                                    jsonObject.optString("item_hierarchy"),
                                    jsonObject.optString("account_class"),
                                    jsonObject.optString("item_convertion"),
                                    jsonObject.optString("create_by"),
                                    jsonObject.optString("create_date"),
                                    jsonObject.optString("create_time")
                            ));
                        }

                        initListSearch();

                    }else{
                        Utils.showToast("data item not found", MainItemActivity.this);
                        if (progressDialog!=null)progressDialog.dismiss();
                    }


                }catch(JSONException e){
                    e.printStackTrace();
                    progressDialog.dismiss();
                }


                if (progressDialog!=null)progressDialog.dismiss();

            }

            @Override
            public void failure(RetrofitError error) {
                if (progressDialog!=null)progressDialog.dismiss();
            }
        });
    }


    private CompanyModel getCompanyModel(){
        CompanyModel companyModel = new CompanyModel(
                Constants.companyModel.getNameCompany(),
                Constants.companyModel.getEmailCompany(),
                Constants.companyModel.getCountryCompany(),
                Constants.companyModel.getCurrencyCompany(),
                Constants.companyModel.getDbName(),
                Constants.companyModel.getDbSize()
        );
        return  companyModel;
    }


    @Override
    public void getDataitem(ItemModel itemModel){
        showDailogItem(itemModel);
    }


    private void showDailogItem(ItemModel itemModel){
        dialogItem = new Dialog(this);
        View viewsItem = inflater.inflate(R.layout.popup_dialog_item, null);
        declareLayout(viewsItem);
        setDataItem(itemModel);
        setFontDialog();
        setActivateField(false);
        dialogItem.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogItem.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#50000000")));
        dialogItem.setContentView(viewsItem);
        dialogItem.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialogItem.show();
        buttonUpdate.setOnClickListener(onClickListener);
        buttonDelete.setOnClickListener(onClickListener);
    }

    private void declareLayout(View viewsItem){
        toolbar_popup      = (View)     viewsItem.findViewById(R.id.toolbar_popup);
        buttonUpdate       = (Button)   toolbar_popup.findViewById(R.id.btnUpdate);
        buttonDelete       = (Button)   toolbar_popup.findViewById(R.id.btnDelete);
        titleForm          = (TextView) toolbar_popup.findViewById(R.id.titleForm);
        editId             = (EditText) viewsItem.findViewById(R.id.editId);
        editShortDesc      = (EditText) viewsItem.findViewById(R.id.editShortDesc);
        editLongDesc       = (EditText) viewsItem.findViewById(R.id.editLongDesc);
        editBaseUom        = (EditText) viewsItem.findViewById(R.id.editBaseUom);
        editItemGroup      = (EditText) viewsItem.findViewById(R.id.editItemGroup);
        editItemHierarchy  = (EditText) viewsItem.findViewById(R.id.editItemHierarchy);
        editAccountClass   = (EditText) viewsItem.findViewById(R.id.editAccountClass);
        editItemConvertion = (EditText) viewsItem.findViewById(R.id.editItemConvertion);
        editCreateBy       = (EditText) viewsItem.findViewById(R.id.editCreateBy);
        editCreateDate     = (EditText) viewsItem.findViewById(R.id.editCreateDate);
        editCreateTime     = (EditText) viewsItem.findViewById(R.id.editCreateTime);
    }


    private void setActivateField(boolean status){
        editId.setEnabled(status);
        editShortDesc.setEnabled(status);
        editLongDesc.setEnabled(status);
        editBaseUom.setEnabled(status);
        editItemGroup.setEnabled(status);
        editItemHierarchy.setEnabled(status);
        editAccountClass.setEnabled(status);
        editItemConvertion.setEnabled(status);
        editCreateBy.setEnabled(status);
        editCreateDate.setEnabled(status);
        editCreateTime.setEnabled(status);
    }

    private void setFontDialog(){
        editId.setTypeface(FontUtils.getHelvetica_Neue_LT(this));
        editShortDesc.setTypeface(FontUtils.getHelvetica_Neue_LT(this));
        editLongDesc.setTypeface(FontUtils.getHelvetica_Neue_LT(this));
        editBaseUom.setTypeface(FontUtils.getHelvetica_Neue_LT(this));
        editItemGroup.setTypeface(FontUtils.getHelvetica_Neue_LT(this));
        editItemHierarchy.setTypeface(FontUtils.getHelvetica_Neue_LT(this));
        editAccountClass.setTypeface(FontUtils.getHelvetica_Neue_LT(this));
        editItemConvertion.setTypeface(FontUtils.getHelvetica_Neue_LT(this));
        editCreateBy.setTypeface(FontUtils.getHelvetica_Neue_LT(this));
        editCreateDate.setTypeface(FontUtils.getHelvetica_Neue_LT(this));
        editCreateTime.setTypeface(FontUtils.getHelvetica_Neue_LT(this));
        buttonUpdate.setText("Cancel");
        buttonDelete.setText("Delete");
        titleForm.setText("Item");
        buttonDelete.setVisibility(View.GONE);
    }

    private void setDataItem(ItemModel itemModel){
        editId.setText(itemModel.getId());
        editShortDesc.setText(itemModel.getS_description());
        editLongDesc.setText(itemModel.getL_description());
        editBaseUom.setText(itemModel.getBaseUom());
        editItemGroup.setText(itemModel.getItemGroup());
        editItemHierarchy.setText(itemModel.getItemHierarchy());
        editAccountClass.setText(itemModel.getAccountClass());
        editItemConvertion.setText(itemModel.getItemConvertion());
        editCreateBy.setText(itemModel.getCreateBy());
        editCreateDate.setText(itemModel.getCreateDate());
        editCreateTime.setText(itemModel.getCreateTime());
    }

}
