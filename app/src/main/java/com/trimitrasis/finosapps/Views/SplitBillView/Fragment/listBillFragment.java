package com.trimitrasis.finosapps.Views.SplitBillView.Fragment;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.TextView;
import com.trimitrasis.finosapps.ActivityResultBill;
import com.trimitrasis.finosapps.R;
import com.trimitrasis.finosapps.Views.ContentProvider.ItemProvider;
import com.trimitrasis.finosapps.Views.ContentProvider.PromosiBarangProvider;
import com.trimitrasis.finosapps.Views.ContentProvider.PromosiHadiahProvider;
import com.trimitrasis.finosapps.Views.ContentProvider.PromosiProvider;
import com.trimitrasis.finosapps.Views.PosView.BayarActivity_;
import com.trimitrasis.finosapps.Views.PosView.Model.ListCekPromo;
import com.trimitrasis.finosapps.Views.PosView.Model.ListPromoBarangQty;
import com.trimitrasis.finosapps.Views.PosView.Model.RingkasanOrderModel;
import com.trimitrasis.finosapps.Views.SplitBillView.Adapter.ListBillAdapter;
import com.trimitrasis.finosapps.Views.SplitBillView.SplitBillMenuActivity;
import com.trimitrasis.finosapps.Views.UtilView.Constants;
import com.trimitrasis.finosapps.Views.UtilView.UtilPos;
import com.trimitrasis.finosapps.Views.UtilView.Utils;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import de.greenrobot.event.EventBus;

/**
 * Created by rahman on 9/26/2017.
 */

@EFragment(R.layout.fragment_column_split_bill)
public class listBillFragment extends Fragment{

    @ViewById
    RecyclerView listBill;

    RecyclerView.LayoutManager mLayoutManager;

    ArrayList<RingkasanOrderModel> ringkasanOrderModels;

    @ViewById
    Button btnCancelSales, btnBayar;

    @ViewById
    TextView textTotalBayar, textSubTotal, textPpnTab, textDiscountItem;

    public static final int SPLIT_BILL_FINISH = 30;

    List<String> listRingkasan = new ArrayList<>();
    List<String> listPromo = new ArrayList<>();
    List<ListPromoBarangQty> listPromoBarangQtyList = new ArrayList<>();

    List<ListCekPromo> listCekPromos = new ArrayList<>();
    ArrayList<String> listIndexPromo = new ArrayList<String>();
    int min = 1000;
    int idxArr = 0; int qtyListPromo = 0;
    double qtyHadiahBef = 0;

    CallbackCheckListPesanan checkListPesanan;

    @OnActivityResult(SPLIT_BILL_FINISH)
    void onFinishSplitBill(){
        if(Constants.flak_bayar_bill == 1){
            ringkasanOrderModels = new ArrayList<RingkasanOrderModel>();
            ringkasanOrderModels.clear();
            initList();
            setTextLabelNull();
            Constants.flak_bayar_bill = 0;
            checkDataListPesanan();
        }
    }


    @AfterViews
    void afterView(){
        EventBus.getDefault().register(this);
        ringkasanOrderModels = new ArrayList<>();
        ringkasanOrderModels.clear();
        initSetTableView();
        initList();
        checkListPesanan = (CallbackCheckListPesanan) getActivity();
    }



    @Click
    void btnCancelSales(){
        if(ringkasanOrderModels.size() > 0){
            showDialogCancelSales();
        }else{
            Utils.showToast("Silahkah lakukan split bill terlebih dahulu!", getActivity());
        }
    }


    @Click
    void btnBayar(){
        if(ringkasanOrderModels.size() > 0){
            BayarActivity_.intent(this)
                    .subTotalBayar(UtilPos.getTotalBayar(ringkasanOrderModels) - UtilPos.getTotalPajak(ringkasanOrderModels))
                    .totalBayar(UtilPos.getTotalBayar(ringkasanOrderModels))
                    .discount(UtilPos.getTotalDiscount(ringkasanOrderModels))
                    .ringkasanOrderModels(ringkasanOrderModels)
                    .jenisPajak(SplitBillMenuActivity.jenisPajak_)
                    .idPenjualan(SplitBillMenuActivity.idPenjualan_)
                    .infoBayar(SplitBillMenuActivity.infoPembayaran_)
                    .info("split")
                    .startForResult(SPLIT_BILL_FINISH);
        }else{
            Utils.showToast("Silahkah lakukan split bill terlebih dahulu!", getActivity());
        }

    }


    private void initSetTableView(){
        listBill.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        listBill.setLayoutManager(mLayoutManager);
    }


    private void initList(){
        ListBillAdapter adapter = new ListBillAdapter(getActivity(), ringkasanOrderModels);
        listBill.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


    public void onEvent(ActivityResultBill event){
        insertDataOrder(event.getRingkasanOrderModel());
    }


    private void insertDataOrder(RingkasanOrderModel ringkasanOrder){

        boolean k = true;
        double totQty = 0;
        for(int i = 0; i < ringkasanOrderModels.size(); i ++){

            if(ringkasanOrderModels.get(i).getKodeBarcode().equals(ringkasanOrder.getKodeBarcode())){
                if(ringkasanOrder.getNote().equals("update pesanan")){
                    totQty = ringkasanOrderModels.get(i).getQty() + ringkasanOrder.getQty();
                    ringkasanOrderModels.set(i, new RingkasanOrderModel(
                            ringkasanOrder.getItemId(),
                            ringkasanOrder.getKodeBarcode(),
                            ringkasanOrder.getKodeBarang(),
                            ringkasanOrder.getNamaBarang(),
                            ringkasanOrder.getSatuanBarang(),
                            ringkasanOrder.getHargaJual(),
                            totQty,
                            ringkasanOrderModels.get(i).getDiskon(),
                            ringkasanOrder.getTax() * totQty,
                            ringkasanOrderModels.get(i).getInfo(),
                            ringkasanOrder.getStandart_cost(),
                            ringkasanOrder.isFlag_qty(),
                            ringkasanOrder.isFlag_bom(),
                            ringkasanOrder.getTaxGroup(),
                            ringkasanOrder.getNote(),
                            ringkasanOrder.getDetailId()
                    ));

                }else if(ringkasanOrder.getNote().equals("update bill")){
                    if(ringkasanOrderModels.get(i).getQty() > 1.0){
                        totQty = ringkasanOrderModels.get(i).getQty() - ringkasanOrder.getQty();
                        ringkasanOrderModels.set(i, new RingkasanOrderModel(
                                ringkasanOrder.getItemId(),
                                ringkasanOrder.getKodeBarcode(),
                                ringkasanOrder.getKodeBarang(),
                                ringkasanOrder.getNamaBarang(),
                                ringkasanOrder.getSatuanBarang(),
                                ringkasanOrder.getHargaJual(),
                                totQty, 0,
                                ringkasanOrder.getTax() * totQty,
                                "",
                                ringkasanOrder.getStandart_cost(),
                                ringkasanOrder.isFlag_qty(),
                                ringkasanOrder.isFlag_bom(),
                                ringkasanOrder.getTaxGroup(),
                                ringkasanOrder.getNote(),
                                ringkasanOrder.getDetailId()
                        ));
                    }else{
                       ringkasanOrderModels.remove(i);
                    }

                }

                k = false;
                break;
            }
        }

        if(k == true){

            ringkasanOrderModels.add(new RingkasanOrderModel(
                    ringkasanOrder.getItemId(),
                    ringkasanOrder.getKodeBarcode(),
                    ringkasanOrder.getKodeBarang(),
                    ringkasanOrder.getNamaBarang(),
                    ringkasanOrder.getSatuanBarang(),
                    ringkasanOrder.getHargaJual(),
                    ringkasanOrder.getQty(),
                    ringkasanOrder.getDiskon(),
                    ringkasanOrder.getTax(),
                    "",
                    ringkasanOrder.getStandart_cost(),
                    ringkasanOrder.isFlag_qty(),
                    ringkasanOrder.isFlag_bom(),
                    ringkasanOrder.getTaxGroup(),
                    ringkasanOrder.getNote(),
                    ringkasanOrder.getDetailId()
            ));
        }

        cekPromoDiskon_test(ringkasanOrderModels);
        initList();
        textTotalBayar.setText(Utils.getCurrencyRupiahTanpaSimbol(UtilPos.getTotalBayar(ringkasanOrderModels)));
        textSubTotal.setText(Utils.getCurrencyRupiahTanpaSimbol(UtilPos.getTotalBayar(ringkasanOrderModels) - UtilPos.getTotalPajak(ringkasanOrderModels)));
        textPpnTab.setText(Utils.getCurrencyRupiahTanpaSimbol(UtilPos.getTotalPajak(ringkasanOrderModels)));
        textDiscountItem.setText(Utils.getCurrencyRupiahTanpaSimbol(UtilPos.getTotalDiscount(ringkasanOrderModels)));
    }


    private void setTextLabelNull(){
        textTotalBayar.setText("0");
        textSubTotal.setText("0");
        textDiscountItem.setText("0");
        textPpnTab.setText("0");
    }


    private void showDialogCancelSales(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setMessage("Apakah anda yakin cancel split bill?").setCancelable(false).setPositiveButton("Ya",new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog,int id) {
                ringkasanOrderModels = new ArrayList<RingkasanOrderModel>();
                ringkasanOrderModels.clear();
                initList();
                setTextLabelNull();
                SplitBillMenuActivity.jenisPajak_ = "";
                SplitBillMenuActivity.idPenjualan_ = "";
                SplitBillMenuActivity.infoPembayaran_ = "";
                dialog.dismiss();
            }
        })

        .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int id){
                   dialog.cancel();
                }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    private void cekPromoDiskon_test(ArrayList<RingkasanOrderModel> arrRingkasanOrderModels){
        String kodePromo = "";
        for(int i = 0; i < arrRingkasanOrderModels.size(); i++){
            String URL = "content://com.trimitrasis.finosapps.Views.ContentProvider.PromoJualProvider/promojual";
            kodePromo = UtilPos.getKodePromoFromBarcode("barcode_barang", URL, getActivity(), arrRingkasanOrderModels.get(i).getKodeBarcode());
            if(!kodePromo.toString().equals("")){
               cekPromo(kodePromo, arrRingkasanOrderModels);
            }else{
                cekDataPromoHadiah(arrRingkasanOrderModels.get(i), i, arrRingkasanOrderModels);
            }
        }
    }



    private void cekPromo(String kdPromo, ArrayList<RingkasanOrderModel> ringkasanOrderModels){

        String kode_promo = ""; String jenis_promo = ""; String from_ = ""; String to_ = ""; String barcode = "";
        double qty = 0.0; double discount_amount  = 0.0; double discount_percent = 0.0; String barcode_hadiah = "";
        double qtyBarang = 0; int qtyPromo = 0; int p = 0; double qty_hadiah = 0.0;  double totalDiskonPromo = 0;
        double totDiscPercent = 0; double totalDiscount = 0; double amount_hadiah = 0; double totDiscAmount = 0;
        int totCount = 0; int totItem = 0;

        String URLP = "content://com.trimitrasis.finosapps.Views.ContentProvider.PromoJualProvider/promojual";
        totCount = UtilPos.getJumlahDataFromKodePromo("kode_promo_barang", URLP, getActivity(), kdPromo);

        String URL = "content://com.trimitrasis.finosapps.Views.ContentProvider.PromoJualProvider/promojual";
        Uri uri_  = Uri.parse(URL);
        Cursor c_promo = getActivity().getContentResolver().query(uri_, null, "kode_promo_barang " + " = '" +  kdPromo + "'", null, "_id ASC");
        listPromo.clear(); listRingkasan.clear(); listPromoBarangQtyList.clear();

        if(c_promo.moveToFirst()){
            do{
                kode_promo       = (!c_promo.getString(c_promo.getColumnIndex(PromosiProvider.KEY_KODE_PROMO)).equals("") ? c_promo.getString(c_promo.getColumnIndex(PromosiProvider.KEY_KODE_PROMO)) : "");
                jenis_promo      = (!c_promo.getString(c_promo.getColumnIndex(PromosiProvider.KEY_JENIS_PROMO)).equals("") ? c_promo.getString(c_promo.getColumnIndex(PromosiProvider.KEY_JENIS_PROMO)) : "");
                from_            = (!c_promo.getString(c_promo.getColumnIndex(PromosiProvider.KEY_FROM)).equals("") ? c_promo.getString(c_promo.getColumnIndex(PromosiProvider.KEY_FROM)) : "");
                to_              = (!c_promo.getString(c_promo.getColumnIndex(PromosiProvider.KEY_TO)).equals("") ? c_promo.getString(c_promo.getColumnIndex(PromosiProvider.KEY_TO)) : "");
                barcode          = (!c_promo.getString(c_promo.getColumnIndex(PromosiBarangProvider.KEY_BARCODE)).equals("") ? c_promo.getString(c_promo.getColumnIndex(PromosiBarangProvider.KEY_BARCODE)) : "");
                qty              = (c_promo.getString(c_promo.getColumnIndex(PromosiBarangProvider.KEY_QTY_BARANG)) != null) ? Double.parseDouble(c_promo.getString(c_promo.getColumnIndex(PromosiBarangProvider.KEY_QTY_BARANG))) : 0.0;
                discount_amount  = (c_promo.getString(c_promo.getColumnIndex(PromosiBarangProvider.KEY_DISCOUNT_AMOUNT)) != null) ? Double.parseDouble(c_promo.getString(c_promo.getColumnIndex(PromosiBarangProvider.KEY_DISCOUNT_AMOUNT))) : 0.0;
                discount_percent = (c_promo.getString(c_promo.getColumnIndex(PromosiBarangProvider.KEY_DISCOUNT_PERCENT)) != null) ? Double.parseDouble(c_promo.getString(c_promo.getColumnIndex(PromosiBarangProvider.KEY_DISCOUNT_PERCENT))) : 0.0;
                listPromo.add(barcode);
                totItem++;

                Date dateNow = new Date();
                for(int i = 0; i < ringkasanOrderModels.size(); i++){
                    if(barcode.equals(ringkasanOrderModels.get(i).getKodeBarcode())){
                        if(dateNow.after(Utils.getDateRange(from_, -1)) && dateNow.before(Utils.getDateRange(to_, 1))){
                            qtyBarang = ringkasanOrderModels.get(i).getQty() / qty;
                            qtyPromo  = (int) qtyBarang;
                            if(qtyPromo >= 1){

                                listRingkasan.add(ringkasanOrderModels.get(i).getKodeBarcode());
                                if(jenis_promo.equals("DISCOUNT")){

                                    if(discount_amount != 0.0){totDiscAmount = discount_amount * qtyPromo;}
                                    if(discount_percent != 0.0){totDiscPercent = ((ringkasanOrderModels.get(i).getHargaJual()) * (qty * qtyPromo)) * (discount_percent/100);}
                                    totalDiscount = (totDiscAmount + totDiscPercent);

                                }else if(jenis_promo.equals("BUY X GET Y")){

                                    listPromoBarangQtyList.add(new ListPromoBarangQty(kode_promo, barcode, qtyPromo));
                                    if(listPromoBarangQtyList.size() > 0){
                                        for(int j = 0; j < listPromoBarangQtyList.size(); j++){
                                            if(listPromoBarangQtyList.get(j).getBarcodeBarang().equals(barcode)){
                                                listPromoBarangQtyList.set(j, new ListPromoBarangQty(kode_promo, barcode, qtyPromo));
                                            }
                                        }
                                    }

                                    if(totCount == totItem){
                                        for(int k = 0; k < listPromoBarangQtyList.size(); k++){
                                            if(k > 0){
                                                if(listPromoBarangQtyList.get(k).getQtyBarang() < min){
                                                    min = listPromoBarangQtyList.get(k).getQtyBarang();
                                                }
                                            }

                                            if(k == 0){
                                                min = listPromoBarangQtyList.get(k).getQtyBarang();
                                            }
                                        }
                                    }

                                }

                                p = i;
                                totalDiskonPromo = totalDiskonPromo + totalDiscount;

                            }
                        }
                    }
                }

            }while(c_promo.moveToNext());
            c_promo.close();

        }



        boolean result = Arrays.equals(listPromo.toArray(), listRingkasan.toArray());
        if(result == true && (listPromo.size() == listRingkasan.size()) && listPromo.size() != 0){

            if(jenis_promo.toString().equals("DISCOUNT")){

                ringkasanOrderModels.set(p, new RingkasanOrderModel(
                        ringkasanOrderModels.get(p).getItemId(),
                        ringkasanOrderModels.get(p).getKodeBarcode(),
                        ringkasanOrderModels.get(p).getKodeBarang(),
                        ringkasanOrderModels.get(p).getNamaBarang(),
                        ringkasanOrderModels.get(p).getSatuanBarang(),
                        ringkasanOrderModels.get(p).getHargaJual(),
                        ringkasanOrderModels.get(p).getQty(),
                        totalDiskonPromo,
                        ringkasanOrderModels.get(p).getTax(),
                        "set discount",
                        ringkasanOrderModels.get(p).getStandart_cost(),
                        ringkasanOrderModels.get(p).isFlag_qty(),
                        ringkasanOrderModels.get(p).isFlag_bom(),
                        ringkasanOrderModels.get(p).getTaxGroup(),
                        ringkasanOrderModels.get(p).getNote(),
                        ringkasanOrderModels.get(p).getDetailId()
                ));

            }else if(jenis_promo.toString().equals("BUY X GET Y")){

                int totQtyAqum = 0;
                for(int m = 0; m < listPromoBarangQtyList.size(); m++){
                    if(listPromoBarangQtyList.get(m).getKodePromo().equals(kode_promo)){
                        if(listPromoBarangQtyList.get(m).getQtyBarang() >= min){
                            totQtyAqum = totQtyAqum + min;
                        }
                    }
                }

                if(listCekPromos.size() > 0){
                    idxArr = listIndexPromo.indexOf(kode_promo);
                    if(idxArr != -1){
                        qtyListPromo = listCekPromos.get(idxArr).getQtyBarang();
                    }else{
                        qtyListPromo = 0;
                    }
                }else{
                    qtyListPromo = 0;
                }


                if(totQtyAqum%totCount==0 && totQtyAqum > qtyListPromo){

                    if(listCekPromos.size() > 0){
                        int k = 0;
                        for(int i = 0; i < listCekPromos.size(); i++){
                            if(listCekPromos.get(i).getKodePromo().equals(kode_promo)){
                                idxArr = listIndexPromo.indexOf(kode_promo);
                                listCekPromos.set(idxArr, new ListCekPromo(kode_promo, totQtyAqum));
                                k = 1;
                                break;
                            }
                        }

                        if(k == 0){
                            listCekPromos.add(new ListCekPromo(kode_promo, totQtyAqum));
                            listIndexPromo.add(kode_promo);
                        }

                    }else{
                        listCekPromos.add(new ListCekPromo(kode_promo, totQtyAqum));
                        listIndexPromo.add(kode_promo);
                    }


                    String URL_HADIAH = "content://com.trimitrasis.finosapps.Views.ContentProvider.PromosiHadiahProvider/promosihadiah";
                    Uri uri_hadiah = Uri.parse(URL_HADIAH);
                    Cursor c_hadiah = getActivity().getContentResolver().query(uri_hadiah, null, "kode_promo_hadiah " + " = '" + kode_promo + "'", null, "_id ASC");

                    if(c_hadiah.moveToFirst()){

                        do{
                            qty_hadiah     = (c_hadiah.getString(c_hadiah.getColumnIndex(PromosiHadiahProvider.KEY_QTY_HADIAH)) != null) ? Double.parseDouble(c_hadiah.getString(c_hadiah.getColumnIndex(PromosiHadiahProvider.KEY_QTY_HADIAH))) : 0;
                            barcode_hadiah = (c_hadiah.getString(c_hadiah.getColumnIndex(PromosiHadiahProvider.KEY_BARCODE)) != null) ? c_hadiah.getString(c_hadiah.getColumnIndex(PromosiHadiahProvider.KEY_BARCODE)) : "";

                            String URLITEM = "content://com.trimitrasis.finosapps.ContentProvider.ItemProvider/item";
                            Uri uri_item   = Uri.parse(URLITEM);
                            Cursor citem   = getActivity().getContentResolver().query(uri_item, null, "barcode " + " = '" + barcode_hadiah + "'", null, "_id");

                            if(citem.moveToFirst()){
                                do{
                                    amount_hadiah       = citem.getDouble(citem.getColumnIndex(ItemProvider.KEY_HARGA_JUAL));
                                    String itemId       = citem.getString(citem.getColumnIndex(ItemProvider.KEY_KODE_ITEM));
                                    String kodeBarang   = citem.getString(citem.getColumnIndex(ItemProvider.KEY_STOCK_ID));
                                    String namaBarang   = citem.getString(citem.getColumnIndex(ItemProvider.KEY_DESCRIPTION));
                                    String satuanBarang = citem.getString(citem.getColumnIndex(ItemProvider.KEY_UOM_NAME));
                                    double standartCost = citem.getDouble(citem.getColumnIndex(ItemProvider.KEY_STANDART_COST));
                                    int flakQty         = citem.getInt(citem.getColumnIndex(ItemProvider.KEY_FLAG_QTY));
                                    int flakBom         = citem.getInt(citem.getColumnIndex(ItemProvider.KEY_FLAG_BOM));
                                    String taxGroup     = citem.getString(citem.getColumnIndex(ItemProvider.KEY_TAX_GROUP));
                                    String detilId      = citem.getString(citem.getColumnIndex(ItemProvider.KEY_DETAIL_ID));
                                    boolean flag_bom    = (flakBom == 1) ? true : false;
                                    boolean flag_quan   = (flakQty == 1) ? true : false;
                                    totalDiscount       = ((amount_hadiah) * (qty_hadiah * (totQtyAqum/totCount)));

                                    int n = 0;
                                    for(int k = 0; k < ringkasanOrderModels.size(); k++){
                                        if(barcode_hadiah.equals(ringkasanOrderModels.get(k).getKodeBarcode())){

                                            if(ringkasanOrderModels.get(k).getQty() > 0){
                                                qtyHadiahBef = ringkasanOrderModels.get(k).getQty() + 1.0;
                                            }else{
                                                qtyHadiahBef = ringkasanOrderModels.get(k).getQty();
                                            }

                                            ringkasanOrderModels.set(k, new RingkasanOrderModel(
                                                    ringkasanOrderModels.get(k).getItemId(),
                                                    ringkasanOrderModels.get(k).getKodeBarcode(),
                                                    ringkasanOrderModels.get(k).getKodeBarang(),
                                                    ringkasanOrderModels.get(k).getNamaBarang(),
                                                    ringkasanOrderModels.get(k).getSatuanBarang(),
                                                    ringkasanOrderModels.get(k).getHargaJual(),
                                                    qtyHadiahBef,
                                                    totalDiscount,
                                                    ringkasanOrderModels.get(k).getTax(),
                                                    "buy x get y",
                                                    ringkasanOrderModels.get(k).getStandart_cost(),
                                                    ringkasanOrderModels.get(k).isFlag_qty(),
                                                    ringkasanOrderModels.get(k).isFlag_bom(),
                                                    ringkasanOrderModels.get(k).getTaxGroup(),
                                                    ringkasanOrderModels.get(k).getNote(),
                                                    ringkasanOrderModels.get(k).getDetailId()
                                            ));

                                            n = 1;
                                            break;
                                        }
                                    }


                                    if(n == 0){
                                        System.out.println("add barang");
                                        ringkasanOrderModels.add(new RingkasanOrderModel(
                                                itemId,
                                                barcode_hadiah,
                                                kodeBarang,
                                                namaBarang,
                                                satuanBarang,
                                                amount_hadiah,
                                                qty_hadiah * (totQtyAqum/totCount),
                                                totalDiscount, 0,
                                                "buy x get y",
                                                standartCost,
                                                flag_quan,
                                                flag_bom,
                                                taxGroup,
                                                "",
                                                detilId));
                                    }


                                }while(citem.moveToNext());
                                citem.close();
                            }


                        } while (c_hadiah.moveToNext());
                        c_hadiah.close();
                    }
                }


            }

        }else if(result == false && (listPromo.size() != listRingkasan.size()) || listRingkasan.size() == 0 || listRingkasan == null){

            if(jenis_promo.toString().equals("BUY X GET Y")){

                String URL_HADIAH = "content://com.trimitrasis.finosapps.Views.ContentProvider.PromosiHadiahProvider/promosihadiah";
                Uri uri_had  = Uri.parse(URL_HADIAH);
                Cursor c_had = getActivity().getContentResolver().query(uri_had, null, "kode_promo_hadiah " + " = '" +  kode_promo + "'", null, "_id ASC");

                if(c_had.moveToFirst()){
                    do{
                        barcode_hadiah = (c_had.getString(c_had.getColumnIndex(PromosiHadiahProvider.KEY_BARCODE)) != null) ? c_had.getString(c_had.getColumnIndex(PromosiHadiahProvider.KEY_BARCODE)) : "";
                        for(int i = 0; i < ringkasanOrderModels.size(); i++){
                            if(ringkasanOrderModels.get(i).getKodeBarcode().equals(barcode_hadiah)){
                                if(ringkasanOrderModels.get(i).getInfo().equals("buy x get y")){
                                    if(ringkasanOrderModels.get(i).getQty() > 1.0){
                                        double qtyMin = ringkasanOrderModels.get(i).getQty() - 1.0;
                                        System.out.println("remove update > 1");
                                        ringkasanOrderModels.set(i, new RingkasanOrderModel(
                                                ringkasanOrderModels.get(i).getItemId(),
                                                ringkasanOrderModels.get(i).getKodeBarcode(),
                                                ringkasanOrderModels.get(i).getKodeBarang(),
                                                ringkasanOrderModels.get(i).getNamaBarang(),
                                                ringkasanOrderModels.get(i).getSatuanBarang(),
                                                ringkasanOrderModels.get(i).getHargaJual(),
                                                qtyMin,
                                                0,
                                                ringkasanOrderModels.get(i).getTax(),
                                                "",
                                                ringkasanOrderModels.get(i).getStandart_cost(),
                                                ringkasanOrderModels.get(i).isFlag_qty(),
                                                ringkasanOrderModels.get(i).isFlag_bom(),
                                                ringkasanOrderModels.get(i).getTaxGroup(),
                                                ringkasanOrderModels.get(i).getNote(),
                                                ringkasanOrderModels.get(i).getDetailId()
                                        ));

                                    }else{
                                        System.out.println("remove , qty == 1");
                                        ringkasanOrderModels.remove(i);
                                    }

                                    if(listCekPromos.size() > 0){
                                        idxArr = listIndexPromo.indexOf(kode_promo);
                                        if(idxArr != -1) {
                                            listIndexPromo.remove(idxArr);
                                            listCekPromos.remove(idxArr);
                                        }
                                    }

                                }
                            }
                        }
                    }while(c_had.moveToNext());
                    c_had.close();

                }
            }
        }

    }



    private void cekDataPromoHadiah(RingkasanOrderModel ringkasanOrderModel, int index, ArrayList<RingkasanOrderModel> arrRingkasanOrderModels){

        String kode_promo_hadiah = "";
        String URL_HADIAH = "content://com.trimitrasis.finosapps.Views.ContentProvider.PromosiHadiahProvider/promosihadiah";
        Uri uri_had  = Uri.parse(URL_HADIAH);
        Cursor c_had = getActivity().getContentResolver().query(uri_had, null, "barcode_hadiah " + " = '" +  ringkasanOrderModel.getKodeBarcode() + "'", null, "_id ASC");

        int c = 0;
        if(c_had.moveToFirst()){
            do{
                kode_promo_hadiah = (c_had.getString(c_had.getColumnIndex(PromosiHadiahProvider.KEY_KODE_PROMO)) != null) ? c_had.getString(c_had.getColumnIndex(PromosiHadiahProvider.KEY_KODE_PROMO)) : "";
                String URL = "content://com.trimitrasis.finosapps.Views.ContentProvider.PromoJualProvider/promojual";
                Uri uri_  = Uri.parse(URL);
                Cursor c_promo = getActivity().getContentResolver().query(uri_, null, "kode_promo_barang " + " = '" +  kode_promo_hadiah + "'", null, "_id ASC");

                if(c_promo.moveToFirst()){
                    do{
                        String barcode = (!c_promo.getString(c_promo.getColumnIndex(PromosiBarangProvider.KEY_BARCODE)).equals("") ? c_promo.getString(c_promo.getColumnIndex(PromosiBarangProvider.KEY_BARCODE)) : "");
                        for(int i = 0; i < arrRingkasanOrderModels.size(); i++){
                            if(barcode.equals(arrRingkasanOrderModels.get(i).getKodeBarcode())){
                                c = 1;
                            }
                        }

                    }while(c_promo.moveToNext());
                    c_promo.close();
                }

            }while(c_had.moveToNext());
            c_had.close();
        }


        if(c == 0){
            if(ringkasanOrderModel.getInfo().equals("buy x get y")){
                if(ringkasanOrderModel.getQty() > 1){
                    System.out.println("remove lebih dari 1");
                    double qtyOrder = ringkasanOrderModels.get(index).getQty() - 1.0;
                    arrRingkasanOrderModels.set(index, new RingkasanOrderModel(
                            ringkasanOrderModels.get(index).getItemId(),
                            ringkasanOrderModels.get(index).getKodeBarcode(),
                            ringkasanOrderModels.get(index).getKodeBarang(),
                            ringkasanOrderModels.get(index).getNamaBarang(),
                            ringkasanOrderModels.get(index).getSatuanBarang(),
                            ringkasanOrderModels.get(index).getHargaJual(),
                            qtyOrder,
                            0,
                            ringkasanOrderModels.get(index).getTax(),
                            "",
                            ringkasanOrderModels.get(index).getStandart_cost(),
                            ringkasanOrderModels.get(index).isFlag_qty(),
                            ringkasanOrderModels.get(index).isFlag_bom(),
                            ringkasanOrderModels.get(index).getTaxGroup(),
                            ringkasanOrderModels.get(index).getNote(),
                            ringkasanOrderModels.get(index).getDetailId()
                    ));
                }else{
                    System.out.println("remove sama dengan 1");
                    ringkasanOrderModels.remove(index);
                }


                if(listCekPromos.size() > 0){
                    idxArr = listIndexPromo.indexOf(kode_promo_hadiah);
                    if(idxArr != -1) {
                        listIndexPromo.remove(idxArr);
                        listCekPromos.remove(idxArr);
                    }
                }

            }
        }
    }



    private void cekLastDataPromo(String kodePromo,  ArrayList<RingkasanOrderModel> arrRingkasanOrderModels){

        String URL = "content://com.trimitrasis.finosapps.Views.ContentProvider.PromoJualProvider/promojual";
        Uri uri_  = Uri.parse(URL);
        Cursor c_promo = getActivity().getContentResolver().query(uri_, null, "kode_promo_barang " + " = '" +  kodePromo + "'", null, "_id ASC");

        int countBarang = 0; int countPromoBarang = 0;
        if(c_promo.moveToFirst()){
            do{
                countPromoBarang++;
                String barcode = (!c_promo.getString(c_promo.getColumnIndex(PromosiBarangProvider.KEY_BARCODE)).equals("") ? c_promo.getString(c_promo.getColumnIndex(PromosiBarangProvider.KEY_BARCODE)) : "");
                for(int i = 0; i < arrRingkasanOrderModels.size(); i++){
                    if(barcode.equals(arrRingkasanOrderModels.get(i).getKodeBarcode())){
                        countBarang++;
                    }
                }

            }while(c_promo.moveToNext());
            c_promo.close();
        }

        if(countBarang != countPromoBarang){
            if(listCekPromos.size() > 0){
                idxArr = listIndexPromo.indexOf(kodePromo);
                if(idxArr != -1){
                    listIndexPromo.remove(idxArr);
                    listCekPromos.remove(idxArr);
                }
            }
        }



    }




    public interface CallbackCheckListPesanan{
        void checkListPesanan();
    }

    private void checkDataListPesanan(){
        checkListPesanan.checkListPesanan();
    }


}
