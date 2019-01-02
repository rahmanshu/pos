package com.trimitrasis.finosapps.Views.SalesOrderView;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.trimitrasis.finosapps.Connection.ApiConnection;
import com.trimitrasis.finosapps.Connection.Models.CompanyModel;
import com.trimitrasis.finosapps.R;
import com.trimitrasis.finosapps.Views.MasterDataView.Models.BranchModel;
import com.trimitrasis.finosapps.Views.MasterDataView.Models.CustomerModel;
import com.trimitrasis.finosapps.Views.SalesOrderView.CustomView.ViewItemSalesOrder;
import com.trimitrasis.finosapps.Views.SalesOrderView.Models.DetailSalesQuotationModel;
import com.trimitrasis.finosapps.Views.SalesOrderView.Models.SalesQuotationModel;
import com.trimitrasis.finosapps.Views.SalesQuotationView.Models.ItemConvertionSalesQuotModel;
import com.trimitrasis.finosapps.Views.SalesQuotationView.Models.ItemSalesQuotation;
import com.trimitrasis.finosapps.Views.SalesQuotationView.Models.TaxGroupModelNew;
import com.trimitrasis.finosapps.Views.UtilView.Constants;
import com.trimitrasis.finosapps.Views.UtilView.DateUtils;
import com.trimitrasis.finosapps.Views.UtilView.FontUtils;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * Created by rahman on 27/03/2017.
 */


@EActivity(R.layout.layout_sales_order)
public class SalesOrderActivity extends AppCompatActivity implements ViewItemSalesOrder.CallbackDetailItem {

    ArrayList<ViewItemSalesOrder> detailItemArrayList;

    @ViewById
    Button btnAddItem;

    @ViewById
    Toolbar toolbar;

    @ViewById
    LinearLayout contentViewDetailItem;

    @ViewById
    SlidingUpPanelLayout panelLayout;

    @ViewById
    Spinner spinnerSalesQuotation, spinnerCustomer, spinnerBranch, spinnerPaymentTerm, spinnerTaxGroup;

    @ViewById
    TextView orderDate, deliveryDate;

    @ViewById
    TextView subTotal;

    @ViewById
    View lineUp, lineButtom;

    ProgressDialog progressDialogSalesQuotation;

    String orderDateString, deliveryDateString;

    ViewItemSalesOrder viewDetailItemSalesOrder;

    ProgressDialog progressDialog, progressDialogCust, progressDialogPaymentTerm, progressDialogTaxGroup;

    int indexSpinnerCustomer = 0;
    int indexSpinnerBranch = 0;
    int indexSpinnerPaymentTerm = 0;
    int indexSpinnerTaxGroup = 0;
    int indexSpinnerItem = 0;
    int indexSpinnerUom = 0;

    String detailItemName;
    String branchNameSq = ""; String paymentTermSq = ""; String taxGroupSq = "";String salesQuotationString = "";
    String customerNameString, branchNameString, paymentTermString, taxGroupString;
    List<String> listSalesQuotationString = new ArrayList<>();
    List<String> listCustomerString = new ArrayList<>();
    List<String> listBranchString = new ArrayList<>();
    List<String> listPaymentTermString = new ArrayList<>();
    List<String> listTaxGroupString = new ArrayList<>();

    List<BranchModel> branchModels;
    ArrayList<SalesQuotationModel> salesQuotationModels;
    ArrayList<DetailSalesQuotationModel> detailSalesQuotationModels;
    ArrayList<DetailSalesQuotationModel> detailSalesQuot;
    ArrayList<CustomerModel> mainCustomerModels;

    Dialog dialogAddItem, dialogDetailItem;
    Button btnCancelItem, btnSaveItem, btnCancelItemUpdate, btnSaveItemUpdate;
    Spinner spinnerItem, spinnerUom;
    EditText editQuantity, editPrice, editDiscount, editTotal, editTaxItem;
    LayoutInflater inflater;
    ArrayList<ItemSalesQuotation> itemSalesQuotations;
    ArrayList<ItemConvertionSalesQuotModel> itemConvertionSalesQuotModels;
    ArrayList<String> listItemString;
    ArrayList<String> listDetailItemString;
    ArrayList<String> listUomItemString;
    String itemSalesString, uomItemString;
    double qtyItemSq, priceItemSq, discountItemSq, totalItemSq;
    String taxGroupId_;
    ArrayList<TaxGroupModelNew> taxGroupModels;
    double qtyItem, priceItem, discountItem, totalItem;
    int positionDetailItem = 0;

    @AfterViews
    void afterView(){
        inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        salesQuotationModels = new ArrayList<>();
        detailSalesQuotationModels = new ArrayList<>();
        detailSalesQuot = new ArrayList<>();
        mainCustomerModels = new ArrayList<>();
        branchModels = new ArrayList<>();
        detailItemArrayList = new ArrayList<>();
        customView();
        listSalesQuotationString.add("New Sales Quotation..");
        listCustomerString.add("Select Customer..");
        listPaymentTermString.add("Select Payment Term..");
        getDataTaxGroup();
        getDataPaymentTerms();
        getDataCustomer();
        getSalesQuotation();
        itemSalesQuotations = new ArrayList<>();
        itemConvertionSalesQuotModels = new ArrayList<>();
        listItemString = new ArrayList<>();
        listDetailItemString = new ArrayList<>();
        listUomItemString = new ArrayList<>();
        taxGroupModels = new ArrayList<>();
    }


    @Click
    void orderDate(){
        DateUtils.showDatePickerDialog(this.getSupportFragmentManager(), new DateUtils.DateDialogPickerListener() {
            @Override
            public void onDatePick(String date, String month, String year) {
                orderDateString = year + "-" + DateUtils.getMonthNumber(month) + "-" + DateUtils.getDateNumberString(date);
                orderDate.setText(date + " " + DateUtils.getMonthString(month) + " " + year);
                orderDate.setTypeface(FontUtils.getFontHeaderToolbar(SalesOrderActivity.this));
            }
        }, DateUtils.DatePickerFragment.TYPE_BIRTH_DATE);
    }


    @Click
    void deliveryDate(){
        DateUtils.showDatePickerDialog(this.getSupportFragmentManager(), new DateUtils.DateDialogPickerListener() {
            @Override
            public void onDatePick(String date, String month, String year) {
                deliveryDateString = year + "-" + DateUtils.getMonthNumber(month) + "-" + DateUtils.getDateNumberString(date);
                deliveryDate.setText(date + " " + DateUtils.getMonthString(month) + " " + year);
                deliveryDate.setTypeface(FontUtils.getFontHeaderToolbar(SalesOrderActivity.this));
            }
        }, DateUtils.DatePickerFragment.TYPE_BIRTH_DATE);
    }



    @Click
    void btnAddItem(){
        showDailogItem();
        getItemSalesOrder(1);
        editQtyOnChange();
        editDiscountOnChange();
    }


    private void showDailogItem(){
        dialogAddItem = new Dialog(this);
        View viewsItem = inflater.inflate(R.layout.popup_sales_order, null);
        btnCancelItem      = (Button)   viewsItem.findViewById(R.id.btnCancelItem);
        btnSaveItem        = (Button)   viewsItem.findViewById(R.id.btnSaveItem);
        spinnerItem        = (Spinner)  viewsItem.findViewById(R.id.spinnerItem);
        editQuantity       = (EditText) viewsItem.findViewById(R.id.editQuantity);
        spinnerUom         = (Spinner)  viewsItem.findViewById(R.id.spinnerUom);
        editPrice          = (EditText) viewsItem.findViewById(R.id.editPrice);
        editTaxItem        = (EditText) viewsItem.findViewById(R.id.editTaxItem);
        editDiscount       = (EditText) viewsItem.findViewById(R.id.editDiscount);
        editTotal          = (EditText) viewsItem.findViewById(R.id.editTotal);

        btnCancelItem.setOnClickListener(onClickListener);
        btnSaveItem.setOnClickListener(onClickListener);
        dialogAddItem.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogAddItem.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#50000000")));
        dialogAddItem.setContentView(viewsItem);
        dialogAddItem.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialogAddItem.show();
    }



    private void customView(){
        Button buttonCancel = (Button) toolbar.findViewById(R.id.btnCancel);
        buttonCancel.setText("Cancel");
        TextView titleFormSales = (TextView) toolbar.findViewById(R.id.titleForm);
        titleFormSales.setText("Sales Order");
        Button buttonSave = (Button) toolbar.findViewById(R.id.btnSave);
        buttonSave.setText("Save");
        buttonCancel.setOnClickListener(onClickListener);
        buttonSave.setOnClickListener(onClickListener);
    }


    private void spinnerTaxGroup(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, listTaxGroupString){

            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                TextView view = (TextView) super.getView(position, convertView, parent);
                view.setTypeface(FontUtils.getFontHeaderToolbar(SalesOrderActivity.this));
                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent){
                TextView view = (TextView)super.getDropDownView(position, convertView, parent);
                view.setTypeface(FontUtils.getFontHeaderToolbar(SalesOrderActivity.this));
                return view;
            }

        };

        spinnerTaxGroup.setAdapter(adapter);
        spinnerTaxGroup.setSelection(indexSpinnerTaxGroup);
        spinnerTaxGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
                taxGroupString = listTaxGroupString.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent){
            }
        });
    }


    private void spinnerPaymentTerm(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, listPaymentTermString){

            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                TextView view = (TextView) super.getView(position, convertView, parent);
                view.setTypeface(FontUtils.getFontHeaderToolbar(SalesOrderActivity.this));
                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent){
                TextView view = (TextView)super.getDropDownView(position, convertView, parent);
                view.setTypeface(FontUtils.getFontHeaderToolbar(SalesOrderActivity.this));
                return view;
            }

        };

        spinnerPaymentTerm.setAdapter(adapter);
        spinnerPaymentTerm.setSelection(indexSpinnerPaymentTerm);
        spinnerPaymentTerm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
                paymentTermString = listPaymentTermString.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }


    private void spinnerCustomerName(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, listCustomerString){

            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                TextView view = (TextView) super.getView(position, convertView, parent);
                view.setTypeface(FontUtils.getFontHeaderToolbar(SalesOrderActivity.this));
                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent){
                TextView view = (TextView)super.getDropDownView(position, convertView, parent);
                view.setTypeface(FontUtils.getFontHeaderToolbar(SalesOrderActivity.this));
                return view;
            }

        };

        spinnerCustomer.setAdapter(adapter);
        spinnerCustomer.setSelection(indexSpinnerCustomer);
        spinnerCustomer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
                customerNameString = listCustomerString.get(position);
                getBranch(customerNameString);
                getPaymentTerm();
                getTaxGroup();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }


    private void spinnerBranchName(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, listBranchString){

            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                TextView view = (TextView) super.getView(position, convertView, parent);
                view.setTypeface(FontUtils.getFontHeaderToolbar(SalesOrderActivity.this));
                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent){
                TextView view = (TextView)super.getDropDownView(position, convertView, parent);
                view.setTypeface(FontUtils.getFontHeaderToolbar(SalesOrderActivity.this));
                return view;
            }
        };

        spinnerBranch.setAdapter(adapter);
        spinnerBranch.setSelection(indexSpinnerBranch);
        spinnerBranch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
                branchNameString = listBranchString.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent){

            }
        });
    }


    private void spinnerSalesQuot(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, listSalesQuotationString){

            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                TextView view = (TextView) super.getView(position, convertView, parent);
                view.setTypeface(FontUtils.getFontHeaderToolbar(SalesOrderActivity.this));
                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent){
                TextView view = (TextView)super.getDropDownView(position, convertView, parent);
                view.setTypeface(FontUtils.getFontHeaderToolbar(SalesOrderActivity.this));
                return view;
            }
        };

        spinnerSalesQuotation.setAdapter(adapter);
        spinnerSalesQuotation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
                salesQuotationString = listSalesQuotationString.get(position);
                getDataHeaderSalesQuot(salesQuotationString);
                spinnerCustomerName();
                getDetailSalesQuot(salesQuotationString);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent){

            }

        });
    }


    private void spinnerItemDetail(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, listItemString){

            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                TextView view = (TextView) super.getView(position, convertView, parent);
                view.setTypeface(FontUtils.getFontHeaderToolbar(SalesOrderActivity.this));
                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent){
                TextView view = (TextView)super.getDropDownView(position, convertView, parent);
                view.setTypeface(FontUtils.getFontHeaderToolbar(SalesOrderActivity.this));
                return view;
            }
        };

        spinnerItem.setAdapter(adapter);
        spinnerItem.setSelection(indexSpinnerItem);
        spinnerItem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
                itemSalesString = listItemString.get(position);
                getDetailItemEdit(itemSalesString);
                getTaxGroupItem(itemSalesString);
                getAkumulasiTotalItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent){

            }

        });
    }

    private void spinnerItem(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, listItemString){

            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                TextView view = (TextView) super.getView(position, convertView, parent);
                view.setTypeface(FontUtils.getFontHeaderToolbar(SalesOrderActivity.this));
                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent){
                TextView view = (TextView)super.getDropDownView(position, convertView, parent);
                view.setTypeface(FontUtils.getFontHeaderToolbar(SalesOrderActivity.this));
                return view;
            }
        };

        spinnerItem.setAdapter(adapter);
        spinnerItem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
                itemSalesString = listItemString.get(position);
                getDetailItem(itemSalesString);
                getTaxGroupItem(itemSalesString);
                getAkumulasiTotalItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent){

            }
        });
    }

    private void spinnerUomDetailName(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, listUomItemString){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                TextView view = (TextView) super.getView(position, convertView, parent);
                view.setTypeface(FontUtils.getFontHeaderToolbar(SalesOrderActivity.this));
                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent){
                TextView view = (TextView)super.getDropDownView(position, convertView, parent);
                view.setTypeface(FontUtils.getFontHeaderToolbar(SalesOrderActivity.this));
                return view;
            }
        };

        spinnerUom.setAdapter(adapter);
        spinnerUom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
                uomItemString = listUomItemString.get(position);
                getPriceFromUom(itemSalesString, uomItemString);
                getAkumulasiTotalItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent){

            }
        });
    }

    private void spinnerUomName(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, listUomItemString){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                TextView view = (TextView) super.getView(position, convertView, parent);
                view.setTypeface(FontUtils.getFontHeaderToolbar(SalesOrderActivity.this));
                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent){
                TextView view = (TextView)super.getDropDownView(position, convertView, parent);
                view.setTypeface(FontUtils.getFontHeaderToolbar(SalesOrderActivity.this));
                return view;
            }
        };

        spinnerUom.setAdapter(adapter);
        spinnerUom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
                uomItemString = listUomItemString.get(position);
                getPriceFromUom(itemSalesString, uomItemString);
                getAkumulasiTotalItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent){

            }
        });
    }


    public void editQtyOnChange(){
        editQuantity.addTextChangedListener(new TextWatcher(){

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after){
            }

            @Override
            public void onTextChanged(CharSequence query, int start, int before, int count){

                try{
                    if(query.toString().equals("")){
                        qtyItemSq = 0;
                    }else{
                        qtyItemSq = Double.parseDouble(query.toString());
                    }

                    priceItemSq = Double.parseDouble(editPrice.getText().toString());
                    totalItemSq = (qtyItemSq * priceItemSq) - discountItemSq;
                    editTotal.setText(""+totalItemSq);
                    editTotal.setEnabled(false);


                }catch(NumberFormatException e){

                }
            }

            @Override
            public void afterTextChanged(Editable s){

            }
        });
    }


    public void editDiscountOnChange(){
        editDiscount.addTextChangedListener(new TextWatcher(){

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after){
            }

            @Override
            public void onTextChanged(CharSequence query, int start, int before, int count){
                try{
                    if(query.toString().equals("")){
                        discountItemSq = 0;
                    }else{
                        discountItemSq = Double.parseDouble(query.toString());
                    }

                    priceItemSq = Double.parseDouble(editPrice.getText().toString());
                    totalItemSq = (qtyItemSq * priceItemSq) - discountItemSq;
                    editTotal.setText(""+totalItemSq);
                    editTotal.setEnabled(false);

                }catch(NumberFormatException e){
                }
            }

            @Override
            public void afterTextChanged(Editable s){

            }
        });
    }


    private void getAkumulasiTotalItem(){
        if(!editPrice.getText().toString().equals("")){
            priceItemSq = Double.parseDouble(editPrice.getText().toString());
        }else{
            priceItemSq = 0;
        }

        if(!editQuantity.getText().toString().equals("")){
            qtyItemSq = Double.parseDouble(editQuantity.getText().toString());
        }else{
            qtyItemSq = 0;
        }

        if(!editDiscount.getText().toString().equals("")){
            discountItemSq = Double.parseDouble(editDiscount.getText().toString());
        }else{
            discountItemSq = 0;
        }

        totalItemSq = (qtyItemSq * priceItemSq) - discountItemSq;
        editTotal.setText(""+totalItemSq);
        editTotal.setEnabled(false);
    }


    private void updateDetailSalesOrder(int position){

        qtyItem      = (!editQuantity.getText().toString().equals("")) ? Double.parseDouble(editQuantity.getText().toString()) : 0;
        priceItem    = (!editPrice.getText().toString().equals("")) ? Double.parseDouble(editPrice.getText().toString()) : 0;
        discountItem = (!editDiscount.getText().toString().equals("")) ? Double.parseDouble(editDiscount.getText().toString()) : 0;
        totalItem    = (!editTotal.getText().toString().equals("")) ? Double.parseDouble(editTotal.getText().toString()) : 0;

        detailSalesQuotationModels.set(position, new DetailSalesQuotationModel(
                itemSalesString,
                itemSalesString,
                qtyItem,
                uomItemString,
                priceItem,
                discountItem,
                totalItem,
                salesQuotationString
        ));

        getDetailSalesQuot(salesQuotationString);
    }


    private void addDetailSalesOrder(){

        qtyItem      = (!editQuantity.getText().toString().equals("")) ? Double.parseDouble(editQuantity.getText().toString()) : 0;
        priceItem    = (!editPrice.getText().toString().equals("")) ? Double.parseDouble(editPrice.getText().toString()) : 0;
        discountItem = (!editDiscount.getText().toString().equals("")) ? Double.parseDouble(editDiscount.getText().toString()) : 0;
        totalItem    = (!editTotal.getText().toString().equals("")) ? Double.parseDouble(editTotal.getText().toString()) : 0;

        detailSalesQuotationModels.add(new DetailSalesQuotationModel(
                itemSalesString,
                itemSalesString,
                qtyItem,
                uomItemString,
                priceItem,
                discountItem,
                totalItem,
                salesQuotationString
        ));

        getDetailSalesQuot(salesQuotationString);
    }


    private void getPriceFromUom(String item_id, String uom){
        for(int j = 0; j < itemConvertionSalesQuotModels.size(); j++){
            if(item_id.equals(itemConvertionSalesQuotModels.get(j).getItem_id()) && uom.equals(itemConvertionSalesQuotModels.get(j).getUom())){
                editPrice.setText(""+itemConvertionSalesQuotModels.get(j).getHarga_jual());
                editPrice.setEnabled(false);
            }
        }
    }


    private void getDetailItemEdit(String item_id){
        listUomItemString.clear();
        for(int i = 0; i < itemConvertionSalesQuotModels.size(); i++){
            if(item_id.equals(itemConvertionSalesQuotModels.get(i).getItem_id())){
                listUomItemString.add(itemConvertionSalesQuotModels.get(i).getUom());
            }
        }
        spinnerUomDetailName();
    }

    private void getDetailItem(String item_id){
        listUomItemString.clear();
        for(int i = 0; i < itemConvertionSalesQuotModels.size(); i++){
            if(item_id.equals(itemConvertionSalesQuotModels.get(i).getItem_id())){
                listUomItemString.add(itemConvertionSalesQuotModels.get(i).getUom());
            }
        }
        spinnerUomName();
    }


    private void getTaxGroupItem(String item_id){
        for(int i = 0; i < itemSalesQuotations.size(); i++){
            if(item_id.equals(itemSalesQuotations.get(i).getId())){
                taxGroupId_ = itemSalesQuotations.get(i).getTax_group();
                getDescTaxGroup(taxGroupId_);
            }
        }
    }


    private void getDescTaxGroup(String taxGroupId){
        String taxGroupName = "";
        for(int i = 0; i < taxGroupModels.size(); i++){
            if(taxGroupId.equals(taxGroupModels.get(i).getIdTaxGroup())){
                taxGroupName = taxGroupModels.get(i).getDescription();
            }
        }
        editTaxItem.setText(taxGroupName);
        editTaxItem.setEnabled(false);
    }



    private void getDetailSalesQuot(String trans_no){

        detailItemArrayList.clear();
        contentViewDetailItem.removeAllViews();
        int incremen_so = 0; double subTotalBayar = 0;

        for(int i = 0; i < detailSalesQuotationModels.size(); i++){
            if(trans_no.equals(detailSalesQuotationModels.get(i).getTransNo())){
                viewDetailItemSalesOrder = new ViewItemSalesOrder(this, detailSalesQuotationModels.get(i), i);
                viewDetailItemSalesOrder.setTextItemName(detailSalesQuotationModels.get(i).getDescription());
                viewDetailItemSalesOrder.setTextQty(String.valueOf(detailSalesQuotationModels.get(i).getQty()) + " " + String.valueOf(detailSalesQuotationModels.get(i).getUom()));
                viewDetailItemSalesOrder.setTextTotal(String.valueOf(detailSalesQuotationModels.get(i).getTotal()));
                viewDetailItemSalesOrder.setTextDiscount("Discount : " + String.valueOf(detailSalesQuotationModels.get(i).getDiscount()));
                detailItemArrayList.add(incremen_so, viewDetailItemSalesOrder);
                contentViewDetailItem.addView(detailItemArrayList.get(incremen_so));
                subTotalBayar = subTotalBayar + detailSalesQuotationModels.get(i).getTotal();
                incremen_so++ ;
            }
        }

        if(incremen_so == 0){
            lineUp.setVisibility(View.GONE);
        }else{
            lineUp.setVisibility(View.VISIBLE);
        }

        subTotal.setText("" + subTotalBayar);
    }




    private void getDataHeaderSalesQuot(String trans_no){
        for(int i = 0; i < salesQuotationModels.size(); i++){
            if(trans_no.equals(salesQuotationModels.get(i).getTransNo())){
                indexSpinnerCustomer  = listCustomerString.indexOf(salesQuotationModels.get(i).getCustomerName());
                branchNameSq          = salesQuotationModels.get(i).getBranchName();
                paymentTermSq         = salesQuotationModels.get(i).getPaymentTerm();
                taxGroupSq            = salesQuotationModels.get(i).getTax();
            }
        }
    }


    private void getBranch(String customerName){
        listBranchString.clear();
        listBranchString.add("Select Branch..");
        for(int i = 0; i < branchModels.size(); i++){
            if(customerName.equals(branchModels.get(i).getCustomerName())){
                listBranchString.add(branchModels.get(i).getName());
            }
        }
        indexSpinnerBranch = listBranchString.indexOf(branchNameSq);
        spinnerBranchName();
    }


    private void getPaymentTerm(){
        indexSpinnerPaymentTerm = listPaymentTermString.indexOf(paymentTermSq);
        spinnerPaymentTerm();
    }

    private void getTaxGroup(){
        indexSpinnerTaxGroup = listTaxGroupString.indexOf(taxGroupSq);
        spinnerTaxGroup();
    }






    View.OnClickListener onClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            switch (v.getId()) {
                case R.id.btnCancel:{
                    finish();
                }break;
                case R.id.btnSave:{
                }break;
                case R.id.btnSaveItem:{
                    addDetailSalesOrder();
                    dialogAddItem.dismiss();
                }break;
                case R.id.btnCancelItem:{
                    dialogAddItem.dismiss();
                }break;
            }
        }
    };


    private CompanyModel getCompanyModel(){
        CompanyModel companyModel = new CompanyModel(
                Constants.companyModel.getNameCompany(),
                Constants.companyModel.getEmailCompany(),
                Constants.companyModel.getCountryCompany(),
                Constants.companyModel.getCurrencyCompany(),
                Constants.companyModel.getDbName(),
                Constants.companyModel.getDbSize()
        );
        return companyModel;
    }



    private void getSalesQuotation(){

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading..");
        progressDialog.show();

        ApiConnection.getDataSalesQuotationInterface().getSalesQuotation(Constants.userInfoModel, new Callback<Response>(){

            @Override
            public void success(Response response, Response response2){
                String rawResponse = ApiConnection.getRawResponse(response);
                JSONObject salesOrderObj = null; JSONArray jResult;
                String jStatus;

                try{
                    salesOrderObj = new JSONObject(rawResponse);
                    jStatus       = salesOrderObj.optString("message");
                    jResult       = salesOrderObj.optJSONArray("result");

                    String valid_date;
                    if(jStatus.equals("success")){

                        detailSalesQuotationModels.clear();
                        salesQuotationModels.clear();
                        for(int i = 0; i < jResult.length(); i++){

                            JSONObject jsonObject   = jResult.optJSONObject(i);
                            JSONObject idObj        = (JSONObject) jsonObject.get("_id");
                            String salesOrderId     = (String) idObj.get("$id");
                            JSONArray jsonArrDetail = jsonObject.optJSONArray("detail");
                            valid_date              = jsonObject.optString("valid_date");

                            if(jsonArrDetail != null){
                                for(int j = 0; j < jsonArrDetail.length(); j++){
                                    JSONObject jsonObjectDetail = jsonArrDetail.optJSONObject(j);
                                    JSONObject idObjDetail      = jsonObjectDetail.getJSONObject("item_id");
                                    String detailId             = (String) idObjDetail.get("$id");

                                    detailSalesQuotationModels.add(new DetailSalesQuotationModel(
                                            jsonObject.optJSONObject("_id"),
                                            jsonObjectDetail.optJSONObject("item_id"),
                                            detailId,
                                            jsonObjectDetail.optString("description"),
                                            jsonObjectDetail.optDouble("qty"),
                                            jsonObjectDetail.optString("uom"),
                                            jsonObjectDetail.optDouble("price"),
                                            jsonObjectDetail.optDouble("discount"),
                                            jsonObjectDetail.optDouble("total"),
                                            jsonObject.optString("trans_no")));
                                }
                            }

                            salesQuotationModels.add(new SalesQuotationModel(
                                    jsonObject.optJSONObject("_id"),
                                    salesOrderId,
                                    jsonObject.optString("customer_id"),
                                    jsonObject.optString("customer_name"),
                                    jsonObject.optString("branch_id"),
                                    jsonObject.optString("branch_name"),
                                    jsonObject.optString("payment_term"),
                                    jsonObject.optString("quote_date"),
                                    jsonObject.optString("valid_date"),
                                    jsonObject.optString("tax"),
                                    jsonObject.optString("delivery_from"),
                                    jsonObject.optString("delivery_to"),
                                    jsonObject.optString("address"),
                                    jsonObject.optString("contact_number"),
                                    jsonObject.optString("notes"),
                                    jsonObject.optDouble("amount"),
                                    jsonObject.optDouble("discount"),
                                    jsonObject.optString("trans_no")));

                                    listSalesQuotationString.add(jsonObject.optString("trans_no"));
                        }

                        spinnerSalesQuot();
                    }

                }catch(JSONException e){
                    e.printStackTrace();
                    progressDialog.dismiss();
                }

                if(progressDialog!=null)progressDialog.dismiss();
            }

            @Override
            public void failure(RetrofitError error){
                if (progressDialog!=null)progressDialog.dismiss();
            }
        });
    }



    private void getDataCustomer(){

        progressDialogCust = new ProgressDialog(this);
        progressDialogCust.setMessage("Loading..");
        progressDialogCust.show();

        ApiConnection.getViewDataCustomerInterface().getViewDataCustomer(Constants.userInfoModel, new Callback<Response>() {
            @Override
            public void success(Response responsFinos, Response response){
                String rawResponse = ApiConnection.getRawResponse(response);
                JSONObject vendorObj = null;
                try {
                    vendorObj = new JSONObject(rawResponse);
                    String jStatus  = vendorObj.optString("message");
                    JSONArray jResult = vendorObj.optJSONArray("result");

                    if(jStatus.equals("success")){

                        mainCustomerModels.clear();
                        branchModels.clear();
                        for(int i = 0; i < jResult.length(); i ++){

                            JSONObject jsonObject   = jResult.optJSONObject(i);
                            JSONArray jsonArrBranch = jsonObject.optJSONArray("branch");

                            if(jsonArrBranch != null){
                                for(int j = 0; j < jsonArrBranch.length(); j++){
                                    JSONObject jsonObjectBranch = jsonArrBranch.optJSONObject(j);

                                    branchModels.add(new BranchModel(
                                            jsonObject.optJSONObject("_id"),
                                            jsonObject.optString("name"),
                                            jsonObjectBranch.optString("nama"),
                                            jsonObjectBranch.optString("address"),
                                            jsonObjectBranch.optString("phone1"),
                                            jsonObjectBranch.optString("phone2"),
                                            jsonObjectBranch.optString("fax"),
                                            jsonObjectBranch.optString("email")));
                                }
                            }

                            mainCustomerModels.add(i, new CustomerModel(
                                            jsonObject.optJSONObject("_id"),
                                            jsonObject.optString("name"),
                                            jsonObject.optString("phone1"),
                                            jsonObject.optString("phone2"),
                                            jsonObject.optString("address"),
                                            jsonObject.optString("fax"),
                                            jsonObject.optString("email"),
                                            jsonObject.optString("recon_account"),
                                            jsonObject.optString("currency"),
                                            jsonObject.optDouble("credit_limit"),
                                            jsonObject.optString("payment_terms"),
                                            jsonObject.optBoolean("pkp"),
                                            jsonObject.optString("tax_group")));

                            listCustomerString.add(jsonObject.optString("name"));
                        }

                    }


                }catch(JSONException e){
                    e.printStackTrace();
                    progressDialogCust.dismiss();
                }

                if (progressDialogCust!=null)progressDialogCust.dismiss();
            }


            @Override
            public void failure(RetrofitError error) {
                if (progressDialogCust!=null)progressDialogCust.dismiss();
            }

        });
    }


    private void getDataPaymentTerms(){

        progressDialogPaymentTerm = new ProgressDialog(this);
        progressDialogPaymentTerm.setMessage("Loading..");
        progressDialogPaymentTerm.show();

        ApiConnection.getPaymentTermInterface().getDataPaymentTerms(Constants.userInfoModel, new Callback<Response>(){

            @Override
            public void success(Response response, Response response2) {
                String rawResponse = ApiConnection.getRawResponse(response);

                try{
                    JSONObject payObj = null;
                    payObj = new JSONObject(rawResponse);
                    String jStatus    = payObj.optString("message");
                    JSONArray jResult = payObj.optJSONArray("result");

                    if(jStatus.equals("success")){
                        if(jResult != null){
                            for(int i = 0; i < jResult.length(); i++){
                                JSONObject jsonObject = jResult.optJSONObject(i);
                                listPaymentTermString.add(jsonObject.optString("name"));
                            }
                        }
                    }

                }catch(JSONException e){
                    e.printStackTrace();
                    progressDialogPaymentTerm.dismiss();
                }

                if(progressDialogPaymentTerm!=null)progressDialogPaymentTerm.dismiss();
            }

            @Override
            public void failure(RetrofitError error) {
                if(progressDialogPaymentTerm!=null)progressDialogPaymentTerm.dismiss();
            }
        });
    }



    private void getDataTaxGroup(){

        progressDialogTaxGroup = new ProgressDialog(this);
        progressDialogTaxGroup.setMessage("Loading..");
        progressDialogTaxGroup.show();

        ApiConnection.getTaxGroupInterface().getDataTaxGroup(Constants.userInfoModel, new Callback<Response>(){

            @Override
            public void success(Response response, Response response2) {
                String rawResponse = ApiConnection.getRawResponse(response);

                try{
                    JSONObject taxGroupObj = null;
                    taxGroupObj       = new JSONObject(rawResponse);
                    String jStatus    = taxGroupObj.optString("message");
                    JSONArray jResult = taxGroupObj.optJSONArray("result");

                    if(jStatus.equals("success")){
                        listTaxGroupString.clear();
                        taxGroupModels.clear();

                        if(jResult != null){
                            for(int i = 0; i < jResult.length(); i++){
                                JSONObject jsonObject = jResult.optJSONObject(i);

                                JSONObject idObj  = (JSONObject) jsonObject.get("_id");
                                String strMongoId = (String)     idObj.get("$id");

                                taxGroupModels.add(new TaxGroupModelNew(
                                        strMongoId,
                                        jsonObject.optString("description")
                                ));

                                listTaxGroupString.add(jsonObject.optString("description"));
                            }
                        }
                    }

                }catch(JSONException e){
                    e.printStackTrace();
                    progressDialogTaxGroup.dismiss();
                }

                if (progressDialogTaxGroup!=null)progressDialogTaxGroup.dismiss();
            }

            @Override
            public void failure(RetrofitError error) {
                if(progressDialogTaxGroup!=null)progressDialogTaxGroup.dismiss();
            }
        });
    }



    private void getItemSalesOrder(final int flak){

        progressDialogSalesQuotation = new ProgressDialog(this);
        progressDialogSalesQuotation.setMessage("Loading..");
        progressDialogSalesQuotation.show();

        ApiConnection.getItemSalesQuotationInterface().getDataItemSalesQuot(Constants.userInfoModel, new Callback<Response>(){

            @Override
            public void success(Response response, Response response2){

                String rawResponse = ApiConnection.getRawResponse(response);
                JSONObject sqObj = null;

                try{
                    sqObj = new JSONObject(rawResponse);
                    String jStatus    = sqObj.optString("message");
                    JSONArray jResult = sqObj.optJSONArray("result");

                    if(jStatus.equals("success")){

                        itemConvertionSalesQuotModels.clear();
                        itemSalesQuotations.clear();
                        listItemString.clear();

                        for(int i = 0; i < jResult.length(); i++){

                            JSONObject jsonObject      = jResult.optJSONObject(i);
                            JSONArray  jsonItemConvert = jsonObject.optJSONArray("item_convertion");

                            if(jsonItemConvert != null){
                                for(int j = 0; j < jsonItemConvert.length(); j++){
                                    JSONObject jsonObjectItemCon = jsonItemConvert.optJSONObject(j);
                                    itemConvertionSalesQuotModels.add(new ItemConvertionSalesQuotModel(
                                            jsonObjectItemCon.optString("uom"),
                                            jsonObjectItemCon.optDouble("uom_convertion"),
                                            jsonObjectItemCon.optDouble("base_uom_convertion"),
                                            jsonObjectItemCon.optDouble("harga_jual"),
                                            jsonObjectItemCon.optString("barcode"),
                                            jsonObject.optString("id")
                                    ));
                                }
                            }

                            itemSalesQuotations.add(i, new ItemSalesQuotation(
                                    jsonObject.optString("id"),
                                    jsonObject.optString("s_description"),
                                    jsonObject.optString("l_description"),
                                    jsonObject.optString("base_uom"),
                                    jsonObject.optString("item_group"),
                                    jsonObject.optString("item_hierarchy"),
                                    jsonObject.optString("account_class"),
                                    jsonObject.optString("create_by"),
                                    jsonObject.optString("create_date"),
                                    jsonObject.optString("create_time"),
                                    jsonObject.optString("tax_group")
                            ));

                            listItemString.add(jsonObject.optString("id"));
                        }

                        if(flak == 1){
                            spinnerItem();
                        }else if(flak == 2){
                            indexSpinnerItem = listItemString.indexOf(detailItemName);
                            spinnerItemDetail();
                        }

                    }

                }catch(JSONException e){
                    e.printStackTrace();
                    progressDialogSalesQuotation.dismiss();
                }

                if (progressDialogSalesQuotation!=null)progressDialogSalesQuotation.dismiss();

            }

            @Override
            public void failure(RetrofitError error) {
                if (progressDialogSalesQuotation!=null)progressDialogSalesQuotation.dismiss();
            }
        });
    }


    @Override
    public void callDetailItem(DetailSalesQuotationModel detailSalesQuotationModel, int position) {
        showDailogDetailItem(detailSalesQuotationModel, position);
    }


    private void showDailogDetailItem(DetailSalesQuotationModel detailSalesQuotationModel, int position){
        dialogDetailItem = new Dialog(this); positionDetailItem = position;
        View viewsDetailItemSo = inflater.inflate(R.layout.popup_sales_order, null);
        btnCancelItemUpdate    = (Button)   viewsDetailItemSo.findViewById(R.id.btnCancelItem);
        btnSaveItemUpdate      = (Button)   viewsDetailItemSo.findViewById(R.id.btnSaveItem);
        spinnerItem            = (Spinner)  viewsDetailItemSo.findViewById(R.id.spinnerItem);
        editQuantity           = (EditText) viewsDetailItemSo.findViewById(R.id.editQuantity);
        spinnerUom             = (Spinner)  viewsDetailItemSo.findViewById(R.id.spinnerUom);
        editPrice              = (EditText) viewsDetailItemSo.findViewById(R.id.editPrice);
        editTaxItem            = (EditText) viewsDetailItemSo.findViewById(R.id.editTaxItem);
        editDiscount           = (EditText) viewsDetailItemSo.findViewById(R.id.editDiscount);
        editTotal              = (EditText) viewsDetailItemSo.findViewById(R.id.editTotal);

        dialogDetailItem.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogDetailItem.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#50000000")));
        dialogDetailItem.setContentView(viewsDetailItemSo);
        dialogDetailItem.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialogDetailItem.show();
        detailItemName = detailSalesQuotationModel.getDescription();
        setDataDetailItem(detailSalesQuotationModel);
        getItemSalesOrder(2);

        editQtyOnChange();
        editDiscountOnChange();
    }


    private void setDataDetailItem(DetailSalesQuotationModel dataDetailItem){
        editQuantity.setText(String.valueOf(dataDetailItem.getQty()));
        //editPrice.setText(String.valueOf(dataDetailItem.getPrice()));
        editDiscount.setText(String.valueOf(dataDetailItem.getDiscount()));
        editTotal.setText(String.valueOf(dataDetailItem.getTotal()));
    }



}
