package com.trimitrasis.finosapps.Views.PosView;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import com.trimitrasis.finosapps.Connection.ApiConnection;
import com.trimitrasis.finosapps.R;
import com.trimitrasis.finosapps.Views.ContentProvider.DetailHoldSalesProvider;
import com.trimitrasis.finosapps.Views.ContentProvider.EndOfShiftProvider;
import com.trimitrasis.finosapps.Views.ContentProvider.HeaderHoldSalesProvider;
import com.trimitrasis.finosapps.Views.ContentProvider.TaxGroupProvider;
import com.trimitrasis.finosapps.Views.ContentProvider.TaxProvider;
import com.trimitrasis.finosapps.Views.ContentProvider.TransJualBayarProvider;
import com.trimitrasis.finosapps.Views.ContentProvider.TransJualDetailProvider;
import com.trimitrasis.finosapps.Views.ContentProvider.TransJualProvider;
import com.trimitrasis.finosapps.Views.PosView.Model.MemberPointModel;
import com.trimitrasis.finosapps.Views.PosView.Model.PpnModel;
import com.trimitrasis.finosapps.Views.PosView.Model.RingkasanOrderModel;
import com.trimitrasis.finosapps.Views.PrintView.PrintPointOfSalesActivity_;
import com.trimitrasis.finosapps.Views.SplitBillView.SplitBillMenuActivity_;
import com.trimitrasis.finosapps.Views.UtilView.Constants;
import com.trimitrasis.finosapps.Views.UtilView.FontUtils;
import com.trimitrasis.finosapps.Views.UtilView.Utils;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by rahman on 02/04/2017.
 */


@EActivity(R.layout.layout_order_bayar)
public class BayarActivity extends AppCompatActivity{

    @ViewById
    Toolbar toolbar;

    @Extra
    double totalBayar;

    @Extra
    double subTotalBayar;

    @Extra
    String info;

    @Extra
    double tax;

    @Extra
    double discount;

    @Extra
    String idPenjualan;

    @Extra
    String infoBayar;

    @Extra
    String jenisPajak;

    String id_jualan   = "";
    String info_bayar  = "";
    String jenis_pajak = "";

    @Extra
    ArrayList<RingkasanOrderModel> ringkasanOrderModels;

    @ViewById
    Button btnBayar;

    @ViewById
    TextView textTotalBayar, textDataMember, textDataPoint, textNominalPoint, labelTotalBayar;

    @ViewById
    EditText textNominalTunai, textNominalKembalian, textNominalNonTunai, textNominalDonasi, textNomerKartu, textInputNominalPoint;

    @ViewById
    Spinner spinnerBank, spinnerMetodeBayar, spinnerPpn;

    @ViewById
    Button btnCekMember;

    @ViewById
    EditText textMemberId;

    ArrayList<String> listBank = new ArrayList<>();
    ArrayList<String> listPpn = new ArrayList<>();
    ArrayList<String> listMetodeBayar = new ArrayList<>();
    ArrayList<PpnModel> ppnModels;

    String bankNameString, metodeBayarString, ppnNameString, id_user_member = "";
    double nominalNonTunai = 0, nominalTunai = 0, nominalPoint = 0, totalNominalKembalian = 0;
    double nominalDonasi = 0, jumlahUang = 0;
    double totDebit =0, totKredit =0, totCash =0, totKembalian=0, totalBayarTemp =0, taxTemp =0;
    double totPoint = 0; double pointUse = 0; double nomPoint = 0; double pointGet = 0;
    double nominalPointBayar = 0;  int indexSpinnerPajak = 0;
    ProgressDialog progressDialog;
    String infoSplitBill = "";


    @AfterViews
    void afterView(){

        progressDialog = new ProgressDialog(this);
        ppnModels      = new ArrayList<>();
        totalBayarTemp = totalBayar;
        taxTemp        = tax;
        infoSplitBill  = info;

        customView();
        setValue();
        spinnerInitDebit();
        spinnerInitMetodeBayar();
        initTabbar();

        nominalNonTunai = 0;
        nominalTunai = 0;
        totalNominalKembalian = 0;
        nominalDonasi = 0;
        jumlahUang = 0;

        nominalNonTunaiOnChange();
        nominalTunaiOnChange();
        nominalDonasiOnChange();
        nominalPointOnChange();

        id_jualan   = idPenjualan;
        info_bayar  = infoBayar;
        jenis_pajak = jenisPajak;

        getDataTax();
        textInputNominalPoint.setEnabled(false);
        setTypeFont();
    }


    private void initTabbar(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_arrow_left);
    }

    private void setTypeFont(){
        labelTotalBayar.setTypeface(FontUtils.getHelvetica_Neue_LT(this), Typeface.BOLD);
        textTotalBayar.setTypeface(FontUtils.getHelvetica_Neue_LT(this), Typeface.BOLD);
        textDataMember.setTypeface(FontUtils.getHelvetica_Neue_LT(this));
        textDataPoint.setTypeface(FontUtils.getHelvetica_Neue_LT(this));
        textNominalPoint.setTypeface(FontUtils.getHelvetica_Neue_LT(this));
        textNominalTunai.setTypeface(FontUtils.getHelvetica_Neue_LT(this));
        textNominalKembalian.setTypeface(FontUtils.getHelvetica_Neue_LT(this));
        textNominalNonTunai.setTypeface(FontUtils.getHelvetica_Neue_LT(this));
        textNominalDonasi.setTypeface(FontUtils.getHelvetica_Neue_LT(this));
        textNomerKartu.setTypeface(FontUtils.getHelvetica_Neue_LT(this));
        textInputNominalPoint.setTypeface(FontUtils.getHelvetica_Neue_LT(this));
        textMemberId.setTypeface(FontUtils.getHelvetica_Neue_LT(this));
    }


    @OptionsItem(android.R.id.home)
    void homeClick(){
        onBackPressed();
    }

    private void setValue(){
        textTotalBayar.setText(Utils.getCurrencyRupiah(totalBayar));
    }

    private void customView(){
        TextView toolbarView = (TextView) toolbar.findViewById(R.id.headerText);

        toolbarView.setText("Bayar");
        listBank.add("BNI");
        listBank.add("BCA");
        listBank.add("BRI");
        listBank.add("MANDIRI");

        listMetodeBayar.add("Debit");
        listMetodeBayar.add("Kredit");
        textNominalKembalian.setEnabled(false);

        ImageView iconSplitView = (ImageView) toolbar.findViewById(R.id.iconSplit);
        int orientation=this.getResources().getConfiguration().orientation;

        if(infoSplitBill != null){
            if(orientation == Configuration.ORIENTATION_LANDSCAPE){
                iconSplitView.setVisibility(View.GONE);
            }
        }else{
            if(orientation == Configuration.ORIENTATION_LANDSCAPE){
                iconSplitView.setVisibility(View.VISIBLE);
            }
        }


        if(iconSplitView != null){
            iconSplitView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    SplitBillMenuActivity_.intent(BayarActivity.this)
                            .infoBayar(info_bayar)
                            .jenisPajak(jenis_pajak)
                            .idJual(id_jualan)
                            .getRingkasanOrderModels(ringkasanOrderModels).start();
                            finish();
                }
            });
        }


    }


    private void spinnerPpn(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, listPpn){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                TextView view = (TextView) super.getView(position, convertView, parent);
                view.setTypeface(FontUtils.getFontHeaderToolbar(BayarActivity.this));
                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent){
                TextView view = (TextView)super.getDropDownView(position, convertView, parent);
                view.setTypeface(FontUtils.getFontHeaderToolbar(BayarActivity.this));
                return view;
            }
        };


        spinnerPpn.setAdapter(adapter);
        if(jenis_pajak != null){
            spinnerPpn.setSelection(indexSpinnerPajak);
        }

        spinnerPpn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
                ppnNameString = listPpn.get(position);
                getIdTaxGroup(ppnNameString);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent){

            }
        });
    }


    private void spinnerInitDebit(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, listBank){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                TextView view = (TextView) super.getView(position, convertView, parent);
                view.setTypeface(FontUtils.getFontHeaderToolbar(BayarActivity.this));
                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent){
                TextView view = (TextView)super.getDropDownView(position, convertView, parent);
                view.setTypeface(FontUtils.getFontHeaderToolbar(BayarActivity.this));
                return view;
            }

        };

        spinnerBank.setAdapter(adapter);
        spinnerBank.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
                bankNameString = listBank.get(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent){

            }
        });
    }


    private void spinnerInitMetodeBayar(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, listMetodeBayar){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                TextView view = (TextView) super.getView(position, convertView, parent);
                view.setTypeface(FontUtils.getFontHeaderToolbar(BayarActivity.this));
                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent){
                TextView view = (TextView)super.getDropDownView(position, convertView, parent);
                view.setTypeface(FontUtils.getFontHeaderToolbar(BayarActivity.this));
                return view;
            }
        };

        spinnerMetodeBayar.setAdapter(adapter);
        spinnerMetodeBayar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
                metodeBayarString = listMetodeBayar.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent){

            }

        });
    }


    public void nominalDonasiOnChange(){
        textNominalDonasi.addTextChangedListener(new TextWatcher(){

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after){
            }

            @Override
            public void onTextChanged(CharSequence query, int start, int before, int count){
            }

            @Override
            public void afterTextChanged(Editable s){
                textNominalDonasi.removeTextChangedListener(this);
                try{
                    if(s.toString().equals("")){
                        nominalDonasi = 0;
                    }else{
                        setDisplayFormatNumeric(s.toString(), textNominalDonasi);
                        nominalDonasi = Double.parseDouble(setNumericToText(textNominalDonasi.getText().toString()));
                    }
                    totalNominalKembalian = (nominalNonTunai + nominalTunai + nominalPoint) - (nominalDonasi + totalBayar);
                    textNominalKembalian.setText(""+ Utils.getCurrencyRupiahTanpaSimbol(totalNominalKembalian));

                }catch(NumberFormatException nfe){
                    nfe.printStackTrace();
                }

                textNominalDonasi.addTextChangedListener(this);
            }

        });
    }


    public void nominalTunaiOnChange(){
        textNominalTunai.addTextChangedListener(new TextWatcher(){

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after){

            }

            @Override
            public void onTextChanged(CharSequence query, int start, int before, int count){

            }

            @Override
            public void afterTextChanged(Editable s){
                textNominalTunai.removeTextChangedListener(this);
                try{
                    if(s.toString().equals("")){
                        nominalTunai = 0;
                    }else{
                        setDisplayFormatNumeric(s.toString(), textNominalTunai);
                        nominalTunai = Double.parseDouble(setNumericToText(textNominalTunai.getText().toString()));
                    }
                    totalNominalKembalian = (nominalNonTunai + nominalTunai + nominalPoint) - (nominalDonasi + totalBayar);
                    textNominalKembalian.setText(""+ Utils.getCurrencyRupiahTanpaSimbol(totalNominalKembalian));

                }catch(NumberFormatException nfe){
                    nfe.printStackTrace();
                }

                textNominalTunai.addTextChangedListener(this);
            }

        });
    }



    public void nominalNonTunaiOnChange(){
        textNominalNonTunai.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after){
            }

            @Override
            public void onTextChanged(CharSequence query, int start, int before, int count){

            }

            @Override
            public void afterTextChanged(Editable s){
                textNominalNonTunai.removeTextChangedListener(this);
                try{
                    if(s.toString().equals("")){
                        nominalNonTunai = 0;
                    }else{
                        setDisplayFormatNumeric(s.toString(), textNominalNonTunai);
                        nominalNonTunai = Double.parseDouble(setNumericToText(textNominalNonTunai.getText().toString()));
                    }
                    totalNominalKembalian = (nominalNonTunai + nominalTunai + nominalPoint) - (nominalDonasi + totalBayar);
                    textNominalKembalian.setText(""+ Utils.getCurrencyRupiahTanpaSimbol(totalNominalKembalian));

                }catch(NumberFormatException nfe){
                    nfe.printStackTrace();
                }

                textNominalNonTunai.addTextChangedListener(this);
            }

        });
    }


    public void nominalPointOnChange(){
        textInputNominalPoint.addTextChangedListener(new TextWatcher(){

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after){

            }

            @Override
            public void onTextChanged(CharSequence query, int start, int before, int count){

            }


            @Override
            public void afterTextChanged(Editable s){
                textInputNominalPoint.removeTextChangedListener(this);
                try{
                    if(s.toString().equals("")){
                        nominalPoint = 0;
                    }else{

                        setDisplayFormatNumeric(s.toString(), textInputNominalPoint);
                        nominalPoint = Double.parseDouble(setNumericToText(textInputNominalPoint.getText().toString()));

                        if(nominalPoint > totalBayar){
                            Utils.showToast("Nominal Point tidak boleh melebihi total bayar !", BayarActivity.this);
                        }
                    }

                    totalNominalKembalian = (nominalNonTunai + nominalTunai + nominalPoint) - (nominalDonasi + totalBayar);
                    textNominalKembalian.setText(""+ Utils.getCurrencyRupiahTanpaSimbol(totalNominalKembalian));

                }catch(NumberFormatException nfe){
                    nfe.printStackTrace();
                }
                textInputNominalPoint.addTextChangedListener(this);
            }

        });

    }



    @Click
    void btnCekMember(){

        if(isValidInput()){
            progressDialog.setMessage("Loading..");
            progressDialog.show();
            getDataMember();
        }
    }


    private void getDataMember(){
        ApiConnection.getMemberPointInterface().getMemberPoint(getMemberPointModel(), new Callback<Response>(){
            @Override
            public void success(Response response, Response response2){
                String rawResponse = ApiConnection.getRawResponse(response);
                JSONObject loginObj = null;
                double point = 0;
                Date dateNow = null;
                String start_date; String end_date;
                totPoint = 0; pointUse = 0; nomPoint = 0; pointGet = 0;

                try{

                    loginObj = new JSONObject(rawResponse);
                    String jStatus    = loginObj.optString("message");
                    String jmember    = loginObj.optString("member");
                    JSONArray jResult = loginObj.optJSONArray("result");
                    JSONArray jData   = loginObj.optJSONArray("data_point");

                    if(jStatus.equals("success")){

                         if(jResult != null){
                            for (int i = 0; i < jResult.length(); i++){
                                JSONObject jsonObject = jResult.optJSONObject(i);
                                point          = jsonObject.optDouble("point");
                                totPoint       = totPoint + point;
                            }
                        }

                        id_user_member = jmember;

                        if(jData != null){
                            for(int j = 0; j < jData.length(); j++){

                                JSONObject jsonObject = jData.optJSONObject(j);
                                start_date = jsonObject.optString("start_date");
                                end_date   = jsonObject.optString("end_date");

                                try{
                                    dateNow = new Date();
                                    if(dateNow.after(Utils.getDateRange(start_date, -1)) && dateNow.before(Utils.getDateRange(end_date, 1))){
                                        pointUse = jsonObject.optDouble("point_use");
                                        pointGet = jsonObject.optDouble("point_get");
                                        textDataPoint.setText("1 Point = " + Utils.getCurrencyRupiah(pointUse));
                                    }
                                }catch(Exception e){
                                    e.printStackTrace();
                                }
                            }
                        }

                        showDialogMessage("Anda terdaftar sebagai member!");

                    }else if(jStatus.equals("member not found")){
                        showDialogMessage("Member tidak terdaftar!");
                    }else if(jStatus.equals("master point empty")){
                        showDialogMessage("Tidak Ada data point!");
                    }else if(jStatus.equals("poin empty")){
                        showDialogMessage("Point User kosong!");
                    }

                    if (progressDialog!=null)progressDialog.dismiss();

                }catch(JSONException e){
                    e.printStackTrace();
                    if(progressDialog!=null)progressDialog.dismiss();
                }


                if(totPoint != 0){
                    textInputNominalPoint.setEnabled(true);
                }

                int totPointInt = (int) totPoint;
                textDataMember.setText("Point Anda : " + totPointInt);
                nomPoint = totPoint*pointUse;
                textNominalPoint.setText("Point yang dapat dipakai : " + Utils.getCurrencyRupiah(nomPoint));
            }

            @Override
            public void failure(RetrofitError error){
                if (progressDialog!=null)progressDialog.dismiss();
            }
        });
    }



    private boolean isValidInput(){
        if (textMemberId.getText().toString().isEmpty() || textMemberId.getText() == null || textMemberId.getText().toString().equals("")){
            Utils.showToast("Data Member ID kosong!", this);
            return false;
        }else return true;
    }



    @Click
    void btnBayar(){

        if((nominalNonTunai + nominalTunai + nominalPoint) >= (totalBayar + nominalDonasi)){
            if(nominalNonTunai <= totalBayar && nominalPoint <= totalBayar){
                if(!textInputNominalPoint.getText().toString().equals("") && !id_user_member.toString().equals("")){ //with point

                    String nominalPointString = setNumericToText(textInputNominalPoint.getText().toString());
                    double nominPoin          = Double.parseDouble(nominalPointString);
                    double pointTerpotong     = Math.ceil(nominPoin / pointUse);

                    if(pointTerpotong <= totPoint && pointTerpotong >= 1){
                        insertDataPenjualan("point");
                        insertDataMemberPoint(id_user_member, id_jualan, -(pointTerpotong));
                    }else{
                        Utils.showToast("Point Anda tidak mencukupi !", this);
                    }

                }else{
                    insertDataPenjualan("");
                }

            }
        }else{
            Utils.showToast("Saldo Anda tidak mencukupi!", this);
        }
    }



    private MemberPointModel getDataMemberPoint(String memberId, String idjual, double point){

         MemberPointModel  memberPointModel = new MemberPointModel(
                    memberId,
                    Constants.userInfoModel,
                    idjual,
                    point,
                    Constants.userInfoModel.getEmail()
            );
        return memberPointModel;
    }


    private void insertDataMemberPoint(String memberId, String idjual, double point){
        ApiConnection.getInsertMemberPointInterface().insertDataMemberPoint(getDataMemberPoint(memberId, idjual, point), new Callback<Response>() {
            @Override
            public void success(Response response, Response response2){

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }


    private void tambahPointUser(String memberId, String idjual, double point){
        ApiConnection.getUpdateMemberPointInterface().updateDataMemberPoint(getDataMemberPoint(memberId, idjual, point), new Callback<Response>() {
            @Override
            public void success(Response response, Response response2){

            }

            @Override
            public void failure(RetrofitError error){

            }
        });
    }


    private void insertDataPenjualan(String info){

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();

        String idJualan = Utils.getIdJual(this);
        ContentValues valuesHeader  = new ContentValues();
        if(infoSplitBill != null){
            if (infoSplitBill.equals("split")){
                valuesHeader.put(TransJualProvider.KEY_ID_JUAL, idJualan);
            }
        }else{
            valuesHeader.put(TransJualProvider.KEY_ID_JUAL, id_jualan);
        }

        valuesHeader.put(TransJualProvider.KEY_ID_KASIR, Constants.userInfoModel.getEmail());
        valuesHeader.put(TransJualProvider.KEY_TANGGAL, dateFormat.format(date));
        valuesHeader.put(TransJualProvider.KEY_TIME, timeFormat.format(date));
        valuesHeader.put(TransJualProvider.KEY_SUBTOTAL, subTotalBayar);
        valuesHeader.put(TransJualProvider.KEY_TAX, tax);
        valuesHeader.put(TransJualProvider.KEY_TOTAL_DISCOUNT, discount);
        valuesHeader.put(TransJualProvider.KEY_TOTAL_BAYAR, totalBayar);
        valuesHeader.put(TransJualProvider.KEY_KEMBALIAN, totalNominalKembalian);
        Uri uriHeader = getContentResolver().insert(TransJualProvider.CONTENT_URI, valuesHeader);

        if(ringkasanOrderModels.size() > 0){
                ContentValues valuesDetail  = new ContentValues();
                for(int i = 0; i < ringkasanOrderModels.size(); i++){

                    valuesDetail.put(TransJualDetailProvider.KEY_DETAIL_ID, ringkasanOrderModels.get(i).getDetailId());
                    if(infoSplitBill != null){
                        if (infoSplitBill.equals("split")){
                            valuesDetail.put(TransJualDetailProvider.KEY_ID_JUAL_DETAIL, idJualan);
                        }
                    }else{
                        valuesDetail.put(TransJualDetailProvider.KEY_ID_JUAL_DETAIL, id_jualan);
                    }

                    valuesDetail.put(TransJualDetailProvider.KEY_ITEM_CODE, ringkasanOrderModels.get(i).getKodeBarang());
                    valuesDetail.put(TransJualDetailProvider.KEY_DESCRIPTION, ringkasanOrderModels.get(i).getNamaBarang());
                    valuesDetail.put(TransJualDetailProvider.KEY_BARCODE, ringkasanOrderModels.get(i).getKodeBarcode());
                    valuesDetail.put(TransJualDetailProvider.KEY_QTY, ringkasanOrderModels.get(i).getQty());
                    valuesDetail.put(TransJualDetailProvider.KEY_HARGA, ringkasanOrderModels.get(i).getHargaJual());
                    valuesDetail.put(TransJualDetailProvider.KEY_HARGA_MEMBER, 0);
                    valuesDetail.put(TransJualDetailProvider.KEY_TOTAL, ringkasanOrderModels.get(i).getQty() * ringkasanOrderModels.get(i).getHargaJual());
                    valuesDetail.put(TransJualDetailProvider.KEY_SATUAN, ringkasanOrderModels.get(i).getSatuanBarang());
                    valuesDetail.put(TransJualDetailProvider.KEY_PROMO, "");
                    valuesDetail.put(TransJualDetailProvider.KEY_DISCOUNT, ringkasanOrderModels.get(i).getDiskon());
                    valuesDetail.put(TransJualDetailProvider.KEY_DISC_AMOUNT, "");
                    valuesDetail.put(TransJualDetailProvider.KEY_DISC_PERCENT, "");
                    valuesDetail.put(TransJualDetailProvider.KEY_TAX_TYPE, "");
                    valuesDetail.put(TransJualDetailProvider.KEY_HARGA_HPP, ringkasanOrderModels.get(i).getStandart_cost());
                    Uri uriDetail = getContentResolver().insert(TransJualDetailProvider.CONTENT_URI, valuesDetail);
                }
        }


        if(!(textNominalNonTunai.getText().toString().equals(""))){
            if(metodeBayarString.equals("Debit")){
                totDebit  = Double.parseDouble(setNumericToText(textNominalNonTunai.getText().toString()));
                insertDataMetodeBayar(id_jualan, bankNameString, bankNameString, textNomerKartu.getText().toString(), metodeBayarString, totDebit, 0.0);
            }else if(metodeBayarString.equals("Kredit")){
                totKredit = Double.parseDouble(setNumericToText(textNominalNonTunai.getText().toString()));
                insertDataMetodeBayar(id_jualan, bankNameString, bankNameString, textNomerKartu.getText().toString(), metodeBayarString, totKredit, 0.0);
            }
        }

        if(!(textNominalKembalian.getText().toString().equals(""))){
            totKembalian = Double.parseDouble(setNumericToText(textNominalKembalian.getText().toString()));
        }else{
            totKembalian = 0;
        }

        if(!(textNominalTunai.getText().toString().equals(""))){
            totCash = Double.parseDouble(setNumericToText(textNominalTunai.getText().toString()));
            insertDataMetodeBayar(id_jualan, "", "", "", "CASH", totCash, totalNominalKembalian);
        }else{
            totCash = 0.0;
        }

        if(info.toString().equals("point")){
            nominalPointBayar = Double.parseDouble(setNumericToText(textInputNominalPoint.getText().toString()));
            insertDataMetodeBayar(id_jualan, "", "", "", "POINT", nominalPointBayar, 0);
        }

        if(!id_user_member.toString().equals("")){
            double pointAdd = totalBayar/pointGet;
            int pAdd = (int) pointAdd;
            if(pAdd >= 1){
                tambahPointUser(id_user_member, id_jualan, Double.parseDouble(String.valueOf(pAdd)));
            }
        }

        if(id_jualan != null){
            if(info_bayar.toString().equals("hold")){
                int emptyHeaderHoldSales = getContentResolver().delete(HeaderHoldSalesProvider.CONTENT_URI, "id_jual " + " = '" + id_jualan + "'", null);
                int emptyDetailHoldSales = getContentResolver().delete(DetailHoldSalesProvider.CONTENT_URI, "id_jual_detail " + " = '" + id_jualan + "'", null);
                Constants.flak_hold = 1;
            }
        }


        if(infoSplitBill != null){
            if(infoSplitBill.equals("split")){
                Constants.flak_bayar_bill = 1;
            }
        }

        Constants.flak_bayar = 1;
        CreateOrderActivity.idJualHold = "";
        addDataEndOfShift(totCash, totalBayar, 0, tax, totKredit,  totDebit, discount, 1, totalNominalKembalian);
        Utils.showToast("anda telah Success melakukan pembayaran", this);
        ringkasanOrderModels.clear();
        finish();
    }


    private void insertDataMetodeBayar(String keyIdJual, String bankId, String bankName, String nomerkartu, String metodBayar, double totalBayar, double kembalian){
        ContentValues valuesBayar = new ContentValues();
        valuesBayar.put(TransJualBayarProvider.KEY_ID_JUAL_BAYAR, keyIdJual);
        valuesBayar.put(TransJualBayarProvider.KEY_BANK_ID, bankId);
        valuesBayar.put(TransJualBayarProvider.KEY_BANK_NAME, bankName);
        valuesBayar.put(TransJualBayarProvider.KEY_NO_KARTU, nomerkartu);
        valuesBayar.put(TransJualBayarProvider.KEY_TIPE, metodBayar);
        valuesBayar.put(TransJualBayarProvider.KEY_TOTAL_BAYAR, totalBayar);
        valuesBayar.put(TransJualBayarProvider.KEY_KEMBALIAN, kembalian);
        Uri uriBayar = getContentResolver().insert(TransJualBayarProvider.CONTENT_URI, valuesBayar);
    }


    private void addDataEndOfShift(double cash, double netSales, double netReturn, double ppn, double credit, double debit, double discount, double jumTrans, double kembalianNya){

        long milis = System.currentTimeMillis();
        String date_ = timeMilisToString(milis, "yyyy-MM-dd");
        ContentValues values  = new ContentValues();
        values.put(EndOfShiftProvider.KEY_CASH, cash);
        values.put(EndOfShiftProvider.KEY_TANGGAL, date_);
        values.put(EndOfShiftProvider.KEY_NET_SALES, netSales);
        values.put(EndOfShiftProvider.KEY_NET_RETURN, netReturn);
        values.put(EndOfShiftProvider.KEY_PPN, ppn);
        values.put(EndOfShiftProvider.KEY_CREDIT, credit);
        values.put(EndOfShiftProvider.KEY_DEBIT, debit);
        values.put(EndOfShiftProvider.KEY_DISCOUNT, discount);
        values.put(EndOfShiftProvider.KEY_JUMLAH_TRANS, jumTrans);
        values.put(EndOfShiftProvider.KEY_USERNAME, Constants.userInfoModel.getEmail());
        Uri uri = getContentResolver().insert(EndOfShiftProvider.CONTENT_URI, values);
        PrintPointOfSalesActivity_.intent(BayarActivity.this).ringkasanOrderModels(ringkasanOrderModels).discount(discount).tax(ppn).totalBayar(netSales).debit(debit).kredit(credit).cash(cash).kembalian(kembalianNya).start();
    }


    public static String timeMilisToString(long milis, String format){
        SimpleDateFormat sd = new SimpleDateFormat(format);
        Calendar calendar   = Calendar.getInstance();
        calendar.setTimeInMillis(milis);
        return sd.format(calendar.getTime());
    }



    private void getDataTax(){

        String taxGroupName; String idTaxGroup;
        ppnModels.clear();
        listPpn.clear();
        listPpn.add("--No Tax--");
        String URL = "content://com.trimitrasis.finosapps.ContentProvider.TaxGroupProvider/taxgroup";
        Uri uri_   = Uri.parse(URL);
        Cursor c   = getContentResolver().query(uri_, null, null , null, "_id");

        if(c.moveToFirst()){
            do{
                idTaxGroup   = c.getString(c.getColumnIndex(TaxGroupProvider.KEY_ID_TAX_GROUP));
                taxGroupName = c.getString(c.getColumnIndex(TaxGroupProvider.KEY_DESCRIPTION));
                listPpn.add(taxGroupName);
                ppnModels.add(new PpnModel(idTaxGroup, taxGroupName));
            }while(c.moveToNext());
            c.close();

        }else{
            System.out.println("data tidak ditemukan!");
        }


        if(jenis_pajak != null){
            indexSpinnerPajak = listPpn.indexOf(jenis_pajak);
        }

        spinnerPpn();

    }



    private void getTotalTax(String taxGroup){

        double totBay = 0; double totTax = 0;
        double rate = 0; double totRate = 0;
        String URL = "content://com.trimitrasis.finosapps.ContentProvider.TaxProvider/tax";
        Uri uri_   = Uri.parse(URL);
        Cursor c   = getContentResolver().query(uri_, null, "id_tax_group " + " = '" + taxGroup + "'" , null, "_id");

        if(c.moveToFirst()){

            do{
                rate = Double.parseDouble(c.getString(c.getColumnIndex(TaxProvider.KEY_RATE)));
                totRate = totRate + rate;

            }while(c.moveToNext());
            c.close();

            totBay = totalBayarTemp + ((totRate/100) * totalBayarTemp);
            totTax = taxTemp + ((totRate/100) * totalBayarTemp);
            textTotalBayar.setText(Utils.getCurrencyRupiah(totBay));
            totalBayar = totBay;
            tax = totTax;
        }
    }




    private void getIdTaxGroup(String descTax){

        String taxGroupId; int cekData = 0;
        for(int i = 0; i < ppnModels.size(); i++){
            if(descTax.equals(ppnModels.get(i).getDescPpn())){
                taxGroupId = ppnModels.get(i).getIdPPn();
                getTotalTax(taxGroupId);
                cekData = 1;
            }
        }

        if(cekData == 0){
            totalBayar = totalBayarTemp;
            tax = taxTemp;
            textTotalBayar.setText(Utils.getCurrencyRupiah(totalBayar));
        }

        totalNominalKembalian = (nominalNonTunai + nominalTunai + nominalPoint) - (nominalDonasi + totalBayar);
        textNominalKembalian.setText(""+ Utils.getCurrencyRupiahTanpaSimbol(totalNominalKembalian));

    }



    private MemberPointModel getMemberPointModel(){
        String idMember = textMemberId.getText().toString();
        MemberPointModel memberPointModel = new MemberPointModel(
                idMember,
                Constants.userInfoModel
        );
        return memberPointModel;
    }



    private String setNumericToText(String stringNumeric){
        String stringText = "";
        stringText = stringNumeric.toString();
        if(stringText.contains(",")){
            stringText = stringText.replaceAll(",", "");
        }
        return stringText;
    }



    private void setDisplayFormatNumeric(String stringText, EditText editText){

        String originalString = stringText.toString();
        Long longval;
        if(originalString.contains(",")){
            originalString = originalString.replaceAll(",", "");
        }
        longval = Long.parseLong(originalString);
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        formatter.applyPattern("#,###,###,###");
        String formattedString = formatter.format(longval);

        editText.setText(formattedString);
        editText.setSelection(editText.getText().length());
    }


    private void showDialogMessage(String message){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Yes",new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog,int id){
                        dialog.dismiss();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


}
