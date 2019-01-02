package com.trimitrasis.finosapps.Connection;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.trimitrasis.finosapps.Connection.Models.ResponsFinos;
import com.trimitrasis.finosapps.Connection.Models.UserInfoModel;
import com.trimitrasis.finosapps.Views.LoginView.Models.LoginModel;
import com.trimitrasis.finosapps.Views.MasterDataView.Models.CustomerModel;
import com.trimitrasis.finosapps.Views.MasterDataView.Models.VendorModel;
import com.trimitrasis.finosapps.Views.PosView.Model.EndOfShiftModelPost;
import com.trimitrasis.finosapps.Views.PosView.Model.MemberPointModel;
import com.trimitrasis.finosapps.Views.PosView.Model.StartOfDayModel;
import com.trimitrasis.finosapps.Views.PosView.Model.StartOfShiftModel;
import com.trimitrasis.finosapps.Views.PosView.Model.SyncTransModel;
import com.trimitrasis.finosapps.Views.PosView.Model.TransaksiPenjualanModel;
import com.trimitrasis.finosapps.Views.SalesQuotationView.Models.FormModel;
import com.trimitrasis.finosapps.Views.SalesQuotationView.Models.SalesQuotModel;
import com.trimitrasis.finosapps.Views.UtilView.Constants;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import okhttp3.ResponseBody;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.android.MainThreadExecutor;
import retrofit.client.OkClient;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;

/**
 * Created by rahman on 01/03/2017.
 */


public class ApiConnection{

    private static RestAdapter restAdapter;
    private static ExecutorService mExecutorService;
    private static GsonBuilder gb;

    public static RestAdapter getRestAdapter(){
        return getRestAdapter(Constants.getUrlBase());
    }

    public static RestAdapter getRestAdapter(String urlBase){
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setReadTimeout(3, TimeUnit.MINUTES);

        gb = new GsonBuilder();
        mExecutorService = Executors.newCachedThreadPool();
        restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(urlBase)
                .setClient(new OkClient(okHttpClient))
                .setExecutors(mExecutorService, new MainThreadExecutor())
                .setConverter(new GsonConverter(gb.create()))
                .build();

        return restAdapter;
    }

    //login
    public interface LoginDeviceInterface {
        @POST("/Api/loginApps")
        void loginDevice(@Body LoginModel rawText, Callback<Response> callback);
    }

    public static LoginDeviceInterface getLoginDeviceInterface(){
        return getRestAdapter().create(LoginDeviceInterface.class);
    }

    //view customer
    public interface ViewDataCustomerInterface{
        @POST("/Api/getDataCustomer")
        void getViewDataCustomer(@Body UserInfoModel userInfoModel, Callback<Response> callback);
    }

    public static ViewDataCustomerInterface getViewDataCustomerInterface(){
        return getRestAdapter().create(ViewDataCustomerInterface.class);
    }

    //add customer
    public interface CustomerInterface{
        @POST("/Api/addDataCustomer")
        void getCustomer(@Body CustomerModel rawText, Callback<Response> callback);
    }

    public static CustomerInterface getCustomerInterface(){
        return getRestAdapter().create(CustomerInterface.class);
    }

    //edit customer
    public interface EditCustomerInterface{
        @POST("/Api/editDataCustomer")
        void editCustomerInterface(@Body CustomerModel customerModel, Callback<Response> callback);
    }

    public static EditCustomerInterface getEditCustomerInterface(){
        return getRestAdapter().create(EditCustomerInterface.class);
    }

    //delete customer
    public interface DeleteCustomerInterface{
        @POST("/Api/deleteDataCustomer")
        void deleteCustomerInterface(@Body CustomerModel customerModel, Callback<Response> callback);
    }

    public static DeleteCustomerInterface getDeleteCustomerInterface(){
        return getRestAdapter().create(DeleteCustomerInterface.class);
    }

    //add vendor
    public interface VendorInterface{
        @POST("/Api/addDataVendor")
        void getVendor(@Body VendorModel rawText, Callback<Response> callback);
    }

    public static VendorInterface getVendorInterface(){
        return getRestAdapter().create(VendorInterface.class);
    }

    //edit vendor
    public interface EditVendorInterface{
        @POST("/Api/editDataVendor")
        void editVendorInterface(@Body VendorModel rawText, Callback<Response> callback);
    }

    public static EditVendorInterface getEditVendorInterface(){
        return getRestAdapter().create(EditVendorInterface.class);
    }

    //delete vendor
    public interface DeleteVendorInterface{
        @POST("/Api/deleteDataVendor")
        void deleteVendorInterface(@Body VendorModel rawText, Callback<Response> callback);
    }

    public static DeleteVendorInterface getDeleteVendorInterface(){
        return getRestAdapter().create(DeleteVendorInterface.class);
    }

    //view vendor
    public interface ViewDataVendorInterface{
        @POST("/Api/getDataVendor")
        void getViewDataVendor(@Body UserInfoModel userInfoModel, Callback<Response> callback);
    }

    public static ViewDataVendorInterface getViewDataVendorInterface(){
        return getRestAdapter().create(ViewDataVendorInterface.class);
    }

    //get menu
    public interface MenuInterface{
        @POST("/Api/getForm")
        void getFieldForm(@Body FormModel formModel, Callback<Response> callback);
    }

    public static MenuInterface getMenuInterface(){
        return getRestAdapter().create(MenuInterface.class);
    }


    //get menu mobile
    public interface MenuMobileInterface{
        @POST("/Api/getMenuMobile")
        void getMenuMobile(Callback<Response> callback);
    }

    public static MenuMobileInterface getMenuMobileInterface(){
        return getRestAdapter().create(MenuMobileInterface.class);
    }


    //get item hierarcy
    public interface DataItemHierarchyInterface{
        @POST("/Api/getDataItemHierarchy")
        void getItemHierarchy(@Body UserInfoModel userInfoModel, Callback<Response> callback);
    }

    public static DataItemHierarchyInterface getDataItemHierarchyInterface(){
        return getRestAdapter().create(DataItemHierarchyInterface.class);
    }


    //currency
    public interface CurrencyInterface{
        @POST("/Api/addDataCurrency")
        void getDataCurrency(@Body UserInfoModel userInfoModel, Callback<ResponsFinos> callback);
    }

    public static CurrencyInterface getCurrencyInterface(){
        return getRestAdapter().create(CurrencyInterface.class);
    }
    //end

    //view currency
    public interface ViewDataCurrencyInterface{
        @POST("/Api/getDataCurrency")
        void getViewDataCurrency(@Body UserInfoModel userInfoModel, Callback<Response> callback);
    }

    public static ViewDataCurrencyInterface getViewDataCurrencyInterface(){
        return getRestAdapter().create(ViewDataCurrencyInterface.class);
    }
    //end

    //view master item
    public interface ViewDataItemInterface{
        @POST("/Api/_apiGetDataItem")
        void getAllDataProduct(@Body UserInfoModel userInfoModel, Callback<Response> callback);
    }

    public static ViewDataItemInterface getViewDataItemInterface(){
        return getRestAdapter().create(ViewDataItemInterface.class);
    }
    //end


    //view master bom
    public interface ViewDataBomInterface{
        @POST("/Api/_apiGetDataBom")
        void getAllDataBom(@Body UserInfoModel userInfoModel, Callback<Response> callback);
    }

    public static ViewDataBomInterface getViewDataBomInterface(){
        return getRestAdapter().create(ViewDataBomInterface.class);
    }
    //end



    //get data tax group
    public interface TaxGroupInterface{
        @POST("/Api/getDataTaxGroup")
        void getDataTaxGroup(@Body UserInfoModel userInfoModel, Callback<Response> callback);
    }

    public static TaxGroupInterface getTaxGroupInterface(){
        return getRestAdapter().create(TaxGroupInterface.class);
    }
    //end


    //view promosi
    public interface ViewDataPromoInterface{
        @POST("/Api/_apiGetDataPromo")
        void getViewDataPromo(@Body UserInfoModel userInfoModel, Callback<Response> callback);
    }

    public static ViewDataPromoInterface getViewDataPromoInterface(){
        return getRestAdapter().create(ViewDataPromoInterface.class);
    }
    //end

    //view promosi barang
    public interface ViewDataPromoBarangInterface{
        @POST("/Api/_apiGetDataPromoBarang")
        void getViewDataPromoBarang(@Body  UserInfoModel userInfoModel, Callback<Response> callback);
    }

    public static ViewDataPromoBarangInterface getViewDataPromoBarangInterface(){
        return getRestAdapter().create(ViewDataPromoBarangInterface.class);
    }


    //view promosi hadiah
    public interface ViewDataPromoHadiahInterface{
        @POST("/Api/_apiGetDataPromoHadiah")
        void getViewDataPromoHadiah(@Body  UserInfoModel userInfoModel, Callback<Response> callback);
    }

    public static ViewDataPromoHadiahInterface getViewDataPromoHadiahInterface(){
        return getRestAdapter().create(ViewDataPromoHadiahInterface.class);
    }



    //start of day
    public interface StartOfDayInterface{
        @POST("/Api/_apiStartOfDay")
        void startOfDay(@Body StartOfDayModel startOfDayModel, Callback<Response> callback);
    }


    public static StartOfDayInterface getStartOfDayInterface(){
        return getRestAdapter().create(StartOfDayInterface.class);
    }
    //end


    //getData member point
    public interface MemberPointInterface{
        @POST("/Api/apiGetDataMember")
        void getMemberPoint(@Body MemberPointModel memberPointModel, Callback<Response> callback);
    }


    public static MemberPointInterface getMemberPointInterface(){
        return getRestAdapter().create(MemberPointInterface.class);
    }
    //end


    //getData member point
    public interface InsertMemberPointInterface{
        @POST("/Api/insertDataMemberPoint")
        void insertDataMemberPoint(@Body MemberPointModel memberPointModel, Callback<Response> callback);
    }


    public static InsertMemberPointInterface getInsertMemberPointInterface(){
        return getRestAdapter().create(InsertMemberPointInterface.class);
    }
    //end


    //update member point
    public interface UpdateMemberPointInterface{
        @POST("/Api/updateDataMemberPoint")
        void updateDataMemberPoint(@Body MemberPointModel memberPointModel, Callback<Response> callback);
    }


    public static UpdateMemberPointInterface getUpdateMemberPointInterface(){
        return getRestAdapter().create(UpdateMemberPointInterface.class);
    }
    //end


    //check start of day
    public interface CheckStartOfDayInterface{
        @POST("/Api/getCheckStartOfDay")
        void getCheckStartOfDay(@Body StartOfShiftModel startOfShiftModel, Callback<Response> callback);
    }

    public static CheckStartOfDayInterface getCheckStartOfDayInterface(){
        return getRestAdapter().create(CheckStartOfDayInterface.class);
    }
    //end


    //check start of shift
    public interface CheckSosInterface{
        @POST("/Api/getCheckStartOfShift_")
        void getCheckSos(@Body UserInfoModel userInfoModel, Callback<Response> callback);
    }

    public static CheckSosInterface getCheckSosHoldInterface(){
        return getRestAdapter().create(CheckSosInterface.class);
    }
    //end

    //start of shift
    public interface StartOfShiftInterface{
        @POST("/Api/getStartOfShift")
        void getStartOfShift(@Body StartOfShiftModel startOfShiftModel, Callback<Response> callback);
    }

    public static StartOfShiftInterface getStartOfShiftInterface(){
        return getRestAdapter().create(StartOfShiftInterface.class);
    }
    //end

    //end of shift
    public interface EndOfShiftInterface{
        @POST("/Api/getEndOfShift")
        void getEndOfShift(@Body StartOfShiftModel startOfShiftModel, Callback<Response> callback);
    }

    public static EndOfShiftInterface getEndOfShiftInterface(){
        return getRestAdapter().create(EndOfShiftInterface.class);
    }
    //end


    public interface EndOfShiftPosInterface{
        @POST("/Api/getEndOfShiftPost")
        void getEndOfShiftPos(@Body EndOfShiftModelPost endOfShiftModelPost, Callback<Response> callback);
    }

    public static EndOfShiftPosInterface getEndOfShiftPosInterface(){
        return getRestAdapter().create(EndOfShiftPosInterface.class);
    }

    //sync trans
    public interface SyncTransInterface{
        @POST("/Api/syncTransaksiPenjualan")
        void getSyncTrans(@Body SyncTransModel syncTransModel, Callback<Response> callback);
    }

    public static SyncTransInterface getSyncTransInterface(){
        return getRestAdapter().create(SyncTransInterface.class);
    }

    //end of day
    public interface EndOfDayInterface{
        @POST("/Api/getEndOfDay")
        void getEndOfDay(@Body UserInfoModel userInfoModel, Callback<Response> callback);
    }

    public static EndOfDayInterface getEndOfDayInterface(){
        return getRestAdapter().create(EndOfDayInterface.class);
    }
    //end

    //summary end of shift
    public interface SummaryEoSInterface{
        @POST("/Api/getsummaryEndOfShift/")
        void getSummaryEoS(@Body ArrayList<TransaksiPenjualanModel> transaksiPenjualanModels, Callback<Response> callback);
    }

    public static SummaryEoSInterface getSummaryEoSInterface(){
        return getRestAdapter().create(SummaryEoSInterface.class);
    }
    //end

    //get data customer
    public interface CustomerSqInterface{
        @POST("/Api/getDataCustomerSalesQuotation")
        void getCustomerSalesQuotation(@Body UserInfoModel userInfoModel, Callback<Response> callback);
    }

    public static CustomerSqInterface getCustomerSqSInterface(){
        return getRestAdapter().create(CustomerSqInterface.class);
    }
    //end


    //get data tax group
    public interface TaxGroupPosInterface{
        @POST("/Api/getDataTaxGroup")
        void getDataTaxGroupPos(@Body UserInfoModel userInfoModel, Callback<Response> callback);
    }

    public static TaxGroupPosInterface getTaxGroupPosInterface(){
        return getRestAdapter().create(TaxGroupPosInterface.class);
    }
    //end


    //get data tax group pos
    public interface TaxGroupPosNewInterface{
        @POST("/Api/_apiGetDataTaxGroup")
        void getDataTaxGroupPosNew(@Body UserInfoModel userInfoModel, Callback<Response> callback);
    }

    public static TaxGroupPosNewInterface getTaxGroupPosNewInterface(){
        return getRestAdapter().create(TaxGroupPosNewInterface.class);
    }
    //end


    //get data tax
    public interface TaxPosInterface{
        @POST("/Api/_apiGetDataTax")
        void getDataTaxPos(@Body UserInfoModel userInfoModel, Callback<Response> callback);
    }

    public static TaxPosInterface getTaxPosInterface(){
        return getRestAdapter().create(TaxPosInterface.class);
    }


    //get data tax
    public interface TaxInterface{
        @POST("/Api/getDataTax")
        void getDataTax(@Body UserInfoModel userInfoModel, Callback<Response> callback);
    }

    public static TaxInterface getTaxInterface(){
        return getRestAdapter().create(TaxInterface.class);
    }
    //end

    //get data item sales quotation
    public interface ItemSalesQuotationInterface{
        @POST("/Api/getDataItemSalesQuot")
        void getDataItemSalesQuot(@Body UserInfoModel userInfoModel, Callback<Response> callback);
    }

    public static ItemSalesQuotationInterface getItemSalesQuotationInterface(){
        return getRestAdapter().create(ItemSalesQuotationInterface.class);
    }
    //end


    //get data paymentterms
    public interface PaymentTermInterface{
        @POST("/Api/getDataPaymentTerms")
        void getDataPaymentTerms(@Body UserInfoModel userInfoModel, Callback<Response> callback);
    }

    public static PaymentTermInterface getPaymentTermInterface(){
        return getRestAdapter().create(PaymentTermInterface.class);
    }


    //get data currency
    public interface GetAllCurrencyInterface{
        @POST("/Api/getDataCurrencyCust")
        void getAllCurrency(@Body UserInfoModel userInfoModel, Callback<Response> callback);
    }

    public static GetAllCurrencyInterface getAllCurrencyInterface(){
        return getRestAdapter().create(GetAllCurrencyInterface.class);
    }


    //insert sales quotation
    public interface InsertSalesQuotationInterface{
        @POST("/Api/addDataSalesQuotation")
        void insertSalesQuotation(@Body SalesQuotModel salesQuotModel, Callback<Response> callback);
    }

    public static InsertSalesQuotationInterface geInsertSalesQuotationInterface(){
        return getRestAdapter().create(InsertSalesQuotationInterface.class);
    }

    //get data sales quotation
    public interface GetDataSalesQuotInterface{
        @POST("/Api/getDataSalesQuotation")
        void getSalesQuotation(@Body UserInfoModel userInfoModel, Callback<Response> callback);
    }

    public static GetDataSalesQuotInterface getDataSalesQuotationInterface(){
        return getRestAdapter().create(GetDataSalesQuotInterface.class);
    }



    //Cek Login
    public interface CekLoginInterface{
        @POST("/Api/cekLoginMobile")
        void cekUserLogin(@Body LoginModel rawText, Callback<Response> callback);
    }

    public static CekLoginInterface getCekLoginInterface(){
        return getRestAdapter().create(CekLoginInterface.class);
    }
    //end


    //update flak
    public interface GetProfileInterface{
        @POST("/Api/_apiGetProfile")
        void getProfile(@Body UserInfoModel rawText, Callback<Response> callback);
    }

    public static GetProfileInterface getDataProfileInterface(){
        return getRestAdapter().create(GetProfileInterface.class);
    }


    //get location
    public interface GetLocationInterface{
        @POST("/Api/_apiGetLocation")
        void getLocation(@Body UserInfoModel rawText, Callback<Response> callback);
    }

    public static GetLocationInterface GetDataLocationInterface(){
        return getRestAdapter().create(GetLocationInterface.class);
    }

    //
    public interface HistoryPenjualanInterface{
        @POST("/Api/_apiGetHistoryPenjulan")
        void getHistoryPenjualan(@Body UserInfoModel rawText, Callback<Response> callback);
    }

    public static HistoryPenjualanInterface GetHistoryPenjualanInterface(){
        return getRestAdapter().create(HistoryPenjualanInterface.class);
    }


    //download data
    public interface DownloadImgItemInterface{
        @GET("/uploads/finos_1.zip")
        void getImageItem(Callback<ResponseBody> callback);
    }

    public static DownloadImgItemInterface GetDownloadImgItemInterface(){
        return getRestAdapter().create(DownloadImgItemInterface.class);
    }


    public static String getRawResponse(Response response){

        BufferedReader br;
        StringBuilder sb = new StringBuilder();

        try {
            br = new BufferedReader(new InputStreamReader(response.getBody().in()));
            String line;
            while ((line = br.readLine()) != null){
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }



}
