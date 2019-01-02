package com.trimitrasis.finosapps.Views.UtilView;
import com.trimitrasis.finosapps.Connection.Models.CompanyModel;
import com.trimitrasis.finosapps.Connection.Models.UserInfoModel;
import com.trimitrasis.finosapps.Views.PosView.Model.EndOfShiftModel;

/**
 * Created by rahman on 01/03/2017.
 */

public class Constants{

    public static final String VERSION = "v0.9.1201";
    public static final String API_KEY = "56f512c70e4addf3043ad47b310dd698e41aa139";

    //public static final String URL_BASE = "http://10.0.14.152";
    //public static final String URL_BASE_2 = "http://10.0.14.152";
    //public static final String URL_BASE = "http://192.168.43.159";
    //public static final String URL_BASE_2 = "http://192.168.43.159";

    //staging
    public static final String URL_BASE = "http://192.168.0.200:1001";
    public static final String URL_BASE_2 = "http://192.168.0.200:1001";

    //prod
    //public static final String URL_BASE = "http://app.finos.id";
    //public static final String URL_BASE_2 = "http://app.finos.id";


    public static String getUrlBase(){
        if (Utils.isUsingFirstBaseUrl)return URL_BASE;
        else return URL_BASE_2;
    }


    public static final int RESULT_TAKE_PICTURE = 50;
    public static final int CONTACT_PICKER_RESULT = 55;
    public static final int PICTURE_PICKER_RESULT = 51;
    public static final int PHOTO_RESULT = 30;
    public static final long SECOND = 1000;
    public static final long MINUTE = SECOND*60;
    public static final long HOUR = MINUTE*60;
    public static final long DAY = HOUR*24;
    public static final long MONTH = DAY*30;
    public static final long YEAR = 365*DAY;
    public static UserModel userModel;
    public static CompanyModel companyModel;
    public static UserInfoModel userInfoModel;
    public static int flakAddVendor = 0;
    public static int flakAddCustomer = 0;
    public static EndOfShiftModel endOfShiftModel = new EndOfShiftModel();
    public static int flak_bayar = 0;
    public static int flak_bayar_bill = 0;
    public static int flak_hold = 0;

    public static String printer_address = "";
    public static String printer_address_struk = "";
    public static String printer_address_dapur = "";
    public static String printer_name_struk = "";
    public static String printer_name_dapur = "";

    //QSPrinter
    //00:0D:18:10:15:19

    public static final int START_OF_DAY_MENU = 0;
    public static final int START_OF_SHIFT_MENU = 1;
    public static final int END_OF_DAY_MENU = 7;
    public static final int END_OF_SHIFT_MENU = 5;
    public static final int SYNC_TRANS_MENU = 6;
    public static final int HOLD_SALES_MENU = 2;
    public static final int PENGATURAN_MENU = 8;
    public static final int POS_MENU = 4;
    public static final int SINKRON_MENU = 3;

}
